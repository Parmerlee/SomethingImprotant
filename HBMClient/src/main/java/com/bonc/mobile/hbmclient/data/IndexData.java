package com.bonc.mobile.hbmclient.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter.ColumnDisplyInfo;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.JsonUtil;

/**
 * @author sunwei
 */
public class IndexData {
	public static final String CODE_SPE = "\\|";

	public String op_time, area_code, kpi_code, kpi_name;
	public String value, curday_value, curmonth_value;
	public String value_dr, curday_value_dr, curmonth_value_dr;

	public KpiInfo kpi_info;
	public ColumnDisplyInfo value_cdi, value_dr_cdi;

	static Map<String, KpiInfo> kpiInfoMap;

	public void setFormatRule(String rule, String unit, String dr_rule) {
		value_cdi = ColumnDataFilter.getInstance().doFilter(rule, unit, value);
		value_dr_cdi = ColumnDataFilter.getInstance().doFilter(dr_rule, "",
				value_dr);
	}

	public static Map<String, KpiInfo> getKpiInfoMap() {
		if (kpiInfoMap == null) {
			kpiInfoMap = new HashMap<String, KpiInfo>();
			List<Map<String, String>> kpiInfoList = new SQLHelper()
					.queryForList(
							"select kpi_code as kpi_code,kpi_name as kpi_name,kpi_unit as kpi_unit,kpi_cal_rule as kpi_cal_rule,kpi_define as kpi_define from kpi_info",
							null);
			for (int i = 0; i < kpiInfoList.size(); i++) {
				KpiInfo kInfo = new KpiInfo();
				kInfo.setKpiCode(kpiInfoList.get(i).get("kpi_code"));
				kInfo.setKpiName(kpiInfoList.get(i).get("kpi_name"));
				kInfo.setKpiUnit(kpiInfoList.get(i).get("kpi_unit"));
				kInfo.setKpiRule(kpiInfoList.get(i).get("kpi_cal_rule"));
				kInfo.setKpiDefine(kpiInfoList.get(i).get("kpi_define"));
				kpiInfoMap.put(kpiInfoList.get(i).get("kpi_code"), kInfo);
			}
		}
		return kpiInfoMap;
	}

	public static KpiInfo getKpiInfo(String code) {
		return getKpiInfoMap().get(code);
	}

	public static String getKpiUnit(String code) {
		KpiInfo kpi_info = getKpiInfo(code);
		return kpi_info != null ? kpi_info.getKpiUnit() : "";
	}

	public static ColumnDisplyInfo getColumnDisplyInfo(String code, String value) {
		KpiInfo kpi_info = getKpiInfo(code);
		ColumnDisplyInfo cdi;
		if (kpi_info != null) {
			cdi = ColumnDataFilter.getInstance().doFilter(
					kpi_info.getKpiRule(), kpi_info.getKpiUnit(), value);
		} else {
			cdi = ColumnDataFilter.getInstance().doFilter(null, null, value);
		}
		return cdi;
	}

	public static IndexData build(JSONObject json) {
		IndexData data = new IndexData();
		data.op_time = json.optString("op_time");
		data.area_code = json.optString("area_code");
		data.kpi_code = json.optString("kpi_code");
		data.kpi_name = json.optString("kpi_name");
		data.value = json.has("curday_value") ? json.optString("curday_value")
				: json.optString("curmonth_value");
		data.curday_value = json.optString("curday_value");
		data.curmonth_value = json.optString("curmonth_value");
		data.value_dr = json.has("curday_value_dr") ? json
				.optString("curday_value_dr") : json
				.optString("curmonth_value_dr");
		data.curday_value_dr = json.optString("curday_value_dr");
		data.curmonth_value_dr = json.optString("curmonth_value_dr");
		data.kpi_info = getKpiInfo(data.kpi_code);
		data.value_cdi = getColumnDisplyInfo(data.kpi_code, data.value);
		data.value_dr_cdi = ColumnDataFilter.getInstance().doFilter(
				Constant.TERMINAL_SALE_PERCENT_RULE, Constant.UNIT_PERCENT,
				data.value_dr);
		return data;
	}

	/****
	 * SimpleTerminalActivity类专用 2016年6月13日15:29:52
	 * 
	 * @param json
	 * @return
	 */
	public static IndexData buildUsedBySimpleTerminalActivity(JSONObject json) {
		IndexData data = new IndexData();
		data.op_time = json.optString("op_time".toUpperCase());
		data.area_code = json.optString("area_code".toUpperCase());
		data.kpi_code = json.optString("kpi_code".toUpperCase());
		data.kpi_name = json.optString("kpi_name".toUpperCase());
		data.value = json.has("curday_value".toUpperCase()) ? json
				.optString("curday_value".toUpperCase()) : json
				.optString("curmonth_value".toUpperCase());
		data.curday_value = json.optString("curday_value".toUpperCase());
		data.curmonth_value = json.optString("curmonth_value".toUpperCase());
		data.value_dr = json.has("curday_value_dr".toUpperCase()) ? json
				.optString("curday_value_dr".toUpperCase()) : json
				.optString("curmonth_value_dr".toUpperCase());
		data.curday_value_dr = json.optString("curday_value_dr".toUpperCase());
		data.curmonth_value_dr = json.optString("curmonth_value_dr"
				.toUpperCase());
		data.kpi_info = getKpiInfo(data.kpi_code);
		data.value_cdi = getColumnDisplyInfo(data.kpi_code, data.value);
		data.value_dr_cdi = ColumnDataFilter.getInstance().doFilter(
				Constant.TERMINAL_SALE_PERCENT_RULE, Constant.UNIT_PERCENT,
				data.value_dr);
		return data;
	}

	public static Map<String, String> extractSingleData(JSONObject json,
			String[] cols) {
		Map<String, String> data = new HashMap<String, String>();
		for (int i = 0; i < cols.length; i++) {
			data.put(cols[i], json.optString(cols[i]));
		}
		return data;
	}

	public static List<Map<String, String>> getDummyData(int n) {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		for (int i = 0; i < n; i++) {
			data.add(new HashMap<String, String>());
		}
		return data;
	}

	public static List<Map<String, String>> extractData(JSONObject json,
			String[] kpi_codes) {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		for (int i = 0; i < kpi_codes.length; i++) {
			JSONObject o = json.optJSONObject(kpi_codes[i]);
			if (o != null) {
				data.add(JsonUtil.toMap(o));
			}
		}
		return data;
	}

	public static List<Map<String, String>> extractData(JSONArray json) {
		return JsonUtil.toList(json);
	}

	public static List<Map<String, String>> extractData(JSONArray json,
			String[] kpi_codes) {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		for (int j = 0; j < kpi_codes.length; j++) {
			for (int i = 0; i < json.length(); i++) {
				JSONObject o = json.optJSONObject(i);
				if (kpi_codes[j].equals(o.optString("kpi_code"))) {
					data.add(JsonUtil.toMap(o));
				}
			}
		}
		return data;
	}

	public static Map<String, List<Double>> extractTrendData(JSONObject json) {
		return JsonUtil.toTrendMap(json);
	}

	public static double[] extractRangeData(JSONArray a, String col,
			String beginTime, boolean monthQuery) {
		int count = monthQuery ? 12 : 30;
		double[] d = new double[count];
		Map<String, JSONObject> m = new HashMap<String, JSONObject>();
		for (int i = 0; i < a.length(); i++) {
			JSONObject o = a.optJSONObject(i);
			m.put(o.optString("op_time"), o);
		}
		for (int i = 0; i < count; i++) {
			String op_time = monthQuery ? DateUtil.monthBefore(beginTime,
					DateUtil.PATTERN_6, -i) : DateUtil.dayBefore(beginTime,
					DateUtil.PATTERN_8, -i);
			JSONObject o = m.get(op_time);
			if (o != null)
				d[i] = o.optDouble(col);
		}
		return d;
	}

	public static void putTitles(List<Map<String, String>> data,
			String title_col, String[] titles) {
		for (int i = 0; i < data.size(); i++) {
			data.get(i).put(title_col, titles[i]);
		}
	}

	public static boolean checkKpiCode(String kpi_code, String codes) {
		String[] a = codes.split(CODE_SPE);
		for (String code : a) {
			if (code.equals(kpi_code)) {
				return true;
			}
		}
		return false;
	}
}
