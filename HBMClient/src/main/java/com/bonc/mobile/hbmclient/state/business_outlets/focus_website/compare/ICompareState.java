/**
 * ICompareState
 */
package com.bonc.mobile.hbmclient.state.business_outlets.focus_website.compare;

import java.util.List;
import java.util.Map;

import android.view.View;
import android.widget.Button;

/**
 * @author liweigao
 *
 */
public interface ICompareState {
	void sort(View v, Button[] bts, List<Map<String, String>> data);

	void changeState();
}
