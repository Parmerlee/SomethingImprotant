package com.bonc.mobile.portal;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.anhuimobile.ac.R;
import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.Constant;
import com.bonc.mobile.common.User;
import com.bonc.mobile.common.EventBus.DefaultEvent;
import com.bonc.mobile.common.activity.PatternViewActivity;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.net.UpgradeManager;
import com.bonc.mobile.common.util.Base64;
import com.bonc.mobile.common.util.DES;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.FileUtils;
import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.common.util.MD5;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.PhoneUtil;
import com.bonc.mobile.common.util.SMTools;
import com.bonc.mobile.common.util.SharePreferenceUtil;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.common.view.PatternView;
import com.bonc.mobile.common.view.PatternView.Cell;
import com.bonc.mobile.common.view.PatternView.OnPatternListener;
import com.bonc.mobile.portal.db.SQLHelper;

//import de.greenrobot.event.EventBus;
import org.greenrobot.eventbus.EventBus;

/**
 * @author sunwei
 */
public class LoginActivity extends PatternViewActivity {

    private boolean isSimChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // checkImsi();
        initUser();
        setContentView(R.layout.activity_login);
        initView();
        checkDb();
        if (MD5.checkSign(getApplicationContext()))
            new MyUpgradeManager(this).checkUpgrade();
        else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "请从正确的途径下载安装包",
                            Toast.LENGTH_LONG).show();
                    LoginActivity.this.finish();
                }

            }, 1000);

        }

    }

    void initUser() {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        DataUtil.saveSettingE(sp, AppConstant.KEY_SERVER_PATH,
                Constant.PORTAL_PATH);
        SMTools smTools = new SMTools(this);
        User user = User.getInstance();
        user.imei = sp.getString(AppConstant.KEY_IMEI, smTools.getIMEI());
        user.imsi = sp.getString(AppConstant.KEY_IMSI, smTools.getIMSI());
        user.mobileKey = sp.getString(AppConstant.KEY_MOBILE_KEY, null);
        user.phone = PhoneUtil.getPhoneNum(this);
        //如果能从手机卡上读，就拿卡上的；没有再从缓存里获取。
        if (TextUtils.isEmpty(user.phone))
            user.phone = DES.decrypt(sp.getString(AppConstant.KEY_PHONE, null));
    }

    // 下次上线时再上 2016年11月7日10:20:29 各种操作提示等符合要求

    /**
     * 换机提示
     */
    private void checkImsi() {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);

        SMTools smTools = new SMTools(this);
        String imsi = smTools.getIMSI();
        String imsi2 = sp.getString(AppConstant.KEY_IMSI, "");

        LogUtils.debug("AAAA", "equal:" + TextUtils.equals(imsi, imsi2)
                + ";内存:" + imsi2 + ",手机卡:" + imsi);
        //
        // if (!AppConstant.SEC_ENH)
        // showToast("equal:" + TextUtils.equals(imsi, imsi2) + ";user:"
        // + imsi2 + ",phone:" + imsi + smTools.getTel());

        if (!TextUtils.isEmpty(imsi2)) {
            if (!TextUtils.equals(imsi, imsi2)) {
                Toast.makeText(getApplicationContext(), "检测到你的手机卡发生变更，请重新认证",
                        Toast.LENGTH_LONG).show();

                isSimChange = true;
            } else
                isSimChange = false;
        } else
            isSimChange = false;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case MyUtils.INT_MPROTAL_SEND:
                    Toast.makeText(getApplicationContext(), "send", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyUtils.STRING_PROTAL_ALIVE);
                    intent.setPackage(MyUtils.mHBMClientPkgName);
                    getApplicationContext().sendBroadcast(intent);
                    this.sendEmptyMessageDelayed(MyUtils.INT_MPROTAL_SEND, 5000);
                    break;

                default:
                    break;
            }
        }
    };

    boolean isUserVaild = false;

    private void checkUserStatus() {
        Map<String, String> param = new LinkedHashMap<String, String>();
        param.put("user4ACode", User.getInstance().userCode);
        param.put("os", "android");
        param.put("mobile", User.getInstance().phone);
        param.put("nouser", "nouser");

        new checkUserTask(this).execute("/sysUser/getUserState", param);
    }

    // 检查用户信息task -1为正常状态
    class checkUserTask extends HttpRequestTask {
        public checkUserTask(Context context) {
            super(context);
        }

        @Override
        protected void handleResult(JSONObject result) {

            if (result == null) {

                if (password != null) {
                    doLogin(password);
                }

                isUserVaild = true;
                return;
            }
            try {

                if (result.optBoolean("flag")) {
                    if (password != null) {
                        doLogin(password);
                    }
                } else {
                    showToast(result.optString("msg"));

                }
            } catch (Exception e) {
                Map<String, String> map = new HashMap<String, String>();
                DefaultEvent event = new DefaultEvent();
                event.setMsg("log");

                event.buildUser(User.getInstance().userCode, User.getInstance().phone);
                EventBus.getDefault().post(event);
                // EventBus.getDefault().post(new DefaultEvent("log"));
                showToast("请求失败");
            }
        }
    }

    class MyUpgradeManager extends UpgradeManager {
        public MyUpgradeManager(Context context) {
            super(context);
            basePath = Constant.PORTAL_PATH;
            action = "/sysResource/getPortalVersion";
        }

        @Override
        protected void noUpgrade() {
            testMode();
        }

        @Override
        protected void doCancel() {
            super.doCancel();
            if (json.optInt("flag") == 0) {
                noUpgrade();
            } else {
                finish();
            }
        }

        @Override
        protected void afterUpgrade() {
            finish();
        }

    }

    public void onClick(View v) {
        if (v.getId() == R.id.rereg) {
            launchReg(true);
        }
    }

    String password = null;

    void initView() {
        patternView = (PatternView) findViewById(R.id.lockPattern);
        patternView.setOnPatternListener(new OnPatternListener() {

            @Override
            public void onPatternStart() {
            }

            @Override
            public void onPatternDetected(List<Cell> pattern) {
                patternView.disableInput();
                if (isSimChange) {
                    showToast("检测到你的手机卡发生变更，请重新认证");
                } else {
                    password = getPassword(pattern);
                    //在手势码登录之前，先去检测用户状态 2017年1月5日11:54:03
                    checkUserStatus();
//                    doLogin(getPassword(pattern));
                }
                resetPattern();
            }

            @Override
            public void onPatternCleared() {
            }

            @Override
            public void onPatternCellAdded(List<Cell> pattern) {
            }
        });
        hint = (TextView) findViewById(R.id.hint);
        hint.setText(R.string.login_hint);

        LinearLayout layout = (LinearLayout) patternView.getParent();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layout.getLayoutParams();
//        params.height = UIUtil.getDisplayHeight(this)/3;
//        params.width = UIUtil.getDisplayWidth(this)*2/3;
//        params.setMargins(0, UIUtil.fromDPtoPX(this,UIUtil.getDisplayHeight(this) / 4), 0, 0);
        params.setMargins(0, UIUtil.getDisplayHeight(this) / 3, 0, 0);
        layout.setLayoutParams(params);
        LogUtils.logBySys("height:" + UIUtil.getDisplayHeight(this));

    }

    void testMode() {
        // 获取数据加密与否标志 mode = "production" 为加密
        Map<String, String> param = new LinkedHashMap<String, String>();
        new TestModeTask(this).execute("/sysResource/appMode", param);
    }

    private SQLHelper sqlHelper = new SQLHelper();

    class TestModeTask extends HttpRequestTask {
        public TestModeTask(Context context) {
            super(context);
        }

        @Override
        protected void handleResult(JSONObject result) {
            User.getInstance().mode = result.optString("mode");

            // if (!AppConstant.SEC_ENH) {
            // User.getInstance().mobileKey =
            // "101e73d0b8e04d78a7acc706d1ddbcb9";
            // }
            // checkUser();
            // checkDb();
        }

    }

    void checkDb() {
        if (!sqlHelper.isDBFileExists()) {
            sqlHelper.createEmptyDbFile();
            // sqlHelper.deleteDbIfExists();
            sqlHelper.useEmptyDB();
        }

        startService(new Intent(LoginActivity.this, ProtalService.class));
    }

    void checkUser() {
        if (User.getInstance().mobileKey == null) {
            loginImei();
        } else {
            SMTools smTools = new SMTools(this);
            User user = User.getInstance();
            if (user.imei != null && !user.imei.equals(smTools.getIMEI())
                    || user.imsi != null
                    && !user.imsi.equals(smTools.getIMSI())) {
                launchReg(false);
            }
        }
    }

    void doLogin(String password) {
        String p = new MD5().getMD5ofStr(password);
        User.getInstance().password = p;
        Map<String, String> param = new LinkedHashMap<String, String>();
        param.put("pwd", p);
        // 新字段sessionId
        // param.put("sessionId", User.getInstance().sessionId);
        new LoadDataTask(this).execute("/hbmLogin", param);
    }

    void initWatermark() {
        Bitmap bitmap = FileUtils.createWatermark(this,
                "4A:" + User.getInstance().userCode);
        FileUtils.writeBitmap(this, "watermark", bitmap);
    }

    class LoadDataTask extends HttpRequestTask {
        public LoadDataTask(Context context) {
            super(context);
        }

        @SuppressWarnings("static-access")
        @Override
        protected void handleResult(JSONObject result) {
            int flag = result.optInt("login_flag");
            // showToast(result.optString("msg"));
            if (flag != -1)
                EventBus.getDefault().post(new DefaultEvent("log"));
            Toast.makeText(getApplicationContext(), result.optString("msg"), Toast.LENGTH_LONG)
                    .show();
            if (flag == -1) {
                timer.start();

                SharePreferenceUtil.setLoginStatus(getApplicationContext(),
                        true);

                User.getInstance().userCode = result.optString("userCode");
                User.getInstance().save(context);
                initWatermark();
                Intent intent = new Intent(LoginActivity.this,
                        PortalActivity.class);
                startActivity(intent);
                finish();
            } else if (flag == 2) {
                launchReg(false);
            } else if (flag == 3) {
                Intent intent = new Intent(LoginActivity.this,
                        PasswordActivity.class);
                intent.putExtra("set", true);
                startActivity(intent);
            }
        }
    }

    private CountDownTimer timer = new CountDownTimer(1000 * 10, 1 * 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
//            Toast.makeText(LoginActivity.this,"CCCCC",Toast.LENGTH_SHORT).show();
            getApplicationContext().sendBroadcast(new Intent(Base64.encode(MyUtils.STRING_PROTAL_LOGIN.getBytes())));
        }

        @Override
        public void onFinish() {

        }
    };

    void loginImei() {
        Map<String, String> param = new LinkedHashMap<String, String>();
        new LoginImeiTask(this).execute("/hbmLogin/4aLoginWithImei", param);
    }

    class LoginImeiTask extends HttpRequestTask {
        public LoginImeiTask(Context context) {
            super(context);
        }

        @Override
        protected void handleResult(JSONObject result) {
            if (result.optBoolean("flag")) {
                String mobileKey = result.optString("mobileKey");
                User.getInstance().mobileKey = mobileKey;
                User.getInstance().save(context);
            } else {
                launchReg(false);
            }
        }
    }

    void launchReg(boolean rereg) {
        Intent i = new Intent(this, RegisterActivity.class);
        i.putExtra("rereg", rereg);
        startActivity(i);
        finish();
    }

}
