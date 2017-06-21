/**
 * AFocusWebsite
 */
package com.bonc.mobile.hbmclient.state.business_outlets.focus_website;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bonc.mobile.hbmclient.command.business_outlets.DateSelector;
import com.bonc.mobile.hbmclient.state.business_outlets.FocusWebsiteState;

/**
 * @author liweigao
 *
 */
public abstract class AFocusWebsite implements IFocusWebsite {
	protected FocusWebsiteState machine;
	protected DateSelector dateSelector;
	protected Button styleSelect;
	protected ListView listView;
	protected View viewStyle;

	public AFocusWebsite(FocusWebsiteState machine, DateSelector ds) {
		this.machine = machine;
		this.dateSelector = ds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.focus_website.IFocusWebsite
	 * #setDate(com.bonc.mobile.hbmclient.command.business_outlets.DateSelector)
	 */
	@Override
	public void setDate(DateSelector ds) {

	}

	public void setListener() {
		this.styleSelect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				changeState();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.focus_website.IFocusWebsite
	 * #updateView()
	 */
	@Override
	public void updateView() {
		if (this.viewStyle == null) {
			addView();
			questData();
		} else {
			LinearLayout main = this.machine.getMainContainer();
			main.removeAllViews();
			main.addView(viewStyle);
		}
	}
}
