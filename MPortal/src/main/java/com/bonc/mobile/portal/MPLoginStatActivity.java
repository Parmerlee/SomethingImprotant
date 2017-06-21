package com.bonc.mobile.portal;

import java.util.Map;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.bonc.mobile.common.activity.LoginDetailActivity;
import com.bonc.mobile.common.activity.LoginStatActivity;
import com.bonc.mobile.common.util.DateUtil;
import com.bonc.mobile.portal.common.AppManager;

public class MPLoginStatActivity extends LoginStatActivity {

	private ScreenStateRecevier mRecevier;
	private ScreenOffRecevier mOffRecevier;
	private TimeOutDialog mTimeUpDia;

	@Override
	protected String getAction() {
		return "/PalmBiServer/PalmBiKpi/palmBiSysFuncAction_getLoginSumInfo.do";
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Map<String, String> m = (Map<String, String>) parent.getAdapter()
				.getItem(position);
		Intent intent = new Intent(this, LoginDetailActivity.class);
		intent.putExtra("optime",
				DateUtil.formatter(date_button.getDate(), DateUtil.PATTERN_8));
		intent.putExtra("queryCode", m.get("user_code"));
		startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppManager.getInstance().addActivity(this);
		mTimeUpDia = new TimeOutDialog(this);
		mRecevier = new ScreenStateRecevier();
		mOffRecevier = new ScreenOffRecevier();
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
