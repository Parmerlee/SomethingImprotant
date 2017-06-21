/**
 * KeyValueView3
 */
package com.bonc.mobile.hbmclient.state.business_outlets.item;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.view.KeyValueView;

/**
 * @author liweigao
 *
 */
public class KeyValueView3 extends LinearLayout {
	private TextView up;
	private KeyValueView down;

	/**
	 * @param context
	 * @param attrs
	 */
	public KeyValueView3(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialView(context, attrs);
	}

	private void initialView(Context context, AttributeSet attrs) {
		this.up = new TextView(context, attrs);
		this.up.setLines(2);
		this.up.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		this.down = new KeyValueView(context, attrs);
		setOrientation(LinearLayout.VERTICAL);
		// LinearLayout.LayoutParams lp = new
		// LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		addView(up);
		// lp.topMargin =
		// context.getResources().getDimensionPixelSize(R.dimen.margintop);
		addView(down);
	}

	/**
	 * @param key
	 * @see KeyValueView#setKeyText(String)
	 */
	public void setDownKeyText(String key) {
		down.setKeyText(key);
	}

	/**
	 * @param id
	 * @see KeyValueView#setKeyColor(int)
	 */
	public void setDownKeyColor(int id) {
		down.setKeyColor(id);
	}

	/**
	 * @param size
	 * @see KeyValueView#setKeySize(int)
	 */
	public void setDownKeySize(int size) {
		down.setKeySize(size);
	}

	/**
	 * @param id
	 * @see KeyValueView#setKeyAppearance(int)
	 */
	public void setDownKeyAppearance(int id) {
		down.setKeyAppearance(id);
	}

	/**
	 * @param value
	 * @see KeyValueView#setValueText(String)
	 */
	public void setDownValueText(String value) {
		down.setValueText(value);
	}

	/**
	 * @param id
	 * @see KeyValueView#setValueColor(int)
	 */
	public void setDownValueColor(int id) {
		down.setValueColor(id);
	}

	/**
	 * @param size
	 * @see KeyValueView#setValueSize(int)
	 */
	public void setDownValueSize(int size) {
		down.setValueSize(size);
	}

	/**
	 * @param id
	 * @see KeyValueView#setValueAppearance(int)
	 */
	public void setDownValueAppearance(int id) {
		down.setValueAppearance(id);
	}

	public void setUpText(String text) {
		this.up.setText(text);
	}

	public void setUpColor(int id) {
		this.up.setTextColor(getContext().getResources().getColor(id));
	}

	public void setUpSize(int size) {
		this.up.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
	}
}
