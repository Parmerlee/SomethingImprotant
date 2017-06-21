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

public class KpiAreaLeftAdapter extends BaseAdapter {

	private LayoutInflater mInflater;// View布局填充用
	private int childLayout = R.layout.kpi_area_left;
	private List<String> listData;
	private String[] colkey;
	private List<Map<String, String>> colData;

	public KpiAreaLeftAdapter(Context context,
			List<Map<String, String>> colData, String[] colkey) {
		this.mInflater = LayoutInflater.from(context);
		this.colkey = colkey;
		this.colData = colData;
		listData = new ArrayList<String>();
		for (int i = 0; i < colData.size(); i++) {
			listData.add(colData.get(i).get(colkey[0]));
		}
	}

	public KpiAreaLeftAdapter(Context context, String[] mLeftTag) {
		// TODO Auto-generated constructor stub
		this.mInflater = LayoutInflater.from(context);
		listData = new ArrayList<String>();
		for (int i = 0; i < mLeftTag.length; i++) {
			listData.add(mLeftTag[i]);
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
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
		if (position % 2 == 0) {
			holder.rl_com_list_left
					.setBackgroundResource(R.color.zeven_list_color);// list_left_item
		} else {
			holder.rl_com_list_left
					.setBackgroundResource(R.color.zodd_list_color);
		}
		String areaName = listData.get(position);
		holder.index.setText(areaName);

		return convertView;
	}

	class ViewHolder {
		TextView index;
		RelativeLayout rl_com_list_left;
	}

	// 刷新数据
	public void refresh() {

		listData.clear();
		listData = new ArrayList<String>();
		for (int i = 0; i < colData.size(); i++) {
			listData.add(colData.get(i).get(colkey[0]));
		}

		notifyDataSetChanged();

	}

}
