/**
 * RelationKpiConfigAndDataAdapter
 */
package com.bonc.mobile.hbmclient.adapter.levelkpi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.JsonUtil;

/**
 * @author liweigao
 * 
 */
public class RelationKpiConfigAndDataAdapter extends AConfigAndDataAdapter {

	private List<String> groupList;
	private List<List<Map<String, String>>> kpiList;
	private Map<String, String> mainKpiData;
	private Map<String, List<Double>> trendMap;
	private Map<String, Map<String, String>> kpiInfo;
	private String mainKpiName;
	private List<Double> mainKpiTrend;
	private String mainKpiCode;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.adapter.levelkpi.AConfigAndDataAdapter#handle()
	 */
	@Override
	public void handle() throws JSONException {
		JSONObject jo = new JSONObject(this.backData);
		if (!jo.has("kpiList"))
			return;
		JSONArray kpiListJA = jo.getJSONArray("kpiList");
		JSONObject trendListJO = jo.getJSONObject("trendData");

		this.groupList = new ArrayList<String>();
		this.kpiList = new ArrayList<List<Map<String, String>>>();
		for (int i = 0; i < kpiListJA.length(); i++) {
			JSONObject item = kpiListJA.getJSONObject(i);
			if (i == 0) {
				this.mainKpiData = JsonUtil.toMap(item.getJSONArray("dataList")
						.getJSONObject(0));
			} else {
				groupList.add(item.getJSONObject("groupInfo").getString(
						"group_name"));
				List<Map<String, String>> dataList = JsonUtil.toDataList(item
						.getJSONArray("dataList"));
				this.kpiList.add(dataList);
			}
		}

		this.trendMap = JsonUtil.toTrendMap(trendListJO);
		Set<String> key = this.trendMap.keySet();
		this.kpiInfo = this.visitor.getKpiRuleAndUnit(key);
		setMainKpiData();
	}

	private void setMainKpiData() {
		this.mainKpiCode = this.mainKpiData.get("rel_kpi_code");
		Map<String, String> kpi_property = this.kpiInfo.get(mainKpiCode);
		this.mainKpiName = kpi_property.get("kpi_name");
		this.mainKpiTrend = this.trendMap.get(mainKpiCode);
		this.mainKpiData.putAll(kpi_property);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.adapter.levelkpi.AConfigAndDataAdapter#
	 * getKpiRuleAndUnit()
	 */
	@Override
	public Map<String, Map<String, String>> getKpiRuleAndUnit() {
		return this.kpiInfo;
	}

	/**
	 * @return the mainKpiData
	 */
	public Map<String, String> getMainKpiData() {
		return mainKpiData;
	}

	/**
	 * @return the mainKpiCode
	 */
	public String getMainKpiCode() {
		return mainKpiCode;
	}

	/**
	 * @return the mainKpiName
	 */
	public String getMainKpiName() {
		return mainKpiName;
	}

	/**
	 * @return the mainKpiTrend
	 */
	public List<Double> getMainKpiTrend() {
		return mainKpiTrend;
	}

	/**
	 * @return the groupList
	 */
	@Override
	public List<String> getGroupList() {
		return groupList;
	}

	/**
	 * @return the kpiList
	 */
	@Override
	public List<List<Map<String, String>>> getKpiList() {
		return kpiList;
	}

	/**
	 * @return the trendMap
	 */
	@Override
	public Map<String, List<Double>> getTrendMap() {
		return trendMap;
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
		String showDate = null;
		String optime = this.visitor.getOpTime();
		DateRangeEnum dre = this.visitor.getDataType();
		if (optime == null) {
			showDate = dre.getDefaultDate();
		} else {
			showDate = DateUtil.oneStringToAntherString(optime,
					dre.getDateServerPattern(), dre.getDateShowPattern());
		}

		return showDate;
	}

}
