package com.bonc.mobile.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.R;
import com.bonc.mobile.common.User;
import com.bonc.mobile.common.EventBus.DefaultEvent;
import com.bonc.mobile.common.net.HttpUtil;
import com.bonc.mobile.common.util.DES;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.LogUtils;

public class BaseService extends Service {
	static final int INTERVAL = 1000 * 60 * 5;
	static final String KEY_FLOW_ID = "flow_id";
	protected boolean join_msg;

	MessageThread msgthread;
	SharedPreferences pref;
	private Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		msgthread = new MessageThread();
		msgthread.start();
		context = this.getApplicationContext();
	}

	protected void stopLoop() {
		msgthread.quit = true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		msgthread.quit = true;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	void loadUserInfo() {
		User u = User.getInstance();
		if (u.userCode == null) {
			u.load(this);
		}
	}


	protected String getMsgPushAction() {
		return "";
	}

	protected boolean enableMsgPush() {
		return pref.getBoolean("msg_push", true);
	}

	protected void addNotification(int id, String content) {
		NotificationManager manager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		String appName = getString(R.string.app_name);
		notification.tickerText = appName;
		notification.icon = R.mipmap.ic_launcher;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.defaults = Notification.DEFAULT_SOUND;
		RemoteViews rv = new RemoteViews(getPackageName(),
				R.layout.notification_layout);
		rv.setImageViewResource(R.id.id_notification_image, notification.icon);
		rv.setTextViewText(R.id.id_notification_title, appName + "消息提示");
		rv.setTextViewText(R.id.id_notification_content, content);
		notification.contentView = rv;
		Intent intent = getAppIntent();
		intent.putExtra("launch_from_srv", true);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, 0);
		notification.contentIntent = pendingIntent;
		manager.notify(id, notification);
	}

	protected Intent getAppIntent() {
		return null;
	}

	class MessageThread extends Thread {
		boolean quit = false;

		@Override
		public void run() {
			while (!quit) {
				try {
					loadUserInfo();
					Map<String, String> params = new HashMap<String, String>();
					params.put(KEY_FLOW_ID, pref.getString(KEY_FLOW_ID, "0"));
					String msg = HttpUtil.sendRequest(DataUtil.getSettingE(
							BaseService.this, AppConstant.KEY_SERVER_PATH, ""),
							getMsgPushAction(), params);
					msg = DES.decrypt2(msg);
					handleMessage(new JSONObject(msg));
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(INTERVAL);
				} catch (Exception e) {
				}
			}
		}

		void handleMessage(JSONObject msg) {
			boolean flag = msg.optBoolean("flag");
			if (flag) {
				List<String> infoList = new ArrayList<String>();
				String info_text = "";
				JSONArray a = msg.optJSONArray("data");
				String last_id = "0";
				for (int i = 0; i < a.length(); i++) {
					JSONObject o = a.optJSONObject(i);
					if (join_msg) {
						info_text += o.optString("MSG_VALUE") + ";";
						if ((i + 1) % 3 == 0) {
							infoList.add(info_text);
							info_text = "";
						} else if ((i + 1) == a.length()) {
							infoList.add(info_text);
							info_text = "";
						}
					} else {
						infoList.add(o.optString("MSG_VALUE"));
					}
					last_id = o.optString("FLOW_ID");
				}
				DataUtil.saveSetting(pref, KEY_FLOW_ID, last_id);
				if (enableMsgPush()) {
					int i = 100;
					for (String s : infoList) {
						addNotification(i++, s);
					}
				}
			}
		}
	}
}
