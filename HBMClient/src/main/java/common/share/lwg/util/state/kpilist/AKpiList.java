/**
 * AKpiList
 */
package common.share.lwg.util.state.kpilist;

import common.share.lwg.util.mediator.proxy_impl.mainkpi.ViewColleague;

/**
 * @author liweigao
 *
 */
public abstract class AKpiList implements IKpiList {
	protected ViewColleague machine;
	protected boolean isFirst = true;
	
	abstract protected void addView();
	abstract protected void updateView();
	
	public AKpiList(ViewColleague machine) {
		this.machine = machine;
	}

	/* (non-Javadoc)
	 * @see common.share.lwg.util.state.IState#enter()
	 */
	@Override
	public void enter() {
		if(isFirst) {
			addView();
			isFirst = false;
		} else {
			
		}
		
		updateView();
	}

	/* (non-Javadoc)
	 * @see common.share.lwg.util.state.IState#changeState()
	 */
	@Override
	public void changeState() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see common.share.lwg.util.state.IState#getStateFlag()
	 */
	@Override
	public Object getStateFlag() {
		// TODO Auto-generated method stub
		return null;
	}

}
