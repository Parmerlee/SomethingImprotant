/**
 * LevelKpiTemplateObservable
 */
package com.bonc.mobile.hbmclient.observer.levelkpi;

import java.util.Calendar;
import java.util.Map;

import org.json.JSONException;

import com.bonc.mobile.hbmclient.adapter.IAdapter;
import com.bonc.mobile.hbmclient.adapter.levelkpi.ConfigAndDataAdapter;
import com.bonc.mobile.hbmclient.asyn_task.LevelKpiAsynTask;
import com.bonc.mobile.hbmclient.command.ICommand;
import com.bonc.mobile.hbmclient.command.date_change.AreaPickController;
import com.bonc.mobile.hbmclient.command.date_change.AreaSelectCommand;
import com.bonc.mobile.hbmclient.command.date_change.DateSelectCommand;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.enum_type.ProcedureEnum;
import com.bonc.mobile.hbmclient.observer.AObservable;
import com.bonc.mobile.hbmclient.state.levelkpi.IStateKpi;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.HttpUtil;
import com.bonc.mobile.hbmclient.visitor.DBNode;
import com.bonc.mobile.hbmclient.visitor.IConfig;
import com.bonc.mobile.hbmclient.visitor.KpiConfigVisitor;
import com.bonc.mobile.hbmclient.visitor.ObjectStructure;

/**
 * @author liweigao
 * 
 */
public class LevelKpiTemplateObservable extends AObservable implements IAdapter {
	private ObjectStructure configInfo = new ObjectStructure();
	private KpiConfigVisitor configVisitor;
	private ConfigAndDataAdapter mAdapter;
	private LevelKpiAsynTask mLevelKpiAsynTask;

	private AreaPickController mAreaPickController;

	public LevelKpiTemplateObservable(String menuCode,
			IStateKpi<DateRangeEnum> state, String excludedMenuCode) {

		DBNode dbNode = new DBNode();
		this.configInfo.add(dbNode);
		this.configVisitor = new KpiConfigVisitor(menuCode);
		this.configVisitor.setParentMenuCode(excludedMenuCode);
		this.configVisitor.setDataType(state.getStateFlag());

		this.mAdapter = new ConfigAndDataAdapter();
		this.mAdapter.setConfig(configVisitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.observer.AObservable#notifyObservers(java.lang
	 * .Object)
	 */
	@Override
	public void notifyObservers(Object arg) {
		// TODO Auto-generated method stub
		super.setChanged();
		super.notifyObservers(arg);
	}

	public void prepareData() {
		getConfigInfo();
		this.mLevelKpiAsynTask = new LevelKpiAsynTask(this);
		this.mLevelKpiAsynTask.execute();
	}

	private void getConfigInfo() {
		// 只需要执行一次
		String optime = null;
		this.mAreaPickController.initialAreaInfo();
		String areaCode = this.mAreaPickController.getAreaCode();

		this.configInfo.accept(configVisitor);
		this.configVisitor.initialConfigInfo();
		this.configVisitor.setDimKey("T-1");
		this.configVisitor.setOpTime(optime);
		this.configVisitor.setAreaCode(areaCode);

		notifyObservers(ProcedureEnum.STATIC_END);
	}

	public void requestServer() {
		Map<String, String> param = this.configVisitor.getRequestServerParams();
		String result = HttpUtil.sendRequest(ActionConstant.GET_LEVEL_KPI_DATA,
				param);
		try {
			this.mAdapter.setBackData(result);
		} catch (JSONException e) {
			this.mLevelKpiAsynTask.cancel(true);
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.adapter.IAdapter#adapt(java.lang.Object[])
	 */
	@Override
	public void adapt(Object... args) {
		// TODO Auto-generated method stub
		ICommand command = (ICommand) args[0];
		if (command instanceof DateSelectCommand) {
			Calendar c = (Calendar) args[1];
			changeDate(c);
		} else if (command instanceof AreaSelectCommand) {
			String areaCode = (String) args[1];
			this.configVisitor.setAreaCode(areaCode);
		}
		this.mLevelKpiAsynTask = new LevelKpiAsynTask(this);
		this.mLevelKpiAsynTask.execute();
	}

	private void changeDate(Calendar c) {
		DateRangeEnum dre = this.configVisitor.getDataType();
		String optime = DateUtil.formatter(c.getTime(),
				dre.getDateServerPattern());
		String starttime = null;
		switch (dre) {
		case DAY:
			starttime = DateUtil.dayBefore(optime, dre.getDateServerPattern(),
					dre.getTrendSize());
			break;
		case MONTH:
			starttime = DateUtil.monthBefore(optime,
					dre.getDateServerPattern(), dre.getTrendSize());
			break;
		}

		this.configVisitor.setStartTime(starttime);
		this.configVisitor.setOpTime(optime);
	}

	/**
	 * @param mAreaPickController
	 *            the mAreaPickController to set
	 */
	public void setAreaPickController(AreaPickController mAreaPickController) {
		this.mAreaPickController = mAreaPickController;
	}

	/**
	 * @return the configVisitor
	 */
	public IConfig getConfigVisitor() {
		return configVisitor;
	}

	/**
	 * @return the mAdapter
	 */
	public ConfigAndDataAdapter getConfigAndDataAdapter() {
		return mAdapter;
	}

	/**
	 * @return
	 * @see AreaPickController#getAreaName()
	 */
	public String getAreaName() {
		return mAreaPickController.getAreaName();
	}
}
