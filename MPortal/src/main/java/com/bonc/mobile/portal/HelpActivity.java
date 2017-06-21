package com.bonc.mobile.portal;

import android.os.Bundle;

import com.bonc.mobile.common.util.FileUtils;
import com.bonc.mobile.common.util.UIUtil;

import org.apache.cordova.CordovaActivity;



/***
public class HelpActivity extends BaseActivity  {

    protected SystemWebView systemWebView;
    protected CordovaWebView cordovaWebView;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        try {

            systemWebView = (SystemWebView) findViewById(R.id.web_view);
            ConfigXmlParser parser = new ConfigXmlParser();
            parser.parse(this);
            cordovaWebView = new CordovaWebViewImpl(new SystemWebViewEngine(
                    systemWebView));
            cordovaWebView.init(new CordovaInterfaceImpl(this),
                    parser.getPluginEntries(), parser.getPreferences());


            // systemWebView.loadUrl(loadUrl());
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(this, "启动失败,请检查手机环境后重试", Toast.LENGTH_LONG).show();
        }

        url = "file:///android_asset/www/index.html";
    }

    @Override
    protected void onResume() {
        super.onResume();
        systemWebView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(systemWebView!=null){
            systemWebView.destroy();
        }
    }
}
******/


public class HelpActivity extends CordovaActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Set by <content src="index.html" /> in config.xml
        loadUrl(launchUrl);


        this.appView.getView().setAlpha(0.75f);
        UIUtil.setWatermarkImage(this,android.R.id.content);

        this.appView.getView().setPadding(0,0,0,10);
//        this.appView.getView().setBackgroundDrawable( FileUtils.getWatermark(this, "watermark"));
    }


}


