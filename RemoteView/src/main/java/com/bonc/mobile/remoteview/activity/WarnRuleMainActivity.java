package com.bonc.mobile.remoteview.activity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.common.Constant;
import com.bonc.mobile.remoteview.common.RemoteUtil;

public class WarnRuleMainActivity extends WarnRuleBaseActivity {
	EditText text_search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rule_main);
		initView();
		loadData();
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
		text_search = (EditText) findViewById(R.id.text_search);
		text_search.setHint(R.string.search_kpi);
	}

	@Override
	protected void loadData() {
		super.loadData();
		Map<String, String> params = new LinkedHashMap<String, String>();
		LoadDataTask t = new LoadDataTask(this);
		t.setRetAsArray(true);
		t.execute(Constant.Path_LoadKpiRuleFirst, params);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Map<String, String> m = (Map<String, String>) mList.getAdapter()
				.getItem(position);
		showDetail(m.get("KPI_CODE"));
	}

	void showDetail(String kpiCode) {
		Intent intent = new Intent(this, WarnRuleDetailActivity.class);
		intent.putExtra(BaseConfigLoader.KEY_MENU_CODE, menuCode);
		intent.putExtra("KPI_CODE", kpiCode);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.button_search) {
			loadKpiNames(text_search.getText().toString());
		}
	}

	void loadKpiNames(String text) {
		if (text.length() == 0)
			return;
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("term", text);
		LoadKpiNameTask t = new LoadKpiNameTask(this);
		t.setRetAsArray(true);
		t.execute(Constant.Path_LoadKpiName, params);
	}

	class LoadKpiNameTask extends HttpRequestTask {
		public LoadKpiNameTask(Context context) {
			super(context);
		}

		@Override
		protected void handleResult(JSONArray result) {
			final List<Map<String, String>> data = JsonUtil.toList(result);
			if (data.size() == 0) {
				showToast("未搜索到相关的指标!");
				return;
			}
			String[] items = DataUtil.extractList(data, "KPI_NAME").toArray(
					new String[0]);
			new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT)
					.setTitle("请点击要查看的指标:")
					.setItems(items, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Map<String, String> m = data.get(which);
							showDetail(m.get("KPI_CODE"));
						}
					}).setNegativeButton("取消", null).show();
		}
	}
}
