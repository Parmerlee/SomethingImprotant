package com.bonc.mobile.common.view;
import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogUtils {
    private static ProgressDialog dialog = null;

    public static void showProgressDialog(Context context, String message) {
        if (null != dialog) {
            dialog.dismiss();
            dialog = null;
        }

        dialog = new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.show();
    }

    public static void dismissPrgressDialog() {
        if (null != dialog) {
            dialog.dismiss();
        }
    }
}
