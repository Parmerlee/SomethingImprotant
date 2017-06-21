/**
 * KeyValueView2
 */
package com.bonc.mobile.hbmclient.state.business_outlets.item;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.bonc.mobile.hbmclient.view.KeyValueView;

/**
 * @author liweigao
 *
 */
public class KeyValueView2 extends LinearLayout {
	private KeyValueView up;
	private KeyValueView down;

	/**
	 * @param context
	 * @param attrs
	 */
	public KeyValueView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialView(context, attrs);
	}

	private void initialView(Context context, AttributeSet attrs) {
		this.up = new KeyValueView(context, attrs);
		this.down = new KeyValueView(context, attrs);
		// setOrientation(LinearLayout.VERTICAL);
		addView(up);
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
	 * @param key
	 * @see KeyValueView#setKeyText(String)
	 */
	public void setUpKeyText(String key) {
		up.setKeyText(key);
	}

	/**
	 * @param id
	 * @see KeyValueView#setKeyColor(int)
	 */
	public void setUpKeyColor(int id) {
		up.setKeyColor(id);
	}

	/**
	 * @param size
	 * @see KeyValueView#setKeySize(int)
	 */
	public void setUpKeySize(int size) {
		up.setKeySize(size);
	}

	/**
	 * @param id
	 * @see KeyValueView#setKeyAppearance(int)
	 */
	public void setUpKeyAppearance(int id) {
		up.setKeyAppearance(id);
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

	/**
	 * @param value
	 * @see KeyValueView#setValueText(String)
	 */
	public void setUpValueText(String value) {
		up.setValueText(value);
	}

	/**
	 * @param id
	 * @see KeyValueView#setValueColor(int)
	 */
	public void setUpValueColor(int id) {
		up.setValueColor(id);
	}

	/**
	 * @param size
	 * @see KeyValueView#setValueSize(int)
	 */
	public void setUpValueSize(int size) {
		up.setValueSize(size);
	}

	/**
	 * @param id
	 * @see KeyValueView#setValueAppearance(int)
	 */
	public void setUpValueAppearance(int id) {
		up.setValueAppearance(id);
	}

}
