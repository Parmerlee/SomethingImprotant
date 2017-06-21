/**
 * ChannelCostDetailsEnum
 */
package com.bonc.mobile.hbmclient.enum_type;

/**
 * @author liweigao
 *
 */
public enum ChannelCostDetailsEnum {
	VALUEN1("valuen1", "水电费", "元"), VALUEN2("valuen2", "营业员人数", "人"), VALUEN3(
			"valuen3", "营业员平均工资", "元"), VALUEN4("valuen4", "人工成本", "元"), VALUEN5(
			"valuen5", "建设装修折旧", "元"), VALUEN6("valuen6", "办公家具折旧", "元"), VALUEN7(
			"valuen7", "设备折旧租金", "元"), VALUEN8("valuen8", "房屋租金", "元"), VALUEN9(
			"valuen9", "其他费用", "元");

	private final String keyValue;
	private final String tagName;
	private final String unit;

	private ChannelCostDetailsEnum(String kv, String tn, String unit) {
		this.keyValue = kv;
		this.tagName = tn;
		this.unit = unit;
	}

	/**
	 * @return the keyValue
	 */
	public String getKeyValue() {
		return keyValue;
	}

	/**
	 * @return the tagName
	 */
	public String getTagName() {
		return tagName;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
}
