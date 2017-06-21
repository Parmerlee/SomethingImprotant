/**
 * TitleBuilder
 */
package com.bonc.mobile.hbmclient.levelkpi;

import android.content.Context;

import com.bonc.mobile.hbmclient.visitor.IConfig;

/**
 * @author liweigao 只控制流程，不要控制某些参数
 */
public class TitleBuilder implements LinearBuilder<TitleProduct> {
	private TitleProduct product;

	public TitleBuilder(Context c) {
		if (this.product == null) {
			this.product = new TitleProduct(c);
		}
	}

	public TitleBuilder(TitleProduct tp) {
		this.product = tp;
	}

	public void setConfigData(IConfig visitor) {
		this.product.setConfigData(visitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.builder.levelkpi.LinearBuilder#buildBackground
	 * ()
	 */
	@Override
	public void buildBackground() {
		this.product.buildBackground();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.builder.levelkpi.LinearBuilder#buildRelationColumn
	 * ()
	 */
	@Override
	public void buildRelationColumn() {
		this.product.buildRelationColumn();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.builder.levelkpi.LinearBuilder#buildTrendColumn
	 * ()
	 */
	@Override
	public void buildTrendColumn() {
		this.product.buildTrendColumn();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.builder.levelkpi.LinearBuilder#buildKpiColumn()
	 */
	@Override
	public void buildKpiColumn() {
		this.product.buildKpiColumn();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.builder.levelkpi.LinearBuilder#getProduct()
	 */
	@Override
	public TitleProduct getProduct() {
		// TODO Auto-generated method stub
		return this.product;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.builder.levelkpi.LinearBuilder#buildKpiNameColumn
	 * ()
	 */
	@Override
	public void buildKpiNameColumn() {
		this.product.buildKpiNameColumn();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.builder.levelkpi.LinearBuilder#
	 * buildMainKpiNameColumn()
	 */
	@Override
	public void buildMainKpiNameColumn() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.builder.levelkpi.LinearBuilder#buildMainKpiColumn
	 * ()
	 */
	@Override
	public void buildMainKpiColumn() {
		// TODO Auto-generated method stub

	}

}
