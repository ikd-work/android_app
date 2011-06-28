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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitor);
        setTitle(R.string.title_monitor_result);
        
        
        gestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
			
			@Override
			public boolean onSingleTapUp(MotionEvent arg0) {
				// TODO 自動生成されたメソッド・スタブ
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent arg0) {
				// TODO 自動生成されたメソッド・スタブ
				
			}
			
			@Override
			public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
					float arg3) {
				// TODO 自動生成されたメソッド・スタブ
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent arg0) {
				// TODO 自動生成されたメソッド・スタブ
				
			}
			
			@Override
			public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
					float arg3) {
				if (arg0.getX() < arg1.getX()) {
					Toast.makeText(MonitorActivity.this, "左", Toast.LENGTH_LONG).show();
				}else if (arg0.getX() > arg1.getX()) {
					Toast.makeText(MonitorActivity.this, "右", Toast.LENGTH_LONG).show();
				}
				// TODO 自動生成されたメソッド・スタブ
				return false;
			}
			
			@Override
			public boolean onDown(MotionEvent arg0) {
				// TODO 自動生成されたメソッド・スタブ
				return false;
			}
		});
        
        
        
        

        Intent intent = getIntent();
        item = (Item)intent.getSerializableExtra("item");
        
        //String itemID = intent.getStringExtra("itemid");
        String itemdescription = intent.getStringExtra("itemdescription");
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
        
        final LineChartView lineview = (LineChartView) findViewById(R.id.lineview);
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
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
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
}
