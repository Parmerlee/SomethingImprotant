package com.bonc.mobile.hbmclient.view;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Path;
import android.graphics.Shader;
import android.view.MotionEvent;
import android.view.View;

import com.bonc.mobile.hbmclient.R;

/**
 * 线性曲线视图
 * 
 * @author liulu
 * @version 2011-10-12
 */
public class LineGraphView extends GraphView {

	private enum Action {
		NONE, DOWN, MOVE, UP
	}

	private Enum gesture = Action.NONE;

	private static final int DEFAULT_LAST_X = -100;

	private final Paint paintBackground;
	private boolean drawBackground;

	private Paint mPathPaint = new Paint();
	private Paint mPathAreaPaint = new Paint();
	private Paint mPointPaint = new Paint();
	private Paint mPointPaint2 = new Paint();
	private Path mGraphLine = new Path();
	private Bitmap brick;
	private Bitmap line = BitmapFactory.decodeResource(getResources(),
			R.mipmap.com_graph_line_long);
	private LinearGradient lg;

	private int mLastX = DEFAULT_LAST_X;

	private int mLastY;
	private int mDx;// 手势X轴间距
	private int bkColor;

	public Map<String, Float[]> map;
	public float lastX;// 记录第一条曲线的X的最大值
	private boolean mCircleFlow = false;// 圆是否跟随砖头滑动

	private boolean mShowBrick = true;
	public float midX;

	private OnBrickMoveListener obml;
	private Runnable showBrickRunnable = new Runnable() {

		@Override
		public void run() {

			if (!mShowBrick) {
				drawSomethingLayout.setVisibility(View.INVISIBLE);
			}

		}
	};

	public LineGraphView(Context context) {
		super(context);
		paintBackground = new Paint();
		paintBackground.setStrokeWidth(4);
		setClickable(true);
		setFocusable(true);

		mPathPaint.setAntiAlias(true);
		mPathPaint.setColor(Color.BLUE);
		mPathPaint.setStyle(Paint.Style.STROKE);

		mPathAreaPaint.setAntiAlias(true);
		mPathAreaPaint.setDither(true);
		mPathAreaPaint.setColor(Color.BLUE);
		mPathAreaPaint.setAlpha(128);
		mPathAreaPaint.setStyle(Paint.Style.FILL);

		mPointPaint.setAntiAlias(true);
		mPointPaint.setDither(true);
		mPointPaint.setStyle(Paint.Style.FILL);
		mPointPaint.setStrokeCap(Cap.ROUND);
		mPointPaint.setStrokeJoin(Join.ROUND);

		mPointPaint2.setTextSize(13);
		mPointPaint2.setStyle(Paint.Style.STROKE);
		mPointPaint2.setAntiAlias(true);
		mPointPaint2.setDither(true);
		mPointPaint2.setStyle(Paint.Style.FILL);
		mPointPaint2.setStrokeCap(Cap.ROUND);
		mPointPaint2.setStrokeJoin(Join.ROUND);
	}

	@Override
	public void drawSomething(Canvas canvas, float graphwidth,
			float graphheight, double minX, double minY, double diffX,
			double diffY) {
		long a = System.currentTimeMillis();
		GraphData[] values = mGraph.get(0).values;
		// 如果绘制区域超出第一条线
		if (mLastX > graphwidth / getXSize() * values.length) {
			for (Graph g : mGraph) {
				if (g.values.length == getXSize()) {
					values = g.values;
					break;
				}
			}
		}

		if (mLastX == DEFAULT_LAST_X && values.length != 0) {
			mLastX = (int) (graphwidth / diffX * values[values.length - 1]
					.getValueX()) + 2;
		}
		final int bottom = getBottom();
		// 判断边界,画线
		int left = Math.min(Math.max(mLastX, 0), getWidth());
		paint.setColor(0XFFFD7F7F);
		float scaleY = 1;
		if (line.getHeight() != graphheight - dy - brick.getHeight()) {
			scaleY = (graphheight - dy - brick.getHeight()) / line.getHeight();
		}

		Matrix m = new Matrix();
		m.postScale(1, scaleY);
		Bitmap line1 = Bitmap.createBitmap(line, 0, 0, line.getWidth(),
				line.getHeight(), m, true);

		final float top = graphheight - line1.getHeight() - dy;
		if (!isDrawPoint)
			canvas.drawBitmap(line1, left - line1.getWidth() / 2, top, paint);
		double lastEndY = 0;
		double lastEndX = 0;

		float pointY = -10f;
		float pointX = -10f;
		String hint = (values.length == 0) ? "" : values[0].getStringValueY()
				+ values[0].getUnit();
		int brickDataIndex = 0;

		for (int i = 0; i < values.length; i++) {
			double valY = values[i].getValueY() - minY;
			double ratY = valY / diffY;
			double y = graphheight * ratY + dy;

			double valX = values[i].getValueX() - minX;
			double ratX = valX / diffX;
			double x = graphwidth * ratX;

			if (i > 0) {
				float startX = (float) lastEndX;
				float startY = (float) (-lastEndY) + graphheight;
				float endX = (float) x;
				float endY = (float) (-y) + graphheight;

				if (startX <= mLastX && mLastX <= endX) {

					hint = values[i - 1].getStringValueY()
							+ values[0].getUnit();
					brickDataIndex = i - 1;
					// 计算圆点圆心
					float scale = (mLastX - startX) / (endX - startX);
					midX = (endX - startX) / 2;
					if (mCircleFlow) {
						pointY = startY + (endY - startY) * scale;
						pointX = mLastX;
						if (mDx < 0) {
							hint = values[i].getStringValueY()
									+ values[0].getUnit();
							brickDataIndex = i;
						}
					} else {
						pointY = startY;
						pointX = startX;
						if ((mDx > 0 && (mLastX - startX) > midX)
								|| (mDx < 0 && (mLastX - startX) > midX)
								&& (mLastX > midX * 2 + 2)) {
							hint = values[i].getStringValueY()
									+ values[0].getUnit();
							brickDataIndex = i;
							pointX = endX;
							pointY = endY;
						} else {
							hint = values[i - 1].getStringValueY()
									+ values[0].getUnit();
							brickDataIndex = i - 1;
							pointX = startX;
							pointY = startY;
						}
					}
				} else if (mLastX < 0) {
					pointY = startY;
					pointX = startX;
					hint = values[0].getStringValueY() + values[0].getUnit();
					break;
				} else if (mLastX >= endX) {
					pointY = endY;
					pointX = endX;
					hint = values[i].getStringValueY() + values[0].getUnit();
					brickDataIndex = i;
				}
			}
			lastEndY = y;
			lastEndX = x;
		}
		if (obml != null) {
			obml.onMove(brickDataIndex);
			if (gesture == Action.UP) {
				obml.onStop(brickDataIndex);
			}
		}

		final int halfW = brick.getWidth() / 2;
		int tx = mLastX - halfW;
		final int ty = brick.getHeight() / 2;

		if (mLastX < halfW) {
			tx = 0;
		} else if (mLastX > getWidth() - halfW) {
			tx = getWidth() - brick.getWidth();
		}

		// 画砖头
		if (!isDrawPoint) {
			canvas.drawBitmap(brick, tx, getTop(), paint);

			// 画砖头文阴影
			mPointPaint.setStrokeWidth(0);
			mPointPaint.setColor(0xFFFFFFFF);
			mPointPaint.setTextAlign(Align.CENTER);
			final int txtSize = 20;
			mPointPaint.setTextSize(txtSize);
			canvas.drawText(hint, tx + halfW + 1, ty + txtSize / 2 + 1,
					mPointPaint);
			// 画砖头文
			mPointPaint.setColor(0XFF888888);
			canvas.drawText(hint, tx + halfW, ty + txtSize / 2 - 2, mPointPaint);
			if (mLastX > 0 && mLastX < graphwidth) {
				// 画外圆
				mPointPaint.setStrokeWidth(12f);
				mPointPaint.setColor(0XFFFFFFFF);
				canvas.drawPoint((float) pointX, pointY, mPointPaint);
				// 画内圆
				mPointPaint.setStrokeWidth(10f);
				mPointPaint.setColor(0XFF3075FF);
				canvas.drawPoint((float) pointX, pointY, mPointPaint);
			}
		}
		long b = System.currentTimeMillis();
	}

	/**
	 * 绘制曲线图
	 * 
	 * @param vanvas
	 *            画布
	 * @param values
	 *            画图所需数据集合
	 * @param grapwidth
	 *            曲线视图宽
	 * @param grapheight
	 *            曲线视图高
	 * @param border
	 */
	@Override
	public void drawSeries(Canvas canvas, int index, float graphwidth,
			float graphheight, double minX, double minY, double diffX,
			double diffY) {
		long a = System.currentTimeMillis();
		final GraphData[] values = mGraph.get(index).values;
		map = new HashMap<String, Float[]>();

		// 画曲线
		for (int i = 0; i < values.length; i++) {
			double valY = values[i].getValueY() - minY;
			double ratY = valY / diffY;
			double y = graphheight * ratY + dy;

			double valX = values[i].getValueX() - minX;
			double ratX = valX / diffX;
			double x = graphwidth * ratX + dx * 1.2f;

			if (i > 0) {
				float endX = (float) x + 1;
				float endY = (float) (-y) + graphheight;
				mGraphLine.lineTo(endX, endY);
				if (isDrawPoint) {
					map.put(i + "", new Float[] { endX - 1, endY });
				}
			} else {
				mGraphLine.rewind();
				final float startX = dx * 1.2f;
				final float startY = (float) (-y) + graphheight;
				mGraphLine.moveTo(startX, startY);
				map.put(i + "", new Float[] { startX, startY });
			}
		}
		mPathPaint.setStyle(Paint.Style.STROKE);
		mPathPaint.setStrokeWidth(2f);
		mPathPaint.setColor(mGraph.get(index).color);
		float pointSize = getResources().getDimensionPixelSize(
				R.dimen.default_graph_point_size);
		if (isDrawPoint) {
			mPathPaint.setStrokeWidth(pointSize / 4);
		}
		canvas.drawPath(mGraphLine, mPathPaint);

		if (isDrawPoint) {
			float dx = 0;// 调试x坐标label的位置
			float tempY = 0;// 记录点文字的Y坐标
			float ddy = dy;
			float sapceY = 0;
			// 每个曲线折点的圆点
			for (int i = 0; i < map.size(); i++) {
				Float[] point = map.get(i + "");
				// 画竖线
				mPointPaint2.setColor(0XFFB2B0B0);

				if (i == 0) {
					mPointPaint2.setStrokeWidth(2f);
					canvas.drawLine(point[0], ddy / 2, point[0], graphheight
							- dy, mPointPaint2);
					Path path = new Path();
					path.moveTo(point[0] - 10, ddy / 2);
					path.lineTo(point[0], 0);
					path.lineTo(point[0] + 10, ddy / 2);
					path.moveTo(point[0] - 10, ddy / 2);
					canvas.drawPath(path, mPointPaint2);
					canvas.drawText("(" + values[i].getUnit() + ")",
							point[0] + 10, ddy / 2, mPointPaint2);
				} else {
					mPointPaint2.setStrokeWidth(0);
					canvas.drawLine(point[0], ddy, point[0], graphheight - dy,
							mPointPaint2);
				}
				// 画外圆
				mPointPaint2.setStrokeWidth(pointSize);
				mPointPaint2.setColor(0X1FFFFFFF);
				canvas.drawPoint(point[0], point[1], mPointPaint2);
				// 画内圆
				mPointPaint2.setStrokeWidth(pointSize - 2);
				mPointPaint2.setColor(mGraph.get(index).color);
				canvas.drawPoint(point[0], point[1], mPointPaint2);

				if (index == 0 && i == map.size() - 1)
					lastX = point[0];
				String txt = values[i].getValueY() + values[i].getUnit();
				mPointPaint2.setColor(Color.BLACK);
				mPointPaint2.setTextAlign(Align.CENTER);
				mPointPaint2.setTextSize(getResources().getDimensionPixelSize(
						R.dimen.default_graph_textsize));
				if (i == 0) {
					sapceY = 10;
				} else {
					if (point[1] + 10 - tempY > mPointPaint2.getTextSize()) {
						sapceY = -15;
					} else {
						sapceY = 10;
					}
				}
				if (i == 0) {
					dx = txt.length() * mPointPaint2.getTextSize() / 2;
					canvas.drawText(txt, point[0] + mPointPaint2.getTextSize()
							+ 3, point[1] - sapceY, mPointPaint2);
				} else {
					canvas.drawText(txt, point[0] - mPointPaint2.getTextSize()
							/ 2, point[1] - sapceY, mPointPaint2);
				}
				tempY = point[1] - sapceY;
			}
		} else {

			// 画曲线背景
			if (drawBackground && values.length > 1) {
				mGraphLine.lineTo(graphwidth, graphheight - dy);
				mGraphLine.lineTo(0, graphheight - dy);
				mGraphLine.close();
				if (lg == null) {
					lg = new LinearGradient(0, 0, 0, getHeight(), new int[] {
							bkColor, Color.WHITE }, null, Shader.TileMode.CLAMP);
				}
				mPathAreaPaint.setShader(lg);
				canvas.drawPath(mGraphLine, mPathAreaPaint);
			} else {
				Float[] point = map.get(0 + "");
				// 画外圆
				mPointPaint2.setStrokeWidth(9f);
				mPointPaint2.setColor(0X1FFFFFFF);
				canvas.drawPoint(point[0], point[1], mPointPaint2);
				// 画内圆
				mPointPaint2.setStrokeWidth(7f);
				mPointPaint2.setColor(mGraph.get(index).color);
				canvas.drawPoint(point[0], point[1], mPointPaint2);
			}
		}
		long b = System.currentTimeMillis();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// 获得触碰手指个数
		final int point = event.getPointerCount();

		if (point == 1 && !isDrawPoint) {
			final int x = (int) event.getX();
			final int y = (int) event.getY();
			final int action = event.getAction();
			switch (action) {

			case MotionEvent.ACTION_MOVE:
				gesture = Action.MOVE;
				if (Math.abs(event.getX() - mLastX) > 1.5f) {
					if (Math.abs(x - mLastX) > 0) {
						mDx = x - mLastX;
						mLastX = x;
						mLastY = y;
						drawSomethingLayout.invalidate();
					}
				}

				break;
			case MotionEvent.ACTION_DOWN:
				gesture = Action.DOWN;
				mLastX = x;
				mLastY = y;
				// 取消Post
				removeCallbacks(showBrickRunnable);
				if (drawSomethingLayout.getVisibility() == View.INVISIBLE) {
					drawSomethingLayout.setVisibility(View.VISIBLE);
				}
				drawSomethingLayout.invalidate();
				if (getParent() != null) {
					getParent().requestDisallowInterceptTouchEvent(true);
				}

				break;
			case MotionEvent.ACTION_UP:
				gesture = Action.UP;
				mLastX = x;
				mLastY = y;
				drawSomethingLayout.invalidate();
				postDelayed(showBrickRunnable, 2000);
				if (getParent() != null) {
					getParent().requestDisallowInterceptTouchEvent(false);
				}
				break;
			}

		}
		return super.onTouchEvent(event);
	}

	public boolean isDrawBackground() {
		return drawBackground;
	}

	/**
	 * 是否填充曲线背景
	 * 
	 * @param color
	 *            背景渐变色
	 */
	public void setDrawBackground(boolean drawBackground, int color) {
		this.drawBackground = drawBackground;
		this.bkColor = color;
	}

	/**
	 * 设置砖头的图案
	 */
	public void setBrickResource(int resId) {
		Options op = new Options();
		op.inScaled = false;
		brick = BitmapFactory.decodeResource(getResources(), resId, op);
	}

	public void setBrickBitmap(Bitmap bmp) {
		brick = bmp;
	}

	/**
	 * 圆点是否跟随砖头移动
	 * 
	 * @return
	 */
	public boolean ismCircleFlow() {
		return mCircleFlow;
	}

	/**
	 * 圆点是否跟随砖头移动
	 * 
	 * @param mCircleFlow
	 */
	public void setCircleFlow(boolean mCircleFlow) {
		this.mCircleFlow = mCircleFlow;
	}

	/**
	 * 设置砖头移动监听
	 * 
	 * @param obml
	 */
	public void setOnBrickMoveListener(OnBrickMoveListener obml) {
		this.obml = obml;
	}

	/**
	 * 砖头移动监听
	 * 
	 * @author Administrator
	 * 
	 */
	public interface OnBrickMoveListener {

		/**
		 * @param graphIndex
		 *            曲线的索引
		 * @param graphDataIndex
		 *            曲线的第几条数据
		 * @param data
		 *            该条数据
		 */
		public void onMove(int graphDataIndex);

		public void onStop(int graphDataIndex);
	}

	/**
	 * 设置是否画折线的顶点
	 */
	public void setDrawPoint(boolean isDrawPoint) {
		this.isDrawPoint = isDrawPoint;
		if (isDrawPoint) {
			dx = getResources().getDimensionPixelSize(R.dimen.default_graph_dy);
		}
	}
}
