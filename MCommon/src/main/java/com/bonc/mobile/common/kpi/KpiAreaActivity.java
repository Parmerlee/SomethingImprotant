
package com.bonc.mobile.common.kpi;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.chart.BarChartView;
import com.bonc.mobile.common.chart.SimpleChartData;
import com.bonc.mobile.common.kpi.SimpleKpiTitleRow.OnColumnSortListener;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.DateUtil;
import com.bonc.mobile.common.util.NumberUtil;
import com.bonc.mobile.common.util.StringUtil;
import com.bonc.mobile.common.util.TextViewUtils;

public abstract class KpiAreaActivity extends BaseSubKpiActivity {
    protected KpiDataModel dataModel;
    protected BarChartView bar_chart;
    protected KpiDataView dataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kpi_area);
        initView();
        loadData();
    }

    @Override
    protected void initView() {
        super.initView();
        TextViewUtils.setText(this, R.id.title, areaName + ">" + kpiName);
        String highdesc = StringUtil.nullToString(menuInfo.get("HIGHEST_DESC"));
        TextViewUtils.setText(this, R.id.highest_tv, highdesc + "最高");
        TextViewUtils.setText(this, R.id.lowest_tv, highdesc + "最低");
        bar_chart = (BarChartView) findViewById(R.id.bar_chart);
        dataView = (KpiDataView) findViewById(R.id.data_view);
        findViewById(R.id.area_button).setVisibility(View.GONE);
    }

    @Override
    protected void resetView() {
        TextViewUtils.setText(this, R.id.highest_area, "");
        TextViewUtils.setText(this, R.id.lowest_area, "");
        bar_chart.setData(null);
    }

    @Override
    protected void loadData() {
        super.loadData();
        Map<String, String> param = new LinkedHashMap<String, String>();
        if (!firstQuery) {
            optime = DateUtil.formatter(date_button.getDate(), getDatePattern());
        }
        param.put("menuCode", menuCode);
        param.put("optime", optime);
        param.put("areaCode", areaCode);
        param.put("queryCode", kpiCode);
        param.put("dateType", dateType);
        firstQuery = false;
        new LoadDataTask(this).execute(getQueryAction(), param);
    }

    @Override
    protected void bindData(JSONObject result) {
        KpiDataModel model = new KpiDataModel();
        model.setType(1);
        model.setKpiCode(kpiCode);
        model.build(result);
        model.setColInfoLeft(KpiDataModel.COL_INFO_LEFT_AREA);
        KpiDataModel.mergeRuleUnit(model.getColumnInfoRight(), model.getKpiConfig(kpiCode));
        dataModel = model;
        if (model.getKpiDataList().size() == 0) {
            showToast(getString(R.string.no_data));
        } else {
            bindTopData(model);
            bindChartData(model);
        }
        dataView.setModel(model);
        dataView.getRightKpiTitle().setOnColumnSortListener(new OnColumnSortListener() {
            @Override
            public void onColumnSort(int index, int direction) {
                sortDataView(index, direction);
            }
        });
        date_button.setDate(DateUtil.getDate(model.getOpTime(), getDatePattern()));
    }

    void bindTopData(KpiDataModel model) {
        List<Map<String, String>> data = model.getKpiDataList();
        TextViewUtils.setText(this, R.id.highest_area, data.get(0).get(KpiConstant.KEY_AREA_NAME));
        TextViewUtils.setText(this, R.id.lowest_area,
                data.get(data.size() - 1).get(KpiConstant.KEY_AREA_NAME));
    }

    void bindChartData(KpiDataModel model) {
        String val_col = model.getColumnKey(0);
        double scale = NumberUtil.getScale(NumberUtil.getAverage(DataUtil.extractValArray(
                model.getKpiDataList(), val_col)));
        String unit = model.getColumnInfo(0, KpiConstant.KEY_KPIVALUE_UNIT);
        TextViewUtils.setText(this, R.id.tv_w_unit, NumberUtil.getUnit(scale) + unit);
        SimpleChartData chartData = SimpleChartData.build(model.getKpiDataList(),
                KpiConstant.KEY_AREA_NAME, val_col, SimpleChartData.getConverter(scale));
        bar_chart.setData(chartData);
    }

    void sortDataView(int index, int direction) {
        KpiDataComparator comparator = new KpiDataComparator(dataModel.getColumnKey(index),
                direction);
        Collections.sort(dataModel.getKpiDataList(), comparator);
        dataModel.syncKpiData(1);
        dataView.updateView();
    }
}
