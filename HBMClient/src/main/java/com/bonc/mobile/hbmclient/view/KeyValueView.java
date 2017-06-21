/**
 * KeyValueView
 */
package com.bonc.mobile.hbmclient.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.util.NumberUtil;

/**
 * @author liweigao
 *
 */
public class KeyValueView extends LinearLayout {
	private TextView keyView;
	private TextView valueView;

	/**
	 * @param context
	 * @param attrs
	 */
	public KeyValueView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialView(context, attrs);
	}

	private void initialView(Context context, AttributeSet attrs) {
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.lDivider);
		boolean hasDivider = ta.getBoolean(R.styleable.lDivider_showDivider,
				false);
		boolean padValue = ta.getBoolean(R.styleable.lDivider_padValue, false);
		ta.recycle();
		this.keyView = new TextView(context, attrs);
		this.keyView.setSingleLine();
		addView(keyView);
		if (hasDivider) {
			View divider = new View(context);
			LayoutParams lp = null;
			int orientation = getOrientation();
			int divider_narrow = context.getResources().getDimensionPixelSize(
					R.dimen.divider_narrow);
			if (LinearLayout.VERTICAL == orientation) {
				lp = new LayoutParams(LayoutParams.FILL_PARENT, divider_narrow);
				divider.setBackgroundResource(R.color.pink);
			} else {
				lp = new LayoutParams(divider_narrow, LayoutParams.FILL_PARENT);
				divider.setBackgroundResource(R.color.pink);
			}

			addView(divider, lp);
		} else {

		}
		this.valueView = new TextView(context, attrs);
		this.valueView.setSingleLine();
		if (padValue) {
			LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			p.leftMargin = NumberUtil.DpToPx(getContext(), 10);
			addView(valueView, p);
		} else {
			addView(valueView);
		}
	}

	public void setKeyText(String key) {
		this.keyView.setText(key);
	}

	public void setValueText(String value) {
		this.valueView.setText(value);
	}

	public void setKeyColor(int id) {
		this.keyView.setTextColor(getContext().getResources().getColor(id));
	}

	public void setValueColor(int id) {
		this.valueView.setTextColor(getContext().getResources().getColor(id));
	}

	public void setKeySize(int size) {
		this.keyView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
	}

	public void setValueSize(int size) {
		this.valueView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
	}

	public void setKeyAppearance(int id) {
		this.keyView.setTextAppearance(getContext(), id);
	}

	public void setValueAppearance(int id) {
		this.valueView.setTextAppearance(getContext(), id);
	}
}
