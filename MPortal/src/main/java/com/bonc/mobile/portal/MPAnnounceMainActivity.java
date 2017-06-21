package com.bonc.mobile.portal;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.bonc.anhuimobile.ac.R;
import com.bonc.mobile.common.activity.BaseTabActivity;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.portal.common.AppManager;

public class MPAnnounceMainActivity extends BaseTabActivity {
	String perm;

	private ScreenStateRecevier mRecevier;
	private ScreenOffRecevier mOffRecevier;
	private TimeOutDialog mTimeUpDia;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		perm = getIntent().getStringExtra("perm");
		setContentView(R.layout.activity_announce_main);
		setWatermarkImage();
		TextViewUtils.setText(this, R.id.title, "公告查看");
		String menu = "[{'MENU_CODE':0,'MENU_NAME':'全部','MENU_TYPE':''},{'MENU_CODE':1,'MENU_NAME':'掌上分析','MENU_TYPE':'BI'},{'MENU_CODE':2,'MENU_NAME':'网络千里眼','MENU_TYPE':'RVS'}]";
		initTab(JsonUtil.toList(menu));
		getTabWidget().getChildTabViewAt(1).setEnabled(perm.contains("BI"));
		getTabWidget().getChildTabViewAt(2).setEnabled(perm.contains("RVS"));

		AppManager.getInstance().addActivity(this);

		mTimeUpDia = new TimeOutDialog(this);
		mRecevier = new ScreenStateRecevier();
		mOffRecevier = new ScreenOffRecevier();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		long ts = System.currentTimeMillis();
		if (AppManager.mTimestamp > 0
				&& ts - AppManager.mTimestamp > AppManager.TIME_OUT_WHILE) {
			mTimeUpDia.setCancelable(false);
			mTimeUpDia.show();
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		AppManager.mTimestamp = -1;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		AppManager.mTimestamp = System.currentTimeMillis();
		unregisterReceiver(mRecevier);
		unregisterReceiver(mOffRecevier);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		IntentFilter inf = new IntentFilter();
		AppManager.mTimestamp = -1;
		inf.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(mRecevier, inf);
		IntentFilter inf2 = new IntentFilter();
		inf2.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mOffRecevier, inf2);
	}

	@Override
	protected int getTabViewLayout() {
		return R.layout.tab_button_2;
	}

	@Override
	protected Intent getTabIntent(String code, String type) {
		Intent intent = new Intent(this, MPAnnounceListActivity.class);
		intent.putExtra("filter", type.length() > 0 ? "'" + type + "'" : perm);
		return intent;
	}

	@Override
	protected BaseConfigLoader getConfigLoader() {
		return null;
	}

	public class TimeOutDialog extends AlertDialog {

		protected TimeOutDialog(Context context) {
			super(context);
			setTitle("登陆超时");
			setCancelable(false);
			setMessage("长时间未操作,登陆已超时,请重新登陆!");
			setButton(AlertDialog.BUTTON_NEUTRAL, "确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					callLogin();
				}
			});
			setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface arg0) {
					// TODO Auto-generated method stub
					callLogin();
				}
			});
		}

	}

	private void callLogin() {
		AppManager.getInstance().exit();
		System.exit(0);
		finish();
	}

	private class ScreenStateRecevier extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			onRestart();
		}

	}

	private class ScreenOffRecevier extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			AppManager.mTimestamp = System.currentTimeMillis();
		}

	}
}
