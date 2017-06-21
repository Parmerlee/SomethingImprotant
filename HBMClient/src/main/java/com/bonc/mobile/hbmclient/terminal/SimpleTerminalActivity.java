/**
 * TODO
 */
package com.bonc.mobile.hbmclient.terminal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.view.FixedHoloDatePickerDialog;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.BaseActivity;
import com.bonc.mobile.hbmclient.asyn_task.TermLoadDataAsynTask;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;
import com.bonc.mobile.hbmclient.terminal.component.ViewComponent;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.util.UIUtil;
import com.bonc.mobile.hbmclient.view.BoardScrollView;
import com.bonc.mobile.hbmclient.view.MyDatePickerDialog;
import com.bonc.mobile.hbmclient.view.MyDatePickerDialogWithoutDay;
import com.bonc.mobile.hbmclient.view.Top10HorizontalScrollView;

/**
 * @author liweigao
 */
public abstract class SimpleTerminalActivity extends Activity {
    protected TerminalGroupActivity mTerminalGroupActivity;
    public TerminalActivityEnum mTerminalActivityEnum;
    protected ViewComponent mRootView;
    protected String mDateString;
    protected Button mDateSelect, areaSelect;
    protected BoardScrollView mBoardScrollView;
    protected ImageView iv_arrow_down;
    protected Top10HorizontalScrollView mTop10HorizontalScrollView;
    protected ImageView iv_arrow_top10;

    private long last_select_time = 0;
    private DatePickerDialog mDateDia;
    private ProgressDialog progressDialog;
    List<Map<String, String>> areaInfoList;
    String[] areaNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mTerminalGroupActivity = (TerminalGroupActivity) getParent();
        if (!AppConstant.SEC_ENH)
            Log.d("AAAA", "the act is:" + this.getClass().getName());

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // startArrowAni();
        if (MyUtils.doInfilter(this)) {

            if (!MyUtils.isBackground(this)) {
                MyUtils.startProtal(this);
            }
        }
    }

    protected void initialDateRangeSelectListener(Button tv,
                                                  final TerminalActivityEnum tae) {
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTerminalGroupActivity.setTerminalActivityEnum(tae);
                mTerminalGroupActivity.showActivity(tae);
            }
        });
    }

    public ViewComponent getViewComponent() {
        return this.mRootView;
    }

    protected void initialDateSelectListener(Button tv) {
        tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (mTerminalActivityEnum.getDateRange()) {
                    case DAY:
                        showDatePicker();
                        break;
                    case MONTH:
                        showDatePickerWithoutDay();
                        break;
                }
            }
        });
    }

    public void initialUI() {
        if (initialDateShow()) {
            if (mRootView != null) {
                mRootView.iteratorUpdateView();
                mRootView.iteratorSetViewListener();
            }
            progressDialog.dismiss();
            startArrowAni();
        } else {
            noDataTip();
        }
    }

    protected void startArrowAni() {
        ImageView iv_arrow_right = (ImageView) this
                .findViewById(R.id.id_arrow_right);
        Animation ani_alpha_change = AnimationUtils.loadAnimation(this,
                R.anim.alpha_change);
        iv_arrow_right.startAnimation(ani_alpha_change);
        if (this.mBoardScrollView.getContentView().getMeasuredHeight() <= this.mBoardScrollView
                .getScrollY() + this.mBoardScrollView.getHeight()) {
            iv_arrow_down.setVisibility(View.INVISIBLE);
        } else {
            iv_arrow_down.setVisibility(View.VISIBLE);
            iv_arrow_down.startAnimation(ani_alpha_change);
        }
    }

    public void initialDate() {
        switch (mTerminalActivityEnum.getDateRange()) {
            case DAY:
                String date1 = Business.getMaxDay(mTerminalActivityEnum
                        .getMenuCode());
                if (date1 == null || "".equals(date1)) {

                } else {
                    Calendar c1 = DateUtil.getCalendar(date1,
                            DateRangeEnum.DAY.getDateServerPattern());
                    mTerminalActivityEnum.getView().setTag(c1);
                    this.mTerminalActivityEnum.setMaxDate(Business
                            .getMaxDay(mTerminalActivityEnum.getMenuCode()));
                    this.mTerminalActivityEnum.setMinDate(Business
                            .getMinDay(mTerminalActivityEnum.getMenuCode()));
                }
                break;
            case MONTH:
                String date2 = Business.getMaxMonth(mTerminalActivityEnum
                        .getMenuCode());
                if (date2 == null || "".equals(date2)) {

                } else {
                    Calendar c2 = DateUtil.getCalendar(date2,
                            DateRangeEnum.MONTH.getDateServerPattern());
                    mTerminalActivityEnum.getView().setTag(c2);
                    this.mTerminalActivityEnum.setMaxDate(Business
                            .getMaxMonth(mTerminalActivityEnum.getMenuCode()));
                    this.mTerminalActivityEnum.setMinDate(Business
                            .getMinMonth(mTerminalActivityEnum.getMenuCode()));
                }
                break;
        }
    }

    private boolean initialDateShow() {
        boolean hasData = false;

        switch (mTerminalActivityEnum.getDateRange()) {
            case DAY:
                String date1 = mTerminalActivityEnum.getMaxDate();
                if (date1 == null || "".equals(date1)) {
                    hasData = false;
                } else {
                    Calendar c1 = DateUtil.getCalendar(date1,
                            DateRangeEnum.DAY.getDateServerPattern());
                    mDateSelect.setText(DateUtil.oneStringToAntherString(date1,
                            DateRangeEnum.DAY.getDateServerPattern(),
                            DateRangeEnum.DAY.getDateShowPattern()));
                    hasData = true;
                }
                break;
            case MONTH:
                String date2 = mTerminalActivityEnum.getMaxDate();
                if (date2 == null || "".equals(date2)) {
                    hasData = false;
                } else {
                    Calendar c2 = DateUtil.getCalendar(date2,
                            DateRangeEnum.MONTH.getDateServerPattern());
                    mDateSelect.setText(DateUtil.oneStringToAntherString(date2,
                            DateRangeEnum.MONTH.getDateServerPattern(),
                            DateRangeEnum.MONTH.getDateShowPattern()));
                    hasData = true;
                }
                break;
        }

        return hasData;
    }

    private void showDatePicker() {
        if (mDateDia != null) {
            mDateDia.dismiss();
            mDateDia = null;
        }
        Calendar c = (Calendar) this.mTerminalActivityEnum.getView().getTag();

        LogUtils.toast(SimpleTerminalActivity.this, Build.VERSION.SDK_INT);

        OnDateSetListener listener = new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                // 两秒之内的操作不能重复
                long current_time = System.currentTimeMillis();
                if (current_time - getTime() < 2000) {
                    return;
                }

                setTime(current_time);

                Calendar c = Calendar.getInstance();
                c.set(year, monthOfYear, dayOfMonth);
                if (mTerminalActivityEnum.isDateValid(c)) {
                    mTerminalActivityEnum.getView().setTag(c);
                    mDateSelect.setText(DateUtil.formatter(c.getTime(),
                            mTerminalActivityEnum.getDateRange()
                                    .getDateShowPattern()));
                    // ResetDateAsynTask rdat = new ResetDateAsynTask(
                    // mRootView, progressDialog);
                    // rdat.execute();
                    new TermLoadDataAsynTask(
                            SimpleTerminalActivity.this)
                            .execute(mTerminalActivityEnum
                                    .getMenuCode());
                } else {
                    Toast.makeText(SimpleTerminalActivity.this,
                            "无该日期数据!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        };

        if (Build.VERSION.SDK_INT >= 24) {
            final Context themedContext = new ContextThemeWrapper(this,
                    // android.R.style.Widget_Material_NumberPicker
                    android.R.style.Theme_Holo_Light_Dialog);
            mDateDia = new FixedHoloDatePickerDialog(themedContext,
                    listener, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH));
        } else
            mDateDia = new MyDatePickerDialog(SimpleTerminalActivity.this,
                    listener, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH));

        mDateDia.setTitle("请选择时间");
        mDateDia.show();
    }

    private void showDatePickerWithoutDay() {
        if (mDateDia != null) {
            mDateDia.dismiss();
        }
        Calendar c = (Calendar) this.mTerminalActivityEnum.getView().getTag();

        LogUtils.toast(SimpleTerminalActivity.this, Build.VERSION.SDK_INT);

        OnDateSetListener listener = new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                // 两秒之内的操作不能重复
                long current_time = System.currentTimeMillis();
                if (current_time - getTime() < 2000) {
                    return;
                }

                setTime(current_time);

                Calendar c = Calendar.getInstance();
                c.set(year, monthOfYear, dayOfMonth);
                if (mTerminalActivityEnum.isDateValid(c)) {
                    mTerminalActivityEnum.getView().setTag(c);
                    mDateSelect.setText(DateUtil.formatter(c.getTime(),
                            mTerminalActivityEnum.getDateRange()
                                    .getDateShowPattern()));
                    // ResetDateAsynTask rdat = new ResetDateAsynTask(
                    // mRootView, progressDialog);
                    // rdat.execute();
                    new TermLoadDataAsynTask(
                            SimpleTerminalActivity.this)
                            .execute(mTerminalActivityEnum
                                    .getMenuCode());
                } else {
                    Toast.makeText(SimpleTerminalActivity.this,
                            "无该日期数据!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        };

        if (Build.VERSION.SDK_INT >= 24) {

            final Context themedContext = new ContextThemeWrapper(this,
                    // android.R.style.Widget_Material_NumberPicker
                    android.R.style.Theme_Holo_Light_Dialog);

            mDateDia = new FixedHoloDatePickerDialog(themedContext,
                    listener, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH));
            ((FixedHoloDatePickerDialog) mDateDia).setHasNoDay(true);
        } else
            mDateDia = new MyDatePickerDialogWithoutDay(SimpleTerminalActivity.this,
                    listener, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH));
        mDateDia.setTitle("请选择时间");
        mDateDia.show();
    }

    public void showTip() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = new ProgressDialog(SimpleTerminalActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("正在加载数据!如果是首次加载,请耐心等待!");
        progressDialog.setIndeterminate(false);
        progressDialog.show();

    }

    /**
     * 木有数据
     */
    public void noDataTip() {
        progressDialog.dismiss();
        Toast.makeText(SimpleTerminalActivity.this, "没有数据!", Toast.LENGTH_SHORT)
                .show();
    }

    public void dismissTip() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    protected void initialBoardScrollView() {
        this.mBoardScrollView = (BoardScrollView) this
                .findViewById(R.id.id_scrollview_board);
        iv_arrow_down = (ImageView) findViewById(R.id.id_arrow_down);
        this.mBoardScrollView
                .setOnBorderListener(new BoardScrollView.OnBorderListener() {

                    @Override
                    public void onTop() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onBottom() {
                        // TODO Auto-generated method stub
                        iv_arrow_down.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onNotBottom() {
                        // TODO Auto-generated method stub
                        iv_arrow_down.setVisibility(View.VISIBLE);
                    }
                });
    }

    protected void initialTop10ScrollView() {
        this.mTop10HorizontalScrollView = (Top10HorizontalScrollView) this
                .findViewById(R.id.id_ranking_view);
        this.iv_arrow_top10 = (ImageView) this
                .findViewById(R.id.id_arrow_top10);
        this.mTop10HorizontalScrollView
                .setOnBorderListener(new Top10HorizontalScrollView.OnBorderListener() {

                    @Override
                    public void onLeft() {
                        // TODO Auto-generated method stub
                        iv_arrow_top10.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onNotBorder() {
                        // TODO Auto-generated method stub
                        iv_arrow_top10.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onRight() {
                        // TODO Auto-generated method stub

                    }

                });
    }

    private synchronized void setTime(long time) {
        this.last_select_time = time;
    }

    private synchronized long getTime() {
        return last_select_time;
    }

    void initArea() {
        areaSelect = (Button) findViewById(R.id.id_area_select);
        areaSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseArea();
            }
        });
        String areaId = new BusinessDao().getUserInfo().get("areaId");
        String areaName = Business.getAreaName(areaId);
        mTerminalActivityEnum.setAreaCode(areaId);
        areaSelect.setText(areaName);
    }

    void loadData() {
        TermLoadDataAsynTask task = new TermLoadDataAsynTask(this);
        task.setFirst(true);
        task.execute(mTerminalActivityEnum.getMenuCode());
    }

    public void setArea(JSONObject json) {
        JSONArray a = json.optJSONArray("areaList");
        if (a == null) {
            areaInfoList = new ArrayList<Map<String, String>>();
            areaNameList = new String[0];
            return;
        }
        areaInfoList = JsonUtil.toDataList(a);
        areaNameList = new String[areaInfoList.size()];
        for (int i = 0; i < areaInfoList.size(); i++) {
            areaNameList[i] = areaInfoList.get(i).get("AREA_NAME");
        }
    }

    protected void chooseArea() {
        UIUtil.showAlertDialog(this, areaNameList,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int which) {
                        String areaId = areaInfoList.get(which)
                                .get("AREA_CODE");
                        mTerminalActivityEnum.setAreaCode(areaId);
                        areaSelect.setText(Business.getAreaName(areaId));
                        new TermLoadDataAsynTask(SimpleTerminalActivity.this)
                                .execute(mTerminalActivityEnum.getMenuCode());
                    }
                });
    }

}
