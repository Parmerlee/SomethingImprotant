package com.bonc.mobile.hbmclient.view;

import java.util.List;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

import com.bonc.mobile.hbmclient.util.NumberUtil;

/**
 * 
 * @author ZZZ
 *
 */
public class BarChar4MonArea extends LinearLayout {
	public List<Map<String, String>> totalDatas;// 柱图数据
	public String title = "";
	public double maxValue = 0; // 数据的最大值
	public double minValue = 0; // 数据的最小值
	String unit;
	double unitDiv = 1;

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getUnitDiv() {
		return unitDiv;
	}

	public void setUnitDiv(double unitDiv) {
		this.unitDiv = unitDiv;
	}

	Context context;

	// public ArrayList<Information> list;
	public BarChar4MonArea(Context context,
			List<Map<String, String>> totalDatas, String unit) {
		super(context);
		this.context = context;
		this.totalDatas = totalDatas;
		this.unit = unit;
	}

	public View getView() {
		GraphicalView view = null;
		initData();
		view = ChartFactory.getBarChartView(context, getBarDataSet(),
				getBarRenderer(), Type.STACKED);
		view.ignoreAllTouchEvent();
		return view;
	}

	// 设置柱图自身所需的数据源
	private XYMultipleSeriesDataset getBarDataSet() {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		// 声明一个柱形图
		CategorySeries series = new CategorySeries(title);
		// 为柱形图添加值
		int len = totalDatas.size();

		for (int i = 0; i < len; i++) {

			try {
				series.add(NumberUtil.changeToDouble(totalDatas.get(i).get(
						"value"))
						/ unitDiv);// 除以10000单位变成 "万"
			} catch (Exception e) {
				e.printStackTrace();
				series.add(0.0);
			}
		}

		/*
		 * for (Businesssecond business : totalDatas)
		 * series.add(NumberUtil.changeToDouble
		 * (business.getCurmon_value())/10000);//除以10000单位变成 "万"
		 */dataset.addSeries(series.toXYSeries());// 添加该柱形图到数据设置列表
		return dataset;
	}

	// 描绘器总的设置
	public XYMultipleSeriesRenderer getBarRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		// 通过SimpleSeriesDenderer设置描绘器的颜色
		SimpleSeriesRenderer r = new SimpleSeriesRenderer();
		// 柱子添加蓝色渐变效果
		r.setColor(Color.rgb(0, 171, 245));
		r.setGradientEnabled(true);
		r.setGradientStop(0, 0xff1eaef1);
		r.setGradientStart(0, 0xff1950da);
		r.setShowBarShadow(true);
		r.setShadowColor(0xff101010);
		renderer.addSeriesRenderer(r);
		setChartSettings(renderer);// 设置描绘器的其他属性
		return renderer;
	}

	// 具体设置Render属性
	private void setChartSettings(XYMultipleSeriesRenderer renderer) {
		renderer.setXAxisMin(-0.01);// 设置X轴的最小值为0.5
		renderer.setXAxisMax(totalDatas.size() + 1);// 设置X轴的最大值为
		renderer.setYAxisMin(minValue);// 设置Y轴的最小值为0
		renderer.setYAxisMax(maxValue);// 设置Y轴最大值为500
		renderer.setDisplayChartValues(false); // 设置是否在柱体上方显示值
		renderer.setShowLegend(false);
		renderer.setShowGrid(true);// 设置是否在图表中显示网格
		renderer.setXLabels(0);// 设置X轴显示的刻度标签的个数
		renderer.setBarSpacing(0.3f); // 柱状间的间隔
		renderer.setPanEnabled(false, false);// 允许左右拖动,但不允许上下拖动.
		renderer.setZoomEnabled(true);
		renderer.setLabelsColor(0xff000000);
		renderer.setYLabelsColor(0, 0xff000000);
		renderer.setXLabelsColor(0xff000000);
		renderer.setLabelsTextSize((float) 11);
		renderer.setMargins(new int[] { 11, 18, 0, -10 });

		int len = totalDatas.size();
		for (int i = 0; i < len; i++) {
			renderer.addXTextLabel(i + 1, totalDatas.get(i).get("title"));
		}

		/*
		 * //为X轴的每个柱状图设置底下的标题 ..... int count=1; for (Businesssecond business :
		 * totalDatas) { renderer.addXTextLabel(count,business.getArea_name());
		 * count++; }
		 */
	}

	/**
	 * 初始化绑定柱图数据...
	 */
	public void initData() {

		int len = totalDatas.size();
		double allC = 0;
		double averageC = 0;
		for (int i = 0; i < len; i++) {

			try {
				double value = NumberUtil.changeToDouble(totalDatas.get(i).get(
						"value"));
				allC += Math.abs(value);
				if (value > maxValue)
					maxValue = value;
				if (i == 0) {
					minValue = value;
				} else {
					if (minValue > value) {
						minValue = value;
					}
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if (len != 0) {
			averageC = allC / len;
		}
		if (unit != null) {
			if (unit.contains("%")) {
				unitDiv = 0.01;
			} else {
				int scale = 1;
				// int scalechinese=1;
				while (scale * 10 < Math.abs(averageC)) {
					scale *= 10;

				}
				if (scale >= 100000000) {
					unitDiv = 100000000;
					unit = "亿" + unit;
				} else if (scale >= 10000) {
					unitDiv = 10000;
					unit = "万" + unit;
				}
			}
		}
		maxValue = maxValue / unitDiv;
		maxValue = maxValue + (maxValue / 7); // 让左边刻度线高出数字的最大值
		minValue = minValue / unitDiv;
		minValue = minValue - (Math.abs(minValue) / 7);
		if (!unit.contains("%")) {
			if (minValue > 0)
				minValue = 0;
		}
	}

}
