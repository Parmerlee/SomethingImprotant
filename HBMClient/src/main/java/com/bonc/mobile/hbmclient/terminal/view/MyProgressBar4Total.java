package com.bonc.mobile.hbmclient.terminal.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class MyProgressBar4Total extends ProgressBar {
	String text1, text2;
	Paint mPaint;
	int progress;

	public MyProgressBar4Total(Context context) {
		super(context);
		initText();
	}

	public MyProgressBar4Total(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initText();
	}

	public MyProgressBar4Total(Context context, AttributeSet attrs) {
		super(context, attrs);
		initText();
	}

	@Override
	public synchronized void setProgress(int progress) {
		super.setProgress(progress);
		this.progress = progress;
		setText(progress);
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Rect rect = new Rect();
		Rect rect2 = new Rect();
		this.mPaint.getTextBounds(this.text1, 0, this.text1.length(), rect);
		this.mPaint.getTextBounds(this.text2, 0, this.text2.length(), rect2);

		int xtotal = getWidth() - rect2.width() - 5;
		int y = (getHeight() / 2) - rect.centerY();
		int x1 = 2;
		int x2 = 200;
		canvas.drawText(this.text1, x1, y, this.mPaint); // 画第一百分比
		canvas.drawText(this.text2, xtotal, y, this.mPaint); // 画第一百分比
	}

	// 初始化，画笔
	private void initText() {
		this.mPaint = new Paint();
		this.mPaint.setColor(Color.WHITE);
		// this.mPaint.setStyle(Paint.Style.STROKE);
		this.mPaint.setTextSize((float) 18.00);
		// this.mPaint.setStrokeWidth(1);
	}

	// 设置文字内容
	private void setText(int progress) {
		System.err.println("max-get=" + this.getMax());
		int max = this.getMax();
		int i = (progress * 1) % this.getMax();
		this.text1 = String.valueOf(i) + "%";
		if (progress == getMax()) {
			this.text1 = "100%";
			i = 100;
		}
		this.text2 = String.valueOf(100 - i) + "%";
	}
}
