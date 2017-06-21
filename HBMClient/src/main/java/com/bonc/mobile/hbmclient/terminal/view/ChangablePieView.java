package com.bonc.mobile.hbmclient.terminal.view;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.terminal.TerminalHomePageDataLoad;
import com.bonc.mobile.hbmclient.util.NumberUtil;

public class ChangablePieView extends FrameLayout {
	public static double[] TEST_DATA = new double[] { 12434, 2355, 9085, 2345,
			6800 };
	public static String[] TEST_LABEL = new String[] { "中高端", "高校", "家庭", "集团",
			"流动" };
	public static double[] values;
	public static String[] titles;

	private RainbowHorBarView mBarVIew;

	public ChangablePieView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ChangablePieView(Context context) {
		super(context);
		init();
	}

	private void init() {
		inflate(getContext(), R.layout.changable_pie_layout, this);
		mBarVIew = (RainbowHorBarView) findViewById(R.id.changale_bar_view);
		LayoutParams flp = (LayoutParams) mBarVIew
				.getLayoutParams();
		// flp.leftMargin=10;
		// flp.topMargin=55;
		flp.leftMargin = 10;
		flp.topMargin = 20;
		List<Map<String, String>> list = TerminalHomePageDataLoad
				.getFiveMarket();
		if (list == null) {
			return;
		}
		int size = list.size();
		values = new double[size];
		titles = new String[size];
		for (int i = 0; i < list.size(); i++) {
			values[i] = NumberUtil.changeToDouble(list.get(i).get("numZb"));
			titles[i] = list.get(i).get("analysisWay");
		}

		mBarVIew.setValues(values);
		mBarVIew.setLabels(titles);
	}

	public void setBarDatas(double[] datas) {
		mBarVIew.setValues(datas);
	}

	public void setBarLabels(String[] labels) {
		mBarVIew.setLabels(labels);
	}
}
