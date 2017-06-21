package com.bonc.mobile.common.util;

import com.bonc.mobile.common.AppConstant;

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
	 * 获得设备号
	 * 
	 * @return
	 */
	public String getDeviceID() {

		if (tm != null) {
			return tm.getDeviceId();
		}

		return null;
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
			return AppConstant.SEC_ENH ? tm.getDeviceId() : "867609029572328";

		}

		return null;
	}

	/**
	 * 获得IMSI
	 * 
	 * @return
	 */
	public String getIMSI() {

		try {
			if (tm != null) {
                return  tm.getSubscriberId();
            }
		} catch (Exception e) {
			e.printStackTrace();
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
