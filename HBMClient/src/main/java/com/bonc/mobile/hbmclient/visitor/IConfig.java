/**
 * IConfig
 */
package com.bonc.mobile.hbmclient.visitor;

import java.util.Map;
import java.util.Set;

import com.bonc.mobile.hbmclient.data.ComplexDimInfo;
import com.bonc.mobile.hbmclient.data.KpiInfo;
import com.bonc.mobile.hbmclient.data.MenuColumnInfo;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;

/**
 * @author liweigao
 *
 */
public interface IConfig {
	void initialConfigInfo();

	void setAreaCode(String areaCode);

	void setDataType(DateRangeEnum dre);

	void setStartTime(String startTime);

	void setOpTime(String optime);

	void setMainKpiCode(String kpiCode);

	void setDimKey(String dimKey);

	void setParentMenuCode(String menuCode);

	String getParentMenuCode();

	String getMenuCode();

	String getMenuName();

	String getAreaCode();

	String getOpTime();

	String getShowDailyReport();

	DateRangeEnum getDataType();

	String[] getColKey();

	String[] getTitleName();

	String getColKeyComposite();

	ComplexDimInfo getComplexDimInfo();

	Map<String, KpiInfo> getKpiInfoMap();

	Map<String, String> getRequestServerParams();

	Map<String, MenuColumnInfo> getMenuColInfoMap();

	Map<String, Map<String, String>> getLevelKpiFlagMap();

	Map<String, Map<String, String>> getKpiRuleAndUnit();

	Map<String, Map<String, String>> getKpiRuleAndUnit(Set<String> key);
}
