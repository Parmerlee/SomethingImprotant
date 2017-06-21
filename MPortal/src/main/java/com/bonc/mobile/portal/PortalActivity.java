package com.bonc.mobile.portal;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bonc.anhuimobile.ac.R;
import com.bonc.mobile.common.User;
import com.bonc.mobile.common.data.DatabaseManager;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.util.Base64;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.SharePreferenceUtil;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.common.view.HomeWatcher;
import com.bonc.mobile.common.view.HomeWatcher.OnHomePressedListener;
import com.bonc.mobile.portal.common.AppManager;
import com.bonc.mobile.portal.search.SearchMainActivity;

/**
 * @author sunwei
 */
public class PortalActivity extends BasePortalActivity {
	GridView gridView;
	DatabaseManager dbMan;
	String perm = "null";

	private ScreenStateRecevier mRecevier;
	private ScreenOffRecevier mOffRecevier;
	private TimeOutDialog mTimeUpDia;

	HomeWatcher mWatcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (User.getInstance().userCode == null)
			User.getInstance().load(this);

		SharePreferenceUtil.setLoginStatus(this, true);

		setContentView(R.layout.activity_main);
		setWatermarkImage();

		ImageView search = (ImageView) findViewById(R.id.search);
		findViewById(R.id.qrcode).setVisibility(View.VISIBLE);
		// findViewById(R.id.search).setVisibility(View.VISIBLE);


		this.findViewById(R.id.help).setVisibility(View.VISIBLE);
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setOnItemClickListener(this);
		loadAppList();
		dbMan = new DatabaseManager(this);
		loadAnnounce();
		// startSvc();

		handler.sendEmptyMessage(MyUtils.INT_MPROTAL_SEND);
		AppManager.getInstance().addActivity(this);
		mTimeUpDia = new TimeOutDialog(this);
		mRecevier = new ScreenStateRecevier();
		mOffRecevier = new ScreenOffRecevier();

		mWatcher = new HomeWatcher(this);

		mWatcher.setOnHomePressedListener(new OnHomePressedListener() {
			@Override
			public void onHomePressed() {
				PortalActivity.this.finish();
			}

			@Override
			public void onHomeLongPressed() {
			}
		});

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case MyUtils.INT_MPROTAL_SEND:

				Intent intent = new Intent(
						Base64.encode(MyUtils.STRING_PROTAL_ALIVE.getBytes()));
				intent.setPackage(MyUtils.mHBMClientPkgName);
				getApplicationContext().sendBroadcast(intent);
				this.sendEmptyMessageDelayed(MyUtils.INT_MPROTAL_SEND, 5000);
				break;

			default:
				break;
			}
		}
	};

	void startSvc() {
		Intent serviceIntent = new Intent(this, ProtalService.class);
		startService(serviceIntent);
	}

	@Override
	protected void onDestroy() {
		dbMan.close();
		sendBroadcast(new Intent(SharePreferenceUtil.mString_Logout), null);
		SharePreferenceUtil.setLoginStatus(this, false);
		handler.removeMessages(MyUtils.INT_MPROTAL_SEND);
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateAnnounceRead();

		mWatcher.startWatch();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.announce) {
			Intent intent = new Intent(this, MPAnnounceMainActivity.class);
			intent.putExtra("perm", perm);
			startActivity(intent);
		} else if (v.getId() == R.id.qrcode) {
			startActivity(new Intent(this, QrcodeActivity.class));
		} else if (v.getId() == R.id.search) {
			startActivity(new Intent(this, SearchMainActivity.class));
		} else if (v.getId()==R.id.help){
			startActivity(new Intent(this, HelpActivity.class));
		}
	}

	@Override
	protected void bindAppList(JSONObject result) {
		super.bindAppList(result);
		gridView.setAdapter(new PortalAdaper(this, manager.getAppList(),
				R.layout.grid_item, new String[] { AppManager.KEY_NAME,
						AppManager.KEY_ICON }, new int[] { R.id.label,
						R.id.icon }));
		JSONObject userInfo = result.optJSONObject("userInfo");
		TextViewUtils.setText(this, R.id.text11,
				JsonUtil.optString(userInfo, "USER_NAME"));
		TextViewUtils.setText(this, R.id.text21, userInfo.optString("FOURA_ID"));
		TextViewUtils.setText(this, R.id.text31,
				userInfo.optString("UPDATE_TIME"));
		new LoadAppIconTask(this).execute();
	}

	@Override
	protected void refreshIcon() {
		((BaseAdapter) gridView.getAdapter()).notifyDataSetChanged();
	}

	protected void loadAnnounce() {
		Map<String, String> param = new LinkedHashMap<String, String>();
		// 默认从第一条开启读起
		param.put("flow_id", PreferenceManager
				.getDefaultSharedPreferences(this).getString("flow_id", "-1"));
		new LoadAnnounceTask(this).execute("/sysResource/notice", param);
	}

	class LoadAnnounceTask extends HttpRequestTask {
		public LoadAnnounceTask(Context context) {
			super(context);
		}

		@Override
		protected void handleResult(JSONObject result) {
			bindAnnounce(result);
		}
	}

	void bindAnnounce(JSONObject result) {
		List<Map<String, String>> data = JsonUtil.toList(result
				.optJSONArray("data"));

		dbMan.replaceList("mp_announce", data);
		updateAnnounceRead();
		JSONArray authList = result.optJSONArray("authList");
		try {
			perm = authList.length() == 0 ? "null" : authList.join(",")
					.replace("\"", "'");
		} catch (JSONException e) {
		}
	}

	void updateAnnounceRead() {
		int tcount = (int) dbMan.queryNumEntries("mp_announce");
		int nread = tcount - DataUtil.getInt(this, "a_read");
		if (nread > 0) {
			findViewById(R.id.info_count).setVisibility(View.VISIBLE);
			TextViewUtils.setText(this, R.id.info_count, nread + "");
		} else
			findViewById(R.id.info_count).setVisibility(View.GONE);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		IntentFilter inf = new IntentFilter();
		AppManager.mTimestamp = -1;
		inf.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(mRecevier, inf);
		IntentFilter inf2 = new IntentFilter();
		inf2.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mOffRecevier, inf2);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		long ts = System.currentTimeMillis();
		if (AppManager.mTimestamp > 0
				&& ts - AppManager.mTimestamp > AppManager.TIME_OUT_WHILE) {
			mTimeUpDia.setCancelable(false);
			mTimeUpDia.show();
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		AppManager.mTimestamp = -1;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		AppManager.mTimestamp = System.currentTimeMillis();
		unregisterReceiver(mRecevier);
		unregisterReceiver(mOffRecevier);
	}

	public class TimeOutDialog extends AlertDialog {

		protected TimeOutDialog(Context context) {
			super(context);
			setTitle("登陆超时");
			setCancelable(false);
			setMessage("长时间未操作,登陆已超时,请重新登陆!");
			setButton(AlertDialog.BUTTON_NEUTRAL, "确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					callLogin();
				}
			});
			setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface arg0) {
					callLogin();
				}
			});
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		mWatcher.stopWatch();
	}

	private void callLogin() {
		AppManager.getInstance().exit();
		System.exit(0);
		finish();
	}

	private class ScreenStateRecevier extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			onRestart();
		}

	}

	private class ScreenOffRecevier extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			AppManager.mTimestamp = System.currentTimeMillis();
		}

	}
}
