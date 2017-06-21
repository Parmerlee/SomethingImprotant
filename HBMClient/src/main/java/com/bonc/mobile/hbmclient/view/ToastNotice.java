/**
 * TODO
 */
package com.bonc.mobile.hbmclient.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;

/**
 * @author liweigao
 *
 */
public class ToastNotice extends Toast {
	private LayoutInflater mLayoutInflater;
	private int gravity;
	private int x;
	private int y;

	/**
	 * @param context
	 */
	public ToastNotice(Context context, int gravity, int x, int y) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.gravity = gravity;
		this.x = x;
		this.y = y;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		View v = this.mLayoutInflater.inflate(R.layout.layout_toast_notice,
				null);
		setView(v);
		setGravity(gravity, x, y);
		setDuration(Toast.LENGTH_SHORT);
		super.show();
	}
}
