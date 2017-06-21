package com.bonc.mobile.common.util;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.view.FixedHoloDatePickerDialog;
import com.bonc.mobile.common.view.GuideWindow;
import com.bonc.mobile.common.view.MyDatePickerDialogWithoutDay;

/**
 * 界面相关工具类
 *
 * @author sunwei
 */
public class UIUtil {
    public static Context getSafeContext(Context c) {
        Activity a = (Activity) c;
        if (a.isChild())
            c = a.getParent();
        return c;
    }

    public static void setWindowLayout(Window window, float width, float height) {
        Display display = window.getWindowManager().getDefaultDisplay();
        window.setLayout((int) (display.getWidth() * width),
                (int) (display.getHeight() * height));
    }

    public static void setWatermarkImage(Activity activity) {
        setWatermarkImage(activity, R.id.root);
    }

    public static void setWatermarkImage(Activity activity, int rootId) {
        View v = activity.findViewById(rootId);
        if (v != null) {
            Drawable d = FileUtils.getWatermark(activity, "watermark");
            if (d != null)
                v.setBackgroundDrawable(d);
            else
                v.setBackgroundResource(R.mipmap.watermark_background);
        }
    }

    public static void showAlertDialog(Context context, String message,
                                       OnClickListener listener) {
        new AlertDialog.Builder(context).setTitle(R.string.hint)
                .setMessage(message).setPositiveButton(R.string.ok, listener)
                .show();
    }

    public static void showAlertDialog(Context context, String message,
                                       OnClickListener listener1, OnClickListener listener2) {
        new AlertDialog.Builder(context).setTitle(R.string.hint)
                .setMessage(message).setPositiveButton(R.string.ok, listener1)
                .setNegativeButton(R.string.cancel, listener2).show();
    }

    public static void showAlertDialog(Context context, String title,
                                       View dialog_view, OnClickListener listener1,
                                       OnClickListener listener2) {
        new AlertDialog.Builder(UIUtil.resolveDialogTheme(context)).setTitle(title).setView(dialog_view)
                .setPositiveButton(R.string.ok, listener1)
                .setNegativeButton(R.string.cancel, listener2).show();
    }

    /**千里眼自定义频道管理专用 新增*/
    public static void showAlertDialogBy(Context context, String title,
                                         View dialog_view, OnClickListener listener1,
                                         OnClickListener listener2) {
        new AlertDialog.Builder(context)
                .setTitle(title).setView(dialog_view)
                .setPositiveButton(R.string.ok, listener1)
                .setNegativeButton(R.string.cancel, listener2).show();
    }
    public static void showAlertDialog(Context context, String title,
                                       CharSequence[] items, OnClickListener listener) {
        Context c  = com.bonc.mobile.common.util.UIUtil.resolveDialogTheme(context);
        AlertDialog d = new AlertDialog.Builder(c).setTitle(title)
                .setItems(items, listener).create();
        d.setCanceledOnTouchOutside(true);
        d.show();
    }

    public static void showDatePickerDialog(Context context,
                                            DatePickerDialog.OnDateSetListener callBack, Calendar calendar) {
        DatePickerDialog dialog = new DatePickerDialog(resolveDialogTheme(context), callBack,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        dialog.getDatePicker().setCalendarViewShown(false);

        dialog.setTitle("请选择时间");
        dialog.show();


    }

    /**
     * 有日框
     * API>24专用
     *
     * @param context
     * @param callBack
     * @param calendar
     */
    public static void showDatePickerDialogFor24(Context context,
                                                 DatePickerDialog.OnDateSetListener callBack, Calendar calendar) {
        final Context themedContext = new ContextThemeWrapper(context,
                // android.R.style.Widget_Material_NumberPicker
                android.R.style.Theme_Holo_Light_Dialog);

        FixedHoloDatePickerDialog datePicker = new FixedHoloDatePickerDialog(themedContext, callBack
                , calendar.get(Calendar.YEAR), calendar
                .get(Calendar.MONTH), calendar
                .get(Calendar.DAY_OF_MONTH));

//        dialog.getDatePicker().setCalendarViewShown(false);

        datePicker.setTitle("请选择时间");
        datePicker.show();


    }

    public static Context resolveDialogTheme(Context context) {

//        if (context instanceof ContextThemeWrapper)
//            return context;

        Context themedContext = new ContextThemeWrapper(context,
                // android.R.style.Widget_Material_NumberPicker
                android.R.style.Theme_Holo_Light_Dialog);

        return themedContext;
    }

    public static void showDatePickerDialogWithoutDay(Context context,
                                                      DatePickerDialog.OnDateSetListener callBack, Calendar calendar) {
        DatePickerDialog dialog = new MyDatePickerDialogWithoutDay(resolveDialogTheme(context), callBack,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setTitle("请选择时间");
        dialog.show();
    }

    /**
     * 无日框
     * API>24专用
     *
     * @param context
     * @param callBack
     * @param calendar
     */
    public static void showDatePickerDialogWithoutDayFor24(Context context,
                                                           DatePickerDialog.OnDateSetListener callBack, Calendar calendar) {


        Context themedContext = new ContextThemeWrapper(context,
                // android.R.style.Widget_Material_NumberPicker
                android.R.style.Theme_Holo_Light_Dialog);
        FixedHoloDatePickerDialog dialog = new FixedHoloDatePickerDialog(themedContext, callBack
                , calendar.get(Calendar.YEAR), calendar
                .get(Calendar.MONTH), calendar
                .get(Calendar.DAY_OF_MONTH));

        dialog.setHasNoDay(true);

        dialog.setTitle("请选择时间");
        dialog.show();
    }

    public static void disableScrShot(Activity a) {
        // if(AppConstant.SEC_ENH)
        // a.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }

    public static int fromDPtoPX(Context c, float dp) {
        return (int) (dp * c.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static void setListViewScrollSync(final ListView listView1,
                                             final ListView listView2) {
        listView2.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 如果已经停止滑动
                if (scrollState == 0 || scrollState == 1) {
                    // 获得第一个子view
                    View subView = view.getChildAt(0);

                    if (subView != null) {
                        final int top = subView.getTop();
                        final int top1 = listView1.getChildAt(0).getTop();
                        final int position = view.getFirstVisiblePosition();

                        // 如果两个首个显示的子view高度不等
                        if (top != top1) {
                            listView1.setSelectionFromTop(position, top);
                        }
                    }
                }

            }

            public void onScroll(AbsListView view, final int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                View subView = view.getChildAt(0);
                if (subView != null) {
                    final int top = subView.getTop();

                    int top1 = listView1.getChildAt(0).getTop();
                    if (!(top1 - 7 < top && top < top1 + 7)) {
                        listView1.setSelectionFromTop(firstVisibleItem, top);
                        listView2.setSelectionFromTop(firstVisibleItem, top);
                    }

                }
            }
        });

        // 设置左侧列表的scroll监听，用于滑动过程中左右不同步时校正
        listView1.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0 || scrollState == 1) {
                    View subView = view.getChildAt(0);

                    if (subView != null) {
                        final int top = subView.getTop();
                        final int top1 = listView2.getChildAt(0).getTop();
                        final int position = view.getFirstVisiblePosition();

                        if (top != top1) {
                            listView1.setSelectionFromTop(position, top);
                            listView2.setSelectionFromTop(position, top);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, final int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                View subView = view.getChildAt(0);
                if (subView != null) {
                    final int top = subView.getTop();

                    listView1.setSelectionFromTop(firstVisibleItem, top);
                    listView2.setSelectionFromTop(firstVisibleItem, top);

                }
            }
        });
    }

    public static void showNotice(Context c) {
        Toast t = new Toast(c);
        t.setView(LayoutInflater.from(c).inflate(R.layout.toast_notice, null));
        t.setGravity(Gravity.LEFT | Gravity.TOP, 20, 10);
        t.setDuration(Toast.LENGTH_LONG);
        t.show();
    }

    public static void selectItems(ListView view, boolean v) {
        ListAdapter adapter = view.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            view.setItemChecked(i, v);
        }
    }

    public static PopupWindow showInfoWindow(Context context, View v,
                                             View anchor) {
        PopupWindow p = new PopupWindow(v, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        p.showAsDropDown(anchor, 100, -40);
        return p;
    }

    public static void showGuideWindow(Context context, View parent,
                                       String tag, int[] guides) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        boolean skip_guide = sp.getBoolean("skip_guide", false);
        boolean visit = sp.getBoolean(tag, false);
        if (!skip_guide && !visit) {
            GuideWindow w = new GuideWindow(context);
            w.show(parent, guides);
            DataUtil.saveSetting(sp, tag, true);
        }
    }

//    public static void showGuideWindow(Context context, View parent,
//                                       String tag, int[] guides,String spname) {
//        SharedPreferences sp = PreferenceManager
//                .getDefaultSharedPreferences(context);
//        boolean skip_guide = sp.getBoolean(spname, false);
//        boolean visit = sp.getBoolean(tag, false);
//        if (!skip_guide && !visit) {
//            GuideWindow w = new GuideWindow(context);
//            w.show(parent, guides);
//            DataUtil.saveSetting(sp, tag, true);
//        }
//    }

    public static void startTransAnim(Activity a) {
        a.overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);
    }

    public static int getDisplayHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();

        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static int getDisplayWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();

        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
}
