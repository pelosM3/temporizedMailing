package es.horus.temporizedMailing.principal.tabs;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import es.horus.temporizedMailing.beans.Contacto;
import es.horus.temporizedMailing.events.ContactoEvent;
import es.horus.temporizedMailing.principal.PrincipalCtl;
import es.horus.temporizedMailing.validators.EmailValidator;

public class ContactosLayout extends GenericTabLayout {

	public ContactosLayout(PrincipalCtl ctl) {
		super(ctl);
	}

	@Override
	protected void buildLayout() {
		VerticalLayout v = new VerticalLayout();
		HorizontalLayout h = new HorizontalLayout();
		TextField nombre = new TextField("Nombre");
		TextField ape1 = new TextField("Primer apellido");
		TextField ape2 = new TextField("Segundo apellido");
		TextField email = new TextField("Email");
		nombre.setRequired(true);
		ape1.setRequired(true);
		email.setRequired(true);
		email.addValidator(new EmailValidator());
		h.addComponent(nombre);
		h.addComponent(ape1);
		h.addComponent(ape2);
		h.addComponent(email);
		h.setSpacing(true);

		
		Table contactos = new Table();
		contactos.setContainerDataSource(new BeanItemContainer(Contacto.class, ctl.getContactos()));
		contactos.setVisibleColumns(new Object[] { "nombre", "ape1", "ape2","email" });
		contactos.setColumnHeaders(new String[] { "Nombre", "Primer apellido", "Segundo apellido","Email" });
		
		Button guardar = new Button("Guardar");
		guardar.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Contacto c = new Contacto(nombre.getValue(),ape1.getValue(),ape2.getValue(),email.getValue());
				ContactoEvent ce = new ContactoEvent(PrincipalCtl.ADD_CONTACT_EVENT,c);
				try {
					ctl.doEvent(ce);
					contactos.addItem(c);
					nombre.clear();
					ape1.clear();
					ape2.clear();
					email.clear();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		v.addComponent(h);
		v.addComponent(guardar);
		v.addComponent(contactos);
		v.setComponentAlignment(h, Alignment.MIDDLE_CENTER);
		v.setComponentAlignment(guardar, Alignment.BOTTOM_CENTER);
		v.setComponentAlignment(contactos,Alignment.MIDDLE_CENTER);
		v.setSpacing(true);
		v.setMargin(true);
		
		addComponent(v);
	}

}
