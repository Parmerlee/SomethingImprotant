
package com.bonc.mobile.common.activity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.os.Bundle;
import android.widget.SimpleAdapter;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.TextViewUtils;

public class LoginDetailActivity extends BaseListDataActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_detail);
        initBaseData();
        initView();
        loadData();
    }

    @Override
    protected void initView() {
        super.initView();
        TextViewUtils.setText(this, R.id.title, "登录统计");
    }
    
    @Override
    protected void loadData() {
        Map<String, String> param = new LinkedHashMap<String, String>();
        param.put("optime", getIntent().getStringExtra("optime"));
        param.put("queryCode", getIntent().getStringExtra("queryCode"));
        LoadDataTask t=new LoadDataTask(this);
        t.setRetAsArray(true);
        t.execute("/sysResource/loginDetail", param);
    }

    @Override
    protected void bindData(JSONArray result) {
        List<Map<String, String>> data = JsonUtil.toList(result);
        mList.setAdapter(new SimpleAdapter(this, data, R.layout.login_detail_item, new String[] {
                "USER_CODE", "APPTYPE", "TERM_TYPE", "OPTIME"
        }, new int[] {
                R.id.data1, R.id.data2, R.id.data3, R.id.data4
        }));
    }

}
