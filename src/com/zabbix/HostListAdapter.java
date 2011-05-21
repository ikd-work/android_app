package com.zabbix;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HostListAdapter extends ArrayAdapter<Host>{

	private LayoutInflater inflater;
	private TextView hostIdView;
	private TextView hostNameView;

	public HostListAdapter(Context context, ArrayList<Host> objects) {
		super(context, 0, objects);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (convertView == null) {
			view = inflater.inflate(R.layout.host, null);
		}
		
		Host host = this.getItem(position);
		
		if( host != null){
			String hostid = host.getHostId();
			String hostname = host.getHostName();
			String hoststatus = host.getHostStatus();
			hostIdView = (TextView)view.findViewById(R.id.host_id);
			hostNameView = (TextView)view.findViewById(R.id.host_name);
			
			if (hoststatus.equals("1")) {
				hostIdView.setTextColor(Color.RED);
				hostNameView.setTextColor(Color.RED);
			}
			hostIdView.setText(hostid);
			hostNameView.setText(hostname);
			
		}
		return view;
	}
}
