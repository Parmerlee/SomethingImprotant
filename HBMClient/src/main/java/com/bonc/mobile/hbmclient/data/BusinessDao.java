package com.bonc.mobile.hbmclient.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.database.sqlite.SQLiteDatabase;

import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.DefaultEncrypt;
import com.bonc.mobile.hbmclient.util.LogUtil;
import com.bonc.mobile.hbmclient.util.StringUtil;

public class BusinessDao {

	public final static String DEFAULT_DATE = "1970";

	public List<Map<String, String>> getNewMenu() {
		String sql = "select a.menu_code from menu_add_info a left join scanstatistics b on a.menu_code=b.menu_code "
				+ "where a.menu_code in(select aa.menu_code from menu_info aa left join menu_add_info bb on aa.menu_code=bb.menu_code "
				+ "where bb.startTime is not null and bb.startTime !='' and bb.endTime is not null and bb.endTime !='') "
				+ "and (((b.newestTime is null or b.newestTime='') and a.endTime>strftime('%Y%m%d','now','localtime')) "
				+ "or ((b.newestTime is not null and b.newestTime !='') "
				+ "and b.newestTime<a.startTime and a.endTime>strftime('%Y%m%d','now','localtime')))";
		return new SQLHelper().queryForList(sql, null);
	}

	/**
	 * 获取一级菜单. 可以获取一个或所有的 。
	 * 
	 * @return
	 */
	public List<Map<String, String>> getMenuFisrt(String menuCode) {
		String sql = "Select menu_name as menuName,menu_order as menuOrder, "
				+ " view_dim_flag as viewDimFlag,menu_explain as menuExpalin,data_type as dataType, "
				+ " menu_type as menuType ,menu_code  as menuCode, menu_level as menuLevel,DATA_TABLE as dataTable,LAST_UPDATE_TIME AS lastUpdateTime "
				+ " From MENU_INFO where menu_level='1' ";

		if (menuCode != null && !"".equals(menuCode)) {
			sql += " and menu_code='" + menuCode + "'";
		}

		sql += " order by  cast(menu_order as number) asc";

		List<Map<String, String>> resultList = new SQLHelper().queryForList(
				sql, null);

		if (resultList == null) {
			resultList = new ArrayList<Map<String, String>>(0);
		}

		return resultList;
	}

	/**
	 * 按分类查询一级菜单
	 */

	public List<Map<String, String>> getMenuFisrtByKind(String menuKind) {

		String sql = "Select  menu_change as menuChange, menu_name as menuName,menu_order as menuOrder, "
				+ " view_dim_flag as viewDimFlag,menu_explain as menuExpalin,data_type as dataType, "
				+ " menu_type as menuType ,i.menu_code  as menuCode, menu_level as menuLevel,DATA_TABLE as dataTable,"
				+ " LAST_UPDATE_TIME AS lastUpdateTime,ma.img_id as imgId "
				+ " From  MENU_INFO i left join menu_add_info ma on i.menu_code=ma.menu_code where menu_level='1'  and i.menu_code=ma.[MENU_CODE] and menu_kind = ? "
				+ " order by  cast(menu_order as integer) asc";

		List<Map<String, String>> resultList = new SQLHelper().queryForList(
				sql, new String[] { menuKind });

		if (resultList == null) {
			resultList = new ArrayList<Map<String, String>>(0);
		}

		return resultList;

	}

	/**
	 * 按分类查询侧边栏数据
	 */

	public List<Map<String, String>> getSideMenuData(String menuKind,
			String menuCode) {

		String sql = "Select menu_name as menuName,menu_order as menuOrder, "
				+ " view_dim_flag as viewDimFlag,menu_explain as menuExpalin,data_type as dataType, "
				+ " menu_type as menuType ,i.menu_code  as menuCode, menu_level as menuLevel,DATA_TABLE as dataTable,"
				+ " LAST_UPDATE_TIME AS lastUpdateTime,ma.img_id as imgId "
				+ " From  MENU_INFO i,menu_add_info ma where menu_level='1'  and i.menu_code=ma.[MENU_CODE] and menu_kind = ? and menuCode != ?"
				+ " order by  cast(menu_order as integer) asc";

		List<Map<String, String>> resultList = new SQLHelper().queryForList(
				sql, new String[] { menuKind, menuCode });

		if (resultList == null) {
			resultList = new ArrayList<Map<String, String>>(0);
		}

		return resultList;

	}

	/**
	 * 根据menuCode查询菜单信息
	 */
	public Map<String, String> getMenuInfo(String menuCode) {
		String sql = "Select menu_name as menuName,menu_order as menuOrder, "
				+ " view_dim_flag as viewDimFlag,menu_explain as menuExpalin,data_type as dataType, "
				+ " menu_type as menuType ,menu_code  as menuCode, menu_level as menuLevel,DATA_TABLE as dataTable,LAST_UPDATE_TIME AS lastUpdateTime "
				+ " From MENU_INFO where menu_code = ? limit 1 ";

		Map<String, String> resultMap = new SQLHelper().queryForMap(sql,
				new String[] { menuCode });

		return resultMap;
	}

	public String getMenuName(String menuCode) {
		return getMenuInfo(menuCode).get("menuName");
	}

	public String getParentMenuCode(String menuCode) {
		String sql = "select parent_menu_code from menu_info where menu_code=?";
		Map<String, String> result = new SQLHelper().queryForMap(sql,
				new String[] { menuCode });
		return result.get("PARENT_MENU_CODE");
	}

	public String getMenuKind(String menuCode) {
		String sql = "select menu_kind from menu_info where menu_code=?";
		Map<String, String> result = new SQLHelper().queryForMap(sql,
				new String[] { menuCode });
		return result.get("menu_kind");
	}

	public Map<String, String> getMenuStateInfo(String menuCode) {
		String sql = "select a.menu_code,a.menu_name,parent_menu_code, show_daily_report   from menu_info a left join menu_add_info b on a.menu_code=b.menu_code where a.menu_code=? ";
		Map<String, String> result = new SQLHelper().queryForMap(sql,
				new String[] { menuCode });
		return result;
	}

	public Map<String, String> getDailyReportFlag(String menuCode) {
		String sql = "select show_daily_report from MENU_ADD_INFO where menu_code=? limit 1";
		Map<String, String> resultMap = new SQLHelper().queryForMap(sql,
				new String[] { menuCode });
		return resultMap;
	}

	/**
	 * 获取多个menu的信息
	 * 
	 * @param menuCode
	 * @return
	 */
	public List<Map<String, String>> getMenuInfo(String[] menuCodes) {
		if (menuCodes == null || menuCodes.length == 0) {
			return null;
		}

		String tmpString = "";
		for (int i = 0; i < menuCodes.length; i++) {
			if (StringUtil.isNull(menuCodes[i])) {
				continue;
			}
			if ("".equals(tmpString)) {
				tmpString += "'" + menuCodes[i] + "'";
			} else {
				tmpString += ",'" + menuCodes[i] + "'";
			}
		}

		String sql = "Select menu_name as menuName,menu_order as menuOrder, "
				+ " view_dim_flag as viewDimFlag,menu_explain as menuExpalin,data_type as dataType, "
				+ " menu_type as menuType ,menu_code  as menuCode, menu_level as menuLevel,DATA_TABLE as dataTable,LAST_UPDATE_TIME AS lastUpdateTime "
				+ " From MENU_INFO where menu_code in(" + tmpString + ")";

		List<Map<String, String>> resultMap = new SQLHelper().queryForList(sql,
				null);

		return resultMap;
	}

	/**
	 * 查询二级菜单
	 */
	public List<Map<String, String>> getMenuSecond(String parentCode) {
		String sql = "Select menu_change as menuChange,menu_name as menuName,menu_order as menuOrder, "
				+ "view_dim_flag as viewDimFlag,menu_explain as menuExpalin, "
				+ "menu_type as menuType ,MENU_INFO.menu_code  as menuCode, menu_level as menuLevel, "
				+ "DATA_TABLE as dataTable,data_type as dataType,LAST_UPDATE_TIME AS lastUpdateTime "
				+ "From MENU_INFO left join MENU_ADD_INFO on MENU_INFO.menu_code = MENU_ADD_INFO.menu_code where menu_level='2' AND PARENT_MENU_CODE=? "
				+ "order by  cast(menu_order as number) asc";

		List<Map<String, String>> resultList = new SQLHelper().queryForList(
				sql, new String[] { parentCode });

		if (resultList == null) {
			resultList = new ArrayList<Map<String, String>>(0);
		}

		return resultList;
	}

	public List<Map<String, String>> getSecondMenuType(String parentCode) {
		String sql = "select menu_code as menuCode, menu_type as menuType from MENU_INFO where parent_menu_code=?";
		return new SQLHelper().queryForList(sql, new String[] { parentCode });
	}

	public List<Map<String, String>> getDateType(String menuCode) {
		String sql = "select menu_code,date_type from MENU_INFO where parent_menu_code=?";
		return new SQLHelper().queryForList(sql, new String[] { menuCode });
	}

	/**
	 * 查询三级菜单
	 */
	public List<Map<String, String>> getMenuThrid(String parentCode) {
		String sql = "Select menu_name as menuName,menu_order as menuOrder, "
				+ "view_dim_flag as viewDimFlag,menu_explain as menuExpalin, "
				+ "menu_type as menuType ,menu_code  as menuCode, menu_level as menuLevel, "
				+ "DATA_TABLE as dataTable,data_type as dataType,LAST_UPDATE_TIME AS lastUpdateTime "
				+ "From MENU_INFO where menu_level='3' and PARENT_MENU_CODE=? "
				+ "order by  cast(menu_order as number) asc";

		List<Map<String, String>> resultList = new SQLHelper().queryForList(
				sql, new String[] { parentCode });

		if (resultList == null) {
			resultList = new ArrayList<Map<String, String>>(0);
		}

		return resultList;
	}

	/**
	 * 查询菜单的父菜单的i选内息
	 * 
	 * @param menuCode
	 * @return
	 */
	public Map<String, String> getMenuParentInfo(String menuCode) {

		String sql = " Select b.[MENU_NAME] as menuName,b.[MENU_CODE] as menuCode,b.menu_type as menuType From MAIN.[MENU_INFO] a, MENU_INFO b"
				+ " where  a.[MENU_CODE] = ?"
				+ " and  b.[MENU_CODE] = a.[PARENT_MENU_CODE] limit 1";

		return new SQLHelper().queryForMap(sql, new String[] { menuCode });

	}

	/**
	 * 获取菜单的额外信息
	 */
	public Map<String, String> getMenuAddInfo(String menu_code) {
		String sql = "select menu_code as menuCode ,show_thrend_flag as showThrendFlag ,show_area_level as showAreaLevel "
				+ " ,FLUCTUATEST_COLUMN as fluctuatestColumn,HIGHEST_COLUMN as highestColumn,HIGHEST_DESC as highestDesc,"
				+ " FLUCTUATEST_DESC as fluctuatestDesc,SHOW_TOP5_FLAG as showTop5Flag,TOP5_ORDER_COLS top5OrderCols,TOP5_ORDER_NAMES AS top5OrderNames"
				+ " ,img_id as imgId"
				+ " from MENU_ADD_INFO where menu_code = ? limit 1";
		return new SQLHelper().queryForMap(sql, new String[] { menu_code });
	}

	/**
	 * 更新菜单数据的最后检查日期
	 */

	public void updateLastTime(final String updatetime, final String menuCode) {

		new SQLHelper().doDBOperateWithTransation(new DatabaseCallBack() {

			@Override
			public boolean doCallBack(SQLiteDatabase database) {
				String updateLastTimeSQL = "update menu_info set last_update_time = ? where menu_code= ?";
				database.execSQL(updateLastTimeSQL, new String[] { updatetime,
						menuCode });
				return true;
			}
		});

		/*
		 * SQLiteDatabase database = new SQLHelper().getDatabase(); String
		 * updateLastTimeSQL =
		 * "update menu_info set last_update_time = ? where menu_code= ?"; try{
		 * database.beginTransaction(); database.execSQL(updateLastTimeSQL, new
		 * String[]{updatetime,menuCode}); database.setTransactionSuccessful();
		 * }catch (Exception e) { e.printStackTrace(); }finally{ try{
		 * if(database != null){ database.endTransaction(); database.close(); }
		 * }catch (Exception e) { e.printStackTrace(); } }
		 */
	}

	/**
	 * 重置所有菜单的数据最后检查日期
	 */
	/*
	 * public void resetAllLastTime(){
	 * 
	 * SQLiteDatabase database = new SQLHelper().getDatabase(); String
	 * updateLastTimeSQL = "update menu_info set last_update_time = null "; try{
	 * database.beginTransaction(); database.execSQL(updateLastTimeSQL);
	 * database.setTransactionSuccessful(); }catch (Exception e) {
	 * e.printStackTrace(); }finally{ try{ if(database != null){
	 * database.endTransaction(); database.close(); } }catch (Exception e) {
	 * e.printStackTrace(); } }
	 * 
	 * }
	 */

	/**
	 * 获取二级菜单下面的数据日期
	 * 
	 * @return
	 */
	public String getMenuDataBetween(String menuCode, String tableName,
			String dataType) {

		String sql = "select min(op_time) as mindate ,max(op_time) as maxDate from  "
				+ tableName + "  where menu_code = ? ";

		Map<String, String> result = new SQLHelper().queryForMap(sql,
				new String[] { menuCode });

		if (result == null) {
			return "";
		}

		if (result.get("mindate") == null || "".equals(result.get("mindate"))) {
			return "";
		}

		if (dataType.equalsIgnoreCase(Constant.DATA_TYPE_DAY)) {
			return DateUtil.oneStringToAntherString(result.get("mindate"),
					"yyyyMMdd", "yyyy年MM月dd日")
					+ "--"
					+ DateUtil.oneStringToAntherString(result.get("maxDate"),
							"yyyyMMdd", "yyyy年MM月dd日");
		} else {
			return DateUtil.oneStringToAntherString(result.get("mindate"),
					"yyyyMM", "yyyy年MM月")
					+ "--"
					+ DateUtil.oneStringToAntherString(result.get("maxDate"),
							"yyyyMM", "yyyy年MM月");
		}

	}

	/**
	 * 获取某个菜单的最大值或最小值
	 */
	public String getMenuMinOrMaxDate(String menuCode, String tableName,
			int type) {

		// 1代表取最小值 . 2代表最小值
		String sql = "select ";
		if (type == 1) {
			sql += "min(op_time)";
		} else {
			sql += "max(op_time)";
		}
		sql += " as date from  " + tableName + "  where menu_code = ? ";
		Map<String, String> result = new SQLHelper().queryForMap(sql,
				new String[] { menuCode });

		if (result == null || result.get("date") == null) {
			return DEFAULT_DATE;
		}

		return result.get("date");

	}

	/**
	 * 获取数据菜单要展示的列.
	 * 
	 * @param menuCode
	 * @return
	 */
	public String getMenuAllColumnString(String menuCode) {
		// String sql =
		// "select RELATION_KPIVALUE_COLUMN  as col from MENU_KPI_COL_REL where RESOURCE_CODE=? order by show_order asc";

		List<Map<String, String>> result = getMenuAllColumnList(menuCode);

		if (result == null || result.size() == 0) {
			return null;
		}
		String cols = "";
		int len = result.size();
		for (int i = 0; i < len; i++) {
			cols += result.get(i).get("col").toLowerCase(Locale.CHINA);
			if (i < len - 1)
				cols += Constant.DEFAULT_CONJUNCTION;
		}
		return cols;
	}

	/**
	 * 获取所有列.
	 * 
	 * @param menuCode
	 * @return
	 */
	public List<Map<String, String>> getMenuAllColumnList(String menuCode) {
		String sql = "select RELATION_KPIVALUE_COLUMN_NAME as colname , RELATION_KPIVALUE_COLUMN  as col ,KPIVALUE_UNIT unit,KPIVALUE_RULE rule,resource_code as menucode from MENU_KPI_COL_REL where RESOURCE_CODE=? order by show_order asc";

		List<Map<String, String>> result = new SQLHelper().queryForList(sql,
				new String[] { menuCode });

		return result;
	}

	public String[] getColTitleList(String menuCode) {
		List<Map<String, String>> allColumnList = getMenuAllColumnList(menuCode);
		int len = allColumnList.size();
		String[] ret = new String[len];
		for (int i = 0; i < len; i++) {
			ret[i] = allColumnList.get(i).get("colname")
					.toLowerCase(Locale.CHINA);
		}
		return ret;
	}

	public Map<String, MenuColumnInfo> getColInfoMap(String menuCode) {
		Map<String, MenuColumnInfo> menuColInfoMap = new HashMap<String, MenuColumnInfo>();
		List<Map<String, String>> allColumnList = getMenuAllColumnList(menuCode);
		int len = allColumnList.size();
		for (int i = 0; i < len; i++) {
			String col = allColumnList.get(i).get("col")
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
		return menuColInfoMap;
	}

	/**
	 * 获取菜单所有的指标. 用 | 分割
	 */
	public String getMenuAllKpiString(String menuCode) {

		List<Map<String, String>> kpiCodeList = getMenuAllKpiList(menuCode);

		if (kpiCodeList == null || kpiCodeList.size() == 0) {
			return null;
		}
		String kpiString = "";
		int len = kpiCodeList.size();
		for (int i = 0; i < len; i++) {
			kpiString += kpiCodeList.get(i).get("kpi_code");
			if (i < len - 1)
				kpiString += Constant.DEFAULT_CONJUNCTION;
		}

		return kpiString;
	}

	/**
	 * 获取所有的kpi列表
	 */
	public List<Map<String, String>> getMenuAllKpiList(String menuCode) {

		if (menuCode == null || "".equals(menuCode)) {
			return null;
		}

		menuCode = "'" + menuCode + "'";
		String sql1 = "Select menu_code as menuCode From MENU_INFO where parent_menu_code in (";
		String sql2 = ")";
		while (true) {
			String sql = sql1 + menuCode + sql2;
			List<Map<String, String>> menuList = new SQLHelper().queryForList(
					sql, null);

			if (menuList == null || menuList.size() == 0) {
				break;
			}
			menuCode = "";
			int len = menuList.size();
			for (int i = 0; i < len; i++) {
				menuCode += "'" + menuList.get(i).get("menuCode") + "'";
				if (i < len - 1) {
					menuCode += ",";
				}
			}

		}

		/*
		 * sql1=
		 * "Select r.RESOURCE_CODE as kpi_code,r.menu_code as menu_code ,m.menu_name as menu_name "
		 * +
		 * " From KPI_MENU_REL r,menu_info m where  r.menu_code=m.menu_code and  r.menu_code in ("
		 * +menuCode+")";
		 */

		sql1 = " Select RESOURCE_CODE as kpi_code ,m.menu_code as menu_code,m.menu_name as menu_name,k.kpi_unit as kpiUnit,"
				+ " kpi_cal_rule as rule ,kpi_name as kpiName,k.kpi_define as kpi_define "
				+ " From KPI_MENU_REL r,kpi_info k ,menu_info m "
				+ " where r.[RESOURCE_CODE] = k.[KPI_CODE]  and r.menu_code=m.menu_code and  m.menu_code in ("
				+ menuCode
				+ ")"
				+ " order by  m.menu_order asc , k.KPI_ORDER asc";

		List<Map<String, String>> kpiCodeList = new SQLHelper().queryForList(
				sql1, null);

		return kpiCodeList;
	}

	/**
	 * 获取菜单所有的指标. 用 | 分割
	 */
	public String getMenuAllKpiString2(String menuCode) {

		List<Map<String, String>> kpiCodeList = getKpiOrderList(menuCode);

		if (kpiCodeList == null || kpiCodeList.size() == 0) {
			return null;
		}
		String kpiString = "";
		int len = kpiCodeList.size();
		for (int i = 0; i < len; i++) {
			kpiString += kpiCodeList.get(i).get("kpi_code");
			if (i < len - 1)
				kpiString += Constant.DEFAULT_CONJUNCTION;
		}

		return kpiString;
	}

	public List<Map<String, String>> getKpiOrderList(String menuCode) {

		if (menuCode == null || "".equals(menuCode)) {
			return null;
		}

		menuCode = "'" + menuCode + "'";
		String sql1 = "Select menu_code as menuCode From MENU_INFO where parent_menu_code in (";
		String sql2 = ")";
		while (true) {
			String sql = sql1 + menuCode + sql2;
			List<Map<String, String>> menuList = new SQLHelper().queryForList(
					sql, null);

			if (menuList == null || menuList.size() == 0) {
				break;
			}
			menuCode = "";
			int len = menuList.size();
			for (int i = 0; i < len; i++) {
				menuCode += "'" + menuList.get(i).get("menuCode") + "'";
				if (i < len - 1) {
					menuCode += ",";
				}
			}

		}

		/*
		 * sql1=
		 * "Select r.RESOURCE_CODE as kpi_code,r.menu_code as menu_code ,m.menu_name as menu_name "
		 * +
		 * " From KPI_MENU_REL r,menu_info m where  r.menu_code=m.menu_code and  r.menu_code in ("
		 * +menuCode+")";
		 */

		sql1 = " Select RESOURCE_CODE as kpi_code ,m.menu_code as menu_code,m.menu_name as menu_name,k.kpi_unit as kpiUnit,"
				+ " kpi_cal_rule as rule ,kpi_name as kpiName,k.kpi_define as kpi_define "
				+ " From KPI_MENU_REL r,kpi_info k ,menu_info m "
				+ " where r.[RESOURCE_CODE] = k.[KPI_CODE]  and r.menu_code=m.menu_code and  m.menu_code in ("
				+ menuCode
				+ ")"
				+ " order by r.group_order asc,r.KPI_ORDER asc";

		List<Map<String, String>> kpiCodeList = new SQLHelper().queryForList(
				sql1, null);

		return kpiCodeList;
	}

	/**
	 * 获取指标和列的对应关系
	 */
	public Map<String, String> getKpiMenuRelation(String menuCode) {

		if (menuCode == null || "".equals(menuCode)) {
			return null;
		}

		menuCode = "'" + menuCode + "'";
		String sql1 = "Select menu_code as menuCode From MENU_INFO where parent_menu_code in (";
		String sql2 = ")";
		while (true) {
			String sql = sql1 + menuCode + sql2;
			List<Map<String, String>> menuList = new SQLHelper().queryForList(
					sql, null);

			if (menuList == null || menuList.size() == 0) {
				break;
			}
			menuCode = "";
			int len = menuList.size();
			for (int i = 0; i < len; i++) {
				menuCode += "'" + menuList.get(i).get("menuCode") + "'";
				if (i < len - 1) {
					menuCode += ",";
				}
			}

		}

		sql1 = "Select RESOURCE_CODE as kpiCode,menu_code as menuCode From KPI_MENU_REL where menu_code in ("
				+ menuCode + ")";
		/*
		 * sql1=
		 * " Select RESOURCE_CODE as kpiCode,menu_code as menuCode,k.kpi_unit as kpiUnit,kpi_cal_rule as rule ,kpi_name as kpiName From KPI_MENU_REL r,kpi_info k"
		 * +
		 * " where r.[RESOURCE_CODE] = k.[KPI_CODE] and menu_code in ("+menuCode
		 * +")";
		 */
		List<Map<String, String>> kpiCodeList = new SQLHelper().queryForList(
				sql1, null);

		if (kpiCodeList == null) {
			LogUtil.error(this.getClass().toString(), "获取指标和菜单的对应关系出错. ");

		}
		Map<String, String> resultMap = new HashMap<String, String>();
		int len = kpiCodeList.size();
		for (int i = 0; i < len; i++) {
			resultMap.put(kpiCodeList.get(i).get("kpiCode"), kpiCodeList.get(i)
					.get("menuCode"));
		}

		return resultMap;
	}

	/**
	 * 获取指标和指标名称的关系
	 */
	public Map<String, String> getKpiMenuNameRelation(String menuCode) {

		if (menuCode == null || "".equals(menuCode)) {
			return null;
		}

		menuCode = "'" + menuCode + "'";
		String sql1 = "Select menu_code as menuCode From MENU_INFO where parent_menu_code in (";
		String sql2 = ")";
		while (true) {
			String sql = sql1 + menuCode + sql2;
			List<Map<String, String>> menuList = new SQLHelper().queryForList(
					sql, null);

			if (menuList == null || menuList.size() == 0) {
				break;
			}
			menuCode = "";
			int len = menuList.size();
			for (int i = 0; i < len; i++) {
				menuCode += "'" + menuList.get(i).get("menuCode") + "'";
				if (i < len - 1) {
					menuCode += ",";
				}
			}

		}

		// sql1 =
		// "Select RESOURCE_CODE as kpiCode,menu_code as menuCode From KPI_MENU_REL where menu_code in ("+menuCode+")";

		sql1 = "Select RESOURCE_CODE as kpiCode,m.menu_name as menuName "
				+ " From KPI_MENU_REL r,menu_info m  where r.menu_code = m.menu_code and  r.menu_code in ("
				+ menuCode + ")";

		List<Map<String, String>> kpiCodeList = new SQLHelper().queryForList(
				sql1, null);

		if (kpiCodeList == null) {
			LogUtil.error(this.getClass().toString(), "获取指标和菜单名称的对应关系出错. ");

		}
		Map<String, String> resultMap = new HashMap<String, String>();
		int len = kpiCodeList.size();
		for (int i = 0; i < len; i++) {
			resultMap.put(kpiCodeList.get(i).get("kpiCode"), kpiCodeList.get(i)
					.get("menuName"));
		}

		return resultMap;
	}

	/**
	 * 模糊查询kpi
	 */

	public List<Map<String, String>> qryKpiInfoByName(String kpiName) {
		String sql = "select  kpi_code as kpiCode , kpi_name as kpiName,kpi_desc as kpiDesc from  kpi_info ki,KPI_MENU_REL kmr,menu_info mi where ki.kpi_code = kmr.resource_code and kmr.menu_code = mi.MENU_CODE and ki.[KPI_NAME] like '%"
				+ kpiName + "%'";

		return new SQLHelper().queryForList(sql, null);

	}

	/**
	 * 通过kpi_code 查询
	 */
	public Map<String, String> qryKpiInfoByCode(String kpiCode) {
		String sql = "select  kpi_code as kpiCode , kpi_name as kpiName,kpi_desc as kpiDesc,kpi_define as kpiDefine from  kpi_info ki where ki.[KPI_code] = '"
				+ kpiCode + "' limit 1";

		return new SQLHelper().queryForMap(sql, null);

	}

	/**
	 * 查询指标归属的一级和二级菜单的名称.
	 */
	public Map<String, List<Map<String, String>>> qryKpiMenusBykpiName(
			String kpiName) {
		Map<String, List<Map<String, String>>> resultMap = new HashMap<String, List<Map<String, String>>>();
		String sql = " Select ki.[KPI_CODE] as kpiCode,mi.menu_code as menuCode,"
				+ " mi.menu_name as menuName,mi.[MENU_LEVEL] as menuLevel"
				+ " From MAIN.[KPI_MENU_REL] kmr,menu_info mi,kpi_info ki where"
				+ " ki.kpi_code = kmr.resource_code "
				+ " and kmr.menu_code = mi.[MENU_CODE]  "
				+ " and ki.[KPI_NAME] like '%"
				+ kpiName
				+ "%' "
				+ " order by kmr.[resource_CODE] asc , mi.[MENU_ORDER] asc";

		List<Map<String, String>> qryList = new SQLHelper().queryForList(sql,
				null);

		if (qryList != null && qryList.size() > 0) {
			for (int i = 0; i < qryList.size(); i++) {
				Map<String, String> tempMap = qryList.get(i);
				String kpiCode = tempMap.get("kpiCode");
				if (resultMap.get(kpiCode) == null) {
					List<Map<String, String>> menuList = new ArrayList<Map<String, String>>();
					menuList.add(tempMap);
					resultMap.put(kpiCode, menuList);
				} else {
					List<Map<String, String>> menuList = resultMap.get(kpiCode);
					if (menuList == null) {
						menuList = new ArrayList<Map<String, String>>();
					}
					menuList.add(tempMap);
				}
			}
		}

		return resultMap;

	}

	/**
	 * 获取所有的维度.
	 */
	public List<Map<String, String>> getDimInfo(String dimCode) {
		String sql = "Select dim_code as dimCode,dim_name as dimname,dim_order as dimOrder,dim_presix as dimPresix From DIM_INFO where dim_order > 2 ";
		if (dimCode != null && !"".equals(dimCode)) {
			sql += " and dim_code='" + dimCode + "' ";
		}
		sql += " order by dim_order asc";
		return new SQLHelper().queryForList(sql, null);
	}

	/***
	 * 获取某个维度下的所有维值
	 */
	public List<Map<String, String>> getDimValue(String dimCode) {
		String sql = "Select dim_value_id  as dimvalueid , dim_value_name as dimvaluename,dim_code as dimcode,show_order as showOrder"
				+ " From DIM_VALUE_INFO   where dim_code= ? order by show_order asc  ";
		return new SQLHelper().queryForList(sql, new String[] { dimCode });
	}

	/**
	 * 获取菜单所有的维度.
	 */
	public List<Map<String, String>> getMenuDim(String menuCode) {
		String sql = " Select m.menu_code as menuCode,d.dim_code as dimCode, d.dim_name as  dimName,dim_order as dimOrder,dim_presix as dimPresix"
				+ " From DIM_MENU_REL M,DIM_INFO D where  M.RESOURCE_CODE = D.DIM_CODE AND m.menu_code = ?"
				+ " order by d.DIM_ORDER ";

		return new SQLHelper().queryForList(sql, new String[] { menuCode });

	}

	/**
	 * 查询指标显示的列的数据存储的 表 server端使用.
	 */
	public String getMenuColDataTable(String menuCode) {
		String sql = "select min(RELATION_KPIVALUE_TABLE)  as dataTable from MENU_KPI_COL_REL where RESOURCE_CODE=? ";

		Map<String, String> result = new SQLHelper().queryForMap(sql,
				new String[] { menuCode });

		if (result == null || result.size() == 0) {
			return null;
		}

		return result.get("dataTable");
	}

	/**
	 * 获取用户信息
	 */
	public Map<String, String> getUserInfo() {
		String sql = "Select  user_code as usercode,user_area_id as areaId,authority_time as authorityTime  from USER_INFO limit 1";

		Map<String, String> result = new SQLHelper().queryForMap(sql, null);

		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				result.put("usercode",
						DefaultEncrypt.decrypt(result.get("usercode")));
				result.put("areaId",
						DefaultEncrypt.decrypt(result.get("areaId")));
			} else {
				result.put("usercode", result.get("usercode"));
				result.put("areaId", result.get("areaId"));
			}

		}

		return result;
	}

	/**
	 * 获取地区及其下面的子地区
	 */

	public List<Map<String, String>> getAreaInfo(String areaId) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder
				.append("select area_code as areaCode,area_name as areaName,parent_area_code as parentAreaCode,")
				.append(" area_order as areaOrder,area_level as areaLevel,area_feature as areaFeature")
				.append(" from (Select * ")
				.append(" from  DIM_AREA_CODE   where parent_area_code= ?")
				.append(" union ").append(" Select *")
				.append(" from  DIM_AREA_CODE   where  area_code  = ? )")
				.append(" order by  area_order asc");

		return new SQLHelper().queryForList(sqlBuilder.toString(),
				new String[] { areaId, areaId });

	}

	/**
	 * 查询地区基本信息.
	 * 
	 * @param areaId
	 * @return
	 */
	public Map<String, String> getAreaBaseInfo(String areaId) {

		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder
				.append("select area_code as areaCode,area_name as areaName,parent_area_code as parentAreaCode,")
				.append(" area_order as areaOrder,area_level as areaLevel,area_feature as areaFeature")
				.append(" from  DIM_AREA_CODE   where  area_code  = ? limit 1");

		return new SQLHelper().queryForMap(sqlBuilder.toString(),
				new String[] { areaId });

	}

	/**
	 * 获取菜单的复合维度. 如果菜单是二级菜单 。 同时这个二级菜单又有三级菜单 又要按某个维度展开查看. 这种情况下会有冲突,认为是配置错误，
	 * 不同步数据. 如果菜单是一级或者是二级菜单在没有三级菜单的情况下 可以按本菜单的第三维度展开. 如果不需要按第三维度展开 则按汇总拼出符合维度
	 * 
	 * @param menu
	 */
	public ComplexDimInfo getMenuComplexDimKey(Map<String, String> menu) {

		ComplexDimInfo complexDimInfo = new ComplexDimInfo();
		if (menu == null || menu.get("menuLevel") == null
				|| "3".equalsIgnoreCase(menu.get("menuLevel"))) {
			LogUtil.info(this.getClass().toString(), "参数为空!或者菜单为三级菜单");
			return null;
		}

		String menuLevel = menu.get("menuLevel");
		String menuCode = menu.get("menuCode");
		String menuName = menu.get("menuMenu");
		String viewDimFlag = menu.get("viewDimFlag");
		boolean hasThirdMenu = false;

		List<Map<String, String>> thridMenuList = getMenuThrid(menuCode);
		// 判斷是否存在 三級菜單.
		if (thridMenuList != null && thridMenuList.size() > 0) {
			hasThirdMenu = true;
			complexDimInfo.setHasGroup(true);
		}

		if (hasThirdMenu && "1".equals(viewDimFlag)) {
			LogUtil.error(this.getClass().toString(), "菜单[" + menuName
					+ "]配置错误，存在三级菜单. 并且要求按第三维度展开. 目前不支持这样的需求.");
			complexDimInfo.setIsMenuCofingRight("error");
			return complexDimInfo;
		}

		List<Map<String, String>> allDimList = getDimInfo(null);
		List<Map<String, String>> menuDimList = getMenuDim(menuCode);

		if (allDimList == null || allDimList.size() == 0) {
			LogUtil.info(this.getClass().toString(), "没有第三维度");
			return null;
		}

		int len = allDimList.size();
		String complexDimKey = "";
		// 如果为1级菜单且不按维度展开. 或者为2级菜单且不按维度展开 或者为2级别菜单 且存在三级菜单的情况下. 拼汇总的复合维度
		if (("1".equals(menuLevel) && !"1".equals(viewDimFlag))
				|| ("2".equals(menuLevel) && !"2".equals(viewDimFlag))) {

			for (int i = 0; i < len; i++) {
				String dimcode = allDimList.get(i).get("dimCode");
				String presix = allDimList.get(i).get("dimPresix");
				if (!"".equals(complexDimKey)) {
					complexDimKey += "@";
				}

				// 如果菜单没有配置维度. 则拼 -1
				if (menuDimList == null || menuDimList.size() == 0) {
					complexDimKey += (presix + Constant.NULL_DIM_CODE);
					continue;
				}

				int k = 0;
				for (; k < menuDimList.size(); k++) {

					if (dimcode.equals(menuDimList.get(k).get("dimCode"))) { // 如果存在此维度
																				// 则获取维度的汇总维
						List<Map<String, String>> dimvaluList = getDimValue(dimcode);

						if (dimvaluList == null || dimvaluList.size() == 0) {
							complexDimKey += (presix + Constant.NULL_DIM_CODE);
						} else {
							complexDimKey += (presix + dimvaluList.get(0).get(
									"dimvalueid"));
						}

						break;
					}
				}
				// 以下说明没找到菜单下面不存在这个维度. 则拼一个 -1
				if (k == menuDimList.size()) {
					complexDimKey += (presix + Constant.NULL_DIM_CODE);
				}
			}
			complexDimInfo.setComplexDimKeyString(complexDimKey);
			return complexDimInfo;
		}

		// 如果需要按维度展开. 则获取应展开的维度. 地区维不展开. 按照排序取非地区维的第一个维度

		if (menuDimList == null || menuDimList.size() == 0) {
			LogUtil.info(BusinessDao.class.toString(), "拼复合维度时,菜单没有找到相应的维度");
			complexDimInfo.setIsMenuCofingRight("error");
			return complexDimInfo;
		}

		// 取第一个维度. 判断是否是地区维度. 地区维度不支持展开 .
		// 如果第一个不是地区维度 则认为是可展开的维度 如果第一个是地区维度 则取下一个维度.
		Map<String, String> menuDim = menuDimList.get(0);

		if ("2".equals(menuDim.get("dimOrder"))) {
			if (menuDimList.size() >= 2) {
				menuDim = menuDimList.get(1);
			} else {
				LogUtil.error(this.getClass().toString(), "菜单配置错误,没有找到可以展开的维度,");
				complexDimInfo.setIsMenuCofingRight("error");
				return complexDimInfo;
			}
		} else {
			menuDim = menuDimList.get(0);
		}

		// 设置是按第三维度展开.
		complexDimInfo.setDimExpand(true);
		complexDimInfo.setHasGroup(true);
		// 找到要展开的维度.
		String thirdDimCode = menuDim.get("dimCode");
		List<Map<String, String>> dimvList = getDimValue(thirdDimCode);

		// 找到符合维度样式.
		for (int i = 0; i < len; i++) {
			String dimcode = allDimList.get(i).get("dimCode");
			String presix = allDimList.get(i).get("dimPresix");
			if (!"".equals(complexDimKey)) {
				complexDimKey += "@";
			}

			int k = 0;
			for (; k < menuDimList.size(); k++) {

				if (dimcode.equals(thirdDimCode)) {
					complexDimKey += (presix + "[-]");
					break;
				}

				if (dimcode.equals(menuDimList.get(k).get("dimCode"))) { // 如果存在此维度
																			// 则获取维度的汇总维
					List<Map<String, String>> dimvaluList = getDimValue(dimcode);

					if (dimvaluList == null || dimvaluList.size() == 0) {
						complexDimKey += (presix + Constant.NULL_DIM_CODE);
					} else {
						complexDimKey += (presix + dimvaluList.get(0).get(
								"dimvalueid"));
					}

					break;
				}
			}
			// 以下说明没找到菜单下面不存在这个维度. 则拼一个 -1
			if (k == menuDimList.size()) {
				complexDimKey += (presix + Constant.NULL_DIM_CODE);
			}
		}

		len = dimvList.size();
		String result = "";
		for (int i = 0; i < len; i++) {
			if (!"".equals(result)) {
				result += Constant.DEFAULT_CONJUNCTION;
			}

			String dimvalueid = dimvList.get(i).get("dimvalueid");
			String dimvaluename = dimvList.get(i).get("dimvaluename");
			String coplexdimString = complexDimKey.replace("[-]", dimvalueid);

			complexDimInfo.getComplexDimToExpandDim().put(coplexdimString,
					dimvalueid);
			complexDimInfo.getExpandDimToComplexDim().put(dimvalueid,
					coplexdimString);
			complexDimInfo.getExpandDimInfo().put(dimvalueid, dimvaluename);

			result += coplexdimString;

		}
		complexDimInfo.setComplexDimKeyString(result);
		return complexDimInfo;

	}

	/**
	 * 获取某个菜单下 按某个维度的维值 和其他维度进行复合 的对应关系. 在做按某个维度查询时使用.
	 */
	public Map<String, String> getSpecialDimComplexKey(String menuCode,
			String specialDimCode) {

		List<Map<String, String>> allDimList = getDimInfo(null);
		if (allDimList == null || allDimList.size() == 0) {
			LogUtil.error(this.getClass().toString(), "没有第三种以后的维度.返回");
			return null;
		}

		List<Map<String, String>> menuDimList = getMenuDim(menuCode);
		if (menuDimList == null || menuDimList.size() == 0) {
			LogUtil.error(this.getClass().toString(), "菜单没有第三维度.返回");
			return null;
		}

		List<Map<String, String>> dimvList = getDimValue(specialDimCode);

		if (dimvList == null || dimvList.size() == 0) {
			LogUtil.error(this.getClass().toString(), "根据维度编码  查询的维值为空!返回");
			return null;
		}

		String complexDimKey = "";
		int len = allDimList.size();
		for (int i = 0; i < len; i++) {
			String dimcode = allDimList.get(i).get("dimCode");
			String presix = allDimList.get(i).get("dimPresix");
			if (!"".equals(complexDimKey)) {
				complexDimKey += "@";
			}

			int k = 0;
			for (; k < menuDimList.size(); k++) {

				if (dimcode.equals(specialDimCode)) {
					complexDimKey += presix + "[-]";
					break;
				}

				if (dimcode.equals(menuDimList.get(k).get("dimCode"))) { // 如果存在此维度
																			// 则获取维度的汇总维
					List<Map<String, String>> dimvaluList = getDimValue(dimcode);

					if (dimvaluList == null || dimvaluList.size() == 0) {
						// 找不到维值 则拼默认的值
						complexDimKey += presix + Constant.NULL_DIM_CODE;
					} else {
						// 第一个为默认的汇总的维值
						complexDimKey += (presix + dimvaluList.get(0).get(
								"dimvalueid"));
					}

					break;
				}
			}
			// 以下说明没找到菜单下面不存在这个维度. 则拼一个 -1
			if (k == menuDimList.size()) {
				complexDimKey += (presix + Constant.NULL_DIM_CODE);
			}
		}

		// 用维值替换掉里面的特殊字符.
		len = dimvList.size();
		Map<String, String> result = new HashMap<String, String>();
		for (int i = 0; i < len; i++) {
			String dimvalueid = dimvList.get(i).get("dimvalueid");
			result.put(dimvalueid, complexDimKey.replace("[-]", dimvalueid));
		}

		return result;

	}

	/**
	 * 查询菜单下面所的数据.
	 */
	public List<Map<String, String>> queryMenuBaseData(String menuCode,
			String optime) {

		if (optime == null || "".equals(optime)) {
			LogUtil.error(this.getClass().toString(), "optime 为空. 返回.");
			return null;
		}

		List<Map<String, String>> menuDimList = getMenuDim(menuCode);
		Map<String, String> menuInfo = getMenuInfo(menuCode);

		if (menuInfo == null) {
			LogUtil.error(this.getClass().toString(), "查询菜单信息出错。  返回.");
			return null;
		}

		String areaId = null;
		if (menuDimList == null || menuDimList.size() == 0) {
			LogUtil.debug("", "菜单没有第三维度");
		} else {
			String dimOrder = menuDimList.get(0).get("dimOrder");
			if ("2".equals(dimOrder)) {
				areaId = getUserInfo().get("areaId");
			}

		}
		// 获取符合维度
		// String complexDimKey = getMenuComplexDimKey(menuInfo);
		ComplexDimInfo complexDimKey = getMenuComplexDimKey(menuInfo);
		if ("error".equals(complexDimKey.getIsMenuCofingRight())) {
			return null;
		}
		// 获取菜单所有的指标.
		List<Map<String, String>> allKpiList = getMenuAllKpiList(menuCode);
		if (allKpiList == null || allKpiList.size() == 0) {
			LogUtil.error(this.getClass().toString(), "没有查到指标. 返回");
			return null;
		}

		// 获取所有列.
		List<Map<String, String>> allColumnList = getMenuAllColumnList(menuCode);
		if (allColumnList == null || allColumnList.size() == 0) {
			LogUtil.error(this.getClass().toString(), "没有查询到与菜单要展示的列.");
			return null;
		}

		List<Map<String, String>> data;

		try {

			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder
					.append("Select a.op_time as op_time, area_code as area_code, dim_key as dim_key ,")
					.append(" A.kpi_code as kpi_code, p.kpi_name as kpi_name ,p.kpi_define as kpi_define,");

			int len = allColumnList.size();
			for (int i = 0; i < len; i++) {
				String colkey = allColumnList.get(i).get("col")
						.toLowerCase(Locale.CHINA);
				sqlBuilder.append(colkey + " as " + colkey);
				if (i < len - 1)
					sqlBuilder.append(",");
			}

			sqlBuilder
					.append(" From " + menuInfo.get("dataTable")
							+ "  A , KPI_INFO p ")
					.append(" where  a.KPI_CODE = p.KPI_CODE")
					.append(" AND A.MENU_CODE ='" + menuCode + "'")
					.append(" AND A.OP_TIME  = '" + optime + "'");
			if (areaId != null && !"".equals(areaId)) {
				sqlBuilder.append(" AND AREA_CODE='" + areaId + "'");
			}

			sqlBuilder.append(" AND A.KPI_CODE in (");
			len = allKpiList.size();
			for (int i = 0; i < len; i++) {
				sqlBuilder
						.append("'" + allKpiList.get(i).get("kpi_code") + "'");
				if (i < len - 1)
					sqlBuilder.append(",");
			}

			sqlBuilder.append(")");

			sqlBuilder.append(" AND DIM_KEY IN (");

			String[] dimkeyArray = complexDimKey.getComplexDimKeyString()
					.split("\\" + Constant.DEFAULT_CONJUNCTION);

			len = dimkeyArray.length;
			for (int i = 0; i < len; i++) {
				sqlBuilder.append("'" + dimkeyArray[i] + "'");
				if (i < len - 1)
					sqlBuilder.append(",");
			}

			sqlBuilder.append(")");
			sqlBuilder.append(" order by  dim_key asc ,p.kpi_order asc");

			LogUtil.debug(this.getClass().toString(), sqlBuilder.toString());

			data = new SQLHelper().queryForList(sqlBuilder.toString(), null);

			if (data != null && data.size() > 0 && Constant.IS_DATA_ENCRYPT) {

				int rlen = data.size();
				int clen = allColumnList.size();
				for (int i = 0; i < rlen; i++) {
					Map<String, String> tmpMap = data.get(i);

					for (int ci = 0; ci < clen; ci++) {
						String colkey = allColumnList.get(ci).get("col")
								.toLowerCase(Locale.CHINA);
						tmpMap.put(colkey,
								DefaultEncrypt.decrypt(tmpMap.get(colkey)));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return data = null;
		}

		return data;

	}

	/**
	 * 查询趋势图数据
	 */
	public List<Map<String, String>> queryMenuKpiTrendData(String menuCode,
			String colkey, String starttime, String endtime) {

		if (starttime == null || "".equals(starttime) || endtime == null
				|| "".equals(endtime)) {
			LogUtil.debug(this.getClass().toString(),
					"starttime 或者 endtime 为空.");
			return null;
		}

		List<Map<String, String>> menuDimList = getMenuDim(menuCode);
		Map<String, String> menuInfo = getMenuInfo(menuCode);

		if (menuInfo == null) {
			LogUtil.error(this.getClass().toString(), "查询菜单信息出错。  返回.");
			return null;
		}

		String areaId = null;
		if (menuDimList == null || menuDimList.size() == 0) {
			LogUtil.debug("", "菜单没有第三维度");
		} else {
			String dimOrder = menuDimList.get(0).get("dimOrder");
			if ("2".equals(dimOrder)) {
				areaId = getUserInfo().get("areaId");
			}

		}
		// 获取符合维度
		ComplexDimInfo complexDimKey = getMenuComplexDimKey(menuInfo);
		if ("error".equals(complexDimKey.getIsMenuCofingRight())) {
			return null;
		}
		// 获取菜单所有的指标.
		List<Map<String, String>> allKpiList = getMenuAllKpiList(menuCode);
		if (allKpiList == null || allKpiList.size() == 0) {
			LogUtil.error(this.getClass().toString(), "没有查到指标. 返回");
			return null;
		}

		// 获取所有列.
		List<Map<String, String>> allColumnList = getMenuAllColumnList(menuCode);
		if (allColumnList == null || allColumnList.size() == 0) {
			LogUtil.error(this.getClass().toString(), "没有查询到与菜单要展示的列.");
			return null;
		}

		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder
				.append("Select A.op_time as op_time, dim_key as dim_key ,")
				.append(" K.MENU_CODE AS menu_code, A.kpi_code as kpi_code, K.MENU_CODE||'|'||dim_key as group_tag,");

		int len = allColumnList.size();

		// 默认情况下我们把配置的第一列 作为趋势图取数据的依据.
		// String colkey =
		// allColumnList.get(0).get("col").toLowerCase(Locale.CHINA);
		sqlBuilder.append(colkey + " as " + colkey);

		sqlBuilder
				.append(" From " + menuInfo.get("dataTable")
						+ "  A , KPI_MENU_REL K ,MENU_INFO M,KPI_INFO p ")
				.append(" WHERE A.KPI_CODE = K.RESOURCE_CODE AND K.MENU_CODE =M.MENU_CODE ")
				.append(" AND a.KPI_CODE = p.KPI_CODE")
				.append(" AND A.MENU_CODE ='" + menuCode + "'")
				.append(" AND A.OP_TIME  >= '" + starttime + "'")
				.append(" AND A.OP_TIME  <= '" + endtime + "'");
		if (areaId != null && !"".equals(areaId)) {
			sqlBuilder.append(" AND AREA_CODE='" + areaId + "'");
		}

		sqlBuilder.append(" AND A.KPI_CODE in (");
		len = allKpiList.size();
		for (int i = 0; i < len; i++) {
			sqlBuilder.append("'" + allKpiList.get(i).get("kpi_code") + "'");
			if (i < len - 1)
				sqlBuilder.append(",");
		}

		sqlBuilder.append(")");

		sqlBuilder.append(" AND DIM_KEY IN (");

		String[] dimkeyArray = complexDimKey.getComplexDimKeyString().split(
				"\\" + Constant.DEFAULT_CONJUNCTION);

		len = dimkeyArray.length;
		for (int i = 0; i < len; i++) {
			sqlBuilder.append("'" + dimkeyArray[i] + "'");
			if (i < len - 1)
				sqlBuilder.append(",");
		}

		sqlBuilder.append(")");

		sqlBuilder
				.append(" order by   a.op_time asc ,  K.MENU_CODE asc,dim_key asc ,p.kpi_order asc ");

		LogUtil.debug(this.getClass().toString(), sqlBuilder.toString());

		List<Map<String, String>> data = new SQLHelper().queryForList(
				sqlBuilder.toString(), null);
		if (Constant.IS_DATA_ENCRYPT) {
			int dlen = data.size();
			for (int i = 0; i < dlen; i++) {
				Map<String, String> tmpMap = data.get(i);
				tmpMap.put(colkey, DefaultEncrypt.decrypt(tmpMap.get(colkey)));
			}
		}

		return data;

	}

	/**
	 * 查询趋势图数据
	 */
	public List<Map<String, String>> queryMainKpiTrendData(String menuCode,
			String colkey, String starttime, String endtime) {

		if (starttime == null || "".equals(starttime) || endtime == null
				|| "".equals(endtime)) {
			LogUtil.debug(this.getClass().toString(),
					"starttime 或者 endtime 为空.");
			return null;
		}

		List<Map<String, String>> menuDimList = getMenuDim(menuCode);
		Map<String, String> menuInfo = getMenuInfo(menuCode);

		if (menuInfo == null) {
			LogUtil.error(this.getClass().toString(), "查询菜单信息出错。  返回.");
			return null;
		}

		String areaId = null;
		if (menuDimList == null || menuDimList.size() == 0) {
			LogUtil.debug("", "菜单没有第三维度");
		} else {
			String dimOrder = menuDimList.get(0).get("dimOrder");
			if ("2".equals(dimOrder)) {
				areaId = getUserInfo().get("areaId");
			}

		}
		// 获取符合维度
		ComplexDimInfo complexDimKey = getMenuComplexDimKey(menuInfo);
		if ("error".equals(complexDimKey.getIsMenuCofingRight())) {
			return null;
		}
		// 获取菜单所有的指标.
		List<Map<String, String>> allKpiList = getMenuAllKpiList(menuCode);
		if (allKpiList == null || allKpiList.size() == 0) {
			LogUtil.error(this.getClass().toString(), "没有查到指标. 返回");
			return null;
		}

		// 获取所有列.
		List<Map<String, String>> allColumnList = getMenuAllColumnList(menuCode);
		if (allColumnList == null || allColumnList.size() == 0) {
			LogUtil.error(this.getClass().toString(), "没有查询到与菜单要展示的列.");
			return null;
		}

		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder
				.append("Select A.op_time as op_time, dim_key as dim_key ,")
				.append(" K.MENU_CODE AS menu_code, A.kpi_code as kpi_code, K.MENU_CODE||'|'||dim_key as group_tag,");

		int len = allColumnList.size();

		// 默认情况下我们把配置的第一列 作为趋势图取数据的依据.
		// String colkey =
		// allColumnList.get(0).get("col").toLowerCase(Locale.CHINA);
		sqlBuilder.append(colkey + " as " + colkey);

		sqlBuilder
				.append(" From " + menuInfo.get("dataTable")
						+ "  A , KPI_MENU_REL K ,MENU_INFO M,KPI_INFO p ")
				.append(" WHERE A.KPI_CODE = K.RESOURCE_CODE AND K.MENU_CODE =M.MENU_CODE ")
				.append(" AND a.KPI_CODE = p.KPI_CODE")
				.append(" AND K.MENU_CODE ='" + menuCode + "'")
				.append(" AND A.OP_TIME  >= '" + starttime + "'")
				.append(" AND A.OP_TIME  <= '" + endtime + "'");
		if (areaId != null && !"".equals(areaId)) {
			sqlBuilder.append(" AND AREA_CODE='" + areaId + "'");
		}

		sqlBuilder.append(" AND A.KPI_CODE in (");
		len = allKpiList.size();
		for (int i = 0; i < len; i++) {
			sqlBuilder.append("'" + allKpiList.get(i).get("kpi_code") + "'");
			if (i < len - 1)
				sqlBuilder.append(",");
		}

		sqlBuilder.append(")");

		sqlBuilder.append(" AND DIM_KEY IN (");

		String[] dimkeyArray = complexDimKey.getComplexDimKeyString().split(
				"\\" + Constant.DEFAULT_CONJUNCTION);

		len = dimkeyArray.length;
		for (int i = 0; i < len; i++) {
			sqlBuilder.append("'" + dimkeyArray[i] + "'");
			if (i < len - 1)
				sqlBuilder.append(",");
		}

		sqlBuilder.append(")");

		sqlBuilder
				.append(" order by   a.op_time asc ,  K.MENU_CODE asc,dim_key asc ,p.kpi_order asc ");

		LogUtil.debug(this.getClass().toString(), sqlBuilder.toString());

		List<Map<String, String>> data = new SQLHelper().queryForList(
				sqlBuilder.toString(), null);
		if (Constant.IS_DATA_ENCRYPT) {
			int dlen = data.size();
			for (int i = 0; i < dlen; i++) {
				Map<String, String> tmpMap = data.get(i);
				tmpMap.put(colkey, DefaultEncrypt.decrypt(tmpMap.get(colkey)));
			}
		}

		return data;

	}

	/**
	 * 获取所有的kpi列表
	 */
	public List<Map<String, String>> getMenuKpiRelationTagList(String menuCode) {

		if (menuCode == null || "".equals(menuCode)) {
			return null;
		}

		menuCode = "'" + menuCode + "'";
		String sql1 = "Select menu_code as menuCode From MENU_INFO where parent_menu_code in (";
		String sql2 = ")";
		while (true) {
			String sql = sql1 + menuCode + sql2;
			List<Map<String, String>> menuList = new SQLHelper().queryForList(
					sql, null);

			if (menuList == null || menuList.size() == 0) {
				break;
			}
			menuCode = "";
			int len = menuList.size();
			for (int i = 0; i < len; i++) {
				menuCode += "'" + menuList.get(i).get("menuCode") + "'";
				if (i < len - 1) {
					menuCode += ",";
				}
			}

		}

		sql1 = " Select RESOURCE_CODE as kpi_code,KPI_LEVEL as kpiLevel,RELATION_TAG as relationTAG ,m.menu_code as menu_code,m.menu_name as menu_name,k.kpi_unit as kpiUnit,"
				+ " kpi_cal_rule as rule ,kpi_name as kpiName,k.kpi_define as kpi_define "
				+ " From KPI_MENU_REL r,kpi_info k ,menu_info m "
				+ " where r.[RESOURCE_CODE] = k.[KPI_CODE]  and r.menu_code=m.menu_code and  m.menu_code in ("
				+ menuCode
				+ ")"
				+ " order by  m.menu_order asc , k.KPI_ORDER asc";

		List<Map<String, String>> kpiCodeList = new SQLHelper().queryForList(
				sql1, null);

		return kpiCodeList;
	}

	/**
	 * 查询菜单下面所的数据.
	 */
	public List<Map<String, String>> queryMainKpiData(String menuCode,
			String optime) {

		if (optime == null || "".equals(optime)) {
			LogUtil.error(this.getClass().toString(), "optime 为空. 返回.");
			return null;
		}

		List<Map<String, String>> menuDimList = getMenuDim(menuCode);
		Map<String, String> menuInfo = getMenuInfo(menuCode);

		if (menuInfo == null) {
			LogUtil.error(this.getClass().toString(), "查询菜单信息出错。  返回.");
			return null;
		}

		String areaId = null;
		if (menuDimList == null || menuDimList.size() == 0) {
			LogUtil.debug("", "菜单没有第三维度");
		} else {
			String dimOrder = menuDimList.get(0).get("dimOrder");
			if ("2".equals(dimOrder)) {
				areaId = getUserInfo().get("areaId");
			}

		}
		// 获取符合维度
		ComplexDimInfo complexDimKey = getMenuComplexDimKey(menuInfo);
		if ("error".equals(complexDimKey.getIsMenuCofingRight())) {
			return null;
		}
		// 获取菜单所有的指标.
		List<Map<String, String>> allKpiList = getMenuAllKpiList(menuCode);
		if (allKpiList == null || allKpiList.size() == 0) {
			LogUtil.error(this.getClass().toString(), "没有查到指标. 返回");
			return null;
		}

		// 获取所有列.
		List<Map<String, String>> allColumnList = getMenuAllColumnList(menuCode);
		if (allColumnList == null || allColumnList.size() == 0) {
			LogUtil.error(this.getClass().toString(), "没有查询到与菜单要展示的列.");
			return null;
		}

		List<Map<String, String>> data;

		try {

			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder
					.append("Select a.op_time as op_time, area_code as area_code, dim_key as dim_key ,")
					.append(" A.kpi_code as kpi_code, p.kpi_name as kpi_name ,p.kpi_define as kpi_define,");

			int len = allColumnList.size();
			for (int i = 0; i < len; i++) {
				String colkey = allColumnList.get(i).get("col")
						.toLowerCase(Locale.CHINA);
				sqlBuilder.append(colkey + " as " + colkey);
				if (i < len - 1)
					sqlBuilder.append(",");
			}

			sqlBuilder
					.append(" From " + menuInfo.get("dataTable")
							+ "  A , KPI_INFO p , KPI_MENU_REL k ")
					.append(" where  a.KPI_CODE = p.KPI_CODE and p.KPI_CODE = k.RESOURCE_CODE")
					.append(" AND k.MENU_CODE ='" + menuCode + "'")
					.append(" AND A.OP_TIME  = '" + optime + "'");
			if (areaId != null && !"".equals(areaId)) {
				sqlBuilder.append(" AND AREA_CODE='" + areaId + "'");
			}

			sqlBuilder.append(" AND A.KPI_CODE in (");
			len = allKpiList.size();
			for (int i = 0; i < len; i++) {
				sqlBuilder
						.append("'" + allKpiList.get(i).get("kpi_code") + "'");
				if (i < len - 1)
					sqlBuilder.append(",");
			}

			sqlBuilder.append(")");

			sqlBuilder.append(" AND DIM_KEY IN (");

			String[] dimkeyArray = complexDimKey.getComplexDimKeyString()
					.split("\\" + Constant.DEFAULT_CONJUNCTION);

			len = dimkeyArray.length;
			for (int i = 0; i < len; i++) {
				sqlBuilder.append("'" + dimkeyArray[i] + "'");
				if (i < len - 1)
					sqlBuilder.append(",");
			}

			sqlBuilder.append(")");
			sqlBuilder
					.append(" order by  dim_key asc ,k.group_order asc,k.kpi_order asc");

			LogUtil.debug(this.getClass().toString(), sqlBuilder.toString());

			data = new SQLHelper().queryForList(sqlBuilder.toString(), null);

			if (data != null && data.size() > 0 && Constant.IS_DATA_ENCRYPT) {

				int rlen = data.size();
				int clen = allColumnList.size();
				for (int i = 0; i < rlen; i++) {
					Map<String, String> tmpMap = data.get(i);

					for (int ci = 0; ci < clen; ci++) {
						String colkey = allColumnList.get(ci).get("col")
								.toLowerCase(Locale.CHINA);
						tmpMap.put(colkey,
								DefaultEncrypt.decrypt(tmpMap.get(colkey)));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return data = null;
		}

		return data;

	}

	public List<Map<String, String>> getGroupNew(String groupKind) {
		SQLHelper sh = new SQLHelper();
		String sql = "SELECT a.MENU_CODE,b.menu_change FROM MENU_INFO a left join MENU_ADD_INFO b on a.menu_code=b.menu_code WHERE MENU_KIND=?";
		List<Map<String, String>> result = sh.queryForList(sql,
				new String[] { groupKind });
		return result;
	}

	public List<Map<String, String>> getMenuGroupInfo() {
		SQLHelper sh = new SQLHelper();
		String sql = "select * from MENU_GROUP_INFO order by GROUP_ORDER asc";
		List<Map<String, String>> result = sh.queryForList(sql, null);
		return result;
	}
}
