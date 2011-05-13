package com.zabbix;

public class Host {
	private String hostId;
	private String hostName;
	
	public Host(){
		hostId = "";
		hostName = "";
	}
	
	public void setHostId(String hostid){
		hostId = hostid;
	}
	
	public void setHostName(String hostname){
		hostName = hostname;
	}
	
	public String getHostId(){
		return hostId;
	}
	
	public String getHostName(){
		return hostName;
	}
}
