/**
 * AStateLevelKpi
 */
package com.bonc.mobile.hbmclient.state.levelkpi;

import android.widget.RelativeLayout;

import com.bonc.mobile.hbmclient.activity.SlideHolderActivity;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.observer.levelkpi.LevelKpiTemplateObservable;
import com.bonc.mobile.hbmclient.observer.levelkpi.LevelKpiTemplateObserver;

/**
 * @author liweigao 该模块最好是优先采用中介者模式(或者观察者模式，中介者模式更优，但是粒度更细)，后采用状态模式，这样可以明确区分日月状态
 */
public abstract class AStateLevelKpi implements IStateKpi<DateRangeEnum> {
	protected MachineLevelKpi mMachine;
	protected LevelKpiTemplateObservable mObservable;
	protected LevelKpiTemplateObserver mObserver;
	protected DateRangeEnum mDateRangeEnum;
	protected String areaId, optime, starttime;

	public AStateLevelKpi(MachineLevelKpi machine,
			SlideHolderActivity activity, String menuCode,
			String excludedMenuCode) {
		initialStateFlag();
		this.mMachine = machine;
		this.mObservable = new LevelKpiTemplateObservable(menuCode, this,
				excludedMenuCode);
		this.mObserver = new LevelKpiTemplateObserver(this.mObservable,
				activity, menuCode);
	}

	abstract protected void initialStateFlag();

	/**
	 * @return
	 * @see LevelKpiTemplateObserver#getActivity()
	 */
	public SlideHolderActivity getActivity() {
		return mObserver.getActivity();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.state.levelkpi.IStateKpi#getStateFlag()
	 */
	@Override
	public DateRangeEnum getStateFlag() {
		// TODO Auto-generated method stub
		return mDateRangeEnum;
	}

	public RelativeLayout getNeedView() {
		return (RelativeLayout) this.mObserver.getNeedView();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.state.levelkpi.IStateKpi#changeState()
	 */
	@Override
	public void changeState() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.state.levelkpi.IStateKpi#enter()
	 */
	@Override
	public void enter() {
		this.mObserver.setContentView();
		this.mObservable.prepareData();
	}

}
