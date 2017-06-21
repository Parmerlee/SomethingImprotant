/**
 * ABOState
 */
package com.bonc.mobile.hbmclient.state.business_outlets;

/**
 * @author liweigao
 *
 */
public abstract class ABOState implements IBOState {
	protected BusinessOutletsViewSwitcher machine;

	public ABOState(BusinessOutletsViewSwitcher vs) {
		this.machine = vs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.IBOState#onActivityResult
	 * (int)
	 */
	@Override
	public void onActivityResult(int code) {

	}

}
