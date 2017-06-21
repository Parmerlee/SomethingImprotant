package com.bonc.mobile.hbmclient.view.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;

public class KpiPeriodLeftAdapter extends BaseAdapter {

	private LayoutInflater mInflater;// View布局填充用
	private int childLayout = R.layout.kpi_graph_left;
	private List<String> listData;

	public KpiPeriodLeftAdapter(Context context,
			List<Map<String, String>> colData, String[] colkey) {
		this.mInflater = LayoutInflater.from(context);
		listData = new ArrayList<String>();
		for (int i = 0; i < colData.size(); i++) {
			listData.add(colData.get(i).get(colkey[0]));
		}
	}

	@Override
	public int getCount() {
		return listData == null ? 0 : listData.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// 是否画背景色
	private boolean isDrawBackground = true;

	@Override
	public View getView(int position_l, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(childLayout, null);
			holder = new ViewHolder();
			holder.index = (TextView) convertView
					.findViewById(R.id.zhibiao_left_1);
			holder.rl_com_list_left = (RelativeLayout) convertView
					.findViewById(R.id.rl_com_list_left);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		int position = this.getCount() - 1 - position_l;
		if (position_l % 2 == 0) {
			holder.rl_com_list_left
					.setBackgroundResource(R.color.zeven_list_color);// list_left_item
		} else {
			holder.rl_com_list_left
					.setBackgroundResource(R.color.zodd_list_color);
		}
		if (isDrawBackground) {
			if (select == position_l) {
				holder.rl_com_list_left
						.setBackgroundResource(R.color.selected_color);
			} /*
			 * else if (select != position_l) {
			 * convertView.setBackgroundColor(0x00000000); }
			 */
		}

		String areaName = listData.get(position);
		holder.index.setText(areaName);
		// holder.index.setText(listData.get(position).get("areaName").toString());

		return convertView;
	}

	public void setDrawBackground(boolean isDrawBackground) {
		this.isDrawBackground = isDrawBackground;
	}

	class ViewHolder {
		TextView index;
		RelativeLayout rl_com_list_left;
	}

	int select = 0;

	public void changeColor(int pos) {
		select = pos;
		notifyDataSetChanged();
	}
}
