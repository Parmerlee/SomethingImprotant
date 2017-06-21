/**
 * StateRelationKpiDay
 */
package com.bonc.mobile.hbmclient.state.relationkpi;

import com.bonc.mobile.hbmclient.activity.SlideHolderActivity;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.observer.relationkpi.RelationKpiDayObservable;
import com.bonc.mobile.hbmclient.observer.relationkpi.RelationKpiDayObserver;

/**
 * @author liweigao
 *
 */
public class StateRelationKpiDay extends AStateRelationKpi {
	private RelationKpiDayObservable mObservable;
	private RelationKpiDayObserver mObserver;

	public StateRelationKpiDay(String mainKpiCode, String optime,
			String areacode, String levelMenuCode, SlideHolderActivity a) {
		this.mObservable = new RelationKpiDayObservable(mainKpiCode, optime,
				areacode, levelMenuCode, getStateFlag());
		this.mObserver = new RelationKpiDayObserver(this.mObservable, a);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.relationkpi.AStateRelationKpi#enter()
	 */
	@Override
	public void enter() {
		this.mObserver.setContentView();
		this.mObservable.prepareData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.relationkpi.AStateRelationKpi#getStateFlag
	 * ()
	 */
	@Override
	public DateRangeEnum getStateFlag() {
		// TODO Auto-generated method stub
		return DateRangeEnum.DAY;
	}

}
