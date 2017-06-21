package com.bonc.mobile.remoteview.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bonc.mobile.common.chart.DialChartView;
import com.bonc.mobile.common.util.DateUtil;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.StringUtil;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.R.color;
import com.bonc.mobile.remoteview.activity.HolidayMaintainActivity;
import com.bonc.mobile.remoteview.common.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class HMBaseAdapter extends BaseAdapter {

	protected int textViewResourceId;

	protected LayoutParams params;

	protected Activity activity;

	protected List<Map<String, String>> list_kpi = new ArrayList<Map<String, String>>();

	protected JSONArray list_dates = new JSONArray();

	protected String time;

	protected int[] posarr = new int[] {};

	public HMBaseAdapter(Context context, int textViewResourceId,
			LayoutParams params, JSONObject object) {
		// TODO Auto-generated constructor stub
		activity = (Activity) context;
		this.textViewResourceId = textViewResourceId;
		this.params = params;

		list_kpi = JsonUtil.toList(JsonUtil.optJSONArray(object, "kpiData"));

		list_dates = JsonUtil.optJSONArray(object, "datas");

		list_dates = Utils.removeTheEmptyItem_arr(list_dates);

	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public int getCount() {

		// Log.d("AAAA", "list_kpi.size():" + list_kpi.size());
		// Log.d("AAAA", "list_dates.length():" + list_dates.length());
		// return list_kpi.size() > list_dates.length() ? list_dates.length()
		// : list_kpi.size();
		return list_dates.length();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		return convertView;
	}

	/***
	 * 根据KPI_CODE找到在list_dates中的位置
	 * 
	 * @param key
	 *            list_kpi中某个item中KPI_CODE对应的value
	 * @return
	 * @throws JSONException
	 */
	protected int getposition(String value) throws JSONException {

		int pos = -1;
		for (int i = 0; i < list_kpi.size(); i++) {
			if (TextUtils.equals(value, list_kpi.get(i).get("KPI_CODE"))) {
				return i;
			}
		}
		return pos;
	}

	/***
	 * 
	 * 根据OP_TIME找到在list_dates中pos位置item的具体的pos
	 * 
	 * @param 上个方法得到的pos
	 * @return
	 * @throws JSONException
	 */
	protected int getInt(int postion) throws JSONException {
		// int pos = -1;
		List<Map<String, String>> list_temp = new ArrayList<Map<String, String>>();

		list_temp = JsonUtil.toList(list_dates.getJSONArray(postion));

		return list_temp.size();
	}


	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}
