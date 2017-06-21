package com.bonc.mobile.hbmclient.activity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.chart.BarLineChartView;
import com.bonc.mobile.hbmclient.chart.SimpleChartData;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.util.TextViewUtils;

/**
 * @author sunwei
 */
public class PackTrendActivity extends TerminalTrendActivity {
	View blc_container1, blc_container2;
	BarLineChartView bar_line_chart1, bar_line_chart2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pack_trend);
		initBaseData();
		initView();
		loadData();
	}

	protected void initView() {
		super.initView();
		setTitle("深度合约 > 趋势对比");
		blc_container1 = findViewById(R.id.blc1_container);
		bar_line_chart1 = (BarLineChartView) blc_container1
				.findViewById(R.id.bar_line_chart);
		blc_container2 = findViewById(R.id.blc2_container);
		bar_line_chart2 = (BarLineChartView) blc_container2
				.findViewById(R.id.bar_line_chart);
		TextViewUtils.setText(blc_container1, R.id.blc_legend1, "● 拆包量");
		TextViewUtils.setText(blc_container1, R.id.blc_legend2, "■ 合约机销量");
		TextViewUtils.setText(blc_container1, R.id.blc_unit, "(万台)");
		TextViewUtils.setText(blc_container2, R.id.chart_title,
				monthQuery ? "月拆包率" : "日拆包率");
		TextViewUtils.setText(blc_container2, R.id.blc_unit, "(%)");
	}

	@Override
	protected void loadData() {
		Map<String, Object> param = new LinkedHashMap<String, Object>();
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		String optime_before = getBeginTime();
		if (monthQuery) {
			list1.add(buildChartParam("2860", KPICOLS_MONTH, optime_before,
					optime));
			list1.add(buildChartParam("5410", KPICOLS_MONTH, optime_before,
					optime));
			list1.add(buildChartParam("5420", KPICOLS_MONTH, optime_before,
					optime));
		} else {
			list1.add(buildChartParam("420", KPICOLS_DAY, optime_before, optime));
			list1.add(buildChartParam("4870", KPICOLS_DAY, optime_before,
					optime));
			list1.add(buildChartParam("4880", KPICOLS_DAY, optime_before,
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
			bindBarLineChartData(bar_line_chart1, data1.optJSONArray(0),
					data1.optJSONArray(1), getChartCol(false),
					SimpleChartData.Div10KConverter);
			bindBarLineChartData(bar_line_chart2, data1.optJSONArray(2),
					data1.optJSONArray(2), getChartCol(false),
					SimpleChartData.PercentConverter);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
