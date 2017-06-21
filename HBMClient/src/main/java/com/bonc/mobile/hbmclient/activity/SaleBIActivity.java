package com.bonc.mobile.hbmclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.bonc.mobile.common.activity.BaseDataActivity;
import com.bonc.mobile.common.chart.BarChartView;
import com.bonc.mobile.common.chart.SimpleChartData;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.util.DateUtil;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.NumberUtil;
import com.bonc.mobile.common.util.StringUtil;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.common.view.DataChooseButton;
import com.bonc.mobile.common.view.DataChooseButton.OnDataChooseListener;
import com.bonc.mobile.common.view.DatePickerButton;
import com.bonc.mobile.common.view.SimpleListView;
import com.bonc.mobile.common.view.TopNView;
import com.bonc.mobile.common.view.adapter.SimpleHtmlTextAdapter;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.data.BusinessDao;

public class SaleBIActivity extends BaseDataActivity {
	DataChooseButton area_button;
	View product_topn, product_part2, product_bar_chart1, product_bar_chart2;
	View fee_topn, fee_part2, fee_bar_chart1, fee_bar_chart2;
	View loss_topn, inactive_topn;
	String[] TOPN_KEYS = new String[] { "PRODUCT_NAME", "CURMONTH_VALUE",
			"EXT_STR1" };
	String[] PART2_KEYS = new String[] { "name1", "name2", "name3", "value1",
			"value2", "value3" };
	boolean firstQuery = true;
	BusinessDao business = new BusinessDao();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sale_bi);
		initView();
		loadData();
	}

	@Override
	protected void initBaseData() {
		menuCode = getIntent().getStringExtra("key_menu_code");
	}

	@Override
	protected void initView() {
		super.initView();
		date_button.setPattern(DateUtil.PATTERN_MODEL2_7);
		area_button = (DataChooseButton) findViewById(R.id.area_button);
		area_button.setOnDataChooseListener(new OnDataChooseListener() {
			@Override
			public void onChoose(Map<String, String> item) {
				loadData();
			}
		});
		product_topn = findViewById(R.id.product_topn);
		product_part2 = findViewById(R.id.product_part2);
		product_bar_chart1 = findViewById(R.id.product_bar_chart1);
		product_bar_chart2 = findViewById(R.id.product_bar_chart2);
		fee_topn = findViewById(R.id.fee_topn);
		fee_part2 = findViewById(R.id.fee_part2);
		fee_bar_chart1 = findViewById(R.id.fee_bar_chart1);
		fee_bar_chart2 = findViewById(R.id.fee_bar_chart2);
		loss_topn = findViewById(R.id.loss_topn);
		inactive_topn = findViewById(R.id.inactive_topn);
	}

	@Override
	protected void loadData() {
		loadData(0);
	}

	void loadData(int seq) {
		Map<String, String> param = new LinkedHashMap<String, String>();
		if (!firstQuery) {
			param.put("optime", DateUtil.formatter(date_button.getDate(),
					DateUtil.PATTERN_6));
			param.put("areaCode", area_button.getChoiceValue());
		}
		new LoadDataTask(this)
				.execute("/bi/marketing/" + (seq + 1), param, seq);
	}

	void bindTopnData(View view, final int type, final String title,
			List<Map<String, String>> data, String[] from) {
		TextViewUtils.setText(view, R.id.topn_view_title, title);
		for (Map<String, String> m : data) {
			m.put(from[1],
					"历史保有：<br/><b>"
							+ NumberUtil.format(NumberUtil.changeToDouble(m
									.get(from[1]))) + "</b>");
			m.put(from[2],
					"环比：<font color='red'>"
							+ NumberUtil.format(NumberUtil.changeToDouble(m
									.get(from[2]))) + "</font>");
		}
		TopNView topn_view = (TopNView) view.findViewById(R.id.topn_view);
		topn_view.setAdapter(new SimpleHtmlTextAdapter(this, data,
				R.layout.topn_item, from, new int[] { R.id.item_title,
						R.id.data1, R.id.data2 }));
		topn_view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startTopNChart(title, type);
			}
		});
	}

	void startTopNChart(String title, int type) {
		Intent intent = new Intent(this, SaleBIChartActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("type", type);
		intent.putExtra("optime",
				DateUtil.formatter(date_button.getDate(), DateUtil.PATTERN_6));
		intent.putExtra("areaCode", area_button.getChoiceValue());
		startActivity(intent);
	}

	List<Map<String, String>> buildPart2Data(JSONObject result) {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		List<Map<String, String>> kpiData = JsonUtil.toList(JsonUtil
				.optJSONArray(result, "kpiData")), rkpiInfo = JsonUtil
				.toList(JsonUtil.optJSONArray(result, "kpiInfo"));
		if (kpiData.size() == 0)
			return data;
		Map<String, Map<String, String>> kpiInfo = new HashMap<String, Map<String, String>>();
		for (Map<String, String> m : rkpiInfo)
			kpiInfo.put(m.get("KPI_CODE"), m);
		Map<String, String> m = new HashMap<String, String>();
		for (int i = 0; i < kpiData.size(); i++) {
			Map<String, String> d = kpiData.get(i);
			m.put("name" + (i % 3 + 1),
					kpiInfo.get(d.get("KPI_CODE")).get("KPI_NAME") + ":");
			m.put("value" + (i % 3 + 1),
					"<b>"
							+ StringUtil.nullToString2(d.get("CURMONTH_VALUE"))
							+ "</b>"
							+ StringUtil.nullToString(kpiInfo.get(
									d.get("KPI_CODE")).get("KPI_UNIT")));
			if (m.size() == 6) {
				data.add(m);
				m = new HashMap<String, String>();
			}
		}
		if (m.size() > 0)
			data.add(m);
		return data;
	}

	@Override
	protected void initDateSelect() {
		date_button = (DatePickerButton) findViewById(R.id.date_button);
		date_button.setWithoutDay(true);
		if (date_button != null) {
			date_button.setOnDateSetListener(new OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					loadData();
				}
			});
		}
	}

	void bindPart2Data(SimpleListView view, final int type,
			List<Map<String, String>> data, String[] from) {
		((View) view.getParent()).setVisibility(data.size() == 0 ? View.GONE
				: View.VISIBLE);
		view.setAdapter(new Part2Adapter(this, data,
				R.layout.sale_bi_part2_item, from, new int[] { R.id.name1,
						R.id.name2, R.id.name3, R.id.value1, R.id.value2,
						R.id.value3 }));
	}

	class Part2Adapter extends SimpleHtmlTextAdapter {
		public Part2Adapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = super.getView(position, convertView, parent);
			Map<String, String> m = (Map<String, String>) getItem(position);
			if (m.size() < 6)
				v.findViewById(R.id.col3).setVisibility(View.GONE);
			return v;
		}
	}

	void bindChartData(View view, String title, List<Map<String, String>> data,
			String catCol, String valCol) {
		view.setVisibility(data.size() == 0 ? View.GONE : View.VISIBLE);
		TextViewUtils.setText(view, R.id.bar_chart_title, title);
		if (data.size() == 0)
			return;
		BarChartView bar_chart = (BarChartView) view
				.findViewById(R.id.bar_chart);
		SimpleChartData chartData = SimpleChartData.build(data, catCol, valCol,
				null);
		bar_chart.setData(chartData);
	}

	String getBarTitle(JSONObject data, String key) {
		return data.optJSONObject(key).optString("KPI_NAME");
	}

	class LoadDataTask extends HttpRequestTask {
		int requestCode;

		public LoadDataTask(Context context) {
			super(context, Constant.BASE_PATH);
		}

		@Override
		protected String doInBackground(Object... params) {
			requestCode = (Integer) params[2];
			return super.doInBackground(params);
		}

		@Override
		protected void handleResult(JSONObject result) {
			firstQuery = false;
			if (requestCode == 0) {
				date_button.setDate(DateUtil.getDate(
						result.optString("optime"), DateUtil.PATTERN_6));
				area_button.setData(JsonUtil.toList(JsonUtil.optJSONArray(
						result, "authArea")), "AREA_NAME", "AREA_CODE");
				area_button.setChoice(result.optString("areaCode"));
				bindTopnData(
						product_topn,
						requestCode,
						"产品营销TOP10",
						JsonUtil.toList(JsonUtil.optJSONArray(result, "top10")),
						TOPN_KEYS);
				bindPart2Data((SimpleListView) product_part2, requestCode,
						buildPart2Data(result), PART2_KEYS);
				bindChartData(product_bar_chart1,
						getBarTitle(result, "bar1_title"),
						JsonUtil.toList(JsonUtil.optJSONArray(result, "bar1")),
						"AREA_NAME", "CURMONTH_VALUE");
				bindChartData(product_bar_chart2,
						getBarTitle(result, "bar2_title"),
						JsonUtil.toList(JsonUtil.optJSONArray(result, "bar2")),
						"AREA_NAME", "CURMONTH_VALUE");
				loadData(1);
				loadData(2);
			} else if (requestCode == 1) {
				bindTopnData(
						fee_topn,
						requestCode,
						"资费营销TOP10",
						JsonUtil.toList(JsonUtil.optJSONArray(result, "top10")),
						TOPN_KEYS);
				bindPart2Data((SimpleListView) fee_part2, requestCode,
						buildPart2Data(result), PART2_KEYS);
				bindChartData(fee_bar_chart1,
						getBarTitle(result, "bar1_title"),
						JsonUtil.toList(JsonUtil.optJSONArray(result, "bar1")),
						"AREA_NAME", "CURMONTH_VALUE");
				bindChartData(fee_bar_chart2,
						getBarTitle(result, "bar2_title"),
						JsonUtil.toList(JsonUtil.optJSONArray(result, "bar2")),
						"AREA_NAME", "CURMONTH_VALUE");
			} else {
				bindTopnData(loss_topn, 2, "持续流失TOP10",
						JsonUtil.toList(JsonUtil.optJSONArray(result,
								"lossTop10")), TOPN_KEYS);
				bindTopnData(inactive_topn, 3, "低活跃度营销TOP10",
						JsonUtil.toList(JsonUtil.optJSONArray(result,
								"lowTop10")), TOPN_KEYS);
			}
		}
	}
}
