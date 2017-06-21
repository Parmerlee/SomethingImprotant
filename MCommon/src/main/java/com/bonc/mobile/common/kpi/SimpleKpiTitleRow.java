
package com.bonc.mobile.common.kpi;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonc.mobile.common.R;

public class SimpleKpiTitleRow extends LinearLayout {
    public static final int SORT_NO = 0;
    public static final int SORT_UP = 1;// 升序
    public static final int SORT_DOWN = 2;// 降序

    int columnWidth;
    boolean sortEnabled = false;
    int sortIndex = -1, sortDirection = SORT_NO;
    OnColumnSortListener sortListener;

    public SimpleKpiTitleRow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleKpiTitleRow(Context context) {
        super(context);
    }

    public void setOnColumnSortListener(OnColumnSortListener listener) {
        sortListener = listener;
        sortEnabled = true;
    }

    public void setTextAppearance(int resid) {
        for (int i = 0; i < getChildCount(); i++) {
            ((TextView) getChildAt(i)).setTextAppearance(getContext(), resid);
        }
    }

    public void buildTitleRow(List<Map<String, String>> columnInfo) {
        buildTitleRow(columnInfo, -1);
    }

    public void buildTitleRow(List<Map<String, String>> columnInfo, int columnWidth) {
        this.columnWidth = columnWidth;
        removeAllViews();
        for (int i = 0; i < columnInfo.size(); i++) {
            Map<String, String> item = columnInfo.get(i);
            buildTitleColumn(item, i);
        }
    }

    protected void buildTitleColumn(Map<String, String> item, int position) {
        TextView textView = getTextView(item, position);
        addView(textView);
    }

    protected String getColumnTitle(Map<String, String> item) {
        String title = item.get(KpiConstant.KEY_RELATION_KPIVALUE_COLUMN_NAME);
        if (title == null)
            title = item.get("COLUMN_NAME");
        return title;
    }

    protected TextView getTextView() {
        TextView tv = new TextView(getContext());
        tv.setGravity(Gravity.CENTER);
        tv.setTextAppearance(getContext(), R.style.mainkpi_title_style);
        setColumnWidth(tv, columnWidth);
        return tv;
    }

    protected TextView getTextView(Map<String, String> item, int position) {
        TextView tv = getTextView();
        tv.setText(getColumnTitle(item));
        tv.setId(position);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sortEnabled)
                    return;
                if (v.getId() != sortIndex && sortIndex != -1)
                    setSortIcon(findViewById(sortIndex), SORT_NO);
                sortIndex = v.getId();
                sortDirection = sortDirection == SORT_UP ? SORT_DOWN : SORT_UP;
                setSortIcon(v, sortDirection);
                if (sortListener != null)
                    sortListener.onColumnSort(sortIndex, sortDirection);
            }
        });
        return tv;
    }

    protected void setColumnWidth(View tv, int w) {
        if (w == -1)
            tv.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
        else
            tv.setLayoutParams(new LayoutParams(w, LayoutParams.MATCH_PARENT));
    }

    protected void setSortIcon(View v, int direction) {
        TextView tv = (TextView) v;
        int right = 0;
        switch (direction) {
            case SORT_UP:
                right = R.mipmap.triangle_upward;
                break;
            case SORT_DOWN:
                right = R.mipmap.triangle_downward;
                break;
        }
        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, right, 0);
    }

    public interface OnColumnSortListener {
        public void onColumnSort(int index, int direction);
    }
}
