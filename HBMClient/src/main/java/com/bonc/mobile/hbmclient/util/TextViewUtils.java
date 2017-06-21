package com.bonc.mobile.hbmclient.util;

import android.view.View;
import android.widget.TextView;

public class TextViewUtils {
	public static void setText(View parent, int id, CharSequence value) {
		TextView tv = (TextView) parent.findViewById(id);
		tv.setText(value);
	}

	public static void setVisibility(View parent, int id, int visibility) {
		TextView tv = (TextView) parent.findViewById(id);
		tv.setVisibility(visibility);
	}

	/**
	 * 在指定文本控件上显示数字
	 * 
	 * @param tv
	 * @param value
	 */
	public static void setNumberValue(TextView tv, double value) {

		tv.setText(NumberUtil.format(value));

	}

	/**
	 * 在指定文本控件上显示数字
	 * 
	 * @param tv
	 * @param value
	 */
	public static void setNumberValue(TextView tv, String value) {

		tv.setText(NumberUtil.format(NumberUtil.changeToDouble(value)));

	}

	/**
	 * 在指定文本控件上显示百分比
	 * 
	 * @param tv
	 * @param value
	 */
	public static void setPercentValue(TextView tv, double value) {

		final String txt = NumberUtil.format(value * 100, "0.0") + "%";

		if (value <= 0) {
			tv.setTextColor(0XFF208600);
		} else {
			tv.setTextColor(0XFFe60005);
		}

		tv.setText(txt);

	}

	/**
	 * 在指定文本控件上显示百分比
	 * 
	 * @param tv
	 * @param value
	 * @param format
	 *            格式
	 */
	public static void setPercentValue(TextView tv, double value, String format) {

		final String txt = NumberUtil.format(value * 100, format) + "%";

		if (value <= 0) {
			tv.setTextColor(0XFF208600);
		} else {
			tv.setTextColor(0XFFe60005);
		}

		tv.setText(txt);

	}

	/**
	 * 在指定文本控件上显示百分比
	 * 
	 * @param tv
	 * @param value
	 * @param format
	 *            格式
	 */
	public static void setPercentValue(TextView tv, String value, String format) {

		final double v = NumberUtil.changeToDouble(value);

		final String txt = NumberUtil.format(v * 100, format) + "%";

		if (v <= 0) {
			tv.setTextColor(0XFF208600);
		} else {
			tv.setTextColor(0XFFe60005);
		}

		tv.setText(txt);

	}

	/**
	 * 在指定文本控件上显示百分比
	 * 
	 * @param tv
	 * @param value
	 */
	public static void setPercentValue(TextView tv, String value) {
		if (value.equals("∞")) {
			tv.setText(value);
			tv.setTextColor(0xff000000);
			return;
		} else if (value.equals("-")) {
			tv.setText("--");
			tv.setTextColor(0xff000000);
			return;
		}
		final double v = NumberUtil.changeToDouble(value);

		final String txt = NumberUtil.format4percent(v * 100, "0.0") + "%";

		if (v <= 0) {
			// tv.setTextColor(0XFF208600);
			tv.setTextColor(0XFFe60005);
		} else {
			tv.setTextColor(0xff000000);
			// tv.setTextColor(0XFFe60005);
		}

		tv.setText(txt);

	}

}
