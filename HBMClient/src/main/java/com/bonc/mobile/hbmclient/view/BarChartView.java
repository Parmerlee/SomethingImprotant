package com.bonc.mobile.hbmclient.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.util.NumberUtil;

public class BarChartView extends View {

	private Paint paint = new Paint();
	private Paint textPaint = new Paint();
	private Paint linePaint = new Paint();
	private Paint bottomLinePaint = new Paint();
	private List<BarGraph> graphs = new ArrayList<BarGraph>();
	private float width;
	private float height;
	// 水平竖直坐标
	private double horLabel[];
	private double verLabel[];
	// 水平竖直坐标描述
	private String horDesc[];
	private String verDesc[];
	private float dy = 0;

	private int chartWidth = 0;
	private LinearGradient lg;
	private int barColor = 0XFFF7991D;
	private int endBarColor = 0XFFFFC20F;
	private int horLineColor = 0XFFA6D1FD;

	public BarChartView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BarChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public BarChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onDraw(Canvas canvas) {

		if (graphs == null || graphs.size() == 0) {
			return;
		}
		chartWidth = getResources().getDimensionPixelSize(
				R.dimen.default_barchart_width);// 矩形的宽

		float startHor = 10;
		double maxY = getMaxY();
		double minY = getMinY();
		double diy = maxY - minY;
		width = getWidth() - startHor * 2;
		height = getHeight();
		// double maxX = getMaxX();
		// double minX = getMinX();
		// double dx = maxX-minX;

		dy = getResources().getDimensionPixelSize(R.dimen.default_graph_dy) * 1.2f;

		textPaint.setColor(Color.BLACK);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(getResources().getDimensionPixelSize(
				R.dimen.default_graph_textsize));
		paint.setAntiAlias(true);
		paint.setColor(barColor);
		linePaint.setAntiAlias(true);
		linePaint.setColor(horLineColor);
		bottomLinePaint.setAntiAlias(true);
		bottomLinePaint.setColor(0XFFD6D6D6);

		// 画垂直标线
		verLabel = getVerLabel();
		for (int i = 0, n = verLabel.length; i <= n; i++) {
			double dx = (width - startHor) / n;
			double x = dx * i;
			x = Math.min(x, width - startHor - 1) + startHor;
			// canvas.drawLine((float)x, 0, (float)x, height-dy, paint);
			if (i != n)
				// canvas.drawText(verDesc[i], (float)(x+dx/2),
				// height-dy+textPaint.getTextSize()+dy/3-3, textPaint);
				canvas.drawText(verDesc[i], (float) (x + dx / 2), height - dy
						+ textPaint.getTextSize() + dy / 3 - 3, textPaint);
		}

		// 画水平标线
		horLabel = getHorLabel();
		for (int i = 0, n = horLabel.length; i < n; i++) {
			double y = (height - startHor) / (n - 1) * i + dy;
			y = Math.min(y, height - 1);
			if (i != 0 && i != n - 1) {
				canvas.drawLine(startHor, height - (float) y, width, height
						- (float) y, linePaint);
				canvas.drawLine(width, height - (float) y, width + dy / 3,
						height - (float) y + dy / 3, linePaint);
			}
			if (i == n - 2) {
				canvas.drawLine(width + dy / 3, height - (float) y + dy / 3,
						width + dy / 3, height - dy + dy / 3 - 3, linePaint);
			}
			if (i == 0) {
				Path path = new Path();
				path.moveTo(startHor, height - (float) y - 3);
				path.lineTo(width, height - (float) y - 3);
				path.lineTo(width + dy / 3, height - (float) y + dy / 3 - 3);
				path.lineTo(startHor, height - (float) y + dy / 3 - 3);
				path.moveTo(startHor, height - (float) y - 3);
				path.close();
				canvas.drawPath(path, bottomLinePaint);
			}
			// String hint = horDesc==null?horLabel[i]+"":horDesc[i];
			// canvas.drawText(hint, startHor, height-(float)y, paint);
		}

		if (lg == null) {
			lg = new LinearGradient(0, 0, 0, getHeight(), new int[] { barColor,
					endBarColor }, null, Shader.TileMode.CLAMP);
		}
		paint.setShader(lg);
		// 画柱形图
		for (int i = 0, n = verLabel.length; i < n; i++) {
			double valueY = graphs.get(0).getGraphData()[i].valueY;
			double posY = height - (height - 10) * valueY / diy - dy + minY;
			double dx = (width - startHor) / n;
			double x = dx * i;
			x = Math.min(x, width - startHor - 1) + startHor;

			canvas.drawRect((float) (x + dx / 2 - chartWidth), (float) posY,
					(float) (x + dx / 2 + chartWidth), height - dy, paint);

			String hint = graphs.get(0).getGraphData()[i].getStringY().trim();
			canvas.drawText(hint, (float) (x + dx / 2), (float) posY
					- textPaint.getTextSize() / 2, textPaint);
		}
	}

	/**
	 * 获得水平坐标
	 * 
	 * @return
	 */
	public double[] getHorLabel() {
		double horPosition[] = new double[10];
		horDesc = new String[horPosition.length];
		double max = getMaxY();
		double min = getMinY();
		for (int i = 0, n = horPosition.length; i < n; i++) {
			horPosition[i] = min + (max - min) / (n - 1) * i;
			if (horPosition[i] == (int) horPosition[i]) {
				horDesc[i] = (int) horPosition[i] + "";
			} else {
				horDesc[i] = horPosition[i] + "";
			}
		}
		return horPosition;
	}

	/**
	 * 获得垂直坐标
	 * 
	 * @return
	 */
	public double[] getVerLabel() {
		double verPosition[] = new double[graphs.get(0).getGraphData().length];
		verDesc = new String[verPosition.length];
		double max = getMaxX();
		double min = getMinX();

		for (int i = 0, n = verPosition.length; i < n; i++) {
			verPosition[i] = (int) (min + (max - min) / (n - 1) * i);
			verDesc[i] = graphs.get(0).getGraphData()[i].getDesc();
		}
		return verPosition;
	}

	/**
	 * 获得X轴最大值
	 * 
	 * @return
	 */
	public double getMaxX() {
		double maxX = 0;
		for (BarGraph g : graphs) {
			BarGraphData[] graphData = g.getGraphData();
			for (int i = 0, n = graphData.length; i < n; i++) {
				if (maxX < graphData[i].getValueX()) {
					maxX = graphData[i].getValueX();
				}
			}
		}
		return maxX;
	}

	/**
	 * 获得X轴最小值
	 * 
	 * @return
	 */
	public double getMinX() {
		double minX = 0;
		return minX;
	}

	/**
	 * 获得Y轴最大值
	 * 
	 * @return
	 */
	public double getMaxY() {
		double maxY = Double.MIN_VALUE;
		for (BarGraph g : graphs) {
			BarGraphData[] graphData = g.getGraphData();
			int n = graphData.length;
			for (int i = 0; i < n; i++) {
				double y = graphData[i].getValueY();
				if (maxY < y) {
					maxY = y;
				}
			}
		}
		// 获取最大计量单位
		long total = (long) maxY;
		String tempY = total + "";
		double d = Math.pow(10, tempY.length() - 1);
		maxY = (Math.round(maxY / d) + 2.5) * d;

		return maxY;
	}

	/**
	 * 获得Y轴最小值
	 * 
	 * @return
	 */
	public double getMinY() {
		double minY = Double.MAX_VALUE;
		double maxY = Double.MIN_VALUE;
		for (BarGraph g : graphs) {
			BarGraphData[] graphData = g.getGraphData();
			int n = graphData.length;
			for (int i = 0; i < n; i++) {
				double y = graphData[i].getValueY();
				if (maxY < y) {
					maxY = y;
				}
				if (minY > y) {
					minY = y;
				}
			}
		}
		// 获取最大计量单位
		long total = (long) maxY;
		String tempY = total + "";
		double d = Math.pow(10, tempY.length() - 1);
		minY = (Math.round(minY / d) - 0.5) * d;

		return 0;
	}

	public void addGraph(BarGraph graph) {
		graphs.add(graph);
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
				return NumberUtil.format(valueY, "#.##");
		}
	}

	public static class BarGraph {
		private int graph_color;
		private BarGraphData[] datas;

		public BarGraph(int graph_color, BarGraphData[] datas) {
			this.graph_color = graph_color;
			this.datas = datas;
		}

		public int getGraphColor() {
			return graph_color;
		}

		public BarGraphData[] getGraphData() {
			return datas;
		}
	}

	public void setBarColor(int barColor, int endBarColor) {
		this.barColor = barColor;
		this.endBarColor = endBarColor;
	}
}
