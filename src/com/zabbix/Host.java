package com.zabbix;

public class Host {
	private String hostId;
	private String hostName;
	private String hostStatus;
	private String hostDns;
	private String hostIp;
	private int errorNum;
	
	public Host(){
		hostId = "";
		hostName = "";
		hostStatus = "";
		hostDns = "";
		hostIp = "";
		errorNum = 0;
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

	public void setErrorNum(int errornum){
		errorNum = errornum;
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
	
	public int getErrorNum(){
		return errorNum;
	}

}
