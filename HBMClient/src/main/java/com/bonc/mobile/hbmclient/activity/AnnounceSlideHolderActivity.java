package com.bonc.mobile.hbmclient.activity;

import org.json.JSONObject;

import com.bonc.mobile.common.net.HttpRequestTask;

import android.content.Context;

public class AnnounceSlideHolderActivity extends SlideHolderActivity {

	protected boolean firstQuery = true;

	protected void bindData(JSONObject result, int requestCode) {
	}

	protected class LoadDataTask2 extends HttpRequestTask {
		int requestCode;

		public LoadDataTask2(Context context, String basePath, int requestCode) {
			super(context, basePath);
			this.requestCode = requestCode;
		}

		@Override
		protected void handleResult(JSONObject result) {
			firstQuery = false;
			bindData(result, requestCode);
		}
	}
}
