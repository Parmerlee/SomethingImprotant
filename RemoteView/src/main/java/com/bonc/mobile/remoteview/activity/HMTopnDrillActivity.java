package com.bonc.mobile.remoteview.activity;

//拥塞小区topN 界面
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.bonc.mobile.common.Constant;
import com.bonc.mobile.common.User;
import com.bonc.mobile.common.activity.BaseListDataActivity;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.util.DateUtil;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.adapter.TopNListAdapter_second;
import com.bonc.mobile.remoteview.common.ConfigLoader;
import com.bonc.mobile.remoteview.common.RemoteUtil;
import com.bonc.mobile.remoteview.common.Utils;

public class HMTopnDrillActivity extends BaseListDataActivity {

	private ListView listview;

	TextView time;

	int pos = -1;

	Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hm_drill_topn);
		initView();
		loadData();
		activity = this;
		
		RemoteUtil.getInstance().addActivity(activity);
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
	protected void initView() {
		super.initView();
		listview = (ListView) this.findViewById(R.id.list);

		time = (TextView) this.findViewById(R.id.time);

		Bundle b = new Bundle();
		b = this.getIntent().getExtras();
		menuCode = b.getString(BaseConfigLoader.KEY_MENU_CODE);
		pos = b.getInt("position");

		date_button.setWithTime(true);
		date_button.setPattern("yyyy/MM/dd HH时");
		date_button.setDate(DateUtil.getDate(Utils.getTime(), "yyyyMMddHH"));

		area_button.setData(JsonUtil.toList(Utils.getAreacode()), "AREA_NAME",
				"AREA_CODE");
		area_button.setChoice(Utils.getAreaname());

		time.setText(DateUtil.formatter(
				DateUtil.getDate(Utils.getTime(), "yyyyMMddHH"),
				"yyyy-MM-dd HH时"));
		
		findViewById(R.id.root).setBackgroundColor(Color.WHITE);
		TextView water = (TextView) this.findViewById(R.id.date_water);
		water.setVisibility(View.VISIBLE);
		water.setText("4A:" + User.getInstance().userCode);

	}

	public void setMyTitle(String title) {
		TextViewUtils.setText(this, R.id.title, title);
	}

	@Override
	protected void loadData() {
		Map<String, String> param;
		param = new LinkedHashMap<String, String>();

		param.put("queryDate",
				DateUtil.formatter(date_button.getDate(), "yyyyMMddHH"));

		param.put("clickCode", menuCode);
		param.put("appType", "ANDROID_RVS");
		param.put("clickType", "MENU");
		param.put("top", "10");
		new LoadDataTask(this, Constant.RV_PATH).execute(
				"/holiday/getDatas/type5/", param);

		time.setText(DateUtil.formatter(date_button.getDate(), "yyyy-MM-dd HH时"));
	}

	@Override
	protected void bindData(JSONObject result) {

		TopNListAdapter_second adapter = new TopNListAdapter_second(activity,
				R.layout.holiday_toplv_item_second_layout, result, pos);
		listview.setAdapter(adapter);

	}

}
