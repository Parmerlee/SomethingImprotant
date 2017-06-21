
package com.bonc.mobile.common.activity;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.view.adapter.MenuListAdapter;

/**
 * @author sunwei
 */
public abstract class MenuListActivity extends BaseListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);
        initBaseData();
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        List<Map<String, String>> menuList = getConfigLoader().getSecondMenu(menuCode);
        SimpleAdapter adapter = new MenuListAdapter(this, menuList, R.layout.menu_list_item,
                new String[] {
                        BaseConfigLoader.KEY_MENU_NAME, BaseConfigLoader.KEY_MENU_ICON
                }, new int[] {
                        R.id.text, R.id.icon
                }, getConfigLoader());
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map<String, String> m = (Map<String, String>) getListAdapter().getItem(position);
        Intent intent = getConfigLoader()
                .getMenuIntent(this, m.get(BaseConfigLoader.KEY_MENU_TYPE));
        intent.putExtra(BaseConfigLoader.KEY_MENU_CODE, m.get(BaseConfigLoader.KEY_MENU_CODE));
        startActivity(intent);
    }

    protected abstract BaseConfigLoader getConfigLoader();
}
