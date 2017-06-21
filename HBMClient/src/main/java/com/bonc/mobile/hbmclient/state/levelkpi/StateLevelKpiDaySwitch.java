/**
 * StateLevelKpiDaySwitch
 */
package com.bonc.mobile.hbmclient.state.levelkpi;

import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.bonc.mobile.hbmclient.activity.SlideHolderActivity;
import com.bonc.mobile.hbmclient.asyn_task.DailyReportAsynTask;
import com.bonc.mobile.hbmclient.asyn_task.NoticeAsynTask;
import com.bonc.mobile.hbmclient.view.DateRangeSwitchView2;
import com.bonc.mobile.hbmclient.view.DateRangeSwitchView2.OnStateChangeListener;

/**
 * @author liweigao
 *
 */
public class StateLevelKpiDaySwitch extends StateLevelKpiDay {
	private DateRangeSwitchView2 mSwitchView;

	public StateLevelKpiDaySwitch(MachineLevelKpi machine,
			SlideHolderActivity activity, String menuCode,
			String excludedMenuCode) {
		super(machine, activity, menuCode, excludedMenuCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.state.levelkpi.AStateLevelKpi#enter()
	 */
	@Override
	public void enter() {
		if (this.mObserver.getContentView() != null) {
			this.mObserver.setContentView();
		} else {
			this.mObserver.setContentView();
			this.mSwitchView = new DateRangeSwitchView2(
					this.mObserver.getActivity(), DateRangeSwitchView2.day);
			RelativeLayout rl = getNeedView();
			LayoutParams lp = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			rl.addView(mSwitchView, lp);

			this.mSwitchView
					.setOnStateChangeListener(new OnStateChangeListener() {

						@Override
						public void toStateMonth() {
							changeState();
						}

						@Override
						public void toStateDay() {

						}
					});

			this.mObservable.prepareData();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.levelkpi.AStateLevelKpi#changeState()
	 */
	@Override
	public void changeState() {
		if (DailyReportAsynTask.mPopupWindow != null
				&& DailyReportAsynTask.mPopupWindow.isShowing()) {
			DailyReportAsynTask.mPopupWindow.dismiss();
		}
		if (NoticeAsynTask.mPopupWindow != null
				&& NoticeAsynTask.mPopupWindow.isShowing()) {
			NoticeAsynTask.mPopupWindow.dismiss();
		}
		this.mMachine.setState(this.mMachine.getStateMonthSwitch());
	}
}
