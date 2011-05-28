package com.zabbix;

public class Item {
	private String itemId;
	private String itemDescription;
	private String itemKey;
	private String itemValue;
	
	public Item() {
		itemId = "";
		itemDescription = "";
		itemKey = "";
		itemValue = "";
	}
	
	public void setItemId(String itemid) {
		itemId = itemid;
	}
	
	public void setItemDescription(String itemdescription) {
		itemDescription = itemdescription;
	}
	
	public void setItemKey(String itemkey) {
		itemKey = itemkey;
	}
	
	public void setItemValue(String itemvalue) {
		itemValue = itemvalue;
	}
	
	public String getItemId() {
		return itemId;
	}
	
	public String getItemDescription() {
		return itemDescription;
	}
	
	public String getItemKey() {
		return itemKey;
	}
	
	public String getItemValue() {
		return itemValue;
	}
	
}
