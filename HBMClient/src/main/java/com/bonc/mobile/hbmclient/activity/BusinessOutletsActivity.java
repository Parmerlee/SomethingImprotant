/**
 * BusinessOutletsActivity
 */
package com.bonc.mobile.hbmclient.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.state.business_outlets.BusinessOutletsViewSwitcher;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;

/**
 * @author liweigao
 *
 */
public class BusinessOutletsActivity extends SlideHolderActivity {
	private BusinessOutletsViewSwitcher viewSwitcher;
	private TextView infoSummary;
	private TextView focusWebsite;
	private boolean clickedInfoSummary = true;
	private BusinessDao dao = new BusinessDao();
	private Context context;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.activity.SlideHolderActivity#onCreate(android
	 * .os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String menuCode = getIntent().getStringExtra(
				MenuEntryAdapter.KEY_MENU_CODE);
		setMainContent(menuCode, R.layout.business_outlets_activity);

		findViewById(R.id.id_parent).setBackgroundDrawable(
				WatermarkImage.getWatermarkDrawable());
		context = this;
		TextView title = (TextView) this.findViewById(R.id.logo_word_mon_dev);
		String menuName = dao.getMenuName(menuCode);
		title.setText(menuName);
		RelativeLayout rl = (RelativeLayout) this
				.findViewById(R.id.id_mainkpi_title);
		LayoutParams lp = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp.addRule(RelativeLayout.CENTER_VERTICAL);
		Button kpiExplain = new Button(context);
		kpiExplain.setBackgroundResource(R.mipmap.ic_kpi_explain);
		rl.addView(kpiExplain, lp);
		kpiExplain.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						BusinessOutletsKpiExplainActivity.class);
				startActivity(intent);
			}
		});

		this.viewSwitcher = (BusinessOutletsViewSwitcher) this
				.findViewById(R.id.viewSwitcher);
		this.infoSummary = (TextView) this.findViewById(R.id.infoSummary);
		this.focusWebsite = (TextView) this.findViewById(R.id.focusWebsite);
		focusWebsite.setTextColor(context.getResources()
				.getColor(R.color.black));
		infoSummary.setTextColor(context.getResources().getColor(
				R.color.ffffffff));
		this.infoSummary.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (clickedInfoSummary) {

				} else {
					infoSummary
							.setBackgroundResource(R.mipmap.business_outlets_type_unfocus);
					focusWebsite
							.setBackgroundResource(R.mipmap.business_outlets_type_focus);
					focusWebsite.setTextColor(context.getResources().getColor(
							R.color.black));
					infoSummary.setTextColor(context.getResources().getColor(
							R.color.ffffffff));
					viewSwitcher.showPrevious();
					clickedInfoSummary = true;
				}
			}
		});

		this.focusWebsite.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (clickedInfoSummary) {
					infoSummary
							.setBackgroundResource(R.mipmap.business_outlets_type_focus);
					focusWebsite
							.setBackgroundResource(R.mipmap.business_outlets_type_unfocus);
					focusWebsite.setTextColor(context.getResources().getColor(
							R.color.ffffffff));
					infoSummary.setTextColor(context.getResources().getColor(
							R.color.black));
					viewSwitcher.showNext();
					clickedInfoSummary = false;
				} else {

				}

			}
		});

		this.viewSwitcher.initialSwitcherView(menuCode);
		Button navigator = (Button) this.findViewById(R.id.id_navigator);
		navigator.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				slideHolder.toggle();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == NetPointActivity.REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				this.viewSwitcher.onActivityResult(resultCode);
			} else {

			}
		}
	}

}
