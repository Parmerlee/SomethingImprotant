package com.bonc.mobile.hbmclient.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.bonc.mobile.common.activity.BaseTabActivity;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.util.FileUtils;
import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.data.SQLHelper;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.DateRangeSwitchView2;
import com.bonc.mobile.hbmclient.view.DateRangeSwitchView3;
import com.bonc.mobile.hbmclient.view.SlideHolder;
import com.bonc.mobile.hbmclient.view.DateRangeSwitchView3.OnStateChangeListener;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;
import com.bonc.mobile.saleclient.common.ConfigLoader;

import common.share.lwg.util.state.IState;
import common.share.lwg.util.state.mainkpi.MainKpiDay;
import common.share.lwg.util.state.mainkpi.MainKpiDaySwitch;
import common.share.lwg.util.state.mainkpi.MainKpiMonth;
import common.share.lwg.util.state.mainkpi.MainKpiMonthSwitch;

public class BITabActivity extends BaseTabActivity implements
        OnStateChangeListener {
    protected String menuCode;
    protected BusinessDao business = new BusinessDao();

    private BusinessDao dao = new BusinessDao();
    private final String KEY_DATE_TYPE = "date_type";
    private final String KEY_MENU_CODE = "MENU_CODE";

    private DateRangeSwitchView3 mSwitchView;
    private Map<String, String> map = new HashMap<String, String>();

    protected void init() {
        findViewById(R.id.slideHolder).setBackgroundDrawable(
                WatermarkImage.getWatermarkDrawable());
        // findViewById(R.id.id_button_logo).setVisibility(View.VISIBLE);
        menuCode = getIntent().getStringExtra("key_menu_code");
        TextViewUtils.setText(this, R.id.logo_word_mon_dev,
                business.getMenuName(menuCode));
        initSlideHolder();
        String sql = "select menu_code as MENU_CODE, MENU_NAME as MENU_NAME, menu_type as MENU_TYPE, PARENT_MENU_CODE as PARENT_MENU_CODE from MENU_INFO order by cast(menu_order as number) asc";
        ConfigLoader.getInstance(this).loadMenu(
                new SQLHelper().queryForList(sql, null));
        initTab(getConfigLoader().getSecondMenu(menuCode));

        List<Map<String, String>> secondMenuType = dao.getDateType(menuCode);
        // System.out.println("mennType:" + secondMenuType.toString());
        setDefualtSwitchView();
        // 首先判断第一个的日/月类型
        try {
            menuCode = secondMenuType.get(0).get(KEY_MENU_CODE);
        } catch (Exception e) {
            Toast.makeText(this, "无菜单或菜单配置错误", Toast.LENGTH_SHORT).show();
        }
        arbitrateState(menuCode);
    }

    private void setDefualtSwitchView() {
        this.mSwitchView = new DateRangeSwitchView3(this,
                DateRangeSwitchView3.day);
        mSwitchView.setOnStateChangeListener(null);
        this.mSwitchView.setOnStateChangeListener(BITabActivity.this);
        RelativeLayout rl = (RelativeLayout) this
                .findViewById(R.id.id_mainkpi_title);
        LayoutParams lp = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        rl.addView(mSwitchView, lp);

    }

    @Override
    public void onTabChanged(String tabId) {
        // TODO Auto-generated method stub
        super.onTabChanged(tabId);

        menuCode = tabId;
        arbitrateState(tabId);
    }

    private void arbitrateState(String menuCode) {

        try {

            if (map.get(menuCode).equals("M")) {
                mSwitchView.toStateMonth();
            }
        } catch (Exception e) {
            mSwitchView.toStateDay();
        }

        List<Map<String, String>> secondMenuType = dao.getDateType(menuCode);

        int size = secondMenuType.size();
        if (size == 2) {
            mSwitchView.setVisibility(View.VISIBLE);

        } else {
            this.mSwitchView.setVisibility(View.INVISIBLE);
        }

    }

    public SlideHolder slideHolder;
    public ExpandableListView sidemenu;

    protected void initSlideHolder() {
        // TODO Auto-generated method stub
        slideHolder = (SlideHolder) findViewById(R.id.slideHolder);
        slideHolder.setEnabled(false);
        sidemenu = (ExpandableListView) findViewById(R.id.id_sidemenu);
        this.sidemenu.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true;
            }
        });
        findViewById(R.id.id_navigator).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        slideHolder.toggle();
                    }
                });
        new MenuEntryAdapter(this, sidemenu, menuCode);
    }

    @Override
    protected BaseConfigLoader getConfigLoader() {
        return ConfigLoader.getInstance(this);
    }

    @Override
    protected int getTabViewLayout() {
        return R.layout.tab_button_3;
    }



    @Override
    public void toStateDay() {


        map.put(menuCode, "D");
        Intent intnet = new Intent("BITabActivity.mSwitchView.onCkick");
        intnet.putExtra("ststus", "toDay");
        intnet.putExtra("menucode", menuCode);
        LocalBroadcastManager.getInstance(BITabActivity.this).sendBroadcast(
                intnet);

        reflashTitle("toDay");
    }

    @Override
    public void toStateMonth() {


        map.put(menuCode, "M");
        Intent intnet = new Intent("BITabActivity.mSwitchView.onCkick");
        intnet.putExtra("ststus", "toMonth");
        intnet.putExtra("menucode", menuCode);
        LocalBroadcastManager.getInstance(BITabActivity.this).sendBroadcast(
                intnet);

        reflashTitle("toMonth");

    }

    private void reflashTitle(String str) {
//        if (TextUtils.equals("toDay", str)) {
//            this.mSwitchView.toStateDay();
//        } else {
//            this.mSwitchView.toStateMonth();
//        }

    }
}
