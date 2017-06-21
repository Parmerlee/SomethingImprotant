package com.bonc.mobile.hbmclient.asyn_task;

import android.os.AsyncTask;

import com.bonc.mobile.hbmclient.data.DataLoader;
import com.bonc.mobile.hbmclient.terminal.SimpleTerminalActivity;
import com.bonc.mobile.hbmclient.terminal.component.ViewComponent;

/**
 * @author liweigao
 * 
 */
public class FirstLoadDataAsynTask extends AsyncTask<String, Integer, Boolean> {
	private DataLoader dataLoader;
	SimpleTerminalActivity mSimpleTerminalActivity;
	ViewComponent mRootView;
	boolean first = false;

	public FirstLoadDataAsynTask(SimpleTerminalActivity sta) {
		dataLoader = new DataLoader();
		this.mSimpleTerminalActivity = sta;
		this.mRootView = sta.getViewComponent();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		this.mSimpleTerminalActivity.showTip();
	}

	@Override
	protected Boolean doInBackground(String... menuCodes) {
		if (menuCodes == null || menuCodes.length == 0) {
			return false;
		} else {
			this.dataLoader.menuDataLoad(menuCodes);
			if (isCancelled()) {
				return false;
			} else {
				this.mSimpleTerminalActivity.initialDate();
				mRootView.iteratorSetData(null);
				return true;
			}
		}
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (result) {
			this.mSimpleTerminalActivity.initialUI();
		} else {

		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		this.mSimpleTerminalActivity.noDataTip();
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

}
