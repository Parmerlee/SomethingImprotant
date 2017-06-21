package com.bonc.mobile.hbmclient.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter.ColumnDisplyInfo;
import com.bonc.mobile.hbmclient.data.KpiInfo;
import com.bonc.mobile.hbmclient.data.MenuColumnInfo;
import com.bonc.mobile.hbmclient.net.ConnectManager;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.HttpUtil;
import com.bonc.mobile.hbmclient.util.LogUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.ACLineGraphView;
import com.bonc.mobile.hbmclient.view.KpiHasChartRightView;
import com.bonc.mobile.hbmclient.view.LineGraphView.OnBrickMoveListener;
import com.bonc.mobile.hbmclient.view.ListViewSetting;
import com.bonc.mobile.hbmclient.view.adapter.KpiPeriodLeftAdapter;
import com.bonc.mobile.hbmclient.view.adapter.KpiPeriodRightAdapter;

public class KPIPeriodActivity extends BaseActivity {

	private TextView graph_title_name;
	// 趋势图指标
	private int[] graph_title_descId = { R.id.zhibiao_desc1,
			R.id.zhibiao_desc2, R.id.zhibiao_desc3 };
	private int[] graph_title_valueId = { R.id.zhibiao_value_1,
			R.id.zhibiao_value_2, R.id.zhibiao_value_3 };
	private int[] graph_title_unitId = { R.id.zhibiao_unit_1,
			R.id.zhibiao_unit_2, R.id.zhibiao_unit_3 };
	// 列表指标
	// 标题栏名（只包括右侧滑动部分的指标名称）
	private String[] titleName;
	// 左侧列表
	private ListView period_left_listView;
	// 右侧列表
	private ListView period_right_listView;
	// 列表相应的Adapter
	private KpiPeriodLeftAdapter leftAdapter;
	private KpiPeriodRightAdapter rightAdapter;
	// 相关滑动用组件
	private ListViewSetting listViewSetting;
	// 接收传入的数据
	private TextView mChartTitleView1;
	private String mLastDate;
	private LinearLayout linearlayout;
	// 折线图数据
	private String[] dateString;
	private String[] lineDateString;
	private double[] dataline1;
	private double[] dataline2;
	private DataTask mTask;

	// 传递的参数.
	private String areaId; // 用户选择的地区ID 没有则为空
	private String areaName; // 地区名称.
	private String opTime; // 操作日期.
	private String dataType; // 数据类型.
	private String kpiCode; // 指标编码
	private String kpiName; // 指标名称.
	private String complexKey; // 复合维度.
	private String thirdDimValueName;// 用户选择的第三维度名称
	private KpiInfo kpiInfo;// 指标信息.
	private Map<String, MenuColumnInfo> colInfoMap; // 菜单列和列的基本信息.
	private String menuCode; // 菜单id
	private String thirdDimeName;// 第三名称.
	private List<Map<String, String>> allColumnList; // 所有列.
	private Map<String, String> param;
	private String columnString;
	private List<Map<String, String>> data; // 数据

	private String colKeyList[]; // 列名称.
	private String begin; // 开始时间.
	private Map<String, String> menuAddInfo;
	private Map<String, String> areaInfo;

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {

		setTheme(android.R.style.Theme_NoTitleBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kpi_peroid_lay);

		RelativeLayout rl = (RelativeLayout) findViewById(R.id.parent);
		rl.setBackgroundDrawable(WatermarkImage.getWatermarkDrawable());

		Intent intent = getIntent();
		areaId = intent.getStringExtra("areaId");
		areaName = intent.getStringExtra("areaName");
		opTime = intent.getStringExtra("opTime");
		dataType = intent.getStringExtra("dataType");
		kpiCode = intent.getStringExtra("kpiCode");

		kpiName = intent.getStringExtra("kpiName");
		complexKey = intent.getStringExtra("complexKey");
		thirdDimValueName = intent.getStringExtra("thirdDimValueName");
		menuCode = intent.getStringExtra("menuCode");
		kpiInfo = (KpiInfo) intent.getSerializableExtra("kpiInfo");
		colInfoMap = (Map<String, MenuColumnInfo>) intent
				.getSerializableExtra("colInfoMap");
		allColumnList = (List<Map<String, String>>) intent
				.getSerializableExtra("allColumnList");

		String acTitle = "";
		if (areaName != null) {
			acTitle += areaName;
		}

		if (kpiName != null) {
			if ("".equals(areaId)) {
				acTitle += kpiName;
			} else {
				acTitle += ">" + kpiName;
			}
		}

		if (thirdDimValueName != null) {
			acTitle += ">" + thirdDimValueName;
		}

		graph_title_name = (TextView) findViewById(R.id.graph_title_name);
		graph_title_name.setText(acTitle);

		if (opTime != null && !opTime.equals("")) {
			if (dataType.equals(Constant.DATA_TYPE_DAY)) {
				begin = DateUtil.dayBefore(opTime, DateUtil.PATTERN_8,
						Constant.KPI_RECENT_DAY);
			} else {
				begin = DateUtil.monthBefore(opTime, DateUtil.PATTERN_6,
						Constant.KPI_RECENT_MONTH);
			}
		}

		titleName = new String[allColumnList.size() + 1];
		colKeyList = new String[allColumnList.size() + 1];
		titleName[0] = getResources().getString(R.string.kpi_period_left_title);
		colKeyList[0] = getResources().getString(R.string.kpi_period_left_key);
		columnString = "";
		int len = allColumnList.size();
		for (int i = 0; i < len; i++) {

			if (!"".equals(columnString)) {
				columnString += Constant.DEFAULT_CONJUNCTION;
			}
			String col = allColumnList.get(i).get("col")
					.toLowerCase(Locale.CHINA);
			colKeyList[i + 1] = col;
			columnString += col;
			titleName[i + 1] = allColumnList.get(i).get("colname")
					.toLowerCase(Locale.CHINA);
		}

		if (menuCode == null || "".equals(menuCode)) {
			LogUtil.error(this.getClass().toString(), "传入的菜单为空!");
			finish();
			return;
		}

		menuAddInfo = new BusinessDao().getMenuAddInfo(menuCode);
		areaInfo = new BusinessDao().getAreaBaseInfo(areaId);

		initWidget();
		initData();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	public void initWidget() {

		mChartTitleView1 = (TextView) findViewById(R.id.financial_graph_view_title);
		ismonorday();
		// graph_title_name = (TextView)findViewById(R.id.graph_title_name);
		// graph_title_name.setText(modulename);

		period_left_listView = (ListView) findViewById(R.id.mon_graph_left_listview);
		period_right_listView = (ListView) findViewById(R.id.mon_graph_right_listview);
		listViewSetting = new ListViewSetting();

	}

	// 初始化横屏控件
	public void initLandscapeWidget() {
		setContentView(R.layout.month_dev_graph_h);
		LinearLayout ll = (LinearLayout) findViewById(R.id.parent1);
		ll.setBackgroundDrawable(WatermarkImage.getWatermarkDrawable());
		ismonorday();

	}

	/**
	 * 
	 */
	private void ismonorday() {
		linearlayout = (LinearLayout) findViewById(R.id.chart);

		mLastDate = "197001";

	}

	public void initData() {
		if (!ConnectManager.isConnected()) {
			Toast.makeText(this, R.string.connection_not_open,
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (mTask != null)
			mTask.cancel(true);
		mTask = new DataTask();
		mTask.execute();
	}

	// 画图
	private void drawPicture() {
		linearlayout.removeAllViews();
		linearlayout.addView(initGraphView());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
	}

	/**
	 * 初始化曲线图
	 * 
	 * @return
	 */
	private ACLineGraphView initGraphView() {
		ACLineGraphView graphView = new ACLineGraphView(this, dateString,
				lineDateString, dataline1, dataline2);
		if (data == null || data.size() == 0) {
			return graphView;
		}

		if (data.size() != 0) {
			for (int i = 0, n = graph_title_descId.length; i < n
					&& i < colKeyList.length - 1; i++) {
				TextView tv = (TextView) findViewById(graph_title_valueId[i]);
				TextView tvU = (TextView) findViewById(graph_title_unitId[i]);
				String sValue = data.get(data.size() - 1)
						.get(colKeyList[i + 1]);
				MenuColumnInfo mci = colInfoMap.get(colKeyList[i + 1]);
				ColumnDisplyInfo cdi = ColumnDataFilter.getInstance().filter(
						mci, sValue, kpiInfo);
				if (cdi != null) {
					if (cdi.getValue() != null
							&& !cdi.getValue().equals("null")
							&& cdi.getUnit() != null
							&& !cdi.getUnit().equals("null")) {
						tv.setText(cdi.getValue()/* +cdi.getUnit() */);
						tv.setTextColor(cdi.getColor());
						tvU.setText(cdi.getUnit());
					}
				}
			}
		}

		OnBrickMoveListener obml = new OnBrickMoveListener() {
			@Override
			public void onMove(final int graphDataIndex) {

				handler.post(new Runnable() {
					@Override
					public void run() {
						// final String date =
						// trendlist.get(graphDataIndex).getThird_curmon_value();
						final String date = data.get(graphDataIndex).get(
								"op_time");
						if (!mLastDate.equals(date)) {
							if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
									&& android.os.Build.VERSION.SDK_INT > 7)
								period_left_listView
										.smoothScrollToPosition(data.size()
												- graphDataIndex - 1);

							mLastDate = date;
							for (int i = 0, n = graph_title_descId.length; i < n
									&& i < colKeyList.length - 1; i++) {
								TextView tv = (TextView) findViewById(graph_title_valueId[i]);
								TextView tvU = (TextView) findViewById(graph_title_unitId[i]);

								String sValue = data.get(graphDataIndex).get(
										colKeyList[i + 1]);
								MenuColumnInfo mci = colInfoMap
										.get(colKeyList[i + 1]);

								ColumnDisplyInfo cdi = ColumnDataFilter
										.getInstance().filter(mci, sValue,
												kpiInfo);
								if (cdi != null) {
									if (cdi.getValue() != null
											&& !cdi.getValue().equals("null")
											&& cdi.getUnit() != null
											&& !cdi.getUnit().equals("null")) {
										tv.setText(cdi.getValue());
										tvU.setText(cdi.getUnit());
										tv.setTextColor(cdi.getColor());
									}
								} else {
									tv.setText("");
									tvU.setText("");
									tv.setTextColor(0xff000000);
								}
							}

						}
					}
				});
			}

			@Override
			public void onStop(final int graphDataIndex) {

				// TODO Auto-generated method stub
				if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							leftAdapter.changeColor(data.size()
									- graphDataIndex - 1);
							rightAdapter.changeColor(data.size()
									- graphDataIndex - 1);
						}
					});
				}
			}
		};
		graphView.setOnBrickMoveListener(obml);
		return graphView;
	}

	private class DataTask extends AsyncTask<Void, Void, Object> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showMessageDailog(KPIPeriodActivity.this);
		}

		@Override
		protected Object doInBackground(Void... params) {

			if (param == null) {
				param = new HashMap<String, String>();

				param.put("cols", columnString);
				param.put("areacode", areaId);
				param.put("dimkey", complexKey);
				param.put("kpicode", kpiCode);
				param.put("datatype", dataType);
				param.put("datatable",
						new BusinessDao().getMenuColDataTable(menuCode));
				param.put("optime", opTime);
			}

			param.put("starttime", begin);
			param.put("endtime", opTime);

			String action = menuCode.equals(ChannelMainActivity.MENU_CODE) ? ActionConstant.CUSTOMER_RETENTION_VALUE
					: ActionConstant.KPI_PERIOD_DATA;
			String resultString = HttpUtil.sendRequest(action, param);
			if (resultString == null || "".equals(resultString)) {
				data = null;
				return null;
			}
			try {
				JSONArray bdArray = new JSONArray(resultString);

				if (bdArray == null || bdArray.length() == 0) {
					return null;
				}
				int len = bdArray.length();

				if (bdArray == null || bdArray.length() == 0) {
					return null;
				}

				dateString = new String[len];
				lineDateString = new String[len];
				String orgUnit = "";
				if (allColumnList.size() >= 0 && colInfoMap != null) {
					MenuColumnInfo cinfo = colInfoMap.get(allColumnList.get(0)
							.get("col").toLowerCase(Locale.CHINA));
					if (cinfo != null) {
						if ("-1".equals(cinfo.getColRule()) && kpiInfo != null) {
							orgUnit = kpiInfo.getKpiUnit();
						} else {
							orgUnit = cinfo.getColUnit();
						}

					}

				}
				int scale = 1;
				if (orgUnit.contains("%")) {
					scale = 100;
				}
				dataline1 = new double[len];
				dataline2 = new double[len];
				if (data == null)
					data = new ArrayList<Map<String, String>>();
				int clen = colKeyList.length;

				for (int i = 0; i < len; i++) {
					Map<String, String> dataMap = new HashMap<String, String>();

					String optime = bdArray.optJSONObject(i).optString(
							"op_time");
					for (int k = 0; k < clen; k++) {
						dataMap.put(colKeyList[k], bdArray.optJSONObject(i)
								.optString(colKeyList[k]));
					}

					data.add(dataMap);

					if (dataType.equals(Constant.DATA_TYPE_MONTH)) {
						dateString[i] = DateUtil.oneStringToAntherString(
								optime, "yyyyMM", "MM");
					} else {
						dateString[i] = DateUtil.oneStringToAntherString(
								optime, "yyyyMMdd", "dd");
					}
					lineDateString[i] = optime;

					try {
						dataline1[i] = bdArray.optJSONObject(i).getDouble(
								colKeyList[1])
								* scale;
					} catch (Exception e) {
						dataline1[i] = 0;
						e.printStackTrace();
					}

					try {
						dataline2[i] = bdArray.optJSONObject(i).getDouble(
								colKeyList[1])
								* scale;
					} catch (Exception e) {
						dataline2[i] = 0;
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				data = null;
				e.printStackTrace();
			}
			return data;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			pDialog.dismiss();
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (data != null && data.size() != 0) {
				drawPicture();
				changeTitle(titleName);
				leftAdapter = new KpiPeriodLeftAdapter(KPIPeriodActivity.this,
						data, colKeyList);
				rightAdapter = new KpiPeriodRightAdapter(
						KPIPeriodActivity.this, data, kpiInfo, colKeyList,
						colInfoMap);
				period_left_listView.setAdapter(leftAdapter);
				period_right_listView.setAdapter(rightAdapter);
				listViewSetting.setListViewOnTouchAndScrollListener(
						period_left_listView, period_right_listView);
			} else {
				toast(KPIPeriodActivity.this, getString(R.string.no_data));
			}
			pDialog.dismiss();
		}
	}

	// 变更标题栏内容
	private void changeTitle(String[] namesRight) {
		for (int i = 0, n = graph_title_descId.length; i < n
				&& i < colKeyList.length - 1; i++) {
			TextView tvT = (TextView) findViewById(graph_title_descId[i]);
			tvT.setText(namesRight[i + 1]);
			TextView tv = (TextView) findViewById(graph_title_valueId[i]);
			TextView tvU = (TextView) findViewById(graph_title_unitId[i]);
			String sValue = data.get(data.size() - 1).get(colKeyList[i + 1]);
			MenuColumnInfo mci = colInfoMap.get(colKeyList[i + 1]);
			ColumnDisplyInfo cdi = ColumnDataFilter.getInstance().filter(mci,
					sValue, kpiInfo);
			if (cdi != null) {
				if (cdi.getValue() != null && !cdi.getValue().equals("null")
						&& cdi.getUnit() != null
						&& !cdi.getUnit().equals("null")) {
					tv.setText(cdi.getValue());
					tvU.setText(cdi.getUnit());
					tv.setTextColor(cdi.getColor());
				}
			}
		}
		// TODO Auto-generated method stub
		TextView tv = (TextView) findViewById(R.id.per_left_title);
		tv.setText(namesRight[0]);
		LinearLayout tl = (LinearLayout) findViewById(R.id.title_lineralayout);
		tl.setBackgroundResource(R.drawable.glay_list_title);
		mChartTitleView1.setText(namesRight[0]);
		LinearLayout layRightTitle = (LinearLayout) findViewById(R.id.lineralayout_right_title);
		String str[] = new String[namesRight.length - 1];
		for (int i = 0; i < str.length; i++) {
			str[i] = namesRight[i + 1];
		}

		KpiHasChartRightView ret = new KpiHasChartRightView(
				KPIPeriodActivity.this, R.layout.title_right_item, str, 1,
				getWindowManager().getDefaultDisplay().getWidth()
						- this.getResources().getDimension(
								R.dimen.zhl_left_column_width));

		layRightTitle.removeAllViews();
		layRightTitle.addView(ret);
		ret.setAllBackgroundByID(R.drawable.glay_list_title);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, ret.getItemH());// (LayoutParams)
																		// ret.getLayoutParams();
		ret.setLayoutParams(lp);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 当点击某一行记录时.
	 */
	public void onViewItemClick(int position) {

		String showTop5Flag = menuAddInfo.get("showTop5Flag");
		String areaLevel = areaInfo.get("areaLevel");

		if ("1".equals(showTop5Flag) && !"1".equals(areaLevel)) {
			Intent intent = new Intent(KPIPeriodActivity.this,
					KPIOrderTop5Activity.class);
			intent.putExtra("parentAreaId", areaInfo.get("parentAreaCode"));
			intent.putExtra("areaId", areaId);
			intent.putExtra("areaName", areaName);
			intent.putExtra("opTime", data.get(position).get(colKeyList[0]));
			intent.putExtra("dataType", dataType);
			intent.putExtra("kpiCode", kpiCode);
			intent.putExtra("kpiName", kpiName);
			intent.putExtra("dimkey", complexKey);
			intent.putExtra("menuCode", menuCode);
			intent.putExtra("top5OrderCols", menuAddInfo.get("top5OrderCols"));
			intent.putExtra("top5OrderNames", menuAddInfo.get("top5OrderNames"));
			intent.putExtra("datatable",
					new BusinessDao().getMenuColDataTable(menuCode));
			intent.putExtra("allColumnList", (Serializable) allColumnList);
			intent.putExtra("kpiInfo", kpiInfo);
			intent.putExtra("colInfoMap", (Serializable) colInfoMap);
			startActivity(intent);
		}

	}

}
