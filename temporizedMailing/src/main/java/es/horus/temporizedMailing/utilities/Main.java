package es.horus.temporizedMailing.utilities;

import java.util.List;

import es.horus.temporizedMailing.beans.AvisoBE;
import es.horus.temporizedMailing.beans.ProgramacionBE;
import es.horus.temporizedMailing.dao.AvisosDAO;

public class Main {

	public static void main(String[] args) {
		
		
		try {
			AvisosDAO bd=new AvisosDAO();
			
			AvisoBE o = new AvisoBE();
			o.setDestinatario("pelos@gmail.com");
			o.setAsunto("Email de pueba de email programado");
			o.setMensaje("y aqui todo el cuerpo del mensaje.... con programaciones cambiado");
			o.setFechaCreacion(201811261236L);

			o.addProgramacion(new ProgramacionBE(20190131L));
			o.addProgramacion(new ProgramacionBE(20190831L));
			o.addProgramacion(new ProgramacionBE(20200131L));
			
			ProgramacionBE [] l=o.getProgramaciones();
			for(ProgramacionBE p:l) {
				p.setEnviado(ProgramacionBE.ESTADO_ENVIADO);
			}
					
			
			bd.guarda(o);
			
			//o=bd.lee();
			List<AvisoBE>lista=bd.cargaBBDD();
			
			for(AvisoBE a:lista) {
				System.out.println(a.getAsunto());
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
