package com.bonc.mobile.common.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bonc.mobile.common.R;

public class AppInfoActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_app_info);
		this.renderStaticData();
		this.renderAppInfo();
	}

	private void renderStaticData() {
		this.setWatermarkImage();
		TextView view = (TextView) this.findViewById(R.id.title);
		view.setText("版本信息");
		Button btn = (Button) this.findViewById(R.id.id_button_back);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
	private void renderAppInfo(){
		try {
			PackageInfo pi=this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
			TextView versionNum = (TextView) this.findViewById(R.id.versionNum);
			versionNum.setText(String.valueOf(pi.versionCode));
			TextView buildName = (TextView) this.findViewById(R.id.buildName);
			buildName.setText(pi.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

}
