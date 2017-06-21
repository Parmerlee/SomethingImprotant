package com.bonc.mobile.hbmclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.SimpleAdapter;

import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.chart.BarLineChartView;
import com.bonc.mobile.hbmclient.chart.PieChartView;
import com.bonc.mobile.hbmclient.chart.SimpleChartData;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.data.IndexData;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.util.LogUtil;
import com.bonc.mobile.hbmclient.util.TextViewUtils;
import com.bonc.mobile.hbmclient.view.HorizontalProgressBarViewGroup;
import com.bonc.mobile.hbmclient.view.TopNView;
import com.bonc.mobile.hbmclient.view.adapter.KpiValueViewBinder;

/**
 * @author sunwei
 */
public class PurchaseSellTrendActivity extends TerminalTrendActivity {
	static final String[] pbg_names = new String[] { "销售新机", "销售库存", "裸机",
			"合约机", "定制版", "非定制版", "智能机", "普通机", "4G终端", "非4G终端", "自有资源", "代理商" };
	static final String pbg_codes_day = "4600|4610|400|420|6513|6514|440|460|480|500|520|540";
	static final String pbg_codes_month = "5140|5150|2840|2860|6556|6557|2880|2900|2920|2940|2960|2980";
	static final String title_topn_codes = "6515|6516|6517";
	static final String butie_names_1 = "终端补贴金额|户均补贴";
	static final String butie_names_2 = "成本补贴|话费补贴";
	static final String butie_codes_1 = "6561|6562";
	static final String butie_codes_2 = "6918|6919";
	static final String td_codes_1 = "6563|6564|6565|6566|6567|6568|6569|6570";
	static final String td_codes_2 = "6571|6572|6573|6574|6575|6576|6577|6578";
	static final String[] td_cats = new String[] { "400以下", "400-700",
			"700-1000", "1000-2000", "2000以上" };

	View blc_container;
	BarLineChartView bar_line_chart;
	PieChartView pie_chart_left, pie_chart_right;
	HorizontalProgressBarViewGroup progressbar_group;
	TopNView top3_grid, butie_grid1, butie_grid2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_purchase_sell_trend);
		initBaseData();
		initView();
		loadData();
	}

	protected void initView() {
		super.initView();
		setTitle("进销形势 > 趋势对比");
		blc_container = findViewById(R.id.blc_container);
		bar_line_chart = (BarLineChartView) findViewById(R.id.bar_line_chart);
		progressbar_group = (HorizontalProgressBarViewGroup) findViewById(R.id.id_progressbar_group);
		top3_grid = (TopNView) findViewById(R.id.top3_grid);
		butie_grid1 = (TopNView) findViewById(R.id.butie_grid1);
		butie_grid2 = (TopNView) findViewById(R.id.butie_grid2);
		pie_chart_left = (PieChartView) findViewById(R.id.pie_chart_left);
		pie_chart_right = (PieChartView) findViewById(R.id.pie_chart_right);
		if (monthQuery) {
			findViewById(R.id.top3_container).setVisibility(View.GONE);
			findViewById(R.id.butie_container).setVisibility(View.VISIBLE);
			findViewById(R.id.pc2_container).setVisibility(View.VISIBLE);
			TextViewUtils.setText(blc_container, R.id.blc_legend1, "● 当月销量");
			TextViewUtils.setText(blc_container, R.id.blc_legend2, "■ 当月进货");
		} else {
			findViewById(R.id.top3_container).setVisibility(View.VISIBLE);
			findViewById(R.id.butie_container).setVisibility(View.GONE);
			findViewById(R.id.pc2_container).setVisibility(View.GONE);
			TextViewUtils.setText(blc_container, R.id.blc_legend1, "● 累计销量");
			TextViewUtils.setText(blc_container, R.id.blc_legend2, "■ 累计进货");
		}
		TextViewUtils.setText(blc_container, R.id.blc_unit, "(万台)");
	}

	@Override
	protected void loadData() {
		Map<String, Object> param = new LinkedHashMap<String, Object>();
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		String optime_before = getBeginTime();
		if (monthQuery) {
			list1.add(buildChartParam("6536", KPICOLS_MONTH, optime_before,
					optime));
			list1.add(buildChartParam("2780", KPICOLS_MONTH, optime_before,
					optime));
			list1.add(buildChartParam(pbg_codes_month, KPICOLS_MONTH, optime,
					optime));
			list1.add(buildChartParam(butie_codes_1, KPICOLS_MONTH, optime,
					optime));
			list1.add(buildChartParam(butie_codes_2, KPICOLS_MONTH, optime,
					optime));
			list1.add(buildChartParam(td_codes_1, KPICOLS_MONTH, optime, optime));
			list1.add(buildChartParam(td_codes_2, KPICOLS_MONTH, optime, optime));
		} else {
			list1.add(buildChartParam("370", KPICOLS_DAY, optime_before, optime));
			list1.add(buildChartParam("360", KPICOLS_DAY, optime_before, optime));
			list1.add(buildChartParam(pbg_codes_day, KPICOLS_DAY, optime,
					optime));
			list1.add(buildChartParam(title_topn_codes, KPICOLS_DAY, optime,
					optime));
		}
		LogUtils.logBySys(list1.toString());

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
			bindPBGData(data1.optJSONArray(2));
			if (monthQuery) {
				bindButieData(butie_grid1, data1.optJSONArray(3), butie_names_1);
				bindButieData(butie_grid2, data1.optJSONArray(4), butie_names_2);
				bindTDFendangData(pie_chart_left, data1.optJSONArray(5),
						td_cats);
				bindTDFendangData(pie_chart_right, data1.optJSONArray(6),
						td_cats);
			} else {
				bindTopNData(data1.optJSONArray(3));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	void bindPBGData(JSONArray a) {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		String[] kpiCodes;
		if (monthQuery) {
			kpiCodes = pbg_codes_month.split("\\|");
		} else {
			kpiCodes = pbg_codes_day.split("\\|");
		}
		String[] kpiNames = pbg_names;
		Map<String, String> m = null;
		for (int i = 0; i < kpiCodes.length; i++) {
			if (i % 2 == 0) {
				m = new HashMap<String, String>();
			}
			for (int k = 0; k < a.length(); k++) {
				IndexData idata = IndexData.build(a.optJSONObject(k));
				idata.setFormatRule(Constant.TERMINAL_SALE_DEFAULT_RULE, "",
						Constant.TERMINAL_SALE_PERCENT_RULE_0);
				if (kpiCodes[i].equals(idata.kpi_code)) {
					if (i % 2 == 0) {
						m.put("leftName", kpiNames[i]);
						m.put("leftValue", idata.value_cdi.getValue()
								+ idata.value_cdi.getUnit());
						m.put("pbLeft", idata.value_dr_cdi.getValue());
					} else {
						m.put("rightName", kpiNames[i]);
						m.put("rightValue", idata.value_cdi.getValue()
								+ idata.value_cdi.getUnit());
						m.put("pbRight", idata.value_dr_cdi.getValue());
					}
				}
			}
			if (i % 2 == 1 && !m.isEmpty()) {
				data.add(m);
			}
		}
		progressbar_group.dispatchView(a.length() / 2);
		progressbar_group.setData(data);
		progressbar_group.updateView();
	}

	void bindTopNData(JSONArray a) {
		List<Map<String, String>> list = IndexData.extractData(a);
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.topn_view_item2, new String[] { "dim_value_name",
						"curday_value" }, new int[] { R.id.topn_title,
						R.id.topn_value });
		adapter.setViewBinder(new KpiValueViewBinder(
				Constant.TERMINAL_SALE_DEFAULT_RULE, Constant.UNIT_TAI));
		top3_grid.setAdapter(adapter);
	}

	void bindButieData(TopNView view, JSONArray a, String titles) {
		List<Map<String, String>> list = IndexData.extractData(a);
		IndexData.putTitles(list, "kpi_title", titles.split("\\|"));
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.index_grid_item_big, new String[] { "kpi_title",
						"curmonth_value" }, new int[] { R.id.index_title,
						R.id.index_value });
		adapter.setViewBinder(new KpiValueViewBinder(
				Constant.TERMINAL_SALE_DEFAULT_RULE, Constant.UNIT_YUAN));
		view.setAdapter(adapter);
	}

	void bindTDFendangData(PieChartView chart, JSONArray a, String[] cats) {
		if (a.length() < 8)
			return;
		String col = "curmonth_value_dr";
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i <= 2; i++) {
			list.add(JsonUtil.toMap(a.optJSONObject(i)));
		}
		JSONObject o;
		o = a.optJSONObject(3);
		JsonUtil.put(o, col,
				o.optDouble(col) + a.optJSONObject(4).optDouble(col));
		list.add(JsonUtil.toMap(a.optJSONObject(3)));
		o = a.optJSONObject(5);
		for (int i = 6; i < a.length(); i++) {
			JsonUtil.put(o, col, o.optDouble(col)
					+ a.optJSONObject(i).optDouble(col));
		}
		list.add(JsonUtil.toMap(a.optJSONObject(5)));
		bindPieChartData(chart, list, col, SimpleChartData.PercentConverter,
				cats);
	}

	void bindTDFendangDataOld(PieChartView chart, JSONArray a, String[] cats) {
		String col = "curmonth_value_dr";
		bindPieChartData(chart, a, col, SimpleChartData.PercentConverter, cats);
	}
}
