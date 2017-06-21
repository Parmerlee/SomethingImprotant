/**
 * 
 */
package com.bonc.mobile.hbmclient.observer.data_presentation;

import com.bonc.mobile.hbmclient.observer.AObservable;

/**
 * @author liweigao
 *
 */
public class TemplateDataPresentationObservable extends AObservable {
	private DataPresentationAsynTask mTask;

	protected void executeTask() {
		this.mTask = new DataPresentationAsynTask(this);
		this.mTask.execute();
	}

	protected void dataException() {
		if (mTask != null) {
			if (mTask.isCancelled()) {

			} else {
				mTask.cancel(true);
			}
		} else {

		}
	}

	@Override
	public void notifyObservers(Object arg) {
		// TODO Auto-generated method stub
		super.setChanged();
		super.notifyObservers(arg);
	}
}
