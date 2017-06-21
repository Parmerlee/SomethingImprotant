/**
 * TypeComparator
 */
package com.bonc.mobile.hbmclient.state.business_outlets.kpi.compare;

import java.util.Comparator;
import java.util.Map;

/**
 * @author liweigao
 *
 */
public class TypeComparator implements Comparator<Map<String, String>> {
	private int column;
	private final String PRI = "private";
	private final String PUB = "public";

	public TypeComparator(int column) {
		this.column = column;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Map<String, String> lhs, Map<String, String> rhs) {
		String lType = null;
		String rType = null;
		String lKey = null;
		String rKey = null;
		try {
			lType = lhs.get("type");
			lKey = getKey(lType);
			rType = rhs.get("type");
			rKey = getKey(rType);
		} catch (Exception e) {
			// TODO: handle exception
		}

		int left = 0;
		int right = 0;
		try {
			left = Integer.parseInt(lhs.get(lKey));
		} catch (Exception e) {
			left = 0;
		}
		try {
			right = Integer.parseInt(rhs.get(rKey));
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

	private String getKey(String type) {
		if (0 == column) {
			if (PRI.equals(type)) {
				return "privateCurrent";
			} else {
				return "publicCurrent";
			}
		} else {
			if (PRI.equals(type)) {
				return "privateTotal";
			} else {
				return "publicTotal";
			}
		}
	}

}
