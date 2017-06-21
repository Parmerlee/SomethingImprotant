/**
 * WebsiteStyleState
 */
package com.bonc.mobile.hbmclient.state.business_outlets.focus_website;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.NetPointActivity;
import com.bonc.mobile.hbmclient.asyn_task.DailyReportAsynTask;
import com.bonc.mobile.hbmclient.command.business_outlets.DateSelector;
import com.bonc.mobile.hbmclient.command.business_outlets.DateSelector.OnDateSelectListener;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.state.business_outlets.FocusWebsiteState;
import com.bonc.mobile.hbmclient.state.business_outlets.UserPermissionVarify;
import com.bonc.mobile.hbmclient.state.business_outlets.focus_website.compare.ASCState;
import com.bonc.mobile.hbmclient.state.business_outlets.focus_website.compare.DESCState;
import com.bonc.mobile.hbmclient.state.business_outlets.focus_website.compare.ICompareState;
import com.bonc.mobile.hbmclient.state.business_outlets.item.IBOItem;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.view.adapter.BusinessOutletsWebsiteWebsiteStyleAdapter;

/**
 * @author liweigao
 * 
 */
public class WebsiteStyleState extends AFocusWebsite {
	private UserPermissionVarify mUPVarify;
	private Button[] kpiNameButton = new Button[4];
	private Button attention;
	private Button dateSelect;
	private Map<String, String> questMap = new HashMap<String, String>();
	private String[] kpiCodes = new String[4];
	private List<Map<String, String>> listData;
	private boolean isFirst = true;
	private BusinessOutletsWebsiteWebsiteStyleAdapter listAdapter;
	private DailyReportAsynTask noticeTask;

	private ICompareState state, ascState, descState;

	/**
	 * @param machine
	 * @param ds
	 */
	public WebsiteStyleState(FocusWebsiteState machine, DateSelector ds) {
		super(machine, ds);
		this.mUPVarify = new UserPermissionVarify(this.machine.getContext()) {

			@Override
			public void validateUser(JSONObject jo) {
				resetButtons(kpiNameButton);
				if (isFirst) {
					String date = null;
					try {
						date = jo.optString("date");
					} catch (Exception e) {
						// TODO: handle exception
					} finally {
						dateSelector.setDate(date);
						dateSelect.setText(dateSelector.getShowDate());
						noticeTask = new DailyReportAsynTask(kpiNameButton[3],
								"点击可排序");
						noticeTask.execute();
						isFirst = false;
					}
				} else {

				}

				try {
					JSONArray head_ja = jo.optJSONArray("head");
					List<Map<String, String>> headData = JsonUtil
							.toList(head_ja);
					if (headData == null) {
						headData = new ArrayList<Map<String, String>>();
					}
					for (int i = 0; i < kpiNameButton.length; i++) {
						Map<String, String> item = headData.get(i);
						if (item == null) {
							item = new HashMap<String, String>();
						}
						Button bt = kpiNameButton[i];
						String kpiCode = item.get("kpiCode");
						String kpiName = item.get("kpiName");
						if (kpiName == null) {
							kpiName = "--";
						}

						bt.setText(kpiName);
						bt.setTag(kpiCode);
						kpiCodes[i] = kpiCode;
					}
					JSONArray listData_ja = jo.optJSONArray("data");
					listData = JsonUtil.toList(listData_ja);
					if (listData == null) {
						listData = new ArrayList<Map<String, String>>();
					}
					listAdapter = new BusinessOutletsWebsiteWebsiteStyleAdapter(
							listData, kpiCodes);
					listView.setAdapter(listAdapter);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		};

		this.ascState = new ASCState(this);
		this.descState = new DESCState(this);
		this.state = this.ascState;
	}

	protected void resetButtons(Button[] bts) {
		for (Button bt : bts) {
			bt.setBackgroundResource(R.mipmap.kpi_name_unfocus);
		}
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
				R.layout.business_website_website_webstyle, main, false);
		main.removeAllViews();
		main.addView(viewStyle);
		kpiNameButton[0] = (Button) viewStyle.findViewById(R.id.kpi1);
		kpiNameButton[1] = (Button) viewStyle.findViewById(R.id.kpi2);
		kpiNameButton[2] = (Button) viewStyle.findViewById(R.id.kpi3);
		kpiNameButton[3] = (Button) viewStyle.findViewById(R.id.kpi4);
		this.listView = (ListView) viewStyle.findViewById(R.id.listView);
		View emptyView = viewStyle.findViewById(R.id.emptyView);
		this.listView.setEmptyView(emptyView);
		this.styleSelect = (Button) viewStyle.findViewById(R.id.button1);
		this.dateSelect = (Button) viewStyle.findViewById(R.id.button2);
		this.attention = (Button) viewStyle.findViewById(R.id.button3);
		this.styleSelect.setText("指标视图");
		this.attention.setText("添加关注");
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
		if (this.noticeTask != null) {
			if (this.noticeTask.isCancelled()) {

			} else {
				this.noticeTask.destroy();
			}
		}

		this.machine.setState(this.machine.getKpiStyleState());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.focus_website.AFocusWebsite
	 * #setListener()
	 */
	@Override
	public void setListener() {
		super.setListener();
		this.dateSelect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String text = dateSelect.getText().toString();
				text = text.trim();
				if (text == null || text.equals("--")) {

				} else {
					dateSelector.chooseDate();
				}
			}
		});

		this.dateSelector.setOnDateSelectListener(new OnDateSelectListener() {

			@Override
			public void onDateSelect(DateSelector ds) {
				dateSelect.setText(ds.getShowDate());
				questData();
			}
		});
		this.attention.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Context context = machine.getContext();
				Intent intent = new Intent(context, NetPointActivity.class);
				Activity activity = (Activity) context;
				activity.startActivityForResult(intent,
						NetPointActivity.REQUEST_CODE);
			}
		});
		for (int i = 0; i < kpiNameButton.length; i++) {
			Button bt = kpiNameButton[i];
			bt.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (listData != null && listData.size() > 0) {
						state.sort(v, kpiNameButton, listData);
					}
				}
			});
		}
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
		questMap.put(IBOItem.KEY_DATE, this.dateSelector.getServerDate());
		this.mUPVarify.questData(
				ActionConstant.GET_BUSINESS_OUTLETS_FOCUS_WEBSITE_WEBSTYLE,
				questMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.focus_website.AFocusWebsite
	 * #setDate(com.bonc.mobile.hbmclient.command.business_outlets.DateSelector)
	 */
	@Override
	public void setDate(DateSelector ds) {
		String showDate = ds.getShowDate();
		this.dateSelect.setText(showDate);
	}

	/**
	 * @return the ascState
	 */
	public ICompareState getAscState() {
		return ascState;
	}

	/**
	 * @return the descState
	 */
	public ICompareState getDescState() {
		return descState;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(ICompareState state) {
		this.state = state;
	}

	/**
	 * @return the listAdapter
	 */
	public BusinessOutletsWebsiteWebsiteStyleAdapter getListAdapter() {
		return listAdapter;
	}

}
