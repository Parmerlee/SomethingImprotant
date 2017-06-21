package com.bonc.mobile.hbmclient.util;

import android.content.Context;
import android.content.Intent;

import com.bonc.mobile.hbmclient.activity.DimensionAreaActivity;
import com.bonc.mobile.hbmclient.activity.DimensionTimeActivity;
import com.bonc.mobile.hbmclient.activity.MainKpiActivity;
import com.bonc.mobile.hbmclient.activity.TrendChartActivity;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;

/**
 * @author sunwei
 */
public class MenuUtil {
	/***
	 * KPI主界面
	 * 
	 * @param context
	 * @param menuCode
	 */
	public static void startKPIHomeActivity(Context context, String menuCode) {
		Intent intent = new Intent(context, MainKpiActivity.class);
		intent.putExtra(MenuEntryAdapter.KEY_MENU_CODE, menuCode);
		context.startActivity(intent);
	}

	/****
	 * KPI区域
	 * 
	 * @param context
	 * @param menuCode
	 * @param optime
	 * @param areaCode
	 * @param kpiCode
	 * @param dataType
	 */
	public static void startKPIAreaActivity(Context context, String menuCode,
			String optime, String areaCode, String kpiCode, String dataType) {
		Intent intent = new Intent(context, DimensionAreaActivity.class);
		intent.putExtra(DimensionAreaActivity.KEY_AREA_CODE, areaCode);
		intent.putExtra(DimensionAreaActivity.KEY_DATA_TYPE, dataType);
		intent.putExtra(DimensionAreaActivity.KEY_DIM_KEY, "T-1");
		intent.putExtra(DimensionAreaActivity.KEY_KPI_CODE, kpiCode);
		intent.putExtra(DimensionAreaActivity.KEY_MENU_CODE, menuCode);
		intent.putExtra(DimensionAreaActivity.KEY_OPTIME, optime);
		context.startActivity(intent);
	}

	/***
	 * KPI时间
	 * 
	 * @param context
	 * @param menuCode
	 * @param optime
	 * @param areaCode
	 * @param kpiCode
	 * @param dataType
	 */
	public static void startKPITimeActivity(Context context, String menuCode,
			String optime, String areaCode, String kpiCode, String dataType) {
		Intent intent = new Intent(context, DimensionTimeActivity.class);
		intent.putExtra(DimensionTimeActivity.KEY_AREA_CODE, areaCode);
		intent.putExtra(DimensionTimeActivity.KEY_DATA_TYPE, dataType);
		intent.putExtra(DimensionTimeActivity.KEY_DIM_KEY, "T-1");
		intent.putExtra(DimensionTimeActivity.KEY_KPI_CODE, kpiCode);
		intent.putExtra(DimensionTimeActivity.KEY_MENU_CODE, menuCode);
		intent.putExtra(DimensionTimeActivity.KEY_OPTIME, optime);
		context.startActivity(intent);
	}

	/***
	 * KPI趋势图
	 * 
	 * @param context
	 * @param menuCode
	 * @param optime
	 * @param areaCode
	 * @param kpiCode
	 * @param dataType
	 */
	public static void startKPITrendActivity(Context context, String menuCode,
			String optime, String areaCode, String kpiCode, String dataType) {
		Intent intent = new Intent(context, TrendChartActivity.class);
		intent.putExtra(TrendChartActivity.AREA_CODE, areaCode);
		intent.putExtra(TrendChartActivity.DATA_TYPE, dataType);
		intent.putExtra(TrendChartActivity.KPI_CODE, kpiCode);
		intent.putExtra(TrendChartActivity.MENU_CODE, menuCode);
		intent.putExtra(TrendChartActivity.OPTIME, optime);
		context.startActivity(intent);
	}

}
