package com.zabiroid;

import java.util.ArrayList;

import com.zabiroid.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class HostDetailActivity extends Activity{
	private static final String PREFERENCE_KEY = "AuthData";
	SharedPreferences authData;
	View mFooter;
	String authToken;
	ListView list;
	String hostID;
	String hostName;
	ArrayList<String> itemIdList;
	ArrayList<Item> itemList;
	ZabbixApiAccess zabbix;
	AsyncTask<Void, Void, String> mTask = null;
	Intent intent;
	ItemListAdapter adapter;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_detail);
        setTitle(R.string.title_host_detail);
        
        intent = getIntent();
        hostID = intent.getStringExtra("hostid");
        hostName = intent.getStringExtra("hostname");
        String hostStatus = intent.getStringExtra("hoststatus");
        String hostDns = intent.getStringExtra("hostdns");
        String hostIp = intent.getStringExtra("hostip");

        
        authData = getSharedPreferences(PREFERENCE_KEY, MODE_PRIVATE);
        authToken = authData.getString("AuthToken", "No Data");
        String uri = authData.getString("URI", "No Data");
        
        zabbix = new ZabbixApiAccess(uri,authToken);
		itemIdList = zabbix.getItemIdList(hostID);
		
		if ( itemIdList.size() != 0) {
			itemList = zabbix.getItemList(hostID, itemIdList, 20);
		}
		
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
        
      
		if( itemList != null && itemIdList.size() != 0) {
			adapter = new ItemListAdapter(this, itemList);		    
			list= (ListView)findViewById(R.id.itemlistview);
		    list.addFooterView(getFooter());
			list.setAdapter(adapter);
			list.setOnScrollListener(new OnScrollListener() {
				public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
					if(totalItemCount == firstVisibleItem + visibleItemCount) {
					
						if( totalItemCount < itemIdList.size()) {
							if (mTask == null || mTask.getStatus() != AsyncTask.Status.RUNNING) {
								mTask = new AsyncItemGetTask().execute();
							}
						}
						else {
							list.removeFooterView(getFooter());
						}
						
					}
					
				}
				public void onScrollStateChanged(AbsListView view, int arg1){
					
				}
			});
			list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					ListView list = (ListView) parent;
					Item item = (Item) list.getItemAtPosition(position);
					Intent intent;
					if(item.getItemValueType().equals("3") | item.getItemValueType().equals("0") ) {
						intent = new Intent(HostDetailActivity.this,MonitorActivity.class);
					}else {
						intent = new Intent(HostDetailActivity.this,MonitorStringActivity.class);
					}
					intent.putExtra("item", item);
					intent.putExtra("itemdescription", item.getItemDescription());
					intent.putExtra("itemunits",item.getItemUnits());
					intent.putExtra("hostName", hostName);
					startActivity(intent);
				}
			});
		}
        
    }
    
    private View getFooter() {
    
		if (mFooter == null) {
    		mFooter = getLayoutInflater().inflate(R.layout.host_detail_footer, null);
    	}
		return mFooter;
    }
    
    class AsyncItemGetTask extends AsyncTask<Void,Void,String>{
    	@Override
    	protected void onPreExecute() {
    		
    	}
    	@Override
		protected String doInBackground(Void... arg0) {
    		itemList = zabbix.getItemList(hostID, itemIdList, 20);
			return null;
		}
    	@Override
    	protected void onPostExecute(String result) {
    		list.invalidateViews();
    		
    	}
		
    }
    
    public boolean onCreateOptionsMenu(Menu menu){
    	boolean ret = super.onCreateOptionsMenu(menu);
    	
    	menu.add(0, Menu.FIRST, Menu.NONE,R.string.logout);
    	menu.add(0, Menu.FIRST+1, Menu.NONE,R.string.refresh);
    	menu.add(0, Menu.FIRST+2, Menu.NONE,R.string.hostlist);
    	
    	return ret;
    }
    
    public boolean onOptionsItemSelected(MenuItem item){
    	if( item.getItemId() == 1){
    		authData = getSharedPreferences(PREFERENCE_KEY, Activity.MODE_APPEND);
    		authData.edit().clear().commit();
    		
    		Intent intent = new Intent(HostDetailActivity.this,LoginActivity.class);
    		startActivity(intent);
    	}else if( item.getItemId() == 2) {
    		//startActivity(intent);
    		
    		zabbix.clearItemListCount();
    		itemIdList = zabbix.getItemIdList(hostID);
    		
    		if ( itemIdList.size() != 0) {
    			itemList = zabbix.getItemList(hostID, itemIdList, 20);
    		}
    		adapter = new ItemListAdapter(this, itemList);
    		list.setAdapter(adapter);
    	}else if( item.getItemId() == 3) {
    		finish();
    	}
    	
    	return super.onOptionsItemSelected(item);
    }
    
}
