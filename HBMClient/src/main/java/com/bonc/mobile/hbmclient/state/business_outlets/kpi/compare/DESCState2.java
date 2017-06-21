/**
 * DESCState
 */
package com.bonc.mobile.hbmclient.state.business_outlets.kpi.compare;

import java.util.List;
import java.util.Map;

import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.state.business_outlets.kpi.IBOKpiState;

/**
 * @author liweigao
 *
 */
public class DESCState2 extends ACompareState2<IBOKpiState> {

	/**
	 * @param machine
	 */
	public DESCState2(IBOKpiState machine) {
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
		this.machine.setSortState(this.machine.getASCState());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.kpi.compare.ICompareState2
	 * #sort(java.util.List, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void sort(List<Map<String, String>> data, String key, Object... out) {
		try {
			this.sort.descSort(data, key);
			this.machine.resetSort();
			TextView tv = (TextView) out[0];
			tv.setCompoundDrawablesWithIntrinsicBounds(
					R.mipmap.triangle_downward, 0, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		changeState();
	}

}
