package com.zabiroid;

public class Trigger {
	
	private String triggerId;
	private String description;
	private String lastchange;
	private String value;
	
	public void setTriggerId(String triggerId) {
		this.triggerId = triggerId;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setLastchange(String lastchange) {
		this.lastchange = lastchange;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getTriggerId() {
		return this.triggerId;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getLastchange() {
		return this.lastchange;
	}
	
	public String getValue() {
		return this.value;
	}
	

}
