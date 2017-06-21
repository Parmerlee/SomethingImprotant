package com.bonc.mobile.remoteview.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bonc.mobile.common.util.DateUtil;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.NumberUtil;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.activity.HolidayMaintainActivity;
import com.bonc.mobile.remoteview.common.Utils;

public class KpiAdapter extends HMBaseAdapter {

	int pos = -1;

	public KpiAdapter(Context context, int textViewResourceId,
			LayoutParams params, JSONObject object) {

		super(context, textViewResourceId, params, object);
		posarr = new int[getCount()];
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(activity).inflate(
					textViewResourceId, null);

			holder.title = (TextView) convertView
					.findViewById(R.id.holiday_kpilv_item_title);
			holder.time = (TextView) convertView
					.findViewById(R.id.holiday_kpilv_item_time);
			holder.value = (TextView) convertView
					.findViewById(R.id.holiday_kpilv_item_value);
			holder.num = (TextView) convertView
					.findViewById(R.id.holiday_kpilv_item_num);
			holder.percent = (TextView) convertView
					.findViewById(R.id.holiday_kpilv_item_percent);
			holder.num_unit = (TextView) convertView
					.findViewById(R.id.holiday_kpilv_item_num_unit);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		convertView.setLayoutParams(new LinearLayout.LayoutParams(
				Utils.width / 3, Utils.width / 4));


		try {
			List<Map<String, String>> list_temp = new ArrayList<Map<String, String>>();
			list_temp = JsonUtil.toList(list_dates.getJSONArray(position));

			pos = getposition(list_temp.get(list_temp.size() - 1).get(
					"KPI_CODE"));

			if (pos != -1) {
				convertView.setVisibility(View.VISIBLE);
				posarr[position] = pos;
				holder.time.setText((DateUtil.formatter(DateUtil.getDate(
						list_temp.get(list_temp.size() - 1).get("OP_TIME"),
						"yyyyMMddHH"), "yyyy-MM-dd")));

				holder.time.setVisibility(View.GONE);

				holder.num.setText(Html.fromHtml(NumberUtil.format(Double
						.valueOf(list_temp.get(list_temp.size() - 1).get(
								"CURHOUR_VALUE")))
						+ list_kpi.get(pos).get("KPI_UNIT")));

				holder.value.setText("当前值");
				holder.value.setVisibility(View.GONE);
				String percent = list_temp.get(list_temp.size() - 1).get(
						"CD_COL");

				Drawable right = null;
				Bitmap bitmap = null;
				if (percent.compareTo("0") > 0) {

					bitmap = BitmapFactory
							.decodeResource(activity.getResources(),
									R.mipmap.triangle_upward);
					right = activity.getResources().getDrawable(
							R.mipmap.triangle_upward);
				} else {
					
					bitmap = BitmapFactory
							.decodeResource(activity.getResources(),
									R.mipmap.triangle_downward);
					
					right = activity.getResources().getDrawable(
							R.mipmap.triangle_downward);
				}


				right.setBounds(0, -5, bitmap.getWidth(),
						bitmap.getHeight());
				holder.percent.setCompoundDrawables(null, null, right, null);
				holder.percent.setText("环比：" + percent + "%");


				holder.title.setText(list_kpi.get(pos).get("KPI_NAME"));

			} else {
				convertView.setVisibility(View.GONE);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!clickable)
			return convertView;

		convertView.setId(R.id.hoilday_kpi_adapter);

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Map<String, String> map = new HashMap<String, String>();

				List<Map<String, String>> list_temp = new ArrayList<Map<String, String>>();
				try {
					list_temp = JsonUtil.toList(list_dates
							.getJSONArray(position));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				map.put("kpiCode",
						list_temp.get(list_temp.size() - 1).get("KPI_CODE"));

				map.put("title", list_kpi.get(posarr[position]).get("KPI_NAME"));

				((HolidayMaintainActivity) activity).doAdapterClickListener(
						position, v, map);
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView title, time, value, num, percent, num_unit;
	}

	boolean clickable;

	public void setClick(boolean b) {
		// TODO Auto-generated method stub
		clickable = b;
	}

}
