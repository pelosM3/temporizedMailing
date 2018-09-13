package es.horus.temporizedMailing.principal;

import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import es.horus.temporizedMailing.principal.tabs.ContactosLayout;
import es.horus.temporizedMailing.principal.tabs.MensajesLayout;
import es.horus.temporizedMailing.principal.tabs.ProgramacionesLayout;

public class PrincipalView {

	private Window ventana;
	private VerticalLayout containerPrincipal;
	private PrincipalCtl ctl;
	
	public PrincipalView(Layout container, PrincipalCtl ctl) {
		containerPrincipal = (VerticalLayout) container;
		this.ctl = ctl;
	}
	public void show() {
		containerPrincipal.removeAllComponents();

		ventana = new Window();
		ventana.setDraggable(false);
		ventana.setResizable(false);
		ventana.setClosable(false);
		ventana.setSizeFull();
		ventana.center();

		TabSheet ts = new TabSheet();
		addTab(ts,"Enviar mensaje",new MensajesLayout(ctl));
		addTab(ts,"Contactos",new ContactosLayout(ctl));
		addTab(ts,"Programaciones",new ProgramacionesLayout(ctl));
		ventana.setContent(ts);
				
		containerPrincipal.getUI().addWindow(ventana);
	}

	private TabSheet.Tab addTab(TabSheet sheet, String title, Component tab) {
		if(tab == null) tab = new VerticalLayout();
		return sheet.addTab(tab, title);
	}
}
