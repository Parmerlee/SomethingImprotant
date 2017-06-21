package com.bonc.mobile.common.view;

/***
 * android 滑轮 lijing 
 * 2016年4月5日21:36:07
 */
import java.util.ArrayList;
import java.util.List;

import com.bonc.mobile.common.util.UIUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class WheelView extends ScrollView {
	public static final String TAG = WheelView.class.getSimpleName();

	public static class OnWheelViewListener {
		public void onSelected(int selectedIndex, String item) {
		}
	}

	private Context context;

	private LinearLayout views;

	public WheelView(Context context) {
		super(context);
		init(context);
	}

	public WheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public WheelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	List<String> items = new ArrayList<String>();

	/** 设置内容 */
	public void setItems(List<String> list) {
		items.clear();
		items.addAll(list);

		// 前面和后面补全
		for (int i = 0; i < offset; i++) {
			items.add(0, "");
			items.add("");
		}

		initData();
	}

	public static final int OFF_SET_DEFAULT = 1;
	int offset = OFF_SET_DEFAULT; // 偏移量（需要在最前面和最后面补全）

	public int getOffset() {
		return offset;
	}

	/** 设置显示界面上展现多少个item */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	int displayItemCount; // 每页显示的数量

	int selectedIndex = 1;

	private void init(Context context) {
		this.context = context;
		this.setVerticalScrollBarEnabled(false);

		views = new LinearLayout(context);
		views.setOrientation(LinearLayout.VERTICAL);
		this.addView(views);

		scrollerTask = new Runnable() {

			public void run() {

				int newY = getScrollY();
				if (initialY - newY == 0) { // stopped
					final int remainder = initialY % itemHeight;
					final int divided = initialY / itemHeight;

					if (remainder == 0) {
						selectedIndex = divided + offset;

						onSeletedCallBack();
					} else {
						if (remainder > itemHeight / 2) {
							WheelView.this.post(new Runnable() {
								@Override
								public void run() {
									WheelView.this.smoothScrollTo(0, initialY - remainder + itemHeight);
									selectedIndex = divided + offset + 1;
									onSeletedCallBack();
								}
							});
						} else {
							WheelView.this.post(new Runnable() {
								@Override
								public void run() {
									WheelView.this.smoothScrollTo(0, initialY - remainder);
									selectedIndex = divided + offset;
									onSeletedCallBack();
								}
							});
						}
					}
				} else {
					initialY = getScrollY();
					WheelView.this.postDelayed(scrollerTask, newCheck);
				}
			}
		};

	}

	int initialY;

	Runnable scrollerTask;
	int newCheck = 50;

	public void startScrollerTask() {

		initialY = getScrollY();
		this.postDelayed(scrollerTask, newCheck);
	}

	private void initData() {
		displayItemCount = offset * 2 + 1;

		for (String item : items) {
			System.out.println(item);
			views.addView(createView(item));
		}

		refreshItemView(0);
	}

	int itemHeight = 0;

	/** 滚轮控件中的View 在这里创建， 本方法里面只是简单的TextView */
	private TextView createView(String item) {
		TextView tv = new TextView(context);
		itemHeight = UIUtil.fromDPtoPX(context, 46);
		LayoutParams tvlp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
		tv.setLayoutParams(tvlp);

		tv.setSingleLine(true);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		tv.setText(item);
		tv.setGravity(Gravity.CENTER);

		if(TextUtils.equals(item, "59")){
			System.out.println("AAAAA");
		}
		views.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight * displayItemCount));
		ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) this.getLayoutParams();
		
		this.setLayoutParams(new ViewGroup.LayoutParams(lp.width, itemHeight * displayItemCount));
		int padding = UIUtil.fromDPtoPX(context, 15);
		this.setPadding(padding, 0, padding, 0);
		
		return tv;
	}

	public static int getViewMeasuredHeight(View view) {
		calcViewMeasure(view);
		return view.getMeasuredHeight();
	}

	/**
	 * 获取控件的宽度，如果获取的宽度为0，则重新计算尺寸后再返回宽度
	 * 
	 * @param view
	 * @return
	 */
	public static int getViewMeasuredWidth(View view) {
		calcViewMeasure(view);
		return view.getMeasuredWidth();
	}

	/**
	 * 测量控件的尺寸
	 * 
	 * @param view
	 */
	public static void calcViewMeasure(View view) {
		int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		view.measure(width, expandSpec);
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);

		refreshItemView(t);

		if (t > oldt) {
			// Logger.d(TAG, "向下滚动");
			scrollDirection = SCROLL_DIRECTION_DOWN;
		} else {
			// Logger.d(TAG, "向上滚动");
			scrollDirection = SCROLL_DIRECTION_UP;

		}

	}

	private void refreshItemView(int y) {
		int position = y / itemHeight + offset;
		int remainder = y % itemHeight;
		int divided = y / itemHeight;

		if (remainder == 0) {
			position = divided + offset;
		} else {
			if (remainder > itemHeight / 2) {
				position = divided + offset + 1;
			}

		}

		int childSize = views.getChildCount();
		for (int i = 0; i < childSize; i++) {
			TextView itemView = (TextView) views.getChildAt(i);
			if (null == itemView) {
				return;
			}
			
			if (position == i) {
				itemView.setTextColor(Color.parseColor("#00a2ff"));
				itemView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			} else if (Math.abs(position - i) == 1) {
				itemView.setTextColor(Color.parseColor("#afafaf"));
				itemView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			} else {
				itemView.setTextColor(Color.parseColor("#dfdfdf"));
				itemView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			}

		}
	}

	/**
	 * 获取选中区域的边界
	 */
	int[] selectedAreaBorder;

	private int[] obtainSelectedAreaBorder() {
		if (null == selectedAreaBorder) {
			selectedAreaBorder = new int[2];
			selectedAreaBorder[0] = itemHeight * offset;
			selectedAreaBorder[1] = itemHeight * (offset + 1);
		}
		return selectedAreaBorder;
	}

	@SuppressWarnings("unused")
	private int scrollDirection = -1;
	private static final int SCROLL_DIRECTION_UP = 0;
	private static final int SCROLL_DIRECTION_DOWN = 1;

	Paint paint;
	int viewWidth;

	@SuppressWarnings("deprecation")
	@Override
	public void setBackgroundDrawable(Drawable background) {
		if (viewWidth == 0) {
			viewWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
		}

		if (null == paint) {
			paint = new Paint();
			paint.setColor(Color.parseColor("#919191"));
			paint.setStrokeWidth(1);
		}

		background = new Drawable() {
			@Override
			public void draw(Canvas canvas) {
				
				canvas.drawLine(viewWidth * 0 / 6, obtainSelectedAreaBorder()[0], viewWidth * 6 / 6, obtainSelectedAreaBorder()[0], paint);
				canvas.drawLine(viewWidth * 0 / 6, obtainSelectedAreaBorder()[1], viewWidth * 6 / 6, obtainSelectedAreaBorder()[1], paint);
			}

			@Override
			public void setAlpha(int alpha) {
			}

			@Override
			public void setColorFilter(ColorFilter cf) {
			}

			@Override
			public int getOpacity() {
				return 0;
			}
		};

		super.setBackgroundDrawable(background);

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		viewWidth = w;
		setBackgroundDrawable(null);
	}

	/**
	 * 选中回调
	 */
	private void onSeletedCallBack() {
		if (null != onWheelViewListener) {
			onWheelViewListener.onSelected(selectedIndex, items.get(selectedIndex));
		}

	}

	public void setSeletion(int position) {
		final int p = position;
		selectedIndex = p + offset;
		this.post(new Runnable() {
			@Override
			public void run() {
				WheelView.this.smoothScrollTo(0, p * itemHeight);
			}
		});

	}

	public String getSeletedItem() {
		return items.get(selectedIndex);
	}

	public int getSeletedIndex() {
		return selectedIndex - offset;
	}

	@Override
	public void fling(int velocityY) {
		super.fling(velocityY / 3);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_UP) {

			startScrollerTask();
		}
		return super.onTouchEvent(ev);
	}

	private OnWheelViewListener onWheelViewListener;

	public OnWheelViewListener getOnWheelViewListener() {
		return onWheelViewListener;
	}

	public void setOnWheelViewListener(OnWheelViewListener onWheelViewListener) {
		this.onWheelViewListener = onWheelViewListener;
	}

}