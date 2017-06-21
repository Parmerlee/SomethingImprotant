package com.bonc.mobile.hbmclient.view;

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
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;

public class ACLineChartView extends FrameLayout {

	/** The main dataset that includes all the series that go into a chart. */
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	/** The main renderer that includes all the renderers customizing a chart. */
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	private GraphicalView mChartView;
	private XYSeriesRenderer mLastRenderer;
	private double mMaxY = 0, mMaxX = 0;
	private String mCommonUnit = null;
	private String[] mDateTexts;
	private NumberFormat format = NumberFormat.getInstance();

	public ACLineChartView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ACLineChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ACLineChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ACLineChartView(Context context, String[] dateTexts,
			double[] curMonthData, double[] lastMonthData) {
		super(context);
		// TODO Auto-generated constructor stub
		initData(dateTexts, curMonthData, lastMonthData);
		initGraphics();
	}

	String unit;
	String mUnit;
	double valusScale = 1;

	public ACLineChartView(Context context, String[] dateTexts,
			double[] curMonthData, double[] lastMonthData, String unit) {
		super(context);
		// TODO Auto-generated constructor stub
		this.unit = unit;
		initData(dateTexts, curMonthData, lastMonthData);

		initGraphics();
	}

	public void setData(String[] dateTexts, double[] curMonthData,
			double[] lastMonthData, String unit) {
		this.unit = unit;
		initData(dateTexts, curMonthData, lastMonthData);
		initGraphics();
	}

	public void initData(String[] dateTexts, double[] curMonthData,
			double[] lastMonthData) {
		XYSeries seri2 = new XYSeries("");
		XYSeries seri1 = new XYSeries("");

		changeToRealData(curMonthData);

		for (int i = 0; i < lastMonthData.length; i++) {
			if (mUnit.contains("%")) {
				seri2.add(i, lastMonthData[i] / valusScale, true);
				seri1.add(i, curMonthData[i] / valusScale, true);
			} else {
				seri2.add(i, lastMonthData[i] / valusScale);
				seri1.add(i, curMonthData[i] / valusScale);
			}
		}
		mDateTexts = dateTexts;
		mDataset.addSeries(seri2);
		mDataset.addSeries(seri1);
	}

	private void changeToRealData(double[] datas) {
		// TODO Auto-generated method stub

		double dCount = 0;
		for (int i = 0; i < datas.length; i++) {
			double deltaY = (datas[i]);

			dCount += Math.abs(datas[i]);

		}
		mUnit = unit;
		if (unit.contains("%")) {
			valusScale = 0.01;

		} else {
			int scale = 1;
			// double averageC=dCount/datas.length;
			double averageC = dCount;
			if (datas.length != 0) {
				averageC = dCount / datas.length;
			}
			while (scale * 10 < Math.abs(averageC)) {
				scale *= 10;

			}
			if (scale >= 100000000) {
				valusScale = 100000000;
				mUnit = "亿" + unit;
			} else if (scale >= 10000) {
				valusScale = 10000;
				mUnit = "万" + unit;
			}

		}

	}

	public void initGraphics() {
		mRenderer.setApplyBackgroundColor(false);
		mRenderer.setGridColor(0xff8F8F8F);
		XYSeriesRenderer renderer = new XYSeriesRenderer();
		mRenderer.setMarginsColor(Color.TRANSPARENT);
		mRenderer.setShowLegend(false);
		mRenderer.setAxesColor(0xffe2e2e2);
		mRenderer.setXLabels(0);
		mRenderer.setPointSize(7f);
		mRenderer.setIsRightAxis(true);
		TypedValue tp = new TypedValue();
		getResources().getValue(R.dimen.default_chart_view_textsize, tp, false);
		TypedValue chart_margin = new TypedValue();
		getResources().getValue(R.dimen.default_chart_margin_top, chart_margin,
				false);
		int margin = (int) chart_margin.getDimension(new DisplayMetrics());
		renderer.setChartValuesTextSize(tp.getDimension(new DisplayMetrics()) + 3);
		mRenderer.setLabelsTextSize(tp.getDimension(new DisplayMetrics()) + 5);
		mRenderer.setMargins(new int[] { margin, 45, 20, 10 });
		// set some renderer properties
		FillOutsideLine fill = FillOutsideLine.BOUNDS_ALL;
		fill.setGradient(true);
		fill.setColor2(0x0000000);
		fill.setColor(0x8000b2bf);
		renderer.setPointStyle(PointStyle.CIRCLE);
		renderer.setDisplayChartValuesDistance(1);
		renderer.setLineWidth(3);
		renderer.setPointStrokeWidth(3);
		// renderer.setDisplayChartValues(true);
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
		mChartView = ChartFactory.getLineChartView(getContext(), mDataset,
				mRenderer);
		addView(mChartView);
		mChartView.getLayoutParams().height = LayoutParams.MATCH_PARENT;
		mChartView.getLayoutParams().width = LayoutParams.MATCH_PARENT;
		mChartView.setDrawingCacheBackgroundColor(0xffffffff);
		double ymax = 0, ymin = 0;
		if (mDataset.getSeriesCount() > 1) {
			ymax = Math.max(mDataset.getSeriesAt(0).getMaxY(), mDataset
					.getSeriesAt(1).getMaxY());
			ymin = Math.min(mDataset.getSeriesAt(0).getMinY(), mDataset
					.getSeriesAt(1).getMinY());
		} else {
			ymax = mDataset.getSeriesAt(0).getMaxY();
			ymin = mDataset.getSeriesAt(0).getMinY();
		}
		if (ymax > 0)
			mRenderer.setYAxisMax(ymax * 1.1);
		else {
			mRenderer.setYAxisMax(ymax * 0.9);
		}
		if (ymin > 0)
			mRenderer.setYAxisMin(ymin * 0.9);
		else {
			mRenderer.setYAxisMin(ymin * 1.1);
		}
		mRenderer.setXAxisMin(-0.2);
		mRenderer.setXAxisMax(Math.max(mDataset.getSeriesAt(1).getItemCount(),
				mDataset.getSeriesAt(0).getItemCount()) - 0.8);
		mRenderer.setPanEnabled(false);
		mRenderer.setZoomEnabled(false);
		mRenderer.setYLabelsAlign(Align.CENTER);
		mRenderer.setAxisTitleTextSize(16);
		mRenderer.setLegendTextSize(15);
		mRenderer.setLabelsTextSize(15);
		mRenderer.setLabelFormat(format);
		for (int i = 0; i < mDataset.getSeries()[mDataset.getSeriesCount() - 1]
				.getItemCount(); i++) {
			mRenderer
					.addXTextLabel(mDataset.getSeries()[mDataset
							.getSeriesCount() - 1].getX(i), mDateTexts[i]);
		}
		mRenderer.setXLabelsColor(0xff000000);
		mRenderer.setYLabelsColor(0, 0xff000000);
		View vs = inflate(getContext(), R.layout.ac_line_chart_title_layout,
				null);
		addView(vs);
		View titlev = findViewById(R.id.ac_title_layout);
		LayoutParams lp = (LayoutParams) titlev.getLayoutParams();
		lp.gravity = Gravity.TOP;
		mChartView.ignoreAllTouchEvent();
		postInvalidate();

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
