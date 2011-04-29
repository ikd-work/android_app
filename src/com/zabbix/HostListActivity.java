package com.zabbix;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HostListActivity extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_list);
        setTitle(R.string.title_host_list);
        Bundle extras = getIntent().getExtras();
        
   
		if (extras != null) {
	//		TextView textView = (TextView)findViewById(R.id.Message);
	//		TextView passView = (TextView)findViewById(R.id.Password);
	//		textView.setText(extras.getCharSequence("AUTH_KEY"));
	//		passView.setText(extras.getCharSequence("PASS"));
			String host = extras.getCharSequence("HOSTNAME").toString();
			ZabbixApiAccess zabbix = new ZabbixApiAccess();
			zabbix.setHost(host);
			String uri = zabbix.makeUri(host);
			zabbix.setHttpPost(uri);
			//zabbix.setMethod("host.get");
			
			ArrayList<String> hostList = zabbix.getHostList(extras.getCharSequence("AUTH_KEY").toString(), "all");
			
		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.host, hostList);
		    
		    ListView list= (ListView)findViewById(R.id.hostlistview);
		    
		    list.setAdapter(adapter);
			Log.e("hostList",hostList.get(0).toString());
		}
    }

}
