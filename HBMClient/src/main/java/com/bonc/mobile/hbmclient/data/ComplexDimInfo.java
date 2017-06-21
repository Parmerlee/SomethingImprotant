package com.bonc.mobile.hbmclient.data;

import java.util.HashMap;
import java.util.Map;

/**
 * 维度复合信息
 * 
 * @author tengshibo
 *
 */
public class ComplexDimInfo {

	private String isMenuCofingRight = "rigth"; // 菜单维度信息配置是否正确. 错误为 error
												// 默认为right.

	private boolean isDimExpand = false; // 是否按第三维度展开.

	private boolean hasGroup = false; // 是否具有分组. 按第三维度展开的 和 二级菜单有三级菜单的 具有第三维度.

	private Map<String, String> expandDimToComplexDim = new HashMap<String, String>(); // 按第三维度展开以后得维度和和复合维度的对应关系.

	private Map<String, String> complexDimToExpandDim = new HashMap<String, String>();// 按第三维度展开以后的复合维度和维度的对应关系.

	private Map<String, String> expandDimInfo = new HashMap<String, String>(); // 展开维度的信息.

	private String complexDimKeyString; // 复合维度的字符串表示法. 默认用 | 隔开.

	public String getIsMenuCofingRight() {
		return isMenuCofingRight;
	}

	public void setIsMenuCofingRight(String isMenuCofingRight) {
		this.isMenuCofingRight = isMenuCofingRight;
	}

	public boolean isDimExpand() {
		return isDimExpand;
	}

	public void setDimExpand(boolean isDimExpand) {
		this.isDimExpand = isDimExpand;
	}

	public boolean isHasGroup() {
		return hasGroup;
	}

	public void setHasGroup(boolean hasGroup) {
		this.hasGroup = hasGroup;
	}

	public Map<String, String> getExpandDimToComplexDim() {
		return expandDimToComplexDim;
	}

	public void setExpandDimToComplexDim(
			Map<String, String> expandDimToComplexDim) {
		this.expandDimToComplexDim = expandDimToComplexDim;
	}

	public Map<String, String> getComplexDimToExpandDim() {
		return complexDimToExpandDim;
	}

	public void setComplexDimToExpandDim(
			Map<String, String> complexDimToExpandDim) {
		this.complexDimToExpandDim = complexDimToExpandDim;
	}

	public Map<String, String> getExpandDimInfo() {
		return expandDimInfo;
	}

	public void setExpandDimInfo(Map<String, String> expandDimInfo) {
		this.expandDimInfo = expandDimInfo;
	}

	public String getComplexDimKeyString() {
		return complexDimKeyString;
	}

	public void setComplexDimKeyString(String complexDimKeyString) {
		this.complexDimKeyString = complexDimKeyString;
	}

}
