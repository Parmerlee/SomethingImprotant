package com.bonc.mobile.common.chart;

import java.lang.reflect.Field;

import org.xclcharts.chart.BarChart;
import org.xclcharts.renderer.bar.FlatBar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class BarChart2 extends BarChart {
    
    public BarChart2() {
        try{
            Field f=BarChart.class.getDeclaredField("mFlatBar");
            f.setAccessible(true);
            f.set(this, new RoundBar());
        }
        catch(Exception e){
        }
        defaultAxisSetting();
    }
}

class RoundBar extends FlatBar {

    @Override
    public boolean renderBar(float left, float top, float right, float bottom, Canvas canvas) {
        if (Float.compare(top, bottom) == 0)
            return true;
        RectF rect = new RectF(left, bottom, right, top);
        getBarPaint().setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rect, 10, 10, getBarPaint());
        return true;
    }

}
