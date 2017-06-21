package com.bonc.mobile.hbmclient.terminal.subactivity;

import java.util.Map;

import org.json.JSONObject;

import android.os.Bundle;

import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;
import com.bonc.mobile.hbmclient.util.HttpUtil;
import com.bonc.mobile.hbmclient.util.StringUtil;

/**
 * 销售结构分析 包括进货渠道 销售渠道 用户结构 智能机等
 * 
 * @author Administrator 0 1 2 3
 */
public class SaleAnalysisActivity extends AbsStatisticsSelectableActivity {

	private String[] buttonIds = "0,1,2,3,4".split(Constant.DEFAULT_SPLIT);
	private String[] skpis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	protected JSONObject onLoading(String[] keys) {
		String action = intent.getExtras().getString(
				TerminalConfiguration.KEY_ACTION);
		if (this.configMap == null) {
			this.configMap = (Map<String, String>) intent.getExtras()
					.getSerializable(TerminalConfiguration.KEY_MAP);
			this.skpis = this.intent.getExtras()
					.getString(TerminalConfiguration.KEY_KPI_CODES_ARRAY)
					.split(Constant.DEFAULT_SPLIT);
		} else {

		}
		this.configMap.put(TerminalConfiguration.KEY_KPI_CODES,
				this.skpis[mTopSelectedID]);
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
		return this.intent.getExtras().getString(
				TerminalConfiguration.TITLE_BIG);
	}

	@Override
	protected String[] onGetColumnNames(Bundle savedInstanceState) {
		String[] title_column = this.intent.getExtras()
				.getString(TerminalConfiguration.TITLE_COLUMN)
				.split(Constant.DEFAULT_SPLIT);
		return title_column[mTopSelectedID].split(TerminalConfiguration.DIV0);
	}

	@Override
	protected String[] onGetKeys(Bundle savedInstanceState) {
		String[] response_key = this.intent.getExtras()
				.getString(TerminalConfiguration.KEY_RESPONSE_KEY)
				.split(TerminalConfiguration.SEM);
		return response_key[mTopSelectedID].split(Constant.DEFAULT_SPLIT);
	}

	@Override
	protected String[] onGetColumnKpi() {
		String tempString = intent.getExtras().getString(
				TerminalConfiguration.KEY_COLUNM_KPI_CODE);

		if (StringUtil.isNull(tempString)) {
			return new String[] {};
		}

		String[] tempArray = tempString.split(Constant.DEFAULT_SPLIT);

		if (tempArray == null || tempArray.length == 0) {
			return new String[] {};
		}

		if (mTopSelectedID >= tempArray.length) {
			return new String[] {};
		}

		String kpiCodes[] = tempArray[mTopSelectedID].split("\\"
				+ Constant.DEFAULT_CONJUNCTION);

		if (kpiCodes != null) {
			return kpiCodes;
		} else {
			return new String[] {};
		}

	};

}
