package com.bonc.mobile.remoteview.activity;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.kpi.SimpleKpiDataAdapter;
import com.bonc.mobile.common.kpi.SimpleKpiDataColumn;
import com.bonc.mobile.common.kpi.SimpleKpiDataModel;
import com.bonc.mobile.common.kpi.SimpleKpiDataRow;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.common.RemoteUtil;

@SuppressLint("ResourceAsColor")
public class RVStationReportActivity extends RVStationReportBaseActivity {
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
	protected String getQueryAction() {
		return "/cellphone/dailyReport";
	}

	@Override
	protected SimpleKpiDataAdapter getDataAdapter(SimpleKpiDataModel model,
			int type) {
		return new MySimpleKpiDataAdapter(this, model, type);
	}

	class MySimpleKpiDataAdapter extends TableKpiDataAdapter {
		public MySimpleKpiDataAdapter(Context context,
				SimpleKpiDataModel dataModel, int type) {
			super(context, dataModel, type);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			SimpleKpiDataRow v = (SimpleKpiDataRow) super.getView(position,
					convertView, parent);
			Map<String, String> item = data.get(position);
			Map<String, String> map = new HashMap<String, String>();
			if (type == 1) {
				map = this.header.get(0);
			} else {
				map = null;
			}

			if (map != null) {

				SimpleKpiDataColumn column2 = v.getColumn(map
						.get("COLUMN_VALUE"));
				if (column2 != null) {
					TextView tv = (TextView) column2.getView().findViewById(
							R.id.data_column_content);

					LayoutParams params = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					params.addRule(RelativeLayout.CENTER_VERTICAL);
					tv.setLayoutParams(params);
					tv.setGravity(Gravity.LEFT);

				}
			}

			if (item.containsKey("ALARM_TAG")
					&& "1".equals(item.get("ALARM_TAG"))) {
				v.setColumnTextColor(R.color.red);
				SimpleKpiDataColumn column = v.getColumn("ALARM_TAG");
				if (column != null) {
					column.setTextData("是");
				}

			} else {
				SimpleKpiDataColumn column = v.getColumn("ALARM_TAG");
				if (column != null) {

					column.setTextData("否");
				}
				v.setColumnTextColor(R.color.black);
			}
			return v;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Map<String, String> item = (Map<String, String>) parent.getAdapter()
				.getItem(position);
		if (item.containsKey("ALARM_TAG") && "1".equals(item.get("ALARM_TAG"))) {
			Intent intent = new Intent(this, RVStationReportDrillActivity.class);
			intent.putExtra(BaseConfigLoader.KEY_MENU_CODE, menuCode);
			intent.putExtra("areaCode", area_button.getChoiceValue());
			intent.putExtra("cellPhoneTower", item.get("BTS_NAME"));
			startActivity(intent);
		}
	}

	@Override
	protected boolean hasDateArea() {
		return true;
	}
}
