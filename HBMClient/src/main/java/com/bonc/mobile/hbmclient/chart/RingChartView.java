package com.bonc.mobile.hbmclient.chart;

import org.achartengine.ChartFactory;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author sunwei
 */
public class RingChartView extends PieChartView {

	public RingChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RingChartView(Context context) {
		super(context);
	}

	@Override
	protected View getView() {
		buildDataset();
		buildRenderer();
		return ChartFactory.getDoughnutChartView(getContext(), dataset,
				renderer);
	}

	@Override
	protected void buildRenderer() {
		super.buildRenderer();
	}

}
