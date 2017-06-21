
package com.bonc.mobile.common.chart;

import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author sunwei
 */
public class PieChartView extends LinearLayout {
    static final int[] colorsLib = new int[] {
            0xff72b84c, 0xff3cb2ea, 0xffffb400, 0xffbf0707, 0xffa112d0
    };

    CategorySeries dataset;
    DefaultRenderer renderer;
    SimpleChartData data;
    ChartConfig config;

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieChartView(Context context) {
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

    public void setConfig(ChartConfig config) {
        this.config = config;
    }

    protected View getView() {
        buildDataset();
        buildRenderer();
        return ChartFactory.getModulePieChartView(getContext(), dataset, renderer);
    }

    protected void buildDataset() {
        dataset = new CategorySeries("");
        for (int i = 0; i < data.seriesData.length; i++) {
            dataset.add(data.cats[i], data.seriesData[i]);
        }
    }

    protected void buildRenderer() {
        renderer = new DefaultRenderer();
        // 设置图表的外边框(上/左/下/右)
        renderer.setMargins(new int[] {
                3, 3, 3, 3
        });
        renderer.setPanEnabled(false);
        renderer.setZoomEnabled(false);
        renderer.setInScroll(true);
        renderer.setAntialiasing(true);
        renderer.setClickEnabled(true);
        renderer.setShowLabels(true);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setLabelsColor(Color.BLACK);
        renderer.setDisplayValues(true);
        renderer.setShowLableBack(false);
        renderer.setIsPeculiarLegendTextColor(true);
        Random ran = new Random();
        for (int i = 0; i < data.seriesData.length; i++) {
            SimpleSeriesRenderer rend = new SimpleSeriesRenderer();
            if (i < colorsLib.length)
                rend.setColor(colorsLib[i]);
            else
                rend.setColor(ran.nextInt(0xffffff) + 0xff000000);
            renderer.addSeriesRenderer(rend);
        }
        if (config != null) {
            renderer.setmLegendType(config.legendType);
            renderer.setDisplayValues(config.displayValues);
            renderer.setDrawTextFrame(config.drawTextFrame);
        }
    }
}
