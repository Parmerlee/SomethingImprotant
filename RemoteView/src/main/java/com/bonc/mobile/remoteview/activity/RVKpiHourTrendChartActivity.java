package com.bonc.mobile.remoteview.activity;

import android.os.Bundle;

import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.kpi.KpiHourTrendChartActivity;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.remoteview.common.RemoteUtil;

public class RVKpiHourTrendChartActivity extends KpiHourTrendChartActivity {

	@Override
	protected BaseConfigLoader getConfigLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getQueryAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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
}
