package common.share.lwg.util.asyntask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.net.SimpleDownloadManager;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.PackageUtil;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.util.HttpUtil;

public class UpgradeManager extends SimpleDownloadManager {
    protected String basePath, action, apkPath;
    protected JSONObject json;

    public UpgradeManager(Context context) {
        super(context);
    }

    public void checkUpgrade() {

        Map<String, String> param = new HashMap<String, String>();
        param.put("versionno", "" + PackageUtil.getVersionCode(context));
        new CheckUpgradeTask(context, basePath).execute(action, param);

    }

    class CheckUpgradeTask extends HttpRequestTask {
        public CheckUpgradeTask(Context context, String basePath) {
            super(context, basePath);
        }

        @Override
        protected void handleResult(JSONObject result) {
            json = result;

            if (json.optBoolean("isUpgrade")) {
                try {
                    JSONObject data = json.getJSONObject("data");

                    final String url = data.optString("version_updateurl");
                    String msg = data.optString("msg");
                    UIUtil.showAlertDialog(context, msg,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                    doUpgrade(url);
                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                noUpgrade();
            }
        }
    }

    protected void doCancel() {
        if (json.optInt("flag") == 0) {
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
//		url = apkPath + url;
        downloadFile(url, new File(Environment.getExternalStorageDirectory(),
                "tmp/update.apk").getAbsolutePath());
    }

    @Override
    protected void onDownloadFinished(String path) {
        super.onDownloadFinished(path);
        afterUpgrade();
    }

}
