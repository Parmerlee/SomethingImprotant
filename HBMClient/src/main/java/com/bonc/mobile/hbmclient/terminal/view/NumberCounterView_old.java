package com.bonc.mobile.hbmclient.terminal.view;

import java.text.NumberFormat;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.util.NumberUtil;

/** 特殊效果view */
public class NumberCounterView_old extends LinearLayout {
	/** 必须设置为10的整数倍否则将导致严重错误 */
	protected static final int SCALE = 10000;
	protected static final String SCALE_UNIT = "万";

	protected TextView mCountTv;
	protected TextView mUnitTv;

	/** 默认0 */
	private int mCurrentScale = 1;
	private double mNumber = 0;
	NumberFormat mFormat;
	private String mClassifier = "";

	public NumberCounterView_old(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();

	}

	public NumberCounterView_old(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	protected void init() {
		inflate(getContext(), R.layout.number_counter_layout, this);
		mCountTv = (TextView) findViewById(R.id.number_count_tv);
		mUnitTv = (TextView) findViewById(R.id.number_unit_tv);
		mFormat = NumberFormat.getInstance();
		mFormat.setMaximumFractionDigits(0);
		mFormat.setGroupingUsed(false);
	}

	/** 设置欲显示的数字 */
	public void setNumber(double number) {
		mNumber = number;
		mCurrentScale = 1;
		if (number > 10) {
			mFormat.setMaximumFractionDigits(0);
		}
		double n = number;
		String uio = "";
		while (n >= SCALE) {
			mCurrentScale *= SCALE;
			n /= SCALE;

		}
		if (mCurrentScale >= SCALE) {
			uio = SCALE_UNIT;
			if (n > 10 && n < 1000) {
				mFormat.setMaximumFractionDigits(1);
			} else if (n < 10) {
				mFormat.setMaximumFractionDigits(2);
			}
		}
		uio += mClassifier;
		mUnitTv.setText(uio);
		((TextView) getChildAt(0)).setText(mFormat.format(n));
	}

	public void setNumberByString(String numbers) throws NumberFormatException {
		double number = NumberUtil.changeToDouble(numbers);
		setNumber(number);
	}

	/** 设置子view的字体颜色 */
	public void setChildrenColor(int color) {
		((TextView) getChildAt(0)).setTextColor(color);
	}

	/** 获取当前倍数 */
	public int getScale() {
		return mCurrentScale;
	}

	/** 设置当前倍数 */
	protected void setScale(int scale) {
		this.mCurrentScale = scale;
	}

	public void setUnitString(String u) {
		mClassifier = u;
		String uio = "";
		if (mCurrentScale >= SCALE) {
			uio = SCALE_UNIT;
		}
		uio += mClassifier;
		mUnitTv.setText(uio);
	}
}
