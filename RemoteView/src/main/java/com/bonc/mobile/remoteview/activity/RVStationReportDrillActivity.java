package com.bonc.mobile.remoteview.activity;

import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bonc.mobile.common.kpi.SimpleKpiDataAdapter;
import com.bonc.mobile.common.kpi.SimpleKpiDataColumn;
import com.bonc.mobile.common.kpi.SimpleKpiDataModel;
import com.bonc.mobile.common.kpi.SimpleKpiDataRow;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.common.RemoteUtil;

public class RVStationReportDrillActivity extends RVStationReportBaseActivity {
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
		return "/cellphone/top20Detail";
	}

	@Override
	protected void fillExtraParam(Map<String, String> param) {
		param.put("areaCode", getIntent().getStringExtra("areaCode"));
		param.put("cellPhoneTower", getIntent()
				.getStringExtra("cellPhoneTower"));
	}

	@Override
	protected SimpleKpiDataAdapter getDataAdapter(SimpleKpiDataModel model,
			int type) {
		return new TableKpiDataAdapter(this, model, type);
	}

	protected class TableKpiDataAdapter extends SimpleKpiDataAdapter {
		public TableKpiDataAdapter(Context context,
				SimpleKpiDataModel dataModel, int type) {
			super(context, dataModel, type);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			SimpleKpiDataRow view;
			if (convertView == null) {
				view = (SimpleKpiDataRow) LayoutInflater
						.from(context)
						.inflate(
								com.bonc.mobile.remoteview.R.layout.simple_kpi_data_row_new,
								parent, false);
				view.buildDataRow(
						header,
						com.bonc.mobile.remoteview.R.layout.simple_kpi_data_column_new);

			} else
				view = (SimpleKpiDataRow) convertView;

			view.setColumnWidth(view.getResources().getDimensionPixelSize(
					R.dimen.kpi_data_col_width));
			Map<String, String> item = data.get(position);
			view.updateDataRow(header, item);

			if (item.containsKey("ALARM_TAG")
					&& "1".equals(item.get("ALARM_TAG"))) {
				SimpleKpiDataColumn column = view.getColumn("ALARM_TAG");
				if (column != null) {
					column.setTextData("是");
				}

			} else {
				SimpleKpiDataColumn column = view.getColumn("ALARM_TAG");
				if (column != null) {

					column.setTextData("否");
				}
			}
			return view;
		}
	}
}
