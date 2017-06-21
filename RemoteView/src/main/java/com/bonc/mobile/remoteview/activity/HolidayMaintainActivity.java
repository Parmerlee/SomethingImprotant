package com.bonc.mobile.remoteview.activity;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.Constant;
import com.bonc.mobile.common.User;
import com.bonc.mobile.common.activity.BaseMultiDataActivity;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.util.DateUtil;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.common.view.SimpleListView;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.adapter.BLChartAdapter;
import com.bonc.mobile.remoteview.adapter.DialAdapter;
import com.bonc.mobile.remoteview.adapter.KpiAdapter;
import com.bonc.mobile.remoteview.adapter.TopNListAdapter;
import com.bonc.mobile.remoteview.common.ConfigLoader;
import com.bonc.mobile.remoteview.common.RemoteUtil;
import com.bonc.mobile.remoteview.common.Utils;

public class HolidayMaintainActivity extends BaseMultiDataActivity implements
		OnClickListener {

	private SimpleListView BLChart_listview, TopNList_listview,
			kpilist1_listview, kpilist2_listview, dial_listview;
	private Activity activity;

	public int width;

	private Map<String, String> param;

	private String currentTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_holiday_maintain);
		initView();
		loadData();

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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.reflesh:
			firstQuery = true;
			loadData();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		Utils.setTime(null);
		Utils.setAreaname(null);
		Utils.setAreacode(null);
	}

	@Override
	protected void initView() {
		super.initView();

		BLChart_listview = (SimpleListView) findViewById(R.id.holiday_maintain_listview);

		TextViewUtils.setText(this, R.id.title, ConfigLoader.getInstance(this)
				.getMenuName(menuCode));
		activity = this;

		this.findViewById(R.id.reflesh).setOnClickListener(this);
		this.findViewById(R.id.reflesh).setVisibility(View.VISIBLE);

		TopNList_listview = (SimpleListView) findViewById(R.id.horizontal_simplelistview);
		kpilist1_listview = (SimpleListView) findViewById(R.id.horizontal_kpilist1);
		kpilist2_listview = (SimpleListView) findViewById(R.id.horizontal_kpilist2);
		dial_listview = (SimpleListView) findViewById(R.id.horizontal_dial);

		WindowManager wm = (WindowManager) HolidayMaintainActivity.this
				.getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth();

		Utils.setWidth(width);

		findViewById(R.id.root).setBackgroundColor(Color.WHITE);
		TextView water = (TextView) this.findViewById(R.id.date_water);
		water.setVisibility(View.VISIBLE);
		water.setText("4A:" + User.getInstance().userCode);

	}

	@Override
	protected void loadData() {

		if (firstQuery) {
			param = new LinkedHashMap<String, String>();
			param.put("clickCode", menuCode);
			param.put("appType", "ANDROID_RVS");
			param.put("clickType", "MENU");

			new LoadDataTask2(this, Constant.RV_PATH, 6).execute(
					"/holiday/getAreas", param);
		} else {
			currentTime = DateUtil.formatter(date_button.getDate(),
					"yyyyMMddHH");
			if (!AppConstant.SEC_ENH)
				Log.d("AAAA", "time:" + currentTime);
			Utils.setTime(currentTime);
			Utils.setAreaname(area_button.getChoiceValue());
			doLoading();
		}

	}

	public LinearLayout.LayoutParams createParams(int i) {

		if (i > 3) {
			return new LinearLayout.LayoutParams(width / 3, width / 3);
		} else {

			return new LinearLayout.LayoutParams(width / i, width / i);
		}
	}

	@Override
	protected void bindData(JSONObject result, int requestCode) {
		switch (requestCode) {
		case 1:
			// 折线图
			renderBLChartList(result);
			break;
		case 2:
			// 仪表盘
			renderDashboardList(result);
			break;
		case 3:
			// 第一个指标列表

			renderKpiList1(result);
			break;
		case 4:
			// 第二个指标
			renderKpiList2(result);
			break;
		case 5:
			// top 5

			renderTopNList(result);
			break;
		case 6:
			// 时间日期 若有一个参数没有就不用再请求折线图接口了
			redderTimeAreas(result);
		}
	}

	void renderBLChartList(JSONObject result) {

		BLChartAdapter adapter = new BLChartAdapter(activity,
				R.layout.holiday_simple_item, createParams(3), result);
		adapter.setActivity(activity);
		adapter.setTime(currentTime);
		if (adapter.getCount() > 0) {
			BLChart_listview.setVisibility(View.VISIBLE);
			BLChart_listview.setAdapter(adapter);
		} else {
			BLChart_listview.setVisibility(View.GONE);
		}

	}

	void renderDashboardList(JSONObject result) {

		DialAdapter adapter = new DialAdapter(activity,
				R.layout.holiday_diallv_item, createParams(6), result);

		adapter.setTime(currentTime);

		if (adapter.getCount() > 0) {
			((View) dial_listview.getParent()).setVisibility(View.VISIBLE);
			dial_listview.setAdapter(adapter);
		} else {
			((View) dial_listview.getParent()).setVisibility(View.GONE);
		}

	}

	KpiAdapter kpiadapter;

	void renderKpiList1(JSONObject result) {

		kpiadapter = new KpiAdapter(activity, R.layout.holiday_kiplv_item,
				createParams(3), result);
		kpiadapter.setTime(currentTime);
		kpiadapter.setClick(true);

		if (kpiadapter.getCount() > 0) {
			((View) kpilist1_listview.getParent()).setVisibility(View.VISIBLE);
			kpilist1_listview.setAdapter(kpiadapter);
		} else {
			((View) kpilist1_listview.getParent()).setVisibility(View.GONE);
		}

	}

	void renderKpiList2(JSONObject result) {
		kpiadapter.setClick(false);
		kpiadapter = new KpiAdapter(activity, R.layout.holiday_kiplv_item,
				createParams(3), result);
		kpiadapter.setTime(currentTime);


		if (kpiadapter.getCount() > 0) {
			((View) kpilist2_listview.getParent()).setVisibility(View.VISIBLE);
			kpilist2_listview.setAdapter(kpiadapter);
		} else {
			((View) kpilist2_listview.getParent()).setVisibility(View.GONE);
		}

	}

	void renderTopNList(JSONObject result) {

		TopNListAdapter topadapter = new TopNListAdapter(activity,
				R.layout.holiday_toplv_item_layout, result);
		if (topadapter.getCount() > 0) {
			((View) TopNList_listview.getParent()).setVisibility(View.VISIBLE);
			TopNList_listview.setAdapter(topadapter);
		} else {
			((View) TopNList_listview.getParent()).setVisibility(View.GONE);
		}
	}

	void redderTimeAreas(JSONObject result) {
		if (!result.isNull("maxTime") && !result.isNull("areas")) {
			date_button.setWithTime(true);

			date_button.setPattern("yyyy/MM/dd HH时");

			currentTime = result.optString("maxTime");
			Utils.setTime(currentTime);
			Utils.setmTime_temp(currentTime);

			date_button.setDate(DateUtil.getDate(currentTime, "yyyyMMddHH"));
			if (Utils.getAreacode() == null) {
				area_button
						.setData(JsonUtil.toList(JsonUtil.optJSONArray(result,
								"areas")), "AREA_NAME", "AREA_CODE");

				Utils.setAreacode(JsonUtil.optJSONArray(result, "areas"));
				Utils.setAreaname(area_button.getChoiceValue());
			}
			doLoading();

		}
	}

	private void doLoading() {
		// TODO Auto-generated method stub

		param = new LinkedHashMap<String, String>();
		param.put("queryDate",
				DateUtil.formatter(date_button.getDate(), "yyyyMMddHH"));
		param.put("areaCode", area_button.getChoiceValue());

		param.put("clickCode", menuCode);
		param.put("appType", "ANDROID_RVS");
		param.put("clickType", "MENU");

		new LoadDataTask2(this, Constant.RV_PATH, 1).execute(
				"/holiday/getDatas/type1/", param);

		new LoadDataTask2(this, Constant.RV_PATH, 2).execute(
				"/holiday/getDatas/type3/", param);

		new LoadDataTask2(this, Constant.RV_PATH, 3).execute(
				"/holiday/getDatas/type2/", param);

		new LoadDataTask2(this, Constant.RV_PATH, 4).execute(
				"/holiday/getDatas/type4/", param);

		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("queryDate",
				DateUtil.formatter(date_button.getDate(), "yyyyMMddHH"));
		map.put("top", "5");
		param.put("clickCode", menuCode);
		param.put("appType", "ANDROID_RVS");
		param.put("clickType", "MENU");

		new LoadDataTask2(this, Constant.RV_PATH, 5).execute(
				"/holiday/getDatas/type5/", map);

	}

	@SuppressWarnings("unchecked")
	public void doAdapterClickListener(int position, View v, Object obj) {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		bundle.putString(BaseConfigLoader.KEY_MENU_CODE, menuCode);
		bundle.putInt("position", position);

		switch (v.getId()) {
		case R.id.hoilday_topn_adapter:
			bundle.putString("title", ((Map<String, String>) obj).get("title"));
			startActivity(new Intent(this, HMTopnDrillActivity.class)
					.putExtras(bundle));
			UIUtil.startTransAnim(this);
			break;
		case R.id.hoilday_kpi_adapter:

			bundle.putString("kpiCode",
					((Map<String, String>) obj).get("kpiCode"));
			bundle.putString("title", ((Map<String, String>) obj).get("title"));

			if (TextUtils.equals(((Map<String, String>) obj).get("kpiCode"),
					"DQ250011")
					|| TextUtils.equals(
							((Map<String, String>) obj).get("kpiCode"),
							"DQ250012")) {
				break;
			}
			startActivity(new Intent(this, HMFlowDrillChartAreaActivity.class)
					.putExtras(bundle));
			UIUtil.startTransAnim(this);
			break;
		case R.id.hoilday_blchart_adapter:
			String lowerKpi = ((Map<String, String>) obj).get("lowerKpi");
			bundle.putString("title", ((Map<String, String>) obj).get("title"));
			if (TextUtils.isEmpty(lowerKpi)) {

				bundle.putString("kpiCode",
						((Map<String, String>) obj).get("kpiCode"));
				startActivity(new Intent(this, HMFlowDrillAreaActivity.class)
						.putExtras(bundle));
				UIUtil.startTransAnim(this);
				break;
			}
			bundle.putString("lowerKpi", lowerKpi);
			startActivity(new Intent(this, HMFlowDrillChartActivity.class)
					.putExtras(bundle));
			UIUtil.startTransAnim(this);
			break;

		case R.id.hoilday_dash_layout:
			break;

		default:
			break;
		}

	}
}
