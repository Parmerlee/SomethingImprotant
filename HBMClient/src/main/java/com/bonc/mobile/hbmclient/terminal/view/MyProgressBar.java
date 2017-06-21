package com.bonc.mobile.hbmclient.terminal.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.bonc.mobile.common.util.LogUtils;

public class MyProgressBar extends ProgressBar {
    String text;
    Paint mPaint;
    int progress;

    public MyProgressBar(Context context) {
        super(context);
        initText();
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
        setText(progress);
    }

    // 开门红专用
    String newText = null;
    boolean isFirst;

    public synchronized void setProgress(int progress, String title) {
        super.setProgress(progress);
        this.progress = progress;
        newText = title;
        setText(progress);
        isFirst = false;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String[] strs = this.text.split("[%]");
        int z = -1;
        try {
            z = Integer.parseInt(strs[0]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            z = 0;
        }
        Rect rect = new Rect();
        this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
        int x = -1;
        if (TextUtils.isEmpty(this.newText)) {
            x = (getWidth() * (50 % getMax()) / getMax() - rect.width() / 2);
        } else {
            x = (getWidth() * (progress % getMax()) / getMax()) - rect.width();
        }

        int y = (getHeight() / 2) - rect.centerY();
        if (x < 0)
            x = 2;
        LogUtils.logBySys("AAAAAAAA" + newText + "text:" + text + "x:" + x);
        if (TextUtils.isEmpty(newText)) {

            canvas.drawText(this.text, x, y, this.mPaint);
        } else {

            canvas.drawText(this.newText, x, y, this.mPaint);
        }
        newText = new String();
        if (!isFirst) {
            isFirst = true;
            this.postInvalidate();
        }

    }

    // 初始化，画笔
    private void initText() {
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.WHITE);
        // this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setTextSize((float) 33.00);
        // this.mPaint.setStrokeWidth(1);
    }

    // 设置文字内容
    private void setText(int progress) {
        int i = (progress * 1) % this.getMax();
        if (progress == getMax())
            i = 100;
        if (!TextUtils.isEmpty(newText)) {
            this.text = newText;
        } else
            this.text = String.valueOf(i) + "%";
    }

}
