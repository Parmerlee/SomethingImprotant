package com.bonc.mobile.portal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.anhuimobile.ac.R;
import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.Constant;
import com.bonc.mobile.common.User;
import com.bonc.mobile.common.EventBus.DefaultEvent;
import com.bonc.mobile.common.activity.BaseActivity;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.util.Base64;
import com.bonc.mobile.common.util.DES;
import com.bonc.mobile.common.util.StringUtil;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.common.view.HomeWatcher;
import com.bonc.mobile.common.view.HomeWatcher.OnHomePressedListener;

//import de.greenrobot.event.EventBus;
import org.greenrobot.eventbus.EventBus;
/**
 * @author sunwei
 */
public class RegisterActivity extends BaseActivity {
    EditText name, name_4a, pass_4a, vcode;
    String channel = null;

    /**
     * *
     * 是否为二次认证标志位
     */
    boolean mIsSecond = false;
    String vcode_sessionId;

    Button get_vcode;
    long countTime;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    get_vcode.setText(countTime / 1000 + "秒后重新获取");
                    countTime = countTime - 1000;
                    if (countTime > 0)
                        sendEmptyMessageDelayed(0, 1000);
                    else {
                        get_vcode.setClickable(true);
                        get_vcode.setText(R.string.get_vcode);
                    }
                    break;

                case 1:
                    if (needAlarm
                            && (!getRunningAppProcesses(RegisterActivity.this))) {
                        Toast.makeText(getApplicationContext(),
                                "您的认证界面已被覆盖，请确认您的手机环境是否安全", Toast.LENGTH_SHORT)
                                .show();
                        removeMessages(1);
                        needToast = true;
                        break;
                    }
                    if (needAlarm
                            && (getRunningAppProcesses(RegisterActivity.this)))
                        sendEmptyMessageDelayed(1, 1000);
                    break;
                default:
                    break;
            }

        }
    };

    // * 移动：134[0-8],135,136,137,138,139,150,151,157,158,159,182,187,188
    // * 联通：130,131,132,152,155,156,185,186
    // * 电信：133,1349,153,180,189 26

    String[] str_phone = {"130", "131", "132", "133", "134", "135", "136",
            "137", "138", "139", "150", "151", "152", "153", "155", "156",
            "157", "158", "159", "180", "182", "185", "186", "187", "188",
            "189"};

    private boolean checkPhone() {

        List<String> list = new ArrayList<String>();
        for (String str : str_phone) {
            list.add(str);
        }
        String phone = name.getText().toString();
        boolean flag = false;
        String str = null;
        String msg = "请输入正确的11位手机号";
        if (phone.length() != 11) {
            flag = false;
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
                    .show();
            return flag;

        }
        try {
            str = (String) phone.subSequence(0, 3);

        } catch (Exception e) {
            flag = false;
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
                    .show();
            return flag;

        }
        int i = list.indexOf(str);
        if (i != -1) {
            flag = true;
        } else {
            flag = false;
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
                    .show();
            return flag;

        }

        return flag;

    }

    private HomeWatcher mHomeWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        initView();

        TextView tv = (TextView) this.findViewById(R.id.tv_version);
        tv.setText(getVersion());

        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                needAlarm = false;
                needToast = false;
            }

            @Override
            public void onHomeLongPressed() {
                needAlarm = false;
                needToast = false;
            }
        });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (needToast) {
            Toast.makeText(getApplicationContext(), "欢迎回到认证界面，请确保你的手机环境是安全的",
                    Toast.LENGTH_LONG).show();

        }
        handler.removeMessages(1);
        handler.sendEmptyMessage(1);
        needAlarm = true;

        if (mHomeWatcher != null) {
            mHomeWatcher.startWatch();
        }
    }

    protected void initView() {
        setWatermarkImage();
        TextViewUtils.setText(this, R.id.text, getIntent()
                .hasExtra("login_msg") ? getIntent()
                .getStringExtra("login_msg") : "4A认证");
        name = (EditText) findViewById(R.id.name);
        name_4a = (EditText) findViewById(R.id.name_4a);
        pass_4a = (EditText) findViewById(R.id.pass_4a);
        vcode = (EditText) findViewById(R.id.vcode);
        get_vcode = (Button) findViewById(R.id.get_vcode);
        channel = getIntent().getStringExtra("channel");

        mIsSecond = (getIntent().hasExtra("login_msg"))
                && (getIntent().hasExtra("channel"));


//		View view = (LinearLayout) pass_4a.getParent().getParent();
//
//		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
//		params.height = (int)(UIUtil.getDisplayHeight(this)/3.2);
//		view.setLayoutParams(params);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("resultCode:" + resultCode);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            this.handler.removeMessages(1);
            finish();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                if (name_4a.length() == 0 || pass_4a.length() == 0
                        || vcode.length() == 0) {
                    showToast("帐号,密码,验证码不能为空");
                    return;
                }
                if (!checkPhone()) {
                    return;
                }

                //更改逻辑，不再检查用户状态  直接登录 2017年1月5日09:46:41
//                checkUser();
                doReg();
                break;
            case R.id.cancel:
                finish();
                break;
            case R.id.send_info:
                Intent intent = new Intent(this, SendDevInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.get_vcode:
                if (name_4a.length() == 0 || pass_4a.length() == 0) {
                    showToast("帐号,密码不能为空");
                    return;
                }
                if (!checkPhone()) {
                    return;
                }
                getVcode();
                countTime = 30000;
                if (!AppConstant.SEC_ENH) {
                    countTime = 5000;
                }
                get_vcode.setClickable(false);
                handler.sendEmptyMessage(0);
                break;
        }
    }

    private void checkUser() {
        Map<String, String> param = new LinkedHashMap<String, String>();
        param.put("user4ACode", name_4a.getText().toString());
        param.put("os", "android");
        param.put("mobile", name.getText().toString());
        param.put("nouser", "nouser");

        new checkUserTask(this).execute("/sysUser/getUserState", param);
    }

    void getVcode() {
        Map<String, String> param = new LinkedHashMap<String, String>();
        param.put("user4ACode", name_4a.getText().toString());
        param.put("user4APwd", pass_4a.getText().toString());
        param.put("channel", StringUtil.nullToString(channel));

        DES.encrypt(param);

        // phone字段不能加密
        // phone字段不能加密
        // phone字段不能加密 2016年6月30日09:46:43
        param.put("phone", name.getText().toString());
        param.put("nouser", "nouser");

        new GetVcodeTask(this).execute("/hbmLogin/getDynamicPwd", param);

    }

    class GetVcodeTask extends HttpRequestTask {
        public GetVcodeTask(Context context) {
            super(context);
        }

        @Override
        protected void handleResult(JSONObject result) {
            if (!AppConstant.SEC_ENH)
                System.out.println(result.toString());
            vcode_sessionId = result.optString("sessionId");
            if (!TextUtils.isEmpty(result.optString("msg"))) {
                Toast.makeText(getApplicationContext(),
                        result.optString("msg"), Toast.LENGTH_LONG).show();
            }
            if (result.optBoolean("flag")) {
                showToast("获取动态码成功");
            } else {
                sendEvent();
            }
            // 测试环境下自动输入验证码
            if (!AppConstant.SEC_ENH) {
                vcode.setText(result.optString("smscode"));
            }
        }
    }

    boolean isUserVaild = false;

    // 检查用户信息task -1为正常状态
    class checkUserTask extends HttpRequestTask {
        public checkUserTask(Context context) {
            super(context);
        }

        @Override
        protected void handleResult(JSONObject result) {

            if (result == null) {
                doReg();
                return;
            }
            try {

                if (result.optBoolean("flag")) {
                    isUserVaild = true;
                    doReg();
                } else {
                    showToast(result.optString("msg"));

                }
            } catch (Exception e) {
                sendEvent();
                // EventBus.getDefault().post(new DefaultEvent("log"));
                showToast("请求失败");
            }
        }
    }

    boolean needAlarm = true;
    boolean needToast = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME)
                && event.getRepeatCount() == 0) {
            needAlarm = false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        System.out.println("onDestory");
        super.onDestroy();
        handler.removeMessages(1);
        handler = null;
    }

    public static boolean getRunningAppProcesses(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && appProcess.processName.equals(context.getPackageName())) {
                return true;
            }
        }

        return false;
    }

    void doReg() {
        String action;
        // 前者是二次认证，后者是第一次认证
        // action = channel != null ? "/hbmLogin/4aLoginWithKey"
        // : "/hbmLogin/4aLogin";

        action = mIsSecond ? "/hbmLogin/4aLoginWithKey" : "/hbmLogin/4aLogin";

        Map<String, String> param = new LinkedHashMap<String, String>();
        param.put("user4ACode", name_4a.getText().toString());
        param.put("user4APwd", pass_4a.getText().toString());
        param.put("phone", name.getText().toString());
        param.put("smsCode", vcode.getText().toString());
        param.put("sessionId", vcode_sessionId);
        param.put("channel", StringUtil.nullToString(channel));
        param.put("os", "android");
        param.put(AppConstant.KEY_IMEI, User.getSingleInstance().imei);
        param.put(AppConstant.KEY_IMSI, User.getSingleInstance().imsi);

        param.put("nouser", "nouser");

        if (mIsSecond)
            param.put("mobileKey", User.getInstance().mobileKey);

        DES.encrypt(param);

        if (!mIsSecond) {
            // 下次上线时加上 2016年5月11日10:58:02

            DES.encryptByBase64(param);
            // param.put("os", Base64.encode(param.get("os").getBytes()));
            // param.put("user4ACode",
            // Base64.encode(param.get("user4ACode").getBytes()));
            // param.put("user4APwd",
            // Base64.encode(param.get("user4APwd").getBytes()));
            // param.put("phone", Base64.encode(param.get("phone").getBytes()));
            // param.put("smsCode",
            // Base64.encode(param.get("smsCode").getBytes()));
            //
            // param.put("sessionId", TextUtils.isEmpty(vcode_sessionId) ? ""
            // : Base64.encode(param.get("sessionId").getBytes()));
            // param.put("channel", TextUtils.isEmpty(param.get("channel")) ? ""
            // : Base64.encode(param.get("channel").getBytes()));
            //
            // param.put(AppConstant.KEY_IMEI,
            // Base64.encode(param.get(AppConstant.KEY_IMEI).getBytes()));
            // param.put(AppConstant.KEY_IMSI,
            // Base64.encode(param.get(AppConstant.KEY_IMSI).getBytes()));
        }

        // save user info to cache

        // EventBus.getDefault().post(
        // new DefaultEvent("logWithUser:" + user.toString()));
        // ---end

        action = "post" + action;
        new LoadDataTask(this).execute(action, param);
    }

    class LoadDataTask extends HttpRequestTask {
        public LoadDataTask(Context context) {
            super(context);
        }

        @Override
        protected void handleResult(JSONObject result) {
            boolean flag = result.optBoolean("flag");
            if (flag) {
                // if (channel != null) {
                // setResult(RESULT_OK);
                // finish();
                // return;
                // }
                if (mIsSecond) {
                    setResult(RESULT_OK);
                    handler.removeMessages(1);
                    finish();
                    return;
                }
                String mobileKey = result.optString("mobileKey");

                User.getInstance().clearUserInfo(context);

                User.getInstance().mobileKey = mobileKey;
                // 存入4A账号
                User.getInstance().userCode = name_4a.getText().toString();
                // 手机号
                User.getInstance().phone = name.getText().toString();

                User.getInstance().save(context);

                Intent intent;
                /****
                 * 之前逻辑： rereg 重新认证与否标志（true 是），配合返回值中的hasPwd字段
                 * 判断是否需要重新发送手势码到后台。 若不为重新认证且已经有密码，那么就直接跳过发送手势码阶段，直接返回到手势码登陆界面
                 *
                 * 新逻辑： 为了与ios保持一致，现在去掉判断是否需要重新发送手势码阶段。即总是要重新发送手势码到后台。
                 * 2016年3月2日17:39:09
                 */
                intent = new Intent(RegisterActivity.this,
                        PasswordActivity.class);
                intent.putExtra("set", true);
                startActivityForResult(intent, 100);
            } else {
                sendEvent();
                // EventBus.getDefault().post(new DefaultEvent("log"));
            }
            showToast(result.optString("msg"));
        }
    }

    void sendEvent() {
        Map<String, String> map = new HashMap<String, String>();
        DefaultEvent event = new DefaultEvent();
        event.setMsg("log");

        event.buildUser(name_4a.getText().toString(), name.getText().toString());
        EventBus.getDefault().post(event);
    }

    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return "版本号：" + version;
        } catch (Exception e) {
            e.printStackTrace();
            return "获取版本号失败";
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mHomeWatcher.stopWatch();
    }
}
