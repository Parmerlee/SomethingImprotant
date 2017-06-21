package com.bonc.mobile.hbmclient.common;

/**
 * 登录返回状态常量
 * 
 * @author hhf
 *
 */
public class LoginConstant {

	// -2 密码错误 -1 用户名不存在 1验证通过(用户存在且至少一个指标权限) -3用户没赋权 -4 异常 -5账号被锁定
	/** 1:正常 */
	public static final String RESULT_NORMAL = "1";

	/** 2:警告 */
	public static final String RESULT_WARNING = "0";

	/** -1:一般错误(用户名不存在) */
	public static final String RESULT_CHECK_ERROR = "-1";

	/** -2:一般错误(密码错误) */
	public static final String RESULT_CHECK_PASSWORD_ERROR = "-2";

	/** -3:一般错误(用户没赋权) */
	// public static final String RESULT_CHECK_PASSWORD_MAX_ERROR = "-3";
	public static final String RESULT_CHECK_NO_AUTHORITY = "-3";
	public static final String RESULT_CHECK_NO_AUTHORITY_ROLE = "-13";
	public static final String RESULT_CHECK_NO_AUTHORITY_AREA = "-14";
	/** -4:一般错误(异常错误) */
	public static final String RESULT_CHECK_USER_INVALID = "-4";

	/** -5:权限错误(账号被锁) */
	public static final String RESULT_CHECK_LACK_PERMISSION = "-5";

	/** -10：系统错误 */
	public static final String RESULT_SYSTEM_ERROR = "-10";

	/**
	 * 密码错误超过三次,不能再登陆
	 */
	public static final String RESULT_CHECK_EXCEED_THREE = "-12";

	/**
	 * -6 没有IMEI, -7没有IMSI
	 */
	public static final String LOGIN_FLAG_HASIMISNOIMEI = "-6";
	public static final String LOGIN_FLAG_HASIMEINOIMSI = "-7";
	// -8 4A账号错
	public static final String LOGIN_WRONG_ACCOUNT = "-8";
	// -9 手机号码错
	public static final String LOGIN_WRONG_MOBILE = "-9";
	// -10 填写内容都错错
	public static final String LOGIN_WRONG_ALL = "-11";

	public static final String WRONG_TIMES_EXCEEDLIMITS = "-6";

	// 本机信息标志
	public static final String LOGIN_FLAG_PHONE = "99";

	/**
	 * -3密码输入错误，账号冻结
	 */
	public static final String RESULT_CHECK_PASSWORD_MAX_ERROR = "-3";
}
