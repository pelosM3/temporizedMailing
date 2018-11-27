package es.horus.temporizedMailing.events;

import es.horus.temporizedMailing.beans.AvisoBE;

public class ProgramacionEvent extends PrincipalEvent {
	private AvisoBE programacion;

	public ProgramacionEvent(Integer id, AvisoBE programacion) {
		super(id);
		this.programacion = programacion;
	}

	public AvisoBE getProgramacion() {
		return programacion;
	}

	public void setProgramacion(AvisoBE programacion) {
		this.programacion = programacion;
	}

}
