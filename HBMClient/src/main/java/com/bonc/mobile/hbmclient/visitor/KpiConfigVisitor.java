/**
 * KpiConfigVisitor
 */
package com.bonc.mobile.hbmclient.visitor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author liweigao
 * 
 */
public class KpiConfigVisitor extends AKpiConfigVisitor {

	private Map<String, Map<String, String>> levelKpiFlagMap;
	private Map<String, Map<String, String>> kpiRuleAndUnitMap;

	public KpiConfigVisitor(String menuCode) {
		super(menuCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#initialConfigInfo()
	 */
	@Override
	public void initialConfigInfo() {
		this.mDBNode.initialKpiConfig(menuCode);
		initialRequestServerParams();

		this.levelKpiFlagMap = super.getLevelKpiFlagMap();
		Set<String> codeSet = this.levelKpiFlagMap.keySet();
		this.kpiRuleAndUnitMap = super.getKpiRuleAndUnit(codeSet);
	}

	private void initialRequestServerParams() {
		this.requestMap = new HashMap<String, String>();
		this.requestMap.put(KEY_COLKEY, this.mDBNode.getColkey());
		this.requestMap.put(KEY_COLS, this.mDBNode.getCols());
		this.requestMap.put(KEY_DATATABLE, this.mDBNode.getDatatable());
		this.requestMap.put(KEY_KPICODES, this.mDBNode.getKpicodes());

		this.requestMap.put(KEY_OPTIME, this.opTime);
		this.requestMap.put(KEY_AREA_CODE, this.areaCode);
		this.requestMap.put(KEY_DATA_TYPE, this.dataType.getDateFlag());
		this.requestMap.put(KEY_MENU_CODE, this.menuCode);
	}

	/**
	 * @return the levelKpiFlagMap
	 */
	public Map<String, Map<String, String>> getLevelKpiFlagMap() {
		return levelKpiFlagMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getKpiRuleAndUnit()
	 */
	@Override
	public Map<String, Map<String, String>> getKpiRuleAndUnit() {
		// TODO Auto-generated method stub
		return this.kpiRuleAndUnitMap;
	}

}
