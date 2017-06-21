package com.bonc.mobile.remoteview.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonc.mobile.common.util.DateUtil;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.view.SimpleListView;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.activity.HolidayMaintainActivity;
import com.bonc.mobile.remoteview.common.Utils;

public class TopNListAdapter extends BaseAdapter {

	private List<List<Map<String, String>>> list_kpi = new ArrayList<List<Map<String, String>>>();

	private List<JSONArray> list_data = new ArrayList<JSONArray>();

	private String[] key_kpi, key_data;

	private Context context;

	private int textViewResourceId;

	HolidayMaintainActivity activity;

	private int datesize;

	String[] posarr = new String[] {};

	public TopNListAdapter(Context context, int textViewResourceId,
			JSONObject object) {
		activity = (HolidayMaintainActivity) context;
		this.context = context;
		this.textViewResourceId = textViewResourceId;

		try {
			datesize = Integer.valueOf(object.getString("dataSize"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		posarr = new String[getCount()];

		key_data = new String[datesize];
		key_kpi = new String[datesize];
		for (int i = 0; i < datesize; i++) {
			key_data[i] = "data" + i;
			key_kpi[i] = "kpiData" + i;
		}

		for (int i = 0; i < datesize; i++) {
			List<Map<String, String>> temp_kpi = new ArrayList<Map<String, String>>();
			JSONArray temp_data = new JSONArray();
			temp_kpi = JsonUtil.toList(JsonUtil
					.optJSONArray(object, key_kpi[i]));
			temp_data = JsonUtil.optJSONArray(object, key_data[i]);

			temp_data = Utils.removeTheEmptyItem_arr(temp_data);
			for (int j = 0; j < temp_kpi.size(); j++) {
				if (temp_kpi.get(j).size() == 0)
					temp_kpi.remove(j);
			}

			list_data.add(temp_data);

			list_kpi.add(temp_kpi);
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Integer.valueOf(datesize);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(activity).inflate(
					textViewResourceId, null);

			convertView
					.setLayoutParams(new LinearLayout.LayoutParams(
							activity.width / 2,
							LinearLayout.LayoutParams.WRAP_CONTENT));
			holder.time = (TextView) convertView
					.findViewById(R.id.holiday_toplv_item_layout_tilte1);
			holder.name = (TextView) convertView
					.findViewById(R.id.holiday_toplv_item_layout_tltle2);
			holder.listview = (SimpleListView) convertView
					.findViewById(R.id.holiday_toplv_item_layout_listview);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		try {
			if (list_data.get(position).length() > 0) {
				convertView.setVisibility(View.VISIBLE);
				holder.time
						.setText((DateUtil.formatter(DateUtil
								.getDate(
										JsonUtil.toList(
												list_data.get(position)
														.getJSONArray(0))
												.get(0).get("OP_TIME"),
										"yyyyMMddHH"), "yyyy-MM-dd HH时")));

				holder.name
						.setText(list_kpi.get(position)
								.get(list_kpi.get(position).size() - 1)
								.get("KPI_NAME"));

				posarr[position] = JsonUtil
						.toList(list_data.get(position).getJSONArray(0)).get(0)
						.get("TOP_NAME");

				MySimpleAdapter adapter = new MySimpleAdapter(activity,
						list_kpi.get(position), position);

				holder.listview.setAdapter(adapter);

				convertView.setId(R.id.hoilday_topn_adapter);
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Map<String, String> map = new HashMap<String, String>();
						map.put("title", posarr[position]);
						((HolidayMaintainActivity) activity)
								.doAdapterClickListener(position, v, map);
					}
				});
			} else {
				convertView.setVisibility(View.GONE);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return convertView;
	}

	class MySimpleAdapter extends BaseAdapter {

		List<Map<String, String>> list_kpi_temp;
		int pos = -1;

		public MySimpleAdapter(HolidayMaintainActivity activity,
				List<Map<String, String>> list_kpi, int position) {

			list_kpi_temp = list_kpi;
			pos = position;
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			convertView = LayoutInflater.from(activity).inflate(
					R.layout.holiday_list_layout_item, null);

			TextView num = (TextView) convertView
					.findViewById(R.id.holiday_list_layout_item_tv1);
			TextView name = (TextView) (convertView
					.findViewById(R.id.holiday_list_layout_item_tv2));

			View view = convertView
					.findViewById(R.id.holiday_list_layout_item_view);

			num.setText(String.valueOf(position + 1));

			if (position == 0) {
				view.setBackgroundColor(Color.rgb(164, 0, 0));
				num.setTextColor(Color.rgb(164, 0, 0));
			} else if (position == 1) {
				view.setBackgroundColor(Color.rgb(208, 81, 38));
				num.setTextColor(Color.rgb(208, 81, 38));
			} else if (position == 2) {
				view.setBackgroundColor(Color.rgb(255, 159, 0));
				num.setTextColor(Color.rgb(255, 159, 0));
			} else {
				num.setTextColor(Color.rgb(79, 95, 111));
				view.setVisibility(View.INVISIBLE);
			}

			try {
				name.setText(JsonUtil
						.toList(list_data.get(pos).getJSONArray(position)).get(0)
						.get("TOP_NAME"));
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			TextView value = (TextView) (convertView
					.findViewById(R.id.holiday_list_layout_item_tv3));

			try {
				value.setText(Utils.changeValue(list_data.get(pos)
						.getJSONArray(position).getJSONObject(0)
						.get("CURHOUR_VALUE"))
						+ list_kpi_temp.get(position).get("KPI_UNIT"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			return list_kpi_temp.size() > list_data.get(pos).length() ? list_data
					.get(pos).length() : list_kpi_temp.size();
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

	class ViewHolder {
		private TextView time, name;
		private SimpleListView listview;

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