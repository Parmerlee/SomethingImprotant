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

public class DialAdapter extends HMBaseAdapter {

	public DialAdapter(Context context, int textViewResourceId,
			LayoutParams params, JSONObject object) {
		super(context, textViewResourceId, params, object);

		posarr = new int[getCount()];
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(activity).inflate(
					R.layout.holiday_diallv_item, null);

			holder.title = (TextView) convertView
					.findViewById(R.id.holiday_diallv_item_title);
			holder.view = (DialChartView) convertView
					.findViewById(R.id.holiday_diallv_item_dail);

			holder.time = (TextView) convertView
					.findViewById(R.id.holiday_diallv_item_time);
			holder.percent = (TextView) convertView
					.findViewById(R.id.holiday_diallv_item_percent);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		params = new LinearLayout.LayoutParams(
				((HolidayMaintainActivity) activity).width / 3,
				(int) (((HolidayMaintainActivity) activity).width / 3.2)
						+ ((HolidayMaintainActivity) activity).width / 11);

		convertView.setLayoutParams(params);

		holder.view.setLayoutParams(new LinearLayout.LayoutParams(
				((HolidayMaintainActivity) activity).width / 3,
				(int) (((HolidayMaintainActivity) activity).width / 3.2)));

		int pos = -1;
		try {

			List<Map<String, String>> list_temp = new ArrayList<Map<String, String>>();
			list_temp = JsonUtil.toList(list_dates.getJSONArray(position));

			pos = getposition(list_temp.get(list_temp.size() - 1).get(
					"KPI_CODE"));

			if (pos != -1) {
				convertView.setVisibility(View.VISIBLE);

				holder.view.setData(
						Arrays.asList(list_kpi.get(pos).get("KPI_SCALE")
								.split("\\|")),
						Arrays.asList(list_kpi.get(pos).get("KPI_COLOUR")
								.split("\\|")),
						Double.valueOf(list_temp.get(list_temp.size() - 1).get(
								"CURHOUR_VALUE")), ""

				);

				holder.time.setText(DateUtil.formatter(DateUtil.getDate(
						list_temp.get(list_temp.size() - 1).get("OP_TIME"),
						"yyyyMMddHH"), "yyyy-MM-dd HH时"));

				holder.title.setText(list_kpi.get(pos).get("KPI_NAME"));

				holder.percent.setText(Double.valueOf(
						list_temp.get(list_temp.size() - 1)
								.get("CURHOUR_VALUE")).toString());

			} else {
				convertView.setVisibility(View.GONE);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		convertView.setId(R.id.hoilday_dash_layout);

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((HolidayMaintainActivity) activity).doAdapterClickListener(
						position, v, new Object());
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView title, time, percent;
		DialChartView view;
		LinearLayout ll;
	}
}
