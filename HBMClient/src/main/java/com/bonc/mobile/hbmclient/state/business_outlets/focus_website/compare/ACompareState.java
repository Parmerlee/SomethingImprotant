/**
 * ACompareState
 */
package com.bonc.mobile.hbmclient.state.business_outlets.focus_website.compare;

import android.widget.Button;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.state.business_outlets.focus_website.WebsiteStyleState;

/**
 * @author liweigao
 * 
 */
public abstract class ACompareState implements ICompareState {
	protected WebsiteStyleState machine;

	public ACompareState(WebsiteStyleState machine) {
		this.machine = machine;
	}

	protected void resetButtons(Button[] bts) {
		for (Button bt : bts) {
			bt.setBackgroundResource(R.mipmap.kpi_name_unfocus);
		}
	}
}
