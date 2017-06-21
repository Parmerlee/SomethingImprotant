
package com.bonc.mobile.common.net;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.net.Download.OnDownloadListener;
import com.bonc.mobile.common.util.LogUtils;

public class SimpleDownloadManager {
    protected Context context;
    protected ProgressDialog progressDialog;
    protected Handler handler;

    public SimpleDownloadManager(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        handler = new Handler();
    }

    public void downloadFile(String url, String path) {
        System.out.println("DL:" + url);
        HttpUtil.download(url, path, new OnDownloadListener() {
            public void onDownload(final long downSize, final long fileSize) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.setMax((int) fileSize);
                        progressDialog.setProgress((int) downSize);
                    }
                });
            }

            public void onFinished(long downSize, String url, final String path) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        onDownloadFinished(path);
                    }
                });
            }

            public void onStop(long downSize, String url, String path) {
            }

            public void OnError(String error) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(context, "下载失败", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        showProgress(url, "正在下载,请稍候...");
    }

    void showProgress(String url, String message) {
        progressDialog.setTitle(R.string.hint);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    protected void onDownloadFinished(String path) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(path)),
                "application/vnd.android.package-archive");
//        try {
        context.startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//            File file = new File(path);
//            if (file.exists()) {
//                openFile(file, context);
//            }
//        }
    }


    public void openFile(File file, Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String mimeType = getMIMEType(file);
        //   //7.0
//        if (Build.VERSION.SDK_INT >= 24) {
//            Uri uri = null;
//            try {
//                uri = FileProvider.getUriForFile(context, "com.bonc.mobile.hbmclient.fileprovider", file);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
////            LogUtils.toast(this, uri);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.setDataAndType(uri, mimeType);
//        } else
        intent.setDataAndType(Uri.fromFile(file), mimeType);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();

            intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            mimeType = getMIMEType(file);
            Uri uri = null;
            uri = FileProvider.getUriForFile(context, "com.bonc.mobile.hbmclient.fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, mimeType);
            context.startActivity(intent);
        }

    }

    public String getMIMEType(File file) {
        String var1 = "";
        String var2 = file.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }
}
