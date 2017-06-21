
package com.bonc.mobile.common.kpi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.util.NumberUtil;
import com.bonc.mobile.common.util.StringUtil;

public class KpiDataRow extends LinearLayout {
    KpiNameColumn nameColumn;
    KpiRelationColumn relationColumn;
    KpiTrendColumn trendColumn;
    Map<String, KpiDataColumn> dataColumnMap = new HashMap<String, KpiDataColumn>();
    KpiTableColumnClickListener columnClickListener;

    public KpiDataRow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KpiDataRow(Context context) {
        super(context);
    }

    public void setColumnClickListener(KpiTableColumnClickListener columnClickListener) {
        this.columnClickListener = columnClickListener;
    }

    public void buildDataRow(List<Map<String, String>> columnInfo) {
        for (int i = 0; i < columnInfo.size(); i++) {
            buildDataColumn(columnInfo.get(i));
        }
    }

    void buildDataColumn(Map<String, String> item) {
        KpiTableColumn column;
        if (item.containsKey(KpiConstant.KEY_COLUMN_TYPE)) {
            int columnType = Integer.parseInt(item.get(KpiConstant.KEY_COLUMN_TYPE));
            switch (columnType) {
                case 0:// relation col
                    relationColumn = new KpiRelationColumn(getContext());
                    column = relationColumn;
                    break;
                case 1:// trend col
                    trendColumn = new KpiTrendColumn(getContext());
                    column = trendColumn;
                    break;
                default:// name col
                    nameColumn = new KpiNameColumn(getContext());
                    column = nameColumn;
                    break;
            }
        } else {
            String key = item.get(KpiConstant.KEY_RELATION_KPIVALUE_COLUMN);
            KpiDataColumn c = new KpiDataColumn(getContext(), key);
            dataColumnMap.put(key, c);
            column = c;
        }
        column.build(this);
        addView(column.getView());
    }

    public void updateDataRow(String rowKey, List<Map<String, String>> columnInfo,
            KpiDataModel dataModel) {
        setRowBackground(rowKey, dataModel);
        for (int i = 0; i < columnInfo.size(); i++) {
            updateDataColumn(rowKey, columnInfo.get(i), dataModel);
        }
    }

    void setRowBackground(String rowKey, KpiDataModel dataModel) {
        int BACKGROUND_FIRST = R.mipmap.rectangle_light_blue_horizontal_long;
        int BACKGROUND_SECOND = R.mipmap.rectangle_light_blue_horizontal_long;
        if (dataModel.getType() == 0) {
            int kpiLevel = NumberUtil.changeToInt(dataModel.getKpiConfig(rowKey).get(
                    KpiConstant.KEY_KPI_LEVEL));
            setBackgroundResource(kpiLevel > 1 ? BACKGROUND_SECOND : BACKGROUND_FIRST);
        } else {
            setBackgroundResource(BACKGROUND_FIRST);
        }
    }

    void updateDataColumn(String rowKey, Map<String, String> item, KpiDataModel dataModel) {
        Map<String, String> kpiConfig = dataModel.getKpiConfig(dataModel.getType() == 0 ? rowKey
                : dataModel.getKpiCode());
        if (item.containsKey(KpiConstant.KEY_COLUMN_TYPE)) {
            int columnType = Integer.parseInt(item.get(KpiConstant.KEY_COLUMN_TYPE));
            switch (columnType) {
                case 0:// relation col
                    break;
                case 1:// trend col
                    trendColumn.setData(dataModel.getTrendData(rowKey));
                    trendColumn.setOnClickListener(rowKey, dataModel, columnClickListener);
                    break;
                default:// name col
                    String rowName;
                    if (dataModel.getType() == 0) {
                        rowName = kpiConfig.get(KpiConstant.KEY_KPI_NAME);
                    } else if (dataModel.getType() == 1) {
                        rowName = dataModel.getKpiData(rowKey).get(KpiConstant.KEY_AREA_NAME);
                    } else {
                        rowName = dataModel.getKpiData(rowKey).get(KpiConstant.KEY_OP_TIME);
                    }
                    nameColumn.setText(StringUtil.nullToString2(rowName));
                    nameColumn.setTextAppearance(kpiConfig.get(KpiConstant.KEY_KPI_LEVEL));
                    nameColumn.setOnClickListener(rowKey, dataModel, columnClickListener);
                    nameColumn.setOnLongClickListener(rowKey, dataModel, columnClickListener);
                    nameColumn.setOnTouchListener(rowKey, dataModel, columnClickListener);
                    break;
            }
        } else {
            String key = item.get(KpiConstant.KEY_RELATION_KPIVALUE_COLUMN);
            KpiDataColumn c = dataColumnMap.get(key);
            c.loadRuleUnit(item, kpiConfig);
            c.setData(dataModel.getKpiData(rowKey));
            c.setOnClickListener(rowKey, dataModel, columnClickListener);
        }
    }
}
