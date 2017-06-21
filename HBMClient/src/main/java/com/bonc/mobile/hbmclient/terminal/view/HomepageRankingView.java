package com.bonc.mobile.hbmclient.terminal.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;

import com.bonc.mobile.hbmclient.R;

public class HomepageRankingView extends HorizontalScrollView {
	ViewGroup vg;

	public HomepageRankingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public HomepageRankingView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {
		inflate(getContext(), R.layout.home_rank_layout, this);
		WindowManager wManager = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		int width = wManager.getDefaultDisplay().getWidth();
		vg = (ViewGroup) findViewById(R.id.ll_view);
		setHorizontalFadingEdgeEnabled(false);
		setClickable(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (vg.getChildCount() == 0) {
			Top10View top10View = new Top10View(getContext(), getWidth());
			vg.addView(top10View.getView());
		}
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		// TODO Auto-generated method stub
		vg.setOnClickListener(l);
	}
}
