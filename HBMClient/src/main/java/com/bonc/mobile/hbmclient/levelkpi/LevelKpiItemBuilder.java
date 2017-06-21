/**
 * ContentItemBuilder
 */
package com.bonc.mobile.hbmclient.levelkpi;

import android.content.Context;

/**
 * @author liweigao
 *
 */
public class LevelKpiItemBuilder implements LinearBuilder<LevelKpiItemProduct> {
	private LevelKpiItemProduct product;

	public LevelKpiItemBuilder(Context c) {
		if (this.product == null) {
			this.product = new LevelKpiItemProduct(c);
		}
	}

	public LevelKpiItemBuilder(LevelKpiItemProduct product) {
		this.product = product;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.builder.levelkpi.LinearBuilder#getProduct()
	 */
	@Override
	public LevelKpiItemProduct getProduct() {
		// TODO Auto-generated method stub
		return this.product;
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
		// TODO Auto-generated method stub
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
		this.product.buildMainKpiNameColumn();
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
		this.product.buildMainKpiColumn();
	}

}
