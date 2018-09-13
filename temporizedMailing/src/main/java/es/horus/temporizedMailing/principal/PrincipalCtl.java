package es.horus.temporizedMailing.principal;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;

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
	private Set<Contacto> contactos, contactosSeleccionados;
	private Set<Programacion> programaciones;
	
	public PrincipalCtl() {
		try {
			contactos = new ContactoDAO().getRows();
		} catch (ParserConfigurationException | SAXException | IOException | InstantiationException | IllegalAccessException e) {
			contactos = new HashSet<>();
		}
		try {
			programaciones = new ProgramacionDAO().getRows();
		} catch (ParserConfigurationException | SAXException | IOException | InstantiationException | IllegalAccessException e) {
			programaciones = new HashSet<>();
		}
		contactosSeleccionados = new HashSet<>();
	}
	
	public void showView(Layout layout) {
		view = new PrincipalView(layout,this);
		view.show();
	}

	public Set<Contacto> getContactos() {
		return contactos;
	}
	
	public Set<Contacto> getContactosSeleccionados() {
		return contactosSeleccionados;
	}
	
	public Set<Programacion> getProgramaciones() {
		return programaciones;
	}
	
	public boolean doEvent(PrincipalEvent event) throws NoSuchFieldException, SecurityException, ParserConfigurationException, SAXException, IOException {
		if(ADD_CONTACT_EVENT.equals(event.getId()) && event instanceof ContactoEvent) {
			Contacto insert = ((ContactoEvent)event).getContacto();
			if(contactos.add(insert)) {
				if(!new ContactoDAO().insertRow(insert)) {
					contactos.remove(insert);
					Notification.show("No se pudo guardar el contacto en la base de datos.");
					return false;
				}
				Notification.show("Nuevo contacto","Se añadió el contacto con email: "+insert.getEmail(),Notification.Type.TRAY_NOTIFICATION);
				return true;
			} else {
				Notification.show(String.format("El contacto con email %s ya existe en la base de datos.",insert.getEmail()));
				return false;
			}
		} else if(ADD_TIMER_EVENT.equals(event.getId()) && event instanceof ProgramacionEvent) {
			Programacion insert = ((ProgramacionEvent)event).getProgramacion();
			if(programaciones.add(insert)) {
				if(!new ProgramacionDAO().insertRow(insert)) {
					programaciones.remove(insert);
					Notification.show("No se pudo guardar la programación en la base de datos.");
					return false;
				}
				Notification.show("Nueva programación","Se añadió la programación con uuid: "+insert.getUuid(),Notification.Type.TRAY_NOTIFICATION);
				return true;
			} else {
				Notification.show("No se pudo guardar la programación en la base de datos.");
				return false;
			}
		} else {
			return false;
		}
	}
	
}
