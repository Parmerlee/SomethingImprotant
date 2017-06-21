package com.bonc.mobile.hbmclient.terminal.view;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.bonc.mobile.hbmclient.R;

public class StatisticsViewWide_old extends StatisticsViewLite {

	public StatisticsViewWide_old(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public StatisticsViewWide_old(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		setBaseID(R.layout.statistic_lite_wide_old);
		inflate(getContext(), getBaseID(), this);
		setOrientation(VERTICAL);
		this.mKeyNames = new String[7];
		this.mKeyValues = new String[7];
		mKeyNameViews = new ArrayList<View>();
		mKeyValueViews = new ArrayList<View>();
		archiveViewsByTag(this);
	}
}
