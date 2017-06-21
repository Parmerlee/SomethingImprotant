
package com.bonc.mobile.common.view;

import java.text.NumberFormat;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer.FillOutsideLine;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.util.NumberUtil;
import com.bonc.mobile.common.util.StringUtil;

/***
 * 折线图
 * @author Lenevo
 *
 */
public class ACLineChartView extends FrameLayout {

    /** The main dataset that includes all the series that go into a chart. */
    protected XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    /** The main renderer that includes all the renderers customizing a chart. */
    protected XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    private GraphicalView mChartView;
    private XYSeriesRenderer mLastRenderer;
    private String[] mDateTexts;
    private NumberFormat format = NumberFormat.getInstance();
    String mUnit;
    double valusScale = 1;
    double ymax = 0, ymin = 0;

    public ACLineChartView(Context context) {
        super(context);
    }

    public ACLineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(String[] dateTexts, double[] curMonthData, double[] lastMonthData,
            String unit) {
        this.mUnit = StringUtil.nullToString(unit);
        initData(dateTexts, curMonthData, lastMonthData);
        initGraphics();
    }

    private void initData(String[] dateTexts, double[] curMonthData, double[] lastMonthData) {
        XYSeries seri2 = new XYSeries("");
        XYSeries seri1 = new XYSeries("");

        if (mUnit.contains("%")) {
            valusScale = 0.01;
        } else {
            valusScale = NumberUtil.getScale(curMonthData);
            mUnit = NumberUtil.getUnit(valusScale) + mUnit;
        }
        for (int i = 0; i < curMonthData.length; i++) {
            if (mUnit.contains("%")) {
                seri1.add(i, curMonthData[i] / valusScale, true);
            } else {
                seri1.add(i, curMonthData[i] / valusScale);
            }
        }
        for (int i = 0; i < lastMonthData.length; i++) {
            if (mUnit.contains("%")) {
                seri2.add(i, lastMonthData[i] / valusScale, true);
            } else {
                seri2.add(i, lastMonthData[i] / valusScale);
            }
        }
        mDateTexts = dateTexts;
        mDataset.addSeries(seri2);
        mDataset.addSeries(seri1);
    }

    private void initGraphics() {
        Context context=getContext();
        mRenderer.setApplyBackgroundColor(false);
        mRenderer.setGridColor(0xff8F8F8F);
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        mRenderer.setMarginsColor(Color.TRANSPARENT);
        mRenderer.setShowLegend(false);
        mRenderer.setAxesColor(0xffe2e2e2);
        mRenderer.setXLabels(0);
        mRenderer.setPointSize(7f);
        mRenderer.setIsRightAxis(true);
        renderer.setChartValuesTextSize(14);
        mRenderer.setMargins(new int[] {
                100, 45, 20, 10
        });
        FillOutsideLine fill = FillOutsideLine.BOUNDS_ALL;
        fill.setGradient(true);
        fill.setColor2(0x0000000);
        fill.setColor(0x8000b2bf);
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setDisplayChartValuesDistance(1);
        renderer.setLineWidth(3);
        renderer.setPointStrokeWidth(3);
        renderer.setFillPoints(true);
        renderer.setDisplayChartValuesDistance(25);
        renderer.setColor(0xff006dd1);
        renderer.setChartValuesFormat(format);
        renderer.addFillOutsideLine(fill);
        mLastRenderer = new XYSeriesRenderer();
        mLastRenderer.setPointStyle(PointStyle.CIRCLE);
        mLastRenderer.setDisplayChartValuesDistance(1);
        mLastRenderer.setLineWidth(3);
        mLastRenderer.setPointStrokeWidth(3);
        mLastRenderer.setFillPoints(true);
        mLastRenderer.setDisplayChartValues(false);
        mLastRenderer.setColor(0xffaeaeae);
        mLastRenderer.setChartValuesFormat(format);
        if (mDataset.getSeriesCount() > 1) {
            mRenderer.addSeriesRenderer(mLastRenderer);
        }
        mRenderer.addSeriesRenderer(renderer);
        mChartView = ChartFactory.getLineChartView(getContext(), mDataset, mRenderer);
        addView(mChartView);
        mChartView.getLayoutParams().height = LayoutParams.MATCH_PARENT;
        mChartView.getLayoutParams().width = LayoutParams.MATCH_PARENT;
        mChartView.setDrawingCacheBackgroundColor(0xffffffff);
        setYAxis();
        mRenderer.setXAxisMin(-0.2);
        mRenderer.setXAxisMax(Math.max(mDataset.getSeriesAt(1).getItemCount(), mDataset
                .getSeriesAt(0).getItemCount()) - 0.8);
        mRenderer.setPanEnabled(false);
        mRenderer.setZoomEnabled(false);
        mRenderer.setYLabelsAlign(Align.CENTER);
        mRenderer.setAxisTitleTextSize(16);
        mRenderer.setLegendTextSize(15);
        mRenderer.setLabelsTextSize(NumberUtil.DpToPx(context, 9));
        mRenderer.setLabelFormat(format);
        XYSeries s = mDataset.getSeries()[0].getItemCount() >= mDataset.getSeries()[1]
                .getItemCount() ? mDataset.getSeries()[0] : mDataset.getSeries()[1];
        for (int i = 0; i < s.getItemCount(); i++) {
            mRenderer.addXTextLabel(s.getX(i), mDateTexts[i]);
        }
        mRenderer.setXLabelsColor(0xff000000);
        mRenderer.setYLabelsColor(0, 0xff000000);
        View vs = inflate(getContext(), R.layout.ac_line_chart_title_layout, null);
        addView(vs);
        View titlev = findViewById(R.id.ac_title_layout);
        LayoutParams lp = (LayoutParams) titlev.getLayoutParams();
        lp.gravity = Gravity.TOP;
        mChartView.ignoreAllTouchEvent();
        postInvalidate();

    }

    public void setYAxis(String info){
        String[] a=info.split("\\|");
        if(a.length==2){
            ymin=NumberUtil.changeToDouble(a[0]);
            ymax=NumberUtil.changeToDouble(a[1]);
        }
    }
    
    protected void setYAxis(){
        if (ymax == 0 && ymin == 0){
            if (mDataset.getSeriesCount() > 1) {
                ymax = Math.max(mDataset.getSeriesAt(0).getMaxY(), mDataset.getSeriesAt(1).getMaxY());
                ymin = Math.min(mDataset.getSeriesAt(0).getMinY(), mDataset.getSeriesAt(1).getMinY());
            } else {
                ymax = mDataset.getSeriesAt(0).getMaxY();
                ymin = mDataset.getSeriesAt(0).getMinY();
            }
        }
        mRenderer.setYAxisMax(ymax);
        mRenderer.setYAxisMin(ymin);
    }
    
    public void setLineTitle(String s) {
        TextView tv = (TextView) findViewById(R.id.ac_chart_title_tv);
        s = s + "(单位:" + mUnit + ")";
        if (tv != null) {
            tv.setText(s);
        }
    }

    public void setLine1Name(String s) {
        TextView tv = (TextView) findViewById(R.id.ac_chart_line1_tv);
        if (tv != null) {
            tv.setText("● " + s);
        }
    }

    public void setLine2Name(String s) {
        TextView tv = (TextView) findViewById(R.id.ac_chart_line2_tv);
        if (tv != null) {
            tv.setText("● " + s);
        }
    }
}
