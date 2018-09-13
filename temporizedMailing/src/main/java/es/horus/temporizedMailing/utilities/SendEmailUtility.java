package es.horus.temporizedMailing.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TimerTask;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import es.horus.temporizedMailing.MyUI;

public class SendEmailUtility extends TimerTask {

	private final static String PROPS_PATH = "/mail.properties";
	private String toEmail,subject,body;
	private List<InputStream> attachtments;
	private List<String> fileNames;
	
	public SendEmailUtility(String toEmail, String subject, String body, List<InputStream> attachtments, List<String> fileNames) {
		super();
		this.toEmail = toEmail;
		this.subject = subject;
		this.body = body;
		this.attachtments = attachtments == null ? new ArrayList<>() : attachtments;
		this.fileNames = fileNames == null ? new ArrayList<>() : fileNames;
	}
	
    private void sendEmail(){   
    	Properties props = new Properties();
    	try {
			props.load(MyUI.class.getResourceAsStream(PROPS_PATH));
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
    	String username = props.getProperty("username");
    	String password = props.getProperty("password");

		Session session = Session.getDefaultInstance(props,
			new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username,password);
				}
			});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(toEmail.replace(';', ',')));
			message.setSubject(subject);
//			attachtments.add(new FileInputStream(Paths.get(System.getProperty("user.home"),"prueba.txt").toString()));
//			fileNames.add("prueba.txt");
			message.setContent(createMessageContent());
			
			Transport.send(message);

		} catch (MessagingException | IOException e) {
			throw new RuntimeException(e);
		}   
    }
    
    private MimeMultipart createMessageContent() throws MessagingException, IOException {
		MimeMultipart multipart = new MimeMultipart();
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(body, "text/html");
		multipart.addBodyPart(messageBodyPart);
		
//		for(int i = 0 ; i < attachtments.size() ; i++) {
//			messageBodyPart = new MimeBodyPart();
//			DataSource fds = new ByteArrayDataSource(attachtments.get(i),"text/plain");
//			messageBodyPart.setDataHandler(new DataHandler(fds));
//			messageBodyPart.setFileName(fileNames.get(i));
//			
//			// add image to the multipart
//			multipart.addBodyPart(messageBodyPart);
//		}
		
		return multipart;
    }
    
//    private String getMimeType(String fileName) throws IOException {
//    	URL url = new URL("http://www.iana.org/assignments/media-types/application.csv");
//    	Scanner scanner = new Scanner(url.openStream());
//    	String line = scanner.next(FilenameUtils.getExtension(fileName));
//    	
//    	return line.split(",")[1];
//    }

	@Override
	public void run() {
		sendEmail();
	}
}
