/**
 * LinearBuilder
 */
package com.bonc.mobile.hbmclient.levelkpi;

/**
 * @author liweigao
 *
 */
public interface LinearBuilder<T> {
	T getProduct();

	void buildBackground();

	void buildRelationColumn();

	void buildTrendColumn();

	void buildKpiColumn();

	void buildKpiNameColumn();

	void buildMainKpiNameColumn();

	void buildMainKpiColumn();
}
