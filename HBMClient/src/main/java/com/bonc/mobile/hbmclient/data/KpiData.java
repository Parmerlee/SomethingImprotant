package com.bonc.mobile.hbmclient.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.util.NumberUtil;

public class KpiData {

	private boolean hasGroup = false; // 是否具有分组.

	private List<Map<String, String>> groupList; // 分组信息. Map<grouptag 代表 组的标示
													// title组标题.>

	private Map<String, List<Map<String, String>>> subList; // 分组的数据. 根据grouptag
															// 获取.

	private Map<String, List<Double>> trendList; // 趋势图数据. 拿 grouptag||kpi_code
													// 获取 趋势图.
	private String[] trend_kpicode;

	private String[] colkey; // 要显示的列的key
	private String[] titleName; // 要显示的指标名title.

	private boolean hasTrendChart = true; // 是否有趋势图
	private String trendChartTitle = "趋势图"; // 趋势图名称.

	private Map<String, KpiInfo> kpiInfoMap; // kpi信息.
	private Map<String, MenuColumnInfo> colInfoMap; // 菜单列和列的基本信息.
	private String groupTag;

	private Map<String, Map<String, String>> mainKpiInfoMap;
	private SQLHelper sqlHelper = new SQLHelper();

	/**
	 * 构建所需的基本数据 .
	 */
	public void buildDataFromJson(JSONArray jsonbasedata,
			ComplexDimInfo complexDimInfo, String mc) {

		if (groupList == null) {
			groupList = new ArrayList<Map<String, String>>();
		} else {
			groupList.clear();
		}

		if (subList == null) {
			subList = new HashMap<String, List<Map<String, String>>>();
		} else {
			subList.clear();
		}

		this.setHasGroup(complexDimInfo.isHasGroup());
		Map<String, String> kpiMenuRel = new BusinessDao()
				.getKpiMenuRelation(mc);
		Map<String, String> kpiMenuNameRel = new BusinessDao()
				.getKpiMenuNameRelation(mc);
		int len = jsonbasedata.length();
		int colen = colkey.length;

		for (int i = 0; i < len; i++) {
			// 菜单+复合维度可以确定一个唯一分组.
			JSONObject jsonObject = jsonbasedata.optJSONObject(i);
			String kpi_code = jsonObject.optString("kpi_code");
			String menuCode = kpiMenuRel.get(kpi_code);
			String dimKey = jsonObject.optString("dim_key");
			String groupTag = menuCode + Constant.DEFAULT_CONJUNCTION + dimKey;
			this.groupTag = groupTag;

			int glen = groupList.size();
			int k = 0;
			for (; k < glen; k++) {
				if (groupTag.equals(groupList.get(k).get("groupTag"))) {
					break;
				}
			}
			// 如果分组没有添加进去 则添加分组.
			if (k == glen) {
				Map<String, String> group = new HashMap<String, String>();
				group.put("groupTag", groupTag);
				if (complexDimInfo.isDimExpand()) {
					// 如果事按维度展开 则应该取维度的名称作为分组的名称.
					group.put(
							"title",
							complexDimInfo.getExpandDimInfo().get(
									complexDimInfo.getComplexDimToExpandDim()
											.get(dimKey)));
				} else {
					// 如果是根据三级菜单分组 则应该拿三级菜单组修改.
					group.put("title", kpiMenuNameRel.get(kpi_code));
				}
				groupList.add(group);
			}
			// 判断子数据是否加入.
			List<Map<String, String>> sList;
			if (subList.containsKey(groupTag)) {
				sList = subList.get(groupTag);
			} else {
				sList = new ArrayList<Map<String, String>>();
				subList.put(groupTag, sList);
			}

			Map<String, String> kpidata = new HashMap<String, String>();
			kpidata.put("kpi_code", jsonObject.optString("kpi_code"));
			kpidata.put("kpi_define", jsonObject.optString("kpi_define"));
			kpidata.put("op_time", jsonObject.optString("op_time"));
			kpidata.put("dim_key", jsonObject.optString("dim_key"));
			kpidata.put("area_code", jsonObject.optString("area_code"));

			for (int m = 0; m < colen; m++) {
				kpidata.put(colkey[m], jsonObject.optString(colkey[m]));
			}
			sList.add(kpidata);

		}
	}

	/**
	 * 从json构建基本数据.
	 */
	public void buildData(List<Map<String, String>> basedata,
			ComplexDimInfo complexDimInfo, String menuCode) {

		if (groupList == null) {
			groupList = new ArrayList<Map<String, String>>();
		}

		if (subList == null) {
			subList = new HashMap<String, List<Map<String, String>>>();
		}

		this.setHasGroup(complexDimInfo.isHasGroup());

		List<Map<String, String>> menuKpiList = new BusinessDao()
				.getMenuAllKpiList(menuCode);

		int mklen = menuKpiList.size();
		int len = basedata.size();

		// 爲了避免一個指標掛在多個分類的時候出錯 . 所以從菜單與指標的關係開始循環 找相關指標.
		for (int m = 0; m < mklen; m++) {
			String kpicode1 = menuKpiList.get(m).get("kpi_code");
			String mc = menuKpiList.get(m).get("menu_code");
			String menu_name = menuKpiList.get(m).get("menu_name");
			for (int i = 0; i < len; i++) {
				// 菜单+复合维度可以确定一个唯一分组.
				String kpicode2 = basedata.get(i).get("kpi_code");

				if (!kpicode1.equals(kpicode2)) {
					continue;
				}

				String dimKey = basedata.get(i).get("dim_key");
				String groupTag = mc + Constant.DEFAULT_CONJUNCTION + dimKey;

				int glen = groupList.size();
				int k = 0;
				for (; k < glen; k++) {
					if (groupTag.equals(groupList.get(k).get("groupTag"))) {
						break;
					}
				}
				// 如果分组没有添加进去 则添加分组.
				if (k == glen) {
					Map<String, String> group = new HashMap<String, String>();
					group.put("groupTag", groupTag);
					if (complexDimInfo.isDimExpand()) {
						// 如果事按维度展开 则应该取维度的名称作为分组的名称.
						group.put(
								"title",
								complexDimInfo.getExpandDimInfo().get(
										complexDimInfo
												.getComplexDimToExpandDim()
												.get(dimKey)));
					} else {
						group.put("title", menu_name);
					}
					groupList.add(group);
				}
				// 判断子数据是否加入.
				if (subList.containsKey(groupTag)) {
					List<Map<String, String>> sList = subList.get(groupTag);
					sList.add(basedata.get(i));

				} else {
					List<Map<String, String>> sList = new ArrayList<Map<String, String>>();
					sList.add(basedata.get(i));
					subList.put(groupTag, sList);
				}
			}
		}
	}

	public void buildMainKpiData(List<Map<String, String>> basedata,
			ComplexDimInfo complexDimInfo, String menuCode) {

		if (groupList == null) {
			groupList = new ArrayList<Map<String, String>>();
		}

		if (subList == null) {
			subList = new HashMap<String, List<Map<String, String>>>();
		}

		this.setHasGroup(complexDimInfo.isHasGroup());

		List<Map<String, String>> menuKpiList = new BusinessDao()
				.getKpiOrderList(menuCode);

		int mklen = menuKpiList.size();
		int len = basedata.size();

		// 爲了避免一個指標掛在多個分類的時候出錯 . 所以從菜單與指標的關係開始循環 找相關指標.
		for (int m = 0; m < mklen; m++) {
			String kpicode1 = menuKpiList.get(m).get("kpi_code");
			String mc = menuKpiList.get(m).get("menu_code");
			String menu_name = menuKpiList.get(m).get("menu_name");
			for (int i = 0; i < len; i++) {
				// 菜单+复合维度可以确定一个唯一分组.
				String kpicode2 = basedata.get(i).get("kpi_code");

				if (!kpicode1.equals(kpicode2)) {
					continue;
				}

				String dimKey = basedata.get(i).get("dim_key");
				String groupTag = mc + Constant.DEFAULT_CONJUNCTION + dimKey;

				int glen = groupList.size();
				int k = 0;
				for (; k < glen; k++) {
					if (groupTag.equals(groupList.get(k).get("groupTag"))) {
						break;
					}
				}
				// 如果分组没有添加进去 则添加分组.
				if (k == glen) {
					Map<String, String> group = new HashMap<String, String>();
					group.put("groupTag", groupTag);
					if (complexDimInfo.isDimExpand()) {
						// 如果事按维度展开 则应该取维度的名称作为分组的名称.
						group.put(
								"title",
								complexDimInfo.getExpandDimInfo().get(
										complexDimInfo
												.getComplexDimToExpandDim()
												.get(dimKey)));
					} else {
						group.put("title", menu_name);
					}
					groupList.add(group);
				}
				// 判断子数据是否加入.
				if (subList.containsKey(groupTag)) {
					List<Map<String, String>> sList = subList.get(groupTag);
					sList.add(basedata.get(i));

				} else {
					List<Map<String, String>> sList = new ArrayList<Map<String, String>>();
					sList.add(basedata.get(i));
					subList.put(groupTag, sList);
				}
			}
		}
	}

	/**
	 * 构建趋势图数据. 参数为List<Map<String,String>> 类型.
	 */
	public void buildTrendData(List<Map<String, String>> trenddata,
			String colkey) {
		if (trendList == null) {
			trendList = new HashMap<String, List<Double>>();
		}

		try {
			int len = trenddata.size();
			this.trend_kpicode = new String[len];
			for (int i = 0; i < len; i++) {
				String groupTag = trenddata.get(i).get("group_tag");
				String kpiCode = trenddata.get(i).get("kpi_code");
				this.trend_kpicode[i] = kpiCode;
				String key = groupTag + Constant.DEFAULT_CONJUNCTION + kpiCode;

				if (trendList.containsKey(key)) {
					List<Double> valueList = trendList.get(key);
					try {
						valueList.add(NumberUtil.changeToDouble(trenddata
								.get(i).get(colkey)));
					} catch (Exception e) {
						e.printStackTrace();
						valueList.add(0.0);
					}

				} else {
					List<Double> valueList = new ArrayList<Double>();
					try {
						valueList.add(NumberUtil.changeToDouble(trenddata
								.get(i).get(colkey)));
					} catch (Exception e) {
						e.printStackTrace();
						valueList.add(0.0);
					}
					trendList.put(key, valueList);
				}

			}
		} catch (Exception e) {
			Log.e(this.getClass().toString(), "构建趋势图时候出错。 把趋势图数据置为 null.");
			e.printStackTrace();
			trendList = null;
		}

	}

	/**
	 * 构建趋势图的数据 。 参数为json数据.
	 */
	public void buildTrendDataFromJson(JSONArray jsonArray, String colkey,
			String menuCode) {
		if (jsonArray == null || colkey == null || "".equals(colkey)) {
			Log.e(this.getClass().toString(), "json 构建趋势图数据出错. 对象为空.");
		}

		Map<String, String> kpiMenuRel = new BusinessDao()
				.getKpiMenuRelation(menuCode);

		if (kpiMenuRel == null) {
			return;
		}

		if (trendList == null) {
			trendList = new HashMap<String, List<Double>>();
		} else {
			trendList.clear();
		}

		try {
			int len = jsonArray.length();

			for (int i = 0; i < len; i++) {
				JSONObject jsonObject = jsonArray.optJSONObject(i);
				String kpiCode = jsonObject.optString("kpi_code");
				String groupTag = kpiMenuRel.get(kpiCode)
						+ Constant.DEFAULT_CONJUNCTION
						+ jsonObject.optString("dim_key");

				String key = groupTag + Constant.DEFAULT_CONJUNCTION + kpiCode;
				if (trendList.containsKey(key)) {
					List<Double> valueList = trendList.get(key);
					valueList.add(NumberUtil.changeToDouble(jsonObject
							.optString(colkey)));

				} else {
					List<Double> valueList = new ArrayList<Double>();
					valueList.add(NumberUtil.changeToDouble(jsonObject
							.optString(colkey)));
					trendList.put(key, valueList);
				}
			}
		} catch (Exception e) {
			Log.e(this.getClass().toString(), "构建趋势图时候出错。 把趋势图数据置为 null.");
			e.printStackTrace();
			trendList = null;
		}

	}

	public boolean isHasTrendChart() {
		return hasTrendChart;
	}

	public void setHasTrendChart(boolean hasTrendChart) {
		this.hasTrendChart = hasTrendChart;
	}

	public String getTrendChartTitle() {
		return trendChartTitle;
	}

	public void setTrendChartTitle(String trendChartTitle) {
		this.trendChartTitle = trendChartTitle;
	}

	public boolean isHasGroup() {
		return hasGroup;
	}

	public void setHasGroup(boolean hasGroup) {
		this.hasGroup = hasGroup;
	}

	public List<Map<String, String>> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<Map<String, String>> groupList) {
		this.groupList = groupList;
	}

	public Map<String, List<Map<String, String>>> getSubList() {
		return subList;
	}

	public void setSubList(Map<String, List<Map<String, String>>> subList) {
		this.subList = subList;
	}

	public Map<String, List<Double>> getTrendList() {
		return trendList;
	}

	public void setTrendList(Map<String, List<Double>> trendList) {
		this.trendList = trendList;
	}

	public String[] getColkey() {
		return colkey;
	}

	public void setColkey(String[] colkey) {
		this.colkey = colkey;
	}

	public String[] getTitleName() {
		return titleName;
	}

	public void setTitleName(String[] titleName) {
		this.titleName = titleName;
	}

	public Map<String, MenuColumnInfo> getColInfoMap() {
		return colInfoMap;
	}

	public void setColInfoMap(Map<String, MenuColumnInfo> colInfoMap) {
		this.colInfoMap = colInfoMap;
	}

	public Map<String, KpiInfo> getKpiInfoMap() {
		return kpiInfoMap;
	}

	public void setKpiInfoMap(Map<String, KpiInfo> kpiInfoMap) {
		this.kpiInfoMap = kpiInfoMap;
	}

	public Map<String, Map<String, String>> getMainKpiInfoMap() {
		return this.mainKpiInfoMap;
	}

	public void setMainKpiInfoMap(Map<String, Map<String, String>> data) {
		this.mainKpiInfoMap = data;
	}

	public String getGroupTag() {
		return this.groupTag;
	}
}
