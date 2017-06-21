package com.bonc.mobile.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import android.os.Build;
import android.text.TextUtils;

public class CmdUtil {
    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(
                    new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    return null;
                }
            }
        }
        return line;
    }

    /**
     * @return
     */
    public static boolean isMeiZu() {
        return CmdUtil.getSystemProperty("ro.build.display.id").toLowerCase()
                .contains("flyme");
    }

    /***
     * 仅能检测出是vibeui系统的手机 如联想乐檬 K3 Note
     * @return
     */
    public static boolean isLianXiang(){
        return CmdUtil.getSystemProperty("ro.build.display.id").toLowerCase()
                .contains("vibeui");
    }
    public static boolean isHuaWei() {

        boolean b = TextUtils.isEmpty(getSystemProperty("ro.build.hw_emui_api_level"))
                || TextUtils.isEmpty(getSystemProperty("ro.build.version.emui"))
                || TextUtils.isEmpty(getSystemProperty("ro.confg.hw_systemversion"));
        return !b;

    }

    public static boolean isXiaoMi() {

        boolean b = TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.code"))
                || TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name"))
                || TextUtils.isEmpty(getSystemProperty("ro.miui.internal.storage"));

        return !b;
    }

    public static boolean hasSmartBar() {
        try {

            Method method = Class.forName("android.os.Build").getMethod(
                    "hasSmartBar");
            return ((Boolean) method.invoke(null)).booleanValue();
        } catch (Exception e) {
        }

        if (Build.DEVICE.equals("mx2")) {
            return true;
        } else if (Build.DEVICE.equals("mx") || Build.DEVICE.equals("m9")) {
            return false;
        }
        return false;
    }
}
