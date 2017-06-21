package com.bonc.mobile.hbmclient.util;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * SIM卡工具类
 * 
 * @author liulu
 *
 */
public class SMTools {

	private TelephonyManager tm;

	public SMTools(Context context) {
		tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

	}

	/**
	 * 获得电话号码
	 * 
	 * @return
	 */
	public String getTel() {

		if (tm != null) {
			return tm.getLine1Number();
		}

		return null;
	}

	/**
	 * 获得IMEI
	 * 
	 * @return
	 */
	public String getIMEI() {

		if (tm != null) {
			return tm.getDeviceId();

		}

		return null;
	}

	/**
	 * 获得IMSI
	 * 
	 * @return
	 */
	public String getIMSI() {

		if (tm != null) {
			return tm.getSubscriberId();
		}

		return null;
	}

	/**
	 * 获得手机状态
	 * 
	 * @return
	 */
	public int getState() {

		if (tm != null) {
			return tm.getSimState();
		}

		return 0;
	}

}
