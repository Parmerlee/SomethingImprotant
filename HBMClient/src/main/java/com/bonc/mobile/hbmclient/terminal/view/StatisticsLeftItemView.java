package com.bonc.mobile.hbmclient.terminal.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;

public class StatisticsLeftItemView extends LinearLayout {
	private TextView mTextView;

	public StatisticsLeftItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		// inflate(getContext(), R.layout.statistics_left_item, this);
		inflate(getContext(), R.layout.kpi_area_left, this);
		mTextView = (TextView) findViewById(R.id.zhibiao_left_1);
	}

	public void setText(String s) {
		mTextView.setText(s);
		if (s.length() >= 4) {
			mTextView.setSingleLine(false);
		}
	}

	public String getText() {
		return (String) mTextView.getText();
	}
}
