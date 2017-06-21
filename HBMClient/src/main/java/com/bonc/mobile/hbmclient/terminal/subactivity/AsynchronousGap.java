package com.bonc.mobile.hbmclient.terminal.subactivity;

import org.json.JSONObject;

import android.os.Bundle;

/** 终端模块约束接口：若需要自己定义新的终端模块，建议引入此接口 */
public interface AsynchronousGap {
	/** 载入数据接口 应返回符合格式的供解析数据 */
	JSONObject onLoading(String[] keys);

	/** 返回字段名 */
	String[] onGetKeys(Bundle bundle);

	String onGetTitle(Bundle bundle);

	String[] onGetColumnNames(Bundle bundle);
}
