package com.bonc.mobile.common.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.util.UIUtil;

/**
 * Displays and detects the user's unlock attempt, which is a drag of a finger
 * across 9 regions of the screen.
 * 
 * Is also capable of displaying a static pattern in "in progress", "wrong" or
 * "correct" states.
 * 九宫格
 */
public class PatternView extends View {

	private int mPaddingLeft;
	private int mPaddingRight;
	private int mPaddingTop;
	private int mPaddingBottom;

	// 定义画图笔刷以及画轨迹笔刷
	private Paint mPaint = new Paint();
	private Paint mPathPaint = new Paint();

	// 每个按钮的位置(单位:像素)
	private int[] patternPositionX = { 0, 0, 0 };// 800*480
	private int[] patternPositionY = { 0, 0, 0 };// 800*480

	// 每个按钮的大小(单位:dip)
	private int mBitmapWidth = 0;
	private int mBitmapHeight = 0;

	private OnPatternListener mOnPatternListener;
	private ArrayList<Cell> mPattern = new ArrayList<Cell>(9);

	/**
	 * Lookup table for the circles of the pattern we are currently drawing.
	 * This will be the cells of the complete pattern unless we are animating,
	 * in which case we use this to hold the cells we are drawing for the in
	 * progress animation.
	 */
	private boolean[][] mPatternDrawLookup = new boolean[3][3];

	/**
	 * the in progress point: - during interaction: where the user's finger is -
	 * during animation: the current tip of the animating line
	 */
	private float mInProgressX = -1;
	private float mInProgressY = -1;

	private DisplayMode mPatternDisplayMode = DisplayMode.Correct;
	private boolean mInputEnabled = true;
	private boolean mInStealthMode = false;// 是否关闭画路径

	private boolean mPatternInProgress = false;

	/**
	 * 正方形的宽和高
	 */
	private float mSquareWidth;
	private float mSquareHeight;

	private Bitmap mBitmapCircleDefault;// 圆圈默认图
	private Bitmap mBitmapCircleGreen;// 绿色圆圈
	private Bitmap mBitmapCircleRed;// 红色圆圈

	private Bitmap mBitmapArrowGreenUp;// 绿色箭头
	private Bitmap mBitmapArrowRedUp;// 红色箭头

	private final Path mCurrentPath = new Path();

	// 新加代码
	public boolean flag;

	/**
	 * 在没有锁住的绘图VIew中的3 X 3 的矩阵 绘制一个小细胞, Represents a cell in the 3 X 3 matrix of
	 * the unlock pattern view.
	 */

	public static class Cell {
		int row;
		int column;

		// keep # objects limited to 9
		static Cell[][] sCells = new Cell[3][3];
		static {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					sCells[i][j] = new Cell(i, j);
				}
			}
		}

		/**
		 * @param row
		 *            The row of the cell.
		 * @param column
		 *            The column of the cell.
		 */
		private Cell(int row, int column) {
			checkRange(row, column);
			this.row = row;
			this.column = column;
		}

		public int getRow() {
			return row;
		}

		public int getColumn() {
			return column;
		}

		/**
		 * @param row
		 *            The row of the cell.
		 * @param column
		 *            The column of the cell.
		 */
		public static synchronized Cell of(int row, int column) {
			checkRange(row, column);
			return sCells[row][column];
		}

		/**
		 * 检查范围
		 * 
		 * @param row
		 * @param column
		 */
		private static void checkRange(int row, int column) {
			if (row < 0 || row > 2) {
				throw new IllegalArgumentException("row must be in range 0-2");
			}
			if (column < 0 || column > 2) {
				throw new IllegalArgumentException(
						"column must be in range 0-2");
			}
		}

		public String toString() {
			return "(row=" + row + ",clmn=" + column + ")";
		}
	}

	/**
	 * How to display the current pattern.
	 */
	public enum DisplayMode {

		/**
		 * The pattern drawn is correct (i.e draw it in a friendly color)
		 * 绘制正确图案.
		 */
		Correct,

		/**
		 * Animate the pattern (for demo, and help). 绘制动画(for demo, and help)
		 */
		Animate,

		/**
		 * The pattern is wrong (i.e draw a foreboding color) 绘制错误图案.
		 */
		Wrong
	}

	/**
	 * The call back interface for detecting patterns entered by the user.
	 */
	public static interface OnPatternListener {

		/**
		 * A new pattern has begun. 开始绘制一个新的图案
		 */
		void onPatternStart();

		/**
		 * The pattern was cleared. 图案被清空
		 */
		void onPatternCleared();

		/**
		 * The user extended the pattern currently being drawn by one cell.
		 * 添加一个绘制按钮
		 * 
		 * @param pattern
		 *            The pattern with newly added cell.
		 */
		void onPatternCellAdded(List<Cell> pattern);

		/**
		 * A pattern was detected from the user.
		 * 
		 * @param pattern
		 *            The pattern.
		 */
		void onPatternDetected(List<Cell> pattern);
	}

	public PatternView(Context context) {

		this(context, null);
		this.context = context;
	}

	Context context;

	public PatternView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// mAspect = ASPECT_SQUARE;

		setClickable(true);

		mPathPaint.setAntiAlias(true);
		mPathPaint.setDither(true);
		mPathPaint.setColor(Color.BLACK);
		mPathPaint.setAlpha(128);
		mPathPaint.setStyle(Paint.Style.STROKE);
		mPathPaint.setStrokeJoin(Paint.Join.ROUND);
		mPathPaint.setStrokeCap(Paint.Cap.ROUND);

		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setFilterBitmap(true);

		mBitmapCircleDefault = BitmapFactory.decodeResource(getResources(),R.mipmap.img_btn_code_lock_point_area_default);
		mBitmapCircleGreen = BitmapFactory.decodeResource(getResources(),R.mipmap.img_btn_indicator_code_lock_point_area_green);
		mBitmapCircleRed = BitmapFactory.decodeResource(getResources(),R.mipmap.img_btn_indicator_code_lock_point_area_red);

		mBitmapArrowGreenUp = BitmapFactory.decodeResource(getResources(),
				R.mipmap.ic_daydep_direction_green_up);
		mBitmapArrowRedUp = BitmapFactory.decodeResource(getResources(),
				R.mipmap.ic_daydep_direction_red_up);

//		ViewGroup.LayoutParams params = this.getLayoutParams();
//		params.height = UIUtil.getDisplayHeight((Activity) context)/3;
//		params.width = UIUtil.getDisplayWidth((Activity) context)*2/3;
//		PatternView.this.setLayoutParams(params);
	}

	/**
	 * @return Whether the view is in stealth mode.
	 */
	public boolean isInStealthMode() {
		return mInStealthMode;
	}

	/**
	 * Set whether the view is in stealth mode. If true, there will be no
	 * visible feedback as the user enters the pattern.
	 * 
	 * @param inStealthMode
	 *            Whether in stealth mode.
	 */
	public void setInStealthMode(boolean inStealthMode) {
		mInStealthMode = inStealthMode;
	}

	/**
	 * Set the call back for pattern detection.
	 * 
	 * @param onPatternListener
	 *            The call back.
	 */
	public void setOnPatternListener(OnPatternListener onPatternListener) {
		mOnPatternListener = onPatternListener;
	}

	/**
	 * Set the pattern explicitely (rather than waiting for the user to input a
	 * pattern).
	 * 
	 * @param displayMode
	 *            How to display the pattern.
	 * @param pattern
	 *            The pattern.
	 */
	public void setPattern(DisplayMode displayMode, List<Cell> pattern) {
		mPattern.clear();
		mPattern.addAll(pattern);
		clearPatternDrawLookup();
		for (Cell cell : pattern) {
			mPatternDrawLookup[cell.getRow()][cell.getColumn()] = true;
		}

		setDisplayMode(displayMode);
	}

	/**
	 * Set the display mode of the current pattern. This can be useful, for
	 * instance, after detecting a pattern to tell this view whether change the
	 * in progress result to correct or wrong.
	 * 
	 * @param displayMode
	 *            The display mode.
	 */
	public void setDisplayMode(DisplayMode displayMode) {
		mPatternDisplayMode = displayMode;
		invalidate();
	}

	/**
	 * Clear the pattern.
	 */
	public void clearPattern() {
		resetPattern();
	}

	/**
	 * Reset all pattern state.
	 */
	private void resetPattern() {
		mPattern.clear();
		clearPatternDrawLookup();
		mPatternDisplayMode = DisplayMode.Correct;
		invalidate();
	}

	/**
	 * Clear the pattern lookup table.
	 */
	private void clearPatternDrawLookup() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				mPatternDrawLookup[i][j] = false;
			}
		}
	}

	/**
	 * Disable input (for instance when displaying a message that will timeout
	 * so user doesn't get view into messy state).
	 */
	public void disableInput() {
		mInputEnabled = false;
	}

	/**
	 * Enable input.
	 */
	public void enableInput() {
		mInputEnabled = true;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		final int width = w - mPaddingLeft - mPaddingRight;

		mSquareWidth = width / 3.0f;

		final int height = h - mPaddingTop - mPaddingBottom;
		mSquareHeight = height / 3.0f;
	}

	int unitwidth;
	int unitheigh;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int height = MeasureSpec.getSize(heightMeasureSpec);

		mBitmapWidth = mBitmapCircleDefault.getWidth();
		mBitmapHeight = mBitmapCircleDefault.getHeight();

		// 设置路径块宽度
		mPathPaint.setStrokeWidth(mBitmapWidth * 0.3f);

		unitwidth = width / 7;
		unitheigh = (int) (height / 7.5);

		for (int i = 0; i < 3; i++) {

			patternPositionX[i] = (int) (unitwidth * (i + 1) + unitwidth * i);
			patternPositionY[i] = (int) (unitheigh * (i) + unitheigh * i);

		}

		setMeasuredDimension(width, height);
	}

	/**
	 * Determines whether the point x, y will add a new point to the current
	 * pattern (in addition to finding the cell, also makes heuristic choices
	 * such as filling in gaps based on current pattern).
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 */
	private Cell detectAndAddHit(float x, float y) {
		final Cell cell = checkForNewHit(x, y);
		if (cell != null) {

			// check for gaps in existing pattern
			Cell fillInGapCell = null;
			final ArrayList<Cell> pattern = mPattern;
			if (!pattern.isEmpty()) {
				final Cell lastCell = pattern.get(pattern.size() - 1);
				int dRow = cell.row - lastCell.row;
				int dColumn = cell.column - lastCell.column;

				int fillInRow = lastCell.row;
				int fillInColumn = lastCell.column;

				// 如果当前选中的点与上一个点之间隔了一个点,那么将中间的点也一起选中.
				if (Math.abs(dRow) == 2 && Math.abs(dColumn) != 1) {
					fillInRow = lastCell.row + ((dRow > 0) ? 1 : -1);
				}

				if (Math.abs(dColumn) == 2 && Math.abs(dRow) != 1) {
					fillInColumn = lastCell.column + ((dColumn > 0) ? 1 : -1);
				}

				fillInGapCell = Cell.of(fillInRow, fillInColumn);
			}

			if (fillInGapCell != null
					&& !mPatternDrawLookup[fillInGapCell.row][fillInGapCell.column]) {
				addCellToPattern(fillInGapCell);
			}
			addCellToPattern(cell);
			return cell;
		}
		return null;
	}

	/**
	 * 添加细胞图案
	 * 
	 * @param newCell
	 */
	private void addCellToPattern(Cell newCell) {
		mPatternDrawLookup[newCell.getRow()][newCell.getColumn()] = true;
		mPattern.add(newCell);
		if (mOnPatternListener != null) {
			// 响应图案画上事件
			mOnPatternListener.onPatternCellAdded(mPattern);
		}
	}

	// helper method to find which cell a point maps to
	private Cell checkForNewHit(float x, float y) {

		final int rowHit = getRowHit(y);
		if (rowHit < 0) {
			return null;
		}
		final int columnHit = getColumnHit(x);
		if (columnHit < 0) {
			return null;
		}

		if (mPatternDrawLookup[rowHit][columnHit]) {
			return null;
		}
		return Cell.of(rowHit, columnHit);
	}

	/**
	 * Helper method to find the row that y falls into.
	 * 
	 * @param y
	 *            The y coordinate
	 * @return The row that y falls in, or -1 if it falls in no row.
	 */
	private int getRowHit(float y) {

		float hitSize = mBitmapWidth;

		for (int i = 0; i < 3; i++) {

			final float hitTop = mPaddingTop + patternPositionY[i];
			if (y >= hitTop - 10 && y <= hitTop + hitSize + 10) {

				return i;
			}
		}
		return -1;
	}

	/**
	 * Helper method to find the column x fallis into.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @return The column that x falls in, or -1 if it falls in no column.
	 */
	private int getColumnHit(float x) {
		float hitSize = mBitmapHeight;

		for (int i = 0; i < 3; i++) {

			final float hitLeft = mPaddingLeft + patternPositionX[i];
			if (x >= hitLeft - 10 && x <= hitLeft + hitSize + 10) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean onTouchEvent(MotionEvent motionEvent) {
		if (!mInputEnabled || !isEnabled()) {
			return false;
		}

		final float x = motionEvent.getX();
		final float y = motionEvent.getY();
		Cell hitCell;
		switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			resetPattern();
			hitCell = detectAndAddHit(x, y);
			if (hitCell != null && mOnPatternListener != null) {
				mPatternInProgress = true;
				mPatternDisplayMode = DisplayMode.Correct;
				mOnPatternListener.onPatternStart();
			} else if (mOnPatternListener != null) {
				mPatternInProgress = false;
				mOnPatternListener.onPatternCleared();
			}
			if (hitCell != null) {
				final float startX = getCenterXForColumn(hitCell.column);
				final float startY = getCenterYForRow(hitCell.row);

				final float widthOffset = mBitmapWidth / 2f;
				final float heightOffset = mBitmapHeight / 2f;

				// 只刷新方块区域
				invalidate((int) (startX - widthOffset),
						(int) (startY - heightOffset),
						(int) (startX + widthOffset),
						(int) (startY + heightOffset));
			}
			mInProgressX = x;
			mInProgressY = y;
			return true;
		case MotionEvent.ACTION_UP:
			// report pattern detected
			if (!mPattern.isEmpty() && mOnPatternListener != null) {
				mPatternInProgress = false;
				mOnPatternListener.onPatternDetected(mPattern);
				invalidate();
			}
			return true;
		case MotionEvent.ACTION_MOVE:
			hitCell = detectAndAddHit(x, y);
			final int patternSize = mPattern.size();
			// 如果手指在滑动过程中选中了第一个按钮
			if (hitCell != null && (mOnPatternListener != null)
					&& (patternSize == 1)) {
				mPatternInProgress = true;
				mOnPatternListener.onPatternStart();
			}

			// note current x and y for rubber banding of in progress
			// patterns
			final float dx = Math.abs(x - mInProgressX);
			final float dy = Math.abs(y - mInProgressY);

			// 如果手指移动了位置,这里考虑到用户体验,设有一定的滑动距离误差
			if (dx + dy > mBitmapWidth * 0.01f) {
				mInProgressX = x;
				mInProgressY = y;
				invalidate();
			}
			return true;
		}
		return false;
	}

	private float getCenterXForColumn(int column) {

		return mPaddingLeft + patternPositionX[column] + mBitmapWidth / 2f;
	}

	private float getCenterYForRow(int row) {

		return mPaddingTop + patternPositionY[row] + mBitmapHeight / 2f;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		final ArrayList<Cell> pattern = mPattern;
		final int count = pattern.size();
		final boolean[][] drawLookup = mPatternDrawLookup;

		final Path currentPath = mCurrentPath;
		currentPath.rewind();

		// TODO: the path should be created and cached every time we hit-detect
		// a cell
		// only the last segment of the path should be computed here
		// draw the path of the pattern (unless the user is in progress, and
		// we are in stealth mode)
		final boolean drawPath = (!mInStealthMode || mPatternDisplayMode == DisplayMode.Wrong);
		if (drawPath) {
			boolean anyCircles = false;
			for (int i = 0; i < count; i++) {
				Cell cell = pattern.get(i);

				// only draw the part of the pattern stored in
				// the lookup table (this is only different in the case
				// of animation).
				if (!drawLookup[cell.row][cell.column]) {
					break;
				}
				anyCircles = true;

				float centerX = getCenterXForColumn(cell.column);
				float centerY = getCenterYForRow(cell.row);
				if (i == 0) {
					currentPath.moveTo(centerX, centerY);
				} else {
					currentPath.lineTo(centerX, centerY);
				}
			}

			// add last in progress section
			if ((mPatternInProgress) && anyCircles) {
				currentPath.lineTo(mInProgressX, mInProgressY);
			}
			canvas.drawPath(currentPath, mPathPaint);
		}

		// draw the circles
		final int paddingTop = mPaddingTop;
		final int paddingLeft = mPaddingLeft;
		for (int i = 0; i < 3; i++) {

			float centerY = getCenterYForRow(i);
			for (int j = 0; j < 3; j++) {
				float centerX = getCenterXForColumn(j);
				drawCircle(canvas, (int) centerX, (int) centerY,
						drawLookup[i][j]);
			}
		}

		// draw the arrows associated with the path (unless the user is in
		// progress, and
		// we are in stealth mode)
		if (drawPath) {
			for (int i = 0; i < count - 1; i++) {
				Cell cell = pattern.get(i);
				Cell next = pattern.get(i + 1);

				// only draw the part of the pattern stored in
				// the lookup table (this is only different in the case
				// of animation).
				if (!drawLookup[next.row][next.column]) {
					break;
				}

				float leftX = paddingLeft + patternPositionX[cell.column];
				float topY = paddingTop + patternPositionY[cell.row];
				drawArrow(canvas, leftX, topY, cell, next);
			}
		}
	}

	private void drawArrow(Canvas canvas, float leftX, float topY, Cell start,
			Cell end) {
		boolean green = mPatternDisplayMode != DisplayMode.Wrong;

		final int endRow = end.row;
		final int startRow = start.row;
		final int endColumn = end.column;
		final int startColumn = start.column;

		// compute transform to place arrow bitmaps at correct angle inside
		// circle.
		// This assumes that the arrow image is drawn at 12:00 with it's top
		// edge
		// coincident with the circle bitmap's top edge.
		Bitmap arrow = green ? mBitmapArrowGreenUp : mBitmapArrowRedUp;
		Matrix matrix = new Matrix();
		final int cellWidth = mBitmapWidth;
		final int cellHeight = mBitmapHeight;

		// 获得箭头的角度
		// the up arrow bitmap is at 12:00, so find the rotation from x axis and
		// add 90 degrees.
		final float theta = (float) Math.atan2((double) (endRow - startRow),
				(double) (endColumn - startColumn));
		final float angle = (float) Math.toDegrees(theta) + 90.0f;

		// compose matrix
		matrix.setTranslate(leftX, topY); // transform to cell position
		matrix.preRotate(angle, cellWidth / 2.0f, cellHeight / 2.0f); // rotate
																		// about
																		// cell
																		// center
		matrix.preTranslate((cellWidth) * 2f / 6f, 0.0f); // translate to 12:00
															// pos

		mPaint.setAntiAlias(true);
		canvas.drawBitmap(arrow, matrix, mPaint);
	}

	/**
	 * @param canvas
	 * @param leftX
	 * @param topY
	 * @param partOfPattern
	 *            Whether this circle is part of the pattern.
	 */
	private void drawCircle(Canvas canvas, int leftX, int topY,
			boolean partOfPattern) {
		Bitmap outerCircle;

		if (!partOfPattern
				|| (mInStealthMode && mPatternDisplayMode != DisplayMode.Wrong)) {
			// unselected circle
			outerCircle = mBitmapCircleDefault;
		} else if (mPatternInProgress) {
			// user is in middle of drawing a pattern
			outerCircle = mBitmapCircleGreen;
		} else if (mPatternDisplayMode == DisplayMode.Wrong) {
			// the pattern is wrong
			outerCircle = mBitmapCircleRed;
		} else if (mPatternDisplayMode == DisplayMode.Correct
				|| mPatternDisplayMode == DisplayMode.Animate) {
			// the pattern is correct
			outerCircle = mBitmapCircleGreen;
		} else {
			throw new IllegalStateException("unknown display mode "
					+ mPatternDisplayMode);
		}

		final float widthOffset = mBitmapWidth / 2f;
		final float heightOffset = mBitmapHeight / 2f;
		mPaint.setAntiAlias(true);
		mPaint.setFilterBitmap(true);

		canvas.drawBitmap(outerCircle, leftX - widthOffset,
				topY - heightOffset, mPaint);
	}
}
