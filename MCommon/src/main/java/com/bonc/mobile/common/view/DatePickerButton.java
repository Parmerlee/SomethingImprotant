package com.bonc.mobile.common.view;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.util.DateUtil;
import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.common.util.UIUtil;

/**
 * @author sunwei
 */
public class DatePickerButton extends Button {
    protected Calendar mCalendar;
    protected String mDatePattern;
    protected DatePickerDialog.OnDateSetListener mOnDateSetListener;
    boolean withTime;
    boolean isSet;
    Context context;

    public DatePickerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public DatePickerButton(Context context) {
        super(context);
        this.context = context;
        init();
    }

    protected void init() {
        mCalendar = Calendar.getInstance();
        mDatePattern = DateUtil.PATTERN_MODEL2_10;
        if (Build.VERSION.SDK_INT >= 24) {
            setOnClickListenerFor24();
        } else {
            setOnClickListener();
        }

    }

    void setOnClickListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.toast(context, "AAAAA:" + Build.VERSION.SDK_INT);
                if (hasNoDay) {
                    UIUtil.showDatePickerDialogWithoutDay(
                            UIUtil.getSafeContext(getContext()),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view,
                                                      int year, int monthOfYear,
                                                      int dayOfMonth) {
                                    // hideDay(view);
                                    isSet = true;
                                    mCalendar
                                            .set(year, monthOfYear, dayOfMonth);
                                    setText(DateUtil.formatter(
                                            mCalendar.getTime(),
                                            DateUtil.PATTERN_MODEL2_7));
                                    if (mOnDateSetListener != null)
                                        mOnDateSetListener.onDateSet(view,
                                                year, monthOfYear, dayOfMonth);
                                }
                            }, mCalendar);

                    return;
                }
                if (withTime) {
                    showDateTimeDlg();
                } else {
                    UIUtil.showDatePickerDialog(
                            UIUtil.getSafeContext(getContext()),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view,
                                                      int year, int monthOfYear,
                                                      int dayOfMonth) {
                                    // hideDay(view);
                                    isSet = true;
                                    mCalendar
                                            .set(year, monthOfYear, dayOfMonth);
                                    setText(DateUtil.formatter(
                                            mCalendar.getTime(), mDatePattern));
                                    if (mOnDateSetListener != null)
                                        mOnDateSetListener.onDateSet(view,
                                                year, monthOfYear, dayOfMonth);
                                }
                            }, mCalendar);
                }
            }
        });
    }

    void setOnClickListenerFor24() {

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.toast(context, "AAAAA:" + Build.VERSION.SDK_INT);
                if (withTime) {
                    showDateTimeDlg();
                    return;
                }
                final Context themedContext = new ContextThemeWrapper(
                        UIUtil.getSafeContext(getContext()),
//                            android.R.style.Widget_Material_NumberPicker
                        android.R.style.Theme_Holo_Light_Dialog
                );

                DatePickerDialog dialog = new FixedHoloDatePickerDialog(
                        themedContext,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view,
                                                  int year, int monthOfYear,
                                                  int dayOfMonth) {
                                isSet = true;
                                mCalendar
                                        .set(year, monthOfYear, dayOfMonth);
                                setText(DateUtil.formatter(
                                        mCalendar.getTime(), mDatePattern));
                                if (mOnDateSetListener != null)
                                    mOnDateSetListener.onDateSet(view,
                                            year, monthOfYear, dayOfMonth);
                            }
                        },
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)
                );

                ((FixedHoloDatePickerDialog) dialog).setHasNoDay(hasNoDay);


                dialog.setTitle("请选择日期");

                dialog.show();
            }
        });
    }


    //	return;
//}
//}
    void showDateTimeDlg() {
        Context c = UIUtil.getSafeContext(getContext());
        View v = View.inflate(c, R.layout.dt_picker_dlg, null);
        final DatePicker datePicker = (DatePicker) v
                .findViewById(R.id.datePicker);
        final TimePicker timePicker = (TimePicker) v
                .findViewById(R.id.timePicker);
        datePicker.init(mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH), null);
        timePicker.setIs24HourView(true);

        timePicker.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(mCalendar.get(Calendar.MINUTE));
        hideMin(timePicker, datePicker);
        UIUtil.showAlertDialog(c, c.getString(R.string.hint), v,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        mCalendar.set(datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth(),
                                timePicker.getCurrentHour(), 0);
                        setText(DateUtil.formatter(mCalendar.getTime(),
                                mDatePattern));
                        if (mOnDateSetListener != null)
                            mOnDateSetListener.onDateSet(datePicker, 0, 0, 0);
                    }
                }, null);
    }

    public void setOnDateSetListener(DatePickerDialog.OnDateSetListener listener) {
        mOnDateSetListener = listener;
    }

    /**
     * 显示分钟时间 *
     */
    public void setWithTime(boolean b) {
        withTime = b;
    }

    boolean hasNoDay;

    /**
     * 选择框无日 *
     */
    public void setWithoutDay(boolean b) {
        hasNoDay = b;
    }

    public Date getDate() {
        return mCalendar.getTime();
    }

    public boolean isDateSet() {
        return isSet;
    }

    public void setDate(Date date) {
        mCalendar.setTime(date);
        setText(DateUtil.formatter(mCalendar.getTime(), mDatePattern));
    }

    public void setPattern(String pattern) {
        mDatePattern = pattern;
    }

    void hideMin(TimePicker p, DatePicker datePicker) {
//        try {
//            Field f = TimePicker.class.getDeclaredField("mMinuteSpinner");
//            f.setAccessible(true);
//            NumberPicker n = (NumberPicker) f.get(p);
//            n.setVisibility(View.GONE);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        Field[] datePickerfFields = p.getClass().getDeclaredFields();
//
//        for (Field datePickerField : datePickerfFields) {
//
//            LogUtils.logBySys(datePickerField.getName());
//            if ("mMinuteSpinner".equals(datePickerField.getName())) {
//
//                //这里这个变量是系统的定义的变量，不是自己定义的​
//                datePickerField.setAccessible(true);
//
//                Object dayPicker = new Object();
//
//                try {
//
//                    dayPicker = datePickerField.get(p);
//
//                } catch (IllegalAccessException e) {
//
//                    e.printStackTrace();
//
//                } catch (IllegalArgumentException e) {
//
//                    e.printStackTrace();
//
//                }
//
//                // datePicker.getCalendarView().setVisibility(View.GONE);
//
//                ((View) dayPicker).setVisibility(View.GONE);
//
//            }
//
//        }

//        try {
//            ((ViewGroup) ((ViewGroup) p.getChildAt(0))
//                    .getChildAt(0)).getChildAt(2).setVisibility(
//                    View.GONE);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        try {
            List<NumberPicker> npList = findNumberPicker(p);
            npList.get(1).setVisibility(View.GONE);
//            findNumberPicker(datePicker)
            npList.addAll(findNumberPicker(datePicker));

            for (NumberPicker np : npList) {
                resizeNumberPicker(datePicker, np);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void resizeNumberPicker(DatePicker p, NumberPicker np) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                UIUtil.fromDPtoPX(getContext(), 80), ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(UIUtil.fromDPtoPX(getContext(), -3), 0,
                UIUtil.fromDPtoPX(getContext(), -3), 0);
//        EditText view = (EditText) np.findViewById(android.R.id.numberpicker_input);
//        view.setTextColor(android.R.color.primary_text_light);
//        view.setTextSize(25f);
//        NumberPicker mp = (NumberPicker) p.findViewById(android.R.id.day);
        np.setLayoutParams(params);

    }

    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if (null != viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    LogUtils.logBySys("number:" + child.toString());
                    npList.add((NumberPicker) child);

//                    EditText view = (EditText) child.findViewById(android.R.id.numberpicker_input);
//                    view.setTextColor(android.R.color.primary_text_light);
//                    view.setTextSize(25f);

                } else if (child instanceof LinearLayout) {
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0) {
                        return result;
                    }
                }
            }
        }
        return npList;
    }
}
