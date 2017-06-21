package com.bonc.mobile.hbmclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.data.KpiInfo;
import com.bonc.mobile.hbmclient.data.MenuColumnInfo;
import com.bonc.mobile.hbmclient.util.HttpUtil;
import com.bonc.mobile.hbmclient.util.StringUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.KpiHasChartRightView;
import com.bonc.mobile.hbmclient.view.ListViewSetting;
import com.bonc.mobile.hbmclient.view.adapter.KPIOrderLeftAdapter;
import com.bonc.mobile.hbmclient.view.adapter.KPIOrderRightAdapter;

public class KPIOrderTop5Activity extends BaseActivity {

	private Handler myHandler = new Handler();
	public ProgressDialog pDialog;
	private KpiInfo kpiInfo;
	private Map<String, MenuColumnInfo> colInfoMap;
	private List<Map<String, String>> allColumnList;
	private String columnString;
	private String[] colKeyList;
	private String[] titleName;
	private List<Map<String, String>> groupInfo; // 分组信息
	private Map<String, List<Map<String, String>>> subInfo; // 每个组的字信息.
	private ExpandableListView leftListView;
	private ExpandableListView rightListView;
	private ListViewSetting listViewSetting;
	private String areaId;
	private String opTime;
	private String dataType;
	private String kpiCode;
	private String kpiName;
	private String datatable;
	private String top5OrderCols;
	private String top5OrderNames;
	private KPIOrderLeftAdapter leftAdapter;
	private KPIOrderRightAdapter rightAdapter;
	private String[] orderCols;
	private String[] orderNames;
	private String dimkey;
	private String parentAreaId;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_NoTitleBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kpi_order_top5);
		LinearLayout ll = (LinearLayout) findViewById(R.id.parent);
		ll.setBackgroundDrawable(WatermarkImage.getWatermarkDrawable());

		Intent intent = getIntent();
		kpiInfo = (KpiInfo) intent.getSerializableExtra("kpiInfo");
		colInfoMap = (Map<String, MenuColumnInfo>) intent
				.getSerializableExtra("colInfoMap");
		allColumnList = (List<Map<String, String>>) intent
				.getSerializableExtra("allColumnList");
		parentAreaId = intent.getStringExtra("parentAreaId");
		areaId = intent.getStringExtra("areaId");
		opTime = intent.getStringExtra("opTime");
		dataType = intent.getStringExtra("dataType");
		kpiCode = intent.getStringExtra("kpiCode");
		kpiName = intent.getStringExtra("kpiName");
		datatable = intent.getStringExtra("datatable");
		dimkey = intent.getStringExtra("dimkey");
		top5OrderCols = intent.getStringExtra("top5OrderCols");
		top5OrderNames = intent.getStringExtra("top5OrderNames");

		if ("".equals(StringUtil.nullToString(top5OrderCols))
				|| "".equals(StringUtil.nullToString(top5OrderNames))) {
			finish();
			return;
		}

		orderCols = top5OrderCols.split("\\" + Constant.DEFAULT_CONJUNCTION);
		orderNames = top5OrderNames.split("\\" + Constant.DEFAULT_CONJUNCTION);

		int len = allColumnList.size();
		colKeyList = new String[allColumnList.size()];
		titleName = new String[allColumnList.size()];
		columnString = "";
		for (int i = 0; i < len; i++) {

			if (!"".equals(columnString)) {
				columnString += Constant.DEFAULT_CONJUNCTION;
			}
			String col = allColumnList.get(i).get("col")
					.toLowerCase(Locale.CHINA);
			colKeyList[i] = col;
			columnString += col;
			titleName[i] = allColumnList.get(i).get("colname")
					.toLowerCase(Locale.CHINA);
		}

		listViewSetting = new ListViewSetting();
		pDialog = new ProgressDialog(KPIOrderTop5Activity.this);

		initWegdet();
		setTitle(titleName);
		initData();

	}

	void initWegdet() {

		TextView logoTitle = (TextView) findViewById(R.id.logo_title);
		logoTitle.setText(kpiName + ">" + opTime + ">" + "top5排名");

		leftListView = (ExpandableListView) findViewById(R.id.data_left_list_view);
		rightListView = (ExpandableListView) findViewById(R.id.data_right_list_view);

		leftAdapter = new KPIOrderLeftAdapter(KPIOrderTop5Activity.this);
		leftListView.setAdapter(leftAdapter);

		rightAdapter = new KPIOrderRightAdapter(KPIOrderTop5Activity.this,
				kpiInfo, colInfoMap, colKeyList);
		rightListView.setAdapter(rightAdapter);

	}

	/**
	 * 设置列名称.
	 * 
	 * @param titleNames
	 */
	public void setTitle(String[] titleNames) {

		TextView leftTitle = (TextView) findViewById(R.id.top5_left_column_title);
		leftTitle.setText(getResources()
				.getString(R.string.mon_area_left_title));

		LinearLayout llLayout = (LinearLayout) findViewById(R.id.top5_right_column_title);
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		KpiHasChartRightView ret = new KpiHasChartRightView(
				KPIOrderTop5Activity.this, R.layout.title_right_item,
				titleNames, 0, screenWidth);

		llLayout.removeAllViews();
		llLayout.addView(ret);
		ret.setAllBackgroundByID(R.drawable.glay_list_title);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, ret.getItemH());// (LayoutParams)
																		// ret.getLayoutParams();
		ret.setLayoutParams(lp);

	}

	// 初始化数据.
	void initData() {

		showMessageDailog();

		new Thread(new Runnable() {

			@Override
			public void run() {

				makeData();
				myHandler.post(new Runnable() {
					@Override
					public void run() {
						leftAdapter.setGroupInfo(groupInfo);
						leftAdapter.setSubInfo(subInfo);

						rightAdapter.setGroupInfo(groupInfo);
						rightAdapter.setSubInfo(subInfo);

						listViewSetting.expandListViewAndSetNotClickGroup(
								leftListView, rightListView, leftAdapter,
								rightAdapter);
						listViewSetting.setListViewOnTouchAndScrollListener(
								leftListView, rightListView);

						leftAdapter.notifyDataSetChanged();
						rightAdapter.notifyDataSetChanged();

						if (groupInfo == null || groupInfo.size() == 0) {
							showEmptyMessage();
						}

						pDialog.dismiss();
					}
				});
			}
		}).start();

	}

	// 测试数据

	void makeData() {

		Map<String, String> paramMap = new HashMap<String, String>();

		paramMap.put("optime", opTime);
		paramMap.put("dimkey", dimkey);
		paramMap.put("cols", columnString);
		paramMap.put("parentareacode", parentAreaId);
		paramMap.put("selfareacode", areaId);
		paramMap.put("kpicode", kpiCode);
		paramMap.put("datatype", dataType);
		paramMap.put("datatable", datatable);
		paramMap.put("rankcolumns", top5OrderCols);

		String reply = HttpUtil.sendRequest(ActionConstant.KPI_ORDER_TOP5,
				paramMap);

		if (reply == null || "".equals(reply)) {
			groupInfo = null;
			return;
		}

		JSONObject jsonObject = null;

		try {
			jsonObject = new JSONObject(reply);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (jsonObject == null) {
			groupInfo = null;
			return;
		}

		groupInfo = new ArrayList<Map<String, String>>();

		for (int i = 0; i < orderCols.length && i < orderNames.length; i++) {
			Map<String, String> group = new HashMap<String, String>();
			group.put("groupId", orderCols[i]);
			group.put("groupName", orderNames[i]);

			JSONArray jsonArray = jsonObject.optJSONArray(orderCols[i]);

			if (jsonArray == null || jsonArray.length() == 0) {
				continue;
			}

			if (subInfo == null) {
				subInfo = new HashMap<String, List<Map<String, String>>>();
			}

			List<Map<String, String>> subdatalist = new ArrayList<Map<String, String>>();

			int alen = jsonArray.length();

			for (int ai = 0; ai < alen; ai++) {

				Map<String, String> rowMap = new HashMap<String, String>();
				for (int m = 0; m < colKeyList.length; m++) {
					rowMap.put(colKeyList[m], jsonArray.optJSONObject(ai)
							.optString(colKeyList[m]));
				}

				rowMap.put(
						getString(R.string.mon_area_left_key),
						jsonArray.optJSONObject(ai).optString(
								getString(R.string.mon_area_left_key)));
				subdatalist.add(rowMap);
			}

			subInfo.put(orderCols[i], subdatalist);
			groupInfo.add(group);
		}
	}

	public void showMessageDailog() {
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setCancelable(true);
		pDialog.show();
		pDialog.setContentView(R.layout.progress);
	}

	public void showEmptyMessage() {
		Toast.makeText(KPIOrderTop5Activity.this, "没有查询到相关数据!",
				Toast.LENGTH_LONG).show();
	}

}
