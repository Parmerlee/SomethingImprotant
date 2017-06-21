
package com.bonc.mobile.common.kpi;

import android.view.MotionEvent;
import android.view.View;

public interface KpiTableColumnClickListener {
    void onClick(View v, String rowKey, int columnType, KpiDataModel dataModel);

    void onLongClick(View v, String rowKey, int columnType, KpiDataModel dataModel);

    boolean onTouch(View v, MotionEvent event, String rowKey, int columnType, KpiDataModel dataModel);
}
