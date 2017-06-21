package com.bonc.mobile.common.util;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.bonc.mobile.common.AppConstant;

public class MyUtils {
    private static final String mProtalPkgName = "com.bonc.anhuimobile.ac";

    public static final String mHBMClientPkgName = "com.bonc.mobile.hbmclient";

    public static final String mRemoteViewPkgName = "com.bonc.mobile.remoteview";

    public static final String STRING_PROTAL_ALIVE = "com.bonc.mprotal.alive";

    public static final String STRING_PROTAL_LOGIN = "com.bonc.mprotal.login";

    public static final int INT_MPROTAL_SEND = 1;

    public static final int INT_HBMPROTAL_GET = 2;

    public static boolean isBackground(Activity activity) {

        boolean isActive = false;

        ActivityManager activityManager = (ActivityManager) activity
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();

        if (appProcesses.size() == 1
                && appProcesses.get(0).processName.equals(mHBMClientPkgName)) {
            isActive = true;
        } else if (appProcesses.size() == 1
                && appProcesses.get(0).processName.equals(mRemoteViewPkgName)) {
            isActive = true;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {

            if (appProcess.processName.equals(mProtalPkgName)) {

                isActive = true;
                break;

            }
        }

        LogUtils.logBySys("login:" + SharePreferenceUtil.getLoginStatus(activity));
        return isActive && SharePreferenceUtil.getLoginStatus(activity);
    }

    public static boolean doInfilter(Activity activity) {
        boolean filter1 = TextUtils.equals(activity.getApplication()
                .getPackageName(), MyUtils.mHBMClientPkgName);
        boolean filter2 = TextUtils.equals(activity.getApplication()
                .getPackageName(), MyUtils.mRemoteViewPkgName);

//		if (!AppConstant.SEC_ENH) {
//			return false;
//		}
        return filter1 || filter2;

    }

    public static void startProtal(final Activity activity) {

        Intent intent = new Intent("com.bonc.mobile.portal.Main");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);

    }
}
