/**
 * 
 */
package com.bonc.mobile.hbmclient.view.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import common.share.lwg.util.mediator.proxy_impl.port.GetNewInfo;

/**
 * @author liweigao
 *
 */
public class UnLoadDataSecondAdapter extends BaseAdapter {
	private List<Map<String, String>> data;
	private LayoutInflater mInflater;
	private GetNewInfo newInfo = GetNewInfo.getSingleInstance();

	public UnLoadDataSecondAdapter(Context c, List<Map<String, String>> data) {
		this.data = data;
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
		return this.data.size();
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
			convertView = mInflater.inflate(R.layout.unload_data_item, parent,
					false);
		}

		TextView tv = (TextView) convertView.findViewById(R.id.id_text);
		tv.setText(data.get(position).get("menuName"));
		String menuCode = data.get(position).get("menuCode");
		if (this.newInfo.isNew(menuCode)) {
			convertView.findViewById(R.id.ic_new).setVisibility(View.VISIBLE);
		} else {

		}
		/*
		 * String menuChange = data.get(position).get("menuChange");
		 * if("1".equals(menuChange)) {
		 * convertView.findViewById(R.id.ic_new).setVisibility(View.VISIBLE); }
		 * else {
		 * 
		 * }
		 */
		return convertView;
	}

}
