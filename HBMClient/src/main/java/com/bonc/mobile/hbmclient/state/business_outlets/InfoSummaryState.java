/**
 * InfoSummaryState
 */
package com.bonc.mobile.hbmclient.state.business_outlets;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.asyn_task.ChannelSummaryAsynTask;
import com.bonc.mobile.hbmclient.asyn_task.ChannelSummaryAsynTask.OnPostListener;
import com.bonc.mobile.hbmclient.command.business_outlets.AreaSelector;
import com.bonc.mobile.hbmclient.command.business_outlets.AreaSelector.OnAreaSelectListener;
import com.bonc.mobile.hbmclient.command.business_outlets.DateSelector;
import com.bonc.mobile.hbmclient.command.business_outlets.DateSelector.OnDateSelectListener;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.state.business_outlets.item.HightItemState;
import com.bonc.mobile.hbmclient.state.business_outlets.item.IBOItem;
import com.bonc.mobile.hbmclient.state.business_outlets.item.LowItemState;

/**
 * @author liweigao
 *
 */
public class InfoSummaryState extends ABOState {
	private IBOItem state, hightState, lowState;
	private AreaSelector areaSelector;
	private DateSelector dateSelector;
	private ChannelSummaryAsynTask mTask;
	private Map<String, String> questMap = new HashMap<String, String>();
	private boolean isFirst = true;

	private RelativeLayout titleContainer;
	private RelativeLayout buttonContainer;
	private ListView listView;

	/**
	 * @param vs
	 */
	public InfoSummaryState(BusinessOutletsViewSwitcher vs) {
		super(vs);
		this.areaSelector = new AreaSelector(this.machine.getContext());
		this.dateSelector = new DateSelector(this.machine.getContext());
		this.hightState = new HightItemState(this, this.areaSelector,
				this.dateSelector);
		this.lowState = new LowItemState(this, this.areaSelector,
				this.dateSelector);
		this.areaSelector.setOnAreaSelectListener(new OnAreaSelectListener() {

			@Override
			public void onAreaSelect(AreaSelector selector) {
				String areaLevel = selector.getAreaLevel();
				int level = Integer.parseInt(areaLevel);
				if (state instanceof HightItemState) {
					if (level <= 3) {

					} else {
						state.changeState();
						state.addView();
						state.setDate(dateSelector);
					}
				} else if (state instanceof LowItemState) {
					if (level <= 3) {
						state.changeState();
						state.addView();
						state.setDate(dateSelector);
					} else {

					}
				} else {

				}
				state.setArea(selector);
				questData();
			}
		});
		this.dateSelector.setOnDateSelectListener(new OnDateSelectListener() {

			@Override
			public void onDateSelect(DateSelector ds) {
				state.setDate(dateSelector);
				questData();
			}
		});
	}

	public void questData() {
		this.mTask = new ChannelSummaryAsynTask(this.machine.getContext(),
				new OnPostListener() {
					@Override
					public void onPost(String s) {
						checkResult(s);
					}
				});
		questMap.put("menuCode", this.machine.getMenuCode());
		questMap.put(IBOItem.KEY_DATE, this.dateSelector.getServerDate());
		questMap.put(IBOItem.KEY_AREA_CODE, this.areaSelector.getAreaCode());
		this.mTask.execute(ActionConstant.GET_BUSINESS_OUTLETS_INFO_SUMMARY,
				questMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.IBOState#firstEnter()
	 */
	@Override
	public void firstEnter() {
		questData();
	}

	public void setState(IBOItem state) {
		this.state = state;

	}

	/**
	 * @return the hightState
	 */
	public IBOItem getHightState() {
		return hightState;
	}

	/**
	 * @return the lowState
	 */
	public IBOItem getLowState() {
		return lowState;
	}

	private void checkResult(String result) {
		try {
			JSONObject jo = null;
			try {
				jo = new JSONObject(result);
				String validate = jo.optString("validate", "0");
				if ("1".equals(validate)) {
					validateUser(jo);
				} else {
					invalidateUser(jo);
				}
			} catch (Exception e) {
				Toast.makeText(this.machine.getContext(), "数据加载异常",
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Toast.makeText(this.machine.getContext(), "数据加载异常",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void invalidateUser(JSONObject jo) {
		String msg = jo.optString("msg");
		if (msg == null || "".equals(msg) || "null".equals(msg)) {
			msg = "该用户无权访问此界面";
		}
		Toast.makeText(this.machine.getContext(), msg, Toast.LENGTH_LONG)
				.show();
	}

	private void validateUser(JSONObject jo) {
		JSONArray ja_area = jo.optJSONArray("areaInfo");
		this.areaSelector.parseAreaInfo(ja_area);
		if (isFirst) {
			this.dateSelector.setDate(jo.optString(IBOItem.KEY_DATE));
			this.areaSelector.setSelectArea(0);
			String areaLevel = this.areaSelector.getAreaLevel();
			int level = Integer.parseInt(areaLevel);
			if (level <= 3) {
				this.state = this.hightState;
			} else {
				this.state = this.lowState;
			}
			this.state.addView();
			this.state.setArea(areaSelector);
			this.state.setDate(dateSelector);
			isFirst = false;
		} else {

		}
		this.state.updateView(jo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.IBOState#getViewContainer
	 * ()
	 */
	@Override
	public View getViewContainer() {
		LayoutInflater inflater = LayoutInflater
				.from(this.machine.getContext());
		View main = inflater.inflate(R.layout.business_outlets_main,
				this.machine, false);
		titleContainer = (RelativeLayout) main
				.findViewById(R.id.business_outlets_title);
		buttonContainer = (RelativeLayout) main
				.findViewById(R.id.business_outlets_button);
		this.listView = (ListView) main.findViewById(R.id.listView);
		View emptyView = main.findViewById(R.id.id_emptyview);
		this.listView.setEmptyView(emptyView);
		return main;
	}

	/**
	 * @return the titleContainer
	 */
	public RelativeLayout getTitleContainer() {
		return titleContainer;
	}

	/**
	 * @return the buttonContainer
	 */
	public RelativeLayout getButtonContainer() {
		return buttonContainer;
	}

	/**
	 * @return the listView
	 */
	public ListView getListView() {
		return listView;
	}

	public Context getContext() {
		return this.machine.getContext();
	}
}
