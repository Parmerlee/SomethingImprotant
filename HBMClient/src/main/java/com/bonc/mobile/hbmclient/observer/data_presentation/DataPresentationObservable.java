/**
 * 
 */
package com.bonc.mobile.hbmclient.observer.data_presentation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bonc.mobile.common.AppConstant;
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
public class DataPresentationObservable extends
		TemplateDataPresentationObservable {
	private Calendar calendar;
	private String showTime;
	private BusinessDao dao = new BusinessDao();

	private List<String> groupList;
	private List<List<String>> childList;
	private String menuCode;

	private final String KEY_OPTIME = "optime";
	private final String KEY_MENU_CODE = "menucode";
	private final String KEY_DATA_TYPE = "dataType";
	private final String KEY_AREA_CODE = "areaCode";
	private final String KEY_GROUP_INFO = "groupInfo";
	private final String KEY_DATAS = "datas";
	private final String KEY_NAME = "name";
	private final String KEY_DATE = "date";
	private final String KEY_ALL_DATA = "allData";
	public static final String DEFAULT_DATE = "1970/01/01";

	public DataPresentationObservable(String menuCode) {
		this.menuCode = menuCode;
	}

	/*
	 * public void setDate(Calendar c) { if(calendar != null) { int difference =
	 * calendar.compareTo(c); if(difference == 0) {
	 * 
	 * } else { this.calendar = c; dateChanged(); } } else { this.calendar = c;
	 * dateChanged(); } }
	 */

	public void setDate(Calendar c) {
		this.calendar = c;
		dateChanged();
	}

	@Override
	public void setChanged() {
		// TODO Auto-generated method stub
		this.groupList = new ArrayList<String>();
		this.childList = new ArrayList<List<String>>();

		String optime = null;
		if (this.calendar != null) {
			optime = DateUtil.formatter(calendar.getTime(),
					DateRangeEnum.DAY.getDateServerPattern());
		} else {

		}

		Map<String, String> map = new HashMap<String, String>();
		map.put(KEY_MENU_CODE, null);
		map.put(KEY_OPTIME, optime);
		map.put(KEY_DATA_TYPE, DateRangeEnum.DAY.getDateFlag());
		String areaId = dao.getUserInfo().get("areaId");
		map.put(KEY_AREA_CODE, areaId);
		map.put("menuCode", this.menuCode);
		String result = HttpUtil.sendRequest(
				ActionConstant.GET_DATA_PRESENTATION_DATA, map);
		try {
			JSONObject jo_result = new JSONObject(result);
			if (this.calendar != null) {
				this.showTime = DateUtil.formatter(this.calendar.getTime(),
						DateRangeEnum.DAY.getDateShowPattern());
			} else {
				if (jo_result.isNull(KEY_DATE)
						|| "".equals(jo_result.getString(KEY_DATE))) {
					this.showTime = DEFAULT_DATE;
				} else {
					this.showTime = jo_result.getString(KEY_DATE);
					this.showTime = DateUtil.oneStringToAntherString(
							this.showTime,
							DateRangeEnum.DAY.getDateServerPattern(),
							DateRangeEnum.DAY.getDateShowPattern());
				}
				/*
				 * this.showTime = jo_result.getString(KEY_DATE);
				 * if(this.showTime == null ||
				 * "null".equalsIgnoreCase(this.showTime)) { this.showTime =
				 * DEFAULT_DATE; } else { this.showTime =
				 * DateUtil.oneStringToAntherString(this.showTime,
				 * DateRangeEnum.DAY.getDateServerPattern(),
				 * DateRangeEnum.DAY.getDateShowPattern()); }
				 */
			}
			JSONArray ja_result = jo_result.getJSONArray(KEY_ALL_DATA);
			for (int i = 0; i < ja_result.length(); i++) {
				JSONObject jo_item = ja_result.getJSONObject(i);
				this.groupList.add(jo_item.getJSONObject(KEY_GROUP_INFO)
						.getString(KEY_NAME));
				List<String> item_child = JsonUtil.toReportList(jo_item
						.getJSONArray(KEY_DATAS));
				this.childList.add(item_child);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dataException();
			this.groupList.clear();
			this.childList.clear();
		}
	}

	public String getShowTime() {
		return this.showTime;
	}

	public List<String> getGroupList() {
		return this.groupList;
	}

	public List<List<String>> getChildList() {
		return this.childList;
	}

	private void dateChanged() {
		executeTask();
	}
}
