package com.bonc.mobile.hbmclient.util;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.os.Build;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.ContextThemeWrapper;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bonc.mobile.common.util.*;
import com.bonc.mobile.common.view.FixedHoloDatePickerDialog;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.view.MyDatePickerDialogWithoutDay;

/**
 * 界面相关工具类
 *
 * @author sunwei
 */
public class UIUtil {

    /***判断是否是开门空  开门红menuCode为60001101 **/
    public static boolean isNeedNew(String menuCode){
        return TextUtils.equals(menuCode,"60001100")||TextUtils.equals(menuCode,"60001101");
    }

    public static void showAlertDialog(Context context, int messageId,
                                       OnClickListener listener) {
        new AlertDialog.Builder(context).setTitle(R.string.hint)
                .setMessage(messageId)
                .setPositiveButton(R.string.sure, listener).show();
    }

    public static void showAlertDialog(Context context, CharSequence[] items,
                                       OnClickListener listener) {
        Context c  = com.bonc.mobile.common.util.UIUtil.resolveDialogTheme(context);
        new AlertDialog.Builder(c).setTitle(R.string.hint)
                .setItems(items, listener).show();
    }

    public static void showDatePickerDialog(Context context,
                                            DatePickerDialog.OnDateSetListener callBack, Calendar calendar) {
        if (Build.VERSION.SDK_INT >= 24) {
            showDatePickerDialogFor24(context, callBack, calendar);
            return;
        } else {


            DatePickerDialog dialog = new DatePickerDialog(com.bonc.mobile.common.util.UIUtil.resolveDialogTheme(context), callBack,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.setTitle("请选择时间");
            dialog.show();
        }
    }

    private static void showDatePickerDialogFor24(Context context,
                                                  DatePickerDialog.OnDateSetListener callBack, Calendar calendar) {
        showDatePickerDialogFor24(context, callBack, calendar, true);
    }

    /**
     * @param context
     * @param callBack
     * @param calendar
     * @param hasDay   是否有日 默认为true 有日
     */
    public static void showDatePickerDialogFor24(Context context,
                                                 DatePickerDialog.OnDateSetListener callBack, Calendar calendar, boolean hasDay) {
        final Context themedContext = new ContextThemeWrapper(context,
//                            android.R.style.Widget_Material_NumberPicker
                android.R.style.Theme_Holo_Light_Dialog
        );

        DatePickerDialog dialog = new FixedHoloDatePickerDialog(
                themedContext,
                callBack,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        ((FixedHoloDatePickerDialog) dialog).setHasNoDay(!hasDay);

        dialog.setTitle("请选择时间");

        dialog.show();

    }

    public static void showDatePickerDialogWithoutDay(Context context,
                                                      DatePickerDialog.OnDateSetListener callBack, Calendar calendar) {
        if (Build.VERSION.SDK_INT >= 24) {
            showDatePickerDialogFor24(context, callBack, calendar, false);
            return;
        } else {


            MyDatePickerDialogWithoutDay dialog = new MyDatePickerDialogWithoutDay(
                    com.bonc.mobile.common.util.UIUtil.resolveDialogTheme(context), callBack, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));

            dialog.setTitle("请选择时间");

            dialog.show();
        }
    }

}
