package com.bonc.mobile.remoteview.activity;

import com.bonc.mobile.remoteview.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ProgressBar;

public class MyProgressBar extends ProgressBar {
	String text;
	Paint mPaint;
	int progress;
	private String str;

	// 设置文字内容
	public void setStr(String str) {
		this.str = str;
	}

	public MyProgressBar(Context context) {
		super(context);

		initText();
	}

	public MyProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.MyProgress);
		int proColor = a.getColor(R.styleable.MyProgress_progressColor,
				0XFFFFFFFF);
		this.setBackgroundColor(a.getColor(R.styleable.MyProgress_bgColor,
				0XFFFFFFFF));

		initText();
	}

	public MyProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initText();
	}

	@Override
	public synchronized void setProgress(int progress) {
		super.setProgress(progress);
		this.progress = progress;
		// setText(progress);
	}

	public void setMyProgressDrawable(int proColor) {
		ClipDrawable d = new ClipDrawable(new ColorDrawable(proColor),
				Gravity.LEFT, ClipDrawable.HORIZONTAL);
		Rect rect = new Rect();
		// rect.

		// new BitmapDrawable(getResources(),
		// toRoundCorner(drawableToBitmap(d), 1));

		this.setProgressDrawable(d);

	}

	/**
	 * 获取圆角位图的方法
	 * 
	 * @param bitmap
	 *            需要转化成圆角的位图
	 * @param pixels
	 *            圆角的度数，数值越大，圆角越大
	 * @return 处理后的圆角位图
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		// 取 drawable 的颜色格式
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		// drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		String[] strs = this.str.split("[%]");
		// int z = Integer.parseInt(strs[0]);
		Rect rect = new Rect();
		this.mPaint.getTextBounds(this.str, 0, this.str.length(), rect);
		// int x = (getWidth() * (progress % getMax()) / getMax()) -
		// rect.width();
		int x = (getWidth() * (50) / getMax()) - rect.width();

		int y = (getHeight() / 2) - rect.centerY();
		if (x < 0)
			x = 2;
		canvas.drawText(this.str + "%", x, y, this.mPaint);
	}

	// 初始化，画笔
	private void initText() {

		mPaint = new Paint();
		mPaint.setColor(Color.WHITE);
		mPaint.setTextSize(25);
		mPaint.setFakeBoldText(true);
		mPaint.setAntiAlias(true); // 防锯齿
		mPaint.setDither(true); // 防抖动
		mPaint.setStyle(Paint.Style.FILL);
		// this.mPaint.setStyle(Paint.Style.STROKE);
		// this.mPaint.setTextSize((float) 18.00);
		// this.mPaint.setStrokeWidth(1);
	}

	// private void setText(int progress) {
	// System.err.println("max-get=" + this.getMax());
	// int i = (progress * 1) % this.getMax();
	// if (progress == getMax())
	// i = 100;
	// this.text = String.valueOf(i) + "%";
	// }
}
