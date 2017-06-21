package com.bonc.mobile.hbmclient.view;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ListView;

/****
 * 用来设置listview expandablelistview的各个监听事件
 * 
 * @author hhf
 * 
 */
public class ListViewSetting {
	public int leftTouchFlag;// 用来标识左侧是否被touch

	/**
	 * 设置ExpandableListView的组不可点击，并且初始状态全部组都展开
	 * 
	 * @param listView1
	 *            左侧列表
	 * @param listView2
	 *            右侧列表
	 * @param ea1
	 *            左侧列表的适配器
	 * @param ea2
	 *            右侧列表的适配器
	 */
	public void expandListViewAndSetNotClickGroup(ExpandableListView listView1,
			ExpandableListView listView2, ExpandableListAdapter ea1,
			ExpandableListAdapter ea2) {
		// 将组都展开
		for (int i = 0; i < ea2.getGroupCount(); i++) {
			listView2.expandGroup(i);
		}
		// 设置组不可点击
		listView2.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {

				return true;
			}
		});

		for (int i = 0; i < ea1.getGroupCount(); i++) {
			listView1.expandGroup(i);
		}
		// 设置组不可点击
		listView1.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {

				return true;
			}
		});
	}

	/**
	 * 设置左右Expandble列表的touch和scroll监听，用于实现两个列表同步滑动
	 * 
	 * @param listView1
	 *            左侧列表
	 * @param listView2
	 *            右侧列表
	 */
	public void setListViewOnTouchAndScrollListener(
			final ExpandableListView listView1,
			final ExpandableListView listView2) {
		// 设置右侧列表的touch监听，将事件传递给左侧列表 ，并将touch坐标转换成touch左侧的坐标
		listView2.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				leftTouchFlag = 0;
				return false;
			}
		});

		// 设置左侧列表的touch监听，将事件传递给右侧列表
		listView1.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				leftTouchFlag = 1;

				return false;
			}
		});

		// 设置右侧列表的scroll监听，用于滑动过程中左右不同步时校正
		listView2.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 如果已经停止滑动
				if (scrollState == 0 || scrollState == 1) {
					// 获得第一个子view
					View subView = view.getChildAt(0);

					if (subView != null) {
						final int top = subView.getTop();
						final int position = view.getFirstVisiblePosition();
						// 如果两个首个显示的子view高度不等
						listView1.setSelectionFromTop(position, top);
						listView2.setSelectionFromTop(position, top);
					}
				}

			}

			@Override
			public void onScroll(AbsListView view, final int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				View subView = view.getChildAt(0);
				if (subView != null) {
					final int top = subView.getTop();

					int top1 = listView1.getChildAt(0).getTop();
					if (!(top1 - 7 < top && top < top1 + 7)) {
						listView1.setSelectionFromTop(firstVisibleItem, top);
						listView2.setSelectionFromTop(firstVisibleItem, top);
					}
				}
			}
		});

		// 设置左侧列表的scroll监听，用于滑动过程中左右不同步时校正
		listView1.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == 0 || scrollState == 1) {
					// 获得第一个子view
					View subView = view.getChildAt(0);

					if (subView != null) {
						final int top = subView.getTop();
						final int top1 = listView2.getChildAt(0).getTop();
						final int position = view.getFirstVisiblePosition();

						// 如果两个首个显示的子view高度不等
						if (top != top1) {
							listView1.setSelectionFromTop(position, top);
							listView2.setSelectionFromTop(position, top);
						}
					}
				}

			}

			@Override
			public void onScroll(AbsListView view, final int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				View subView = view.getChildAt(0);
				if (subView != null) {
					final int top = subView.getTop();
					listView2.setSelectionFromTop(firstVisibleItem, top);
					listView1.setSelectionFromTop(firstVisibleItem, top);

				}
			}
		});
	}

	// ///////////////////////////////////////////////////////////////////以上
	/**
	 * 设置左右ListView列表的touch和scroll监听，用于实现两个列表同步滑动
	 * 
	 * @param listView1
	 *            左侧列表
	 * @param listView2
	 *            右侧列表
	 */
	public void setListViewOnTouchAndScrollListener(final ListView listView1,
			final ListView listView2) {
		// 设置右侧列表的touch监听，将事件传递给左侧列表 ，并将touch坐标转换成touch左侧的坐标
		listView2.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				leftTouchFlag = 0;
				return false;
			}
		});

		// 设置左侧列表的touch监听，将事件传递给右侧列表
		listView1.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				leftTouchFlag = 1;
				return false;
			}
		});

		// 设置右侧列表的scroll监听，用于滑动过程中左右不同步时校正
		listView2.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 如果已经停止滑动
				if (scrollState == 0 || scrollState == 1) {
					// 获得第一个子view
					View subView = view.getChildAt(0);

					if (subView != null) {
						final int top = subView.getTop();
						final int top1 = listView1.getChildAt(0).getTop();
						final int position = view.getFirstVisiblePosition();

						// 如果两个首个显示的子view高度不等
						if (top != top1) {
							listView1.setSelectionFromTop(position, top);
						}
					}
				}

			}

			public void onScroll(AbsListView view, final int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				View subView = view.getChildAt(0);
				if (subView != null) {
					final int top = subView.getTop();

					int top1 = listView1.getChildAt(0).getTop();
					if (!(top1 - 7 < top && top < top1 + 7)) {
						listView1.setSelectionFromTop(firstVisibleItem, top);
						listView2.setSelectionFromTop(firstVisibleItem, top);
					}

				}
			}
		});

		// 设置左侧列表的scroll监听，用于滑动过程中左右不同步时校正
		listView1.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == 0 || scrollState == 1) {
					// 获得第一个子view
					View subView = view.getChildAt(0);

					if (subView != null) {
						final int top = subView.getTop();
						final int top1 = listView2.getChildAt(0).getTop();
						final int position = view.getFirstVisiblePosition();

						// 如果两个首个显示的子view高度不等
						if (top != top1) {
							listView1.setSelectionFromTop(position, top);
							listView2.setSelectionFromTop(position, top);
						}
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, final int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				View subView = view.getChildAt(0);
				if (subView != null) {
					final int top = subView.getTop();

					listView1.setSelectionFromTop(firstVisibleItem, top);
					listView2.setSelectionFromTop(firstVisibleItem, top);

				}
			}
		});
	}
}
