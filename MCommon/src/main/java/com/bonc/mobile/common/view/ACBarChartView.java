
package com.bonc.mobile.common.view;

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.Point;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.util.NumberUtil;

/***
 * 柱状图
 * @author Lenevo
 *
 */
public class ACBarChartView extends FrameLayout {
    private BarChart mChart;
    private ACBarChartDValueView mValueView;
    private View mTitleView;
    private GraphicalView mGraphicalView;
    NumberFormat mFormat;
    private double mDataRate = 1;
    private double mMaxY = 0;
    private double mMinY = 0;
    private Queue<SeriesSelection> mCompare;
    private String mUnit;
    int highlightColor=0xffff0000;//0xff00446e

    public void setUnit(String mUnit) {
        this.mUnit = mUnit;
    }

    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

    public ACBarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ACBarChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setData(List<Double> v, List<String> cat, String unit) {
        setUnit(unit);
        BarGraphData[] datas = new BarGraphData[v.size()];
        for (int i = 0; i < v.size(); i++) {
            datas[i] = new BarGraphData(i * 500, v.get(i), cat.get(i));
        }
        setData(datas);
    }

    public void setData(BarGraphData[] datasF) {
        Context context=getContext();
        mFormat = NumberFormat.getInstance();
        mValueView = new ACBarChartDValueView(getContext());
        mCompare = new LinkedList<SeriesSelection>();
        mFormat.setMaximumFractionDigits(2);
        mFormat.setGroupingUsed(false);

        mRenderer.setYLabelsAlign(Align.CENTER, 0);
        mRenderer.setAxisTitleTextSize(16);
        mRenderer.setChartTitleTextSize(20);
        mRenderer.setLabelsTextSize(NumberUtil.DpToPx(context, 9));
        mRenderer.setLegendTextSize(15);
        mRenderer.setOrientation(Orientation.HORIZONTAL);
        mRenderer.setMarginsColor(Color.TRANSPARENT);
        mRenderer.setXLabelsAngle(-40f);
        mRenderer.setXAxisMax(-67.5f);
        mRenderer.setBarSpacing(0.2);
        mRenderer.setAxesColor(0x00000000);
        mRenderer.setXLabelsColor(0xff000000);
        mRenderer.setYLabelsColor(0, 0xff000000);
        CategorySeries series = new CategorySeries("");
        changeToRealData(datasF, mUnit);
        for (int i = 0; i < datasF.length; i++) {
            double deltaY = (datasF[i].getValueY()) / mDataRate;
            series.add(datasF[i].getStringY(), deltaY);
            mRenderer.addXTextLabel(i + 1, datasF[i].getDesc());
        }
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setColor(0xff1eaef1);
        renderer.setDefaultBarLabelMode(0);
        renderer.setGradientEnabled(true);

        renderer.setGradientStart(15000, 0xff1950da);
        renderer.setGradientStop(-15000, 0xff1eaef1);
        renderer.setShadowColor(0xff000000);
        renderer.setShowBarShadow(true);
        mRenderer.addSeriesRenderer(renderer);
        // renderer.setColor(Color.rgb(0,171,245));
        renderer.setChartValuesFormat(mFormat);
        renderer.setChartValuesTextSize(14);
        mRenderer.setMargins(new int[] {
                100, 30, 20, 20
        });
        mRenderer.setLabelFormat(mFormat);
        mDataset.addSeries(series.toXYSeries());
        mChart = new BarChart(mDataset, mRenderer, Type.DEFAULT);
        mChart.setCategorys(null);
        mGraphicalView = new GraphicalView(getContext(), mChart);
        mGraphicalView.ignoreAllTouchEvent();
        mRenderer.setShowLegend(false);

        mRenderer.setYAxisMax(mMaxY * 1.1);
        renderer.setDisplayChartValues(false);
        if (mMinY < 0)
            mRenderer.setYAxisMin(mMinY * 1.1);
        else {
            mRenderer.setYAxisMin(mMinY * 0.9);
        }
        mRenderer.setXAxisMin(0);
        mRenderer.setAxesColor(0xffe2e2e2);
        mRenderer.setXLabels(0);
        mRenderer.setXAxisMax(datasF.length + 1);
        mValueView.setVisibility(View.GONE);
        mValueView.setTextPivotsY(110);
        addView(mValueView);
        addView(mGraphicalView);
        mTitleView = inflate(getContext(), R.layout.ac_line_chart_title_layout, null);
        addView(mTitleView);
        View titlev = findViewById(R.id.ac_title_layout);
        View v1 = titlev.findViewById(R.id.ac_chart_line1_tv);
        v1.setVisibility(View.GONE);
        View v2 = titlev.findViewById(R.id.ac_chart_line2_tv);
        v2.setVisibility(View.GONE);

    }

    public void setDataRate(int rate) {
        mDataRate = rate;
    }

    private void changeToRealData(BarGraphData[] datas, String unit) {
        double dMax = 0;
        double dMin = 0;
        double dCount = 0;
        for (int i = 0; i < datas.length; i++) {
            double deltaY = (datas[i].getValueY());

            if (deltaY > dMax) {
                dMax = deltaY;
            }
            if (i == 0) {
                dMin = datas[0].getValueY();
            } else {

                if (deltaY < dMin) {
                    dMin = deltaY;
                }
            }
            dCount += Math.abs(datas[i].getValueY());

        }
        if (unit.contains("%")) {
            mDataRate = 0.01;
        } else {
            int scale = 1;
            double averageC = dCount;
            if (datas.length != 0) {
                averageC = dCount / datas.length;
            }
            while (scale * 10 < Math.abs(averageC)) {
                scale *= 10;

            }
            if (scale >= 100000000) {
                mDataRate = 100000000;
                unit = "亿" + unit;
            } else if (scale >= 10000) {
                mDataRate = 10000;
                unit = "万" + unit;
            }
            mUnit = unit;
        }
        mMaxY = dMax / mDataRate;
        mMinY = dMin / mDataRate;
        if (mMinY > 0 && !unit.contains("%"))
            mMinY = 0;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        SeriesSelection ss = null;
        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
            Point p = new Point(event.getX(), event.getY());
            ss = mChart.getSeriesAndPointForScreenCoordinate(p);
        }

        if (ss != null) {
            showLabelView(ss);
            //showDeltaView(ss);
        }

        return super.onTouchEvent(event);
    }

    void showLabelView(SeriesSelection ss){
        double[] transition = mChart.toScreenPoint(new double[] {
                ss.getXValue(), ss.getValue()
        });
        mValueView.setLinePivotsX((float) transition[0], (float) transition[0]);
        mValueView.setTextPivotsY((float) transition[1]);
        mValueView.setNumberString(mFormat.format(ss.getValue()));
        mValueView.setTextOnly(true);
        mValueView.invalidate();
        mValueView.setVisibility(View.VISIBLE);
        mRenderer.getSeriesRendererAt(0).clearPeculiarBarSet();
        mRenderer.getSeriesRendererAt(0).addPeculiarBarSet(
                (int) ss.getXValue() - 1, highlightColor);
        mGraphicalView.invalidate();
    }
    
    void showDeltaView(SeriesSelection ss){
        mCompare.offer(ss);
        mGraphicalView.invalidate();
        if (mCompare.size() >= 3) {
            mCompare.poll();
        }
        if (mCompare.size() >= 2) {
            mRenderer.getSeriesRendererAt(0).clearPeculiarBarSet();
            Iterator<SeriesSelection> iterator = mCompare.iterator();
            SeriesSelection sec1 = iterator.next();
            SeriesSelection sec2 = iterator.next();
            if (sec1.getXValue() != sec2.getXValue()) {
                double[] transition1 = mChart.toScreenPoint(new double[] {
                        sec1.getXValue(), sec1.getValue()
                });
                double[] transition2 = mChart.toScreenPoint(new double[] {
                        sec2.getXValue(), sec2.getValue()
                });
                double v1 = sec1.getValue();
                double v2 = sec2.getValue();

                float maxY = (float) (v1 > v2 ? transition1[1] : transition2[1]);
                float minY = (float) (v1 > v2 ? transition2[1] : transition1[1]);
                mValueView.setVisibility(View.VISIBLE);
                if (v1 * v2 > 0) {
                    if (v1 < 0) {

                        mValueView.setLinePivotsY(maxY);
                        mValueView.setTextPivotsY(getMeasuredHeight() - 55);

                    } else {
                        mValueView.setLinePivotsY(minY);
                        mValueView.setTextPivotsY(110);
                    }
                } else {
                    SeriesSelection minV = v1 > v2 ? sec2 : sec1;
                    double[] transitionO = mChart.toScreenPoint(new double[] {
                            minV.getXValue(), 0
                    });
                    mValueView.setLinePivotsY((float) transitionO[1]);
                    mValueView.setTextPivotsY(110);
                }

                mValueView.setLinePivotsX((float) transition1[0], (float) transition2[0]);
                mValueView.setNumberString(mFormat.format(Math.abs(v1 - v2)));
                mValueView.invalidate();
            } else {
                mValueView.setVisibility(GONE);
            }
        }

        Iterator<SeriesSelection> iterator = mCompare.iterator();
        while (iterator.hasNext()) {
            mRenderer.getSeriesRendererAt(0).addPeculiarBarSet(
                    (int) iterator.next().getXValue() - 1, highlightColor);
        }
    }
    
    public void setLineTitle(String s) {
        TextView tv = (TextView) findViewById(R.id.ac_chart_title_tv);
        s = s + "(单位:" + mUnit + ")";
        if (tv != null) {
            tv.setText(s);
        }
    }

    public static class BarGraphData {
        private double valueX;
        private double valueY;
        private String desc;

        public BarGraphData(double valueX, double valueY, String desc) {
            this.valueX = valueX;
            this.valueY = valueY;
            this.desc = desc;
        }

        public double getValueX() {
            return valueX;
        }

        public double getValueY() {
            return valueY;
        }

        public String getDesc() {
            return desc;
        }

        public String getStringY() {
            if (valueY == (int) valueY)
                return (int) valueY + "";
            else
                return NumberUtil.format(valueY);
        }
    }

}

class ACBarChartDValueView extends View {
    private float mPanelPointX1, mPanelPointX2, mPanelPointY;
    private float mPanelRungY = 110;
    private String mNumberString = "0";
    private int mColor = Color.rgb(0, 84, 139);
    private int mTextSize = 13;
    private Paint mPaint;
    boolean textOnly;

    public ACBarChartDValueView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setColor(mColor);
            mPaint.setTextSize(NumberUtil.DpToPx(getContext(), mTextSize));
            mPaint.setAntiAlias(true);
        }
        int offset = mNumberString.length() * 6;
        if(textOnly){
            canvas.drawText(mNumberString, Math.abs(mPanelPointX1) - offset,
                    mPanelRungY - 4, mPaint);
        }
        else{
            canvas.drawLine(mPanelPointX1, mPanelPointY, mPanelPointX1, mPanelRungY, mPaint);
            canvas.drawLine(mPanelPointX1, mPanelRungY, mPanelPointX2, mPanelRungY, mPaint);
            canvas.drawLine(mPanelPointX2, mPanelRungY, mPanelPointX2, mPanelPointY, mPaint);
            canvas.drawText(mNumberString, Math.abs((mPanelPointX2 + mPanelPointX1) / 2) - offset,
                    mPanelRungY - 4, mPaint);
        }
    }

    public void setLinePivotsX(float pivotX1, float pivotX2) {
        mPanelPointX1 = pivotX1;
        mPanelPointX2 = pivotX2;
    }

    public void setNumberString(String s) {
        mNumberString = s;
    }

    public void setLinePivotsY(float pivotY) {
        mPanelPointY = pivotY;
    }

    public void setTextPivotsY(float py) {
        mPanelRungY = py;
    }

    public void setTextOnly(boolean textOnly) {
        this.textOnly = textOnly;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        getLayoutParams().height = LayoutParams.MATCH_PARENT;
        getLayoutParams().width = LayoutParams.MATCH_PARENT;
    }
}
