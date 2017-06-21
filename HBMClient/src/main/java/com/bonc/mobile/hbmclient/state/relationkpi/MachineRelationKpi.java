/**
 * MachineRelationKpi
 */
package com.bonc.mobile.hbmclient.state.relationkpi;

import com.bonc.mobile.hbmclient.activity.SlideHolderActivity;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.state.levelkpi.IStateKpi;

/**
 * @author liweigao
 *
 */
public class MachineRelationKpi {
	private IStateKpi state, stateDay, stateMonth;

	public MachineRelationKpi(String mainKpiCode, String dateType,
			String optime, String areacode, String levelMenuCode,
			SlideHolderActivity a) {
		if (DateRangeEnum.DAY.getDateFlag().equals(dateType)) {
			this.stateDay = new StateRelationKpiDay(mainKpiCode, optime,
					areacode, levelMenuCode, a);
			this.state = this.stateDay;
		} else {
			this.stateMonth = new StateRelationKpiMonth(mainKpiCode, optime,
					areacode, levelMenuCode, a);
			this.state = this.stateMonth;
		}
	}

	/**
	 * @return the state
	 */
	public IStateKpi getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(IStateKpi state) {
		this.state = state;
		launch();
	}

	/**
	 * @return the stateDay
	 */
	public IStateKpi getStateDay() {
		return stateDay;
	}

	/**
	 * @return the stateMonth
	 */
	public IStateKpi getStateMonth() {
		return stateMonth;
	}

	public void launch() {
		this.state.enter();
	}

}
