package com.bonc.mobile.hbmclient.terminal.subactivity;

import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bonc.mobile.common.util.FileUtils;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;
import com.bonc.mobile.hbmclient.util.HttpUtil;

/** 新版本当月销量页面 */
public class CurrentMonthActivity extends AbsListCurMonthVolumeActivity {

	boolean isFirstLoad = true;

	public static Intent getCurrentMonthIntent(Context cont) {
		Intent ret = new Intent(cont, CurrentMonthActivity.class);
		return ret;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getOrder() == 1023) {
			mChatView.showContextMenu();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public JSONObject onLoading(String[] keys) {
		String action = intent.getExtras().getString(
				TerminalConfiguration.KEY_ACTION);
		if (this.configMap == null) {
			this.configMap = (Map<String, String>) intent.getExtras()
					.getSerializable(TerminalConfiguration.KEY_MAP);
		} else {

		}
		String result = HttpUtil.sendRequest(action, this.configMap);
		try {
			return new JSONObject(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		refreshList();
		return super.onContextItemSelected(item);
	}

	@Override
	public String[] onGetKeys(Bundle bundle) {
		// TODO Auto-generated method stub
		return intent.getExtras()
				.getString(TerminalConfiguration.KEY_RESPONSE_KEY)
				.split(Constant.DEFAULT_SPLIT);
	}

	@Override
	public String onGetTitle(Bundle bundle) {
		// TODO Auto-generated method stub
		return this.intent.getExtras().getString(
				TerminalConfiguration.TITLE_BIG);
	}

	@Override
	public String[] onGetColumnNames(Bundle bundle) {
		// TODO Auto-generated method stub
		return this.intent.getExtras()
				.getString(TerminalConfiguration.TITLE_COLUMN)
				.split(Constant.DEFAULT_SPLIT);
	}

	@Override
	protected JSONObject onChartDataLoading() {
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_share:
			FileUtils.shareScreen(this, true);
			break;
		}
	}

}
