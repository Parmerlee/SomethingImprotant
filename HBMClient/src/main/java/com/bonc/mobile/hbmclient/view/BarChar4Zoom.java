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

import android.app.Activity;
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
public class BarChar4Zoom extends LinearLayout {
	public List<Map<String, String>> totalDatas;// 柱图数据
	public String title = "";
	public double maxValue = 0; // 数据的最大值
	public double minValue = 0; // 数据的最小值
	Context context;
	public static final int BARW = 40;
	public double unitDiv;

	public BarChar4Zoom(Context context, List<Map<String, String>> totalDatas,
			double unitDiv) {
		super(context);
		this.context = context;
		this.totalDatas = totalDatas;
		this.unitDiv = unitDiv;
	}

	public View getView() {
		GraphicalView view = null;
		initData();
		view = ChartFactory.getBarChartView(context, getBarDataSet(),
				getBarRenderer(), Type.STACKED);
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

			series.add(NumberUtil
					.changeToDouble(totalDatas.get(i).get("value")) / unitDiv);// 除以10000单位变成
																				// "万"

		}

		dataset.addSeries(series.toXYSeries());// 添加该柱形图到数据设置列表
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
		renderer.setXAxisMin(0.4);// 设置X轴的最小值为0.5
		renderer.setXAxisMax(totalDatas.size() + 1);// 设置X轴的最大值为
		renderer.setYAxisMin(minValue);// 设置Y轴的最小值为0
		renderer.setYAxisMax(maxValue);// 设置Y轴最大值为500
		renderer.setDisplayChartValues(false); // 设置是否在柱体上方显示值
		renderer.setShowLegend(false);
		renderer.setShowGrid(true);// 设置是否在图表中显示网格
		renderer.setXLabels(0);// 设置X轴显示的刻度标签的个数
		renderer.setBarSpacing(0.3f); // 柱状间的间隔
		renderer.setPanEnabled(false, false);// 允许左右拖动,但不允许上下拖动.
		renderer.setZoomEnabled(false, false);// .setZoomEnabled(true);
		renderer.setLabelsColor(0xff000000);
		renderer.setYLabelsColor(0, 0xff000000);
		renderer.setXLabelsColor(0xff000000);
		renderer.setLabelsTextSize((float) 16);
		renderer.setBarWidth(BARW);
		renderer.setZoomRate(1.5f);

		int len = totalDatas.size();
		for (int i = 0; i < len; i++) {
			renderer.addXTextLabel(i + 1, totalDatas.get(i).get("title"));

		}
		int widthW = ((Activity) context).getWindowManager()
				.getDefaultDisplay().getWidth();
		int maginR = -10;
		if ((BARW + 10) * len > widthW) {
			maginR = -10 - (BARW + 10) * len + widthW;
			renderer.setPanEnabled(true, false);
		}
		renderer.setMargins(new int[] { 16, 20, 0, maginR });
	}

	/**
	 * 初始化绑定柱图数据...
	 */
	public void initData() {

		int len = totalDatas.size();

		for (int i = 0; i < len; i++) {
			try {
				double value = NumberUtil.changeToDouble(totalDatas.get(i).get(
						"value"))
						/ unitDiv;
				if (value > maxValue)
					maxValue = value;
				if (i == 0) {
					minValue = value;
				} else {
					if (value < minValue)
						minValue = value;
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		maxValue = maxValue + (maxValue / 7); // 让左边刻度线高出数字的最大值

		minValue = minValue - (Math.abs(minValue) / 7);

		if (unitDiv > 1) {

			if (minValue > 0)
				minValue = 0;
		}
	}

}
