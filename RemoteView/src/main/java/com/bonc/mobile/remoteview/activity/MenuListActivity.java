package com.bonc.mobile.remoteview.activity;

import android.view.View;

import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.common.ConfigLoader;
import com.bonc.mobile.remoteview.common.RemoteUtil;

/**
 * @author sunwei
 */
public class MenuListActivity extends
		com.bonc.mobile.common.activity.MenuListActivity {

	@Override
	protected void initView() {
		super.initView();
		TextViewUtils.setText(this, R.id.title,
				getConfigLoader().getMenuName(menuCode));
		findViewById(R.id.id_share).setVisibility(View.GONE);
		RemoteUtil.getInstance().addActivity(this);
	}

	@Override
	protected BaseConfigLoader getConfigLoader() {
		return ConfigLoader.getInstance(this);
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
