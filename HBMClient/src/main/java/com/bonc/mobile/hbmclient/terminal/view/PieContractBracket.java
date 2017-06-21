package com.bonc.mobile.hbmclient.terminal.view;

import java.util.List;
import java.util.Map;

import android.content.Context;

import com.bonc.mobile.hbmclient.terminal.TerminalHomePageDataLoad;

public class PieContractBracket extends PieChartBase implements
		TerminalHomeRefreshInterface {

	public PieContractBracket(Context context) {
		super(context);
		TerminalRefreshListener.register(this);
	}

	@Override
	protected List<Map<String, String>> loadingData() {
		return TerminalHomePageDataLoad.getPieOne();
	}

	@Override
	public void refresh() {
		super.refresh();
	}

}
