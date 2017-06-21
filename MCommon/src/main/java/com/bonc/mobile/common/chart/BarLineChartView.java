
package com.bonc.mobile.common.chart;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.xclcharts.chart.BarChart;
import org.xclcharts.chart.BarData;
import org.xclcharts.chart.LineChart;
import org.xclcharts.chart.LineData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.axis.DataAxis;
import org.xclcharts.view.GraphicalView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;

import com.bonc.mobile.common.util.NumberUtil;

/**
 * @author sunwei
 */
public class BarLineChartView extends GraphicalView {
    BarChart chart;
    LineChart lnChart;

    SimpleChartData data;
    int color1=Color.parseColor("#2dc0e8"),color2=Color.parseColor("#dae1e8");

    public BarLineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BarLineChartView(Context context) {
        super(context);
        init();
    }

    void init(){
        chart = new BarChart2();
        lnChart = new LineChart();
    }
    
    public void setData(SimpleChartData data) {
        this.data = data;
        renderBar();
        renderLine();
        new Thread(new CAnimation1()).start();
        new Thread(new CAnimation2()).start();
    }

    void renderBar() {
        List<String> chartLabels = new ArrayList<String>(Arrays.asList(data.cats));
        List<BarData> chartDataset = new ArrayList<BarData>();
        List<Double> dataSeries = new ArrayList<Double>();
        for (double d : data.seriesData)
            dataSeries.add(d);
        BarData bd = new BarData("", dataSeries, color1);
        chartDataset.add(bd);
        chart.setDataSource(chartDataset);
        chart.setCategories(chartLabels);
        setBar(chart);
        setBar2(chart);
    }

    void setBar(BarChart chart){
        int[] ltrb = getBarLnDefaultSpadding();
        chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);
        chart.getPlotLegend().hide();
        chart.getBar().setBarMaxPxWidth(DensityUtil.dip2px(getContext(), 8));
        DataAxis dataAxis = chart.getDataAxis();
        double[] maxmin = NumberUtil.getMaxMinNumber(data.seriesData);
        double[] maxmin2 = NumberUtil.getMaxMinNumber(data.seriesData2);
        double maxYValue = Math.max(maxmin[0],maxmin2[0]);
        double minYValue = Math.min(maxmin[1],maxmin2[1]);
        dataAxis.setAxisMax(maxYValue);
        dataAxis.setAxisMin(minYValue);
        dataAxis.setAxisSteps((maxYValue*1.1-minYValue) / 4);
        dataAxis.setLabelFormatter(new IFormatterTextCallBack() {
            @Override
            public String textFormatter(String value) {
                double label = Double.parseDouble(value);
                DecimalFormat df = new DecimalFormat("0.#");
                return df.format(label).toString();
            }
        });
    }
    
    void setBar2(BarChart chart){
        chart.getDataAxis().hide();
        chart.getCategoryAxis().hide();
    }
    
    void renderLine() {
        List<String> chartLabels = new ArrayList<String>(Arrays.asList(data.cats));
        List<LineData> chartDataset = new ArrayList<LineData>();
        List<Double> dataSeries = new ArrayList<Double>();
        for (double d : data.seriesData2)
            dataSeries.add(d);
        LineData ld = new LineData("", dataSeries, color2);
        chartDataset.add(ld);
        lnChart.setDataSource(chartDataset);
        lnChart.setCategories(chartLabels);
        int[] ltrb = getBarLnDefaultSpadding();
        lnChart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);
        lnChart.getPlotLegend().hide();
        DataAxis dataAxis = lnChart.getDataAxis();
        double maxYValue = NumberUtil.getMaxNumber(data.seriesData);
        dataAxis.setAxisMax(maxYValue);
        dataAxis.setAxisSteps(maxYValue * 1.1 / 4);
        dataAxis.setLabelFormatter(new IFormatterTextCallBack() {
            @Override
            public String textFormatter(String value) {
                double label = Double.parseDouble(value);
                DecimalFormat df = new DecimalFormat("#0");
                return df.format(label).toString();
            }
        });
        dataAxis.hideTickMarks();
        dataAxis.getTickLabelPaint().setTextSize(DensityUtil.dip2px(getContext(), 9));
        lnChart.getCategoryAxis().hideTickMarks();
        lnChart.getCategoryAxis().getTickLabelPaint()
                .setTextSize(DensityUtil.dip2px(getContext(), 9));
    }

    int[] getBarLnDefaultSpadding() {
        int[] ltrb = new int[4];
        ltrb[0] = DensityUtil.dip2px(getContext(), 35); // left
        ltrb[1] = DensityUtil.dip2px(getContext(), 30); // top
        ltrb[2] = DensityUtil.dip2px(getContext(), 5); // right
        ltrb[3] = DensityUtil.dip2px(getContext(), 10); // bottom
        return ltrb;
    }

    void sleep(long n) {
        try {
            Thread.sleep(n);
        } catch (InterruptedException e) {
        }
    }

    class CAnimation1 implements Runnable {
        @Override
        public void run() {
            int[] padding = getBarLnDefaultSpadding();
            chart.setPadding(padding[0], 20 * 20 + padding[1], padding[2], padding[3]);
            postInvalidate();
            sleep(1000);
            for (int i = 19; i >= 0; i--) {
                chart.setPadding(padding[0], i * 20 + padding[1], padding[2], padding[3]);
                postInvalidate();
                sleep(50);
            }
        }
    }

    class CAnimation2 implements Runnable {
        @Override
        public void run() {
            List<LineData> dataSet = lnChart.getDataSource();
            lnChart.setDataSource(new ArrayList<LineData>());
            postInvalidate();
            sleep(1000);
            int count = dataSet.get(0).getLinePoint().size();
            for (int i = 0; i < count; i++) {
                List<LineData> animationData = new ArrayList<LineData>();
                for (int j = 0; j < dataSet.size(); j++) {
                    LineData org_data = dataSet.get(j);
                    LineData ld=new LineData(org_data.getLineKey(), org_data.getLinePoint()
                            .subList(0, i + 1), org_data.getLineColor());
                    animationData.add(ld);
                    ld.getLinePaint().setStrokeWidth(DensityUtil.dip2px(getContext(), 1));
                    ld.getDotPaint().setColor(color1);
                    ld.setDotRadius(DensityUtil.dip2px(getContext(), 2));
                }
                lnChart.setDataSource(animationData);
                postInvalidate();
                sleep(50);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        chart.setChartRange(w, h);
        lnChart.setChartRange(w, h);
    }

    @Override
    public void render(Canvas canvas) {
        try {
            chart.render(canvas);
            lnChart.render(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
