package com.bonc.mobile.hbmclient.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.bonc.mobile.hbmclient.util.LogUtil;

/**
 * 粘滞水平滚动视图。目前的代码架构决定了它支持横向在页间推送的效果。 默认情况下它会把所有childView都修正成和屏幕同样宽
 * */
public class StickyHorizontalScrollView extends LinearLayout {
	private Scroller mScroller;

	/** 滚动监听器成员变量 */
	private OnAutoScrollListener mScrollEndListener;

	/** 滑动速度计算器 */
	private VelocityTracker mVelocityTracker;

	/** 子View坐标位置集合 */
	private List<Integer> mXPositions = new ArrayList<Integer>();

	private float mLastMotionX;
	private float mLastDownX;
	private int mCurrentPage;
	private float mLastMotionY;
	private int mRightBorderX;
	private float mTouchDownX;
	private boolean mBeingDraged;

	final static public int SNAP_VELOCITY = 1000;
	final static public int AUTO_SCROLL_INTERRUPTED = -1;

	public StickyHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public StickyHorizontalScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public void init() {
		mScroller = new Scroller(getContext());
		mScrollEndListener = null;
	}

	/**
	 * 为这个StickyHorizontalScrollView设定一个滚动结束监听器，它将在惯性滚动结束时瞬间触发
	 * 警告：目前onAutoScrollEnd工作得不是非常好
	 * 
	 * @param l
	 *            注意最好不要传入一个非匿名的类
	 * */
	public void setOnAutoScrollListener(OnAutoScrollListener l) {
		mScrollEndListener = l;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (mVelocityTracker == null) {
			// 用来追踪触摸事件（flinging事件和其他手势事件）的速率。用obtain()函数来获得类的实例
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);
		final float x = ev.getX();

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mCurrentPage = getStickyPageIndex();
			mTouchDownX = ev.getX();
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaX = (int) (mLastMotionX - x);
			// 在当前视图内容继续偏移(x , y)个单位，显示(可视)区域也跟着偏移(x,y)个单位
			LogUtil.info("view scroll distance is isisis", mLastMotionX + " "
					+ deltaX);
			scrollBy(deltaX, 0);
			mLastMotionX = x;
			break;
		case MotionEvent.ACTION_UP:
			float touchDistance = mTouchDownX - ev.getX();
			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(1000);
			int velocityX = (int) velocityTracker.getXVelocity();
			int targetDX = (int) (getStickyPageX() - getScrollX());
			if (mScrollEndListener != null) {
				mScrollEndListener.onAutoScrollStart(ev, velocityX, 0,
						getScrollX(), getScrollY());
			}
			if (velocityX > SNAP_VELOCITY
					&& touchDistance > 0 - getMeasuredWidth() / 2) {
				targetDX -= getMeasuredWidth();
			} else if (velocityX < -SNAP_VELOCITY
					&& touchDistance < getMeasuredWidth() / 2) {
				targetDX += getMeasuredWidth();
			}
			if (getScrollX() + targetDX > mRightBorderX) {
				targetDX = mRightBorderX - getScrollX();
			}
			if (getScrollX() + targetDX < 0) {
				targetDX = 0 - getScrollX();
			}
			mScroller.startScroll(getScrollX(), 0, targetDX, 0, 500);
			invalidate();
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			mTouchDownX = ev.getX();
			break;
		case MotionEvent.ACTION_CANCEL:
			mLastMotionX = x;
			break;
		default:
			break;
		}
		mTouchDownX = -1;
		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		final ViewConfiguration configuration = ViewConfiguration
				.get(getContext());
		if ((ev.getAction() == MotionEvent.ACTION_MOVE) && (mBeingDraged)) {
			return true;
		}
		switch (ev.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_MOVE:
			float x = ev.getX();
			final int xDiff = (int) Math.abs(x - mLastDownX);
			LogUtil.info(
					"xDiff",
					"xDiff is " + xDiff + " "
							+ configuration.getScaledTouchSlop());
			if (xDiff > configuration.getScaledTouchSlop()) {// 灵敏度控制，一旦滑动启动就强制接受事件了

				mBeingDraged = true;
				mLastMotionX = x;
				if (getParent() != null)
					getParent().requestDisallowInterceptTouchEvent(true);
			} else {
				mBeingDraged = false;
				// mLastMotionX = x;
			}
			break;
		case MotionEvent.ACTION_DOWN:
			x = ev.getX();
			mLastDownX = x;
			/*
			 * Remember location of down touch. ACTION_DOWN always refers to
			 * pointer index 0.
			 */
			mLastMotionX = x;
			mBeingDraged = false;// mScroller.isFinished();
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			/* Release the drag */
			mBeingDraged = false;
			if (getParent() != null)
				getParent().requestDisallowInterceptTouchEvent(false);
			break;
		default:
			break;
		}
		// if (Math.abs(mLastMotionY - ev.getY()) < 5) {
		// return true;
		// }
		mLastMotionY = ev.getY();
		LogUtil.info("mBeingDraged", "mBeingDraged " + mBeingDraged + "");
		return mBeingDraged;
	}

	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}

	}

	public int getStickyPageIndex() {
		if (getScrollX() < 0) {
			return 0;
		}
		for (int i = 0; i < mXPositions.size(); i++) {
			if (getScrollX() < mXPositions.get(i)) {
				int distance1 = getScrollX() - mXPositions.get(i - 1);
				int distance2 = mXPositions.get(i) - getScrollX();
				if (distance1 < distance2) {
					return i - 1;
				} else {
					return i;
				}
			}
		}
		return mXPositions.size() - 1;
	}

	public int getStickyPageX() {
		if (getScrollX() < 0) {
			return 0;
		}
		for (int i = 1; i < mXPositions.size(); i++) {
			if (getScrollX() < mXPositions.get(i)) {
				int distance1 = getScrollX() - mXPositions.get(i - 1);
				int distance2 = mXPositions.get(i) - getScrollX();
				if (distance1 < distance2) {
					return mXPositions.get(i - 1);
				} else {
					return mXPositions.get(i);
				}
			}
		}
		return mXPositions.get(mXPositions.size() - 1);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		mXPositions.clear();
		int childLeft = 0;
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View childView = getChildAt(i);
			mXPositions.add(childLeft);
			if (childView.getVisibility() != View.GONE) {
				final int childWidth = childView.getMeasuredWidth();
				childView.layout(childLeft, 0, childLeft + childWidth,
						childView.getMeasuredHeight());
				mRightBorderX = childLeft;
				childLeft += childWidth;

			}

		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		if (widthMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException(
					"ScrollLayout only canmCurScreen run at EXACTLY mode!");
		}
		/**
		 * wrap_content 传进去的是AT_MOST 代表的是最大尺寸 固定数值或fill_parent 传入的模式是EXACTLY
		 * 代表的是精确的尺寸
		 */
		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (heightMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException(
					"ScrollLayout only can run at EXACTLY mode!");
		}

		// The children are given the same width and height as the scrollLayout
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		/* 以下判断滚动结束时所处的页数 */
		if (mScroller.isFinished()) {
			LogUtil.info("finished", l + " " + t);
		}
		if (mScroller.isFinished() && mScrollEndListener != null
				&& mTouchDownX != -1) {
			int i;
			for (i = 0; i < mXPositions.size(); i++) {
				if (i == mXPositions.size() - 1 && mXPositions.get(i) != l) {
					i = -1;
					break;
				} else if (mXPositions.get(i) == l)
					break;
			}
			LogUtil.info("mScrollEndListener be called", l + " " + t);
			mScrollEndListener.onAutoScrollEnd(l, t, i);
		}
	}

	/**
	 * 滚动结束监听器，在<u>自动</u>滑动动画完成时调用(若滚动被打断则不会调用)，参见<a
	 * href="#setOnScrollEndListener">setOnScrollEndListener</a>获取更多信息
	 */
	public interface OnAutoScrollListener {
		/**
		 * 在滚动结束时调用以获取当前页数，坐标等等
		 * 
		 * @param x
		 *            滚动结束时的x坐标
		 * @param y
		 *            滚动结束时的Y坐标，在当前类中恒为0
		 * @param index
		 *            结束时所在页数或视图数
		 */
		@Deprecated
		void onAutoScrollEnd(int x, int y, int index);

		/**
		 * 在滚动开始时调用以获取当前触摸事件，滚动坐标等等
		 * 
		 * @param ev
		 *            滚动开始时瞬间触摸事件
		 * @param vx
		 *            x方向瞬间速度
		 * @param vy
		 *            y方向瞬间速度
		 * @param l
		 *            滚动开始时的Y坐标，在当前类中恒为0
		 * @param d
		 *            开始时所在页数或视图数
		 */
		void onAutoScrollStart(MotionEvent ev, int vx, int vy, int l, int d);
	}

	private boolean inChild(int x, int y) {
		if (getChildCount() > 0) {
			final int scrollX = getScrollX();
			final View child = getChildAt(0);
			return !(y < child.getTop() || y >= child.getBottom()
					|| x < child.getLeft() - scrollX || x >= child.getRight()
					- scrollX);
		}
		return false;
	}

}
