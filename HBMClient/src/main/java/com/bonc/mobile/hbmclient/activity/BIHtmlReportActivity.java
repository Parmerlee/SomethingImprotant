package com.bonc.mobile.hbmclient.activity;

import android.view.View;

import com.bonc.mobile.common.kpi.HtmlReportActivity;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.hbmclient.R;

public class BIHtmlReportActivity extends HtmlReportActivity {
	@Override
	protected void initView() {
		super.initView();
		findViewById(R.id.id_share).setVisibility(View.VISIBLE);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (MyUtils.doInfilter(this))
			if (!MyUtils.isBackground(this)) {
				MyUtils.startProtal(this);
			}
	}
}
