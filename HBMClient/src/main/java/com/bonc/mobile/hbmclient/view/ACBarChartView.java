package com.bonc.mobile.hbmclient.view;

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
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.util.LogUtil;
import com.bonc.mobile.hbmclient.view.BarChartView.BarGraphData;

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

	public String getmUnit() {
		return mUnit;
	}

	public void setmUnit(String mUnit) {
		this.mUnit = mUnit;
	}

	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

	public ACBarChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ACBarChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ACBarChartView(Context context, BarGraphData[] datas) {
		super(context);
		// TODO Auto-generated constructor stub

		initChart(datas);
	}

	public ACBarChartView(Context context, BarGraphData[] datas, String unit) {
		super(context);
		// TODO Auto-generated constructor stub
		setmUnit(unit);
		initChart(datas);
	}

	public void setData(BarGraphData[] datas) {

		initChart(datas);
	}

	public void setData(List<Double> v, List<String> cat, String unit) {
		setmUnit(unit);
		BarGraphData[] datas = new BarGraphData[v.size()];
		for (int i = 0; i < v.size(); i++) {
			datas[i] = new BarGraphData(i * 500, v.get(i), cat.get(i));
		}
		setData(datas);
	}

	public void setDataRate(int rate) {
		mDataRate = rate;
	}

	private void initChart(BarGraphData[] datasF) {

		mFormat = NumberFormat.getInstance();
		mValueView = new ACBarChartDValueView(getContext());
		mCompare = new LinkedList<SeriesSelection>();
		mFormat.setMaximumFractionDigits(2);
		mFormat.setGroupingUsed(false);

		mRenderer.setYLabelsAlign(Align.CENTER, 0);
		mRenderer.setAxisTitleTextSize(16);
		mRenderer.setChartTitleTextSize(20);
		mRenderer.setLabelsTextSize(15);
		mRenderer.setLegendTextSize(15);
		mRenderer.setOrientation(Orientation.HORIZONTAL);
		mRenderer.setMarginsColor(Color.TRANSPARENT);
		mRenderer.setXLabelsAngle(-40f);
		mRenderer.setXAxisMax(-67.5f);
		mRenderer.setBarSpacing(0.2);
		mRenderer.setAxesColor(0x00000000);
		mRenderer.setXLabelsColor(0xff000000);
		mRenderer.setYLabelsColor(0, 0xff000000);
		// mUnit=datas[0].get;
		CategorySeries series = new CategorySeries("单位：千元");
		changeToRealData(datasF, mUnit);
		for (int i = 0; i < datasF.length; i++) {
			double deltaY = (datasF[i].getValueY()) / mDataRate;
			series.add(datasF[i].getStringY(), deltaY);

			/*
			 * if (datas[i].getValueY() > mMaxY) { mMaxY = deltaY; }
			 */

			mRenderer.addXTextLabel(i + 1, datasF[i].getDesc());
		}
		XYSeriesRenderer renderer = new XYSeriesRenderer();
		renderer.setColor(0xff1eaef1);
		renderer.setDefaultBarLabelMode(0);
		renderer.setGradientEnabled(true);

		// double mMinYT =/* mMinY>0?0:*/mMinY;
		renderer.setGradientStart(15000, 0xff1950da);
		renderer.setGradientStop(-15000, 0xff1eaef1);
		renderer.setShadowColor(0xff000000);
		renderer.setShowBarShadow(true);
		mRenderer.addSeriesRenderer(renderer);
		// renderer.setColor(Color.rgb(0,171,245));
		renderer.setChartValuesFormat(mFormat);
		TypedValue tp = new TypedValue();
		getResources().getValue(R.dimen.default_chart_view_textsize, tp, false);
		TypedValue chart_margin = new TypedValue();
		getResources().getValue(R.dimen.default_chart_margin_top, chart_margin,
				false);
		renderer.setChartValuesTextSize(tp.getDimension(new DisplayMetrics()) + 3);
		mRenderer.setLabelsTextSize(tp.getDimension(new DisplayMetrics()) + 5);
		int margin = (int) chart_margin.getDimension(new DisplayMetrics());
		mRenderer.setMargins(new int[] { margin, 30, 20, 20 });
		mRenderer.setLabelFormat(mFormat);
		mDataset.addSeries(series.toXYSeries());
		mChart = new BarChart(mDataset, mRenderer, Type.DEFAULT);
		mChart.setCategorys(null);
		mGraphicalView = new GraphicalView(getContext(), mChart);
		mGraphicalView.ignoreAllTouchEvent();
		mRenderer.setShowLegend(false);

		mRenderer.setYAxisMax(mMaxY * 1.1);
		if (Math.abs(mMaxY) < 1000)
			renderer.setDisplayChartValues(true);
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
		mValueView.setTextPivotsY(margin + 10);
		addView(mValueView);
		addView(mGraphicalView);
		mTitleView = inflate(getContext(), R.layout.ac_line_chart_title_layout,
				null);
		addView(mTitleView);
		View titlev = findViewById(R.id.ac_title_layout);
		View v1 = titlev.findViewById(R.id.ac_chart_line1_tv);
		v1.setVisibility(View.GONE);
		View v2 = titlev.findViewById(R.id.ac_chart_line2_tv);
		v2.setVisibility(View.GONE);

		/*
		 * View mUnitView=inflate(getContext(), R.layout.unit_text_view, null);
		 * TextView unit_text =
		 * (TextView)mUnitView.findViewById(R.id.unit_text);
		 * unit_text.setText(mUnit); addView(mUnitView);
		 */
		// invalidate();
	}

	private void changeToRealData(BarGraphData[] datas, String unit) {
		// TODO Auto-generated method stub
		double dMax = 0;
		double dMin = 0;// = datas[0].getValueY();
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
		// TODO Auto-generated method stub

		SeriesSelection ss = null;
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			Point p = new Point((int) event.getX(), event.getY()/*
																 * getMeasuredHeight
																 * ()-mRenderer.
																 * getMargins
																 * ()[2]-50
																 */);
			LogUtil.info(
					"ss",
					"ss "
							+ (getMeasuredHeight() - mRenderer.getMargins()[2] - 50)
							+ " " + event.getY());
			ss = mChart.getSeriesAndPointForScreenCoordinate(p);
			break;
		default:
			break;
		}

		if (ss != null) {
			double[] transition = mChart.toScreenPoint(new double[] {
					ss.getXValue(), ss.getValue() });
			LogUtil.info("ss",
					getClass().toString() + " " + mTitleView.getBottom() + " "
							+ mTitleView.getLeft());
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
							sec1.getXValue(), sec1.getValue() });
					double[] transition2 = mChart.toScreenPoint(new double[] {
							sec2.getXValue(), sec2.getValue() });
					double v1 = sec1.getValue();
					double v2 = sec2.getValue();

					float maxY = (float) (v1 > v2 ? transition1[1]
							: transition2[1]);
					float minY = (float) (v1 > v2 ? transition2[1]
							: transition1[1]);
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
						double[] transitionO = mChart
								.toScreenPoint(new double[] { minV.getXValue(),
										0 });
						mValueView.setLinePivotsY((float) transitionO[1]);
						mValueView.setTextPivotsY(110);
					}

					mValueView.setUnitString(mUnit);
					mValueView.setLinePivotsX((float) transition1[0],
							(float) transition2[0]);
					String ns = mFormat.format(Math.abs(v1 - v2));
					mValueView.setNumberString(ns);
					mValueView.invalidate();
					// Toast.makeText(getContext(), ""+Math.abs(v1-v2),
					// Toast.LENGTH_SHORT).show();
				} else {
					mValueView.setVisibility(GONE);
				}
			}

			Iterator<SeriesSelection> iterator = mCompare.iterator();
			while (iterator.hasNext()) {
				mRenderer.getSeriesRendererAt(0).addPeculiarBarSet(
						(int) iterator.next().getXValue() - 1, 0xff00446e);
			}

		}

		return super.onTouchEvent(event);
	}

	public void setLineTitle(String s) {
		TextView tv = (TextView) findViewById(R.id.ac_chart_title_tv);
		s = s + "(单位:" + mUnit + ")";
		if (tv != null) {
			tv.setText(s);
		}
	}
}
