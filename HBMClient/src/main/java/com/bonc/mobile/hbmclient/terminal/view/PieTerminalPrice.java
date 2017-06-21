package com.bonc.mobile.hbmclient.terminal.view;

import java.util.List;
import java.util.Map;

import android.content.Context;

import com.bonc.mobile.hbmclient.terminal.TerminalHomePageDataLoad;

public class PieTerminalPrice extends PieChartBase implements
		TerminalHomeRefreshInterface {

	public PieTerminalPrice(Context context) {
		super(context);
		TerminalRefreshListener.register(this);
	}

	@Override
	protected List<Map<String, String>> loadingData() {
		return TerminalHomePageDataLoad.getPieTwo();
	}

	@Override
	public void refresh() {
		super.refresh();
	}

}
