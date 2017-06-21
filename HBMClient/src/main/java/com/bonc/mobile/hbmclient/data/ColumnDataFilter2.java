/**
 * ColumnDataFilter2
 */
package com.bonc.mobile.hbmclient.data;

import java.text.DecimalFormat;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.util.StringUtil;

/**
 * @author liweigao
 * 
 */
public class ColumnDataFilter2 {

	// 构造方法私有.
	private ColumnDataFilter2() {

	}

	private static ColumnDataFilter2 columnDataFilter;
	private int default_normal_color = R.color.default_color;
	private int default_plus_color = R.color.default_plus_color;
	private int default_minus_color = R.color.default_minus_color;
	private int default_percent_ws = 2;

	public static ColumnDataFilter2 getInstance() {
		if (columnDataFilter == null) {
			columnDataFilter = new ColumnDataFilter2();

		}

		return columnDataFilter;
	}

	public ColumnDisplyInfo filter(MenuColumnInfo columnInfo, String value,
			KpiInfo kpiInfo) {

		ColumnDisplyInfo result = null;
		if (columnInfo == null) {
			result = doFilter(null, null, value);

		} else {
			String rule = columnInfo.getColRule();
			if ("-1".equals(rule)) {

				if (kpiInfo == null) {
					result = doFilter(null, null, value);
				} else {
					result = doFilter(kpiInfo.getKpiRule(),
							kpiInfo.getKpiUnit(), value);
				}
			} else {
				result = doFilter(rule, columnInfo.getColUnit(), value);
			}
		}

		if (result == null) {
			result = getDefaultResult(columnInfo, value, kpiInfo);
		}

		return result;

	}

	public ColumnDisplyInfo doFilter(String rule, String unit, String value) {

		// 如果传入的数值为空 那么单位也为空.
		if (value == null || "".equals(value)) {
			ColumnDisplyInfo columnDisplyInfo = new ColumnDisplyInfo();
			columnDisplyInfo.setValue("--");
			columnDisplyInfo.setColor(default_normal_color);
			columnDisplyInfo.setUnit("--");
			return columnDisplyInfo;
		}

		if (rule == null || "".equals(rule)) {
			ColumnDisplyInfo columnDisplyInfo = new ColumnDisplyInfo();
			columnDisplyInfo.setValue(value);
			columnDisplyInfo.setColor(default_normal_color);
			columnDisplyInfo.setUnit(StringUtil.nullToString(unit));
			return columnDisplyInfo;
		}

		// 百分数.
		if (rule.startsWith("%") || rule.startsWith("+")) {
			String[] ruleList = rule.split("\\|");
			int ws = default_percent_ws;

			try {
				if (ruleList.length >= 2) {
					ws = Integer.parseInt(ruleList[1]);
				}
			} catch (Exception e) {
				e.printStackTrace();
				ws = default_percent_ws;
			}

			String pattern = "0.";
			if (ws == 0) {
				pattern = "0";
			} else {
				for (int i = 0; i < ws; i++) {
					pattern += "0";
				}
			}

			DecimalFormat decimalFormat = new DecimalFormat(pattern);

			try {
				value = decimalFormat.format(Double.valueOf(value) * 100);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

			ColumnDisplyInfo columnDisplyInfo = new ColumnDisplyInfo();
			columnDisplyInfo.setUnit(StringUtil.nullToString(unit));
			columnDisplyInfo.setValue(value);

			if (rule.startsWith("+")) {
				if (value.startsWith("-")) {
					columnDisplyInfo.setColor(default_plus_color);
				} else {
					columnDisplyInfo.setColor(default_minus_color);
				}
			}

			return columnDisplyInfo;

		}
		// 是保留位数的.
		if (rule.startsWith("[") && rule.endsWith("]")) {

			String divdor = "\\|";

			if (rule.indexOf("@") > 0) {
				divdor = "@";
			}

			String[] ruleList = rule.substring(1, rule.length() - 1).split(
					divdor);
			int before_deal = 0, after_deal = 0;
			if (ruleList.length >= 1) {
				try {
					before_deal = Integer.valueOf(ruleList[0]);
				} catch (Exception e) {
					before_deal = 0;
				}
			}

			if (ruleList.length >= 2) {
				try {
					after_deal = Integer.valueOf(ruleList[1]);
				} catch (Exception e) {
					after_deal = 0;
				}
			}
			double newvalue = 0;
			try {
				newvalue = Double.parseDouble(value);
			} catch (Exception e) {
				return null;
			}

			ColumnDisplyInfo columnDisplyInfo = new ColumnDisplyInfo();

			if (Math.abs(newvalue) < 10000) {
				String pattern = "0.";
				if (before_deal == 0) {
					pattern = "0";
				} else {
					for (int i = 0; i < before_deal; i++) {
						pattern += "0";
					}
				}

				columnDisplyInfo.setValue(new DecimalFormat(pattern)
						.format(newvalue));
				columnDisplyInfo.setUnit(StringUtil.nullToString(unit));
				return columnDisplyInfo;
			}

			if (Math.abs(newvalue) < 100000000) {
				String pattern = "0.";
				if (after_deal == 0) {
					pattern = "0";
				} else {
					for (int i = 0; i < after_deal; i++) {
						pattern += "0";
					}
				}
				columnDisplyInfo.setValue(new DecimalFormat(pattern)
						.format(newvalue / 10000));
				columnDisplyInfo.setUnit("万" + StringUtil.nullToString(unit));
				return columnDisplyInfo;

			}

			String pattern = "0.";
			if (after_deal == 0) {
				pattern = "0";
			} else {
				for (int i = 0; i < after_deal; i++) {
					pattern += "0";
				}
			}

			columnDisplyInfo.setValue(new DecimalFormat(pattern)
					.format(newvalue / 100000000));
			columnDisplyInfo.setUnit("亿" + StringUtil.nullToString(unit));
			return columnDisplyInfo;
		}

		return null;
	}

	// 出异常的情况下 获取默认的返回值.
	public ColumnDisplyInfo getDefaultResult(MenuColumnInfo columnInfo,
			String value, KpiInfo kpiInfo) {
		ColumnDisplyInfo columnDisplyInfo = new ColumnDisplyInfo();
		columnDisplyInfo.setColor(default_normal_color);
		columnDisplyInfo.setUnit(columnDisplyInfo.getUnit());
		columnDisplyInfo.setValue("");

		return columnDisplyInfo;
	}

	/**
	 * 返回一个带默认值的对象.
	 * 
	 * @param rule
	 * @param unit
	 * @param value
	 * @return
	 */
	public ColumnDisplyInfo filterWithDefaultValue(String rule, String unit,
			String value) {

		ColumnDisplyInfo cdi = doFilter(rule, unit, value);
		if (cdi == null) {
			cdi = new ColumnDisplyInfo();
			cdi.setColor(default_normal_color);
			cdi.setUnit(unit);
			cdi.setValue(value);
		}

		return cdi;

	}

	public class ColumnDisplyInfo {
		private String value; // 要显示的值

		private String unit; // 单位

		private int color;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getUnit() {
			return unit;
		}

		public void setUnit(String unit) {
			this.unit = unit;
		}

		public int getColor() {
			return color;
		}

		public void setColor(int color) {
			this.color = color;
		}

	}

}
