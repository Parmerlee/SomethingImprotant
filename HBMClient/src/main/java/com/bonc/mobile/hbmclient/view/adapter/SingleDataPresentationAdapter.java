/**
 * 
 */
package com.bonc.mobile.hbmclient.view.adapter;

import java.util.List;

import android.content.Context;
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
public class SingleDataPresentationAdapter extends BaseAdapter {
	private List<String> childData;
	private LayoutInflater mInflater;

	public SingleDataPresentationAdapter(List<String> data, Context c) {
		this.childData = data;
		this.mInflater = LayoutInflater.from(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.childData.size();
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
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.data_presentation_item,
					parent, false);
		}
		TextView tv_number = (TextView) convertView
				.findViewById(R.id.id_number);
		TextView tv_content = (TextView) convertView
				.findViewById(R.id.id_content);

		String number = String.valueOf(position + 1);
		tv_number.setText(number);
		String text = this.childData.get(position);
		if (text == null) {
			text = "--";
		}
		tv_content.setText(text);
		return convertView;
	}

}
