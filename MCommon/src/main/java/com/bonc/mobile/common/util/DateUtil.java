package com.bonc.mobile.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	public static final String PATTERN_9 = "yyyy-MM-dd";

	public static final String PATTERN_8 = "yyyyMMdd";
	public static final String PATTERN_6 = "yyyyMM";
	public static final String PATTERN_MODEL2_10 = "yyyy/MM/dd";
	public static final String PATTERN_MODEL2_7 = "yyyy/MM";
    public static final String PATTERN_YMDH = "yyyy/MM/dd HH时";

	/**
	 * 获取传入日期的下一天
	 */
	public static String nextDay(String day, String fommater) {
		SimpleDateFormat sdf = new SimpleDateFormat(fommater,
				Locale.getDefault());

		try {
			Date date = sdf.parse(day);
			Calendar c = Calendar.getInstance();
			c.setTime(date);

			c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1);

			return sdf.format(c.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 获取与传入日期相差若干天的日期.
	 */
	public static String dayBefore(String day, String fommater, int before) {
		SimpleDateFormat sdf = new SimpleDateFormat(fommater,
				Locale.getDefault());

		try {
			Date date = sdf.parse(day);
			Calendar c = Calendar.getInstance();
			c.setTime(date);

			c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) - before);

			return sdf.format(c.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 获取下个月
	 */
	public static String nextMonth(String month, String fommater) {
		SimpleDateFormat sdf = new SimpleDateFormat(fommater,
				Locale.getDefault());

		try {
			Date date = sdf.parse(month);
			Calendar c = Calendar.getInstance();
			c.setTime(date);

			c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);

			return sdf.format(c.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 获取上年
	 */
	public static String preYear(String month, String fommater) {
		SimpleDateFormat sdf = new SimpleDateFormat(fommater,
				Locale.getDefault());

		try {
			Date date = sdf.parse(month);
			Calendar c = Calendar.getInstance();
			c.setTime(date);

			c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);

			return sdf.format(c.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 获取上个月
	 */
	public static String preMonth(String month, String fommater) {
		SimpleDateFormat sdf = new SimpleDateFormat(fommater,
				Locale.getDefault());

		try {
			Date date = sdf.parse(month);
			Calendar c = Calendar.getInstance();
			c.setTime(date);

			c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1);

			return sdf.format(c.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 获取当前月的前几个月或者后几个月.
	 */
	public static String monthBefore(String month, String fommater, int before) {
		SimpleDateFormat sdf = new SimpleDateFormat(fommater,
				Locale.getDefault());

		try {
			Date date = sdf.parse(month);
			Calendar c = Calendar.getInstance();
			c.setTime(date);

			c.set(Calendar.MONTH, c.get(Calendar.MONTH) - before);

			return sdf.format(c.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 将日期字符串转为另外一种格式
	 * 
	 * @param date
	 * @param oldFormmat
	 * @param newFormmat
	 * @return
	 */
	public static String oneStringToAntherString(String dateString,
			String oldFormmat, String newFormmat) {
		SimpleDateFormat sdf = new SimpleDateFormat(oldFormmat,
				Locale.getDefault());
		SimpleDateFormat sdf2 = new SimpleDateFormat(newFormmat,
				Locale.getDefault());
		try {
			return sdf2.format(sdf.parse(dateString));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 将日期字符串格式化成clendar类
	 * 
	 * @param dateString
	 * @param pattern
	 * @return
	 */
	public static Calendar getCalendar(String dateString, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern,
				Locale.getDefault());

		try {
			Date date = sdf.parse(dateString);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return calendar;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 将日期字符串格式化成Date类
	 * 
	 * @param dateString
	 * @param pattern
	 * @return
	 */
	public static Date getDate(String dateString, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern,
				Locale.getDefault());

		try {
			Date date = sdf.parse(dateString);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return calendar.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 将日期格式化为指定格式.
	 */
	public static String formatter(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern,
				Locale.getDefault());
		return sdf.format(date);
	}

	/*
	 * get last date of last month
	 * 
	 * @param date current date
	 */
	public static String getLastMonthLastDay(Date date, String pattern) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				1);

		calendar.add(Calendar.DATE, -1);

		return DateUtil.formatter(calendar.getTime(), pattern);
	}

	/*
	 * get first month of year
	 * 
	 * @param date current date
	 */
	public static String getFirstMonth(Date date, String pattern) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(calendar.get(Calendar.YEAR), 0, 1);

		return DateUtil.formatter(calendar.getTime(), pattern);
	}

	/*
	 * get first day or first year
	 */
	public static String getFirstDayorMonth(String date) {
		Calendar calendar = Calendar.getInstance();
		String ret;

		if (date.length() == 6) {
			// ret = date.substring(0, 4)+"01";
			Date curDate = DateUtil.getDate(date, PATTERN_6);
			calendar.setTime(curDate);
			calendar.set(calendar.get(Calendar.YEAR), 0, 1);
			ret = DateUtil.formatter(calendar.getTime(), PATTERN_6);

		} else {
			Date curDate = DateUtil.getDate(date, PATTERN_8);
			calendar.setTime(curDate);
			calendar.set(calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH), 1);
			ret = DateUtil.formatter(calendar.getTime(), PATTERN_8);
		}

		return ret;

	}

	/*
	 * get first month of last year
	 * 
	 * @param date current date
	 */
	public static String getFirstMonthLastyear(Date date, String pattern) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(calendar.get(Calendar.YEAR) - 1, 0, 1);

		return DateUtil.formatter(calendar.getTime(), pattern);
	}

	/*
	 * get last month of last year
	 * 
	 * @param date current date
	 */
	public static String getMonthLastyear(Date date, String pattern) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(calendar.get(Calendar.YEAR) - 1,
				calendar.get(Calendar.MONTH), 1);

		return DateUtil.formatter(calendar.getTime(), pattern);
	}

	/*
	 * get first date of last month
	 * 
	 * @param date current date
	 */
	public static String getLastMonthFirstDay(Date date, String pattern) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH) - 1, 1);

		return DateUtil.formatter(calendar.getTime(), pattern);
	}

	/*
	 * get first date of current month
	 * 
	 * @param date current date
	 */
	public static String getFirstDay(Date date, String pattern) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				1);

		return DateUtil.formatter(calendar.getTime(), pattern);
	}

	/**
	 * get first date of current month
	 * 
	 * @param date
	 *            current date
	 */
	public static String getCurrentDay(Date date, String pattern) {

		return DateUtil.formatter(date, pattern);
	}

	/**
	 * 计算两个日期之间的天数
	 */

	public static int dayBetween(String date1, String date2, String pattern) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(getDate(date1, pattern));

		Calendar c2 = Calendar.getInstance();
		c2.setTime(getDate(date2, pattern));

		return Math.abs(c1.get(Calendar.DAY_OF_YEAR)
				- c2.get(Calendar.DAY_OF_YEAR));

	}

	/**
	 * 计算两个日期之间的月份
	 */
	public static int MonthBetween(String date1, String date2, String pattern) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(getDate(date1, pattern));

		Calendar c2 = Calendar.getInstance();
		c2.setTime(getDate(date2, pattern));

		return Math.abs(c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH));

	}

	public static String[] getCurrentLastDay(String date, String pattern) {
		String[] result = new String[4];
		Date dateTemp = getDate(date, pattern);
		Calendar calendarTemp = Calendar.getInstance();
		calendarTemp.setTime(dateTemp);
		calendarTemp.add(Calendar.DATE, -29);
		String head_cur = formatter(calendarTemp.getTime(), pattern);
		String end_cur = date;

		// calendarTemp.add(Calendar.MONTH, -1);
		calendarTemp.setTime(dateTemp);
		calendarTemp.add(Calendar.MONTH, -1);
		String end_last = formatter(calendarTemp.getTime(), pattern);
		calendarTemp.add(Calendar.DATE, -29);
		String head_last = formatter(calendarTemp.getTime(), pattern);

		result[0] = head_cur;
		result[1] = end_cur;
		result[2] = head_last;
		result[3] = end_last;

		return result;
	}

	public static String getDateSpecified(String date, String pattern,
			int offset) {
		Date dateTemp = getDate(date, pattern);
		Calendar calendarTemp = Calendar.getInstance();
		calendarTemp.setTime(dateTemp);
		calendarTemp.add(Calendar.DATE, offset);
		String result = formatter(calendarTemp.getTime(), pattern);
		return result;
	}

	public static String getDayOfDate(String date, String pattern) {
		Date dateTemp = getDate(date, pattern);
		Calendar calendarTemp = Calendar.getInstance();
		calendarTemp.setTime(dateTemp);
		String result = String.valueOf(calendarTemp.get(Calendar.DAY_OF_MONTH));
		return result;
	}

	public static String[] getDayInNum(String headDate, String pattern, int num) {
		String[] result = new String[num];
		Date dateTemp = getDate(headDate, pattern);
		Calendar calendarTemp = Calendar.getInstance();
		calendarTemp.setTime(dateTemp);
		for (int i = 0; i < num; i++) {
			result[i] = String.valueOf(calendarTemp.get(Calendar.DAY_OF_MONTH));
			calendarTemp.add(Calendar.DATE, 1);
		}

		return result;
	}
}
