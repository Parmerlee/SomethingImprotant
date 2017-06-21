/**
 * NewCustomASCState
 */
package com.bonc.mobile.hbmclient.state.business_outlets.kpi.compare;

import java.util.List;
import java.util.Map;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.NewCustomActivity;
import com.bonc.mobile.hbmclient.view.NCListRightItemVIew;

/**
 * @author liweigao
 * 
 */
public class NewCustomASCState extends ATypeCompareState<NewCustomActivity> {
	private TypeSort sort;

	/**
	 * @param machine
	 */
	public NewCustomASCState(NewCustomActivity machine) {
		super(machine);
		this.sort = new TypeSort();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.kpi.compare.ICompareState2
	 * #changeState()
	 */
	@Override
	public void changeState() {
		this.machine.setSortState(this.machine.getDescState());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.kpi.compare.ICompareState2
	 * #sort(java.util.List, java.lang.Object, java.lang.Object[])
	 */
	@Override
	public void sort(List<Map<String, String>> data, Integer key, Object... out) {
		try {
			this.sort.ascSort(data, key);
			this.machine.resetSort();
			NCListRightItemVIew v = (NCListRightItemVIew) out[0];
			v.setContentCompoundDrawable(R.mipmap.triangle_upward);
		} catch (Exception e) {
			e.printStackTrace();
		}
		changeState();
	}

}
