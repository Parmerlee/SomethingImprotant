package com.bonc.mobile.portal.search;

import java.util.List;

import com.bonc.anhuimobile.ac.R;
import com.bonc.mobile.common.util.UIUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MyAdapter extends android.widget.BaseAdapter {
	private LayoutInflater mInflater;
	private int selectItem = -1;
	private List<String> mData;

	public MyAdapter(Activity activity, ListView listview, List<String> mData) {
		this.mInflater = LayoutInflater.from(activity);
		this.mData = mData;
		listview.setVisibility(View.VISIBLE);

		listview.setLayoutParams(new LinearLayout.LayoutParams(UIUtil
				.getDisplayWidth((Activity) activity) * 1 / 4,
				LinearLayout.LayoutParams.MATCH_PARENT));
	}

	public MyAdapter(Context context, List<String> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
		// listview.setVisibility(View.VISIBLE);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mData.get(arg0);
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.flowlayout_item, null);
			holder.titleText = (TextView) convertView
					.findViewById(R.id.flowlayout_tv);
			// holder.infoText = (TextView) convertView
			// .findViewById(R.id.infoleftlist);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.titleText.setText((String) mData.get(position).substring(0));
		holder.titleText.setGravity(Gravity.CENTER_HORIZONTAL);
		// holder.infoText.setText((String) mData.get(position).get("info"));

		if (position == selectItem) {
			convertView.setBackgroundColor(Color.RED);
		} else {
			convertView.setBackgroundColor(Color.TRANSPARENT);
		}

		// convertView.getBackground().setAlpha(80);

		return convertView;
	}

	public void setSelectItem(int selectItem) {
		this.selectItem = selectItem;
	}

	public final class ViewHolder {
		public TextView titleText;
		public TextView infoText;
	}
}