package com.bonc.mobile.hbmclient.activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.User;
import com.bonc.mobile.common.kpi.HtmlReportActivity;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.util.DES;
import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.StringUtil;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.HttpUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class BIHtmlReportActivity_reflesh extends HtmlReportActivity {
    @Override
    protected void initView() {
        super.initView();


        findViewById(R.id.id_share).setVisibility(View.VISIBLE);

    }

    @Override
    protected void loadData() {

        Map<String, String> m = new HashMap<String, String>();
        m.putAll(User.getInstance().getUserMap());
        m.put("id", this.getIntent().getStringExtra("id"));

        new GetVcodeTask(this).execute("/bi/textReport/dailyContents", m);
    }

    class GetVcodeTask extends HttpRequestTask {
        public GetVcodeTask(Context context) {
            super(context, Constant.BASE_PATH);
        }

        @Override
        protected void handleResult(JSONObject result) {

            try {
                web_view.loadDataWithBaseURL(null, result.optJSONObject("data").getString("HTML_CONTENT"), null, "UTF-8", null);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (MyUtils.doInfilter(this))
            if (!MyUtils.isBackground(this)) {
                MyUtils.startProtal(this);
            }
    }

    @Override
    protected void setWatermarkImage() {
        this.findViewById(android.R.id.content).setBackgroundDrawable(
                WatermarkImage.getWatermarkDrawable());
    }
}
