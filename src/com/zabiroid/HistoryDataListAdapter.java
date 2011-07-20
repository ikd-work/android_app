package com.zabiroid;

import java.util.ArrayList;
import java.util.Date;

import com.zabiroid.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HistoryDataListAdapter extends ArrayAdapter<HistoryData>{
	private LayoutInflater inflater;
	private TextView timeView;
	private TextView valueView;

	public HistoryDataListAdapter(Context context, ArrayList<HistoryData> objects) {
		super(context, 0, objects);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (convertView == null) {
			view = inflater.inflate(R.layout.history, null);
		}
		
		HistoryData history = this.getItem(position);
		
		if( history != null){
			String time = history.getUnixtime();
			String value = history.getValue();
			
			Date date = new Date();
			date.setTime(Long.valueOf(time)*1000);
			timeView = (TextView)view.findViewById(R.id.history_time);
			valueView = (TextView)view.findViewById(R.id.history_value);
			
			timeView.setText(date.toLocaleString());
			valueView.setText(value);
			
		}
		return view;
	}

}
