package com.bonc.mobile.hbmclient.chart;

import java.text.DecimalFormat;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.bonc.mobile.hbmclient.util.NumberUtil;

/**
 * @author sunwei
 */
public class BarLineChartView extends LinearLayout {
	XYMultipleSeriesDataset dataset;
	XYMultipleSeriesRenderer renderer;
	String[] types = new String[] { BarChart.TYPE, LineChart.TYPE };
	SimpleChartData data;

	public BarLineChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BarLineChartView(Context context) {
		super(context);
	}

	public void setData(SimpleChartData data) {
		removeAllViews();
		if (data == null) {
			return;
		}
		this.data = data;
		addView(getView());
	}

	protected View getView() {
		buildDataset();
		buildRenderer();
		return ChartFactory.getCombinedXYChartView(getContext(), dataset,
				renderer, types);
	}

	protected void buildDataset() {
		dataset = new XYMultipleSeriesDataset();
		CategorySeries bar_series = new CategorySeries("");
		for (int i = 0; i < data.seriesData.length; i++) {
			bar_series.add(data.seriesData[i]);
		}
		dataset.addSeries(bar_series.toXYSeries());
		XYSeries line_series_xy = new XYSeries("", 1);
		for (int i = 0; i < data.seriesData2.length; i++) {
			line_series_xy.add(i + 1, data.seriesData2[i]);
		}
		dataset.addSeries(line_series_xy);
	}

	protected void buildRenderer() {
		XYSeriesRenderer mBarRender = new XYSeriesRenderer();
		mBarRender.setColor(Color.rgb(16, 75, 215));
		mBarRender.setGradientEnabled(true);
		mBarRender.setGradientStart(15000, 0xff1950da);
		mBarRender.setGradientStop(0, 0xff1eaef1);
		mBarRender.setShadowColor(0xff000000);
		mBarRender.setShowBarShadow(true);
		XYSeriesRenderer mLine1Render = new XYSeriesRenderer();
		mLine1Render.setColor(0xff000000);
		mLine1Render.setPointStyle(PointStyle.CIRCLE);
		mLine1Render.setFillPoints(true);
		mLine1Render.setLineWidth(1);
		renderer = new XYMultipleSeriesRenderer(2);
		renderer.addSeriesRenderer(mBarRender);
		renderer.addSeriesRenderer(mLine1Render);
		renderer.setXLabels(0);
		renderer.setXLabelsColor(0xff000000);
		renderer.setYLabels(0);
		renderer.setYLabelsPadding(5);
		renderer.setYLabelsColor(0, 0xff000000);
		renderer.setYLabelsColor(1, 0x00000000);
		renderer.setYLabelsAlign(Align.RIGHT, 0);
		renderer.setBarSpacing(1.2);
		renderer.setGridColor(0xff7ca6c7);
		renderer.setMargins(new int[] { 5, 30, 0, 10 });
		renderer.setPanEnabled(false);
		renderer.setZoomEnabled(false);
		renderer.setInScroll(true);
		renderer.setShowLegend(false);
		renderer.setShowAxes(false);
		renderer.setShowCustomTextGrid(true);

		renderer.setXAxisMin(0, 0);
		renderer.setXAxisMin(0, 1);
		for (int i = 0; i < data.xTextLabels.length; i++) {
			renderer.addXTextLabel(i + 1, data.xTextLabels[i]);
		}

		double maxYValue;
		maxYValue = Math.max(NumberUtil.getMaxNumber(data.seriesData),
				NumberUtil.getMaxNumber(data.seriesData2));
		maxYValue = maxYValue + (Math.abs(maxYValue) / 7); // 让左边刻度线高出数字的最大值
		renderer.setYAxisMin(0, 0);
		renderer.setYAxisMin(0, 1);
		renderer.setYAxisMax(maxYValue, 0);
		renderer.setYAxisMax(maxYValue, 1);
		double labelvalue = maxYValue / 5;
		DecimalFormat df = new DecimalFormat("#.#");
		if (Math.abs(labelvalue) > 1) {
			df.applyPattern("#.#");
		} else {
			df.applyPattern("#.###");
		}
		for (int i = 1; i < 5; i++) {
			renderer.addYTextLabel(i * labelvalue, df.format(i * labelvalue), 0);
		}
	}
}
