package com.bonc.mobile.hbmclient.util;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author sunwei
 */
public class JsonUtil {
	public static String optString(JSONObject o, String name) {
		return o.isNull(name) ? "" : o.optString(name);
	}

	public static String toJson(Object src) {
		Gson gson = new Gson();
		return gson.toJson(src);
	}

	public static Map<String, String> toMap(String s) {
		Gson gson = new Gson();
		return gson.fromJson(s, new TypeToken<Map<String, String>>() {
		}.getType());
	}

	public static Map<String, String> toMap(JSONObject o) {
		Gson gson = new Gson();
		return gson.fromJson(o.toString(),
				new TypeToken<Map<String, String>>() {
				}.getType());
	}

	public static List<Map<String, String>> toList(String a) {
		Gson gson = new Gson();
		return gson.fromJson(a, new TypeToken<List<Map<String, String>>>() {
		}.getType());
	}

	public static List<Map<String, String>> toList(JSONArray a) {
		Gson gson = new Gson();
		return gson.fromJson(a.toString(),
				new TypeToken<List<Map<String, String>>>() {
				}.getType());
	}

	public static List<Map<String, Object>> toList2(JSONArray a) {
		Gson gson = new Gson();
		return gson.fromJson(a.toString(),
				new TypeToken<List<Map<String, Object>>>() {
				}.getType());
	}

	public static void put(JSONObject o, String name, double value) {
		try {
			o.put(name, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static List<Map<String, String>> toDataList(JSONArray a) {
		Gson gson = new Gson();
		return gson.fromJson(a.toString(),
				new TypeToken<List<Map<String, String>>>() {
				}.getType());
	}

	public static Map<String, List<Double>> toTrendMap(JSONObject jo) {
		Gson gson = new Gson();
		return gson.fromJson(jo.toString(),
				new TypeToken<Map<String, List<Double>>>() {
				}.getType());
	}

	public static List<String> toReportList(JSONArray a) {
		Gson gson = new Gson();
		return gson.fromJson(a.toString(), new TypeToken<List<String>>() {
		}.getType());
	}

	public static Map<String, Map<String, String>> toMap2(JSONObject jo) {
		Gson gson = new Gson();
		return gson.fromJson(jo.toString(),
				new TypeToken<Map<String, Map<String, String>>>() {
				}.getType());
	}
}
