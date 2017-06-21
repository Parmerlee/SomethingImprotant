/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bonc.mobile.hbmclient.terminal.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarGapChart.Type2;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;

import com.bonc.mobile.hbmclient.terminal.TerminalHomePageDataLoad;
import com.bonc.mobile.hbmclient.util.NumberUtil;

/**
 * Sales demo bar chart.
 */
public class SalesBarChart extends AbstractBaseChart implements
		TerminalHomeRefreshInterface {
	private List<Map<String, String>> list;
	private Handler handler = new Handler();
	private View view;
	private XYMultipleSeriesRenderer renderer;
	private XYMultipleSeriesDataset dataset;
	int[] colorLib = new int[] { 0xffca1b1b, 0xfff0c800, 0xff0d9bdd,
			0xff5ba209, 0xfff58c00, 0xffb700a8, 0xFFD1EEEE, 0xff90EE90 };

	Context context;

	public SalesBarChart(Context context) {
		super(context);
		this.context = context;
		TerminalRefreshListener.register(this);
	}

	public View getView() {
		initRenderer();
		view = ChartFactory.getBarGapChartView(this.context, dataset, renderer,
				Type2.DEFAULT);

		return view;
	}

	/**
	 * 初始化renderer
	 */
	protected void initRenderer() {

		list = loadingData();

		List<double[]> values = new ArrayList<double[]>();

		if (list == null) { // fxf fix null pointer
			return;
		}
		int len = list.size();
		int colorLen = colorLib.length;
		int colors[] = new int[len];
		String[] titles = new String[len];
		double max = 0;
		for (int i = 0; i < len; i++) {
			double tmp = Double.parseDouble(list.get(i).get("numZb"));
			if (max < tmp) {
				max = tmp;
			}
			colors[i] = colorLib[i % colorLen];
			double d = NumberUtil.changeToDouble(list.get(i).get("numZb"));
			values.add(new double[] { d });
			titles[i] = (list.get(i).get("analysisWay"));
		}

		double maxcolumelength = max * 1.2;

		renderer = buildBarRenderer(colors);
		renderer.setOrientation(Orientation.VERTICAL);
		setChartSettings(renderer, null, null, null, -10, 10, 0,
				maxcolumelength, Color.GRAY, Color.LTGRAY);

		// int labelTextSize=(int)
		// context.getResources().getDimension(R.dimen.heyue_text_size);
		// renderer.setLabelsTextSize(labelTextSize);
		// renderer.setLegendTextSize(labelTextSize);
		// 设置图表的外边框(上/左/下/右)
		// renderer.setMargins(new int[] { 20, 30, 15, 0 });
		// renderer.setMargins(new int[] { 3, 3, 3, 3 });
		// renderer.setChartTitle(title);
		renderer.setShowCustomTextGrid(false);
		renderer.setShowGrid(false);
		renderer.setShowLabels(false);
		renderer.setShowGridX(false);
		renderer.setShowAxes(false);
		// renderer.setBarSpacing(0.1);
		renderer.setBarWidth(13);

		renderer.setPanEnabled(false);// 禁止拖动图
		// renderer.setDisplayValues(true);
		// renderer.setInScroll(true);
		renderer.setZoomEnabled(false);
		// renderer.setIsPeculiarLegendTextColor(true);
		// 消除锯齿
		// renderer.setAntialiasing(true);
		// renderer.setShowLabels(false);
		//
		// renderer.setLabelsColor(Color.BLACK);
		// renderer.setClickEnabled(true);
		// NumberFormat format = NumberFormat.getPercentInstance();

		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			SimpleSeriesRenderer seriesRenderer = renderer
					.getSeriesRendererAt(i);
			seriesRenderer.setDisplayChartValues(true);
		}

		dataset = buildBarDataset(titles, values);

	}

	/**
	 * return the Graphic view
	 * 
	 * @param context
	 *            the context
	 * @return the built intent
	 */
	public View buildbarcharview(Context context) {

		String[] titles = new String[] { "2007", "2008", "2009", "2010", "2011" };
		List<double[]> values = new ArrayList<double[]>();
		values.add(new double[] { 5230 });
		values.add(new double[] { 2000 });
		values.add(new double[] { 1000 });
		values.add(new double[] { 654 });
		values.add(new double[] { 1900 });
		int[] colors = new int[] { Color.CYAN, Color.BLUE, Color.RED,
				Color.YELLOW, Color.DKGRAY };
		XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
		renderer.setOrientation(Orientation.VERTICAL);
		setChartSettings(renderer, null, null, null, -10, 10, 0, 10000,
				Color.GRAY, Color.LTGRAY);
		// renderer.setXLabels(1);
		// renderer.setYLabels(5);
		// renderer.addXTextLabel(1, "");
		// renderer.addXTextLabel(3, "");
		// renderer.addXTextLabel(5, "");
		// renderer.setBarSpacing(100.00);
		renderer.setShowCustomTextGrid(false);
		renderer.setShowGrid(false);
		renderer.setShowLabels(false);
		renderer.setShowGridX(false);
		renderer.setShowAxes(false);
		// renderer.setBarSpacing(0.1);
		renderer.setBarWidth(12);
		renderer.setPanEnabled(false);
		renderer.setZoomEnabled(false);

		// renderer.setX

		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			SimpleSeriesRenderer seriesRenderer = renderer
					.getSeriesRendererAt(i);
			seriesRenderer.setDisplayChartValues(true);
		}
		return ChartFactory.getBarGapChartView(context,
				buildBarDataset(titles, values), renderer, Type2.DEFAULT);
	}

	private List<Map<String, String>> loadingData() {
		return TerminalHomePageDataLoad.getFiveMarket();
	}

	/**
	 * 刷新市场占有比图
	 */
	public void refresh() {
		// super.refresh();
		list = loadingData();
		initRenderer();

		handler.post(new Runnable() {
			@Override
			public void run() {
				view.invalidate();
			}
		});
	}
}
