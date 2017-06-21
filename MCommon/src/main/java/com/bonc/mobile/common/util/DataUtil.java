
package com.bonc.mobile.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.bonc.mobile.common.AppConstant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author sunwei
 */
public class DataUtil {
    public static void saveSetting(SharedPreferences sp, String key, String value) {
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void saveSetting(SharedPreferences sp, String key, boolean value) {
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void saveSetting(SharedPreferences sp, String key, int value) {
        Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void removeSetting(SharedPreferences sp, String key) {
        Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    public static String getSettingE(Context context, String key, String value) {
        String s = PreferenceManager.getDefaultSharedPreferences(context).getString(key, value);
        if (AppConstant.SEC_ENH && s.endsWith("="))
            s = DES.decrypt(s);
        return s;
    }

    public static void saveSettingE(SharedPreferences sp, String key, String value) {
        if (AppConstant.SEC_ENH)
            value = DES.encrypt(value);
        saveSetting(sp, key, value);
    }

    public static int getInt(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, 0);
    }

    public static List<String> extractList(List<Map<String, String>> list, String key) {
        List<String> l = new ArrayList<String>();
        for (Map<String, String> m : list) {
            l.add(m.get(key));
        }
        return l;
    }

    public static List<String> extractList(List<Map<String, String>> list, String key,
            long[] itemIds) {
        List<String> l = new ArrayList<String>();
        for (long i : itemIds) {
            l.add(list.get((int) i).get(key));
        }
        return l;
    }

    public static List<Double> extractValList(List<Map<String, String>> list, String key) {
        List<Double> l = new ArrayList<Double>();
        for (int i = 0; i < list.size(); i++) {
            l.add(NumberUtil.changeToDouble(list.get(i).get(key)));
        }
        return l;
    }

    public static double[] extractValArray(List<Map<String, String>> list, String key) {
        double[] l = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            l[i] = NumberUtil.changeToDouble(list.get(i).get(key));
        }
        return l;
    }

    public static List<Map<String, String>> extractList(List<Map<String, String>> list, String key,
            String val) {
        List<Map<String, String>> l = new ArrayList<Map<String, String>>();
        for (Map<String, String> m : list) {
            if (val.equals(m.get(key)))
                l.add(m);
        }
        return l;
    }

    public static Map<String, String> extractRow(List<Map<String, String>> list, String key,
            String val) {
        for (Map<String, String> m : list) {
            if (val.equals(m.get(key)))
                return m;
        }
        return null;
    }

    public static int getRowNum(List<Map<String, String>> list, String key, String val) {
        for (int i = 0; i < list.size(); i++) {
            if (val.equals(list.get(i).get(key))) {
                return i;
            }
        }
        return -1;
    }

    public static void deleteRows(List<Map<String, String>> list, String key, String val) {
        Iterator<Map<String, String>> iterator = list.iterator();
        while (iterator.hasNext()) {
            Map<String, String> m = iterator.next();
            if (val.equals(m.get(key)))
                iterator.remove();
        }
    }
}
