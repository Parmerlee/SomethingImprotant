/**
 * ACompareState
 */
package com.bonc.mobile.hbmclient.state.business_outlets.kpi.compare;

/**
 * @author liweigao
 * 
 */
public abstract class ACompareState2<T> implements ICompareState2<String> {
	protected T machine;
	protected SimpleSort sort;

	public ACompareState2(T machine) {
		this.machine = machine;
		this.sort = new SimpleSort();
	}

}
