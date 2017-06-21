/**
 * IFocusWebsite
 */
package com.bonc.mobile.hbmclient.state.business_outlets.focus_website;

import com.bonc.mobile.hbmclient.command.business_outlets.DateSelector;

/**
 * @author liweigao
 *
 */
public interface IFocusWebsite {
	void addView();

	void updateView();

	void setDate(DateSelector ds);

	void changeState();

	void questData();
}
