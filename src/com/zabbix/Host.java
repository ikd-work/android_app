package com.zabbix;

public class Host {
	private String hostId;
	private String hostName;
	private String hostStatus;
	private String hostDns;
	private String hostIp;
	
	public Host(){
		hostId = "";
		hostName = "";
		hostStatus = "";
		hostDns = "";
		hostIp = "";
	}
	
	public void setHostId(String hostid){
		hostId = hostid;
	}
	
	public void setHostName(String hostname){
		hostName = hostname;
	}
	
	public void setHostStatus(String hoststatus){
		hostStatus = hoststatus;
	}

	public void setHostDns(String hostdns){
		hostDns = hostdns;
	}

	public void setHostIp(String hostip){
		hostIp = hostip;
	}

	
	public String getHostId(){
		return hostId;
	}
	
	public String getHostName(){
		return hostName;
	}
	
	public String getHostStatus(){
		return hostStatus;
	}

	public String getHostDns(){
		return hostDns;
	}

	public String getHostIp(){
		return hostIp;
	}

}
