package es.horus.temporizedMailing.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AvisoBE {
	

	private String asunto;
	private String destinatario;
	private String mensaje;
	private Long fechaCreacion;
	
	private ProgramacionBE[] programaciones;
	private List<ProgramacionBE> programacionesList;
	
	public AvisoBE() {
		programacionesList=new ArrayList<ProgramacionBE>();
	}
	
	public String getAsunto() {
		return asunto;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	public String getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public long getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(long fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public void setFechaCreacion(Long fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
	public void addProgramacion(ProgramacionBE p) {
		programacionesList.add(p);
		
		programaciones=programacionesList.toArray(new ProgramacionBE[programacionesList.size()]);
	}

	public ProgramacionBE[] getProgramaciones() {
		return programaciones;
	}

	public void setProgramaciones(ProgramacionBE[] programaciones) {
		this.programaciones = programaciones;
	}

	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	public boolean hayPendientes() {
		
		for(ProgramacionBE p:programacionesList) {
			if(p.getEnviado()==ProgramacionBE.ESTADO_PENDIENTE) {
				return true;
			}
		}
		
		return false;		
	}


	public String getFileFullName() {
		return getFileFullName(hayPendientes());
	}
	public String getFileFullName(boolean pendientes) {
		String ret=getFileName();
		if(pendientes) {
			ret+=".dat";
		}else {
			ret+=".hst";
		}
		return ret;
	}
	public String getFileName() {
		String ret="programacion";
		
		if(asunto!=null && destinatario!=null) {
			String tmp=(destinatario+asunto).replaceAll("[^a-zA-Z]", "");
			
			if(tmp.length()>0) {
				ret=tmp;
			}
		}
		
		
		return ret;
	}

	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	
	
}