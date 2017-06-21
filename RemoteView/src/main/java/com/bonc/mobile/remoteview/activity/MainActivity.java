package com.bonc.mobile.remoteview.activity;

import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bonc.mobile.common.activity.MenuPagerActivity;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.common.ConfigLoader;
import com.bonc.mobile.remoteview.common.RemoteUtil;
import com.bonc.mobile.remoteview.service.RVService;

public class MainActivity extends MenuPagerActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		startService(new Intent(this, RVService.class));
		setWatermarkImage();
		initPager(getConfigLoader().getFirstMenu());
		// showAnnounce();
		this.addSettingEvent();


		RemoteUtil.getInstance().addActivity(this);
	}


	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		showGuide();
	}

	@Override
	protected BaseConfigLoader getConfigLoader() {
		return ConfigLoader.getInstance(this);
	}

	@Override
	protected void onMenuItemClick(Map<String, String> menu_info) {
		if ("25".equals(menu_info.get(BaseConfigLoader.KEY_MENU_TYPE)))
			onBackPressed();
		else
			super.onMenuItemClick(menu_info);
	}

	void showAnnounce() {
		Intent intent = new Intent(this, RVAnnounceActivity.class);
		intent.putExtra("showUnread", true);
		startActivity(intent);
	}

	void showGuide() {
		UIUtil.showGuideWindow(this, findViewById(R.id.root), "guide.main",
				new int[] { R.mipmap.guide_main });
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder bu = new AlertDialog.Builder(MainActivity.this);
		bu.setTitle("确定要退出吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).setNegativeButton("取消", null).create().show();
	}

	private void addSettingEvent() {
		Button btn = (Button) this.findViewById(R.id.globalSettingBtn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goToSystemSettingActivity();
			}
		});
	}

	private void goToSystemSettingActivity() {
		Intent intent = new Intent(this, SystemSettingActivity.class);
		startActivity(intent);
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
