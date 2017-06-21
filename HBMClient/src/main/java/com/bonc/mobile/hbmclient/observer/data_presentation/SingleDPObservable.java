/**
 * 
 */
package com.bonc.mobile.hbmclient.observer.data_presentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.HttpUtil;
import com.bonc.mobile.hbmclient.util.JsonUtil;

/**
 * @author liweigao
 *
 */
public class SingleDPObservable extends TemplateDataPresentationObservable {
	private final String DEFAULT_GROUP_NAME = "简报数据";
	private String label = DEFAULT_GROUP_NAME;
	private List<String> childList;

	private BusinessDao dao = new BusinessDao();
	private String menuCode;

	private final String KEY_DP_TIME = "preftime";
	private final String KEY_MENU_CODE = "menucode";
	private final String KEY_AREA_CODE = "areaCode";
	private final String KEY_GROUP_INFO = "groupInfo";
	private final String KEY_DATAS = "datas";
	private final String KEY_NAME = "name";

	public void setMenuCode(String code) {
		this.menuCode = code;
		executeTask();
	}

	@Override
	public void setChanged() {
		// TODO Auto-generated method stub
		this.label = DEFAULT_GROUP_NAME;
		this.childList = new ArrayList<String>();

		Map<String, String> map = new HashMap<String, String>();
		map.put(KEY_MENU_CODE, this.menuCode);
		String areaId = dao.getUserInfo().get("areaId");
		map.put(KEY_AREA_CODE, areaId);
		String result = HttpUtil.sendRequest(
				ActionConstant.GET_SINGLE_DATA_PRESENTATION_DATA, map);
		try {
			JSONArray ja_result = new JSONArray(result);
			for (int i = 0; i < ja_result.length(); i++) {
				JSONObject jo_item = ja_result.getJSONObject(i);
				String time = jo_item.getString(KEY_DP_TIME);
				String groupName = jo_item.getJSONObject(KEY_GROUP_INFO)
						.getString(KEY_NAME);
				time = DateUtil.oneStringToAntherString(time,
						DateRangeEnum.DAY.getDateServerPattern(),
						DateRangeEnum.DAY.getPatternChina());
				this.label = time + "的" + groupName;
				this.childList = JsonUtil.toReportList(jo_item
						.getJSONArray(KEY_DATAS));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dataException();
			this.label = DEFAULT_GROUP_NAME;
			this.childList.clear();
		}
	}

	public String getTitle() {
		return this.label;
	}

	public List<String> getChildData() {
		return this.childList;
	}
}
