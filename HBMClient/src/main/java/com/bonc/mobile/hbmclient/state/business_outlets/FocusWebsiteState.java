/**
 * FocusWebsitState
 */
package com.bonc.mobile.hbmclient.state.business_outlets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.command.business_outlets.DateSelector;
import com.bonc.mobile.hbmclient.command.business_outlets.DateSelector.OnDateSelectListener;
import com.bonc.mobile.hbmclient.state.business_outlets.focus_website.IFocusWebsite;
import com.bonc.mobile.hbmclient.state.business_outlets.focus_website.KpiStyleState;
import com.bonc.mobile.hbmclient.state.business_outlets.focus_website.WebsiteStyleState;

/**
 * @author liweigao
 *
 */
public class FocusWebsiteState extends ABOState {
	private IFocusWebsite state, websiteStyleState, kpiStyleState;
	private LinearLayout mainContainer;
	private DateSelector dateSelector;

	/**
	 * @param vs
	 */
	public FocusWebsiteState(BusinessOutletsViewSwitcher vs) {
		super(vs);
		this.dateSelector = new DateSelector(this.machine.getContext());
		this.websiteStyleState = new WebsiteStyleState(this, this.dateSelector);
		this.kpiStyleState = new KpiStyleState(this, this.dateSelector);
		this.state = this.websiteStyleState;
		this.dateSelector.setOnDateSelectListener(new OnDateSelectListener() {

			@Override
			public void onDateSelect(DateSelector ds) {
				state.setDate(ds);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.IBOState#firstEnter()
	 */
	@Override
	public void firstEnter() {
		this.state.updateView();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.IBOState#getViewContainer
	 * ()
	 */
	@Override
	public View getViewContainer() {
		LayoutInflater inflater = LayoutInflater
				.from(this.machine.getContext());
		View main = inflater.inflate(R.layout.business_outlets_website_main,
				this.machine, false);
		mainContainer = (LinearLayout) main.findViewById(R.id.focusWebsiteMain);
		return main;
	}

	/**
	 * @return the websiteStyleState
	 */
	public IFocusWebsite getWebsiteStyleState() {
		return websiteStyleState;
	}

	/**
	 * @return the kpiStyleState
	 */
	public IFocusWebsite getKpiStyleState() {
		return kpiStyleState;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(IFocusWebsite state) {
		this.state = state;
		this.state.updateView();
	}

	/**
	 * @return the mainContainer
	 */
	public LinearLayout getMainContainer() {
		return mainContainer;
	}

	public Context getContext() {
		return this.machine.getContext();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.ABOState#onActivityResult
	 * (int)
	 */
	@Override
	public void onActivityResult(int code) {
		this.state.questData();
	}
}
