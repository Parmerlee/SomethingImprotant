
package com.bonc.mobile.common.kpi;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.util.UIUtil;

public class KpiDataView extends LinearLayout {
    KpiDataModel model;
    View contentView;
    KpiTableColumnClickListener columnClickListener;

    public KpiDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KpiDataView(Context context) {
        super(context);
    }

    public void setColumnClickListener(KpiTableColumnClickListener columnClickListener) {
        this.columnClickListener = columnClickListener;
    }

    public KpiTitleRow getLeftKpiTitle() {
        return (KpiTitleRow) contentView.findViewById(R.id.title_left);
    }

    public KpiTitleRow getRightKpiTitle() {
        return (KpiTitleRow) contentView.findViewById(R.id.title_right);
    }

    public ListView getLeftList() {
        return (ListView) contentView.findViewById(R.id.list_left);
    }

    public ListView getRightList() {
        return (ListView) contentView.findViewById(R.id.list_right);
    }

    public KpiDataListAdapter getLeftListAdapter() {
        return (KpiDataListAdapter) getLeftList().getAdapter();
    }

    public KpiDataListAdapter getRightListAdapter() {
        return (KpiDataListAdapter) getRightList().getAdapter();
    }

    public void smoothScrollToPosition(int position) {
        getLeftList().smoothScrollToPositionFromTop(position, 0, 1000);
        getRightList().smoothScrollToPositionFromTop(position, 0, 1000);
    }

    public void setSelection(int position) {
        getLeftListAdapter().setSelection(position);
        getRightListAdapter().setSelection(position);
    }

    public void reset() {
        removeAllViews();
    }

    public void setModel(KpiDataModel model) {
        reset();
        this.model = model;
        buildTableView();
        updateView();
    }

    public KpiDataModel getModel() {
        return model;
    }

    public void updateView() {
        if (model.isGroup() == 1)
            updateGroupTableView();
        else
            updateTableView();
    }

    void buildTableView() {
        contentView = View.inflate(getContext(),
                model.isGroup() == 1 ? R.layout.kpi_group_table_view : R.layout.kpi_table_view,
                null);
        addView(contentView);
        buildTitle();
        syncTwoList();
    }

    void updateTableView() {
        KpiDataListAdapter adapter;
        ListView list_left = getLeftList();
        adapter = new KpiDataListAdapter(getContext(), model, true);
        adapter.setColumnClickListener(columnClickListener);
        list_left.setAdapter(adapter);
        ListView list_right = getRightList();
        adapter = new KpiDataListAdapter(getContext(), model, false);
        adapter.setColumnClickListener(columnClickListener);
        list_right.setAdapter(adapter);
    }

    void updateGroupTableView() {
        KpiDataExpandableListAdapter adapter;
        ExpandableListView list_left = (ExpandableListView) getLeftList();
        adapter = new KpiDataExpandableListAdapter(getContext(), model, true);
        adapter.setColumnClickListener(columnClickListener);
        list_left.setAdapter(adapter);
        ExpandableListView list_right = (ExpandableListView) getRightList();
        adapter = new KpiDataExpandableListAdapter(getContext(), model, false);
        adapter.setColumnClickListener(columnClickListener);
        list_right.setAdapter(adapter);
    }

    void buildTitle() {
        getLeftKpiTitle().buildTitleRow(model.getColumnInfoLeft());
        getRightKpiTitle().buildTitleRow(model.getColumnInfoRight());
    }

    void syncTwoList() {
        UIUtil.setListViewScrollSync(getLeftList(), getRightList());
    }
}
