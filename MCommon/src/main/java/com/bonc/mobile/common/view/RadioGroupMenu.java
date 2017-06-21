
package com.bonc.mobile.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.RadioGroup;

public class RadioGroupMenu extends RadioGroup {
    public RadioGroupMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadioGroupMenu(Context context) {
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
