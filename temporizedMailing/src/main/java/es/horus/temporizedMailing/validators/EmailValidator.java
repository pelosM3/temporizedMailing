package es.horus.temporizedMailing.validators;

import com.vaadin.data.Validator;

public class EmailValidator implements Validator {
//	private final static String MULTIPLE_REGEX = "([\\w\\.-]+@[\\w\\.-]+\\.[a-z]{2,6};)*([\\w\\.-]+@[\\w\\.-]+\\.[a-z]{2,6};?)";
	private final static String REGEX = "^[\\w\\.-]+@[\\w\\.-]+\\.[a-z]{2,6}$";

	@Override
	public void validate(Object value) throws Validator.InvalidValueException {
		if (value instanceof String) {
			String email = (String)value;
//			if (!email.matches(MULTIPLE_REGEX)) {
//				List<String> failingAddresses = new ArrayList<>();
//				for(String s : email.split(";")){
//					if(!s.matches(REGEX)) {
//						failingAddresses.add(s);
//					}
//				}
//				throw new Validator.InvalidValueException("Las siguientes direcciones no están bien construidas: "
//							+ String.join(", ", failingAddresses));
//			}
			if(!email.matches(REGEX)) {
				throw new Validator.InvalidValueException("La dirección no está bien construida.");
			}
		}
	}

}
