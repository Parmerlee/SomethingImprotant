package com.bonc.mobile.hbmclient.observer.listener;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class SimpleGestureListener extends SimpleOnGestureListener {
	private GestureFlingCallBack mGestureFlingCallBack;
	private int verticalMinDistance = 50;
	private int minVelocity = 5;

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		super.onLongPress(e);
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		if (Math.abs(distanceX) >= Math.abs(distanceY)) {
			if (Math.abs(distanceX) >= verticalMinDistance) {
				if (distanceX > 0) {
					this.mGestureFlingCallBack.onFlingLeft();
					return true;
				} else {
					this.mGestureFlingCallBack.onFlingRight();
					return true;
				}

			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		if (e1.getX() - e2.getX() > verticalMinDistance
				&& Math.abs(velocityX) > minVelocity) {
			this.mGestureFlingCallBack.onFlingLeft();
			return true;
		} else if (e2.getX() - e1.getX() > verticalMinDistance
				&& Math.abs(velocityX) > minVelocity) {
			this.mGestureFlingCallBack.onFlingRight();
			return true;
		}

		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		super.onShowPress(e);
	}

	public void setOnGestureFlingListener(GestureFlingCallBack gfcb) {
		this.mGestureFlingCallBack = gfcb;
	}

	public interface GestureFlingCallBack {
		void onFlingLeft();

		void onFlingRight();
	}

}
