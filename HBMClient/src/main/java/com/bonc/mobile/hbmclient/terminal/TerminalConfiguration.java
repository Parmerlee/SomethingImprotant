/**
 * TODO
 */
package com.bonc.mobile.hbmclient.terminal;

/**
 * @author liweigao
 * 
 */
public class TerminalConfiguration {
	public final static String KEY_MAP = "key_map";
	public final static String KEY_OPTIME = "optime";
	public final static String KEY_DATA_TYPE = "datatype";
	public final static String KEY_MENU_CODE = "key_menu_code";
	public final static String KEY_KPI_CODES = "kpicodes";
	public final static String KEY_KPI_CODES_ARRAY = "kpicodes_array";
	public final static String KEY_COLUMN_NAME = "cols";
	public final static String KEY_DATATABLE = "datatable";
	public final static String KEY_AREA_CODE = "areacode";
	public final static String KEY_DIM_KEY = "dimkey";
	public final static String KEY_ISEXPAND = "isexpand";
	public final static String KEY_PART3TODATA = "part3todata";
	public final static String KEY_ACTION = "key_action";
	public final static String KEY_ACTIVITY_ENUM = "key_activity_enum";
	public final static String KEY_RESPONSE_KEY = "key_response_key";
	public final static String KEY_COLUNM_KPI_CODE = "key_column_kpi_code";

	// 图表key
	public final static String KEY_MAP_CHART = "key_map_chart";
	public final static String KEY_KPI_CODE = "kpicode";

	public final static String KEY_MENU_CODE_PSS_DAY = "134";
	public final static String KEY_MENU_CODE_PSS_DAY_2 = "148";
	public final static String KEY_MENU_CODE_UNSALE_DAY = "135";
	public final static String KEY_MENU_CODE_UNPACK_DAY = "136";

	public final static String KEY_MENU_CODE_PSS_MONTH = "138";
	public final static String KEY_MENU_CODE_PSS_MONTH_2 = "150";
	public final static String KEY_MENU_CODE_UNSALE_MONTH = "139";
	public final static String KEY_MENU_CODE_UNPACK_MONTH = "140";
	public final static String KEY_MENU_CODE_FG_MONTH = "137";
	public final static String KEY_MENU_CODE_FG_MONTH_2 = "149";

	// 进销存日的top20.
	public final static String KEY_MENU_CODE_TOP20_PSS_DAY = "141";
	public final static String KEY_MENU_CODE_TOP20_UNSALE_DAY = "142";
	public final static String KEY_MENU_CODE_TOP20_UNPACK_DAY = "143";

	public final static String KEY_MENU_CODE_TOP20_PSS_MONTH = "145";
	public final static String KEY_MENU_CODE_TOP20_UNSALE_MONTH = "146";
	public final static String KEY_MENU_CODE_TOP20_UNPACK_MONTH = "147";
	public final static String KEY_MENU_CODE_TOP20_FG_MONTH = "144";

	// 终端分析
	public final static String KEY_MENU_CODE_TA_DAY = "250";

	// top20 column name
	public final static String DIV = "|";
	public final static String DIV0 = "\\|";
	public final static String SEM = ";";
	public final static String TITLE_BIG = "title_big";
	public final static String TITLE_COLUMN = "title_column";
	public final static String TITLE_GROUP = "title_group";

	public final static String TERM_TYPE_NAME = "term_type_name";
	public final static String CURDAY_VALUE = "curday_value";
	public final static String CURDAY_VALUE_DR = "curday_value_dr";
	public final static String TANK_CHANGE = "tank_change";
	public final static String PM_CURDAY_VALUE = "pm_curday_value";
	public final static String PM_CURDAY_VALUE_DR = "pm_curday_value_dr";
	public final static String CM_COL = "cm_col";
	public final static String CM_YOY = "cm_yoy";
	public final static String TOP20_COLUMN_NAME_DAY = CURDAY_VALUE + DIV
			+ CURDAY_VALUE_DR + DIV + TANK_CHANGE + DIV + PM_CURDAY_VALUE + DIV
			+ PM_CURDAY_VALUE_DR;

	public final static String CURMONTH_VALUE = "curmonth_value";
	public final static String CURMONTH_VALUE_DR = "curmonth_value_dr";
	public final static String PREMONTH_VALUE = "premonth_value";
	public final static String PREMONTH_VALUE_DR = "premonth_value_dr";
	public final static String TOP20_COLUMN_NAME_MONTH = CURMONTH_VALUE + DIV
			+ CURMONTH_VALUE_DR + DIV + TANK_CHANGE + DIV + PREMONTH_VALUE
			+ DIV + PREMONTH_VALUE_DR;

	public final static String CD_COL = "cd_col";
	public final static String CD_MYOY = "cd_myoy";
	public final static String CD_YYOY = "cd_yyoy";

	// 当日终端销量指标.
	public final static String KPI_PSS_DAY_SALE = "340";
	// 当月终端累计销量
	public final static String KPI_PSS_MONTH_LJ_SALE = "360";
	// 当月终端进货
	public final static String KPI_PSS_TERMINAL_JH_COUNT = "370";
	// 库存量
	public final static String KPI_PSS_TERMINAL_KC_COUNT = "380";
	// 支撑天数
	public final static String KPI_PSS_TERMINAL_ZC_DAY = "390";
	// 新机当月累计销量
	public final static String KPI_PSS_TERMINAL_XJ_COUNT = "4600";
	// 库存当月销量
	public final static String KPI_PSS_TERMINAL_KCS_COUNT = "4610";
	// 裸机
	public final static String KPI_PSS_TERMINAL_LJ_COUNT = "400";
	// 合约机
	public final static String KPI_PSS_TERMINAL_HYJ_COUNT = "420";
	// 智能机
	public final static String KPI_PSS_TERMINAL_ZNJ_COUNT = "440";
	// 普通机
	public final static String KPI_PSS_TERMINAL_PTJ_COUNT = "460";
	// td终端
	public final static String KPI_PSS_TERMINAL_TD_COUNT = "480";
	// 2G终端
	public final static String KPI_PSS_TERMINAL_2G_COUNT = "500";
	// 自有资源
	public final static String KPI_PSS_TERMINAL_ZY_COUNT = "520";
	// 代理资源
	public final static String KPI_PSS_TERMINAL_DL_COUNT = "540";
	// 当月窜货两
	public final static String KPI_5680 = "5680";
	// 当月激活量.
	public final static String KPI_5720 = "5720";
	// 当月窜货率(当月省内窜出率)
	public final static String KPI_5930 = "5930";

	// 省内窜出
	public final static String KPI_5690 = "5690";
	// 省际窜出
	public final static String KPI_5700 = "5700";
	// 升级窜入
	public final static String KPI_5710 = "5710";

	// 当日机卡分离量
	public final static String KPI_4910 = "4910";
	// 当日机卡分离率
	public final static String KPI_4920 = "4920";
	// 当日话费终止返还量
	public final static String KPI_4930 = "4930";
	// 当月机卡分离量
	public final static String KPI_5450 = "5450";
	// 当月机卡分离率
	public final static String KPI_5460 = "5460";
	// 当月话费终止返还量
	public final static String KPI_5470 = "5470";
	// 当月激活率
	public final static String KPI_5980 = "5980";
	// 当月省内窜入量
	public final static String KPI_6010 = "6010";
	// 当月省内窜入率
	public final static String KPI_6020 = "6020";
	// 疑似省际窜出率
	public final static String KPI_6030 = "6030";

	// ********************
	// 进销存日，环图
	public final static String KPI_PSS_DAY_RINGCHART = "4570|4580|4590";
	public final static String TITLE_COLUMN_PSS_DAY_RINGCHART = "区域,全球通,动感地带,神州行";
	public final static String RESPONSE_PSS_DAY_RINGCHART_KEY = "area_name|,curday_value_dr_4570|%,curday_value_dr_4580|%,curday_value_dr_4590|%";
	public final static String COLUMN_KPI_CODE_PSS_DAY_RINGCHART = "4570,4580,4590";

	// 进销存日，八大渠道占比
	public final static String KPI_PSS_DAY_8 = "4490|4500|4510|4520|4530|4540|4550|4560";
	public final static String TITLE_COLUMN_PSS_DAY_8 = "区域,专营店,自营厅,其他渠道,代理点,24H自助,他营厅,村代理点,服务站";
	public final static String RESPONSE_PSS_DAY_8_KEY = "area_name|,curday_value_dr_4520|%,curday_value_dr_4490|%,curday_value_dr_4560|%,curday_value_dr_4530|%,curday_value_dr_4500|%,curday_value_dr_4510|%,curday_value_dr_4550|%,curday_value_dr_4540|%";
	public final static String COLUMN_KPI_CODE_PSS_DAY_8 = "4520,4490,4560,4530,4500,4510,4550,4540";

	// 进销存月，价格分档
	public final static String KPI_PSS_MONTH_RATE_TRANCHE = "3000|3020|3040|3060";
	public final static String TITLE_COLUMN_PSS_MONTH_RATE_TRANCHE = "区域,1k以下,1k-2k,2k-3k,3k以上";
	public final static String RESPONSE_PSS_MONTH_RATE_TRANCHE = "area_name|,curmonth_value_dr_3000|%,curmonth_value_dr_3020|%,curmonth_value_dr_3040|%,curmonth_value_dr_3060|%";
	public final static String COLUMN_KPI_CODE_PM_RATE_TRANCHE = "3000,3020,3040,3060";
	// 进销存月，补贴分档
	public final static String KPI_PSS_MONTH_SUBSIDY_TRANCHE = "3080|3100|3120|3140|3160";
	public final static String TITLE_COLUMN_PSS_MONTH_SUBSIDY_TRANCHE = "区域,0.5k以下,0.5k-1k,1k-2k,2k-3k,3k以上";
	public final static String RESPONSE_PSS_MONTH_SUBSIDY_TRANCHE = "area_name|,curmonth_value_dr_3080|%,curmonth_value_dr_3100|%,curmonth_value_dr_3120|%,curmonth_value_dr_3140|%,curmonth_value_dr_3160|%";
	public final static String COLUMN_KPI_CODE_PM_SUBSIDY_TRANCHE = "3080,3100,3120,3140,3160";
	// 滞销日，直销周期占比
	public final static String KPI_UNSALE_DAY_PIE_PERIOD = "4630|4640|4650|4660";
	public final static String TITLE_COLUMN_UNSALE_DAY_PIE_PERIOD = "区域,7-12个月,13-18个月,19-24个月,大于24个月";
	public final static String RESPONSE_UNSALE_DAY_PIE_PERIOD = "area_name|,curday_value_dr_4630|%,curday_value_dr_4640|%,curday_value_dr_4650|%,curday_value_dr_4660|%";
	public final static String COLUMN_KPI_CODE_UNSALE_DAY_PIE_PERIOD = "4630,4640,4650,4660";
	// 滞销月，直销周期占比
	public final static String KPI_UNSALE_MONTH_PIE_PERIOD = "5170|5180|5190|5200";
	public final static String TITLE_COLUMN_UNSALE_MONTH_PIE_PERIOD = "区域,7-12个月,13-18个月,19-24个月,大于24个月";
	public final static String RESPONSE_UNSALE_MONTH_PIE_PERIOD = "area_name|,curmonth_value_dr_5170|%,curmonth_value_dr_5180|%,curmonth_value_dr_5190|%,curmonth_value_dr_5200|%";
	public final static String COLUMN_KPI_CODE_UNSALE_MONTH_PIE_PERIOD = "5170,5180,5190,5200";
	// **************************

	// ************************************
	// 进销存日，进货、库存
	public final static String KPI_PSS_DAY_IN_STORE = "370|380|390";
	public final static String TITLE_COLUMN_PSS_DAY_IN_STROE = "区域,进货量,库存量,支撑天数";
	public final static String RESPONSE_PSS_DAY_IN_STORE_KEY = "area_name|,curday_value_370|台,curday_value_380|台,curday_value_390|天";
	public final static String COLUMN_KPI_CODE_PSS_DAY_IN_STOR = "370,380,390";

	// 进销存日，销量
	public final static String KPI_PSS_DAY_6ROW = "340|360";
	public final static String COLUMN_PSS_DAY_6ROW = CURDAY_VALUE + DIV
			+ CD_COL + DIV + CD_MYOY;
	public final static String TITLE_COLUMN_PSS_DAY_6ROW = "区域,日销量,日环比,月累计销量,月同比";
	public final static String RESPONSE_PSS_DAY_6ROW = "area_name|,curday_value_340|台,cd_col_340|+,curday_value_360|台,cd_myoy_360|+";
	public final static String COLUMN_KPI_CODE_PSS_DAY = "340,340,360,360";

	// 进销存月,销量
	public final static String KPI_PSS_MONTH_SALE_COUNT = "2780|2800";
	public final static String COLUMN_PSS_MONTH_SALE_COUNT = CURMONTH_VALUE
			+ DIV + CM_COL;
	public final static String TITLE_COLUMN_PSS_MONTH_SALE_COUNT = "区域,月销量,环比,年累计销量,环比";
	public final static String RESPONSE_PSS_MONTH_SALE_COUNT = "area_name|,curmonth_value_2780|台,cm_col_2780|+,curmonth_value_2800|台,cm_col_2800|+";
	public final static String COLUMN_KPI_CODE_PSS_MONTH_SALE_COUNT = "2780,2800,2800";
	// 滞销日，滞销量
	public final static String KPI_UNSALE_DAY_COUNT = "4620|5990";
	public final static String COLUMN_UNSALE_DAY_COUNT = CURDAY_VALUE + DIV
			+ CD_MYOY;
	public final static String TITLE_COLUMN_UNSALE_DAY_COUNT = "区域,日滞销量,月同比,滞销率";
	public final static String RESPONSE_UNSALE_DAY_COUNT = "area_name|,curday_value_4620|台,cd_myoy_4620|+,curday_value_5990|%";
	public final static String COLUMN_KPI_CODE_UNSALE_DAY_COUNT = "4620,4620,5990";
	// 滞销月，滞销量
	public final static String KPI_UNSALE_MONTH_COUNT = "5160|6000";
	public final static String COLUMN_UNSALE_MONTH_COUNT = CURMONTH_VALUE + DIV
			+ CM_COL;
	public final static String TITLE_COLUMN_UNSALE_MONTH_COUNT = "区域,月滞销量,环比,滞销率";
	public final static String RESPONSE_UNSALE_MONTH_COUNT = "area_name|,curmonth_value_5160|台,cm_col_5160|+,curmonth_value_6000|%";
	public final static String COLUMN_KPI_CODE_UNSALE_MONTH_COUN = "5160,5160,6000";
	// 拆包日，返回量，分离量
	public final static String KPI_UNPACK_DAY_CHARGE_UIM = "4910|4920|4930";
	public final static String TITLE_COLUMN_UNPACK_DAY_CHARGE_UIM = "区域,机卡分离量,机卡分离率,话费返还量";
	public final static String RESPONSE_UNPACK_DAY_CHARGE_UIM = "area_name|,curday_value_4910|台,curday_value_4920|%,curday_value_4930|台";
	public final static String COLUMN_KPI_CODE_UNPACK_DAY_CHARGE_UIM = "4910,4920,4930";
	// 拆包月，返回量，分离量
	public final static String KPI_UNPACK_MONTH_CHARGE_UIM = "5450|5460|5470";
	public final static String TITLE_COLUMN_UNPACK_MONTH_CHARGE_UIM = "区域,机卡分离量,机卡分离率,话费返还量";
	public final static String RESPONSE_UNPACK_MONTH_CHARGE_UIM = "area_name|,curmonth_value_5450|台,curmonth_value_5460|%,curmonth_value_5470|台";
	public final static String COLUMN_KPI_CODE_UNPACK_MONTH_CHARGE_UIM = "5450,5460,5470";
	// 拆包日，拆包量
	public final static String KPI_UNPACK_DAY_COUNT = "4870|4880";
	public final static String COLUMN_UNPACK_DAY_COUNT = CURDAY_VALUE + DIV
			+ CURDAY_VALUE_DR + DIV + CD_MYOY;
	public final static String TITLE_COLUMN_UNPACK_DAY_COUNT = "区域,日拆包量,月同比,拆包率";
	public final static String RESPONSE_UNPACK_DAY_COUNT = "area_name|,curday_value_4870|台,cd_myoy_4870|+,curday_value_4880|%";
	public final static String COLUMN_KPI_CODE_UNPACK_DAY_COUNT = "4870,4870,4870,4880";
	// 拆包月，拆包量
	public final static String KPI_UNPACK_MONTH_COUNT = "5410|5420";
	public final static String COLUMN_UNPACK_MONTH_COUNT = CURMONTH_VALUE + DIV
			+ CURMONTH_VALUE_DR + DIV + CM_COL;
	public final static String TITLE_COLUMN_UNPACK_MONTH_COUNT = "区域,月拆包量,环比,拆包率";
	public final static String RESPONSE_UNPACK_MONTH_COUNT = "area_name|,curmonth_value_5410|台,cm_col_5410|+,curmonth_value_5420|%";
	public final static String COLUMN_KPI_CODE_UNPACK_MONTH_COUNT = "5410,5410,5410,5420";
	// 窜货月，激活量，省际窜出
	public final static String KPI_FG_MONTH_COUNT_ACTIVATE = "5720|5980|5700|6030";
	public final static String TITLE_COLUMN_FG_MONTH_COUNT_ACTIVATE = "区域,激活量,激活率,省际窜出量,省际窜出率";
	public final static String RESPONSE_FG_MONTH_COUNT_ACTIVATE = "area_name|,curmonth_value_5720|台,curmonth_value_5980|%,curmonth_value_5700|台,curmonth_value_6030|%";
	public final static String COLUMN_KPI_CODE_FG_MONTH_COUNT_ACTIVATE = "5720,5980,5700,6030";

	// 窜货月，窜入窜出
	public final static String KPI_FG_MONTH_FG = "6010|6020|5690|5930";
	public final static String TITLE_COLUMN_FG_MONTH_FG = "区域,省内窜入量,省内窜入率,省内窜出量,省内窜出率";
	public final static String RESPONSE_FG_MONTH_FG = "area_name|,curmonth_value_6010|台,curmonth_value_6020|%,curmonth_value_5690|台,curmonth_value_5930|%";
	public final static String COLUMN_KPI_CODE_MONTH_FG = "6010,6020,5690,5930";

	// *************************************
	// **************************************
	// 进销存日，终端销售结构
	public final static String KPI_PSS_DAY_SALE_STRUCTURE = "4600|4610,400|420,440|460,480|500,520|540";
	public final static String COLUMN_PSS_DAY_SALE_STRUCTURE = CURDAY_VALUE
			+ DIV + CURDAY_VALUE_DR;
	public final static String TITLE_GROUP_PSS_DAY_SALE_STRUCTURE = "是否新机|是否裸机|是否智能机|是否4G终端|资源类型";
	public final static String TITLE_COLUMN_PSS_DAY_SALE_STRUCTURE = "区域|新机销量|新机占比|库存销量|库存占比,区域|裸机销量|裸机占比|合约机销量|合约机占比,区域|智能机销量|智能机占比|普通机销量|普通机占比,区域|4G销量|4G占比|非4G销量|非4G占比,区域|自有销量|自有占比|代理销量|代理占比";
	public final static String RESPONSE_PSS_DAY_SALE_STRUCTURE = "area_name|,curday_value_4600|台,curday_value_dr_4600|%,curday_value_4610|台,curday_value_dr_4610|%;area_name|,curday_value_400|台,curday_value_dr_400|%,curday_value_420|台,curday_value_dr_420|%;area_name|,curday_value_440|台,curday_value_dr_440|%,curday_value_460|台,curday_value_dr_460|%;area_name|,curday_value_480|台,curday_value_dr_480|%,curday_value_500|台,curday_value_dr_500|%;area_name|,curday_value_520|台,curday_value_dr_520|%,curday_value_540|台,curday_value_dr_540|%";
	public final static String COLUMN_KPI_CODE_SALE_STRUCTURE = "4600|4600|4610|4610,400|400|420|420,440|440|460|460,480|480|500|500,520|520|540|540";
	// 进销存月，终端销售结构
	public final static String KPI_PSS_MONTH_SALE_STRUCTURE = "5140|5150,2840|2860,2880|2900,2920|2940,2960|2980";
	public final static String COLUMN_PSS_MONTH_SALE_STRUCTURE = CURMONTH_VALUE
			+ DIV + CURMONTH_VALUE_DR;
	public final static String TITLE_GROUP_PSS_MONTH_SALE_STRUCTURE = "是否新机|是否裸机|是否智能机|是否4G终端|资源类型";
	public final static String TITLE_COLUMN_PSS_MONTH_SALE_STRUCTURE = "区域|新机销量|新机占比|库存销量|库存占比,区域|裸机销量|裸机占比|合约机销量|合约机占比,区域|智能机销量|智能机占比|普通机销量|普通机占比,区域|4G销量|4G占比|非4G销量|非4G占比,区域|自有销量|自有占比|代理销量|代理占比";
	public final static String RESPONSE_PSS_MONTH_SALE_STRUCTURE = "area_name|,curmonth_value_5140|台,curmonth_value_dr_5140|%,curmonth_value_5150|台,curmonth_value_dr_5150|%;area_name|,curmonth_value_2840|台,curmonth_value_dr_2840|%,curmonth_value_2860|台,curmonth_value_dr_2860|%;area_name|,curmonth_value_2880|台,curmonth_value_dr_2880|%,curmonth_value_2900|台,curmonth_value_dr_2900|%;area_name|,curmonth_value_2920|台,curmonth_value_dr_2920|%,curmonth_value_2940|台,curmonth_value_dr_2940|%;area_name|,curmonth_value_2960|台,curmonth_value_dr_2960|%,curmonth_value_2980|台,curmonth_value_dr_2980|%";
	public final static String COLUMN_KPI_CODE_SALE_STRUCTURE_MONTH = "5140|5140|5150|5150,2840|2840|2860|2860,2880|2880|2900|2900,2920|2920|2940|2940,2960|2960|2980|2980";
	// ***************************************
	// *****************************************
	public final static String TITLE_BIG_CHART = "title_big_chart";

	// chart 单位
	public final static String KPI_UNIT_CHART = "kpi_unit_chart";
	// 进销存日，当月每日销量chart
	public final static String KPI_PSS_DAY_COUNT_CHART = "340";

	// 进销存月，chart
	public final static String KPI_PSS_MONTH_COUNT_CHART = "2780";
	// 滞销日，chart
	public final static String KPI_UNSALE_DAY_COUNT_CHART = "5990";
	// 滞销月，chart
	public final static String KPI_UNSALE_MONTH_COUNT_CHART = "6000";
	// 拆包日，chart
	public final static String KPI_UNPACK_DAY_COUNT_CHART = "4880";
	// 拆包月，chart
	public final static String KPI_UNPACK_MONTH_COUNT_CHART = "5420";
	// 窜货月，chart
	public final static String KPI_FG_MONTH_COUNT_CHART = "5930";
	// 窜货月，chart右侧列表
	public final static String KPI_FG_MONTH_COUNT_ACTIVATE_CHART = "5720|5980|5690|5930";
	public final static String TITLE_COLUMN_FG_MONTH_COUNT_ACTIVATE_CHART = "区域,激活量,激活率,省内窜出量,省内窜出率";
	public final static String RESPONSE_FG_MONTH_COUNT_ACTIVATE_CHART = "area_name|,curmonth_value_5720|台,curmonth_value_5980|%,curmonth_value_5690|台,curmonth_value_5930|%";
	public final static String COLUMN_KPI_CODE_FG_MONTH_COUNT_ACTIVATE_CHART = "5720,5980,5690,5930";
	// *****************************************
	// 进销存【月】 指标
	public final static String KPI_MONTH_PSS_2780 = "2780"; // 当月终端销量
	public final static String KPI_MONTH_PSS_2800 = "2800"; // 当年累计终端销量

	public final static String KPI_MONTH_PSS_3000 = "3000"; // TD终端价格分档1000以下当月销量
	public final static String KPI_MONTH_PSS_3020 = "3020"; // TD终端价格分档[1000-2000]以下当月销量
	public final static String KPI_MONTH_PSS_3040 = "3040"; // TD终端价格分档[2000-3000]以下当月销量
	public final static String KPI_MONTH_PSS_3060 = "3060"; // TD终端价格分档3000以上当月销量

	public final static String KPI_MONTH_PSS_3080 = "3080";
	public final static String KPI_MONTH_PSS_3100 = "3100";
	public final static String KPI_MONTH_PSS_3120 = "3120";
	public final static String KPI_MONTH_PSS_3140 = "3140";
	public final static String KPI_MONTH_PSS_3160 = "3160";

	public final static String POS_MONTH_PSS_LEFT_PIE = "0";
	public final static String POS_MONTH_PSS_RIGHT_PIE = "1";

	public final static String KPI_DAILY_UNSALE_4630 = "4630"; // 滞销周期[7-12个月]当日滞销量
	public final static String KPI_DAILY_UNSALE_4640 = "4640"; // 滞销周期[13-18个月]当日滞销量
	public final static String KPI_DAILY_UNSALE_4650 = "4650"; // 滞销周期[19-24个月]当日滞销量
	public final static String KPI_DAILY_UNSALE_4660 = "4660"; // 滞销周期[大于24个月]当日滞销量

	public final static String KPI_MONTH_UNSALE_5170 = "5170"; // 滞销周期[7-12个月]当月滞销量
	public final static String KPI_MONTH_UNSALE_5180 = "5180"; // 滞销周期[13-18个月]当月滞销量
	public final static String KPI_MONTH_UNSALE_5190 = "5190"; // 滞销周期[19-24个月]当月滞销量
	public final static String KPI_MONTH_UNSALE_5200 = "5200"; // 滞销周期[大于24个月]当月滞销量
}
