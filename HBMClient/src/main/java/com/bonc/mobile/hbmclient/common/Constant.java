package com.bonc.mobile.hbmclient.common;

import com.bonc.mobile.common.AppConstant;

import android.os.Environment;

public class Constant extends com.bonc.mobile.common.Constant {
	public static final String SDCard = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	/**
	 * 外部存储路径
	 */

	public static final String LOADER = SDCard + "/chinamobile/bonc/dp/";

	/**
	 * 分享截屏存放地址
	 */
	public static final String SHOOTCUT_PATH = SDCard
			+ "/chinamobile/bonc/shootcut/";

	/**
	 * sd卡数据库相对路径
	 */
	public static final String DB_PATH = "data/db/data.db";

	/**
	 * sd卡数据库所在目录相对路径
	 */
	public static final String DB_PATH_FOLDER = "data/db";

	/**
	 * 数据库版本
	 */
	public static final int DB_VERSION = 1;

	/**
	 * 包路径
	 */
	public static final String PKG_NAME = "com.bonc.mobile.hbmclient";

	/**
	 * 数据库名称
	 */
	public static final String DB_NAME = "data.db";
	public static final boolean IS_DATA_ENCRYPT = false;
	public static final boolean DEBUG_ABLE = false;
	public final static boolean SCREEN_SHORT = !AppConstant.SEC_ENH;

	public static final boolean ENCRYPT_DATABASE = true;
	public static final boolean COMPRESS_WATERMARK = true;

	// server 地址
	public static final String BASE_SERVER_PATH = BASE_PATH + "/PalmBiServer";
	public static final String SERVER_PATH = BASE_SERVER_PATH + "/PalmBiKpi/"; // 李涛地址

	public static final String[] confTables = { "menu_kpi_col_rel",
			"user_info", "menu_info", "menu_add_info", "dim_area_code",
			"dim_menu_rel", "dim_info", "kpi_menu_rel", "kpi_info",
			"dim_value_info", "m_kpi_main_rel", "MENU_GROUP_INFO" };

	/**
	 * 配置文件中文字的分隔符\
	 */

	public static final String DEFAULT_SPLIT = ",";

	// 指标数据类型.
	public static final String DATA_TYPE_MONTH = "M";
	public static final String DATA_TYPE_DAY = "D";
	public static final String DEFAULT_CONJUNCTION = "|";
	public static final String NULL_DIM_CODE = "-1";

	// 指标查看的最近数据的天数和月数
	public static final int KPI_RECENT_DAY = 9;
	public static final int KPI_RECENT_MONTH = 11;
	public static final String DIMKEY_SPLIT = "@";

	public static final String NULL_VALUE_INSTEAD = "--";

	// 终端销量部分 默认规则
	public static final String TERMINAL_SALE_DEFAULT_RULE = "[0|2]";
	public static final String DECIMALS_DEFAULT_RULE = "[2|2]";
	public static final String TERMINAL_SALE_DEFAULT_UNIT = "台";
	public static final String TERMINAL_SALE_PERCENT_RULE = "%|2";
	public static final String TERMINAL_SALE_PERCENT_RULE_0 = "%|0";
	public static final String TERMINAL_SALE_PERCENT_UNIT = "%";
	public static final String UNIT_YUAN = "元";
	public static final String UNIT_TAI = "台";
	public static final String UNIT_HU = "户";
	public static final String UNIT_PERCENT = "%";
}
