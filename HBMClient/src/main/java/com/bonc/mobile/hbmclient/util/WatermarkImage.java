/**
 * TODO
 */
package com.bonc.mobile.hbmclient.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.view.View;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.Constant;

/**
 * @author liweigao
 * 
 */
public class WatermarkImage {
	private static Drawable mDrawable = null;
	private static Drawable mLandDrawable = null;

	public static void setWatermarkDrawable(Context context, String tag) {
		Bitmap src = ImageUtil.fromDrawableToBitmap(context,
				R.mipmap.watermark_background);

		TextView tv = new TextView(context);
		tv.setTextSize(15);
		tv.setText(tag);
		Bitmap watermark = getRemodelBitmap(tv);
		Bitmap compositeBitmap = getWatermarkBitmap(src, watermark);
		src.recycle();
		watermark.recycle();
		Bitmap finalBitmap;
		if (Constant.COMPRESS_WATERMARK) {
			finalBitmap = ThumbnailUtils.extractThumbnail(compositeBitmap,
					compositeBitmap.getWidth() / 2,
					compositeBitmap.getHeight() / 2);
			compositeBitmap.recycle();
		} else {
			finalBitmap = compositeBitmap;
		}
		// 垂直界面
		mDrawable = ImageUtil.fromBitmapToDrawable(finalBitmap);
		// 水平界面
		mLandDrawable = ImageUtil.fromBitmapToDrawable(remodelBitmap(
				finalBitmap, 90));
	}

	public static Drawable getWatermarkDrawable() {
		return mDrawable;
	}

	public static Drawable getWatermarkLandDrawable() {
		return mLandDrawable;
	}

	private static Bitmap getWatermarkBitmap(Bitmap src, Bitmap watermark) {
		int src_w = src.getWidth();
		int src_h = src.getHeight();

		Bitmap newBitmap = Bitmap.createBitmap(src_w, src_h, Config.RGB_565);
		Canvas c = new Canvas(newBitmap);
		c.drawBitmap(src, 0, 0, null);
		// 确定文字显示的位置
		c.drawBitmap(watermark, 0, 100, null);
		c.drawBitmap(watermark, 90, 250, null);
		c.drawBitmap(watermark, 180, 400, null);
		c.drawBitmap(watermark, 270, 550, null);
		c.drawBitmap(watermark, 360, 700, null);
		c.drawBitmap(watermark, 450, 850, null);
		c.drawBitmap(watermark, 540, 1000, null);
		c.drawBitmap(watermark, 630, 1150, null);
		c.save(Canvas.ALL_SAVE_FLAG);
		c.restore();
		return newBitmap;
	}

	private static Bitmap getRemodelBitmap(View view) {
		Bitmap b = ImageUtil.fromViewToBitmap(view);
		// 旋转角度
		return remodelBitmap(b, -45);
	}

	private static Bitmap remodelBitmap(Bitmap b, float degree) {
		Matrix m = new Matrix();
		m.reset();
		m.setRotate(degree);

		return Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m,
				true);
	}
}
