package com.bonc.mobile.hbmclient.terminal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.data.SQLHelper;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.DefaultEncrypt;
import com.bonc.mobile.hbmclient.util.NumberUtil;

public class TerminalHomePageDataLoad {
	/**
	 * 缓存首页数据.
	 */
	@SuppressWarnings("rawtypes")
	private static final Map dataCache = new HashMap();

	/**
	 * 获取终端销量统计数据
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map getTerminalSales() {
		return (Map) dataCache.get("terminalSale");
	}

	/**
	 * 获取智能机销量数据
	 */
	@SuppressWarnings({ "unchecked" })
	public static Map<String, String> getIntelligenceMacheine() {
		return (Map<String, String>) dataCache.get("intelligenceMacheineSale");
	}

	/**
	 * 获取非智能机销量数据
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getNonIntelligenceMacheineSale() {
		return (Map<String, String>) dataCache
				.get("nonIntelligenceMacheineSale");
	}

	/**
	 * 获取趋势图数据
	 */
	@SuppressWarnings("rawtypes")
	public static Map getTendencyData() {
		return (Map) dataCache.get("tendencyChart");
	}

	/**
	 * 获取终端排名
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getRanking() {
		return (List<Map<String, String>>) dataCache.get("ranking");
	}

	/**
	 * 获取合约计划分档数据
	 */

	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getPieOne() {
		return (List<Map<String, String>>) dataCache.get("contractplan");
	}

	/**
	 * 获取终端补贴价格分档数据
	 */

	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getallowrance() {
		return (List<Map<String, String>>) dataCache.get("allowrance");
	}

	/**
	 * 终端价格分档
	 */

	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getPieTwo() {
		return (List<Map<String, String>>) dataCache.get("pricebracket");
	}

	/**
	 * 五大市场占比
	 */

	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getFiveMarket() {
		return (List<Map<String, String>>) dataCache.get("fivemarket");
	}

	/**
	 * 新增存量占比
	 */

	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getNewRisePort() {
		return (List<Map<String, String>>) dataCache.get("newriseport");
	}

	/**
	 * 进货渠道分析
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getBuychannel() {

		return (List<Map<String, String>>) dataCache.get("buychannel");

	}

	/**
	 * 裸机合约机销售结构
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getBareContact() {

		return (List<Map<String, String>>) dataCache.get("barecontact");

	}

	/**
	 * 智能机普通级销售结构
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getIntelCommon() {

		return (List<Map<String, String>>) dataCache.get("intelcommon");

	}

	/**
	 * TD 2G 销售结构
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getTD2G() {

		return (List<Map<String, String>>) dataCache.get("td2g");

	}

	/**
	 * 自有 代理 销售结构
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getSelfProxy() {

		return (List<Map<String, String>>) dataCache.get("selfproxy");

	}

	/**
	 * 获取补贴数据
	 */

	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getButie() {
		return (List<Map<String, String>>) dataCache.get("butie");
	}

	/**
	 * 获取补贴数据
	 */

	@SuppressWarnings("unchecked")
	public static Map<String, String> getGoodsinout() {
		return (Map<String, String>) dataCache.get("goodsinout");
	}

	/**
	 * 销售渠道
	 */

	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getSalechannel() {
		return (List<Map<String, String>>) dataCache.get("salechannel");
	}

	/**
	 * 用户结构
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getUserstructure() {
		return (List<Map<String, String>>) dataCache.get("userstructure");
	}

	/**
	 * 加载首页数据
	 * 
	 * @param date
	 *            日期
	 * @param type
	 *            数据类型
	 * 
	 */
	/*
	 * public static void loadData(String date, int type) {
	 * 
	 * dataCache.clear(); // 清除缓存的数据 DataMessageControl dmc =
	 * DataMessageControl.getInstance(); if (dmc.getDataType() ==
	 * DataMessageControl.DATATYPE_DAY) {
	 * loadDayData(DataMessageControl.getInstance().getDateString1()); } else {
	 * loadMonthData(DataMessageControl.getInstance().getDateString1()); }
	 * 
	 * }
	 */

	/**
	 * 加载日数据
	 * 
	 * @param date
	 */
	@SuppressWarnings("unchecked")
	private static void loadDayData(String date) {

		StringBuilder builder = new StringBuilder();
		// 定义KPI code

		String database = " TERMINAL_DAILY_VALUE_A ";
		// 定义KPI code
		String dTotalKPI = "360"; // 日累计
		String dVolumeKPI = "340"; // 日销量

		String group = "570"; // 集团占比
		String groupnum = "560"; // 集团销量
		String m_h_end = "590"; // 中高端占比
		String m_h_endnum = "580"; // 中高端销量
		String college = "610"; // 高校占比
		String collegenum = "600"; // 高校销量
		String family = "630"; // 家庭
		String familynum = "620"; // 家庭销量
		String going = "650"; // 流动
		String goingnum = "640"; // 流动销量

		String newclient = "660"; // 新增客户
		String stableclient = "680"; // 存量客户
		String offlineclient = "700"; // 离网客户

		String newclientp = "670"; // 新增客户占比
		String stableclientp = "690"; // 存量客户占比
		String offlineclientp = "710"; // 离网客户占比

		String innum = "370"; // 进货量
		String outnum = "380"; // 库存
		String supportnum = "390"; // 支撑天数

		String baresale = "400"; // 裸机
		String barepropotion = "410";

		// 合约机
		String contactsale = "420";
		String contactpropotion = "430";
		// 智能机
		String intelligencesale = "440";
		String intelligencepropotion = "450";
		// 普通机
		String commonsale = "460";
		String commonpropotion = "470";
		// TD
		String tdsale = "480";
		String tdpropotion = "490";

		// 2g
		String g2sale = "500";
		String g2propotion = "510";
		// 自有
		String selfsale = "520";
		String selfpropotion = "530";

		// 代理
		String proxysale = "540";
		String proxypropotion = "550";

		// 终端排名
		String endrank1 = "720";
		String endrank2 = "730";
		String endrank3 = "740";
		String endrank4 = "750";
		String endrank5 = "760";
		String endrank6 = "770";
		String endrank7 = "780";
		String endrank8 = "790";
		String endrank9 = "800";
		String endrank10 = "810";

		// 加载终端销量统计数据
		Date curDate = DateUtil.getDate(date, "yyyyMMdd");

		dataCache.remove("terminalSale");
		Map comre = new HashMap();

		String preSql = builder
				.append("Select CURDAY_VALUE as ljz from TERMINAL_DAILY_VALUE_A WHERE KPI_CODE=? and OP_TIME = ?")
				.toString();
		Map result = new SQLHelper().queryForMap(preSql, new String[] {
				dTotalKPI, date });
		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				comre.put("ljz",
						DefaultEncrypt.decrypt((String) result.get("ljz")));
			} else {
				comre.put("ljz", result.get("ljz"));
			}

		}

		preSql = builder
				.delete(0, builder.length())
				.append("Select CURDAY_VALUE as num, CD_COL as tb,CD_MYOY as hb from TERMINAL_DAILY_VALUE_A WHERE KPI_CODE=? and OP_TIME = ?")
				.toString();
		result = new SQLHelper().queryForMap(preSql, new String[] { dVolumeKPI,
				date });
		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				comre.put("num",
						DefaultEncrypt.decrypt((String) result.get("num")));
				comre.put("tb",
						DefaultEncrypt.decrypt((String) result.get("tb")));
				comre.put("hb",
						DefaultEncrypt.decrypt((String) result.get("hb")));

			} else {
				comre.put("num", result.get("num"));
				comre.put("tb", result.get("tb"));
				comre.put("hb", result.get("hb"));
			}
		}

		if (comre.size() > 0) {
			dataCache.put("terminalSale", comre);
		}

		// 加载销量每日趋势图数据.
		if (result != null) {
			result.clear();
		}

		preSql = builder
				.delete(0, builder.length())
				.append("Select CURDAY_VALUE as num, CD_COL as hb,CD_MYOY as tb,OP_TIME as op_time from TERMINAL_DAILY_VALUE_A WHERE KPI_CODE=? and OP_TIME between ? and ?  order by  OP_TIME asc")
				.toString();

		// String this_month = date.substring(0,6);

		// 当前月每日销量
		// Date now = new Date();
		String curr_first_day = DateUtil.getFirstDay(curDate, "yyyyMMdd");
		String curr_last_day = DateUtil.getCurrentDay(curDate, "yyyyMMdd");

		String last_first_day = DateUtil.getLastMonthFirstDay(curDate,
				"yyyyMMdd");
		String last_last_day = DateUtil
				.getLastMonthLastDay(curDate, "yyyyMMdd");

		List<Map<String, String>> resultList = new SQLHelper().queryForList(
				preSql, new String[] { dVolumeKPI, curr_first_day,
						curr_last_day });
		// 上个月每日销量
		List<Map<String, String>> resultList2 = new SQLHelper().queryForList(
				preSql, new String[] { dVolumeKPI, last_first_day,
						last_last_day });

		int daynum = DateUtil.dayBetween(curr_first_day, curr_last_day,
				"yyyyMMdd") + 1;

		double[] nums = new double[daynum]; // 当月值
		double[] last_nums = new double[daynum]; // 上月同期值
		double[] this_hbs = new double[daynum]; // 当月环比

		String temptime = curr_first_day;
		String lasttemptime = last_first_day;
		int k = 0;

		if (resultList != null && resultList.size() > 0) {
			int len = resultList.size();

			for (int i = 0; i < len;) {

				String op_time = resultList.get(i).get("op_time");
				if (temptime.equals(op_time)) {
					if (Constant.IS_DATA_ENCRYPT) {
						nums[k] = NumberUtil.changeToDouble(DefaultEncrypt
								.decrypt(resultList.get(i).get("num")));
						this_hbs[k] = NumberUtil.changeToDouble(DefaultEncrypt
								.decrypt(resultList.get(i).get("hb")));
					} else {
						nums[k] = NumberUtil.changeToDouble(resultList.get(i)
								.get("num"));
						this_hbs[k] = NumberUtil.changeToDouble(resultList.get(
								i).get("hb"));
					}

					i++;
				} else {
					nums[k] = 0.0;
					this_hbs[k] = 0.0;
				}

				if (++k >= daynum) {
					break;
				}
				temptime = DateUtil.dayBefore(temptime, "yyyyMMdd", -1);

			}

			if (result == null) {
				result = new HashMap();
			}

			// 当月每日销量。
			result.put("numdouble", nums);
			result.put("numhb", this_hbs);

			k = 0;

			if (resultList2 != null && resultList2.size() > 0) {
				int len2 = resultList2.size();
				for (int i = 0; i < len && i < len2;) {
					String last_op_time = resultList2.get(i).get("op_time");
					if (lasttemptime.equals(last_op_time)) {
						if (Constant.IS_DATA_ENCRYPT) {
							last_nums[k] = NumberUtil
									.changeToDouble(DefaultEncrypt
											.decrypt(resultList2.get(i).get(
													"num")));
						} else {
							last_nums[k] = NumberUtil
									.changeToDouble(resultList2.get(i).get(
											"num"));
						}

						i++;
					} else {
						last_nums[k] = 0.0;
					}
					if (++k >= daynum) {
						break;
					}
					lasttemptime = DateUtil.dayBefore(lasttemptime, "yyyyMMdd",
							-1);

				}

			}
			// 去年同期每日销量
			result.put("tbdouble", last_nums);
		}

		if (result != null && result.size() > 0) {
			dataCache.put("tendencyChart", result);
		}

		// 分档sql,饼状图

		// 终端价格分档. ---> 五大市场占比
		List<Map<String, String>> pricebracketList = new ArrayList<Map<String, String>>();

		preSql = builder
				.delete(0, builder.length())
				.append("Select '集团' as analysisWay, CURDAY_VALUE as numZb from TERMINAL_DAILY_VALUE_A WHERE KPI_CODE = ? and  OP_TIME = ?")
				.toString();
		Map<String, String> groupResult = new SQLHelper().queryForMap(preSql,
				new String[] { group, date });

		if (groupResult != null) {

			if (Constant.IS_DATA_ENCRYPT) {
				groupResult.put("numZb", String.valueOf(NumberUtil
						.changeToDouble(DefaultEncrypt.decrypt(groupResult
								.get("numZb")), 0.0) * 100));
			} else {
				groupResult.put(
						"numZb",
						String.valueOf(NumberUtil.changeToDouble(
								groupResult.get("numZb"), 0.0) * 100));
			}
			pricebracketList.add(groupResult);
		}

		preSql = builder
				.delete(0, builder.length())
				.append("Select '中高端' as analysisWay, CURDAY_VALUE as numZb from TERMINAL_DAILY_VALUE_A WHERE KPI_CODE= ? and  OP_TIME = ?")
				.toString();
		Map<String, String> m_h_endResult = new SQLHelper().queryForMap(preSql,
				new String[] { m_h_end, date });

		if (m_h_endResult != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				m_h_endResult.put("numZb", String.valueOf(NumberUtil
						.changeToDouble(DefaultEncrypt.decrypt(m_h_endResult
								.get("numZb")), 0.0) * 100));
			} else {
				m_h_endResult.put(
						"numZb",
						String.valueOf(NumberUtil.changeToDouble(
								m_h_endResult.get("numZb"), 0.0) * 100));
			}
			pricebracketList.add(m_h_endResult);
		}

		preSql = builder
				.delete(0, builder.length())
				.append("Select '高校' as analysisWay, CURDAY_VALUE as numZb from TERMINAL_DAILY_VALUE_A WHERE KPI_CODE=? and  OP_TIME = ?")
				.toString();
		Map<String, String> collegeResult = new SQLHelper().queryForMap(preSql,
				new String[] { college, date });

		if (collegeResult != null) {

			double dvalue = 0.0;

			if (Constant.IS_DATA_ENCRYPT) {
				dvalue = NumberUtil
						.changeToDouble(DefaultEncrypt.decrypt(collegeResult
								.get("numZb")), 0.0);
			} else {
				dvalue = NumberUtil.changeToDouble(collegeResult.get("numZb"),
						0.0);
			}

			collegeResult.put("numZb", String.valueOf(dvalue * 100));

			pricebracketList.add(collegeResult);
		}

		preSql = builder
				.delete(0, builder.length())
				.append("Select '家庭' as analysisWay, CURDAY_VALUE as numZb from TERMINAL_DAILY_VALUE_A WHERE KPI_CODE= ? and  OP_TIME = ?")
				.toString();
		Map<String, String> familyResult = new SQLHelper().queryForMap(preSql,
				new String[] { family, date });

		if (familyResult != null) {
			double dvalue = 0.0;

			if (Constant.IS_DATA_ENCRYPT) {
				dvalue = NumberUtil.changeToDouble(
						DefaultEncrypt.decrypt(familyResult.get("numZb")), 0.0);
			} else {
				dvalue = NumberUtil.changeToDouble(familyResult.get("numZb"),
						0.0);
			}

			familyResult.put("numZb", String.valueOf(dvalue * 100));
			pricebracketList.add(familyResult);
		}

		preSql = builder
				.delete(0, builder.length())
				.append("Select '流动' as analysisWay, CURDAY_VALUE as numZb from TERMINAL_DAILY_VALUE_A WHERE KPI_CODE=? and  OP_TIME = ?")
				.toString();
		Map<String, String> goingResult = new SQLHelper().queryForMap(preSql,
				new String[] { going, date });

		if (goingResult != null) {
			double dvalue = 0.0;

			if (Constant.IS_DATA_ENCRYPT) {
				dvalue = NumberUtil.changeToDouble(
						DefaultEncrypt.decrypt(goingResult.get("numZb")), 0.0);
			} else {
				dvalue = NumberUtil.changeToDouble(goingResult.get("numZb"),
						0.0);
			}

			goingResult.put("numZb", String.valueOf(dvalue * 100));
			pricebracketList.add(goingResult);
		}

		dataCache.put("fivemarket", pricebracketList);

		// 新增存量占比

		List<Map<String, String>> newRiseList = new ArrayList<Map<String, String>>();

		preSql = builder
				.delete(0, builder.length())
				.append("Select '新增' as analysisWay, CURDAY_VALUE as numZb from TERMINAL_DAILY_VALUE_A WHERE KPI_CODE = ? and  OP_TIME = ?")
				.toString();
		Map<String, String> newclientResult = new SQLHelper().queryForMap(
				preSql, new String[] { newclientp, date });

		if (newclientResult != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				newclientResult.put("numZb",
						DefaultEncrypt.decrypt(newclientResult.get("numZb")));
			}
			newRiseList.add(newclientResult);
		}

		preSql = builder
				.delete(0, builder.length())
				.append("Select '存量' as analysisWay, CURDAY_VALUE as numZb from TERMINAL_DAILY_VALUE_A WHERE KPI_CODE = ? and  OP_TIME = ?")
				.toString();
		Map<String, String> stableclientResult = new SQLHelper().queryForMap(
				preSql, new String[] { stableclientp, date });

		if (stableclientResult != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				stableclientResult
						.put("numZb", DefaultEncrypt.decrypt(stableclientResult
								.get("numZb")));
			}
			newRiseList.add(stableclientResult);
		}

		preSql = builder
				.delete(0, builder.length())
				.append("Select '离网' as analysisWay, CURDAY_VALUE as numZb from TERMINAL_DAILY_VALUE_A WHERE KPI_CODE = ? and  OP_TIME = ?")
				.toString();
		Map<String, String> offlineclientResult = new SQLHelper().queryForMap(
				preSql, new String[] { offlineclientp, date });

		if (offlineclientResult != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				offlineclientResult.put("numZb", DefaultEncrypt
						.decrypt(offlineclientResult.get("numZb")));
			}
			newRiseList.add(offlineclientResult);
		}

		dataCache.put("newriseport", newRiseList);

		// 进货量 库存 支撑

		Map<String, String> inoutstatistic = new HashMap<String, String>();

		preSql = builder
				.delete(0, builder.length())
				.append("Select CURDAY_VALUE as innum from TERMINAL_DAILY_VALUE_A WHERE KPI_CODE = ? and  OP_TIME = ?")
				.toString();
		Map<String, String> inoutResult = new SQLHelper().queryForMap(preSql,
				new String[] { innum, date });

		if (inoutResult != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				inoutstatistic.put("innum",
						DefaultEncrypt.decrypt(inoutResult.get("innum")));
			} else {
				inoutstatistic.put("innum", inoutResult.get("innum"));
			}
		}

		preSql = builder
				.delete(0, builder.length())
				.append("Select CURDAY_VALUE as outnum from TERMINAL_DAILY_VALUE_A WHERE KPI_CODE = ? and  OP_TIME = ?")
				.toString();
		inoutResult = new SQLHelper().queryForMap(preSql, new String[] {
				outnum, date });
		if (inoutResult != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				inoutstatistic.put("outnum",
						DefaultEncrypt.decrypt(inoutResult.get("outnum")));
			} else {
				inoutstatistic.put("outnum", inoutResult.get("outnum"));
			}

		}

		preSql = builder
				.delete(0, builder.length())
				.append("Select CURDAY_VALUE as supportnum from TERMINAL_DAILY_VALUE_A WHERE KPI_CODE = ? and  OP_TIME = ?")
				.toString();
		inoutResult = new SQLHelper().queryForMap(preSql, new String[] {
				supportnum, date });
		if (inoutResult != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				inoutstatistic.put("supportnum",
						DefaultEncrypt.decrypt(inoutResult.get("supportnum")));
			} else {
				inoutstatistic.put("supportnum", inoutResult.get("supportnum"));
			}

		}

		dataCache.put("goodsinout", inoutstatistic);

		// 销售终端结构数据
		Map barestruct = new HashMap();
		preSql = builder
				.delete(0, builder.length())
				.append("Select  CURDAY_VALUE as num from TERMINAL_DAILY_VALUE_A WHERE KPI_CODE=? and  OP_TIME = ?")
				.toString();
		result = new SQLHelper().queryForMap(preSql, new String[] { baresale,
				date });

		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				barestruct.put("num",
						DefaultEncrypt.decrypt((String) result.get("num")));
			} else {
				barestruct.put("num", result.get("num"));
			}

		}

		String preSql2 = builder
				.delete(0, builder.length())
				.append("Select  CURDAY_VALUE as numZb from TERMINAL_DAILY_VALUE_A WHERE KPI_CODE=? and  OP_TIME = ?")
				.toString();
		result = new SQLHelper().queryForMap(preSql2, new String[] {
				barepropotion, date });

		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				barestruct.put("numZb",
						DefaultEncrypt.decrypt((String) result.get("numZb")));
			} else {
				barestruct.put("numZb", result.get("numZb"));
			}

		}

		List<Map> barecontact = new ArrayList<Map>();
		barecontact.add(barestruct);

		// 合约机
		Map contactstruct = new HashMap();
		result = new SQLHelper().queryForMap(preSql, new String[] {
				contactsale, date });

		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				contactstruct.put("num",
						DefaultEncrypt.decrypt((String) result.get("num")));
			} else {
				contactstruct.put("num", result.get("num"));
			}

		}

		result = new SQLHelper().queryForMap(preSql2, new String[] {
				contactpropotion, date });

		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				contactstruct.put("numZb",
						DefaultEncrypt.decrypt((String) result.get("numZb")));
			} else {
				contactstruct.put("numZb", result.get("numZb"));
			}

		}

		barecontact.add(contactstruct);
		dataCache.put("barecontact", barecontact);

		// 智能机
		Map intelligencestruct = new HashMap();
		result = new SQLHelper().queryForMap(preSql, new String[] {
				intelligencesale, date });

		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				intelligencestruct.put("num",
						DefaultEncrypt.decrypt((String) result.get("num")));
			} else {
				intelligencestruct.put("num", result.get("num"));
			}

		}

		result = new SQLHelper().queryForMap(preSql2, new String[] {
				intelligencepropotion, date });

		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				intelligencestruct.put("numZb",
						DefaultEncrypt.decrypt((String) result.get("numZb")));
			} else {
				intelligencestruct.put("numZb", result.get("numZb"));
			}

		}

		List<Map> intelcommon = new ArrayList<Map>();
		intelcommon.add(intelligencestruct);
		// 普通级
		Map commonstruct = new HashMap();
		result = new SQLHelper().queryForMap(preSql, new String[] { commonsale,
				date });

		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				commonstruct.put("num",
						DefaultEncrypt.decrypt((String) result.get("num")));
			} else {
				commonstruct.put("num", result.get("num"));
			}

		}

		result = new SQLHelper().queryForMap(preSql2, new String[] {
				commonpropotion, date });

		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				commonstruct.put("numZb",
						DefaultEncrypt.decrypt((String) result.get("numZb")));
			} else {
				commonstruct.put("numZb", result.get("numZb"));
			}

		}

		intelcommon.add(commonstruct);
		dataCache.put("intelcommon", intelcommon);
		// TD
		Map tdstruct = new HashMap();
		result = new SQLHelper().queryForMap(preSql, new String[] { tdsale,
				date });

		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				tdstruct.put("num",
						DefaultEncrypt.decrypt((String) result.get("num")));
			} else {
				tdstruct.put("num", result.get("num"));
			}

		}

		result = new SQLHelper().queryForMap(preSql2, new String[] {
				tdpropotion, date });

		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				tdstruct.put("numZb",
						DefaultEncrypt.decrypt((String) result.get("numZb")));
			} else {
				tdstruct.put("numZb", result.get("numZb"));
			}

		}

		List<Map> td2g = new ArrayList<Map>();
		td2g.add(tdstruct);

		// 2G
		Map g2struct = new HashMap();
		result = new SQLHelper().queryForMap(preSql, new String[] { g2sale,
				date });

		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				g2struct.put("num",
						DefaultEncrypt.decrypt((String) result.get("num")));
			} else {
				g2struct.put("num", result.get("num"));
			}

		}

		result = new SQLHelper().queryForMap(preSql2, new String[] {
				g2propotion, date });

		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				g2struct.put("numZb",
						DefaultEncrypt.decrypt((String) result.get("numZb")));
			} else {
				g2struct.put("numZb", result.get("numZb"));
			}

		}

		td2g.add(g2struct);
		dataCache.put("td2g", td2g);

		// 自有代理
		Map selfstruct = new HashMap();
		result = new SQLHelper().queryForMap(preSql, new String[] { selfsale,
				date });

		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				selfstruct.put("num",
						DefaultEncrypt.decrypt((String) result.get("num")));
			} else {
				selfstruct.put("num", result.get("num"));
			}

		}

		result = new SQLHelper().queryForMap(preSql2, new String[] {
				selfpropotion, date });

		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				selfstruct.put("numZb",
						DefaultEncrypt.decrypt((String) result.get("numZb")));
			} else {
				selfstruct.put("numZb", result.get("numZb"));
			}

		}

		List<Map> selfproxy = new ArrayList<Map>();
		selfproxy.add(selfstruct);

		// 代理
		Map proxystruct = new HashMap();
		result = new SQLHelper().queryForMap(preSql, new String[] { proxysale,
				date });

		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				proxystruct.put("num",
						DefaultEncrypt.decrypt((String) result.get("num")));
			} else {
				proxystruct.put("num", result.get("num"));
			}

		}

		result = new SQLHelper().queryForMap(preSql2, new String[] {
				proxypropotion, date });

		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				proxystruct.put("numZb",
						DefaultEncrypt.decrypt((String) result.get("numZb")));
			} else {
				proxystruct.put("numZb", result.get("numZb"));
			}

		}

		selfproxy.add(proxystruct);
		dataCache.put("selfproxy", selfproxy);

		// 终端排名

		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		List<Map> ranklist = new ArrayList<Map>();

		preSql = builder
				.delete(0, builder.length())
				.append(" Select  term_type_name as model,CURDAY_VALUE as ljz , CURDAY_VALUE_DR as ljzZb, tank_change as rankingChanges ")
				.append(" from ").append(database)
				.append(" WHERE KPI_CODE=? and  OP_TIME = ?").toString();

		result = new SQLHelper().queryForMap(preSql, new String[] { endrank1,
				date });

		if (result != null) {

			if (Constant.IS_DATA_ENCRYPT) {
				result.put("ljz",
						DefaultEncrypt.decrypt((String) result.get("ljz")));
				result.put("ljzZb",
						String.valueOf(decimalFormat.format(NumberUtil
								.changeToDouble(DefaultEncrypt
										.decrypt((String) result.get("ljzZb")),
										0.0) * 100)));
				result.put("rankingChanges", DefaultEncrypt
						.decrypt((String) result.get("rankingChanges")));
			} else {
				result.put("ljzZb", String.valueOf(decimalFormat
						.format(NumberUtil.changeToDouble(
								(String) result.get("ljzZb"), 0.0) * 100)));
			}

			ranklist.add(result);
		}

		result = new SQLHelper().queryForMap(preSql, new String[] { endrank2,
				date });

		if (result != null) {

			if (Constant.IS_DATA_ENCRYPT) {
				result.put("ljz",
						DefaultEncrypt.decrypt((String) result.get("ljz")));
				result.put("ljzZb",
						String.valueOf(decimalFormat.format(NumberUtil
								.changeToDouble(DefaultEncrypt
										.decrypt((String) result.get("ljzZb")),
										0.0) * 100)));
				result.put("rankingChanges", DefaultEncrypt
						.decrypt((String) result.get("rankingChanges")));
			} else {
				result.put("ljzZb", String.valueOf(decimalFormat
						.format(NumberUtil.changeToDouble(
								(String) result.get("ljzZb"), 0.0) * 100)));
			}

			ranklist.add(result);
		}

		result = new SQLHelper().queryForMap(preSql, new String[] { endrank3,
				date });

		if (result != null) {

			if (Constant.IS_DATA_ENCRYPT) {
				result.put("ljz",
						DefaultEncrypt.decrypt((String) result.get("ljz")));
				result.put("ljzZb",
						String.valueOf(decimalFormat.format(NumberUtil
								.changeToDouble(DefaultEncrypt
										.decrypt((String) result.get("ljzZb")),
										0.0) * 100)));
				result.put("rankingChanges", DefaultEncrypt
						.decrypt((String) result.get("rankingChanges")));
			} else {
				result.put("ljzZb", String.valueOf(decimalFormat
						.format(NumberUtil.changeToDouble(
								(String) result.get("ljzZb"), 0.0) * 100)));
			}

			ranklist.add(result);
		}

		result = new SQLHelper().queryForMap(preSql, new String[] { endrank4,
				date });

		if (result != null) {

			if (Constant.IS_DATA_ENCRYPT) {
				result.put("ljz",
						DefaultEncrypt.decrypt((String) result.get("ljz")));
				result.put("ljzZb",
						String.valueOf(decimalFormat.format(NumberUtil
								.changeToDouble(DefaultEncrypt
										.decrypt((String) result.get("ljzZb")),
										0.0) * 100)));
				result.put("rankingChanges", DefaultEncrypt
						.decrypt((String) result.get("rankingChanges")));
			} else {
				result.put("ljzZb", String.valueOf(decimalFormat
						.format(NumberUtil.changeToDouble(
								(String) result.get("ljzZb"), 0.0) * 100)));
			}
			ranklist.add(result);
		}

		result = new SQLHelper().queryForMap(preSql, new String[] { endrank5,
				date });

		if (result != null) {

			if (Constant.IS_DATA_ENCRYPT) {
				result.put("ljz",
						DefaultEncrypt.decrypt((String) result.get("ljz")));
				result.put("ljzZb",
						String.valueOf(decimalFormat.format(NumberUtil
								.changeToDouble(DefaultEncrypt
										.decrypt((String) result.get("ljzZb")),
										0.0) * 100)));
				result.put("rankingChanges", DefaultEncrypt
						.decrypt((String) result.get("rankingChanges")));
			} else {
				result.put("ljzZb", String.valueOf(decimalFormat
						.format(NumberUtil.changeToDouble(
								(String) result.get("ljzZb"), 0.0) * 100)));
			}

			ranklist.add(result);
		}

		result = new SQLHelper().queryForMap(preSql, new String[] { endrank6,
				date });

		if (result != null) {

			if (Constant.IS_DATA_ENCRYPT) {
				result.put("ljz",
						DefaultEncrypt.decrypt((String) result.get("ljz")));
				result.put("ljzZb",
						String.valueOf(decimalFormat.format(NumberUtil
								.changeToDouble(DefaultEncrypt
										.decrypt((String) result.get("ljzZb")),
										0.0) * 100)));
				result.put("rankingChanges", DefaultEncrypt
						.decrypt((String) result.get("rankingChanges")));
			} else {
				result.put("ljzZb", String.valueOf(decimalFormat
						.format(NumberUtil.changeToDouble(
								(String) result.get("ljzZb"), 0.0) * 100)));
			}
			ranklist.add(result);
		}

		result = new SQLHelper().queryForMap(preSql, new String[] { endrank7,
				date });

		if (result != null) {

			if (Constant.IS_DATA_ENCRYPT) {
				result.put("ljz",
						DefaultEncrypt.decrypt((String) result.get("ljz")));
				result.put("ljzZb",
						String.valueOf(decimalFormat.format(NumberUtil
								.changeToDouble(DefaultEncrypt
										.decrypt((String) result.get("ljzZb")),
										0.0) * 100)));
				result.put("rankingChanges", DefaultEncrypt
						.decrypt((String) result.get("rankingChanges")));
			} else {
				result.put("ljzZb", String.valueOf(decimalFormat
						.format(NumberUtil.changeToDouble(
								(String) result.get("ljzZb"), 0.0) * 100)));
			}
			ranklist.add(result);
		}

		result = new SQLHelper().queryForMap(preSql, new String[] { endrank8,
				date });

		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				result.put("ljz",
						DefaultEncrypt.decrypt((String) result.get("ljz")));
				result.put("ljzZb",
						String.valueOf(decimalFormat.format(NumberUtil
								.changeToDouble(DefaultEncrypt
										.decrypt((String) result.get("ljzZb")),
										0.0) * 100)));
				result.put("rankingChanges", DefaultEncrypt
						.decrypt((String) result.get("rankingChanges")));
			} else {
				result.put("ljzZb", String.valueOf(decimalFormat
						.format(NumberUtil.changeToDouble(
								(String) result.get("ljzZb"), 0.0) * 100)));
			}
			ranklist.add(result);
		}

		result = new SQLHelper().queryForMap(preSql, new String[] { endrank9,
				date });

		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				result.put("ljz",
						DefaultEncrypt.decrypt((String) result.get("ljz")));
				result.put("ljzZb",
						String.valueOf(decimalFormat.format(NumberUtil
								.changeToDouble(DefaultEncrypt
										.decrypt((String) result.get("ljzZb")),
										0.0) * 100)));
				result.put("rankingChanges", DefaultEncrypt
						.decrypt((String) result.get("rankingChanges")));
			} else {
				result.put("ljzZb", String.valueOf(decimalFormat
						.format(NumberUtil.changeToDouble(
								(String) result.get("ljzZb"), 0.0) * 100)));
			}
			ranklist.add(result);
		}

		result = new SQLHelper().queryForMap(preSql, new String[] { endrank10,
				date });

		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				result.put("ljz",
						DefaultEncrypt.decrypt((String) result.get("ljz")));
				result.put("ljzZb",
						String.valueOf(decimalFormat.format(NumberUtil
								.changeToDouble(DefaultEncrypt
										.decrypt((String) result.get("ljzZb")),
										0.0) * 100)));
				result.put("rankingChanges", DefaultEncrypt
						.decrypt((String) result.get("rankingChanges")));
			} else {
				result.put("ljzZb", String.valueOf(decimalFormat
						.format(NumberUtil.changeToDouble(
								(String) result.get("ljzZb"), 0.0) * 100)));
			}
			ranklist.add(result);
		}

		dataCache.put("ranking", ranklist);

	}

	private static void initTopDataM(List<Map> list, String kpicode,
			String database, String date) {
		StringBuilder builder = new StringBuilder();

		String preSql = builder
				.delete(0, builder.length())
				.append(" Select term_type_name as model,CURMONTH_VALUE as ljz , CURMONTH_VALUE_DR as ljzZb,tank_change as rankingChanges ")
				.append(" from ").append(database)
				.append(" WHERE KPI_CODE=? and  OP_TIME = ?").toString();

		Map<String, String> result = new SQLHelper().queryForMap(preSql,
				new String[] { kpicode, date });

		DecimalFormat decimalFormat = new DecimalFormat("0.00");

		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				result.put("ljz", DefaultEncrypt.decrypt(result.get("ljz")));
				result.put("ljzZb", String.valueOf(decimalFormat
						.format(NumberUtil.changeToDouble(
								DefaultEncrypt.decrypt(result.get("ljzZb")),
								0.0) * 100)));
				result.put("rankingChanges",
						DefaultEncrypt.decrypt(result.get("rankingChanges")));
			} else {
				result.put("ljzZb", String.valueOf(decimalFormat
						.format(NumberUtil.changeToDouble(result.get("ljzZb"),
								0.0) * 100)));
			}
			list.add(result);
		}

	}

	/**
	 * 加载月数据
	 * 
	 * @param date
	 */
	@SuppressWarnings("unchecked")
	private static void loadMonthData(String date) {
		StringBuilder builder = new StringBuilder();
		String database = " TERMINAL_MONTH_VALUE_A ";

		Date curDate = DateUtil.getDate(date, "yyyyMM");

		// 定义KPI code
		String mTotalKPI = "2800"; // 年销量累计
		String mVolumeKPI = "2780"; // 月销量

		// 价格分档
		String mPrice1KPI = "3010"; // TD终端价格分档<1000当月销量
		String mPrice2KPI = "3030"; // 1000-2000
		String mPrice3KPI = "3050"; // 2000-3000
		String mPrice4KPI = "3070"; // />3000

		// 终端排名
		String endrank1 = "3180";
		String endrank2 = "3190";
		String endrank3 = "3200";
		String endrank4 = "3210";
		String endrank5 = "3220";
		String endrank6 = "3230";
		String endrank7 = "3240";
		String endrank8 = "3250";
		String endrank9 = "3260";
		String endrank10 = "3270";

		// 销售结构
		String mRawKPI = "2840";
		String mRawpKPI = "2850";
		String mContactKPI = "2860";
		String mContactpKPI = "2870";
		String mItelligenceKPI = "2880";
		String mItelligencepKPI = "2890";
		String mCommonKPI = "2900";
		String mCommonpKPI = "2910";
		String mTDKPI = "2920";
		String mTDpKPI = "2930";
		String m2GKPI = "2940";
		String m2GpKPI = "2950";
		String mSelfKPI = "2960";
		String mSelfpKPI = "2970";
		String mProxyKPI = "2980";
		String mProxypKPI = "2990";

		// TD补贴价格分档占比
		String tdpricesupport1KPI = "3090"; // <500
		String tdpricesupport2KPI = "3110"; // 500-1000
		String tdpricesupport3KPI = "3130"; // 1000-2000
		String tdpricesupport4KPI = "3150"; // 2000 -3000
		String tdpricesupport5KPI = "3170"; // >3000

		String mCurMonActiveKPI = "2810"; // 当月累计激活量
		String provinceKPI = "2820"; // 省内窜出
		String provincialKPI = "2830"; // 省际窜出

		// 数据格式化.保留两位小数
		DecimalFormat decimalFormat = new DecimalFormat("0.00");

		// 加载终端销量统计数据
		dataCache.remove("terminalSale");
		Map<String, String> comre = new HashMap<String, String>();

		String preSql = builder.append("Select CURMONTH_VALUE as ljz from ")
				.append(database).append(" WHERE KPI_CODE=? and OP_TIME = ?")
				.toString();
		Map<String, String> result = new SQLHelper().queryForMap(preSql,
				new String[] { mTotalKPI, date });
		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				result.put("ljz", DefaultEncrypt.decrypt(result.get("ljz")));
			}
			comre.put("ljz", result.get("ljz"));
		}

		preSql = builder
				.delete(0, builder.length())
				.append("Select CURMONTH_VALUE as num, CM_COL as tb,CM_YOY as hb from ")
				.append(database).append(" WHERE KPI_CODE=? and OP_TIME = ?")
				.toString();
		result = new SQLHelper().queryForMap(preSql, new String[] { mVolumeKPI,
				date });
		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				comre.put("num", DefaultEncrypt.decrypt(result.get("num")));
				comre.put("tb", DefaultEncrypt.decrypt(result.get("tb")));
				comre.put("hb", DefaultEncrypt.decrypt(result.get("hb")));
			} else {
				comre.put("num", result.get("num"));
				comre.put("tb", result.get("tb"));
				comre.put("hb", result.get("hb"));
			}
		}

		if (comre.size() > 0) {
			dataCache.put("terminalSale", comre);
		}

		// 加载销量每月趋势图数据.
		if (result != null) {
			result.clear();
		} else {
			result = new HashMap();
		}
		preSql = builder
				.delete(0, builder.length())
				.append("Select CURMONTH_VALUE as num, CM_COL as tb,CM_YOY as hb,OP_TIME as op_time from ")
				.append(database)
				.append(" WHERE KPI_CODE=? and OP_TIME between ? and ?  order by  OP_TIME asc")
				.toString();

		String curr_first_month = DateUtil.getFirstMonth(curDate, "yyyyMM");

		// 当年每月销量
		List<Map<String, String>> resultList = new SQLHelper().queryForList(
				preSql, new String[] { mVolumeKPI, curr_first_month, date });
		// 上年每月销量
		String last_first_month = DateUtil.getFirstMonthLastyear(curDate,
				"yyyyMM");
		String last_curr_month = DateUtil.getMonthLastyear(curDate, "yyyyMM");
		List<Map<String, String>> resultList2 = new SQLHelper().queryForList(
				preSql, new String[] { mVolumeKPI, last_first_month,
						last_curr_month });

		int month_numbs = DateUtil.MonthBetween(curr_first_month, date,
				"yyyyMM") + 1;

		double[] nums = new double[month_numbs]; // 当月值
		double[] last_nums = new double[month_numbs]; // 上月同期值
		double[] this_hbs = new double[month_numbs]; // 当月环比

		int month_count = 0;

		Map<String, Object> objResult = new HashMap<String, Object>();
		if (resultList != null && resultList.size() > 0) {
			int len = resultList.size();

			for (int i = 0; i < len;) {
				if (curr_first_month.equals(resultList.get(i).get("op_time"))) {

					if (Constant.IS_DATA_ENCRYPT) {
						nums[month_count] = NumberUtil.changeToDouble(
								DefaultEncrypt.decrypt(resultList.get(i).get(
										"num")), 0.0);
						this_hbs[month_count] = NumberUtil
								.changeToDouble(DefaultEncrypt
										.decrypt(resultList.get(i).get("hb"))) * 100;
					} else {
						nums[month_count] = NumberUtil
								.changeToDouble(resultList.get(i).get("num"));
						this_hbs[month_count] = NumberUtil
								.changeToDouble(resultList.get(i).get("hb")) * 100;
					}

					i++;
				}

				if (++month_count >= month_numbs) {
					break;
				}
				curr_first_month = DateUtil.monthBefore(curr_first_month,
						"yyyyMM", -1);
			}

			objResult.put("numdouble", nums);
			objResult.put("numhb", this_hbs);

			month_count = 0;
			if (resultList2 != null && resultList2.size() > 0) {
				int len2 = resultList2.size();
				for (int i = 0; i < len && i < len2;) {
					if (last_first_month.equals(resultList2.get(i).get(
							"op_time"))) {
						if (Constant.IS_DATA_ENCRYPT) {
							last_nums[month_count] = NumberUtil
									.changeToDouble(DefaultEncrypt
											.decrypt(resultList2.get(i).get(
													"num")));
						} else {
							last_nums[month_count] = NumberUtil
									.changeToDouble(resultList2.get(i).get(
											"num"));
						}

						i++;
					}
					if (++month_count >= month_numbs) {
						break;
					}
					last_first_month = DateUtil.monthBefore(last_first_month,
							"yyyyMM", -1);
				}

			}

			objResult.put("tbdouble", last_nums);

		}

		if (objResult != null && objResult.size() > 0) {
			dataCache.put("tendencyChart", objResult);
		} else {
			objResult.put("numdouble", new Double[12]);
			objResult.put("numhb", new Double[12]);
			objResult.put("tbdouble", new Double[12]);
		}

		// TD终端价格分档
		List<Map<String, String>> pricebracketList = new ArrayList<Map<String, String>>();

		preSql = builder
				.delete(0, builder.length())
				.append("Select ? as analysisWay, CURMONTH_VALUE as numZb from")
				.append(database)
				.append(" WHERE KPI_CODE = ? and  OP_TIME = ?").toString();
		result = new SQLHelper().queryForMap(preSql, new String[] { "小于1000",
				mPrice1KPI, date });
		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				result.put("numZb", DefaultEncrypt.decrypt(result.get("numZb")));
			}
			pricebracketList.add(result);
		}

		result = new SQLHelper().queryForMap(preSql, new String[] {
				"1000-2000", mPrice2KPI, date });
		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				result.put("numZb", DefaultEncrypt.decrypt(result.get("numZb")));
			}
			pricebracketList.add(result);
		}

		result = new SQLHelper().queryForMap(preSql, new String[] {
				"2000-3000", mPrice3KPI, date });
		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				result.put("numZb", DefaultEncrypt.decrypt(result.get("numZb")));
			}
			pricebracketList.add(result);
		}

		result = new SQLHelper().queryForMap(preSql, new String[] { "大于3000",
				mPrice4KPI, date });
		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				result.put("numZb", DefaultEncrypt.decrypt(result.get("numZb")));
			}
			pricebracketList.add(result);
		}

		dataCache.put("pricebracket", pricebracketList);

		// 累计激活量 省内窜出 省际窜出

		List<Map> provincelist = new ArrayList<Map>();
		preSql = builder
				.delete(0, builder.length())
				.append("Select CM_YOY as ljzTb, CM_COL as num_Hb,CURMONTH_VALUE as num from ")
				.append(database)
				.append(" WHERE KPI_CODE = ? and  OP_TIME = ?").toString();
		result = new SQLHelper().queryForMap(preSql, new String[] {
				provinceKPI, date });
		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				result.put("ljzTb", DefaultEncrypt.decrypt(result.get("ljzTb")));
				result.put("num_Hb",
						DefaultEncrypt.decrypt(result.get("num_Hb")));
				result.put("num", DefaultEncrypt.decrypt(result.get("num")));
			}
			provincelist.add(result);
		}

		preSql = builder
				.delete(0, builder.length())
				.append("Select CM_YOY as ljzTb, CM_COL as num_Hb,CURMONTH_VALUE as num from ")
				.append(database)
				.append(" WHERE KPI_CODE = ? and  OP_TIME = ?").toString();
		result = new SQLHelper().queryForMap(preSql, new String[] {
				provincialKPI, date });
		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				result.put("ljzTb", DefaultEncrypt.decrypt(result.get("ljzTb")));
				result.put("num_Hb",
						DefaultEncrypt.decrypt(result.get("num_Hb")));
				result.put("num", DefaultEncrypt.decrypt(result.get("num")));
			}
			provincelist.add(result);
		}

		preSql = builder.delete(0, builder.length())
				.append("Select CURMONTH_VALUE as num from ").append(database)
				.append(" WHERE KPI_CODE = ? and  OP_TIME = ?").toString();
		result = new SQLHelper().queryForMap(preSql, new String[] {
				mCurMonActiveKPI, date });
		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				result.put("num", DefaultEncrypt.decrypt(result.get("num")));
			}
			provincelist.add(result);
		}

		dataCache.put("butie", provincelist);
		// 销售终端结构数据
		// 销售终端结构数据
		// 裸机

		List<Map> barecontact = new ArrayList<Map>();
		// 裸机
		initMonthSaleStructData(date, mRawKPI, mRawpKPI, database, barecontact);
		// 合约机
		initMonthSaleStructData(date, mContactKPI, mContactpKPI, database,
				barecontact);

		dataCache.put("barecontact", barecontact);

		List<Map> intlcommon = new ArrayList<Map>();
		// 智能机
		initMonthSaleStructData(date, mItelligenceKPI, mItelligencepKPI,
				database, intlcommon);
		// 普通级
		initMonthSaleStructData(date, mCommonKPI, mCommonpKPI, database,
				intlcommon);

		dataCache.put("intelcommon", intlcommon);

		List<Map> td2g = new ArrayList<Map>();
		// 裸机
		initMonthSaleStructData(date, mTDKPI, mTDpKPI, database, td2g);
		// 合约机
		initMonthSaleStructData(date, m2GKPI, m2GpKPI, database, td2g);

		dataCache.put("td2g", td2g);

		List<Map> selfproxy = new ArrayList<Map>();
		// 裸机
		initMonthSaleStructData(date, mSelfKPI, mSelfpKPI, database, selfproxy);
		// 合约机
		initMonthSaleStructData(date, mProxyKPI, mProxypKPI, database,
				selfproxy);

		dataCache.put("selfproxy", selfproxy);

		// 终端排名TOP

		List<Map> ranklist = new ArrayList<Map>();

		initTopDataM(ranklist, endrank1, database, date);
		initTopDataM(ranklist, endrank2, database, date);
		initTopDataM(ranklist, endrank3, database, date);
		initTopDataM(ranklist, endrank4, database, date);
		initTopDataM(ranklist, endrank5, database, date);
		initTopDataM(ranklist, endrank6, database, date);
		initTopDataM(ranklist, endrank7, database, date);
		initTopDataM(ranklist, endrank8, database, date);
		initTopDataM(ranklist, endrank9, database, date);
		initTopDataM(ranklist, endrank10, database, date);

		dataCache.put("ranking", ranklist);

		List<Map<String, String>> pricesupportList = new ArrayList<Map<String, String>>();

		preSql = builder
				.delete(0, builder.length())
				.append("Select ? as analysisWay, CURMONTH_VALUE as numZb from ")
				.append(database)
				.append(" WHERE KPI_CODE = ? and  OP_TIME = ?").toString();
		result = new SQLHelper().queryForMap(preSql, new String[] { "小于500",
				tdpricesupport1KPI, date });
		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				result.put("numZb", DefaultEncrypt.decrypt(result.get("numZb")));
			}
			pricesupportList.add(result);
		}

		result = new SQLHelper().queryForMap(preSql, new String[] { "500-1000",
				tdpricesupport2KPI, date });
		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				result.put("numZb", DefaultEncrypt.decrypt(result.get("numZb")));
			}
			pricesupportList.add(result);
		}

		result = new SQLHelper().queryForMap(preSql, new String[] {
				"1000-2000", tdpricesupport3KPI, date });
		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				result.put("numZb", DefaultEncrypt.decrypt(result.get("numZb")));
			}
			pricesupportList.add(result);
		}

		result = new SQLHelper().queryForMap(preSql, new String[] {
				"2000-3000", tdpricesupport4KPI, date });
		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				result.put("numZb", DefaultEncrypt.decrypt(result.get("numZb")));
			}
			pricesupportList.add(result);
		}

		result = new SQLHelper().queryForMap(preSql, new String[] { "大于3000",
				tdpricesupport5KPI, date });
		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				result.put("numZb", DefaultEncrypt.decrypt(result.get("numZb")));
			}
			pricesupportList.add(result);
		}

		dataCache.put("allowrance", pricesupportList);

	}

	private static void initMonthSaleStructData(String date, String salekpi,
			String propotionkpi, String database, List<Map> list) {
		Map barestruct = new HashMap();
		StringBuilder builder = new StringBuilder();
		String preSql = builder.delete(0, builder.length())
				.append("Select  CURMONTH_VALUE as num from").append(database)
				.append(" WHERE KPI_CODE=? and  OP_TIME = ?").toString();
		Map result = new SQLHelper().queryForMap(preSql, new String[] {
				salekpi, date });

		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				barestruct.put("num",
						DefaultEncrypt.decrypt((String) result.get("num")));
			} else {
				barestruct.put("num", result.get("num"));
			}

		}

		String preSql2 = builder.delete(0, builder.length())
				.append("Select  CURMONTH_VALUE as numZb from ")
				.append(database).append(" WHERE KPI_CODE=? and  OP_TIME = ?")
				.toString();
		result = new SQLHelper().queryForMap(preSql2, new String[] {
				propotionkpi, date });

		if (result != null) {
			if (Constant.IS_DATA_ENCRYPT) {
				barestruct.put("numZb",
						DefaultEncrypt.decrypt((String) result.get("numZb")));
			} else {
				barestruct.put("numZb", result.get("numZb"));
			}

		}

		list.add(barestruct);
	}

}
