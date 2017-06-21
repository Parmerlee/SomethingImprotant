package com.bonc.mobile.hbmclient.view.adapter;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.bonc.mobile.hbmclient.data.KpiData;

public class KpiHomeLeftExpanableAdapter extends BaseExpandableListAdapter {

	private LayoutInflater mInflater;// View布局填充用
	private boolean hasGroup = true;
	private int groupLayout = R.layout.kpi_com_group;// 组布局
	private int childLayout = R.layout.kpi_main_left;
	private List<String> groupTag = new ArrayList<String>(); // 分组 title组标题.
	private List<String> groupTitle = new ArrayList<String>(); // 分组 key信息
	Map<String, List<Map<String, String>>> subList;

	private String[] colkey; // 要显示的列的key

	KpiData kpiData;

	public KpiData getKpiData() {
		return kpiData;
	}

	public void setKpiData(KpiData kpiData) {
		this.kpiData = kpiData;
		if (kpiData == null) {
			colkey = new String[] {};

			subList = new HashMap<String, List<Map<String, String>>>();
			groupTag = new ArrayList<String>(); // 分组 title组标题.
			groupTitle = new ArrayList<String>();
		} else {
			hasGroup = kpiData.isHasGroup();
			colkey = kpiData.getColkey();
			if (!hasGroup) {
				groupLayout = R.layout.kpi_com_group_no;
			}
			List<Map<String, String>> groupList = kpiData.getGroupList();
			subList = kpiData.getSubList();
			for (int i = 0; i < groupList.size(); i++) {
				groupTag.add(groupList.get(i).get("groupTag"));
				groupTitle.add(groupList.get(i).get("title"));
			}
		}
	}

	Context context;

	public KpiHomeLeftExpanableAdapter(Context context, KpiData kpiData) {
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.kpiData = kpiData;
		hasGroup = kpiData.isHasGroup();
		colkey = kpiData.getColkey();
		if (!hasGroup) {
			groupLayout = R.layout.kpi_com_group_no;
		}
		List<Map<String, String>> groupList = kpiData.getGroupList();
		subList = kpiData.getSubList();
		for (int i = 0; i < groupList.size(); i++) {
			groupTag.add(groupList.get(i).get("groupTag"));
			groupTitle.add(groupList.get(i).get("title"));
		}
	}

	@Override
	public int getGroupCount() {

		return groupTag.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		int n = (subList.get(groupTag.get(groupPosition)) == null ? 0 : subList
				.get(groupTag.get(groupPosition)).size());
		return n;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(groupLayout, null);
			holder = new ViewHolder();

			holder.index = (TextView) convertView
					.findViewById(R.id.group_text1);
			convertView.setTag(holder);
			convertView.setTag(R.id.rl_com_list_left, groupPosition);
			convertView.setTag(R.id.zhibiao_left_1, -1);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (hasGroup) {

			String title = (String) groupTitle.get(groupPosition);

			if (title.length() <= 6) {
				holder.index.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
				holder.index.setText(title);
			} else {
				holder.index
						.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
				holder.index.setText(title.substring(0, 6));
			}

		}

		// Businessfirst groupData =
		// (Businessfirst)dataValues.get(groupPosition).get(0);
		// holder.index.setText(groupData.getGro_desc());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(childLayout, null);
			holder = new ViewHolder();
			holder.index = (TextView) convertView
					.findViewById(R.id.zhibiao_left_1);
			holder.rl_com_list_left = (RelativeLayout) convertView
					.findViewById(R.id.rl_com_list_left);
			// holder.index.setTag(childPosition);
			// holder.rl_com_list_left.setTag(groupPosition);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (childPosition % 2 == 0) {
			holder.rl_com_list_left
					.setBackgroundResource(R.color.zeven_list_color);// list_left_item
		} else {
			holder.rl_com_list_left
					.setBackgroundResource(R.color.zeven_list_color);
		}
		// Businessfirst groupData =
		// (Businessfirst)dataValues.get(groupPosition).get(childPosition);
		final String leftValue = ((subList.get((String) groupTag
				.get(groupPosition))).get(childPosition)).get(colkey[0]);
		holder.index.setText(leftValue);

		convertView.setTag(R.id.rl_com_list_left, groupPosition);
		convertView.setTag(R.id.zhibiao_left_1, childPosition);
		// holder.index.setOnLongClickListener(new OnLongClickListener(){
		//
		// @Override
		// public boolean onLongClick(View v) {
		// //长按处理，显示指标解释
		//
		// Toast.makeText(context, "长按："+leftValue, Toast.LENGTH_LONG).show();
		//
		// // PopupWindow pop = new PopupWindow();
		// // pop.set
		// return true;
		// }});
		// holder.index.setText((String)groupTitle.get(groupPosition));
		// holder.index.setText(groupData.getSub_desc());

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	class ViewHolder {
		TextView index;
		RelativeLayout rl_com_list_left;
	}
}
