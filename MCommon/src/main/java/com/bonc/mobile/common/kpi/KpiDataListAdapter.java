package com.bonc.mobile.common.kpi;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.R;

public class KpiDataListAdapter extends BaseAdapter {
	Context context;
	KpiDataModel dataModel;
	List<Map<String, String>> columnInfo;
	List<String> composite;
	KpiTableColumnClickListener columnClickListener;
	int selection = -1;

	public KpiDataListAdapter(Context context, KpiDataModel dataModel,
			boolean left_side) {
		this.context = context;
		this.dataModel = dataModel;
		columnInfo = left_side ? dataModel.getColumnInfoLeft() : dataModel
				.getColumnInfoRight();
		composite = dataModel.getCompositeList();
		if (!AppConstant.SEC_ENH)
			Log.d(this.getClass().getName(), "columnInfo:" + columnInfo);
	}

	@Override
	public int getCount() {
		return composite.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		KpiDataRow view;
		if (convertView == null) {
			view = (KpiDataRow) LayoutInflater.from(context).inflate(
					R.layout.kpi_data_row, parent, false);
			view.buildDataRow(columnInfo);
			view.setColumnClickListener(columnClickListener);
		} else
			view = (KpiDataRow) convertView;
		String rowKey = composite.get(position);
		view.updateDataRow(rowKey, columnInfo, dataModel);
		if (position == selection)
			view.setBackgroundResource(R.color.selected_color);
		return view;
	}

	public void setColumnClickListener(
			KpiTableColumnClickListener columnClickListener) {
		this.columnClickListener = columnClickListener;
	}

	public void setSelection(int position) {
		selection = position;
		notifyDataSetChanged();
	}
}
