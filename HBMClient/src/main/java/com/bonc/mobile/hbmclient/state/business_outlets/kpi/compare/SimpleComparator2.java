/**
 * SimpleComparator
 */
package com.bonc.mobile.hbmclient.state.business_outlets.kpi.compare;

import java.util.Comparator;
import java.util.Map;

/**
 * @author liweigao
 *
 */
public class SimpleComparator2 implements Comparator<Map<String, String>> {
	private String key;

	public SimpleComparator2(String key) {
		this.key = key;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Map<String, String> lhs, Map<String, String> rhs) {
		int left = 0;
		int right = 0;
		try {
			left = Integer.parseInt(lhs.get(key));
		} catch (Exception e) {
			left = 0;
		}
		try {
			right = Integer.parseInt(rhs.get(key));
		} catch (Exception e) {
			right = 0;
		}

		if (left > right) {
			return 1;
		} else if (left == right) {
			return 0;
		} else {
			return -1;
		}
	}

}
