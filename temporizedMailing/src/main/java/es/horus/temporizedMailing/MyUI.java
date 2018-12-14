package es.horus.temporizedMailing;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.DefaultConverterFactory;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.server.CustomizedSystemMessages;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.SystemMessages;
import com.vaadin.server.SystemMessagesInfo;
import com.vaadin.server.SystemMessagesProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import es.horus.temporizedMailing.principal.PrincipalCtl;
import es.horus.temporizedMailing.utilities.AvisosManager;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@Widgetset("MyWidgetset")
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
		getReconnectDialogConfiguration().setDialogText("Perdida conexión con el servidor, intentando reconectar...");
		DefaultErrorHandler errorHandler = new DefaultErrorHandler() {
			@Override
			public void error(com.vaadin.server.ErrorEvent event) {
				event.getThrowable().printStackTrace();
				Notification.show("Ha ocurrido un error", Type.ERROR_MESSAGE);
			}
		};
		setErrorHandler(errorHandler);
		getSession().setErrorHandler(errorHandler);
		
		// Titulo
		getPage().setTitle("Programación de e-mails");

		final VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		setContent(layout);
		principalCtl = new PrincipalCtl();
		principalCtl.showView(layout);
		

    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = true)
    public static class MyUIServlet extends VaadinServlet {
    	@Override
    	protected void servletInitialized() throws ServletException {
    		// Iniciamos mensajes
			getService().setSystemMessagesProvider(new SystemMessagesProvider() {
				@Override
				public SystemMessages getSystemMessages(SystemMessagesInfo systemMessagesInfo) {
					CustomizedSystemMessages messages = new CustomizedSystemMessages();
					messages.setSessionExpiredCaption("Tome nota de los datos que necesite y haga click aquí o pulse ESC para continuar.");
					messages.setSessionExpiredMessage("Sesión expirada");
					
					messages.setInternalErrorCaption("Ha ocurrido un error");
					messages.setInternalErrorMessage("Ha ocurrido un error interno en la aplicacion.");
					
					messages.setCommunicationErrorCaption("Ha ocurrido un error");
					messages.setCommunicationErrorMessage("Perdida conexión con el servidor, intentando reconectar...");
					
					messages.setAuthenticationErrorCaption("Ha ocurrido un error");
					messages.setAuthenticationErrorMessage("Acceso denegado.");
					
					messages.setCookiesDisabledCaption("Ha ocurrido un error");
					messages.setCookiesDisabledMessage("Cookies desactivadas");
			        return messages;
				}
			});
			

			AvisosManager avisoMgHilo=new AvisosManager();
			Timer timer = new Timer();
			 
			timer.schedule(avisoMgHilo, new Date(),TimeUnit.HOURS.toMillis(1));//hardcodeado cada hora, meter a properties un rato
    		
    	}
    }
}
