/**
 * KpiLeaf
 */
package com.bonc.mobile.hbmclient.composite.levelkpi;

import java.util.Map;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.bonc.mobile.hbmclient.adapter.levelkpi.DimensionAdapter;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter2;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter2.ColumnDisplyInfo;
import com.bonc.mobile.hbmclient.util.MenuUtil;
import com.bonc.mobile.hbmclient.util.ViewUtil;
import com.bonc.mobile.hbmclient.view.KpiListRightItemVIew;

/**
 * @author liweigao
 *
 */
public class KpiLeaf extends ALevelKpiLeaf {
	private String colKey;
	private ColumnDisplyInfo cdi;
	private ColumnDataFilter2 cdf = ColumnDataFilter2.getInstance();
	private ViewUtil mViewUtil = ViewUtil.getSingleInstance();
	private RelativeLayout containerView;
	private KpiListRightItemVIew contentView;

	/**
	 * @param c
	 */
	public KpiLeaf(Context c, String key) {
		super(c);
		this.colKey = key;
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
				.fromDPtoPX(DimensionAdapter.KPI_COLUMN_WIDTH_DP);
		this.containerView
				.setLayoutParams(new LayoutParams(
						pxWidth, LayoutParams.FILL_PARENT));
		this.containerView.setGravity(Gravity.CENTER);

		this.contentView = new KpiListRightItemVIew(context);
		this.containerView.addView(contentView);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.levelkpi.ALevelKpiLeaf#getView()
	 */
	@Override
	public View getView() {
		return this.containerView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.levelkpi.ALevelKpiLeaf#setData(java
	 * .util.Map)
	 */
	@Override
	public void setData(Map<String, String> data) {
		String content = null;
		String unit = null;
		int content_color = 0;
		try {
			String value = data.get(colKey);
			String key_rule = this.colKey + "_rule";
			String key_unit = this.colKey + "_unit";

			String kpiRule = data.get(key_rule);
			String kpiUnit = data.get(key_unit);
			cdi = cdf.doFilter(kpiRule, kpiUnit, value);
			content = cdi.getValue();
			unit = cdi.getUnit();
			content_color = cdi.getColor();
		} catch (Exception e) {
			content = "--";
			unit = "--";
		} finally {
			this.contentView.setContentText(content);
			this.contentView.setUnitText(unit);
			if (content_color != 0) {
				this.contentView.setContentColorID(content_color);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.levelkpi.ALevelKpiLeaf#setFirstLevelStyle
	 * () 一级列表style
	 */
	@Override
	public void setFirstLevelStyle() {
		mViewUtil.setKLRIVStyle1(contentView);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.levelkpi.ALevelKpiLeaf#
	 * setSecondLevelStyle() 二级列表style
	 */
	@Override
	public void setSecondLevelStyle() {
		mViewUtil.setKLRIVStyle2(contentView);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.levelkpi.ALevelKpiLeaf#setMainKpiStyle
	 * ()
	 */
	@Override
	public void setMainKpiStyle() {
		mViewUtil.setStyleCUMainKpi(contentView);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.levelkpi.ALevelKpiLeaf#
	 * setRelationKpiStyle()
	 */
	@Override
	public void setRelationKpiStyle() {
		mViewUtil.setStyleCURelative(contentView);
	}

	public void setOnClickListener(final String menuCode, final String optime,
			final String areaCode, final String kpiCode, final String dataType) {
		this.containerView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MenuUtil.startKPITimeActivity(context, menuCode, optime,
						areaCode, kpiCode, dataType);
			}
		});
	}

}
