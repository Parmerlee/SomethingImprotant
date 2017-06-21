package com.bonc.mobile.hbmclient.terminal.view;

import java.text.DecimalFormat;
import java.util.Calendar;

import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.CombinedXYChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.terminal.subactivity.CurMonthVolumeActivity;
import com.bonc.mobile.hbmclient.terminal.subactivity.CurrentMonthActivity;

/** 当前月销量数据折线及柱形复合图表的封装，需要构造onDeployData方法否则将报错 */
public abstract class CurMonthVolumeView extends FrameLayout {

	public static final int TYPE_AFFILIATE_LIST = 1;
	public static final int TYPE_SIMPLE = 0;
	public static final int TYPE_CHART = 2;

	private final static double INTERVAL = 40;

	private int mType;// 点击时启动的Activity类型，0为单独图表 1为带右侧滑滚列表的图表
	private DateRangeEnum mCType = DateRangeEnum.DAY;// 指定是数据周期类型为日/月/年/百年等等
	private int mMinimumDays = 12;

	protected GraphicalView mChartView;
	private double[] mThirdDatas;
	private Button mButton;
	private XYMultipleSeriesDataset mDataset;
	private XYMultipleSeriesRenderer mRenderer;
	private XYSeriesRenderer mBarRender;
	private XYSeriesRenderer mLine1Render;
	private XYSeriesRenderer mLine2Render;
	private CombinedXYChart mChart;
	// private NumberFormat mFormat=NumberFormat.getInstance();
	private DecimalFormat mDecimalFormat = new DecimalFormat();
	private boolean mSingle = true;
	private String dateString = "";
	private String[] xLabel;

	/**
	 * 静态重建方法，在你需要将chart传递到新的activity/fragment来显示时，把chart放入intent.extra里然后取出
	 * 用此方法重建图表这样你就不用重新写一遍onDeployData
	 * 
	 * @param context
	 *            上下文建议为activity
	 * @param chart
	 *            获取自另一个CurMonthVolumeView的图表
	 * @return 构成好的CurMonthVolumeView
	 * */
	public static CurMonthVolumeView rebuildInstanceByChart(Context context,
			CombinedXYChart chart) {
		CurMonthVolumeView view = new CurMonthVolumeView(context, chart) {
			@Override
			protected XYMultipleSeriesDataset onDeployData(
					XYMultipleSeriesDataset aDataset) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		return view;
	}

	private final static String[] TYPE_OP = { BarChart.TYPE, LineChart.TYPE,
			LineChart.TYPE };

	/** 标准构造方法，若在实现抽象方法时返回错误的dataset将报错 */
	public CurMonthVolumeView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
		initData();
	}

	int lineW[] = new int[] { 1, 2 };

	public CurMonthVolumeView(Context context, int[] lineW, DateRangeEnum dre) {
		super(context);
		// TODO Auto-generated constructor stub
		this.lineW = lineW;
		this.mCType = dre;
		init();
		initData();
	}

	public void setXLabel(String[] x) {
		this.xLabel = x;
	}

	/** 非标准构造方法，需要传递一个构造好的chart */
	private CurMonthVolumeView(Context context, CombinedXYChart chart) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
		mDataset = chart.getDataset();
		mRenderer = chart.getRenderer();
		mRenderer.setPointSize(6f);
		mChart = new CombinedXYChart(mDataset, mRenderer, TYPE_OP);
		mChartView = new GraphicalView(getContext(), mChart);
		mChartView.ignoreAllTouchEvent();

		addView(mChartView);
	}

	/** 内部初始化方法，将被系统的inflate构造和手动java构造二者调用 */
	protected void init() {
		mRenderer = new XYMultipleSeriesRenderer(3);
		mRenderer.setMarginsColor(Color.TRANSPARENT);
		mRenderer.setInScroll(true);
		mBarRender = new XYSeriesRenderer();
		mBarRender.setColor(Color.rgb(16, 75, 215));
		mBarRender.setGradientEnabled(true);
		mBarRender.setGradientStart(15000, 0xff1950da);
		mBarRender.setGradientStop(0, 0xff1eaef1);
		mBarRender.setShadowColor(0xff000000);
		mBarRender.setShowBarShadow(true);
		mLine1Render = new XYSeriesRenderer();
		mLine1Render.setColor(0xff000000);
		mLine1Render.setPointStyle(PointStyle.CIRCLE);
		mLine1Render.setFillPoints(true);

		mLine1Render.setLineWidth(lineW[0]);

		if (mSingle) {

		} else {

		}
		mRenderer.addSeriesRenderer(mBarRender);
		mRenderer.addSeriesRenderer(mLine1Render);
		mRenderer.setBarSpacing(0.7);
		mRenderer.setXLabels(0);
		mRenderer.setXLabelsColor(0xff000000);
		mRenderer.setLabelsTextSize(13);
		mRenderer.setMargins(new int[] { 5, 40, 0, 30 });
		mRenderer.setShowAxes(false);
		mRenderer.setYAxisMin(0, 0);
		mRenderer.setYAxisMin(0, 1);
		mRenderer.setPointSize(lineW[1]);
		mLine2Render = new XYSeriesRenderer();
	}

	/** 设置表格的标题（位于下方） */
	public void setChartTitle(String s) {
		mRenderer.setXTitle(s);
		invalidate();
	}

	/** 数据初始化方法，可以手动调用 */
	public void initData() {
		mDataset = onDeployData(mDataset);
		mRenderer.clearXTextLabels();
		mRenderer.clearYTextLabels();
		removeAllViews();
		// mFormat.setMaximumFractionDigits(1);
		mDecimalFormat.setMaximumFractionDigits(1);
		if (mDataset.getSeriesAt(0).getItemCount() < mMinimumDays) {
			for (int i = mDataset.getSeriesAt(0).getItemCount(); i < mMinimumDays; i++) {
				mDataset.getSeriesAt(0).add(i + 1, 0);
			}
		}
		if (mRenderer.getSeriesRendererCount() < 3) {

			mRenderer.addSeriesRenderer(2, mLine2Render);
		}
		if (mDataset.getSeriesCount() < 3) {
			mDataset.addSeries(2, new XYSeries(""));
		}
		mChart = new CombinedXYChart(mDataset, mRenderer, TYPE_OP);
		mChartView = new GraphicalView(getContext(), mChart);

		mChartView.setDrawingCacheEnabled(true);
		mChartView.ignoreAllTouchEvent();
		addView(mChartView);

		int daycount = mDataset.getSeriesAt(0).getItemCount();
		int date_space = 1;

		switch (mCType) {
		case MONTH:
			if (xLabel == null || xLabel.length != 12) {
				for (int i = 0; i < daycount; i += date_space) {
					mRenderer.addXTextLabel(mDataset.getSeriesAt(0).getX(i),
							(i + 1) + "");
				}
			} else {
				for (int i = 0; i < daycount; i += date_space) {
					mRenderer.addXTextLabel(mDataset.getSeriesAt(0).getX(i),
							xLabel[i]);
				}
			}
			break;
		case DAY:
			if (xLabel == null || xLabel.length != 30) {
				for (int i = 0; i < daycount; i += date_space) {
					mRenderer.addXTextLabel(mDataset.getSeriesAt(0).getX(i),
							(i + 1) + "");
				}
			} else {
				for (int i = 0; i < daycount; i += date_space) {
					mRenderer.addXTextLabel(mDataset.getSeriesAt(0).getX(i),
							xLabel[i]);
				}
			}
			break;
		default:
			date_space = 5;
			mRenderer.addXTextLabel(mDataset.getSeriesAt(0).getX(0), "1");
			for (int i = date_space - 1; i < daycount; i += date_space) {
				if (i + 1 != 30) {
					mRenderer.addXTextLabel(mDataset.getSeriesAt(0).getX(i),
							(i + 1) + "");
				}
			}
			mRenderer.addXTextLabel(mDataset.getSeriesAt(0).getX(daycount - 1),
					daycount + "");
			break;
		}

		mRenderer.setYLabelsColor(0, 0xff000000);
		mRenderer.setYLabelsColor(1, 0x00000000);
		mRenderer.setYLabelsAlign(Align.RIGHT, 0);
		mRenderer.setShowCustomTextGrid(true);

		mRenderer.setGridColor(0xff7ca6c7);
		mRenderer.setYLabels(0);
		mRenderer.setYLabelsPadding(5);
		DecimalFormat dcf;

		double max_dual = Math.max(mDataset.getSeriesAt(0).getMaxY(), mDataset
				.getSeriesAt(1).getMaxY());
		String unit = "";
		int scale = 1;
		int scalechinese = 1;
		while (scale * 10 < Math.abs(max_dual)) {
			scale *= 10;
			if (scale >= 100000000) {
				scalechinese = 100000000;
				unit = "亿";
			} else if (scale >= 10000) {
				scalechinese = 10000;
				unit = "万";
			}
		}
		double les = max_dual + Math.abs(max_dual) / 7;
		/*
		 * for(les=1;les*scale<max_dual;les++){
		 * 
		 * }
		 */
		mRenderer.setYAxisMax(les, 0);
		mRenderer.setYAxisMax(les, 1);
		float labelvalue = (float) (les / 5);
		if (Math.abs(labelvalue) > 1) {
			mDecimalFormat.applyPattern("#.#");
		} else {
			mDecimalFormat.applyPattern("#.###");
		}
		for (int i = 1; i < 5; i++) {
			String floats = mDecimalFormat
					.format(i * labelvalue / scalechinese);
			mRenderer.addYTextLabel(i * labelvalue, floats + unit, 0);
		}
		// Log.i(" ","les "+les+" unit "+unit+" labelvalue "+labelvalue+" max "+max_dual);
		mRenderer.setXAxisMin(mDataset.getSeriesAt(0).getMinX() - 0.3);
		mRenderer.setXAxisMin(mDataset.getSeriesAt(0).getMinX() - 0.3, 1);
		mRenderer.setShowLegend(false);
		mRenderer.setXAxisMax(daycount + 0.3);
		mRenderer.setXAxisMax(daycount + 0.3, 1);
		// mRenderer.setLabelFormat(dcf);
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				switch (mType) {
				case TYPE_AFFILIATE_LIST:
					intent.setClass(getContext(), CurrentMonthActivity.class);
					intent.putExtra("ctype", mCType);
					intent.putExtra("cdate", dateString);
					break;
				case TYPE_SIMPLE:
					intent.setClass(getContext(), CurMonthVolumeActivity.class);

					break;
				default:
					return;
				}
				CombinedXYChart chartparam = new CombinedXYChart(mDataset,
						mRenderer, TYPE_OP);
				intent.putExtra("chart", chartparam);
				getContext().startActivity(intent);
			}
		});
		invalidate();
	}

	/**
	 * 将第三条线的数据充填进chart 默认不会显示，需要手动设置setThidLineDisplay来显示出特定的数据线条
	 * 
	 * @param datas
	 *            每一个double[]封装一组走势数据
	 */
	public void addThirdLineDatas(double[] datas) {
		mThirdDatas = datas;
	}

	public XYMultipleSeriesRenderer getMultipleRenderer() {
		return mRenderer;
	}

	public XYSeriesRenderer getLine1Renderer() {
		return mLine1Render;
	}

	public XYSeriesRenderer getLine2Renderer() {
		return mLine2Render;
	}

	public XYSeriesRenderer getBarRenderer() {
		return mBarRender;
	}

	public void setThirdLineDisplay() {
		double[] datas = mThirdDatas;
		XYSeries series = mDataset.getSeriesAt(1);
		XYSeries series_third = mDataset.getSeriesAt(2);
		series_third.clear();
		for (int i = 0; i < Math.min(series.getItemCount(), datas.length); i++) {
			series_third.add(series.getX(i), datas[i]);
		}
		invalidate();
	}

	/** 获取点击时的启动类型，0为默认，1为带右侧滑滚列表 */
	public int getType() {
		return mType;
	}

	/** 设置点击时的启动类型 */
	public void setType(int mType) {
		this.mType = mType;
	}

	/** 设置数据周期类型是月还是日或其他 */
	public void setCycleType(DateRangeEnum type) {
		mCType = type;
		int daycount = mDataset.getSeriesAt(0).getItemCount();
		int date_space = 1;
		switch (type) {
		case MONTH:
			mBarRender.setDisplayChartValues(true);
			break;
		case DAY:
			mBarRender.setDisplayChartValues(false);
			break;
		default:
			mBarRender.setDisplayChartValues(false);
			date_space = 5;
			break;
		}
		mRenderer.clearXTextLabels();
		switch (mCType) {
		case MONTH:
			if (xLabel == null || xLabel.length != 12) {
				for (int i = 0; i < daycount; i += date_space) {
					mRenderer.addXTextLabel(mDataset.getSeriesAt(0).getX(i),
							(i + 1) + "");
				}
			} else {
				for (int i = 0; i < daycount; i += date_space) {
					mRenderer.addXTextLabel(mDataset.getSeriesAt(0).getX(i),
							xLabel[i]);
				}
			}
			break;
		case DAY:
			if (xLabel == null || xLabel.length != 30) {
				for (int i = 0; i < daycount; i += date_space) {
					mRenderer.addXTextLabel(mDataset.getSeriesAt(0).getX(i),
							(i + 1) + "");
				}
			} else {
				for (int i = 0; i < daycount; i += date_space) {
					mRenderer.addXTextLabel(mDataset.getSeriesAt(0).getX(i),
							xLabel[i]);
				}
			}
			break;
		}
	}

	private int computeAxisWithData(double max) {
		if (mDataset == null || mRenderer == null) {
			return 1;
		}
		double m = Math.abs(max);
		int log = 10;
		while (m > 100) {
			m = m / 10;
			log *= 10;
		}
		return log;
	}

	public void calculatecComplementary(String date_string) {
		if (date_string.length() < 6)
			return;
		this.dateString = date_string;
		switch (mCType) {
		case MONTH:
			mMinimumDays = 12;
			break;
		default:
			try {
				String months = date_string.substring(4, 6);
				int month = Integer.parseInt(months);
				String year_s = date_string.substring(0, 4);
				int year = Integer.parseInt(year_s);
				Calendar a = Calendar.getInstance();
				a.set(Calendar.YEAR, year);
				a.set(Calendar.MONTH, month - 1);
				a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
				a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
				int maxDate = a.get(Calendar.DATE);
				mMinimumDays = maxDate;
			} catch (Exception e) {

			}
			break;
		}

	}

	/**
	 * 必须返回一个有效的多重dataset，需要在其中封装2种不同的数据类dataset否则会报错
	 * Note:其中至少有一个dataset务必构造scalenumber为1
	 * 
	 * @return 
	 *         你的XYMultipleSeriesDataset第一个dataset用于柱状图-例如XYValueSeries，第二个dataset用于折线图
	 *         -例如XYSeries
	 * */
	abstract protected XYMultipleSeriesDataset onDeployData(
			XYMultipleSeriesDataset aDataset);
}
