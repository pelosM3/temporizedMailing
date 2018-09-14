package es.horus.temporizedMailing.principal;

import java.util.HashSet;
import java.util.Set;

import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import es.horus.temporizedMailing.beans.Contacto;
import es.horus.temporizedMailing.beans.Programacion;
import es.horus.temporizedMailing.dao.ContactoDAO;
import es.horus.temporizedMailing.dao.ProgramacionDAO;
import es.horus.temporizedMailing.events.ContactoEvent;
import es.horus.temporizedMailing.events.PrincipalEvent;
import es.horus.temporizedMailing.events.ProgramacionEvent;

public class PrincipalCtl {

	public final static Integer ADD_CONTACT_EVENT = 0;
	public final static Integer ADD_TIMER_EVENT = 1;
	
	private PrincipalView view;
	private Set<Contacto> contactosSeleccionados;
	
	public PrincipalCtl() {
		contactosSeleccionados = new HashSet<>();
	}
	
	public void showView(Layout layout) {
		view = new PrincipalView(layout,this);
		view.show();
	}

	public Set<Contacto> getContactos() {
		try {
			return ContactoDAO.get().getRows();
		} catch (Exception e) {
			e.printStackTrace();
			return new HashSet<>();
		}
	}

	public Set<Contacto> getContactosSeleccionados() {
		return contactosSeleccionados;
	}
	
	public Set<Programacion> getProgramaciones() {
		try {
			return ProgramacionDAO.get().getRows();
		} catch (Exception e) {
			e.printStackTrace();
			return new HashSet<>();
		}
	}
	
	public boolean doEvent(PrincipalEvent event) throws Exception {
		if(ADD_CONTACT_EVENT.equals(event.getId()) && event instanceof ContactoEvent) {
			Contacto insert = ((ContactoEvent)event).getContacto();
			if(!ContactoDAO.get().insertRow(insert)) {
				Notification.show("No se pudo guardar el contacto en la base de datos.", Type.ERROR_MESSAGE);
				return false;
			}
			Notification.show("Nuevo contacto","Se añadió el contacto con email: "+insert.getEmail(),Notification.Type.TRAY_NOTIFICATION);
			return true;
		} else if(ADD_TIMER_EVENT.equals(event.getId()) && event instanceof ProgramacionEvent) {
			Programacion insert = ((ProgramacionEvent)event).getProgramacion();
			if(!ProgramacionDAO.get().insertRow(insert)) {
				Notification.show("No se pudo guardar la programación en la base de datos.", Type.ERROR_MESSAGE);
				return false;
			}
			Notification.show("Nueva programación","Se añadió la programación con uuid: "+insert.getUuid(),Notification.Type.TRAY_NOTIFICATION);
			return true;
		} else {
			return false;
		}
	}
	
}
