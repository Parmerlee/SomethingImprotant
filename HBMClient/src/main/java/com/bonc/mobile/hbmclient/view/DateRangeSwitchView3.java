/**
 * DateRangeSwitchView2
 */
package com.bonc.mobile.hbmclient.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bonc.mobile.common.util.FileUtils;
import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.hbmclient.R;

/**
 * @author 日月切换按钮 BITabActivity类专用 2016年6月3日11:40:17
 */
public class DateRangeSwitchView3 extends LinearLayout {
    private ImageView dateRange;
    private OnStateChangeListener mOnStateChangeListener;

    public static final boolean day = true;
    public static final boolean month = false;
    private boolean state = day;
    public boolean changeIcon = true;

    CountDownTimer countDownTimer = null;

    Context context;

    public DateRangeSwitchView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialSwitchView(context);
    }

    public DateRangeSwitchView3(Context context, boolean daterange) {
        super(context);
        this.state = daterange;
        // this.changeIcon = false;
        initialSwitchView(context);
    }

    private void initialSwitchView(Context context) {
        setOrientation(LinearLayout.HORIZONTAL);
        this.dateRange = new ImageView(context);
        this.context = context;
        addView(dateRange);
        if (state == day) {
            toStateDay();
        } else {
            toStateMonth();
        }
    }

    public void setDayOnClickListener() {
        LogUtils.toast(context, countDownTimer == null);

        if (countDownTimer != null) {
            return;
        }


        countDownTimer = new CountDownTimer(3 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                countDownTimer = null;
            }
        };

//        countDownTimer = FileUtils.postDelayWithTime(3);
        countDownTimer.start();

        this.dateRange.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (changeIcon) {
                    toStateMonth();
                }
                mOnStateChangeListener.toStateMonth();
            }
        });
    }

    public void setMonthOnClickListener() {
        LogUtils.toast(context, countDownTimer == null);
        if (countDownTimer != null) {
            return;
        }


        countDownTimer = new CountDownTimer(3 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                countDownTimer = null;
            }
        };


//        countDownTimer = FileUtils.postDelayWithTime(3);
        countDownTimer.start();
        this.dateRange.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (changeIcon) {
                    toStateDay();
                }
                mOnStateChangeListener.toStateDay();
            }
        });
    }

    public void toStateDay() {
        this.state = day;
        this.dateRange
                .setBackgroundResource(R.mipmap.rectangle_blue_small_day);
        setDayOnClickListener();
    }

    public void toStateMonth() {
        this.state = month;
        this.dateRange
                .setBackgroundResource(R.mipmap.rectangle_blue_small_month);
        setMonthOnClickListener();
    }

    public interface OnStateChangeListener {
        void toStateDay();

        void toStateMonth();
    }

    public void setOnStateChangeListener(OnStateChangeListener oscl) {
        this.mOnStateChangeListener = oscl;
    }

}
