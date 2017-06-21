
package com.bonc.mobile.common.chart;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.bonc.mobile.common.util.NumberUtil;

/**
 * @author sunwei
 */
public class BarChartView extends LinearLayout {
    XYMultipleSeriesDataset dataset;

    XYMultipleSeriesRenderer renderer;

    SimpleChartData data;

    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarChartView(Context context) {
        super(context);
    }

    public void setData(SimpleChartData data) {
        removeAllViews();
        if (data == null) {
            return;
        }
        this.data = data;
        addView(getView());
    }

    protected View getView() {
        buildDataset();
        buildRenderer();
        GraphicalView v = ChartFactory.getBarChartView(getContext(), dataset, renderer,
                BarChart.Type.DEFAULT);
        v.ignoreAllTouchEvent();
        return v;
    }

    protected void buildDataset() {
        dataset = new XYMultipleSeriesDataset();
        CategorySeries bar_series = new CategorySeries("");
        for (int i = 0; i < data.seriesData.length; i++) {
            bar_series.add(data.seriesData[i]);
        }
        dataset.addSeries(bar_series.toXYSeries());
    }

    protected void buildRenderer() {
        renderer = new XYMultipleSeriesRenderer(1);
        buildSeriesRenderer();
        renderer.setXLabelsColor(0xff000000);
        renderer.setYLabelsPadding(5);
        renderer.setYLabelsColor(0, 0xff000000);
        renderer.setBarSpacing(0.3f);
        renderer.setGridColor(0xff7ca6c7);
        Context context = getContext();
        renderer.setMargins(new int[] {
                NumberUtil.DpToPx(context, 5), NumberUtil.DpToPx(context, 15), 0,
                NumberUtil.DpToPx(context, 15)
        });
        renderer.setLabelsTextSize(NumberUtil.DpToPx(context, 9));
        renderer.setPanEnabled(false);
        renderer.setZoomEnabled(false);
        renderer.setInScroll(true);
        renderer.setShowLegend(false);
        renderer.setShowAxes(false);
        renderer.setShowCustomTextGrid(true);

        renderer.setXLabels(0);
        renderer.setXAxisMin(0, 0);
        if (data.cats != null) {
            for (int i = 0; i < data.cats.length; i++) {
                renderer.addXTextLabel(i + 1, data.cats[i]);
            }
        }
    }

    void setYRange(double[] array, int s) {
        double maxYValue;
        maxYValue = NumberUtil.getMaxNumber(array);
        maxYValue = maxYValue + (Math.abs(maxYValue) / 7); // 让左边刻度线高出数字的最大值
        renderer.setYAxisMin(0, s);
        renderer.setYAxisMax(maxYValue, s);
    }
    
    protected void buildSeriesRenderer() {
        XYSeriesRenderer mBarRender = new XYSeriesRenderer();
        mBarRender.setColor(Color.rgb(16, 75, 215));
        mBarRender.setGradientEnabled(true);
        mBarRender.setGradientStart(15000, 0xff1950da);
        mBarRender.setGradientStop(0, 0xff1eaef1);
        mBarRender.setShadowColor(0xff000000);
        mBarRender.setShowBarShadow(true);
        renderer.addSeriesRenderer(mBarRender);
    }
}
