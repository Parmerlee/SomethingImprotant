package com.bonc.mobile.hbmclient.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.TextView;

import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.hbmclient.service.BoncService;

public class LogoutUtil {

    public static void Logout(Context context) {
        for (int i = 0; i < 5; i++) {

            System.out.println(i + "");
            context.stopService(new Intent(context, BoncService.class));
            ExitApplication.getInstance().exit();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());

            ((Activity) context).finish();
        }
    }

    //	static TextView alertMsg;
    static CountDownTimer timer;

    public static void timeCounter(final Context context, final AlertDialog dialog) {

        final TextView alertMsg = (TextView) dialog.findViewById(android.R.id.message);
        if (timer != null)
            timer.cancel();
        timer = new CountDownTimer(10000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {


                // Toast.makeText(MenuActivity.this,"AAA:"+(alertMsg==null),Toast.LENGTH_LONG).show();
                String str = "长时间未操作,登陆已超时,请重新登陆! " + millisUntilFinished / 1000 + "(" + "s)后自动退出。";


                LogUtils.logBySys("AAA:" + (str) + ";context:" + context.getClass().getName());

                alertMsg.setText(str);
            }

            @Override
            public void onFinish() {
                alertMsg.setText("长时间未操作,登陆已超时,即将自动退出 。");
                //LogoutUtil.Logout(context);

                if (dialog != null && dialog.isShowing()) {
                    LogoutUtil.Logout(context);
                }

            }
        };

        timer.start();
    }
}
