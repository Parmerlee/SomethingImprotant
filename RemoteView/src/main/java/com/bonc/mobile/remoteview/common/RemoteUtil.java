package com.bonc.mobile.remoteview.common;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;

/****
 *  已废弃
 * @author Administrator
 *
 */
public class RemoteUtil {
	private List<Activity> activityList = new LinkedList<Activity>();
	private static RemoteUtil instance;

	private static long mTimestamp = -1;
	private static int TIME_OUT_WHILE = 10 * 60 * 1000;

	public ScreenStateRecevier mRecevier;
	public ScreenOffRecevier mOffRecevier;
	public TimeOutDialog mTimeUpDia;
	private Activity rootActivity;

	private RemoteUtil() {
	}

	public static RemoteUtil getInstance() {
		if (null == instance)
			instance = new RemoteUtil();
		return instance;
	}

	public void addActivity(Activity activity) {
		return;
		// if (activityList.isEmpty()) {
		// rootActivity = activity;
		// mRecevier = new ScreenStateRecevier();
		// mTimeUpDia = new TimeOutDialog(activity);
		//
		// IntentFilter inf = new IntentFilter();
		// inf.addAction(Intent.ACTION_SCREEN_ON);
		// inf.addAction(Intent.ACTION_SCREEN_OFF);
		// activity.registerReceiver(mRecevier, inf);
		// activity.registerReceiver(mOffRecevier, inf);
		// }
		//
		// mTimeUpDia = new TimeOutDialog(activity);
		// activityList.add(activity);
		// mTimestamp = -1;

	}

	public void removeActivity(Activity activity) {
		activityList.remove(activity);
	}

	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
	}

	public void callLogin() {
		RemoteUtil.getInstance().exit();
		System.exit(0);
		Activity activity = activityList.get(0);
		activity.unregisterReceiver(mRecevier);
	}

	public void checkTime() {
		long ts = System.currentTimeMillis();
		if (RemoteUtil.mTimestamp > 0
				&& ts - RemoteUtil.mTimestamp > RemoteUtil.TIME_OUT_WHILE) {
			mTimeUpDia.setCancelable(false);
			mTimeUpDia.show();
		}
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

	private class ScreenStateRecevier extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if (TextUtils.equals(arg1.getAction(), Intent.ACTION_SCREEN_ON)) {

				checkTime();
			} else if (TextUtils.equals(arg1.getAction(),
					Intent.ACTION_SCREEN_OFF)) {
				RemoteUtil.mTimestamp = System.currentTimeMillis();
			}
		}

	}

	private class ScreenOffRecevier extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if (arg1.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)) {

				RemoteUtil.mTimestamp = System.currentTimeMillis();
			}
		}

	}
}
