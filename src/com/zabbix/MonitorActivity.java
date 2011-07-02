package com.zabbix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.ChartUtilities;
import org.afree.chart.axis.DateAxis;
import org.afree.chart.axis.NumberAxis;
import org.afree.chart.plot.PiePlot;
import org.afree.chart.plot.Plot;
import org.afree.chart.plot.PlotOrientation;
import org.afree.chart.plot.XYPlot;
import org.afree.chart.title.TextTitle;
import org.afree.data.category.CategoryDataset;
import org.afree.data.general.DefaultPieDataset;
import org.afree.data.time.RegularTimePeriod;
import org.afree.data.time.Second;
import org.afree.data.time.TimePeriod;
import org.afree.data.time.TimeSeries;
import org.afree.data.time.TimeSeriesCollection;
import org.afree.data.xy.DefaultXYDataset;
import org.afree.graphics.PaintType;
import org.afree.graphics.SolidColor;
import org.afree.graphics.geom.Font;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MonitorActivity extends Activity {
	private static final String PREFERENCE_KEY = "AuthData";
	SharedPreferences authData;
	TimeRange timerange = new TimeRange();
	ZabbixApiAccess zabbix;
	String authToken;
	String uri;
	Item item;
	GestureDetector gestureDetector;
	LineChartView lineview;
	String itemdescription;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitor);
        setTitle(R.string.title_monitor_result);
        
        
        gestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
			
			@Override
			public boolean onSingleTapUp(MotionEvent arg0) {
				// TODO 自動生成されたメソッド・スタブ
				//Toast.makeText(MonitorActivity.this, "onSingleTapUp", Toast.LENGTH_LONG).show();
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent arg0) {
				// TODO 自動生成されたメソッド・スタブ
				//Toast.makeText(MonitorActivity.this, "onShowPress", Toast.LENGTH_LONG).show();
				
			}
			
			@Override
			public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
					float arg3) {
				// TODO 自動生成されたメソッド・スタブ
				//Toast.makeText(MonitorActivity.this, "onScroll", Toast.LENGTH_LONG).show();
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent arg0) {
				// TODO 自動生成されたメソッド・スタブ
				//Toast.makeText(MonitorActivity.this, "onLongPress", Toast.LENGTH_LONG).show();
				
			}
			
			@Override
			public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
					float arg3) {
				
				int pointerCount = arg0.getPointerCount();
				if ( pointerCount == 1 ) {
					if (arg0.getX() < arg1.getX()) {
						//Toast.makeText(MonitorActivity.this, "1時間戻る", Toast.LENGTH_LONG).show();
					//	Toast.makeText(MonitorActivity.this, Integer.toString(pointerCount), Toast.LENGTH_LONG).show();
						lineview.setChart(getPreviousLineChart(timerange.getTimeFrom()));
						lineview.invalidate();
					}else if (arg0.getX() > arg1.getX()) {
						Toast.makeText(MonitorActivity.this, "1時間進む", Toast.LENGTH_LONG).show();
						lineview.setChart(getNextLineChart(timerange.getTimeTill()));
						lineview.invalidate();
					}
				}else if ( pointerCount == 2 ) {
					if ( Math.abs(arg0.getX(0) - arg0.getX(1)) < Math.abs(arg1.getX(0) - arg1.getX(1)) ) {
//					if ( arg0.getX(0) > arg1.getX(0) & arg0.getX(1) < arg1.getX(1) ) {
					//	Toast.makeText(MonitorActivity.this, "ピンチイン", Toast.LENGTH_LONG).show();
					}
					Log.e("pointerCount",Integer.toString(pointerCount));
				}
				
				
				// TODO 自動生成されたメソッド・スタブ
				return false;
			}
			
			@Override
			public boolean onDown(MotionEvent arg0) {
				// TODO 自動生成されたメソッド・スタブ
				Toast.makeText(MonitorActivity.this, "onDown", Toast.LENGTH_LONG).show();
				int pointerCount = arg0.getPointerCount();
				if ( pointerCount == 1 ) {
					Log.d("getX(0)",Float.toString(arg0.getX(0)));
				}else if ( pointerCount == 2 ) {
					Log.d("getX(0)",Float.toString(arg0.getX(0)));
					Log.d("getX(1)",Float.toString(arg0.getX(1)));
				}
				
				
				
				return false;
			}
		});
        
        
        
        

        Intent intent = getIntent();
        item = (Item)intent.getSerializableExtra("item");
        
        //String itemID = intent.getStringExtra("itemid");
        itemdescription = intent.getStringExtra("itemdescription");
        String hostName = intent.getStringExtra("hostName");
        
        authData = getSharedPreferences(PREFERENCE_KEY, MODE_PRIVATE);
        authToken = authData.getString("AuthToken", "No Data");
        uri = authData.getString("URI", "No Data");

        //timerange作成
        Date now = new Date();
        timerange.setTranslateDateToTimeTill(now);
        timerange.setTimeFromBeforeHour(1);
        
        
        zabbix = new ZabbixApiAccess();
		zabbix.setHttpPost(uri);
		ArrayList<HistoryData> historyDataList = zabbix.getHistoryData(authToken, item, timerange);        
     
        TextView textViewMonitor = (TextView)this.findViewById(R.id.monitor_name);
        textViewMonitor.setText(hostName);
  
        TimeSeries series = new TimeSeries(itemdescription, Second.class);
        
        int count = historyDataList.size();
        Log.e("SIZE", Integer.toString(count));
        
        for(int i=0; i < count; i++) {
        	TimeRange t = new TimeRange();
        	t.setTimeTill(historyDataList.get(i).getUnixtime());
        	if(item.getItemValueType().equals("3")) {
        		series.add(new Second(t.getTimeTillAtDateType()),Integer.valueOf(historyDataList.get(i).getValue()));
        	}else if (item.getItemValueType().equals("0")) {
        		series.add(new Second(t.getTimeTillAtDateType()),Double.valueOf(historyDataList.get(i).getValue()));
        	}
        }
        
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);
        
        lineview = (LineChartView) findViewById(R.id.lineview);
        lineview.setChart(getLineChartView(dataset, itemdescription));    
        lineview.setOnLongClickListener(new View.OnLongClickListener(){
         	       	
        	public boolean onLongClick(View v) {
        		View view = lineview.getRootView();
        		view.setDrawingCacheEnabled(true);
        		Bitmap bmp = view.getDrawingCache();
        		String status = Environment.getExternalStorageState();
        		File dataDir = null;
        		if ( status.equals(Environment.MEDIA_MOUNTED)) {
        			dataDir = new File(Environment.getExternalStorageDirectory(),"com.zabbix");
       				dataDir.mkdir();
        		}
        		else {
        			new AlertDialog.Builder(MonitorActivity.this).setMessage("SDカードがありません").setPositiveButton("OK",null).show();
        		}
        		String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + dataDir.getName() + File.separator + "graph.png";
        		if ( bmp != null ) {
        			ByteArrayOutputStream os = new ByteArrayOutputStream();
					
					//FileOutputStream output = openFileOutput(filePath,Context.MODE_WORLD_READABLE);
					bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
					Log.e("outputStream",Integer.toString(os.size()));
					try {
						os.flush();
						byte[] w = os.toByteArray();
						os.close();
						FileOutputStream out = new FileOutputStream(filePath);
						out.write(w, 0, w.length);
						out.flush();
						
					} catch (IOException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
        		}
        		Uri uri = Uri.fromFile(new File(filePath));
        		Intent intent = new Intent();
        		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        		intent.setAction(Intent.ACTION_SEND);
        		intent.setType("image/*");
        		intent.putExtra(Intent.EXTRA_STREAM, uri);
        		
        		startActivity(intent);
        		return true;
        	}
        	
        	
        });
        
        lineview.setOnTouchListener(new View.OnTouchListener() {
			
        	float down_x0;
			float down_x1;
			float up_x0;
			float up_x1;
			
        	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int pointerCount = event.getPointerCount();
				int action = event.getAction();
				
				if ( action == MotionEvent.ACTION_DOWN) {
					Log.d("Down action",Integer.toString(action));
					Toast.makeText(MonitorActivity.this, "ACTION_DOWN", Toast.LENGTH_LONG).show();
					//down_x = event.getX();
					//down_y = event.getY();
			
				}
				else if ( action == MotionEvent.ACTION_UP) {
					Log.d("Up action",Integer.toString(action));
					Toast.makeText(MonitorActivity.this, "ACTION_UP", Toast.LENGTH_LONG).show();
				}
				
				if ( pointerCount == 1 ) {
				//	Log.d("onTouch getX(0)",Float.toString(event.getX(0)));
				//	Log.d("historicalgetX(0)",Float.toString(event.getHistoricalX(1)));
				}else if ( pointerCount == 2 ) {
					Log.d("action",Integer.toString(action));
					//if ( action == MotionEvent.ACTION_POINTER_2_DOWN) {
					if ( action == 261) {
						down_x0 = event.getX(0);
						down_x1 = event.getX(1);
						Log.d("down_x0",Float.toString(down_x0));
						Log.d("down_x1",Float.toString(down_x1));
					}
					else if ( action == MotionEvent.ACTION_POINTER_2_UP) {
						up_x0 = event.getX(0);
						up_x1 = event.getX(1);
						Log.d("up_x0",Float.toString(up_x0));
						Log.d("up_x1",Float.toString(up_x1));
						if ( Math.abs(up_x0 - up_x1) < Math.abs(down_x0 - down_x1) ) {
							Toast.makeText(MonitorActivity.this, "ピンチイン", Toast.LENGTH_LONG).show();
							down_x0 = 0;
							down_x1 = 0;
							up_x0 = 0;
							up_x1 = 0;
						}
					}
					
					
				//	if ( Math.abs(event.getHistoricalX(0) - event.getHistoricalX(1)) < Math.abs(event.getX(0) - event.getX(1)) ) {
						Log.d("onTouch getX(0)",Float.toString(event.getX(0)));
						Log.d("onTouch getX(1)",Float.toString(event.getX(1)));
						//Toast.makeText(MonitorActivity.this, "ピンチイン", Toast.LENGTH_LONG).show();
				//	}
				}
				return gestureDetector.onTouchEvent(event);
				// TODO 自動生成されたメソッド・スタブ
			}
		});
        
        
	}
	
	public AFreeChart getLineChartView(TimeSeriesCollection dataset, String itemdescription) {
		TextTitle title = new TextTitle(itemdescription);
		title.setPaintType(new SolidColor(Color.WHITE));
		AFreeChart chart = ChartFactory.createTimeSeriesChart("", "time", "data", dataset, true, false, false);
		XYPlot plot = (XYPlot) chart.getXYPlot();
		plot.setBackgroundPaintType(new SolidColor(Color.BLACK));
		DateAxis domainAxis = (DateAxis)plot.getDomainAxis();
	    domainAxis.setAxisLinePaintType(new SolidColor(Color.WHITE));
	    domainAxis.setTickLabelPaintType(new SolidColor(Color.CYAN));
	    domainAxis.setLabelPaintType(new SolidColor(Color.CYAN));
	    NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
	    rangeAxis.setAxisLinePaintType(new SolidColor(Color.WHITE));
	    rangeAxis.setTickLabelPaintType(new SolidColor(Color.CYAN));
	    rangeAxis.setLabelPaintType(new SolidColor(Color.CYAN));
	    NumberFormat nf = NumberFormat.getInstance();
	    DecimalFormat df = (DecimalFormat)nf;
	    df.applyPattern("###,###.#");
	    rangeAxis.setNumberFormatOverride(df);
	    plot.setDomainGridlinePaintType(new SolidColor(Color.DKGRAY));
	    plot.setRangeGridlinePaintType(new SolidColor(Color.DKGRAY));
	    chart.setBorderPaintType(new SolidColor(Color.DKGRAY));
	    chart.setTitle(title);
	    chart.setBackgroundPaintType(new SolidColor(Color.DKGRAY));
	    return chart;
	}
	
	private AFreeChart getPreviousLineChart(String time) {
		timerange.setTimeTill(time);
		timerange.setTimeFromBeforeHour(1);
		ArrayList<HistoryData> historyDataList = zabbix.getHistoryData(authToken, item, timerange);
		
		TimeSeries series = new TimeSeries(itemdescription, Second.class);
        
        int count = historyDataList.size();
        Log.e("SIZE", Integer.toString(count));
        
        for(int i=0; i < count; i++) {
        	TimeRange t = new TimeRange();
        	t.setTimeTill(historyDataList.get(i).getUnixtime());
        	if(item.getItemValueType().equals("3")) {
        		series.add(new Second(t.getTimeTillAtDateType()),Integer.valueOf(historyDataList.get(i).getValue()));
        	}else if (item.getItemValueType().equals("0")) {
        		series.add(new Second(t.getTimeTillAtDateType()),Double.valueOf(historyDataList.get(i).getValue()));
        	}
        }
        
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);
        return getLineChartView(dataset, itemdescription);
		
	}
	
	private AFreeChart getNextLineChart(String time) {
		timerange.setTimeFrom(time);
		timerange.setTimeTillAfterHour(1);
		
		ArrayList<HistoryData> historyDataList = zabbix.getHistoryData(authToken, item, timerange);
		
		TimeSeries series = new TimeSeries(itemdescription, Second.class);
        
        int count = historyDataList.size();
        Log.e("SIZE", Integer.toString(count));
        
        for(int i=0; i < count; i++) {
        	TimeRange t = new TimeRange();
        	t.setTimeTill(historyDataList.get(i).getUnixtime());
        	if(item.getItemValueType().equals("3")) {
        		series.add(new Second(t.getTimeTillAtDateType()),Integer.valueOf(historyDataList.get(i).getValue()));
        	}else if (item.getItemValueType().equals("0")) {
        		series.add(new Second(t.getTimeTillAtDateType()),Double.valueOf(historyDataList.get(i).getValue()));
        	}
        }
        
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);
        return getLineChartView(dataset, itemdescription);
		
	}
}
