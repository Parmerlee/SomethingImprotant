package com.bonc.mobile.hbmclient.terminal.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;

/** 封装一个用于显示键值对的UI组件 */
public class StatisticsTagView extends LinearLayout {
	private TextView mKeyTextView;
	private TextView mValueTextVIew;

	public StatisticsTagView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public StatisticsTagView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	/** 内部通用初始化方法 */
	private void init() {
		inflate(getContext(), R.layout.statistics_tag_merge, this);
		mKeyTextView = (TextView) findViewById(R.id.statistics_tag_key);
		mValueTextVIew = (TextView) findViewById(R.id.statistics_tag_name);
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 设置左侧字符串显示
	 * 
	 * @param keystring
	 *            建议在此参数末位加上":"
	 */
	public void setKeyText(String keystring) {
		mKeyTextView.setText(keystring);
	}

	/** 设置右侧字符串显示 */
	public void setValueText(String valuestring) {
		mValueTextVIew.setText(valuestring);
	}
}
