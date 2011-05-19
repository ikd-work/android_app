package com.zabbix;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HostListActivity extends Activity {
	
	private static final String PREFERENCE_KEY = "AuthData";
	SharedPreferences authData;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_list);
        setTitle(R.string.title_host_list);
        
        authData = getSharedPreferences(PREFERENCE_KEY, MODE_PRIVATE);
        String authToken = authData.getString("AuthToken", "No Data");
        String uri = authData.getString("URI", "No Data");
    
		ZabbixApiAccess zabbix = new ZabbixApiAccess();
		zabbix.setHttpPost(uri);
		ArrayList<Host> hostList = zabbix.getHostList(authToken, "all");
			
		HostListAdapter adapter = new HostListAdapter(this, hostList);		    
		ListView list= (ListView)findViewById(R.id.hostlistview);
		    
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView list = (ListView) parent;
				Host host = (Host) list.getItemAtPosition(position);
				Intent intent = new Intent(HostListActivity.this,HostDetailActivity.class);
				intent.putExtra("hostid", host.getHostId());
				startActivity(intent);
			}
		});
    }
    
    public boolean onCreateOptionsMenu(Menu menu){
    	boolean ret = super.onCreateOptionsMenu(menu);
    	
    	menu.add(0, Menu.FIRST, Menu.NONE,R.string.logout);
    	
    	return ret;
    }
    
    public boolean onOptionsItemSelected(MenuItem item){
    	if( item.getItemId() == 1){
    		authData = getSharedPreferences(PREFERENCE_KEY, Activity.MODE_APPEND);
    		authData.edit().clear().commit();
    		
    		Intent intent = new Intent(HostListActivity.this,LoginActivity.class);
    		startActivity(intent);
    	}
    	
    	return super.onOptionsItemSelected(item);
    }
    

}
