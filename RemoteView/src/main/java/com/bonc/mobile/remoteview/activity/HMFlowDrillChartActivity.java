package com.bonc.mobile.remoteview.activity;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.common.Constant;
import com.bonc.mobile.common.User;
import com.bonc.mobile.common.activity.BaseDataActivity;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.util.DateUtil;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.common.view.SimpleListView;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.adapter.BLChartAdapter;
import com.bonc.mobile.remoteview.common.ConfigLoader;
import com.bonc.mobile.remoteview.common.RemoteUtil;
import com.bonc.mobile.remoteview.common.Utils;

//图表下钻一级界面
public class HMFlowDrillChartActivity extends BaseDataActivity {

	SimpleListView BLChart_listview;
	Activity activity;
	int pos = -1;

	String lowerKpi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hm_drill_chart);
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
				RemoteUtil.getInstance().exit();
			}
		}
	}

	@Override
	protected void initView() {
		super.initView();
		BLChart_listview = (SimpleListView) findViewById(R.id.activity_hm_drill_chart_listview);
		activity = this;
		Bundle b = new Bundle();
		b = this.getIntent().getExtras();
		menuCode = b.getString(BaseConfigLoader.KEY_MENU_CODE);
		pos = b.getInt("postion");
		lowerKpi = b.getString("lowerKpi");

		TextViewUtils.setText(this, R.id.title, b.getString("title"));

		date_button.setWithTime(true);
		date_button.setPattern("yyyy/MM/dd HH时");
		date_button.setDate(DateUtil.getDate(Utils.getTime(), "yyyyMMddHH"));
		area_button.setVisibility(View.GONE);

		area_button.setData(JsonUtil.toList(Utils.getAreacode()), "AREA_NAME",
				"AREA_CODE");
		area_button.setChoice(Utils.getAreaname());

		this.findViewById(R.id.root).setBackgroundColor(Color.WHITE);
		TextView water = (TextView) this.findViewById(R.id.date_water);
		water.setVisibility(View.VISIBLE);
		water.setText("4A:" + User.getInstance().userCode);

	}

	@Override
	protected void loadData() {
		Map<String, String> param = new LinkedHashMap<String, String>();

		param.put("queryDate",
				DateUtil.formatter(date_button.getDate(), "yyyyMMddHH"));
		param.put("areaCode", area_button.getChoiceValue());
		param.put("lowerKpi", lowerKpi);
		new LoadDataTask(this, Constant.RV_PATH).execute(
				"/holiday/drill/level1/", param);

		param.put("clickCode", menuCode);
		param.put("appType", "ANDROID_RVS");
		param.put("clickType", "KPI");

		Utils.setmTime_temp(DateUtil.formatter(date_button.getDate(),
				"yyyyMMddHH"));

	}

	@Override
	protected void bindData(JSONObject result) {
		BLChartAdapter adapter = new BLChartAdapter(activity,
				R.layout.holiday_simple_item, new LinearLayout.LayoutParams(
						Utils.getWidth() / 3, Utils.getWidth() / 3), result);
		adapter.setActivity(activity);
		adapter.setTime(Utils.getTime());
		BLChart_listview.setAdapter(adapter);
	}

	@SuppressWarnings("unchecked")
	public void doAdapterClickListener(int position, View v, Object obj) {

		Bundle bundle = new Bundle();
		bundle.putString(BaseConfigLoader.KEY_MENU_CODE, menuCode);
		bundle.putInt("position", position);

		bundle.putString("kpiCode", ((Map<String, String>) obj).get("kpiCode"));
		bundle.putString("title", ((Map<String, String>) obj).get("title"));
		startActivity(new Intent(this, HMFlowDrillAreaActivity.class)
				.putExtras(bundle));
		UIUtil.startTransAnim(this);
	}

}
