/**
 * RelationKpiAsynTask
 */
package com.bonc.mobile.hbmclient.asyn_task;

import android.os.AsyncTask;

import com.bonc.mobile.hbmclient.enum_type.ProcedureEnum;
import com.bonc.mobile.hbmclient.observer.relationkpi.ARelationKpiObservable;

/**
 * @author liweigao
 *
 */
public class RelationKpiAsynTask extends AsyncTask<Void, Integer, Boolean> {
	private ARelationKpiObservable subject;

	public RelationKpiAsynTask(ARelationKpiObservable o) {
		this.subject = o;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Boolean doInBackground(Void... params) {
		this.subject.requestServer();
		if (isCancelled()) {
			return false;
		} else {
			return true;
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
		this.subject.notifyObservers(ProcedureEnum.START);
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
			this.subject.notifyObservers(ProcedureEnum.END);
		} else {
			this.subject.notifyObservers(ProcedureEnum.EXCEPTION);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
	 */
	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onCancelled()
	 */
	@Override
	protected void onCancelled() {
		super.onCancelled();
		this.subject.notifyObservers(ProcedureEnum.EXCEPTION);
	}
}
