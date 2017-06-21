/**
 * IBOState
 */
package com.bonc.mobile.hbmclient.state.business_outlets;

import android.view.View;

/**
 * @author liweigao
 *
 */
public interface IBOState {
	void firstEnter();

	View getViewContainer();

	void onActivityResult(int code);
}
