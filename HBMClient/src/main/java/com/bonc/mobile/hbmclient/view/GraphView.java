package com.bonc.mobile.hbmclient.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.util.NumberUtil;

/**
 * 曲线图基础
 * 
 * @author liulu
 * @version 2011.10.11
 * 
 */
abstract public class GraphView extends LinearLayout {

	private static final int LABEL_NUM = 4;

	protected final Paint paint;
	private String[] horlabels;// 横提示线文字
	private double[] horlabelpos;// 横提示线位置
	private String[] verlabels;// 竖提示线文字
	private double[] verlabelpos;// 竖提示线位置
	private double viewportStart;
	private double viewportSize;
	protected final List<Graph> mGraph;
	private boolean showLegend = false;
	private float legendWidth = 120;
	private LegendAlign legendAlign = LegendAlign.MIDDLE;

	private Double graphMaxY;// 曲线图最高Y值
	private Double graphMaxX;// 曲线图最高X值
	private Double graphMinX;// 最小X值
	private Double graphMinY;// 最小Y值
	private Integer graphXsize;// 曲线X轴最大长度单位

	private int mLabelColor = Color.GRAY;
	private int mLabelAlpha = 255;

	protected RelativeLayout graphLayout;// 曲线图的布局
	protected RelativeLayout drawSomethingLayout;// 在曲线图上画图的布局

	// 划线时Y轴的差值
	public float dy = 0;
	public float dx = 0;
	public boolean isDrawPoint;// 是否画圆点

	// 横纵标尺以及每格代表的值
	public int horCor;
	public int verCor;
	public int dd;

	/**
	 * 默认构造
	 * 
	 * @param context
	 */
	public GraphView(Context context) {
		super(context);
		setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));

		dy = getResources().getDimensionPixelSize(R.dimen.default_graph_dy);
		paint = new Paint();
		mGraph = new ArrayList<Graph>();

		// 曲线图的整个布局
		graphLayout = new RelativeLayout(getContext());
		graphLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT, 1));

		drawSomethingLayout = new RelativeLayout(getContext());
		drawSomethingLayout.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		drawSomethingLayout.addView(new DrawSomethingView(context), 0);
		// drawSomethingLayout.setVisibility(INVISIBLE);

		// 曲线图
		graphLayout.addView(new GraphViewContentView(context));

		graphLayout.addView(drawSomethingLayout);

		addView(graphLayout);
	}

	/**
	 * 获得曲线数据源
	 * 
	 * @param id
	 *            曲线的id
	 * @return
	 */
	public GraphData[] getGraphValues(int id) {
		GraphData[] values = mGraph.get(id).values;
		if (viewportStart == 0 && viewportSize == 0) {
			// all data
			return values;
		} else {
			// viewport
			List<GraphData> listData = new ArrayList<GraphData>();
			for (int i = 0; i < values.length; i++) {
				if (values[i].valueX >= viewportStart) {
					if (values[i].valueX > viewportStart + viewportSize) {
						listData.add(values[i]); // one more for nice scrolling
						break;
					} else {
						listData.add(values[i]);
					}
				} else {
					if (listData.isEmpty()) {
						listData.add(values[i]);
					}
					listData.set(0, values[i]); // one before, for nice
												// scrolling
				}
			}
			return listData.toArray(new GraphData[listData.size()]);
		}
	}

	/**
	 * 添加曲线
	 * 
	 * @param series
	 */
	public void addGraph(Graph series) {
		mGraph.add(series);
	}

	/**
	 * 绘制曲线说明图
	 * 
	 * @param canvas
	 * @param height
	 *            图宽
	 * @param width
	 *            图高
	 */
	protected void drawLegend(Canvas canvas, float height, float width) {
		int shapeSize = 15;

		// rect
		paint.setARGB(100, 0, 0, 0);
		float legendHeight = (shapeSize + 5) * mGraph.size() + 5;
		float lLeft = width - legendWidth - 10;
		float lTop;
		switch (legendAlign) {
		case TOP:
			lTop = 10;
			break;
		case MIDDLE:
			lTop = height / 2 - legendHeight / 2;
			break;
		default:
			lTop = height - legendHeight - 10;
		}
		float lRight = lLeft + legendWidth;
		float lBottom = lTop + legendHeight;
		canvas.drawRoundRect(new RectF(lLeft, lTop, lRight, lBottom), 8, 8,
				paint);

		for (int i = 0; i < mGraph.size(); i++) {
			paint.setColor(mGraph.get(i).color);
			canvas.drawRect(new RectF(lLeft + 5, lTop + 5
					+ (i * (shapeSize + 5)), lLeft + 5 + shapeSize, lTop
					+ ((i + 1) * (shapeSize + 5))), paint);
			if (mGraph.get(i).description != null) {
				paint.setColor(Color.WHITE);
				paint.setTextAlign(Align.LEFT);
				canvas.drawText(mGraph.get(i).description, lLeft + 5
						+ shapeSize + 5, lTop + shapeSize
						+ (i * (shapeSize + 5)), paint);
			}
		}
	}

	/**
	 * 绘制曲线图
	 * 
	 * @param canvas
	 * @param index
	 *            曲线的ID
	 * @param graphwidth
	 *            绘制区域的宽度
	 * @param graphheight
	 *            绘制区域的高度
	 * @param minX
	 *            X轴最小值
	 * @param minY
	 *            Y轴最小值
	 * @param diffX
	 *            X轴最大值与最小值的差
	 * @param diffY
	 *            Y轴最大值与最小值的差
	 */
	abstract public void drawSeries(Canvas canvas, int index, float graphwidth,
			float graphheight, double minX, double minY, double diffX,
			double diffY);

	/**
	 * 在曲线图上绘制一些图案,却并不想让这些图案与曲线图重叠.
	 * 
	 * @param canvas
	 * @param graphwidth
	 *            绘制区域的宽度
	 * @param graphheight
	 *            绘制区域的高度
	 * @param minX
	 *            X轴最小值
	 * @param minY
	 *            Y轴最小值
	 * @param diffX
	 *            X轴最大值与最小值的差
	 * @param diffY
	 *            Y轴最大值与最小值的差
	 */
	abstract public void drawSomething(Canvas canvas, float graphwidth,
			float graphheight, double minX, double minY, double diffX,
			double diffY);

	/**
	 * 获得曲线说明图的对其方式
	 * 
	 * @return
	 */
	public LegendAlign getLegendAlign() {
		return legendAlign;
	}

	/**
	 * 获得曲线说明图宽度
	 * 
	 * @return
	 */
	public float getLegendWidth() {
		return legendWidth;
	}

	/***
	 * 获取X轴的最大长度单位
	 * 
	 * @return
	 */
	public int getXSize() {
		if (graphXsize == null) {

			int max = 0;

			for (Graph g : mGraph) {

				final int n = g.values.length;

				if (max < n) {
					max = n;
				}
			}

			graphXsize = max;
		}

		return graphXsize;

	}

	/**
	 * 获得X轴最大值
	 * 
	 * @return
	 */
	public double getMaxX() {
		if (graphMaxX == null) {
			double max = 0;
			for (Graph g : mGraph) {
				GraphData[] vs = g.values;
				for (int i = 0, n = vs.length; i < n; i++) {
					if (max < vs[i].valueX) {
						max = vs[i].valueX;
					}
				}
			}
			this.graphMaxX = max;
		}
		return this.graphMaxX;
	}

	/**
	 * 获得Y轴最大值
	 * 
	 * @return
	 */
	public double getMaxY() {
		if (graphMaxY == null) {
			double max = 0;
			for (Graph g : mGraph) {
				GraphData[] vs = g.values;
				for (int i = 0, n = vs.length; i < n; i++) {
					if (max < vs[i].valueY) {
						max = vs[i].valueY;
					}
				}
			}

			long total = (long) max;
			String s = total + "";
			// 获得最小计量单位
			double d = Math.pow(10, s.length() - 1);
			max = (Math.round(max / d) + 5) * d;

			this.graphMaxY = max;
		}
		return this.graphMaxY;
	}

	/**
	 * 获得X轴最小值
	 * 
	 * @return
	 */
	public double getMinX() {
		if (this.graphMinX == null) {
			this.graphMinX = 0.0;
		}
		return this.graphMinX;
	}

	/**
	 * 获得Y轴最小值
	 * 
	 * @return
	 */
	public double getMinY() {
		if (graphMinY == null) {
			// 将最小值初始为最大值
			double min = Double.MAX_VALUE;
			double max = 0;
			for (Graph g : mGraph) {
				GraphData[] vs = g.values;
				for (int i = 0, n = vs.length; i < n; i++) {
					if (min > vs[i].valueY) {
						min = vs[i].valueY;
					}
					if (max < vs[i].valueY) {
						max = vs[i].valueY;
					}
				}
			}

			long total = (long) max;
			String s = total + "";
			// 获得最小计量单位
			double d = Math.pow(10, s.length() - 1);
			min = (Math.round(min / d) - 1) * d;

			this.graphMinY = min;
		}
		return this.graphMinY;
	}

	/**
	 * 获得额外绘图布局
	 * 
	 * @return RelativeLayout
	 */
	public RelativeLayout getDrawSomethingLayout() {
		return drawSomethingLayout;
	}

	/**
	 * 是否显示曲线说明图
	 * 
	 * @return
	 */
	public boolean isShowLegend() {
		return showLegend;
	}

	/**
	 * 设置X轴最大值
	 * 
	 * @param graphMaxX
	 */
	public void setMaxX(double graphMaxX) {
		this.graphMaxX = graphMaxX;
	}

	/**
	 * 设置Y轴最大值
	 * 
	 * @param graphMaxY
	 */
	public void setMaxY(double graphMaxY) {
		this.graphMaxY = graphMaxY;
	}

	/**
	 * 设置X轴最小值
	 * 
	 * @param graphMinX
	 */
	public void setMinX(double graphMinX) {
		this.graphMinX = graphMinX;
	}

	/**
	 * 设置Y轴最小值
	 * 
	 * @param graphMinY
	 */
	public void setMinY(double graphMinY) {
		this.graphMinY = graphMinY;
	}

	/**
	 * 设置水平方向标线
	 * 
	 * @param d
	 *            下标位置(minY 与maxY之间 任意值)
	 * @param str
	 *            下标显示文字内容
	 */
	public void setHorizontalLabels(double[] d, String[] str) {

		if (d != null && str != null && d.length != str.length) {
			new Throwable("horizontalLabels array's length not equal ");
		}
		horlabels = str;
		horlabelpos = d;
	}

	/**
	 * 设置曲线说明图对齐方式
	 * 
	 * @param legendAlign
	 */
	public void setLegendAlign(LegendAlign legendAlign) {
		this.legendAlign = legendAlign;
	}

	/**
	 * 设置曲线说明图宽度
	 * 
	 * @param legendWidth
	 */
	public void setLegendWidth(float legendWidth) {
		this.legendWidth = legendWidth;
	}

	/**
	 * 设置是否显示曲线说明图
	 * 
	 * @param showLegend
	 */
	public void setShowLegend(boolean showLegend) {
		this.showLegend = showLegend;
	}

	/**
	 * 设置Y轴上X方向表格标线及个数
	 * 
	 * @param d
	 *            下标位置(minX 与maxX之间 任意值)
	 * @param str
	 *            下标显示文字内容
	 */
	public void setVerticalLabels(double[] d, String[] str) {
		if (d != null && str != null && d.length != str.length) {
			new Throwable("horizontalLabels array's length not equal ");
		}
		verlabels = str;
		verlabelpos = d;

	}

	/**
	 * 绘制曲线图View,界面中呈现的曲线图将会被绘制到这个View中
	 * 
	 * @author liulu
	 * 
	 */
	private class GraphViewContentView extends View {
		private float graphwidth;

		/**
		 * @param context
		 */
		public GraphViewContentView(Context context) {
			super(context);
			setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
		}

		/**
		 * @param canvas
		 */
		@Override
		protected void onDraw(Canvas canvas) {

			paint.setAntiAlias(true);
			paint.setStrokeWidth(0);
			float horstart = dx * 1.2f;
			float verStart = 0;
			float height = getHeight();
			float width = getWidth();
			double maxY = getMaxY();
			double minY = getMinY();
			double diffY = maxY - minY;
			double maxX = getMaxX();
			double minX = getMinX();
			double diffX = maxX - minX;
			float graphheight = height;
			graphwidth = width - dx * 2.2f;

			// 画垂直标线
			if (verlabelpos == null) {
				verlabelpos = generateVerlabels();
			}

			if (verlabelpos != null) {
				float ddy = 0;
				paint.setTextAlign(Align.LEFT);
				for (int i = 0; i < verlabelpos.length; i++) {
					paint.setColor(Color.DKGRAY);
					paint.setAlpha(55);
					final double valX = verlabelpos[i] - minX;
					if (valX < 0) {
						new Throwable(
								"horlabels position to be smaller than @minX");
					}
					final double ratX = valX / diffX;
					double x = graphwidth * ratX;
					x = Math.max(Math.min(x, graphwidth - 1), 1);
					if (isDrawPoint) {
						ddy = dy;
					}
					canvas.drawLine(horstart + (float) x, ddy, horstart
							+ (float) x, graphheight - dy, paint);

					paint.setColor(Color.BLACK);
					paint.setTextAlign(Align.CENTER);
					paint.setTextSize(getResources().getDimension(
							R.dimen.default_graph_textsize) + 1);
					String txt = verlabels == null ? "" : verlabels[i];

					float spaceV = getResources().getDimension(
							R.dimen.default_graph_textvspace);
					if (mGraph.get(0).values.length == 3 && i == 1) {
						canvas.drawText(txt, horstart + (float) x - horstart
								/ 2, graphheight - dy - paint.getTextSize()
								+ spaceV, paint);
					} else if (mGraph.get(0).values.length == 2) {
						if (i == 0) {
							canvas.drawText(txt, horstart + (float) x
									+ horstart / 2,
									graphheight - dy - paint.getTextSize()
											+ spaceV, paint);
						} else {
							canvas.drawText(txt, horstart + (float) x
									- horstart / 2,
									graphheight - dy - paint.getTextSize()
											+ spaceV, paint);
						}
					} else if (mGraph.get(0).values.length == 1) {
						canvas.drawText(
								txt,
								horstart,
								graphheight - dy - paint.getTextSize() + spaceV,
								paint);
					} else {
						canvas.drawText(txt, horstart + (float) x, graphheight
								- dy - paint.getTextSize() + spaceV, paint);
					}
				}
			}

			// 画水平标线
			if (horlabelpos == null) {
				horlabelpos = generateHorlabels();
			}

			if (horlabelpos != null) {
				for (int i = 0; i < horlabelpos.length; i++) {
					paint.setColor(Color.DKGRAY);
					paint.setAlpha(55);
					final double valY = horlabelpos[i] - minY;
					if (valY < 0) {
						new Throwable(
								"horlabels position to be smaller than @minY");
					}
					final double ratY = valY / diffY;
					double y = graphheight - (graphheight * ratY) - dy;
					y = Math.max(Math.min(y, graphheight - 1), 1);

					paint.setStrokeWidth(2f);
					paint.setColor(0XFFB2B0B0);

					if (isDrawPoint) {
						if (i == 0) {
							float ddx = 10;
							float endX = graphwidth + dx + ddx;
							canvas.drawLine(horstart, (float) y, endX,
									(float) y, paint);
							Path path = new Path();
							paint.setStyle(Style.FILL);
							path.moveTo(endX, (float) y - ddx);
							path.lineTo(endX + dy / 2, (float) y);
							path.lineTo(endX, (float) y + ddx);
							path.moveTo(endX, (float) y - ddx);
							canvas.drawPath(path, paint);
						} else if (i < horlabelpos.length - 1) {
							canvas.drawLine(horstart, (float) y, horstart + dx
									/ 5, (float) y, paint);
						}
					} else if (!isDrawPoint && i < horlabelpos.length - 1) {
						canvas.drawLine(horstart, (float) y, graphwidth + dx,
								(float) y, paint);
					}

					paint.setTextAlign(Align.RIGHT);
					paint.setColor(Color.BLACK);
					paint.setTextSize(getResources().getDimension(
							R.dimen.default_graph_textsize));
					String txt = horlabels == null ? NumberUtil
							.format(horlabelpos[i]) : horlabels[i];
					float space = getResources().getDimension(
							R.dimen.default_graph_texthspace);
					if (i < horlabelpos.length - 1) {
						canvas.drawText(txt, horstart * 0.95f, (float) y
								+ paint.getTextSize() / 3, paint);
					}
				}
			}

			// 画曲线
			if (maxY != minY) {
				paint.setStrokeCap(Paint.Cap.ROUND);
				paint.setStrokeWidth(3);

				for (int i = 0; i < mGraph.size(); i++) {
					paint.setColor(mGraph.get(i).color);
					drawSeries(canvas, i, graphwidth, graphheight, minX, minY,
							diffX, diffY);
				}
			}
		}

		private double[] generateHorlabels() {
			double[] pos = new double[6];
			double min = getMinY();
			double max = getMaxY();
			for (int i = 0, n = pos.length; i < n; i++) {
				double d = min + (max - min) / (n - 1) * i;
				pos[i] = d;
			}
			return pos;
		}

		// 画垂直标线
		private double[] generateVerlabels() {
			double[] pos = new double[mGraph.get(0).values.length];
			verlabels = new String[pos.length];
			double min = getMinX();
			double max = getMaxX();
			for (int i = 0, n = pos.length; i < n; i++) {
				int v = (int) (min + (max - min) / (n - 1) * i);
				pos[i] = v;
				int index = 0;
				if (dd == 0) {
					index = i;
					;
				} else {
					if (i == 0) {
						index = 0;
					} else if (i + dd * i >= 30) {
						index = mGraph.get(0).values.length - 1;
					} else {
						index = i + dd * i - 1;
					}
				}
				verlabels[i] = mGraph.get(0).values[index].txt;
			}
			return pos;
		}

	}

	public class DrawSomethingView extends View {

		public DrawSomethingView(Context context) {
			super(context);
			setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
		}

		@Override
		protected void onDraw(Canvas canvas) {

			// 自定义画图
			drawSomething(canvas, getWidth(), getHeight(), getMinX(),
					getMinY(), getMaxX() - getMinX(), getMaxY() - getMinY());
		}
	}

	/**
	 * 曲线数据
	 */
	public static class GraphData {// i, dayvalue,date,unit
		private final double valueX;
		private final double valueY;
		private final String txt;
		private final String unit;

		public GraphData(double valueX, double valueY, String txt, String unit) {
			this.valueX = valueX;
			this.valueY = valueY;
			this.txt = txt;
			this.unit = unit;
		}

		public double getValueX() {
			return valueX;
		}

		public double getValueY() {
			return valueY;
		}

		public String getName() {
			return txt;
		}

		public String getUnit() {
			if (unit.equals("%"))
				return "";
			else
				return unit;
		}

		public String getStringValueY() {
			if (unit.equals("")) {
				return txt;
			} else if (unit.equals("%")) {
				return valueY + "";
			} else {
				if (valueY == (int) valueY) {
					return (int) valueY + "";
				} else {
					return valueY + "";
				}
			}
		}
	}

	/**
	 * 绘制曲线时所需曲线对象
	 */
	public static class Graph {

		final String description;// 曲线名称
		final int color;// 曲线颜色
		final GraphData[] values;// 曲线数据

		/**
		 * 默认构造
		 * 
		 * @param values
		 *            曲线数据源
		 */
		public Graph(GraphData[] values) {
			description = null;
			color = Color.BLUE;
			this.values = values;
		}

		/**
		 * 构造
		 * 
		 * @param values
		 *            曲线数据源
		 * @param color
		 *            曲线颜色
		 * @param description
		 *            曲线名称
		 */
		public Graph(GraphData[] values, Integer color, String description) {
			super();
			this.description = description;
			if (color == null) {
				color = Color.BLUE;// blue version
			}
			this.color = color;
			this.values = values;
		}
	}

	/**
	 * 曲线说明显示位置
	 * 
	 * @author liulu
	 * 
	 */
	public enum LegendAlign {
		TOP, MIDDLE, BOTTOM
	}

	/**
	 * 曲线的横纵坐标
	 * 
	 */
	public void setHorVerCor(int horCor, int dd) {
		this.horCor = horCor;
		this.dd = dd;
	}
}
