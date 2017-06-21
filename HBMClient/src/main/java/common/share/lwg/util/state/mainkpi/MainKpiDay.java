/**
 * MainKpiDay
 */
package common.share.lwg.util.state.mainkpi;

import com.bonc.mobile.hbmclient.activity.BITabActivity;
import com.bonc.mobile.hbmclient.activity.MainKpiActivity;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;

/**
 * @author liweigao
 *
 */
public class MainKpiDay extends AStateMainKpi {

	/**
	 * @param machine
	 */
	public MainKpiDay(MainKpiActivity machine,String menuCode) {		
		super(machine,menuCode);
		

	}
	

	/* (non-Javadoc)
	 * @see common.share.lwg.util.state.IState#getStateFlag()
	 */
	@Override
	public DateRangeEnum getStateFlag() {
		return DateRangeEnum.DAY;
	}

}
