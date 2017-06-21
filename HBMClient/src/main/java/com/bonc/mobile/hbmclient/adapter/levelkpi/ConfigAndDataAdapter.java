/**
 * ConfigAndDataAdapter
 */
package com.bonc.mobile.hbmclient.adapter.levelkpi;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bonc.mobile.hbmclient.data.KpiData;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.util.DateUtil;

/**
 * @author liweigao
 * 
 */
public class ConfigAndDataAdapter extends AConfigAndDataAdapter {
	private KpiData kpiData;
	private String showTime;

	private final String KEY_BACK_DATE = "date";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.adapter.levelkpi.IConfigAndDataAdapter#handle()
	 */
	@Override
	public void handle() throws JSONException {
		bulidNewKpi();

		JSONObject jsonObject = null;
		jsonObject = new JSONObject(backData);
		JSONArray trendData = jsonObject.optJSONArray("trenddata");
		if (trendData != null) {
			kpiData.buildTrendDataFromJson(trendData,
					this.visitor.getColKeyComposite(),
					this.visitor.getMenuCode());
		}

		JSONArray basedata = jsonObject.optJSONArray("base_data");
		if (basedata != null) {
			kpiData.buildDataFromJson(basedata,
					this.visitor.getComplexDimInfo(),
					this.visitor.getMenuCode());
		}

		handleDate(jsonObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.adapter.levelkpi.IConfigAndDataAdapter#getShowTime
	 * ()
	 */
	@Override
	public String getShowTime() {
		// TODO Auto-generated method stub
		return showTime;
	}

	public List<Map<String, String>> getKpiData() {
		String groupTag = this.kpiData.getGroupTag();
		return this.kpiData.getSubList().get(groupTag);
	}

	/**
	 * @return
	 * @see com.bonc.mobile.hbmclient.data.KpiData#getTrendList()
	 */
	public Map<String, List<Double>> getTrendList() {
		return kpiData.getTrendList();
	}

	private void bulidNewKpi() {
		// TODO Auto-generated method stub
		kpiData = new KpiData();
		kpiData.setColkey(this.visitor.getColKey());
		kpiData.setTitleName(this.visitor.getTitleName());
		kpiData.setColInfoMap(this.visitor.getMenuColInfoMap());
		kpiData.setKpiInfoMap(this.visitor.getKpiInfoMap());
	}

	private void handleDate(JSONObject jo) {
		DateRangeEnum dre = this.visitor.getDataType();
		try {
			if (jo.isNull(KEY_BACK_DATE)
					|| "".equals(jo.getString(KEY_BACK_DATE))) {
				this.showTime = dre.getDateSpecified(-1,
						dre.getDateShowPattern());
			} else {
				String date = jo.getString(KEY_BACK_DATE);
				this.showTime = DateUtil.oneStringToAntherString(date,
						dre.getDateServerPattern(), dre.getDateShowPattern());
			}
		} catch (JSONException e) {
			this.showTime = dre.getDateSpecified(-1, dre.getDateShowPattern());
		}
		String optime = DateUtil.oneStringToAntherString(showTime,
				dre.getDateShowPattern(), dre.getDateServerPattern());
		this.visitor.setOpTime(optime);
	}

}
