package com.bonc.mobile.common;

import com.bonc.mobile.common.util.Base64;

public class Constant {
    public static final boolean DEBUG = true;

    // https://120.202.17.117:29443
    // http://120.202.17.126:8070
    // http://192.168.1.114:9080

    // http://10.25.124.217

    // https://192.168.1.204:443
    // http://192.168.1.108:8080

    // 正式环境是：https://120.202.17.117:29443
    public static final String BASE_PATH = DEBUG ? "https://120.202.17.117:29443"
            : dec("aHR0cHM6Ly8xMjAuMjAyLjE3LjExNzoyOTQ0Mw==");

//    // 正式环境是：http://120.202.17.126:8070
//    public static final String BASE_PATH = DEBUG ?
//            "http://120.202.17.126:8070"
//            : dec("aHR0cDovLzEyMC4yMDIuMTcuMTI2OjgwNzA=");

    public static final String PORTAL_PATH = BASE_PATH;
    public static final String GE_PATH = BASE_PATH + "/esop/";
    public static final String RV_PATH = BASE_PATH + "/remoteview";
    public static final String STOCK_PATH = BASE_PATH + "/stock/";
    public static final String APK_PATH = DEBUG ? "https://raw.githubusercontent.com/jack20000/hbmobile/master"
            : dec("aHR0cHM6Ly9yYXcuZ2l0aHVidXNlcmNvbnRlbnQuY29tL2phY2syMDAwMC9o Ym1vYmlsZS9tYXN0ZXI=");
    public static final String PORTAL_URL = BASE_PATH
            + "/PalmBiServer/ios/sys_downIOSApp.do";

    public static String dec(String s) {

        return new String(Base64.decode(s));
    }

}
