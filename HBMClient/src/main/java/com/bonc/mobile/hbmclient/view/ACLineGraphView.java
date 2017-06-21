package com.bonc.mobile.hbmclient.view;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

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
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.view.LineGraphView.OnBrickMoveListener;

public class ACLineGraphView extends FrameLayout {
	private FlingTask mTask;
	private String mUnit;
	private int mUnitScale = 1;
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
	private XYSeriesRenderer mLine2Render;
	private GraphicalView mChartView;
	private OnBrickMoveListener mMoveListener;
	private LineChart mChart;
	private float mMotionX, mDownX;
	private float mValueX, mValueY;

	public ACLineGraphView(Context context, String[] dateString,
			String[] brickStrings, double[] dataLine1, double[] dataLine2) {
		super(context);
		// TODO Auto-generated constructor stub
		mCursorStrings = brickStrings;
		mArrowBmp = BitmapFactory.decodeResource(getResources(),
				R.mipmap.motioncursor);
		mDotBmp = BitmapFactory.decodeResource(getResources(),
				R.mipmap.blue_fimbriated);
		setClickable(true);
		setFocusable(true);
		setLongClickable(true);
		mLineView = new MotionCursorView(getContext());
		mTextPaint = new Paint();
		mTextPaint.setColor(0xffffffff);
		mTextPaint.setTextSize(17);
		mTextPaint.setAntiAlias(true);
		mLinePaint = new Paint();
		mLinePaint.setColor(Color.rgb(10, 93, 191));
		mRenderer = new XYMultipleSeriesRenderer();
		mDataset = new XYMultipleSeriesDataset();
		mFormat = NumberFormat.getInstance();
		mFormat.setGroupingUsed(false);
		NumberFormat labelFormat = new DecimalFormat("0.####E0");
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
		mLine2Render = new XYSeriesRenderer();
		mLine2Render = new XYSeriesRenderer();
		mLine2Render.setPointStyle(PointStyle.CIRCLE);
		mLine2Render.setDisplayChartValuesDistance(1);
		mLine2Render.setLineWidth(3);
		mLine2Render.setPointStrokeWidth(3);
		mLine2Render.setDisplayChartValues(false);
		mLine2Render.setFillPoints(true);
		mLine2Render.setDisplayChartValuesDistance(25);
		mLine2Render.setColor(0xffaeaeae);
		// mRenderer.addSeriesRenderer(mLine2Render);
		mRenderer.addSeriesRenderer(mLine1Render);
		TypedValue tp = new TypedValue();
		getResources().getValue(R.dimen.default_chart_view_textsize, tp, false);
		mRenderer.setLabelsTextSize(tp.getDimension(new DisplayMetrics()) + 3);
		mRenderer.setShowLegend(false);
		mRenderer.setPointSize(5f);
		mRenderer.setYLabels(0);
		mRenderer.setXLabelsAngle(-67.5f);
		initData(dateString, dataLine1, dataLine2);
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
		mRenderer.setMargins(new int[] { 35, 0, 0, 30 });
		// mRenderer.setLabelFormat(labelFormat);
		mChartView = new GraphicalView(getContext(), mChart);
		mChartView.ignoreAllTouchEvent();
		addView(mChartView);
		addView(mLineView);
	}

	public void initData(String[] dateString, double[] dataLine1,
			double[] dataLine2) {
		XYSeries seri1, seri2;
		double maxY = 0, minY = 0;
		seri1 = new XYSeries("");
		int i;
		for (i = 0; i < dataLine1.length; i++) {
			if (i < dataLine1.length) {
				seri1.add(i, dataLine1[i]);
				if (dataLine1[i] > maxY) {
					maxY = dataLine1[i];
				}
				if (dataLine1[i] < minY) {
					minY = dataLine1[i];
				}
			}
		}
		mSize = i;
		String unit = "";
		double scale = 1;
		double scalechinese = 1;
		while (scale * 10 < maxY) {
			scale *= 10;
			if (scale >= 100000000) {
				scalechinese = 100000000;
				unit = "亿";
			} else if (scale >= 10000) {
				scalechinese = 10000;
				unit = "万";
			}
		}
		int les;
		for (les = 1; les * scale < maxY; les++) {

		}
		mRenderer.setYAxisMax(les * scale, 0);

		double labelvalue = les * scale / 5;
		for (int j = 1; j < 5; j++) {
			mRenderer.addYTextLabel(j * labelvalue, j * labelvalue
					/ scalechinese + unit, 0);
		}
		mRenderer.setXAxisMin(-0.5);
		double setAA = maxY * 1.05;
		mRenderer.setYAxisMax(setAA);
		mRenderer.setYAxisMin(minY * 0.95);
		mRenderer.setXAxisMax(i + 0.5);
		mDataset.addSeries(0, seri1);
		// mDataset.addSeries(1, seri2);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		if (mXYSelectRadius == 0) {
			mXYSelectRadius = getMeasuredWidth() / (mSize + 1) / 2;
			mRenderer.setSelectableXBuffer(mXYSelectRadius + 2);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
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
				mMoveListener.onStop(ss.getPointIndex());
				double[] screenPoint = mChart.toScreenPoint(new double[] {
						ss.getXValue(), ss.getValue() });
				mTask = new FlingTask();
				mTask.execute((int) screenPoint[0]);
			} else {
				int la = mDataset.getSeriesAt(0).getItemCount() - 1;
				double[] screenPoint = mChart.toScreenPoint(new double[] {
						mDataset.getSeriesAt(0).getMaxX(), 0 });
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
			// TODO Auto-generated constructor stub
			setVisibility(View.GONE);
			mmBmpPaint = new Paint();
		}

		@Override
		protected void onLayout(boolean changed, int left, int top, int right,
				int bottom) {
			// TODO Auto-generated method stub
			getLayoutParams().height = LayoutParams.MATCH_PARENT;
			getLayoutParams().width = LayoutParams.MATCH_PARENT;
			super.onLayout(changed, left, top, right, bottom);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			canvas.drawLine(mMotionX, 0, mMotionX, getMeasuredHeight(),
					mLinePaint);
			canvas.drawBitmap(mArrowBmp, mMotionX - mmBitmapWidth / 2,
					getMeasuredHeight() - mmBitmapHeight, mmBmpPaint);
			int len = mCursorStrings[mCurCursorIndex].length();
			int offset = len * 9;
			canvas.drawText(mCursorStrings[mCurCursorIndex], mMotionX - offset
					/ 2, getMeasuredHeight() - 6, mTextPaint);
			canvas.drawBitmap(mDotBmp, mValueX - 6, mValueY - 6, mmBmpPaint);
		}
	}

	private class FlingTask extends AsyncTask<Integer, Integer, Void> {
		float mmStep;
		int mmTargetX;

		@Override
		protected Void doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			mmTargetX = params[0];
			mmStep = (mMotionX - (float) mmTargetX) / 4;

			try {
				for (int i = 0; i < 4; i++) {
					Thread.sleep(50);
					publishProgress(i);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			mMotionX -= mmStep;
			mLineView.invalidate();
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
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
					ss.getXValue(), ss.getValue() });
			mValueX = (float) screenPoint[0];
			mValueY = (float) screenPoint[1];
			mCurCursorIndex = ss.getPointIndex();
			mMoveListener.onMove(ss.getPointIndex());
		}
	}

	static public double[] getTestData() {
		Random ran = new Random();
		double[] ret = new double[12];
		for (int i = 0; i < 12; i++) {
			int billion = ran.nextInt(100);
			ret[i] = billion * 1000000;
		}
		return ret;
	}

	static public String[] getTestDate() {
		String[] ret = new String[12];
		for (int i = 0; i < 12; i++) {
			ret[i] = "" + (i + 1);
		}
		return ret;
	}
}
