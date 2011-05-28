package com.zabbix;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeRange {
	private String time_from;
	private String time_till;
	
	public void setTimeFrom(String time_from) {
		this.time_from = time_from;
	}
	
	public void setTimeTill(String time_till) {
		this.time_till = time_till;
	}
	
	public String getTimeFrom() {
		return this.time_from;
	}
	
	public String getTimeTill() {
		return this.time_till;
	}
	
	public void setTranslateDateToTimeFrom(Date date) {
		this.time_from = Long.toString(date.getTime()/1000);
	}
	
	public void setTranslateDateToTimeTill(Date date) {
		this.time_till = Long.toString(date.getTime()/1000);
	}
	
	public Date getTimeFromAtDateType() {
		Long time = Long.valueOf(this.time_from);
		time = time * 1000;
		Date date = new Date();
		date.setTime(time);
		return date;
	}
	
	public Date getTimeTillAtDateType() {
		Long time = Long.valueOf(this.time_till);
		time = time * 1000;
		Date date = new Date();
		date.setTime(time);
		return date;
	}
	
	public void setTimeFromBeforeHour(int time) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("JST"));
	    Date time_till_date = new Date();
	    time_till_date.setTime(Long.valueOf(this.time_till)*1000);
		cal.setTime(time_till_date);
	    cal.add(Calendar.HOUR, -time);
	    Date time_from_date = cal.getTime();
	    this.setTranslateDateToTimeFrom(time_from_date);
	}
}
