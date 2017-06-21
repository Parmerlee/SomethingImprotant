
package com.bonc.mobile.common.kpi;

import java.util.Map;

import android.content.Intent;

import com.bonc.mobile.common.activity.BaseDataActivity;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.util.DateUtil;

public abstract class BaseSubKpiActivity extends BaseDataActivity {
    public final static String KPI_CODE = "kpiCode";
    public final static String OPTIME = "dataTime";
    public final static String AREA_CODE = "areaCode";
    public final static String DATE_TYPE = "dateType";
    public final static String AREA_NAME = "areaName";
    public final static String KPI_NAME = "kpiName";

    protected String optime, areaCode, dateType, kpiCode;
    protected String areaName, kpiName;
    protected boolean monthQuery, firstQuery = true;
    protected Map<String, String> menuInfo;

    @Override
    protected void initBaseData() {
        super.initBaseData();
        Intent intent = getIntent();
        optime = intent.getStringExtra(OPTIME);
        areaCode = intent.getStringExtra(AREA_CODE);
        kpiCode = intent.getStringExtra(KPI_CODE);
        dateType = intent.getStringExtra(DATE_TYPE);
        areaName = intent.getStringExtra(AREA_NAME);
        kpiName = intent.getStringExtra(KPI_NAME);
        monthQuery = KpiConstant.DATA_TYPE_MONTH.equals(dateType);
        menuInfo = getConfigLoader().getMenuInfo(menuCode);
    }

    protected String getDatePattern() {
        return "D".equals(dateType) ? DateUtil.PATTERN_8 : DateUtil.PATTERN_6;
    }

    protected abstract BaseConfigLoader getConfigLoader();

    protected abstract String getQueryAction();

}
