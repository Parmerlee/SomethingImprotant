/**
 * SimpleSort
 */
package com.bonc.mobile.hbmclient.state.business_outlets.kpi.compare;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author liweigao
 *
 */
public class SimpleSort {

	public void ascSort(List<Map<String, String>> data, String key) {
		SimpleComparator2 sc = new SimpleComparator2(key);
		Collections.sort(data, sc);
	}

	public void descSort(List<Map<String, String>> data, String key) {
		SimpleComparator2 sc = new SimpleComparator2(key);
		Comparator comparator = Collections.reverseOrder(sc);
		Collections.sort(data, comparator);
	}
}
