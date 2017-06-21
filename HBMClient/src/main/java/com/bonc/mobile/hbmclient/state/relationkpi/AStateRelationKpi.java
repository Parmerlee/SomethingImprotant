/**
 * AStateRelationKpi
 */
package com.bonc.mobile.hbmclient.state.relationkpi;

import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.state.levelkpi.IStateKpi;

/**
 * @author liweigao
 *
 */
public abstract class AStateRelationKpi implements IStateKpi<DateRangeEnum> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.state.levelkpi.IStateKpi#enter()
	 */
	@Override
	public void enter() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.state.levelkpi.IStateKpi#changeState()
	 */
	@Override
	public void changeState() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.state.levelkpi.IStateKpi#getStateFlag()
	 */
	@Override
	public DateRangeEnum getStateFlag() {
		// TODO Auto-generated method stub
		return null;
	}

}
