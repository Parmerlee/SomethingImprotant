package com.bonc.mobile.hbmclient.view.adapter;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter.ColumnDisplyInfo;
import com.bonc.mobile.hbmclient.data.KpiInfo;
import com.bonc.mobile.hbmclient.data.MenuColumnInfo;
import com.bonc.mobile.hbmclient.view.KpiHasChartRightView;

public class KPIOrderRightAdapter extends BaseExpandableListAdapter {

	private List<Map<String, String>> groupInfo; // 分组信息
	private Map<String, List<Map<String, String>>> subInfo; // 每个组的字信息.
	private Context context;
	private String[] colKeyList;
	private Map<String, MenuColumnInfo> colInfoMap;
	private KpiInfo kpiInfo;
	private float width;

	public KPIOrderRightAdapter(Context context,
			List<Map<String, String>> groupInfo,
			Map<String, List<Map<String, String>>> subInfo, KpiInfo kpiInfo,
			Map<String, MenuColumnInfo> colInfoMap, String[] colKeyList) {

		this.context = context;
		this.subInfo = subInfo;
		this.groupInfo = groupInfo;
		this.colKeyList = colKeyList;
		this.colInfoMap = colInfoMap;
		this.kpiInfo = kpiInfo;

		width = (((Activity) context).getWindowManager().getDefaultDisplay()
				.getWidth() - context.getResources().getDimension(
				R.dimen.zhl_left_column_width));
	}

	public KPIOrderRightAdapter(Context context, KpiInfo kpiInfo,
			Map<String, MenuColumnInfo> colInfoMap, String[] colKeyList) {
		this.context = context;
		this.colKeyList = colKeyList;
		this.colInfoMap = colInfoMap;
		this.kpiInfo = kpiInfo;

		width = (((Activity) context).getWindowManager().getDefaultDisplay()
				.getWidth() - context.getResources().getDimension(
				R.dimen.zhl_left_column_width));
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

		Map<String, String> row = subInfo.get(
				groupInfo.get(groupPosition).get("groupId")).get(childPosition);

		int len = colKeyList.length;
		ColumnDisplyInfo[] columnDisplyInfos = new ColumnDisplyInfo[len];

		for (int i = 0; i < colKeyList.length; i++) {

			ColumnDisplyInfo columnDisplyInfo = ColumnDataFilter.getInstance()
					.filter(colInfoMap.get(colKeyList[i]),
							row.get(colKeyList[i]), kpiInfo);
			columnDisplyInfos[i] = columnDisplyInfo;
		}
		int screenWidth = ((Activity) context).getWindowManager()
				.getDefaultDisplay().getWidth();
		KpiHasChartRightView ret = new KpiHasChartRightView(context,
				columnDisplyInfos, screenWidth, false);

		if (childPosition % 2 == 0) {
			ret.setAllBackgroundByID(R.color.zeven_list_color);
		} else {
			ret.setAllBackgroundByID(R.color.zodd_list_color);
		}

		return ret;

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
		return 0;
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

		if (title.length() > 6) {
			holder.index.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
			holder.index.setText(title.substring(6));
		}

		return convertView;

	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
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
