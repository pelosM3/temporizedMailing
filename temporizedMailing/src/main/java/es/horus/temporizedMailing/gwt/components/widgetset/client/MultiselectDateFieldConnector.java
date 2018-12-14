package es.horus.temporizedMailing.gwt.components.widgetset.client;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.UIDL;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.datefield.AbstractDateFieldConnector;
import com.vaadin.shared.ui.Connect;

import es.horus.temporizedMailing.gwt.components.MultiselectDateField;
import es.horus.temporizedMailing.gwt.components.widgetset.client.VMultiselectCalendarPanel.FocusChangeListener;

@Connect(MultiselectDateField.class)
public class MultiselectDateFieldConnector extends AbstractDateFieldConnector {

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        getWidget().setTabIndex(getState().tabIndex);
        getWidget().setRangeStart(getState().rangeStart);
        getWidget().setRangeEnd(getState().rangeEnd);
    }

    @Override
    public VMultiselectDateField getWidget() {
        return (VMultiselectDateField) super.getWidget();
    }
	
    @Override
    public MultiselectDateFieldState getState() {
        return (MultiselectDateFieldState) super.getState();
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);
        if (!isRealUpdate(uidl)) {
            return;
        }
        getWidget().setMonthsNum(uidl.getIntVariable("monthsNum"));
        Set<Long> multiselecteds = new HashSet<>();
        for(String selected : uidl.getStringArrayVariableAsSet("multiselecteds")) {
        	multiselecteds.add(Long.valueOf(selected));
        }
        getWidget().setMultiselecteds(multiselecteds);

        getWidget().setShowISOWeekNumbers(getWidget().isShowISOWeekNumbers());
        getWidget().setDateTimeService(getWidget().getDateTimeService());

        getWidget().setFocusChangeListener(new FocusChangeListener() {
	        @Override
	        public void focusChanged(Date date) {
	            getWidget().updateValueFromPanel();
	        }
    	});

        // Update possible changes
        getWidget().renderCalendar();
    }
}
