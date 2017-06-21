/**
 * 
 */
package com.bonc.mobile.hbmclient.observer.data_presentation;

import android.os.AsyncTask;

import com.bonc.mobile.hbmclient.enum_type.ProcedureEnum;
import com.bonc.mobile.hbmclient.observer.IObservable;

/**
 * @author liweigao
 *
 */
public class DataPresentationAsynTask extends AsyncTask<Void, Integer, Boolean> {
	private IObservable subject;

	public DataPresentationAsynTask(IObservable o) {
		this.subject = o;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		this.subject.setChanged();
		if (isCancelled()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		this.subject.notifyObservers(ProcedureEnum.START);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (result) {
			this.subject.notifyObservers(ProcedureEnum.END);
		} else {
			this.subject.notifyObservers(ProcedureEnum.EXCEPTION);
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
		this.subject.notifyObservers(ProcedureEnum.EXCEPTION);
	}
}
