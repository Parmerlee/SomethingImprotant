package com.bonc.mobile.saleclient.common;

import java.util.HashMap;

import android.content.Context;

import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.hbmclient.activity.MainKpiActivity;
import com.bonc.mobile.hbmclient.activity.SaleBIActivity;
import com.bonc.mobile.saleclient.activity.MyOrderListActivity;

public class ConfigLoader extends BaseConfigLoader {
	static ConfigLoader inst;

	public String userId;

	public static ConfigLoader getInstance(Context context) {
		if (inst == null) {
			inst = new ConfigLoader(context.getApplicationContext());
		}
		return inst;
	}

	ConfigLoader(Context context) {
		super(context);
		initIconMap();
		initTypeMap();
	}

	void initIconMap() {
		iconMap = new HashMap<String, Integer>();
	}

	void initTypeMap() {
		typeMap = new HashMap<String, Class>();
		typeMap.put("171", MyOrderListActivity.class);
		typeMap.put("172", MyOrderListActivity.class);
		typeMap.put("15", SaleBIActivity.class);
		typeMap.put("12", MainKpiActivity.class);
	}
}
