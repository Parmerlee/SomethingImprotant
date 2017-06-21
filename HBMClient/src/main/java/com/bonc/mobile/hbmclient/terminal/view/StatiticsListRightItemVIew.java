package com.bonc.mobile.hbmclient.terminal.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;

public class StatiticsListRightItemVIew extends LinearLayout {
	private TextView mContentView;
	private TextView mUnitTextView;
	private Context context;

	public StatiticsListRightItemVIew(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		inflate(getContext(), R.layout.statistics_list_right_item, this);
		mContentView = (TextView) findViewById(R.id.statistics_right_item_content_tv);
		mUnitTextView = (TextView) findViewById(R.id.statistics_right_item_unit_tv);
	}

	public void setContentText(String s) {
		mContentView.setText(s);
	}

	public void setUnitText(String s) {
		mUnitTextView.setText(s);
	}

	public void setContentColor(int color) {
		mContentView.setTextColor(color);
	}

	public void setContentColorID(int color) {
		mContentView.setTextColor(context.getResources().getColor(color));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		getLayoutParams().height = LayoutParams.MATCH_PARENT;
		getChildAt(0).getLayoutParams().height = LayoutParams.MATCH_PARENT;
	}
}
