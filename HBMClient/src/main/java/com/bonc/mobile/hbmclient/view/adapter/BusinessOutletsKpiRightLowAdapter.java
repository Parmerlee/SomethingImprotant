/**
 * BusinessOutletsKpiRightLowAdapter
 */
package com.bonc.mobile.hbmclient.view.adapter;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.util.NumberUtil;

/**
 * @author liweigao
 *
 */
public class BusinessOutletsKpiRightLowAdapter extends BaseAdapter {
	private List<Map<String, String>> data;
	private DecimalFormat decimalFormat;

	private String keyColumn1;
	private String keyColumn2;
	private String keyColumn3;
	private String keyColumn4;

	public BusinessOutletsKpiRightLowAdapter(List<Map<String, String>> data,
			int type) {
		this.data = data;
		if (type == 1) {
			this.keyColumn1 = "publicCurrent";
			this.keyColumn2 = "publicCurrentRank";
			this.keyColumn3 = "publicTotal";
			this.keyColumn4 = "publicTotalRank";
		} else {
			this.keyColumn1 = "privateCurrent";
			this.keyColumn2 = "privateCurrentRank";
			this.keyColumn3 = "privateTotal";
			this.keyColumn4 = "privateTotalRank";
		}
		this.decimalFormat = new DecimalFormat(",###");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		if (this.data != null) {
			return this.data.size();
		} else {
			return 0;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(
					R.layout.business_outlets_kpi_right_item, parent, false);
		}
		TextView column1TV = (TextView) convertView.findViewById(R.id.column1);
		TextView column3TV = (TextView) convertView.findViewById(R.id.column3);
		Map<String, String> item = this.data.get(position);
		String column1 = item.get(keyColumn1);
		column1TV.setText(getValue(column1));
		String column3 = item.get(keyColumn3);
		column3TV.setText(getValue(column3));

		return convertView;
	}

	private String getValue(String value) {
		String temp = null;
		try {
			temp = this.decimalFormat.format(NumberUtil.changeToDouble(value));
		} catch (Exception e) {
			temp = "--";
		}

		return temp;
	}
}
