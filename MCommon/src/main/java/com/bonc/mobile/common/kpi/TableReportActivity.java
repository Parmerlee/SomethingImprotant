
package com.bonc.mobile.common.kpi;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.activity.BaseDataActivity;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.util.DateUtil;
import com.bonc.mobile.common.util.TextViewUtils;

public abstract class TableReportActivity extends BaseDataActivity implements OnItemClickListener {
    protected SimpleKpiDataView dataView;
    boolean firstQuery = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentRes());
        initView();
        loadData();
    }

    protected int getContentRes() {
        return R.layout.activity_kpi_list_2;
    }

    @Override
    protected void initView() {
        super.initView();
        TextViewUtils.setText(this, R.id.title, getConfigLoader().getMenuName(menuCode));
        dataView = (SimpleKpiDataView) findViewById(R.id.data_view);
        dataView.getLeftList().setOnItemClickListener(this);
        dataView.getRightList().setOnItemClickListener(this);
        if (!hasDateArea())
            findViewById(R.id.kpi_panel).setVisibility(View.GONE);
    }

    @Override
    protected void loadData() {
        Map<String, String> param = new LinkedHashMap<String, String>();
        param.put("menuCode", menuCode);
        if (!firstQuery && hasDateArea()) {
            param.put("dataTime", DateUtil.formatter(date_button.getDate(), DateUtil.PATTERN_8));
            param.put("areaCode", area_button.getChoiceValue());
        }
        fillExtraParam(param);
        new LoadDataTask(this).execute(getQueryAction(), param);
    }

    protected void fillExtraParam(Map<String, String> param) {
    }

    @Override
    protected void bindData(JSONObject result) {
        if (result.optBoolean("flag")) {
            firstQuery = false;
            if (hasDateArea()) {
                renderButtons(result);
            }
            SimpleKpiDataModel model = buildDataModel(result);
            dataView.setModel(model);
            dataView.updateView(getDataAdapter(model, 1), getDataAdapter(model, 2));
            if (model.getData().size() == 0)
                showNoData();
        } else
            showToast(result.optString("msg"));
    }

    protected abstract SimpleKpiDataModel buildDataModel(JSONObject result);

    protected SimpleKpiDataAdapter getDataAdapter(SimpleKpiDataModel model, int type) {
        return new TableKpiDataAdapter(this, model, type);
    }

    protected class TableKpiDataAdapter extends SimpleKpiDataAdapter {
        public TableKpiDataAdapter(Context context, SimpleKpiDataModel dataModel, int type) {
            super(context, dataModel, type);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SimpleKpiDataRow v = (SimpleKpiDataRow) super.getView(position, convertView, parent);
            v.setColumnWidth(v.getResources().getDimensionPixelSize(R.dimen.kpi_data_col_width));
            return v;
        }
    }

    protected boolean hasDateArea() {
        return false;
    }

    protected void renderButtons(JSONObject result) {
    }

    protected abstract BaseConfigLoader getConfigLoader();

    protected abstract String getQueryAction();

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
