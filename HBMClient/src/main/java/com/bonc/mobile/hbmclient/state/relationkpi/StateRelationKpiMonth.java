/**
 * StateRelationKpiMonth
 */
package com.bonc.mobile.hbmclient.state.relationkpi;

import com.bonc.mobile.hbmclient.activity.SlideHolderActivity;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.observer.relationkpi.RelationKpiMonthObservable;
import com.bonc.mobile.hbmclient.observer.relationkpi.RelationKpiMonthObserver;

/**
 * @author liweigao
 *
 */
public class StateRelationKpiMonth extends AStateRelationKpi {
	private RelationKpiMonthObservable mObservable;
	private RelationKpiMonthObserver mObserver;

	public StateRelationKpiMonth(String mainKpiCode, String optime,
			String areacode, String levelMenuCode, SlideHolderActivity a) {
		this.mObservable = new RelationKpiMonthObservable(mainKpiCode, optime,
				areacode, levelMenuCode, getStateFlag());
		this.mObserver = new RelationKpiMonthObserver(this.mObservable, a);
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
		return DateRangeEnum.MONTH;
	}
}
