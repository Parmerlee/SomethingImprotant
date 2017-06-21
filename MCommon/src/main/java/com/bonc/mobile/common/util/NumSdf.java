package com.bonc.mobile.common.util;

import android.text.TextUtils;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/1/16.
 */
public class NumSdf {
    public static String changeData(String number) {
        LogUtils.logBySys("numner:" + number);
        String data = null;
        try {
            int length = number.length();
            if (length > 4) {
                Float f = (Float.valueOf(number) / 10000);
                data = getProcessText(String.valueOf(f));
                data += "万";
                return data;
            } else {
                return number;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return number;
        }
    }

    private static String getProcessText(String str) {
        LogUtils.logBySys("AAA:" + str);
        String string = null;
        if (TextUtils.equals(str, "0") || TextUtils.equals(str, "0.0")) {
            return "0";
        }
        Float i = Float.valueOf(str);
//            new DecimalFormat("##0.00").format(i * 100);
        return String.valueOf(new DecimalFormat("##0").format(i));
    }
}
