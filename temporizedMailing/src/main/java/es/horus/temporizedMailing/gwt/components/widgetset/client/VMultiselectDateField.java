package es.horus.temporizedMailing.gwt.components.widgetset.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.vaadin.client.DateTimeService;
import com.vaadin.client.ui.VDateField;

import es.horus.temporizedMailing.gwt.components.widgetset.client.VMultiselectCalendarPanel.FocusChangeListener;
import es.horus.temporizedMailing.gwt.components.widgetset.client.VMultiselectCalendarPanel.FocusOutListener;
import es.horus.temporizedMailing.gwt.components.widgetset.client.VMultiselectCalendarPanel.SubmitListener;

public class VMultiselectDateField extends VDateField {
	private Set<Long> multiselecteds;
	private int monthsNum = 1;
	
	private List<VMultiselectCalendarPanel> calendarsList;
	private Date rangeStart;
	private Date rangeEnd;

    public VMultiselectDateField() {
        super();
        buildCalendars();
    }

    private void buildCalendars() {
    	clear();
        HorizontalPanel horizontal = new HorizontalPanel();
        add(horizontal);
        calendarsList = new ArrayList<>(monthsNum);
        for (int i = 0; i < monthsNum; i++) {
	        VMultiselectCalendarPanel calendarPanel = new VMultiselectCalendarPanel();
	        calendarPanel.setParentField(this);
	        calendarPanel.setRangeStart(rangeStart);
	        calendarPanel.setRangeEnd(rangeEnd);
	        calendarPanel.setSubmitListener(new SubmitListener() {
	            @Override
	            public void onSubmit() {
	                updateValueFromPanel();
	            }
	
	            @Override
	            public void onCancel() {
	            }
	        });
	        calendarPanel.setFocusOutListener(new FocusOutListener() {
	            @Override
	            public boolean onFocusOut(DomEvent<?> event) {
	                updateValueFromPanel();
	                return false;
	            }
	        });
	        calendarsList.add(calendarPanel);
	        horizontal.add(calendarPanel);
	        if(i != 0) {
	        	Date firstDisplayedMonth = calendarsList.get(0).getDisplayedMonth();
	        	calendarPanel.setDisplayedMonth(firstDisplayedMonth.getYear(), firstDisplayedMonth.getMonth()+i);
	        }
        }
	}

	@SuppressWarnings("deprecation")
    public void updateValueFromPanel() {
        // If field is invisible at the beginning, client can still be null when this function is called.
        if (getClient() == null) {
            return;
        }
        
        String[] multiseleteds = new String[multiselecteds.size()];
        int i = 0;
        for (Long selected : multiselecteds) {
			multiseleteds[i++] = selected.toString();
		}
        getClient().updateVariable(getId(), "multiselecteds", multiseleteds, immediate);

        if (isImmediate()) {
            getClient().sendPendingVariableChanges();
        }
    }

    public void setTabIndex(int tabIndex) {
        getElement().setTabIndex(tabIndex);
    }

    public int getTabIndex() {
        return getElement().getTabIndex();
    }

	public void setMultiselecteds(Set<Long> multiselecteds) {
		this.multiselecteds = multiselecteds;
		for(VMultiselectCalendarPanel calendarPanel : calendarsList) {
			calendarPanel.setMultiselecteds(multiselecteds);
		}
	}

	public void setMonthsNum(int months) {
		if(monthsNum != months) {
			monthsNum = months;
			buildCalendars();
		}
	}

	public void setRangeStart(Date rangeStart) {
		this.rangeStart = rangeStart;
		for(VMultiselectCalendarPanel calendarPanel : calendarsList) {
			calendarPanel.setRangeStart(rangeStart);
		}
	}

	public void setRangeEnd(Date rangeEnd) {
		this.rangeEnd = rangeEnd;
		for(VMultiselectCalendarPanel calendarPanel : calendarsList) {
			calendarPanel.setRangeEnd(rangeEnd);
		}
	}

	public void setDateTimeService(DateTimeService dateTimeService) {
		for(VMultiselectCalendarPanel calendarPanel : calendarsList) {
			calendarPanel.setDateTimeService(dateTimeService);
		}
	}

	public void setFocusChangeListener(FocusChangeListener focusChangeListener) {
		for(VMultiselectCalendarPanel calendarPanel : calendarsList) {
			calendarPanel.setFocusChangeListener(focusChangeListener);
		}
	}

	public void renderCalendar() {
		for(VMultiselectCalendarPanel calendarPanel : calendarsList) {
			calendarPanel.renderCalendar();
		}
	}

	public void displayedMonthChanged(VMultiselectCalendarPanel origin, int command) {
		for (VMultiselectCalendarPanel calendar : calendarsList) {
			if (calendar != origin) {
				calendar.setResendChange(false);
				if (command == 1) {
					calendar.focusNextMonth();
				} else if (command == -1) {
					calendar.focusPreviousMonth();
				} else if (command == 2) {
					calendar.focusNextYear(1);
				} else if (command == -2) {
					calendar.focusPreviousYear(1);
				}
				calendar.setResendChange(true);
			}
		}
		
	}
}
