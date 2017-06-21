package com.bonc.mobile.hbmclient.activity;

import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.data.MenuDataSyncThread;
import com.bonc.mobile.hbmclient.util.LogUtil;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;

public class MenuFirstDataSyncActivity extends BaseActivity {

	private String menuCode;
	private String menuName;
	private MenuDataSyncThread dataThread;
	private Map<String, String> menuInfo;
	private Thread thread;
	private Handler handler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_NoTitleBar);
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		menuCode = bundle.getString(MenuEntryAdapter.KEY_MENU_CODE);
		// menuName=bundle.getString(MenuEntryAdapter.KEY_MENU_NAME);
		if (menuCode == null || "".equals(menuCode)) {
			LogUtil.info(this.getClass().toString(), "菜单编码为空.退出");

			finishSync();
			return;
		}

		List<Map<String, String>> menuList = new BusinessDao()
				.getMenuFisrt(menuCode);
		if (menuList == null || menuList.size() <= 0) {
			finish();
		} else {
			menuInfo = menuList.get(0);

			dataThread = new MenuDataSyncThread(MenuFirstDataSyncActivity.this,
					menuList);

			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					showTip();
					dataThread.start();
				}
			});

			handler.post(thread);
		}

	}

	private void finishSync() {
		// TODO Auto-generated method stub
		if (MenuDataSyncThread.isSynchring()) {
			MenuDataSyncThread.setInterupt(true);
		}
		finish();
	}

	/*
	 * 提示数据同步中.
	 */
	public void showTip() {
		pDialog = new ProgressDialog(MenuFirstDataSyncActivity.this);
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setTitle("提示");
		pDialog.setMessage("数据同步中....请稍候！");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				finishSync();
			}
		});
		pDialog.show();

	}

	@Override
	public void refreshListView(int which, int state) {
		if (which == -1) { // 同步结束.
			pDialog.dismiss();
			if (MenuDataSyncThread.isSynchring()) {
				MenuDataSyncThread.setInterupt(true);
			}
			intent = new Intent(MenuFirstDataSyncActivity.this,
					KPIHomeActivity.class);
			intent.putExtra("menu_name", menuName);
			intent.putExtra("menuCode", menuCode);

			startActivity(intent);
			overridePendingTransition(0, 0);

			finish();
		} else {
			handler.post(new Runnable() {

				@Override
				public void run() {
					String dateBetween = new BusinessDao().getMenuDataBetween(
							menuCode, menuInfo.get("dataTable"),
							menuInfo.get("dataType"));
					pDialog.setMessage(dateBetween);
				}
			});

		}
	}

}
