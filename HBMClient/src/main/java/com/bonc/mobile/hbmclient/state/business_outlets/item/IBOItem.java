/**
 * IHightItem
 */
package com.bonc.mobile.hbmclient.state.business_outlets.item;

import org.json.JSONObject;

import com.bonc.mobile.hbmclient.command.business_outlets.AreaSelector;
import com.bonc.mobile.hbmclient.command.business_outlets.DateSelector;

/**
 * @author liweigao
 *
 */
public interface IBOItem {
	String KEY_DATE = "date";
	String KEY_USER_CODE = "userCode";
	String KEY_AREA_CODE = "areaCode";

	void addView();

	void updateView(JSONObject jo);

	void setArea(AreaSelector as);

	void setDate(DateSelector ds);

	void changeState();
}
