package com.bonc.mobile.hbmclient.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bonc.mobile.hbmclient.common.Publicapp;
import com.bonc.mobile.hbmclient.util.LogUtil;

public class ConnectManager {

	public static boolean isConnected() {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) Publicapp
					.getInstance().getSystemService(
							Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			LogUtil.error("error", e.toString());
		}

		return false;
	}

}
