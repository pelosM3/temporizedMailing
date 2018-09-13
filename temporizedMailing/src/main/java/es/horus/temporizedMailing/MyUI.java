package es.horus.temporizedMailing;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.DefaultConverterFactory;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import es.horus.temporizedMailing.principal.PrincipalCtl;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

	private static final String DEFAULT_LANG = "es";
	private PrincipalCtl principalCtl;

	@Override
    protected void init(VaadinRequest request) {      
		Set<String> admitidos = new HashSet<>(Arrays.asList(DEFAULT_LANG));
        Locale locale;
        if (admitidos.contains(request.getLocale().getLanguage())) {
        	locale = request.getLocale();
        } else {
        	locale = new Locale(DEFAULT_LANG);
        }
        setLocale(locale);
		
		// Iniciamos los ficheros de recursos
//		resources = ResourceBundle.getBundle("language", locale);
		
		getSession().setConverterFactory(new DefaultConverterFactory() {
			@Override
		    protected <PRESENTATION, MODEL> Converter<PRESENTATION, MODEL> findConverter(
		            Class<PRESENTATION> presentationType, Class<MODEL> modelType) {
		        // Conversor String <-> Integer
		        if (presentationType == String.class && modelType == Integer.class) {
		            return (Converter<PRESENTATION, MODEL>) new StringToIntegerConverter(){
		            	public String convertToPresentation(Integer value, Class<? extends String> targetType, Locale locale) throws Converter.ConversionException {
		            		return value == null ? null : value.toString();
		            	};
		            };
		        }
		        // Para el resto de conversores dejamos el comportamiento por defecto
		        return super.findConverter(presentationType, modelType);
		    }
		});
		
		// Configuracion de mensajes
//		getReconnectDialogConfiguration().setDialogText(getString("mensaje.error.servidor.conexion"));
//		errorHandler = new DefaultErrorHandler() {
//			@Override
//			public void error(com.vaadin.server.ErrorEvent event) {
//				log.error("Error interno: ", event.getThrowable());
//				Notification.show(getString("mensaje.error.generico"), Type.ERROR_MESSAGE);
//				//TODO enviar email
//			}
//		};
//		setErrorHandler(errorHandler);
//		getSession().setErrorHandler(errorHandler);
		
		// Titulo
		getPage().setTitle("Programación de e-mails");

		final VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
//		String pro = CanalContenedor.getInstance().getValor(CanalContenedor.ENTORNO);
//		if(!es.horus.g2common.utils.Constants.ENTORNO_PRO.equals(pro)){
//			layout.setStyleName(UIConstants.BROWNLIGHT);
//		}
		setContent(layout);
		principalCtl = new PrincipalCtl();
		principalCtl.showView(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
