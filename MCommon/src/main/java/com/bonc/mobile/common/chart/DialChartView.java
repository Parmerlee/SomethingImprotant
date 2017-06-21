
package com.bonc.mobile.common.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.xclcharts.chart.DialChart;
import org.xclcharts.common.MathHelper;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotAttrInfo;
import org.xclcharts.view.GraphicalView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;

import com.bonc.mobile.common.util.NumberUtil;
import com.bonc.mobile.common.util.UIUtil;

public class DialChartView extends GraphicalView {
    DialChart chart = new DialChart();

    public DialChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public DialChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DialChartView(Context context) {
        super(context);
        initView();
    }

    void initView() {
    }

    public void setData(List<String> kedu, List<String> colors, double value, String info) {
        List<Double> l = NumberUtil.toDoubleList(kedu);
        int count = l.size() - 1;
        float rper = MathHelper.getInstance().div(1, count);
        double vper = 0;
        List<Float> ringPercentage = new ArrayList<Float>();
        for (int i = 0; i < count; i++) {
            ringPercentage.add(rper);
            if (value >= l.get(i) && value <= l.get(i + 1)) {
                vper = rper * i + (value - l.get(i)) / (l.get(i + 1) - l.get(i)) * rper;
            }
        }
        if(value<=l.get(0))vper=0;
        else if(value>=l.get(count))vper=1;
        List<Integer> rcolor = new ArrayList<Integer>();
        Random ran = new Random();
        for (int i = 0; i < count; i++) {
            if (i < colors.size()){
                rcolor.add(Color.parseColor(colors.get(i)));
            }
            else
                rcolor.add(ran.nextInt(0xffffff) + 0xff000000);
        }
        chart.addStrokeRingAxis(0.9f, 0.75f, ringPercentage, rcolor);

        chart.addInnerTicksAxis(0.75f, kedu);

        chart.getPlotAxis().get(1).getTickLabelPaint().setColor(Color.BLACK);
        chart.getPlotAxis().get(1).getTickLabelPaint()
                .setTextSize(UIUtil.fromDPtoPX(getContext(), 8));
        chart.getPlotAxis().get(1).getTickMarksPaint().setColor(Color.BLACK);
        chart.getPlotAxis().get(1).hideAxisLine();
        chart.getPlotAxis().get(1).setDetailModeSteps(3);

        // 设置当前百分比
        chart.getPointer().setPercentage((float) vper);
        chart.getPointer().setLength(0.72f);
        chart.getPointer().setPointerStyle(XEnum.PointerStyle.TRIANGLE);
        chart.getPointer().getPointerPaint().setColor(Color.rgb(247, 181, 81));
        chart.getPointer().getPointerPaint().setStyle(Style.FILL);
        chart.getPointer().getBaseCirclePaint().setColor(Color.rgb(76, 94, 112));
        chart.getPointer().setBaseRadius(UIUtil.fromDPtoPX(getContext(), 5));

        PlotAttrInfo plotAttrInfo = chart.getPlotAttrInfo();
        Paint paint = new Paint(chart.getPlotAxis().get(1).getTickLabelPaint());
        paint.setTextSize(UIUtil.fromDPtoPX(getContext(), 12));
        plotAttrInfo.addAttributeInfo(XEnum.Location.BOTTOM, info, 0.4f, paint);
    }

    @Override
    public void render(Canvas canvas) {
        try {
            chart.render(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        chart.setChartRange(w, h);
    }
}
