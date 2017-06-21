package com.bonc.mobile.hbmclient.terminal.view;

import java.text.NumberFormat;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;

/** 小一号 */
public class NumberCounterViewMini2 extends LinearLayout {
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

	public NumberCounterViewMini2(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();

	}

	public NumberCounterViewMini2(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	protected void init() {
		inflate(getContext(), R.layout.number_counter_layout, this);
		mCountTv = (TextView) findViewById(R.id.number_count_tv);
		mCountTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
		mUnitTv = (TextView) findViewById(R.id.number_unit_tv);
		mFormat = NumberFormat.getInstance();
		mFormat.setMaximumFractionDigits(1);
		mFormat.setGroupingUsed(false);
	}

	/** 设置欲显示的数字 */
	public void setNumber(double number) {
		mNumber = number;
		mCurrentScale = 1;
		double n = number;
		if (number > 10) {
			mFormat.setMaximumFractionDigits(0);
		}
		mUnitTv.setVisibility(VISIBLE);
		while (n >= SCALE) {
			mCurrentScale *= SCALE;
			n /= SCALE;
		}
		String nio = "";
		if (mCurrentScale >= SCALE) {
			nio += SCALE_UNIT;
		}
		nio += mClassifier;
		mUnitTv.setText(nio);
		((TextView) getChildAt(0)).setText(mFormat.format(n));
	}

	public void setNumberByString(String numbers) throws NumberFormatException {
		double number = Double.parseDouble(numbers);
		setNumber(number);
	}

	/** 设置子view的字体颜色 */
	public void setChildrenColor(int color) {
		((TextView) getChildAt(0)).setTextColor(color);
	}

	public void setUnitText(String un) {
		((TextView) getChildAt(1)).setText(SCALE_UNIT + un);
	}

	/** 获取当前倍数 */
	public int getScale() {
		return mCurrentScale;
	}

	/** 设置当前倍数 */
	protected void setScale(int scale) {
		this.mCurrentScale = scale;
	}

	public void setUnitString(String classifier) {
		// TODO Auto-generated method stub
		mClassifier = classifier;
		String nio = "";
		if (mCurrentScale >= SCALE) {
			nio += SCALE_UNIT;
		}
		nio += mClassifier;
		mUnitTv.setText(nio);
	}

}