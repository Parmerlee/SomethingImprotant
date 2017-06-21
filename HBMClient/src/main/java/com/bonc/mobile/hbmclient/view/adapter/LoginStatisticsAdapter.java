package com.bonc.mobile.hbmclient.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.util.StringUtil;
import com.bonc.mobile.hbmclient.view.AutoFitView;

public class LoginStatisticsAdapter extends BaseAdapter {

	private List<String[]> mColT;
	private Context context;

	private int width;

	public LoginStatisticsAdapter(Context context, List<String[]> listData) {
		this.context = context;
		mColT = new ArrayList<String[]>();
		mColT = listData;
		width = (int) (((Activity) context).getWindowManager()
				.getDefaultDisplay().getWidth()) - 6;
	}

	class ViewHolder {

		LinearLayout linearlayout;
	}

	@Override
	public int getCount() {
		return mColT == null ? 0 : mColT.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AutoFitView ret;
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.kpi_listlayout_right, null);
			holder = new ViewHolder();
			holder.linearlayout = (LinearLayout) convertView
					.findViewById(R.id.zhl_right_static);

			ret = new AutoFitView(context, R.layout.autview_item,
					mColT.get(position).length, width, false);

			holder.linearlayout.removeAllViews();
			holder.linearlayout.addView(ret, 0);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

			ret = (AutoFitView) holder.linearlayout.getChildAt(0);

		}
		String[] strP = mColT.get(position);
		for (int i = 0; i < strP.length; i++) {
			TextView tn = (TextView) ret.getChildAt(i).findViewById(
					R.id.autview_text);
			tn.setText(StringUtil.nullToString2(strP[i]));
		}
		if (position % 2 == 0) {
			ret.setAllBackgroundByID(R.color.zeven_list_color);
		} else {
			ret.setAllBackgroundByID(R.color.zodd_list_color);
		}

		return convertView;
	}

}
