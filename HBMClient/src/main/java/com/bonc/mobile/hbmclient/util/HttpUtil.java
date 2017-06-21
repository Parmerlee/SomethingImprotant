package com.bonc.mobile.hbmclient.util;

import java.io.IOException;
import java.util.Map;

import com.bonc.mobile.common.util.DES;
import com.bonc.mobile.hbmclient.common.Constant;

/**
 * http请求的工具类.
 * 
 * @author tengshibo
 * 
 */

public class HttpUtil extends com.bonc.mobile.common.net.HttpUtil {
	public static String sendRequest(String action, Map<String, String> map) {
		try {
			return DES.decrypt2(sendRequest(Constant.SERVER_PATH, action, map));
		} catch (IOException e) {
			return null;
		}
	}

	public static String sendRequestByJSON(String action, String s) {
		try {
			return DES.decrypt2(sendRequest(Constant.SERVER_PATH, action, s));
		} catch (IOException e) {
			return null;
		}
	}
}
