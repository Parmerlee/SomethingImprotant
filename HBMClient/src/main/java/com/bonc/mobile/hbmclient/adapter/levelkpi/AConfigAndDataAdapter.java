/**
 * AConfigAndDataAdapter
 */
package com.bonc.mobile.hbmclient.adapter.levelkpi;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;

import com.bonc.mobile.hbmclient.data.ComplexDimInfo;
import com.bonc.mobile.hbmclient.data.KpiInfo;
import com.bonc.mobile.hbmclient.data.MenuColumnInfo;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.visitor.IConfig;

/**
 * @author liweigao
 * 
 */
public abstract class AConfigAndDataAdapter implements IConfigAndDataAdapter {
	protected IConfig visitor;
	protected String backData;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#initialConfigInfo()
	 */
	@Override
	public void initialConfigInfo() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.visitor.IConfig#setAreaCode(java.lang.String)
	 */
	@Override
	public void setAreaCode(String areaCode) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.visitor.IConfig#setStartTime(java.lang.String)
	 */
	@Override
	public void setStartTime(String startTime) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.visitor.IConfig#setOpTime(java.lang.String)
	 */
	@Override
	public void setOpTime(String optime) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.visitor.IConfig#setMainKpiCode(java.lang.String
	 * )
	 */
	@Override
	public void setMainKpiCode(String kpiCode) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.visitor.IConfig#setDimKey(java.lang.String)
	 */
	@Override
	public void setDimKey(String dimKey) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getMenuCode()
	 */
	@Override
	public String getMenuCode() {
		return visitor.getMenuCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getColKey()
	 */
	@Override
	public String[] getColKey() {
		// TODO Auto-generated method stub
		return this.visitor.getColKey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getTitleName()
	 */
	@Override
	public String[] getTitleName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getColKeyComposite()
	 */
	@Override
	public String getColKeyComposite() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getComplexDimInfo()
	 */
	@Override
	public ComplexDimInfo getComplexDimInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getKpiInfoMap()
	 */
	@Override
	public Map<String, KpiInfo> getKpiInfoMap() {
		return this.visitor.getKpiInfoMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getRequestServerParams()
	 */
	@Override
	public Map<String, String> getRequestServerParams() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getMenuColInfoMap()
	 */
	@Override
	public Map<String, MenuColumnInfo> getMenuColInfoMap() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getLevelKpiFlagMap()
	 */
	@Override
	public Map<String, Map<String, String>> getLevelKpiFlagMap() {
		return visitor.getLevelKpiFlagMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getKpiRuleAndUnit()
	 */
	@Override
	public Map<String, Map<String, String>> getKpiRuleAndUnit() {
		return visitor.getKpiRuleAndUnit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.visitor.IConfig#getKpiRuleAndUnit(java.util
	 * .Set)
	 */
	@Override
	public Map<String, Map<String, String>> getKpiRuleAndUnit(Set<String> key) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.adapter.levelkpi.IConfigAndDataAdapter#setConfig
	 * (com.bonc.mobile.hbmclient.visitor.IConfig)
	 */
	@Override
	public void setConfig(IConfig config) {
		this.visitor = config;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.adapter.levelkpi.IConfigAndDataAdapter#setBackData
	 * (java.lang.String)
	 */
	@Override
	public void setBackData(String result) throws JSONException {
		this.backData = result;
		handle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.adapter.levelkpi.IConfigAndDataAdapter#getKpiData
	 * ()
	 */
	@Override
	public List<Map<String, String>> getKpiData() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.adapter.levelkpi.IConfigAndDataAdapter#getTrendMap
	 * ()
	 */
	@Override
	public Map<String, List<Double>> getTrendMap() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.adapter.levelkpi.IConfigAndDataAdapter#getGroupList
	 * ()
	 */
	@Override
	public List<String> getGroupList() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.adapter.levelkpi.IConfigAndDataAdapter#getKpiList
	 * ()
	 */
	@Override
	public List<List<Map<String, String>>> getKpiList() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getMenuName()
	 */
	@Override
	public String getMenuName() {
		// TODO Auto-generated method stub
		return this.visitor.getMenuName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getAreaCode()
	 */
	@Override
	public String getAreaCode() {
		// TODO Auto-generated method stub
		return this.visitor.getAreaCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getOpTime()
	 */
	@Override
	public String getOpTime() {
		// TODO Auto-generated method stub
		return this.visitor.getOpTime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.visitor.IConfig#setDataType(com.bonc.mobile
	 * .hbmclient.enum_type.DateRangeEnum)
	 */
	@Override
	public void setDataType(DateRangeEnum dre) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getDataType()
	 */
	@Override
	public DateRangeEnum getDataType() {
		// TODO Auto-generated method stub
		return this.visitor.getDataType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.visitor.IConfig#setExcludedMenuCode(java.lang
	 * .String)
	 */
	@Override
	public void setParentMenuCode(String menuCode) {
		this.visitor.setParentMenuCode(menuCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getExCludedMenuCode()
	 */
	@Override
	public String getParentMenuCode() {
		// TODO Auto-generated method stub
		return this.visitor.getParentMenuCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.adapter.levelkpi.IConfigAndDataAdapter#handle()
	 */
	@Override
	public void handle() throws JSONException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getShowDailyReport()
	 */
	@Override
	public String getShowDailyReport() {
		// TODO Auto-generated method stub
		return visitor.getShowDailyReport();
	}

}
