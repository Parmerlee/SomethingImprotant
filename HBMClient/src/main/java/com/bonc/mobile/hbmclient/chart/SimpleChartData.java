package com.bonc.mobile.hbmclient.chart;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.util.NumberUtil;

/**
 * @author sunwei
 */
public class SimpleChartData {
	public double[] seriesData, seriesData2;
	public String[] cats;
	public String[] xTextLabels;

	public static DataConverter Div10KConverter = new DataConverter() {
		@Override
		public double convert(double v) {
			return v / 10000.0;
		}
	};

	public static DataConverter PercentConverter = new DataConverter() {
		@Override
		public double convert(double v) {
			return Double.valueOf(String.format("%.1f", v * 100));
		}
	};

	public static SimpleChartData build(JSONArray array, String col,
			DataConverter converter) {
		return build(JsonUtil.toList(array), col, converter);
	}

	public static SimpleChartData build(List<Map<String, String>> list,
			String col, DataConverter converter) {
		SimpleChartData data = new SimpleChartData();
		int len = list.size();
		data.seriesData = new double[len];
		data.cats = new String[len];
		for (int i = 0; i < len; i++) {
			Map<String, String> o = list.get(i);
			data.cats[i] = (String) o.get("kpi_name");
			double v = NumberUtil.changeToDouble(o.get(col));
			if (converter != null)
				data.seriesData[i] = converter.convert(v);
			else
				data.seriesData[i] = v;
		}
		return data;
	}

	public static SimpleChartData build(JSONArray array1, String col1,
			JSONArray array2, String col2, DataConverter converter) {
		SimpleChartData data = new SimpleChartData();
		int len = Math.min(array1.length(), array2.length());
		data.seriesData = new double[len];
		data.seriesData2 = new double[len];
		for (int i = 0; i < len; i++) {
			JSONObject o1 = array1.optJSONObject(i);
			JSONObject o2 = array2.optJSONObject(i);
			if (converter != null) {
				data.seriesData[i] = converter.convert(o1.optDouble(col1));
				data.seriesData2[i] = converter.convert(o2.optDouble(col2));
			} else {
				data.seriesData[i] = o1.optDouble(col1);
				data.seriesData2[i] = o2.optDouble(col2);
			}
		}
		return data;
	}

	public static SimpleChartData build(double[] array1, DataConverter converter) {
		SimpleChartData data = new SimpleChartData();
		int len = array1.length;
		data.seriesData = new double[len];
		data.seriesData2 = new double[len];
		for (int i = 0; i < len; i++) {
			if (converter != null) {
				data.seriesData[i] = converter.convert(array1[i]);
			} else {
				data.seriesData[i] = array1[i];
			}
		}
		return data;
	}

	public static SimpleChartData build(double[] array1, double[] array2,
			DataConverter converter) {
		SimpleChartData data = new SimpleChartData();
		int len = Math.min(array1.length, array2.length);
		data.seriesData = new double[len];
		data.seriesData2 = new double[len];
		for (int i = 0; i < len; i++) {
			if (converter != null) {
				data.seriesData[i] = converter.convert(array1[i]);
				data.seriesData2[i] = converter.convert(array2[i]);
			} else {
				data.seriesData[i] = array1[i];
				data.seriesData2[i] = array2[i];
			}
		}
		return data;
	}
}
