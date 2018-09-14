package es.horus.temporizedMailing.principal.tabs;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import es.horus.temporizedMailing.beans.Programacion;
import es.horus.temporizedMailing.principal.PrincipalCtl;

public class ProgramacionesLayout extends GenericTabLayout {

	public ProgramacionesLayout(PrincipalCtl ctl) {
		super(ctl);
	}

	protected void buildLayout() {
		VerticalLayout v = new VerticalLayout();
		
		Table programaciones = new Table();
		programaciones.setContainerDataSource(new BeanItemContainer<Programacion>(Programacion.class, ctl.getProgramaciones()));
		programaciones.containerItemSetChange(new ItemSetChangeEvent() {
			@Override
			public Container getContainer() {
				return new BeanItemContainer<Programacion>(Programacion.class, ctl.getProgramaciones());
			}
		});
		programaciones.setVisibleColumns(new Object[] { "uuid","cuando" });
		programaciones.setColumnHeaders(new String[] { "UUID", "Cuando" });
		
		v.addComponent(programaciones);
		v.setComponentAlignment(programaciones,Alignment.MIDDLE_CENTER);
		v.setSpacing(true);
		v.setMargin(true);
		
		addComponent(v);
	}
}
