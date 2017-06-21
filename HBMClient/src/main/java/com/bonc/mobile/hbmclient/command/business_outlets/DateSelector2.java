/**
 * DateSelector2
 */
package com.bonc.mobile.hbmclient.command.business_outlets;

import java.lang.reflect.Field;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Build;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.bonc.mobile.common.view.FixedHoloDatePickerDialog;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.view.MyDatePickerDialog;
import com.bonc.mobile.hbmclient.view.MyDatePickerDialogWithoutDay;

/**
 * @author liweigao
 */
public class DateSelector2 {
    private Context context;
    private Calendar calendar;
    private DatePickerDialog datePicker;
    private long last_select_time = 0;
    private OnDateSelectListener listener;
    private DateRangeEnum dateRangeEnum;

    public DateSelector2(Context c, DateRangeEnum dre) {
        this.context = c;
        this.dateRangeEnum = dre;
    }

    public void setOnDateSelectListener(OnDateSelectListener odsl) {
        this.listener = odsl;
    }

    public void setDate(String date) {
        if (date == null) {
            date = this.dateRangeEnum.getDateSpecified(-1,
                    this.dateRangeEnum.getDateServerPattern());
        } else {

        }
        this.calendar = DateUtil.getCalendar(date,
                this.dateRangeEnum.getDateServerPattern());
    }

    public void chooseDate() {
        if (datePicker != null && datePicker.isShowing()) {
            datePicker.dismiss();
        }

        if (this.calendar == null) {
            this.calendar = Calendar.getInstance();
        }

        OnDateSetListener listener1 = new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // 两秒之内的操作不能重复
                long current_time = System.currentTimeMillis();
                if (current_time - getTime() < 2000) {
                    return;
                }
                // hideDay(view);
                setTime(current_time);
                calendar.set(year, monthOfYear, dayOfMonth);
                listener.onDateSelect(DateSelector2.this);
            }
        };
        if (Build.VERSION.SDK_INT >= 24) {
            final Context themedContext = new ContextThemeWrapper(context,
                    // android.R.style.Widget_Material_NumberPicker
                    android.R.style.Theme_Holo_Light_Dialog);

            datePicker = new FixedHoloDatePickerDialog(themedContext,
                    listener1, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));


            ((FixedHoloDatePickerDialog) datePicker).setHasNoDay(withoutDay);
        } else {
            if (withoutDay) {

                datePicker = new MyDatePickerDialogWithoutDay(this.context,
                        listener1, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
            } else {

                datePicker = new MyDatePickerDialog(this.context, listener1,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
            }
        }
        datePicker.setTitle("请选择时间");
        datePicker.show();
    }

    boolean withoutDay = false;

    /**
     * 日期控件，不现实Day *
     */
    public void setWithoutDay(boolean b) {
        withoutDay = b;
    }

    void hideDay(DatePicker p) {
        try {
            Field f = DatePicker.class.getDeclaredField("mDaySpinner");
            f.setAccessible(true);
            NumberPicker n = (NumberPicker) f.get(p);
            n.setVisibility(View.GONE);
        } catch (Exception e) {
        }
    }

    private synchronized void setTime(long time) {
        this.last_select_time = time;
    }

    private synchronized long getTime() {
        return last_select_time;
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    public String getShowDate() {
        return DateUtil.formatter(this.calendar.getTime(),
                this.dateRangeEnum.getDateShowPattern());
    }

    public String getServerDate() {
        if (this.calendar != null) {
            return DateUtil.formatter(this.calendar.getTime(),
                    this.dateRangeEnum.getDateServerPattern());
        } else {
            return null;
        }
    }

    public interface OnDateSelectListener {
        void onDateSelect(DateSelector2 ds);
    }
}
