
package com.bonc.mobile.common.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class NumberUtil {
    public static double changeToDouble(String intoString, double defaultDouble) {
        if (intoString == null)
            return defaultDouble;
        double result = defaultDouble;
        try {
            result = Double.parseDouble(intoString);
        } catch (NumberFormatException e) {
        }
        return result;
    }

    public static double changeToDouble(String intoString) {
        return changeToDouble(intoString, 0);
    }

    public static List<Double> toDoubleList(List<String> l) {
        List<Double> r = new ArrayList<Double>();
        for (String s : l)
            r.add(changeToDouble(s));
        return r;
    }

    public static double percentToDouble(String s) {
        if (s == null)
            return 0;
        s = s.replace("%", "");
        return changeToDouble(s);
    }

    public static int changeToInt(String intoString) {
        int result = 0;
        try {
            result = Integer.parseInt(intoString);
        } catch (NumberFormatException e) {
        }
        return result;
    }

    public static int DpToPx(Context context, int dpValue) {
        int pxValue = dpValue;
        final float scale = context.getResources().getDisplayMetrics().density;
        pxValue = (int) (dpValue * scale + 0.5f);
        return pxValue;
    }

    public static int PxToDp(Context context, int pxValue) {
        int dpValue = pxValue;
        final float scale = context.getResources().getDisplayMetrics().density;
        dpValue = (int) (pxValue / scale + 0.5f);
        return dpValue;
    }

    public static double getAverage(double[] array) {
        if (array.length == 0)
            return 0;
        double ret = 0;
        for (double d : array) {
            ret += Math.abs(d);
        }
        return ret / array.length;
    }

    /**
     * 返回当前数值的比值数，万或亿
     * 
     * @param value
     * @return
     */
    public static double getScale(double value) {
        double scale = 1;
        if (value >= 100000000) {
            scale = 100000000;
        } else if (value >= 10000) {
            scale = 10000;
        }
        return scale;
    }

    public static double getScale(double[] array) {
        return getScale(getAverage(array));
    }

    /**
     * 返回当前单位附加值，万或亿
     * 
     * @param value
     * @return
     */
    public static String getUnit(double scale) {
        String unit = "";

        if (scale >= 100000000) {

            unit = "亿";
        } else if (scale >= 10000) {

            unit = "万";
        }
        return unit;
    }

    public static String format(double value) {
        DecimalFormat df = new DecimalFormat("0.00");
        if (Math.abs(value) >= 100000000) {
            value = value / 100000000;
            return df.format(value) + "亿";
        } else if (Math.abs(value) >= 10000) {
            value = value / 10000;
            return df.format(value) + "万";
        }
        return df.format(value);
    }

    public static String format(double value, String fmt) {
        if ("D".equals(fmt))
            return format(value);
        DecimalFormat df = new DecimalFormat(fmt);
        return df.format(value);
    }

    /**
     * 计算数组的最大最小值
     */
    public static double[] getMaxMinNumber(double[] array) {
        int index = 0;
        double max = 0;
        double min = 0;
        double[] maxMin = new double[2];
        if (array.length == 0)
            return maxMin;
        if (array.length % 2 == 0) {
            if (array[0] <= array[1]) {
                min = array[0];
                max = array[1];
            } else {
                min = array[1];
                max = array[0];
            }

            index = 2;
        } else {
            min = array[0];
            max = array[0];

            index = 1;
        }

        for (int i = index; i < array.length - 1; i++) {
            if (array[i] <= array[i + 1]) {
                if (array[i] < min)
                    min = array[i];

                if (array[i + 1] > max)
                    max = array[i + 1];
            } else {
                if (array[i + 1] < min)
                    min = array[i + 1];

                if (array[i] > max)
                    max = array[i];
            }
        }
        maxMin[0] = max;
        maxMin[1] = min;
        return maxMin;

    }

    public static double getMaxNumber(double[] array) {
        return getMaxMinNumber(array)[0];
    }
}
