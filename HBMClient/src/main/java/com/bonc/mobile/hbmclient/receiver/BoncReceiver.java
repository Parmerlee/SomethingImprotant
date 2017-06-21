package com.bonc.mobile.hbmclient.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bonc.mobile.hbmclient.common.Publicapp;
import com.bonc.mobile.hbmclient.service.BoncService;

public class BoncReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Intent serviceIntent = new Intent(Publicapp.getInstance(),
					BoncService.class);
			serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(serviceIntent);
		}

		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Intent serviceIntent = new Intent(Publicapp.getInstance(),
					BoncService.class);
			// serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// aaaaa
			context.stopService(serviceIntent);
		}

	}

}
