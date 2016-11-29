package wap.vannahz.test;

import wap.vannahz.mail.EmailConfigureReader;
import wap.vannahz.mail.SmtpGmail;

public class gmailSenderTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		EmailConfigureReader.EmailConfigure er = EmailConfigureReader.ReadConfigFromConfigFile();
		
		System.out.println("-->" + er.SendTo);
		
		//send mail
		try {
			SmtpGmail.sendMail(er.EmailAccount, er.EmailPassword, er.SendTo.split(";"), "Test for Worksap", "You are successfully");
			System.out.println("Successfully Sent.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Failed to send gmail.");
		}
		
	}

}
