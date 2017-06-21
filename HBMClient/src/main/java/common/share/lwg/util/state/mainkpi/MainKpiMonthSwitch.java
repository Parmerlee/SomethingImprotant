/**
 * MainKpiMonthSwitch
 */
package common.share.lwg.util.state.mainkpi;

import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.MainKpiActivity;
import com.bonc.mobile.hbmclient.view.DateRangeSwitchView2;
import com.bonc.mobile.hbmclient.view.DateRangeSwitchView2.OnStateChangeListener;

/**
 * @author liweigao
 *
 */
public class MainKpiMonthSwitch extends MainKpiMonth {
	private DateRangeSwitchView2 mSwitchView;
	/**
	 * @param machine
	 */
	public MainKpiMonthSwitch(MainKpiActivity machine,String menuCode) {
		super(machine,menuCode);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see common.share.lwg.util.state.mainkpi.MainKpiMonth#firstEnter()
	 */
	@Override
	protected void firstEnter() {
		this.mSwitchView = new DateRangeSwitchView2(this.machine,DateRangeSwitchView2.month);
		RelativeLayout rl = (RelativeLayout) this.machine.findViewById(R.id.id_mainkpi_title);
		LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);	
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp.addRule(RelativeLayout.CENTER_VERTICAL);
		rl.addView(mSwitchView, lp);
		
		this.mSwitchView.setOnStateChangeListener(new OnStateChangeListener() {
			
			@Override
			public void toStateMonth() {
				
			}
			
			@Override
			public void toStateDay() {					
				changeState();
			}
		});
		
		super.firstEnter();
	}

	/* (non-Javadoc)
	 * @see common.share.lwg.util.state.mainkpi.AStateMainKpi#changeState()
	 */
	@Override
	public void changeState() {
		this.machine.setState(this.machine.getStateDaySwitch());
	}

}
