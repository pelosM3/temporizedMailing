package es.horus.temporizedMailing.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import es.horus.temporizedMailing.beans.AvisoBE;
import es.horus.temporizedMailing.beans.ProgramacionBE;

public class AvisosDAO {

	private boolean existe=false;
	private static final String DIRBASE = System.getProperty("user.home")+"/avisos";
	
	private File dirBBDD=null;
	
	public AvisosDAO() {
		dirBBDD=new File(DIRBASE);
		if(dirBBDD.exists() ) {
			if(!dirBBDD.isDirectory()) {
				System.out.println("ERROR NO SE PUEDE CREAR EL DIRECTORIO PARA LA BBDD user.home/avisos");
			}else {
				//todo ok, la bbdd existe
				existe=true;
				verificaNombresFicheros();
				System.out.println("BBDD OK");
			}
		}else{
			if(dirBBDD.mkdirs()) {
				System.out.println("BBDD creada. vacia");
			}else {
				System.out.println("Error al crear bbdd");
			}
		}
	}
	private void verificaNombresFicheros() {//si alguien le cambia el nombre a un fichero se generarian duplicados continuamente, hay que renombrar todo al arrancar
		if(existe) {
			File[] l=dirBBDD.listFiles();
			
			for(File f:l) {
				String fileName=f.getAbsolutePath();
				if(fileName.endsWith(".dat")) {//los historicos se rescatan cambiandoles la extension, aqui se descartan solo se cargan los activos
					AvisoBE v=lee(f.getAbsolutePath());
					
					if(!fileName.endsWith(v.getFileFullName())){
						String nombreDestino=DIRBASE+"/"+v.getFileFullName();
						System.out.println("Reparamos nombre de fichero FICHERO de "+fileName+" a "+nombreDestino);
						f.renameTo(new File(nombreDestino));
					}
				}
			}
		}
	}
	
	public List<AvisoBE> cargaBBDD() {
		List<AvisoBE> ret=new ArrayList<AvisoBE>();
		if(existe) {
			File[] l=dirBBDD.listFiles();
			
			for(File f:l) {
				String fileName=f.getAbsolutePath();
				if(fileName.endsWith(".dat")) {//los historicos se rescatan cambiandoles la extension, aqui se descartan solo se cargan los activos
					AvisoBE v=lee(f.getAbsolutePath());
					if(v!=null) {
						ret.add(v);
					}
				}
			}
		}
		return ret;
	}
	
	public AvisoBE lee(String path) {
		AvisoBE ret=null;
		
		try {
			JAXBContext context = JAXBContext.newInstance(AvisoBE.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			//Deserealizamos a partir de un documento XML
			ret = (AvisoBE) unmarshaller.unmarshal(new File(path));
			
			/*System.out.println("********* Provincia cargado desde fichero XML***************");
			//Mostramos por linea de comandos el objeto Java obtenido 
			//producto de la deserialziacion
			marshaller.marshal(ret, System.out);
			*/
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return  ret;
	}
	
	public boolean guarda(AvisoBE p) {
		
		boolean ret=false;
		try {
			
			if(p.hayPendientes()==false) {
				//si va a pasar a historico, borro el fichero actual
				try {
					File f=new File((DIRBASE+"/"+p.getFileFullName(true)));
					if(f.exists()) {
						f.delete();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			JAXBContext context = JAXBContext.newInstance(AvisoBE.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			

			//Mostramos el documento XML generado por la salida estandar
			//marshaller.marshal(p, System.out);
			
			FileOutputStream fos = new FileOutputStream(DIRBASE+"/"+p.getFileFullName());
			//guardamos el objeto serializado en un documento XML
			marshaller.marshal(p, fos);
			fos.close();
			ret=true;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
 

 
}