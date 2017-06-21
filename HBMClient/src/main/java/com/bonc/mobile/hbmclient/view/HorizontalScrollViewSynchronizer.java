package com.bonc.mobile.hbmclient.view;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import android.view.View;

/**
 * @author sunwei
 *
 */
public class HorizontalScrollViewSynchronizer {
	List<LinkageHScrollView> viewList = new ArrayList<LinkageHScrollView>();
	View touchedView;

	public void addView(LinkageHScrollView view) {
		view.setOnTouchListener(mOnTouchListener);
		view.setOnLinkageScrollListener(mOnScrollListener);
		viewList.add(view);
	}

	View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			touchedView = v;
			return false;
		}

	};

	LinkageHScrollView.OnLinkageScrollListener mOnScrollListener = new LinkageHScrollView.OnLinkageScrollListener() {
		@Override
		public void onLinkageScroll(LinkageHScrollView view, int l, int t,
				int oldl, int oldt) {
			if (touchedView == null || view.getChildCount() == 0)
				return;
			if (view.equals(touchedView)) {
				for (LinkageHScrollView v : viewList) {
					if (!v.equals(view)) {
						v.scrollTo(l, t);
					}
				}
			}
		}

	};
}
