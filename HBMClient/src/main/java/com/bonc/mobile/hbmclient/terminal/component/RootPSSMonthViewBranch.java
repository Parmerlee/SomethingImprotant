package com.bonc.mobile.hbmclient.terminal.component;

import org.achartengine.renderer.DefaultRenderer;

import android.content.Context;
import android.view.View;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;

/**
 * @author liweigao
 * @author houyangyang
 * 
 */
public class RootPSSMonthViewBranch extends ViewBranch {
	// private ModuleBasicViewCubeBarViewBranch mCubeBarViewBranch_bar;
	private ModuleBasicViewCubeLineViewBranch mCubeBarViewBranch_line;
	private ModuleBasicViewTop10ViewBranch mTop10;

	// 年累计销量,当月销量
	private ModuleBasicViewPSSmonthSales mRow3sales;
	// 销售结构
	private ModuleBasicViewProgressBarGroupViewBranch mProgressBarGroup;
	// 终端价格分档 TD终端补贴分档
	private ModuleBasicViewPieChartViewBranch mPieChartViewBranchLeft,
			mPieChartViewBranchRight;

	/**
	 * @param c
	 * @param tae
	 */
	public RootPSSMonthViewBranch(Context c, TerminalActivityEnum tae) {
		super(c, tae);
		this.mTop10 = new ModuleBasicViewTop10ViewBranch(c, tae);
		this.mRow3sales = new ModuleBasicViewPSSmonthSales(c, tae);
		this.mProgressBarGroup = new ModuleBasicViewProgressBarGroupViewBranch(
				c, tae);
		this.mCubeBarViewBranch_line = new ModuleBasicViewCubeLineViewBranch(c,
				tae);
		this.mPieChartViewBranchLeft = new ModuleBasicViewPieChartViewBranch(c,
				tae, new boolean[] { true, false, false },
				DefaultRenderer.LEGEND_BOTTOM,
				TerminalConfiguration.POS_MONTH_PSS_LEFT_PIE);
		this.mPieChartViewBranchRight = new ModuleBasicViewPieChartViewBranch(
				c, tae, new boolean[] { true, false, false },
				DefaultRenderer.LEGEND_BOTTOM,
				TerminalConfiguration.POS_MONTH_PSS_RIGHT_PIE);
		this.mViewList.add(mCubeBarViewBranch_line);
		this.mViewList.add(mTop10);
		this.mViewList.add(mRow3sales);
		this.mViewList.add(mProgressBarGroup);
		this.mViewList.add(mPieChartViewBranchLeft);
		this.mViewList.add(mPieChartViewBranchRight);
	}

	@Override
	public void dispatchView() {
		// TODO Auto-generated method stub
		View mcc = this.mView.findViewById(R.id.id_bar_chart_container);
		this.mCubeBarViewBranch_line.setView(mcc);
		View sales = this.mView.findViewById(R.id.id_pss_month_middle_sales);
		this.mRow3sales.setView(sales);
		View top10 = this.mView.findViewById(R.id.id_ranking_view);
		this.mTop10.setView(top10);
		View pbgroup = this.mView.findViewById(R.id.id_progressbar_group);
		this.mProgressBarGroup.setView(pbgroup);
		View piel = this.mView.findViewById(R.id.id_pie_chart_left);
		this.mPieChartViewBranchLeft.setView(piel);

		View pieR = this.mView.findViewById(R.id.id_pie_chart_right);
		this.mPieChartViewBranchRight.setView(pieR);
	}
}
