package es.horus.temporizedMailing.principal.tabs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import es.horus.temporizedMailing.beans.AvisoBE;
import es.horus.temporizedMailing.beans.ProgramacionBE;
import es.horus.temporizedMailing.events.ProgramacionEvent;
import es.horus.temporizedMailing.gwt.components.MultiselectDateField;
import es.horus.temporizedMailing.principal.PrincipalCtl;
import es.horus.temporizedMailing.utilities.Utiles;

public class MensajesLayout extends GenericTabLayout {
	
	private MultiselectDateField calendar;
	private OptionGroup when;
	private PopupDateField fin;
	private PopupDateField ini;
		
	public MensajesLayout(PrincipalCtl ctl) {
		super(ctl);
	}

	@Override
	protected void buildLayout() {
		HorizontalLayout h = new HorizontalLayout();
		h.setSpacing(true);
		h.setWidth("100%");
		
		TextField asunto = new TextField("Asunto:");
		asunto.setWidth("100%");
		h.addComponent(asunto);
		h.setExpandRatio(asunto, 3.0f);
		
		TextField addAddress = new TextField("Para:");
		addAddress.setWidth("100%");
		h.addComponent(addAddress);
		h.setExpandRatio(addAddress, 2.0f);
		
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
		programaciones.setSpacing(true);
		
		ini = new PopupDateField("Fecha para el primer envío:", new Date());
		ini.setTextFieldEnabled(false);
		ini.setResolution(Resolution.DAY);

		fin = new PopupDateField("Fecha fin:", Utiles.getFechaActualMasDias(200));
		fin.setTextFieldEnabled(false);
		fin.setResolution(Resolution.DAY);
		programaciones.addComponent(new VerticalLayout(ini, fin));
		
		when = new OptionGroup("Cuándo");
		when.addItem("Unico");
		when.addItem("Semanal");
		when.addItem("Quincenal");
		when.addItem("Mensual");
		when.addItem("Anual");
		when.setImmediate(true);
		programaciones.addComponent(when);
		when.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent e) {
				updateSelectedDates();
				// capturar el tipo, generar calendarios y marcar los dias en funcion del tipo, incluso si pinchan en el calendario cualquier dia añadir ese dia tb para enviar
			}
		});
		//preseleccionado el que sea
		
		ValueChangeListener datesChangeLis = new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date fhIni = ini.getValue();
				Date fhFin = fin.getValue();
				if(fhIni.after(fhFin)) {
					Notification.show("La fecha de fin debe ser posterior a la de inicio", Type.ERROR_MESSAGE);
				} else {
					Calendar calIni = Calendar.getInstance();
					calIni.setTime(fhIni);
					Calendar calFin = Calendar.getInstance();
					calFin.setTime(fhFin);
					int dif = calFin.get(Calendar.MONTH) - calIni.get(Calendar.MONTH);
					if(dif < 0) {
						dif += 12;
					}
					//TODO poner limites segun las fechas seleccionadas??
					// calendar.setRangeStart(fhIni);
					// calendar.setRangeEnd(fhFin);
					calendar.setMonthsNum(dif+1);
					
					updateSelectedDates();
				}
			}
		};
		ini.addValueChangeListener(datesChangeLis);
		fin.addValueChangeListener(datesChangeLis);
		
		calendar = new MultiselectDateField("Fechas seleccionadas (se pueden modificar haciendo click en cualquier día):");
		// Llamamos al listener para que se pinten el numero de meses necesarios
		datesChangeLis.valueChange(null);
		calendar.setRequired(true);
		programaciones.addComponent(calendar);
		
		Button send = new Button("Programar envío");
		send.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				List<Date> dates = calendar.getSelecteds();
				if(dates.isEmpty()) {
					Notification.show("Debe seleccionar al menos una fecha", Type.ERROR_MESSAGE);
					return;
				}
				if(!isValid()) {
					return;
				}

				AvisoBE miAvisoNuevo = new AvisoBE();
				miAvisoNuevo.setDestinatario(addAddress.getValue());
				miAvisoNuevo.setAsunto(asunto.getValue());
				miAvisoNuevo.setMensaje(cuerpo.getValue());
				miAvisoNuevo.setFechaCreacion(Utiles.getFechaAsLong(ini.getValue()));
				
				StringBuilder sb = new StringBuilder("Añadida programación para las fechas:\n");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				for (Date date : dates) {
					miAvisoNuevo.addProgramacion(new ProgramacionBE(Utiles.getFechaAsLong(date)));
					sb.append(sdf.format(date)).append("\n");
				}
				
		        try {
					ctl.doEvent(new ProgramacionEvent(PrincipalCtl.ADD_TIMER_EVENT,miAvisoNuevo));
					Notification.show(sb.toString(), Type.ERROR_MESSAGE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		addRequiredFields(asunto,addAddress, cuerpo, ini, fin);

		addComponent(h);
		addComponent(container);
		addComponent(programaciones);
		addComponent(send);
		setComponentAlignment(send, Alignment.MIDDLE_CENTER);
		setSpacing(true);
		setMargin(true);
	}

	/**
	 * Actualiza las fechas seleccionadas segun las fechas de inicio y fin y la frecuencia
	 */
	private void updateSelectedDates() {
		if(when == null || when.getValue() == null) {
			return;
		}
		Calendar auxCal = Calendar.getInstance();
		Date fhIni = Utiles.clearDateBelowDay(ini.getValue());
		Date fhFin = Utiles.clearDateBelowDay(fin.getValue());
		
		if(when.getValue().equals("Unico")) {
			calendar.setSelecteds(Arrays.asList(fhIni));
		} else if(when.getValue().equals("Semanal")) {
			List<Date> selecteds = new ArrayList<>();
			auxCal.setTime(fhIni);
			while(!auxCal.getTime().after(fhFin)) {
				selecteds.add(auxCal.getTime());
				auxCal.add(Calendar.DAY_OF_MONTH, 7);
			}
			calendar.setSelecteds(selecteds);
		} else if(when.getValue().equals("Quincenal")) {
			//TODO: cada 15, cada dos semanas, o de cada mes el dia inicio seleccionado y 15 despues??
			List<Date> selecteds = new ArrayList<>();
			auxCal.setTime(fhIni);
			while(!auxCal.getTime().after(fhFin)) {
				selecteds.add(auxCal.getTime());
				auxCal.add(Calendar.DAY_OF_MONTH, 15);
			}
			calendar.setSelecteds(selecteds);
		} else if(when.getValue().equals("Mensual")) {
			List<Date> selecteds = new ArrayList<>();
			auxCal.setTime(fhIni);
			while(!auxCal.getTime().after(fhFin)) {
				selecteds.add(auxCal.getTime());
				auxCal.add(Calendar.MONTH, 1);
			}
			calendar.setSelecteds(selecteds);
		} else if(when.getValue().equals("Anual")) {
			List<Date> selecteds = new ArrayList<>();
			auxCal.setTime(fhIni);
			while(!auxCal.getTime().after(fhFin)) {
				selecteds.add(auxCal.getTime());
				auxCal.add(Calendar.YEAR, 1);
			}
			calendar.setSelecteds(selecteds);
		}
	}

}
