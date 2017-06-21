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
import com.bonc.mobile.hbmclient.activity.KPIPeriodActivity;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter.ColumnDisplyInfo;
import com.bonc.mobile.hbmclient.data.KpiInfo;
import com.bonc.mobile.hbmclient.data.MenuColumnInfo;
import com.bonc.mobile.hbmclient.view.KpiHasChartRightView;

public class KpiPeriodRightAdapter extends BaseAdapter {

	/*
	 * private LayoutInflater mInflater;//View布局填充用 private int childLayout =
	 * R.layout.com_list_right;//子list布局 private List<ThirdTrend> listData;
	 */
	List<ColumnDisplyInfo[]> mColT;
	Context context;
	List<Map<String, String>> listData;
	String[] colkey;
	int width;

	public KpiPeriodRightAdapter(Context context,
			List<Map<String, String>> listData, KpiInfo kpiInfo,
			String[] colkey, Map<String, MenuColumnInfo> colInfoMap) {
		this.context = context;
		this.listData = listData;
		this.colkey = colkey;
		mColT = new ArrayList<ColumnDisplyInfo[]>();
		;
		width = (int) (((Activity) context).getWindowManager()
				.getDefaultDisplay().getWidth() - context.getResources()
				.getDimension(R.dimen.zhl_left_column_width));
		int columnCount = colkey.length - 1;

		for (int i = 0; i < listData.size(); i++) {
			ColumnDisplyInfo[] tempColInfo = new ColumnDisplyInfo[columnCount];
			for (int j = 0; j < columnCount; j++) {
				String sValue = listData.get(i).get(colkey[j + 1]);
				MenuColumnInfo mci = colInfoMap.get(colkey[j + 1]);

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
	public View getView(int position_r, View convertView, ViewGroup parent) {
		KpiHasChartRightView ret;
		ViewHolder holder = null;
		final int position = this.getCount() - 1 - position_r;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.kpi_listlayout_right, null);
			holder = new ViewHolder();
			holder.linearlayout = (LinearLayout) convertView
					.findViewById(R.id.zhl_right_static);

			ret = new KpiHasChartRightView(context, mColT.get(position), width,
					false);

			holder.linearlayout.removeAllViews();
			holder.linearlayout.addView(ret, 0);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();

			ret = (KpiHasChartRightView) holder.linearlayout.getChildAt(0);

			ret.rechargeData(mColT.get(position), null);
		}

		if (position_r % 2 == 0) {
			ret.setAllBackgroundByID(R.color.zeven_list_color);
		} else {
			ret.setAllBackgroundByID(R.color.zodd_list_color);
		}
		if (isDrawBackground) {
			if (select == position_r) {
				// 此处只能用setAllBackgroundByID方法，用setAllBackgroundColor会颜色混乱，除非上面改为ret.setAllBackgroundByID(R.drawable.odd_list_item);
				// setAllBackgroundColor和setAllBackgroundByID不能混用
				ret.setAllBackgroundByID(R.color.selected_color);
			}
			/*
			 * else if (select != position_r) {
			 * ret.setAllBackgroundColor(0x00000000); }
			 */
		}

		if (convertView != null) {
			convertView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (context instanceof KPIPeriodActivity) {
						KPIPeriodActivity kpiPeriodActivity = (KPIPeriodActivity) context;
						kpiPeriodActivity.onViewItemClick(position);
					}
				}
			});
		}

		return convertView;
	}

	public void setDrawBackground(boolean isDrawBackground) {
		this.isDrawBackground = isDrawBackground;
	}

	// 是否画背景色
	private boolean isDrawBackground = true;
	int select = 0;

	public void changeColor(int pos) {
		select = pos;
		notifyDataSetChanged();
	}

}
