package com.bonc.mobile.remoteview.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bonc.mobile.common.activity.BaseDataActivity;
import com.bonc.mobile.common.kpi.ColumnDataFilter;
import com.bonc.mobile.common.kpi.KpiConstant;
import com.bonc.mobile.common.kpi.ColumnDataFilter.ColumnDisplyInfo;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.view.DataChooseButton;
import com.bonc.mobile.common.view.DataChooseButton.OnDataChooseListener;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.common.Constant;
import com.bonc.mobile.remoteview.common.RemoteUtil;

public class KpiWarnHisActivity extends BaseDataActivity {

	private String areaCode;
	private String kpiCode;

	private List<Map<String, String>> warns = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> areas = new ArrayList<Map<String, String>>();

	private DataChooseButton area_button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_kpi_warn_his);
		this.initView();
		this.loadKpiHisWarns(areaCode);
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

		Bundle extras = this.getIntent().getExtras();
		String menuName = (String) extras.get("KPI_NAME");
		kpiCode = (String) extras.get("KPI_CODE");
		areaCode = (String) extras.get("areaCode");

		// 栏目标题
		TextView view = (TextView) this.findViewById(R.id.title);
		if (menuName.length() > 9) {
			menuName = menuName.substring(0, 9) + "...";
		}
		view.setText("历史告警(" + menuName + ")");

		// 地区按钮
		area_button = (DataChooseButton) findViewById(R.id.area_button);
		area_button.setOnDataChooseListener(new OnDataChooseListener() {
			@Override
			public void onChoose(Map<String, String> item) {
				areaCode = item.get("AREA_CODE");
				loadKpiHisWarns(areaCode);
			}
		});
	}

	private void renderKpiHisWarns(JSONObject result) {
		this.refactorData(result);
		ListView kpiWarnList = (ListView) findViewById(R.id.kpiWarnList);
		KpiHisWarnAdaptor kpiWarnAdaptor = new KpiHisWarnAdaptor(this, warns);
		kpiWarnList.setAdapter(kpiWarnAdaptor);
		area_button.setData(areas, "AREA_NAME", "AREA_CODE");
		area_button.setChoice(areaCode);
	}

	/**
	 * 重构数据结构，告警数据列表、地区列表
	 */
	private void refactorData(JSONObject result) {
		areaCode = JsonUtil.optString(result, "currentCode");

		warns.clear();
		JSONArray data = result.optJSONArray("data");
		int len = data.length();
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				JSONObject obj = data.optJSONObject(i);
				if (obj != null) {
					Map<String, String> item = new HashMap<String, String>();
					item.put("KPI_NAME", JsonUtil.optString(obj, "KPI_NAME"));
					item.put("KPI_UNIT", JsonUtil.optString(obj, "KPI_UNIT"));
					item.put("KPI_CAL_RULE",
							JsonUtil.optString(obj, "KPI_CAL_RULE"));
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
					item.put("OP_TIME", JsonUtil.optString(obj, "OP_TIME"));
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

	private void loadKpiHisWarns(String currentCode) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("kpiCode", kpiCode);
		params.put("areaCode", currentCode);
		HttpRequestTask task = new KpiHisTask(this);
		task.execute(Constant.Path_LoadKpiWarnHis, params);
	}

	class KpiHisWarnAdaptor extends BaseAdapter {

		private Context context;
		private LayoutInflater layoutInflater;
		private List<Map<String, String>> warns;

		public KpiHisWarnAdaptor(Context context,
				List<Map<String, String>> warns) {
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
						R.layout.activity_kpi_warn_his_item, null);
			}

			TextView kpiValue_content = (TextView) convertView
					.findViewById(R.id.kpiValue_content);
			TextView kpiValue_unit = (TextView) convertView
					.findViewById(R.id.kpiValue_unit);
			TextView kpiCol = (TextView) convertView.findViewById(R.id.kpiCol);
			TextView kpiMyoy = (TextView) convertView
					.findViewById(R.id.kpiMyoy);
			TextView kpiMax = (TextView) convertView.findViewById(R.id.kpiMax);
			TextView kpiDate = (TextView) convertView
					.findViewById(R.id.kpiDate);

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
			kpiDate.setText(item.get("OP_TIME"));
			return convertView;
		}

	}

	class KpiHisTask extends HttpRequestTask {

		public KpiHisTask(Context context) {
			super(context);
		}

		@Override
		protected void handleResult(JSONObject result) {
			renderKpiHisWarns(result);
		}
	}
}
