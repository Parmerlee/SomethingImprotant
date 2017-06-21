package com.bonc.mobile.hbmclient.util;

import android.util.Log;

import com.bonc.mobile.hbmclient.common.Constant;

/**
 * 日志工具类. 做了简单的修改,根据系统的配置来决定是否打印日志信息.
 * 
 * @author tengshibo
 *
 */
public class LogUtil {

	/**
	 * 根据配置是否允许打印debug信息.
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void debug(String tag, String msg) {
		if (Constant.DEBUG_ABLE) {
			Log.d(tag, msg);
		}

	}

	/**
	 * 根据配置判断是否打印info信息
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void info(String tag, String msg) {

		if (Constant.DEBUG_ABLE) {
			Log.i(tag, msg);
		}

	}

	/**
	 * 根据配置判断是否打印error信息.
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void error(String tag, String msg) {

		if (Constant.DEBUG_ABLE) {
			Log.e(tag, msg);
		}
	}

}
