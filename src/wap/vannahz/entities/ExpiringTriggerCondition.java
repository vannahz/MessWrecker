package wap.vannahz.entities;

public class ExpiringTriggerCondition {
	
	String send_to;
	String schedule_time;
	String schedule;
	double tg_percentage;
	int tg_days;
	

	public ExpiringTriggerCondition(){	
	}


	public String getSend_to() {
		return send_to;
	}


	public void setSend_to(String send_to) {
		this.send_to = send_to;
	}

	public String getSchedule_time() {
		return schedule_time;
	}


	public void setSchedule_time(String schedule_time) {
		this.schedule_time = schedule_time;
	}


	public String getSchedule() {
		return schedule;
	}


	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}


	public double getTg_percentage() {
		return tg_percentage;
	}


	public void setTg_percentage(double tg_percentage) {
		this.tg_percentage = tg_percentage;
	}


	public int getTg_days() {
		return tg_days;
	}


	public void setTg_days(int tg_days) {
		this.tg_days = tg_days;
	}
	
}
