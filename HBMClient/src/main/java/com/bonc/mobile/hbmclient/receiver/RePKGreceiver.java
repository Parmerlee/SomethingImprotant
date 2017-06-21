package com.bonc.mobile.hbmclient.receiver;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.DeletePkgActivity;

public class RePKGreceiver extends BroadcastReceiver {
	public final static String DELETE_PKG_NAME = "com.bonc.mobile.ac";

	@Override
	public void onReceive(final Context context, Intent intent) {
		// TODO Auto-generated method stub
		try {
			List<PackageInfo> packages = context.getPackageManager()
					.getInstalledPackages(0);
			PackageInfo packageInfo = null;
			for (int i = 0; i < packages.size(); i++) {
				packageInfo = packages.get(i);
				// Only display the non-system app info
				if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					if (packageInfo.packageName
							.equalsIgnoreCase(DELETE_PKG_NAME)) {
						Intent in = new Intent(context, DeletePkgActivity.class);
						in.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(in);
					}
				}
			}
		} catch (Exception e) {
			Toast.makeText(context,
					context.getResources().getString(R.string.pkg_deleted),
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}

	}

}
