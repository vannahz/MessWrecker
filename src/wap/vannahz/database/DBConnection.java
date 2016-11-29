package wap.vannahz.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

	public static Connection getConnection() throws Exception {
		
		DatabaseSetting stat_setting = Constant.databaseSettings;
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://" + stat_setting.database + "/" + stat_setting.schema + "?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true";
		Connection conn = DriverManager.getConnection(url, stat_setting.username, stat_setting.password);
		return conn;
	}
}
