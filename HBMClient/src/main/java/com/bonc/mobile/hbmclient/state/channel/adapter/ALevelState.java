/**
 * ALevelState
 */
package com.bonc.mobile.hbmclient.state.channel.adapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.enum_type.ChannelCostDetailsEnum;
import com.bonc.mobile.hbmclient.enum_type.ChannelHallInfoEnum;
import com.bonc.mobile.hbmclient.enum_type.ChannelIncomeDetailsEnum;
import com.bonc.mobile.hbmclient.util.NumberUtil;

/**
 * @author liweigao
 * 
 */
public class ALevelState {
	private DetailsMachine machine;

	protected JSONObject data;
	protected List<Map<String, String>> groupData;
	protected int groupLayout;
	protected String[] groupFrom;
	protected int[] groupTo;
	protected List<List<Map<String, String>>> childData;
	protected int childLayout;
	protected String[] childFrom;
	protected int[] childTo;

	public final static String KEY_GROUP_NAME = "group_name";
	public final static String KEY_GROUP_STRETCH = "group_stretch";
	public final static String KEY_CHILD_NAME = "child_name";
	public final static String KEY_CHILD_VALUE = "child_value";
	public final static String KEY_VALUE_KEY = "value_key";

	public ALevelState(DetailsMachine m) {
		this.machine = m;
	}

	public void setData(JSONObject jo) {
		this.data = jo;
	}

	/**
	 * @return the groupData
	 */
	public List<Map<String, String>> getGroupData() {
		return groupData;
	}

	/**
	 * @return the groupLayout
	 */
	public int getGroupLayout() {
		this.groupLayout = R.layout.channel_details_group_layout;
		return groupLayout;
	}

	/**
	 * @return the groupFrom
	 */
	public String[] getGroupFrom() {
		this.groupFrom = new String[] { KEY_GROUP_NAME };
		return groupFrom;
	}

	/**
	 * @return the groupTo
	 */
	public int[] getGroupTo() {
		this.groupTo = new int[] { R.id.id_groupName };
		return groupTo;
	}

	/**
	 * @return the childData
	 */
	public List<List<Map<String, String>>> getChildData() {
		return childData;
	}

	/**
	 * @return the childLayout
	 */
	public int getChildLayout() {
		this.childLayout = R.layout.channel_details_child_layout;
		return childLayout;
	}

	/**
	 * @return the childFrom
	 */
	public String[] getChildFrom() {
		this.childFrom = new String[] { KEY_CHILD_NAME, KEY_CHILD_VALUE };
		return childFrom;
	}

	/**
	 * @return the childTo
	 */
	public int[] getChildTo() {
		this.childTo = new int[] { R.id.id_keyName, R.id.id_value };
		return childTo;
	}

	protected List<Map<String, String>> getIncomeDetails() {
		List<Map<String, String>> income = new ArrayList<Map<String, String>>();
		ChannelIncomeDetailsEnum[] elements = ChannelIncomeDetailsEnum.values();
		for (ChannelIncomeDetailsEnum element : elements) {
			Map<String, String> map = new HashMap<String, String>();
			map.put(KEY_CHILD_NAME, element.getTagName());
			map.put(KEY_VALUE_KEY, element.getKeyValue());

			String value = null;
			if (this.data == null) {
				value = "--";
			} else {
				value = this.data.optString(element.getKeyValue());
			}
			if (value == null || "".equals(value) || "null".equals(value)) {
				value = "--";
			} else {
			}
			try {
				value = NumberFormat.getNumberInstance().format(
						NumberUtil.changeToDouble(value));
			} catch (Exception e) {
				// TODO: handle exception
			}

			value += element.getUnit();
			map.put(KEY_CHILD_VALUE, value);
			income.add(map);
		}
		return income;
	}

	protected List<Map<String, String>> getCostDetails() {
		List<Map<String, String>> cost = new ArrayList<Map<String, String>>();
		ChannelCostDetailsEnum[] cost_elements = ChannelCostDetailsEnum
				.values();
		for (ChannelCostDetailsEnum element : cost_elements) {
			Map<String, String> map = new HashMap<String, String>();
			map.put(KEY_CHILD_NAME, element.getTagName());
			map.put(KEY_VALUE_KEY, element.getKeyValue());
			String value = null;
			if (this.data == null) {
				value = "--";
			} else {
				value = this.data.optString(element.getKeyValue());
			}
			if (value == null || "".equals(value) || "null".equals(value)) {
				value = "--";
			} else {

			}
			try {
				value = NumberFormat.getNumberInstance().format(
						NumberUtil.changeToDouble(value));
			} catch (Exception e) {
				// TODO: handle exception
			}

			value += element.getUnit();
			map.put(KEY_CHILD_VALUE, value);
			cost.add(map);
		}
		return cost;
	}

	protected List<Map<String, String>> getHallInfo() {
		List<Map<String, String>> hallInfo = new ArrayList<Map<String, String>>();
		JSONObject jo = null;
		if (this.data == null) {
			jo = new JSONObject();
		} else {
			jo = this.data.optJSONObject("hallDetail");
		}
		ChannelHallInfoEnum[] elements = ChannelHallInfoEnum.values();
		for (ChannelHallInfoEnum element : elements) {
			Map<String, String> map = new HashMap<String, String>();
			map.put(KEY_CHILD_NAME, element.getTagName());
			map.put(KEY_VALUE_KEY, element.getKeyValue());
			String value = jo.optString(element.getKeyValue());
			if (value == null || "".equals(value) || "null".equals(value)) {
				value = "--";
			} else {

			}
			map.put(KEY_CHILD_VALUE, value);
			hallInfo.add(map);
		}
		return hallInfo;
	}

	protected Map<String, String> getGroupIncome() {
		Map<String, String> income = new HashMap<String, String>();
		income.put(KEY_GROUP_NAME, "收入明细");
		return income;
	}

	protected Map<String, String> getGroupCost() {
		Map<String, String> cost = new HashMap<String, String>();
		cost.put(KEY_GROUP_NAME, "成本明细");
		return cost;
	}

	protected Map<String, String> getGroupHall() {
		Map<String, String> hall = new HashMap<String, String>();
		hall.put(KEY_GROUP_NAME, "厅信息");
		return hall;
	}

}
