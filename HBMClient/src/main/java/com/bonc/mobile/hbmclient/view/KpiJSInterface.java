package com.bonc.mobile.hbmclient.view;

import android.content.Context;

import com.bonc.mobile.hbmclient.util.MenuUtil;

/**
 * @author sunwei
 */
public class KpiJSInterface {
	Context context;

	public KpiJSInterface(Context context) {
		this.context = context;
	}

	public void startKPIHomeActivity(String menuCode) {
		MenuUtil.startKPIHomeActivity(context, menuCode);
	}

	public void startKPIAreaActivity(String menuCode, String optime,
			String areaCode, String kpiCode, String dataType) {
		MenuUtil.startKPIAreaActivity(context, menuCode, optime, areaCode,
				kpiCode, dataType);
	}

	public void startKPITimeActivity(String menuCode, String optime,
			String areaCode, String kpiCode, String dataType) {
		MenuUtil.startKPITimeActivity(context, menuCode, optime, areaCode,
				kpiCode, dataType);
	}

	public void startKPITrendActivity(String menuCode, String optime,
			String areaCode, String kpiCode, String dataType) {
		MenuUtil.startKPITrendActivity(context, menuCode, optime, areaCode,
				kpiCode, dataType);
	}
}
