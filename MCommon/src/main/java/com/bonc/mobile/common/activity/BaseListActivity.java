
package com.bonc.mobile.common.activity;

import android.app.ListActivity;
import android.view.View;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.util.UIUtil;

/**
 * @author sunwei
 */
public abstract class BaseListActivity extends ListActivity {
    protected String menuCode;

    public void onClick(View v) {
        if (v.getId() == R.id.id_button_back) {
            finish();
        }
    }

    protected void initBaseData() {
        menuCode = getIntent().getStringExtra(BaseConfigLoader.KEY_MENU_CODE);
    }

    protected void initView() {
        UIUtil.disableScrShot(this);
        UIUtil.setWatermarkImage(this);
    }
}
