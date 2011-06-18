package com.zabbix;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HostListActivity extends Activity {
	
	ZabbixApiAccess zabbix;
	ArrayList<Host> hostList;
	String authToken;
	String uri;
	ListView list;
	HostListAdapter adapter;
	
	private static final String PREFERENCE_KEY = "AuthData";
	SharedPreferences authData;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_list);
        setTitle(R.string.title_host_list);
        
        authData = getSharedPreferences(PREFERENCE_KEY, MODE_PRIVATE);
        authToken = authData.getString("AuthToken", "No Data");
        uri = authData.getString("URI", "No Data");
    
		zabbix = new ZabbixApiAccess();
		zabbix.setHttpPost(uri);
		hostList = zabbix.getHostList(authToken, "all");
		
		if( hostList != null) {
			adapter = new HostListAdapter(this, hostList);		    
			list= (ListView)findViewById(R.id.hostlistview);
		    
			list.setAdapter(adapter);
		
			list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					ListView list = (ListView) parent;
					Host host = (Host) list.getItemAtPosition(position);
					Intent intent = new Intent(HostListActivity.this,HostDetailActivity.class);
					intent.putExtra("hostid", host.getHostId());
					intent.putExtra("hostname", host.getHostName());
					intent.putExtra("hoststatus", host.getHostStatus());
					intent.putExtra("hostdns", host.getHostDns());
					intent.putExtra("hostip", host.getHostIp());
					startActivity(intent);
				}
			});
		}
    }
    
    
    public boolean onCreateOptionsMenu(Menu menu){
    	boolean ret = super.onCreateOptionsMenu(menu);
    	
    	menu.add(0, Menu.FIRST, Menu.NONE,R.string.logout);
    	menu.add(0, Menu.FIRST+1, Menu.NONE,R.string.refresh);
    	menu.add(0, Menu.FIRST+2, Menu.NONE,R.string.exit);
    	
    	return ret;
    }
    
    public boolean onOptionsItemSelected(MenuItem item){
    	if( item.getItemId() == 1){
    		authData = getSharedPreferences(PREFERENCE_KEY, Activity.MODE_APPEND);
    		authData.edit().clear().commit();
    		
    		Intent intent = new Intent(HostListActivity.this,LoginActivity.class);
    		startActivity(intent);
    	}else if( item.getItemId() == 2) {
    		hostList = zabbix.getHostList(authToken, "all");
    		adapter = new HostListAdapter(this, hostList);
    		list.setAdapter(adapter);
    	}else if( item.getItemId() == 3) {
    		finish();
    	}
    	
    	return super.onOptionsItemSelected(item);
    }
    

}
