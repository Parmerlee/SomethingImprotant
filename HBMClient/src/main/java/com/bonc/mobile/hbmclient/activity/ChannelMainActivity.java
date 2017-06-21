package com.bonc.mobile.hbmclient.activity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.achartengine.renderer.DefaultRenderer;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.chart.ChartConfig;
import com.bonc.mobile.hbmclient.chart.DataConverter;
import com.bonc.mobile.hbmclient.chart.PieChartView;
import com.bonc.mobile.hbmclient.chart.SimpleChartData;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.data.IndexData;
import com.bonc.mobile.hbmclient.data.KpiInfo;
import com.bonc.mobile.hbmclient.data.MenuColumnInfo;
import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;
import com.bonc.mobile.hbmclient.terminal.subactivity.TerminalPriceBracketActivity;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.util.MenuUtil;
import com.bonc.mobile.hbmclient.util.TextViewUtils;
import com.bonc.mobile.hbmclient.view.LineTrendView;
import com.bonc.mobile.hbmclient.view.ListViewSetting;
import com.bonc.mobile.hbmclient.view.adapter.SimpleKpiTrendAdapter;

/**
 * @author sunwei
 */
public class ChannelMainActivity extends KpiMainBaseActivity {
	public static final String MENU_CODE = "270";
	static final String[] chart1_cats = new String[] { "保底(0,30)", "保底[30,50)",
			"保底[50,80)", "保底[80,128)", "保底[128,200)", "保底[200,∞)" };
	static final String[] chart2_cats = new String[] { "售价(0,600)",
			"售价[600,1500)", "售价[1500,3000)", "售价[3000,∞)" };
	static final String[] chart3_cats = new String[] { "售价(0,600)",
			"售价[600,1500)", "售价[1500,3000)", "售价[3000,∞)" };
	static final String col_title1 = "区域,(0-30),[30-50),[50-80),[80-128),[128-200),[200-∞)";
	static final String col_title2 = "区域,(0-600),[600-1500),[1500-3000),[3000-∞)";
	static final String col_title3 = "区域,(0-600),[600-1500),[1500-3000),[3000-∞)";
	static final String chart1_codes = "DQ0007|DQ0008|DQ0009|DQ0010|DQ0011|DQ0012";
	static final String chart2_codes = "DQ0013|DQ0014|DQ0015|DQ0016";
	static final String chart3_codes = "DQ0017|DQ0018|DQ0019|DQ0020";
	static final String chart1_rule = "area_name|,curday_value_DQ0007|台,curday_value_DQ0008|台,curday_value_DQ0009|台,curday_value_DQ0010|台,curday_value_DQ0011|台,curday_value_DQ0012|台";
	static final String chart2_rule = "area_name|,curday_value_DQ0013|台,curday_value_DQ0014|台,curday_value_DQ0015|台,curday_value_DQ0016|台";
	static final String chart3_rule = "area_name|,curday_value_DQ0017|台,curday_value_DQ0018|台,curday_value_DQ0019|台,curday_value_DQ0020|台";

	ListView name_list, data_list;
	View chart1_container, chart2_container, chart3_container;
	PieChartView chart1, chart2, chart3;
	BusinessDao dao = new BusinessDao();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setMainContent(R.layout.activity_channel_main);
		initBaseData();
		initView();
		updateView();
		loadData();
	}

	protected void initBaseData() {
		super.initBaseData();
		menuCode = MENU_CODE;
	}

	protected void initView() {
		super.initView();
		name_list = (ListView) findViewById(R.id.name_list);
		name_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Map<String, String> m = (Map<String, String>) name_list
						.getAdapter().getItem(position);
				startAreaActivity(m.get("kpi_code"));
			}
		});
		data_list = (ListView) findViewById(R.id.data_list);
		new ListViewSetting().setListViewOnTouchAndScrollListener(name_list,
				data_list);
		chart1_container = findViewById(R.id.chart1_container);
		chart2_container = findViewById(R.id.chart2_container);
		chart3_container = findViewById(R.id.chart3_container);
		TextViewUtils
				.setText(chart1_container, R.id.pie_chart_title, "合约机保底占比");
		TextViewUtils
				.setText(chart2_container, R.id.pie_chart_title, "合约机售价占比");
		TextViewUtils.setText(chart3_container, R.id.pie_chart_title, "裸机售价占比");
		chart1 = (PieChartView) chart1_container.findViewById(R.id.pie_chart);
		chart1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startManyKpiActivity("合约机保底", col_title1, chart1_rule,
						chart1_codes);
			}
		});
		ChartConfig config = new ChartConfig();
		config.legendType = DefaultRenderer.LEGEND_RIGHT;
		chart1.setConfig(config);
		chart2 = (PieChartView) chart2_container.findViewById(R.id.pie_chart);
		chart2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startManyKpiActivity("合约机售价", col_title2, chart2_rule,
						chart2_codes);
			}
		});
		chart3 = (PieChartView) chart3_container.findViewById(R.id.pie_chart);
		chart3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startManyKpiActivity("裸机售价", col_title3, chart3_rule,
						chart3_codes);
			}
		});
	}

	void resetView() {
		name_list.setAdapter(null);
		data_list.setAdapter(null);
		chart1.setData(null);
		chart2.setData(null);
		chart3.setData(null);
	}

	protected void loadData() {
		resetView();
		optime = DateUtil.formatter(calendar.getTime(), DateUtil.PATTERN_8);
		areaCode = areaInfoList.get(areaIndex).get("areaCode");
		Map<String, Object> param = new LinkedHashMap<String, Object>();
		param.put("optime", optime);
		param.put("areaCode", areaCode);
		param.put("dataType", dataType);
		new LoadDataTask(this).execute(ActionConstant.CUSTOMER_RETENTION,
				JsonUtil.toJson(param));
	}

	protected void bindData(JSONObject result) {
		if (result.optJSONArray("bdDataList").length() == 0
				&& result.optJSONObject("zdywlPieChar").length() == 0) {
			Toast.makeText(this, getString(R.string.no_data),
					Toast.LENGTH_SHORT).show();
			return;
		}
		bindListData(result);
		bindChartData(result);
	}

	void bindListData(JSONObject result) {
		JSONArray a = result.optJSONArray("bdDataList");
		if (a.length() == 0)
			return;
		List<Map<String, String>> data = IndexData.extractData(a);
		Map<String, List<Double>> trendData = IndexData.extractTrendData(result
				.optJSONObject("bdTrendData"));
		name_list.setAdapter(new SimpleAdapter(this, data,
				R.layout.ch_index_list_item_left, new String[] { "kpi_name" },
				new int[] { R.id.id_index_name }));
		data_list.setAdapter(new ChannelKpiListAdapter(this,
				R.layout.ch_index_list_item_right, data, trendData, dao
						.getColInfoMap(menuCode)));
	}

	void bindChartData(JSONObject result) {
		JSONObject a = result.optJSONObject("zdywlPieChar");
		if (a.length() == 0)
			return;
		String col = "curday_value";
		bindPieChartData(chart1, a.optJSONArray("hyjbd"), col, null,
				chart1_cats);
		bindPieChartData(chart2, a.optJSONArray("hyjsj"), col, null,
				chart2_cats);
		bindPieChartData(chart3, a.optJSONArray("ljsj"), col, null, chart3_cats);
	}

	void bindPieChartData(PieChartView chart, JSONArray a, String col,
			DataConverter converter, String[] cats) {
		bindPieChartData(chart, JsonUtil.toList(a), col, converter, cats);
	}

	void bindPieChartData(PieChartView chart, List<Map<String, String>> list,
			String col, DataConverter converter, String[] cats) {
		int count = list.size();
		if (count == 0)
			return;
		SimpleChartData data = SimpleChartData.build(list, col, converter);
		if (cats != null) {
			data.cats = cats;
		}
		chart.setData(data);
	}

	void startAreaActivity(String kpiCode) {
		MenuUtil.startKPIAreaActivity(this, menuCode, optime, areaCode,
				kpiCode, dataType);
	}

	void startTimeActivity(String kpiCode) {
		MenuUtil.startKPITimeActivity(this, menuCode, optime, areaCode,
				kpiCode, dataType);
	}

	void startTrendActivity(String kpiCode) {
		MenuUtil.startKPITrendActivity(this, menuCode, optime, areaCode,
				kpiCode, dataType);
	}

	void startManyKpiActivity(String title, String col_title, String col_rule,
			String col_codes) {
		Intent intent = new Intent(this, TerminalPriceBracketActivity.class);
		intent.putExtra(TerminalConfiguration.TITLE_COLUMN, col_title);
		intent.putExtra(TerminalConfiguration.KEY_RESPONSE_KEY, col_rule);
		intent.putExtra(TerminalConfiguration.KEY_COLUNM_KPI_CODE,
				col_codes.replace("|", ","));
		TerminalActivityEnum tae = TerminalActivityEnum.PSS_DAY_ACTIVITY;
		View dummy = new View(this);
		dummy.setTag(calendar);
		tae.setView(dummy);
		intent.putExtra(TerminalConfiguration.KEY_ACTIVITY_ENUM, tae);
		intent.putExtra(TerminalConfiguration.TITLE_BIG, title);
		intent.putExtra(TerminalConfiguration.KEY_ACTION,
				ActionConstant.CUSTOMER_RETENTION_PIE);

		HashMap<String, String> param = new HashMap<String, String>();
		param.put("optime", optime);
		param.put("areacode", areaCode);
		param.put("kpicodes", col_codes);
		param.put("datatype", dataType);
		intent.putExtra(TerminalConfiguration.KEY_MAP, param);// 远程请求参数

		startActivity(intent);
	}

	class ChannelKpiListAdapter extends SimpleKpiTrendAdapter {
		public ChannelKpiListAdapter(Context c, int resource,
				List<Map<String, String>> data,
				Map<String, List<Double>> trendData,
				Map<String, MenuColumnInfo> colInfo) {
			super(c, resource, data, trendData, colInfo);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			Map<String, String> m = data.get(position);
			final String kpi_code = m.get("kpi_code");
			LineTrendView trendView = (LineTrendView) view
					.findViewById(R.id.id_trend_chart);
			trendView.setData(trendData.get(kpi_code));
			trendView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					startTrendActivity(kpi_code);
				}
			});
			view.findViewById(R.id.id_container_1).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							startTimeActivity(kpi_code);
						}
					});
			KpiInfo kpiInfo = IndexData.getKpiInfo(kpi_code);
			setColValue(view, m, R.id.index_value, "curday_value", kpiInfo);
			setColValue(view, m, R.id.col1_value, "cperiod_0_6_value", kpiInfo);
			setColValue(view, m, R.id.col2_value, "cperiod_6_12_value", kpiInfo);
			setColValue(view, m, R.id.col3_value, "cperiod_12_0_value", kpiInfo);
			return view;
		}
	}
}
