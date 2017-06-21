
package com.bonc.mobile.common.chart;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.bonc.mobile.common.util.NumberUtil;

/**
 * @author sunwei
 */
public class LineChartView extends LinearLayout {
    XYMultipleSeriesDataset dataset;
    XYMultipleSeriesRenderer renderer;
    SimpleChartData data;

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChartView(Context context) {
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
        return ChartFactory.getLineChartView(getContext(), dataset, renderer);
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
        XYSeriesRenderer render = new XYSeriesRenderer();
        render.setColor(0xff165aa0);
        render.setPointStyle(PointStyle.CIRCLE);
        renderer = new XYMultipleSeriesRenderer(1);
        renderer.addSeriesRenderer(render);
        renderer.setXLabelsColor(0xff000000);
        renderer.setYLabelsPadding(5);
        renderer.setYLabelsColor(0, 0xff000000);
        renderer.setYLabelsAlign(Align.RIGHT, 0);
        renderer.setGridColor(0xff7ca6c7);
        renderer.setMargins(new int[] {
                5, 20, 5, 20
        });
        renderer.setPanEnabled(false);
        renderer.setZoomEnabled(false);
        renderer.setInScroll(true);
        renderer.setShowLegend(false);
        renderer.setShowAxes(false);

        renderer.setXAxisMin(0, 0);

        double maxYValue;
        maxYValue = NumberUtil.getMaxNumber(data.seriesData);
        maxYValue = maxYValue + (Math.abs(maxYValue) / 7); // 让左边刻度线高出数字的最大值
        renderer.setYAxisMin(0, 0);
        renderer.setYAxisMax(maxYValue, 0);
    }
}
