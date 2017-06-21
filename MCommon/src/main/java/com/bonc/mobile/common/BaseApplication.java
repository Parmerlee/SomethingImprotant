package com.bonc.mobile.common;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class BaseApplication extends Application {

	private List<Activity> activityList = new LinkedList<Activity>();
	private static BaseApplication instance;

	private BaseApplication() {
	}

	// 单例模式中获取唯一的ExitApplication 实例
	public static BaseApplication getInstance() {
		if (null == instance)
			instance = new BaseApplication();
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
