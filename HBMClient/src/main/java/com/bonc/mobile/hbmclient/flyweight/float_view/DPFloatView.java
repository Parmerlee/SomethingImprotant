/**
 * 
 */
package com.bonc.mobile.hbmclient.flyweight.float_view;

import android.app.Application;
import android.graphics.PixelFormat;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.observer.data_presentation.SingleDPObservable;
import com.bonc.mobile.hbmclient.observer.data_presentation.SingleDPObserver;
import com.bonc.mobile.hbmclient.util.FirstScanUtil;

/**
 * @author liweigao
 * 
 */
public class DPFloatView implements FloatView {
	private Application mAPP;
	private String menuCode;
	private SingleDPObservable mObservable;
	private SingleDPObserver mObserver;

	private LayoutParams wmParams;
	private WindowManager mWindowManager;

	private ImageView mFloatView;
	private FirstScanUtil mFSU = FirstScanUtil.getSingleInstance();

	public DPFloatView(Application app) {
		this.mAPP = app;
		initialFloatView();
		this.mObservable = new SingleDPObservable();
		this.mObserver = new SingleDPObserver(mObservable, app, this.mFloatView);
	}

	private void initialFloatView() {
		// 获取的是WindowManagerImpl.CompatModeWrapper
		mWindowManager = (WindowManager) mAPP
				.getSystemService(Application.WINDOW_SERVICE);

		wmParams = new LayoutParams();
		// 设置window type
		wmParams.type = LayoutParams.TYPE_PHONE;
		// 设置图片格式，效果为背景透明
		wmParams.format = PixelFormat.RGBA_8888;
		// 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
		wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;

		// 设置悬浮窗口长宽数据
		wmParams.width = LayoutParams.WRAP_CONTENT;
		wmParams.height = LayoutParams.WRAP_CONTENT;
		// 调整悬浮窗显示的停靠位置为左侧置顶
		wmParams.gravity = Gravity.LEFT | Gravity.TOP;

		this.mFloatView = new ImageView(mAPP);
		this.mFloatView.setImageResource(R.mipmap.icon_float_view);
		this.mFloatView.setClickable(true);
		this.mFloatView.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

		final GestureDetector gd = new GestureDetector(mAPP,
				new GestureDetector.OnGestureListener() {

					@Override
					public boolean onSingleTapUp(MotionEvent e) {
						// TODO Auto-generated method stub
						onClickFloatView();
						return true;
					}

					@Override
					public void onShowPress(MotionEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public boolean onScroll(MotionEvent e1, MotionEvent e2,
							float distanceX, float distanceY) {
						wmParams.x = (int) e2.getRawX()
								- mFloatView.getMeasuredWidth() / 2;
						// 减25为状态栏的高度
						wmParams.y = (int) e2.getRawY()
								- mFloatView.getMeasuredHeight() / 2 - 25;
						// 刷新
						mWindowManager.updateViewLayout(mFloatView, wmParams);
						return true;
					}

					@Override
					public void onLongPress(MotionEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public boolean onDown(MotionEvent e) {
						// TODO Auto-generated method stub
						return false;
					}
				});
		this.mFloatView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return gd.onTouchEvent(event);
			}
		});
	}

	private void onClickFloatView() {
		mFloatView.setVisibility(View.INVISIBLE);
		mObservable.setMenuCode(menuCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.flyweight.float_view.FloatView#removeFloatView
	 * ()
	 */
	@Override
	public void removeFloatView() {
		// TODO Auto-generated method stub
		if (mWindowManager != null && mFloatView != null) {
			// 移除悬浮窗口
			ViewParent parent = mFloatView.getParent();
			if (parent != null) {
				mWindowManager.removeView(mFloatView);
				this.mFloatView = null;
				// this.mFloatView.setVisibility(View.VISIBLE);
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.flyweight.float_view.FloatView#showFloatView
	 * (com.bonc.mobile.hbmclient.flyweight.float_view.Position)
	 */
	@Override
	public void showFloatView(String menuCode) {
		// TODO Auto-generated method stub
		if (this.mWindowManager != null && this.mFloatView != null
				&& this.wmParams != null) {
			this.menuCode = menuCode;
			initialPosition(menuCode);
			ViewParent parent = this.mFloatView.getParent();
			if (parent != null) {
				this.mWindowManager.removeViewImmediate(mFloatView);
			}

			this.mWindowManager.addView(this.mFloatView, this.wmParams);

			if (mFSU.isFirstScanHeavy(mAPP, menuCode)) {
				onClickFloatView();
			} else {

			}
		}
	}

	private void initialPosition(String code) {
		int width = mWindowManager.getDefaultDisplay().getWidth();
		wmParams.x = width;
		wmParams.y = 200;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.flyweight.float_view.FloatView#hideFloatView()
	 */
	@Override
	public void hideFloatView() {
		// TODO Auto-generated method stub
		if (this.mObserver.isDialogShowing()) {
			this.mObserver.hideView();
			this.mFloatView.setVisibility(View.INVISIBLE);
		} else {
			this.mFloatView.setVisibility(View.INVISIBLE);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.flyweight.float_view.FloatView#unHideFloatView
	 * () 可以采用备忘录模式，记录一下前一次的状态，然后恢复成前一次状态就行
	 */
	@Override
	public void unHideFloatView() {
		// TODO Auto-generated method stub
		if (this.mObserver.isDialogShowing()) {
			this.mFloatView.setVisibility(View.INVISIBLE);
			this.mObserver.unHideView();
		} else {
			if (mFSU.isFirstScan()) {

			} else {
				this.mFloatView.setVisibility(View.VISIBLE);
			}
		}
	}

}
