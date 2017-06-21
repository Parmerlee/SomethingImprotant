/**
 * TODO
 */
package com.bonc.mobile.hbmclient.view;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * @author liweigao
 */
public class Top10HorizontalScrollView extends HorizontalScrollView {
    // scrollview只可以直接作用于一个子view；
    private Top10ViewGroup mTop10ViewGroup;
    private OnBorderListener onBorderListener;

    /**
     * @param context
     * @param attrs
     */
    public Top10HorizontalScrollView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.mTop10ViewGroup = new Top10ViewGroup(context);
        addView(this.mTop10ViewGroup);
    }

    public void dispatchView() {
        this.mTop10ViewGroup.dispatchView();
    }

    public void setData(List<Map<String, String>> data) {
        this.mTop10ViewGroup.setData(data);
    }

    public void updateView() {
        this.mTop10ViewGroup.updateView();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        // return super.onInterceptTouchEvent(ev);
        getParent().requestDisallowInterceptTouchEvent(true);
        ScrollViewGroup.setDisallowIntercept(true);
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        // TODO Auto-generated method stub
        this.mTop10ViewGroup.setOnClickListener(l);
    }

    public void setOnBorderListener(final OnBorderListener onBorderListener) {
        this.onBorderListener = onBorderListener;
        if (onBorderListener == null) {
            return;
        }

        if (mTop10ViewGroup == null) {
            mTop10ViewGroup = (Top10ViewGroup) getChildAt(0);
        }
    }

    public interface OnBorderListener {
        public void onLeft();

        public void onNotBorder();

        public void onRight();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        // TODO Auto-generated method stub
        super.onScrollChanged(l, t, oldl, oldt);
        doOnBorderListener();
    }

    private void doOnBorderListener() {
        if (getScrollX() <= 5) {
            if (onBorderListener != null) {
                onBorderListener.onLeft();
            }
        } else if (mTop10ViewGroup != null
                && getScrollX() > 5
                && mTop10ViewGroup.getMeasuredWidth() > (getScrollX()
                + getWidth() + 5)) {
            if (onBorderListener != null) {
                onBorderListener.onNotBorder();
            }
        } else if (mTop10ViewGroup != null
                && mTop10ViewGroup.getMeasuredWidth() <= (getScrollX()
                + getWidth() + 5)) {
            if (onBorderListener != null) {
                onBorderListener.onRight();
            }
        }
    }

    Boolean isSpring;

    public void isSpring(Boolean isSpring) {
        this.isSpring = isSpring;
        if (mTop10ViewGroup != null) {
            mTop10ViewGroup.setSpring(isSpring);
        }
    }
}
