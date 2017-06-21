package com.bonc.mobile.hbmclient.view;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.GraphicalView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.bonc.mobile.common.kpi.TrendViewBuilder;

/**
 * 
 * @author ZZZ
 *
 */
public class LineTrendView extends LinearLayout {
	TrendViewBuilder builder = new TrendViewBuilder();

	public LineTrendView(Context context) {
		super(context);
	}

	public LineTrendView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GraphicalView getView() {
		return builder.buildView(getContext());
	}

	public void setData(List<Double> trendData) {
		removeAllViews();
		List<String> data = new ArrayList<String>();
		for (Double d : trendData)
			data.add(d + "");
		builder.setData(data);
		addView(getView());
	}
}
