/**
 * LVLeftAdapter
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
public class LVLeftAdapter extends BaseAdapter {
	private List<Map<String,String>> columnInfoLeft;
	private List<String> composite;
	private Map<String,Map<String,String>> kpiConfigInfo;
	private LinearDirector linearDirector;
	private DataColleague dataColleague;
	
	public LVLeftAdapter(DataColleague dataColleague) {
		super();
		this.dataColleague = dataColleague;
		this.columnInfoLeft = this.dataColleague.getColumnInfoLeft();
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
			this.linearDirector.buildKpiRow(builder, this.columnInfoLeft);
			convertView = builder;
			convertView.setTag(builder);
		} 
		LKpiRowBuilder builder = (LKpiRowBuilder) convertView.getTag();
		String kpiCode = this.composite.get(position);
		
		builder.setColumnType2Args(kpiCode,this.dataColleague);
		Map<String, String> kpiConfig = this.kpiConfigInfo.get(kpiCode);
		builder.buildAppearance(kpiConfig);
		
		return convertView;
	}

	
}
