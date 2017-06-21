package com.bonc.mobile.portal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.Constant;
import com.bonc.mobile.common.R;
import com.bonc.mobile.common.User;
import com.bonc.mobile.common.EventBus.DefaultEvent;
import com.bonc.mobile.common.net.HttpUtil;
import com.bonc.mobile.common.service.BaseService;
import com.bonc.mobile.common.util.DES;
import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.portal.db.BussinessDao;
import com.bonc.mobile.portal.db.SQLHelper;

import org.greenrobot.eventbus.ThreadMode;


public class ProtalService extends BaseService {

    BussinessDao dao = new BussinessDao();
    Context context;
    final String ERROE_LOG = "数据格式转化异常";
    int id = -1;
    // 发送失败的条数
    int index = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        stopLoop();
        // checkDB();
        EventBus.getDefault().register(this);
        context = this;
        handler.removeMessages(1);
        handler.sendEmptyMessage(1);

    }


    //    public void onUserEvent(DefaultEvent event) {
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DefaultEvent event) {
        LogUtils.debug(getClass(), "捕获到的异常:" + event.getMsg());
        Map map = event.getmMap();
        User user = new User();
        if (map != null && map.containsKey(DefaultEvent.TAG_USERCODE)) {
            // System.out.println("AAAAAAAA");
            user.phone = (String) map.get(DefaultEvent.TAG_USERPHONE);
            user.userCode = (String) map.get(DefaultEvent.TAG_USERCODE);
        }

        // 过滤上传日志本身的产生的日志 getComErrorLog接口
        try {
            Map<String, String> map2 = HttpUtil.getMap();
            String url = null;
            for (Map.Entry<String, String> m : map2.entrySet()) {
                url = m.getKey();
            }
            String[] str = url.split("\\?");

            // for (String s : str) {
            // System.out.println("AAA:"+s);
            // }
            str = str[0].split("\\/");
            // for (String s : str) {
            // System.out.println("BBB:"+s);
            // }

            String action = str[str.length - 1];
            // System.out.println("action:"+action);
            LogUtils.logBySys("action:" + action);
            if (TextUtils.equals(action, "getComErrorLog")) {
                return;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (filter(event.getMsg())) {

            dao.addInfo(context, HttpUtil.getMap(),
                    user.userCode == null ? User.getInstance() : user);
        } else {

            dao.addInfo(context, HttpUtil.getMap(),
                    user.userCode == null ? User.getInstance() : user,
                    event.getMsg());
        }
    }

    private void checkDB() {
        // TODO Auto-generated method stub
        List<Map<String, String>> list = dao.queryForAll();
        LogUtils.debug(getClass(), list);

    }

    boolean filter(String msg) {
        boolean b = false;
        if (TextUtils.equals(ERROE_LOG, msg)) {
            b = true;
        }
        if (TextUtils.equals("log", msg)) {
            b = true;
        }
        if (TextUtils.equals(msg, context.getString(R.string.network_error))) {
            b = true;
        }
        return b;
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
//            LogUtils.debug(getClass(), msg);
            switch (msg.what) {
                case 1:
                    // Toast.makeText(getApplicationContext(), "AAAAA", 1).show();
                    sendMessager();
//                    LogUtils.debug("AAAA", "id:" + id);
                    List<Map<String, String>> list = dao.queryForAll();

                    if (list.isEmpty()) {
                        return;
                    }

                    if (list.size() > 99) {
                        new SQLHelper().useEmptyDB();
                        return;
                    }
                    // 发送不成功的就跳过，再次发送下一条
                    if (id != -1) {
                        index++;
                    }
                    // 如果index==size 表示现存的所有数据都已经发送过一次，那么就重新从0条开始发送
                    if (index == list.size()) {
                        index = 0;
                    }
                    Map<String, String> map = list.get(Math.min(index,
                            list.size() - 1));
                    id = Integer.valueOf(map.get("id"));

                    // LogUtils.debug("aaaa", map.toString());
                    sendLogInfo(map);

                    break;

                default:
                    break;
            }
        }

    };

    /**
     * send loginfo to service
     *
     * @param map
     */
    private void sendLogInfo(Map<String, String> map) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("ComInfo", map.get("ComInfo"));
        params.put("URLInfo", map.get("URLInfo"));
        params.put("Msg", map.get("Msg"));
        params.put("Time", map.get("Time"));
        params.put("User4ACode", map.get("User4ACode"));
        params.put("Phone", map.get("Phone"));

//        LogUtils.debug("AAAA", "map:" + map);
        // if (AppConstant.SEC_ENH)
        DES.encryptByBase64(params);

        // params.put(KEY_FLOW_ID, pref.getString(KEY_FLOW_ID, "0"));
        if (!AppConstant.SEC_ENH) {
            return;
        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                String msg;

                try {
                    // msg = HttpUtil.sendRequest2("http://192.168.1.114:9080",
                    // "/hbmLogin/getComErrorLog", params);
                    msg = HttpUtil.sendRequest2(Constant.BASE_PATH,
                            "/hbmLogin/getComErrorLog", params);

                    if (TextUtils.isEmpty(msg))
                        return;
                    // if (AppConstant.SEC_ENH)
                    msg = DES.decrypt(msg.replace("\"", ""));

                    LogUtils.logBySys(msg);
                    JSONObject obj = new JSONObject(msg);
                    if (obj.getBoolean("flag")) {
                        dao.deteleInfo(id);
                        id = -1;

                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                sendMessager();
            }
        }).start();

    }

    void sendMessager() {
        handler.removeMessages(1);
        handler.sendEmptyMessageDelayed(1, 10 * 1000);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (handler != null) {
            handler.sendEmptyMessage(1);
            EventBus.getDefault().unregister(this);
        }
    }
}
