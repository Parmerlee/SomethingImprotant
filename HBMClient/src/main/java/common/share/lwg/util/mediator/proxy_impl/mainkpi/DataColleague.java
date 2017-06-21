/**
 * DataColleague
 */
package common.share.lwg.util.mediator.proxy_impl.mainkpi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.util.NumberUtil;
import com.bonc.mobile.hbmclient.util.JsonUtil;

/**
 * @author liweigao
 *         显示规则：不对KPI数据进行为空判断 二级列表默认展开 2016年2月19日15:53:56
 * 
 */
public class DataColleague extends AMainKpiColleague {
	private String isGroup;
	private List<Map<String, String>> columnInfoRight;
	private Map<String, List<Double>> trendData;
	private Map<String, Map<String, String>> kpiData;
	private List<Map<String, Object>> compositeGroup;
	private List<String> compositeList;
	private List<Map<String, String>> columnInfoLeft;
	public Map<String, Map<String, String>> kpiConfigInfo;
	private String menuCode;
	private String areaCode;
	private String dateType;
	private String optime;
	public Map<String, List<String>> levelMap;

	Map<String, Boolean> list_status;

	boolean isSecond_show;

	public void setData(JSONObject allJO) throws Exception {
		
		isGroup = allJO.getString("isGroup");
		JSONArray columnJA = allJO.optJSONArray("showColumn");
		if (columnJA == null) {
			columnJA = new JSONArray();
		}
		columnInfoRight = JsonUtil.toDataList(columnJA);
		JSONObject kpiConfigJO = allJO.optJSONObject("kpiInfo");
		if (kpiConfigJO == null) {
			kpiConfigJO = new JSONObject();
		}
		this.kpiConfigInfo = JsonUtil.toMap2(kpiConfigJO);
		JSONObject trendJO = allJO.optJSONObject("trend_data");
		if (trendJO == null) {
			trendJO = new JSONObject();
		}
		trendData = JsonUtil.toTrendMap(trendJO);
		JSONObject kpiJO = allJO.optJSONObject("base_data");
		if (kpiJO == null) {
			kpiJO = new JSONObject();
		}
		kpiData = JsonUtil.toMap2(kpiJO);

		levelMap = new HashMap<String, List<String>>();
		list_status = new HashMap<String, Boolean>();

		isSecond_show = ((MainKpiMediator) this.getMediator())
				.getViewColleague().getActivity().getIntent()
				.getBooleanExtra("show_second", true);


		if ("1".equals(isGroup)) {
			JSONArray compositeJA = allJO.optJSONArray("composite");
			if (compositeJA == null) {
				compositeJA = new JSONArray();
			}
			compositeGroup = JsonUtil.toList2(compositeJA);

			for (int i = 0; i < compositeGroup.size(); i++) {
				Map<String, Object> item = compositeGroup.get(i);
				List<String> kpicodes = (List<String>) item.get("KPICODELIST");
				filterSPKpi(item, kpicodes);
				
				levelMap.putAll(buildLevelMap(kpicodes));
				filterKpiList2(kpicodes);
			}
		} else {
			JSONArray compositeJA = allJO.optJSONArray("composite");
			if (compositeJA == null) {
				compositeJA = new JSONArray();
			}
			compositeList = JsonUtil.toReportList(compositeJA);

			levelMap.putAll(buildLevelMap(compositeList));
			filterKpiList2(compositeList);
		}

		this.areaCode = allJO.optString("areaCode");
		this.dateType = allJO.optString("dateType");
		this.menuCode = allJO.optString("menuCode");
		this.optime = allJO.optString("optime");
		createTitleLeftData();

		this.mainKpiMediator.updateView();
	}

	public boolean isEmpty() {
		if ("1".equals(isGroup))
			return compositeGroup.size() == 0;
		else
			return compositeList.size() == 0;
	}

	Map<String, List<String>> buildLevelMap(List<String> list) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (String kpiCode : list) {
			if (getKpiLevel(kpiCode) != 2) {
				List<String> second = new ArrayList<String>();
				for (String c : list) {
					if (kpiCode.equals(kpiConfigInfo.get(c).get(
							"PARENT_KPI_CODE"))) {
						second.add(c);
					}
				}
				map.put(kpiCode, second);
			}
		}
		return map;
	}

	int getKpiLevel(String kpiCode) {
		return NumberUtil.changeToInt(kpiConfigInfo.get(kpiCode).get(
				"KPI_LEVEL"));
	}

	boolean isAllNull(Collection<String> c) {
		for (String s : c) {
			if (s != null)
				return false;
		}
		return true;
	}

	void filterSPKpi(Map<String, Object> group, List<String> list) {
		Iterator it = list.iterator();
		while (it.hasNext()) {
			String kpiCode = (String) it.next();
			if (kpiCode.startsWith("PBN")) {
				if (kpiData.containsKey(kpiCode))
					group.put("GROUP_Progress", kpiCode);
				it.remove();
			}
		}
	}

	void filterKpiList1(List<String> list) {
		Iterator it = list.iterator();
		while (it.hasNext()) {
			String kpiCode = (String) it.next();
			Map<String, String> singleData = kpiData.get(kpiCode);
			if (getKpiLevel(kpiCode) == 2) {
				if (singleData == null || isAllNull(singleData.values()))
					it.remove();
			}
		}
	}

	void filterKpiList2(List<String> list) {
		for (String kpiCode : levelMap.keySet()) {
			list_status.put(kpiCode, isSecond_show);
			Map<String, String> singleData = kpiData.get(kpiCode);
			if (singleData == null || isAllNull(singleData.values())) {
				// list.remove(kpiCode);
				// for (String c : levelMap.get(kpiCode)) {
				// list.remove(c);
				// }
			} else {
				if (!isSecond_show) {

					for (String c : levelMap.get(kpiCode)) {
						list.remove(c);
					}
				}
			}
		}
	}

	public int getPosOnListView(String kpiCode) {

		int pos_sum = 0;
		if (isGroup.equals("1")) {
			for (int i = 0; i < compositeGroup.size(); i++) {
				Map<String, Object> item = compositeGroup.get(i);
				List<String> kpicodes = (List<String>) item.get("KPICODELIST");

				int tem = kpicodes.indexOf(kpiCode);
				if (tem != -1) {
					pos_sum += tem;
					break;
				}
				pos_sum += kpicodes.size() + 1;
			}
		} else {

			int tem = compositeList.indexOf(kpiCode);
			if (tem != -1) {
				pos_sum = tem;
			}
		}
		return pos_sum;
	}

	// 显隐二级指标
	public void showSecondLevelKpi(String kpiCode, boolean show) {
		if (!show) {
			if (isGroup.equals("1")) {
				for (int i = 0; i < compositeGroup.size(); i++) {
					Map<String, Object> item = compositeGroup.get(i);
					List<String> kpicodes = (List<String>) item
							.get("KPICODELIST");
					for (int j = 0; j < kpicodes.size(); j++) {
						if (TextUtils.equals(kpicodes.get(j), kpiCode)) {
							for (int k = 0; k < levelMap.get(kpiCode).size(); k++) {
								kpicodes.add(j + k + 1, levelMap.get(kpiCode)
										.get(k));
							}
						}
					}
				}
			} else {
				for (int i = 0; i < compositeList.size(); i++) {
					if (TextUtils.equals(kpiCode, compositeList.get(i))) {

						for (int j = 0; j < levelMap.get(kpiCode).size(); j++) {
							compositeList.add(j + i + 1, levelMap.get(kpiCode)
									.get(j));
						}
					}
				}
			}

		} else {
			if (isGroup.equals("1")) {
				for (int i = 0; i < compositeGroup.size(); i++) {
					Map<String, Object> item = compositeGroup.get(i);
					List<String> kpicodes = (List<String>) item
							.get("KPICODELIST");

					for (String str : levelMap.get(kpiCode))
						kpicodes.remove(str);
				}
			} else {
				for (String str : levelMap.get(kpiCode)) {

					compositeList.remove(str);
				}

			}
		}
		list_status.put(kpiCode, !show);
		this.mainKpiMediator.updateView();
	}

	public boolean isSecondLevelShow(String kpiCode) {
		return list_status.containsKey(kpiCode) && list_status.get(kpiCode);
	}

	private void createTitleLeftData() {
		this.columnInfoLeft = new ArrayList<Map<String, String>>();
		Map<String, String> left = new HashMap<String, String>();
		left.put("COLUMNTYPE", "2");
		left.put("RELATION_KPIVALUE_COLUMN_NAME", "指标名称");
		this.columnInfoLeft.add(left);
	}

	/**
	 * @return the isGroup
	 */
	public String getIsGroup() {
		return isGroup;
	}

	/**
	 * @return the columnInfo
	 */
	public List<Map<String, String>> getColumnInfoRight() {
		return columnInfoRight;
	}

	/**
	 * @return the trendData
	 */
	public Map<String, List<Double>> getTrendData() {
		return trendData;
	}

	/**
	 * @return the kpiData
	 */
	public Map<String, Map<String, String>> getKpiData() {
		return kpiData;
	}

	/**
	 * @return the composite
	 */
	public List<Map<String, Object>> getCompositeGroup() {
		return compositeGroup;
	}

	/**
	 * @return the columnInfoLeft
	 */
	public List<Map<String, String>> getColumnInfoLeft() {
		return columnInfoLeft;
	}

	/**
	 * @return the kpiConfigInfo
	 */
	public Map<String, Map<String, String>> getKpiConfigInfo() {
		return kpiConfigInfo;
	}

	/**
	 * @return the menuCode
	 */
	public String getMenuCode() {
		return menuCode;
	}

	/**
	 * @return the areaCode
	 */
	public String getAreaCode() {
		return areaCode;
	}

	/**
	 * @return the dateType
	 */
	public String getDateType() {
		return dateType;
	}

	/**
	 * @return the optime
	 */
	public String getOptime() {
		return optime;
	}

	/**
	 * @return the compositeList
	 */
	public List<String> getCompositeList() {
		return compositeList;
	}

}
