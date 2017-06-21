package com.bonc.mobile.hbmclient.terminal.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter.ColumnDisplyInfo;
import com.bonc.mobile.hbmclient.view.KpiHasChartRightView;

/**
 * 右侧列表的adapter，每次返回一个<a
 * href="#StatisticsRightView">StatisticsRightView</a>，这种view封装和呈现一整行数据
 */
public class StatisticsRightAdapter extends BaseAdapter {
	private String[][] mDatas;
	private Activity mContext;
	private String[] mKeysFitler = null;

	public StatisticsRightAdapter(Activity activity, String[][] datas) {
		mDatas = datas;
		mContext = activity;
	}

	public void setFilter(String[] keys) {
		mKeysFitler = keys;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if (arg0 == mDatas.length)
			return new String[0];
		return mDatas[arg0];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return mDatas[position].hashCode();
	}

	class ViewHolder {

		LinearLayout linearlayout;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		KpiHasChartRightView ret;
		ViewHolder holder = null;

		convertView = LayoutInflater.from(mContext).inflate(
				R.layout.kpi_listlayout_right, null);
		holder = new ViewHolder();
		holder.linearlayout = (LinearLayout) convertView
				.findViewById(R.id.zhl_right_static);

		if (position >= mDatas.length) {
			return new TextView(mContext);
		}

		String[] singleColDatas = mDatas[position];

		int len = singleColDatas.length;
		ColumnDisplyInfo[] cDisplyInfo = new ColumnDisplyInfo[len];

		for (int i = 0; i < len; i++) {

			if (singleColDatas[i] == null || "".equals(singleColDatas[i])) {
				ColumnDisplyInfo columnDisplyInfo = ColumnDataFilter
						.getInstance().new ColumnDisplyInfo();
				columnDisplyInfo.setColor(mContext.getResources().getColor(
						R.color.default_normal_color));
				columnDisplyInfo.setUnit("");
				columnDisplyInfo.setValue("");

				cDisplyInfo[i] = columnDisplyInfo;
				continue;
			}

			if (i < mKeysFitler.length) {
				String rule = "";
				String unit = "";

				if (mKeysFitler[i].startsWith("+")
						|| mKeysFitler[i].startsWith("%")) {
					rule = mKeysFitler[i] + "|2";
					unit = "%";
				} else {
					rule = "[0|2]";
					unit = mKeysFitler[i];
				}

				ColumnDisplyInfo columnDisplyInfo = ColumnDataFilter
						.getInstance().doFilter(rule, unit, singleColDatas[i]);

				if (columnDisplyInfo == null) {
					ColumnDisplyInfo temp = ColumnDataFilter.getInstance().new ColumnDisplyInfo();
					temp.setColor(mContext.getResources().getColor(
							R.color.default_normal_color));
					temp.setUnit(unit);
					temp.setValue(singleColDatas[i]);

					cDisplyInfo[i] = temp;
				} else {
					cDisplyInfo[i] = columnDisplyInfo;
				}

			} else {
				ColumnDisplyInfo columnDisplyInfo = ColumnDataFilter
						.getInstance().new ColumnDisplyInfo();
				columnDisplyInfo.setColor(mContext.getResources().getColor(
						R.color.default_normal_color));
				columnDisplyInfo.setUnit("");
				columnDisplyInfo.setValue(singleColDatas[i]);

				cDisplyInfo[i] = columnDisplyInfo;
			}

		}
		float width = mContext.getWindowManager().getDefaultDisplay()
				.getWidth();
		ret = new KpiHasChartRightView(mContext, cDisplyInfo, width, true);
		holder.linearlayout.removeAllViews();
		holder.linearlayout.addView(ret);
		if (position % 2 == 0) {
			ret.setAllBackgroundByID(R.color.zeven_list_color);
		} else {
			ret.setAllBackgroundByID(R.color.zodd_list_color);
		}

		return convertView;

		/*
		 * if(convertView==null||convertView instanceof TextView){ ret=new
		 * StatisticsRightView(mContext, mDatas[position]);
		 * ret.setFilter(mKeysFitler); ret.rechargeData(mDatas[position]);
		 * mItemView[position]=ret; }else{ ret=(StatisticsRightView)convertView;
		 * ret.rechargeData(mDatas[position]); } if(isFill){
		 * ret.setFillInExactly(); } if(position%2==0){
		 * ret.setAllBackgroundByID(R.drawable.odd_list_item); }else{
		 * ret.setAllBackgroundByID(R.drawable.even_list_item); } return ret;
		 */
	}

}
