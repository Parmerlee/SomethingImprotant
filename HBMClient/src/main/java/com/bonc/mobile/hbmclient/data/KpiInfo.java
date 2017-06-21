package com.bonc.mobile.hbmclient.data;

import java.io.Serializable;

public class KpiInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String kpiCode;
	private String menuCode;
	private String kpiUnit;
	private String kpiName;
	private String kpiRule;
	private String kpiDefine;

	public String getKpiCode() {
		return kpiCode;
	}

	public void setKpiCode(String kpiCode) {
		this.kpiCode = kpiCode;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getKpiUnit() {
		return kpiUnit;
	}

	public void setKpiUnit(String kpiUnit) {
		this.kpiUnit = kpiUnit;
	}

	public String getKpiName() {
		return kpiName;
	}

	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}

	public String getKpiRule() {
		return kpiRule;
	}

	public void setKpiRule(String kpiRule) {
		this.kpiRule = kpiRule;
	}

	public String getKpiDefine() {
		return kpiDefine;
	}

	public void setKpiDefine(String kpiDefine) {
		this.kpiDefine = kpiDefine;
	}

}
