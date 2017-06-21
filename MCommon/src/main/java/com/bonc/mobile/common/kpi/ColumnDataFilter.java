package com.bonc.mobile.common.kpi;

import java.text.DecimalFormat;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.util.StringUtil;

public class ColumnDataFilter {

	// 构造方法私有.
	private ColumnDataFilter() {

	}

	private static ColumnDataFilter columnDataFilter;
	private int default_normal_color = R.color.default_color;
	private int default_plus_color = R.color.default_plus_color;
	private int default_minus_color = R.color.default_minus_color;
	private int default_percent_ws = 2;

	public static ColumnDataFilter getInstance() {
		if (columnDataFilter == null) {
			columnDataFilter = new ColumnDataFilter();

		}

		return columnDataFilter;
	}


	public ColumnDisplyInfo doFilter(String rule, String unit, String value) {

        if (KpiConstant.KPI_RULE_STRING.equals(rule)) {
            ColumnDisplyInfo columnDisplyInfo = new ColumnDisplyInfo();
            columnDisplyInfo.setValue(StringUtil.nullToString(value));
            columnDisplyInfo.setColor(default_normal_color);
            columnDisplyInfo.setUnit("");
            return columnDisplyInfo;
        }

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
		
        int flag_gb = 0;
        if (rule.startsWith("GB")) {
            flag_gb = Integer.parseInt(rule.substring(2, 3));
            rule = rule.substring(3);
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

                if (flag_gb == 1&&newvalue>=1024) {
                    columnDisplyInfo.setValue(new DecimalFormat(pattern).format(newvalue / 1024));
                    columnDisplyInfo.setUnit("TB");
                } else {
                    columnDisplyInfo.setValue(new DecimalFormat(pattern).format(newvalue));
                    columnDisplyInfo.setUnit(StringUtil.nullToString(unit));
                }
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
                if (flag_gb == 1) {
                    columnDisplyInfo.setValue(new DecimalFormat(pattern).format(newvalue / 1024));
                    columnDisplyInfo.setUnit("TB");
                } else if (flag_gb == 2) {
                    columnDisplyInfo.setValue(new DecimalFormat(pattern).format(newvalue));
                    columnDisplyInfo.setUnit("GB");
                } else {
                    columnDisplyInfo.setValue(new DecimalFormat(pattern).format(newvalue / 10000));
                    columnDisplyInfo.setUnit("万" + StringUtil.nullToString(unit));
                }
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
		private String value; 

		private String unit; 

		private int color=R.color.default_color;

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
