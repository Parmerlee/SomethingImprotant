package com.bonc.mobile.hbmclient.view.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter.ColumnDisplyInfo;
import com.bonc.mobile.hbmclient.data.KpiInfo;
import com.bonc.mobile.hbmclient.data.MenuColumnInfo;
import com.bonc.mobile.hbmclient.util.NumberUtil;
import com.bonc.mobile.hbmclient.view.KpiHasChartRightView;

public class PopWindowListAdapter extends BaseAdapter {

	List<ColumnDisplyInfo[]> mColT;
	Context context;
	int width;
	int firstCW;

	public PopWindowListAdapter(Context context,
			List<Map<String, String>> listData, KpiInfo kpiInfo,
			String[] colkey, Map<String, MenuColumnInfo> colInfoMap) {
		this.context = context;
		mColT = new ArrayList<ColumnDisplyInfo[]>();
		;
		this.width = ((Activity) context).getWindowManager()
				.getDefaultDisplay().getWidth();

		int columnCount = colkey.length;
		firstCW = NumberUtil.DpToPx(context, 110);
		for (int i = 0; i < listData.size(); i++) {
			ColumnDisplyInfo[] tempColInfo = new ColumnDisplyInfo[columnCount];
			for (int j = 0; j < columnCount; j++) {
				String sValue = listData.get(i).get(colkey[j]);
				MenuColumnInfo mci = colInfoMap.get(colkey[j]);

				ColumnDisplyInfo cdi = ColumnDataFilter.getInstance().filter(
						mci, sValue, kpiInfo);

				tempColInfo[j] = cdi;

			}
			mColT.add(tempColInfo);
		}

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
		KpiHasChartRightView ret;
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.kpi_listlayout_right, null);
			holder = new ViewHolder();
			holder.linearlayout = (LinearLayout) convertView
					.findViewById(R.id.zhl_right_static);

			ret = new KpiHasChartRightView(context, mColT.get(position), width,
					true, firstCW);

			holder.linearlayout.removeAllViews();
			holder.linearlayout.addView(ret, 0);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

			ret = (KpiHasChartRightView) holder.linearlayout.getChildAt(0);

			ret.rechargeData(mColT.get(position), null);
		}
		// ret.setFirstColum(110);
		if (position % 2 == 0) {
			ret.setAllBackgroundByID(R.color.zeven_list_color);
		} else {
			ret.setAllBackgroundByID(R.color.zodd_list_color);
		}

		return convertView;
	}

}
