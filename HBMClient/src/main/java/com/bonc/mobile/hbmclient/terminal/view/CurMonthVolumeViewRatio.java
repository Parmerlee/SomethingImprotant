package com.bonc.mobile.hbmclient.terminal.view;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.CombinedXYChart;
import org.achartengine.chart.LineChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.bonc.mobile.hbmclient.R;

public class CurMonthVolumeViewRatio extends FrameLayout {
	public static final int BARW = 20;
	protected GraphicalView mChartView;
	private final static String[] TYPE_OP = { BarChart.TYPE, LineChart.TYPE,
			LineChart.TYPE };
	private final static String[] TYPE_BL = { BarChart.TYPE, LineChart.TYPE, };
	private double[] mThirdDatas;
	private Button mButton;
	private XYMultipleSeriesDataset mDataset;
	private XYMultipleSeriesRenderer mRenderer;
	private XYSeries mFirstLineSeries = null;
	private XYSeriesRenderer mBarRender;
	private XYSeriesRenderer mLine2Render;
	private ImageView mRightArrowView;
	private CombinedXYChart mChart;
	private NumberFormat mFormat = new DecimalFormat("##0'%'");
	private boolean mSingle = true;

	public CurMonthVolumeViewRatio(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
		initData();
	}

	protected void init() {
		mRightArrowView = new ImageView(getContext());
		mFormat.setMaximumFractionDigits(0);
		mRightArrowView.setImageDrawable(getResources().getDrawable(
				R.mipmap.a_1_jiantou));
		mRenderer = new XYMultipleSeriesRenderer(2);
		mRenderer.setMarginsColor(Color.TRANSPARENT);
		mRenderer.setShowLegend(false);
		mRenderer.setInScroll(true);
		mBarRender = new XYSeriesRenderer();
		mBarRender.setColor(Color.rgb(16, 75, 215));
		mBarRender.setGradientEnabled(true);
		mBarRender.setGradientStart(15000, 0xff1950da);
		mBarRender.setGradientStop(0, 0xff1eaef1);
		mBarRender.setShadowColor(0xff000000);
		mBarRender.setShowBarShadow(true);

		mBarRender.setDisplayChartValues(true);
		mBarRender.setChartValuesSpacing(10);
		mBarRender.setDisplayChartValuesDistance(0);
		TypedValue tp = new TypedValue();
		getResources().getValue(R.dimen.default_chart_view_textsize, tp, false);
		mBarRender
				.setChartValuesTextSize(tp.getDimension(new DisplayMetrics()) + 3);
		mRenderer.addSeriesRenderer(mBarRender);
		mRenderer.setXLabels(0);
		mRenderer.setXLabelsColor(0xff000000);
		mRenderer.setLabelsTextSize(13);
		mRenderer.setMargins(new int[] { 20, 40, 0, 30 });
		mRenderer.setShowAxes(false);
		mRenderer.setYLabelsColor(0, 0xff000000);
		mRenderer.setYLabelsColor(1, 0x00000000);
		mRenderer.setYLabelsAlign(Align.RIGHT, 0);
		mRenderer.setPointSize(7f);
		mRenderer.setShowLegend(false);
		mRenderer.setBarWidth(1);
		mRenderer.setZoomEnabled(false, false);
		mRenderer.setBarSpacing(0.3f);
		mRenderer.setYLabelsAlign(Align.CENTER);
		mLine2Render = new XYSeriesRenderer();
		// mLine2Render.setChartValuesFormat(mFormat);
		mLine2Render.setDisplayChartValues(true);
		mLine2Render.setChartValuesSpacing(10);
		mLine2Render.setChartValuesTextSize(tp
				.getDimension(new DisplayMetrics()) + 3);
		mLine2Render.setDisplayChartValuesDistance(0);
	}

	public void addThirdLineDatas(double[] datas) {
		mThirdDatas = datas;
	}

	public XYMultipleSeriesRenderer getMultipleRenderer() {
		return mRenderer;
	}

	public XYSeriesRenderer getLine1Renderer() {
		return mBarRender;
	}

	public XYSeriesRenderer getLine2Renderer() {
		return mLine2Render;
	}

	public void setThirdLineDisplay() {
		double[] datas = mThirdDatas;
		XYSeries series = mDataset.getSeriesAt(0);
		XYSeries series_third = new XYSeries("");
		series_third.clear();
		for (int i = 0; i < Math.min(series.getItemCount(), datas.length); i++) {
			series_third.add(series.getX(i), datas[i]);
		}
		if (mDataset.getSeriesCount() > 1) {
			mDataset.removeSeries(1);
		}
		mDataset.addSeries(1, series_third);
		rectifyZoompan();
		mChartView.invalidate();
	}

	public void initData() {
		mDataset = new XYMultipleSeriesDataset();
		mDataset.addSeries(new XYSeries(""));
		mRenderer.clearYTextLabels();
		removeAllViews();
		mFormat.setMaximumFractionDigits(2);
		if (mDataset.getSeriesAt(0).getItemCount() < 12) {
			for (int i = mDataset.getSeriesAt(0).getItemCount(); i < 12; i++) {
				mDataset.getSeriesAt(0).add(i, 0);
			}
		}
		;
		if (mRenderer.getSeriesRendererCount() < 2) {
			mRenderer.addSeriesRenderer(1, mLine2Render);
		}

		if (mDataset.getSeriesCount() < 2) {
			mDataset.addSeries(1, new XYSeries(""));
		}
		mChart = new CombinedXYChart(mDataset, mRenderer, TYPE_BL);
		mChartView = new GraphicalView(getContext(), mChart);
		mChartView.setDrawingCacheEnabled(true);
		mChartView.ignoreAllTouchEvent();
		addView(mChartView);
		addView(mRightArrowView);
		LayoutParams lp = (LayoutParams) mRightArrowView
				.getLayoutParams();
		lp.bottomMargin = 16;
		lp.rightMargin = 0;
		lp.gravity = Gravity.BOTTOM | Gravity.RIGHT;
		lp.height = 67;
		lp.width = 67;
		mRightArrowView.setScaleType(ScaleType.CENTER);
		mRenderer.setShowCustomTextGrid(true);
		mRenderer.setGridColor(0xff7ca6c7);
		mRenderer.setYLabelsPadding(5);
		rectifyZoompan();

		mChartView.invalidate();
	}

	public void rectifyZoompan() {
		int daycount = mDataset.getSeriesAt(0).getItemCount();
		mRenderer.clearXTextLabels();
		for (int i = 0; i < daycount; i++) {
			mRenderer.addXTextLabel(mDataset.getSeriesAt(0).getX(i), (i + 1)
					+ "");
		}
		double max_dual;
		double min_dual;
		if (mDataset.getSeriesCount() < 2) {
			max_dual = mDataset.getSeriesAt(0).getMaxY();
			min_dual = mDataset.getSeriesAt(0).getMinY();
		} else {
			max_dual = Math.max(mDataset.getSeriesAt(0).getMaxY(), mDataset
					.getSeriesAt(1).getMaxY());
			min_dual = Math.min(mDataset.getSeriesAt(0).getMinY(), mDataset
					.getSeriesAt(1).getMinY());
		}

		mRenderer.setYAxisMax(max_dual * 1.2, 0);
		mRenderer.setYAxisMax(max_dual * 1.2, 1);
		mRenderer.setYAxisMin(Math.min(0, min_dual - Math.abs(min_dual) * 0.1),
				0);
		mRenderer.setYAxisMin(Math.min(0, min_dual - Math.abs(min_dual) * 0.1),
				1);

		mRenderer.setXAxisMin(mDataset.getSeriesAt(0).getMinX() - 0.3);
		mRenderer.setXAxisMin(mDataset.getSeriesAt(0).getMinX() - 0.3, 1);
		mRenderer.setXAxisMax(daycount + 0.3);
		mRenderer.setXAxisMax(daycount + 0.3, 1);

	}

	public XYSeries getFirstLineSeries() {
		return mFirstLineSeries;
	}

	public void setFirstLineSeries(double[] data) {
		XYSeries series = new XYSeries("");
		series.clear();
		for (int i = 0; i < data.length; i++) {
			series.add(i, data[i]);
		}
		setFirstLineSeries(series);
	}

	public void setFirstLineSeries(XYSeries firstLineSeries) {
		this.mFirstLineSeries = firstLineSeries;
		mDataset.removeSeries(0);
		mDataset.addSeries(0, mFirstLineSeries);
		rectifyZoompan();
		mChartView.invalidate();
	}

}
