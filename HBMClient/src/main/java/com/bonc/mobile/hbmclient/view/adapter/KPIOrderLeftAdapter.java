package com.bonc.mobile.hbmclient.view.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;

public class KPIOrderLeftAdapter extends BaseExpandableListAdapter {

	private List<Map<String, String>> groupInfo; // 分组信息
	private Map<String, List<Map<String, String>>> subInfo; // 每个组的字信息.
	private Context context;

	public KPIOrderLeftAdapter(Context context,
			List<Map<String, String>> groupInfo,
			Map<String, List<Map<String, String>>> subInfo) {
		this.groupInfo = groupInfo;
		this.subInfo = subInfo;
		this.context = context;
	}

	public KPIOrderLeftAdapter(Context context) {
		this.context = context;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.kpi_main_left, null);
			holder = new ViewHolder();
			holder.index = (TextView) convertView
					.findViewById(R.id.zhibiao_left_1);
			holder.rl_com_list_left = (RelativeLayout) convertView
					.findViewById(R.id.rl_com_list_left);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (childPosition % 2 == 0) {
			holder.rl_com_list_left
					.setBackgroundResource(R.color.zeven_list_color);// list_left_item
		} else {
			holder.rl_com_list_left
					.setBackgroundResource(R.color.zodd_list_color);
		}

		final String leftValue = ((subInfo.get(groupInfo.get(groupPosition)
				.get("groupId"))).get(childPosition)).get(context
				.getString(R.string.mon_area_left_key));

		holder.index.setText(leftValue);

		convertView.setTag(R.id.rl_com_list_left, groupPosition);
		convertView.setTag(R.id.zhibiao_left_1, childPosition);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {

		if (groupInfo == null) {
			return 0;
		}

		if (subInfo.get(groupInfo.get(groupPosition).get("groupId")) == null) {
			return 0;
		}
		int childCount = subInfo.get(
				groupInfo.get(groupPosition).get("groupId")).size();
		return childCount;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount() {

		if (groupInfo == null) {
			return 0;
		}
		return groupInfo.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.kpi_com_group, null);
			holder = new ViewHolder();
			holder.index = (TextView) convertView
					.findViewById(R.id.group_text1);
			convertView.setTag(holder);
			convertView.setTag(R.id.rl_com_list_left, groupPosition);
			convertView.setTag(R.id.zhibiao_left_1, -1);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String title = (String) groupInfo.get(groupPosition).get("groupName");

		if (title.length() <= 6) {
			holder.index.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			holder.index.setText(title);
		} else {
			holder.index.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
			holder.index.setText(title.substring(0, 6));
		}

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	class ViewHolder {
		TextView index;
		RelativeLayout rl_com_list_left;
	}

	public List<Map<String, String>> getGroupInfo() {
		return groupInfo;
	}

	public void setGroupInfo(List<Map<String, String>> groupInfo) {
		this.groupInfo = groupInfo;
	}

	public Map<String, List<Map<String, String>>> getSubInfo() {
		return subInfo;
	}

	public void setSubInfo(Map<String, List<Map<String, String>>> subInfo) {
		this.subInfo = subInfo;
	}

}
