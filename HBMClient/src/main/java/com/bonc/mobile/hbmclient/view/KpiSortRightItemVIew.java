package com.bonc.mobile.hbmclient.view;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;

public class KpiSortRightItemVIew extends LinearLayout {
	private TextView mContentView;
	private TextView mUnitTextView;
	private ImageView mUpDownView;
	private Context context;

	public KpiSortRightItemVIew(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		inflate(getContext(), R.layout.statistics_list_right_item, this);
		mContentView = (TextView) findViewById(R.id.statistics_right_item_content_tv);
		mUnitTextView = (TextView) findViewById(R.id.statistics_right_item_unit_tv);
	}

	public KpiSortRightItemVIew(Context context, int itemLayoutId) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		inflate(getContext(), itemLayoutId, this);
		mContentView = (TextView) findViewById(R.id.statistics_right_item_content_tv);
		mUnitTextView = (TextView) findViewById(R.id.statistics_right_item_unit_tv);
		mUpDownView = (ImageView) findViewById(R.id.statistics_right_up_down);
		// setLongClickable(true);
		// mUpDownView.setVisibility(View.GONE);
	}

	public void setStatics(int statics) {
		mUpDownView.setVisibility(View.VISIBLE);
		switch (statics) {
		case KpiTitleView.TITLE_SORT_UP:
			mUpDownView.clearAnimation();
			mUpDownView.setImageResource(R.mipmap.triangle_upward);
			break;
		case KpiTitleView.TITLE_SORT_DOWN:
			mUpDownView.setImageResource(R.mipmap.triangle_downward);
			break;
		default:
			break;
		}

	}

	public void setNoticeAni() {
		mUpDownView.setImageResource(R.mipmap.triangle_upward);
		mUpDownView.setVisibility(View.VISIBLE);
		Animation ani_alpha_change = AnimationUtils.loadAnimation(getContext(),
				R.anim.alpha_change);
		ani_alpha_change
				.setAnimationListener(new Animation.AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						mUpDownView
								.setImageResource(R.mipmap.triangle_red_dash);
					}
				});
		mUpDownView.startAnimation(ani_alpha_change);
	}

	public void setNormal() {
		// mUpDownView.setImageResource(R.mipmap.order_not);
		mUpDownView.clearAnimation();
		mUpDownView.setVisibility(View.INVISIBLE);
	}

	public void setContentText(String s) {
		mContentView.setText(s);
	}

	public void setUnitText(String s) {
		mUnitTextView.setText(s);
	}

	public void setContentColor(int color) {
		mContentView.setTextColor(color);
	}

	public void setContentSize(int textSize) {
		mContentView.setTextSize(textSize);
	}

	public void setContentColorID(int color) {
		mContentView.setTextColor(context.getResources().getColor(color));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		getLayoutParams().height = LayoutParams.MATCH_PARENT;
		getChildAt(0).getLayoutParams().height = LayoutParams.MATCH_PARENT;
	}
	/*
	 * @Override public boolean onTouchEvent(MotionEvent event) { // TODO
	 * Auto-generated method stub boolean a = super.onTouchEvent(event); return
	 * false; }
	 */

}
