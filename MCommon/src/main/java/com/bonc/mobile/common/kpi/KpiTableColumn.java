
package com.bonc.mobile.common.kpi;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.kpi.ColumnDataFilter.ColumnDisplyInfo;
import com.bonc.mobile.common.util.StringUtil;
import com.bonc.mobile.common.util.TextViewUtils;

public abstract class KpiTableColumn {
    public static final int COLUMN_TYPE_NAME = 0;
    public static final int COLUMN_TYPE_DATA = 1;
    public static final int COLUMN_TYPE_TREND = 2;
    public static final int COLUMN_TYPE_RELATION = 3;

    protected Context context;
    protected View containerView;
    protected LayoutInflater inflater;
    protected int columnType;

    public KpiTableColumn(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public abstract void build(ViewGroup parent);

    public View getView() {
        return containerView;
    }

    public void setOnClickListener(final String rowKey, final KpiDataModel dataModel,
            final KpiTableColumnClickListener listener) {
        containerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(v, rowKey, columnType, dataModel);
            }
        });
    }

    public void setOnLongClickListener(final String rowKey, final KpiDataModel dataModel,
            final KpiTableColumnClickListener listener) {
        containerView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null)
                    listener.onLongClick(v, rowKey, columnType, dataModel);
                return true;
            }
        });
    }

    public void setOnTouchListener(final String rowKey, final KpiDataModel dataModel,
            final KpiTableColumnClickListener listener) {
        containerView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (listener != null)
                    return listener.onTouch(v, event, rowKey, columnType, dataModel);
                return false;
            }
        });
    }
}

class KpiNameColumn extends KpiTableColumn {

    public KpiNameColumn(Context context) {
        super(context);
        columnType = COLUMN_TYPE_NAME;
    }

    @Override
    public void build(ViewGroup parent) {
        containerView = inflater.inflate(R.layout.kpi_name_column, parent, false);
    }

    public void setText(String s) {
        TextViewUtils.setText(containerView, R.id.text, s);
    }

    public void setTextAppearance(int resid) {
        TextViewUtils.setTextAppearance(containerView, R.id.text, resid);
    }

    public void setTextAppearance(String level) {
        setTextAppearance("2".equals(level) ? R.style.kpi_second_level_style
                : R.style.kpi_first_level_style);
    }
}

class KpiRelationColumn extends KpiTableColumn {

    public KpiRelationColumn(Context context) {
        super(context);
        columnType = COLUMN_TYPE_RELATION;
    }

    @Override
    public void build(ViewGroup parent) {

    }
}

class KpiTrendColumn extends KpiTableColumn {
    public KpiTrendColumn(Context context) {
        super(context);
        columnType = COLUMN_TYPE_TREND;
    }

    @Override
    public void build(ViewGroup parent) {
        containerView = inflater.inflate(R.layout.kpi_trend_column, parent, false);
    }

    public void setData(List<String> trendData) {
        ViewGroup vg = (ViewGroup) containerView;
        vg.removeAllViews();
        if (trendData == null)
            return;
        TrendViewBuilder builder = new TrendViewBuilder();
        builder.setData(trendData);
        vg.addView(builder.buildView(context));
    }
}

class KpiDataColumn extends KpiTableColumn {
    String colKey, kpiRule, kpiUnit;

    public KpiDataColumn(Context context, String colKey) {
        super(context);
        this.colKey = colKey;
        columnType = COLUMN_TYPE_DATA;
    }

    @Override
    public void build(ViewGroup parent) {
        containerView = inflater.inflate(R.layout.kpi_data_column, parent, false);
    }

    public void loadRuleUnit(Map<String, String> colInfo, Map<String, String> kpiInfo) {
        kpiRule = colInfo.get(KpiConstant.KEY_KPIVALUE_RULE);
        kpiUnit = colInfo.get(KpiConstant.KEY_KPIVALUE_UNIT);
        if (KpiConstant.KPI_RULE_STRING.equals(kpiInfo.get(KpiConstant.KEY_KPI_CAL_RULE))) {
            kpiRule = kpiInfo.get(KpiConstant.KEY_KPI_CAL_RULE);
            kpiUnit = "";
        } else if ("-1".equals(kpiRule)) {
            kpiRule = kpiInfo.get(KpiConstant.KEY_KPI_CAL_RULE);
            kpiUnit = kpiInfo.get(KpiConstant.KEY_KPI_UNIT);
        }
    }

    @SuppressLint("ResourceAsColor")
    public void setData(Map<String, String> data) {
        if (data == null) {
            TextViewUtils.setText(containerView, R.id.data_column_content, StringUtil.NULL_DEF);
            TextViewUtils.setText(containerView, R.id.data_column_unit, StringUtil.NULL_DEF);
            TextViewUtils.setTextColor(containerView, R.id.data_column_content,
                    R.color.default_color);
        } else {
            String value = data.get(colKey);
            ColumnDisplyInfo cdi = ColumnDataFilter.getInstance().filterWithDefaultValue(kpiRule,
                    kpiUnit, value);
            TextViewUtils.setText(containerView, R.id.data_column_content, cdi.getValue());
            TextViewUtils.setText(containerView, R.id.data_column_unit, cdi.getUnit());
            TextViewUtils.setTextColor(containerView, R.id.data_column_content, cdi.getColor());
            int padding = KpiConstant.KPI_RULE_STRING.equals(kpiRule) ? 0 : containerView
                    .getResources().getDimensionPixelSize(R.dimen.kpi_data_padding);
            containerView.findViewById(R.id.data_column_content).setPadding(0, 0, padding, 0);
        }
    }
}
