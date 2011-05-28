package com.zabbix;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.axis.NumberAxis;
import org.afree.chart.plot.PiePlot;
import org.afree.chart.plot.Plot;
import org.afree.chart.plot.PlotOrientation;
import org.afree.chart.plot.XYPlot;
import org.afree.chart.title.TextTitle;
import org.afree.data.category.CategoryDataset;
import org.afree.data.general.DefaultPieDataset;
import org.afree.data.xy.DefaultXYDataset;
import org.afree.graphics.PaintType;
import org.afree.graphics.SolidColor;
import org.afree.graphics.geom.Font;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MonitorActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitor);
        setTitle(R.string.title_host_detail);

        Intent intent = getIntent();
        String itemID = intent.getStringExtra("itemid");
        String itemdescription = intent.getStringExtra("itemdescription");
        
        TextView textViewMonitor = (TextView)this.findViewById(R.id.monitor_name);
        textViewMonitor.setText(itemdescription);
        DefaultXYDataset dataset = new DefaultXYDataset();
        double[] x = new double[] {1.0, 2.5, 3.0, 7.0};
        double[] y = new double[] {0, 4, 6, 2};
        double[][] data = new double[][] {x,y};
        dataset.addSeries("test", data);
        LineChartView lineview = (LineChartView) findViewById(R.id.lineview);
        lineview.setChart(getLineChartView(dataset));      
	}
	
	public AFreeChart getLineChartView(DefaultXYDataset dataset) {
		TextTitle title = new TextTitle("Line Chart");
		title.setPaintType(new SolidColor(Color.WHITE));
		AFreeChart chart = ChartFactory.createXYLineChart("", "time", "data", dataset, PlotOrientation.VERTICAL, true, false, false);
		XYPlot plot = (XYPlot) chart.getXYPlot();
		plot.setBackgroundPaintType(new SolidColor(Color.BLACK));
        NumberAxis domainAxis = (NumberAxis)plot.getDomainAxis();
	    domainAxis.setAxisLinePaintType(new SolidColor(Color.WHITE));
	    domainAxis.setTickLabelPaintType(new SolidColor(Color.CYAN));
	    domainAxis.setLabelPaintType(new SolidColor(Color.CYAN));
	    NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
	    rangeAxis.setAxisLinePaintType(new SolidColor(Color.WHITE));
	    rangeAxis.setTickLabelPaintType(new SolidColor(Color.CYAN));
	    rangeAxis.setLabelPaintType(new SolidColor(Color.CYAN));
	    plot.setDomainGridlinePaintType(new SolidColor(Color.DKGRAY));
	    plot.setRangeGridlinePaintType(new SolidColor(Color.DKGRAY));
	    chart.setBorderPaintType(new SolidColor(Color.DKGRAY));
	    chart.setTitle(title);
	    chart.setBackgroundPaintType(new SolidColor(Color.DKGRAY));
	    return chart;
	}
}
