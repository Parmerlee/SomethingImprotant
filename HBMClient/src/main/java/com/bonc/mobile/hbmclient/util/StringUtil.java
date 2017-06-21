package com.bonc.mobile.hbmclient.util;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	private static final int DEFAULT_LENGTH = 9;
	private static final String DEFAULT_CHAR = " ";

	public static String toFixedLength(String str) {
		if (str == null)
			return str;

		int strlen = str.length();

		if (strlen == DEFAULT_LENGTH)
			return str;

		if (strlen > DEFAULT_LENGTH) {
			return str.substring(0, DEFAULT_LENGTH);
		}

		String suffix = "";

		for (int i = 0; i < DEFAULT_LENGTH - strlen; i++) {
			suffix += DEFAULT_CHAR;
		}

		return str + suffix;

	}

	/**
	 * 将null值转换为空字符串.
	 * 
	 * @param value
	 * @return
	 */
	public static String nullToString(String value) {
		return value == null ? "" : value;
	}

	public static String nullToString2(String value) {
		return value == null || "".equals(value) || "null".equals(value) ? "--"
				: value;
	}

	/**
	 * 判断字符串是否为空. null 和 "" 都认为是空
	 */

	public static boolean isNull(String value) {

		return value == null || "".equals(value);

	}

	public static String arrayToString(Set<String> s) {
		String temp = s.toString();
		String result = "'" + temp.substring(1, temp.length() - 1) + "'";
		return result.replace(", ", "','");
	}

	public static String statisticsCodesConnect(String... code) {
		StringBuilder sb = null;
		if (code != null && code.length > 0) {
			sb = new StringBuilder(code[0]);
			for (int i = 1; i < code.length; i++) {
				sb.append("|").append(code[i]);
			}
			return sb.toString();
		} else {
			return null;
		}
	}

	public static boolean isPhoneNumberInvalid(String phoneNumber) {
		String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
		String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
		CharSequence inputStr = phoneNumber;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		Pattern pattern2 = Pattern.compile(expression2);
		Matcher matcher2 = pattern2.matcher(inputStr);
		return (matcher.matches() || matcher2.matches()) ? false : true;
	}

	public static String listToString(List<String> data, String divider) {
		StringBuffer sb = new StringBuffer();
		sb.append(data.get(0));
		for (int i = 1; i < data.size(); i++) {
			sb.append(divider).append(data.get(i));
		}
		return sb.toString();
	}
}
