package com.bonc.mobile.common;

import java.util.HashMap;
import java.util.Map;

import com.bonc.mobile.common.util.DES;
import com.bonc.mobile.common.util.UIUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;

public class User {

	public String userCode, password, imei, imsi, phone, mobileKey, userLevel,
			mode, sessionId;

	public boolean isLogin;

	private static User user = new User();

	public static User getInstance() {
		return user;
	}

	// public void LoginInfo()
	// 清空上次保存的用户信息
	public void clearUserInfo(Context context) {
		mobileKey = null;
		userCode = null;
		// imei = null;
		// imsi = null;
		// password = null;
		// mode = null;
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		pref.edit().clear();
		pref.edit().commit();

	}

	public void load(Intent intent) {
		mobileKey = intent.getStringExtra(AppConstant.KEY_MOBILE_KEY);
		userCode = intent.getStringExtra(AppConstant.KEY_USERCODE);
		imei = intent.getStringExtra(AppConstant.KEY_IMEI);
		imsi = intent.getStringExtra(AppConstant.KEY_IMSI);
		password = intent.getStringExtra(AppConstant.KEY_PASSWORD);
		mode = intent.getStringExtra(AppConstant.KEY_MODE);
		// sessionId = intent.getStringExtra(AppConstant.KEY_SESSIONID);
	}

	//http://120.202.17.126:8070/sysResource/appList?os=android&termType=android&usercode=piyue_xf&userCode=piyue_xf&imei=867609029572328&imsi=460027027147092&mobileKey=ea51ba485ab44ebcb950827dd03a2d42&password=C86038FE04BC7097E218B4FD6B767F4B
	public void loadTest() {
		if (user.mobileKey == null) {
			if (!AppConstant.SEC_ENH) {
				// ceshi
				mobileKey = "a8922b13a1d641a89dfc9570fbd1d44b";
				// usercode=lijing_bonc&userCode=lijing_bonc&imei=867609029572328&imsi=460027027147092&mobileKey=65c40477a5464b93a025aeb117a35a72&password=83CDCEC08FBF90370FCF53BDD56604FF
				// //zhengshi
//				mobileKey = "20f5e695f0c44c5bb688d640b79e55aa";
				userCode = "lijing_bonc";
				imei = "867609029572328";
				imsi = "460027027147092";
				password = "C86038FE04BC7097E218B4FD6B767F4B";
				mode = "production";

			}
		}
	}

	public void load(Context context) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		imei = pref.getString(AppConstant.KEY_IMEI, null);
		imsi = pref.getString(AppConstant.KEY_IMSI, null);
		mobileKey = pref.getString(AppConstant.KEY_MOBILE_KEY, null);
		userCode = pref.getString(AppConstant.KEY_USERCODE, null);
		password = pref.getString(AppConstant.KEY_PASSWORD, null);
		mode = pref.getString(AppConstant.KEY_MODE, null);

		phone = DES.decrypt(pref.getString(AppConstant.KEY_PHONE, null));

		sessionId = pref.getString(AppConstant.KEY_SESSIONID, null);
	}

	public void save(Context context) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = pref.edit();
		editor.putString(AppConstant.KEY_IMEI, imei);
		editor.putString(AppConstant.KEY_IMSI, imsi);
		editor.putString(AppConstant.KEY_MOBILE_KEY, mobileKey);
		if (!AppConstant.SEC_ENH) {
			editor.putString(AppConstant.KEY_USERCODE, userCode);
			editor.putString(AppConstant.KEY_PASSWORD, password);
		}
		editor.putString(AppConstant.KEY_MODE, mode);

		editor.putString(AppConstant.KEY_PHONE, DES.encrypt(phone));

		editor.putString(AppConstant.KEY_SESSIONID, sessionId);
		editor.commit();
	}

	public Map<String, String> getUserMap() {
		Map<String, String> userMap = new HashMap<String, String>();
		userMap.put(AppConstant.KEY_PASSWORD, password);
		userMap.put(AppConstant.KEY_USERCODE, userCode);
		userMap.put("usercode", userCode);
		userMap.put(AppConstant.KEY_IMEI, imei);
		userMap.put(AppConstant.KEY_IMSI, imsi);
		userMap.put(AppConstant.KEY_MOBILE_KEY, mobileKey);
		userMap.put("os", "android");
		userMap.put("termType", "android");
		return userMap;
	}

	public boolean isProduct() {
		return  true;

//		if (TextUtils.isEmpty(mode) && AppConstant.SEC_ENH) {
//			return true;
//		} else {
//			return "production".equals(mode);
//		}
	}

	// 兼容代码
	public static User getSingleInstance() {
		return getInstance();
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}

	public String getUUID(Context context) {
		return mobileKey;
	}

	public void setUUID(String s) {
		mobileKey = s;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public String toString() {
		return "User [userCode=" + userCode + ", password=" + password
				+ ", imei=" + imei + ", imsi=" + imsi + ", phone=" + phone
				+ ", mobileKey=" + mobileKey + ", userLevel=" + userLevel
				+ ", mode=" + mode + ", sessionId=" + sessionId + ", isLogin="
				+ isLogin + "]";
	}

}
