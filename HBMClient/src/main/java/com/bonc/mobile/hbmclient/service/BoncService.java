package com.bonc.mobile.hbmclient.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.bonc.mobile.common.User;
import com.bonc.mobile.common.util.Base64;
import com.bonc.mobile.common.util.DES;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.SharePreferenceUtil;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.common.Publicapp;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.HttpUtil;
import com.bonc.mobile.hbmclient.util.SMTools;
import com.bonc.mobile.hbmclient.util.StringUtil;

public class BoncService extends Service {
    private final int SLEEP_TIME = 5 * 60 * 1000;
    private String resultString = "";
    private Map<String, String> param;
    private int flowid = -1;
    private static SharedPreferences preferences;
    private List<String> infoTextList = new ArrayList<String>();
    private final String KEY_DDW_FLOW_ID = "ddwFlowId";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(broadcast);
        System.out.println("detory");
        super.onDestroy();
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    void loadUserInfo() {
        User u = User.getInstance();
        if (u.userCode == null) {
            u.load(this);
        }
    }

    MyBroadcastReceiver broadcast = new MyBroadcastReceiver();

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean firstShown = false;
                Date lastNotifyTime = null;
                while (true) {
                    try {
                        loadUserInfo();
                        if (infoTextList == null) {
                            infoTextList = new ArrayList<String>();
                        } else {
                            infoTextList.clear();
                        }

                        param = new HashMap<String, String>();
                        String flowidS = getLastNotification() + "";
                        String ddwFlowId = getDDWFlowId();
                        param.put(KEY_DDW_FLOW_ID, ddwFlowId);
                        param.put("flowid", flowidS);
                        param.put("user_lastsent_time", getUserLastsentTime());
                        param.put("imsi",
                                new SMTools(Publicapp.getInstance()).getIMSI());
                        resultString = HttpUtil.sendRequest(
                                ActionConstant.DATA_UPDATE_TIMEINFO, param);
                        resultString = DES.decrypt2(resultString);
                        if (!StringUtil.isNull(resultString)) {// 首先判断请求回的通知不能为空
                            JSONObject resObject = new JSONObject(resultString);
                            // 用户发送认证消息后提示.
                            try {
                                JSONArray sentArray = resObject
                                        .optJSONArray("notice_msgs");
                                if (sentArray != null && sentArray.length() > 0) {
                                    int id = 100;
                                    for (int i = 0; i < sentArray.length(); i++) {
                                        addNotification(id + i,
                                                sentArray.optString(i));
                                    }
                                    setUserLastsetTime(resObject
                                            .optString("user_lastsent_time"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                JSONObject jo_dataDelayWarning = resObject
                                        .optJSONObject("dataDelayWarning");
                                if (jo_dataDelayWarning != null) {
                                    String ddwid = jo_dataDelayWarning
                                            .optString("flowId");
                                    String msg = jo_dataDelayWarning
                                            .optString("msg");
                                    if (ddwid != null) {
                                        setDDWFlowId(ddwid);
                                        addNotification(1000, msg);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                // JSONArray bdArray = new
                                // JSONArray(resultString);
                                JSONArray bdArray = resObject
                                        .optJSONArray("update_datas");
                                if (bdArray.length() > 0) {
                                    String info_text_all = "";// 通知的时间戳和内容
                                    String[] menuCodes = new String[bdArray
                                            .length()];
                                    int newID = -1;
                                    for (int i = 0; i < bdArray.length(); i++) {
                                        JSONObject jsonObject = bdArray
                                                .optJSONObject(i);
                                        String info_text = jsonObject
                                                .getString("data_update_desc");// 内容
                                        int tmpFlowId = jsonObject
                                                .getInt("flow_id");
                                        String mc = jsonObject
                                                .optString("mod_type_c");
                                        int is_push = jsonObject.optInt(
                                                "is_push", 1);

                                        if (!StringUtil.isNull(mc)) {
                                            menuCodes[i] = mc;
                                        }
                                        if (tmpFlowId > newID)
                                            newID = tmpFlowId;
                                        if (is_push == 0) {
                                            continue;
                                        }
                                        if (!isUserLoginToday()) {
                                            if (isNeedReset(lastNotifyTime))
                                                firstShown = false;
                                            if (firstShown) {
                                                continue;
                                            } else {
                                                firstShown = true;
                                            }
                                        }
                                        lastNotifyTime = new Date();

                                        if (info_text_all.indexOf(info_text) == -1) {
                                            info_text_all += info_text + ";";
                                        }

                                        // 大于等于三条的情况下，则分几个显示
                                        if ((i + 1) % 3 == 0) {
                                            infoTextList.add(info_text_all);
                                            info_text_all = "";
                                        } else if ((i + 1) == bdArray.length()) {
                                            infoTextList.add(info_text_all);
                                            info_text_all = "";
                                        }
                                    }

                                    flowid = getLastNotification();
                                    if (newID > flowid) {
                                        // 判断消息推送开关状态，关：不提醒notification，开：提醒
                                        String pushStatus = Publicapp
                                                .getNotifyFlag();
                                        if (pushStatus
                                                .equals(getResources()
                                                        .getString(
                                                                R.string.notify_open))) {

                                            for (int i = 0; i < infoTextList
                                                    .size(); i++) {
                                                addNotification(i + 1,
                                                        infoTextList.get(i));
                                            }

                                        }
                                        setLastNotify(newID);
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // 每5分钟请求一次.
                    try {
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        // 注册广播 监听是否退出

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(Base64.encode(MyUtils.STRING_PROTAL_ALIVE
                .getBytes()));

        registerReceiver(broadcast, intentFilter); // 注册监听

    }

    private int getLastNotification() {
        try {
            Context otherContext = createPackageContext(Constant.PKG_NAME,
                    Context.CONTEXT_IGNORE_SECURITY);
            SharedPreferences sharedPreferences = otherContext
                    .getSharedPreferences("SETTING",
                            Context.MODE_WORLD_READABLE);
            flowid = sharedPreferences.getInt("notifyLastTime", -1);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return flowid;
    }

    /**
     * 获取用户最后发送认证时间的字符串.
     *
     * @return
     */
    private String getUserLastsentTime() {
        String lastSentTime = "";
        try {
            Context otherContext = createPackageContext(Constant.PKG_NAME,
                    Context.CONTEXT_IGNORE_SECURITY);
            SharedPreferences sharedPreferences = otherContext
                    .getSharedPreferences("SETTING",
                            Context.MODE_WORLD_READABLE);
            lastSentTime = sharedPreferences.getString("userLastsentTime",
                    DateUtil.formatter(Calendar.getInstance().getTime(),
                            "yyyy-MM-dd 00:00:00"));
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return lastSentTime;
    }

    private boolean isUserLoginToday() {
        String t = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("userLastLoginTime", null);
        if (t == null)
            return false;
        Date loginTime = DateUtil.getDate(t, "yyyy-MM-dd HH:mm:ss");
        Date today = DateUtil.getDate(DateUtil.formatter(Calendar.getInstance()
                .getTime(), "yyyy-MM-dd"), "yyyy-MM-dd");
        return loginTime.after(today);
    }

    private boolean isNeedReset(Date lastNotify) {
        if (lastNotify == null)
            return true;
        String lastNotifyDay = DateUtil.formatter(lastNotify, "yyyy-MM-dd");
        String today = DateUtil.formatter(new Date(), "yyyy-MM-dd");
        return !lastNotifyDay.equals(today);
    }

    private void setUserLastsetTime(String lastsetTime) {
        preferences = getSharedPreferences("SETTING", MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userLastsentTime", lastsetTime);
        editor.commit();
    }

    private void setLastNotify(int notifyLastTime) {
        // 获得可编辑对象
        preferences = getSharedPreferences("SETTING", MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("notifyLastTime", notifyLastTime);
        long nowT = Calendar.getInstance().getTimeInMillis();
        editor.putLong("notifyRealTime", nowT);
        editor.commit();
    }

    private void addNotification(int id, String content) {
        NotificationManager manager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification();
        notification.flags = Notification.FLAG_AUTO_CANCEL;// 点击后不显示通知
        notification.icon = R.mipmap.notify;
        notification.tickerText = "掌上分析系统有新数据";
        notification.defaults = Notification.DEFAULT_SOUND;//
        notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER;

        String action_notify = getResources().getString(
                R.string.action_notify_broadcast);
        Intent intent = new Intent(action_notify);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, -1,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews rv = new RemoteViews(Constant.PKG_NAME,
                R.layout.notification_layout);
        rv.setImageViewResource(R.id.id_notification_image, R.mipmap.notify);
        rv.setTextViewText(R.id.id_notification_title, "掌上分析系统，消息提示");
        rv.setTextViewText(R.id.id_notification_content, content);
        notification.contentView = rv;
        notification.contentIntent = pendingIntent;
        manager.notify(id, notification);
    }

    private String getDDWFlowId() {
        SharedPreferences sp = getSharedPreferences("SETTING",
                Context.MODE_WORLD_READABLE);
        return sp.getString(KEY_DDW_FLOW_ID, "-1");
    }

    private void setDDWFlowId(String id) {
        // 获得可编辑对象
        preferences = getSharedPreferences("SETTING", MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_DDW_FLOW_ID, id);
        editor.commit();
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {

            String action = new String(Base64.decode(intent.getAction()));

            if (MyUtils.STRING_PROTAL_ALIVE.equals(action)
                    && TextUtils.equals(intent.getPackage(),
                    MyUtils.mHBMClientPkgName))

            {
                // 处理内容

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
            if (countTime > 0) {

                this.sendEmptyMessageDelayed(MyUtils.INT_HBMPROTAL_GET, 1000);

                SharePreferenceUtil.setLoginStatus(getApplicationContext(),
                        true);
            } else {

                SharePreferenceUtil.setLoginStatus(getApplicationContext(),
                        false);

                this.removeMessages(MyUtils.INT_HBMPROTAL_GET);
            }
            this.sendEmptyMessageDelayed(MyUtils.INT_HBMPROTAL_GET, 5 * 1000);
        }
    };

}
