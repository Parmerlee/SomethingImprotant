/**
 * LowItemState
 */
package com.bonc.mobile.hbmclient.state.business_outlets.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.command.business_outlets.AreaSelector;
import com.bonc.mobile.hbmclient.command.business_outlets.DateSelector;
import com.bonc.mobile.hbmclient.state.business_outlets.AddFocusWebsite;
import com.bonc.mobile.hbmclient.state.business_outlets.InfoSummaryState;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.view.adapter.BusinessOutletsLowAdapter;

/**
 * @author liweigao
 * 
 */
public class LowItemState extends ABOItem {
	private Button enshrine;
	private TextView titleSingle;
	private AddFocusWebsite addFocusWebsite;

	/**
	 * @param machine
	 * @param as
	 * @param ds
	 */
	public LowItemState(InfoSummaryState machine, AreaSelector as,
			DateSelector ds) {
		super(machine, as, ds);
		this.addFocusWebsite = new AddFocusWebsite(machine.getContext());
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
		if ("public".equalsIgnoreCase(type)) {
			titleSingle.setText("社会");
		} else {
			titleSingle.setText("自有");
		}
		List<Map<String, String>> listData = JsonUtil.toList(data);
		this.listView.setAdapter(new BusinessOutletsLowAdapter(type, listData));
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
				R.layout.business_outlets_info_summary_title_low,
				titleContainer, false);
		titleSingle = (TextView) title.findViewById(R.id.singleTitle);
		titleContainer.removeAllViews();
		titleContainer.addView(title);
		RelativeLayout buttonContainer = this.machine.getButtonContainer();
		View buttons = inflater.inflate(
				R.layout.business_outlets_info_summary_button_low,
				buttonContainer, false);
		buttonContainer.removeAllViews();
		buttonContainer.addView(buttons);
		this.areaSelect = (Button) buttons.findViewById(R.id.button1);
		this.enshrine = (Button) buttons.findViewById(R.id.button2);
		this.enshrine.setText("设为关注");
		this.dateSelect = (Button) buttons.findViewById(R.id.button3);
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
		this.machine.setState(this.machine.getHightState());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.item.ABOItem#setListener
	 * ()
	 */
	@Override
	protected void setListener() {
		super.setListener();
		this.enshrine.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Map<String, String> questMap = new HashMap<String, String>();
				String areaCode = areaSelector.getAreaCode();
				if (areaCode == null) {
					Toast.makeText(v.getContext(), "地区编码为null",
							Toast.LENGTH_SHORT).show();
				} else {
					questMap.put("areaCode", areaCode);
					addFocusWebsite.addFocusWebsite(questMap);
				}

			}
		});
	}
}
