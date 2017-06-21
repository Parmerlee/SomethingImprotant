package com.bonc.mobile.hbmclient.terminal;

import java.util.Map;

import android.content.ContentValues;

import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.data.SQLHelper;
import com.bonc.mobile.hbmclient.util.StringUtil;

public class Business {

	/**
	 * 获取日数据的最大日期.
	 * 
	 * @return
	 */
	public static String getMaxDay() {
		// String sql =
		// "select MAX(CAST(MONTH||DAY AS INTEGER)) as maxDay  from terminal_day_key_indicators";
		String sql = "select MAX( CAST(OP_TIME AS INTEGER) ) as maxDay  from  TERMINAL_DAILY_VALUE_A";
		return new SQLHelper().queryForMap(sql, null).get("maxDay");
	}

	public static String getAreaName(String areacode) {
		String sql = "select AREA_NAME as areaName from DIM_AREA_CODE where AREA_CODE = ?";
		return new SQLHelper().queryForMap(sql, new String[] { areacode }).get(
				"areaName");
	}

	public static Map<String, String> getKpiInfo(String kpicode) {
		String sql = "select * from kpi_info where kpi_code=?";
		return new SQLHelper().queryForMap(sql, new String[] { kpicode });
	}

	public static void insertStatisticsData(String codes, String time,
			String table) {
		ContentValues cv = new ContentValues();
		cv.put("CODES", codes);
		cv.put("TIME", time);
		long rowid = new SQLHelper().insert(table, cv);
		if (rowid <= 0) {
			new SQLHelper().insert(table, cv);
		}
	}

	public static String getMaxDay(String menuCode) {
		if (StringUtil.isNull(menuCode)) {
			return null;
		}
		Map<String, String> menuInfo = new BusinessDao().getMenuInfo(menuCode);

		if (menuInfo == null) {
			return null;
		}

		String dataTable = menuInfo.get("dataTable");

		if (StringUtil.isNull(dataTable)) {
			return null;
		}

		String sql = "select MAX( CAST(OP_TIME AS INTEGER) ) as maxDay  from "
				+ dataTable;

		return new SQLHelper().queryForMap(sql, null).get("maxDay");
	}

	/**
	 * 获取日数据的最小日期.
	 * 
	 * @return
	 */
	public static String getMinDay() {
		// String sql =
		// "select MIN(CAST(MONTH||DAY AS INTEGER)) as minDay  from terminal_day_key_indicators";
		String sql = "select MIN( CAST(OP_TIME AS INTEGER) ) as minDay  from  TERMINAL_DAILY_VALUE_A";
		return new SQLHelper().queryForMap(sql, null).get("minDay");
	}

	public static String getMinDay(String menuCode) {
		if (StringUtil.isNull(menuCode)) {
			return null;
		}
		Map<String, String> menuInfo = new BusinessDao().getMenuInfo(menuCode);

		if (menuInfo == null) {
			return null;
		}

		String dataTable = menuInfo.get("dataTable");

		if (StringUtil.isNull(dataTable)) {
			return null;
		}
		String sql = "select MIN( CAST(OP_TIME AS INTEGER) ) as minDay  from  "
				+ dataTable;
		return new SQLHelper().queryForMap(sql, null).get("minDay");
	}

	/**
	 * 获取数据最大月
	 * 
	 * @return
	 */
	public static String getMaxMonth() {
		// String sql =
		// "select MAX(CAST(YEAR||MONTH AS INTEGER)) as maxMonth  from terminal_month_key_indicators";
		String sql = "select MAX( CAST(OP_TIME AS INTEGER) ) as maxMonth  from  TERMINAL_MONTH_VALUE_A";
		return new SQLHelper().queryForMap(sql, null).get("maxMonth");
	}

	public static String getMaxMonth(String menuCode) {
		if (StringUtil.isNull(menuCode)) {
			return null;
		}
		Map<String, String> menuInfo = new BusinessDao().getMenuInfo(menuCode);

		if (menuInfo == null) {
			return null;
		}

		String dataTable = menuInfo.get("dataTable");

		if (StringUtil.isNull(dataTable)) {
			return null;
		}

		String sql = "select MAX( CAST(OP_TIME AS INTEGER) ) as maxMonth  from  "
				+ dataTable;

		return new SQLHelper().queryForMap(sql, null).get("maxMonth");
	}

	/**
	 * 获取数据最小月
	 * 
	 * @return
	 */
	public static String getMinMonth() {
		// String sql =
		// "select min(CAST(YEAR||MONTH AS INTEGER)) as minMonth  from terminal_month_key_indicators";
		String sql = "select MIN( CAST(OP_TIME AS INTEGER) ) as minMonth  from  TERMINAL_MONTH_VALUE_A";
		return new SQLHelper().queryForMap(sql, null).get("minMonth");
	}

	public static String getMinMonth(String menuCode) {
		if (StringUtil.isNull(menuCode)) {
			return null;
		}
		Map<String, String> menuInfo = new BusinessDao().getMenuInfo(menuCode);

		if (menuInfo == null) {
			return null;
		}

		String dataTable = menuInfo.get("dataTable");

		if (StringUtil.isNull(dataTable)) {
			return null;
		}
		String sql = "select MIN( CAST(OP_TIME AS INTEGER) ) as minMonth  from  "
				+ dataTable;
		return new SQLHelper().queryForMap(sql, null).get("minMonth");
	}

}