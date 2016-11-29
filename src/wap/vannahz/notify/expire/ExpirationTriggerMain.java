package wap.vannahz.notify.expire;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import wap.vannahz.database.Constant;
import wap.vannahz.database.DBFunction;
import wap.vannahz.entities.ExpiringTriggerCondition;

public class ExpirationTriggerMain {
	
	public String schedule = null;
	public String schedule_time = null;
	public Timer scheduleMonitor;
	public Timer scheduleSender;
	public static String send_to;
	public static double percentage;
	public static int remains;
	public static int refreshInterval = 30000;
	private static final Logger logger = Logger.getLogger(ExpirationTriggerMain.class);
	
	public void start(){
		if (DBFunction.initDB()) {
			logger.info("Connected to host name: " + Constant.databaseSettings.database);
		} else {
			logger.error("Fail to initialize database.");
			return;
		}
		
		// Start Thread
		scheduleMonitor = new Timer();
		scheduleMonitor.scheduleAtFixedRate(new ScheduleMonitorRunnable(), new Date(), refreshInterval);
	}
	
	public void refreshReportTimerPolicy(){
		// schedule next running time for Timer
		if (schedule == null || schedule_time == null) {
			return;
		}
		
		if (schedule.equals("NoSchedule")) {	// No Schedule
			if (scheduleSender != null) scheduleSender.cancel();	
			logger.info("Report Timer Scheduled to:  No Schedule.");
		} else {	// Daily & Weekly
			Date nextTime = findNextOfThatDay(schedule_time, schedule);
			if (scheduleSender != null) {
				scheduleSender.cancel();
			}
			scheduleSender = null;
			scheduleSender = new Timer();
			scheduleSender.schedule(new ScheduleSenderRunnable(), nextTime);
			logger.info("Report Timer Scheduled to: " + nextTime.toString());
		}
	}
	
	public Date findNextOfThatDay(String time, String schedule){
		// calculate next running time
		
		int hour = Integer.valueOf(time.split(":")[0]);
		int minute = Integer.valueOf(time.split(":")[1]);
		String weekDay;
		if (schedule.equals("daily") || schedule.equals("now")) {
			weekDay = null;
		} else {
			weekDay = schedule.toUpperCase();
		}
		
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 0);
		if (weekDay != null) {
			switch (weekDay) {
			case "MONDAY":
				cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				break;
			case "TUESDAY":
				cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
				break;
			case "WEDNESDAY":
				cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
				break;
			case "THURSDAY":
				cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
				break;
			case "FRIDAY":
				cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
				break;
			case "SATURDAY":
				cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
				break;
			case "SUNDAY":
				cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				break;
			}
		}
		
		Calendar now = new GregorianCalendar();
		if (now.compareTo(cal) >= 0) {
			if (weekDay == null) {
				cal.add(Calendar.DAY_OF_YEAR, 1);
			} else {
				cal.add(Calendar.DAY_OF_YEAR, 7);
			}
		}
		
		return cal.getTime();
	}
	
	
	public class ScheduleMonitorRunnable extends TimerTask{

		@Override
		public void run() {
			
			ExpiringTriggerCondition etc = DBFunction.getExpiringTriggerCondition2();
			schedule = etc.getSchedule();
			schedule_time = etc.getSchedule_time();
			send_to = etc.getSend_to();
			percentage = etc.getTg_percentage();
			remains = etc.getTg_days();
			refreshReportTimerPolicy();
		}
	}
	
	public class ScheduleSenderRunnable extends TimerTask {
		@Override
		public void run() {
			
			new ExpirationTrigger().sendEmail(send_to, percentage, remains);
			refreshReportTimerPolicy();
		}
	}
	
	
	public static void main(String[] args) {
		BasicConfigurator.configure();
		
		if(args.length>0 && args[0].equals("sendnow")){
			new ExpirationTrigger().sendEmail(send_to, percentage, remains);
		}else{
			new ExpirationTriggerMain().start();
		}
	}
}
