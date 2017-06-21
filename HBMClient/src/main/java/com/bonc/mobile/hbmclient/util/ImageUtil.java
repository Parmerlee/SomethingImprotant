/**
 * TODO
 */
package com.bonc.mobile.hbmclient.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.MeasureSpec;

import com.bonc.mobile.hbmclient.R;

/**
 * @author liweigao
 * 
 */
public class ImageUtil {

	public static Bitmap fromViewToBitmap(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap b = view.getDrawingCache();
		return Bitmap.createBitmap(b);
	}

	public static Bitmap fromDrawableToBitmap(Context context, int id) {
		return ((BitmapDrawable) context.getResources().getDrawable(
				R.mipmap.watermark_background)).getBitmap();
	}

	public static Drawable fromBitmapToDrawable(Bitmap b) {
		return new BitmapDrawable(b);
	}

}
