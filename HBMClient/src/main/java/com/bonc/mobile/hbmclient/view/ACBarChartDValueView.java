package com.bonc.mobile.hbmclient.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;

public class ACBarChartDValueView extends View {
	private float mPanelPointX1, mPanelPointX2, mPanelPointY;
	private float mPanelRungY = 110;
	private String mNumberString = "0";
	private String mUnitString = "";
	private int mColor = Color.rgb(0, 84, 139);
	private float mTextSize = 20;

	private Paint mPaint;

	public ACBarChartDValueView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (mPaint == null) {
			mPaint = new Paint();
			mPaint.setColor(mColor);
			mPaint.setTextSize(mTextSize);
			mPaint.setAntiAlias(true);
		}
		/*
		 * if(!this.isDown) {
		 */
		canvas.drawLine(mPanelPointX1, mPanelPointY, mPanelPointX1,
				mPanelRungY, mPaint);
		canvas.drawLine(mPanelPointX1, mPanelRungY, mPanelPointX2, mPanelRungY,
				mPaint);
		canvas.drawLine(mPanelPointX2, mPanelRungY, mPanelPointX2,
				mPanelPointY, mPaint);
		int offset = mNumberString.length() * 6;
		int offset_int = (int) (mUnitString.length() * mTextSize);
		canvas.drawText(mNumberString,
				Math.abs((mPanelPointX2 + mPanelPointX1) / 2) - offset,
				mPanelRungY - 4, mPaint);
		/*
		 * } else { canvas.drawLine(mPanelPointX1, mPanelPointY, mPanelPointX1,
		 * mPanelRungY, mPaint); canvas.drawLine(mPanelPointX1, mPanelPointY,
		 * mPanelPointX2, mPanelPointY, mPaint); canvas.drawLine(mPanelPointX2,
		 * mPanelRungY, mPanelPointX2, mPanelPointY, mPaint); int
		 * offset=mNumberString.length()*6; int offset_int=(int)
		 * (mUnitString.length()*mTextSize);
		 * canvas.drawText(mNumberString,Math.abs
		 * ((mPanelPointX2+mPanelPointX1)/2)-offset, mPanelPointY-4, mPaint); }
		 */
		// canvas.drawText(mUnitString, getMeasuredWidth()-offset_int-5,
		// mPanelRungY-4-mPaint.getTextSize(), mPaint);
	}

	public void setLinePivotsX(float pivotX1, float pivotX2) {
		mPanelPointX1 = pivotX1;
		mPanelPointX2 = pivotX2;
		invalidate();
	}

	public void setUnitString(String s) {
		if ("".equals(s)) {
			mUnitString = s;
		} else
			mUnitString = "单位:" + s;
	}

	public void setNumberString(String s) {
		mNumberString = s;
	}

	public void setLinePivotsY(float pivotY) {
		mPanelPointY = pivotY;
	}

	public void setTextPivotsY(float py) {
		mPanelRungY = py;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		getLayoutParams().height = LayoutParams.MATCH_PARENT;
		getLayoutParams().width = LayoutParams.MATCH_PARENT;
	}
}
