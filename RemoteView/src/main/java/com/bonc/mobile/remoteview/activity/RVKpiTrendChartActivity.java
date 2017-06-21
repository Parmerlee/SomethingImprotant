package com.bonc.mobile.remoteview.activity;

import android.os.Bundle;

import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.kpi.KpiTrendChartActivity;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.common.ConfigLoader;
import com.bonc.mobile.remoteview.common.RemoteUtil;

public class RVKpiTrendChartActivity extends KpiTrendChartActivity {

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

	@Override
	protected BaseConfigLoader getConfigLoader() {
		return ConfigLoader.getInstance(this);
	}

	@Override
	protected String getQueryAction() {
		return "/kpi/trendDrill1|/kpi/trendDrill2";
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		showGuide();
	}

	void showGuide() {
		UIUtil.showGuideWindow(this, findViewById(R.id.root),
				"guide.kpi_trend", new int[] { R.mipmap.guide_kpi_trend });
	}

}
