package com.bonc.mobile.hbmclient.terminal.subactivity;

import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.util.FileUtils;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.MenuActivity;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;
import com.bonc.mobile.hbmclient.service.BoncService;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;
import com.bonc.mobile.hbmclient.util.ExitApplication;
import com.bonc.mobile.hbmclient.util.LogoutUtil;

public class BaseTerminalActivity extends Activity {
	private TimeOutDialog mTimeUpDia;
	private ScreenStateRecevier mRecevier;
	private ScreenOffRecevier mOffRecevier;

	protected Intent intent;
	protected TerminalActivityEnum mTerminalActivityEnum;

	protected Map<String, String> configMap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		intent = getIntent();
		this.mTerminalActivityEnum = (TerminalActivityEnum) intent.getExtras()
				.get(TerminalConfiguration.KEY_ACTIVITY_ENUM);
		ExitApplication.getInstance().addActivity(this);
		mTimeUpDia = new TimeOutDialog(this);
		mRecevier = new ScreenStateRecevier();
		mOffRecevier = new ScreenOffRecevier();

		if (!AppConstant.SEC_ENH) {
			Log.d("AAA", "this act is:" + this.getClass().getName());
		}

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		unregisterReceiver(mOffRecevier);
		unregisterReceiver(mRecevier);
		super.onStop();
		MenuActivity.mTimestamp = System.currentTimeMillis();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		long ts = System.currentTimeMillis();
		if (MenuActivity.mTimestamp > 0
				&& ts - MenuActivity.mTimestamp > MenuActivity.TIME_OUT_WHILE) {
			mTimeUpDia.show();

			LogoutUtil.timeCounter(BaseTerminalActivity.this,mTimeUpDia);
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		MenuActivity.mTimestamp = -1;
	}

	@Override
	public void startActivity(Intent intent) {
		// TODO Auto-generated method stub
		super.startActivity(intent);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		IntentFilter inf = new IntentFilter();
		inf.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(mRecevier, inf);
		IntentFilter inf2 = new IntentFilter();
		inf2.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mOffRecevier, inf2);

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

	public class TimeOutDialog extends AlertDialog {

		protected TimeOutDialog(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			setTitle("超时");
			setCancelable(false);
			setMessage("登陆已超时，请重新登陆");
			setButton(AlertDialog.BUTTON_NEUTRAL, "确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub

					LogoutUtil.Logout(BaseTerminalActivity.this);
					// callLogin();
				}
			});
			setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface arg0) {
					// TODO Auto-generated method stub
					LogoutUtil.Logout(BaseTerminalActivity.this);
					// callLogin();
				}
			});
		}

		private void callLogin() {
			stopService(new Intent(BaseTerminalActivity.this, BoncService.class));
			ExitApplication.getInstance().exit();
			System.exit(0);
			finish();
		}
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
			MenuActivity.mTimestamp = System.currentTimeMillis();
		}

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_share:
			FileUtils.shareScreen(this);
			break;
		}
	}
}
