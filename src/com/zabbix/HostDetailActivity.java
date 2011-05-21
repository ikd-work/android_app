package com.zabbix;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

public class HostDetailActivity extends Activity{
	private static final String PREFERENCE_KEY = "AuthData";
	SharedPreferences authData;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_detail);
        setTitle(R.string.title_host_detail);
        
        Intent intent = getIntent();
        String hostID = intent.getStringExtra("hostid");
        String hostName = intent.getStringExtra("hostname");
        String hostStatus = intent.getStringExtra("hoststatus");
        String hostDns = intent.getStringExtra("hostdns");
        String hostIp = intent.getStringExtra("hostip");

        
        authData = getSharedPreferences(PREFERENCE_KEY, MODE_PRIVATE);
        String authToken = authData.getString("AuthToken", "No Data");
        String uri = authData.getString("URI", "No Data");
        
        ZabbixApiAccess zabbix = new ZabbixApiAccess();
		zabbix.setHttpPost(uri);
		ArrayList<Item> itemList = zabbix.getItemList(authToken, hostID, 10);
              
        TextView textViewHostId = (TextView)this.findViewById(R.id.host_detail_id);
        TextView textViewHostName = (TextView)this.findViewById(R.id.host_detail_name);
        TextView textViewHostStatus = (TextView)this.findViewById(R.id.host_detail_status);
        TextView textViewHostDns = (TextView)this.findViewById(R.id.host_detail_dns);
        TextView textViewHostIp = (TextView)this.findViewById(R.id.host_detail_ip);
        textViewHostId.setText(hostID);
        textViewHostName.setText(hostName);
        
        if (hostStatus.equals("1")) {
        	textViewHostStatus.setText("–³Œø");
        	textViewHostStatus.setTextColor(Color.RED);
        	TextView textViewTitle = (TextView)this.findViewById(R.id.host_detail_status_title);
        	textViewTitle.setTextColor(Color.RED);
        }
        else {
        	textViewHostStatus.setText("—LŒø");
        }
        
        textViewHostDns.setText(hostDns);
        textViewHostIp.setText(hostIp);
      
		if( itemList != null) {
			ItemListAdapter adapter = new ItemListAdapter(this, itemList);		    
			ListView list= (ListView)findViewById(R.id.itemlistview);
		    
			list.setAdapter(adapter);
		}
        
    }

}
