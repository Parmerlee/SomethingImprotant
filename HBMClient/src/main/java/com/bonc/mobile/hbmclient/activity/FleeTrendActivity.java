package com.bonc.mobile.hbmclient.activity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.chart.BarChartView;
import com.bonc.mobile.hbmclient.chart.SimpleChartData;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter.ColumnDisplyInfo;
import com.bonc.mobile.hbmclient.data.IndexData;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.util.TextViewUtils;
import com.bonc.mobile.hbmclient.view.adapter.SimpleKpiAdapter;

/**
 * @author sunwei
 *         FleeTrendActivity，StockTrendActivity，PackTrendActivity这三个是领导视窗--终端类
 *         点击趋势对比后的二级界面（顺序可能有误）
 */
public class FleeTrendActivity extends TerminalTrendActivity {
	static final String flee_codes = "5700|6030|6010|6020|5690|5930";
	static final String flee_titles = "疑似省际<br>窜出|窜出率|省内窜入|窜入率|省内窜出|窜出率";

	GridView grid_flee;
	BarChartView bar_chart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flee_trend);
		initBaseData();
		initView();
		loadData();
	}

	protected void initView() {
		super.initView();
		setTitle("串货情况 > 趋势对比");
		grid_flee = (GridView) findViewById(R.id.grid_flee);
		bar_chart = (BarChartView) findViewById(R.id.bar_chart);
	}

	@Override
	protected void loadData() {
		Map<String, Object> param = new LinkedHashMap<String, Object>();
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		list1.add(buildChartParam(flee_codes, KPICOLS_MONTH, optime, optime));
		list1.add(buildChartParam("5930", KPICOLS_MONTH, getBeginTime(), optime));
		param.put("type1", list1);
		new LoadDataTask(this).execute(ActionConstant.GET_TERM_CHART_DATA,
				JsonUtil.toJson(param));
	}

	@Override
	protected void bindData(JSONObject json) {
		try {
			JSONArray data1;
			data1 = json.getJSONArray("data1");
			bindGridData(data1.optJSONArray(0));
			bindBarChartData(bar_chart, data1.optJSONArray(1),
					getChartCol(false), SimpleChartData.PercentConverter);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	void bindGridData(JSONArray a) {
		List<Map<String, String>> list = IndexData.extractData(a,
				flee_codes.split("\\|"));
		IndexData.putTitles(list, "kpi_title", flee_titles.split("\\|"));
		FleeListAdapter adapter = new FleeListAdapter(this,
				R.layout.index_grid_item, list);
		grid_flee.setAdapter(adapter);
	}

	class FleeListAdapter extends SimpleKpiAdapter {
		public FleeListAdapter(Context c, int resource,
				List<Map<String, String>> data) {
			super(c, resource, data);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			Map<String, String> m = data.get(position);
			TextViewUtils.setText(view, R.id.index_title,
					Html.fromHtml(m.get("kpi_title")));
			String kpi_code = m.get("kpi_code");
			String curmonth_value = m.get("curmonth_value");
			ColumnDisplyInfo cdi = IndexData.getColumnDisplyInfo(kpi_code,
					curmonth_value);
			TextViewUtils.setText(
					view,
					R.id.index_value,
					Html.fromHtml("<font color='red'>" + cdi.getValue()
							+ "</font>" + cdi.getUnit()));
			return view;
		}

	}
}
