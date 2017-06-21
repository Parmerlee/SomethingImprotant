package com.bonc.mobile.portal;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.SimpleAdapter;

import com.bonc.anhuimobile.ac.R;
import com.bonc.mobile.common.activity.BaseListDataActivity;
import com.bonc.mobile.common.data.DatabaseManager;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.DateUtil;
import com.bonc.mobile.common.util.NumberUtil;

public class MPAnnounceListActivity extends BaseListDataActivity {
	DatabaseManager dbMan;
	String filter;
	int limit = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_announce_list_2);
		initView();
		dbMan = new DatabaseManager(this);
		loadData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbMan.close();
	}

	@Override
	protected void initBaseData() {
		super.initBaseData();
		filter = getIntent().getStringExtra("filter");
	}

	@Override
	protected void loadData() {
		DataUtil.saveSetting(
				PreferenceManager.getDefaultSharedPreferences(this), "a_read",
				(int) dbMan.queryNumEntries("mp_announce"));
		String sql = "select * from mp_announce where TYPE in (" + filter
				+ ") limit " + limit;
		List<Map<String, String>> data = dbMan.queryForList(sql, null);
		for (Map<String, String> m : data) {
			m.put("NOTICE_PUBLISH_DATE", DateUtil.oneStringToAntherString(
					m.get("NOTICE_PUBLISH_DATE"), DateUtil.PATTERN_8,
					"yyyy年MM月dd日"));
			m.put("TYPE", "RVS".equals(m.get("TYPE")) ? "网络千里眼" : "掌上分析");
		}
		mList.setAdapter(new SimpleAdapter(this, data,
				R.layout.announce_list_item_2, new String[] {
						"NOTICE_PUBLISH_DATE", "TYPE", "MSG" }, new int[] {
						R.id.data1, R.id.data2, R.id.data3 }));
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.button_more) {
			String sql = "select count(*) as cnt from mp_announce where TYPE in ("
					+ filter + ")";
			Map<String, String> m = dbMan.queryForMap(sql, null);
			if (limit >= NumberUtil.changeToInt(m.get("cnt")))
				showToast("无更多数据");
			else
				limit += 5;
			loadData();
		}
	}

}
