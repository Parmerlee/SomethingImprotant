package com.bonc.mobile.hbmclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * @author sunwei
 */
public class MultiScrollView extends ScrollView {
	GestureDetector detector;

	public MultiScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		detector = new GestureDetector(context, new YScrollDetector());
	}

	public MultiScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		detector = new GestureDetector(context, new YScrollDetector());
	}

	public MultiScrollView(Context context) {
		super(context);
		detector = new GestureDetector(context, new YScrollDetector());
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		boolean b1 = super.onInterceptTouchEvent(ev);
		boolean b2 = detector.onTouchEvent(ev);
		return b1 && b2;
	}

	class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return Math.abs(distanceY) > Math.abs(distanceX) ? true : false;
		}
	}
}
