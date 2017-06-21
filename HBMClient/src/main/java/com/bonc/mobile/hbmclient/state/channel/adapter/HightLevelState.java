/**
 * HightLevelState
 */
package com.bonc.mobile.hbmclient.state.channel.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liweigao
 * 
 */
public class HightLevelState extends ALevelState {
	/**
	 * @param m
	 */
	public HightLevelState(DetailsMachine m) {
		super(m);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.channel.adapter.ALevelState#getGroupData
	 * ()
	 */
	@Override
	public List<Map<String, String>> getGroupData() {
		this.groupData = new ArrayList<Map<String, String>>();
		this.groupData.add(getGroupIncome());
		this.groupData.add(getGroupCost());
		return this.groupData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.state.channel.adapter.ALevelState#getChildData
	 * ()
	 */
	@Override
	public List<List<Map<String, String>>> getChildData() {
		this.childData = new ArrayList<List<Map<String, String>>>();
		this.childData.add(getIncomeDetails());
		this.childData.add(getCostDetails());
		return this.childData;
	}
}
