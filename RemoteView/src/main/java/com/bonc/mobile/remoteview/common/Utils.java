package com.bonc.mobile.remoteview.common;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import com.bonc.mobile.common.util.JsonUtil;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ShapeDrawable;
import android.text.TextUtils;
import android.view.View;

public class Utils {
	/****
	 * yyyyMMDDHH
	 */
	static String mTime, mTime_temp;

	/***
	 * HB
	 */
	static JSONArray mAreacode;

	static String mAreaname = null;

	static Map<String, String> param;

	private static Activity context;

	public static int width;

	public static int getWidth() {
		return width;
	}

	public static void setWidth(int width) {
		Utils.width = width;
	}

	public static String getmTime_temp() {
		return mTime_temp;
	}

	public static void setmTime_temp(String mTime_temp) {
		Utils.mTime_temp = mTime_temp;
	}

	public static String getTime() {

		return mTime;
	}

	public static void setTime(String time) {
		mTime = time;
	}

	public static JSONArray getAreacode() {
		return mAreacode;
	}

	public static void setAreacode(JSONArray areacode) {
		mAreacode = areacode;
	}

	public static void setAreaname(String name) {
		mAreaname = name;
	}

	public static String getAreaname() {
		return mAreaname;
	}

	public static String changeValue(Object value) {
		DecimalFormat df1 = new DecimalFormat("0.00");
		return df1.format(value);
	}

	public static String changeValueWithoutPoint(Object value) {
		DecimalFormat df1 = new DecimalFormat("##");
		return df1.format(value);
	}

	public static String changeValue(String value) {
		DecimalFormat df1 = new DecimalFormat("0.00");
		return df1.format((String) value);
	}

	public static Bitmap CreateRoundconeorBg(View view, int bgcolor, int pixels) {

		// 创建一个和原始图片一样大小位图
		Bitmap output = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
				Config.ARGB_8888);
		// 创建带有位图bitmap的画布
		Canvas canvas = new Canvas(output);

		final int color = bgcolor;
		// 创建画笔
		final Paint paint = new Paint();
		// 创建一个和原始图片一样大小的矩形
		final Rect rect = new Rect(0, 0, view.getWidth(), view.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		// 去锯齿
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		// 画一个和原始图片一样大小的圆角矩形
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		// 设置相交模式
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// 把图片画到矩形去
		canvas.drawBitmap(null, rect, rect, paint);
		return output;
		// return null;

	}

	public static JSONArray removeTheEmptyItem_arr(JSONArray arr) {
		JSONArray list_temp = new JSONArray();
		for (int i = 0; i < arr.length(); i++) {
			try {
				if (arr.getJSONArray(i).length() == 0) {

				} else {
					list_temp.put(list_temp.length(), arr.getJSONArray(i));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return list_temp;
	}

	public static List<Map<String, String>> removeTheEmptyItem_list(
			List<Map<String, String>> list_kpi) {
		for (int j = 0; j < list_kpi.size(); j++) {
			if (list_kpi.get(j).size() == 0)
				list_kpi.remove(j);
		}
		return list_kpi;
	}

	public static String fetchAreaName(String string) {
		// TODO Auto-generated method stub
		String str = null;
		if (TextUtils.equals(string, "HB.ES"))
			str = "恩施";
		if (TextUtils.equals(string, "HB.EZ"))
			str = "鄂州";
		if (TextUtils.equals(string, "HB.HG"))
			str = "黄冈";
		if (TextUtils.equals(string, "HB.HS"))
			str = "黄石";
		if (TextUtils.equals(string, "HB.JH"))
			str = "江汉";
		if (TextUtils.equals(string, "HB.JM"))
			str = "荆门";
		if (TextUtils.equals(string, "HB.JZ"))
			str = "荆州";
		if (TextUtils.equals(string, "HB.QJ"))
			str = "潜江";
		if (TextUtils.equals(string, "HB.SY"))
			str = "十堰";
		if (TextUtils.equals(string, "HB.SZ"))
			str = "随州";
		if (TextUtils.equals(string, "HB.TM"))
			str = "天门";
		if (TextUtils.equals(string, "HB.WH"))
			str = "武汉";
		if (TextUtils.equals(string, "HB.XF"))
			str = "襄阳";
		if (TextUtils.equals(string, "HB.XG"))
			str = "孝感";
		if (TextUtils.equals(string, "HB.XN"))
			str = "咸宁";
		if (TextUtils.equals(string, "HB.YC"))
			str = "宜昌";

		return str;
	}

}
