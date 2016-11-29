package wap.vannahz.commandline;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import wap.vannahz.database.DBFunction;
import wap.vannahz.entities.ExpiringTriggerCondition;

public class ExpireCommandLine {
	
	
	static SimpleDateFormat df = new SimpleDateFormat("HH:mm");

	public static void main(String[] args) {
		
		ExpiringTriggerCondition tc = new ExpiringTriggerCondition();
		
		System.out.println("# Welcome to set your own trigger condition #");
		
		Scanner sc = new Scanner(System.in);  
		
		System.out.println("1. Please type-in your targets (email address)");
		
		String send_to = sc.nextLine();
		
		while(!send_to.contains("@")){
			System.out.println("Invalid email address, Please type again.");
			send_to = sc.nextLine();
		}
		
		tc.setSend_to(send_to);
		
		
		System.out.println("2. Please type-in your trigger in percentage");
		
		tc.setTg_percentage(sc.nextDouble());
		
		System.out.println("3. Please type-in your trigger as left days");
		
		tc.setTg_days(sc.nextInt());
		
		System.out.println("4. Please type-in your schedule (now, daily, weekly)");
		
		String schedule = sc.next();
		
		while(!schedule.equals("now") && !schedule.equals("daily") && !schedule.equals("weekly")){
			
			System.out.println("Invalid schedule, Please type-in now, daily or weekly");
			schedule = sc.next();
			
		}
		tc.setSchedule(schedule);
		
		
		if(schedule.equals("now")){
			
			Date da = new Date();
			da.setMinutes(da.getMinutes() + 1);
			String now = df.format(da);
			tc.setSchedule_time(now);
			tc.setSchedule("daily");
			
			System.out.println("5. You will receive the report winthin 5 mins.");
			
			System.out.println(" ( Schedule time is set to 'daily' on " + now + " as default. ) ");
		}else{
		
			System.out.println("5. Please type-in your alert time (hh:mm)");
			
			
			String time = sc.next();
			
			while(!time.contains(":")){
				
				System.out.println("Invalid Schedule Time. Please type-in again.");
				
				time = sc.next();
			}
			tc.setSchedule_time(time);
		}
		
		if(DBFunction.initDB()){

			if(DBFunction.setExpiringTriggerCondition(tc)){
				System.out.println("You have set successfully.");
			}else{
				System.out.println("Failed to set.");
			}
			
		}else{
			System.out.println("Failed to connect to database");
		}
	}
}
