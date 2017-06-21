package com.bonc.mobile.remoteview.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bonc.mobile.common.activity.BaseDataActivity;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.common.view.adapter.SimpleCheckedAdapter;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.common.Constant;
import com.bonc.mobile.remoteview.common.RemoteUtil;

public class AddChannelKpiActivity extends BaseDataActivity {
	ViewSwitcher view_switcher;
	ListView group_list, children_list;
	Button button_back;
	TextView hint, channel_title;
	EditText text_search;

	
	static final String KEY_CHECKED = "checked";
	String channelCode;
	List<String> groupData;
	Map<String, List<Map<String, String>>> childrenData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_channel_kpi);
		initView();
		loadData();
		RemoteUtil.getInstance().addActivity(this);
	}

	@Override
	public void onBackPressed() {
		if (button_back.getVisibility() == View.VISIBLE)
			showGroupPanel();
		else
			super.onBackPressed();
	}

	@Override
	protected void initBaseData() {
		super.initBaseData();
		channelCode = getIntent().getStringExtra(ChannelKpiMgrActivity.CH_CODE);
	}

	@Override
	protected void initView() {
		super.initView();
		TextViewUtils.setText(this, R.id.title, "选择指标");
		view_switcher = (ViewSwitcher) findViewById(R.id.view_switcher);
		group_list = (ListView) findViewById(R.id.group_list);
		children_list = (ListView) findViewById(R.id.children_list);
		button_back = (Button) findViewById(R.id.button_back);
		hint = (TextView) findViewById(R.id.hint);
		text_search = (EditText) findViewById(R.id.text_search);
		text_search.setHint(R.string.search_kpi);
		channel_title = (TextView) findViewById(R.id.channel_title);
	}

	@Override
	protected void loadData() {
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("appType", Constant.APP_TYPE);
		param.put("menuCode", channelCode);
		LoadDataTask t = new LoadDataTask(this);
		t.setRetAsArray(true);
		t.execute("/custmenu/getSelectList", param);
	}

	protected void bindData(JSONArray result) {
		childrenData = new HashMap<String, List<Map<String, String>>>();
		groupData = new ArrayList<String>();
		for (int i = 0; i < result.length(); i++) {
			Map<String, List<Map<String, String>>> m = JsonUtil.toMap2(result
					.optString(i));
			groupData.addAll(m.keySet());
			childrenData.putAll(m);
		}
		for (List<Map<String, String>> data : childrenData.values()) {
			filterKpi(data);
		}
		group_list.setAdapter(new ArrayAdapter<String>(this,
				R.layout.channel_group_item, R.id.text, groupData));
		group_list
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						onGroupItemClick((String) parent.getAdapter().getItem(
								position));
					}
				});
	}

	void filterKpi(List<Map<String, String>> data) {
		String kpiCodes = getIntent().getStringExtra("kpiCodes");
		Iterator<Map<String, String>> it = data.iterator();
		while (it.hasNext()) {
			Map<String, String> m = it.next();
			if (kpiCodes.contains(m.get("KPI_CODE")))
				it.remove();
		}
	}

	void onGroupItemClick(String item) {
		channel_title.setText(item);
		children_list.setAdapter(new ChannelKpiListAdapter(this, childrenData
				.get(item), R.layout.list_item_checked,
				new String[] { "KPI_NAME" }, new int[] { R.id.text },
				children_list));
		showChildrenPanel();
	}

	class ChannelKpiListAdapter extends SimpleCheckedAdapter {
		public ChannelKpiListAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to, ListView listView) {
			super(context, data, resource, from, to, listView);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final Map<String, String> m = (Map<String, String>) getItem(position);
			View v = super.getView(position, convertView, parent);
			CheckBox check_state = (CheckBox) v.findViewById(R.id.check_state);
			check_state
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							children_list.setItemChecked(position, isChecked);
							m.put(KEY_CHECKED, String.valueOf(isChecked));
							hint.setText("已选" + getChosenKpi().size() + "个指标");
						}
					});
			check_state.setChecked(Boolean.parseBoolean(m.get(KEY_CHECKED)));
			return v;
		}

	}

	ArrayList<String> getChosenKpi() {
		ArrayList<String> ret = new ArrayList<String>();
		for (String groupName : childrenData.keySet()) {
			List<Map<String, String>> l = childrenData.get(groupName);
			for (Map<String, String> m : l) {
				if (Boolean.parseBoolean(m.get(KEY_CHECKED))) {
					ret.add(m.get("KPI_CODE"));
				}
			}
		}
		return ret;
	}

	List<Map<String, String>> searchKpi(String s) {
		List<Map<String, String>> ret = new ArrayList<Map<String, String>>();
		for (String groupName : childrenData.keySet()) {
			List<Map<String, String>> l = childrenData.get(groupName);
			for (Map<String, String> m : l) {
				String name = m.get("KPI_NAME");
				if (name.contains(s)) {
					ret.add(m);
				}
			}
		}
		return ret;
	}

	void showGroupPanel() {
		view_switcher.setInAnimation(this, R.anim.switcher_previous_in);
		view_switcher.setOutAnimation(this, R.anim.switcher_previous_out);
		view_switcher.showPrevious();
		button_back.setVisibility(View.INVISIBLE);
	}

	void showChildrenPanel() {
		view_switcher.setInAnimation(this, R.anim.switcher_next_in);
		view_switcher.setOutAnimation(this, R.anim.switcher_next_out);
		view_switcher.showNext();
		button_back.setVisibility(View.VISIBLE);
	}

	void onSearch() {
		String s = text_search.getText().toString();
		channel_title.setText("关键词:" + s);
		children_list.setAdapter(new ChannelKpiListAdapter(this, searchKpi(s),
				R.layout.list_item_checked, new String[] { "KPI_NAME" },
				new int[] { R.id.text }, children_list));
		showChildrenPanel();
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

	void onDone() {
		ArrayList<String> l = getChosenKpi();
		if (l.size() > 0) {
			Intent data = new Intent();
			data.putStringArrayListExtra("data", getChosenKpi());
			setResult(RESULT_OK, data);
		}
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_cancel:
			finish();
			break;
		case R.id.button_back:
			showGroupPanel();
			break;
		case R.id.button_done:
			onDone();
			break;
		case R.id.button_search:
			onSearch();
			break;
		}
	}

}
