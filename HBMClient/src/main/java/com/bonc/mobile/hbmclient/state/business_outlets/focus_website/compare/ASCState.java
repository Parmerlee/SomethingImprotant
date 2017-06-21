/**
 * ASCState
 */
package com.bonc.mobile.hbmclient.state.business_outlets.focus_website.compare;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.view.View;
import android.widget.Button;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.state.business_outlets.focus_website.WebsiteStyleState;

/**
 * @author liweigao
 *
 */
public class ASCState extends ACompareState {

	/**
	 * @param machine
	 */
	public ASCState(WebsiteStyleState machine) {
		super(machine);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.focus_website.compare
	 * .ICompareState#changeState()
	 */
	@Override
	public void changeState() {
		this.machine.setState(this.machine.getDescState());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.focus_website.compare
	 * .ICompareState#sort(android.view.View, android.widget.Button[],
	 * java.util.List)
	 */
	@Override
	public void sort(View v, Button[] bts, List<Map<String, String>> data) {
		String kpiCode = (String) v.getTag();
		SimpleComparator sc = new SimpleComparator(kpiCode);
		Collections.sort(data, sc);

		resetButtons(bts);
		v.setBackgroundResource(R.mipmap.asc);
		this.machine.getListAdapter().notifyDataSetChanged();
		changeState();
	}

}
