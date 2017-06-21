package com.bonc.mobile.hbmclient.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bonc.mobile.hbmclient.activity.WelcomeActivity;

public class NotifyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancelAll();

		Intent new_intent = new Intent(context, WelcomeActivity.class);
		new_intent.setAction(Intent.ACTION_MAIN);
		new_intent.addCategory(Intent.CATEGORY_LAUNCHER);
		new_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		context.startActivity(new_intent);
	}

}
