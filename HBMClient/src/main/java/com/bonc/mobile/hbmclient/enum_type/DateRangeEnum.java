/**
 * TODO
 */
package com.bonc.mobile.hbmclient.enum_type;

import java.util.Calendar;
import java.util.Date;

import com.bonc.mobile.hbmclient.util.DateUtil;

/**
 * @author liweigao
 *
 */
public enum DateRangeEnum {
	DAY("D", "yyyyMMdd", "yyyy/MM/dd", 9, "本月", "上月", "yyyy年MM月dd日",
			"1970/01/01", Calendar.DAY_OF_MONTH), MONTH("M", "yyyyMM",
			"yyyy/MM", 11, "本年", "上年", "yyyy年MM月", "1970/01", Calendar.MONTH);

	private final String date_flag;
	private final String server_pattern;
	private final String show_pattern;
	private final int trendSize;
	private final String pattern_china;
	private final String line1Name;
	private final String line2Name;
	private final String default_date;
	private final int calendarField;

	private String maxDate;
	private String minDate;

	private DateRangeEnum(String flag, String server_pattern,
			String show_pattern, int trendSize, String s1, String s2,
			String pattern_china, String default_date, int field) {
		this.date_flag = flag;
		this.server_pattern = server_pattern;
		this.show_pattern = show_pattern;
		this.trendSize = trendSize;
		this.line1Name = s1;
		this.line2Name = s2;
		this.pattern_china = pattern_china;
		this.default_date = default_date;
		this.calendarField = field;
	}

	public String getPatternChina() {
		return this.pattern_china;
	}

	public String getLine1() {
		return this.line1Name;
	}

	public String getLine2() {
		return this.line2Name;
	}

	public int getTrendSize() {
		return this.trendSize;
	}

	public String getDateServerPattern() {
		return this.server_pattern;
	}

	public String getDateShowPattern() {
		return this.show_pattern;
	}

	public String getDateFlag() {
		return this.date_flag;
	}

	public void setMaxDate(String max) {
		this.maxDate = max;
	}

	public void setMinDate(String min) {
		this.minDate = min;
	}

	public String getMaxDate() {
		return this.maxDate;
	}

	public String getMinDate() {
		return this.minDate;
	}

	public boolean isDateValid(Calendar c) {
		int selectDate = Integer.valueOf(DateUtil.formatter(c.getTime(),
				this.server_pattern));
		int max = Integer.valueOf(this.maxDate);
		int min = Integer.valueOf(this.minDate);
		return selectDate >= min && selectDate <= max;
	}

	/**
	 * @return the default_date
	 */
	public String getDefaultDate() {
		return default_date;
	}

	public String getDateSpecified(int number, String pattern) {
		Calendar c = Calendar.getInstance();
		c.add(this.calendarField, number);
		Date date = c.getTime();

		return DateUtil.formatter(date, pattern);
	}
}
