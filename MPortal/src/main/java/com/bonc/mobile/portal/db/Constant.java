package com.bonc.mobile.portal.db;

import com.bonc.mobile.common.AppConstant;

import android.os.Environment;

public class Constant extends com.bonc.mobile.common.Constant {
	public static final String SDCard = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	/**
	 * 外部存储路径
	 */

	public static final String LOADER = SDCard + "/chinamobile/bonc/protal/";

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
	 * 数据库名称
	 */
	public static final String DB_NAME = "data.db";
	public static final boolean IS_DATA_ENCRYPT = false;
	public static final boolean DEBUG_ABLE = false;
	public final static boolean SCREEN_SHORT = !AppConstant.SEC_ENH;

	public static final boolean ENCRYPT_DATABASE = AppConstant.SEC_ENH;
	public static final boolean COMPRESS_WATERMARK = true;

}
