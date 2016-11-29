package wap.vannahz.database;

import java.io.FileInputStream;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class DatabaseConfigureReader {
	public static class DatabaseConfigure {
		
		public String database;
		public String username;
		public String password;
		public String schema;
	}

	public static DatabaseConfigure ReadXml() {
		try {
			Element Root = null;
			Root = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new FileInputStream("DatabaseConfiguration.xml"))
					.getDocumentElement();
			DatabaseConfigure DatabaseConfigure = new DatabaseConfigure();
			for (Node node = Root.getFirstChild(); node != null; node = node.getNextSibling())
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					
					String nodeName = node.getNodeName();
					String nodeValue = getNodeValueFromNode(node);
					switch (nodeName) {
					
					case "database":
						DatabaseConfigure.database = nodeValue;
						break;
					case "username":
						DatabaseConfigure.username = nodeValue;
						break;
					case "password":
						DatabaseConfigure.password = nodeValue;
						break;
					case "schema":
						DatabaseConfigure.schema = nodeValue;
						break;
					}
				}
			return DatabaseConfigure;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getNodeValueFromNode(Node node){
		String returnValue;
		try {
			returnValue = node.getFirstChild().getNodeValue();
		} catch (Exception e) {
			returnValue = "";
		}
		return returnValue;
	}
}
