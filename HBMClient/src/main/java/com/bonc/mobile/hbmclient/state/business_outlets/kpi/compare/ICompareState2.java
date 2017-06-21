/**
 * ICompareState
 */
package com.bonc.mobile.hbmclient.state.business_outlets.kpi.compare;

import java.util.List;
import java.util.Map;

/**
 * @author liweigao
 *
 */
public interface ICompareState2<T> {
	void sort(List<Map<String, String>> data, T key, Object... out);

	void changeState();
}
