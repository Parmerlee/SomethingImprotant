package com.bonc.mobile.hbmclient.util;

import java.text.DecimalFormat;

import android.content.Context;
import android.util.Log;

public class NumberUtil {
	public static Double changeToDouble(String intoString, Double defaultDouble) {
		Double resultDouble = defaultDouble;
		try {
			resultDouble = Double.parseDouble(intoString);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			Log.e("NumberUtil", "Double默认值转换出错");
		}
		return resultDouble;
	}

	public static Double changeToDouble(String intoString) {
		Double resultDouble = 0.0;
		try {
			resultDouble = Double.parseDouble(intoString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultDouble;
	}

	public static int changeToInt(String intoString) {
		int resultDouble = 0;
		try {
			resultDouble = Integer.parseInt(intoString);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("NumberUtil", "Int无默认值转换出错");
		}
		return resultDouble;
	}

	public static int DpToPx(Context context, int dpValue) {
		int pxValue = dpValue;
		final float scale = context.getResources().getDisplayMetrics().density;
		pxValue = (int) (dpValue * scale + 0.5f);
		return pxValue;
	}

	public static int PxToDp(Context context, int pxValue) {
		int dpValue = pxValue;
		final float scale = context.getResources().getDisplayMetrics().density;
		dpValue = (int) (pxValue / scale + 0.5f);
		return dpValue;
	}

	/**
	 * 返回当前数值的比值数，万或亿
	 * 
	 * @param value
	 * @return
	 */
	public static double getScale(double value) {
		double scale = 1;

		while (scale * 10 < Math.abs(value)) {
			scale *= 10;

		}
		if (scale >= 100000000) {
			scale = 100000000;

		} else if (scale >= 10000) {
			scale = 10000;

		}
		return scale;
	}

	/**
	 * 返回当前单位附加值，万或亿
	 * 
	 * @param value
	 * @return
	 */
	public static String getUnit(double scale) {
		String unit = "";

		if (scale >= 100000000) {

			unit = "亿";
		} else if (scale >= 10000) {

			unit = "万";
		}
		return unit;
	}

	/**
	 * 格式化数据
	 * 
	 * @param value
	 *            数据
	 * @param format
	 *            格式化参数,例如("#.#" 标识保留到小数点后一位)
	 */
	public static String format(double value, String format) {
		if (value >= -0.5 && value <= 0) {
			value = 0;
		}
		// 判断是否过10亿
		if (Math.abs(value) >= 100000000) {
			value = value / 100000000;
			DecimalFormat df = new DecimalFormat(format);
			return df.format(value) + "亿";
		} else if (Math.abs(value) >= 100000) {// 10万
			value = value / 10000;
			DecimalFormat df = new DecimalFormat(format);
			return df.format(value) + "万";
		}
		DecimalFormat df = new DecimalFormat(format);
		return df.format(value);
	}

	// 写一个对于同比、环比的数据处理方法
	public static String format4percent(double value, String format) {
		if (value >= -0.5 && value <= 0) {
			value = 0;
		}
		DecimalFormat df = new DecimalFormat(format);
		return df.format(value);
	}

	/**
	 * 格式化数据
	 * 
	 * @return
	 */
	public static String format(double value) {
		if (value >= -0.05 && value <= 0) {
			value = 0;
		}

		// 判断是否过10亿
		if (Math.abs(value) >= 100000000) {
			value = value / 100000000;
			DecimalFormat df = new DecimalFormat("#.##");
			return df.format(value) + "亿";
		} else if (Math.abs(value) >= 100000) {// 10万
			value = value / 10000;
			DecimalFormat df = new DecimalFormat("#.##");
			return df.format(value) + "万";
		}
		DecimalFormat df = new DecimalFormat("#.##");

		return df.format(value);
	}

	/**
	 * 仿照地格式化数据获取单位和数值
	 * 
	 * @return
	 */
	public static String[] formatSplit(double value) {
		String[] ret = new String[2];
		if (value >= -0.05 && value <= 0) {
			value = 0;
		}
		// 判断是否过10亿
		if (Math.abs(value) >= 100000000) {
			value = value / 100000000;
			ret[1] = "亿";
			DecimalFormat df = new DecimalFormat("#.00");
			ret[0] = df.format(value);
			return ret;
		} else if (Math.abs(value) >= 100000) {// 10万
			value = value / 10000;
			ret[1] = "万";
			DecimalFormat df = new DecimalFormat("#.00");
			ret[0] = df.format(value);
			return ret;
		}
		DecimalFormat df = new DecimalFormat("#.00");
		ret[0] = df.format(value);
		ret[1] = "";
		return ret;
	}

	/**
	 * 计算数组的最大最小值
	 */
	public static double[] getMaxMinNumber(double[] array) {
		int index = 0;
		double max = 0;
		double min = 0;
		double[] maxMin = new double[2];
		if (array.length % 2 == 0) {
			if (array[0] <= array[1]) {
				min = array[0];
				max = array[1];
			} else {
				min = array[1];
				max = array[0];
			}

			index = 2;
		} else {
			min = array[0];
			max = array[0];

			index = 1;
		}

		for (int i = index; i < array.length - 1; i++) {
			if (array[i] <= array[i + 1]) {
				if (array[i] < min)
					min = array[i];

				if (array[i + 1] > max)
					max = array[i + 1];
			} else {
				if (array[i + 1] < min)
					min = array[i + 1];

				if (array[i] > max)
					max = array[i];
			}
		}
		maxMin[0] = max;
		maxMin[1] = min;
		return maxMin;

	}

	public static double getMaxNumber(double[] array) {
		return getMaxMinNumber(array)[0];
	}

	/**
	 * 保留小数点后2位
	 * 
	 * @param d
	 * @return
	 */
	public static String getDecimal(double d) {
		DecimalFormat df2 = new DecimalFormat("0.00");
		return df2.format(d);
	}

	/**
	 * 保留小数点后指定的位数
	 * 
	 * @param d
	 * @return
	 */
	public static String getDecimal(double d, String format) {
		DecimalFormat df2 = new DecimalFormat(format);
		return df2.format(d);
	}

	/**
	 * 若环比大于1000则返回1000,小于-1000则返回-1000,在-1000~1000内则返回原数
	 * 
	 * @param value
	 *            环比值
	 * @return
	 */
	public static double formatThousand(double value) {
		if (value >= 1000) {
			return 1000;
		} else if (value <= -1000) {
			return -1000;
		} else {
			return value;
		}

	}
}
