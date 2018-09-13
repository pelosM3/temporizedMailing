package es.horus.temporizedMailing.principal;

import java.util.Set;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import es.horus.temporizedMailing.beans.Contacto;

public class SeleccionaDireccionesWindow extends Window {
	
	private TwinColSelect contactosSeleccion;
	public SeleccionaDireccionesWindow(Set<Contacto> contactos, Set<Contacto> contactosSeleccionados) {		
		VerticalLayout v = new VerticalLayout();
 
		contactosSeleccion = new TwinColSelect("Contactos", contactos);
		contactosSeleccion.setValue(contactosSeleccionados);
		contactosSeleccion.setRows(6);
		contactosSeleccion.setWidth("50%");
		contactosSeleccion.setLeftColumnCaption("Contactos disponibles");
		contactosSeleccion.setRightColumnCaption("Contactos seleccionados");

		v.addComponent(contactosSeleccion);
		v.setComponentAlignment(contactosSeleccion, Alignment.MIDDLE_CENTER);
		v.setSizeFull();
		v.setSpacing(true);
		v.setMargin(true);
		
		setContent(v);
		setWidth("80%");
		setHeight("80%");
		setModal(true);
		this.addCloseListener(new CloseListener() {
			@Override
			public void windowClose(CloseEvent e) {
				contactosSeleccionados.addAll(getContactosSeleccionados());
			}
		});
	}

	private Set<Contacto> getContactosSeleccionados() {
		return (Set<Contacto>) contactosSeleccion.getValue();
	}

	public String getContactosSeleccionadosEmail() {
		StringBuilder sb = new StringBuilder("");
		getContactosSeleccionados().forEach(c -> sb.append(c.getEmail()).append(';'));
		return sb.toString();
	}
	
	
}