
package com.bonc.mobile.common.kpi;

import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.bonc.mobile.common.R;

public class KpiTitleRow extends SimpleKpiTitleRow {
    Context context;

    public KpiTitleRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public KpiTitleRow(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void buildTitleColumn(Map<String, String> item, int position) {
        TextView textView = getTextView(item, position);
        if (item.containsKey(KpiConstant.KEY_COLUMN_TYPE)) {
            int columnType = Integer.parseInt(item.get(KpiConstant.KEY_COLUMN_TYPE));
            switch (columnType) {
                case 0:// relation col
                    setColumnWidth(textView,
                            getResources().getDimensionPixelSize(R.dimen.kpi_rel_col_width));
                    break;
                case 1:// trend col
                    setColumnWidth(textView,
                            context.getResources().getDimensionPixelSize(R.dimen.kpi_trend_col_width));
                    break;
                default:// name col
                    setColumnWidth(textView, LayoutParams.MATCH_PARENT);
                    break;
            }
        } else {
            setColumnWidth(textView,
                    getResources().getDimensionPixelSize(R.dimen.kpi_data_col_width));
        }
        addView(textView);
    }
}
