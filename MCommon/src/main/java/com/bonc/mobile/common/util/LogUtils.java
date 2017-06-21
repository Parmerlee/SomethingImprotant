package com.bonc.mobile.common.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.bonc.mobile.common.Constant;

public class LogUtils {

	/**
	 * 根据配置判断是否打印info信息
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void info(String tag, Object msg) {

		if (Constant.DEBUG) {
			Log.i(tag, String.valueOf(msg));
		}

	}

	/**
	 * 根据配置判断是否打印info信息
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void debug(String tag, Object msg) {

		if (Constant.DEBUG) {
			Log.d(tag, String.valueOf(msg));
		}

	}

	/**
	 * 根据配置判断是否打印info信息
	 * 
	 * @param <T>
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void debug(Class cla, Object msg) {

		if (Constant.DEBUG) {
			Log.d(cla.getName(), String.valueOf(msg));
		}

	}

	/**
	 * 根据配置判断是否打印info信息
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void error(String tag, Object msg) {

		if (Constant.DEBUG) {
			Log.e(tag, String.valueOf(msg));
		}

	}

	/**
	 * 根据配置判断是否打印info信息
	 *
	 * @param tag
	 * @param msg
	 */
	public static void toast(Context context, Object msg) {

		if (Constant.DEBUG) {
			Toast.makeText(context,String.valueOf(msg),Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * 调用系统log
	 * @param str
	 */
	public static void logBySys(String str) {
		if (Constant.DEBUG) {
			System.out.println(str);
		}
	}
}
