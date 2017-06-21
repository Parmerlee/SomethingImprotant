package com.bonc.mobile.hbmclient.activity;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.util.LogUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.BarChar4Zoom;

public class BarLandActivity extends BaseActivity {

	private LinearLayout ll_dev_area_bar;

	private List<Map<String, String>> BarData;

	private String unit;
	private double unitDiv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_NoTitleBar);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.cube_land_lay);
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.id_cube_land_lay);
		rl.setBackgroundDrawable(WatermarkImage.getWatermarkLandDrawable());

	}

	@Override
	public void onContentChanged() {
		// TODO Auto-generated method stub
		super.onContentChanged();
		Intent intent = getIntent();
		BarData = (List<Map<String, String>>) intent
				.getSerializableExtra("BarData");
		unit = intent.getStringExtra("unit");
		unitDiv = intent.getDoubleExtra("div", 1);
		initWidget();

	}

	public void initWidget() {

		ll_dev_area_bar = (LinearLayout) findViewById(R.id.ll_dev_area_bar);
		TextView textView = (TextView) findViewById(R.id.tv_w_unit);

		textView.setText(unit);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		BarChar4Zoom barChart = new BarChar4Zoom(BarLandActivity.this, BarData,
				unitDiv);

		View view = barChart.getView();
		ll_dev_area_bar.addView(view);

		ll_dev_area_bar.invalidate();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_HOME) {

			LogUtil.debug("ChangeOr", "变化方向竖屏");

			// BarLandActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			this.finish();
			return true;

		}
		return super.onKeyDown(keyCode, event);
	}

}
