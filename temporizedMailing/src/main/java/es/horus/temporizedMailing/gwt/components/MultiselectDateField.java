package es.horus.temporizedMailing.gwt.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vaadin.data.Property;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.ui.InlineDateField;

import es.horus.temporizedMailing.gwt.components.widgetset.client.MultiselectDateFieldState;

public class MultiselectDateField extends InlineDateField {
	
	private final int MAX_MONTHS = 5;
	
	private Set<Long> multiselecteds = new HashSet<>();
	
	private int monthsNum = 1;

	public MultiselectDateField() {
	}

	public MultiselectDateField(Property<Date> dataSource) throws IllegalArgumentException {
		super(dataSource);
	}

	public MultiselectDateField(String caption) {
		super(caption);
	}

	public MultiselectDateField(String caption, Date value) {
		super(caption, value);
	}

	public MultiselectDateField(String caption, Property<Date> dataSource) {
		super(caption, dataSource);
	}
	
	@Override
	public MultiselectDateFieldState getState() {
	    return (MultiselectDateFieldState) super.getState();
	}

	public void setSelecteds(Collection<Date> selected) {
		Set<Long> sel = new HashSet<>();
		for (Date date : selected) {
			sel.add(date.getTime());
		}
		multiselecteds  = sel;
		markAsDirty();
	}
	
	public List<Date> getSelecteds() {
		List<Date> selecteds = new ArrayList<>(multiselecteds.size());
		for (Long date : multiselecteds) {
			selecteds.add(new Date(date));
		}
		Collections.sort(selecteds);
		return selecteds;
	}
	
	@Override
    public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);
		String[] variable = new String[multiselecteds.size()];
		int i = 0;
		for (Long selected : multiselecteds) {
			variable[i++] = selected.toString();
		}
		target.addVariable(this, "multiselecteds", variable);
		target.addVariable(this, "monthsNum", monthsNum);
	}
	
	@Override
	public void changeVariables(Object source, Map<String, Object> variables) {
		super.changeVariables(source, variables);
		
		if(variables.containsKey("multiselecteds")) {
			final String[] variable = (String[]) variables.get("multiselecteds");
			Set<Long> selecteds = new HashSet<>(variable.length);
			for (String selected : variable) {
				selecteds.add(Long.valueOf(selected));
			}
			multiselecteds = selecteds; 
		}
	}

	public void setMonthsNum(int monthsNum) {
		monthsNum = Math.min(monthsNum, MAX_MONTHS);
		if (this.monthsNum != monthsNum) {
			this.monthsNum = monthsNum;
			markAsDirty();
		}
	}
}
