
package com.bonc.mobile.common.kpi;

import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.kpi.ColumnDataFilter.ColumnDisplyInfo;
import com.bonc.mobile.common.util.NumberUtil;
import com.bonc.mobile.common.util.TextViewUtils;

public class SimpleKpiDataColumn {
    Context context;
    View containerView;
    LayoutInflater inflater;
    String kpiRule, kpiUnit;

    public SimpleKpiDataColumn(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void build(int resource, ViewGroup parent) {
        containerView = inflater.inflate(resource, parent, false);
    }

    public View getView() {
        return containerView;
    }

    public void loadRuleUnit(Map<String, String> colInfo) {
        kpiRule = colInfo.get(KpiConstant.KEY_KPIVALUE_RULE);
        kpiUnit = colInfo.get(KpiConstant.KEY_KPIVALUE_UNIT);
    }

    public void setData(String data) {
        ColumnDisplyInfo cdi = ColumnDataFilter.getInstance().filterWithDefaultValue(kpiRule,
                kpiUnit, data);
        TextViewUtils.setText(containerView, R.id.data_column_content, cdi.getValue());
        TextViewUtils.setText(containerView, R.id.data_column_unit, cdi.getUnit());
        TextViewUtils.setTextColor(containerView, R.id.data_column_content, cdi.getColor());
    }

    public void setData(String data, String fmt, String unit) {
        if (fmt != null)
            data = NumberUtil.format(NumberUtil.changeToDouble(data), fmt);
        TextViewUtils.setText(containerView, R.id.data_column_content, data);
        TextViewUtils.setText(containerView, R.id.data_column_unit, unit);
    }

    public void setTextData(String data) {
        TextViewUtils.setText(containerView, R.id.data_column_content, data);
        containerView.findViewById(R.id.data_column_unit).setVisibility(View.GONE);
    }

    public void setTextColor(int colorid) {
        TextViewUtils.setTextColor(containerView, R.id.data_column_content, colorid);
        TextViewUtils.setTextColor(containerView, R.id.data_column_unit, colorid);
    }
}
