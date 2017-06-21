/**
 * DateSelector
 */
package com.bonc.mobile.hbmclient.command.business_outlets;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Build;
import android.text.format.DateUtils;
import android.view.ContextThemeWrapper;
import android.widget.DatePicker;

import com.bonc.mobile.common.view.FixedHoloDatePickerDialog;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.view.MyDatePickerDialog;

/**
 * @author liweigao
 */
public class DateSelector {
    private Context context;
    private Calendar calendar;
    private DatePickerDialog datePicker;
    private long last_select_time = 0;
    private OnDateSelectListener listener;

    public DateSelector(Context c) {
        this.context = c;
    }

    public void setOnDateSelectListener(OnDateSelectListener odsl) {
        this.listener = odsl;
    }

    public void setDate(String date) {
        if (date == null) {
            date = DateRangeEnum.DAY.getDateSpecified(-1,
                    DateRangeEnum.DAY.getDateServerPattern());
        } else {

        }
        this.calendar = DateUtil.getCalendar(date,
                DateRangeEnum.DAY.getDateServerPattern());
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
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                // 两秒之内的操作不能重复
                long current_time = System.currentTimeMillis();
                if (current_time - getTime() < 2000) {
                    return;
                }
                setTime(current_time);
                calendar.set(year, monthOfYear, dayOfMonth);
                listener.onDateSelect(DateSelector.this);
            }
        };
        if (Build.VERSION.SDK_INT >= 24) {
            final Context themedContext = new ContextThemeWrapper(this.context,
                    // android.R.style.Widget_Material_NumberPicker
                    android.R.style.Theme_Holo_Light_Dialog);

            datePicker = new FixedHoloDatePickerDialog(themedContext, listener1
                    , calendar.get(Calendar.YEAR), calendar
                    .get(Calendar.MONTH), calendar
                    .get(Calendar.DAY_OF_MONTH));
        } else
            datePicker = new MyDatePickerDialog(this.context,
                    listener1, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));

//        if (hasNoDay)
//            title = year + "年" + (month + 1) + "月";

        datePicker.setTitle("请选择时间");
        datePicker.show();
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
                DateRangeEnum.DAY.getDateShowPattern());
    }

    public String getServerDate() {
        if (this.calendar != null) {
            return DateUtil.formatter(this.calendar.getTime(),
                    DateRangeEnum.DAY.getDateServerPattern());
        } else {
            return null;
        }
    }

    public interface OnDateSelectListener {
        void onDateSelect(DateSelector ds);
    }
}
