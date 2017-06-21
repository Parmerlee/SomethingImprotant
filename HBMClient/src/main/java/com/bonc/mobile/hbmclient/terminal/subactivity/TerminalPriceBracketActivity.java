package com.bonc.mobile.hbmclient.terminal.subactivity;

import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;

import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;
import com.bonc.mobile.hbmclient.util.HttpUtil;
import com.bonc.mobile.hbmclient.util.StringUtil;

/**
 * 终端价格分档
 * 
 * @author tengshibo
 *
 */
public class TerminalPriceBracketActivity extends AbsSectionChartSelectableAct {
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.intent = getIntent();
		super.onCreate(savedInstanceState);
	}

	@Override
	public JSONObject onLoading(String[] keys) {
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

		return new JSONObject();
	}

	@Override
	public String[] onGetKeys(Bundle bundle) {

		return this.intent.getExtras()
				.getString(TerminalConfiguration.KEY_RESPONSE_KEY)
				.split(Constant.DEFAULT_SPLIT);
	}

	@Override
	public String onGetTitle(Bundle bundle) {
		return this.intent.getExtras().getString(
				TerminalConfiguration.TITLE_BIG);
	}

	@Override
	public String[] onGetColumnNames(Bundle bundle) {
		// 列名
		return this.intent.getExtras()
				.getString(TerminalConfiguration.TITLE_COLUMN)
				.split(Constant.DEFAULT_SPLIT);
	}

	@Override
	protected void onListLineClicked(int index) {
		int a = 0;
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
