package com.zabiroid;

import java.io.IOException;
import java.util.ArrayList;

import com.zabiroid.R;

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
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    
		zabbix = new ZabbixApiAccess(uri,authToken);
		try {
			hostList = zabbix.getHostList("all");
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
			} else {
				TextView nodata_view = new TextView(this);
				nodata_view.setText("登録ホストはありません");
				setContentView(nodata_view,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			}
		
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			Toast.makeText(HostListActivity.this,"接続エラー",Toast.LENGTH_LONG).show();
			TextView error_view = new TextView(this);
			error_view.setText("ホストリストの取得に失敗しました");
			setContentView(error_view,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
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
    		try {
				hostList = zabbix.getHostList("all");
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				Toast.makeText(HostListActivity.this,"接続エラー",Toast.LENGTH_LONG).show();
				TextView error_view = new TextView(this);
				error_view.setText("ホストリストの取得に失敗しました");
				setContentView(error_view,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			}
    		adapter = new HostListAdapter(this, hostList);
    		list.setAdapter(adapter);
    	}else if( item.getItemId() == 3) {
    		finish();
    	}
    	
    	return super.onOptionsItemSelected(item);
    }
    

}
