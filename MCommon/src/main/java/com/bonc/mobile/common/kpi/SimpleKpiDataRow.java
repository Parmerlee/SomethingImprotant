
package com.bonc.mobile.common.kpi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.bonc.mobile.common.R;

public class SimpleKpiDataRow extends LinearLayout {
    Map<String, SimpleKpiDataColumn> dataColumnMap = new HashMap<String, SimpleKpiDataColumn>();

    public SimpleKpiDataRow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleKpiDataRow(Context context) {
        super(context);
    }

    public void buildDataRow(List<Map<String, String>> columnInfo) {
        buildDataRow(columnInfo, R.layout.simple_kpi_data_column);
    }

    public void buildDataRow(List<Map<String, String>> columnInfo, int resource) {
        for (int i = 0; i < columnInfo.size(); i++) {
            buildDataColumn(columnInfo.get(i), resource);
        }
    }

    protected void buildDataColumn(Map<String, String> info, int resource) {
        String key = getColumnKey(info);
        SimpleKpiDataColumn column = new SimpleKpiDataColumn(getContext());
        dataColumnMap.put(key, column);
        column.build(resource, this);
        addView(column.getView());
    }

    protected String getColumnKey(Map<String, String> info) {
        String key = info.get(KpiConstant.KEY_RELATION_KPIVALUE_COLUMN);
        if (key == null)
            key = info.get("COLUMN_VALUE");
        return key;
    }

    public void updateDataRow(List<Map<String, String>> columnInfo, Map<String, String> dataItem) {
        for (int i = 0; i < columnInfo.size(); i++) {
            Map<String, String> info = columnInfo.get(i);
            String key = getColumnKey(info);
            SimpleKpiDataColumn c = dataColumnMap.get(key);
            if (info.containsKey(KpiConstant.KEY_COLUMN_TYPE))
                c.setTextData(dataItem.get(key));
            else if (info.containsKey(KpiConstant.KEY_KPIVALUE_RULE)) {
                c.loadRuleUnit(info);
                c.setData(dataItem.get(key));
            } else if (info.containsKey("EXT_STR1") || info.containsKey("COLUMN_UNIT")) {
                c.setData(dataItem.get(key), info.get("EXT_STR1"), info.get("COLUMN_UNIT"));
            } else {
                c.setTextData(dataItem.get(key));
            }
        }
    }

    public SimpleKpiDataColumn getColumn(String key) {
        return dataColumnMap.get(key);
    }

    public void setColumnWidth(int w) {
        for (SimpleKpiDataColumn c : dataColumnMap.values()) {
            c.getView().setLayoutParams(new LayoutParams(w, LayoutParams.MATCH_PARENT));
        }
    }

    public void setColumnTextColor(int colorid) {
        for (SimpleKpiDataColumn c : dataColumnMap.values()) {
            c.setTextColor(colorid);
        }
    }
}
