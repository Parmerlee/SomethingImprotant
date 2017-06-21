/**
 * TODO
 */
package com.bonc.mobile.hbmclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * @author liweigao
 *
 */
public class BoardScrollView extends ScrollView {

	private OnBorderListener onBorderListener;
	// private OnTouchListener onBorderTouchListener;
	private View contentView;

	/**
	 * @param context
	 */
	public BoardScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BoardScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public BoardScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public View getContentView() {
		if (contentView == null) {
			contentView = getChildAt(0);
		}
		return this.contentView;
	}

	public void setOnBorderListener(final OnBorderListener onBorderListener) {
		this.onBorderListener = onBorderListener;
		if (onBorderListener == null) {
			// this.onBorderTouchListener = null;
			return;
		}

		if (contentView == null) {
			contentView = getChildAt(0);
		}
		/*
		 * this.onBorderTouchListener = new OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) { switch
		 * (event.getAction()) { case MotionEvent.ACTION_UP:
		 * doOnBorderListener(); break; } return false; }
		 * 
		 * }; super.setOnTouchListener(onBorderTouchListener);
		 */
	}

	/*
	 * @Override public void setOnTouchListener(final OnTouchListener l) {
	 * OnTouchListener onTouchListener = new OnTouchListener() {
	 * 
	 * @Override public boolean onTouch(View v, MotionEvent event) {
	 * 
	 * if (onBorderTouchListener != null) { onBorderTouchListener.onTouch(v,
	 * event); } return l.onTouch(v, event); }
	 * 
	 * }; super.setOnTouchListener(onTouchListener); }
	 */

	public interface OnBorderListener {
		public void onBottom();

		public void onNotBottom();

		public void onTop();
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		doOnBorderListener();
	}

	private void doOnBorderListener() {
		if (contentView != null
				&& contentView.getMeasuredHeight() <= (getScrollY()
						+ getHeight() + 10)) {
			if (onBorderListener != null) {
				onBorderListener.onBottom();
			}
		} else if (getScrollY() == 0) {
			if (onBorderListener != null) {
				onBorderListener.onTop();
			}
		} else if (contentView != null
				&& contentView.getMeasuredHeight() > (getScrollY()
						+ getHeight() + 10)) {
			if (onBorderListener != null) {
				onBorderListener.onNotBottom();
			}
		}
	}

}
