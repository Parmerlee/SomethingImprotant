package com.bonc.mobile.hbmclient.activity;

import android.content.Intent;
import android.os.Bundle;

import com.bonc.mobile.hbmclient.R;

public class KpiTabMainActivity extends BITabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sale_main);
		init();

	}

	@Override
	protected Intent getTabIntent(String code, String type) {
		Intent intent = super.getTabIntent(code, type);
		intent.putExtra("key_menu_code", code);
		intent.putExtra("hide_head", true);
		intent.putExtra("show_second", true);
		return intent;
	}

}
