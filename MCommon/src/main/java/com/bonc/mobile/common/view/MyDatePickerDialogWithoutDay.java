package com.bonc.mobile.common.view;

import java.util.ArrayList;
import java.util.List;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.bonc.mobile.common.util.UIUtil;

public class MyDatePickerDialogWithoutDay extends DatePickerDialog {
    // private OnDateSetListener callBack;

    public MyDatePickerDialogWithoutDay(Context context,
                                        OnDateSetListener callBack, int year, int monthOfYear,
                                        int dayOfMonth) {
        super(UIUtil.resolveDialogTheme(context), callBack, year, monthOfYear, dayOfMonth);

        this.setTitle(year + "年" + (monthOfYear + 1) + "月");

        try {
            ((ViewGroup) ((ViewGroup) this.getDatePicker().getChildAt(0))
                    .getChildAt(0)).getChildAt(2).setVisibility(View.GONE);

            resizePikcer(this.getDatePicker());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void resizePikcer(DatePicker tp) {

        List<NumberPicker> npList = findNumberPicker(tp);
        for (NumberPicker np : npList) {
            resizeNumberPicker(np);
        }
    }

    private void resizeNumberPicker(NumberPicker np) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                UIUtil.fromDPtoPX(getContext(), 90), LayoutParams.WRAP_CONTENT);
        params.setMargins(UIUtil.fromDPtoPX(getContext(), 5), 0,
                UIUtil.fromDPtoPX(getContext(), 5), 0);
        np.setLayoutParams(params);

    }

    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if (null != viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    npList.add((NumberPicker) child);
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

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        super.onDateChanged(view, year, month, day);
        this.setTitle(year + "年" + (month + 1) + "月");
    }

    @Override
    public void show() {
        super.show();

        setCanceledOnTouchOutside(true);

    }

}
