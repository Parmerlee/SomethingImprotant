package com.bonc.mobile.hbmclient.view.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;

import com.bonc.mobile.hbmclient.data.ColumnDataFilter;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter.ColumnDisplyInfo;
import com.bonc.mobile.hbmclient.data.KpiInfo;
import com.bonc.mobile.hbmclient.data.MenuColumnInfo;
import com.bonc.mobile.hbmclient.util.TextViewUtils;

/**
 * @author sunwei
 */
public class SimpleKpiTrendAdapter extends SimpleKpiAdapter {
	protected Map<String, List<Double>> trendData;
	protected Map<String, MenuColumnInfo> colInfo;

	public SimpleKpiTrendAdapter(Context c, int resource,
			List<Map<String, String>> data,
			Map<String, List<Double>> trendData,
			Map<String, MenuColumnInfo> colInfo) {
		super(c, resource, data);
		this.trendData = trendData;
		this.colInfo = colInfo;
	}

	protected void setColValue(View parent, Map<String, String> data, int id,
			String col, KpiInfo kpiInfo) {
		MenuColumnInfo mci = colInfo.get(col);
		ColumnDisplyInfo cdi = ColumnDataFilter.getInstance().filter(mci,
				data.get(col), kpiInfo);
		TextViewUtils.setText(parent, id, cdi.getValue() + cdi.getUnit());
	}

}
