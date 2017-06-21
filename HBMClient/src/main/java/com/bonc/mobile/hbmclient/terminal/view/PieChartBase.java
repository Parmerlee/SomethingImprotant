package com.bonc.mobile.hbmclient.terminal.view;

import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.util.NumberUtil;
import com.bonc.mobile.hbmclient.util.StringUtil;

public abstract class PieChartBase extends LinearLayout {
	Context context;

	public PieChartBase(Context context) {
		super(context);
		this.context = context;
	}

	int[] colorLib = new int[] { 0xffca1b1b, 0xfff0c800, 0xff0d9bdd,
			0xff5ba209, 0xfff58c00, 0xffb700a8, 0xFFD1EEEE, 0xff90EE90 };
	private String title = "";
	private View view;
	protected List<Map<String, String>> list;
	private CategorySeries categorySeries;
	private Handler handler = new Handler();
	private DefaultRenderer renderer;

	public View getView() {
		initRenderer();
		categoryDataset();
		view = ChartFactory.getPieChartView(this.getContext(), categorySeries,
				renderer);
		return view;
	}

	public DefaultRenderer getRenderer() {
		return renderer;
	}

	private void setRenderer(DefaultRenderer renderer) {
		this.renderer = renderer;
	}

	/**
	 * 初始化renderer
	 */
	protected void initRenderer() {
		renderer = new DefaultRenderer();
		int labelTextSize = (int) context.getResources().getDimension(
				R.dimen.heyue_text_size);
		renderer.setLabelsTextSize(labelTextSize);
		renderer.setLegendTextSize(labelTextSize);
		// 设置图表的外边框(上/左/下/右)
		// renderer.setMargins(new int[] { 20, 30, 15, 0 });
		renderer.setMargins(new int[] { 3, 3, 3, 3 });
		renderer.setChartTitle(title);
		renderer.setPanEnabled(false);// 禁止拖动图
		renderer.setDisplayValues(true);
		renderer.setInScroll(true);
		renderer.setZoomEnabled(false);
		renderer.setIsPeculiarLegendTextColor(true);
		// 消除锯齿
		renderer.setAntialiasing(true);
		// renderer.setShow
		renderer.setLabelsColor(Color.BLACK);
		renderer.setClickEnabled(true);
		renderer.setShowLabels(true);
		renderer.setDrawTextFrame(true);
		NumberFormat format = NumberFormat.getPercentInstance();

		list = loadingData();
		if (list == null) { // fxf fix null pointer
			return;
		}
		int len = list.size();
		int colorLen = colorLib.length;
		for (int i = 0; i < len; i++) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setChartValuesFormat(format);
			r.setColor(colorLib[i % colorLen]);
			renderer.addSeriesRenderer(r);
		}
	}

	/**
	 * 数据设置.
	 */
	protected void categoryDataset() {

		if (list == null) { // fxf fix null pointer
			return;
		}

		if (categorySeries == null) {
			categorySeries = new CategorySeries(title);
		}

		categorySeries.clear();

		Double[] d = new Double[list.size()];
		String[] wd = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			d[i] = NumberUtil.changeToDouble(list.get(i).get("numZb"));
			wd[i] = StringUtil.toFixedLength(list.get(i).get("analysisWay"));
		}
		int len = d.length;
		double tempValue;
		String tempString;
		for (int i = 0; i < len; i++) {
			for (int k = i + 1; k < len; k++) {
				if (d[k] < d[i]) {
					tempValue = d[i];
					d[i] = d[k];
					d[k] = tempValue;

					tempString = wd[i];
					wd[i] = wd[k];
					wd[k] = tempString;

				}
			}
		}

		int med = (len + 1) / 2;

		for (int i = 0; i < med; i++) {
			categorySeries.add(wd[i], d[i]);
			if (med + i < len) {
				categorySeries.add(wd[med + i], d[med + i]);
			}
		}

		/*
		 * for (Map<String,String> map: list){
		 * categorySeries.add(StringUtil.toFixedLength(map.get("analysisWay")),
		 * NumberUtil.changeToDouble(map.get("numZb"))); }
		 */
	}

	public CategorySeries getCategorySeries() {
		return categorySeries;
	}

	/**
	 * 刷新饼图
	 */
	public void refresh() {
		list = loadingData();
		categoryDataset();

		handler.post(new Runnable() {
			@Override
			public void run() {
				if (view != null)
					view.invalidate();
			}
		});
	}

	/**
	 * 获取数据
	 */
	protected abstract List<Map<String, String>> loadingData();

}
