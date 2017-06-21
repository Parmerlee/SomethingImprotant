package com.bonc.mobile.remoteview.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bonc.mobile.common.activity.BaseDataActivity;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.kpi.ColumnDataFilter;
import com.bonc.mobile.common.kpi.ColumnDataFilter.ColumnDisplyInfo;
import com.bonc.mobile.common.kpi.KpiConstant;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.util.DateUtil;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.common.view.DataChooseButton;
import com.bonc.mobile.common.view.DataChooseButton.OnDataChooseListener;
import com.bonc.mobile.common.view.DatePickerButton;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.common.ConfigLoader;
import com.bonc.mobile.remoteview.common.Constant;
import com.bonc.mobile.remoteview.common.RemoteUtil;

public class KpiWarnActivity extends BaseDataActivity {

	private String optime;
	private String currentCode;
	private List<Map<String, String>> warns = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> areas = new ArrayList<Map<String, String>>();

	private DataChooseButton area_button;
	protected DatePickerButton date_button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kpi_warn);
		this.initView();
		this.loadKpiWarns(optime, currentCode);
		RemoteUtil.getInstance().addActivity(this);
	}

	@Override
	protected void onResume() {
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
		Bundle extras = this.getIntent().getExtras();
		String menuCode = (String) extras.get(BaseConfigLoader.KEY_MENU_CODE);
		String menuName = ConfigLoader.getInstance(this).getMenuName(menuCode);
		TextView view = (TextView) this.findViewById(R.id.title);
		view.setText(menuName);
		// 日期按钮
		date_button = (DatePickerButton) findViewById(R.id.date_button);
		date_button.setPattern(DateUtil.PATTERN_MODEL2_10);
		if (date_button != null) {
			date_button.setOnDateSetListener(new OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					optime = DateUtil.formatter(date_button.getDate(),
							DateUtil.PATTERN_8);
					loadKpiWarns(optime, currentCode);
				}
			});
		}
		// 地区按钮
		area_button = (DataChooseButton) findViewById(R.id.area_button);
		area_button.setOnDataChooseListener(new OnDataChooseListener() {
			@Override
			public void onChoose(Map<String, String> item) {
				currentCode = item.get("AREA_CODE");
				loadKpiWarns(optime, currentCode);
			}
		});

		ListView kpiNameList = (ListView) findViewById(R.id.kpiNameList);
		ListView kpiWarnList = (ListView) findViewById(R.id.kpiWarnList);
		// 下钻事件
		kpiNameList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// showHistoryWarns(position);
			}
		});
		kpiWarnList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// showHistoryWarns(position);
			}
		});
	}

	private void showHistoryWarns(int position) {
		if (warns != null) {
			Map<String, String> item = warns.get(position);
			if (item != null) {
				String kpiCode = item.get("KPI_CODE");
				String kpiName = item.get("KPI_NAME");
				Intent intent = new Intent();
				intent.putExtra("KPI_NAME", kpiName);
				intent.putExtra("KPI_CODE", kpiCode);
				intent.putExtra("areaCode", currentCode);
				intent.setClass(this, KpiWarnHisActivity.class);
				startActivity(intent);
			}
		}
	}

	/**
	 * 加载热点信息
	 */
	private void loadKpiWarns(String date, String area) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("optime", date);
		params.put("areaCode", area);
		HttpRequestTask task = new LoadKpiWarnTask(this);
		task.execute(Constant.Path_LoadKpiWarn, params);
	}

	private void renderKpiWarnList(JSONObject result) {
		this.refactorData(result);
		this.renderListViews();
		date_button.setDate(DateUtil.getDate(optime, DateUtil.PATTERN_8));
		area_button.setData(areas, "AREA_NAME", "AREA_CODE");
		area_button.setChoice(currentCode);
	}

	/**
	 * 重构数据结构，告警数据列表、地区列表
	 */
	private void refactorData(JSONObject result) {
		optime = JsonUtil.optString(result, "optime");
		currentCode = JsonUtil.optString(result, "currentCode");

		warns.clear();
		JSONArray data = result.optJSONArray("data");
		int len = data.length();
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				JSONObject obj = data.optJSONObject(i);
				if (obj != null) {
					Map<String, String> item = new HashMap<String, String>();
					item.put("KPI_CODE", JsonUtil.optString(obj, "KPI_CODE"));
					item.put("KPI_NAME", JsonUtil.optString(obj, "KPI_NAME"));
					item.put("KPI_UNIT", JsonUtil.optString(obj, "KPI_UNIT"));
					item.put("KPI_CAL_RULE",
							JsonUtil.optString(obj, "KPI_CAL_RULE"));
					item.put("AREA_NAME", JsonUtil.optString(obj, "AREA_NAME"));
					item.put("CURDAY_VALUE",
							JsonUtil.optString(obj, "CURDAY_VALUE"));
					item.put("CD_COL", JsonUtil.optString(obj, "CD_COL"));
					item.put("CD_MYOY", JsonUtil.optString(obj, "CD_MYOY"));
					item.put(
							"ALARM_PERCENT",
							KpiConstant.getColTitle(JsonUtil.optString(obj,
									"ALARM_COLUMN"))
									+ JsonUtil
											.optString(obj, "ALARM_CONDITION")
									+ JsonUtil.optString(obj, "ALARM_PERCENT"));
					item.put("KPI_TYPE_NAME",
							JsonUtil.optString(obj, "KPI_TYPE_NAME"));
					warns.add(item);
				}
			}
		}

		areas.clear();
		JSONArray authArea = result.optJSONArray("authArea");
		int authAreaLen = authArea.length();
		if (authAreaLen > 0) {
			for (int j = 0; j < authAreaLen; j++) {
				JSONObject areaObj = authArea.optJSONObject(j);
				if (areaObj != null) {
					Map<String, String> item = new HashMap<String, String>();
					item.put("AREA_CODE",
							JsonUtil.optString(areaObj, "AREA_CODE"));
					item.put("AREA_NAME",
							JsonUtil.optString(areaObj, "AREA_NAME"));
					item.put("AREA_ORDER",
							JsonUtil.optString(areaObj, "AREA_ORDER"));
					areas.add(item);
				}
			}
		}
	}

	private void renderListViews() {

		ListView kpiNameList = (ListView) findViewById(R.id.kpiNameList);
		ListView kpiWarnList = (ListView) findViewById(R.id.kpiWarnList);

		// render name list
		String[] from1 = new String[] { "KPI_NAME" };
		int[] to1 = new int[] { R.id.kpiName };
		SimpleAdapter nameAdapter = new SimpleAdapter(this, warns,
				R.layout.activity_kpi_warn_item_name, from1, to1);
		kpiNameList.setAdapter(nameAdapter);

		// render warns list
		KpiWarnAdaptor kpiWarnAdaptor = new KpiWarnAdaptor(this, warns);
		kpiWarnList.setAdapter(kpiWarnAdaptor);

		// set sync-scroll and other events
		UIUtil.setListViewScrollSync(kpiNameList, kpiWarnList);
		kpiNameList.setLongClickable(false);
		kpiWarnList.setLongClickable(false);

	}

	class KpiWarnAdaptor extends BaseAdapter {

		private Context context;
		private LayoutInflater layoutInflater;
		private List<Map<String, String>> warns;

		public KpiWarnAdaptor(Context context, List<Map<String, String>> warns) {
			this.context = context;
			this.layoutInflater = LayoutInflater.from(context);
			this.warns = warns;
		}

		@Override
		public int getCount() {
			if (warns == null)
				return 0;
			return warns.size();
		}

		@Override
		public Object getItem(int position) {
			return warns.get(position);
		}

		@Deprecated
		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = layoutInflater.inflate(
						R.layout.activity_kpi_warn_item_warns, null);
			}

			TextView kpiValue_content = (TextView) convertView
					.findViewById(R.id.kpiValue_content);
			TextView kpiValue_unit = (TextView) convertView
					.findViewById(R.id.kpiValue_unit);
			TextView kpiCol = (TextView) convertView.findViewById(R.id.kpiCol);
			TextView kpiMyoy = (TextView) convertView
					.findViewById(R.id.kpiMyoy);
			TextView kpiMax = (TextView) convertView.findViewById(R.id.kpiMax);
			TextView kpiType = (TextView) convertView
					.findViewById(R.id.kpiType);
			TextView kpiArea = (TextView) convertView
					.findViewById(R.id.kpiArea);

			Map<String, String> item = warns.get(position);

			ColumnDataFilter cdf = ColumnDataFilter.getInstance();
			ColumnDisplyInfo cdi1 = cdf.filterWithDefaultValue(
					item.get("KPI_CAL_RULE"), item.get("KPI_UNIT"),
					item.get("CURDAY_VALUE"));
			kpiValue_content.setText(cdi1.getValue());
			kpiValue_content.setTextColor(context.getResources().getColor(
					cdi1.getColor()));
			kpiValue_unit.setText(cdi1.getUnit());

			ColumnDisplyInfo cdi2 = cdf.filterWithDefaultValue("+|2", "%",
					item.get("CD_COL"));
			kpiCol.setText(cdi2.getValue() + cdi2.getUnit());
			kpiCol.setTextColor(context.getResources()
					.getColor(cdi2.getColor()));
			cdi2 = cdf.filterWithDefaultValue("+|2", "%", item.get("CD_MYOY"));
			kpiMyoy.setText(cdi2.getValue() + cdi2.getUnit());
			kpiMyoy.setTextColor(context.getResources().getColor(
					cdi2.getColor()));

			kpiMax.setText(item.get("ALARM_PERCENT"));
			kpiType.setText(item.get("KPI_TYPE_NAME"));
			kpiArea.setText(item.get("AREA_NAME"));
			return convertView;
		}

	}

	class LoadKpiWarnTask extends HttpRequestTask {
		public LoadKpiWarnTask(Context context) {
			super(context);
		}

		@Override
		protected void handleResult(JSONObject result) {
			renderKpiWarnList(result);
		}
	}
}
