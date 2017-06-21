package com.bonc.mobile.hbmclient.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;

public class DataPresentationELVadapter extends BaseExpandableListAdapter {
	private List<String> groupData;
	private List<List<String>> childData;
	private LayoutInflater mInflater;

	public DataPresentationELVadapter(Context c, List<String> gd,
			List<List<String>> cd) {
		this.groupData = gd;
		this.childData = cd;
		this.mInflater = LayoutInflater.from(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupCount()
	 */
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return this.groupData.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return this.childData.get(groupPosition).size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroup(int)
	 */
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChild(int, int)
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupId(int)
	 */
	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildId(int, int)
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#hasStableIds()
	 */
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean,
	 * android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = this.mInflater.inflate(
					R.layout.data_presentation_title, parent, false);
		}

		TextView tv = (TextView) convertView
				.findViewById(R.id.id_data_presentation_title);
		String text = this.groupData.get(groupPosition);
		if (text == null) {
			text = "--";
		}
		tv.setText(text);

		return convertView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean,
	 * android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = this.mInflater.inflate(
					R.layout.data_presentation_item, parent, false);
		}
		TextView tv_number = (TextView) convertView
				.findViewById(R.id.id_number);
		TextView tv_content = (TextView) convertView
				.findViewById(R.id.id_content);

		String number = String.valueOf(childPosition + 1);
		tv_number.setText(number);
		String text = this.childData.get(groupPosition).get(childPosition);
		if (text == null) {
			text = "--";
		}
		tv_content.setText(text);

		if (childPosition % 2 == 0) {
			convertView.setBackgroundResource(R.color.zeven_list_color);
		} else {
			convertView.setBackgroundResource(R.color.zodd_list_color);
		}
		return convertView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
