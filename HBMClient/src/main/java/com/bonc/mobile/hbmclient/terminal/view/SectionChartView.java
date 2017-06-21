package com.bonc.mobile.hbmclient.terminal.view;

import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/** 封装好的上方图表 */
public class SectionChartView extends LinearLayout {
	private XYMultipleSeriesDataset mDataset;
	private XYMultipleSeriesRenderer mRenderer;
	private SimpleSeriesRenderer mBar1Render;
	private SimpleSeriesRenderer mBar2Render;
	private CategorySeries mBar1Data;
	private CategorySeries mBar2Data;
	private GraphicalView mView;
	private BarChart mChart;

	public SectionChartView(Context context, double[] data1, double[] data2) {
		super(context);
		initRender();
		initialize(data1, data2);
	}

	public SectionChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initRender();
	}

	private void initRender() {
		mDataset = new XYMultipleSeriesDataset();
		mRenderer = new XYMultipleSeriesRenderer();
		mBar1Render = new SimpleSeriesRenderer();
		mBar2Render = new SimpleSeriesRenderer();
		mBar1Data = new CategorySeries("");
		mBar2Data = new CategorySeries("");
		mRenderer.setXLabels(0);
		mRenderer.setBarSpacing(0.3);
		mRenderer.addSeriesRenderer(mBar1Render);
		mRenderer.addSeriesRenderer(mBar2Render);
		mRenderer.setShowGridX(true);
		mChart = new BarChart(mDataset, mRenderer, Type.DEFAULT);
		mView = new GraphicalView(getContext(), mChart);
		mDataset.addSeries(mBar1Data.toXYSeries());
		mDataset.addSeries(mBar2Data.toXYSeries());
		mBar1Render.setColor(Color.rgb(0, 171, 245));
		mBar1Render.setGradientEnabled(true);
		mBar1Render.setGradientStop(0, 0xff1eaef1);
		mBar1Render.setGradientStart(0, 0xff1950da);
		mBar1Render.setShowBarShadow(true);
		mBar1Render.setShadowColor(0xff101010);
		mBar2Render.setColor(Color.rgb(245, 171, 0));
		mBar2Render.setGradientEnabled(true);
		mBar2Render.setGradientStop(0, 0xffffb400);
		mBar2Render.setGradientStart(0, 0xffff5a00);
		mBar2Render.setShowBarShadow(true);
		mBar2Render.setShadowColor(0xff101010);
		mRenderer.setShowLegend(false);
		mView.ignoreAllTouchEvent();
		addView(mView);
	}

	/** 充填数据，呈现画面 */
	public void initialize(double[] data1, double[] data2) {
		int size = Math.min(data1.length, data2.length);
		mDataset.removeSeries(0);
		mDataset.removeSeries(0);
		CategorySeries cate1 = new CategorySeries("本月");
		CategorySeries cate2 = new CategorySeries("上月");
		double max = 0;
		for (int i = 0; i < size; i++) {
			cate1.add(data1[i]);
			if (data1[i] > max)
				max = data1[i];
			cate2.add(data2[i]);
			if (data2[i] > max)
				max = data2[i];
		}
		mDataset.addSeries(cate1.toXYSeries());
		mDataset.addSeries(cate2.toXYSeries());
		mRenderer.setXAxisMax(size - 0.4);
		mRenderer.setXAxisMin(0.2);
		mRenderer.setYAxisMax(100);
		mRenderer.setYLabels(0);
		for (int i = 0; i < 5; i++) {
			mRenderer.addYTextLabel(i * 20, i * 20 + "", 0);
		}
		mRenderer.setYAxisMin(0);
		mRenderer.setYLabelsAlign(Align.LEFT);
		mRenderer.setLabelsColor(0xff000000);
		mRenderer.setYLabelsColor(0, 0xff000000);
		mRenderer.setLabelsTextSize(15);
		mRenderer.setMargins(new int[] { 5, 10, 10, -10 });
		mRenderer.setXLabelsColor(0xff000000);
		invalidate();
	}

	public XYMultipleSeriesRenderer getRenderer() {
		return mRenderer;
	}
}
