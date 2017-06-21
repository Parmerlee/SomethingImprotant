package com.bonc.mobile.remoteview.common;

import com.bonc.mobile.remoteview.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyPro extends LinearLayout {

	View view;
	TextView tv;
	LinearLayout layout;

	public MyPro(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		if (context != null)
			view = LayoutInflater.from(context).inflate(R.layout.myprogress,
					null);
		initView();
	}

	public MyPro(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		if (context != null)
			view = LayoutInflater.from(context).inflate(R.layout.myprogress,
					null);
		initView();
	}

	public MyPro(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (context != null)
			view = LayoutInflater.from(context).inflate(R.layout.myprogress,
					null);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		tv = (TextView) view.findViewById(R.id.myprogress_tv);
		layout = (LinearLayout) view.findViewById(R.id.myprogress_ll);
	}

	public void setText(String str) {
		tv.setText(str);
		tv.setWidth(view.getWidth() * Integer.valueOf(str) / 100);
	}

	public void setTextBg(int color) {
		tv.setBackgroundColor(color);
	}

	public void setLayoutBg(int color) {
		layout.setBackgroundColor(color);
	}
}
