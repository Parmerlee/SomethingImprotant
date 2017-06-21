package com.bonc.mobile.hbmclient.data;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.bonc.mobile.hbmclient.activity.BaseActivity;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.common.Publicapp;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.DefaultEncrypt;
import com.bonc.mobile.hbmclient.util.HttpUtil;
import com.bonc.mobile.hbmclient.util.LogUtil;
import com.bonc.mobile.hbmclient.util.StringUtil;

public class MenuDataSyncThread extends Thread {

	private List<Map<String, String>> menuList;
	private BaseActivity mactivity;
	private static boolean isInterupt = false;
	private static boolean isSynchring = false;
	private boolean isCheck = false;

	public MenuDataSyncThread(BaseActivity mactivity,
			List<Map<String, String>> menuList) {
		this.mactivity = mactivity;
		this.menuList = menuList;
	}

	@Override
	public void run() {
		super.run();

		if (menuList == null)
			return;
		// 如果步存在没结束的同步. 则半秒检查一次.
		while (isSynchring()) {
			try {
				sleep(500); // 每次等待半秒.
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		int len = menuList.size();
		for (int i = 0; i < len; i++) {
			if (isInterupt()) {
				break;
			}

			setSynchring(true);
			if (mactivity != null) {
				mactivity.refreshListView(i, 1);
			}
			// 同步菜单数据.
			doSecondMenuDataSync(menuList.get(i), i);
			// 检查是否有数据需要重新同步
			checkMenuDataRollback(menuList.get(i));
			if (mactivity != null) {
				mactivity.refreshListView(i, 0);
			}
			setSynchring(false);

		}

		if (isSynchring()) {
			setSynchring(false);
		}
		if (isInterupt()) {
			setInterupt(false);
		} else {
			// 表示同步结束.
			if (mactivity != null) {
				mactivity.refreshListView(-1, -1);
			}
		}

	}

	/**
	 * 开始数据同步.
	 * 
	 * @param index
	 */
	public void doSecondMenuDataSync(Map<String, String> menuinfo, int index) {
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
			LogUtil.error(this.getClass().toString(), "没用查询到用户信息,退出同步!!");
			return;
		}

		if (StringUtil.isNull(userInfo.get("usercode"))) {
			LogUtil.error(this.getClass().toString(), "没用查询到用户账号,退出同步!!");
			return;
		}

		String areaId = bd.getUserInfo().get("areaId");
		if (areaId == null || "".equals(areaId)) {
			LogUtil.error(this.getClass().toString(), "菜单[" + menuName
					+ "]用户没有归属地区.同步结束.");
			return;
		}

		// 获取所有的列
		String colums = bd.getMenuAllColumnString(menuCode);
		if (colums == null || "".equals(colums)) {
			LogUtil.info(MenuDataSyncThread.this.toString(), "菜单[" + menuName
					+ "]没有配置列，不在继续同步数据!");
			return;
		}
		// 数据类型.
		String dataType = menuinfo.get("dataType");
		if (dataType == null || "".equals(dataType)) {
			LogUtil.info(MenuDataSyncThread.this.toString(), "菜单[" + menuName
					+ "]没有配置数据类型");
			return;
		}

		// 获取菜单下面所有的指标
		String kpicodes = bd.getMenuAllKpiString(menuCode);
		if (kpicodes == null || "".equals(kpicodes)) {
			LogUtil.info(MenuDataSyncThread.this.toString(), "菜单[" + menuName
					+ "]下面没有kpi指标.");
			return;
		}

		// 获取server端存储数据的表
		String dataTable = bd.getMenuColDataTable(menuCode);

		if (dataTable == null || "".equals(dataTable)) {
			LogUtil.info(MenuDataSyncThread.this.toString(), "菜单[" + menuName
					+ "]没有查到server端的数据表.");
			return;
		}

		// 获取符合维度
		// String complexDimKey = bd.getMenuComplexDimKey(menuinfo);
		ComplexDimInfo complexDimKey = bd.getMenuComplexDimKey(menuinfo);
		if ("error".equalsIgnoreCase(complexDimKey.getIsMenuCofingRight())) {
			LogUtil.info(MenuDataSyncThread.this.toString(), "菜单[" + menuName
					+ "]查询复合维度出错，检查菜单配置情况.");
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
				LogUtil.debug(this.getName(), "数据已更新至最新日期" + lastTime
						+ ".不需要向server端同步.");
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

			if (json_reply == null || "".equals(json_reply)) {

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

			final JSONArray jsArrayFinal = jsonArray;
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

				if (mactivity != null) {
					mactivity.refreshListView(index, 1);
				}

				if (!success) {
					break;
				}

			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.info(this.getClass().toString(), "菜单[" + menuName + "]"
						+ "数据同步过程中,数据库插入异常. 退出同步.");
				break;
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

	public void checkMenuDataRollback(Map<String, String> menuinfo) {
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
			LogUtil.error(this.getName(),
					"数据更新返回json串转化为对象的时候出现异常.结束更新！ json串:" + jsonString);
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
			LogUtil.info(MenuDataSyncThread.this.toString(), "菜单[" + menuName
					+ "]没有配置列，不在继续同步数据!");
			return;
		}
		// 数据类型.
		String dataType = menuinfo.get("dataType");
		if (dataType == null || "".equals(dataType)) {
			LogUtil.info(MenuDataSyncThread.this.toString(), "菜单[" + menuName
					+ "]没有配置数据类型");
			return;
		}

		// 用户信息
		String areaId = bd.getUserInfo().get("areaId");
		if (areaId == null || "".equals(areaId)) {
			LogUtil.error(this.getClass().toString(), "菜单[" + menuName
					+ "]用户没有归属地区.同步结束.");
			return;
		}

		// 获取菜单下面所有的指标
		String kpicodes = bd.getMenuAllKpiString(menuCode);
		if (kpicodes == null || "".equals(kpicodes)) {
			LogUtil.info(MenuDataSyncThread.this.toString(), "菜单[" + menuName
					+ "]下面没有kpi指标.");
			return;
		}

		// 获取server端存储数据的表
		String dataTable = bd.getMenuColDataTable(menuCode);

		if (dataTable == null || "".equals(dataTable)) {
			LogUtil.info(MenuDataSyncThread.this.toString(), "菜单[" + menuName
					+ "]没有查到server端的数据表.");
			return;
		}

		// 获取符合维度
		ComplexDimInfo complexDimKey = bd.getMenuComplexDimKey(menuinfo);
		if ("error".equalsIgnoreCase(complexDimKey.getIsMenuCofingRight())) {
			LogUtil.info(MenuDataSyncThread.this.toString(), "菜单[" + menuName
					+ "]查询复合维度出错，检查菜单配置情况.");
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
				LogUtil.error(this.getName(),
						"返回的menuCode为空!或者与本次的menucode对不上! 返回的menuCode:["
								+ mcode + "]  本地的menucode:[" + menuCode + "]");
				continue;
			}

			String startt = jObject.optString("ref_start_time");
			String endt = jObject.optString("ref_end_time");
			String mid = jObject.optString("flow_id");

			if (StringUtil.isNull(mid)) {
				LogUtil.error(this.getName(), "markid为空!跳出本次同步  执行下一次同步.");
				continue;
			}

			if (StringUtil.isNull(startt)) {
				LogUtil.error(this.getName(), "数据开始的日期时间为空. 跳出本次同步.");
				continue;
			}

			if (StringUtil.isNull(endt)) {
				LogUtil.error(this.getName(), "数据结束的日期时间为空. 跳出本次同步.");
				continue;
			}

			try {
				int tmpstime = Integer.parseInt(startt);
				int tmpetime = Integer.parseInt(endt);
				int tmpmarkid = Integer.parseInt(mid);

				if (tmpstime < starttime) {
					starttime = tmpstime;
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

	/**
	 * 是否中断.
	 * 
	 * @return
	 */
	public synchronized static boolean isInterupt() {
		return isInterupt;
	}

	public synchronized static void setInterupt(boolean isInterupt) {
		MenuDataSyncThread.isInterupt = isInterupt;
	}

	public synchronized static boolean isSynchring() {
		return isSynchring;
	}

	public synchronized static void setSynchring(boolean isSynchring) {
		MenuDataSyncThread.isSynchring = isSynchring;
	}

}
