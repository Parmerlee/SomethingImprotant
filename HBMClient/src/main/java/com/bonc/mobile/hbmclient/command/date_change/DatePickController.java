/**
 *
 */
package com.bonc.mobile.hbmclient.command.date_change;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Build;
import android.view.ContextThemeWrapper;
import android.widget.DatePicker;

import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.common.view.FixedHoloDatePickerDialog;
import com.bonc.mobile.hbmclient.command.ICommand;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.LogUtil;
import com.bonc.mobile.hbmclient.view.MyDatePickerDialog;
import com.bonc.mobile.hbmclient.view.MyDatePickerDialogWithoutDay;

/**
 * @author liweigao
 */
public class DatePickController {
    private Context context;
    private ICommand slot;
    private DatePickerDialog mDateDia;
    private Calendar calendar;
    private long last_select_time = 0;

    public DatePickController(Context c) {
        this.context = c;
    }

    public void initialCalendar() {
        // this.calendar = Calendar.getInstance();
        // this.calendar.setTime(new Date());
        // this.calendar.add(Calendar.DAY_OF_MONTH, -1);

        executeCommand();
    }

    public void setCommand(ICommand c) {
        this.slot = c;
    }

    public void pickDate() {
        if (mDateDia != null && mDateDia.isShowing()) {
            mDateDia.dismiss();
        }

        LogUtils.toast(context, Build.VERSION.SDK_INT);
        OnDateSetListener listener = new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                // 两秒之内的操作不能重复
                long current_time = System.currentTimeMillis();
                if (current_time - getTime() < 2000) {
                    return;
                }

                setTime(current_time);

                calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                calendar.getTimeInMillis();
                executeCommand();
            }
        };
        if (Build.VERSION.SDK_INT >= 24) {
            final Context themedContext = new ContextThemeWrapper(context,
                    // android.R.style.Widget_Material_NumberPicker
                    android.R.style.Theme_Holo_Light_Dialog);

            mDateDia = new FixedHoloDatePickerDialog(themedContext,
                    listener, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));

//            ((FixedHoloDatePickerDialog) datePicker).setHasNoDay(withoutDay);
        } else {
//            if (withoutDay) {
//
//                mDateDia = new MyDatePickerDialogWithoutDay(this.context,
//                        listener, calendar.get(Calendar.YEAR),
//                        calendar.get(Calendar.MONTH),
//                        calendar.get(Calendar.DAY_OF_MONTH));
//            } else {

            mDateDia = new MyDatePickerDialog(this.context, listener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
//            }
        }

//        mDateDia = new MyDatePickerDialog(context, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH));
        mDateDia.setTitle("请选择时间");
        mDateDia.show();
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    public void setCalendar(Calendar c) {
        this.calendar = c;
    }

    public void setCalendar(String date, String pattern) {
        if (this.calendar != null) {

        } else {
            this.calendar = DateUtil.getCalendar(date, pattern);
        }
    }

    private void executeCommand() {
        slot.execute(calendar);
    }

    private synchronized void setTime(long time) {
        this.last_select_time = time;
    }

    private synchronized long getTime() {
        return last_select_time;
    }
}
