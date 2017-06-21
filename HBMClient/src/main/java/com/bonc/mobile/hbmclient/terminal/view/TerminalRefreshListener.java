package com.bonc.mobile.hbmclient.terminal.view;

import java.util.ArrayList;
import java.util.List;

public class TerminalRefreshListener {
	private static List<TerminalHomeRefreshInterface> listener;

	public static void register(TerminalHomeRefreshInterface instance) {
		if (listener == null) {
			listener = new ArrayList<TerminalHomeRefreshInterface>();
		}

		listener.add(instance);
	}

	public static void refresh() {
		for (TerminalHomeRefreshInterface thri : listener) {
			thri.refresh();
		}
	}

}
