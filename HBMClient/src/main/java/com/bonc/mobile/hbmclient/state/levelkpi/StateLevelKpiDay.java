/**
 * StateLevelKpiDay
 */
package com.bonc.mobile.hbmclient.state.levelkpi;

import com.bonc.mobile.hbmclient.activity.SlideHolderActivity;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;

/**
 * @author liweigao
 * 
 */
public class StateLevelKpiDay extends AStateLevelKpi {

	public StateLevelKpiDay(MachineLevelKpi machine,
			SlideHolderActivity activity, String menuCode,
			String excludedMenuCode) {
		super(machine, activity, menuCode, excludedMenuCode);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.levelkpi.AStateLevelKpi#initialStateFlag
	 * ()
	 */
	@Override
	protected void initialStateFlag() {
		this.mDateRangeEnum = DateRangeEnum.DAY;
	}

}
