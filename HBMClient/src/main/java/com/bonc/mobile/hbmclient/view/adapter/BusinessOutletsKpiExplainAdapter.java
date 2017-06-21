/**
 * BusinessOutletsKpiExplainAdapter
 */
package com.bonc.mobile.hbmclient.view.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;

/**
 * @author liweigao
 *
 */
public class BusinessOutletsKpiExplainAdapter extends BaseExpandableListAdapter {
	private List<Map<String, String>> data;

	public BusinessOutletsKpiExplainAdapter(List<Map<String, String>> data) {
		this.data = data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupCount()
	 */
	@Override
	public int getGroupCount() {
		if (data == null) {
			return 0;
		} else {
			return data.size();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
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
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(
					R.layout.business_outlets_kpi_explain_group, parent, false);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.groupName);
		ImageView iv = (ImageView) convertView.findViewById(R.id.groupImage);
		Map<String, String> item = data.get(groupPosition);
		if (item == null) {
			item = new HashMap<String, String>();
		}
		String groupName = item.get("KPI_NAME");
		if (groupName == null) {
			groupName = "--";
		}
		tv.setText(groupName);
		if (isExpanded) {
			iv.setImageResource(R.mipmap.triangle_upward);
		} else {
			iv.setImageResource(R.mipmap.triangle_downward);
		}

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
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(
					R.layout.business_outlets_kpi_explain_child, parent, false);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.kpiExplain);
		Map<String, String> item = data.get(groupPosition);
		if (item == null) {
			item = new HashMap<String, String>();
		}
		String kpiExplain = item.get("KPI_DESC");
		if (kpiExplain == null) {
			kpiExplain = "--";
		}
		tv.setText(kpiExplain);

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
		return true;
	}

}
