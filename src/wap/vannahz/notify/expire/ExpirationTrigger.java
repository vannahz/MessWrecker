package wap.vannahz.notify.expire;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import wap.vannahz.database.Constant;
import wap.vannahz.database.DBFunction;
import wap.vannahz.entities.ExpiringGoods;
import wap.vannahz.mail.EmailConfigureReader;
import wap.vannahz.mail.SmtpGmail;

public class ExpirationTrigger {
	
	
	SimpleDateFormat formatter_Day = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final Logger logger = Logger.getLogger(ExpirationTrigger.class);
	
	public String genEmailSubject(Date date){

		return "Expiring Goods List " + formatter_Day.format(date);
	}
	
	public String genEmailBody(Date date, double percentage, int remains){
		
		List<ExpiringGoods> eglist = DBFunction.searchExpiringGoods(percentage, remains, date);
		
		StringBuffer contents = new StringBuffer(1023);
		
		if(eglist.isEmpty()){
			
			
			contents.append("<html>");
			contents.append("<head><style>table,th,td{border:0px solid #F8F8F8;border-collapse:collapse;}</style></head>");
			contents.append("<body>");
			contents.append("<div align='center'>");
			contents.append("<font face='verdana' size=5>Expiring Goods List</font><br>");
			contents.append("<font face='verdana' size=2>2016-11-19</font><br>");
			contents.append("<HR noshade size=1 width='75%' COLOR='LightSkyBlue'><br>");
			contents.append("</div><div align='center'>");
			contents.append("<font face='verdana' size=2>No Expiring Goods Today.</font>");
			contents.append("</div><br><div align='center'>");
			contents.append("<HR noshade size=1 width='75%' COLOR='LightSkyBlue'>");
			contents.append("<font face='verdana' size=1 color='gray'> Copyright &copy; 2016 Vannahz Zhang. All rights reserved.</font><br>");
			contents.append("</div></body></html>");
			
		}else{
		
			if(DBFunction.storeExpiringGoods(eglist)) logger.info("Expiring Goods are stored in db successfully");
			
			contents.append("<html>");
			contents.append("<head><style>table,th,td{border:1px solid #F8F8F8;border-collapse:collapse;}</style></head>");
			contents.append("<body><div align='center'>");
			contents.append("<font face='verdana' size=5>Expiring Goods List</font><br>");
			contents.append("<font face='verdana' size=2>" + date +"</font><br>");
			contents.append("<HR noshade size=1 width='95%' COLOR='LightSkyBlue'><br></div>");
			contents.append("<div align='center'><font face='verdana' size =2>");
			contents.append("<table style='width:600px;'>");
			contents.append("<tr><th>Product ID</th><th>Product Name</th><th>Amount</th><th align=right>Remains</th></tr>");
			contents.append("<tr><td colspan='4'>&emsp;</td></tr>");
			
			for(int i=0; i<eglist.size(); i++){
				
				ExpiringGoods eg = eglist.get(i);
				contents.append("<tr><td>" + eg.getProdect_id() + "</td><td align=center>" + eg.getProduct_name() + "</td><td align=right>");
				contents.append(eg.getTotal_amount() + " pcs</td><td align=right>" + eg.getLeft_days() + " days</td></tr>");
			}
			
			contents.append("<tr><td colspan='4'>&emsp;</td></tr>");
			contents.append("</table><br></font></div>");
			
			//footer
			contents.append("<div align='center'>");
			contents.append("<HR noshade size=1 width='95%' COLOR='LightSkyBlue'>");
			contents.append("<font face='verdana' size=1 color='gray'> Copyright &copy; 2016 Vannahz Zhang. All rights reserved.</font><br>");
			contents.append("<div align='center'>");
			contents.append("</body></html>");
		
		}
		
		return contents.toString();
	}
	
	public void sendEmail(String send_to, double percentage, int remains){
		
		Date date = Calendar.getInstance().getTime();
		
		
		// Initialize DB
		if (DBFunction.initDB()) {
			logger.info("Database settings initialized. Host name: " + Constant.databaseSettings.database);
		} else {
			logger.error("Fail to initialize database.");
			return;
		}
		
		//initialize Email Setting
		EmailConfigureReader.EmailConfigure er = EmailConfigureReader.ReadConfigFromConfigFile();
		if (er!=null) {
			logger.info("Email configure initialized. Send from: " + er.EmailAccount);
		} else {
			logger.error("Fail to initialize Email configure.");
			return;
		}
		
		
		//generate email subject & body
		logger.info("Searching for Expiring Goods...");
		String subject = genEmailSubject(date);
		String body = genEmailBody(date, percentage, remains);
		logger.info("Finish searching... " );
		
		
		//send mail
		try {
			SmtpGmail.sendMail(er.EmailAccount, er.EmailPassword, send_to.split(";"), subject, body);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Failed to send emails." + e);
		}
		
		logger.info("Successfully sent email to " + send_to);
	}

}
