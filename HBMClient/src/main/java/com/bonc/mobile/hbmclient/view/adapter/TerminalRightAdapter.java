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

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter.ColumnDisplyInfo;
import com.bonc.mobile.hbmclient.view.KpiHasChartRightView;

public class TerminalRightAdapter extends BaseAdapter {

	List<ColumnDisplyInfo[]> mColT;
	Context context;
	int width;

	public TerminalRightAdapter(Context context, String[][] rightStrings,
			String[] mKeysFitler) {
		this.context = context;
		mColT = new ArrayList<ColumnDisplyInfo[]>();
		;
		width = (int) (((Activity) context).getWindowManager()
				.getDefaultDisplay().getWidth() - context.getResources()
				.getDimension(R.dimen.zhl_left_column_width));

		if (rightStrings != null) {
			for (int m = 0; m < rightStrings.length; m++) {
				String[] singleColDatas = rightStrings[m];

				int len = singleColDatas.length;
				ColumnDataFilter.ColumnDisplyInfo[] cDisplyInfo = new ColumnDataFilter.ColumnDisplyInfo[len];

				for (int j = 0; j < len; j++) {

					if (singleColDatas[j] == null
							|| "".equals(singleColDatas[j])) {
						ColumnDisplyInfo columnDisplyInfo = ColumnDataFilter
								.getInstance().new ColumnDisplyInfo();
						columnDisplyInfo.setColor(context.getResources()
								.getColor(R.color.default_normal_color));
						columnDisplyInfo.setUnit("");
						columnDisplyInfo.setValue("");

						cDisplyInfo[j] = columnDisplyInfo;
						continue;
					}

					if (j < mKeysFitler.length && mKeysFitler[j] != null) {
						String rule = "";
						String unit = "";

						if (mKeysFitler[j].startsWith("+")
								|| mKeysFitler[j].startsWith("%")) {
							rule = mKeysFitler[j] + "|2";
							unit = "%";
						} else {
							rule = "[0|2]";
							unit = mKeysFitler[j];
						}

						ColumnDisplyInfo columnDisplyInfo = ColumnDataFilter
								.getInstance().doFilter(rule, unit,
										singleColDatas[j]);

						if (columnDisplyInfo == null) {
							ColumnDisplyInfo temp = ColumnDataFilter
									.getInstance().new ColumnDisplyInfo();
							temp.setColor(context.getResources().getColor(
									R.color.default_normal_color));
							temp.setUnit(unit);
							temp.setValue(singleColDatas[j]);

							cDisplyInfo[j] = temp;
						} else {
							cDisplyInfo[j] = columnDisplyInfo;
						}

					} else {
						ColumnDisplyInfo columnDisplyInfo = ColumnDataFilter
								.getInstance().new ColumnDisplyInfo();
						columnDisplyInfo.setColor(context.getResources()
								.getColor(R.color.default_normal_color));
						columnDisplyInfo.setUnit("");
						columnDisplyInfo.setValue(singleColDatas[j]);

						cDisplyInfo[j] = columnDisplyInfo;
					}

				}
				mColT.add(cDisplyInfo);
			}
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
					false);

			holder.linearlayout.removeAllViews();
			holder.linearlayout.addView(ret, 0);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			ret = (KpiHasChartRightView) holder.linearlayout.getChildAt(0);
			ret.rechargeData(mColT.get(position), null);
		}

		if (position % 2 == 0) {
			ret.setAllBackgroundByID(R.color.zeven_list_color);
		} else {
			ret.setAllBackgroundByID(R.color.zodd_list_color);
		}

		return convertView;
	}

}
