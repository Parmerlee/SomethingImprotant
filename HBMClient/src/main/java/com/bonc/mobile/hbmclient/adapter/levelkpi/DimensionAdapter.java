/**
 * DimensionAdapter
 */
package com.bonc.mobile.hbmclient.adapter.levelkpi;

import android.content.Context;

/**
 * @author liweigao
 *
 */
public class DimensionAdapter {
	private final float density;

	public final static float RELATION_WIDTH_DP = 50;
	public final static float TREND_WIDTH_DP = 80;
	public final static float KPI_COLUMN_WIDTH_DP = 100;

	public DimensionAdapter(Context c) {
		this.density = c.getResources().getDisplayMetrics().density;
	}

	public int fromDPtoPX(float dp) {
		return (int) (dp * density + 0.5f);
	}
}
