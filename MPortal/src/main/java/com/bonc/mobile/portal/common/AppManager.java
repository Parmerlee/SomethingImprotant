package com.bonc.mobile.portal.common;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.Constant;
import com.bonc.mobile.common.User;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.common.util.SharePreferenceUtil;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author sunwei
 */
public class AppManager {
    public static final String KEY_NAME = "APP_TITLE";
    public static final String KEY_ICON = "icon";
    public static final String KEY_ACTION = "APP_ACTION";
    public static final String KEY_PACKAGE = "APP_PACKAGE";
    public static final String KEY_APP_DOWNLOAD_URL = "APP_DOWNLOAD_URL";
    public static final String KEY_VALI_CHANNEL = "VALI_CHANNEL";
    public static final String KEY_HAS_AUTH = "hasAuth";

    public static long mTimestamp = -1;

    public static final int TIME_OUT_WHILE = 10 * 60 * 1000;

    static AppManager inst = new AppManager();
    List<Map<String, String>> applist;
    Map<String, Drawable> iconMap = new HashMap<String, Drawable>();

    public static AppManager getInstance() {
        return inst;
    }

    public void load(String s) {
        applist = JsonUtil.toList(s);
        for (Map<String, String> m : applist) {
            m.put(KEY_ICON,
                    Boolean.parseBoolean(m.get(KEY_HAS_AUTH)) ? m
                            .get("APP_ICON") : m.get("APP_ICON2"));
        }
    }

    public void loadIcons(List<String> list) {
        for (String s : list) {
            try {
                LogUtils.logBySys("path:" + (Constant.BASE_PATH
                        + s));
                iconMap.put(
                        s,
                        Drawable.createFromStream(new URL(Constant.BASE_PATH
                                + s).openStream(), ""));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Drawable getAppIcon(String key) {
        return iconMap.get(key);
    }

    public List<Map<String, String>> getAppList() {
        return applist;
    }

    public Map<String, String> getAppInfo(int position) {
        return getAppList().get(position);
    }

    public Map<String, String> getAppInfo(String pkg) {
        return DataUtil.extractRow(getAppList(), KEY_PACKAGE, pkg);
    }

    public static void startApp(Context context, String action) {
        User u = User.getInstance();
        Intent intent = new Intent(action);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(AppConstant.KEY_USERCODE, u.userCode);
        intent.putExtra(AppConstant.KEY_PASSWORD, u.password);
        intent.putExtra(AppConstant.KEY_IMEI, u.imei);
        intent.putExtra(AppConstant.KEY_IMSI, u.imsi);
        intent.putExtra(AppConstant.KEY_MOBILE_KEY, u.mobileKey);
        intent.putExtra(AppConstant.KEY_MODE, u.mode);

        intent.putExtra(AppConstant.KEY_LOGIN,
                SharePreferenceUtil.getLoginStatus(context));
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "已安装的应用程序版本过旧，请先卸载", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private List<Activity> activityList = new LinkedList<Activity>();

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    public void exitOthers(Activity nowA) {
        for (Activity activity : activityList) {
            if (activity != nowA)
                activity.finish();
        }
    }
}
