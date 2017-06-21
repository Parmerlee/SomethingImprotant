package com.bonc.mobile.common.net;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.R;
import com.bonc.mobile.common.EventBus.DefaultEvent;
import com.bonc.mobile.common.util.DES;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.LogUtils;

//import de.greenrobot.event.EventBus;
import org.greenrobot.eventbus.EventBus;

/**
 * http请求Task
 * 
 * @author sunwei
 */
public class HttpRequestTask extends AsyncTask<Object, Integer, String> {
	protected Context context;
	protected ProgressDialog dialog;
	protected boolean retAsArray, showDialog = true;
	protected String basePath;

	public HttpRequestTask(Context context) {
		this(context, DataUtil.getSettingE(context,
				AppConstant.KEY_SERVER_PATH, ""));
	}

	public HttpRequestTask(Context context, String basePath) {
		super();
		this.context = context;
		this.basePath = basePath;
	}

	@Override
	protected void onPreExecute() {
		if (showDialog)
			dialog = ProgressDialog.show(context,
					context.getString(R.string.hint),
					context.getString(R.string.loading_data));
	}

	@Override
	protected String doInBackground(Object... params) {
		String r = null;
		String action = (String) params[0];
		LogUtils.logBySys("action:"+action+";equal:"+(params[1] instanceof String));
		try {
			if (params[1] instanceof String) {
				r = HttpUtil.sendRequest(basePath, action, (String) params[1]);
			} else {
				if (action.startsWith("post")) {

					action = action.replace("post", "");
					
					r = HttpUtil.sendRequest2(basePath, action,
							(Map<String, String>) params[1]);
				} else {

					r = HttpUtil.sendRequest(basePath, action,
							(Map<String, String>) params[1]);
				}
			}
		} catch (Exception e) {
			LogUtils.debug(getClass(), e);

			EventBus.getDefault().post(new DefaultEvent(e.getMessage()));
			// context.sendBroadcast(new Intent("log"));
			e.printStackTrace();
			// return e.toString();
		}
		return r;
	}

	@Override
	protected void onPostExecute(String result) {
		if (dialog != null && context != null && isValidContext(context))
			dialog.dismiss();
		if (result == null) {
			showToast(R.string.network_error);
			EventBus.getDefault()
					.post(new DefaultEvent(context
							.getString(R.string.network_error)));
		} else if (result.length() == 0) {
			showToast(R.string.data_error);
			EventBus.getDefault().post(
					new DefaultEvent(context.getString(R.string.data_error)));
		} else {
			handleResult(DES.decrypt2(result));
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	private boolean isValidContext(Context c) {

		Activity a = (Activity) c;

		if (Build.VERSION.SDK_INT >= 17)
			if (a.isDestroyed() || a.isFinishing()) {

				return false;
			} else {
				return true;
			}
		else
			return true;
	}

	protected void showToast(String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	protected void showToast(int id) {
		showToast(context.getString(id));
	}

	protected void handleResult(String result) {
		// if (!AppConstant.SEC_ENH)
		// Log.d("result", result);
		LogUtils.debug(getClass(), result);
		try {
			if (retAsArray)
				handleResult(new JSONArray(result));
			else
				handleResult(new JSONObject(result));
		} catch (JSONException e) {
			EventBus.getDefault().post(new DefaultEvent("log"));
			// LogUtils.debug(getClass(), "数据格式转化异常："+e.toString());
			showToast(R.string.data_error);
			e.printStackTrace();
		}
	}

	protected void handleResult(JSONObject result) {
	}

	protected void handleResult(JSONArray result) {
	}

	public void setRetAsArray(boolean retAsArray) {
		this.retAsArray = retAsArray;
	}
}
