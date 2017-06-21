package com.bonc.mobile.hbmclient.terminal.view;

import java.util.Random;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;

/**
 * 重要指标首页展示模块，显示为六（6）个指标名-指标值对，每行2个，共3行。 标题为可选，不设置时不会显示
 * */
public class StatisticsVolumeViewLite extends LinearLayout {
	private StatisticsTagView[] mTagViews = new StatisticsTagView[6];
	private TextView mTitleView;

	public StatisticsVolumeViewLite(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public StatisticsVolumeViewLite(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	/** 内部初始化方法 */
	private void init() {
		View cont = inflate(getContext(), R.layout.statistic_lite, null);
		addView(cont);
		mTitleView = (TextView) findViewById(R.id.statistic_lite_title);
		mTagViews[0] = (StatisticsTagView) findViewById(R.id.tag1);
		mTagViews[1] = (StatisticsTagView) findViewById(R.id.tag2);
		mTagViews[2] = (StatisticsTagView) findViewById(R.id.tag3);
		mTagViews[3] = (StatisticsTagView) findViewById(R.id.tag4);
		mTagViews[4] = (StatisticsTagView) findViewById(R.id.tag5);
		mTagViews[5] = (StatisticsTagView) findViewById(R.id.tag6);
		Random ran = new Random();
		if (mTitleView.getText().equals("") || mTitleView.getText() == null) {
			mTitleView.setVisibility(View.GONE);
		}
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		for (StatisticsTagView view : mTagViews) {
			view.setKeyText("属性：");
			view.setValueText(ran.nextFloat() + "");
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return true;
	}

	/** 设置标题 */
	public void setTitle(String title) {
		if (title == null || title.equals("")) {
			mTitleView.setVisibility(View.GONE);
		} else {
			mTitleView.setVisibility(View.VISIBLE);
			mTitleView.setText(title);
		}
	}

	/**
	 * 一次设置全部指标值。若数组长度超过6个，自datas[6]起全部会被忽略
	 * 
	 * @param datas
	 *            存储指标值的数组
	 * */
	public void setStatisticsDatas(String[] datas) {
		for (int i = 0; i < mTagViews.length; i++) {
			mTagViews[i].setValueText(datas[i]);
		}
	}

	/**
	 * 一次设置全部指标名。若数组长度超过6个，自names[6]起全部会被忽略
	 * 
	 * @param names
	 *            存储指标名的数组
	 * */
	public void setStatisticsDatasName(String[] names) {
		for (int i = 0; i < mTagViews.length; i++) {
			mTagViews[i].setKeyText(names[i]);
		}
	}
}
