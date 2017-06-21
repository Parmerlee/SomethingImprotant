
package com.bonc.mobile.common.activity;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.util.DateUtil;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.common.util.UIUtil;

/**
 * @author sunwei
 */
public abstract class LoginStatActivity extends BaseListDataActivity {
    Button termSel;
    String[] termList = new String[] {
            "android", "ios"
    };
    String termType = termList[0];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_stat);
        initBaseData();
        initView();
        loadData();
    }

    @Override
    protected void resetView() {
        super.resetView();
        TextViewUtils.setText(this, R.id.data1, "--");
        TextViewUtils.setText(this, R.id.data2, "--");
        TextViewUtils.setText(this, R.id.data3, "--");
        TextViewUtils.setText(this, R.id.data4, "--");
        TextViewUtils.setText(this, R.id.data5, "--");
        String time = DateUtil.formatter(date_button.getDate(), DateUtil.PATTERN_MODEL2_10);
        TextViewUtils.setText(this, R.id.data6, "截止" + time + "的登录统计");
        TextViewUtils.setText(this, R.id.time, time);
    }

    @Override
    protected void initView() {
        super.initView();
        TextViewUtils.setText(this, R.id.title, "登录统计");
        date_button.setDate(Calendar.getInstance().getTime());
        termSel = (Button) findViewById(R.id.id_term_select);
        termSel.setText(termType);
        termSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTerm();
            }
        });
    }

    @Override
    protected void loadData() {
        resetView();
        Map<String, String> param = new LinkedHashMap<String, String>();
        param.put("datatime", DateUtil.formatter(date_button.getDate(), DateUtil.PATTERN_8));
        param.put("termtype", "");
        new LoadDataTask(this).execute(getAction(), param);
    }

    protected abstract String getAction();
    
    @Override
    protected void bindData(JSONObject json) {
        if (!json.has("dataall")) {
            showToast(getString(R.string.no_data));
            return;
        }
        JSONObject dataall = json.optJSONObject("dataall");
        TextViewUtils.setText(this, R.id.data1, dataall.optString("tda"));
        TextViewUtils.setText(this, R.id.data2, dataall.optString("tdp"));
        TextViewUtils.setText(this, R.id.data3, dataall.optString("tdn"));
        TextViewUtils.setText(this, R.id.data4, dataall.optString("dp"));
        TextViewUtils.setText(this, R.id.data5, dataall.optString("dn"));
        List<Map<String, String>> logdetallist = JsonUtil.toList(JsonUtil.optJSONArray(json,"logdetallist"));
        mList.setAdapter(new SimpleAdapter(this, logdetallist, R.layout.login_stat_item,
                new String[] {
                        "user_self_area_name", "user_name", "login_count", "term_type"
                }, new int[] {
                        R.id.depart, R.id.name, R.id.count, R.id.type
                }));
    }

    void chooseTerm() {
        UIUtil.showAlertDialog(this, "选择终端类型", termList, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                termType = termList[which];
                termSel.setText(termType);
                loadData();
            }
        });
    }
}
