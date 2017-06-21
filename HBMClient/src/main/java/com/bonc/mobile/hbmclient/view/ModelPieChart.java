package com.bonc.mobile.hbmclient.view;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.chart.PieMapper;
import org.achartengine.chart.RoundChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.Point;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.Log;

/**
 * The pie chart rendering class.
 */
public class ModelPieChart extends RoundChart {
	/** Handles returning values when tapping on PieChart. */
	private PieMapper mPieMapper;

	/**
	 * Builds a new pie chart instance.
	 * 
	 * @param dataset
	 *            the series dataset
	 * @param renderer
	 *            the series renderer
	 * @param renderSet
	 *            the reder's feature 0:mShowLable;1:mDisplayValues;
	 */
	public ModelPieChart(CategorySeries dataset, DefaultRenderer renderer) {
		super(dataset, renderer);
		mPieMapper = new PieMapper();

	}

	/**
	 * The graphical representation of the pie chart.
	 * 
	 * @param canvas
	 *            the canvas to paint to
	 * @param x
	 *            the top left x value of the view to draw to
	 * @param y
	 *            the top left y value of the view to draw to
	 * @param width
	 *            the width of the view to draw to
	 * @param height
	 *            the height of the view to draw to
	 * @param paint
	 *            the paint
	 */
	@Override
	public void draw(Canvas canvas, int x, int y, int width, int height,
			Paint paint) {
		// height = (int) (height/mRenderer.getScale());
		paint.setAntiAlias(mRenderer.isAntialiasing());
		paint.setStyle(Style.FILL);
		paint.setTextSize(mRenderer.getLabelsTextSize());
		boolean buiFrame = mRenderer.isDrawTextFrame();
		// int legendSize = getLegendSize(mRenderer, height / 5, 0);//
		int left = x;
		int top = y;
		int right = x + width;
		int sLength = mDataset.getItemCount();
		int bottom = y + height;

		double total = 0;
		String[] titles = new String[sLength];
		for (int i = 0; i < sLength; i++) {
			total += mDataset.getValue(i);
			titles[i] = mDataset.getCategory(i);
		}
		/*
		 * if (mRenderer.isFitLegend()) { legendSize = drawLegend(canvas,
		 * mRenderer, titles, left, right, y, width, height, legendSize, paint,
		 * true); }
		 */
		int legendW = getLegendSize(mRenderer, width / 5, 0);//
		int legendH = getLegendSize(mRenderer, height / 5, 0);
		switch (mRenderer.getmLegendType()) {
		case DefaultRenderer.LEGEND_BOTTOM:
			bottom = bottom - legendH;
			break;
		case DefaultRenderer.LEGEND_TOP:
			top = top + legendH;
			break;
		case DefaultRenderer.LEGEND_LEFT:
			left = left + legendW;
			break;
		case DefaultRenderer.LEGEND_RIGHT:
			right = right - legendW;
			break;
		}
		/*- legendSize*/;
		drawBackground(mRenderer, canvas, x, y, width, height, paint, false,
				DefaultRenderer.NO_COLOR);

		float currentAngle = mRenderer.getStartAngle();
		int mRadius = Math.min(Math.abs(right - left), Math.abs(bottom - top));
		int radius = (int) (mRadius * 0.35 * mRenderer.getScale());

		if (mCenterX == NO_VALUE) {
			mCenterX = (left + right) / 2;
		}
		if (mCenterY == NO_VALUE) {
			mCenterY = (bottom + top) / 2;// 此处出现偏移，查找到原因，因此加上偏移量
		}

		// Hook in clip detection after center has been calculated
		mPieMapper.setDimensions(radius, mCenterX, mCenterY);
		boolean loadPieCfg = !mPieMapper.areAllSegmentPresent(sLength);
		if (loadPieCfg) {
			mPieMapper.clearPieSegments();
		}

		float shortRadius = radius * 0.9f;
		float longRadius = radius * 1.1f;
		RectF oval = new RectF(mCenterX - radius, mCenterY - radius, mCenterX
				+ radius, mCenterY + radius);
		List<RectF> prevLabelsBounds = new ArrayList<RectF>();
		float[] currentAngles = new float[sLength];
		float[] angles = new float[sLength];
		for (int i = 0; i < sLength; i++) {

			SimpleSeriesRenderer seriesRenderer = mRenderer
					.getSeriesRendererAt(i);
			if (seriesRenderer.isGradientEnabled()) {
				RadialGradient grad = new RadialGradient(mCenterX, mCenterY,
						longRadius, seriesRenderer.getGradientStartColor(),
						seriesRenderer.getGradientStopColor(), TileMode.MIRROR);
				paint.setShader(grad);
			} else {
				paint.setColor(seriesRenderer.getColor());
			}

			float value = (float) mDataset.getValue(i);
			float angle = (float) (value / total * 360);
			angles[i] = angle;
			currentAngles[i] = currentAngle;
			if (seriesRenderer.isHighlighted()) {
				double rAngle = Math.toRadians(90 - (currentAngle + angle / 2));
				float translateX = (float) (radius * 0.1 * Math.sin(rAngle));
				float translateY = (float) (radius * 0.1 * Math.cos(rAngle));
				oval.offset(translateX, translateY);
				canvas.drawArc(oval, currentAngle, angle, true, paint);
				oval.offset(-translateX, -translateY);
			} else {
				canvas.drawArc(oval, currentAngle, angle, true, paint);
			}
			paint.setShader(null);
			if (buiFrame) {
				Log.i("", "qqu " + i);
				double rAngle = Math.toRadians(90 - (currentAngle + angle / 2));
				double sinValue = Math.sin(rAngle);
				double cosValue = Math.cos(rAngle);
				float size = mRenderer.getLabelsTextSize();
				float extra = Math.max(size / 2, 10);
				int x1 = Math
						.round(mCenterX + (float) (shortRadius * sinValue));
				int x2 = Math.round(mCenterX + (float) (longRadius * sinValue));
				int y2 = Math.round(mCenterY + (float) (longRadius * cosValue));
				if (x1 > x2) {
					extra = -extra;
				}
				// 显示折线和文字背景图片
				if (mRenderer.isShowLableBack()) {

					float xLabel = x2 + extra;
					float yLabel = y2;
					int jColor = mRenderer.getSeriesRendererAt(i).getColor();
					float[] hsv = new float[3];
					float frameHeight = paint.getTextSize() + 6;
					float frameWidth = mDataset.getCategory(i).length()
							* paint.getTextSize();// paint.measureText(mDataset.getCategory(i));
					Color.colorToHSV(jColor, hsv);
					hsv[1] *= 0.3;
					int bColor = Color.HSVToColor(hsv);
					paint.setColor(bColor);
					if (extra > 0) {
						paint.setStyle(Style.FILL);
						canvas.drawRect(xLabel - 3, yLabel + 3 - frameHeight,
								xLabel - 6 + frameWidth, yLabel + 3, paint);
						paint.setColor(jColor);
						paint.setStyle(Style.STROKE);
						canvas.drawRect(xLabel - 3, yLabel + 3 - frameHeight,
								xLabel - 6 + frameWidth, yLabel + 3, paint);
						paint.setStyle(Style.FILL);
					} else {
						paint.setStyle(Style.FILL);
						canvas.drawRect(xLabel - frameWidth + 3, yLabel + 3
								- frameHeight, xLabel, yLabel + 3, paint);
						paint.setColor(jColor);
						paint.setStyle(Style.STROKE);
						canvas.drawRect(xLabel - frameWidth + 3, yLabel + 3
								- frameHeight, xLabel, yLabel + 3, paint);
						paint.setStyle(Style.FILL);
					}
				}
				paint.setColor(seriesRenderer.getColor());
				drawLabel(canvas, "" + mDataset.getValue(i), mRenderer,
						prevLabelsBounds, mCenterX, mCenterY, shortRadius,
						longRadius, currentAngle, angle, left, right,
						mRenderer.getLabelsColor(), paint, true, false);
			}
			// 显示数值
			if (mRenderer.isDisplayValues() && i == sLength - 1) {
				for (int j = 0; j < sLength; j++) {
					String lab = getLabel(mRenderer.getSeriesRendererAt(j)
							.getChartValuesFormat(), mDataset.getValue(j));

					drawLabel(canvas, lab, mRenderer, prevLabelsBounds,
							mCenterX, mCenterY, shortRadius / 2,
							longRadius / 2, currentAngles[j], angles[j], left,
							right, mRenderer.getLabelsColor(), paint, false,
							true);
				}
			}

			// Save details for getSeries functionality
			if (loadPieCfg) {
				mPieMapper.addPieSegment(i, value, currentAngle, angle);
			}
			currentAngle += angle;

		}
		prevLabelsBounds.clear();
		switch (mRenderer.getmLegendType()) {
		case DefaultRenderer.LEGEND_BOTTOM:
			drawLegendHori(canvas, mRenderer, titles, left, bottom, width,
					legendH, paint, false);
			break;
		case DefaultRenderer.LEGEND_TOP:
			drawLegendHori(canvas, mRenderer, titles, left, top, width,
					legendH, paint, false);
			break;
		case DefaultRenderer.LEGEND_LEFT:
			drawLegendVerti(canvas, mRenderer, titles, left, top + 10, legendW,
					height - 15, paint, false);
			break;
		case DefaultRenderer.LEGEND_RIGHT:
			drawLegendVerti(canvas, mRenderer, titles, right, top + 10,
					legendW, height - 15, paint, false);
			break;
		}

		// drawTitle(canvas, x, y, width, paint);
	}

	private void drawLegendHori(Canvas canvas, DefaultRenderer renderer,
			String[] titles, int startX, int startY, int width, int height,
			Paint paint, boolean calculate) {
		// TODO Auto-generated method stub

		float size = 6;
		if (renderer.isShowLegend()) {

			float currentX = startX;
			float currentY = startY + size;
			paint.setTextAlign(Align.LEFT);
			paint.setTextSize(renderer.getLegendTextSize());
			int sLength = Math.min(titles.length,
					renderer.getSeriesRendererCount());
			for (int i = 0; i < sLength; i++) {
				SimpleSeriesRenderer r = renderer.getSeriesRendererAt(i);
				final float lineSize = getLegendShapeWidth(i);
				if (r.isShowLegendItem()) {
					String text = titles[i];
					if (titles.length == renderer.getSeriesRendererCount()) {
						paint.setColor(r.getColor());
					} else {
						paint.setColor(Color.LTGRAY);
					}
					float[] widths = new float[text.length()];
					paint.getTextWidths(text, widths);
					float sum = 0;
					for (float value : widths) {
						sum += value;
					}
					float extraSize = lineSize + 10 + sum;
					float currentWidth = currentX + extraSize;

					if (i > 0
							&& getExceed(currentWidth, renderer,
									startX + width, width)) {
						currentX = startX;
						currentY += renderer.getLegendTextSize();
						// size += renderer.getLegendTextSize();
						currentWidth = currentX + extraSize;
					}
					if (getExceed(currentWidth, renderer, startX + width, width)) {
						float maxWidth = startX + width - currentX - lineSize
								- 10;
						if (isVertical(renderer)) {
							maxWidth = width - currentX - lineSize - 10;
						}
						int nr = paint.breakText(text, true, maxWidth, widths);
						text = text.substring(0, nr) + "...";
					}
					if (!calculate) {
						drawLegendShape(canvas, r, currentX, currentY, i, paint);
						Paint mpaint = new Paint();

						if (renderer.isPeculiarLegendTextColor()) {
							Paint mp = new Paint(paint);
							mp.setColor(0xff101010);
							drawString(canvas, text, currentX + lineSize + 5,
									currentY + 5, mp);
						} else {
							drawString(canvas, text, currentX + lineSize + 5,
									currentY + 5, paint);
						}

					}
					currentX += extraSize;
				}
			}
		}
		// return Math.round(size + renderer.getLegendTextSize());

	}

	private void drawLegendVerti(Canvas canvas, DefaultRenderer renderer,
			String[] titles, int startX, int startY, int width, int height,
			Paint paint, boolean calculate) {
		// TODO Auto-generated method stub

		float size = 6;
		if (renderer.isShowLegend()) {
			int wordH = (int) renderer.getLegendTextSize() + 2;
			/*
			 * float currentX = startX+ size ; float currentY =startY;
			 */
			paint.setTextAlign(Align.LEFT);
			paint.setTextSize(renderer.getLegendTextSize());
			int sLength = Math.min(titles.length,
					renderer.getSeriesRendererCount());
			int linN = 0;
			int columnN = 1;
			if (sLength * wordH < height - size) {
				wordH = (int) ((height - size) / sLength);
				linN = sLength;
			} else {
				linN = (int) ((height - size) / wordH);
				columnN = (sLength / linN) + (sLength % linN > 0 ? 0 : 1);
			}
			int lineW = width / columnN - 2;
			for (int i = 0; i < sLength; i++) {
				float currentX = startX + (i / linN) * (lineW + 2);
				float currentY = startY + (i % linN) * wordH;
				SimpleSeriesRenderer r = renderer.getSeriesRendererAt(i);
				final float lineSize = getLegendShapeWidth(i);
				if (r.isShowLegendItem()) {
					String text = titles[i];
					if (titles.length == renderer.getSeriesRendererCount()) {
						paint.setColor(r.getColor());
					} else {
						paint.setColor(Color.LTGRAY);
					}

					float[] widths = new float[text.length()];
					paint.getTextWidths(text, widths);
					float sum = 0;
					for (float value : widths) {
						sum += value;
					}
					float extraSize = lineSize + 10 + sum;
					// float currentWidth = currentX + extraSize;

					if (extraSize > lineW) {
						float maxWidth = lineW - 10;

						int nr = paint.breakText(text, true, maxWidth, widths);
						text = text.substring(0, nr) + "...";
					}
					if (!calculate) {
						drawLegendShape(canvas, r, currentX, currentY, i, paint);
						Paint mpaint = new Paint();

						if (renderer.isPeculiarLegendTextColor()) {
							Paint mp = new Paint(paint);
							mp.setColor(0xff101010);
							drawString(canvas, text, currentX + lineSize + 5,
									currentY + 5, mp);
						} else {
							drawString(canvas, text, currentX + lineSize + 5,
									currentY + 5, paint);
						}

					}

				}
			}
		}
		// return Math.round(size + renderer.getLegendTextSize());

	}

	public SeriesSelection getSeriesAndPointForScreenCoordinate(
			Point screenPoint) {
		return mPieMapper.getSeriesAndPointForScreenCoordinate(screenPoint);
	}

}
