package es.horus.temporizedMailing.principal.tabs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

import es.horus.temporizedMailing.principal.PrincipalCtl;

public abstract class GenericTabLayout extends VerticalLayout {
	
	private List<AbstractField<?>> requiredFields = new ArrayList<>();

	protected PrincipalCtl ctl;
	
	public GenericTabLayout(PrincipalCtl ctl) {
		this.ctl = ctl;
		buildLayout();
	}
	
	protected void addRequiredFields(AbstractField<?>... fields) {
		if(fields != null) {
			for (AbstractField<?> field : fields) {
				requiredFields.add(field);
				field.setRequiredError("Campo obligatorio");
				field.setValidationVisible(false);
			}
		}
	}
	
	protected boolean isValid() {
		List<String> invalids = new ArrayList<>();
		for (AbstractField<?> field : requiredFields) {
			if (!field.isValid()) {
				invalids.add(field.getCaption());
			}
			field.setValidationVisible(!field.isValid());
		}
		if(!invalids.isEmpty()) {
			Notification.show("Rellene correctamente los campos: "+StringUtils.join(invalids, ", "), Type.ERROR_MESSAGE);
		}
		return invalids.isEmpty();
	}

	protected abstract void buildLayout();
}
