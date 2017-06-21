package com.bonc.mobile.common.kpi;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.util.UIUtil;

public class SimpleKpiDataView extends LinearLayout {
	SimpleKpiDataModel model;
	View contentView;
	int columnWidth;

	public SimpleKpiDataView(Context context, AttributeSet attrs) {
		super(context, attrs);
		columnWidth = getResources().getDimensionPixelSize(
				R.dimen.kpi_data_col_width);
		contentView = View.inflate(getContext(),
				R.layout.simple_kpi_table_view, null);
		addView(contentView);
		syncTwoList();
	}

	public SimpleKpiDataView(Context context) {
		this(context, null);
	}

	public SimpleKpiTitleRow getLeftKpiTitle() {
		return (SimpleKpiTitleRow) contentView.findViewById(R.id.title_left);
	}

	public SimpleKpiTitleRow getRightKpiTitle() {
		return (SimpleKpiTitleRow) contentView.findViewById(R.id.title_right);
	}

	public ListView getLeftList() {
		return (ListView) contentView.findViewById(R.id.list_left);
	}

	public ListView getRightList() {
		return (ListView) contentView.findViewById(R.id.list_right);
	}

	public SimpleKpiDataAdapter getLeftListAdapter() {
		return (SimpleKpiDataAdapter) getLeftList().getAdapter();
	}

	public SimpleKpiDataAdapter getRightListAdapter() {
		return (SimpleKpiDataAdapter) getRightList().getAdapter();
	}

	public void smoothScrollToPosition(int position) {
		getLeftList().smoothScrollToPositionFromTop(position, 0, 1000);
		getRightList().smoothScrollToPositionFromTop(position, 0, 1000);
	}

	public void setTitleStyle(int resid, int resid2) {
		getLeftKpiTitle().setBackgroundResource(resid);
		getLeftKpiTitle().setTextAppearance(resid2);
		getRightKpiTitle().setBackgroundResource(resid);
		getRightKpiTitle().setTextAppearance(resid2);
	}

	public void reset() {
		getLeftList().setAdapter(null);
		getRightList().setAdapter(null);
	}

	public void setModel(SimpleKpiDataModel model) {
		reset();
		this.model = model;
		buildTitle();
	}

	public void updateView(SimpleKpiDataAdapter left, SimpleKpiDataAdapter right) {
		getLeftList().setAdapter(left);
		getRightList().setAdapter(right);
	}

	//调整左侧title的宽度
	public void bulidLfetTilteWithWidth(int width) {
		getLeftKpiTitle().buildTitleRow(model.getLeftHeader(), width);
	}

	void buildTitle() {
		getLeftKpiTitle().buildTitleRow(model.getLeftHeader(), columnWidth);
		getRightKpiTitle().buildTitleRow(model.getRightHeader(), columnWidth);
	}

	void syncTwoList() {
		UIUtil.setListViewScrollSync(getLeftList(), getRightList());
	}
}
