
package com.bonc.mobile.common.kpi;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.activity.BaseTabActivity;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.view.DMSwitchButton;
import com.bonc.mobile.common.view.DMSwitchButton.OnSwitchListener;

public abstract class KpiPortalActivity extends BaseTabActivity {
    protected String menuCode, queryCode;
    protected List<Map<String, String>> sub_menu;
    protected DMSwitchButton dm_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBaseData();
        initView();
    }

    protected void initBaseData() {
        menuCode = getIntent().getStringExtra(BaseConfigLoader.KEY_MENU_CODE);
        queryCode = getIntent().getStringExtra(BaseConfigLoader.KEY_QUERY_CODE);
    }

    protected void initView() {
        setContentView(R.layout.activity_kpi_portal);
        setWatermarkImage();
        initDMSwitch();
        sub_menu = getConfigLoader().getSecondMenu(menuCode);
        if (sub_menu.size() == 0) {
            sub_menu.add(getConfigLoader().getMenuInfo(menuCode));
        }
        initTab(sub_menu);
        setTitle(sub_menu.get(0).get(BaseConfigLoader.KEY_MENU_CODE));
        if (sub_menu.size() > 1)
            dm_switch.setVisibility(View.VISIBLE);
        if (getConfigLoader().isMenuType(menuCode, BaseConfigLoader.MENU_TYPE_KPI_CUST))
            findViewById(R.id.kpi_mgr).setVisibility(View.VISIBLE);
    }

    protected void initDMSwitch() {
        dm_switch = (DMSwitchButton) findViewById(R.id.dm_switch);
        dm_switch.setOnSwitchListener(new OnSwitchListener() {
            @Override
            public void onSwitch(DMSwitchButton view, int dateType) {
                getTabHost().setCurrentTab(dateType);
                setTitle(sub_menu.get(dateType).get(BaseConfigLoader.KEY_MENU_CODE));
            }
        });
    }

    @Override
    protected int getTabViewLayout() {
        return R.layout.tab_button_2;
    }

    @Override
    protected Intent getTabIntent(String code, String type) {
        Intent intent = new Intent(this, getKpiMainClass());
        intent.putExtra(BaseConfigLoader.KEY_MENU_CODE, code);
        intent.putExtra(BaseConfigLoader.KEY_DATE_TYPE,
                getConfigLoader().getMenuAttr(code, BaseConfigLoader.KEY_DATE_TYPE));
        intent.putExtra(BaseConfigLoader.KEY_QUERY_CODE, queryCode);
        return intent;
    }

    protected abstract Class getKpiMainClass();
}
