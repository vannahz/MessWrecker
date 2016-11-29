package wap.vannahz.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;

import com.sun.mail.smtp.SMTPMessage;
import com.sun.mail.smtp.SMTPSSLTransport;

public class SmtpGmail{
	
    private static final String SMTP_HOST_NAME = "smtp.gmail.com";
    private static final int SMTP_HOST_PORT = 465;
   
    
    public static void sendMail(String accName, String accPassword, String[] to, String subject, String body) throws Exception{
	
		
		Session session = Session.getInstance(new Properties());
		SMTPMessage emailBody = new SMTPMessage(session);
		emailBody.setFrom(new InternetAddress(accName));
		for (int i = 0; i < to.length; i++) {
			emailBody.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
		}
		emailBody.setSubject(subject, "UTF-8");
		emailBody.setText(body, "UTF-8", "html");
		emailBody.saveChanges();

		// Send message
		SMTPSSLTransport tr = (SMTPSSLTransport) session.getTransport("smtps");
		tr.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, accName, accPassword);
		tr.sendMessage(emailBody, emailBody.getAllRecipients());
		tr.close();

	}

}
    
    

