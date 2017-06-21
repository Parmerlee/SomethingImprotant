package com.bonc.mobile.hbmclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * @author liweigao
 *
 */
public class LinkageHScrollView extends HorizontalScrollView {

	private OnLinkageScrollListener listener;

	/**
	 * @param context
	 */
	public LinkageHScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public LinkageHScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public LinkageHScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void setOnLinkageScrollListener(
			final OnLinkageScrollListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (listener != null) {
			this.listener.onLinkageScroll(this, x, y, oldx, oldy);
		}
	}

	public interface OnLinkageScrollListener {
		public void onLinkageScroll(LinkageHScrollView view, int x, int y,
									int oldx, int oldy);
	}

}
