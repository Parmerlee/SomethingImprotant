package com.bonc.mobile.hbmclient.util;

public class ArrayUtil {

	public static String getInClause(String column, String[] strs) {
		StringBuilder inClause = new StringBuilder();
		if (strs != null && strs.length > 0) {
			inClause.append(" ").append(column).append(" in ('")
					.append(strs[0]).append("'");
			for (int i = 1; i < strs.length; i++) {
				inClause.append(",'").append(strs[i]).append("'");
			}
			inClause.append(") ");
		}

		return inClause.toString();
	}

	public static String join(String[] strs, String sp) {
		StringBuilder buf = new StringBuilder();
		for (String s : strs) {
			buf.append(s + sp);
		}
		buf.deleteCharAt(buf.length() - 1);
		return buf.toString();
	}

}
