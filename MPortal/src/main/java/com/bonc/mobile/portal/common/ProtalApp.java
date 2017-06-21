package com.bonc.mobile.portal.common;

import android.app.Application;

public class ProtalApp extends Application {

	private static ProtalApp mApp;

	public static ProtalApp getInstance() {
		return mApp;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mApp = this;
	}

}
