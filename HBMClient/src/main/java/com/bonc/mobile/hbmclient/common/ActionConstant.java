package com.bonc.mobile.hbmclient.common;

public class ActionConstant {

	public final static String LOGIN_CHECK_ACTION = "palmBiLoginAction_doLoginSys.do"; // 用户登录验证

	public final static String CHECK_USER_PERMISSION = "palmBiLoginAction_doPermissionLoad.do"; // 获取用户权限相关信息

	public final static String GET_USER_PERMISSION_RESETINFO = "palmBiLoginAction_getPermissionResetInfo.do"; // 获取用户权限配置信息

	public final static String GET_USER_DBPATH_BYROLE = "palmBiLoginAction_getUserDbPathByRole.do"; // 根据用户角色获取客户端DB文件Url

	public final static String GET_USER_PERMISSION_CONF = "palmBiLoginAction_getUserPermissionConfig.do"; // 根据用户角色获取客户端DB文件Url

	public final static String KPI_DATA_SYNCHRIONZID = "palmBiDataAction_synDataFunc.do"; // 经分日报
																							// 财务月报等类似的数据同步action.

	public final static String TOP_10_DATA_SYNCHRIONZID = "palmBiDataAction_synTermDataFunc.do"; // 终端top
																									// 20数据.

	public final static String KPI_HOME_DATA = "palmBiDataAction_realTimeQueryData.do"; // 指标首页实时查询数据.

	public final static String KPI_AREA_DATA = "palmBiDataAction_ViewKpiByAreaDim.do"; // 指标按地区查看.

	public final static String KPI_PERIOD_DATA = "palmBiDataAction_viewKpiAreaBySingle.do"; // 指标按时间查看.

	public final static String KPI_AREA_PERIOD_DATA = "palmBiDataAction_viewKpiAreaByDouble.do";// 指标首页趋势图点击数据.

	public final static String MODIFY_PASSWORD = "palmBiSysFuncAction_modifyUserPass.do"; // 修改密码接口.

	public final static String SEND_MOBILE_INFO = "palmBiSysFuncAction_recordUserMobileInfo.do";// 发送本机信息.

	public final static String GET_NEW_APK = "palmBiSysFuncAction_getMobileVersionNo.do"; // 获取最新版本.

	public final static String GET_UPDATE_MESSAGE = "palmBiSysFuncAction_checkUpgradeInfo.do"; // 获取升级信息

	public final static String MENUDATA_ROLLBACK_CHECK = "palmBiDataAction_synDataSetInfo.do"; // 检查是否有数据更新的action

	public final static String TERMIANLDATA_UPDATE_INTERFACE = "palmBiDataAction_synTermDataFuncForUpdate.do";// 终端数据回滚action

	public final static String MENUDATA_UPDATE_INTERFACE = "palmBiDataAction_synDataFuncForUpdate.do"; // 数据重新更新接口

	public final static String DATA_UPDATE_TIMEINFO = "palmBiSysFuncAction_getDataUpdateTimeInfo.do"; // 数据更新通知接口

	public final static String NOTICE_INFO = "palmBiSysFuncAction_getNoticeInfo.do"; // 公告提取接口

	public final static String GET_LOGIN_SUM = "palmBiSysFuncAction_getLoginSumInfo.do"; // 登录记录统计
	public final static String GET_CERTQUERY_INFO = "palmBiSysFuncAction_getMobileCertRecordInfo.do";
	public final static String RESET_SELF = "palmBiSysFuncAction_resetPassBySelf.do"; // 个人重置密码身份认证
	public final static String RESET_BY_ADMIN = "palmBiSysFuncAction_resetPassByAdmin.do"; // 管理员回复某人默认密码

	public final static String KPI_ORDER_TOP5 = "palmBiDataAction_ViewKpiByAreaDimWithRankCount.do";// 指标排名top5

	// 终端模块
	public final static String Term_GetAreaMaxMinKpiValue = "palmBiTermDataAction_getAreaMaxMinKpiValue.do";

	public final static String QUERY_ETL_DETAIL_INFO = "palmBiSysFuncAction_queryrETLdetail.do";

	public final static String STATISTICS_INFO = "palmBiSysFuncAction_KpiAndMenuStatistics.do";

	public final static String GET_TERM_MAIN_DATA = "palmBiDataAction_getTremLeaderData.do";

	public final static String GET_TERM_CHART_DATA = "palmBiDataAction_getReportData.do";

	public final static String CUSTOMER_RETENTION = "crAction_customerRetention.do";

	public final static String CUSTOMER_RETENTION_KPI = "crAction_customerRetentionKpi.do";

	public final static String CUSTOMER_RETENTION_VALUE = "crAction_customerRetentionValue.do";

	public final static String CUSTOMER_RETENTION_TREND = "crAction_customerRetentionTrend.do";

	public final static String CUSTOMER_RETENTION_PIE = "crAction_customerRetentionPie.do";

	public final static String GET_4G_REPORT_DATE = "leaderAction_get4gReportLatestDate.do";

	public final static String GET_4G_DAILY_REPORT = "leaderAction_get4GDailyReport.do";

	public final static String RELATIVE_KPI_DATA = "leaderAction_kpiRelData.do";
	// 数据简报接口
	public final static String GET_DATA_PRESENTATION_DATA = "briefReportAction_getBriefReport.do";
	public final static String GET_SINGLE_DATA_PRESENTATION_DATA = "briefReportAction_getBriefReportByModel.do";

	// 新等级指标界面接口
	public final static String GET_LEVEL_KPI_DATA = "leaderAction_queryRelKpiData.do";
	// 渠道分析一级接口
	public final static String GET_CHANNEL_ANALYZE_DATA = "palmChannel_getChannelBenefitData.do";
	// 营业网点业务量
	public final static String GET_BUSINESS_OUTLETS_INFO_SUMMARY = "palmChannel_getKpiDataByAreaCode.do";
	public final static String GET_BUSINESS_OUTLETS_FOCUS_WEBSITE_WEBSTYLE = "palmChannel_focusChannel.do";
	public final static String GET_BUSINESS_OUTLETS_FOCUS_WEBSITE_KPISTYLE = "palmChannel_kpiView.do";
	public final static String GET_BUSINESS_OUTLETS_FOCUS_WEBSITE_WEBSITEKPI = "palmChannel_getKpiDataByFocus.do";
	public final static String GET_BUSINESS_OUTLETS_INFO_SUMMARY_KPI = "palmChannel_getKpiDataByKpiCode.do";
	public final static String GET_BUSINESS_OUTLETS_FOCUS_WEBSITE_FOCUS = "palmChannel_searchNetWork.do";
	public final static String GET_BUSINESS_OUTLETS_FOCUS_WEBSITE_SEND_FOCUS = "palmChannel_addFocusChannel.do";
	public final static String GET_BUSINESS_OUTLETS_FOCUS_WEBSITE_DELETE_WEBSITE = "palmChannel_unBindChannelRelation.do";
	public final static String GET_BUSINESS_OUTLETS_FOCUS_WEBSITE_KPI_SHOW = "palmChannel_getKpiDataByChannel.do";
	public final static String GET_BUSINESS_OUTLETS_KPI_EXPLAIN = "palmChannel_getKpiInfoByCode.do";

	// 主指标界面
	public final static String GET_MAIN_KPI_DATA = "palmKpi_loadKpiData.do";
}
