package es.horus.temporizedMailing.beans;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class ProgramacionBE {

	public static final int ESTADO_ENVIADO=1;
	public static final int ESTADO_PENDIENTE=0;
	
	private Integer enviado;//1=enviado por email, 0=pendiente
	private Long fecha;//yyyyMMdd
	
	public ProgramacionBE(){
		
	}
	public ProgramacionBE(long fechaEnvio) {
		enviado=0;
		fecha=fechaEnvio;
	}
	
	public Integer getEnviado() {
		return enviado;
	}
	public void setEnviado(Integer enviado) {
		this.enviado = enviado;
	}
	public Long getFecha() {
		return fecha;
	}
	public void setFecha(Long fecha) {
		this.fecha = fecha;
	}
	
}
