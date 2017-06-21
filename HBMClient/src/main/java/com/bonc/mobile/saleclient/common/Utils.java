package com.bonc.mobile.saleclient.common;

import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

import com.bonc.mobile.common.util.JsonUtil;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Utils {

	public static boolean checkResult(JSONObject result, Activity activity) {
		// TODO Auto-generated method stub
		boolean flag = true;
		JSONObject obj = new JSONObject();
		try {
			obj = result.getJSONObject("head");
			int code = obj.getInt("retCode");
			if (code != 1) {
				Toast.makeText(activity, obj.getString("retMsg"), 1).show();
				return false;
			}
			// Log.d("AAAAAAA",
			// "BBBBBBBBBBB:"+JsonUtil.toList(JsonUtil.optJSONArray(result,
			// "body")).size());
			// if (JsonUtil.toList(JsonUtil.optJSONArray(result, "body")).size()
			// == 0) {
			// Toast.makeText(activity, "暂无数据", 1).show();
			// return false;
			// }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(activity, "服务器异常，请稍后重试", 1).show();
			return false;
		}
		return flag;
	}

	public static String changeValue(Object value) {
		DecimalFormat df1 = new DecimalFormat("0.00");
		return df1.format(value);
	}

	public static AbsListView.LayoutParams createParams(Activity actiivty, int i) {

		WindowManager wm = (WindowManager) actiivty
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();

		// if (i < 3) {
		// return new AbsListView.LayoutParams(width / i, width / 3);
		// } else {

		if (i != -1) {

			return new AbsListView.LayoutParams(width / i,
					AbsListView.LayoutParams.WRAP_CONTENT);
		} else {
			return new AbsListView.LayoutParams(
					AbsListView.LayoutParams.WRAP_CONTENT,
					AbsListView.LayoutParams.WRAP_CONTENT);
		}
		// }
	}

	public static String getStringWithException(JSONObject obj, String key) {

		try {
			return obj.getString(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return " ";
		}

	}

	public static LinearLayout.LayoutParams createParams(int j,
			Activity actiivty, int i) {

		WindowManager wm = (WindowManager) actiivty
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();

		// if (i < 3) {
		// return new AbsListView.LayoutParams(width / i, width / 3);
		// } else {

		return new LinearLayout.LayoutParams(width / i, 1);
		// }
	}

	public static int getSystemWidth(Activity activity) {
		WindowManager wm = (WindowManager) activity
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		return width;
	}

	public static int getSystemWidth(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		return width;
	}

	public static int getSystemHeight(Activity activity) {
		WindowManager wm = (WindowManager) activity
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getHeight();
		return width;
	}

	public static int getSystemHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getHeight();
		return width;
	}
}
