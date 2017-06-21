/**
 * TODO
 */
package com.bonc.mobile.hbmclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * @author liweigao
 * 
 */
public class ScrollViewGroup extends ViewGroup {
	private static final String TAG = "ScrollViewGroup";
	private Scroller mScroller;
	/**
	 * 当前页面号
	 */
	private int mCurScreen;
	private int mDefaultScreen = 0;
	private float mLastMotionX;
	private float mLastMotionY;
	private static boolean disallowIntercept = false;
	/**
	 * 用于返回当前页号，和需要移动到的页号
	 */
	private OnScreenChanageCallBack occb;
	private boolean isHaveScroll = false;

	public ScrollViewGroup(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ScrollViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScroller = new Scroller(context);
		// 默认显示第一屏
		mCurScreen = mDefaultScreen;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childLeft = 0;
		final int childCount = getChildCount();

		for (int i = 0; i < childCount; i++) {
			final View childView = getChildAt(i);
			if (childView.getVisibility() != View.GONE) {
				final int childWidth = childView.getMeasuredWidth();
				childView.layout(childLeft, 0, childLeft + childWidth,
						childView.getMeasuredHeight());
				childLeft += childWidth;
				// 很明显 0 - 320 | 320 - 640 | 640 - 960 ...（假设屏幕宽320）
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
		// x坐标 y坐标
		// 移动到第几屏
		scrollTo(mCurScreen * width, 0);
	}

	/**
	 * According to the position of current layout scroll to the destination
	 * page. 判断滑动的位置 如果大于当前屏中间的位置 则换屏 否则 仍然是这个屏 getScrollX x方向的偏移量
	 */

	public void snapToDestination() {
		final int screenWidth = getWidth();
		final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
		if (occb != null) {
			occb.glideNext(destScreen);
		}
	}

	public void snapToScreen(int whichScreen) {
		// get the valid layout page
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		if (getScrollX() != (whichScreen * getWidth())) {

			final int delta = whichScreen * getWidth() - getScrollX();
			// 开始滚动
			// x，y，x方向移动量，y方向移动量，滚动持续时间，负数往左滚
			int speed = Math.abs(delta) * 2;// 根据页面的距离来确定动画时间
			if (speed > 960) {
				speed = 350;
			}
			mScroller.startScroll(getScrollX(), 0, delta, 0, speed);
			mCurScreen = whichScreen;
			if (occb != null) {
				occb.onScreenChanage(mCurScreen);
			}
			invalidate();
		}
	}

	public void setOnScreenChangeCallBack(OnScreenChanageCallBack occb) {
		this.occb = occb;
	}

	public void setToScreen(int whichScreen) {
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		mCurScreen = whichScreen;

		scrollTo(whichScreen * getWidth(), 0);
	}

	public int getCurScreen() {
		return mCurScreen;
	}

	public int getScreenCount() {
		return getChildCount();
	}

	// 只有当前LAYOUT中的某个CHILD导致SCROLL发生滚动，才会致使自己的COMPUTESCROLL被调用
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		// 如果返回true，表示动画还没有结束
		// 因为前面startScroll，所以只有在startScroll完成时 才会为false
		if (mScroller.computeScrollOffset()) {
			// 产生了动画效果 每次滚动一点
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// 将事件优先交由 本类的onTouchEvent 处理
		if (!onTouchEvent(ev)) {
			return super.dispatchTouchEvent(ev);
		}

		return true;

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// mGestureDetector.onTouchEvent(ev);
		final int action = ev.getAction();
		final float x = ev.getX();
		final float y = ev.getY();

		boolean flag = false;

		switch (action) {
		case MotionEvent.ACTION_DOWN:// 按下
			// 如果屏幕的滚动动画没有结束 你就按下了 就结束动画
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			mLastMotionY = y;
			break;

		case MotionEvent.ACTION_MOVE:
			if (!disallowIntercept) { // 子view是否禁止了父view的拦截操作
				if (Math.abs(x - mLastMotionX) > 200) {

					// 在这里需要添加左右滑动的切换view的代码
					if (x - mLastMotionX > 0 && mCurScreen > 0 && !isHaveScroll) {
						mCurScreen--;
						if (occb != null)
							occb.glideNext(mCurScreen);
						isHaveScroll = true;
					} else if (x - mLastMotionX < 0 && mCurScreen < 3
							&& !isHaveScroll) {
						mCurScreen++;
						if (occb != null)
							occb.glideNext(mCurScreen);
						isHaveScroll = true;
					}

					flag = true;
				} else if (Math.abs(y - mLastMotionY) > 100
						|| Math.abs(y - mLastMotionY) < 10) {
					flag = false;
				} else {
					flag = true;
				}
			} else { // 如果禁止了 则事件直接分发.
				flag = false;
			}
			break;

		case MotionEvent.ACTION_UP:

			// 处理点击事件.
			if ((Math.abs(y - mLastMotionY) < 10 && Math.abs(x - mLastMotionX) < 10)
					|| isDisallowIntercept()
					|| Math.abs(y - mLastMotionY) > 100) {
				flag = false;
			} else {
				flag = true;
			}

			// // 事件完成时 将标志初始化.供下一次使用。
			// setDisallowIntercept(false);
			// 初始化为未滑动
			isHaveScroll = false;

		}

		return flag;

	}

	public void moveToView(int positon) {
		snapToScreen(positon);
	}

	public interface OnScreenChanageCallBack {
		void onScreenChanage(int page);

		void glideNext(int page);
	}

	public static boolean isDisallowIntercept() {
		return disallowIntercept;
	}

	public static void setDisallowIntercept(boolean disallowIntercept) {
		ScrollViewGroup.disallowIntercept = disallowIntercept;
	}

}
