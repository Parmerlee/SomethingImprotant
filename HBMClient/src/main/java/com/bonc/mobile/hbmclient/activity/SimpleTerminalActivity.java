package com.bonc.mobile.hbmclient.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bonc.mobile.common.util.Base64;
import com.bonc.mobile.common.util.FileUtils;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter;
import com.bonc.mobile.hbmclient.data.IndexData;
import com.bonc.mobile.hbmclient.flyweight.float_view.FloatView;
import com.bonc.mobile.hbmclient.flyweight.float_view.FloatViewFactory;
import com.bonc.mobile.hbmclient.net.HttpRequestTask;
import com.bonc.mobile.hbmclient.util.ArrayUtil;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.util.TextViewUtils;
import com.bonc.mobile.hbmclient.util.UIUtil;
import com.bonc.mobile.hbmclient.view.DateRangeSwitchView2;
import com.bonc.mobile.hbmclient.view.DateRangeSwitchView2.OnStateChangeListener;
import com.bonc.mobile.hbmclient.view.HorizontalScrollViewSynchronizer;
import com.bonc.mobile.hbmclient.view.LinkageHScrollView;
import com.bonc.mobile.hbmclient.view.TopNView;
import com.bonc.mobile.hbmclient.view.adapter.KpiValueViewBinder;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;

/**
 * @author sunwei 终端主界面
 */
public class SimpleTerminalActivity extends KpiMainBaseActivity {
	static final String KPICOLS_DAY = "curday_value|curday_value_dr|cd_myoy";
	static final String KPICOLS_MONTH = "curmonth_value|curmonth_value_dr|cm_col";

	static final String[] group_title_day = new String[] { "累计进货", "累计销售",
			"当前库存", "滞销量", "合约机<br>累计销量", "拆包量", "窜货" };
	static final String[] group_title_month = new String[] { "当月进货", "当月销售",
			"当前库存", "滞销量", "合约机<br>当月销量", "拆包量", "窜货" };
	static final String[] kpi_codes_day = new String[] { "370", "360", "380",
			"4620", "420", "4870" };
	static final String[] kpi_codes_month = new String[] { "6536", "2780",
			"6543", "5160", "2860", "5410", "5690|5700" };
	static final String flee_kpi_prefix = "省内窜出<br>|疑似省际窜出<br>";
	static final String[] ext_kpi_codes_day = new String[] { "6500|6501",
			"390", "6508|6509", "4910|4920" };
	static final String[] ext_kpi_codes_month = new String[] { "6537", "2800",
			"6548|6549", "5450|5460" };
	static final String[] ext_kpi_prefixs_day = new String[] { "天猫:|京东:",
			"预计支撑", "大于1年:|大于2年:", "分离量:|分离率:" };
	static final String[] ext_kpi_prefixs_month = new String[] { "年累计:",
			"年累计:", "大于1年:|大于2年:", "分离量:|分离率:" };
	static final String[] model_topn_kpi_codes_day = new String[] {
			"6502|6503|6504", "720|730|740", "6505|6506|6507",
			"4670|4680|4690", "6510|6511|6512", "4940|4950|4960" };
	static final String[] model_topn_kpi_codes_month = new String[] {
			"6540|6541|6542", "3180|3190|3200", "6544|6545|6546",
			"5210|5220|5230", "6550|6551|6552", "5480|5490|5500",
			"5730|5740|5750" };
	static final String[] area_topn_kpi_codes_day = new String[] { "370",
			"360", "380", "5990", "420", "4880" };
	static final String[] area_topn_kpi_codes_month = new String[] { "6536",
			"2780", "6543", "6000", "2860", "5420", "5930" };

	static final int[] topn_ids = new int[] { R.id.topn_title, R.id.topn_value };

	View purchase_sell_title, purchase_index_content, sell_index_content;
	View stock_title, stock_index_content, unsell_index_content;
	View pack_title, pack_sell_index_content, unpack_index_content;
	View flee_title, flee_index_content;
	View[] title_array, content_array;
	DateRangeSwitchView2 dateRangeSelect;
	private FloatView mFloatView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setMainContent(R.layout.activity_simple_terminal);
		initBaseData();
		initView();
		updateView();
		loadData();
		initLinstener();
		try {
			if (TextUtils.equals(this.getParent().getClass().getName(),
					"com.bonc.mobile.hbmclient.terminal.TerminalGroupActivity")) {
				((View) dateRangeSelect.getParent()).setVisibility(View.GONE);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void initLinstener() {
		// TODO Auto-generated method stub
		LocalBroadcastManager.getInstance(this).registerReceiver(
				new BroadcastReceiver() {

					@Override
					public void onReceive(Context context, Intent intent) {
						boolean flag = intent.getBooleanExtra("month", false);
						// Toast.makeText(context, "flag:" + flag, 1).show();

						switchDateRange(flag);
					}
				}, new IntentFilter("SimpleTerminalActivity.changeDate"));

	}

	@Override
	protected void onDestroy() {
		this.mFloatView.removeFloatView();
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		try {

			if (!TextUtils.equals(this.getParent().getClass().getName(),
					"com.bonc.mobile.hbmclient.terminal.TerminalGroupActivity")) {

				this.mFloatView.unHideFloatView();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	protected void onStop() {
		// this.mFloatView.hideFloatView();
		super.onStop();
	}

	protected void initView() {
		super.initView();

		this.findViewById(R.id.id_share).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						FileUtils.shareScreen(SimpleTerminalActivity.this);
					}
				});
		dateRangeSelect = (DateRangeSwitchView2) findViewById(R.id.id_date_range_select);
		dateRangeSelect.setOnStateChangeListener(new OnStateChangeListener() {
			@Override
			public void toStateDay() {
				switchDateRange(false);
			}

			@Override
			public void toStateMonth() {
				switchDateRange(true);
			}
		});

		purchase_sell_title = findViewById(R.id.purchase_sell_title);
		TextViewUtils.setText(purchase_sell_title, R.id.group_title, "进销形势");
		purchase_sell_title.findViewById(R.id.trend_analyze)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startTrendActivity(PurchaseSellTrendActivity.class);
					}
				});
		purchase_index_content = findViewById(R.id.purchase_index_content);
		purchase_index_content.setBackgroundColor(getResources().getColor(
				R.color.t4cffffff));
		sell_index_content = findViewById(R.id.sell_index_content);
		sell_index_content.setBackgroundColor(getResources().getColor(
				R.color.t4cffffff));

		stock_title = findViewById(R.id.stock_title);
		TextViewUtils.setText(stock_title, R.id.group_title, "库存状态");
		stock_title.findViewById(R.id.trend_analyze).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						startTrendActivity(StockTrendActivity.class);
					}
				});
		stock_index_content = findViewById(R.id.stock_index_content);
		stock_index_content.setBackgroundColor(getResources().getColor(
				R.color.t4cffffff));
		unsell_index_content = findViewById(R.id.unsell_index_content);
		unsell_index_content.setBackgroundColor(getResources().getColor(
				R.color.t4cffffff));

		pack_title = findViewById(R.id.pack_title);
		TextViewUtils.setText(pack_title, R.id.group_title, "深度合约");
		pack_title.findViewById(R.id.trend_analyze).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						startTrendActivity(PackTrendActivity.class);
					}
				});
		pack_sell_index_content = findViewById(R.id.pack_sell_index_content);
		pack_sell_index_content.setBackgroundColor(getResources().getColor(
				R.color.t4cffffff));
		unpack_index_content = findViewById(R.id.unpack_index_content);
		unpack_index_content.setBackgroundColor(getResources().getColor(
				R.color.t4cffffff));

		flee_title = findViewById(R.id.flee_title);
		TextViewUtils.setText(flee_title, R.id.group_title, "窜货情况");
		flee_title.findViewById(R.id.trend_analyze).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						startTrendActivity(FleeTrendActivity.class);
					}
				});
		flee_index_content = findViewById(R.id.flee_index_content);
		flee_index_content.setBackgroundColor(getResources().getColor(
				R.color.t4cffffff));

		title_array = new View[] { purchase_sell_title, stock_title,
				pack_title, flee_title };
		content_array = new View[] { purchase_index_content,
				sell_index_content, stock_index_content, unsell_index_content,
				pack_sell_index_content, unpack_index_content,
				flee_index_content };

		HorizontalScrollViewSynchronizer sy = new HorizontalScrollViewSynchronizer();
		for (int i = 0; i < title_array.length; i++) {
			sy.addView((LinkageHScrollView) title_array[i]
					.findViewById(R.id.index_content));
		}
		for (int i = 0; i < content_array.length; i++) {
			sy.addView((LinkageHScrollView) content_array[i]
					.findViewById(R.id.index_content));
		}

		this.mFloatView = FloatViewFactory.getSingleInstance()
				.createFloatView();

		this.mFloatView.showFloatView(menuCode);
		try {

			if (TextUtils.equals(this.getParent().getClass().getName(),
					"com.bonc.mobile.hbmclient.terminal.TerminalGroupActivity")) {

				this.mFloatView.hideFloatView();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	void resetView() {
		for (int i = 0; i < content_array.length; i++) {
			TextViewUtils.setText(content_array[i], R.id.index_name, Html
					.fromHtml(monthQuery ? group_title_month[i]
							: group_title_day[i]));
			TextViewUtils.setText(content_array[i], R.id.index_value,
					Constant.NULL_VALUE_INSTEAD);
			TextViewUtils.setText(content_array[i], R.id.down_value, "");
			TextViewUtils.setText(content_array[i], R.id.up_value, "");
			((TopNView) content_array[i].findViewById(R.id.model_topn))
					.setAdapter(null);
			((TopNView) content_array[i].findViewById(R.id.area_topn))
					.setAdapter(null);
		}
	}

	Map<String, Object> param;

	protected void loadData() {
		resetView();
		String optime, dataType, areaCode;
		optime = DateUtil
				.formatter(getCalendar().getTime(), DateUtil.PATTERN_8);
		dataType = monthQuery ? "M" : "D";
		areaCode = areaInfoList.get(areaIndex).get("areaCode");
		param = new LinkedHashMap<String, Object>();
		param.put("dataTime", optime);
		param.put("dataType", dataType);
		param.put("areaCode", areaCode);
		param.put(
				"kpis",
				ArrayUtil.join(getKpiCodes(), "|") + "|"
						+ ArrayUtil.join(getMTKpiCodes(), "|") + "|"
						+ ArrayUtil.join(getExtKpiCodes(), "|"));
		param.put("kpiCols", getKpiCols());
		List<Map<String, String>> oc = new ArrayList<Map<String, String>>();
		String[] area_topn_kpi_codes = getATKpiCodes();
		for (int i = 0; i < area_topn_kpi_codes.length; i++) {
			Map<String, String> m = new LinkedHashMap<String, String>();
			m.put("kpiCode", area_topn_kpi_codes[i]);
			m.put("orderColums", getOrderCols(area_topn_kpi_codes[i]));
			m.put("orderType", "desc");
			oc.add(m);
		}
		param.put("orderColums", oc);
		param.put("menuCode",
				getIntent().getStringExtra(MenuEntryAdapter.KEY_MENU_CODE));
		// new Handler().postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// }
		// }, 200);
		// new LoadDataTask(SimpleTerminalActivity.this).execute(
		// ActionConstant.GET_TERM_MAIN_DATA,
		// JsonUtil.toJson(param));

		Map<String, String> man = new HashMap<String, String>();
		man.put("content", Base64.encode(JsonUtil.toJson(param).getBytes()));
		new LoadDataTask(this).execute("/bi/terminalType/getTremLeaderData",
				man);

	}

	protected class LoadDataTask extends HttpRequestTask {
		public LoadDataTask(Context context) {
			super(context);
		}

		@Override
		protected void handleResult(JSONObject result) {
			bindData(result);
		}

	}

	public class HttpRequestTask extends
			com.bonc.mobile.common.net.HttpRequestTask {

		public HttpRequestTask(Context context) {
			super(context, Constant.BASE_PATH);
		}

	}

	protected void bindData(JSONObject json) {
		// data1是一个map格式的json,key是kpi_code,value是对应的指标数据JSONObject
		try {
			checkDataTime(json);
			JSONObject data1, data2;
			data1 = json.getJSONObject("kpiMap");
			data2 = json.getJSONObject("orderResult");
			if (data1.length() == 0) {
				Toast.makeText(this, getString(R.string.no_data),
						Toast.LENGTH_SHORT).show();
				return;
			}
			String[] kpi_codes = getKpiCodes();
			String[] model_topn_kpi_codes = getMTKpiCodes();
			String[] area_topn_kpi_codes = getATKpiCodes();
			for (int i = 0; i < content_array.length; i++) {
				if (i < kpi_codes.length)
					bindMainIndexData(content_array[i], data1, kpi_codes[i],
							i == 6 ? flee_kpi_prefix : null);
				if (i < model_topn_kpi_codes.length) {
					bindModelTopNData(content_array[i], data1,
							model_topn_kpi_codes[i], getTopViewRes(i));
					bindAreaTopNData(content_array[i], data2,
							area_topn_kpi_codes[i], getTopViewRes(i));
				}
			}
			String[] ext_kpi_codes = getExtKpiCodes();
			String[] ext_kpi_prefixs = monthQuery ? ext_kpi_prefixs_month
					: ext_kpi_prefixs_day;
			int i = 0;
			if (monthQuery) {
				bindExtraIndexData(purchase_index_content, data1,
						ext_kpi_codes[i], ext_kpi_prefixs[i], 0, false);
				i++;
				bindExtraIndexData(sell_index_content, data1, ext_kpi_codes[i],
						ext_kpi_prefixs[i], 0, false);
				i++;
				bindExtraIndexData(unsell_index_content, data1,
						ext_kpi_codes[i], ext_kpi_prefixs[i], 1, false);
				i++;
				bindExtraIndexData(unpack_index_content, data1,
						ext_kpi_codes[i], ext_kpi_prefixs[i], 0, false);
			} else {
				bindExtraIndexData(sell_index_content, data1, ext_kpi_codes[i],
						ext_kpi_prefixs[i], 2, false);
				i++;
				bindExtraIndexData(stock_index_content, data1,
						ext_kpi_codes[i], ext_kpi_prefixs[i], 0, true);
				i++;
				bindExtraIndexData(unsell_index_content, data1,
						ext_kpi_codes[i], ext_kpi_prefixs[i], 1, false);
				i++;
				bindExtraIndexData(unpack_index_content, data1,
						ext_kpi_codes[i], ext_kpi_prefixs[i], 0, false);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	void checkDataTime(JSONObject json) {
		Date maxDataTime = DateUtil.getDate(json.optString("maxDataTime"),
				monthQuery ? DateUtil.PATTERN_6 : DateUtil.PATTERN_8);
		if (maxDataTime != null && getCalendar().getTime().after(maxDataTime)) {
			getCalendar().setTime(maxDataTime);
			updateView();
			Toast.makeText(this, "最新的有数据日期是" + dateSelect.getText(),
					Toast.LENGTH_LONG).show();
		}
	}

	void bindMainIndexData(View view, JSONObject json, String kpi_codes,
			String kpi_prefixs) {
		if (kpi_codes == null)
			return;
		StringBuilder buf = new StringBuilder();
		String[] codes = kpi_codes.split(IndexData.CODE_SPE);
		String[] prefixs = null;
		if (kpi_prefixs != null)
			prefixs = kpi_prefixs.split(IndexData.CODE_SPE);
		for (int i = 0; i < codes.length; i++) {
			JSONObject jo = json.optJSONObject(codes[i]);
			if (jo == null)
				continue;
			IndexData data = IndexData.buildUsedBySimpleTerminalActivity(jo);
			if (i > 0)
				buf.append("<br>");
			if (prefixs != null)
				buf.append(prefixs[i]);
			buf.append("<font color='red'>" + data.value_cdi.getValue()
					+ "</font>" + data.value_cdi.getUnit());
		}
		String text = buf.toString();
		if (text.length() == 0)
			text = "--";
		TextViewUtils.setText(view, R.id.index_value, Html.fromHtml(text));
	}

	void bindExtraIndexData(View view, JSONObject json, String kpi_codes,
			String kpi_prefixs, int type, boolean up) {
		if (kpi_codes == null)
			return;
		StringBuilder buf = new StringBuilder();
		String[] codes = kpi_codes.split(IndexData.CODE_SPE), prefixs = kpi_prefixs
				.split(IndexData.CODE_SPE);
		for (int i = 0; i < codes.length; i++) {
			JSONObject jo = json.optJSONObject(codes[i]);
			if (jo == null)
				continue;
			IndexData data = IndexData.buildUsedBySimpleTerminalActivity(jo);
			if (i > 0)
				buf.append("<br>");
			buf.append(prefixs[i]);
			switch (type) {
			case 0:
				buf.append("<font color='red'>" + data.value_cdi.getValue()
						+ "</font>" + data.value_cdi.getUnit());
				break;
			case 1:
				buf.append("<font color='red'>" + data.value_dr_cdi.getValue()
						+ "</font>" + data.value_dr_cdi.getUnit());
				break;
			case 2:
				buf.append("<font color='red'>" + data.value_cdi.getValue()
						+ "</font>" + data.value_cdi.getUnit());
				buf.append(", ");
				buf.append("<font color='red'>" + data.value_dr_cdi.getValue()
						+ "</font>" + data.value_dr_cdi.getUnit());
				break;
			}
		}
		if (up) {
			TextViewUtils.setText(view, R.id.up_value,
					Html.fromHtml(buf.toString()));
		} else {
			TextViewUtils.setText(view, R.id.down_value,
					Html.fromHtml(buf.toString()));
		}
	}

	void bindModelTopNData(View view, JSONObject json, String topn_codes,
			int res) {
		if (topn_codes == null)
			return;
		TopNView model_topn = (TopNView) view.findViewById(R.id.model_topn);
		List<Map<String, String>> data = IndexData.extractData(json,
				topn_codes.split(IndexData.CODE_SPE));
		if (data.size() == 0)
			data = IndexData.getDummyData(3);
		SimpleAdapter model_topn_adapter = new SimpleAdapter(this, data, res,
				getTopNCols(0, null), topn_ids);
		model_topn_adapter.setViewBinder(new KpiValueViewBinder(
				Constant.TERMINAL_SALE_DEFAULT_RULE, Constant.UNIT_TAI));
		model_topn.setAdapter(model_topn_adapter);
	}

	void bindAreaTopNData(View view, JSONObject json, String kpi_code, int res) {
		if (kpi_code == null)
			return;
		JSONArray ja = json.optJSONArray(kpi_code);
		if (ja == null)
			return;
		TopNView area_topn = (TopNView) view.findViewById(R.id.area_topn);
		List<Map<String, String>> data = IndexData.extractData(ja);
		if (data.size() == 0)
			data = IndexData.getDummyData(3);
		SimpleAdapter area_topn_adapter = new SimpleAdapter(this, data, res,
				getTopNCols(1, kpi_code), topn_ids);
		area_topn_adapter
				.setViewBinder(new KpiValueViewBinder(getTopNRule(1, kpi_code),
						getTopNUnit(1, kpi_code), getTopNPrefix(1, kpi_code)));
		area_topn.setAdapter(area_topn_adapter);
	}

	int getTopViewRes(int i) {
		if (i == 6)
			return R.layout.topn_view_item;
		else
			return i % 2 == 1 ? R.layout.topn_view_item_down
					: R.layout.topn_view_item_up;
	}

	String[] getKpiCodes() {
		return monthQuery ? kpi_codes_month : kpi_codes_day;
	}

	String[] getExtKpiCodes() {
		return monthQuery ? ext_kpi_codes_month : ext_kpi_codes_day;
	}

	String[] getMTKpiCodes() {
		return monthQuery ? model_topn_kpi_codes_month
				: model_topn_kpi_codes_day;
	}

	String[] getATKpiCodes() {
		return monthQuery ? area_topn_kpi_codes_month : area_topn_kpi_codes_day;
	}

	String getKpiCols() {
		return monthQuery ? KPICOLS_MONTH : KPICOLS_DAY;
	}

	String getOrderCols(String kpi_code) {
		String col;
		if (IndexData.checkKpiCode(kpi_code, "360|420"))
			col = "cd_myoy";
		else if (IndexData.checkKpiCode(kpi_code, "2780|2860"))
			col = "cm_col";
		else
			col = monthQuery ? "curmonth_value" : "curday_value";
		return col.toUpperCase();
	}

	String[] getTopNCols(int type, String kpi_code) {
		String[] cols = new String[2];
		if (type == 0) {
			cols[0] = "dim_value_name";
			cols[1] = monthQuery ? "curmonth_value" : "curday_value";
		} else {
			cols[0] = "area_name";
			cols[1] = getOrderCols(kpi_code);
		}
		cols[0] = cols[0].toUpperCase();
		cols[1] = cols[1].toUpperCase();
		return cols;
	}

	String getTopNRule(int type, String kpi_code) {
		if (IndexData.checkKpiCode(kpi_code,
				"360|420|2780|2860|5990|6000|4880|5420|5930"))
			return Constant.TERMINAL_SALE_PERCENT_RULE;
		else
			return Constant.TERMINAL_SALE_DEFAULT_RULE;
	}

	String getTopNUnit(int type, String kpi_code) {
		if (IndexData.checkKpiCode(kpi_code,
				"360|420|2780|2860|5990|6000|4880|5420|5930"))
			return Constant.UNIT_PERCENT;
		else
			return Constant.UNIT_TAI;
	}

	String getTopNPrefix(int type, String kpi_code) {
		String prefix = "";
		if (type == 1) {
			if (IndexData.checkKpiCode(kpi_code, "360|420"))
				prefix = "月同比:";
			else if (IndexData.checkKpiCode(kpi_code, "2780|2860"))
				prefix = "月环比:";
			else if (IndexData.checkKpiCode(kpi_code, "5990|6000"))
				prefix = "滞销率:";
			else if (IndexData.checkKpiCode(kpi_code, "4880|5420"))
				prefix = "拆包率:";
			else if ("5930".equals(kpi_code))
				prefix = "省内窜出率:";
		}
		return prefix;
	}

	void switchDateRange(boolean toMonth) {
		monthQuery = toMonth;
		updateView();
		loadData();
	}

	protected void updateView() {
		super.updateView();
		if (monthQuery) {
			flee_title.setVisibility(View.VISIBLE);
			flee_index_content.setVisibility(View.VISIBLE);
		} else {
			flee_title.setVisibility(View.GONE);
			flee_index_content.setVisibility(View.GONE);
		}
	}

	protected void chooseDate() {

		if (monthQuery) {

			UIUtil.showDatePickerDialogWithoutDay(this,
					new OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							getCalendar().set(year, monthOfYear, dayOfMonth);
							updateView();
							loadData();
						}
					}, getCalendar());
		} else {
			super.chooseDate();
		}
	}

	void startTrendActivity(Class<?> cls) {
		String optime, areaCode;
		optime = DateUtil.formatter(getCalendar().getTime(),
				monthQuery ? DateUtil.PATTERN_6 : DateUtil.PATTERN_8);
		areaCode = areaInfoList.get(areaIndex).get("areaCode");
		Intent intent = new Intent(this, cls);
		intent.putExtra("dataTime", optime);
		intent.putExtra("monthQuery", monthQuery);
		intent.putExtra("areaCode", areaCode);
		startActivity(intent);
	}

}
