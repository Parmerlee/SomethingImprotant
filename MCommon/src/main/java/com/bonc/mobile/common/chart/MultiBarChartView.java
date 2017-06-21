
package com.bonc.mobile.common.chart;

import java.util.Random;

import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.util.AttributeSet;

public class MultiBarChartView extends BarChartView {
    static final int[] colorsLib = new int[] {
            0xff72b84c, 0xff3cb2ea, 0xffffb400, 0xffbf0707, 0xffa112d0
    };

    public MultiBarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiBarChartView(Context context) {
        super(context);
    }

    @Override
    protected void buildDataset() {
        dataset = new XYMultipleSeriesDataset();
        for (int i = 0; i < data.seriesDataX.size(); i++) {
            double[] d = data.seriesDataX.get(i);
            CategorySeries bar_series = new CategorySeries("");
            for (int j = 0; j < d.length; j++)
                bar_series.add(d[j]);
            dataset.addSeries(bar_series.toXYSeries());
        }
    }

    @Override
    protected void buildSeriesRenderer() {
        Random ran = new Random();
        for (int i = 0; i < data.seriesDataX.size(); i++) {
            SimpleSeriesRenderer rend = new SimpleSeriesRenderer();
            if (i < colorsLib.length)
                rend.setColor(colorsLib[i]);
            else
                rend.setColor(ran.nextInt(0xffffff) + 0xff000000);
            renderer.addSeriesRenderer(rend);
        }
    }
}
