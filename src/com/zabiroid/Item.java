package com.zabiroid;

import java.io.Serializable;

public class Item implements Serializable {
	private String itemId;
	private String itemDescription;
	private String itemKey;
	private String itemValue;
	private String itemValueType;
	private String itemUnits;
	private String itemLastClock;
	
	public Item() {
		itemId = "";
		itemDescription = "";
		itemKey = "";
		itemValue = "";
		itemUnits = "";
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
	
	public void setItemValueType(String itemvaluetype) {
		itemValueType = itemvaluetype;
	}
	
	public void setItemUnits(String itemunits) {
		itemUnits = itemunits;
	}
	
	public void setItemLastClock(String itemlastclock) {
		itemLastClock = itemlastclock;
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
	
	public String getItemValueType() {
		return itemValueType;
	}
	
	public String getItemUnits() {
		return itemUnits;
	}
	
	public String getItemLastClock() {
		return itemLastClock;
	}
	
}
