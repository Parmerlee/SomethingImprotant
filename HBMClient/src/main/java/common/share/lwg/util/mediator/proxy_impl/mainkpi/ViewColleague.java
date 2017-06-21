/**
 * ViewColleague
 */
package common.share.lwg.util.mediator.proxy_impl.mainkpi;

import android.app.Activity;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;

import common.share.lwg.util.builder.mainkpi.LTitleBuilder;
import common.share.lwg.util.builder.mainkpi.LinearDirector;
import common.share.lwg.util.state.kpilist.ExpandableListViewState;
import common.share.lwg.util.state.kpilist.IKpiList;
import common.share.lwg.util.state.kpilist.ListViewState;

/**
 * @author liweigao
 *
 */
public class ViewColleague extends AMainKpiColleague {
	private IKpiList state,lvState,elvState;
	private Activity activity;
	private LinearDirector linearDirector;
	
	public ViewColleague(Activity activity) {
		this.activity = activity;
		this.linearDirector = new LinearDirector();
		this.lvState = new ListViewState(this);
		this.elvState = new ExpandableListViewState(this);
	}
	
	public void updateView(String isGroup) {
		updateTitle();
		
		if("1".equals(isGroup)) {
			this.state = this.elvState;
		} else {
			this.state = this.lvState;
		}
		if(mainKpiMediator.getDataColleague().isEmpty())
		    Toast.makeText(activity, "暂无今日数据展示", Toast.LENGTH_LONG).show();
		this.state.enter();
	}
	
	private void updateTitle() {
		DataColleague dc = this.mainKpiMediator.getDataColleague();
		LTitleBuilder titleLeft = (LTitleBuilder) this.activity.findViewById(R.id.id_title_left);
		titleLeft.removeAllViews();
		//左侧title
		this.linearDirector.buildTitle(titleLeft, dc.getColumnInfoLeft());
		
		LTitleBuilder titleRight = (LTitleBuilder) this.activity.findViewById(R.id.id_title_right);
		titleRight.removeAllViews();
		//右侧title
		this.linearDirector.buildTitle(titleRight, dc.getColumnInfoRight());
	}

	/**
	 * @return the activity
	 */
	public Activity getActivity() {
		return activity;
	}
	
}
