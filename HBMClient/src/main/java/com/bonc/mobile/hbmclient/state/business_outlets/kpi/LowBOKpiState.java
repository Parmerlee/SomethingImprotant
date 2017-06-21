/**
 * LowBOKpiState
 */
package com.bonc.mobile.hbmclient.state.business_outlets.kpi;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.BusinessOutletsKpiActivity;
import com.bonc.mobile.hbmclient.asyn_task.DailyReportAsynTask;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.view.adapter.BusinessOutletsKpiLeftAdapter;
import com.bonc.mobile.hbmclient.view.adapter.BusinessOutletsKpiRightLowAdapter;

/**
 * @author liweigao
 *
 */
public class LowBOKpiState extends ABOKpiState {
	private TextView kpiNameTV;
	private TextView columnName1;
	private TextView columnName3;

	private String kpiType;
	private int type;
	private String typeName;

	private List<Map<String, String>> data;
	private String key1, key3;

	private BusinessOutletsKpiLeftAdapter leftAdapter;
	private BusinessOutletsKpiRightLowAdapter rightAdapter;

	/**
	 * @param a
	 * @param date
	 * @param areaCode
	 */
	public LowBOKpiState(BusinessOutletsKpiActivity a, int type, String date,
			String areaCode) {
		super(a, date, areaCode);
		this.type = type;
		if (type == 1) {
			this.typeQuest = "public";
			this.typeName = "社会渠道";
			this.key1 = "publicCurrent";
			this.key3 = "publicTotal";
		} else {
			this.typeQuest = "private";
			this.typeName = "自有渠道";
			this.key1 = "privateCurrent";
			this.key3 = "privateTotal";
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.kpi.ABOKpiState#create()
	 */
	@Override
	public void create() {
		super.create();
		LayoutInflater inflater = LayoutInflater.from(this.machine);
		LayoutParams lp = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		View titleSingle = inflater.inflate(
				R.layout.business_outlets_kpi_right_title, titleContainer,
				false);
		TextView typeTV = (TextView) titleSingle.findViewById(R.id.type);
		typeTV.setText(typeName);
		this.titleContainer.removeAllViews();
		this.titleContainer.addView(titleSingle, lp);
		this.kpiNameTV = (TextView) this.machine.findViewById(R.id.titleName);
		this.columnName1 = (TextView) titleSingle
				.findViewById(R.id.columnName1);
		this.columnName3 = (TextView) titleSingle
				.findViewById(R.id.columnName3);
		this.columnName1.setTag(key1);
		this.columnName3.setTag(key3);
		TextView[] tvs = { this.columnName1, this.columnName3 };
		for (TextView tv : tvs) {
			tv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String tag = (String) v.getTag();
					state.sort(data, tag, v);
				}
			});
		}
		questData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.kpi.ABOKpiState#parseData
	 * (java.lang.String)
	 */
	@Override
	protected void parseData(String s) {
		JSONObject jo = null;
		try {
			jo = new JSONObject(s);
			JSONObject headJO = jo.optJSONObject("header");
			String kpiName = headJO.optString("kpiName");
			this.kpiNameTV.setText(kpiName);
			String kpiUnit = headJO.optString("kpiUnit");
			kpiUnit = "(" + kpiUnit + ")";
			columnName1.setText("当日值" + kpiUnit);
			columnName3.setText("日累计值" + kpiUnit);
			resetTitle();
			JSONArray dataJA = jo.optJSONArray("data");
			data = JsonUtil.toList(dataJA);
			if (data != null) {
				if (isFirst) {
					noticeTask = new DailyReportAsynTask(titleContainer,
							"点击可排序");
					noticeTask.execute();
					isFirst = false;
				} else {

				}
			}
			this.leftAdapter = new BusinessOutletsKpiLeftAdapter(data);
			this.listViewLeft.setAdapter(this.leftAdapter);
			this.rightAdapter = new BusinessOutletsKpiRightLowAdapter(data,
					type);
			this.listViewRight.setAdapter(this.rightAdapter);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.business_outlets.kpi.IBOKpiState#resetSort
	 * ()
	 */
	@Override
	public void resetSort() {
		resetTitle();
		this.leftAdapter.notifyDataSetChanged();
		this.rightAdapter.notifyDataSetChanged();
	}

	private void resetTitle() {
		this.columnName1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		this.columnName3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
	}
}
