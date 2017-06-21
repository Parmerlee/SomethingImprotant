/**
 * ChannelHallInfoEnum
 */
package com.bonc.mobile.hbmclient.enum_type;

/**
 * @author liweigao
 *
 */
public enum ChannelHallInfoEnum {
	DATE("统计时间", "hallDate"), CITY("地市", "hallCity"), DISTRICT("区县",
			"hallDistrict"), WEBSITE("网点名称", "hallWebsite"), CHANNEL_CODE(
			"渠道编码", "hallChannelCode"), CHANNEL_TYPE("渠道类型", "");

	private final String tagName;
	private final String keyValue;

	private ChannelHallInfoEnum(String tn, String kv) {
		this.tagName = tn;
		this.keyValue = kv;
	}

	/**
	 * @return the tagName
	 */
	public String getTagName() {
		return tagName;
	}

	/**
	 * @return the keyValue
	 */
	public String getKeyValue() {
		return keyValue;
	}

}
