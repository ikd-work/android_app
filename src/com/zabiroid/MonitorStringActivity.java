package com.zabiroid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import com.zabiroid.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

public class MonitorStringActivity extends Activity {
	private static final String PREFERENCE_KEY = "AuthData";
	SharedPreferences authData;
	TimeRange timerange = new TimeRange();

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_list);
        setTitle(R.string.title_monitor_result);

        Intent intent = getIntent();
        Item item = (Item)intent.getSerializableExtra("item");
        
        String itemdescription = intent.getStringExtra("itemdescription");
        String hostName = intent.getStringExtra("hostName");
        
        authData = getSharedPreferences(PREFERENCE_KEY, MODE_PRIVATE);
        String authToken = authData.getString("AuthToken", "No Data");
        String uri = authData.getString("URI", "No Data");

        //timerange作成
        Date now = new Date();
        timerange.setTranslateDateToTimeTill(now);
        timerange.setTimeFromBeforeHour(1);
        
        ZabbixApiAccess zabbix = new ZabbixApiAccess(uri,authToken);
		ArrayList<HistoryData> historyDataList = null;
		try {
			historyDataList = zabbix.getHistoryData(item, timerange);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			Toast.makeText(this,"接続エラー",Toast.LENGTH_LONG).show();
		} 
     
		ListView list = (ListView)findViewById(R.id.historylistview);
		HistoryDataListAdapter adapter = new HistoryDataListAdapter(this,historyDataList);
		list.setAdapter(adapter);
	}

}
