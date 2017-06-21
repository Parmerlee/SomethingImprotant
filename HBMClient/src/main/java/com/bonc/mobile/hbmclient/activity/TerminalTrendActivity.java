package com.bonc.mobile.hbmclient.activity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.chart.BarChartView;
import com.bonc.mobile.hbmclient.chart.BarLineChartView;
import com.bonc.mobile.hbmclient.chart.DataConverter;
import com.bonc.mobile.hbmclient.chart.PieChartView;
import com.bonc.mobile.hbmclient.chart.SimpleChartData;
import com.bonc.mobile.hbmclient.data.IndexData;
import com.bonc.mobile.hbmclient.net.HttpRequestTask;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;

/**
 * @author sunwei 指标里趋势图点击界面
 */
public class TerminalTrendActivity extends BaseActivity {
	static final String KPICOLS_DAY = SimpleTerminalActivity.KPICOLS_DAY;
	static final String KPICOLS_MONTH = SimpleTerminalActivity.KPICOLS_MONTH;

	protected String optime, areaCode;
	protected boolean monthQuery;

	protected void initBaseData() {
		Intent intent = getIntent();
		optime = intent.getStringExtra("dataTime");
		areaCode = intent.getStringExtra("areaCode");
		monthQuery = intent.getBooleanExtra("monthQuery", false);
	}

	protected void initView() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.parent);
		ll.setBackgroundDrawable(WatermarkImage.getWatermarkDrawable());
	}

	protected void setTitle(String title) {
		TextView act_trend_title = (TextView) findViewById(R.id.act_trend_title);
		act_trend_title.setText(title);
	}

	protected void loadData() {

	}

	protected void bindData(JSONObject json) {

	}

	protected Map<String, Object> buildChartParam(String kpi_codes,
			String kpi_cols, String beginTime, String endTime) {
		Map<String, Object> m = new LinkedHashMap<String, Object>();
		m.put("kpis", kpi_codes);
		m.put("kpiCols", kpi_cols);
		m.put("areaCodes", areaCode);
		m.put("beginTime", beginTime);
		m.put("endTime", endTime);
		m.put("dataType", monthQuery ? "M" : "D");
		if (!beginTime.equals(endTime))
			m.put("orderColums", "op_time");
		return m;
	}

	protected String getBeginTime() {
		if (monthQuery)
			return DateUtil.monthBefore(optime, DateUtil.PATTERN_6, 11);
		else
			return DateUtil.dayBefore(optime, DateUtil.PATTERN_8, 29);
	}

	protected void bindBarLineChartData(BarLineChartView chart, JSONArray a1,
			JSONArray a2, String col, DataConverter converter) {
		SimpleChartData data = SimpleChartData.build(extractRangeData(a1, col),
				extractRangeData(a2, col), converter);
		data.xTextLabels = getRangeLabels();
		chart.setData(data);
	}

	protected void bindBarChartData(BarChartView chart, JSONArray a,
			String col, DataConverter converter) {
		SimpleChartData data = SimpleChartData.build(extractRangeData(a, col),
				converter);
		data.xTextLabels = getRangeLabels();
		chart.setData(data);
	}

	protected double[] extractRangeData(JSONArray a, String col) {
		return IndexData.extractRangeData(a, col, getBeginTime(), monthQuery);
	}

	protected String[] getRangeLabels() {
		int count = monthQuery ? 12 : 30;
		String[] labels = new String[count];
		for (int i = 0; i < count; i++) {
			labels[i] = (i + 1) + "";
		}
		return labels;
	}

	protected void bindPieChartData(PieChartView chart, JSONArray a,
			String col, DataConverter converter, String[] cats) {
		bindPieChartData(chart, JsonUtil.toList(a), col, converter, cats);
	}

	protected void bindPieChartData(PieChartView chart,
			List<Map<String, String>> list, String col,
			DataConverter converter, String[] cats) {
		int count = list.size();
		if (count == 0)
			return;
		SimpleChartData data = SimpleChartData.build(list, col, converter);
		if (cats != null) {
			data.cats = cats;
		}
		chart.setData(data);
	}

	protected String getChartCol(boolean zb) {
		if (zb)
			return monthQuery ? "curmonth_value_dr" : "curday_value_dr";
		else
			return monthQuery ? "curmonth_value" : "curday_value";
	}

	class LoadDataTask extends HttpRequestTask {
		public LoadDataTask(Context context) {
			super(context);
		}

		@Override
		protected void handleResult(JSONObject result) {
			bindData(result);
		}

	}

}
