package com.bonc.mobile.hbmclient.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.observer.data_presentation.DataPresentationObservable;
import com.bonc.mobile.hbmclient.observer.data_presentation.DataPresentationObserver;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;

public class DataPresentationActivity extends SlideHolderActivity {
	private DataPresentationObserver mView;
	private DataPresentationObservable mData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String menuCode = getIntent().getStringExtra(
				MenuEntryAdapter.KEY_MENU_CODE);
		setMainContent(menuCode, R.layout.data_presentation_activity);
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.id_parent);
		rl.setBackgroundDrawable(WatermarkImage.getWatermarkDrawable());

		TextView tv_title = (TextView) this
				.findViewById(R.id.logo_word_mon_dev);
		tv_title.setText("数据简报");
		Button navigator = (Button) this.findViewById(R.id.id_navigator);
		navigator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				slideHolder.toggle();
			}
		});

		this.mData = new DataPresentationObservable(menuCode);
		this.mView = new DataPresentationObserver(this.mData, this);
		this.mView.showDefaultView();
	}

}
