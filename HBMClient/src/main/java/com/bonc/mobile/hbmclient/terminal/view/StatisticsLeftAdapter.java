package com.bonc.mobile.hbmclient.terminal.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;

/** 适用于统计表格左侧单独列的adapter。注意getID返回的是hashcode */
public class StatisticsLeftAdapter extends BaseAdapter {
	StatisticsLeftItemView[] mItems;
	TextView mPillarView;
	Activity mContext;
	String[] mDatas;

	public StatisticsLeftAdapter(Activity activity, String[] datas) {
		mItems = new StatisticsLeftItemView[datas.length];
		mContext = activity;
		mDatas = datas;
		mPillarView = new TextView(activity);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.length + 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position == mDatas.length)
			return "";
		return mDatas[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return mDatas[position].hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.kpi_area_left, null);
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
					.setBackgroundResource(R.mipmap.odd_list_item);// list_left_item
		} else {
			holder.rl_com_list_left
					.setBackgroundResource(R.mipmap.even_list_item);
		}
		String text = (String) getItem(position);
		holder.index.setText(text);

		return convertView;

	}

	class ViewHolder {
		TextView index;
		RelativeLayout rl_com_list_left;
	}
}
