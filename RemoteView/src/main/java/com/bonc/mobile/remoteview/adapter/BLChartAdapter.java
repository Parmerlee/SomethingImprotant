package com.bonc.mobile.remoteview.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.chart.TwoBarChartView;
import com.bonc.mobile.common.chart.SimpleChartData;
import com.bonc.mobile.common.util.DateUtil;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.NumberUtil;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.activity.HMFlowDrillChartActivity;
import com.bonc.mobile.remoteview.activity.HolidayMaintainActivity;
import com.bonc.mobile.remoteview.common.Utils;

public class BLChartAdapter extends HMBaseAdapter {
	private final String TAG = "BLChartAdapter";
	int pos = -1;

	public BLChartAdapter(Context context, int textViewResourceId,
			LayoutParams params, JSONObject object) {
		super(context, textViewResourceId, params, object);
		posarr = new int[getCount()];
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(activity).inflate(
					textViewResourceId, null);
			holder = new ViewHolder();

			holder.chartview = (TwoBarChartView) convertView
					.findViewById(R.id.holiday_simple_item_barLineChartView);
			holder.tv_time = (TextView) convertView
					.findViewById(R.id.holiday_simple_item_time_value);
			holder.tv_sum_key = (TextView) convertView
					.findViewById(R.id.holiday_simple_item_sum_key);
			holder.tv_sum_value = (TextView) convertView
					.findViewById(R.id.holiday_simple_item_sum_value);
			holder.tv_mark1 = (TextView) convertView
					.findViewById(R.id.holiday_simple_item_mark1);
			holder.tv_mark2 = (TextView) convertView
					.findViewById(R.id.holiday_simple_item_mark2);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			List<Map<String, String>> list_temp = new ArrayList<Map<String, String>>();
			list_temp = JsonUtil.toList(list_dates.getJSONArray(position));
			pos = getposition(list_temp.get(list_temp.size() - 1).get(
					"KPI_CODE"));

			if (pos != -1) {

				posarr[position] = pos;
				convertView.setVisibility(View.VISIBLE);
				holder.tv_sum_value.setText(NumberUtil.format(Double
						.valueOf(list_temp.get(list_temp.size() - 1).get(
								"CURHOUR_VALUE")))
						+ list_kpi.get(pos).get("KPI_UNIT"));


				SimpleChartData data = SimpleChartData.build(
						getdouble(list_temp, "CURHOUR_VALUE"),
						getdouble(list_temp, "PY_CURHOUR_VALUE"), null);

				data.cats = gettimeList(list_temp, "OP_TIME");

				holder.chartview.setData(data);

				holder.tv_sum_key.setText(list_kpi.get(pos).get("KPI_NAME"));

				holder.tv_time
						.setText((DateUtil.formatter(
								DateUtil.getDate(time, "yyyyMMddHH"),
								"yyyy-MM-dd HH时")));

			} else {
				convertView.setVisibility(View.GONE);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		convertView.setId(R.id.hoilday_blchart_adapter);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Map<String, String> map = new HashMap<String, String>();
				map.put("kpiCode",
						list_kpi.get(posarr[position]).get("KPI_CODE"));

				map.put("lowerKpi",
						list_kpi.get(posarr[position]).get("LOWER_KPI"));

				map.put("title", list_kpi.get(posarr[position]).get("KPI_NAME"));


				if (activity instanceof HolidayMaintainActivity) {

					((HolidayMaintainActivity) activity)
							.doAdapterClickListener(position, v, map);
				} else if (activity instanceof HMFlowDrillChartActivity) {

					((HMFlowDrillChartActivity) activity)
							.doAdapterClickListener(position, v, map);
				}
			}
		});

		return convertView;
	}

	String[] gettimeList(List<Map<String, String>> list, String key) {
		String[] key_list = new String[list.size()];

		for (int i = 0; i < list.size(); i++) {
			key_list[i] = String.valueOf(list.get(i).get(key).substring(8));
		}
		return key_list;

	}

	double[] getdouble(List<Map<String, String>> list, String key) {
		double[] key_list = new double[list.size()];

		for (int i = 0; i < list.size(); i++) {
			key_list[i] = NumberUtil.changeToDouble(list.get(i).get(key));
		}
		return key_list;

	}

}

class ViewHolder {
	TwoBarChartView chartview;
	TextView tv_time, tv_sum_key, tv_sum_value, tv_mark1, tv_mark2;
}