
package com.bonc.mobile.common.kpi;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;

import com.bonc.mobile.common.util.NumberUtil;

public class TrendViewBuilder  {
    String[] titles = new String[] {
        "null"
    };
    double maxValue = 0;
    double minValue = 0;
    double duliangs[];// 12个月的value
    double[] weidus;

    public GraphicalView buildView(Context context) {
        GraphicalView view = ChartFactory.getLineChartView(context,
                buildDataset(titles, weidus, duliangs), getRenderer(context));
        view.ignoreAllTouchEvent();
        return view;
    }

    private XYMultipleSeriesDataset buildDataset(String[] titles, double[] weidus,
            double duliangs[]) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            XYSeries series = new XYSeries(titles[i], 0);
            for (int k = 0; k < weidus.length; k++)
                series.add(weidus[k], duliangs[k]);
            dataset.addSeries(series);
        }
        return dataset;
    }

    // 设置Y轴 最大的值
    private void setMaxYValue() {
        maxValue = NumberUtil.getMaxMinNumber(duliangs)[0];
    }

    // 设置Y轴 最小的值
    private void setMinYValue() {
        minValue = NumberUtil.getMaxMinNumber(duliangs)[1];
    }

    private XYMultipleSeriesRenderer getRenderer(Context context) {
        setMaxYValue(); // 设置Y轴最大值
        setMinYValue();// 设置Y轴最小值
        int[] colors = new int[] {
            0xff165aa0
        };// 折线的颜色
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        // 设置各种标题字体大小
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(12); // 标注字
        renderer.setLegendTextSize(15);
        if (weidus.length > 15) {// 日指标
            // 设置点的类型
            renderer.setPointSize(2f);
            for (int i = 0; i < colors.length; i++) {
                XYSeriesRenderer r = new XYSeriesRenderer();
                r.setColor(colors[i]);
                r.setPointStyle(PointStyle.CIRCLE);
                r.setLineWidth(1.5f);
                renderer.addSeriesRenderer(r);
            }
        } else {// 月指标
                // 设置点的类型
            renderer.setPointSize(3f);
            for (int i = 0; i < colors.length; i++) {
                XYSeriesRenderer r = new XYSeriesRenderer();
                r.setColor(colors[i]);
                r.setPointStyle(PointStyle.CIRCLE);
                r.setLineWidth(2f);
                renderer.addSeriesRenderer(r);
            }
        }

        for (int i = 0; i < renderer.getSeriesRendererCount(); i++)
            ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);// 实心圆点
        // 设置 x,y轴刻度，显示标题内容
        renderer.setMargins(new int[] {
                NumberUtil.DpToPx(context, 20), 0, NumberUtil.DpToPx(context, 14), 0
        });
        renderer.setXLabels(0);// 设置X轴显示的刻度标签的个数
        renderer.setBarSpacing(0.5);
        renderer.setZoomEnabled(false);
        renderer.setZoomButtonsVisible(false);
        renderer.setXAxisMin(0.5);// 设置X轴的最小值为0.5
        renderer.setXAxisMax(weidus.length + 0.5);
        renderer.setYAxisMax(maxValue);
        renderer.setYAxisMin(minValue);// 设置Y轴的最小值,默认为0
        // 设置显示or不显示的内容
        renderer.setShowGrid(false);// 设置是否在图表中显示网格
        renderer.setShowLabels(false);
        renderer.setShowLegend(false);
        renderer.setShowAxes(false);
        renderer.setDisplayChartValues(false);
        renderer.setInScroll(true);
        // 最后加入点的坐标
        for (int i = 0; i < weidus.length; i++)
            renderer.addXTextLabel(i + 1, Double.toString(weidus[i]) + "月");
        return renderer;
    }

    public void setData(List<String> trendData) {
        int len = 0;
        if (trendData != null) {
            for (int i = 0; i < trendData.size(); i++) {
                if (trendData.get(i) != null)
                    len++;
            }
            duliangs = new double[len];
            weidus = new double[len];
            int index = 0;
            for (int i = 0; i < trendData.size(); i++) {
                if (trendData.get(i) != null) {
                    duliangs[index] = NumberUtil.changeToDouble(trendData.get(i));
                    weidus[index] = index + 1;
                    index++;
                }
            }
        }
    }
}
