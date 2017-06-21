
package com.bonc.mobile.common.kpi;

import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.activity.BaseDataActivity;
import com.bonc.mobile.common.util.FileUtils;
import com.bonc.mobile.common.util.TextViewUtils;

/***
 * HTML日报界面
 *
 */
public class HtmlReportActivity extends BaseDataActivity {
    protected WebView web_view;
    boolean firstQuery = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_report);
        initView();
        loadData();
    }

    @Override
    protected void initView() {
        super.initView();
        TextViewUtils.setText(this, R.id.title, getIntent().getStringExtra("title"));
        web_view = (WebView) findViewById(R.id.web_view);
        web_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        web_view.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        web_view.setBackgroundColor(0);
        findViewById(R.id.id_share).setVisibility(View.VISIBLE);
        findViewById(R.id.id_share).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtils.shareScreen(HtmlReportActivity.this);
            }
        });
    }

    @Override
    protected void loadData() {
        web_view.loadDataWithBaseURL(null, getIntent().getStringExtra("content"), null, "UTF-8",
                null);
    }
}
