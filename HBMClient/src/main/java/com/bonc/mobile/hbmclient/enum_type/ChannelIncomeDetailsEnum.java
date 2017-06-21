/**
 * ChannelIncomeDetails
 */
package com.bonc.mobile.hbmclient.enum_type;

/**
 * @author liweigao
 *
 */
public enum ChannelIncomeDetailsEnum {
	VALUEN10("valuen10", "营业场地出租性收入", "元"), VALUEN11("valuen11", "等效广告面积", ""), VALUEN12(
			"valuen12", "等效广告租金", "元/平方米"), VALUEN13("valuen13", "等效广告收入租金",
			"元/月"), VALUEN14("valuen14", "其它等效收入", "元"), VALUEN15("valuen15",
			"放号收入", "元"), VALUEN16("valuen16", "缴费收入", "元"), VALUEN17(
			"valuen17", "定制终端收入", "元"), VALUEN18("valuen18", "数据业务收入", "元"), VALUEN19(
			"valuen19", "代办业务收入", "元"), VALUEN20("valuen20", "激励考核收入", "元"), VALUEN21(
			"valuen21", "电子渠道收入", "元");

	private final String keyValue;
	private final String tagName;
	private final String unit;

	private ChannelIncomeDetailsEnum(String kv, String tn, String unit) {
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
