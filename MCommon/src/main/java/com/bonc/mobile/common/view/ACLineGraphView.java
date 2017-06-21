
package com.bonc.mobile.common.view;

import java.text.NumberFormat;

import org.achartengine.GraphicalView;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.util.NumberUtil;
import com.bonc.mobile.common.util.StringUtil;

/***
 * 柱状图
 * @author Lenevo
 *
 */
public class ACLineGraphView extends FrameLayout {
    private FlingTask mTask;
    private MotionCursorView mLineView;
    private String[] mCursorStrings;
    private Bitmap mArrowBmp;
    private Bitmap mDotBmp;
    private int mSize, mXYSelectRadius = 0;
    private int mCurCursorIndex;
    private Paint mTextPaint, mLinePaint;
    private NumberFormat mFormat;
    private XYMultipleSeriesRenderer mRenderer;
    private XYMultipleSeriesDataset mDataset;
    private XYSeriesRenderer mLine1Render;
    private GraphicalView mChartView;
    private OnBrickMoveListener mMoveListener;
    private LineChart mChart;
    private float mMotionX, mDownX;
    private float mValueX, mValueY;
    String mUnit;

    public ACLineGraphView(Context context) {
        super(context);
    }

    public ACLineGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(String[] dateString, double[] dataLine, String[] brickStrings,String unit) {
        Context context=getContext();
        removeAllViews();
        mUnit = StringUtil.nullToString(unit);
        mCursorStrings = brickStrings;
        mArrowBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.motioncursor);
        mDotBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.blue_fimbriated);
        setClickable(true);
        setFocusable(true);
        setLongClickable(true);
        mLineView = new MotionCursorView(getContext());
        mLineView.setVisibility(View.GONE);
        mTextPaint = new Paint();
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setTextSize(NumberUtil.DpToPx(context, 9));
        mTextPaint.setAntiAlias(true);
        mLinePaint = new Paint();
        mLinePaint.setColor(Color.rgb(10, 93, 191));
        mRenderer = new XYMultipleSeriesRenderer();
        mDataset = new XYMultipleSeriesDataset();
        mFormat = NumberFormat.getInstance();
        mFormat.setGroupingUsed(false);
        mFormat.setMaximumFractionDigits(2);
        mLine1Render = new XYSeriesRenderer();
        mLine1Render.setPointStyle(PointStyle.CIRCLE);
        mLine1Render.setDisplayChartValuesDistance(1);
        mLine1Render.setLineWidth(3);
        mLine1Render.setPointStrokeWidth(3);
        mLine1Render.setDisplayChartValues(false);
        mLine1Render.setFillPoints(true);
        mLine1Render.setDisplayChartValuesDistance(25);
        mLine1Render.setColor(0xff006dd1);
        mRenderer.addSeriesRenderer(mLine1Render);
        mRenderer.setLabelsTextSize(NumberUtil.DpToPx(context, 9));
        mRenderer.setShowLegend(false);
        mRenderer.setPointSize(5f);
        mRenderer.setXLabelsAngle(-67.5f);
        initData(dateString, dataLine);
        mChart = new LineChart(mDataset, mRenderer);
        mRenderer.setSelectableXBuffer(13);
        mRenderer.setSelectableYBuffer(300);
        mRenderer.setAxesColor(0xffe2e2e2);
        mRenderer.setXLabelsColor(0xff000000);
        mRenderer.setXLabels(0);
        mRenderer.setYAxisAlign(Align.RIGHT, 0);
        mRenderer.setYLabelsColor(0, 0xff000000);
        mRenderer.setShowLabels(true);
        mRenderer.setYLabelsAlign(Align.CENTER, 0);
        mRenderer.setMargins(new int[] {
                35, 0, 0, 30
        });
        mChartView = new GraphicalView(getContext(), mChart);
        mChartView.ignoreAllTouchEvent();
        addView(mChartView);
        addView(mLineView);
    }

    void initData(String[] dateString, double[] dataLine) {
        mSize = dataLine.length;
        double scale;
        if (mUnit.contains("%")) {
            scale = 0.01;
        } else {
            scale = NumberUtil.getScale(dataLine);
        }
        XYSeries seri1 = new XYSeries("");
        for (int i = 0; i < dataLine.length; i++) {
            seri1.add(i, dataLine[i] / scale);
        }
        mDataset.addSeries(0, seri1);
        mRenderer.setXAxisMin(-0.5);
        mRenderer.setXAxisMax(dataLine.length + 0.5);
        mRenderer.setYAxisMin(seri1.getMinY());
        mRenderer.setYAxisMax(seri1.getMaxY());
        for (int i = 0; i < dateString.length; i++) {
            mRenderer.addXTextLabel(i, dateString[i]);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mRenderer!=null&&mXYSelectRadius == 0) {
            mXYSelectRadius = getMeasuredWidth() / (mSize + 1) / 2;
            mRenderer.setSelectableXBuffer(mXYSelectRadius + 2);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mLineView == null)
            return super.onTouchEvent(event);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mLineView.setVisibility(View.VISIBLE);
                mMotionX = event.getX();
                mDownX = mMotionX;
                dealTouchEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                mMotionX = event.getX();
                dealTouchEvent(event);
                mLineView.invalidate();
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mTask != null) {
                    mTask.cancel(true);
                }
                SeriesSelection ss = mChartView.getCurrentSeriesAndPoint();
                if (ss != null) {
                    if (mMoveListener != null)
                        mMoveListener.onStop(ss.getPointIndex());
                    double[] screenPoint = mChart.toScreenPoint(new double[] {
                            ss.getXValue(), ss.getValue()
                    });
                    mTask = new FlingTask();
                    mTask.execute((int) screenPoint[0]);
                } else {
                    double[] screenPoint = mChart.toScreenPoint(new double[] {
                            mDataset.getSeriesAt(0).getMaxX(), 0
                    });
                    mTask = new FlingTask();
                    mTask.execute((int) screenPoint[0]);
                }
                break;
            default:

                break;
        }

        return super.onTouchEvent(event);
    }

    private class MotionCursorView extends View {
        private float mmBitmapHeight = mArrowBmp.getHeight();
        private float mmBitmapWidth = mArrowBmp.getWidth();
        private Paint mmBmpPaint;

        public MotionCursorView(Context context) {
            super(context);
            mmBmpPaint = new Paint();
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            getLayoutParams().height = LayoutParams.MATCH_PARENT;
            getLayoutParams().width = LayoutParams.MATCH_PARENT;
            super.onLayout(changed, left, top, right, bottom);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawLine(mMotionX, 0, mMotionX, getMeasuredHeight(), mLinePaint);
            canvas.drawBitmap(mArrowBmp, mMotionX - mmBitmapWidth / 2, getMeasuredHeight()
                    - mmBitmapHeight, mmBmpPaint);
            int len = mCursorStrings[mCurCursorIndex].length();
            int offset = len * 9;
            canvas.drawText(mCursorStrings[mCurCursorIndex], mMotionX - offset / 2,
                    getMeasuredHeight() - 6, mTextPaint);
            canvas.drawBitmap(mDotBmp, mValueX - 6, mValueY - 6, mmBmpPaint);
        }
    }

    private class FlingTask extends AsyncTask<Integer, Integer, Void> {
        float mmStep;
        int mmTargetX;

        @Override
        protected Void doInBackground(Integer... params) {
            mmTargetX = params[0];
            mmStep = (mMotionX - (float) mmTargetX) / 4;

            try {
                for (int i = 0; i < 4; i++) {
                    Thread.sleep(50);
                    publishProgress(i);
                }
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mMotionX -= mmStep;
            mLineView.invalidate();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mMotionX = mmTargetX;
            mLineView.invalidate();
        }
    }

    public void setOnBrickMoveListener(OnBrickMoveListener l) {
        mMoveListener = l;
    }

    private void dealTouchEvent(MotionEvent event) {
        MotionEvent mce = MotionEvent.obtain(event);
        mce.setAction(MotionEvent.ACTION_DOWN);
        mChartView.onTouchEvent(mce);
        SeriesSelection ss = mChartView.getCurrentSeriesAndPoint();
        if (ss != null) {
            double[] screenPoint = mChart.toScreenPoint(new double[] {
                    ss.getXValue(), ss.getValue()
            });
            mValueX = (float) screenPoint[0];
            mValueY = (float) screenPoint[1];
            mCurCursorIndex = ss.getPointIndex();
            if (mMoveListener != null)
                mMoveListener.onMove(ss.getPointIndex());
        }
    }

    /**
     * 砖头移动监听
     * 
     * @author Administrator
     */
    public interface OnBrickMoveListener {

        /**
         * @param graphIndex 曲线的索引
         * @param graphDataIndex 曲线的第几条数据
         * @param data 该条数据
         */
        public void onMove(int graphDataIndex);

        public void onStop(int graphDataIndex);
    }

}
