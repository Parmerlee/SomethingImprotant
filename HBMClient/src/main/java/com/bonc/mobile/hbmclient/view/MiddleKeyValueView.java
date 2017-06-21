/**
 * MiddleKeyValueView
 */
package com.bonc.mobile.hbmclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;

/**
 * @author liweigao
 *
 */
public class MiddleKeyValueView extends RelativeLayout {
	private TextView keyView;
	private TextView valueView;

	private final int id_divider = 11;

	/**
	 * @param context
	 * @param attrs
	 */
	public MiddleKeyValueView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialView(context);
	}

	private void initialView(Context context) {
		View divider = new View(context);
		divider.setBackgroundResource(R.mipmap.virtual_line);
		int divider_narrow = context.getResources().getDimensionPixelSize(
				R.dimen.divider_narrow);
		LayoutParams lp = new LayoutParams(
				LayoutParams.FILL_PARENT, divider_narrow);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		divider.setId(id_divider);
		addView(divider, lp);

		this.keyView = new TextView(context);
		this.keyView.setMaxLines(2);
		int padding = context.getResources().getDimensionPixelSize(
				R.dimen.range_padding);
		this.keyView.setPadding(padding, 0, padding, 0);
		LayoutParams lp_key = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp_key.addRule(RelativeLayout.ABOVE, id_divider);
		addView(keyView, lp_key);

		this.valueView = new TextView(context);
		this.valueView.setMaxLines(2);
		this.valueView.setPadding(padding, 0, padding, 0);
		LayoutParams lp_value = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp_value.addRule(RelativeLayout.BELOW, id_divider);
		addView(valueView, lp_value);
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
