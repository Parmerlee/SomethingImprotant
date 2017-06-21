/**
 * HightItemState
 */
package com.bonc.mobile.hbmclient.state.business_outlets.item;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.command.business_outlets.AreaSelector;
import com.bonc.mobile.hbmclient.command.business_outlets.DateSelector;
import com.bonc.mobile.hbmclient.state.business_outlets.InfoSummaryState;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.view.adapter.BusinessOutletsHighAdapter;

/**
 * @author liweigao
 *
 */
public class HightItemState extends ABOItem {

	/**
	 * @param machine
	 * @param as
	 * @param ds
	 */
	public HightItemState(InfoSummaryState machine, AreaSelector as,
			DateSelector ds) {
		super(machine, as, ds);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.item.ABOItem#dataParse
	 * (java.lang.String, org.json.JSONArray)
	 */
	@Override
	protected void dataParse(String type, JSONArray data) {
		List<Map<String, String>> listData = JsonUtil.toList(data);
		this.listView.setAdapter(new BusinessOutletsHighAdapter(listData,
				this.dateSelector.getServerDate(), this.areaSelector
						.getAreaCode()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.item.IBOItem#addView()
	 */
	@Override
	public void addView() {
		LayoutInflater inflater = LayoutInflater
				.from(this.machine.getContext());
		RelativeLayout titleContainer = this.machine.getTitleContainer();
		View title = inflater.inflate(
				R.layout.business_outlets_info_summary_title_high,
				titleContainer, false);
		titleContainer.removeAllViews();
		titleContainer.addView(title);
		RelativeLayout buttonContainer = this.machine.getButtonContainer();
		View buttons = inflater.inflate(R.layout.layout_main_kpi_button,
				buttonContainer, false);
		buttonContainer.removeAllViews();
		buttonContainer.addView(buttons);
		this.areaSelect = (Button) buttons.findViewById(R.id.id_button1);
		this.dateSelect = (Button) buttons.findViewById(R.id.id_button2);
		this.listView = this.machine.getListView();
		setListener();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.item.IBOItem#changeState
	 * ()
	 */
	@Override
	public void changeState() {
		this.machine.setState(this.machine.getLowState());
	}

}
