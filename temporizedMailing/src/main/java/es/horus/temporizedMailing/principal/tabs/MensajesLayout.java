package es.horus.temporizedMailing.principal.tabs;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

import es.horus.temporizedMailing.beans.Programacion;
import es.horus.temporizedMailing.events.ProgramacionEvent;
import es.horus.temporizedMailing.principal.PrincipalCtl;
import es.horus.temporizedMailing.principal.SeleccionaDireccionesWindow;
import es.horus.temporizedMailing.utilities.SendEmailUtility;

public class MensajesLayout extends GenericTabLayout {
	
	private Programacion.CUANDO cuando;
	
	public MensajesLayout(PrincipalCtl ctl) {
		super(ctl);
	}

	@Override
	protected void buildLayout() {
		HorizontalLayout h = new HorizontalLayout();
		final TextField direccion;
		Button addAddress, send;
		TextField asunto;
		RichTextArea cuerpo;
		
		direccion = new TextField("Dirección");
		direccion.setReadOnly(true);
		direccion.setRequired(true);
		direccion.setColumns(60);
		asunto = new TextField("Asunto");
		addAddress = new Button("+");
		addAddress.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				SeleccionaDireccionesWindow pickAddresses = new SeleccionaDireccionesWindow(ctl.getContactos(),ctl.getContactosSeleccionados());
				pickAddresses.addCloseListener(new CloseListener() {
					@Override
					public void windowClose(CloseEvent e) {
						direccion.setReadOnly(false);
						direccion.setValue(pickAddresses.getContactosSeleccionadosEmail());
						direccion.setReadOnly(true);
					}
				});
				getUI().addWindow(pickAddresses);
			}
		});
		
		HorizontalLayout direccionesLayout = new HorizontalLayout();
		direccionesLayout.addComponent(direccion);
		direccionesLayout.addComponent(addAddress);
		direccionesLayout.setComponentAlignment(addAddress, Alignment.BOTTOM_CENTER);
		h.addComponent(asunto);
		h.addComponent(direccionesLayout);
		h.setExpandRatio(asunto, 1.0f);
		h.setExpandRatio(direccionesLayout, 5.0f);
		h.setSpacing(true);
		h.setWidth("100%");
		
		cuerpo = new RichTextArea("Cuerpo del mensaje");
		cuerpo.setId("cuerpo");
		cuerpo.setImmediate(true);
		cuerpo.setRequired(true);
		cuerpo.setSizeFull();
		
		HorizontalLayout programaciones = new HorizontalLayout();
		OptionGroup when = new OptionGroup("Cuándo",Arrays.asList(Programacion.CUANDO.values()));
		when.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent e) {
				cuando = (Programacion.CUANDO) e.getProperty().getValue();
			}
		});
		when.setValue(Programacion.CUANDO.UNA_VEZ);
		when.setImmediate(true);
		
		PopupDateField cal = new PopupDateField("Fechas de envío del mensaje.",new Date());
		cal.setTextFieldEnabled(false);
		cal.setResolution(Resolution.SECOND);
		programaciones.addComponent(when);
		programaciones.addComponent(cal);
		
		send = new Button("Programar envío");
		send.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {		        
		        SendEmailUtility task = new SendEmailUtility(direccion.getValue(), asunto.getValue(),cuerpo.getValue(), null,null);
		        Timer timer = new Timer();
		        Programacion programacion;
				Date firstTime = cal.getValue();
				SimpleDateFormat simpleDateformat;
				String caption = "Nueva programación";
				String message = "";
		        switch(cuando) {
			        case UNA_VEZ:
			        	timer.schedule(task, firstTime);
			        	Notification.show("Mensaje programado para: "+firstTime.toString());
			        	break;
					case ANUALMENTE:
			        	timer.schedule(task, firstTime, TimeUnit.DAYS.toMillis(365));
						break;
					case MENSUALMENTE:
			        	timer.schedule(task, firstTime, TimeUnit.DAYS.toMillis(30));
						break;
					case PERSONALIZADO:
						break;
					case SEMANALMENTE:
			        	timer.schedule(task, firstTime, TimeUnit.DAYS.toMillis(7));
			        	simpleDateformat = new SimpleDateFormat("EEEE");
			        	message = "Mensaje programado para todos los: "+simpleDateformat.format(firstTime);
						break;
					default:
			        	task.run();
						break;
		        }
		        programacion = new Programacion(timer, UUID.nameUUIDFromBytes(new Date().toString().getBytes()),cuando);
		        try {
					ctl.doEvent(new ProgramacionEvent(PrincipalCtl.ADD_TIMER_EVENT,programacion));
				} catch (Exception e) {
					e.printStackTrace();
				}
	        	Notification.show(caption,message,Notification.Type.TRAY_NOTIFICATION);
			}
		});

		addComponent(h);
		addComponent(cuerpo);
		addComponent(programaciones);
		addComponent(send);
		setComponentAlignment(send, Alignment.MIDDLE_CENTER);
		setSpacing(true);
		setMargin(true);
	}

}
