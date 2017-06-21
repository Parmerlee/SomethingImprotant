package com.bonc.mobile.hbmclient.terminal.subactivity;

import java.util.Map;

import org.json.JSONObject;

import android.os.Bundle;

import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;
import com.bonc.mobile.hbmclient.util.HttpUtil;
import com.bonc.mobile.hbmclient.util.StringUtil;

public class DaySaleCountStaticsActivity extends AbsNoTopButtonActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected JSONObject onLoading(String[] keys) {
		String action = intent.getExtras().getString(
				TerminalConfiguration.KEY_ACTION);
		if (this.configMap == null) {
			this.configMap = (Map<String, String>) intent.getExtras()
					.getSerializable(TerminalConfiguration.KEY_MAP);
		} else {

		}
		String result = HttpUtil.sendRequest(action, this.configMap);
		try {
			return new JSONObject(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected String onGetTitle(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return this.intent.getExtras().getString(
				TerminalConfiguration.TITLE_BIG);
	}

	@Override
	protected String[] onGetColumnNames(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return this.intent.getExtras()
				.getString(TerminalConfiguration.TITLE_COLUMN)
				.split(Constant.DEFAULT_SPLIT);
	}

	@Override
	protected String[] onGetKeys(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return intent.getExtras()
				.getString(TerminalConfiguration.KEY_RESPONSE_KEY)
				.split(Constant.DEFAULT_SPLIT);
	}

	@Override
	protected String[] onGetColumnKpi() {

		String tempString = intent.getExtras().getString(
				TerminalConfiguration.KEY_COLUNM_KPI_CODE);

		if (!StringUtil.isNull(tempString)) {
			return tempString.split(Constant.DEFAULT_SPLIT);
		} else {
			return new String[] {};
		}
	};

}
