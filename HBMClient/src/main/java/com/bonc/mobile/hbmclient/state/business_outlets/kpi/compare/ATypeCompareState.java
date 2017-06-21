/**
 * ATypeCompareState
 */
package com.bonc.mobile.hbmclient.state.business_outlets.kpi.compare;

/**
 * @author liweigao
 *
 */
public abstract class ATypeCompareState<T> implements ICompareState2<Integer> {
	protected T machine;

	public ATypeCompareState(T machine) {
		this.machine = machine;
	}
}
