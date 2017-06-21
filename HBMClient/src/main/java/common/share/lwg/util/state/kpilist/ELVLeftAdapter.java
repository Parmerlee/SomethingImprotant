/**
 * ELVLeftAdapter
 */
package common.share.lwg.util.state.kpilist;

import java.util.List;
import java.util.Map;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.util.ViewUtil;
import common.share.lwg.util.builder.mainkpi.LKpiRowBuilder;
import common.share.lwg.util.builder.mainkpi.LinearDirector;
import common.share.lwg.util.mediator.proxy_impl.mainkpi.DataColleague;

/**
 * @author liweigao
 * 
 */
public class ELVLeftAdapter extends BaseExpandableListAdapter {
	private List<Map<String, String>> columnInfoLeft;
	private List<Map<String, Object>> composite;
	private Map<String, Map<String, String>> kpiConfigInfo;
	private LinearDirector linearDirector;
	private DataColleague dataColleague;
	private ViewUtil mViewUtil = ViewUtil.getSingleInstance();

	public ELVLeftAdapter(DataColleague dataColleague) {
		this.dataColleague = dataColleague;
		this.columnInfoLeft = this.dataColleague.getColumnInfoLeft();
		this.composite = this.dataColleague.getCompositeGroup();
		this.kpiConfigInfo = this.dataColleague.getKpiConfigInfo();
		this.linearDirector = new LinearDirector();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupCount()
	 */
	@Override
	public int getGroupCount() {
		if (composite == null) {
			return 0;
		} else {
			return composite.size();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		Map<String, Object> item = this.composite.get(groupPosition);
		List<String> kpiCodes = (List<String>) item.get("KPICODELIST");
		if (kpiCodes == null) {
			return 0;
		} else {
			return kpiCodes.size();
		}
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
		RelativeLayout rl = (RelativeLayout) convertView.findViewById(R.id.titleContainer);
		rl.setBackgroundColor(parent.getContext().getResources().getColor(android.R.color.darker_gray));
		TextView tv = (TextView) convertView.findViewById(R.id.id_text);
		tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		String title = null;
		try {
			title = (String) this.composite.get(groupPosition)
					.get("GROUP_NAME");
			if (title != null) {
				if (title.length() <= 7) {
					
				} else {
					tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
					title = title.substring(0, 7);
				}
			} else {
				title = "--";
			}
		} catch (Exception e) {
			title = "--";
		} finally {
			tv.setText(title);
//			mViewUtil.setTextStyleFirstLevel(tv);
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
			LKpiRowBuilder builder = new LKpiRowBuilder(parent.getContext());
			this.linearDirector.buildKpiRow(builder, this.columnInfoLeft);
			convertView = builder;
			convertView.setTag(builder);
		}
		LKpiRowBuilder builder = (LKpiRowBuilder) convertView.getTag();
		Map<String, Object> item = this.composite.get(groupPosition);
		List<String> kpiCodes = (List<String>) item.get("KPICODELIST");
		String kpiCode = kpiCodes.get(childPosition);

		builder.setColumnType2Args(kpiCode, this.dataColleague);
		Map<String, String> kpiConfig = this.kpiConfigInfo.get(kpiCode);
		builder.buildAppearance(kpiConfig);

		return convertView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
