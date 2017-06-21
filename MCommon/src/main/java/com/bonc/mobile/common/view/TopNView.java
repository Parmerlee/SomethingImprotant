
package com.bonc.mobile.common.view;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.util.TextViewUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

/**
 * @author sunwei
 * 终端里的格子
 */
public class TopNView extends LinearLayout {
    public TopNView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopNView(Context context) {
        super(context);
    }

    public void setAdapter(ListAdapter adapter) {
        removeAllViews();
        if (adapter == null) {
            return;
        }
        for (int i = 0; i < adapter.getCount(); i++) {
            View child = adapter.getView(i, null, this);
            TextViewUtils.setText(child, R.id.rank, String.valueOf(i+1));
            addView(child);
        }
    }
}
