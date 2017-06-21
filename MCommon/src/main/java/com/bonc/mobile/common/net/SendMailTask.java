
package com.bonc.mobile.common.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.util.PhoneUtil;

/**
 * http请求Task
 * 
 * @author sunwei
 */
public class SendMailTask extends AsyncTask<String, Integer, Boolean> {
    protected Context context;
    protected ProgressDialog dialog;

    public SendMailTask(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, context.getString(R.string.hint), "正在发送邮件...");
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            PhoneUtil.sendMail(params[0], params[1], params[2], params[3], params[4], params[5]);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        dialog.dismiss();
        Toast.makeText(context, result == true ? "发送成功" : "发送失败", Toast.LENGTH_SHORT).show();
    }
}
