package com.bonc.mobile.hbmclient.data;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.common.Publicapp;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.DefaultEncrypt;
import com.bonc.mobile.hbmclient.util.HttpUtil;
import com.bonc.mobile.hbmclient.util.LogUtil;
import com.bonc.mobile.hbmclient.util.StringUtil;

public class DataLoader {

	private boolean isCheck = false;
	private final String TAG = "FirstLoadDataAsynTask";
	private BusinessDao businessDao = null;
	private SQLHelper sqlHelper = new SQLHelper();
	private boolean isInterupt = false;

	public DataLoader() {
		this.businessDao = new BusinessDao();
		this.sqlHelper = new SQLHelper();
	}

	public boolean isInterupt() {
		return isInterupt;
	}

	public void setInterupt(boolean isInterupt) {
		isInterupt = isInterupt;
	}

	public void menuDataLoad(String... menuCodes) {
		List<Map<String, String>> menuList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < menuCodes.length; i++) {
			Map<String, String> map = businessDao.getMenuInfo(menuCodes[i]);
			menuList.add(map);
		}
		dataSynchronizd(menuList);
	}

	/**
	 * 删除配客户端置表数据
	 * 
	 * @return
	 */
	public boolean deleteConfTableData() {
		boolean isSuccess = sqlHelper
				.doDBOperateWithTransation(new DatabaseCallBack() {
					@Override
					public boolean doCallBack(SQLiteDatabase database) {
						int len = Constant.confTables.length;
						String delString = "delete  from ";
						try {
							for (int i = 0; i < len; i++) {
								database.execSQL(delString
										+ Constant.confTables[i]);
							}
							// database.execSQL("VACUUM");
						} catch (Exception e) {
							e.printStackTrace();
							LogUtil.error(this.getClass().toString(),
									"清除配置信息出错!!");
							return false;
						}
						return true;
					}
				});
		return isSuccess;
	}

	public boolean initConfTableData(String permissiontime) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("permissiontime", permissiontime);
		String reply = HttpUtil.sendRequest(
				ActionConstant.GET_USER_PERMISSION_CONF, param);
		if (reply == null || "".equals(reply)) {
			return false;
		}

		try {
			final JSONObject jsonObject_re = new JSONObject(reply);
			final JSONObject jsonObject_permission = jsonObject_re
					.getJSONObject("permission");
			final JSONObject resetInfo = jsonObject_re
					.optJSONObject("reset_info");
			this.insertConfigInfoToDB(sqlHelper, jsonObject_permission,
					jsonObject_re, resetInfo);
		} catch (Throwable t) {
			t.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean insertConfigInfoToDB(SQLHelper sqlHelper,
			final JSONObject jsonObjectAll, final JSONObject jsonObjectM,
			final JSONObject resetInfo) {
		// 以下部分将用户权限插入数据库. 插入之前要先删除数据库
		boolean sucess = sqlHelper
				.doDBOperateWithTransation(new DatabaseCallBack() {
					@Override
					public boolean doCallBack(SQLiteDatabase database) {
						try {
							// 插入menu_kpi_col_rel
							JSONArray jsonArrayRel = new JSONArray(
									jsonObjectAll.getString("menu_kpi_col_rel"));
							if (jsonArrayRel.length() > 0) {
								Log.i("jsonArrayRel.length()", "长度"
										+ jsonArrayRel.length());
								for (int i = 0; i < jsonArrayRel.length(); i++) {
									JSONObject jsonObjectRel = jsonArrayRel
											.getJSONObject(i);
									ContentValues cv = new ContentValues();
									cv.put("relation_kpivalue_column_name",
											jsonObjectRel
													.getString("relation_kpivalue_column_name"));
									cv.put("relation_kpivalue_column",
											jsonObjectRel
													.getString("relation_kpivalue_column"));
									cv.put("show_order", jsonObjectRel
											.getString("show_order"));
									cv.put("kpivalue_unit", jsonObjectRel
											.getString("kpivalue_unit"));
									cv.put("resource_type", jsonObjectRel
											.getString("resource_type"));
									cv.put("kpivalue_rule", jsonObjectRel
											.getString("kpivalue_rule"));
									cv.put("resource_code", jsonObjectRel
											.getString("resource_code"));
									cv.put("relation_kpivalue_table",
											jsonObjectRel
													.getString("relation_kpivalue_table"));
									database.insert("menu_kpi_col_rel", null,
											cv);
								}
							}
							// 插入user_info
							JSONObject jsonObjectUI = jsonObjectAll
									.getJSONObject("user_info");
							String authority_time = jsonObjectUI
									.getString("authority_time");
							ContentValues cvUI = new ContentValues();
							// 4A账号进行加密
							String user_code = jsonObjectUI
									.getString("user_code");
							String user_area_id = jsonObjectUI
									.getString("user_area_code");
							if (Constant.IS_DATA_ENCRYPT) {
								// 用户归属地区加密
								cvUI.put("user_code",
										DefaultEncrypt.encrpyt(user_code));
								cvUI.put("user_area_id",
										DefaultEncrypt.encrpyt(user_area_id));
							} else {
								cvUI.put("user_code", user_code);
								cvUI.put("user_area_id", user_area_id);
							}

							cvUI.put("authority_time", authority_time);

							database.insert("user_info", null, cvUI);

							// 插入menu_group_info
							JSONArray jaMenuGroupInfo = new JSONArray(
									jsonObjectAll.getString("menu_group_info"));
							if (jaMenuGroupInfo.length() > 0) {
								for (int i = 0; i < jaMenuGroupInfo.length(); i++) {
									JSONObject jomgi = jaMenuGroupInfo
											.getJSONObject(i);
									ContentValues cv = new ContentValues();
									cv.put("GROUP_NAME",
											jomgi.getString("group_name"));
									cv.put("GROUP_KIND",
											jomgi.getString("group_kind"));
									cv.put("GROUP_ORDER",
											jomgi.getString("group_order"));
									cv.put("GROUP_CHANGE",
											jomgi.getString("group_change"));
									database.insert("MENU_GROUP_INFO", null, cv);
								}
							}

							// 插入menu_info
							JSONArray jsonArrayMI = new JSONArray(jsonObjectAll
									.getString("menu_info"));
							if (jsonArrayMI.length() > 0) {
								for (int i = 0; i < jsonArrayMI.length(); i++) {
									JSONObject jsonObjectMI = jsonArrayMI
											.getJSONObject(i);
									ContentValues cv = new ContentValues();
									// 菜单名称加密
									cv.put("menu_name",
											jsonObjectMI.getString("menu_name"));
									cv.put("menu_order", jsonObjectMI
											.getString("menu_order"));
									// 菜单解释进行加密
									cv.put("menu_explain", jsonObjectMI
											.getString("menu_explain"));
									cv.put("view_dim_flag", jsonObjectMI
											.getString("view_dim_flag"));
									cv.put("parent_menu_code", jsonObjectMI
											.getString("parent_menu_code"));
									cv.put("menu_type",
											jsonObjectMI.optString("menu_type"));
									cv.put("menu_code",
											jsonObjectMI.optString("menu_code"));
									cv.put("menu_level", jsonObjectMI
											.optString("menu_level"));
									cv.put("data_table", jsonObjectMI
											.optString("menu_ref_data_table"));
									cv.put("menu_kind",
											jsonObjectMI.optString("menu_kind"));
									cv.put("date_type",
											jsonObjectMI.optString("date_type"));
									database.insert("menu_info", null, cv);
								}
							}

							// 插入菜单额外信息 menu_add_info
							JSONArray menuAddInfoArray = new JSONArray(
									jsonObjectAll.getString("menu_add_info"));
							if (menuAddInfoArray != null
									&& menuAddInfoArray.length() > 0) {

								for (int i = 0; i < menuAddInfoArray.length(); i++) {
									JSONObject jsonObject = menuAddInfoArray
											.getJSONObject(i);
									ContentValues cv = new ContentValues();

									cv.put("menu_code",
											jsonObject.optString("menu_code"));
									cv.put("show_thrend_flag", jsonObject
											.optString("show_thrend_flag"));
									cv.put("show_area_level", jsonObject
											.optString("show_area_level"));
									cv.put("fluctuatest_column", jsonObject
											.optString("fluctuatest_column"));
									cv.put("highest_column", jsonObject
											.optString("highest_column"));
									cv.put("fluctuatest_desc", jsonObject
											.optString("fluctuatest_desc"));
									cv.put("highest_desc", jsonObject
											.optString("highest_desc"));
									cv.put("show_top5_flag", jsonObject
											.optString("show_top5_flag"));
									cv.put("top5_order_cols", jsonObject
											.optString("top5_order_cols"));
									cv.put("top5_order_names", jsonObject
											.optString("top5_order_names"));
									cv.put("img_id",
											jsonObject.optString("img_id"));
									cv.put("show_daily_report", jsonObject
											.optString("show_daily_report"));
									cv.put("MENU_CHANGE",
											jsonObject.optString("menu_change"));
									cv.put("RELATION_TAG", jsonObject
											.optString("relation_tag"));
									cv.put("startTime",
											jsonObject.optString("start_time"));
									cv.put("endTime",
											jsonObject.optString("end_time"));

									database.insert("menu_add_info", null, cv);
								}
							}

							// 插入dim_area_code
							JSONArray jsonArrayDA = new JSONArray(jsonObjectAll
									.getString("dim_area_code"));
							if (jsonArrayDA.length() > 0) {
								for (int i = 0; i < jsonArrayDA.length(); i++) {
									JSONObject jsonObjectDA = jsonArrayDA
											.getJSONObject(i);
									ContentValues cv = new ContentValues();

									cv.put("parent_area_code", jsonObjectDA
											.getString("parent_area_code"));
									cv.put("area_name",
											jsonObjectDA.getString("area_name"));
									cv.put("area_order", jsonObjectDA
											.getString("area_order"));
									cv.put("area_code",
											jsonObjectDA.getString("area_code"));
									cv.put("area_level", jsonObjectDA
											.getString("area_level"));
									cv.put("area_feature", jsonObjectDA
											.getString("area_feature"));
									database.insert("dim_area_code", null, cv);
								}
							}
							// 插入dim_menu_rel
							JSONArray jsonArrayDM = new JSONArray(jsonObjectAll
									.getString("dim_menu_rel"));
							if (jsonArrayDM.length() > 0) {
								for (int i = 0; i < jsonArrayDM.length(); i++) {
									JSONObject jsonObjectDM = jsonArrayDM
											.getJSONObject(i);
									ContentValues cv = new ContentValues();

									cv.put("menu_code",
											jsonObjectDM.getString("menu_code"));
									cv.put("resource_type", jsonObjectDM
											.getString("resource_type"));
									cv.put("resource_code", jsonObjectDM
											.getString("resource_code"));

									database.insert("dim_menu_rel", null, cv);
								}
							}
							// 插入dim_info
							JSONArray jsonArrayDI = new JSONArray(jsonObjectAll
									.getString("dim_info"));
							if (jsonArrayDI.length() > 0) {
								for (int i = 0; i < jsonArrayDI.length(); i++) {
									JSONObject jsonObjectDI = jsonArrayDI
											.getJSONObject(i);
									ContentValues cv = new ContentValues();

									cv.put("dim_name",
											jsonObjectDI.getString("dim_name"));
									cv.put("dim_code",
											jsonObjectDI.getString("dim_code"));
									cv.put("dim_order",
											jsonObjectDI.getString("dim_order"));
									cv.put("dim_presix", jsonObjectDI
											.getString("dim_presix"));

									database.insert("dim_info", null, cv);
								}
							}

							// 插入kpi_menu_rel
							JSONArray jsonArrayKM = new JSONArray(jsonObjectAll
									.getString("kpi_menu_rel"));
							if (jsonArrayKM.length() > 0) {
								for (int i = 0; i < jsonArrayKM.length(); i++) {
									JSONObject jsonObjectKM = jsonArrayKM
											.getJSONObject(i);
									ContentValues cv = new ContentValues();
									cv.put("menu_code",
											jsonObjectKM.getString("menu_code"));
									cv.put("resource_type", jsonObjectKM
											.getString("resource_type"));
									cv.put("resource_code", jsonObjectKM
											.getString("resource_code"));
									cv.put("kpi_level",
											jsonObjectKM.getString("kpi_level"));

									String strValue = jsonObjectKM
											.getString("kpi_order");
									if (!StringUtil.isNull(strValue)) {
										cv.put("kpi_order",
												Integer.parseInt(strValue));
									}
									cv.put("parent_kpi_code", jsonObjectKM
											.getString("parent_kpi_code"));
									strValue = jsonObjectKM
											.getString("relation_tag");
									if (!StringUtil.isNull(strValue)) {
										cv.put("relation_tag",
												Integer.parseInt(strValue));
									}
									strValue = jsonObjectKM
											.getString("group_id");
									if (!StringUtil.isNull(strValue)) {
										cv.put("group_id",
												Integer.parseInt(strValue));
									}
									cv.put("group_name", jsonObjectKM
											.getString("group_name"));
									strValue = jsonObjectKM
											.getString("group_order");
									if (!StringUtil.isNull(strValue)) {
										cv.put("group_order",
												Integer.parseInt(strValue));
									}
									database.insert("kpi_menu_rel", null, cv);
								}
							}

							// 插入m_kpi_main_rel, 指标的关联指标关系
							JSONArray jsonArrayKR = new JSONArray(jsonObjectAll
									.getString("m_kpi_main_rel"));
							int krlen = jsonArrayKR.length();
							if (krlen > 0) {
								for (int i = 0; i < krlen; i++) {
									JSONObject jsonObjectKI = jsonArrayKR
											.getJSONObject(i);
									ContentValues cv = new ContentValues();
									cv.put("main_kpi_code", jsonObjectKI
											.getString("main_kpi_code"));
									cv.put("rel_kpi_code", jsonObjectKI
											.getString("rel_kpi_code"));
									cv.put("group_name", jsonObjectKI
											.getString("group_name"));
									cv.put("rel_table_name", jsonObjectKI
											.getString("rel_table_name"));

									String strValue = jsonObjectKI
											.getString("rel_kpi_order");
									if (!StringUtil.isNull(strValue)) {
										cv.put("rel_kpi_order",
												Integer.parseInt(strValue));
									}
									strValue = jsonObjectKI
											.getString("group_id");
									if (!StringUtil.isNull(strValue)) {
										cv.put("group_id",
												Integer.parseInt(strValue));
									}
									strValue = jsonObjectKI
											.getString("group_order");
									if (!StringUtil.isNull(strValue)) {
										cv.put("group_order",
												Integer.parseInt(strValue));
									}
									database.insert("m_kpi_main_rel", null, cv);
								}
							}

							// 插入kpi_info
							JSONArray jsonArrayKI = jsonObjectAll
									.getJSONArray("kpi_info");
							if (jsonArrayKI.length() > 0) {
								for (int i = 0; i < jsonArrayKI.length(); i++) {
									JSONObject jsonObjectKI = jsonArrayKI
											.getJSONObject(i);
									ContentValues cv = new ContentValues();
									cv.put("kpi_desc",
											jsonObjectKI.getString("kpi_desc"));
									cv.put("kpi_date_type", jsonObjectKI
											.getString("kpi_date_type"));
									cv.put("kpi_code",
											jsonObjectKI.getString("kpi_code"));
									cv.put("kpi_name",
											jsonObjectKI.getString("kpi_name"));
									cv.put("kpi_unit",
											jsonObjectKI.getString("kpi_unit"));
									cv.put("kpi_order",
											jsonObjectKI.getString("kpi_order"));
									cv.put("kpi_define", jsonObjectKI
											.getString("kpi_define"));
									cv.put("kpi_cal_rule", jsonObjectKI
											.getString("kpi_cal_rule"));
									database.insert("kpi_info", null, cv);
								}
							}
							// 插入dim_value_info
							JSONArray jsonArrayDVI = new JSONArray(
									jsonObjectAll.getString("dim_value_info"));
							if (jsonArrayDVI.length() > 0) {
								for (int i = 0; i < jsonArrayDVI.length(); i++) {
									JSONObject jsonObjectDVI = jsonArrayDVI
											.getJSONObject(i);
									ContentValues cv = new ContentValues();
									cv.put("show_order", jsonObjectDVI
											.getString("show_order"));
									cv.put("dim_code",
											jsonObjectDVI.getString("dim_code"));
									cv.put("dim_value_id", jsonObjectDVI
											.getString("dim_value_id"));
									cv.put("dim_value_name", jsonObjectDVI
											.getString("dim_value_name"));

									database.insert("dim_value_info", null, cv);
								}
							}

							/**
							 * 如果db删了. 重新把更新的时间戳 插入.
							 */
							if (resetInfo != null
									&& "1".equals(resetInfo
											.optString("reset_db"))) {
								JSONArray rollbackArray2 = jsonObjectM
										.optJSONArray("menu_ref_data_change");

								if (rollbackArray2 != null
										&& rollbackArray2.length() > 0) {
									for (int i = 0; i < rollbackArray2.length(); i++) {
										ContentValues cv = new ContentValues();
										cv.put("menu_id",
												rollbackArray2
														.optJSONObject(i)
														.optString(
																"ref_resource_code"));
										cv.put("mark_id",
												rollbackArray2.optJSONObject(i)
														.optInt("flow_id"));

										database.insert("MENU_DATAUPDATE_HIS",
												null, cv);
									}
								}
							}
							/**
							 * 更新菜單的數據類型.
							 */
							StringBuilder sqlBuilder = new StringBuilder();
							sqlBuilder
									.append("Select distinct m.[PARENT_MENU_CODE] as menuCode, min(kpi_date_type) as dataType From  KPI_INFO  k  ,")
									.append(" kpi_menu_rel r,menu_info m where k.KPI_CODE = r.RESOURCE_CODE ")
									.append(" and r.menu_code = m.menu_code and m.[MENU_LEVEL] = 3 ")
									.append(" group by m.menu_code ")
									.append(" union  ")
									.append(" Select distinct m.[MENU_CODE] as menuCode, min(kpi_date_type) as dataType From  KPI_INFO  k  ,")
									.append(" kpi_menu_rel r,menu_info m where k.KPI_CODE = r.RESOURCE_CODE ")
									.append(" and r.menu_code = m.menu_code and m.[MENU_LEVEL] in (1,2) ")
									.append(" group by m.menu_code ");

							String update = "update  MENU_INFO  set data_type = ? where menu_code= ? ";
							SQLiteStatement stmt = database
									.compileStatement(update);
							Cursor cursor = database.rawQuery(
									sqlBuilder.toString(), null);

							String menuCode, dataType;
							if (cursor.moveToFirst()) {
								do {
									menuCode = cursor.getString(cursor
											.getColumnIndex("menuCode"));
									dataType = cursor.getString(cursor
											.getColumnIndex("dataType"));

									stmt.bindString(1, dataType);
									stmt.bindString(2, menuCode);

									stmt.execute();

								} while (cursor.moveToNext());

							}

							SharedPreferences spPer = Publicapp.getInstance()
									.getSharedPreferences("permissiontime",
											Publicapp.MODE_WORLD_WRITEABLE);
							Editor editor = spPer.edit();
							editor.putString("permissiontime", authority_time);
							editor.commit();

						} catch (Exception e) {
							e.printStackTrace();
							return false;
						}

						return true;

					}

				});
		return sucess;
	}

	private void dataSynchronizd(List<Map<String, String>> menuList) {
		if (menuList == null)
			return;

		int len = menuList.size();
		for (int i = 0; i < len; i++) {
			if (isInterupt()) {
				break;
			}
			// 同步菜单数据.
			doSecondMenuDataSync(menuList.get(i), i);
			// 检查是否有数据需要重新同步
			checkMenuDataRollback(menuList.get(i));
		}
		if (isInterupt()) {
			setInterupt(false);
		}
	}

	/**
	 * 开始数据同步.
	 * 
	 * @param index
	 */
	@SuppressLint("WorldReadableFiles")
	private void doSecondMenuDataSync(Map<String, String> menuinfo, int index) {
		// 初始化默认值.
		boolean isterminal = false;

		BusinessDao bd = new BusinessDao();
		String menuCode = menuinfo.get("menuCode");
		String lastUpdateTime = menuinfo.get("lastUpdateTime");

		long notifyRealTime = -1;

		try {
			SharedPreferences sharedPreferences = Publicapp.getInstance()
					.getSharedPreferences("SETTING",
							Context.MODE_WORLD_READABLE);
			notifyRealTime = sharedPreferences.getLong("notifyRealTime", -1);
		} catch (Exception e1) {
			notifyRealTime = -1;
			e1.printStackTrace();
		}

		// 检查上次更新时间.
		if (lastUpdateTime != null && !"".equals(lastUpdateTime)) {
			try {

				long lut = Long.valueOf(lastUpdateTime);
				long currtime = System.currentTimeMillis();

				if (currtime - lut <= 4 * 60 * 60 * 1000
						&& lut > notifyRealTime) {
					// 如果上次更检查到现在不超过6小时 则不做检查.
					isCheck = false;
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		isCheck = true;

		String menuName = menuinfo.get("menuName");

		// 用户信息
		Map<String, String> userInfo = bd.getUserInfo();

		if (userInfo == null) {
			LogUtil.error(TAG, "没用查询到用户信息,退出同步!!");
			return;
		}

		if (StringUtil.isNull(userInfo.get("usercode"))) {
			LogUtil.error(TAG, "没用查询到用户账号,退出同步!!");
			return;
		}

		String areaId = bd.getUserInfo().get("areaId");
		if (areaId == null || "".equals(areaId)) {
			LogUtil.error(TAG, "菜单[" + menuName + "]用户没有归属地区.同步结束.");
			return;
		}

		// 获取所有的列
		String colums = bd.getMenuAllColumnString(menuCode);
		if (colums == null || "".equals(colums)) {
			return;
		}
		// 数据类型.
		String dataType = menuinfo.get("dataType");
		if (dataType == null || "".equals(dataType)) {
			LogUtil.info(TAG, "菜单[" + menuName + "]没有配置数据类型");
			return;
		}

		// 获取菜单下面所有的指标
		String kpicodes = bd.getMenuAllKpiString(menuCode);
		if (kpicodes == null || "".equals(kpicodes)) {
			LogUtil.info(TAG, "菜单[" + menuName + "]下面没有kpi指标.");
			return;
		}

		// 获取server端存储数据的表
		String dataTable = bd.getMenuColDataTable(menuCode);

		if (dataTable == null || "".equals(dataTable)) {
			LogUtil.info(TAG, "菜单[" + menuName + "]没有查到server端的数据表.");
			return;
		}

		// 获取符合维度
		// String complexDimKey = bd.getMenuComplexDimKey(menuinfo);
		ComplexDimInfo complexDimKey = bd.getMenuComplexDimKey(menuinfo);
		if ("error".equalsIgnoreCase(complexDimKey.getIsMenuCofingRight())) {
			LogUtil.info(TAG, "菜单[" + menuName + "]查询复合维度出错，检查菜单配置情况.");
			return;
		}

		List<Map<String, String>> colList = bd.getMenuAllColumnList(menuCode);

		Map<String, String> param = new HashMap<String, String>();
		param.put("cols", colums);
		param.put("areacode", areaId);
		param.put("dimkey", complexDimKey.getComplexDimKeyString());
		param.put("kpicodes", kpicodes);
		param.put("datatype", dataType);
		param.put("datatable", dataTable);

		String action = ActionConstant.KPI_DATA_SYNCHRIONZID;

		// 终端top20
		if (TerminalConfiguration.KEY_MENU_CODE_TOP20_PSS_DAY.equals(menuCode
				.trim())
				|| TerminalConfiguration.KEY_MENU_CODE_TOP20_UNSALE_DAY
						.equals(menuCode.trim())
				|| TerminalConfiguration.KEY_MENU_CODE_TOP20_UNPACK_DAY
						.equals(menuCode.trim())
				|| TerminalConfiguration.KEY_MENU_CODE_TOP20_PSS_MONTH
						.equals(menuCode.trim())
				|| TerminalConfiguration.KEY_MENU_CODE_TOP20_UNSALE_MONTH
						.equals(menuCode.trim())
				|| TerminalConfiguration.KEY_MENU_CODE_TOP20_UNPACK_MONTH
						.equals(menuCode.trim())
				|| TerminalConfiguration.KEY_MENU_CODE_TOP20_FG_MONTH
						.equals(menuCode.trim())) {
			isterminal = true;
		}

		if (isterminal) {
			action = ActionConstant.TOP_10_DATA_SYNCHRIONZID;
		}

		int count = 0;

		while (true) {
			if (isInterupt()) {
				break;
			}
			// 单个模块同步不超过20次.
			if (count++ > 20) {
				break;
			}

			String maxDate = bd.getMenuMinOrMaxDate(menuCode,
					menuinfo.get("dataTable"), 2);
			String lastTime = "";
			if (!maxDate.equals(BusinessDao.DEFAULT_DATE)) {
				if (Constant.DATA_TYPE_DAY.equalsIgnoreCase(dataType)) {
					maxDate = DateUtil.nextDay(maxDate, DateUtil.PATTERN_8);
					lastTime = DateUtil.formatter(Calendar.getInstance()
							.getTime(), DateUtil.PATTERN_8);
				} else if (Constant.DATA_TYPE_MONTH.equalsIgnoreCase(dataType)) {
					maxDate = DateUtil.nextMonth(maxDate, DateUtil.PATTERN_6);
					lastTime = DateUtil.formatter(Calendar.getInstance()
							.getTime(), DateUtil.PATTERN_6);
				}
			}

			// 如果数据日期已到最大时间则不向server端发送请求. 判断依据是日数据最大日期是到当前
			// 日期的以前一天。 月数据是到当前日期的前一个月.
			if (maxDate.equals(lastTime)) {
				LogUtil.debug(TAG, "数据已更新至最新日期" + lastTime + ".不需要向server端同步.");
				break;
			}

			param.remove("optime");
			param.put("optime", maxDate);

			Date ddd1 = Calendar.getInstance().getTime();
			String json_reply = HttpUtil.sendRequest(action, param);
			LogUtil.debug(
					"terminltest",
					"请求耗时:"
							+ (Calendar.getInstance().getTimeInMillis() - ddd1
									.getTime()));

			if (json_reply == null || "".equals(json_reply)
					|| "[]".equals(json_reply)) {

				LogUtil.info(this.getClass().toString(), "菜单[" + menuName
						+ "]没有数据返回！结束同步");
				break;
			}

			LogUtil.debug(
					"terminltest",
					"处理开始时间:"
							+ DateUtil.formatter(Calendar.getInstance()
									.getTime(), "yyyyMMdd HH:mm:ss"));
			String sql1 = "insert into " + menuinfo.get("dataTable")
					+ "(menu_code,op_time,area_code,dim_key,kpi_code";
			String sql2 = " values(?,?,?,?,?";

			int collen = colList.size();

			for (int i = 0; i < collen; i++) {
				sql1 += "," + colList.get(i).get("col");
				sql2 += ",?";
			}

			if (isterminal) {
				sql1 += ",term_type_name";
				sql2 += ",?";
			}

			String sql = sql1 + ")" + sql2 + ")";

			JSONArray jsonArray = null;

			try {
				jsonArray = new JSONArray(json_reply);
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}

			if (jsonArray.length() == 0) {
				LogUtil.info(this.getClass().toString(), "返回的json数组为空.同步结束");
				break;
			}

			final WeakReference<JSONArray> jsArrayFinal_wr = new WeakReference<JSONArray>(
					jsonArray);
			// final JSONArray jsArrayFinal = jsonArray;
			final String finalMenuCode = menuCode;
			final int finalCollen = collen;
			final List<Map<String, String>> finalColList = colList;
			final boolean finalIsterminal = isterminal;
			final String finalSql = sql;

			boolean success = false;

			try {
				success = new SQLHelper()
						.doDBOperateWithTransation(new DatabaseCallBack() {

							@Override
							public boolean doCallBack(SQLiteDatabase database) {

								SQLiteStatement sqLiteStatement = database
										.compileStatement(finalSql);

								JSONArray jsArrayFinal = jsArrayFinal_wr.get();
								int len = jsArrayFinal.length();
								String key = "";
								JSONObject jsonObject = null;
								Date date1 = Calendar.getInstance().getTime();
								for (int i = 0; i < len; i++) {
									jsonObject = jsArrayFinal.optJSONObject(i);
									sqLiteStatement
											.bindString(1, finalMenuCode);
									sqLiteStatement.bindString(2,
											jsonObject.optString("op_time"));
									sqLiteStatement.bindString(3,
											jsonObject.optString("area_code"));
									sqLiteStatement.bindString(4,
											jsonObject.optString("dim_key"));
									sqLiteStatement.bindString(5,
											jsonObject.optString("kpi_code"));

									for (int k = 0; k < finalCollen; k++) {
										key = finalColList.get(k).get("col")
												.toLowerCase(Locale.CHINA);
										// 数据加密
										String value = jsonObject
												.optString(key);
										if (Constant.IS_DATA_ENCRYPT) {
											sqLiteStatement.bindString(6 + k,
													DefaultEncrypt
															.encrpyt(value));
										} else {
											sqLiteStatement.bindString(6 + k,
													value);
										}
									}

									if (finalIsterminal) {
										sqLiteStatement.bindString(
												6 + finalCollen,
												jsonObject
														.optString("term_type_name"));
									}

									sqLiteStatement.executeInsert();
								}
								LogUtil.debug(
										"xxxxx",
										"插入"
												+ len
												+ "行,耗时:"
												+ (Calendar.getInstance()
														.getTimeInMillis() - date1
														.getTime()));
								return true;
							}
						});

				if (!success) {
					break;
				}

			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.info(this.getClass().toString(), "菜单[" + menuName + "]"
						+ "数据同步过程中,数据库插入异常. 退出同步.");
				break;
			} finally {
				// 使得weakReference起作用
				jsonArray = null;
			}
		}
		if (!isInterupt()) {
			// 更新数据检查时间.
			bd.updateLastTime(String.valueOf(System.currentTimeMillis()),
					menuCode);
		}
	}

	/**
	 * 检查数据回滚.
	 */

	private void checkMenuDataRollback(Map<String, String> menuinfo) {
		// 如果不用检查更新 则直接
		if (!isCheck) {
			return;
		}

		String sqlString = "Select max(menu_id) as menuCode,max(mark_id) as markid From MAIN.[MENU_DATAUPDATE_HIS]  where menu_id = ?  Limit 1";

		final String menuCode = menuinfo.get("menuCode");

		final Map<String, String> result = new SQLHelper().queryForMap(
				sqlString, new String[] { menuCode });

		Map<String, String> param = new HashMap<String, String>();
		if (result == null || result.get("markid") == null) {
			param.put("flowid", "0");
		} else {
			String markid = result.get("markid");
			param.put("flowid", markid);
		}

		param.put("menucode", menuCode);

		// 检查是否有需要回滚的数据.
		String jsonString = HttpUtil.sendRequest(
				ActionConstant.MENUDATA_ROLLBACK_CHECK, param);

		if (StringUtil.isNull(jsonString)) {
			return;
		}

		JSONArray jArray = null;
		try {
			jArray = new JSONArray(jsonString);
		} catch (JSONException e) {
			LogUtil.error(TAG, "数据更新返回json串转化为对象的时候出现异常.结束更新！ json串:"
					+ jsonString);
			e.printStackTrace();
			return;
		}

		if (jArray == null || jArray.length() == 0) {
			return;
		}

		// 数据同步准备工作.
		BusinessDao bd = new BusinessDao();
		boolean isterminal = false;
		final String menuName = menuinfo.get("menuName");
		// 获取所有的列
		String colums = bd.getMenuAllColumnString(menuCode);
		if (colums == null || "".equals(colums)) {
			LogUtil.info(TAG, "菜单[" + menuName + "]没有配置列，不在继续同步数据!");
			return;
		}
		// 数据类型.
		String dataType = menuinfo.get("dataType");
		if (dataType == null || "".equals(dataType)) {
			LogUtil.info(TAG, "菜单[" + menuName + "]没有配置数据类型");
			return;
		}

		// 用户信息
		String areaId = bd.getUserInfo().get("areaId");
		if (areaId == null || "".equals(areaId)) {
			LogUtil.error(TAG, "菜单[" + menuName + "]用户没有归属地区.同步结束.");
			return;
		}

		// 获取菜单下面所有的指标
		String kpicodes = bd.getMenuAllKpiString(menuCode);
		if (kpicodes == null || "".equals(kpicodes)) {
			LogUtil.info(TAG, "菜单[" + menuName + "]下面没有kpi指标.");
			return;
		}

		// 获取server端存储数据的表
		String dataTable = bd.getMenuColDataTable(menuCode);

		if (dataTable == null || "".equals(dataTable)) {
			LogUtil.info(TAG, "菜单[" + menuName + "]没有查到server端的数据表.");
			return;
		}

		// 获取符合维度
		ComplexDimInfo complexDimKey = bd.getMenuComplexDimKey(menuinfo);
		if ("error".equalsIgnoreCase(complexDimKey.getIsMenuCofingRight())) {
			LogUtil.info(TAG, "菜单[" + menuName + "]查询复合维度出错，检查菜单配置情况.");
			return;
		}

		param.clear();

		param.put("cols", colums);
		param.put("areacode", areaId);
		param.put("dimkey", complexDimKey.getComplexDimKeyString());
		param.put("kpicodes", kpicodes);
		param.put("datatype", dataType);
		param.put("datatable", dataTable);

		String action = ActionConstant.MENUDATA_UPDATE_INTERFACE;

		// 终端top20
		if ("44".equals(menuCode.trim()) || "45".equals(menuCode.trim())) {
			isterminal = true;
		}

		if (isterminal) {
			action = ActionConstant.TERMIANLDATA_UPDATE_INTERFACE;
		}

		int jlen = jArray.length();
		// 要回滚的数据的开始时间和结束时间
		int starttime = 30000101, endtime = 0;
		int markId = 0; // 最大id标示.

		// 对要回滚的时间段进行合并处理，找到开始的最小时间和结束的最大时间. 最后将最大的markId插入到本地数据库.
		for (int ll = 0; ll < jlen; ll++) {
			JSONObject jObject = jArray.optJSONObject(ll);
			String mcode = jObject.optString("ref_resource_code");

			if (!menuCode.equals(mcode)) {
				LogUtil.error(TAG,
						"返回的menuCode为空!或者与本次的menucode对不上! 返回的menuCode:["
								+ mcode + "]  本地的menucode:[" + menuCode + "]");
				continue;
			}

			String startt = jObject.optString("ref_start_time");
			String endt = jObject.optString("ref_end_time");
			String mid = jObject.optString("flow_id");

			if (StringUtil.isNull(mid)) {
				LogUtil.error(TAG, "markid为空!跳出本次同步  执行下一次同步.");
				continue;
			}

			if (StringUtil.isNull(startt)) {
				LogUtil.error(TAG, "数据开始的日期时间为空. 跳出本次同步.");
				continue;
			}

			if (StringUtil.isNull(endt)) {
				LogUtil.error(TAG, "数据结束的日期时间为空. 跳出本次同步.");
				continue;
			}

			try {
				int tmpstime = Integer.parseInt(startt);
				int tmpetime = Integer.parseInt(endt);
				int tmpmarkid = Integer.parseInt(mid);

				if (tmpstime < starttime) {
					starttime = tmpetime;
				}

				if (tmpetime > endtime) {
					endtime = tmpetime;
				}

				if (tmpmarkid > markId) {
					markId = tmpmarkid;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		param.put("starttime", starttime + "");
		param.put("endtime", endtime + "");

		final String json_reply = HttpUtil.sendRequest(action, param);

		final String insertHis = "insert into  MENU_DATAUPDATE_HIS(menu_id,mark_id) values(?,?)";
		final String updateHis = "update MENU_DATAUPDATE_HIS   set mark_id= ? where menu_id = ?";
		final String delsql = "delete from " + menuinfo.get("dataTable")
				+ " where menu_code= ? and op_time >= ? and op_time <= ?";

		String sql1 = "insert into " + menuinfo.get("dataTable")
				+ "(menu_code,op_time,area_code,dim_key,kpi_code";
		String sql2 = " values(?,?,?,?,?";
		final List<Map<String, String>> colList = bd
				.getMenuAllColumnList(menuCode);

		final int collen = colList.size();

		for (int k = 0; k < collen; k++) {
			sql1 += "," + colList.get(k).get("col");
			sql2 += ",?";
		}

		if (isterminal) {
			sql1 += ",term_type_name";
			sql2 += ",?";
		}

		String sql = sql1 + ")" + sql2 + ")";

		boolean success = false;

		final int finalMarkId = markId;
		final boolean finalIsterminal = isterminal;
		final int finalStarttime = starttime;
		final int finalEndtime = endtime;
		final String finaSql = sql;

		success = new SQLHelper()
				.doDBOperateWithTransation(new DatabaseCallBack() {

					@Override
					public boolean doCallBack(SQLiteDatabase database) {

						try {
							database.execSQL(delsql, new String[] { menuCode,
									finalStarttime + "", finalEndtime + "" });

							if (json_reply == null || "".equals(json_reply)) {

								LogUtil.info(this.getClass().toString(), "菜单["
										+ menuName + "]没有数据返回!");
							} else {

								JSONArray jsonArray = null;

								boolean f = true;
								try {
									jsonArray = new JSONArray(json_reply);
								} catch (Exception e) {
									e.printStackTrace();
									f = false;
								}

								if (!f || jsonArray.length() == 0) {
									LogUtil.info(this.getClass().toString(),
											"返回的json数组为空.同步结束");
								} else {

									SQLiteStatement sqLiteStatement = database
											.compileStatement(finaSql);
									int len = jsonArray.length();
									String key = "";
									JSONObject jsonObject = null;
									for (int i = 0; i < len; i++) {

										jsonObject = jsonArray.optJSONObject(i);
										sqLiteStatement.bindString(1, menuCode);
										sqLiteStatement
												.bindString(2, jsonObject
														.optString("op_time"));
										sqLiteStatement.bindString(3,
												jsonObject
														.optString("area_code"));
										sqLiteStatement
												.bindString(4, jsonObject
														.optString("dim_key"));
										sqLiteStatement.bindString(5,
												jsonObject
														.optString("kpi_code"));

										for (int k = 0; k < collen; k++) {
											key = colList.get(k).get("col")
													.toLowerCase(Locale.CHINA);
											if (Constant.IS_DATA_ENCRYPT) {
												sqLiteStatement.bindString(
														6 + k,
														DefaultEncrypt
																.encrpyt(jsonObject
																		.optString(key)));
											} else {
												sqLiteStatement.bindString(
														6 + k, jsonObject
																.optString(key));
											}

										}

										if (finalIsterminal) {
											sqLiteStatement.bindString(
													6 + collen,
													jsonObject
															.optString("term_type_name"));
										}

										sqLiteStatement.executeInsert();
									}
								}

							}

							// 更新数据更新历史表.
							if (result == null
									|| result.get("menuCode") == null) {
								database.execSQL(insertHis, new String[] {
										menuCode, finalMarkId + "" });
							} else {
								database.execSQL(updateHis, new String[] {
										finalMarkId + "", menuCode });
							}

						} catch (Exception e) {
							e.printStackTrace();
							return false;
						}
						return true;
					}
				});

		// 同步失败.
		if (!success) {
			LogUtil.info(this.getClass().toString(), "菜单[" + menuName + "]"
					+ "数据同步过程中,数据库插入异常. 退出同步.");
		}

	}

}
