/**
 * MainKpiActivity
 */
package com.bonc.mobile.hbmclient.activity;

import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.asyn_task.DailyReportAsynTask;
import com.bonc.mobile.hbmclient.asyn_task.NoticeAsynTask;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.data.SQLHelper;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;

import common.share.lwg.util.state.IState;
import common.share.lwg.util.state.mainkpi.MainKpiDay;
import common.share.lwg.util.state.mainkpi.MainKpiDaySwitch;
import common.share.lwg.util.state.mainkpi.MainKpiMonth;
import common.share.lwg.util.state.mainkpi.MainKpiMonthSwitch;

/**
 * @author liweigao
 */
public class MainKpiActivity extends SlideHolderActivity {
    private IState state, stateDay, stateMonth, stateDaySwitch,
            stateMonthSwitch;
    private BusinessDao dao = new BusinessDao();
    private final String KEY_DATE_TYPE = "date_type";
    private final String KEY_MENU_CODE = "MENU_CODE";

    public boolean hasNoDay = false;

    /*
     * (non-Javadoc)
     *
     * @see
     * com.bonc.mobile.hbmclient.activity.SlideHolderActivity#onCreate(android
     * .os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (!AppConstant.SEC_ENH) {

                Log.d("AAA", "the parent activity is:"
                        + this.getParent().getClass().getName());
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        arbitrateState();
        if (this.state != null) {
            this.state.enter();
        } else {
            // 刪除db目录
            new SQLHelper().deleteDbIfExists();

            Toast.makeText(this, "菜单配置错误", Toast.LENGTH_SHORT).show();
        }

        // 清空kpiCodeLocation sp
        DataUtil.saveSetting(
                PreferenceManager.getDefaultSharedPreferences(this),
                "kpiCodeLocation", -1);

        if (this.getParent() != null
                && TextUtils
                .equals(this.getParent().getClass().getName()
                                .toString(),
                        "com.bonc.mobile.hbmclient.activity.KpiTabMainActivity")) {

            // BITabActivity类专用
            initListener();
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (com.bonc.mobile.hbmclient.util.UIUtil.isNeedNew(getMenuCode())) {

            UIUtil.showGuideWindow(this, findViewById(android.R.id.content), "guide.spring",
                    new int[]{R.mipmap.android1, R.mipmap.android2, R.mipmap.android3});
        }
    }

    private void arbitrateState() {
        Intent intent = getIntent();
        String firstLevelMenuCode = intent
                .getStringExtra(MenuEntryAdapter.KEY_MENU_CODE);
        List<Map<String, String>> secondMenuType = dao
                .getDateType(firstLevelMenuCode);

        String type = null;
        int size = secondMenuType.size();
        if (size == 1) {
            type = secondMenuType.get(0).get(KEY_DATE_TYPE);
            String code = secondMenuType.get(0).get(KEY_MENU_CODE);
            if (DateRangeEnum.DAY.getDateFlag().equals(type)) {
                this.stateDay = new MainKpiDay(this, code);
                this.state = this.stateDay;
                hasNoDay = false;
            } else if (DateRangeEnum.MONTH.getDateFlag().equals(type)) {
                this.stateMonth = new MainKpiMonth(this, code);
                this.state = this.stateMonth;
                hasNoDay = true;
            } else {

            }
        } else if (size == 2) {
            type = secondMenuType.get(0).get(KEY_DATE_TYPE);
            String codeDay = null;
            String codeMonth = null;
            if (DateRangeEnum.DAY.getDateFlag().equals(type)) {
                codeDay = secondMenuType.get(0).get(KEY_MENU_CODE);
                codeMonth = secondMenuType.get(1).get(KEY_MENU_CODE);
                hasNoDay = false;
            } else if (DateRangeEnum.MONTH.getDateFlag().equals(type)) {
                codeMonth = secondMenuType.get(0).get(KEY_MENU_CODE);
                codeDay = secondMenuType.get(1).get(KEY_MENU_CODE);
                hasNoDay = true;
            } else {

            }
            // 切换日 月
            this.stateDaySwitch = new MainKpiDaySwitch(this, codeDay);
            this.stateMonthSwitch = new MainKpiMonthSwitch(this, codeMonth);
            this.state = this.stateDaySwitch;
        } else {

        }

    }

    /**
     * @return the stateDaySwitch
     */
    public IState getStateDaySwitch() {
        return stateDaySwitch;
    }

    /**
     * @return the stateMonthSwitch
     */
    public IState getStateMonthSwitch() {
        return stateMonthSwitch;
    }

    /**
     * @param state the state to set
     */
    public void setState(IState state) {
        // System.out.println("state:" + state);
        if (state == null)
            return;
        if (state == getStateMonthSwitch()) {
            hasNoDay = true;
        } else {
            hasNoDay = false;
        }
        this.state = state;
        this.state.enter();
        // 清空kpiCodeLocation sp
        DataUtil.saveSetting(
                PreferenceManager.getDefaultSharedPreferences(this),
                "kpiCodeLocation", -1);
    }

    private void initListener() {
        // TODO Auto-generated method stub
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(new BroadcastReceiver() {

                    @Override
                    public void onReceive(Context context, Intent intent) {
                        if (!TextUtils.equals(
                                MainKpiActivity.this.getIntent()
                                        .getStringExtra(
                                                MenuEntryAdapter.KEY_MENU_CODE),
                                intent.getStringExtra("menucode"))) {
                            return;
                        }
                        String status = intent.getStringExtra("ststus");
                        if (TextUtils.equals("toDay", status)) {
                            MainKpiActivity.this.setState(MainKpiActivity.this
                                    .getStateDaySwitch());
                        } else {
                            MainKpiActivity.this.setState(MainKpiActivity.this
                                    .getStateMonthSwitch());
                        }
                    }
                }, new IntentFilter("BITabActivity.mSwitchView.onCkick"));

    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onActivityResult(int, int,
     * android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MenuSecondActivity.REQUEST_CODE_FINISH
                && resultCode == RESULT_OK) {
            finish();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.bonc.mobile.hbmclient.activity.MenuActivity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        if (DailyReportAsynTask.mPopupWindow != null
                && DailyReportAsynTask.mPopupWindow.isShowing()) {
            DailyReportAsynTask.mPopupWindow.dismiss();
            DailyReportAsynTask.mPopupWindow = null;
        }

        if (NoticeAsynTask.mPopupWindow != null
                && NoticeAsynTask.mPopupWindow.isShowing()) {
            NoticeAsynTask.mPopupWindow.dismiss();
            NoticeAsynTask.mPopupWindow = null;
        }
        super.onDestroy();
    }

    public String getMenuCode() {
        return this.getIntent().getStringExtra(MenuEntryAdapter.KEY_MENU_CODE);
    }
}
