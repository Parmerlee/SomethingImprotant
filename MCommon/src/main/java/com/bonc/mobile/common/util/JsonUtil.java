
package com.bonc.mobile.common.util;

import java.lang.reflect.Type;
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

    public static JSONArray optJSONArray(JSONObject o,String name){
        return o.isNull(name) ? new  JSONArray(): o.optJSONArray(name);
    }

    public static String toJson(Object src) {
        Gson gson = new Gson();
        return gson.toJson(src);
    }

    public static <T> T fromJson(String s, Type typeOfT) {
        Gson gson = new Gson();
        return gson.fromJson(s, typeOfT);
    }

    public static Map<String, String> toMap(String s) {
        return fromJson(s, new TypeToken<Map<String, String>>() {
        }.getType());
    }

    public static Map<String, String> toMap(JSONObject o) {
        return toMap(o.toString());
    }

    public static List<Map<String, String>> toList(String a) {
        return fromJson(a, new TypeToken<List<Map<String, String>>>() {
        }.getType());
    }

    public static List<Map<String, String>> toList(JSONArray a) {
        return toList(a.toString());
    }

    public static void put(JSONObject o, String name, double value) {
        try {
            o.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, List<Map<String, String>>> toMap2(String a) {
        Gson gson = new Gson();
        return gson.fromJson(a, new TypeToken<Map<String, List<Map<String, String>>>>() {
        }.getType());
    }
}
