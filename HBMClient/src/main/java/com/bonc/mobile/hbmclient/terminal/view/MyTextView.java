package com.bonc.mobile.hbmclient.terminal.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

//自定义文本标签，自动换行
public class MyTextView extends View {
	private String txt = "";
	private Paint mPaint = new Paint();

	public MyTextView(Context context) {
		this(context, null);
	}

	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public MyTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public void setText(String text) {
		txt = text;
	}

	public void setTextSize(float size) {
		mPaint.setTextSize(getRawSize(TypedValue.COMPLEX_UNIT_DIP, size));
	}

	private void init() {
		mPaint.setAntiAlias(true);
		mPaint.setColor(0xff000000);
		mPaint.setStyle(Style.STROKE);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// view.draw()绘制了控件的背景
		// 控件的绘制操作及顺序：
		/*
		 * Draw traversal performs several drawing steps which must be executed
		 * in the appropriate order:
		 * 
		 * 1. Draw the background （绘制控件设置的背景） 2. If necessary, save the canvas'
		 * layers to prepare for fading 3. Draw view's content (可以重写，
		 * onDraw(canvas);) 4. Draw children
		 * (可重写，用来分发画布到子控件，具体看ViewGroup。对应方法dispatchDraw
		 * (canvas);此方法依次调用了子控件的draw()方法) 5. If necessary, draw the fading edges
		 * and restore layers （绘制控件四周的阴影渐变效果） 6. Draw decorations (scrollbars
		 * for instance) （用来绘制滚动条，对应方法onDrawScrollBars(canvas);。
		 * 可以重写onDrawHorizontalScrollBar()和onDrawVerticalScrollBar()来自定义滚动条）
		 */
		// 可以绘制内容和滚动条。
		// draw backgroud
		canvas.drawColor(0x00000000);
		// draw text
		FontMetrics fm = mPaint.getFontMetrics();
		float baseline = fm.descent - fm.ascent - 5;
		float x = 0;
		float y = baseline; // 17.8由于系统基于字体的底部来绘制文本，所有需要加上字体的高度。
		// 文本自动换行
		String[] texts = autoSplit(txt, mPaint, getWidth());
		for (String text : texts) {
			if (texts.length == 1)
				y += 7.5;
			canvas.drawText(text, x, y, mPaint); // 坐标以控件左上角为原点
			y += baseline + fm.leading + 2; // 添加字体行间距
		}
	}

	/**
	 * 自动分割文本
	 * 
	 * @param content
	 *            需要分割的文本
	 * @param p
	 *            画笔，用来根据字体测量文本的宽度
	 * @param width
	 *            指定的宽度
	 * @return 一个字符串数组，保存每行的文本
	 */
	private String[] autoSplit(String content, Paint p, float width) {
		int length = content.length();
		float textWidth = p.measureText(content);
		if (textWidth <= width) {
			return new String[] { content };
		}
		int start = 0, end = 1, i = 0;
		float flen = 0;
		List<String> linesList = new ArrayList<String>();
		while (start < length) {
			flen = p.measureText(content, start, end);
			if (p.measureText(content, start, end) > width) { // 文本宽度超出控件宽度时
				linesList.add((String) content.subSequence(start, --end));
				start = end;
			}
			if (end == length) { // 不足一行的文本
				linesList.add((String) content.subSequence(start, end));
				break;
			}
			end += 1;
		}

		return linesList.toArray(new String[linesList.size()]);
	}

	/**
	 * 获取指定单位对应的原始大小（根据设备信息） px,dip,sp -> px
	 * 
	 * Paint.setTextSize()单位为px
	 * 
	 * 代码摘自：TextView.setTextSize()
	 * 
	 * @param unit
	 *            TypedValue.COMPLEX_UNIT_*
	 * @param size
	 * @return
	 */
	public float getRawSize(int unit, float size) {
		Context c = getContext();
		Resources r;

		if (c == null)
			r = Resources.getSystem();
		else
			r = c.getResources();

		return TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
	}
}