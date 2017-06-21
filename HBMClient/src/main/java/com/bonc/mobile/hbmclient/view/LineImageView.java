package com.bonc.mobile.hbmclient.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 绘制双曲线图
 * 
 * @author huanghaifeng
 * @version 2011.10.15
 * @author huanghaifeng
 * @version 2011.12.28
 */
public class LineImageView extends ImageView {

	private Paint currpaint = new Paint();
	private Paint prepaint = new Paint();
	private Paint currpaint_blue_point = new Paint();
	private int paintWidth = 2;// 画笔宽度

	private int preColor = 0x88888888;// 默认画上月曲线颜色
	private int curColor = 0xffc80000;// 默认画当月曲线颜色

	private double bitHeight;// view高度
	private double bitWidth;// view宽度

	private double maxY;// 传进来数据的最大值
	private double minY;// 传进来数据的最小值
	private double maxDrawY;// 画图数据的最大值
	private double minDrawY;// 画图数据的最小值

	private double maxXCount;// 最大X轴点数
	private int preMonthCount;// 上月总天数/上年总月数
	private double xSpace;// x轴上间距
	private int curMonthStart;

	protected List<Double> premonth;// 传入的前月数据
	private List<Double> realPreMonth = new ArrayList<Double>();// 在view要画的缩放后的数据
	protected List<Double> currmonth;// 传入的当前月的数据
	private List<Double> realCurrMonth = new ArrayList<Double>();// 在view上要画的缩放后的数据

	private boolean flag = false;

	/**
	 * 构造参数 ，初始化paint
	 * 
	 * @param context
	 */
	public LineImageView(Context context) {
		super(context);

		// 初始化paint
		initPaint();
	}

	public LineImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 初始化paint
		initPaint();
	}

	/**
	 * 初始化paint
	 */
	public void initPaint() {
		prepaint.setStyle(Paint.Style.STROKE);
		prepaint.setAntiAlias(true);
		prepaint.setStrokeWidth(paintWidth);
		prepaint.setColor(getPreColor());// #888888
		prepaint.setTextAlign(Align.CENTER);

		currpaint.setStyle(Paint.Style.STROKE);
		currpaint.setAntiAlias(true);
		currpaint.setStrokeWidth(paintWidth);
		currpaint.setColor(0xff165aa0);

		currpaint_blue_point.setStyle(Paint.Style.STROKE);
		currpaint_blue_point.setAntiAlias(true);
		currpaint_blue_point.setColor(0xff165aa0);
		currpaint_blue_point.setStrokeWidth(3);
	}

	/**
	 * 处理数据源传来的数据
	 * 
	 * @param currmonth
	 *            数据源传来的当月值数组
	 * @param premonth
	 *            数据源传来的上月值数组
	 */
	public void initData(List<Double> currmonth, List<Double> premonth) {
		this.currmonth = currmonth;
		this.premonth = premonth;
		this.maxXCount = getMaxXCount();
		this.bitHeight = getBitHeight();
		this.bitWidth = getBitWidth();
		this.curMonthStart = getCurMonthStart();

		xSpace = bitWidth / maxXCount - 0.5;

		List<Double> totalNumber1 = getTwoArrayTogether(currmonth, premonth);
		Double min1 = Double.MAX_VALUE;
		Double max1 = Double.MIN_VALUE;
		for (int i = 0; i < totalNumber1.size(); i++) {
			if (max1 != null && (Double) totalNumber1.get(i) != null
					&& max1 < (Double) totalNumber1.get(i)) {
				max1 = (Double) totalNumber1.get(i);
			}
			if (min1 != null && (Double) totalNumber1.get(i) != null
					&& min1 > (Double) totalNumber1.get(i))
				min1 = (Double) totalNumber1.get(i);

		}

		this.maxY = max1;
		this.minY = min1;
		if (this.maxY == this.minY) {
			this.minY = min1 - 1;
		}

		// 得到按比例缩放后的数据
		realCurrMonth = getCurRealDrawData(currmonth);
		realPreMonth = getPreRealDrawData(premonth);

		// 将两个数组数据放到一起
		List<Double> totalNumber = getTwoArrayTogether(realCurrMonth,
				realPreMonth);
		if (totalNumber != null) {
			Double min = Double.MAX_VALUE;
			Double max = Double.MIN_VALUE;
			for (int i = 0; i < totalNumber.size(); i++) {
				if (max != null && (Double) totalNumber.get(i) != null
						&& max < (Double) totalNumber.get(i)) {
					max = (Double) totalNumber.get(i);
				}
				if (min != null && (Double) totalNumber.get(i) != null
						&& min > (Double) totalNumber.get(i))
					min = (Double) totalNumber.get(i);
			}
			this.maxDrawY = max;
			this.minDrawY = min;
			if (this.maxDrawY == this.minDrawY) {
				this.minDrawY = min - 1;
			}
		} else {
			this.maxDrawY = 0;
			this.minDrawY = 0;
		}
	}

	/**
	 * 
	 * @param monthData
	 *            实际的上月真实数据源
	 * @return 缩放后上月实际要画的数据
	 */
	public List<Double> getPreRealDrawData(List<Double> monthData) {
		List<Double> realDrawData = new ArrayList<Double>();
		if (monthData != null) {
			for (int i = monthData.size() - 1; i >= 0; i--) {
				if (monthData.get(i) != null) {
					double temp = monthData.get(i) * bitHeight;
					realDrawData.add(temp / (maxY - minY));
				} else {
					realDrawData.add(null);
				}
			}
		}
		return realDrawData;
	}

	/**
	 * 
	 * @param monthData
	 *            实际的当月真实数据源
	 * @return 缩放后当月实际要画的数据
	 */
	public List<Double> getCurRealDrawData(List<Double> monthData) {
		List<Double> realDrawData = new ArrayList<Double>();
		if (monthData != null) {
			for (int i = 0, n = monthData.size(); i < n; i++) {
				if (monthData.get(i) != null) {
					double temp = monthData.get(i) * bitHeight;
					realDrawData.add(temp / (maxY - minY));
				} else {
					realDrawData.add(null);
				}
			}
		}
		return realDrawData;
	}

	/**
	 * 将两个数组放到一个数组中
	 * 
	 * @param array
	 *            两个数组中第一个数组
	 * @param array
	 *            第二个数组
	 * @return 返回两个数组放到一起的数组
	 */
	public List<Double> getTwoArrayTogether(List<Double> firstArray,
			List<Double> secondArray) {
		List<Double> togetherArray = new ArrayList<Double>();
		// 将两个数组数据放到一起
		if (secondArray != null && firstArray != null) {
			for (int i = 0; i < firstArray.size() + secondArray.size(); i++) {
				if (i < firstArray.size()) {
					togetherArray.add(firstArray.get(i));
				} else {
					for (int j = 0; j < secondArray.size(); j++) {
						togetherArray.add(secondArray.get(j));
					}
				}
			}
		} else if (firstArray == null) {
			for (int i = 0; i < secondArray.size(); i++) {
				if (i < secondArray.size()) {
					togetherArray.add(secondArray.get(i));
				}
			}
		} else {
			for (int i = 0; i < firstArray.size(); i++) {
				if (i < firstArray.size()) {
					togetherArray.add(firstArray.get(i));
				}
			}
		}
		return togetherArray;
	}

	/**
	 * view 的onDraw()方法，用来绘图
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float value_x_h = (int) ((curMonthStart) * (int) xSpace);
		float value_y_h = 0;
		float temp_x_h = 0;
		float temp_y_h = 0;
		// 上月曲线从最后一天开始画图
		if (realPreMonth != null && realPreMonth.size() == 0) {
		} else {
		}

		// 当月曲线从第一天绘图
		if (realCurrMonth != null && realCurrMonth.size() == 0) {
		} else {
			// 画蓝线
			for (int j = 0; j < realCurrMonth.size(); j++) {
				if (realCurrMonth.get(j) == null)
					continue;
				if (j == 0)
					value_y_h = (int) (bitHeight - (maxDrawY - minDrawY)
							/ bitHeight * (realCurrMonth.get(j) - minDrawY));
				else {
					value_y_h = (int) (bitHeight - (maxDrawY - minDrawY)
							/ bitHeight * (realCurrMonth.get(j) - minDrawY));
					canvas.drawLine(temp_x_h, temp_y_h, value_x_h, value_y_h,
							currpaint);
				}
				canvas.drawCircle(value_x_h, value_y_h, (float) 1.5,
						currpaint_blue_point);
				temp_x_h = value_x_h;
				temp_y_h = value_y_h;

				value_x_h = value_x_h + (int) xSpace;
			}
		}
	}

	/**
	 * 得到画图的view 的高度
	 * 
	 * @return
	 */
	public double getBitHeight() {
		return bitHeight;
	}

	/**
	 * 设置画图的view的高度
	 * 
	 * @param bitHeight
	 */
	public void setBitHeight(double bitHeight) {
		this.bitHeight = bitHeight;
	}

	/**
	 * 得到画图的view的宽度
	 * 
	 * @return
	 */
	public double getBitWidth() {
		return bitWidth;
	}

	/**
	 * 设置画图的view的宽度
	 * 
	 * @param bitWidth
	 */
	public void setBitWidth(double bitWidth) {
		this.bitWidth = bitWidth;
	}

	/**
	 * 得到该图的x坐标点数
	 * 
	 * @return
	 */
	public double getMaxXCount() {
		return maxXCount;
	}

	/**
	 * 设置该图的x坐标点数
	 * 
	 * @param maxXCount
	 */
	public void setMaxXCount(double maxXCount) {
		this.maxXCount = maxXCount;
	}

	/**
	 * 设置上月总天数
	 * 
	 * @param preMonthCount
	 */
	public void setPreMonthCount(int preMonthCount) {
		this.preMonthCount = preMonthCount;
	}

	/**
	 * 获得上月总天数
	 * 
	 * @return
	 */
	public int getPreMonthCount() {
		return preMonthCount;
	}

	/**
	 * 设置本月开始日期
	 * 
	 * @param preMonthCount
	 */
	public void setCurMonthStart(int curMonthStart) {
		this.curMonthStart = curMonthStart;
	}

	/**
	 * 获得本月开始日期
	 * 
	 * @return
	 */
	public int getCurMonthStart() {
		return curMonthStart;
	}

	public int getPreColor() {
		return preColor;
	}

	public void setPreColor(int preColor) {
		this.preColor = preColor;
	}

	public int getCurColor() {
		return curColor;
	}

	public void setCurColor(int curColor) {
		this.curColor = curColor;
	}

}
