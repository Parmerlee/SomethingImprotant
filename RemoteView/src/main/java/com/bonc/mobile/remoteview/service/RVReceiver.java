package com.bonc.mobile.remoteview.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class RVReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Intent serviceIntent = new Intent(context, RVService.class);
			serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(serviceIntent);
		}
	}

}
