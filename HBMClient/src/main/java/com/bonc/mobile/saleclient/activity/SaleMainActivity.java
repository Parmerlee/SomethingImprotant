package com.bonc.mobile.saleclient.activity;

import android.os.Bundle;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.BITabActivity;

public class SaleMainActivity extends BITabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sale_main);
		init();
	}
}
