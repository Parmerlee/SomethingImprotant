package com.bonc.mobile.hbmclient.activity2;

import org.apache.cordova.ConfigXmlParser;
import org.apache.cordova.CordovaInterfaceImpl;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewEngine;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;

import android.os.Bundle;
import android.widget.Toast;

public class BroadBandCordovaActivity extends BaseCordovaActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_cordova);
		// try {
		//
		// systemWebView = (SystemWebView) findViewById(R.id.myWebView);
		// ConfigXmlParser parser = new ConfigXmlParser();
		// parser.parse(this);
		// cordovaWebView = new CordovaWebViewImpl(new SystemWebViewEngine(
		// systemWebView));
		// cordovaWebView.init(new CordovaInterfaceImpl(this),
		// parser.getPluginEntries(), parser.getPreferences());
		//
		// menuCode = this.getIntent().getStringExtra(
		// MenuEntryAdapter.KEY_MENU_CODE);
		// systemWebView.loadUrl(loadUrl());
		// } catch (Exception e) {
		// // TODO: handle exception
		// Toast.makeText(this, "启动失败,请检查手机环境后重试", Toast.LENGTH_LONG);
		// }
		//
		// this.findViewById(android.R.id.content).setBackground(
		// WatermarkImage.getWatermarkDrawable());
		//
		// systemWebView.setAlpha(0.75f);
	}

	// @Override
	// protected void onDestroy() {
	// super.onDestroy();
	// if (cordovaWebView != null) {
	// cordovaWebView.handleDestroy();
	// }
	// }

	// @Override
	// String loadUrl() {
	// return "file:///android_asset/www/index.html";
	// }

}
