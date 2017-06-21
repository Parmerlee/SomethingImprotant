/**
 * TrendLeaf
 */
package com.bonc.mobile.hbmclient.composite.levelkpi;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.bonc.mobile.hbmclient.adapter.levelkpi.DimensionAdapter;
import com.bonc.mobile.hbmclient.util.MenuUtil;
import com.bonc.mobile.hbmclient.view.LineTrendView;

/**
 * @author liweigao
 * 
 *         趋势图控件
 *
 */
public class TrendLeaf extends ALevelKpiLeaf {
	private RelativeLayout containerView;

	/**
	 * @param c
	 */
	public TrendLeaf(Context c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.levelkpi.ALevelKpiLeaf#constructView
	 * ()
	 */
	@Override
	public void constructView() {
		this.containerView = new RelativeLayout(context);
		int pxWidth = this.dimenAdapter
				.fromDPtoPX(DimensionAdapter.TREND_WIDTH_DP);
		this.containerView
				.setLayoutParams(new RelativeLayout.LayoutParams(
						pxWidth, LayoutParams.FILL_PARENT));
		this.containerView.setGravity(Gravity.CENTER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.levelkpi.ALevelKpiLeaf#getView()
	 */
	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return this.containerView;
	}

	public void setTrendData(List<Double> data) {
		if (data == null) {
			data = new ArrayList<Double>();
		}
		LineTrendView lineView = new LineTrendView(context);
		lineView.setData(data);
		this.containerView.removeAllViews();
		this.containerView.addView(lineView.getView());
	}

	// 点击事件
	public void setOnClickListener(final String menuCode, final String optime,
			final String areaCode, final String kpiCode, final String dataType) {
		this.containerView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MenuUtil.startKPITrendActivity(context, menuCode, optime,
						areaCode, kpiCode, dataType);
			}
		});
	}
}
