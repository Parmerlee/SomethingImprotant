package com.bonc.mobile.hbmclient.activity2;

import org.apache.cordova.ConfigXmlParser;
import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaInterfaceImpl;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewEngine;

import android.content.res.AssetManager;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bonc.mobile.common.util.CmdUtil;
import com.bonc.mobile.common.util.FileUtils;
import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.BaseActivity;
import com.bonc.mobile.hbmclient.util.LogUtil;
import com.bonc.mobile.hbmclient.util.LogoutUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;

/**
 * Cordova界面基类
 *
 * @author Lenevo
 */

/**
 * public class BaseCordovaActivity extends BaseActivity {
 * protected SystemWebView systemWebView;
 * protected CordovaWebView cordovaWebView;
 * protected String menuCode;
 * private ImageView img;
 * <p/>
 * private String url;
 *
 * @Override protected void onCreate(Bundle savedInstanceState) {
 * // TODO Auto-generated method stub
 * super.onCreate(savedInstanceState);
 * <p/>
 * setContentView(R.layout.activity_cordova);
 * img = (ImageView) this.findViewById(R.id.cordova_img);
 * try {
 * <p/>
 * systemWebView = (SystemWebView) findViewById(R.id.myWebView);
 * ConfigXmlParser parser = new ConfigXmlParser();
 * parser.parse(this);
 * cordovaWebView = new CordovaWebViewImpl(new SystemWebViewEngine(
 * systemWebView));
 * cordovaWebView.init(new CordovaInterfaceImpl(this),
 * parser.getPluginEntries(), parser.getPreferences());
 * <p/>
 * menuCode = this.getIntent().getStringExtra(
 * MenuEntryAdapter.KEY_MENU_CODE);
 * LogUtils.debug(getClass(), menuCode);
 * // systemWebView.loadUrl(loadUrl());
 * } catch (Exception e) {
 * // TODO: handle exception
 * e.printStackTrace();
 * Toast.makeText(this, "启动失败,请检查手机环境后重试", Toast.LENGTH_LONG).show();
 * }
 * <p/>
 * //        LogUtils.toast(this, "is hua wei:" + CmdUtil.isHuaWei());
 * //
 * if (!(CmdUtil.isHuaWei())
 * //                || CmdUtil.isLianXiang()
 * ) {
 * systemWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, new Paint());
 * }
 * <p/>
 * switch (Integer.valueOf(menuCode)) {
 * <p/>
 * // 政企
 * case 40001100:
 * url = "file:///android_asset/www/index1.html";
 * break;
 * <p/>
 * // 家宽
 * case 50001100:
 * <p/>
 * url = "file:///android_asset/www/index.html";
 * // url = "file:///android_asset/www/index2.html";
 * break;
 * default:
 * break;
 * }
 * <p/>
 * <p/>
 * systemWebView.loadUrl(url);
 * <p/>
 * //        Toast.makeText(this, "BBBBBB", Toast.LENGTH_SHORT).show();
 * <p/>
 * <p/>
 * // WatermarkImage.setWatermarkDrawable(this, "4A:"
 * // + User.getInstance().getUserCode());
 * <p/>
 * systemWebView.setAlpha(0.75f);
 * this.findViewById(android.R.id.content).setBackground(
 * WatermarkImage.getWatermarkDrawable());
 * }
 * @Override protected void onDestroy() {
 * super.onDestroy();
 * if (cordovaWebView != null) {
 * cordovaWebView.handleDestroy();
 * }
 * }
 * @Override protected void onResume() {
 * // TODO Auto-generated method stub
 * super.onResume();
 * <p/>
 * if (MyUtils.doInfilter(this)) {
 * if (!MyUtils.isBackground(this)) {
 * MyUtils.startProtal(this);
 * LogoutUtil.Logout(BaseCordovaActivity.this);
 * }
 * }
 * }
 * <p/>
 * public WebView getView() {
 * systemWebView.destroyDrawingCache();
 * systemWebView.setDrawingCacheEnabled(true);
 * systemWebView.buildDrawingCache();
 * return systemWebView;
 * }
 * @Override public void onBackPressed() {
 * // TODO Auto-generated method stub
 * super.onBackPressed();
 * BaseCordovaActivity.this.finish();
 * }
 * <p/>
 * // abstract String loadUrl();
 * }***
 */

public class BaseCordovaActivity extends CordovaActivity {

    protected String menuCode;

    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set by <content src="index.html" /> in config.xml
        menuCode = this.getIntent().getStringExtra(
                MenuEntryAdapter.KEY_MENU_CODE);

        switch (Integer.valueOf(menuCode)) {

            // 政企
            case 40001100:
                url = "file:///android_asset/www/index1.html";
                break;

            // 家宽
            case 50001100:

                url = "file:///android_asset/www/index.html";
                // url = "file:///android_asset/www/index2.html";
                break;
            default:
                break;
        }


        loadUrl(url);

        this.appView.getView().setAlpha(0.75f);
        this.findViewById(android.R.id.content).setBackgroundDrawable(
                WatermarkImage.getWatermarkDrawable());

    }


    public WebView getView() {
        return new WebView(this);
    }

}
