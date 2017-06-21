package com.bonc.mobile.hbmclient.activity;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.activity.*;
import com.bonc.mobile.common.activity.BaseActivity;
import com.bonc.mobile.common.util.Base64;
import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.common.LoginConstant;
import com.bonc.mobile.hbmclient.common.Publicapp;
import com.bonc.mobile.hbmclient.service.BoncService;
import com.bonc.mobile.hbmclient.terminal.subactivity.BaseTerminalActivity;
import com.bonc.mobile.hbmclient.util.ExitApplication;
import com.bonc.mobile.hbmclient.util.HttpUtil;
import com.bonc.mobile.hbmclient.util.LogUtil;
import com.bonc.mobile.hbmclient.util.LogoutUtil;
import com.bonc.mobile.hbmclient.util.SMTools;
import com.bonc.mobile.hbmclient.util.TimeOutUtil;

/**
 * 底部菜单功能
 *
 * @author ZZZ 2013-2-20/5-8 写的好烦 ^_^
 */
public class MenuActivity extends Activity {
    protected static final int LOADING_DIALOG = 0;
    protected static final int LOADING_DIALOG1 = 1;
    public static final int MENU1 = 1;
    public static final int MENU2 = 2;
    public static final int MENU3 = 3;
    public static final int MENU4 = 4;
    public static final int MENU5 = 5;// 密码重置
    public static final int EXIT = 6;
    public static final int TIME_OUT_WHILE = 10 * 60 * 1000;
    public static boolean exit;
    public Intent intent = new Intent();
    public String imsi;
    public int simState;
    public String imei;
    public View dialog_view = null;
    public View resetp_view = null;
    public View announce_view = null;
    public TextView textViewImei;
    public TextView textViewImsi;
    public EditText textEditPhoneNum, text4Account;
    public static List<Activity> activityList = new ArrayList<Activity>();

    public static long mTimestamp = -1;
    private ScreenStateRecevier mRecevier;
    private ScreenOffRecevier mOffRecevier;
    private TimeOutDialog mTimeUpDia;
    // 消息推送 变量定义
    private ComponentName cm;
    private PackageManager pm;
    private String menu4Name = "关闭消息推送";

    private LoginRecevier mLoginRecevier;

    /**
     * 倒计时提示框的msg框
     */
    TextView alertMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cm = new ComponentName(Constant.PKG_NAME,
                "com.bonc.mobile.hbmclient.receiver.BoncReceiver");
        pm = getPackageManager();
        ExitApplication.getInstance().addActivity(this);
        if (mTimeUpDia == null) {
//            mTimeUpDia = new TimeOutDialog(this);
            mTimeUpDia = new TimeOutDialog(this);
        }
        if (mLoginRecevier == null) {
            mLoginRecevier = new LoginRecevier();
        }
        if (mOffRecevier == null) {
            mOffRecevier = new ScreenOffRecevier();
        }
        if (mRecevier == null) {
            mRecevier = new ScreenStateRecevier();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityList.remove(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (MyUtils.doInfilter(MenuActivity.this)) {

            if (!MyUtils.isBackground(this)) {
                MyUtils.startProtal(this);
                LogoutUtil.Logout(MenuActivity.this);
                // callLogin();
            }
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        IntentFilter inf = new IntentFilter();
        MenuActivity.mTimestamp = -1;
        inf.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mRecevier, inf);
        IntentFilter inf2 = new IntentFilter();
        inf2.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mOffRecevier, inf2);


        IntentFilter inf3 = new IntentFilter(Base64.encode(MyUtils.STRING_PROTAL_LOGIN.getBytes()));
        registerReceiver(mLoginRecevier, inf3);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        MenuActivity.mTimestamp = System.currentTimeMillis();
        unregisterReceiver(mRecevier);
        unregisterReceiver(mOffRecevier);
        unregisterReceiver(mLoginRecevier);
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        long ts = System.currentTimeMillis();
        if (MenuActivity.mTimestamp > 0
                && ts - MenuActivity.mTimestamp > MenuActivity.TIME_OUT_WHILE) {
            mTimeUpDia.setCancelable(false);
//            if ((mTimeUpDia != null) && (!mTimeUpDia.isShowing())) {
//                mTimeUpDia.cancel();
//                mTimeUpDia = new TimeOutDialog(this);
//                mTimeUpDia.setCancelable(false);
            mTimeUpDia.show();

            LogoutUtil.timeCounter(MenuActivity.this, mTimeUpDia);
//            }


            // timer.start();
        }
    }


//    public CountDownTimer timer = new CountDownTimer(10000, 1000) {
//
//        @Override
//        public void onTick(long millisUntilFinished) {
//
//            alertMsg = (TextView) mTimeUpDia.findViewById(android.R.id.message);
//            // Toast.makeText(MenuActivity.this,"AAA:"+(alertMsg==null),Toast.LENGTH_LONG).show();
//            LogUtils.logBySys("AAA:" + (alertMsg == null));
//            alertMsg.setText("长时间未操作,登陆已超时,请重新登陆! " + "(" + millisUntilFinished / 1000 + "s)后退出。");
//        }
//
//        @Override
//        public void onFinish() {
//            LogoutUtil.Logout(MenuActivity.this);
//        }
//    };

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        MenuActivity.mTimestamp = -1;
    }

    /**
     * MENU1:查看公告 ; MENU2：更改密码; MENU3：本机信息; MENU4:指标统计口径;MENU5:消息推送;
     */
    public void setMenu4Name() {
        String notifyFlag = Publicapp.getNotifyFlag();
        if (notifyFlag.equals(getResources().getString(R.string.notify_close)))
            menu4Name = getResources().getString(R.string.notify_open_chinese);
        else
            menu4Name = getResources().getString(R.string.notify_close_chinese);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        menu.clear();
        setMenu4Name();
        menu.add(0, MENU2, 1, R.string.menu3);
        menu.add(0, MENU4, 1, menu4Name);
        menu.add(0, EXIT, 1, R.string.exit);
        return super.onPrepareOptionsMenu(menu);
    }

    protected void setNotifyFlag(String pushFlag) {
        // 获得可编辑对象
        SharedPreferences p = getSharedPreferences("SETTING",
                MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = p.edit();
        editor.putString("notifyFlag", pushFlag);
        editor.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU2:
                // 查看本机信息
                getUser();
                LayoutInflater li = LayoutInflater.from(MenuActivity.this);
                dialog_view = li.inflate(R.layout.mobileinfo, null);
                textViewImei = (TextView) dialog_view.findViewById(R.id.imeiValue);
                textViewImsi = (TextView) dialog_view.findViewById(R.id.imsiValue);
                textViewImei.setText(imei);
                textViewImsi.setText(imsi);
                new AlertDialog.Builder(MenuActivity.this)
                        // .setPositiveButton("发送到服务器",sendToServer)
                        .setTitle("本机信息").setIcon(R.mipmap.img_btn_logout_2)
                        .setView(dialog_view).show();
                break;
            case MENU4:
                if (menu4Name.equals(getResources().getString(
                        R.string.notify_open_chinese)))
                    openNotity();
                else
                    closeNotify();
                break;
            case MENU5:
                // 重置密码
                showConfigAccount();

                break;
            case EXIT:
                // 退出
                openQiutDialog();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
    }

    private void showConfigAccount() {
        // TODO Auto-generated method stub
        getUser();
        LayoutInflater resetP = LayoutInflater.from(MenuActivity.this);
        resetp_view = resetP.inflate(R.layout.resetpass_input, null);
        textEditPhoneNum = (EditText) resetp_view.findViewById(R.id.rpm_input);
        text4Account = (EditText) resetp_view.findViewById(R.id.rpa_input);
        new AlertDialog.Builder(MenuActivity.this)
                .setPositiveButton("发送到服务器", sendAccountInfo)
                .setTitle("请输入账户信息").setIcon(R.mipmap.img_btn_logout_2)
                .setView(resetp_view).show();
    }

    // 发送消息到服务器的监听
    private OnClickListener sendMoblileInfo = new OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            Map<String, String> param = new HashMap<String, String>();
            SMTools smTools = new SMTools(MenuActivity.this);
            param.put("mobileno", smTools.getTel());
            param.put("imei", smTools.getIMEI());
            param.put("imsi", smTools.getIMSI());

            String resultString = HttpUtil.sendRequest(
                    ActionConstant.SEND_MOBILE_INFO, param);

            if (resultString == null || "".equals(resultString)) {
                Toast.makeText(MenuActivity.this, "发送失败!", Toast.LENGTH_LONG)
                        .show();
            } else {

                try {
                    JSONObject jsonObject = new JSONObject(resultString);
                    String updateflag = jsonObject.optString("updateflag");
                    if (LoginConstant.RESULT_NORMAL.equals(updateflag)) {
                        Toast.makeText(MenuActivity.this, "认证成功!",
                                Toast.LENGTH_LONG).show();

                    } else if (LoginConstant.LOGIN_FLAG_HASIMEINOIMSI
                            .equals(updateflag)) {

                        showConfigAlertM();

                    } else {
                        Toast.makeText(MenuActivity.this, "认证失败!",
                                Toast.LENGTH_LONG).show();

                    }

                } catch (Exception e) {
                    Toast.makeText(MenuActivity.this, "发送失败!",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

        }
    };

    // 重置密码前发送消息到服务器进行账号确认
    private OnClickListener sendAccountInfo = new OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            String phoneNumString = textEditPhoneNum.getText().toString();
            String accountString = text4Account.getText().toString();
            if (phoneNumString.length() != 11) {
                Toast.makeText(MenuActivity.this, "手机位数不对,请重填",
                        Toast.LENGTH_LONG).show();
                showConfigAccount();
                textEditPhoneNum.setText(phoneNumString);
                text4Account.setText(accountString);
            } else if (accountString.length() < 1) {
                Toast.makeText(MenuActivity.this, "请填写4A账号", Toast.LENGTH_LONG)
                        .show();
                showConfigAccount();
                textEditPhoneNum.setText(phoneNumString);
                text4Account.setText(accountString);
            } else {

                Map<String, String> param = new HashMap<String, String>();
                SMTools smTools = new SMTools(MenuActivity.this);
                param.put("mobileno", phoneNumString);
                param.put("imei", smTools.getIMEI());
                param.put("imsi", smTools.getIMSI());
                param.put("usercode", accountString);
                // 发送到服务器进行账号确认
                String resultString = HttpUtil.sendRequest(
                        ActionConstant.RESET_SELF, param);

                if (resultString == null || "".equals(resultString)) {
                    Toast.makeText(MenuActivity.this, "发送失败!",
                            Toast.LENGTH_LONG).show();
                } else {

                    try {
                        JSONObject jsonObject = new JSONObject(resultString);

                        String resetflag = jsonObject.optString("resetflag");
                        if (LoginConstant.RESULT_NORMAL.equals(resetflag)) {
                            Toast.makeText(MenuActivity.this, "重置成功",
                                    Toast.LENGTH_LONG).show();
                        } else if (LoginConstant.WRONG_TIMES_EXCEEDLIMITS
                                .equals(resetflag)) {
                            Toast.makeText(MenuActivity.this,
                                    "输入错误次数超过三次,请明天再试!", Toast.LENGTH_LONG)
                                    .show();
                            ExitApplication.getInstance().exit();
                        } else if (LoginConstant.LOGIN_FLAG_HASIMEINOIMSI
                                .equals(resetflag)) {
                            Toast.makeText(MenuActivity.this,
                                    "IMSI不正确!请先进行认证！", Toast.LENGTH_LONG)
                                    .show();
                            showConfigAlertM();

                        } else if (LoginConstant.LOGIN_WRONG_MOBILE
                                .equals(resetflag)) {
                            Toast.makeText(MenuActivity.this, "手机号码错误，请重新输入!",
                                    Toast.LENGTH_LONG).show();
                            showConfigAccount();

                        } else if (LoginConstant.LOGIN_WRONG_ACCOUNT
                                .equals(resetflag)) {

                            Toast.makeText(MenuActivity.this, "4A账号错误，请重新输入!",
                                    Toast.LENGTH_LONG).show();
                            showConfigAccount();

                        } else if (LoginConstant.LOGIN_WRONG_ALL
                                .equals(resetflag)) {

                            Toast.makeText(MenuActivity.this,
                                    "手机号码和4A账号都填写错误，请重新输入!", Toast.LENGTH_LONG)
                                    .show();
                            showConfigAccount();

                        } else {
                            Toast.makeText(MenuActivity.this, "认证失败!请联系管理员",
                                    Toast.LENGTH_LONG).show();

                        }

                    } catch (Exception e) {
                        Toast.makeText(MenuActivity.this, "账号确认失败!",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

            }
        }

    };

    public void closeNotify() {
        String notify_close = getResources().getString(R.string.notify_close);
        setNotifyFlag(notify_close);
    }

    public void openNotity() {
        pm.setComponentEnabledSetting(cm,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        String notify_open = getResources().getString(R.string.notify_open);
        setNotifyFlag(notify_open);
    }

    /**
     * 获取IMSI,IMEI
     */
    public void getUser() {
        SMTools sm = new SMTools(MenuActivity.this);
        imsi = sm.getIMSI();// 此号码与SIM唯一对应.
        simState = sm.getState();// 获得SM卡状态
        imei = sm.getIMEI();
    }

    /**
     * 退出 注意如果使用这个方法，需要在AndroidMainifest.xml 中.SynchHome 添加属性：
     * android:launchMode="singleTask"
     */
    public void openQiutDialog() {
        new AlertDialog.Builder(this).setTitle("经分系统").setMessage("是否退出经分系统？")
                .setPositiveButton("确定", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // stopService(new Intent(MenuActivity.this,
                        // BoncService.class));
                        // ExitApplication.getInstance().exit();
                        LogoutUtil.Logout(MenuActivity.this);
                    }
                })
                .setNegativeButton("取消", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private void showConfigAlertM() {
        // TODO Auto-generated method stub
        MenuActivity.this.getUser();
        LayoutInflater li = LayoutInflater.from(MenuActivity.this);
        dialog_view = li.inflate(R.layout.mobileinfo_input, null);
        textViewImei = (TextView) dialog_view.findViewById(R.id.imeiValue);
        textViewImsi = (TextView) dialog_view.findViewById(R.id.imsiValue);
        textEditPhoneNum = (EditText) dialog_view
                .findViewById(R.id.mobile_input);
        textViewImei.setText(imei);
        textViewImsi.setText(imsi);

        new AlertDialog.Builder(MenuActivity.this)

                .setPositiveButton("进行认证", sendMoblileInfoWithNumM)
                .setNegativeButton("退出", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        openQiutDialog();

                    }
                }).setTitle("未经认证的用户(更换手机需要进行重新认证)")
                .setIcon(R.mipmap.img_btn_logout_2).setView(dialog_view)
                .show();
    }

    /**
     * 带手机号进行认证
     */

    private OnClickListener sendToServer = new OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            SMTools smTools = new SMTools(MenuActivity.this);
            Map<String, String> param = new HashMap<String, String>();
            param.put("mobileno", smTools.getTel());
            param.put("imei", smTools.getIMEI());
            param.put("imsi", smTools.getIMSI());
            param.put("loginflag", LoginConstant.LOGIN_FLAG_PHONE);
            // 添加手机号

            String resultString = HttpUtil.sendRequest(
                    ActionConstant.SEND_MOBILE_INFO, param);

            if (resultString == null || "".equals(resultString)) {
                Toast.makeText(MenuActivity.this, "发送失败!", Toast.LENGTH_LONG)
                        .show();
            } else {

                try {
                    JSONObject jsonObject = new JSONObject(resultString);

                    String authflag = jsonObject.optString("authflag");
                    String message = jsonObject.optString("login_message");
                    if ("1".equals(authflag)) {
                        Toast.makeText(MenuActivity.this, message,
                                Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(MenuActivity.this, message,
                                Toast.LENGTH_LONG).show();
                        ExitApplication.getInstance().exit();
                    }

                } catch (Exception e) {
                    Toast.makeText(MenuActivity.this, "发送失败!",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }
    };

    public OnClickListener sendMoblileInfoWithNumM = new OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // 先判断手机号长度
            String phoneNumString = textEditPhoneNum.getText().toString();
            if (phoneNumString.length() != 11) {
                Toast.makeText(MenuActivity.this, "手机位数不对,请重填",
                        Toast.LENGTH_LONG).show();
                showConfigAlertM();
            } else {

                Map<String, String> param = new HashMap<String, String>();
                SMTools smTools = new SMTools(MenuActivity.this);
                param.put("mobileno", phoneNumString);
                param.put("imei", smTools.getIMEI());
                param.put("imsi", smTools.getIMSI());
                param.put("loginflag", LoginConstant.LOGIN_FLAG_HASIMEINOIMSI);
                // 添加手机号

                String resultString = HttpUtil.sendRequest(
                        ActionConstant.SEND_MOBILE_INFO, param);

                if (resultString == null || "".equals(resultString)) {
                    Toast.makeText(MenuActivity.this, "发送失败!",
                            Toast.LENGTH_LONG).show();
                } else {

                    try {
                        JSONObject jsonObject = new JSONObject(resultString);

                        String authflag = jsonObject.optString("authflag");

                        if ("1".equals(authflag)) {
                            Toast.makeText(MenuActivity.this, "认证成功！",
                                    Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(MenuActivity.this, "认证失败，请联系管理员！",
                                    Toast.LENGTH_LONG).show();
                            // stopService(new Intent(MenuActivity.this,
                            // BoncService.class));
                            // ExitApplication.getInstance().exit();
                            LogoutUtil.Logout(MenuActivity.this);
                        }

                    } catch (Exception e) {
                        Toast.makeText(MenuActivity.this, "发送失败!",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

            }
        }
    };

    private class TimeOutDialog extends AlertDialog {

        public TimeOutDialog(Context context) {
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
                    LogoutUtil.Logout(MenuActivity.this);
                    // callLogin();
                }
            });
        }

    }


    public void callLogin() {
        for (int i = 0; i < 5; i++) {

            // long c =long.class.
            // System.out.println("i:" + i);
            //
            // System.out.println(Binder.getCallingPid());
            //
            // System.out.println(android.os.Process.myPid());
            stopService(new Intent(MenuActivity.this, BoncService.class));
            // Intent intent = new Intent("com.bonc.mobile.hbmclient.Main");
            // intent.addCategory(Intent.CATEGORY_DEFAULT);
            // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // startActivity(intent);
            ExitApplication.getInstance().exit();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());

            finish();
            // getCurrentPkgName(MenuActivity.this);
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
            MenuActivity.mTimestamp = System.currentTimeMillis();
            // LogUtil.info("", "[]{}[] " + MenuActivity.mTimestamp);
            //s    String action = arg1.getAction();
//            if (TextUtils.equals(action, "login")) {
//                LogUtils.logBySys("AAAAAAAAAAAAAAAAAAAA");
//                Toast.makeText(arg0, "action:" + arg1.getAction(), Toast.LENGTH_LONG).show();
//                if (mTimeUpDia != null && mTimeUpDia.isShowing()) {
//                    mTimeUpDia.dismiss();
//                }
//            }
        }

    }

    private class LoginRecevier extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            MenuActivity.mTimestamp = System.currentTimeMillis();
            if (mTimeUpDia != null) {
                mTimeUpDia.dismiss();
                mTimeUpDia.hide();

            }

//            String action = arg1.getAction();
//            if (TextUtils.equals(action,"login")){
//                LogUtils.logBySys("AAAAAAAAAAAAAAAAAAAA");
//                Toast.makeText(arg0,"action:"+arg1.getAction(),Toast.LENGTH_LONG).show();
//                if(mTimeUpDia!=null&&mTimeUpDia.isShowing()){
//                    mTimeUpDia.dismiss();
//                }
//            }
        }

    }
}
