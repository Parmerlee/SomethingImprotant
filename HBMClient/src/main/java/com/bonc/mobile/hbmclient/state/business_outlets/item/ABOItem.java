/**
 * ABOItem
 */
package com.bonc.mobile.hbmclient.state.business_outlets.item;

import org.json.JSONArray;
import org.json.JSONObject;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.command.business_outlets.AreaSelector;
import com.bonc.mobile.hbmclient.command.business_outlets.DateSelector;
import com.bonc.mobile.hbmclient.state.business_outlets.InfoSummaryState;

/**
 * @author liweigao
 * 
 */
public abstract class ABOItem implements IBOItem {
	protected InfoSummaryState machine;

	protected Button areaSelect, dateSelect;
	protected ListView listView;

	protected AreaSelector areaSelector;
	protected DateSelector dateSelector;

	public ABOItem(InfoSummaryState machine, AreaSelector as, DateSelector ds) {
		this.machine = machine;
		this.areaSelector = as;
		this.dateSelector = ds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.item.IBOItem#setArea
	 * (com.bonc.mobile.hbmclient.command.business_outlets.AreaSelector)
	 */
	@Override
	public void setArea(AreaSelector as) {
		String areaName = as.getAreaName();
		this.areaSelect.setText(areaName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.item.IBOItem#setDate
	 * (com.bonc.mobile.hbmclient.command.business_outlets.DateSelector)
	 */
	@Override
	public void setDate(DateSelector ds) {
		String showDate = ds.getShowDate();
		this.dateSelect.setText(showDate);
	}

	protected void setListener() {
		this.areaSelect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				areaSelector.chooseArea();
			}
		});
		this.dateSelect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dateSelector.chooseDate();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.item.IBOItem#updateView
	 * (org.json.JSONObject)
	 */
	@Override
	public void updateView(JSONObject jo) {
		String type = null;
		JSONArray data = null;
		try {
			type = jo.optString("column");
			data = jo.optJSONArray("data");
			if (data == null || data.length() <= 0) {
				data = new JSONArray();
				Toast.makeText(machine.getContext(), "没有数据", Toast.LENGTH_SHORT)
						.show();
			} else {

			}
		} catch (Exception e) {
			data = new JSONArray();
		} finally {
			dataParse(type, data);
		}
	}

	abstract protected void dataParse(String type, JSONArray data);
}
