/**
 * BulletinView
 */
package com.bonc.mobile.hbmclient.view;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.DailyReportActivity;
import com.bonc.mobile.hbmclient.activity.MenuSecondActivity;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;

/**
 * @author liweigao
 *
 */
public class BulletinView extends RelativeLayout {
	private ImageView mImageView;
	private String menuCode;
	private Activity activity;

	/**
	 * @param context
	 */
	public BulletinView(Activity a, String menuCode) {
		super(a);
		this.activity = a;
		this.menuCode = menuCode;
		initialView();
		addView(this.mImageView);
	}

	private void initialView() {
		this.mImageView = new ImageView(getContext());
		this.mImageView
				.setBackgroundResource(R.mipmap.rectangle_blue_small_daily_report);
		this.mImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getContext(), DailyReportActivity.class);
				i.putExtra(MenuEntryAdapter.KEY_MENU_CODE, menuCode);
				activity.startActivityForResult(i,
						MenuSecondActivity.REQUEST_CODE_FINISH);
			}
		});
	}
}
