package com.bonc.mobile.hbmclient.terminal.view;

import org.achartengine.chart.PieChart;
import org.achartengine.renderer.DefaultRenderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class VerticalChartLabel extends View {
	DefaultRenderer mRenderer;
	private float mInterBorder;
	private float mRectHeight = 20;
	private float mTextSpace = 22;
	private int[] mColors;
	private String[] mLabels;
	private Paint mRectPaint;
	private Paint mTextPaint;

	public VerticalChartLabel(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public VerticalChartLabel(Context context, PieChart chart, String[] strings) {
		super(context);
		// TODO Auto-generated constructor stub
		setBindedChart(chart);
		mLabels = strings;
		init();
	}

	public VerticalChartLabel(Context context, DefaultRenderer render,
			String[] strings) {
		super(context);
		// TODO Auto-generated constructor stub
		mRenderer = render;
		mLabels = strings;
		init();
	}

	private void init() {
		mRectPaint = new Paint();
		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		tone();
	}

	private void setBindedChart(PieChart chart) {
		mRenderer = chart.getRenderer();
	}

	private void tone() {
		if (mRenderer == null) {
			return;
		}
		int len = mRenderer.getSeriesRenderers().length;
		mColors = new int[len];
		for (int i = 0; i < len; i++) {
			mColors[i] = mRenderer.getSeriesRendererAt(i).getColor();
		}
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		float border = mInterBorder;
		float rectHeight = mRectHeight;
		float space = mTextSpace;
		float step = (canvas.getHeight() - border * 2) / mColors.length;
		mTextPaint.setTextSize(rectHeight - 2);
		if (mColors != null && mLabels != null) {
			for (int i = 0; i < mColors.length; i++) {
				float _i = i + 0.5f;
				mRectPaint.setColor(mColors[i]);
				mRectPaint.setStyle(Paint.Style.FILL);
				canvas.drawRect(border, border + _i * step - rectHeight / 2,
						border + rectHeight, border + _i * step + rectHeight
								/ 2, mRectPaint);
				mRectPaint.setStyle(Paint.Style.STROKE);
				mRectPaint.setColor(0xff000000);
				canvas.drawRect(border, border + _i * step - rectHeight / 2,
						border + rectHeight, border + _i * step + rectHeight
								/ 2, mRectPaint);
				canvas.drawText(mLabels[i], border + rectHeight + space, border
						+ _i * step + rectHeight / 2 - 2, mTextPaint);
			}
		}
	}

	public float getInterBorder() {
		return mInterBorder;
	}

	public void setInterBorder(float interBorder) {
		this.mInterBorder = interBorder;
	}

	public float getmRectHeight() {
		return mRectHeight;
	}

	public void setmRectHeight(float rectHeight) {
		this.mRectHeight = rectHeight;
	}

	public float getTextSpace() {
		return mTextSpace;
	}

	public void setTextSpace(float mTextSpace) {
		this.mTextSpace = mTextSpace;
	}

	public void refreshGraphic(DefaultRenderer render, String[] strings) {
		mRenderer = render;
		mLabels = strings;
		tone();
	}
}
