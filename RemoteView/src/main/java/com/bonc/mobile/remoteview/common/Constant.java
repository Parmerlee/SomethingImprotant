package com.bonc.mobile.remoteview.common;

public class Constant extends com.bonc.mobile.common.Constant {
	public static final String APP_TYPE = "ANDROID_RVS";

	public static final String Path_OrderSetting = "/sys/subjectOrder";
	// 首次查询指标规则
	public static final String Path_LoadKpiRuleFirst = "/sys/alarm";
	// 匹配查询指标名称
	public static final String Path_LoadKpiName = "/sys/alarm/search";
	// 根据指标Code，查询指标规则
	public static final String Path_LoadKpiRule = "/sys/alarm/queryByCode";
	// 修改某个指标规则的状态
	public static final String Path_ModifyKpiRule = "/sys/alarm/modAlarmStatus";
	// 加载热点信息
	public static final String Path_LoadKpiWarn = "/sundry/hotPoint";
	// 加载热点信息之历史信息
	public static final String Path_LoadKpiWarnHis = "/sundry/hotPoint/history";
}
