
package com.bonc.mobile.common.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.util.TextViewUtils;

public class SettingActivity extends BaseListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initBaseData();
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        TextViewUtils.setText(this, R.id.title, "系统设置");
        List<Map<String, String>> l = new ArrayList<Map<String, String>>();
        String[] l1 = new String[] {
                "个人信息", "版本信息", "退出登录"
        };
        String[] l2 = new String[] {
                R.mipmap.icon_userinfo + "", R.mipmap.icon_appinfo + "", R.mipmap.icon_exit + ""
        };
        for (int i = 0; i < l1.length; i++) {
            Map<String, String> m = new HashMap<String, String>();
            m.put("name", l1[i]);
            m.put("icon", l2[i]);
            l.add(m);
        }
        setListAdapter(new SimpleAdapter(this, l, R.layout.menu_list_item, new String[] {
                "name", "icon"
        }, new int[] {
                R.id.text, R.id.icon
        }));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent;
        if (position == 0)
            intent = new Intent(this, UserInfoActivity.class);
        else if(position == 1)
            intent = new Intent(this, AppInfoActivity.class);
        else{
            setResult(RESULT_OK);
            finish();
            return;
        }
        startActivity(intent);
    }
}
