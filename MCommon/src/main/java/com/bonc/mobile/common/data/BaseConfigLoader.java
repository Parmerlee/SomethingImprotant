package com.bonc.mobile.common.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.R;
import com.bonc.mobile.common.util.Base64;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.FileUtils;
import com.bonc.mobile.common.util.JsonUtil;

public abstract class BaseConfigLoader {
	public static final String KEY_MENU_CODE = "MENU_CODE";
	public static final String KEY_MENU_NAME = "MENU_NAME";
	public static final String KEY_MENU_TYPE = "MENU_TYPE";
	public static final String KEY_MENU_ICON = "IMG_ID";
	public static final String KEY_MENU_LEVEL = "MENU_LEVEL";
	public static final String KEY_MENU_PARENT_CODE = "PARENT_MENU_CODE";
	public static final String KEY_DATE_TYPE = "DATE_TYPE";
	public static final String KEY_QUERY_CODE = "queryCode";
	public static final String KEY_FLOW_ID = "FLOW_ID";

	public static final String MENU_TYPE_DEF = "-1";
	public static final String MENU_TYPE_KPI = "10";
	public static final String MENU_TYPE_KPI_CUST = "20";

	protected Context context;
	protected List<Map<String, String>> menuList, announceList;
	protected Map<String, Class> typeMap;
	protected Map<String, Integer> iconMap;
	protected Map<String, String> announceStatus;

	protected BaseConfigLoader(Context context) {
		this.context = context;
	}

	@SuppressWarnings("unchecked")
	protected void load() {
		if (context == null)
			return;
		menuList = decodeDate((List<Map<String, String>>) FileUtils.loadObject(
				context, "menuList"));
		if (!AppConstant.SEC_ENH)
			System.out.println("menuList:" + menuList.toString());
	}

	protected void save() {
		if (context == null)
			return;
		FileUtils.saveObject(context, "menuList", encodeDate(menuList));
		load();
		// System.out.println(menuList.toString());
	}

	String[] targetStr = { "MENU_NAME", "MENU_EXPLAIN", "MENU_DESC" };

	private List<Map<String, String>> encodeDate(
			List<Map<String, String>> menuList) {
		for (int i = 0; i < menuList.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map = menuList.get(i);
			for (int j = 0; j < targetStr.length; j++) {
				map.put(targetStr[j], encodeString(map, targetStr[j]));
			}
		}
		return menuList;

	}

	protected List<Map<String, String>> decodeDate(
			List<Map<String, String>> menuList) {
		List<Map<String, String>> menulist = menuList;

		for (int i = 0; i < menuList.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map = menuList.get(i);
			for (int j = 0; j < targetStr.length; j++) {
				map.put(targetStr[j], decodeString(map, targetStr[j]));
			}
		}
		return menulist;
	}

	private String encodeString(Map<String, String> map, String string) {
		if (!map.containsKey(string)) {
			return null;
		}
		String str = map.get(string);
		if (str == null)
			return null;
		else
			return Base64.encode(map.get(string).getBytes());
	}

	private String decodeString(Map<String, String> map, String string) {
		if (!map.containsKey(string)) {
			return null;
		}
		String str = map.get(string);

		if (str == null)
			return null;
		else
			return new String(Base64.decode(map.get(string)));
	}

	public void loadMenu(String data) {
		menuList = JsonUtil.toList(data);
		save();
	}

	public void loadMenu(List<Map<String, String>> list) {
		menuList = list;
		save();
	}

	public List<Map<String, String>> getMenuInfo() {
		if (menuList == null)
			load();
		return menuList;
	}

	public List<Map<String, String>> getFirstMenu() {
		return getSecondMenu("0");
	}

	public List<Map<String, String>> getSecondMenu(String menuCode) {
		if (!AppConstant.SEC_ENH)
			System.out.println("menu:"
					+ DataUtil.extractList(getMenuInfo(), KEY_MENU_PARENT_CODE,
							menuCode));
		return DataUtil.extractList(getMenuInfo(), KEY_MENU_PARENT_CODE,
				menuCode);
	}

	public List<Map<String, String>> getMenuByType(String type) {
		return DataUtil.extractList(getMenuInfo(), KEY_MENU_TYPE, type);
	}

	public Map<String, String> getMenuInfo(String menuCode) {
		return DataUtil.extractRow(getMenuInfo(), KEY_MENU_CODE, menuCode);
	}

	/*** 获取MenuInfo中的某个menuCode的key对应的value ***/
	public String getMenuAttr(String menuCode, String key) {
		Map<String, String> info = getMenuInfo(menuCode);
		return info == null ? null : info.get(key);
	}

	public boolean isMenuType(String menuCode, String type) {
		return type.equals(getMenuAttr(menuCode, KEY_MENU_TYPE));
	}

	public String getMenuName(String menuCode) {
		return getMenuAttr(menuCode, KEY_MENU_NAME);
	}

	public Intent getMenuIntent(Context context, String type) {
		if (typeMap.containsKey(type))
			return new Intent(context, typeMap.get(type));
		else if (typeMap.containsKey(MENU_TYPE_DEF))
			return new Intent(context, typeMap.get(MENU_TYPE_DEF));
		else
			throw new IllegalArgumentException();
	}

	public int getMenuIcon(String iconId) {
		if (iconMap.containsKey(iconId))
			return iconMap.get(iconId);
		else
			return R.mipmap.ic_default;
	}

	public Drawable getMenuIconDrawable(Context context, String iconId) {
		return context.getResources().getDrawable(getMenuIcon(iconId));
	}

	public void loadAnnounce(String data) {
		announceList = JsonUtil.toList(data);
	}

	public List<Map<String, String>> getAnnounce() {
		return announceList;
	}

	public Map<String, String> loadAnnounceStatus(DatabaseManager dbm) {
		announceStatus = new HashMap<String, String>();
		Cursor cursor = dbm.query("select * from announce_status", null);
		if (cursor.moveToFirst()) {
			do {
				announceStatus.put(
						cursor.getString(cursor.getColumnIndex("flow_id")),
						cursor.getString(cursor.getColumnIndex("status")));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return announceStatus;
	}

	public void updateAnnounceStatus(DatabaseManager dbm, String flow_id) {
		ContentValues values = new ContentValues();
		values.put("flow_id", flow_id);
		values.put("status", "1");
		dbm.replace("announce_status", values);
	}
}
