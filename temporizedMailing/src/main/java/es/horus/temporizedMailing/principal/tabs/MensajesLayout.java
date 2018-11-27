package es.horus.temporizedMailing.principal.tabs;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;

import es.horus.temporizedMailing.beans.AvisoBE;
import es.horus.temporizedMailing.beans.ProgramacionBE;
import es.horus.temporizedMailing.events.ProgramacionEvent;
import es.horus.temporizedMailing.principal.PrincipalCtl;
import es.horus.temporizedMailing.utilities.Utiles;

public class MensajesLayout extends GenericTabLayout {
	
	private AvisoBE miAvisoNuevo=null;
		
	public MensajesLayout(PrincipalCtl ctl) {
		super(ctl);
	}

	@Override
	protected void buildLayout() {
		miAvisoNuevo=new AvisoBE();
		HorizontalLayout h = new HorizontalLayout();
		
		TextField asunto = new TextField("Asunto:");
		TextField addAddress = new TextField("Para:");
		addAddress.setColumns(60);
		
		HorizontalLayout direccionesLayout = new HorizontalLayout();
		direccionesLayout.addComponent(addAddress);
		direccionesLayout.setComponentAlignment(addAddress, Alignment.BOTTOM_CENTER);
		h.addComponent(asunto);
		h.addComponent(direccionesLayout);
		h.setExpandRatio(asunto, 1.0f);
		h.setExpandRatio(direccionesLayout, 5.0f);
		h.setSpacing(true);
		h.setWidth("100%");
		
		RichTextArea cuerpo = new RichTextArea("Cuerpo del mensaje");
		cuerpo.setId("cuerpo");
		cuerpo.setImmediate(true);
		cuerpo.setRequired(true);
		cuerpo.setSizeFull();
		
		CssLayout container = new CssLayout(cuerpo) {
			@Override
			protected String getCss(Component c) {
				return "resize: vertical; overflow: auto;";
			}
		};
		container.setSizeFull();
		
		HorizontalLayout programaciones = new HorizontalLayout();
		OptionGroup when = new OptionGroup("Cuándo");
		when.addItem("Unico");
		when.addItem("Semanal");
		when.addItem("Quincenal");
		when.addItem("Mensual");
		when.addItem("Anual");
		
		when.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent e) {
				// capturar el tipo, generar calendarios y marcar los dias en funcion del tipo, incluso si pinchan en el calendario cualquier dia añadir ese dia tb para enviar
			}
		});
		//preseleccionado el que sea
		
		when.setImmediate(true);
		
		PopupDateField cal = new PopupDateField("Fecha para el primer envío:",new Date());
		cal.setTextFieldEnabled(false);
		cal.setResolution(Resolution.DAY);

		PopupDateField fin = new PopupDateField("Fechas fin",Utiles.getFechaActualMasDias(90));
		fin.setTextFieldEnabled(false);
		fin.setResolution(Resolution.DAY);
		
		programaciones.addComponent(when);
		programaciones.addComponent(cal);
		programaciones.addComponent(fin);
		
		Button send = new Button("Programar envío");
		send.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if(!isValid()) {
					return;
				}

				Date firstTime = cal.getValue();
				SimpleDateFormat simpleDateformat;

				if(miAvisoNuevo==null) {
					//no me preguntes como pero da null ?¿?¿
					
					miAvisoNuevo=new AvisoBE();
				}

				miAvisoNuevo.setDestinatario(addAddress.getValue());
				miAvisoNuevo.setAsunto(asunto.getValue());
				miAvisoNuevo.setMensaje(cuerpo.getValue());
				miAvisoNuevo.setFechaCreacion(Utiles.getFechaAsLong(cal.getValue()));

				//esto habra que ir guardandolo al generar los calendarios no aqui, pero por probar....
				miAvisoNuevo.addProgramacion(new ProgramacionBE(20190131L));
				miAvisoNuevo.addProgramacion(new ProgramacionBE(20190831L));
				miAvisoNuevo.addProgramacion(new ProgramacionBE(20200131L));
				
				
				
		        try {
					ctl.doEvent(new ProgramacionEvent(PrincipalCtl.ADD_TIMER_EVENT,miAvisoNuevo));
				} catch (Exception e) {
					e.printStackTrace();
				}
	        	//Notification.show(caption,message,Notification.Type.TRAY_NOTIFICATION);
			}
		});
		addRequiredFields(asunto,addAddress, cuerpo);

		addComponent(h);
		addComponent(container);
		addComponent(programaciones);
		addComponent(send);
		setComponentAlignment(send, Alignment.MIDDLE_CENTER);
		setSpacing(true);
		setMargin(true);
	}

}
