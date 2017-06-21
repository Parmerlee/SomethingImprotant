/**
 * RelationKpiMonthObservable
 */
package com.bonc.mobile.hbmclient.observer.relationkpi;

import java.util.Calendar;

import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.util.DateUtil;

/**
 * @author liweigao
 * 
 */
public class RelationKpiMonthObservable extends ARelationKpiObservable {

	/**
	 * @param mainKpiCode
	 * @param optime
	 * @param areaCode
	 * @param levelMenuCode
	 * @param dre
	 */
	public RelationKpiMonthObservable(String mainKpiCode, String optime,
			String areaCode, String levelMenuCode, DateRangeEnum dre) {
		super(mainKpiCode, optime, areaCode, levelMenuCode, dre);
		// TODO Auto-generated constructor stub
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

		String starttime = DateUtil.monthBefore(optime,
				dre.getDateServerPattern(), dre.getTrendSize());

		this.configVisitor.setStartTime(starttime);
		this.configVisitor.setOpTime(optime);
	}

}
