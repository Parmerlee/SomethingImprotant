/**
 * RelationKpiListLeftAdapter
 */
package com.bonc.mobile.hbmclient.view.adapter;

import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.adapter.levelkpi.RelationKpiConfigAndDataAdapter;
import com.bonc.mobile.hbmclient.levelkpi.LevelKpiItemBuilder;
import com.bonc.mobile.hbmclient.levelkpi.LevelKpiItemProduct;
import com.bonc.mobile.hbmclient.levelkpi.LinearDirector;
import com.bonc.mobile.hbmclient.util.ViewUtil;

/**
 * @author liweigao
 *
 */
public class RelationKpiListLeftAdapter extends BaseExpandableListAdapter {
	private RelationKpiConfigAndDataAdapter dataAdapter;
	private String[] colKey;
	private ViewUtil mViewUtil = ViewUtil.getSingleInstance();

	public RelationKpiListLeftAdapter(RelationKpiConfigAndDataAdapter rkcada) {
		this.dataAdapter = rkcada;
		this.colKey = rkcada.getColKey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupCount()
	 */
	@Override
	public int getGroupCount() {
		int size = 0;
		List<String> group = this.dataAdapter.getGroupList();
		if (group != null) {
			size = group.size();
		}
		return size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		List<List<Map<String, String>>> children = this.dataAdapter
				.getKpiList();
		int size = 0;
		if (children != null) {
			size = children.get(groupPosition).size();
		}
		return size;
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
			LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
			convertView = mInflater.inflate(R.layout.layout_group_title,
					parent, false);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.id_text);
		String title = null;
		try {
			title = this.dataAdapter.getGroupList().get(groupPosition);
		} catch (Exception e) {
			title = "--";
		} finally {
			tv.setText(title);
			mViewUtil.setTextStyleFirstLevel(tv);
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
			LevelKpiItemBuilder lkib = new LevelKpiItemBuilder(
					parent.getContext());
			LinearDirector director = new LinearDirector();
			director.buildRelationKpiNameProduct(lkib);
			LevelKpiItemProduct lkip = lkib.getProduct();
			lkip.setConfigAndDataAdapter(dataAdapter);
			convertView = lkip;
			convertView.setTag(lkip);
		}

		LevelKpiItemProduct product = (LevelKpiItemProduct) convertView
				.getTag();
		String name = null;
		try {
			String kpiCode = this.dataAdapter.getKpiList().get(groupPosition)
					.get(childPosition).get("rel_kpi_code");
			if (kpiCode != null) {
				try {
					name = this.dataAdapter.getKpiRuleAndUnit().get(kpiCode)
							.get(colKey[0]);
					product.setOnClickListener(kpiCode);
					product.setOnLongClickListener(kpiCode);
				} catch (Exception e) {
					name = "--";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			product.setText(name);
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
		return true;
	}

}
