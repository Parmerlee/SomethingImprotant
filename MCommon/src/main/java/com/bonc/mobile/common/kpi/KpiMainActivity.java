
package com.bonc.mobile.common.kpi;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.activity.BaseDataActivity;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.util.DateUtil;
import com.bonc.mobile.common.util.UIUtil;

public abstract class KpiMainActivity extends BaseDataActivity {
    protected KpiDataView dataView;
    protected String dateType, queryCode;
    protected boolean firstQuery = true, showArea;
    String curKpiCode;
    GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_kpi);
        initView();
        if (!isKpiMutable())
            loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isKpiMutable())
            loadData();
    }

    @Override
    protected void initBaseData() {
        super.initBaseData();
        dateType = getIntent().getStringExtra(BaseConfigLoader.KEY_DATE_TYPE);
        queryCode = getIntent().getStringExtra(BaseConfigLoader.KEY_QUERY_CODE);
        showArea = true;
    }

    @Override
    protected void initView() {
        initDateSelect();
        initAreaSelect();
        dataView = (KpiDataView) findViewById(R.id.data_view);
        dataView.setColumnClickListener(new KpiTableColumnClickListener() {
            @Override
            public void onClick(View v, String kpiCode, int columnType, KpiDataModel dataModel) {
                if (columnType == KpiTableColumn.COLUMN_TYPE_NAME
                        && "1".equals(dataModel.getKpiConfig(kpiCode).get("EXT1")))
                    showToast("该指标无地市数据");
                else if (KpiConstant.KPI_RULE_STRING.equals(dataModel.getKpiRule(kpiCode)))
                    showToast("该指标不支持下钻");
                else
                    showSubKpiActivity(kpiCode, columnType, dataModel);
            }

            @Override
            public void onLongClick(View v, String kpiCode, int columnType, KpiDataModel dataModel) {
                if (columnType == KpiTableColumn.COLUMN_TYPE_NAME) {
                    showKpiDefine(v, dataModel.getKpiConfig(kpiCode).get("KPI_DEFINE"));
                }
            }

            @Override
            public boolean onTouch(View v, MotionEvent event, String rowKey, int columnType,
                    KpiDataModel dataModel) {
                if (columnType == KpiTableColumn.COLUMN_TYPE_NAME) {
                    curKpiCode = rowKey;
                    return detector.onTouchEvent(event);
                }
                return false;
            }
        });
        detector = new GestureDetector(this, new SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                addKpiToCustom(curKpiCode);
                return true;
            }
        });
    }

    @Override
    protected void initDateSelect() {
        super.initDateSelect();
        date_button.setPattern("D".equals(dateType) ? DateUtil.PATTERN_MODEL2_10
                : DateUtil.PATTERN_MODEL2_7);
    }

    @Override
    protected void resetView() {
        dataView.reset();
        date_button.setClickable(false);
        area_button.setClickable(false);
    }

    @Override
    protected void loadData() {
        super.loadData();
        Map<String, String> param = new LinkedHashMap<String, String>();
        if (!firstQuery) {
            param.put("optime", DateUtil.formatter(date_button.getDate(), getDatePattern()));
        }
        if (showArea) {
            if (!firstQuery) {
                param.put("queryCode", area_button.getChoiceValue());
            } else if (queryCode != null)
                param.put("queryCode", queryCode);
        }
        param.put("menuCode", menuCode);
        param.put("dateType", dateType);
        param.put("clickCode",
                getConfigLoader().getMenuAttr(menuCode, BaseConfigLoader.KEY_MENU_PARENT_CODE));
        new LoadDataTask(UIUtil.getSafeContext(this)).execute(getQueryAction(), param);
    }

    @Override
    protected void bindData(JSONObject result) {
        if (result.has("flag") && result.optBoolean("flag") == false) {
            onNoKpi(result);
            return;
        }
        KpiDataModel model = new KpiDataModel();
        model.build(result);
        dataView.setModel(model);
        date_button.setDate(DateUtil.getDate(model.getOpTime(), getDatePattern()));
        date_button.setClickable(true);
        if (showArea) {
            area_button.setData(model.getAreaInfo(), model.getAreaKey()[0], model.getAreaKey()[1]);
            area_button.setChoice(model.getAreaCode());
            area_button.setClickable(true);
        }
        firstQuery = false;
    }

    protected String getDatePattern() {
        return "D".equals(dateType) ? DateUtil.PATTERN_8 : DateUtil.PATTERN_6;
    }

    protected boolean isKpiMutable() {
        return getConfigLoader().isMenuType(menuCode, BaseConfigLoader.MENU_TYPE_KPI_CUST);
    }

    protected Intent getSubKpiIntent(String kpiCode, int columnType, KpiDataModel dataModel) {
        return null;
    }

    protected void showSubKpiActivity(String kpiCode, int columnType, KpiDataModel dataModel) {
        Intent intent = getSubKpiIntent(kpiCode, columnType, dataModel);
        if (intent != null) {
            intent.putExtra(BaseConfigLoader.KEY_MENU_CODE, menuCode);
            intent.putExtra(BaseSubKpiActivity.OPTIME, dataModel.getOpTime());
            intent.putExtra(BaseSubKpiActivity.KPI_CODE, kpiCode);
            intent.putExtra(BaseSubKpiActivity.AREA_CODE, dataModel.getAreaCode());
            intent.putExtra(BaseSubKpiActivity.DATE_TYPE, dataModel.getDateType());
            intent.putExtra(BaseSubKpiActivity.AREA_NAME, area_button.getChoiceName());
            intent.putExtra(BaseSubKpiActivity.KPI_NAME,
                    dataModel.getKpiConfig(kpiCode).get(KpiConstant.KEY_KPI_NAME));
            startActivity(intent);
        }
    }

    protected void showKpiDefine(View anchor, String kpiDefine) {
        showInfowin(anchor,kpiDefine);
    }

    protected void addKpiToCustom(String kpiCode) {
    }

    protected void onNoKpi(JSONObject result) {
        firstQuery = true;
        resetView();
    }

    protected abstract BaseConfigLoader getConfigLoader();

    protected abstract String getQueryAction();
}
