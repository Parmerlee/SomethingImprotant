package com.bonc.mobile.hbmclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * @author sunwei
 */
public class BoardHorizontalScrollView extends HorizontalScrollView {
	private OnBorderListener onBorderListener;

	public BoardHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setOnBorderListener(OnBorderListener onBorderListener) {
		this.onBorderListener = onBorderListener;
	}

	public interface OnBorderListener {
		public void onLeft();

		public void onNotBorder();

		public void onRight();
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (onBorderListener != null)
			doOnBorderListener();
	}

	private void doOnBorderListener() {
		View content = getChildAt(0);
		if (getScrollX() <= 5) {
			onBorderListener.onLeft();
		} else if (content != null && getScrollX() > 5
				&& content.getMeasuredWidth() > (getScrollX() + getWidth() + 5)) {
			onBorderListener.onNotBorder();
		} else if (content != null
				&& content.getMeasuredWidth() <= (getScrollX() + getWidth() + 5)) {
			onBorderListener.onRight();
		}
	}
}
