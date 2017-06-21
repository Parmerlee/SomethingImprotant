package com.bonc.mobile.portal.db;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.text.TextUtils;

import com.bonc.mobile.common.BaseApplication;
import com.bonc.mobile.common.User;
import com.bonc.mobile.common.util.DES;
import com.bonc.mobile.common.util.DateUtil;
import com.bonc.mobile.common.util.LogUtils;

public class BussinessDao {

	private SQLHelper helper = new SQLHelper();

	private final String tableName = "ErrorLog";

	public int getCount() {

		String sql = "select id from " + tableName;
		List<Map<String, String>> list = helper.queryForList(sql, null);
		return list.size();
	}

	private int getMaxId() {
		String sql = "select max(id) as id   from " + tableName;
		List<Map<String, String>> list = helper.queryForList(sql, null);
		if (list.size() != 0) {
			try {

				return Integer.valueOf(list.get(0).get("id"));
			} catch (Exception e) {
				// TODO: handle exception
				return 0;
			}
		} else {
			return 0;
		}
	}

	public List<Map<String, String>> queryForAll() {
		String sql = "select * from " + tableName;
		List<Map<String, String>> list = helper.queryForList(sql, null);
		return list;
	}

	/**
	 * delete info by id
	 * 
	 * @param id
	 */
	public void deteleInfo(int id) {
		if (id < 1)
			return;
		String sql = "delete from " + tableName + " where id = " + id;
		helper.execute(sql);
	}

	public void addInfo(Context context, Map<String, String> map, User user,
			String value) {
		String url = null, msg = null;
		for (Map.Entry<String, String> m : map.entrySet()) {
			url = m.getKey();
			msg = m.getValue();
		}
		msg = DES.decrypt2(msg);
		LogUtils.debug("AAAA", "map:" + map.toString());
		String fourA = user.userCode;
		String phone = user.phone;

		addInfo(context, url, value, fourA, phone);
	}

	public void addInfo(Context context, Map<String, String> map, User user) {
		String url = null, msg = null;
		for (Map.Entry<String, String> m : map.entrySet()) {
			url = m.getKey();
			msg = m.getValue();
		}
		msg = DES.decrypt2(msg);
		LogUtils.debug("AAAA", "map:" + map.toString());
		String fourA = user.userCode;
		String phone = user.phone;

		addInfo(context, url, msg, fourA, phone);
	}

	/***
	 * 添加数据
	 * 
	 * @param context
	 * @param url
	 * @param msg
	 * @param foruA
	 * @param phone
	 */
	private void addInfo(Context context, String url, String msg, String foruA,
			String phone) {
		// LogUtils.debug(getClass(), getCurrentTime());
		ContentValues cv = new ContentValues();
		cv.put("id", getMaxId() + 1);
		cv.put("ComInfo", getcommonSql(context));
		cv.put("URLInfo", url);
		cv.put("Msg", msg);
		cv.put("User4ACode", foruA);
		cv.put("Phone", phone);
		cv.put("Time", getCurrentTime());

		long row = helper.insert(tableName, cv);
		LogUtils.debug(getClass(), cv + ";row:" + row);
	}

	private String getcommonSql(Context context) {
		StringBuffer sql = new StringBuffer();
		sql.append("android-");
		sql.append("手机型号：");
		sql.append(android.os.Build.MANUFACTURER + "-" + android.os.Build.MODEL);
		sql.append("-SDK版本：");
		sql.append(android.os.Build.VERSION.SDK_INT);
		sql.append("-系统版本：");
		sql.append(android.os.Build.VERSION.RELEASE);
		sql.append("-");
		sql.append(getVersionCode(context));
		sql.append("-");
		sql.append(getVersionName(context));
		// LogUtils.debug("AAA", sql);
		return sql.toString();
	}

	private String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINA);
		return sdf.format(new Date());
	}

	// 版本名
	public static String getVersionName(Context context) {
		return getPackageInfo(context).versionName;
	}

	// 版本名称
	public static String getVersionCode(Context context) {
		// if(TextUtils.equals(getPackageInfo(context).packageName, ""))
		return "门户";
	}

	private static PackageInfo getPackageInfo(Context context) {
		PackageInfo pi = null;

		try {
			PackageManager pm = context.getPackageManager();
			pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_CONFIGURATIONS);

			return pi;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pi;
	}

}
