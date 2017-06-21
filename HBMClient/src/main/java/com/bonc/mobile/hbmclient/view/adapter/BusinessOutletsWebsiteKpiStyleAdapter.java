/**
 * BusinessOutletsWebsiteKpiStyleAdapter
 */
package com.bonc.mobile.hbmclient.view.adapter;

import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;

/**
 * @author liweigao
 *
 */
public class BusinessOutletsWebsiteKpiStyleAdapter extends BaseAdapter {
	private List<Map<String, String>> data;

	public BusinessOutletsWebsiteKpiStyleAdapter(List<Map<String, String>> data) {
		this.data = data;
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
					R.layout.business_outlets_website_kpistyle_item, parent,
					false);
		}
		TextView kpiNameTV = (TextView) convertView.findViewById(R.id.kpiName);
		String kpiName = null;
		try {
			Map<String, String> item = this.data.get(position);
			kpiName = item.get("KPI_NAME");
		} catch (Exception e) {
			kpiName = "--";
		}

		if (kpiName == null) {
			kpiName = "--";
		}
		kpiNameTV.setText(kpiName);

		return convertView;
	}

}
