/**
 * IStateKpi
 */
package com.bonc.mobile.hbmclient.state.levelkpi;

/**
 * @author liweigao
 *
 */
public interface IStateKpi<T> {
	void enter();

	void changeState();

	T getStateFlag();
}
