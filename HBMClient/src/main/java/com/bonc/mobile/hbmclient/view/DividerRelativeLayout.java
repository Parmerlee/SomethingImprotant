/**
 * DividerRelativeLayout
 */
package com.bonc.mobile.hbmclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.bonc.mobile.hbmclient.R;

/**
 * @author liweigao
 *
 */
public class DividerRelativeLayout extends RelativeLayout {
	/**
	 * @param context
	 * @param attrs
	 */
	public DividerRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialView(context);
	}

	private void initialView(Context context) {
		int divider_narrow = context.getResources().getDimensionPixelSize(
				R.dimen.divider_narrow);
		View divider1 = new View(context);
		divider1.setBackgroundResource(R.mipmap.divider_hor);
		LayoutParams lp_v = new LayoutParams(divider_narrow,
				LayoutParams.FILL_PARENT);
		lp_v.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		addView(divider1, lp_v);

		View divider2 = new View(context);
		divider2.setBackgroundResource(R.mipmap.divider_hor);
		LayoutParams lp_v2 = new LayoutParams(divider_narrow,
				LayoutParams.FILL_PARENT);
		lp_v2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		addView(divider2, lp_v2);

		View divider3 = new View(context);
		divider3.setBackgroundResource(R.mipmap.divider);
		LayoutParams lp_h = new LayoutParams(
				LayoutParams.FILL_PARENT, divider_narrow);
		lp_h.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		addView(divider3, lp_h);
		View divider4 = new View(context);
		divider4.setBackgroundResource(R.mipmap.divider);
		LayoutParams lp_h2 = new LayoutParams(
				LayoutParams.FILL_PARENT, divider_narrow);
		lp_h2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		addView(divider4, lp_h2);
	}
}
