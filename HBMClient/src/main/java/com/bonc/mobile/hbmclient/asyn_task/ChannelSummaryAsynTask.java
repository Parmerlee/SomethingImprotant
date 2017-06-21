/**
 * ChannelSummaryAsynTask
 */
package com.bonc.mobile.hbmclient.asyn_task;

import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.bonc.mobile.hbmclient.util.HttpUtil;

/**
 * @author liweigao
 *
 */
public class ChannelSummaryAsynTask extends AsyncTask<Object, Integer, String> {
	private ProgressDialog mProgressDialog;
	private OnPostListener listener;

	public ChannelSummaryAsynTask(Context context, OnPostListener l) {
		this.listener = l;
		this.mProgressDialog = new ProgressDialog(context);
		this.mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		this.mProgressDialog.setMessage("数据加载中...");
		this.mProgressDialog.setIndeterminate(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		this.mProgressDialog.show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected String doInBackground(Object... params) {
		String action = (String) params[0];
		Map args = (Map) params[1];
		return HttpUtil.sendRequest(action, args);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (isCancelled()) {

		} else {
			this.listener.onPost(result);
		}

		this.mProgressDialog.dismiss();
	}

	public interface OnPostListener {
		void onPost(String s);
	}

}
