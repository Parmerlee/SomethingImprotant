/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */
package org.apache.cordova.device;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.User;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.util.FileUtils;
import com.bonc.mobile.hbmclient.common.Constant;

public class MyPlugin extends CordovaPlugin {
	public static final String TAG = "SharePlugin";
	public static final String SHARE = "share";
	public static final String FETCH_DATE = "date";

	public CallbackContext callbackContext;

	/**
	 * Constructor.
	 */
	public MyPlugin() {
	}

	/**
	 * Sets the context of the Command. This can then be used to do things like
	 * get file paths associated with the Activity.
	 *
	 * @param cordova
	 *            The context of the main Activity.
	 * @param webView
	 *            The CordovaWebView Cordova is running in.
	 */
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);

	}

	/**
	 * Executes the request and returns PluginResult.
	 *
	 * @param action
	 *            The action to execute.
	 * @param args
	 *            JSONArry of arguments for the plugin.
	 * @param callbackContext
	 *            The callback id used when calling back into JavaScript.
	 * @return True if the action was valid, false if not.
	 */
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		if (TextUtils.equals(action, SHARE)) {
			FileUtils.shareScreen(cordova.getActivity());
			return true;
		} else if (TextUtils.equals(FETCH_DATE, action)) {
			this.callbackContext = callbackContext;

			String[] argsArr = getArgs(args);

			Map<String, String> param = new LinkedHashMap<String, String>();
			param.put("user4ACode", User.getInstance().userCode);
			param.put("sysType", "OA_SYS");

			new LoadAccountTask(this.cordova.getActivity()).execute(
					"/bi/network/analysis/index", param);

			return true;
		} else {
			return false;
		}

	}

	public String[] getArgs(JSONArray args) {
		String[] argsArr = new String[args.length()];
		for (int i = 0; i < args.length(); i++) {
			try {
				argsArr[i] = args.getString(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return argsArr;
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
				PluginResult r = new PluginResult(PluginResult.Status.OK,
						result);
				callbackContext.sendPluginResult(r);
			}
			if (callbackContext != null)
				callbackContext = null;
			// startActivity(new Intent(WelcomeActivity.this, Welcome.class));
			// finish();
		}
	}

}
