package com.bonc.mobile.hbmclient.asyn_task;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.bonc.mobile.hbmclient.terminal.component.ViewComponent;

public class ResetDateAsynTask extends AsyncTask<Void, Integer, Boolean> {

	private ProgressDialog mProgressDialog;
	private ViewComponent mRootView;

	public ResetDateAsynTask(ViewComponent vc, ProgressDialog pd) {
		this.mRootView = vc;
		this.mProgressDialog = pd;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if (mProgressDialog != null)
			mProgressDialog.show();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		if (mRootView != null)
			mRootView.iteratorSetData(null);
		return true;
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
		if (mProgressDialog != null)
			mProgressDialog.dismiss();
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (mRootView != null) {
			mRootView.iteratorUpdateView();
			mRootView.iteratorSetViewListener();
		}

		if (mProgressDialog != null)
			mProgressDialog.dismiss();
	}
}
