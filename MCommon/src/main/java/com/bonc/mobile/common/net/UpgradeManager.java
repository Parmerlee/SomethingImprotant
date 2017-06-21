
package com.bonc.mobile.common.net;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.bonc.mobile.common.Constant;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.common.util.PackageUtil;
import com.bonc.mobile.common.util.UIUtil;

public class UpgradeManager extends SimpleDownloadManager {
    protected String basePath, action, apkPath;
    protected SharedPreferences pref;
    protected JSONObject json;

    public UpgradeManager(Context context) {
        super(context);
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        apkPath = Constant.APK_PATH;
    }

    public void checkUpgrade() {
        checkUpgrade(null);
    }

    public void checkUpgrade(String type) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("version", "" + PackageUtil.getVersionCode(context));
        if (type != null)
            param.put("term", type);
        new CheckUpgradeTask(context, basePath).execute(action, param);
    }

    class CheckUpgradeTask extends HttpRequestTask {
        public CheckUpgradeTask(Context context, String basePath) {
            super(context, basePath);
        }

        @Override
        protected void handleResult(JSONObject result) {
            handleUpgradeResult(result);
        }
    }

    protected void handleUpgradeResult(JSONObject result) {
        json = result;
        int flag = result.optInt("flag");
        if (flag == 1 || flag == 0
                && !pref.getString("last_version", "").equals(result.optString("version"))) {
            final String url = result.optString("url");
            String msg = result.optString("msg");
            msg = msg + "\n本次升级是" + (flag == 1 ? "必需的" : "可选的");
            UIUtil.showAlertDialog(context, msg, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    doUpgrade(url);
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    doCancel();
                }
            });
        } else {
            noUpgrade();
        }
    }

    protected void doCancel() {
        if (json.optInt("flag") == 0) {
            DataUtil.saveSetting(pref, "last_version", json.optString("version"));
            noUpgrade();
        } else {
            cancelUpgrade();
        }
    }

    protected void noUpgrade() {
    }

    protected void afterUpgrade() {
    }

    protected void cancelUpgrade() {
    }

    protected void doUpgrade(String url) {
        LogUtils.logBySys(" the network uri:" + url + ";the local uri:" + apkPath + ";full url:" + apkPath + url);
//        url = apkPath + url;
        downloadFile(url,
                new File(Environment.getExternalStorageDirectory(), "tmp/update.apk")
                        .getAbsolutePath());
    }

    @Override
    protected void onDownloadFinished(String path) {
        super.onDownloadFinished(path);
        afterUpgrade();
    }

}
