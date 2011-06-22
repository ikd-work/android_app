package com.zabbix;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TriggerListAdapter extends ArrayAdapter<Trigger>{

	private LayoutInflater inflater;
	private TextView triggerIdView;
	private TextView descriptionView;

	public TriggerListAdapter(Context context, ArrayList<Trigger> objects) {
		super(context, 0, objects);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (convertView == null) {
			view = inflater.inflate(R.layout.trigger, null);
		}
		
		Trigger trigger = this.getItem(position);
		
		if( trigger != null){
			String triggerid = trigger.getTriggerId();
			String description = trigger.getDescription();
			String lastchange = trigger.getLastchange();
			String value = trigger.getValue();
			triggerIdView = (TextView)view.findViewById(R.id.triggerid);
			descriptionView = (TextView)view.findViewById(R.id.description);
			
			triggerIdView.setText(triggerid);
			descriptionView.setText(description);
		}
		return view;
	}

}
