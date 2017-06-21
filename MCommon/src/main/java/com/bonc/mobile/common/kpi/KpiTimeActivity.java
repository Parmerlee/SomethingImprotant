
package com.bonc.mobile.common.kpi;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.kpi.ColumnDataFilter.ColumnDisplyInfo;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.common.view.ACLineGraphView;
import com.bonc.mobile.common.view.ACLineGraphView.OnBrickMoveListener;

public abstract class KpiTimeActivity extends BaseSubKpiActivity {
    protected KpiDataModel dataModel;
    protected ACLineGraphView line_chart;
    protected KpiDataView dataView;
    int[] top_item_id = {
            R.id.top_item_1, R.id.top_item_2, R.id.top_item_3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kpi_time);
        initView();
        loadData();
    }

    @Override
    protected void initView() {
        super.initView();
        TextViewUtils.setText(this, R.id.title, areaName + ">" + kpiName);
        line_chart = (ACLineGraphView) findViewById(R.id.line_chart);
        line_chart.setOnBrickMoveListener(new OnBrickMoveListener() {
            @Override
            public void onStop(int graphDataIndex) {
                int size = dataModel.getKpiDataList().size();
                if (size > 0) {
                    int position = size - 1 - graphDataIndex;
                    bindTopData(dataModel, position);
                    dataView.smoothScrollToPosition(position);
                    dataView.setSelection(position);
                }
            }

            @Override
            public void onMove(int graphDataIndex) {
                onStop(graphDataIndex);
            }
        });
        dataView = (KpiDataView) findViewById(R.id.data_view);
    }

    @Override
    protected void loadData() {
        Map<String, String> param = new LinkedHashMap<String, String>();
        param.put("menuCode", menuCode);
        param.put("optime", optime);
        param.put("areaCode", areaCode);
        param.put("queryCode", kpiCode);
        param.put("dateType", dateType);
        new LoadDataTask(this).execute(getQueryAction(), param);
    }

    @Override
    protected void bindData(JSONObject result) {
        KpiDataModel model = new KpiDataModel();
        model.setType(2);
        model.setKpiCode(kpiCode);
        model.build(result);
        model.setColInfoLeft(KpiDataModel.COL_INFO_LEFT_TIME);
        KpiDataModel.mergeRuleUnit(model.getColumnInfoRight(), model.getKpiConfig(kpiCode));
        dataModel = model;
        if (model.getKpiDataList().size() == 0) {
            showToast(getString(R.string.no_data));
        } else {
            bindChartData(model);
            Collections.reverse(model.getKpiDataList());
            model.syncKpiData(2);
            dataView.setModel(model);
            bindTopData(model, 0);
        }
    }

    void bindTopData(KpiDataModel model, int index) {
        List<Map<String, String>> data = model.getKpiDataList();
        List<Map<String, String>> colInfo = model.getColumnInfoRight();
        for (int i = 0; i < top_item_id.length && i < colInfo.size(); i++) {
            bindTopItemData(findViewById(top_item_id[i]), data.get(index), colInfo.get(i));
        }
    }

    void bindTopItemData(View container, Map<String, String> data, Map<String, String> colInfo) {
        TextViewUtils.setText(container, R.id.zhibiao_desc,
                colInfo.get(KpiConstant.KEY_RELATION_KPIVALUE_COLUMN_NAME));
        String value = data.get(colInfo.get(KpiConstant.KEY_RELATION_KPIVALUE_COLUMN));
        ColumnDisplyInfo cdi = ColumnDataFilter.getInstance().filterWithDefaultValue(
                colInfo.get(KpiConstant.KEY_KPIVALUE_RULE),
                colInfo.get(KpiConstant.KEY_KPIVALUE_UNIT), value);
        TextViewUtils.setText(container, R.id.zhibiao_value, cdi.getValue());
        TextViewUtils.setText(container, R.id.zhibiao_unit, cdi.getUnit());
    }

    void bindChartData(KpiDataModel model) {
        String val_col = model.getColumnKey(0);
        String[] dateString = KpiDataModel.getTimeLabels(model.getKpiDataList());
        double[] dataLine = DataUtil.extractValArray(model.getKpiDataList(), val_col);
        String[] brickStrings = DataUtil.extractList(model.getKpiDataList(),
                KpiConstant.KEY_OP_TIME).toArray(new String[0]);
        line_chart.setData(dateString, dataLine, brickStrings,model.getKpiConfig(kpiCode).get(KpiConstant.KEY_KPI_UNIT));
    }
}
