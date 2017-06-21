
package com.bonc.mobile.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

/**
 * @author sunwei
 */
public class ButtonGroup extends LinearLayout {
    public ButtonGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonGroup(Context context) {
        super(context);
    }

    public void setAdapter(ListAdapter adapter) {
        removeAllViews();
        if (adapter == null) {
            return;
        }
        for (int i = 0; i < adapter.getCount(); i++) {
            View child = adapter.getView(i, null, this);
            addView(child);
        }
    }
}
