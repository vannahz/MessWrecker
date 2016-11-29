package wap.vannahz.test;

import wap.vannahz.database.DBFunction;

public class databaseConnectionTest {

	public static void main(String[] args) {
		
		if (DBFunction.initDB()) {
			System.out.println("Connected");
		} else {
			System.out.println("Failed");
			return;
		}

	}

}
