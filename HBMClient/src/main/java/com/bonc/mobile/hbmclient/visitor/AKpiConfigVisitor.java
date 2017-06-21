/**
 * AKpiConfigVisitor
 */
package com.bonc.mobile.hbmclient.visitor;

import java.util.HashMap;
import java.util.List;
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
public abstract class AKpiConfigVisitor implements IVisitor, IConfig {
	protected DBNode mDBNode;
	protected Map<String, String> requestMap;
	protected String menuCode;
	protected String areaCode;
	protected String opTime;
	protected DateRangeEnum dataType;
	protected String mainKpiCode;
	protected String parentMenuCode;

	public final String KEY_COLKEY = "trendkpicolumn";
	public final String KEY_COLS = "cols";
	public final String KEY_KPICODES = "kpicodes";
	public final String KEY_DATATABLE = "datatable";
	public final String KEY_AREA_CODE = "areacode";
	public final String KEY_DATA_TYPE = "datatype";
	public final String KEY_START_TIME = "starttime";
	public final String KEY_OPTIME = "currenttime";
	public final String KEY_DIMKEY = "dimkey";
	public final String KEY_MAIN_KPI_CODE = "mainKpiCode";
	public final String KEY_KPI_CODE = "kpiCode";
	public final String KEY_MENU_CODE = "menuCode";

	public AKpiConfigVisitor(String menuCode) {
		this.menuCode = menuCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.visitor.IConfig#setAreaCode(java.lang.String)
	 */
	@Override
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
		if (this.requestMap != null) {
			this.requestMap.put(KEY_AREA_CODE, areaCode);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.visitor.IConfig#setDataType(java.lang.String)
	 */
	@Override
	public void setDataType(DateRangeEnum dataType) {
		this.dataType = dataType;
		if (this.requestMap != null) {
			this.requestMap.put(KEY_DATA_TYPE, dataType.getDateFlag());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.visitor.IConfig#setStartTime(java.lang.String)
	 */
	@Override
	public void setStartTime(String startTime) {
		if (this.requestMap != null) {
			this.requestMap.put(KEY_START_TIME, startTime);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.visitor.IConfig#setOpTime(java.lang.String)
	 */
	@Override
	public void setOpTime(String optime) {
		this.opTime = optime;
		if (this.requestMap != null) {
			this.requestMap.put(KEY_OPTIME, optime);
		}
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
		this.mainKpiCode = kpiCode;
		if (this.requestMap != null) {
			this.requestMap.put(KEY_MAIN_KPI_CODE, kpiCode);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.visitor.IConfig#setDimKey(java.lang.String)
	 */
	@Override
	public void setDimKey(String dimKey) {
		if (this.requestMap != null) {
			this.requestMap.put(KEY_DIMKEY, dimKey);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getMenuCode()
	 */
	@Override
	public String getMenuCode() {
		// TODO Auto-generated method stub
		return this.menuCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getColKey()
	 */
	@Override
	public String[] getColKey() {
		// TODO Auto-generated method stub
		return this.mDBNode.getColkeyList();
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
		Map<String, Map<String, String>> ruleAndUnit = this.mDBNode
				.getKpiRuleAndUnit(key, menuCode);
		return ruleAndUnit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getComplexDimInfo()
	 */
	@Override
	public ComplexDimInfo getComplexDimInfo() {
		return this.mDBNode.getComplexDimInfo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getMenuColInfoMap()
	 */
	@Override
	public Map<String, MenuColumnInfo> getMenuColInfoMap() {
		return this.mDBNode.getMenuColInfoMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getKpiInfoMap()
	 */
	@Override
	public Map<String, KpiInfo> getKpiInfoMap() {
		return this.mDBNode.getKpiInfoMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getTitleName()
	 */
	@Override
	public String[] getTitleName() {
		return this.mDBNode.getTitleList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getRequestServerParams()
	 */
	@Override
	public Map<String, String> getRequestServerParams() {
		return this.requestMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getLevelKpiFlagMap()
	 */
	@Override
	public Map<String, Map<String, String>> getLevelKpiFlagMap() {
		Map<String, Map<String, String>> levelKpiFlagMap = new HashMap<String, Map<String, String>>();
		List<Map<String, String>> list = this.mDBNode.getLevelKpiFlagList();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> map = list.get(i);
			String kpiCode = map.get(KEY_KPI_CODE);
			levelKpiFlagMap.put(kpiCode, map);
		}
		return levelKpiFlagMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.visitor.IVisitor#visitor(com.bonc.mobile.hbmclient
	 * .visitor.DBNode)
	 */
	@Override
	public void visitor(DBNode node) {
		this.mDBNode = node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getColKeyComposite()
	 */
	@Override
	public String getColKeyComposite() {
		return this.mDBNode.getColkey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getMenuName()
	 */
	@Override
	public String getMenuName() {
		return this.mDBNode.getMenuInfo().get("menuName");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getAreaCode()
	 */
	@Override
	public String getAreaCode() {
		// TODO Auto-generated method stub
		return this.areaCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getOpTime()
	 */
	@Override
	public String getOpTime() {
		// TODO Auto-generated method stub
		return this.opTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getDataType()
	 */
	@Override
	public DateRangeEnum getDataType() {
		// TODO Auto-generated method stub
		return this.dataType;
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
		this.parentMenuCode = menuCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getExCludedMenuCode()
	 */
	@Override
	public String getParentMenuCode() {
		// TODO Auto-generated method stub
		return this.parentMenuCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.visitor.IConfig#getShowDailyReport()
	 */
	@Override
	public String getShowDailyReport() {
		Map<String, String> menuAddInfo = this.mDBNode
				.getDailyReportFlag(parentMenuCode);
		String showDailyReport = menuAddInfo.get("show_daily_report");
		return showDailyReport;
	}

}
