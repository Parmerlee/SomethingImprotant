package com.bonc.mobile.hbmclient.asyn_task;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;
import com.bonc.mobile.hbmclient.terminal.SimpleTerminalActivity;
import com.bonc.mobile.hbmclient.util.HttpUtil;

public class TermLoadDataAsynTask extends FirstLoadDataAsynTask {

	public TermLoadDataAsynTask(SimpleTerminalActivity sta) {
		super(sta);
	}

	@Override
	protected Boolean doInBackground(String... menuCodes) {
		String menuCode = menuCodes[0];
		if (menuCode
				.equals(TerminalActivityEnum.PSS_DAY_ACTIVITY.getMenuCode())) {
			return doRequest("palmTerm_jxcDailyDate.do");
		} else if (menuCode.equals(TerminalActivityEnum.PSS_MONTH_ACTIVITY
				.getMenuCode())) {
			return doRequest("palmTerm_jxcMonthDate.do");
		} else if (menuCode.equals(TerminalActivityEnum.UNSALE_DAY_ACTIVITY
				.getMenuCode())) {
			return doRequest("palmTerm_zxDailyDate.do");
		} else if (menuCode.equals(TerminalActivityEnum.UNSALE_MONTH_ACTIVITY
				.getMenuCode())) {
			return doRequest("palmTerm_zxMonthDate.do");
		} else if (menuCode.equals(TerminalActivityEnum.UNPACK_DAY_ACTIVITY
				.getMenuCode())) {
			return doRequest("palmTerm_cbDailyDate.do");
		} else if (menuCode.equals(TerminalActivityEnum.UNPACK_MONTH_ACTIVITY
				.getMenuCode())) {
			return doRequest("palmTerm_cbMonthDate.do");
		} else if (menuCode.equals(TerminalActivityEnum.FG_MONTH_ACTIVITY
				.getMenuCode())) {
			return doRequest("palmTerm_chMonthDate.do");
		} else
			return super.doInBackground(menuCodes);
	}

	boolean doRequest(String action) {
		Map<String, String> params = new HashMap<String, String>();
		if (!first)
			params.put("optime",
					mSimpleTerminalActivity.mTerminalActivityEnum.getOPtime());
		params.put("areaCode",
				mSimpleTerminalActivity.mTerminalActivityEnum.getAreaCode());
		params.put("menuCode",
				mSimpleTerminalActivity.mTerminalActivityEnum.getMenuCode());
		String result = HttpUtil.sendRequest(action, params);
		try {
			if (result == null)
				return false;
			JSONObject json = new JSONObject(result);
			if (first) {
				mSimpleTerminalActivity.mTerminalActivityEnum.setDate(json
						.optString("optime"));
			}
			mSimpleTerminalActivity.setArea(json);
			mRootView.iteratorSetData(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			if (first)
				this.mSimpleTerminalActivity.initialUI();
			else if (mRootView != null) {
				mRootView.iteratorUpdateView();
				mRootView.iteratorSetViewListener();
			}
		}
		mSimpleTerminalActivity.dismissTip();
	}

}
