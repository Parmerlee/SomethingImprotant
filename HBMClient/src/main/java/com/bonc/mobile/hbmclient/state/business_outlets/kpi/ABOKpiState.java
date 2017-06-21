/**
 * ABOKpiState
 */
package com.bonc.mobile.hbmclient.state.business_outlets.kpi;

import java.util.HashMap;
import java.util.Map;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.BusinessOutletsKpiActivity;
import com.bonc.mobile.hbmclient.asyn_task.ChannelSummaryAsynTask;
import com.bonc.mobile.hbmclient.asyn_task.ChannelSummaryAsynTask.OnPostListener;
import com.bonc.mobile.hbmclient.asyn_task.DailyReportAsynTask;
import com.bonc.mobile.hbmclient.command.business_outlets.DateSelector;
import com.bonc.mobile.hbmclient.command.business_outlets.DateSelector.OnDateSelectListener;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.state.business_outlets.item.IBOItem;
import com.bonc.mobile.hbmclient.state.business_outlets.kpi.compare.ASCState2;
import com.bonc.mobile.hbmclient.state.business_outlets.kpi.compare.DESCState2;
import com.bonc.mobile.hbmclient.state.business_outlets.kpi.compare.ICompareState2;
import com.bonc.mobile.hbmclient.view.ListViewSetting;

/**
 * @author liweigao
 *
 */
public abstract class ABOKpiState implements IBOKpiState {
	protected BusinessOutletsKpiActivity machine;
	protected ListView listViewLeft;
	protected ListView listViewRight;
	protected LinearLayout titleContainer;
	protected ChannelSummaryAsynTask mTask;
	protected DateSelector dateSelector;
	protected String areaCode;
	protected String typeQuest;
	protected Button dateSelect;
	protected boolean isFirst = true;
	protected DailyReportAsynTask noticeTask;
	private ListViewSetting listViewSetting;

	protected ICompareState2 state, ascState, descState;

	public final static String KEY_TYPE = "column";
	public final static String KEY_KPI_CODE = "kpiCode";

	public ABOKpiState(BusinessOutletsKpiActivity a, String date,
			String areaCode) {
		this.machine = a;
		this.dateSelector = new DateSelector(machine);
		this.dateSelector.setDate(date);
		this.areaCode = areaCode;
		this.dateSelector.setOnDateSelectListener(new OnDateSelectListener() {

			@Override
			public void onDateSelect(DateSelector ds) {
				dateSelect.setText(ds.getShowDate());
				questData();
			}
		});

		this.ascState = new ASCState2(this);
		this.descState = new DESCState2(this);
		this.state = this.ascState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.life.ILife#create()
	 */
	@Override
	public void create() {
		this.listViewLeft = (ListView) this.machine
				.findViewById(R.id.listViewLeft);
		this.listViewRight = (ListView) this.machine
				.findViewById(R.id.listViewRight);
		this.listViewSetting = new ListViewSetting();
		this.listViewSetting.setListViewOnTouchAndScrollListener(listViewLeft,
				listViewRight);
		this.titleContainer = (LinearLayout) this.machine
				.findViewById(R.id.titleContainer);
		this.dateSelect = (Button) this.machine
				.findViewById(R.id.id_date_select);
		this.dateSelect.setText(this.dateSelector.getShowDate());
		this.dateSelect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dateSelector.chooseDate();
			}
		});
	}

	protected void questData() {
		this.mTask = new ChannelSummaryAsynTask(this.machine,
				new OnPostListener() {
					@Override
					public void onPost(String s) {
						parseData(s);
					}
				});
		Map<String, String> questMap = new HashMap<String, String>();
		questMap.put(IBOItem.KEY_DATE, this.dateSelector.getServerDate());
		questMap.put(IBOItem.KEY_AREA_CODE, areaCode);
		questMap.put(KEY_TYPE, typeQuest);
		questMap.put(KEY_KPI_CODE, machine.getKpiCode());
		this.mTask.execute(
				ActionConstant.GET_BUSINESS_OUTLETS_INFO_SUMMARY_KPI, questMap);
	}

	abstract protected void parseData(String s);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.life.ILife#start()
	 */
	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.life.ILife#resume()
	 */
	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.life.ILife#pause()
	 */
	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.life.ILife#stop()
	 */
	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.life.ILife#destroy()
	 */
	@Override
	public void destroy() {
		if (this.noticeTask != null) {
			if (this.noticeTask.isCancelled()) {

			} else {
				this.noticeTask.destroy();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.kpi.IBOKpiState#getASCState
	 * ()
	 */
	@Override
	public ICompareState2 getASCState() {
		// TODO Auto-generated method stub
		return this.ascState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.kpi.IBOKpiState#getDESCState
	 * ()
	 */
	@Override
	public ICompareState2 getDESCState() {
		// TODO Auto-generated method stub
		return this.descState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.kpi.IBOKpiState#setSortState
	 * (
	 * com.bonc.mobile.hbmclient.state.business_outlets.kpi.compare.ICompareState2
	 * )
	 */
	@Override
	public void setSortState(ICompareState2 state) {
		this.state = state;
	}

}
