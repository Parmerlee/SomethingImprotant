/**
 * KpiConfigVisitor2
 */
package com.bonc.mobile.hbmclient.visitor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liweigao
 * 
 */
public class KpiConfigVisitor2 extends AKpiConfigVisitor {

	public KpiConfigVisitor2(String menuCode) {
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
	}

	private void initialRequestServerParams() {
		this.requestMap = new HashMap<String, String>();
		this.requestMap.put(KEY_OPTIME, this.opTime);
		this.requestMap.put(KEY_AREA_CODE, this.areaCode);
		this.requestMap.put(KEY_DATA_TYPE, this.dataType.getDateFlag());
		this.requestMap.put(KEY_MAIN_KPI_CODE, this.mainKpiCode);
		this.requestMap.put(KEY_MENU_CODE, this.menuCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getKpiRuleAndUnit()
	 */
	@Override
	public Map<String, Map<String, String>> getKpiRuleAndUnit() {
		// TODO Auto-generated method stub
		return null;
	}

}
