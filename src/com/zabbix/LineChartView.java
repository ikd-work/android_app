package com.zabbix;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
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
		width = width - 10;
		height = height - 80;
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
