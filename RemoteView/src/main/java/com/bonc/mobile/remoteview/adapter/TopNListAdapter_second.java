package com.bonc.mobile.remoteview.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.activity.HMTopnDrillActivity;
import com.bonc.mobile.remoteview.common.MyPro;
import com.bonc.mobile.remoteview.common.Utils;

public class TopNListAdapter_second extends ArrayAdapter<Object> {

	Activity context;

	private int pos;

	private List<Map<String, String>> list_kpi = new ArrayList<Map<String, String>>();

	private JSONArray list_data = new JSONArray();

	float width = 0;

	public TopNListAdapter_second(Activity context, int resource,
			JSONObject object, int pos) {
		super(context, resource);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.pos = pos;
		try {
			int datesize = Integer.valueOf(object.getString("dataSize"));

			String[] key_kpi, key_data;

			key_data = new String[datesize];
			key_kpi = new String[datesize];
			for (int i = 0; i < datesize; i++) {
				key_data[i] = "data" + i;
				key_kpi[i] = "kpiData" + i;
			}

			list_kpi = JsonUtil.toList(JsonUtil.optJSONArray(object,
					key_kpi[pos]));
			list_data = JsonUtil.optJSONArray(object, key_data[pos]);

			list_data = Utils.removeTheEmptyItem_arr(list_data);
			for (int i = 0; i < list_kpi.size(); i++) {
				if (list_kpi.get(i).size() == 0)
					list_kpi.remove(i);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_kpi.size() > list_data.length() ? list_data.length()
				: list_kpi.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.holiday_toplv_item_second_layout, null);
			holder.rank = (TextView) convertView
					.findViewById(R.id.holiday_toplv_item_second_rank);
			holder.name = (TextView) convertView
					.findViewById(R.id.holiday_toplv_item_second_name);
			holder.percent = (TextView) convertView
					.findViewById(R.id.holiday_toplv_item_second_percent11);

			holder.progress = (ProgressBar) convertView
					.findViewById(R.id.holiday_toplv_item_second_view);
			holder.pro = (MyPro) convertView
					.findViewById(R.id.holiday_toplv_item_pro);

			holder.layout = (RelativeLayout) convertView
					.findViewById(R.id.myprogress_ll);
			holder.mark = (TextView) convertView
					.findViewById(R.id.myprogress_tv);
			holder.bg = (TextView) convertView
					.findViewById(R.id.myprogress_view);
			holder.topView = convertView
					.findViewById(R.id.holiday_toplv_item_second_topview);

			holder.progress_str = (TextView) convertView
					.findViewById(R.id.myprogress_str);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == 0) {

			holder.rank.setTextColor(Color.RED);
			holder.pro.setTextBg(Color.BLUE);

			holder.bg.setBackgroundResource(R.drawable.textview_border1);
			holder.mark.setBackgroundResource(R.drawable.layout_border1);
			holder.topView.setBackgroundColor(Color.rgb(164, 00, 00));
		} else if (position == 1) {
			holder.rank.setTextColor(Color.rgb(244, 186, 76));
			holder.bg.setBackgroundResource(R.drawable.textview_border2);
			holder.mark.setBackgroundResource(R.drawable.layout_border1);
			holder.topView.setBackgroundColor(Color.rgb(209, 80, 38));
		} else if (position == 2) {
			holder.rank.setTextColor(Color.rgb(65, 176, 208));
			holder.bg.setBackgroundResource(R.drawable.textview_border3);
			holder.mark.setBackgroundResource(R.drawable.layout_border1);
			holder.topView.setBackgroundColor(Color.rgb(255, 159, 00));
		} else {
			holder.topView.setVisibility(View.GONE);
			holder.rank.setTextColor(Color.rgb(134, 134, 134));

			holder.bg.setBackgroundResource(R.drawable.textview_border4);
			holder.mark.setBackgroundResource(R.drawable.layout_border1);
		}
		holder.rank.setText(1 + position + "");
		try {
			((HMTopnDrillActivity) context).setMyTitle(list_kpi.get(
					list_kpi.size() - 1).get("KPI_NAME"));

			holder.name.setText(JsonUtil
					.toList(list_data.getJSONArray(position)).get(0)
					.get("TOP_NAME"));

			String str = JsonUtil.toList(list_data.getJSONArray(position))
					.get(0).get("CURHOUR_VALUE");

			str = Utils.changeValue(Double.valueOf(str));

			holder.percent
					.setText(str + list_kpi.get(position).get("KPI_UNIT"));

			holder.pro.setText(Math.round(Float.valueOf(str)) + "");

			holder.progress_str.setText(str + "%");

			int w = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			int h = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			holder.layout.measure(w, h);

			final View view_lauout = holder.progress_str;

			ViewTreeObserver vto = view_lauout.getViewTreeObserver();
			vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
				public boolean onPreDraw() {
					width = view_lauout.getMeasuredWidth();
					return true;
				}
			});

			width = holder.progress_str.getMeasuredWidth();
			width = width * Float.valueOf(str) / 18;

			int i = Integer.valueOf(Utils.changeValueWithoutPoint(width));

			holder.bg.setLayoutParams(new RelativeLayout.LayoutParams(
					(int) (i),
					android.widget.RelativeLayout.LayoutParams.MATCH_PARENT));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			holder.percent.setText("-- --");
		}

		return convertView;
	}

	class ViewHolder {
		TextView rank, name, percent;
		ProgressBar progress;
		MyPro pro;
		RelativeLayout layout;
		TextView mark;
		TextView bg;
		View topView;
		TextView tv_percent;

		TextView progress_str;
	}
}
