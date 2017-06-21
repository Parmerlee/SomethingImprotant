
package com.bonc.mobile.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class LabelTextView extends TextView {
    String label;

    public LabelTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LabelTextView(Context context) {
        super(context);
        init();
    }

    void init() {
        label = getText().toString();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (text == null)
            text = "";
        if (label != null)
            text = label + text;
        super.setText(text, type);
    }

}
