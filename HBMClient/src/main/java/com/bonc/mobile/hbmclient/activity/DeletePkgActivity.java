package com.bonc.mobile.hbmclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.receiver.RePKGreceiver;

public class DeletePkgActivity extends Activity {
	private Button bt_delete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_delete_pkg_activity);

		this.bt_delete = (Button) findViewById(R.id.id_bt_delete_pkg);
		this.bt_delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Uri uri = Uri.parse("package:" + RePKGreceiver.DELETE_PKG_NAME);
				Intent intent = new Intent(Intent.ACTION_DELETE, uri);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// startArrowAni();
		if (MyUtils.doInfilter(DeletePkgActivity.this)) {
			if (!MyUtils.isBackground(this)) {
				MyUtils.startProtal(this);
			}
		}
	}

}
