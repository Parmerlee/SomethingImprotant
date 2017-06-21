package com.bonc.mobile.remoteview.activity;

import android.os.Bundle;

import com.bonc.mobile.common.activity.AnnounceListActivity;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.common.ConfigLoader;
import com.bonc.mobile.remoteview.common.RemoteUtil;

/***
 * 公告
 * 
 * @author Lenevo
 *
 */
public class RVAnnounceListActivity extends AnnounceListActivity {

	@Override
	protected void initView() {
		super.initView();
		TextViewUtils.setText(this, R.id.title, ConfigLoader.getInstance(this)
				.getMenuName(menuCode));
	}

	@Override
	protected String getAction() {
		return "/sys/getNoticeMain";
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
