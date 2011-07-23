package com.zabiroid;

import java.util.ArrayList;

import com.zabiroid.R;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ItemListAdapter extends ArrayAdapter<Item>{

	private LayoutInflater inflater;
	private TextView itemView;
	private TextView valueView;

	public ItemListAdapter(Context context, ArrayList<Item> objects) {
		super(context, 0, objects);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (convertView == null) {
			view = inflater.inflate(R.layout.item, null);
		}
		
		Item item = this.getItem(position);
		
		if( item != null){
			//String itemid = item.getItemId();
			String itemdescription = item.getItemDescription();
			//Log.e("Description",itemdescription);
			String itemvalue = item.getItemValue();
			Log.e("Value",itemvalue);
			itemView = (TextView)view.findViewById(R.id.item);
			valueView = (TextView)view.findViewById(R.id.value);
			
			itemView.setText(itemdescription);
			if ( itemvalue != "") {
				valueView.setText(itemvalue);	
			}else {
				valueView.setText("No Data");
			}
			
			
		}
		return view;
	}

}
