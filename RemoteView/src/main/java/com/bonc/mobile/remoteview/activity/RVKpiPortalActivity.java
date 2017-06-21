package com.bonc.mobile.remoteview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.kpi.KpiPortalActivity;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.common.ConfigLoader;
import com.bonc.mobile.remoteview.common.RemoteUtil;

public class RVKpiPortalActivity extends KpiPortalActivity {

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.kpi_mgr) {
			startKpiMgr();
		}
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

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		showGuide();
	}

	@Override
	protected Class getKpiMainClass() {
		return RVKpiMainActivity.class;
	}

	@Override
	protected BaseConfigLoader getConfigLoader() {
		return ConfigLoader.getInstance(this);
	}

	void startKpiMgr() {
		Intent intent = new Intent(this, ChannelKpiMgrActivity.class);
		intent.putExtra(ChannelKpiMgrActivity.CH_CODE, menuCode);
		startActivity(intent);
	}

	void showGuide() {
		UIUtil.showGuideWindow(this, findViewById(R.id.root), "guide.kpi",
				new int[] { R.mipmap.guide_kpi_1, R.mipmap.guide_kpi_2,
						R.mipmap.guide_kpi_3, R.mipmap.guide_kpi_4,
						R.mipmap.guide_kpi_5 });
	}

}
