package com.bonc.mobile.hbmclient.terminal.view;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.util.LogUtil;

/** This surface shall draw number as size of data obtuse bar on its blank. */
public class RainbowHorBarView extends SurfaceView implements
		SurfaceHolder.Callback {
	private static int ANIME_DELAY = 40;
	private static int ANIME_UP_TIME = 25;
	public final static int[] BAR_COLORS = new int[] { Color.rgb(0, 157, 219),
			Color.rgb(255, 188, 0), Color.rgb(51, 169, 15),
			Color.rgb(255, 67, 50), Color.rgb(192, 0, 153),
			Color.rgb(230, 230, 230), Color.rgb(0, 157, 219),
			Color.rgb(0, 157, 219), Color.rgb(0, 157, 219) };
	private SurfaceHolder mHolder;
	private double[] mValues = new double[0];
	private double mMaxValue = 0;
	private int mFrameTip = 0;
	private double mAnimeRatio = 1;
	private NumberFormat mVFormat;
	private String[] mValueLabels = new String[0];
	private RepaintRainbowBarThread mRefreshThread;

	/** Constructor for default. Draw nothing. */
	public RainbowHorBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public RainbowHorBarView(Context context, double[] datas) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
		setValues(datas);
	}

	private void init() {
		mHolder = getHolder();
		mVFormat = new DecimalFormat("0.00");
		setZOrderOnTop(true);
		mHolder.setFormat(PixelFormat.TRANSPARENT);
		mHolder.addCallback(this);
		setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getContext(), "" + mAnimeRatio,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		// return super.onTouchEvent(event);
		return false;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (mRefreshThread == null) {
			mRefreshThread = new RepaintRainbowBarThread(mHolder);
			mRefreshThread.setBreak(false);
			mRefreshThread.start();
			resetFrameTip();
			mRefreshThread.setLabels(mValueLabels);
		}
	}

	private void resetFrameTip() {
		// TODO Auto-generated method stub
		mFrameTip = 0;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mRefreshThread.setBreak(true);
		try {
			mRefreshThread.join();
			mRefreshThread = null;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setLabels(String[] labels) {
		mValueLabels = labels;
	}

	public void setValues(double[] values) {
		this.mValues = values;
		mMaxValue = 1;
		for (double value : mValues) {
			if (mMaxValue < value) {
				mMaxValue = value;
			}
		}
		Thread t = new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				double ratio_step = 1 / (double) ANIME_UP_TIME;
				mAnimeRatio = 0;
				while (true) {
					// if(mFrameTip<ANIME_UP_TIME){
					mFrameTip++;
					// }else{
					// break;
					// }
					int tip = mFrameTip;
					try {
						Thread.sleep(ANIME_DELAY);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (tip < ANIME_UP_TIME) {// begin animation
						mAnimeRatio = ratio_step * tip;
					} else {
						mAnimeRatio = 1;
					}
				}
			}
		};
		t.start();
	}

	/**
	 * Drawing thread.: >
	 * */
	private class RepaintRainbowBarThread extends Thread {
		private SurfaceHolder mmHolder;
		private boolean mmBreak = false;
		private String[] mmLabels;
		private float mmBarSize = 21;
		private float mmTextDistance = 15;
		private float mmTextSize = 16;// If surface text is distort,try set it
										// by integral exponential of 2

		public RepaintRainbowBarThread(SurfaceHolder holder) {
			mmHolder = holder;
		}

		@Override
		public synchronized void start() {
			// TODO Auto-generated method stub

			// t.start();
			super.start();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Paint textPaint = new Paint();
			textPaint.setAntiAlias(true);
			Paint barPaint = new Paint();
			Paint backPaint = new Paint();
			backPaint.setColor(0x00000000);
			backPaint.setStyle(Paint.Style.STROKE);
			backPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
			barPaint.setAntiAlias(true);
			// textPaint.setAntiAlias(true);
			Canvas c = null;

			while (!mmBreak) {
				textPaint.setTextSize(mmTextSize);
				try {
					Thread.sleep(5);
					c = mmHolder.lockCanvas();
					// c.drawColor(Color.TRANSPARENT);
					c.drawPaint(backPaint);
					float vtip = (float) ((c.getWidth() - mmTextSize * 6
							- mmTextDistance - mmBarSize) / mMaxValue);
					// double[] values=mmAnimeValues;
					String[] labels = mmLabels;
					synchronized (mValues) {
						float barSpace;// top of every bar;
						int sLength = mValues.length;
						if (sLength < 2) {
							barSpace = (c.getHeight() - mmBarSize) / 2;
						} else {
							barSpace = (c.getHeight() - mmBarSize)
									/ (sLength - 1);
							barSpace = barSpace - barSpace / 5;
						}
						float startY = barSpace / 2;
						for (int i = 0; i < mValues.length; i++) {
							barPaint.setColor(BAR_COLORS[i % BAR_COLORS.length]);
							float drawLength = (float) (vtip * mValues[i] * mAnimeRatio);

							c.drawCircle(mmTextSize * 3 + mmBarSize / 2 + 10, i
									* barSpace + mmBarSize / 2 + startY,
									mmBarSize / 2, barPaint);
							c.drawRect(mmTextSize * 3 + mmBarSize / 2 + 10, i
									* barSpace + startY, mmBarSize / 2
									+ drawLength + mmTextSize * 3, i * barSpace
									+ mmBarSize + startY, barPaint);
							c.drawCircle(mmTextSize * 3 + mmBarSize / 2
									+ drawLength, i * barSpace + mmBarSize / 2
									+ startY, mmBarSize / 2, barPaint);
							c.drawText(
									mVFormat.format(mValues[i] * mAnimeRatio)
											+ "%", mmTextSize * 3 + drawLength
											+ mmBarSize + mmTextDistance,
									barSpace * i + mmBarSize
											- ((mmBarSize - mmTextSize) / 2)
											- 2 + startY, textPaint);
							if (labels != null && i < labels.length
									&& labels[i] != null) {
								c.drawText(labels[i], 0, barSpace * i
										+ mmBarSize
										- ((mmBarSize - mmTextSize) / 2) - 2
										+ startY, textPaint);

							}
						}
					}

				} catch (Exception e) {
					LogUtil.info(getClass().toString(), "Surface error");
					e.printStackTrace();
				} finally {
					if (c != null) {
						mmHolder.unlockCanvasAndPost(c);
					}
				}
			}
		}

		public boolean isBreak() {
			return mmBreak;
		}

		public void setBreak(boolean mmBreak) {
			this.mmBreak = mmBreak;
		}

		public float getBarSize() {
			return mmBarSize;
		}

		public void setBarSize(float mmBarSize) {
			this.mmBarSize = mmBarSize;
		}

		public float getTextSize() {
			return mmTextSize;
		}

		public void setTextSize(float mmTextSize) {
			this.mmTextSize = mmTextSize;
		}

		public void setLabels(String[] labels) {
			mmLabels = labels;
		}
	}

}
