/**
 * 
 */
package com.bonc.mobile.hbmclient.util;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author liweigao
 *
 */
public class FirstScanUtil {
	private volatile static FirstScanUtil mInstance = null;
	private boolean isFirstScan = false;

	// private final String KEY_LAST_TIME = "key_last_time";

	private FirstScanUtil() {
	};

	public static FirstScanUtil getSingleInstance() {
		if (mInstance == null) {
			synchronized (FirstScanUtil.class) {
				if (mInstance == null) {
					mInstance = new FirstScanUtil();
				}
			}
		}
		return mInstance;
	}

	public boolean isFirstScanHeavy(Context c, String menuCode) {
		registerTime(c, menuCode);
		return isFirstScan();
	}

	public boolean isFirstScan() {
		return isFirstScan;
	}

	public void registerTime(Context c, String menuCode) {
		SharedPreferences sp = c.getSharedPreferences("SETTING",
				Context.MODE_WORLD_WRITEABLE);
		String lasttime = sp.getString(menuCode, null);
		if (lasttime == null) {
			this.isFirstScan = true;
		} else {
			long time = Long.parseLong(lasttime);
			Date l_date = new Date(time);
			Date today = DateUtil.getDate(DateUtil.formatter(Calendar
					.getInstance().getTime(), "yyyy-MM-dd"), "yyyy-MM-dd");
			if (l_date.before(today)) {
				this.isFirstScan = true;
			} else {
				this.isFirstScan = false;
			}
		}
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(menuCode, String.valueOf(System.currentTimeMillis()));
		editor.commit();
	}

}
