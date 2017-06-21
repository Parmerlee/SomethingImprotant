/**
 * MainKpiMediator
 */
package common.share.lwg.util.mediator.proxy_impl.mainkpi;

import org.json.JSONObject;

import android.app.Activity;

import common.share.lwg.util.mediator.IMediator;

/**
 * @author liweigao
 *
 */
public class MainKpiMediator implements IMediator {
	private DataColleague dataColleague;
	private ViewColleague viewColleague;

	/* (non-Javadoc)
	 * @see common.share.lwg.util.mediator.IMediator#createColleagues(java.lang.Object[])
	 */
	@Override
	public void createColleagues(Object... args) {
		Activity a = (Activity) args[0];
		this.dataColleague = new DataColleague();
		this.dataColleague.setMediator(this);
		this.viewColleague = new ViewColleague(a);
		this.viewColleague.setMediator(this);
	}

	public void setData(JSONObject jo) throws Exception {
		this.dataColleague.setData(jo);
	}
	
	public void updateView() {
		this.viewColleague.updateView(this.dataColleague.getIsGroup());
	}

	/**
	 * @return the dataColleague
	 */
	public DataColleague getDataColleague() {
		return dataColleague;
	}
	
    public ViewColleague getViewColleague() {
        return viewColleague;
    }
    
}
