package com.bonc.mobile.hbmclient.view.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author sunwei
 */
public class SimpleKpiAdapter extends BaseAdapter {
	protected Context mContext;
	protected LayoutInflater mInflater;
	protected int resource;
	protected List<Map<String, String>> data;

	public SimpleKpiAdapter(Context c, int resource,
			List<Map<String, String>> data) {
		mContext = c;
		mInflater = LayoutInflater.from(c);
		this.resource = resource;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		if (convertView == null) {
			v = mInflater.inflate(resource, parent, false);
		} else
			v = convertView;
		return v;
	}

}
