package com.bonc.mobile.remoteview.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bonc.mobile.common.User;
import com.bonc.mobile.common.chart.BarLineChartView;
import com.bonc.mobile.common.chart.SimpleChartData;
import com.bonc.mobile.common.chart.TwoBarChartView;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.kpi.SimpleKpiDataModel;
import com.bonc.mobile.common.kpi.SimpleKpiDataView;
import com.bonc.mobile.common.kpi.TableReportActivity;
import com.bonc.mobile.common.util.DateUtil;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.NumberUtil;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.common.ConfigLoader;
import com.bonc.mobile.remoteview.common.RemoteUtil;
import com.bonc.mobile.remoteview.common.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class HMFlowDrillChartAreaActivity extends TableReportActivity {

	private TwoBarChartView blView;
	TextView time, tv_unit;
	SimpleKpiDataView skdView;

	String kpiCode;
	int pos = -1;
	String unit = null;

	@Override
	protected BaseConfigLoader getConfigLoader() {
		return ConfigLoader.getInstance(this);
	}

	@Override
	protected int getContentRes() {
		return R.layout.activity_hm_drill_chart_area;
	}

	@Override
	protected void fillExtraParam(Map<String, String> param) {
		// TODO Auto-generated method stub
		super.fillExtraParam(param);
		param.put("queryDate",
				DateUtil.formatter(date_button.getDate(), "yyyyMMddHH"));
		param.put("areaCode", area_button.getChoiceValue());
		param.put("kpiCode", kpiCode);
		param.put("clickCode", menuCode);
		param.put("appType", "ANDROID_RVS");
		param.put("clickType", "KPI");

		time.setText(DateUtil.formatter(date_button.getDate(), "yyyy-MM-dd HH时"));

	}

	@Override
	protected void bindData(JSONObject result) {
		super.bindData(result);
		dataView.setTitleStyle(0, R.style.mainkpi_title_style2);
		try {
			JSONArray arr = result.getJSONArray("chartData");
			SimpleChartData data = SimpleChartData.build(
					getdouble(JsonUtil.toList(arr.getJSONArray(0)),
							"CURHOUR_VALUE"),
					getdouble(JsonUtil.toList(arr.getJSONArray(0)),
							"PY_CURHOUR_VALUE"), SimpleChartData
							.getConverter(getdouble(
									JsonUtil.toList(arr.getJSONArray(0)),
									"CURHOUR_VALUE")));
			data.cats = gettimeList(JsonUtil.toList(arr.getJSONArray(0)),
					"OP_TIME");
			blView.setData(data);

			unit = JsonUtil.toList(JsonUtil.optJSONArray(result, "kpiData"))
					.get(0).get("KPI_UNIT");

			tv_unit.setText(getAverage(getdouble(
					JsonUtil.toList(arr.getJSONArray(0)), "CURHOUR_VALUE")));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String getAverage(double[] getdouble) {
		// TODO Auto-generated method stub
		double sum = 0;
		for (int i = 0; i < getdouble.length; i++) {
			sum += getdouble[i];
		}
		String str = NumberUtil.format(sum / getdouble.length);
		return str.substring(str.length() - 1);
	}

	String[] gettimeList(List<Map<String, String>> list, String key) {
		String[] key_list = new String[list.size()];

		for (int i = 0; i < list.size(); i++) {
			key_list[i] = String.valueOf(list.get(i).get(key).substring(8));
		}
		return key_list;

	}

	double[] getdouble(List<Map<String, String>> list, String key) {
		double[] key_list = new double[list.size()];

		for (int i = 0; i < list.size(); i++) {
			key_list[i] = NumberUtil.changeToDouble(list.get(i).get(key));
		}
		return key_list;

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		blView = (TwoBarChartView) this
				.findViewById(R.id.activity_hm_drill_chart_area_barLineChartView);

		time = (TextView) this
				.findViewById(R.id.activity_hm_drill_chart_area_time);
		tv_unit = (TextView) this
				.findViewById(R.id.activity_hm_drill_chart_area_unit);

		Bundle b = new Bundle();
		b = this.getIntent().getExtras();
		menuCode = b.getString(BaseConfigLoader.KEY_MENU_CODE);
		pos = b.getInt("postion");
		kpiCode = b.getString("kpiCode");

		TextViewUtils.setText(this, R.id.title, b.getString("title"));

		date_button.setWithTime(true);
		date_button.setPattern("yyyy/MM/dd HH时");
		date_button.setDate(DateUtil.getDate(Utils.getTime(), "yyyyMMddHH"));

		area_button.setData(JsonUtil.toList(Utils.getAreacode()), "AREA_NAME",
				"AREA_CODE");
		area_button.setChoice(Utils.getAreaname());

		area_button.setVisibility(View.GONE);

		time.setText(DateUtil.formatter(
				DateUtil.getDate(Utils.getTime(), "yyyyMMddHH"),
				"yyyy-MM-dd HH时"));
		
		this.findViewById(R.id.root).setBackgroundColor(Color.WHITE);
        TextView water = (TextView) this.findViewById(R.id.date_water);
        water.setVisibility(View.VISIBLE);
        water.setText("4A:"+User.getInstance().userCode);
	}

	@Override
	protected SimpleKpiDataModel buildDataModel(JSONObject result) {
		SimpleKpiDataModel model = new SimpleKpiDataModel();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();

		map.put("COLUMN_NAME", "地区");
		map.put("COLUMN_VALUE", "AREA_NAME");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("COLUMN_NAME", "当前值");
		map.put("COLUMN_VALUE", "CURHOUR_VALUE");
		map.put("EXT_STR1", "D");
		map.put("COLUMN_UNIT", unit);
		list.add(map);

		map = new HashMap<String, String>();
		map.put("COLUMN_NAME", "环比");
		map.put("COLUMN_VALUE", "CD_COL");
		map.put("EXT_STR1", "D");
		map.put("COLUMN_UNIT", "%");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("COLUMN_NAME", "同比");
		map.put("COLUMN_VALUE", "CD_YYOY");
		map.put("EXT_STR1", "D");
		map.put("COLUMN_UNIT", "%");
		list.add(map);

		model.build(result, list, getSimpleKpiDate(result, "datas"));

		return model;
	}

	private List<Map<String, String>> getSimpleKpiDate(JSONObject result,
			String key) {
		// TODO Auto-generated method stub
		List<Map<String, String>> temp = JsonUtil.toList(result
				.optJSONArray(key));

		Collections.sort(temp, new Comparator<Map<String, String>>() {

			@Override
			public int compare(Map<String, String> lhs, Map<String, String> rhs) {
				// TODO Auto-generated method stub
				return Float.valueOf(rhs.get("CURHOUR_VALUE")).compareTo(
						Float.valueOf(lhs.get("CURHOUR_VALUE")));
			}
		});

		for (int i = 0; i < temp.size(); i++) {
			temp.get(i).put(
					"AREA_NAME",
					TextUtils.isEmpty(Utils.fetchAreaName(temp.get(i).get(
							"AREA_CODE"))) ? "----" : Utils.fetchAreaName(temp
							.get(i).get("AREA_CODE")));
		}

		for (int i = 0; i < temp.size(); i++) {
			if (TextUtils.equals(temp.get(i).get("AREA_CODE"), "HB")) {
				temp.remove(temp.get(i));
			}
		}

		return temp;
	}

	@Override
	protected String getQueryAction() {
		return "/holiday/drill/level2/mobile";
	}

	@Override
	protected boolean hasDateArea() {
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		RemoteUtil.getInstance().addActivity(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (MyUtils.doInfilter(this)) {

			if (!MyUtils.isBackground(this)) {
				MyUtils.startProtal(this);
				RemoteUtil.getInstance().callLogin();
			}
		}
	}
}
