package com.bonc.mobile.hbmclient.data;

import java.io.Serializable;

public class MenuColumnInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6206176512357571633L;
	private String colKey; // 列的key
	private String colUnit; // 列的单位.
	private String colRule; // 列规则.
	private String colName; // 列的中文名称.
	private String menuCode; // 列归属的菜单.

	public String getColKey() {
		return colKey;
	}

	public void setColKey(String colKey) {
		this.colKey = colKey;
	}

	public String getColUnit() {
		return colUnit;
	}

	public void setColUnit(String colUnit) {
		this.colUnit = colUnit;
	}

	public String getColRule() {
		return colRule;
	}

	public void setColRule(String colRule) {
		this.colRule = colRule;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

}
