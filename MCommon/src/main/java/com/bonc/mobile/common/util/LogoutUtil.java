package com.bonc.mobile.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.bonc.mobile.common.BaseApplication;
import com.bonc.mobile.common.service.BaseService;

public class LogoutUtil {

	public static void Logout(Context context) {
		for (int i = 0; i < 5; i++) {

			System.out.println(i + "");
			context.stopService(new Intent(context, BaseService.class));
			BaseApplication.getInstance().exit();
			System.exit(0);
			android.os.Process.killProcess(android.os.Process.myPid());

			((Activity) context).finish();
		}
	}
}
