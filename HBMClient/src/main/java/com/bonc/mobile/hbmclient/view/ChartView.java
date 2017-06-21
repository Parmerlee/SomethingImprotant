package com.bonc.mobile.hbmclient.view;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.bonc.mobile.hbmclient.R;

/**
 * @author wanghongbian 画饼图类
 */
public class ChartView extends View {

	int areaX = 0; // 矩形框的left
	int areaY = 0; // 矩形框的right
	int colors[];
	int shade_colors[];
	int percent[];
	private int thickness = 0;// 饼图厚度
	// 饼图大小
	private float width;
	private float height;
	// 父布局大小
	private int parent_width;
	private int parent_height;
	// 数据
	private List<Double> chart;
	private double dataValue[];
	private List<String> KPINames;
	private int tempWidth;

	// 数据和
	private double dataTotal;

	// 指标位置
	private int index = 0;

	public ChartView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 *            上下文
	 * @param colors
	 *            最上面颜色数组
	 * @param shade_colors
	 *            阴影颜色数组
	 * @param percent
	 *            百分比 (和必须是360)
	 */
	public void setData(int[] colors, int[] shade_colors, int[] percent,
			float width, float height, List<Double> chart, double dataValue[],
			double dataTotal, List<String> KPINames, int index) {
		this.colors = colors;
		this.shade_colors = shade_colors;
		this.percent = percent;
		this.width = width;
		this.height = height;
		this.chart = chart;
		this.dataValue = dataValue;
		this.dataTotal = dataTotal;
		this.KPINames = KPINames;
		this.index = index;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		parent_width = getWidth();
		parent_height = getHeight();
		areaX = (int) (parent_width - width) / 2;
		areaY = getResources().getDimensionPixelSize(R.dimen.default_chart_top);
		float dr = getResources().getDimensionPixelSize(
				R.dimen.default_chart_below_dr);
		tempWidth = (int) (parent_width * 0.15);

		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStyle(Style.FILL);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(1);
		if (dataTotal != 0) {

			for (int i = 0; i <= thickness; i++) {
				int tempAngle = 90 - percent[index] / 2;
				paint.setColor(0XFFC9C9C9);
				canvas.drawArc(new RectF(areaX - dr, areaY - dr, areaX + width
						+ dr, height + areaY + dr), 0, 360, true, paint);
				if (i == thickness) {
					for (int j = index; j < percent.length; j++) {
						// 画饼图
						paint.setColor(colors[j]);
						canvas.drawArc(new RectF(areaX, areaY, areaX + width,
								height + areaY), tempAngle, percent[j], true,
								paint);
						tempAngle += percent[j];

					}

					for (int j = index - 1; j >= 0; j--) {

						// 画饼图
						paint.setColor(colors[j]);
						canvas.drawArc(new RectF(areaX, areaY, areaX + width,
								height + areaY), tempAngle, percent[j], true,
								paint);
						tempAngle += percent[j];

					}
				}
			}
		} else {
			paint.setColor(Color.WHITE);
			canvas.drawArc(new RectF(areaX, areaY, areaX + width, height
					+ areaY), 0, 360, true, paint);
		}
	}

	// 设置饼图厚度
	public void setThickness(int thickness) {
		this.thickness = thickness;
		areaY = thickness + 2;
		this.invalidate();
	}

}
