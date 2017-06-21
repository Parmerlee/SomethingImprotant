package com.bonc.mobile.hbmclient.terminal.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bonc.mobile.hbmclient.util.LogUtil;

public class MultiScrollLayout extends LinearLayout {
	public static final int MOVE_DISTANCE = 20;

	private List<View> mHoldedViews;
	private float lastX = -100f, lastY = -100f;
	private float lastDownX = 0;
	private float lastDownY = 0;
	private ListView ls1 = null;
	private ListView ls2 = null;

	public MultiScrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub)
	}

	public MultiScrollLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public boolean addHoldedViewId(int aId) {
		View v = findViewById(aId);
		if (v != null) {
			if (mHoldedViews == null)
				mHoldedViews = new ArrayList<View>();
			mHoldedViews.add(v);
			return true;
		} else {
			return false;
		}
	}

	public boolean setHoldedViews(int[] aIds) {
		ArrayList<View> holded = new ArrayList<View>();
		for (Integer id : aIds) {
			View v = findViewById(id);
			if (v instanceof AbsListView) {
				if (ls1 == null) {
					ls1 = (ListView) v;
				} else if (ls1 != null && ls2 == null) {
					ls2 = (ListView) v;
					ls2.setOnScrollListener(new OnScrollListener() {
						@Override
						public void onScrollStateChanged(AbsListView view,
								int scrollState) {
							// 如果已经停止滑动
							if (scrollState == 0 || scrollState == 1) {
								// 获得第一个子view
								View subView = view.getChildAt(0);

								if (subView != null) {
									final int top = subView.getTop();
									final int position = view
											.getFirstVisiblePosition();
									// 如果两个首个显示的子view高度不等
									ls1.setSelectionFromTop(position, top);
									ls2.setSelectionFromTop(position, top);
								}
							}

						}

						@Override
						public void onScroll(AbsListView view,
								final int firstVisibleItem,
								int visibleItemCount, int totalItemCount) {
							// View subView = view.getChildAt(0);
							// if(subView != null){
							// final int top = subView.getTop();
							//
							// //
							// listView1.setSelectionFromTop(firstVisibleItem,
							// top);
							// //
							// listView2.setSelectionFromTop(firstVisibleItem,
							// top);
							//
							// int top1 = ls1.getChildAt(0).getTop();
							// if(!(top1 - 7 < top &&top < top1 + 7)){
							// ls1.setSelectionFromTop(firstVisibleItem, top);
							// ls2.setSelectionFromTop(firstVisibleItem, top);
							// }
							// }
						}
					});
					ls1.setOnScrollListener(new OnScrollListener() {
						@Override
						public void onScrollStateChanged(AbsListView view,
								int scrollState) {
							// 如果已经停止滑动
							if (scrollState == 0 || scrollState == 1) {
								// 获得第一个子view
								View subView = view.getChildAt(0);

								if (subView != null) {
									final int top = subView.getTop();
									final int position = view
											.getFirstVisiblePosition();
									// 如果两个首个显示的子view高度不等
									ls1.setSelectionFromTop(position, top);
									ls2.setSelectionFromTop(position, top);
								}
							}

						}

						@Override
						public void onScroll(AbsListView view,
								final int firstVisibleItem,
								int visibleItemCount, int totalItemCount) {
							// View subView = view.getChildAt(0);
							// if(subView != null){
							// final int top = subView.getTop();
							//
							// //
							// listView1.setSelectionFromTop(firstVisibleItem,
							// top);
							// //
							// listView2.setSelectionFromTop(firstVisibleItem,
							// top);
							//
							// int top1 = ls1.getChildAt(0).getTop();
							// if(!(top1 - 7 < top &&top < top1 + 7)){
							// ls1.setSelectionFromTop(firstVisibleItem, top);
							// ls2.setSelectionFromTop(firstVisibleItem, top);
							// }
							// }
						}
					});
				}
			}
			if (v == null) {
				LogUtil.info(getClass().toString(), "&&&&&&&");
				return false;
			} else {
				holded.add(v);
			}
		}
		mHoldedViews = holded;
		return true;
	}

	public void clearAllHoldedView() {
		mHoldedViews.clear();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		super.onInterceptTouchEvent(ev);

		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		boolean isMove = true;
		if (lastX == -100f) {
			lastX = event.getX();
			lastY = event.getY();
		}
		float tx, ty;
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			lastDownX = event.getX();
			lastDownY = event.getY();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (Math.abs(lastDownX - event.getX()) < MOVE_DISTANCE
					&& Math.abs(lastDownY - event.getY()) < MOVE_DISTANCE) {
				isMove = false;
			}
		}
		LogUtil.info("top", event.getX() + " " + isMove);
		for (int i = 0; mHoldedViews != null && i < mHoldedViews.size(); i++) {
			MotionEvent e = MotionEvent.obtainNoHistory(event);
			if (mHoldedViews.get(i) instanceof ListView) {

				int top = mHoldedViews.get(i).getTop();
				e.setLocation(1, Math.max(1, event.getY() - top));
				if (event.getY() - top >= 0) {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						if (!isMove) {
							mHoldedViews.get(i).onTouchEvent(e);
						}
					} else {
						mHoldedViews.get(i).onTouchEvent(e);
					}

				}
				continue;
			}
			if (mHoldedViews.get(i) instanceof HorizontalScrollView) {
				if (e.getAction() == MotionEvent.ACTION_DOWN) {
					tx = 0;
					ty = 0;
				} else {
					tx = lastX - event.getX();
					ty = lastY - event.getY();
				}
				HorizontalScrollView hv = (HorizontalScrollView) mHoldedViews
						.get(i);
				hv.smoothScrollBy((int) tx, (int) ty);

			}
		}
		lastX = event.getX();
		lastY = event.getY();
		// super.onTouchEvent(event);
		return true;
	}
}
