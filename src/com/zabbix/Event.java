package com.zabbix;

public class Event {
	
	private String eventId;
	private String objectId;
	private String clock;
	private String value;
	
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	
	public void setClock(String clock) {
		this.clock = clock;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getEventId() {
		return this.eventId;
	}
	
	public String getObjectId() {
		return this.objectId;
	}
	
	public String getClock() {
		return this.clock;
	}
	
	public String getValue() {
		return this.value;
	}
	

}
