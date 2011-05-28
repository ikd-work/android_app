package com.zabbix;

public class HistoryData {
	private String unixtime;
	private String value;
	
	public void setUnixtime(String unixtime) {
		this.unixtime = unixtime;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getUnixtime() {
		return this.unixtime;
	}
	
	public String getValue() {
		return this.value;
	}

}
