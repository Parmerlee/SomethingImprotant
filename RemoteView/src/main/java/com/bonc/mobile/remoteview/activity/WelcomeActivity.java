package com.bonc.mobile.remoteview.activity;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.User;
import com.bonc.mobile.common.activity.BaseActivity;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.net.UpgradeManager;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.FileUtils;
import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.common.util.MD5;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.SharePreferenceUtil;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.common.ConfigLoader;
import com.bonc.mobile.remoteview.common.Constant;
import com.bonc.mobile.remoteview.common.RemoteUtil;

public class WelcomeActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if (!MD5.checkSign(getApplicationContext())) {

            Toast.makeText(getApplicationContext(), "请从正确的渠道下载安装包",
                    Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    WelcomeActivity.this.finish();
                }
            }, 1000);

        } else {

            if (getIntent().hasExtra("launch_from_srv")) {
                User.getInstance().load(this);
            } else {
                initUserInfo();
            }
            new MyUpgradeManager(this).checkUpgrade();
            SharePreferenceUtil.setLoginStatus(this, getIntent()
                    .getBooleanExtra(AppConstant.KEY_LOGIN, false));
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        LogUtils.logBySys("filter:" + MyUtils.doInfilter(this) + "；back:" + MyUtils.isBackground(this));
        if (MyUtils.doInfilter(this)) {

            if (!MyUtils.isBackground(this)) {
                MyUtils.startProtal(this);
                RemoteUtil.getInstance().callLogin();
            }
        }
    }

    class MyUpgradeManager extends UpgradeManager {
        public MyUpgradeManager(Context context) {
            super(context);
            basePath = Constant.RV_PATH;
            action = "/sys/getVersion";
        }

        @Override
        protected void noUpgrade() {
            loadConfig();
        }

        @Override
        protected void afterUpgrade() {
            finish();
        }

        @Override
        protected void cancelUpgrade() {
            finish();
        }
    }

    void initUserInfo() {
        User u = User.getInstance();
        u.load(getIntent());
        if (u.userCode == null)
            u.loadTest();
        u.save(this);
        initWatermark();
        DataUtil.saveSettingE(
                PreferenceManager.getDefaultSharedPreferences(this),
                AppConstant.KEY_SERVER_PATH, Constant.RV_PATH);
    }

    void initWatermark() {
        Bitmap bitmap = FileUtils.createWatermark(this,
                "4A:" + User.getInstance().userCode);
        FileUtils.writeBitmap(this, "watermark", bitmap);
    }

    void loadConfig() {
        Map<String, String> param = new LinkedHashMap<String, String>();
        param.put("appType", Constant.APP_TYPE);
        new LoadConfigTask(this).execute("/index", param);
    }

    class LoadConfigTask extends HttpRequestTask {
        public LoadConfigTask(Context context) {
            super(context);
        }

        @Override
        protected void handleResult(JSONObject json) {
            if (json.optBoolean("flag")) {
                ConfigLoader loader = ConfigLoader.getInstance(context);
                loader.loadMenu(json.optJSONArray("data").toString());
                loader.loadChannel(json.optJSONArray("custMenu").toString());
                startMain();
            } else {
                UIUtil.showAlertDialog(context, json.optString("msg"),
                        new FinishDialog());
            }
        }
    }

    void loadAnnounce() {
        Map<String, String> param = new LinkedHashMap<String, String>();
        LoadAnnounceTask t = new LoadAnnounceTask(this);
        t.setRetAsArray(true);
        t.execute("/sys/getNotice", param);
    }

    class LoadAnnounceTask extends HttpRequestTask {
        public LoadAnnounceTask(Context context) {
            super(context);
        }

        @Override
        protected void handleResult(JSONArray json) {
            ConfigLoader.getInstance(context).loadAnnounce(json.toString());
        }
    }

    class FinishDialog implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();
        }
    }

    void startMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
