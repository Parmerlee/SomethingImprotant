/**
 * RelationKpiDayObservable
 */
package com.bonc.mobile.hbmclient.observer.relationkpi;

import java.util.Calendar;

import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.util.DateUtil;

/**
 * @author liweigao
 * 
 */
public class RelationKpiDayObservable extends ARelationKpiObservable {

	public RelationKpiDayObservable(String mainKpiCode, String optime,
			String areaCode, String levelMenuCode, DateRangeEnum dre) {
		super(mainKpiCode, optime, areaCode, levelMenuCode, dre);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.observer.relationkpi.ARelationKpiObservable
	 * #changeDate(java.util.Calendar)
	 */
	@Override
	protected void changeDate(Calendar c) {
		DateRangeEnum dre = this.configVisitor.getDataType();
		String optime = DateUtil.formatter(c.getTime(),
				dre.getDateServerPattern());

		String starttime = DateUtil.dayBefore(optime,
				dre.getDateServerPattern(), dre.getTrendSize());

		this.configVisitor.setStartTime(starttime);
		this.configVisitor.setOpTime(optime);
	}

}
