package com.zabbix;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
        
        TextView textView = (TextView)this.findViewById(R.id.host_detail);
        textView.setText(hostID);
        authData = getSharedPreferences(PREFERENCE_KEY, MODE_PRIVATE);
        String authToken = authData.getString("AuthToken", "No Data");
        String uri = authData.getString("URI", "No Data");
        
    }

}
