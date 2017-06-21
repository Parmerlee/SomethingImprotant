/**
 * BusinessOutletsKpiRightHighAdapter
 */
package com.bonc.mobile.hbmclient.view.adapter;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.util.NumberUtil;

/**
 * @author liweigao
 *
 */
public class BusinessOutletsKpiRightHighAdapter extends BaseAdapter {
	private List<Map<String, String>> data;
	private DecimalFormat decimalFormat;

	public BusinessOutletsKpiRightHighAdapter(List<Map<String, String>> data) {
		this.data = data;
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
					R.layout.business_outlets_kpi_right_container, parent,
					false);
			LinearLayout container = (LinearLayout) convertView
					.findViewById(R.id.root);
			View left = inflater.inflate(
					R.layout.business_outlets_kpi_right_item, container, false);
			left.setId(0);
			container.addView(left);
			View right = inflater.inflate(
					R.layout.business_outlets_kpi_right_item, container, false);
			right.setId(1);
			container.addView(right);
		}
		View left = convertView.findViewById(0);
		TextView column1TV = (TextView) left.findViewById(R.id.column1);
		TextView column3TV = (TextView) left.findViewById(R.id.column3);
		Map<String, String> item = this.data.get(position);
		String publicCurrent = item.get("publicCurrent");
		column1TV.setText(getValue(publicCurrent));
		String publicTotal = item.get("publicTotal");
		column3TV.setText(getValue(publicTotal));

		View right = convertView.findViewById(1);
		TextView column5TV = (TextView) right.findViewById(R.id.column1);
		TextView column7TV = (TextView) right.findViewById(R.id.column3);
		String privateCurrent = item.get("privateCurrent");
		column5TV.setText(getValue(privateCurrent));
		String privateTotal = item.get("privateTotal");
		column7TV.setText(getValue(privateTotal));

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
