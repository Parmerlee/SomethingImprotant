package com.bonc.mobile.common.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.BaseApplication;
import com.bonc.mobile.common.R;
import com.bonc.mobile.common.User;
import com.bonc.mobile.common.util.FileUtils;
import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.common.util.LogoutUtil;
import com.bonc.mobile.common.util.UIUtil;

/**
 * @author sunwei
 */
public class BaseActivity extends Activity {

    public static final int TIME_OUT_WHILE = 10 * 60 * 1000;

    public static long mTimestamp = -1;
    private ScreenStateRecevier mRecevier;
    private ScreenOffRecevier mOffRecevier;
    //	private TimeOutDialog mTimeUpDia;
    public static final String PACKAGE_NAME_RV = "com.bonc.mobile.remoteview";

    private boolean isRemoteView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtil.disableScrShot(this);

        if (!AppConstant.SEC_ENH) {
            Log.d("AAAA", this.getClass().getName());
        }
        if (TextUtils.equals(this.getPackageName(), PACKAGE_NAME_RV)) {
            isRemoteView = true;
            initLinster();
        } else {
            isRemoteView = false;
        }
        User.getSingleInstance().load(this);
        LogUtils.logBySys(User.getInstance().toString());
    }

    private void initLinster() {
        BaseApplication.getInstance().addActivity(this);
//		mTimeUpDia = new TimeOutDialog(this);
        mRecevier = new ScreenStateRecevier();
        mOffRecevier = new ScreenOffRecevier();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        if (isRemoteView) {

            IntentFilter inf = new IntentFilter();
            mTimestamp = -1;
            inf.addAction(Intent.ACTION_SCREEN_ON);
            registerReceiver(mRecevier, inf);
            IntentFilter inf2 = new IntentFilter();
            inf2.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(mOffRecevier, inf2);
        }
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (isRemoteView) {

            mTimestamp = System.currentTimeMillis();
            if (mRecevier != null)
                unregisterReceiver(mRecevier);
            if (mOffRecevier != null)
                unregisterReceiver(mOffRecevier);
        }
    }

//	@Override
//	protected void onRestart() {
//		// TODO Auto-generated method stub
//		super.onRestart();
//		if (isRemoteView) {
//
//			long ts = System.currentTimeMillis();
//			if (mTimestamp > 0 && ts - mTimestamp > TIME_OUT_WHILE) {
//				mTimeUpDia.setCancelable(false);
//				mTimeUpDia.show();
//			}
//		}
//	}

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        mTimestamp = -1;
    }

    public class TimeOutDialog extends AlertDialog {

        public TimeOutDialog(Context context) {
            super(context);
            setTitle("登陆超时");
            setCancelable(false);
            setMessage("长时间未操作,登陆已超时,请重新登陆!");
            setButton(AlertDialog.BUTTON_NEUTRAL, "确定", new OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    LogoutUtil.Logout(BaseActivity.this);
                    // callLogin();
                }
            });
            setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface arg0) {
                    // TODO Auto-generated method stub
                    LogoutUtil.Logout(BaseActivity.this);
                    // callLogin();
                }
            });
        }

    }

    private class ScreenStateRecevier extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            onRestart();
        }

    }

    private class ScreenOffRecevier extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            mTimestamp = System.currentTimeMillis();
            // LogUtil.info("", "[]{}[] " + MenuActivity.mTimestamp);
        }

    }

    public void onClick(View v) {
        if (v.getId() == R.id.id_button_back) {
            finish();
        } else if (v.getId() == R.id.id_share) {
            FileUtils.shareScreen(this);
        }
    }

    protected void showToast(String toastMsg) {
        Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT).show();
    }

    protected void setWatermarkImage() {
        UIUtil.setWatermarkImage(this);
    }

}
