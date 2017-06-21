/**
 * DBNode
 */
package com.bonc.mobile.hbmclient.visitor;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.data.ComplexDimInfo;
import com.bonc.mobile.hbmclient.data.KpiInfo;
import com.bonc.mobile.hbmclient.data.MenuColumnInfo;
import com.bonc.mobile.hbmclient.data.SQLHelper;

/**
 * @author liweigao
 * 
 */
public class DBNode implements INode {
	private BusinessDao dao = new BusinessDao();
	private SQLHelper mSqlHelper = new SQLHelper();

	private String colkey;
	private String cols;
	private String kpicodes;
	private String datatable;

	private List<Map<String, String>> levelKpiFlagList;

	private List<Map<String, String>> allColumnList;// 菜单所有的列
	private String[] colkeyList; // 菜单要展示的列
	private String[] titleList; // 菜单要展示的列中文名称.
	private Map<String, MenuColumnInfo> menuColInfoMap; // 存放所有指标所有列的信息.
	private Map<String, KpiInfo> kpiInfoMap; // 指标信息.
	private Map<String, String> menuInfo; // 菜单信息.
	private ComplexDimInfo complexDimInfo; // 复合维度信息

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.visitor.INode#accept(com.bonc.mobile.hbmclient
	 * .visitor.IVisitor)
	 */
	@Override
	public void accept(IVisitor visitor) {
		visitor.visitor(this);
	}

	public void initialKpiConfig(final String menuCode) {

		this.cols = dao.getMenuAllColumnString(menuCode);
		this.kpicodes = dao.getMenuAllKpiString2(menuCode);
		this.datatable = dao.getMenuColDataTable(menuCode);
		this.colkey = dao.getMenuAllColumnList(menuCode).get(0).get("col")
				.toLowerCase(Locale.CHINA);

		initialLevelKpiFlagList(menuCode);
		initialOldConfig(menuCode);
	}

	private void initialLevelKpiFlagList(String menucode) {
		String sql = "select resource_code as kpiCode,kpi_level as kpiLevel,relation_tag as relationTag from kpi_menu_rel where menu_code=? order by group_order asc,kpi_order asc";
		levelKpiFlagList = mSqlHelper.queryForList(sql,
				new String[] { menucode });
	}

	private void initialOldConfig(final String menuCode) {
		// 查询菜单信息.
		menuInfo = dao.getMenuInfo(menuCode);
		complexDimInfo = dao.getMenuComplexDimKey(menuInfo);
		allColumnList = dao.getMenuAllColumnList(menuCode);
		int len = allColumnList.size();
		colkeyList = new String[len + 1];
		titleList = new String[len + 1];
		// 左侧的key和名称.
		colkeyList[0] = "kpi_name";
		titleList[0] = "指标名称";
		if (menuColInfoMap == null) {
			menuColInfoMap = new HashMap<String, MenuColumnInfo>();
		}
		for (int i = 0; i < len; i++) {
			String col = allColumnList.get(i).get("col")
					.toLowerCase(Locale.CHINA);
			colkeyList[i + 1] = col;
			titleList[i + 1] = allColumnList.get(i).get("colname")
					.toLowerCase(Locale.CHINA);

			MenuColumnInfo colInfo = new MenuColumnInfo();
			colInfo.setColKey(col);
			colInfo.setColName(allColumnList.get(i).get("colname")
					.toLowerCase(Locale.CHINA));
			colInfo.setColRule(allColumnList.get(i).get("rule")
					.toLowerCase(Locale.CHINA));
			colInfo.setColUnit(allColumnList.get(i).get("unit")
					.toLowerCase(Locale.CHINA));
			colInfo.setMenuCode(allColumnList.get(i).get("menucode")
					.toLowerCase(Locale.CHINA));
			menuColInfoMap.put(col, colInfo);

		}

		kpiInfoMap = new HashMap<String, KpiInfo>();
		List<Map<String, String>> kpiInfoList = dao.getMenuAllKpiList(menuCode);
		kpiInfoMap = new HashMap<String, KpiInfo>();
		int kilen = kpiInfoList.size();
		for (int i = 0; i < kilen; i++) {
			KpiInfo kInfo = new KpiInfo();
			kInfo.setKpiCode(kpiInfoList.get(i).get("kpi_code"));
			kInfo.setMenuCode(kpiInfoList.get(i).get("menu_code"));
			kInfo.setKpiName(kpiInfoList.get(i).get("kpiName"));
			kInfo.setKpiUnit(kpiInfoList.get(i).get("kpiUnit"));
			kInfo.setKpiRule(kpiInfoList.get(i).get("rule"));
			kInfo.setKpiDefine(kpiInfoList.get(i).get("kpi_define"));
			kpiInfoMap.put(kpiInfoList.get(i).get("kpi_code"), kInfo);

		}
	}

	public Map<String, String> getDailyReportFlag(String menuCode) {
		return dao.getDailyReportFlag(menuCode);
	}

	/**
	 * @return the colkey
	 */
	public String getColkey() {
		return colkey;
	}

	/**
	 * @param colkey
	 *            the colkey to set
	 */
	public void setColkey(String colkey) {
		this.colkey = colkey;
	}

	/**
	 * @return the cols
	 */
	public String getCols() {
		return cols;
	}

	/**
	 * @param cols
	 *            the cols to set
	 */
	public void setCols(String cols) {
		this.cols = cols;
	}

	/**
	 * @return the kpicodes
	 */
	public String getKpicodes() {
		return kpicodes;
	}

	/**
	 * @param kpicodes
	 *            the kpicodes to set
	 */
	public void setKpicodes(String kpicodes) {
		this.kpicodes = kpicodes;
	}

	/**
	 * @return the datatable
	 */
	public String getDatatable() {
		return datatable;
	}

	/**
	 * @param datatable
	 *            the datatable to set
	 */
	public void setDatatable(String datatable) {
		this.datatable = datatable;
	}

	/**
	 * @return the levelKpiFlagList
	 */
	public List<Map<String, String>> getLevelKpiFlagList() {
		return levelKpiFlagList;
	}

	/**
	 * @return the allColumnList
	 */
	public List<Map<String, String>> getAllColumnList() {
		return allColumnList;
	}

	/**
	 * @return the colkeyList
	 */
	public String[] getColkeyList() {
		return colkeyList;
	}

	/**
	 * @return the titleList
	 */
	public String[] getTitleList() {
		return titleList;
	}

	/**
	 * @return the menuColInfoMap
	 */
	public Map<String, MenuColumnInfo> getMenuColInfoMap() {
		return menuColInfoMap;
	}

	/**
	 * @return the kpiInfoMap
	 */
	public Map<String, KpiInfo> getKpiInfoMap() {
		return kpiInfoMap;
	}

	/**
	 * @return the complexDimInfo
	 */
	public ComplexDimInfo getComplexDimInfo() {
		return complexDimInfo;
	}

	public Map<String, Map<String, String>> getKpiRuleAndUnit(Set<String> s,
			String menuCode) {
		return this.mSqlHelper.queryKpiInfo(s, menuCode);
	}

	/**
	 * @return the menuInfo
	 */
	public Map<String, String> getMenuInfo() {
		return menuInfo;
	}

}
