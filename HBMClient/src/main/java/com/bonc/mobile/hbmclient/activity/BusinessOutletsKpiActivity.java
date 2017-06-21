/**
 * BusinessOutletsKpiActivity
 */
package com.bonc.mobile.hbmclient.activity;

import android.content.Intent;
import android.os.Bundle;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.state.business_outlets.kpi.HighBOKpiState;
import com.bonc.mobile.hbmclient.state.business_outlets.kpi.IBOKpiState;
import com.bonc.mobile.hbmclient.state.business_outlets.kpi.LowBOKpiState;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.adapter.BusinessOutletsHighAdapter;

/**
 * @author liweigao
 *
 */
public class BusinessOutletsKpiActivity extends BaseActivity {
	private IBOKpiState state;
	private String kpiCode;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.activity.BaseActivity#onCreate(android.os.Bundle
	 * )
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.business_outlets_kpi_activity);
		findViewById(R.id.root).setBackgroundDrawable(
				WatermarkImage.getWatermarkDrawable());
		Intent intent = getIntent();
		this.kpiCode = intent
				.getStringExtra(BusinessOutletsHighAdapter.KEY_KPI_CODE);
		int type = intent.getIntExtra(BusinessOutletsHighAdapter.KEY_TYPE, 0);
		String date = intent
				.getStringExtra(BusinessOutletsHighAdapter.KEY_DATE);
		String areaCode = intent
				.getStringExtra(BusinessOutletsHighAdapter.KEY_AREA_CODE);
		if (type == 0) {
			this.state = new HighBOKpiState(this, date, areaCode);
		} else {
			this.state = new LowBOKpiState(this, type, date, areaCode);
		}
		this.state.create();
	}

	/**
	 * @return the kpiCode
	 */
	public String getKpiCode() {
		return kpiCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.activity.MenuActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		this.state.destroy();
		super.onDestroy();
	}

}
