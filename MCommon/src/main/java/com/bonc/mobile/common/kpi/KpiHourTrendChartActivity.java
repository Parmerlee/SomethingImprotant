
package com.bonc.mobile.common.kpi;

import org.json.JSONObject;

import android.os.Bundle;

import com.bonc.mobile.common.R;

public abstract class KpiHourTrendChartActivity extends BaseSubKpiActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kpi_hour_trend);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void bindData(JSONObject result) {
    }

}
