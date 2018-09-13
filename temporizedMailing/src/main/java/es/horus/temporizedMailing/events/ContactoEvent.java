package es.horus.temporizedMailing.events;

import es.horus.temporizedMailing.beans.Contacto;

public class ContactoEvent extends PrincipalEvent {
	private Contacto contacto;

	public ContactoEvent(Integer id, Contacto contacto) {
		super(id);
		this.contacto = contacto;
	}

	public Contacto getContacto() {
		return contacto;
	}

	public void setContacto(Contacto contacto) {
		this.contacto = contacto;
	}
}
