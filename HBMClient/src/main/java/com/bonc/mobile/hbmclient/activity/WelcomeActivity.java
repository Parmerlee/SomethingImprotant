package com.bonc.mobile.hbmclient.activity;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.User;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.MD5;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.SharePreferenceUtil;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.service.BoncService;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.saleclient.common.ConfigLoader;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

/**
 * @author sunwei
 */
public class WelcomeActivity extends Activity {
	private static final String mProtalPkgName = "com.bonc.anhuimobile.ac";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// f6fd9ec413d2d3974325fa597a6b41d4
		// F6:FD:9E:C4:13:D2:D3:97:43:25:FA:59:7A:6B:41:D4
		// checkSign();

		// //获取用户信息
		// initUserInfo();
		// //开启后台
		// startSvc();
		// //下载用户信息
		// loadAccount();

		if (!isTaskRoot()) {
			finish();
			return;
		}
		if (!MD5.checkSign(getApplicationContext())) {
			Toast.makeText(getApplicationContext(), "请从正确的途径下载安装包",
					Toast.LENGTH_LONG).show();
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					finish();
				}
			}, 1000);

			return;
		} else {

			// if (!MyUtils.isBackground(WelcomeActivity.this)) {
			// MyUtils.startProtal(WelcomeActivity.this);
			// } else {
			// 获取用户信息
			initUserInfo();
			// 开启后台
			startSvc();
			// 下载用户信息
			loadAccount();
			// }

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (MyUtils.doInfilter(this))
			if (!MyUtils.isBackground(this)) {
				MyUtils.startProtal(this);
			}
	};

	private void checkSign() {
		// TODO Auto-generated method stub

	}

	void initUserInfo() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		DataUtil.saveSettingE(sp, AppConstant.KEY_SERVER_PATH,
				Constant.SERVER_PATH);
		DataUtil.saveSetting(sp, "userLastLoginTime", DateUtil.formatter(
				Calendar.getInstance().getTime(), "yyyy-MM-dd HH:mm:ss"));
		Intent intent = getIntent();
		User user = User.getInstance();
		user.load(intent);
		user.loadTest();
		user.save(this);

		// Toast.makeText(this,
		// "aaa:" + intent.getBooleanExtra(AppConstant.KEY_LOGIN, false),
		// 10).show();

		SharePreferenceUtil.setLoginStatus(this,
				intent.getBooleanExtra(AppConstant.KEY_LOGIN, false));
	}

	void startSvc() {
		Intent serviceIntent = new Intent(this, BoncService.class);
		startService(serviceIntent);
	}

	void loadAccount() {
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("user4ACode", User.getInstance().userCode);
		param.put("sysType", "OA_SYS");
		new LoadAccountTask(this).execute("/sysUser/getAccount", param);
	}

	class LoadAccountTask extends HttpRequestTask {
		public LoadAccountTask(Context context) {
			super(context, Constant.BASE_PATH);
		}

		@Override
		protected void handleResult(JSONObject result) {
			if (!AppConstant.SEC_ENH) {
				System.out.println("result啊啊啊啊啊啊啊啊啊啊:" + result);
			}
			// {"flag",true};
			JSONObject data = result.optJSONObject("data");
			if (data != null) {
				ConfigLoader.getInstance(context).userId = data
						.optString("SYS_USERID");
			}
			startActivity(new Intent(WelcomeActivity.this, Welcome.class));
			finish();
		}
	}

}
