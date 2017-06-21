package com.bonc.mobile.remoteview.activity;

import org.json.JSONObject;

import android.os.Bundle;

import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.kpi.SimpleKpiDataModel;
import com.bonc.mobile.common.kpi.TableReportActivity;
import com.bonc.mobile.common.util.DateUtil;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.remoteview.common.ConfigLoader;
import com.bonc.mobile.remoteview.common.RemoteUtil;

public abstract class RVStationReportBaseActivity extends TableReportActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		RemoteUtil.getInstance().addActivity(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (MyUtils.doInfilter(this)) {

			if (!MyUtils.isBackground(this)) {
				MyUtils.startProtal(this);
				RemoteUtil.getInstance().callLogin();
			}
		}
	}

	@Override
	protected BaseConfigLoader getConfigLoader() {
		return ConfigLoader.getInstance(this);
	}

	@Override
	protected SimpleKpiDataModel buildDataModel(JSONObject result) {
		SimpleKpiDataModel model = new SimpleKpiDataModel();
		model.build(result, "columnsDatas", "reportDatas");
		sort(model);
		return model;
	}

	@Override
	protected void renderButtons(JSONObject result) {
		if (!result.isNull("dataTime"))
			date_button.setDate(DateUtil.getDate(result.optString("dataTime"),
					DateUtil.PATTERN_8));
		area_button.setData(
				JsonUtil.toList(JsonUtil.optJSONArray(result, "areas")),
				"AREA_NAME", "AREA_CODE");
		area_button.setChoice(JsonUtil.optString(result, "areaCode"));
	}

	void sort(SimpleKpiDataModel model) {
		String key = null;
		boolean asc = false;
		if ("12000085".equals(menuCode)) {
			key = "EXIT_TOTAL";
		} else if ("12000086".equals(menuCode)) {
			key = "EXIT_MAX";
		} else if ("12000087".equals(menuCode)) {
			key = "EXIT_TIME";
		} else if ("12000088".equals(menuCode)) {
			key = "ALARM_TOTAL";
		} else if ("12000089".equals(menuCode)) {
			key = "CONTINUE_TIME";
		} else {
			key = "CONTINUE_TIME";
			asc = true;
		}
		if (key != null)
			model.sort(key, asc);
	}
}
