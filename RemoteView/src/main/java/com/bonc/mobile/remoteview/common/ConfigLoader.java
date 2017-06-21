package com.bonc.mobile.remoteview.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.activity.ChannelMgrActivity;
import com.bonc.mobile.remoteview.activity.HolidayMaintainActivity;
import com.bonc.mobile.remoteview.activity.KpiWarnActivity;
import com.bonc.mobile.remoteview.activity.MenuListActivity;
import com.bonc.mobile.remoteview.activity.OrderSettingActivity;
import com.bonc.mobile.remoteview.activity.RVAnnounceListActivity;
import com.bonc.mobile.remoteview.activity.RVKpiPortalActivity;
import com.bonc.mobile.remoteview.activity.RVReportListActivity;
import com.bonc.mobile.remoteview.activity.RVStationReportActivity;
import com.bonc.mobile.remoteview.activity.WarnRuleMainActivity;

public class ConfigLoader extends BaseConfigLoader {
	static ConfigLoader inst;

	public static ConfigLoader getInstance(Context context) {
		if (inst == null) {
			inst = new ConfigLoader(context.getApplicationContext());
		}
		return inst;
	}

	ConfigLoader(Context context) {
		super(context);
		initTypeMap();
		initIconMap();
	}

	public void loadChannel(List<Map<String, String>> list) {
		DataUtil.deleteRows(getMenuInfo(), KEY_MENU_TYPE, MENU_TYPE_KPI_CUST);
		for (Map<String, String> m : list) {
			m.put(KEY_MENU_TYPE, MENU_TYPE_KPI_CUST);
			m.put(KEY_MENU_PARENT_CODE, "12000001");
			m.put(KEY_MENU_ICON, "10099");
		}
	
		getMenuInfo().addAll(list);
		save();
		decodeDate(list);
	}

	public void loadChannel(String data) {
		loadChannel(JsonUtil.toList(data));
	}

	public List<Map<String, String>> getChannel() {
		return DataUtil.extractList(getMenuInfo(), KEY_MENU_TYPE,
				MENU_TYPE_KPI_CUST);
	}

	void initIconMap() {
		iconMap = new HashMap<String, Integer>();
		iconMap.put("10000", R.mipmap.icon_menu_000);
		iconMap.put("10001", R.mipmap.icon_menu_001);
		iconMap.put("10002", R.mipmap.icon_menu_002);
		iconMap.put("10003", R.mipmap.icon_menu_003);
		iconMap.put("10004", R.mipmap.icon_menu_004);
		iconMap.put("10005", R.mipmap.icon_menu_005);
		iconMap.put("10006", R.mipmap.icon_menu_006);
		iconMap.put("10007", R.mipmap.icon_menu_007);
		iconMap.put("10008", R.mipmap.icon_menu_008);
		iconMap.put("10009", R.mipmap.icon_menu_009);
		iconMap.put("10010", R.mipmap.icon_menu_010);
		iconMap.put("10011", R.mipmap.icon_menu_011);
		iconMap.put("10012", R.mipmap.icon_menu_012);
		iconMap.put("10013", R.mipmap.icon_menu_013);
		iconMap.put("10014", R.mipmap.icon_menu_014);
		iconMap.put("10015", R.mipmap.icon_menu_015);
		iconMap.put("10016", R.mipmap.icon_menu_016);
		iconMap.put("10017", R.mipmap.icon_menu_017);
		iconMap.put("10018", R.mipmap.icon_menu_018);
		iconMap.put("10019", R.mipmap.icon_menu_019);
		iconMap.put("10020", R.mipmap.icon_menu_020);
		iconMap.put("10021", R.mipmap.icon_menu_021);
		iconMap.put("10022", R.mipmap.icon_menu_022);
		iconMap.put("10023", R.mipmap.icon_menu_023);
		iconMap.put("10024", R.mipmap.icon_menu_024);
		iconMap.put("10025", R.mipmap.icon_menu_025);
		iconMap.put("10026", R.mipmap.icon_menu_026);
		iconMap.put("10027", R.mipmap.icon_menu_027);
		iconMap.put("10028", R.mipmap.icon_menu_028);
		iconMap.put("10029", R.mipmap.icon_menu_029);
		iconMap.put("10030", R.mipmap.icon_menu_030);
		iconMap.put("10031", R.mipmap.icon_menu_031);
		iconMap.put("10032", R.mipmap.icon_menu_032);
		iconMap.put("10033", R.mipmap.icon_menu_033);
		iconMap.put("10034", R.mipmap.icon_menu_034);
		iconMap.put("10035", R.mipmap.icon_menu_035);
		iconMap.put("10036", R.mipmap.icon_menu_036);
		iconMap.put("10037", R.mipmap.icon_menu_037);
		iconMap.put("10038", R.mipmap.icon_menu_038);
		iconMap.put("10039", R.mipmap.icon_menu_039);
		iconMap.put("10040", R.mipmap.icon_menu_040);
		iconMap.put("10041", R.mipmap.icon_menu_041);
		iconMap.put("10042", R.mipmap.icon_menu_042);
		iconMap.put("10043", R.mipmap.icon_menu_043);
		iconMap.put("10044", R.mipmap.icon_menu_044);
		iconMap.put("10045", R.mipmap.icon_menu_045);
		iconMap.put("10046", R.mipmap.icon_menu_046);
		iconMap.put("10047", R.mipmap.icon_menu_047);
		iconMap.put("10048", R.mipmap.icon_menu_048);
		iconMap.put("10049", R.mipmap.icon_menu_049);
		iconMap.put("10050", R.mipmap.icon_menu_050);
		iconMap.put("10051", R.mipmap.icon_menu_051);
		iconMap.put("10052", R.mipmap.icon_menu_052);
		iconMap.put("10053", R.mipmap.icon_menu_053);
		iconMap.put("10054", R.mipmap.icon_menu_054);
		iconMap.put("10090", R.mipmap.icon_menu_090);
		iconMap.put("10091", R.mipmap.icon_menu_091);
		iconMap.put("10092", R.mipmap.icon_menu_092);
		iconMap.put("10099", R.mipmap.icon_menu_099);
	}

	void initTypeMap() {
		typeMap = new HashMap<String, Class>();
		typeMap.put(MENU_TYPE_DEF, MenuListActivity.class);
		typeMap.put("1", MenuListActivity.class);
		typeMap.put("10", RVKpiPortalActivity.class);
		typeMap.put("13", KpiWarnActivity.class);
		typeMap.put("14", RVReportListActivity.class);
		typeMap.put("15", RVStationReportActivity.class);
		typeMap.put("16", HolidayMaintainActivity.class);
		typeMap.put("20", RVKpiPortalActivity.class);
		typeMap.put("21", ChannelMgrActivity.class);
		typeMap.put("22", WarnRuleMainActivity.class);
		typeMap.put("23", OrderSettingActivity.class);
		typeMap.put("24", RVAnnounceListActivity.class);
	}
}
