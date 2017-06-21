
package com.bonc.mobile.common.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

/**
 * @author sunwei
 * 仿Linearlayout的普通的横向listview
 */
public class SimpleListView extends LinearLayout {
    public SimpleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleListView(Context context) {
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

    public void setHighlight(int pos,int color) {
        List<View> l=new ArrayList<View>();
        for(int i=0;i<getChildCount();i++){
            View v=getChildAt(i);
            if(v.getId()!=99) l.add(v);
        }
        for(int i=0;i<l.size();i++){
            if(i==pos)l.get(i).setBackgroundColor(color);
            else l.get(i).setBackgroundResource(0);
        }
    }
}
