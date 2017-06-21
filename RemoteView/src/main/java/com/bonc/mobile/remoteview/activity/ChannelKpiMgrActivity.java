package com.bonc.mobile.remoteview.activity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ToggleButton;

import com.bonc.mobile.common.activity.BaseListDataActivity;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.StringUtil;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.common.view.adapter.SimpleCheckedAdapter;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.common.Constant;
import com.bonc.mobile.remoteview.common.RemoteUtil;
import com.mobeta.android.dslv.DragSortListView;

/****
 * 有Channel的Activity都是自定义频道相关的
 * 
 * @author Lenevo
 *
 */
public class ChannelKpiMgrActivity extends BaseListDataActivity {
	public static final String CH_CODE = "channelCode";
	static final String KEY_FLOW_ID = "FLOW_ID";
	List<Map<String, String>> mData;
	String channelCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channel_kpi_mgr);
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
				RemoteUtil.getInstance().exit();
			}
		}
	}

	@Override
	protected void initBaseData() {
		super.initBaseData();
		channelCode = getIntent().getStringExtra(CH_CODE);
	}

	@Override
	protected void initView() {
		super.initView();
		TextViewUtils.setText(this, R.id.title, "频道指标管理");
		((DragSortListView) mList)
				.setDropListener(new DragSortListView.DropListener() {
					@Override
					public void drop(int from, int to) {
						// Toast.makeText(getApplicationContext(), "AAAA",
						// 1).show();
						onDragDrop(from, to);
					}
				});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.select_all:
			selectKpi();
			break;
		case R.id.add:
			addKpi();
			break;
		case R.id.delete:
			deleteKpi();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Map<String, String> param = new LinkedHashMap<String, String>();
			param.put("appType", Constant.APP_TYPE);
			param.put("menuCode", channelCode);
			param.put("kpiCodes",
					StringUtil.join(data.getStringArrayListExtra("data")));
			new ChannelKpiActionTask(this).execute("/custmenu/saveKpi", param);
		}
	}

	@Override
	protected void loadData() {
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("appType", Constant.APP_TYPE);
		param.put("menuCode", channelCode);
		LoadDataTask t = new LoadDataTask(this);
		t.setRetAsArray(true);
		t.execute("/custmenu/queryKpiList", param);
	}

	@Override
	protected void bindData(JSONArray result) {
		List<Map<String, String>> data = JsonUtil.toList(result);
		mData = data;
		mList.setAdapter(new SimpleCheckedAdapter(this, data,
				R.layout.ch_kpi_list_item, new String[] { "KPI_NAME" },
				new int[] { R.id.text }, mList));
	}

	void selectKpi() {
		ToggleButton select_all = (ToggleButton) findViewById(R.id.select_all);
		UIUtil.selectItems(mList, select_all.isChecked());
	}

	void addKpi() {
		Intent intent = new Intent(this, AddChannelKpiActivity.class);
		intent.putExtra(CH_CODE, channelCode);
		intent.putExtra("kpiCodes",
				StringUtil.join(DataUtil.extractList(mData, "KPI_CODE")));
		startActivityForResult(intent, 100);
	}

	void deleteKpi() {
		if (mList.getCheckedItemCount() == 0) {
			showToast("请选择要删除的指标");
			return;
		}
		UIUtil.showAlertDialog(this, "确定删除选择的指标?",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						doDelete(StringUtil.join(DataUtil.extractList(mData,
								KEY_FLOW_ID, mList.getCheckedItemIds())));
					}
				}, null);
	}

	void doDelete(String flowIds) {
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("appType", Constant.APP_TYPE);
		param.put("flowIds", flowIds);
		new ChannelKpiActionTask(this).execute("/custmenu/deleteKpi", param);
	}

	void doSort(String flowIds) {
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("appType", Constant.APP_TYPE);
		param.put("flowIds", flowIds);
		ChannelKpiActionTask t = new ChannelKpiActionTask(this);
		t.refresh = false;
		t.execute("/custmenu/sortKpiOrder", param);
	}

	class ChannelKpiActionTask extends HttpRequestTask {
		boolean refresh = true;

		public ChannelKpiActionTask(Context context) {
			super(context);
		}

		@Override
		protected void handleResult(JSONObject result) {
			boolean flag = result.optBoolean("flag");
			showToast(result.optString("msg"));
			if (flag && refresh) {
				loadData();
			}
		}
	}

	void onDragDrop(int from, int to) {
		if (from != to) {
			Map<String, String> item = mData.get(from);
			mData.remove(item);
			mData.add(to, item);
			((BaseAdapter) mList.getAdapter()).notifyDataSetChanged();
			((DragSortListView) mList).moveCheckState(from, to);
			doSort(StringUtil.join(DataUtil.extractList(mData, KEY_FLOW_ID)));
		}
	}
}
