package es.horus.temporizedMailing.events;

import es.horus.temporizedMailing.beans.Programacion;

public class ProgramacionEvent extends PrincipalEvent {
	private Programacion programacion;

	public ProgramacionEvent(Integer id, Programacion programacion) {
		super(id);
		this.programacion = programacion;
	}

	public Programacion getProgramacion() {
		return programacion;
	}

	public void setProgramacion(Programacion programacion) {
		this.programacion = programacion;
	}

}
