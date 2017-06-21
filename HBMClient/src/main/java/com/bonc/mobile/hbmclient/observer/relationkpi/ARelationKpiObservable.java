/**
 * RelationKpiObservable
 */
package com.bonc.mobile.hbmclient.observer.relationkpi;

import java.util.Calendar;
import java.util.Map;

import org.json.JSONException;

import com.bonc.mobile.hbmclient.adapter.IAdapter;
import com.bonc.mobile.hbmclient.adapter.levelkpi.RelationKpiConfigAndDataAdapter;
import com.bonc.mobile.hbmclient.asyn_task.RelationKpiAsynTask;
import com.bonc.mobile.hbmclient.command.ICommand;
import com.bonc.mobile.hbmclient.command.date_change.DateSelectCommand;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.enum_type.ProcedureEnum;
import com.bonc.mobile.hbmclient.observer.AObservable;
import com.bonc.mobile.hbmclient.util.HttpUtil;
import com.bonc.mobile.hbmclient.visitor.DBNode;
import com.bonc.mobile.hbmclient.visitor.IConfig;
import com.bonc.mobile.hbmclient.visitor.KpiConfigVisitor2;
import com.bonc.mobile.hbmclient.visitor.ObjectStructure;
import com.google.gson.Gson;

/**
 * @author liweigao
 * 
 */
public abstract class ARelationKpiObservable extends AObservable implements
		IAdapter {
	protected KpiConfigVisitor2 configVisitor;

	private ObjectStructure configInfo = new ObjectStructure();
	private RelationKpiConfigAndDataAdapter mAdapter;
	private RelationKpiAsynTask mRelationKpiAsynTask;

	public ARelationKpiObservable(String mainKpiCode, String optime,
			String areaCode, String levelMenuCode, DateRangeEnum dre) {

		DBNode dbNode = new DBNode();
		this.configInfo.add(dbNode);
		this.configVisitor = new KpiConfigVisitor2(levelMenuCode);
		this.configVisitor.setMainKpiCode(mainKpiCode);
		this.configVisitor.setOpTime(optime);
		this.configVisitor.setAreaCode(areaCode);
		this.configVisitor.setDataType(dre);

		this.mAdapter = new RelationKpiConfigAndDataAdapter();
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
		super.setChanged();
		super.notifyObservers(arg);
	}

	public void prepareData() {
		getConfigInfo();
		this.mRelationKpiAsynTask = new RelationKpiAsynTask(this);
		this.mRelationKpiAsynTask.execute();
	}

	private void getConfigInfo() {
		// 只需要执行一次
		this.configInfo.accept(configVisitor);
		this.configVisitor.initialConfigInfo();
		this.configVisitor.setDimKey("T-1");

		notifyObservers(ProcedureEnum.STATIC_END);
	}

	public void requestServer() {
		Map<String, String> param = this.configVisitor.getRequestServerParams();
		String result = HttpUtil.sendRequestByJSON(
				ActionConstant.RELATIVE_KPI_DATA, new Gson().toJson(param));
		try {
			this.mAdapter.setBackData(result);
		} catch (JSONException e) {
			this.mRelationKpiAsynTask.cancel(true);
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
		}
		this.mRelationKpiAsynTask = new RelationKpiAsynTask(this);
		this.mRelationKpiAsynTask.execute();
	}

	abstract protected void changeDate(Calendar c);

	/**
	 * @return the configVisitor
	 */
	public IConfig getConfigVisitor() {
		return configVisitor;
	}

	/**
	 * @return the mAdapter
	 */
	public RelationKpiConfigAndDataAdapter getDataAdapter() {
		return mAdapter;
	}
}
