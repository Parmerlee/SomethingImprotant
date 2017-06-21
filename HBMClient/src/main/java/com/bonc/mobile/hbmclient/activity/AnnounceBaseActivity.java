package com.bonc.mobile.hbmclient.activity;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bonc.mobile.common.activity.BaseDataActivity;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class AnnounceBaseActivity extends BaseDataActivity {

	protected String menuCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		menuCode = this.getIntent().getStringExtra(
				MenuEntryAdapter.KEY_MENU_CODE);
	}

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
			bindData(result, requestCode);
		}
	}

}
