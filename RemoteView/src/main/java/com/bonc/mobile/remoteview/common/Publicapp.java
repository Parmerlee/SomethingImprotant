package com.bonc.mobile.remoteview.common;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;

import com.bonc.mobile.common.util.LogUtils;

public class Publicapp extends Application {

	private static Publicapp mApp;

	private static boolean isLogin;

	public static Publicapp getInstance() {
		return mApp;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		LogUtils.debug(getClass(), "AAAAAA");
		mApp = this;
	//	FloatViewFactory.getSingleInstance().setApplication(this);
	}

	public static boolean isLogin() {
		return isLogin;
	}

	public static void setLogin(boolean isLogin) {
		Publicapp.isLogin = isLogin;
	}

//	public static String getNotifyFlag() {
//		String flag = "";
//		try {
//			Context otherContext = mApp.createPackageContext(Constant.PKG_NAME,
//					Context.CONTEXT_IGNORE_SECURITY);
//			SharedPreferences sharedPreferences = otherContext
//					.getSharedPreferences("SETTING",
//							Context.MODE_WORLD_READABLE);
//			flag = sharedPreferences.getString("notifyFlag", otherContext
//					.getResources().getString(R.string.notify_open));
//		} catch (NameNotFoundException e) {
//			e.printStackTrace();
//		}
//		return flag;
//	}

}
