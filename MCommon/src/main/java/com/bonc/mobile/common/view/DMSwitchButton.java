
package com.bonc.mobile.common.view;

import com.bonc.mobile.common.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/**
 * @author sunwei
 */
public class DMSwitchButton extends Button {
    protected int dateType;
    protected OnSwitchListener switchListener;

    public interface OnSwitchListener {
        void onSwitch(DMSwitchButton view, int dateType);
    }

    public DMSwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DMSwitchButton(Context context) {
        super(context);
        init();
    }

    protected void init() {
        setDateType(0);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateType(dateType == 0 ? 1 : 0);
                if (switchListener != null)
                    switchListener.onSwitch((DMSwitchButton) v, dateType);
            }
        });
    }

    void updateBackground() {
        setBackgroundResource(dateType == 0 ? R.mipmap.rectangle_blue_small_day
                : R.mipmap.rectangle_blue_small_month);
    }

    public void setDateType(int dateType) {
        this.dateType = dateType;
        updateBackground();
    }

    public int getDateType() {
        return dateType;
    }

    public String getDateTypeString() {
        return dateType == 0 ? "D" : "M";
    }

    public void setOnSwitchListener(OnSwitchListener switchListener) {
        this.switchListener = switchListener;
    }

}
