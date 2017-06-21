package com.bonc.mobile.hbmclient.activity;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.activity.BaseTabActivity;
import com.bonc.mobile.common.util.FileUtils;
import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.net.ConnectManager;

public class BaseActivity extends MenuActivity implements OnClickListener {

	public static ProgressDialog pDialog = null;
	protected Handler handler;

	private static final String mProtalPkgName = "com.bonc.anhuimobile.ac";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LogUtils.debug("AAAA", "the current Activity:" + this.getClass().getName());
		handler = new Handler();

	}

	protected void onResume() {
		super.onResume();
		if (MyUtils.doInfilter(BaseActivity.this)) {
			if (!MyUtils.isBackground(this)) {
				MyUtils.startProtal(this);
			}
		}
	};

	ProgressDialog pd_base;

	@Override
	protected Dialog onCreateDialog(int id) {
		pd_base = new ProgressDialog(this);
		switch (id) {
		case LOADING_DIALOG:
			pd_base.setTitle(R.string.hint);
			pd_base.setMessage(getString(R.string.loading_data));
			pd_base.setCancelable(true);
			break;
		}
		return pd_base;
	}

	protected void changeProgressMessage(String mes) {
		if (pd_base != null && pd_base.isShowing()) {
			pd_base.setMessage(mes);
		}
	}

	public void showMessageDailog(Activity activity) {
		pDialog = new ProgressDialog(activity);
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setCancelable(true);
		pDialog.show();
		pDialog.setContentView(R.layout.progress);
	}

	/**
	 * Toast
	 * 
	 * @param context
	 * @param String
	 *            toastMsg
	 */
	protected void toast(Context context, String toastMsg) {
		Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 刷新列表
	 * 
	 * @param which
	 * @param state
	 */
	public void refreshListView(final int which, final int state) {

	}

	/**
	 * 判断网络是否不通.
	 */
	public boolean isNetWorkOk() {

		if (!ConnectManager.isConnected()) {
			Toast.makeText(this, "网络未连接!!", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bottom_back_menu:
		case R.id.main_back:
			this.finish();
			break;

		case R.id.id_share:
			FileUtils.shareScreen(this);
			break;
		}
	}

}
