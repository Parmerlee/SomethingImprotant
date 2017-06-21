/**
 * MachineLevelKpi
 */
package com.bonc.mobile.hbmclient.state.levelkpi;

import java.util.List;
import java.util.Map;

import com.bonc.mobile.hbmclient.activity.SlideHolderActivity;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;

/**
 * @author liweigao
 * 
 */
public class MachineLevelKpi {
	private IStateKpi state, stateDay, stateMonth, stateDaySwitch,
			stateMonthSwitch;
	private BusinessDao dao = new BusinessDao();

	private final String KEY_DATE_TYPE = "date_type";
	private final String KEY_MENU_CODE = "MENU_CODE";

	public MachineLevelKpi(SlideHolderActivity a, String firstLevelCode) {
		List<Map<String, String>> secondMenuType = dao
				.getDateType(firstLevelCode);

		String type = null;
		int size = secondMenuType.size();
		if (size == 1) {
			type = secondMenuType.get(0).get(KEY_DATE_TYPE);
			String code = secondMenuType.get(0).get(KEY_MENU_CODE);
			if (DateRangeEnum.DAY.getDateFlag().equals(type)) {
				this.stateDay = new StateLevelKpiDay(this, a, code,
						firstLevelCode);
				this.state = this.stateDay;
			} else if (DateRangeEnum.MONTH.getDateFlag().equals(type)) {
				this.stateMonth = new StateLevelKpiMonth(this, a, code,
						firstLevelCode);
				this.state = this.stateMonth;
			} else {

			}
		} else if (size == 2) {
			type = secondMenuType.get(0).get(KEY_DATE_TYPE);
			String codeDay = null;
			String codeMonth = null;
			if (DateRangeEnum.DAY.getDateFlag().equals(type)) {
				codeDay = secondMenuType.get(0).get(KEY_MENU_CODE);
				codeMonth = secondMenuType.get(1).get(KEY_MENU_CODE);
			} else if (DateRangeEnum.MONTH.getDateFlag().equals(type)) {
				codeMonth = secondMenuType.get(0).get(KEY_MENU_CODE);
				codeDay = secondMenuType.get(1).get(KEY_MENU_CODE);
			} else {

			}
			this.stateDaySwitch = new StateLevelKpiDaySwitch(this, a, codeDay,
					firstLevelCode);
			this.stateMonthSwitch = new StateLevelKpiMonthSwitch(this, a,
					codeMonth, firstLevelCode);
			this.state = this.stateDaySwitch;
		} else {

		}
	}

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

	/**
	 * @return the stateDaySwitch
	 */
	public IStateKpi getStateDaySwitch() {
		return stateDaySwitch;
	}

	/**
	 * @return the stateMonthSwitch
	 */
	public IStateKpi getStateMonthSwitch() {
		return stateMonthSwitch;
	}

	public void launch() {
		this.state.enter();
	}

}
