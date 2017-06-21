/**
 * DateRangeSwitchView2
 */
package com.bonc.mobile.hbmclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bonc.mobile.hbmclient.R;

/**
 * @author liweigao
 * 
 */
public class DateRangeSwitchView2 extends LinearLayout {
	private ImageView dateRange;
	private OnStateChangeListener mOnStateChangeListener;

	public static final boolean day = true;
	public static final boolean month = false;
	private boolean state = day;
	private boolean changeIcon = true;

	public DateRangeSwitchView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialSwitchView(context);
	}

	public DateRangeSwitchView2(Context context, boolean daterange) {
		super(context);
		this.state = daterange;
		this.changeIcon = false;
		initialSwitchView(context);
	}

	private void initialSwitchView(Context context) {
		setOrientation(LinearLayout.HORIZONTAL);
		this.dateRange = new ImageView(context);
		addView(dateRange);
		if (state == day) {
			toStateDay();
		} else {
			toStateMonth();
		}
	}

	public void setDayOnClickListener() {
		this.dateRange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (changeIcon) {
					toStateMonth();
				}
				mOnStateChangeListener.toStateMonth();
			}
		});
	}

	public void setMonthOnClickListener() {
		this.dateRange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (changeIcon) {
					toStateDay();
				}
				mOnStateChangeListener.toStateDay();
			}
		});
	}

	public void toStateDay() {
		this.state = day;
		this.dateRange
				.setBackgroundResource(R.mipmap.rectangle_blue_small_day);
		setDayOnClickListener();
	}

	public void toStateMonth() {
		this.state = month;
		this.dateRange
				.setBackgroundResource(R.mipmap.rectangle_blue_small_month);
		setMonthOnClickListener();
	}

	public interface OnStateChangeListener {
		void toStateDay();

		void toStateMonth();
	}

	public void setOnStateChangeListener(OnStateChangeListener oscl) {
		this.mOnStateChangeListener = oscl;
	}

}
