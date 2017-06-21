
package com.bonc.mobile.common.kpi;

import java.util.List;
import java.util.Map;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.util.TextViewUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

public class KpiDataExpandableListAdapter extends BaseExpandableListAdapter {
    Context context;
    KpiDataModel dataModel;
    List<Map<String, String>> columnInfo;
    List<Map<String, Object>> composite;
    KpiTableColumnClickListener columnClickListener;

    public KpiDataExpandableListAdapter(Context context, KpiDataModel dataModel, boolean left_side) {
        this.context = context;
        this.dataModel = dataModel;
        columnInfo = left_side ? dataModel.getColumnInfoLeft() : dataModel.getColumnInfoRight();
        composite = dataModel.getCompositeGroup();
    }

    @Override
    public int getGroupCount() {
        return composite.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return dataModel.getGroupKpiCodeList(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.kpi_group_row, parent, false);
        } else
            view = convertView;
        TextViewUtils.setText(view, R.id.group_title,
                (String) composite.get(groupPosition).get("GROUP_NAME"));
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {
        KpiDataRow view;
        if (convertView == null) {
            view = (KpiDataRow) LayoutInflater.from(context).inflate(R.layout.kpi_data_row, parent,
                    false);
            view.buildDataRow(columnInfo);
            view.setColumnClickListener(columnClickListener);
        } else
            view = (KpiDataRow) convertView;
        String rowKey = dataModel.getGroupKpiCodeList(groupPosition).get(childPosition);
        view.updateDataRow(rowKey, columnInfo, dataModel);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setColumnClickListener(KpiTableColumnClickListener columnClickListener) {
        this.columnClickListener = columnClickListener;
    }

}
