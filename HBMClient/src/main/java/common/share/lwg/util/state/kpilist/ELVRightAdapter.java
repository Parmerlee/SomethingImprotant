/**
 * ELVRightAdapter
 */
package common.share.lwg.util.state.kpilist;

import java.util.List;
import java.util.Map;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bonc.mobile.common.kpi.ColumnDataFilter;
import com.bonc.mobile.common.kpi.ColumnDataFilter.ColumnDisplyInfo;
import com.bonc.mobile.common.util.NumberUtil;
import com.bonc.mobile.common.util.StringUtil;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.util.ViewUtil;

import common.share.lwg.util.builder.mainkpi.LKpiRowBuilder;
import common.share.lwg.util.builder.mainkpi.LinearDirector;
import common.share.lwg.util.mediator.proxy_impl.mainkpi.DataColleague;

/**
 * @author liweigao
 * 
 */
public class ELVRightAdapter extends BaseExpandableListAdapter {
	private List<Map<String, String>> columnInfoRight;
	private List<Map<String, Object>> composite;
	private Map<String, Map<String, String>> kpiConfigInfo;
	private LinearDirector linearDirector;
	private DataColleague dataColleague;
	private ViewUtil mViewUtil = ViewUtil.getSingleInstance();

	public ELVRightAdapter(DataColleague dataColleague) {
		super();
		this.dataColleague = dataColleague;
		this.columnInfoRight = this.dataColleague.getColumnInfoRight();
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
		if (this.composite == null) {
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
	//		Log.d("AAAA", "title:"+title);
			if (title != null) {
				if (title.length() <= 7) {
					title = "";
				} else {
					title = title.substring(7);
				}
			} else {
				title = "";
			}
		} catch (Exception e) {
			title = "";
		} finally {
			tv.setText(title);
//			mViewUtil.setTextStyleFirstLevel(tv);
		}

		showProgress(convertView,groupPosition);
		return convertView;
	}

	//判断有无progress，并显示
	void showProgress(View convertView,int groupPosition){
	    if(composite.get(groupPosition).containsKey("GROUP_Progress")){
            convertView.findViewById(R.id.pbContainer).setVisibility(View.VISIBLE);
            String kpiCode = (String)composite.get(groupPosition).get("GROUP_Progress");
            Map<String, String> data=dataColleague.getKpiData().get(kpiCode);
            String kpiRule = StringUtil.nullToString(dataColleague.getKpiConfigInfo().get(kpiCode).get("KPI_CAL_RULE"));
            String[] ra=kpiRule.split("\\$");
            if(ra.length!=6)return;
            ColumnDisplyInfo cdi=ColumnDataFilter.getInstance().doFilter(ra[0], ra[1], data.get("CURMONTH_VALUE"));
	        ProgressBar pb=(ProgressBar)convertView.findViewById(R.id.id_progressbar);
	        pb.setProgress((int)NumberUtil.changeToDouble(cdi.getValue()));
	        TextViewUtils.setText(convertView, R.id.id_pb_num, cdi.getValue()+cdi.getUnit());
	        cdi=ColumnDataFilter.getInstance().doFilter(ra[2], ra[3], data.get("CURYEAR_VALUE"));
            TextViewUtils.setText(convertView, R.id.id_left_value, cdi.getValue()+cdi.getUnit());
            cdi=ColumnDataFilter.getInstance().doFilter(ra[4], ra[5], data.get("PREYEAR_VALUE"));
            TextViewUtils.setText(convertView, R.id.id_right_value, cdi.getValue()+cdi.getUnit());
	    }
	    else convertView.findViewById(R.id.pbContainer).setVisibility(View.INVISIBLE);
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
			this.linearDirector.buildKpiRow(builder, columnInfoRight);
			convertView = builder;
			convertView.setTag(builder);
		}
		LKpiRowBuilder builder = (LKpiRowBuilder) convertView.getTag();
		Map<String, Object> item = this.composite.get(groupPosition);
		List<String> kpiCodes = (List<String>) item.get("KPICODELIST");
		String kpiCode = kpiCodes.get(childPosition);

		Map<String, String> kpiConfig = this.kpiConfigInfo.get(kpiCode);
		for (int i = 0; i < columnInfoRight.size(); i++) {
			Map<String, String> column = columnInfoRight.get(i);
			String columnType = column.get("COLUMNTYPE");
			if ("0".equals(columnType)) {
				builder.setColumnType0Args(kpiCode, this.dataColleague);
			} else if ("1".equals(columnType)) {
				builder.setColumnType1Args(kpiCode, this.dataColleague);
			} else if ("2".equals(columnType)) {

			} else {
				builder.setColumnTypeArgs(kpiCode, this.dataColleague, column);
			}
		}
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
