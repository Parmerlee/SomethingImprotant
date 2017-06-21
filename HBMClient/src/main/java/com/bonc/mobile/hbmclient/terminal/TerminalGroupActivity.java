/**
 * TODO
 */
package com.bonc.mobile.hbmclient.terminal;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.common.util.FileUtils;
import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.MenuActivity;
import com.bonc.mobile.hbmclient.activity.SimpleTerminalActivity;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.common.Publicapp;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;
import com.bonc.mobile.hbmclient.flyweight.float_view.FloatView;
import com.bonc.mobile.hbmclient.flyweight.float_view.FloatViewFactory;
import com.bonc.mobile.hbmclient.service.BoncService;
import com.bonc.mobile.hbmclient.util.ExitApplication;
import com.bonc.mobile.hbmclient.util.LogoutUtil;
import com.bonc.mobile.hbmclient.util.SMTools;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.DateRangeSwitchView2;
import com.bonc.mobile.hbmclient.view.DateRangeSwitchView2.OnStateChangeListener;
import com.bonc.mobile.hbmclient.view.ScrollViewGroup;
import com.bonc.mobile.hbmclient.view.ScrollViewGroup.OnScreenChanageCallBack;
import com.bonc.mobile.hbmclient.view.SlideHolder;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;



/**
 * @author liweigao 终端运营 父类
 */
@SuppressWarnings("deprecation")
public class TerminalGroupActivity extends ActivityGroup implements
        OnClickListener {
    private Button mPSSButton, mUnsaleButton, mUnpackButton, mFGButton,
            mMarketButton;
    private FloatView mFloatView;
    private TerminalActivityEnum[] mTerminalActivityEnums = {
            TerminalActivityEnum.PSS_DAY_ACTIVITY,
            TerminalActivityEnum.UNSALE_DAY_ACTIVITY,
            TerminalActivityEnum.UNPACK_DAY_ACTIVITY,
            TerminalActivityEnum.FG_MONTH_ACTIVITY,
            TerminalActivityEnum.MARKET_DAY_ACTIVITY};
    private ScrollViewGroup mScrollViewGroup;

    private final int MENU1 = 1;
    private final int MENU2 = 2;
    private final int MENU3 = 3;

    private ScreenStateRecevier mRecevier;
    private ScreenOffRecevier mOffRecevier;
    private TimeOutDialog mTimeUpDia;

    private BusinessDao mBusinessDao = new BusinessDao();

    public SlideHolder slideHolder;
    public ExpandableListView sidemenu;
    DateRangeSwitchView2 date_range_select;
    TerminalActivityEnum cur_tae;

    private LoginRecevier mLoginRecevier;
    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terminal_group_activity_layout);
        this.findViewById(R.id.id_share).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FileUtils.shareScreen(TerminalGroupActivity.this);
                    }
                });
        ExitApplication.getInstance().addActivity(this);
        LinearLayout ll = (LinearLayout) this.findViewById(R.id.id_root_view);
        ll.setBackgroundDrawable(WatermarkImage.getWatermarkDrawable());

        this.mFloatView = FloatViewFactory.getSingleInstance()
                .createFloatView();
        this.mFloatView.showFloatView(getIntent().getStringExtra(
                MenuEntryAdapter.KEY_MENU_CODE));

        TextView tv_grouptitlearea = (TextView) this
                .findViewById(R.id.logo_word_mon_dev);
        tv_grouptitlearea.setText("终端运营");

        mTimeUpDia = new TimeOutDialog(this);
        mRecevier = new ScreenStateRecevier();
        mOffRecevier = new ScreenOffRecevier();

        mLoginRecevier = new LoginRecevier();
        date_range_select = (DateRangeSwitchView2) findViewById(R.id.id_date_range_select);
        intent = new Intent("SimpleTerminalActivity.changeDate");
        date_range_select.setOnStateChangeListener(new OnStateChangeListener() {

            @Override
            public void toStateMonth() {
                switch (cur_tae.getPosition()) {
                    case 1:
                        showActivity(TerminalActivityEnum.PSS_MONTH_ACTIVITY);
                        break;
                    case 2:
                        showActivity(TerminalActivityEnum.UNSALE_MONTH_ACTIVITY);
                        break;
                    case 3:
                        showActivity(TerminalActivityEnum.UNPACK_MONTH_ACTIVITY);
                        break;
                    case 0:
                        intent.putExtra("month", true);
                        LocalBroadcastManager.getInstance(getApplicationContext())
                                .sendBroadcast(intent);
                        // Toast.makeText(getApplicationContext(), "AAAA",
                        // 1).show();
                        // showActivity(TerminalActivityEnum.MARKET_MONTH_ACTIVITY);
                        break;
                }
            }

            @Override
            public void toStateDay() {

                switch (cur_tae.getPosition()) {
                    case 1:
                        showActivity(TerminalActivityEnum.PSS_DAY_ACTIVITY);
                        break;
                    case 2:
                        showActivity(TerminalActivityEnum.UNSALE_DAY_ACTIVITY);
                        break;
                    case 3:
                        showActivity(TerminalActivityEnum.UNPACK_DAY_ACTIVITY);
                        break;

                    case 0:
                        // Toast.makeText(getApplicationContext(), "BBBB",
                        // 1).show();
                        intent.putExtra("month", false);
                        LocalBroadcastManager.getInstance(getApplicationContext())
                                .sendBroadcast(intent);
                        // showActivity(TerminalActivityEnum.MARKET_DAY_ACTIVITY);
                        break;
                }
            }
        });

        mPSSButton = (Button) findViewById(R.id.id_pss_button);
        mUnsaleButton = (Button) findViewById(R.id.id_unsale_button);
        mUnpackButton = (Button) findViewById(R.id.id_unpack_button);
        mFGButton = (Button) findViewById(R.id.id_fg_button);

        mMarketButton = (Button) findViewById(R.id.id_mm_button);

        mScrollViewGroup = (ScrollViewGroup) this
                .findViewById(R.id.id_scroll_viewgroup);

        mPSSButton.setOnClickListener(this);
        mUnsaleButton.setOnClickListener(this);
        mUnpackButton.setOnClickListener(this);
        mFGButton.setOnClickListener(this);
        mMarketButton.setOnClickListener(this);
        /* 添加空白页 ,为了站位 */

        LayoutInflater inflager = LayoutInflater.from(this);
        View view1 = inflager.inflate(R.layout.placeholder_layout, null);
        View view2 = inflager.inflate(R.layout.placeholder_layout, null);
        View view3 = inflager.inflate(R.layout.placeholder_layout, null);
        View view4 = inflager.inflate(R.layout.placeholder_layout, null);
        View view5 = inflager.inflate(R.layout.placeholder_layout, null);

        mScrollViewGroup.addView(view1);
        mScrollViewGroup.addView(view2);
        mScrollViewGroup.addView(view3);
        mScrollViewGroup.addView(view4);
        mScrollViewGroup.addView(view5);

        // 设置回调
        mScrollViewGroup
                .setOnScreenChangeCallBack(new OnScreenChanageCallBack() {

                    @Override
                    public void onScreenChanage(int page) {

                        mPSSButton.setBackgroundColor(0x00ffffff);
                        mPSSButton.setTextColor(getResources().getColor(
                                R.color.terminal_menu_text_up_color));
                        mUnsaleButton.setBackgroundColor(0x00ffffff);
                        mUnsaleButton.setTextColor(getResources().getColor(
                                R.color.terminal_menu_text_up_color));
                        mUnpackButton.setBackgroundColor(0x00ffffff);
                        mUnpackButton.setTextColor(getResources().getColor(
                                R.color.terminal_menu_text_up_color));
                        mFGButton.setBackgroundColor(0x00ffffff);
                        mFGButton.setTextColor(getResources().getColor(
                                R.color.terminal_menu_text_up_color));

                        mMarketButton.setBackgroundColor(0x00ffffff);
                        mMarketButton.setTextColor(getResources().getColor(
                                R.color.terminal_menu_text_up_color));

                        switch (page) {
                            case 1:
                                mPSSButton
                                        .setBackgroundResource(R.mipmap.icon_menu_button_down);
                                mPSSButton.setTextColor(getResources().getColor(
                                        R.color.terminal_menu_text_down_color));
                                break;
                            case 2:
                                mUnsaleButton
                                        .setBackgroundResource(R.mipmap.icon_menu_button_down);
                                mUnsaleButton.setTextColor(getResources().getColor(
                                        R.color.terminal_menu_text_down_color));
                                break;
                            case 3:
                                mUnpackButton
                                        .setBackgroundResource(R.mipmap.icon_menu_button_down);
                                mUnpackButton.setTextColor(getResources().getColor(
                                        R.color.terminal_menu_text_down_color));
                                break;
                            case 4:
                                mFGButton
                                        .setBackgroundResource(R.mipmap.icon_menu_button_down);
                                mFGButton.setTextColor(getResources().getColor(
                                        R.color.terminal_menu_text_down_color));
                                break;

                            case 0:
                                mMarketButton
                                        .setBackgroundResource(R.mipmap.icon_menu_button_down);
                                mMarketButton.setTextColor(getResources().getColor(
                                        R.color.terminal_menu_text_down_color));
                                break;
                        }
                    }

                    /**
                     * 滑动到下一页面，可以获得下一页面的页号page 这里直接模拟按下对应tab
                     */
                    @Override
                    public void glideNext(int page) {

                        switch (page) {
                            case 1:
                                onClick(mPSSButton);
                                break;
                            case 2:
                                onClick(mUnsaleButton);
                                break;
                            case 3:
                                onClick(mUnpackButton);
                                break;
                            case 4:
                                onClick(mFGButton);
                                break;

                            case 0:
                                onClick(mMarketButton);
                                break;
                        }
                    }
                });
        // 默认按下第一个tab
        onClick(mMarketButton);
        initSlideHolder();
        buildSideMenu(getIntent()
                .getStringExtra(MenuEntryAdapter.KEY_MENU_CODE));
        Button navigator = (Button) this.findViewById(R.id.id_navigator);
        navigator.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                slideHolder.toggle();
            }
        });
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        MenuActivity.mTimestamp = System.currentTimeMillis();
        unregisterReceiver(mRecevier);
        unregisterReceiver(mOffRecevier);
        this.mFloatView.hideFloatView();
        super.onStop();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        IntentFilter inf = new IntentFilter();
        MenuActivity.mTimestamp = -1;
        inf.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mRecevier, inf);
        IntentFilter inf2 = new IntentFilter();
        inf2.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mOffRecevier, inf2);
        this.mFloatView.unHideFloatView();
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        long ts = System.currentTimeMillis();
        if (MenuActivity.mTimestamp > 0
                && ts - MenuActivity.mTimestamp > MenuActivity.TIME_OUT_WHILE) {
            mTimeUpDia.setCancelable(false);
//            if (!(mTimeUpDia.isShowing())) {

            mTimeUpDia.show();
            LogoutUtil.timeCounter(TerminalGroupActivity.this, mTimeUpDia);
//            }
        }
    }

    private class LoginRecevier extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            MenuActivity.mTimestamp = System.currentTimeMillis();
            if (mTimeUpDia != null) {
                mTimeUpDia.dismiss();
                mTimeUpDia.hide();

            }

//            String action = arg1.getAction();
//            if (TextUtils.equals(action,"login")){
//                LogUtils.logBySys("AAAAAAAAAAAAAAAAAAAA");
//                Toast.makeText(arg0,"action:"+arg1.getAction(),Toast.LENGTH_LONG).show();
//                if(mTimeUpDia!=null&&mTimeUpDia.isShowing()){
//                    mTimeUpDia.dismiss();
//                }
//            }
        }

    }

    @Override
    public void finish() {
        MenuActivity.mTimestamp = -1;
        super.finish();
    }

    @Override
    public void onBackPressed() {
        if (slideHolder.isOpened()) {
            // 如果侧边栏处于打开状态，点击回退键，先关闭侧边栏
            slideHolder.toggle();
            return;
        }
        super.onBackPressed();
    }

    ;

    private void initSlideHolder() {
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
    }

    private void buildSideMenu(String menuCode) {
        new MenuEntryAdapter(this, sidemenu, menuCode);
    }

    @Override
    public void onClick(View v) {

        mPSSButton.setBackgroundColor(0x00ffffff);
        mPSSButton.setTextColor(getResources().getColor(
                R.color.terminal_menu_text_up_color));
        mUnsaleButton.setBackgroundColor(0x00ffffff);
        mUnsaleButton.setTextColor(getResources().getColor(
                R.color.terminal_menu_text_up_color));
        mUnpackButton.setBackgroundColor(0x00ffffff);
        mUnpackButton.setTextColor(getResources().getColor(
                R.color.terminal_menu_text_up_color));
        mFGButton.setBackgroundColor(0x00ffffff);
        mFGButton.setTextColor(getResources().getColor(
                R.color.terminal_menu_text_up_color));

        mMarketButton.setBackgroundColor(0x00ffffff);
        mMarketButton.setTextColor(getResources().getColor(
                R.color.terminal_menu_text_up_color));

        switch (v.getId()) {
            case R.id.id_pss_button:
                mScrollViewGroup.setDisallowIntercept(false);
                // Toast.makeText(getApplicationContext(), "pss", 1).show();
                mPSSButton.setBackgroundResource(R.mipmap.icon_menu_button_down);
                mPSSButton.setTextColor(getResources().getColor(
                        R.color.terminal_menu_text_down_color));

                showActivity(mTerminalActivityEnums[0]);
                break;
            case R.id.id_unsale_button:
                mScrollViewGroup.setDisallowIntercept(false);
                // Toast.makeText(getApplicationContext(), "unsale", 1).show();
                mUnsaleButton
                        .setBackgroundResource(R.mipmap.icon_menu_button_down);
                mUnsaleButton.setTextColor(getResources().getColor(
                        R.color.terminal_menu_text_down_color));
                showActivity(mTerminalActivityEnums[1]);
                break;
            case R.id.id_unpack_button:
                mScrollViewGroup.setDisallowIntercept(false);
                // Toast.makeText(getApplicationContext(), "unpack", 1).show();
                mUnpackButton
                        .setBackgroundResource(R.mipmap.icon_menu_button_down);
                mUnpackButton.setTextColor(getResources().getColor(
                        R.color.terminal_menu_text_down_color));
                showActivity(mTerminalActivityEnums[2]);
                break;
            case R.id.id_fg_button:
                mScrollViewGroup.setDisallowIntercept(false);
                // Toast.makeText(getApplicationContext(), "fg", 1).show();
                mFGButton.setBackgroundResource(R.mipmap.icon_menu_button_down);
                mFGButton.setTextColor(getResources().getColor(
                        R.color.terminal_menu_text_down_color));
                showActivity(mTerminalActivityEnums[3]);
                break;

            case R.id.id_mm_button:
                // Toast.makeText(getApplicationContext(), "mm", 1).show();
                mScrollViewGroup.setDisallowIntercept(true);
                mMarketButton
                        .setBackgroundResource(R.mipmap.icon_menu_button_down);
                mMarketButton.setTextColor(getResources().getColor(
                        R.color.terminal_menu_text_down_color));
                showActivity(mTerminalActivityEnums[4]);
                break;
        }
        date_range_select.toStateDay();
    }

    public void showActivity(TerminalActivityEnum tae) {
        cur_tae = tae;
        date_range_select
                .setVisibility(tae == TerminalActivityEnum.FG_MONTH_ACTIVITY ? View.INVISIBLE
                        : View.VISIBLE);

        int position = tae.getPosition();
        // Toast.makeText(getApplicationContext(), "positions:" + position, 1)
        // .show();
        View v = tae.getView();
        if (v == null) {
            View window = getLocalActivityManager().startActivity(tae.name(),
                    new Intent(this, tae.getIntentClass())).getDecorView();
            tae.setView(window);
            View childView = mScrollViewGroup.getChildAt(position);
            if (childView == null) {
                mScrollViewGroup.addView(window, position);
            } else {
                mScrollViewGroup.removeViewAt(position);
                mScrollViewGroup.addView(window, position);
            }
        } else {
            View childView = mScrollViewGroup.getChildAt(position);
            if (v.equals(childView)) {

            } else {
                mScrollViewGroup.removeViewAt(position);
                mScrollViewGroup.addView(v, position);
            }
        }
        mScrollViewGroup.moveToView(position);
    }

    public void setTerminalActivityEnum(TerminalActivityEnum tae) {
        this.mTerminalActivityEnums[tae.getPosition()] = tae;
    }

    @Override
    protected void onDestroy() {
        this.mFloatView.removeFloatView();
        mScrollViewGroup.removeAllViews();
        TerminalActivityEnum.clearAllView();
        super.onDestroy();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        menu.clear();
        String notifyFlag = Publicapp.getNotifyFlag();
        String notifyName = null;
        if (notifyFlag.equals(getResources().getString(R.string.notify_close)))
            notifyName = getResources().getString(R.string.notify_open_chinese);
        else
            notifyName = getResources()
                    .getString(R.string.notify_close_chinese);

        menu.add(0, MENU1, 1, R.string.menu3);
        menu.add(0, MENU2, 1, notifyName);
        menu.add(0, MENU3, 1, R.string.exit);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU1:
                // 查看本机信息
                LayoutInflater li = LayoutInflater.from(TerminalGroupActivity.this);
                View dialog_view = li.inflate(R.layout.mobileinfo, null);
                TextView imeiTV = (TextView) dialog_view
                        .findViewById(R.id.imeiValue);
                TextView imsiTV = (TextView) dialog_view
                        .findViewById(R.id.imsiValue);
                SMTools tool = new SMTools(TerminalGroupActivity.this);
                imeiTV.setText(tool.getIMEI());
                imsiTV.setText(tool.getIMSI());
                new AlertDialog.Builder(TerminalGroupActivity.this)
                        .setTitle("本机信息").setIcon(R.mipmap.img_btn_logout_2)
                        .setView(dialog_view).show();
                break;
            case MENU2:
                if (item.getTitle().equals(
                        getResources().getString(R.string.notify_open_chinese)))
                    openNotity();
                else
                    closeNotify();
                break;
            case MENU3:
                // 退出
                openQiutDialog();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void closeNotify() {
        String notify_close = getResources().getString(R.string.notify_close);
        setNotifyFlag(notify_close);
    }

    public void openNotity() {
        getPackageManager().setComponentEnabledSetting(
                new ComponentName(Constant.PKG_NAME,
                        "com.bonc.mobile.hbmclient.receiver.BoncReceiver"),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        String notify_open = getResources().getString(R.string.notify_open);
        setNotifyFlag(notify_open);
    }

    private void setNotifyFlag(String pushFlag) {
        // 获得可编辑对象
        SharedPreferences p = getSharedPreferences("SETTING",
                MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = p.edit();
        editor.putString("notifyFlag", pushFlag);
        editor.commit();
    }

    private void openQiutDialog() {
        new AlertDialog.Builder(this).setTitle("经分系统").setMessage("是否退出经分系统？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogoutUtil.Logout(TerminalGroupActivity.this);

                        // stopService(new Intent(TerminalGroupActivity.this,
                        // BoncService.class));
                        // ExitApplication.getInstance().exit();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private class ScreenStateRecevier extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            onRestart();
        }

    }

    private class ScreenOffRecevier extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            MenuActivity.mTimestamp = System.currentTimeMillis();
        }

    }

    private class TimeOutDialog extends AlertDialog {

        protected TimeOutDialog(Context context) {
            super(context);
            setTitle("登陆超时");
            setCancelable(false);
            setMessage("长时间未操作,登陆已超时,请重新登陆!");
            setButton(AlertDialog.BUTTON_NEUTRAL, "确定", new OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    // callLogin();
                    LogoutUtil.Logout(TerminalGroupActivity.this);
                }
            });
            setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface arg0) {
                    // TODO Auto-generated method stub
                    // callLogin();
                    LogoutUtil.Logout(TerminalGroupActivity.this);
                }
            });
        }

    }
}
