package com.bonc.mobile.common.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtil {

	public final static String mString_Logout = "com.bonc.mProtal.loginout";

	public final static void setLoginStatus(Context context, boolean userid) {
		SharedPreferences sp = context.getSharedPreferences("set",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean("userid", userid);
		editor.commit();
	}

	public final static boolean getLoginStatus(Context context) {
		SharedPreferences sp = context.getSharedPreferences("set",
				Context.MODE_PRIVATE);
		boolean userid = sp.getBoolean("userid", false);
		return userid;
	}
}
