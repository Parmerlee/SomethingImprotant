package com.bonc.mobile.portal;

import java.io.File;
import java.util.Map;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.bonc.anhuimobile.ac.R;
import com.bonc.mobile.common.activity.BaseActivity;
import com.bonc.mobile.common.net.SimpleDownloadManager;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.portal.common.AppManager;

public class AppDetailActivity extends BaseActivity {
	Map<String, String> app_info;
	private ScreenStateRecevier mRecevier;
	private ScreenOffRecevier mOffRecevier;
	private TimeOutDialog mTimeUpDia;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_detail);
		initView();

		AppManager.getInstance().addActivity(this);

		mTimeUpDia = new TimeOutDialog(this);
		mRecevier = new ScreenStateRecevier();
		mOffRecevier = new ScreenOffRecevier();
	}

	void initView() {
		app_info = AppManager.getInstance().getAppInfo(
				getIntent().getStringExtra("pkg"));
		setWatermarkImage();
		ImageView icon = (ImageView) findViewById(R.id.icon);
		icon.setImageDrawable(AppManager.getInstance().getAppIcon(
				app_info.get(AppManager.KEY_ICON)));
		TextViewUtils.setText(this, R.id.label,
				app_info.get(AppManager.KEY_NAME));
		TextViewUtils.setText(this, R.id.text1, app_info.get("PRODUCT_DESC"));
		TextViewUtils.setText(this, R.id.text2, app_info.get("FUNCTION_DESC"));
		TextViewUtils.setText(this, R.id.text3, app_info.get("REMARK"));
	}

	public void onClick(View v) {
		downloadApp(app_info.get(AppManager.KEY_APP_DOWNLOAD_URL));
	}

	void downloadApp(String url) {
		SimpleDownloadManager dm = new SimpleDownloadManager(this) {
			@Override
			protected void onDownloadFinished(String path) {
				super.onDownloadFinished(path);
				finish();
			}
		};
		dm.downloadFile(url, new File(
				Environment.getExternalStorageDirectory(), "tmp/app.apk")
				.getAbsolutePath());
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
					callLogin();
				}
			});
			setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface arg0) {
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
			onRestart();
		}

	}

	private class ScreenOffRecevier extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			AppManager.mTimestamp = System.currentTimeMillis();
		}

	}
}
