package com.bonc.mobile.hbmclient.activity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bonc.mobile.common.util.FileUtils;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.data.IndexData;
import com.bonc.mobile.hbmclient.net.HttpRequestTask;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.NumberUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.ACBarChartView;
import com.bonc.mobile.hbmclient.view.ACLineChartView;
import com.bonc.mobile.hbmclient.view.KpiHasChartRightView;
import com.bonc.mobile.hbmclient.view.adapter.PopWindowListAdapter;

/**
 * @author sunwei
 */
public class TrendChartActivity extends BaseActivity {
	public final static String KPI_CODE = "kpiCode";
	public final static String OPTIME = "dataTime";
	public final static String AREA_CODE = "areaCode";
	public final static String DATA_TYPE = "dataType";
	public final static String MENU_CODE = "menuCode";

	String optime, areaCode, dataType, kpiCode, menuCode;
	boolean monthQuery;
	String columnString;
	BusinessDao dao = new BusinessDao();

	ACLineChartView line_chart;
	ACBarChartView bar_chart;
	ListView pop_listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trendchart);
		initBaseData();
		initView();
		loadData();
	}

	void initView() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.parent);
		ll.setBackgroundDrawable(WatermarkImage.getWatermarkLandDrawable());
		line_chart = (ACLineChartView) findViewById(R.id.line_chart);
		bar_chart = (ACBarChartView) findViewById(R.id.bar_chart);
		pop_listview = (ListView) findViewById(R.id.pop_listview);
	}

	void initBaseData() {
		Intent intent = getIntent();
		dataType = intent.getStringExtra(DATA_TYPE);
		optime = intent.getStringExtra(OPTIME);
		areaCode = intent.getStringExtra(AREA_CODE);
		kpiCode = intent.getStringExtra(KPI_CODE);
		menuCode = intent.getStringExtra(MENU_CODE);
		monthQuery = dataType.equals(Constant.DATA_TYPE_MONTH);
		columnString = dao.getMenuAllColumnString(menuCode);
	}

	void loadData() {
		Map<String, String> param = new LinkedHashMap<String, String>();
		if (monthQuery) {
			param.put("starttime", DateUtil.getFirstMonth(
					DateUtil.getDate(optime, DateUtil.PATTERN_6),
					DateUtil.PATTERN_6));
		} else {
			param.put("starttime", DateUtil.getFirstDay(
					DateUtil.getDate(optime, DateUtil.PATTERN_8),
					DateUtil.PATTERN_8));
		}
		param.put("endtime", optime);
		param.put("cols", columnString);
		param.put("optime", optime);
		param.put("areacode", areaCode);
		param.put("dimkey", "T-1");
		param.put("datatable", dao.getMenuColDataTable(menuCode));
		param.put("datatype", dataType);
		param.put("kpicode", kpiCode);
		param.put("isexpand", "1");
		param.put("menuCode", menuCode);
		String action = menuCode.equals(ChannelMainActivity.MENU_CODE) ? ActionConstant.CUSTOMER_RETENTION_TREND
				: ActionConstant.KPI_AREA_PERIOD_DATA;
		new LoadDataTask(this).execute(action, param);
	}

	void bindData(JSONObject result) {
		bindLineChartData(result.optJSONArray("base_data"));
		bindBarChartData(result.optJSONArray("area_data"));
		initListTitle();
		bindListData(result.optJSONArray("base_data"));
	}

	void bindLineChartData(JSONArray a) {
		int len = a.length();
		if (len == 0)
			return;
		double[] currValue = new double[len];
		double[] lastValue = new double[len];
		String[] xLables = new String[len];
		for (int i = 0; i < len; i++) {
			JSONObject data = a.optJSONObject(i);
			currValue[i] = data.optDouble(getChartCol());
			JSONObject pre_date_data = data.optJSONObject("pre_date_data");
			if (pre_date_data != null)
				lastValue[i] = pre_date_data.optDouble(getChartCol());
			String monId = data.optString("op_time");
			xLables[i] = monId.length() > 6 ? monId.substring(6, 8) : monId;
		}
		line_chart.setData(xLables, currValue, lastValue,
				IndexData.getKpiUnit(kpiCode));
		line_chart.setLineTitle(IndexData.getKpiInfo(kpiCode).getKpiName());
		if (monthQuery) {
			line_chart.setLine1Name("本年");
			line_chart.setLine2Name("上年");
		} else {
			line_chart.setLine1Name("本月");
			line_chart.setLine2Name("上月");
		}
	}

	void bindBarChartData(JSONArray a) {
		int len = a.length();
		if (len == 0) {
			bar_chart.setVisibility(View.GONE);
			return;
		}
		List<Double> v = new ArrayList<Double>();
		List<String> cat = new ArrayList<String>();
		String col = getChartCol();
		for (int i = 0; i < len; i++) {
			JSONObject data = a.optJSONObject(i);
			if (areaCode.equals(data.optString("area_code")))
				continue;
			v.add(NumberUtil.changeToDouble(data.optString(col)));
			cat.add(data.optString("area_name"));
		}
		bar_chart.setData(v, cat, IndexData.getKpiUnit(kpiCode));
		bar_chart.setLineTitle(IndexData.getKpiInfo(kpiCode).getKpiName());
	}

	void initListTitle() {
		LinearLayout layRightTitle = (LinearLayout) findViewById(R.id.pop_list_title);
		layRightTitle.setBackgroundResource(R.drawable.glay_list_title);
		KpiHasChartRightView ret = new KpiHasChartRightView(this,
				R.layout.title_right_item, getListTitles(), 0,
				getWindowManager().getDefaultDisplay().getWidth(), true,
				NumberUtil.DpToPx(this, 110));
		layRightTitle.addView(ret);
		ret.setAllBackgroundByID(R.drawable.glay_list_title);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, ret.getItemH());
		ret.setLayoutParams(lp);
	}

	void bindListData(JSONArray a) {
		int len = a.length();
		if (len == 0)
			return;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i < len; i++) {
			list.add(IndexData.extractSingleData(a.optJSONObject(len - 1 - i),
					getListColumns()));
		}
		pop_listview.setAdapter(new PopWindowListAdapter(this, list, IndexData
				.getKpiInfo(kpiCode), getListColumns(), dao
				.getColInfoMap(menuCode)));
	}

	String getChartCol() {
		return columnString.split("\\|")[0];
	}

	String[] getListTitles() {
		String[] a = dao.getColTitleList(menuCode);
		String[] cols = new String[a.length + 1];
		cols[0] = "时间";
		for (int i = 0; i < a.length; i++) {
			cols[i + 1] = a[i];
		}
		return cols;
	}

	String[] getListColumns() {
		String[] a = columnString.split("\\|");
		String[] cols = new String[a.length + 1];
		cols[0] = "op_time";
		for (int i = 0; i < a.length; i++) {
			cols[i + 1] = a[i];
		}
		return cols;
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

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.id_share) {
			FileUtils.shareScreen(this, true);
		}
	}
}
