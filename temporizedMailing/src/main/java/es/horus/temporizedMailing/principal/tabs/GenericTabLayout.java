package es.horus.temporizedMailing.principal.tabs;

import com.vaadin.ui.VerticalLayout;

import es.horus.temporizedMailing.principal.PrincipalCtl;

public abstract class GenericTabLayout extends VerticalLayout {

	protected PrincipalCtl ctl;
	public GenericTabLayout(PrincipalCtl ctl) {
		this.ctl = ctl;
		buildLayout();
	}

	protected abstract void buildLayout();
}
