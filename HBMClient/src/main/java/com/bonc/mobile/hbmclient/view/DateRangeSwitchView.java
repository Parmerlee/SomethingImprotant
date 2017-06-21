package com.bonc.mobile.hbmclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bonc.mobile.hbmclient.R;

/**
 * @author liweigao
 *
 */
public class DateRangeSwitchView extends LinearLayout {
	private Button bt_day;
	private Button bt_month;
	private OnStateChangeListener mOnStateChangeListener;

	public static final boolean day = true;
	public static final boolean month = false;
	private boolean state = day;
	private boolean changeIcon = true;

	public DateRangeSwitchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initialSwitchView(context);
	}

	public DateRangeSwitchView(Context context, boolean daterange,
			boolean changeicon) {
		super(context);
		this.state = daterange;
		this.changeIcon = changeicon;
		initialSwitchView(context);
	}

	private void initialSwitchView(Context context) {
		setOrientation(LinearLayout.HORIZONTAL);
		this.bt_day = new Button(context);
		this.bt_month = new Button(context);
		this.bt_day.setText("日监控");
		this.bt_month.setText("月盘点");
		this.bt_day.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
		this.bt_month.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
		this.bt_day.setPadding(0, 7, 0, 7);
		this.bt_month.setPadding(0, 7, 0, 7);
		// this.bt_day.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_day,
		// 0, 0, 0);
		// this.bt_month.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_month,
		// 0, 0, 0);
		setDayOnClickListener();
		setMonthOnClickListener();
		addView(bt_day, 0);
		addView(bt_month, 1);
		if (state == day) {
			toStateDay();
		} else {
			toStateMonth();
		}
	}

	public void setChangeIcon(boolean ci) {
		this.changeIcon = ci;
	}

	public void setDayOnClickListener() {
		this.bt_day.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (state == month) {
					if (changeIcon) {
						toStateDay();
					}
					mOnStateChangeListener.toStateDay();
				}
			}
		});
	}

	public void setMonthOnClickListener() {
		this.bt_month.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (state == day) {
					if (changeIcon) {
						toStateMonth();
					}
					mOnStateChangeListener.toStateMonth();
				}
			}
		});
	}

	private void toStateDay() {
		this.state = day;
		this.bt_day.setBackgroundResource(R.mipmap.switch_white_l);
		this.bt_month.setBackgroundResource(R.mipmap.switch_gray_r);
		this.bt_day.setTextColor(0xff2e2e2e);
		this.bt_month.setTextColor(0xff717171);
	}

	private void toStateMonth() {
		this.state = month;
		this.bt_day.setBackgroundResource(R.mipmap.switch_gray);
		this.bt_month.setBackgroundResource(R.mipmap.switch_white);
		this.bt_day.setTextColor(0xff717171);
		this.bt_month.setTextColor(0xff2e2e2e);
	}

	public interface OnStateChangeListener {
		void toStateDay();

		void toStateMonth();
	}

	public void setOnStateChangeListener(OnStateChangeListener oscl) {
		this.mOnStateChangeListener = oscl;
	}

}
