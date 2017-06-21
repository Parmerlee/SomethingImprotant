package com.bonc.mobile.hbmclient.activity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.User;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.net.SimpleDownloadManager;
import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.PackageUtil;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.service.BoncService;
import com.bonc.mobile.hbmclient.util.SMTools;
import com.bonc.mobile.hbmclient.util.WatermarkImage;

import common.share.lwg.util.asyntask.UpgradeManager;
import common.share.lwg.util.asyntask.WelComeLoadDateTask;

public class Welcome extends Activity {

    /**
     * 用于标示屏幕高的变量 *
     */
    public static int screen_Height;

    /**
     * 用于标示屏幕宽的变量 *
     */
    public static int screen_Width;

    private Handler handler = new Handler();

    private void setting() {
        SharedPreferences dataSetting = getSharedPreferences("SETTING",
                MODE_WORLD_WRITEABLE);
        boolean StatusBar = dataSetting.getBoolean("StatusBar", true);
        if (StatusBar) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setting();
        setContentView(R.layout.welcome);
        // 设置水印背景
        createWatermarkDrawable();

        TextView hint_welcome = (TextView) findViewById(R.id.hint_welcome);
        hint_welcome.setText(R.string.loading_data);

        // ***取当前手机屏幕的宽高
        WindowManager mWindowManager = getWindowManager();
        Display mDisplay = mWindowManager.getDefaultDisplay();
        screen_Height = mDisplay.getHeight();
        screen_Width = mDisplay.getWidth();

        checkVersion();

        // 通知后台绘制并存储水印
        Map<String, String> param = new HashMap<String, String>();
        param.put("srcImgHeight", String.valueOf(screen_Height));
        param.put("srcImgWidth", String.valueOf(screen_Width));
        param.put("isNew", String.valueOf(1));
        param.put("rotate", String.valueOf(0));

        // new LoadDate(this,
        // com.bonc.mobile.common.Constant.BASE_PATH).execute("/sys/security/getImage",
        // param);
    }

    // class LoadDate extends HttpRequestTask {
    // public LoadDate(Context context, String basePath) {
    // super(context, basePath);
    // }
    //
    // // @Override
    // // protected void handleResult(JSONObject result) {
    // // // callbackContext.success(result);
    // // if (!AppConstant.SEC_ENH) {
    // // System.out.println("result:" + result.toString());
    // // }
    // // }
    //
    // @Override
    // protected void handleResult(String result) {
    // // TODO Auto-generated method stub
    // // super.handleResult(result);
    // System.out.println("result:" + result);
    // }
    // }

    private void checkVersion() {

        new MyUpgradeManager(this).checkUpgrade();
    }

    class MyUpgradeManager extends UpgradeManager {
        public MyUpgradeManager(Context context) {
            super(context);
            basePath = Constant.SERVER_PATH;
            action = ActionConstant.GET_UPDATE_MESSAGE;
//            apkPath = Constant.APK_PATH;
        }

        @Override
        protected void noUpgrade() {
            checkConfig();
        }

        @Override
        protected void doCancel() {
            super.doCancel();
            if (!json.optBoolean("isUpgrade")) {
                noUpgrade();
            } else {
                finish();
            }
        }

        @Override
        protected void afterUpgrade() {
            finish();
        }

        @Override
        protected void onDownloadFinished(String path) {
            try {
                super.onDownloadFinished(path);
            } catch (Exception e) {
                e.printStackTrace();
                openFile(new File(path), Welcome.this);
            }

        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // startArrowAni();
        if (MyUtils.doInfilter(this))
            if (!MyUtils.isBackground(this)) {
                MyUtils.startProtal(this);
            }
    }

    public void checkConfig() {
        // Looper.getMainLooper().prepare();
        // 重设配置信息，正常情况下应该没有数据返回
        dateTask.execute(Constant.BASE_PATH, "/sysResource/resetConfig",
                new HashMap<String, String>());


//        String path = Constant.SDCard + "/tmp/" + "update.apk";
//        //在genymotion下6.0验证通过
//        LogUtils.logBySys("aaaaaaaaaaaaaaaaaaa:" + path);
//        LogUtils.toast(this, "aaaaaaaaaaaaaaaaaaa:" + path);
//        openFile(new File(path), this);

    }


    public void openFile(File file, Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.VIEW");
        String mimeType = getMIMEType(file);
        LogUtils.logBySys("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaauri:" + Uri.fromFile(file));
        LogUtils.toast(this, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaauri:" + Uri.fromFile(file));
        //7.0
        if (Build.VERSION.SDK_INT >= 24) {
            Uri uri = null;
            try {
                uri = FileProvider.getUriForFile(Welcome.this, "com.bonc.mobile.hbmclient.fileprovider", file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            LogUtils.toast(this, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, mimeType);
        } else
            intent.setDataAndType(Uri.fromFile(file), mimeType);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
        }

    }

    public String getMIMEType(File file) {
        String var1 = "";
        String var2 = file.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }

    // 初始化水印图片
    private void createWatermarkDrawable() {
        WatermarkImage.setWatermarkDrawable(this, "4A:"
                + User.getInstance().getUserCode());
    }

    WelComeLoadDateTask dateTask = new WelComeLoadDateTask(this) {

        @Override
        public void onPost(final String result, int flag) {
            switch (flag) {
                case 2:
                case 4:

                    new AlertDialog.Builder(Welcome.this)
                            .setTitle(R.string.hint)
                            .setMessage(result)
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            finish();
                                            stopService(new Intent(Welcome.this,
                                                    BoncService.class));
                                            System.exit(0);
                                        }
                                    }).show();

                    break;
                case 3:

                    new AlertDialog.Builder(Welcome.this)
                            .setTitle(R.string.hint)
                            .setMessage("初始化配置信息错误")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            stopService(new Intent(Welcome.this,
                                                    BoncService.class));
                                            finish();
                                            System.exit(0);
                                        }
                                    }).show();

                    break;
                default:
                    break;
            }
        }
    };

}
