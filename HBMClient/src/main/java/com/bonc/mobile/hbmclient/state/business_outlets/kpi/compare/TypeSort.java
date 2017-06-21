/**
 * TypeSort
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
public class TypeSort {
	public void ascSort(List<Map<String, String>> data, int column) {
		TypeComparator tc = new TypeComparator(column);
		Collections.sort(data, tc);
	}

	public void descSort(List<Map<String, String>> data, int column) {
		TypeComparator tc = new TypeComparator(column);
		Comparator comparator = Collections.reverseOrder(tc);
		Collections.sort(data, comparator);
	}
}
