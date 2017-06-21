package com.bonc.mobile.hbmclient.util;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;

import com.bonc.mobile.hbmclient.common.Publicapp;

public class ExitApplication extends Publicapp {
	private List<Activity> activityList = new LinkedList<Activity>();
	private static ExitApplication instance;

	private ExitApplication() {
	}

	// 单例模式中获取唯一的ExitApplication 实例
	public static ExitApplication getInstance() {
		if (null == instance)
			instance = new ExitApplication();
		return instance;
	}

	// 添加Activity 到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历所有Activity 并finish
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
	}

	// 遍历所有Activity 并finish
	public void exitOthers(Activity nowA) {
		for (Activity activity : activityList) {
			if (activity != nowA)
				activity.finish();
		}
	}
}
