package wap.vannahz.mail;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import wap.vannahz.database.DBConnection;


public class EmailConfigureReader{
	private static final Logger logger = Logger.getLogger(EmailConfigureReader.class);
	
    public static class EmailConfigure{
        public String EmailAccount;
        public String EmailPassword;
        public String SendTo;
    }
    
    public static EmailConfigure ReadConfigFromConfigFile(){
    	 try{
             Element Root = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream("EmailConfiguration.xml")).getDocumentElement();
             EmailConfigure EmailConfigure = new EmailConfigure();
             for (Node node = Root.getFirstChild(); node != null; node = node.getNextSibling()) 
            	 if (node.getNodeType() == Node.ELEMENT_NODE){
	                 if (node.getNodeName().equals("EmailAccount"))       EmailConfigure.EmailAccount = node.getFirstChild().getNodeValue();
	                 else if (node.getNodeName().equals("EmailPassword")) EmailConfigure.EmailPassword = node.getFirstChild().getNodeValue();
	                 else if (node.getNodeName().equals("SendTo"))        EmailConfigure.SendTo = node.getFirstChild().getNodeValue();
             }
             return EmailConfigure;
         }catch (Exception e){
             e.printStackTrace();
             return null;
         }
    }
}