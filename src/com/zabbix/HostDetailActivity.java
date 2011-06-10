package com.zabbix;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
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
        final String hostID = intent.getStringExtra("hostid");
        final String hostName = intent.getStringExtra("hostname");
        String hostStatus = intent.getStringExtra("hoststatus");
        String hostDns = intent.getStringExtra("hostdns");
        String hostIp = intent.getStringExtra("hostip");

        
        authData = getSharedPreferences(PREFERENCE_KEY, MODE_PRIVATE);
        final String authToken = authData.getString("AuthToken", "No Data");
        String uri = authData.getString("URI", "No Data");
        
        final ZabbixApiAccess zabbix = new ZabbixApiAccess();
		zabbix.setHttpPost(uri);
		final ArrayList<String> itemIdList = zabbix.getItemIdList(authToken, hostID);
		final ArrayList<Item> itemList = zabbix.getItemList(authToken, hostID, itemIdList, 20);
        Log.e("itemIdList",itemIdList.toString());
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
			final ListView list= (ListView)findViewById(R.id.itemlistview);
		    list.addFooterView(getFooter());
			list.setAdapter(adapter);
			list.setOnScrollListener(new OnScrollListener() {
				public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
					Log.e("totalItemCount",Integer.toString(totalItemCount));
					if( totalItemCount < itemIdList.size()) {
						ArrayList<Item> itemList = zabbix.getItemList(authToken, hostID, itemIdList, 20);
						list.invalidateViews();
					}
				}
				public void onScrollStateChanged(AbsListView view, int arg1){
					
				}
			});
			list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					ListView list = (ListView) parent;
					Item item = (Item) list.getItemAtPosition(position);
					Intent intent = new Intent(HostDetailActivity.this,MonitorActivity.class);
					intent.putExtra("item", item);
					intent.putExtra("itemdescription", item.getItemDescription());
					intent.putExtra("hostName", hostName);
					startActivity(intent);
				}
			});
		}
        
    }
    
    private View getFooter() {
    	View mFooter = null;
		if (mFooter == null) {
    		mFooter = getLayoutInflater().inflate(R.layout.host_detail_footer, null);
    	}
		return mFooter;
    }
    
    

}
