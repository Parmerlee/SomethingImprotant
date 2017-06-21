/**
 * QuestAsynTask
 */
package common.share.lwg.util.asyntask;

import java.io.IOException;
import java.util.Map;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.Constant;
import com.bonc.mobile.common.util.DES;
import com.bonc.mobile.hbmclient.util.HttpUtil;

/**
 * @author liweigao
 */
public abstract class QuestAsynTask extends AsyncTask<Object, Integer, Object> {
    private ProgressDialog mProgressDialog;
    private Context context;

    private String firstAction = null;

    public QuestAsynTask(Context context) {
        this.context = context;
    }

    public QuestAsynTask(Context context, String action) {
        this.context = context;
        this.firstAction = action;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected Object doInBackground(Object... params) {
        String action = (String) params[0];
        Map<String, String> questArgs = (Map<String, String>) params[1];
        String result = null;
        if (!TextUtils.isEmpty(firstAction)) {
            try {
//                if (!AppConstant.SEC_ENH) {
//                result = DES.decrypt2(com.bonc.mobile.common.net.HttpUtil.sendRequest("http://120.202.17.126:8070", firstAction + action, questArgs));
//                } else
                result = DES.decrypt2(com.bonc.mobile.common.net.HttpUtil.sendRequest(Constant.BASE_PATH, firstAction + action, questArgs));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            result = HttpUtil.sendRequest(action, questArgs);
        }


        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.os.AsyncTask#onPreExecute()
     */
    @TargetApi(17)
    @Override
    protected void onPreExecute() {

        Activity a = (Activity) context;
        if (a.isDestroyed() || a.isFinishing()) {
            return;
        }

        if (this.mProgressDialog != null) {
            if (this.mProgressDialog.isShowing()) {
                this.mProgressDialog.dismiss();
            } else {

            }
            this.mProgressDialog = null;
        } else {

        }
        this.mProgressDialog = new ProgressDialog(context);
        this.mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.mProgressDialog.setMessage("数据加载中...");
        this.mProgressDialog.setIndeterminate(false);

        if (!(a.isDestroyed() || a.isFinishing()))
            this.mProgressDialog.show();

    }

    /*
     * (non-Javadoc)
     *
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(Object result) {
        onPost(result);
        if (this.mProgressDialog != null) {
            if (this.mProgressDialog.isShowing()) {
                this.mProgressDialog.dismiss();
            } else {

            }
        } else {

        }
        this.mProgressDialog = null;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.os.AsyncTask#onProgressUpdate(Progress[])
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        // TODO Auto-generated method stub
        super.onProgressUpdate(values);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.os.AsyncTask#onCancelled()
     */
    @Override
    protected void onCancelled() {
        // TODO Auto-generated method stub
        super.onCancelled();
    }

    abstract public void onPost(Object result);

}
