package com.bonc.mobile.common.util;

import java.lang.reflect.Field;
import java.util.List;

import com.bonc.mobile.common.AppConstant;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.widget.Toast;

public class PackageUtil {
	public static boolean isPackageInstalled(Context context, String packageName) {
		try {
			context.getPackageManager().getPackageInfo(packageName, 0);

			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	public static int getVersionCode(Context context, String packageName) {
		try {
			LogUtils.debug("AAAAA","version:"+context.getPackageManager().getPackageInfo(packageName, 0).packageName);
			return context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
		} catch (NameNotFoundException e) {
			return 0;
		}
	}

	public static int getVersionCode(Context context) {
		LogUtils.logBySys("name:"+context.getPackageName());
		return getVersionCode(context, context.getPackageName());
	}

}
