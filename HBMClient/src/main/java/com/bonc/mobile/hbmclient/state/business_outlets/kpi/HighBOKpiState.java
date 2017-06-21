/**
 * HightBOKpiState
 */
package com.bonc.mobile.hbmclient.state.business_outlets.kpi;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
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
import com.bonc.mobile.hbmclient.view.adapter.BusinessOutletsKpiRightHighAdapter;

/**
 * @author liweigao
 *
 */
public class HighBOKpiState extends ABOKpiState {
	private TextView kpiNameTV;
	private TextView columnName1;
	private TextView columnName3;
	private TextView columnName5;
	private TextView columnName7;
	private BusinessOutletsKpiLeftAdapter leftAdapter;
	private BusinessOutletsKpiRightHighAdapter rightAdapter;
	private List<Map<String, String>> data;

	/**
	 * @param a
	 * @param date
	 * @param areaCode
	 */
	public HighBOKpiState(BusinessOutletsKpiActivity a, String date,
			String areaCode) {
		super(a, date, areaCode);
		this.typeQuest = "public|private";
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
		View titleLeft = inflater.inflate(
				R.layout.business_outlets_kpi_right_title, titleContainer,
				false);
		this.titleContainer.addView(titleLeft, lp);
		View titleRight = inflater.inflate(
				R.layout.business_outlets_kpi_right_title, titleContainer,
				false);
		this.titleContainer.addView(titleRight, lp);
		TextView typeLeftTV = (TextView) titleLeft.findViewById(R.id.type);
		TextView typeRightTV = (TextView) titleRight.findViewById(R.id.type);
		typeLeftTV.setText("社会渠道");
		typeRightTV.setText("自有渠道");

		this.kpiNameTV = (TextView) this.machine.findViewById(R.id.titleName);
		this.columnName1 = (TextView) titleLeft.findViewById(R.id.columnName1);
		this.columnName3 = (TextView) titleLeft.findViewById(R.id.columnName3);
		this.columnName5 = (TextView) titleRight.findViewById(R.id.columnName1);
		this.columnName7 = (TextView) titleRight.findViewById(R.id.columnName3);
		this.columnName1.setTag("publicCurrent");
		this.columnName3.setTag("publicTotal");
		this.columnName5.setTag("privateCurrent");
		this.columnName7.setTag("privateTotal");
		TextView[] tvs = { this.columnName1, this.columnName3,
				this.columnName5, this.columnName7 };
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
			columnName5.setText("当日值" + kpiUnit);
			columnName7.setText("日累计值" + kpiUnit);
			resetTitle();
			JSONArray dataJA = jo.optJSONArray("data");
			data = JsonUtil.toList(dataJA);
			if (data != null) {
				if (isFirst) {
					noticeTask = new DailyReportAsynTask(columnName1, "点击可排序");
					noticeTask.execute();
					isFirst = false;
				} else {

				}
			}
			this.leftAdapter = new BusinessOutletsKpiLeftAdapter(data);
			this.listViewLeft.setAdapter(this.leftAdapter);
			this.rightAdapter = new BusinessOutletsKpiRightHighAdapter(data);
			this.listViewRight.setAdapter(this.rightAdapter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void reset() {
		columnName1.setText("当日值");
		columnName3.setText("日累计值");
		columnName5.setText("当日值");
		columnName7.setText("日累计值");
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
		this.columnName5.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		this.columnName7.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
	}
}
