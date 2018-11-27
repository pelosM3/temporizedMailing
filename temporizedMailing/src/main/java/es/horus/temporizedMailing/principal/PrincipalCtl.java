package es.horus.temporizedMailing.principal;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import es.horus.temporizedMailing.beans.AvisoBE;
import es.horus.temporizedMailing.dao.AvisosDAO;
import es.horus.temporizedMailing.events.PrincipalEvent;
import es.horus.temporizedMailing.events.ProgramacionEvent;

public class PrincipalCtl {

	public final static Integer ADD_TIMER_EVENT = 1;
	
	private PrincipalView view;
	private AvisosDAO  aviDAO=null;
	
	public PrincipalCtl() {
		aviDAO=new AvisosDAO();
	}
	
	public void showView(Layout layout) {
		view = new PrincipalView(layout,this);
		view.show();
	}


	
	public List<AvisoBE> getProgramaciones() {
		try {
			return aviDAO.cargaBBDD();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<AvisoBE>();
		}
	}
	
	public boolean doEvent(PrincipalEvent event) throws Exception {
		if(ADD_TIMER_EVENT.equals(event.getId()) && event instanceof ProgramacionEvent) {
			AvisoBE insert = ((ProgramacionEvent)event).getProgramacion();
			if(!aviDAO.guarda(insert)) {
				Notification.show("No se pudo guardar la programación en la base de datos.", Type.ERROR_MESSAGE);
				return false;
			}
			Notification.show("Nueva programación","Se añadió la programación con uuid: "+insert.getAsunto(),Notification.Type.TRAY_NOTIFICATION);
			return true;
		} else {
			return false;
		}
	}
	
}
