package com.bonc.mobile.remoteview.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.bonc.mobile.common.service.BaseService;
import com.bonc.mobile.common.util.Base64;
import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.SharePreferenceUtil;
import com.bonc.mobile.remoteview.activity.WelcomeActivity;

/**
 * 后台推送Service
 *
 * @author Lenevo
 */
public class RVService extends BaseService {

    MyBroadcastReceiver broadcast = new MyBroadcastReceiver();

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        // 注册广播 监听是否退出

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(Base64.encode(MyUtils.STRING_PROTAL_ALIVE
                .getBytes()));
        LogUtils.logBySys("action:" + Base64.encode(MyUtils.STRING_PROTAL_ALIVE.getBytes()));

        registerReceiver(broadcast, intentFilter); // 注册监听
    }

    @Override
    public void onDestroy() {

        unregisterReceiver(broadcast);
        super.onDestroy();
    }

    @Override
    protected String getMsgPushAction() {
        return "/sundry/msgPush";
    }

    @Override
    protected Intent getAppIntent() {
        return new Intent(this, WelcomeActivity.class);
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {

            LogUtils.logBySys("action1:" + intent.getAction());
            String action = new String(Base64.decode(intent.getAction()));
            LogUtils.logBySys("action2:" + action);

            if (MyUtils.STRING_PROTAL_ALIVE.equals(action)) {
                countTime = 10000;
                handler.removeMessages(MyUtils.INT_HBMPROTAL_GET);
                handler.sendEmptyMessage(MyUtils.INT_HBMPROTAL_GET);
            }

        }

    }

    long countTime;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            countTime = countTime - 1000;
            LogUtils.logBySys("conunt:" + countTime);
            if (countTime > 0) {
//                Toast.makeText(RVService.this, "get", Toast.LENGTH_SHORT).show();
                this.sendEmptyMessageDelayed(MyUtils.INT_HBMPROTAL_GET, 1000);

                SharePreferenceUtil.setLoginStatus(getApplicationContext(),
                        true);
            } else {

                SharePreferenceUtil.setLoginStatus(getApplicationContext(),
                        false);

                this.removeMessages(MyUtils.INT_HBMPROTAL_GET);
            }

            this.sendEmptyMessageDelayed(MyUtils.INT_HBMPROTAL_GET, 1000);
        }
    };

}
