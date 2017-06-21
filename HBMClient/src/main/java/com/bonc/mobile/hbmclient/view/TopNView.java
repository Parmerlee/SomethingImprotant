package com.bonc.mobile.hbmclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

/**
 * @author sunwei
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
		LayoutParams lp;
		if (getOrientation() == HORIZONTAL)
			lp = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
		else
			lp = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
		lp.setMargins(5, 0, 0, 5);
		for (int i = 0; i < adapter.getCount(); i++) {
			View child = adapter.getView(i, null, this);
			addView(child, lp);
		}
	}
}
