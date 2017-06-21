
package com.bonc.mobile.common.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

public class StringUtil {
    public static final String NULL_DEF = "--";

    /**
     * 将null值转换为空字符串.
     * 
     * @param value
     * @return
     */
    public static String nullToString(String value) {
        return value == null ? "" : value;
    }

    public static String nullToString2(String value) {
        return isNull(value) ? NULL_DEF : value;
    }

    /**
     * 判断字符串是否为空. null 和 "" 都认为是空
     */
    public static boolean isNull(String value) {
        return TextUtils.isEmpty(value) || "null".equals(value);
    }

    public static String join(List<String> list) {
        if (list.size() == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        sb.append(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            sb.append(",").append(list.get(i));
        }
        return sb.toString();
    }

    public static String abbr(String s, int len) {
        if (s.length() > len)
            return s.substring(0, len) + "...";
        else
            return s;
    }

    public static boolean isPhoneNumberInvalid(String phoneNumber) {
        String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
        String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        Pattern pattern2 = Pattern.compile(expression2);
        Matcher matcher2 = pattern2.matcher(inputStr);
        return (matcher.matches() || matcher2.matches()) ? false : true;
    }
}
