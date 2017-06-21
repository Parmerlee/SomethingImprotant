package com.bonc.mobile.hbmclient.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Administrator on 2016/12/28.
 */
public class TimeOutUtil {

    public static class TimeOutDialog extends AlertDialog {

        public TimeOutDialog(final Context context) {
            super(context);
            setTitle("登陆超时");
            setCancelable(false);
            setMessage("长时间未操作,登陆已超时,请重新登陆!");
            setButton(AlertDialog.BUTTON_NEUTRAL, "确定", new OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    LogoutUtil.Logout(getContext());
                    // callLogin();
                }
            });
            setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface arg0) {
                    // TODO Auto-generated method stub
                    LogoutUtil.Logout(context);
                    // callLogin();
                }
            });
        }

    }

    public static TimeOutDialog mInstance = null;

    public static TimeOutDialog getInstance(Context context) {
        if (mInstance == null) {
            synchronized (TimeOutUtil.class) {
                if (mInstance == null) {
                    mInstance = new TimeOutDialog(context);
                }
            }
        }
        return mInstance;
    }
}
