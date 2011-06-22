package com.zabbix;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class TriggerActivity extends Activity {
	
	ZabbixApiAccess zabbix;
	ArrayList<Trigger> eventList;
	String authToken;
	String uri;
	ListView list;
	TriggerListAdapter adapter;
	Intent intent;
	String hostId;
	String hostName;
	
	private static final String PREFERENCE_KEY = "AuthData";
	SharedPreferences authData;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trigger_list);
        setTitle(R.string.title_trigger_list);
        
        authData = getSharedPreferences(PREFERENCE_KEY, MODE_PRIVATE);
        authToken = authData.getString("AuthToken", "No Data");
        uri = authData.getString("URI", "No Data");
    
        intent = getIntent();
        hostId = intent.getStringExtra("hostid");
        hostName = intent.getStringExtra("hostname");
        
        
		zabbix = new ZabbixApiAccess();
		zabbix.setHttpPost(uri);
		eventList = zabbix.getTriggerList(authToken, hostId, 20);
		
		if( eventList != null) {
			adapter = new TriggerListAdapter(this, eventList);		    
			list= (ListView)findViewById(R.id.triggerlistview);
		    
			list.setAdapter(adapter);
		
		}
    }
}
