package com.bonc.mobile.hbmclient.activity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.achartengine.renderer.DefaultRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.chart.BarLineChartView;
import com.bonc.mobile.hbmclient.chart.ChartConfig;
import com.bonc.mobile.hbmclient.chart.PieChartView;
import com.bonc.mobile.hbmclient.chart.SimpleChartData;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.util.TextViewUtils;

/**
 * @author sunwei
 */
public class StockTrendActivity extends TerminalTrendActivity {
	static final String unsale_codes_day = "4630|4640|4650|4660";
	static final String unsale_codes_month = "5170|5180|5190|5200";
	static final String[] unsale_cats = new String[] { "7-12个月", "13-18个月",
			"19-24个月", "大于24个月" };

	View blc_container;
	BarLineChartView bar_line_chart;
	PieChartView pie_chart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_trend);
		initBaseData();
		initView();
		loadData();
	}

	protected void initView() {
		super.initView();

		setTitle("库存状态 > 趋势对比");
		blc_container = findViewById(R.id.blc_container);
		TextViewUtils.setText(blc_container, R.id.blc_legend1, "● 滞销量");
		if (monthQuery) {
			TextViewUtils.setText(blc_container, R.id.blc_legend2, "■ 当前库存");
		} else {
			TextViewUtils.setText(blc_container, R.id.blc_legend2, "■ 累计库存");
		}
		TextViewUtils.setText(blc_container, R.id.blc_unit, "(万台)");

		bar_line_chart = (BarLineChartView) findViewById(R.id.bar_line_chart);

		TextView pie_chart_title = (TextView) findViewById(R.id.pie_chart_title);
		pie_chart_title.setText("滞销周期占比");
		pie_chart = (PieChartView) findViewById(R.id.pie_chart);
		ChartConfig config = new ChartConfig();
		config.legendType = DefaultRenderer.LEGEND_RIGHT;
		config.displayValues = false;
		config.drawTextFrame = true;
		pie_chart.setConfig(config);
	}

	@Override
	protected void loadData() {
		Map<String, Object> param = new LinkedHashMap<String, Object>();
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		String optime_before = getBeginTime();
		if (monthQuery) {
			list1.add(buildChartParam("6543", KPICOLS_MONTH, optime_before,
					optime));
			list1.add(buildChartParam("5160", KPICOLS_MONTH, optime_before,
					optime));
			list1.add(buildChartParam(unsale_codes_month, KPICOLS_MONTH,
					optime, optime));
		} else {
			list1.add(buildChartParam("380", KPICOLS_DAY, optime_before, optime));
			list1.add(buildChartParam("4620", KPICOLS_DAY, optime_before,
					optime));
			list1.add(buildChartParam(unsale_codes_day, KPICOLS_DAY, optime,
					optime));
		}
		param.put("type1", list1);
		new LoadDataTask(this).execute(ActionConstant.GET_TERM_CHART_DATA,
				JsonUtil.toJson(param));
	}

	@Override
	protected void bindData(JSONObject json) {
		try {
			JSONArray data1;
			data1 = json.getJSONArray("data1");
			bindBarLineChartData(bar_line_chart, data1.optJSONArray(0),
					data1.optJSONArray(1), getChartCol(false),
					SimpleChartData.Div10KConverter);
			bindPieChartData(pie_chart, data1.optJSONArray(2),
					getChartCol(true), SimpleChartData.PercentConverter,
					unsale_cats);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
