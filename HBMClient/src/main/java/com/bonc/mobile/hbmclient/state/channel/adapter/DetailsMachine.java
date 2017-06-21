/**
 * DetailsMachine
 */
package com.bonc.mobile.hbmclient.state.channel.adapter;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

/**
 * @author liweigao
 * 
 */
public class DetailsMachine {
	private ALevelState state, hightState, lowState;

	public DetailsMachine(JSONObject jo, String areaLevel) {
		this.hightState = new HightLevelState(this);
		this.lowState = new LowLevelState(this);
		if ("4".equals(areaLevel)) {
			this.state = lowState;
		} else {
			this.state = hightState;
		}

		this.state.setData(jo);
	}

	/**
	 * @return the hightState
	 */
	public ALevelState getHightState() {
		return hightState;
	}

	/**
	 * @return the lowState
	 */
	public ALevelState getLowState() {
		return lowState;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(ALevelState state) {
		this.state = state;
	}

	/**
	 * @return
	 * @see ALevelState#getGroupData()
	 */
	public List<Map<String, String>> getGroupData() {
		return state.getGroupData();
	}

	/**
	 * @return
	 * @see ALevelState#getGroupLayout()
	 */
	public int getGroupLayout() {
		return state.getGroupLayout();
	}

	/**
	 * @return
	 * @see ALevelState#getGroupFrom()
	 */
	public String[] getGroupFrom() {
		return state.getGroupFrom();
	}

	/**
	 * @return
	 * @see ALevelState#getGroupTo()
	 */
	public int[] getGroupTo() {
		return state.getGroupTo();
	}

	/**
	 * @return
	 * @see ALevelState#getChildData()
	 */
	public List<List<Map<String, String>>> getChildData() {
		return state.getChildData();
	}

	/**
	 * @return
	 * @see ALevelState#getChildLayout()
	 */
	public int getChildLayout() {
		return state.getChildLayout();
	}

	/**
	 * @return
	 * @see ALevelState#getChildFrom()
	 */
	public String[] getChildFrom() {
		return state.getChildFrom();
	}

	/**
	 * @return
	 * @see ALevelState#getChildTo()
	 */
	public int[] getChildTo() {
		return state.getChildTo();
	}

}
