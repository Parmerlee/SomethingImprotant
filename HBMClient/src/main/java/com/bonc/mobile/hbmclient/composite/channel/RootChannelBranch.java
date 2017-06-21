/**
 * RootChannelBranch
 */
package com.bonc.mobile.hbmclient.composite.channel;

import android.view.View;

import com.bonc.mobile.hbmclient.R;

/**
 * @author liweigao
 *
 */
public class RootChannelBranch extends AChannelBranch {
	private IChannelComposite summary;
	private IChannelComposite profit;

	private String areaLevel;

	public RootChannelBranch() {
		this.summary = new ModuleSummaryBranch();
		this.profit = new ModuleProfitBranch();

		add(summary);
	}

	/**
	 * @param areaLevel
	 *            the areaLevel to set
	 */
	public void setAreaLevel(String areaLevel) {
		this.areaLevel = areaLevel;
		if ("4".equalsIgnoreCase(areaLevel)) {
			remove(profit);
		} else {
			add(profit);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.channel.AChannelBranch#dispatchView
	 * (android.view.View)
	 */
	@Override
	public void dispatchView(View view) {
		View profitView = view.findViewById(R.id.id_profit);
		if ("4".equalsIgnoreCase(areaLevel)) {
			profitView.setVisibility(View.GONE);
		} else {
			profitView.setVisibility(View.VISIBLE);
		}
		iteratorDispatchView(view);
	}

}
