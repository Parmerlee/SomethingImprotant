/**
 * KpiStyleState
 */
package com.bonc.mobile.hbmclient.state.business_outlets.focus_website;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.NewCustomActivity;
import com.bonc.mobile.hbmclient.command.business_outlets.DateSelector;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.state.business_outlets.FocusWebsiteState;
import com.bonc.mobile.hbmclient.state.business_outlets.UserPermissionVarify;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.view.adapter.BusinessOutletsWebsiteKpiStyleAdapter;

/**
 * @author liweigao
 *
 */
public class KpiStyleState extends AFocusWebsite {
	private UserPermissionVarify mUPVarify;
	private Map<String, String> questMap = new HashMap<String, String>();
	private List<Map<String, String>> data;

	/**
	 * @param machine
	 * @param ds
	 */
	public KpiStyleState(final FocusWebsiteState machine, DateSelector ds) {
		super(machine, ds);
		this.mUPVarify = new UserPermissionVarify(this.machine.getContext()) {

			@Override
			public void validateUser(JSONObject jo) {
				JSONArray data_ja = jo.optJSONArray("data");
				data = JsonUtil.toList(data_ja);
				listView.setAdapter(new BusinessOutletsWebsiteKpiStyleAdapter(
						data));
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(machine.getContext(),
								NewCustomActivity.class);
						String kpiCode = null;
						try {
							kpiCode = data.get(position).get("KPI_CODE");
							if (kpiCode == null) {

							} else {
								intent.putExtra(NewCustomActivity.KEY_KPI_CODE,
										kpiCode);
								intent.putExtra(NewCustomActivity.KEY_DATE,
										dateSelector.getServerDate());
								machine.getContext().startActivity(intent);
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				});
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.focus_website.IFocusWebsite
	 * #addView()
	 */
	@Override
	public void addView() {
		LayoutInflater inflater = LayoutInflater
				.from(this.machine.getContext());
		LinearLayout main = this.machine.getMainContainer();
		viewStyle = inflater.inflate(
				R.layout.business_website_website_kpistyle, main, false);
		main.removeAllViews();
		main.addView(viewStyle);
		this.styleSelect = (Button) viewStyle.findViewById(R.id.id_date_select);
		this.styleSelect.setText("网点视图");
		this.listView = (ListView) viewStyle.findViewById(R.id.listView);
		setListener();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.focus_website.IFocusWebsite
	 * #changeState()
	 */
	@Override
	public void changeState() {
		this.machine.setState(this.machine.getWebsiteStyleState());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.focus_website.AFocusWebsite
	 * #questData()
	 */
	@Override
	public void questData() {
		this.mUPVarify.questData(
				ActionConstant.GET_BUSINESS_OUTLETS_FOCUS_WEBSITE_KPISTYLE,
				this.questMap);
	}

}
