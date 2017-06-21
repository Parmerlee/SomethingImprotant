package com.bonc.mobile.hbmclient.terminal.view;

import java.util.List;
import java.util.Map;

import android.content.Context;

import com.bonc.mobile.hbmclient.terminal.TerminalHomePageDataLoad;

public class PieAllowranceBracket extends PieChartBase implements
		TerminalHomeRefreshInterface {

	public PieAllowranceBracket(Context context) {
		super(context);
		TerminalRefreshListener.register(this);
	}

	@Override
	protected List<Map<String, String>> loadingData() {
		return TerminalHomePageDataLoad.getallowrance();
	}

	@Override
	public void refresh() {
		super.refresh();
	}

}
