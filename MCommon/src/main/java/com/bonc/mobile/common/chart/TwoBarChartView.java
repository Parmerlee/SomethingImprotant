package com.bonc.mobile.common.chart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.xclcharts.chart.BarChart;
import org.xclcharts.chart.BarData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.renderer.axis.CategoryAxis;
import org.xclcharts.renderer.axis.DataAxis;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class TwoBarChartView extends BarLineChartView {
    BarChart barChart2,barChartTop;
    
    public TwoBarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TwoBarChartView(Context context) {
        super(context);
    }

    @Override
    void init() {
        chart = new BarChart2();
        barChart2 = new BarChart2();
        barChartTop = new BarChart2();
    }

    @Override
    public void setData(SimpleChartData data) {
        this.data = data;
        renderBar();
        renderBar2();
        renderBarTop();
        invalidate();
    }

    void renderBar2() {
        List<String> chartLabels = new ArrayList<String>(Arrays.asList(data.cats));
        List<BarData> chartDataset = new ArrayList<BarData>();
        List<Double> dataSeries = new ArrayList<Double>();
        for (double d : data.seriesData2)
            dataSeries.add(d);
        BarData bd = new BarData("", dataSeries, color2);
        chartDataset.add(bd);
        barChart2.setDataSource(chartDataset);
        barChart2.setCategories(chartLabels);
        setBar(barChart2);
        DataAxis dataAxis = barChart2.getDataAxis();
        dataAxis.hideTickMarks();
        dataAxis.getTickLabelPaint().setTextSize(DensityUtil.dip2px(getContext(), 8));
        dataAxis.getAxisPaint().setColor(color2);
        CategoryAxis cAxis=barChart2.getCategoryAxis();
        cAxis.hideTickMarks();
        cAxis.getTickLabelPaint()
                .setTextSize(DensityUtil.dip2px(getContext(), 8));
        cAxis.getAxisPaint().setColor(color2);
    }

    void renderBarTop() {
        List<String> chartLabels = new ArrayList<String>(Arrays.asList(data.cats));
        List<BarData> chartDataset = new ArrayList<BarData>();
        List<Double> dataSeries = new ArrayList<Double>();
        for (int i=0;i<data.seriesData.length;i++){
            if(data.seriesData[i]<data.seriesData2[i])
            dataSeries.add(data.seriesData[i]);
            else
                dataSeries.add(0d);
        }
        BarData bd = new BarData("", dataSeries, color1);
        chartDataset.add(bd);
        barChartTop.setDataSource(chartDataset);
        barChartTop.setCategories(chartLabels);
        setBar(barChartTop);
        setBar2(barChartTop);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        chart.setChartRange(w, h);
        barChart2.setChartRange(w, h);
        barChartTop.setChartRange(w, h);
    }

    @Override
    public void render(Canvas canvas) {
        if(data==null)return;
        try {
            chart.render(canvas);
            barChart2.render(canvas);
            barChartTop.render(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
