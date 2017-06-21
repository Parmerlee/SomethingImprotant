/**
 * IBOKpiState
 */
package com.bonc.mobile.hbmclient.state.business_outlets.kpi;

import com.bonc.mobile.hbmclient.state.business_outlets.kpi.compare.ICompareState2;
import common.share.lwg.util.life.ILife;

/**
 * @author liweigao
 *
 */
public interface IBOKpiState extends ILife {
	ICompareState2 getASCState();

	ICompareState2 getDESCState();

	void setSortState(ICompareState2 state);

	void resetSort();
}
