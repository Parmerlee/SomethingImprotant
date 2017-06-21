package com.bonc.mobile.hbmclient.terminal.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;

public class ExclusiveButtonGroup extends LinearLayout {
	private static final String[] DEFAULT_TEXT_ARRAY = { "进货渠道", "销售渠道",
			"用户结构", "智能机" };

	private int mNormalDrawableRes = R.drawable.top_button;
	private int mSelectedDrawableRes = R.mipmap.top_btn_pressed;
	private int mIndex = 0;// 默认选中第一项

	private List<View> mButtons;
	private OnCheckedChangeListener mListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {// TODO
																		// Nothing
		}
	};

	public ExclusiveButtonGroup(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public ExclusiveButtonGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {
		mButtons = new ArrayList<View>();
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).setOnClickListener(
					new TaggedClickListener(i, mListener));
		}
		for (int i = 0; i < Math
				.min(DEFAULT_TEXT_ARRAY.length, getChildCount()); i++) {
			if (getChildAt(i) instanceof Button) {
				Button bu = (Button) getChildAt(i);
				bu.setText(DEFAULT_TEXT_ARRAY[i]);
			}
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		selectItem(mIndex);
	}

	@Override
	public void addView(View child, int index,
			android.view.ViewGroup.LayoutParams params) {
		// TODO Auto-generated method stub
		super.addView(child, index, params);
		child.setOnClickListener(new TaggedClickListener(getChildCount() - 1,
				mListener));
	}

	/** 获取选中的项目号 */
	public int getSelected() {
		return mIndex;
	}

	public void selectItem(int index) {
		if (mIndex >= getChildCount()) {
			return;
		} else {
			this.mIndex = index;
			for (int i = 0; i < getChildCount(); i++) {
				getChildAt(i).setBackgroundResource(mNormalDrawableRes);
				try {
					TextView tv = (TextView) getChildAt(i);
					tv.setTextColor(0xff666666);
				} catch (ClassCastException e) {
					// TODO: Nothing
				}
			}
			getChildAt(index).setBackgroundResource(mSelectedDrawableRes);
			try {
				TextView tv = (TextView) getChildAt(index);
				tv.setTextColor(0xfff0f0f0);
			} catch (ClassCastException e) {
				// TODO: Nothing
			}

		}
	}

	public void setButtonImageRescource(int normal, int selected) {
		mNormalDrawableRes = normal;
		mSelectedDrawableRes = selected;
		for (int i = 0; i < getChildCount(); i++) {
			if (i == getSelected()) {
				getChildAt(i).setBackgroundResource(mSelectedDrawableRes);
			} else {
				getChildAt(i).setBackgroundResource(mNormalDrawableRes);
			}
		}
	}

	/**
	 * 为Group设置一个类似RadioGroup.setOnCheckedChangeListener的监听器，以在点击不同按钮时作出响应
	 * 但，RadioGroup恒返回null
	 */
	public void setOnCheckedChangeListener(OnCheckedChangeListener l) {
		mListener = l;
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).setOnClickListener(
					new TaggedClickListener(i, mListener));
		}
	}

	private class TaggedClickListener implements OnClickListener {
		private int mmTag;
		private OnCheckedChangeListener mmListener;

		public TaggedClickListener(int tag, OnCheckedChangeListener l) {
			mmListener = l;
			mmTag = tag;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mmListener.onCheckedChanged(null, mmTag);
			selectItem(mmTag);
		}
	}
}
