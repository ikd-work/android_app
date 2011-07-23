package com.zabiroid;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.axis.DateAxis;
import org.afree.chart.axis.NumberAxis;
import org.afree.chart.plot.PlotOrientation;
import org.afree.chart.plot.XYPlot;
import org.afree.data.xy.DefaultXYDataset;
import org.afree.graphics.PaintType;
import org.afree.graphics.SolidColor;
import org.afree.graphics.geom.RectShape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class LineChartView extends View {
	private AFreeChart chart;
	int width;
	int height;
	
	
	public LineChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//ディスプレイサイズ取得
		WindowManager wm = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE));
		Display disp = wm.getDefaultDisplay();
		width = disp.getWidth();
		height = disp.getHeight();
		//グラフ描画サイズ計算
		width = width - 20;
		height = height - 170;
		chart = ChartFactory.createTimeSeriesChart("", "", "", null, true, false, false);
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
	    chart.setBackgroundPaintType(new SolidColor(Color.DKGRAY));
	    chart.setBackgroundPaintType(new SolidColor(Color.DKGRAY));
		
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		RectShape chartArea = new RectShape(0.0,0.0,width,height);
		this.chart.draw(canvas, chartArea);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		this.setMeasuredDimension(width,height);
		
	}
	
	public void setChart(AFreeChart chart) {
		this.chart = chart;
	}
	

}
