/**
 * NoticeAsynTask
 */
package com.bonc.mobile.hbmclient.asyn_task;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;

/**
 * @author liweigao
 *
 */
public class NoticeAsynTask extends AsyncTask<Integer, Integer, Boolean> {
	public static PopupWindow mPopupWindow;
	private final WeakReference<View> mWeakReference;
	private String text;

	public NoticeAsynTask(View v, String text) {
		this.mWeakReference = new WeakReference<View>(v);
		this.text = text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Boolean doInBackground(Integer... params) {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (isCancelled()) {
				return false;
			} else {
				return true;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		View view = this.mWeakReference.get();
		if (view != null) {
			Context c = view.getContext();
			LayoutInflater li = LayoutInflater.from(c);
			if (this.mPopupWindow == null) {
				this.mPopupWindow = new PopupWindow(li.inflate(
						R.layout.popup_window, null),
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			}

			TextView v = (TextView) mPopupWindow.getContentView().findViewById(
					R.id.pop_text);
			v.setText(text);
			mPopupWindow.setWidth(c.getResources().getDimensionPixelSize(
					R.dimen.notice_width));
			if (view.isShown()) {
				if (mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
				}
				mPopupWindow.showAsDropDown(
						view,
						c.getResources().getDimensionPixelSize(
								R.dimen.notice_x_off),
						c.getResources().getDimensionPixelSize(
								R.dimen.notice_y_off));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (result) {
			View view = this.mWeakReference.get();
			if (view != null && this.mPopupWindow != null
					&& this.mPopupWindow.isShowing()) {
				this.mPopupWindow.dismiss();
			}
		} else {
			destroy();
		}

	}

	public void destroy() {
		if (this.mPopupWindow != null && this.mPopupWindow.isShowing()) {
			this.mPopupWindow.dismiss();
			this.mPopupWindow = null;
		}
	}
}