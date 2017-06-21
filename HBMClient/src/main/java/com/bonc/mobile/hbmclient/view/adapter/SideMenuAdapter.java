/**
 * SideMenuAdapter
 */
package com.bonc.mobile.hbmclient.view.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.enum_type.MenuEnum;

/**
 * @author liweigao 侧边栏listview
 *
 */
public class SideMenuAdapter extends BaseExpandableListAdapter {
	private List<Map<String, String>> menuGroupInfo;
	private List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
	private BusinessDao dao = new BusinessDao();
	private Context context;
	private LayoutInflater mInflater;

	public SideMenuAdapter(Context context, String menuCode) {
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.menuGroupInfo = dao.getMenuGroupInfo();
		for (int i = 0; i < menuGroupInfo.size(); i++) {
			String kind = menuGroupInfo.get(i).get("GROUP_KIND");
			List<Map<String, String>> itemData = dao.getSideMenuData(kind,
					menuCode);
			this.childData.add(itemData);
		}
	}

	public List<List<Map<String, String>>> getChildData() {
		return this.childData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupCount()
	 */
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return this.menuGroupInfo.size();
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
			convertView = mInflater.inflate(R.layout.layout_sidemenu_group,
					parent, false);
		}
		TextView tv = (TextView) convertView
				.findViewById(R.id.id_sidemenu_group);
		String title = this.menuGroupInfo.get(groupPosition).get("GROUP_NAME");
		tv.setText(title);

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
			convertView = mInflater.inflate(R.layout.layout_sidemenu_child,
					parent, false);
		}
		TextView tv = (TextView) convertView
				.findViewById(R.id.id_sidemenu_child);
		Map<String, String> itemData = this.childData.get(groupPosition).get(
				childPosition);
		int menuCode = 0;
		try {
			menuCode = Integer.parseInt(itemData.get("menuCode"));
		} catch (Exception e) {
			e.printStackTrace();
			menuCode = 0;
		}
		String menuName = itemData.get("menuName");
		int imageid = MenuEnum.getIdIconSmall(menuCode);
		tv.setText(menuName);
		tv.setCompoundDrawablesWithIntrinsicBounds(imageid, 0, 0, 0);

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
