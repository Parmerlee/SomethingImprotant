package com.bonc.mobile.remoteview.activity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bonc.mobile.common.activity.BaseListDataActivity;
import com.bonc.mobile.common.kpi.KpiConstant;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.StringUtil;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.common.ConfigLoader;
import com.bonc.mobile.remoteview.common.Constant;
import com.bonc.mobile.remoteview.common.RemoteUtil;

public class WarnRuleBaseActivity extends BaseListDataActivity {
	List<Map<String, String>> mData;

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
	protected void initView() {
		super.initView();
		TextViewUtils.setText(this, R.id.title, ConfigLoader.getInstance(this)
				.getMenuName(menuCode));
	}

	@Override
	protected void bindData(JSONArray result) {
		List<Map<String, String>> data = JsonUtil.toList(result);
		mData = data;
		for (Map<String, String> m : data) {
			m.put("ruleDesc", KpiConstant.getColTitle(m.get("ALARM_COLUMN"))
					+ m.get("ALARM_CONDITION") + m.get("ALARM_PERCENT"));
		}
		mList.setAdapter(new WarnRuleAdapter(this, data,
				R.layout.rule_detail_list_item, new String[] { "KPI_NAME",
						"AREA_NAME", "ruleDesc" }, new int[] { R.id.kpiName,
						R.id.ruleArea, R.id.ruleDesc }));
	}

	class WarnRuleAdapter extends SimpleAdapter {
		public WarnRuleAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			ToggleButton tb = (ToggleButton) view
					.findViewById(R.id.ruleSwitcher);
			final Map<String, String> item = (Map<String, String>) getItem(position);
			tb.setChecked("1".equalsIgnoreCase(item.get("IS_ALARM")));
			final String flowId = item.get("FLOW_ID");
			tb.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					modAlarmStatus(flowId, item.get("IS_ALARM"));
				}
			});
			return view;
		}
	}

	void modAlarmStatus(String flowId, String status) {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("flowId", flowId);
		params.put("status", status);
		HttpRequestTask task = new UpdateRuleStatusTask(this);
		task.execute(Constant.Path_ModifyKpiRule, params);
	}

	void modAllAlarmStatus(String status) {
		String ids = StringUtil.join(DataUtil.extractList(mData, "FLOW_ID"));
		modAlarmStatus(ids, status);
	}

	class UpdateRuleStatusTask extends HttpRequestTask {

		public UpdateRuleStatusTask(Context context) {
			super(context);
		}

		@Override
		protected void handleResult(JSONObject result) {
			String msg = JsonUtil.optString(result, "msg");
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
			loadData();
		}
	}
}
