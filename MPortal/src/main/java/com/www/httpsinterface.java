package com.www;

import android.app.Activity;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.Constant;
import com.bonc.mobile.common.net.HttpUtil;
import com.bonc.mobile.common.util.DES;
import com.bonc.mobile.common.util.FileUtils;

import com.bonc.mobile.portal.HelpActivity;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class httpsinterface extends CordovaPlugin {

	// 家宽第一个界面
	public static final String METHOD_INDEX = "GetJiakuanData";

	// 家宽第二个界面
	public static final String METHOD_DETAIL = "GetJiakuanData2";

	// 家宽第三个界面 排名
	public static final String METHOD_RANK = "GetJiakuanData3";

	// 政企第一个界面
	public static final String GEMARKET_INDEX = "GetGeMarketData";

	// 政企第二个界面
	public static final String GEMARKET_DETAIL = "GetGeMarketData2";

	// 第三个界面 排名
	public static final String GEMARKET_RANK = "GetGeMarketData3";

	// 获得地域
	public static final String METHOD_AREA = "GetJiakuanArea";

	// 返回上一页
	public static final String METHOD_BACK = "NativeGoBack";

	// 分享
	public static final String METHOD_SHARE = "Html5Share";

	JSONObject obj_index = null;

	JSONObject obj_detail = null;

	String menuCode;

	/**
	 * Executes the request and returns PluginResult.
	 *
	 * @param action
	 *            The action to execute.
	 * @param args
	 *            JSONArray of arguments for the plugin.
	 * @param callbackContext
	 *            The callback context used when calling back into JavaScript.
	 * @return True if the action was valid, false otherwise.
	 */
	String result;

	CallbackContext callbackContext;

	Map<String, String> params;

	Activity context;

	@JavascriptInterface
	public boolean execute(String action, JSONArray args,
			final CallbackContext callbackContext) throws JSONException {

		context = this.cordova.getActivity();

//		menuCode = ((BaseCordovaActivity) (this.cordova.getActivity()))
//				.getIntent().getStringExtra(MenuEntryAdapter.KEY_MENU_CODE);

		params = new LinkedHashMap<String, String>();

		if (TextUtils.equals(action, METHOD_INDEX)) {
			JSONObject object = new JSONObject(args.getString(0));
			this.callbackContext = callbackContext;

			if (object.length() == 0) {
				object = obj_index;
			} else {
				obj_index = object;
			}
			params.put("menuCode", "50001101");
			params.put("dataType", object.getString("pareamtype"));
			params.put("areaCode", object.getString("paramarea"));
			params.put("time", object.getString("paramtime"));

			new Thread(new Runnable() {

				public void run() {
					String result = null;
					try {
						result = DES.decrypt2(HttpUtil.sendRequest(
								Constant.BASE_PATH,
								"/bi/network/analysis/index", params));

						PluginResult pluginResults = new PluginResult(
								PluginResult.Status.OK, result);
						callbackContext.sendPluginResult(pluginResults);
						pluginResults.setKeepCallback(true);
						callbackContext.success();
						if (!AppConstant.SEC_ENH)
							System.out.println(result);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
			return true;

		} else if (TextUtils.equals(action, METHOD_DETAIL)) {
			JSONObject object = new JSONObject(args.getString(0));

			params.put("menuCode", "50001101");
			params.put("dataType", object.getString("dateType"));
			params.put("areaCode", object.getString("areaCode"));
			params.put("time", object.getString("time"));
			params.put("kpiCode", object.getString("kpiCode"));
			params.put("showType", object.getString("showType"));
			params.put("areaShowType", object.getString("areaShowType"));
			params.put("rowNumber", object.getString("rowNumber"));

			new Thread(new Runnable() {

				public void run() {
					String result = null;
					try {
						result = DES.decrypt2(HttpUtil.sendRequest(
								Constant.BASE_PATH,
								"/bi/network/analysis/detailDisplay", params));

						PluginResult pluginResults = new PluginResult(
								PluginResult.Status.OK, result);
						callbackContext.sendPluginResult(pluginResults);
						pluginResults.setKeepCallback(true);
						callbackContext.success();
						if (!AppConstant.SEC_ENH)
							System.out.println(result);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();

		} else if (TextUtils.equals(action, METHOD_AREA)) {
			JSONObject object = new JSONObject(args.getString(0));
			if (object.length() == 0) {
				object = obj_detail;
			} else {
				obj_detail = object;
			}

			params.put("areaCode", object.getString("areaName"));

			new Thread(new Runnable() {

				public void run() {
					String result = null;
					try {
						result = DES.decrypt2(HttpUtil.sendRequest(
								Constant.BASE_PATH, "/bi/getAreaList/general",
								params));

						PluginResult pluginResults = new PluginResult(
								PluginResult.Status.OK, result);
						callbackContext.sendPluginResult(pluginResults);
						pluginResults.setKeepCallback(true);
						callbackContext.success();
						if (!AppConstant.SEC_ENH)
							System.out.println(result);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		} else if (TextUtils.equals(action, METHOD_BACK)) {

			this.cordova.getActivity().finish();
			return true;
		} else if (TextUtils.equals(action, METHOD_SHARE)) {

			((HelpActivity) context).runOnUiThread(new Runnable() {

				@Override
				public void run() {

					FileUtils.shareScreen(context,new WebView(context));

				}
			});

			return true;
		} else if (TextUtils.equals(action, GEMARKET_DETAIL)) {
			JSONObject object = new JSONObject(args.getString(0));

			params.put("menuCode", "40001100");
			params.put("dataType", object.optString("dateType"));
			params.put("areaCode", object.optString("areaCode"));
			params.put("time", object.optString("time"));
			params.put("period", object.optString("rowNumber"));
			params.put("kpiCode", object.optString("kpiCode"));

			new Thread(new Runnable() {

				public void run() {
					String result = null;
					try {
						result = DES.decrypt2(HttpUtil.sendRequest(
								Constant.BASE_PATH, "/bi/esop/market/detail",
								params));

						PluginResult pluginResults = new PluginResult(
								PluginResult.Status.OK, result);
						callbackContext.sendPluginResult(pluginResults);
						pluginResults.setKeepCallback(true);
						callbackContext.success();
						if (!AppConstant.SEC_ENH)
							System.out.println(result);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();

			// return true;
		} else if (TextUtils.equals(action, GEMARKET_INDEX)) {
			JSONObject object = new JSONObject(args.getString(0));

			params.put("menuCode", "40001100");
			params.put("dataType", object.getString("pareamtype"));
			params.put("areaCode", object.getString("paramarea"));
			params.put("time", object.getString("paramtime"));

			new Thread(new Runnable() {

				public void run() {
					String result = null;
					try {
						result = DES.decrypt2(HttpUtil.sendRequest(
								Constant.BASE_PATH, "/bi/esop/market/index",
								params));
						PluginResult pluginResults = new PluginResult(
								PluginResult.Status.OK, result);
						callbackContext.sendPluginResult(pluginResults);
						pluginResults.setKeepCallback(true);
						callbackContext.success();
						if (!AppConstant.SEC_ENH)
							System.out.println(result);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		} else if (TextUtils.equals(action, GEMARKET_RANK)) {
			JSONObject object = new JSONObject(args.getString(0));

			// System.out.println("data:"+object.toString());
			// params.put("menuCode", "40001100");

			params.put("dataType", object.getString("dataType"));
			params.put("areaCode", object.getString("areaCode"));
			params.put("time", object.getString("time"));

			new Thread(new Runnable() {

				public void run() {
					String result = null;
					try {
						result = DES.decrypt2(HttpUtil.sendRequest(
								Constant.BASE_PATH, "/bi/esop/market/sort",
								params));
						PluginResult pluginResults = new PluginResult(
								PluginResult.Status.OK, result);
						callbackContext.sendPluginResult(pluginResults);
						pluginResults.setKeepCallback(true);
						callbackContext.success();
						if (!AppConstant.SEC_ENH) {
							for (int i = 0; i < 10; i++) {
								System.out.println(result.substring(i / 10,
										(i + 1) / 10));
							}

						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		} else if (TextUtils.equals(action, METHOD_RANK)) {
			JSONObject object = new JSONObject(args.getString(0));

			// params.put("menuCode", "40001100");
			params.put("dataType", object.getString("dataType"));
			params.put("areaCode", object.getString("areaCode"));
			params.put("time", object.getString("time"));

			new Thread(new Runnable() {

				public void run() {
					String result = null;
					try {
						result = DES.decrypt2(HttpUtil.sendRequest(
								Constant.BASE_PATH,
								"/bi/network/analysis/sort", params));
						PluginResult pluginResults = new PluginResult(
								PluginResult.Status.OK, result);
						callbackContext.sendPluginResult(pluginResults);
						pluginResults.setKeepCallback(true);
						callbackContext.success();
						if (!AppConstant.SEC_ENH)
							System.out.println(result);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		}
		return true;
	}

}
