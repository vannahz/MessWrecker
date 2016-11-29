package wap.vannahz.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import wap.vannahz.entities.ExpiringGoods;
import wap.vannahz.entities.ExpiringTriggerCondition;

public class DBFunction {
	
	private static final Logger logger = Logger.getLogger(DBFunction.class);
	
	public static boolean initDB() {
		// Read database configuration and initialize database

		boolean init;
		try {
			DatabaseSetting databaseSettings = new DatabaseSetting();
			DatabaseConfigureReader.DatabaseConfigure DatabaseConfigure = DatabaseConfigureReader.ReadXml();

			databaseSettings.database = DatabaseConfigure.database;
			databaseSettings.username = DatabaseConfigure.username;
			databaseSettings.password = DatabaseConfigure.password;
			databaseSettings.schema = DatabaseConfigure.schema;
			
			Constant.databaseSettings = databaseSettings;
			init = true;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			init = false;
		}
		return init;
	}

//	
//	public static ExpiringTriggerCondition getExpiringTriggerCondition(){
//		
//		
//		ExpiringTriggerCondition tc = new ExpiringTriggerCondition();
//		
//		try {
//			Connection conn = DBConnection.getConnection();
//			String query="SELECT trigger_condition FROM notification where notify_type = 'expire';";
//			PreparedStatement pstmt = conn.prepareStatement(query);
//			pstmt = conn.prepareStatement(query);
//			ResultSet rs = pstmt.executeQuery();	
//				
//			String jsonStr= "";
//			while (rs.next()) {
//				jsonStr = rs.getString(1);
//			}
//			JSONObject obj = new JSONObject(jsonStr);		
//			tc.setSchedule( obj.getString("schedule") );
//			tc.setSchedule_time(obj.getString("schedule_time"));
//			tc.setSend_to( obj.getString("send_to") );
//			tc.setTg_percentage( obj.getDouble("tg_percentage") );
//			tc.setTg_days( obj.getInt("tg_days") );
//			
//			rs.close();
//			pstmt.close();
//			conn.close();
//		} catch (Exception Exception) {
//			Exception.printStackTrace();
//			logger.error("Fail to update schedule info from database:" + Exception);
//			return null;
//		}
//		
//		
//		if(tc.getSchedule() == null) tc.setSchedule("daily");
//		if(tc.getSchedule_time() != null) tc.setSchedule_time("13:14");
//		if(tc.getSend_to() == null) tc.setSend_to("waner0120@gmail.com");
//		if(tc.getTg_percentage() == 0) tc.setTg_percentage(0.1);
//		if(tc.getTg_days() == 0) tc.setTg_days(30);
//		
//		return tc;
//		
//	}
	
	
	
	public static ExpiringTriggerCondition getExpiringTriggerCondition2(){
		
		ExpiringTriggerCondition tc = new ExpiringTriggerCondition();
		
		try {
			Connection conn = DBConnection.getConnection();
			String query="SELECT no_schedule, no_schedule_time, no_send_to, no_tg_percentage, no_tg_days FROM worksap.notify where no_type='expire';";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();	
				
			while (rs.next()) {
				
				tc.setSchedule( rs.getString(1) );
				tc.setSchedule_time( rs.getString(2) );
				tc.setSend_to( rs.getString(3) );
				tc.setTg_percentage( rs.getDouble(4) );
				tc.setTg_days( rs.getInt(5) );
				
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception Exception) {
			Exception.printStackTrace();
			logger.error("Fail to update schedule info from database:" + Exception);
			return null;
		}
		
		
		if(tc.getSchedule() == null) tc.setSchedule("daily");
		if(tc.getSchedule_time() == null) tc.setSchedule_time("15:30");
		if(tc.getSend_to() == null) tc.setSend_to("waner0120@gmail.com");
		if(tc.getTg_percentage() == 0) tc.setTg_percentage(0.1);
		if(tc.getTg_days() == 0) tc.setTg_days(30);
		
		return tc;
		
	}
	
	
	
	
	public static boolean setExpiringTriggerCondition(ExpiringTriggerCondition etc){
		
		boolean isSuccess = false;
		
		try {
			Connection conn = DBConnection.getConnection();
			
			String query = "UPDATE notify "
					+ "SET no_schedule=?,no_schedule_time=?,no_send_to=?,no_tg_percentage=?,no_tg_days=? "
					+ "WHERE no_type='expire';";
			
			PreparedStatement pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, etc.getSchedule());
			pstmt.setString(2, etc.getSchedule_time());
			pstmt.setString(3, etc.getSend_to());
			pstmt.setDouble(4, etc.getTg_percentage());
			pstmt.setInt(5, etc.getTg_days());
			
			pstmt.executeUpdate();
			isSuccess = true;
			pstmt.close();
			conn.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		}
		return isSuccess;
	}
	
	
	public static List<ExpiringGoods> searchExpiringGoods(double percentage, int leftdays, Date date){
	
		List<ExpiringGoods> eglist = new ArrayList();
		
		try {
			Connection conn = DBConnection.getConnection();
			
			String query = "SELECT "
					+ "product_mst_id,"
					+ "product_type,"
					+ "product_name,"
					+ "production_date,"
					+ "expiration_date,"
					+ "amount,"
					+ "DATEDIFF(expiration_date, NOW()) AS left_days "
				  + "FROM worksap.product_mst "
				  + "WHERE "
				  	+ "DATEDIFF(expiration_date, NOW()) < ceil(DATEDIFF(expiration_date, production_date) * ?) "
				  	+ "and Datediff(expiration_date, NOW()) < ?";
			
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setDouble(1, percentage);
			pstmt.setInt(2, leftdays);
			ResultSet rs = pstmt.executeQuery();	
			while (rs.next()) {
				
				ExpiringGoods eg = new ExpiringGoods();
				
				eg.setProdect_id(rs.getInt(1));
				eg.setProduct_type(rs.getString(2));
				eg.setProduct_name(rs.getString(3));
				eg.setProduction_date(rs.getDate(4));
				eg.setExpiration_date(rs.getDate(5));
				eg.setTotal_amount(rs.getInt(6));
				eg.setLeft_days(rs.getInt(7));
				eg.setRecord_time(date);
				
				eglist.add(eg);
			}
			
			rs.close();
			pstmt.close();
			conn.close();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
			return null;
		}
		
		return eglist;
	}
	
	
	public static boolean storeExpiringGoods(List<ExpiringGoods> eglist){
		
		boolean isSuccess = false;
		for(int i=0; i<eglist.size(); i++){
		
			isSuccess = false;
			ExpiringGoods eg = eglist.get(i);
			
			try {
				Connection conn = DBConnection.getConnection();
				
				String query = "INSERT INTO "
						+ "expiringgoods(eg_product_id, eg_product_type, eg_product_name, eg_production_date, eg_expiration_date, eg_amount, eg_leftdays, record_time) "
						+ "VALUES (?,?,?,?,?,?,?,?)";
				
				PreparedStatement pstmt = conn.prepareStatement(query);
				pstmt.setInt(1, eg.getProdect_id());
				pstmt.setString(2, eg.getProduct_type());
				pstmt.setString(3, eg.getProduct_name());
				pstmt.setDate(4, new java.sql.Date(eg.getProduction_date().getTime()));
				pstmt.setDate(5, new java.sql.Date(eg.getExpiration_date().getTime()));
				pstmt.setInt(6, eg.getTotal_amount());
				pstmt.setInt(7, eg.getLeft_days());
				pstmt.setDate(8, new java.sql.Date(eg.getRecord_time().getTime()));
				pstmt.executeUpdate();
				isSuccess = true;
				pstmt.close();
				conn.close();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e);
				return isSuccess;
			}	
		}
		
		return isSuccess;
	}
	
}
