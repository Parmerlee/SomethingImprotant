package com.bonc.mobile.remoteview.activity;

import java.util.LinkedHashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.common.Constant;
import com.bonc.mobile.remoteview.common.RemoteUtil;

public class WarnRuleDetailActivity extends WarnRuleBaseActivity {
	String kpiCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rule_detail);
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
		super.onClick(v);
		if (v.getId() == R.id.select_all) {
			UIUtil.showAlertDialog(this, getString(R.string.hint),
					new String[] { "全部开启", "全部关闭" },
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							modAllAlarmStatus(which == 0 ? "1" : "0");
						}
					});
		}
	}

	@Override
	protected void initBaseData() {
		super.initBaseData();
		kpiCode = getIntent().getStringExtra("KPI_CODE");
	}

	@Override
	protected void loadData() {
		super.loadData();
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("kpiCode", kpiCode);
		LoadDataTask t = new LoadDataTask(this);
		t.setRetAsArray(true);
		t.execute(Constant.Path_LoadKpiRule, params);
	}
}
