package com.bonc.mobile.hbmclient.terminal.view;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.bonc.mobile.hbmclient.R;

public class StatisticsViewWide2 extends StatisticsViewLite {

	public StatisticsViewWide2(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public StatisticsViewWide2(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		setBaseID(R.layout.statistic_lite_wide2);
		inflate(getContext(), getBaseID(), this);
		setOrientation(VERTICAL);
		this.mKeyNames = new String[3];
		this.mKeyValues = new String[3];
		mKeyNameViews = new ArrayList<View>();
		mKeyValueViews = new ArrayList<View>();
		archiveViewsByTag(this);
	}

	public void setClassifierForSupport(String classifier) {
		for (View v : mKeyValueViews) {
			if (v instanceof NumberCounterViewMini2) {
				((NumberCounterViewMini2) v).setUnitString(classifier);
			}
		}

	}
}
