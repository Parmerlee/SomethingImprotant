package com.bonc.mobile.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import com.bonc.mobile.common.R;

/***
 * 时间轴
 * @author Lenevo
 *
 */
public class FlowView extends RelativeLayout {
    ButtonGroup button_group;
    public FlowView(Context context) {
        super(context);
        init(context);
    }

    public FlowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    void init(Context context){
        inflate(context, R.layout.flow_view, this);
        button_group = (ButtonGroup) findViewById(R.id.button_group);
    }
    
    public void setAdapter(ListAdapter adapter) {
        button_group.setAdapter(adapter);
    }
}
