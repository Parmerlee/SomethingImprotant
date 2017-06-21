/**
 * LinearDirector
 */
package com.bonc.mobile.hbmclient.levelkpi;

/**
 * @author liweigao
 *
 */
public class LinearDirector {

	public void buildLevelTitleRightProduct(TitleBuilder builder) {
		builder.buildBackground();
		builder.buildRelationColumn();
		builder.buildTrendColumn();
		builder.buildKpiColumn();
	}

	public void buildLevelKpiNameProduct(TitleBuilder builder) {
		builder.buildBackground();
		builder.buildKpiNameColumn();
	}

	public void buildLevelKpiNameProduct(LevelKpiItemBuilder builder) {
		builder.buildKpiNameColumn();
	}

	public void buildLevelRightProduct(LevelKpiItemBuilder builder) {
		builder.buildRelationColumn();
		builder.buildTrendColumn();
		builder.buildKpiColumn();
	}

	public void buildRelationTitleRightProduct(TitleBuilder builder) {
		builder.buildBackground();
		builder.buildTrendColumn();
		builder.buildKpiColumn();
	}

	public void buildRelationKpiNameProduct(TitleBuilder builder) {
		builder.buildBackground();
		builder.buildKpiNameColumn();
	}

	public void buildRelationKpiNameProduct(LevelKpiItemBuilder builder) {
		builder.buildKpiNameColumn();
	}

	public void buildRelationRightProduct(LevelKpiItemBuilder builder) {
		builder.buildTrendColumn();
		builder.buildKpiColumn();
	}

	public void buildMainKpiNameProduct(LevelKpiItemBuilder builder) {
		builder.buildMainKpiNameColumn();
	}

	public void buildMainKpiRightProduct(LevelKpiItemBuilder builder) {
		builder.buildTrendColumn();
		builder.buildMainKpiColumn();
	}
}
