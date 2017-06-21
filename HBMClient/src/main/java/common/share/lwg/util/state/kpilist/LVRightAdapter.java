/**
 * LVRightAdapter
 */
package common.share.lwg.util.state.kpilist;

import java.util.List;
import java.util.Map;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import common.share.lwg.util.builder.mainkpi.LKpiRowBuilder;
import common.share.lwg.util.builder.mainkpi.LinearDirector;
import common.share.lwg.util.mediator.proxy_impl.mainkpi.DataColleague;

/**
 * @author liweigao
 *
 */
public class LVRightAdapter extends BaseAdapter {
	private List<Map<String, String>> columnInfoRight;
	private List<String> composite;
	private Map<String, Map<String, String>> kpiConfigInfo;
	private LinearDirector linearDirector;
	private DataColleague dataColleague;
	
	public LVRightAdapter(DataColleague dataColleague) {
		this.dataColleague = dataColleague;
		this.columnInfoRight = this.dataColleague.getColumnInfoRight();
		this.composite = this.dataColleague.getCompositeList();
		this.kpiConfigInfo = this.dataColleague.getKpiConfigInfo();
		this.linearDirector = new LinearDirector();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		if(this.composite == null) {
			return 0;
		} else {
			return composite.size();
		}
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			LKpiRowBuilder builder = new LKpiRowBuilder(parent.getContext());
			this.linearDirector.buildKpiRow(builder, columnInfoRight);
			convertView = builder;
			convertView.setTag(builder);
		}
		LKpiRowBuilder builder = (LKpiRowBuilder) convertView.getTag();
		String kpiCode = this.composite.get(position);
		
		Map<String,String> kpiConfig = this.kpiConfigInfo.get(kpiCode);
		for(int i=0;i<columnInfoRight.size();i++) {
			Map<String,String> column = columnInfoRight.get(i);
			// columnType为1 是趋势图 
			String columnType = column.get("COLUMNTYPE");
			if("0".equals(columnType)) {
				builder.setColumnType0Args(kpiCode,this.dataColleague);
			} else if("1".equals(columnType)) {
				builder.setColumnType1Args(kpiCode,this.dataColleague);
			} else if("2".equals(columnType)) {
				
			} else {
				builder.setColumnTypeArgs(kpiCode,this.dataColleague,column);
			}
		}
		builder.buildAppearance(kpiConfig);
		
		return convertView;
	}

}
