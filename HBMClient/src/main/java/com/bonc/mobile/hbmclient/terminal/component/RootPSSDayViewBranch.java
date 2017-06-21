/**
 * TODO
 */
package com.bonc.mobile.hbmclient.terminal.component;

import android.content.Context;
import android.view.View;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;

/**
 * @author liweigao
 * 
 */
public class RootPSSDayViewBranch extends ViewBranch {

	private ModuleBasicView6RowViewBranch m6RowLeft;
	private ModuleBasicViewPSSmiddle2RowViewBranch mMiddle2Row;
	private ModuleBasicViewTop10ViewBranch mTop10;

	private ModuleBasicViewCubeBarViewBranch mCubeBarViewBranch_bar;
	private ModuleBasicViewCubeLineViewBranch mCubeBarViewBranch_line;

	private ModuleBasicViewProgressBarGroupViewBranch mProgressBarGroup;
	private ModuleBasicViewRingChartViewBranch mRingChart;

	/**
	 * @param c
	 * @param tae
	 */
	public RootPSSDayViewBranch(Context c, TerminalActivityEnum tae) {
		super(c, tae);

		// 销量 累计销量部分.
		this.m6RowLeft = new ModuleBasicView6RowViewBranch(c, tae);
		// 当月终端进货，库存部分》
		this.mMiddle2Row = new ModuleBasicViewPSSmiddle2RowViewBranch(c, tae);
		// top10部分.
		this.mTop10 = new ModuleBasicViewTop10ViewBranch(c, tae);
		// 八大渠道.
		this.mCubeBarViewBranch_bar = new ModuleBasicViewCubeBarViewBranch(c,
				tae);
		mCubeBarViewBranch_bar.setxLableAngle(-15);

		// 销售结构部分.
		this.mProgressBarGroup = new ModuleBasicViewProgressBarGroupViewBranch(
				c, tae);
		// 趋势图部分.
		this.mCubeBarViewBranch_line = new ModuleBasicViewCubeLineViewBranch(c,
				tae);

		// 进销存三大品牌部分.
		this.mRingChart = new ModuleBasicViewRingChartViewBranch(c, tae);

		this.mViewList.add(mTop10);
		this.mViewList.add(mCubeBarViewBranch_line);
		this.mViewList.add(m6RowLeft);
		this.mViewList.add(mRingChart);

		this.mViewList.add(mCubeBarViewBranch_bar);
		this.mViewList.add(mMiddle2Row);
		this.mViewList.add(mProgressBarGroup);

	}

	@Override
	public void dispatchView() {
		// TODO Auto-generated method stub
		View lb = this.mView.findViewById(R.id.id_fg_lb);
		this.m6RowLeft.setView(lb);
		View middle2row = this.mView
				.findViewById(R.id.id_pss_module_middle_terminal);
		this.mMiddle2Row.setView(middle2row);
		View top10 = this.mView.findViewById(R.id.id_ranking_view);
		this.mTop10.setView(top10);

		View mcc_l = this.mView.findViewById(R.id.id_bar_line_container);
		this.mCubeBarViewBranch_line.setView(mcc_l);

		View mcc = this.mView.findViewById(R.id.id_bar_chart_container);
		this.mCubeBarViewBranch_bar.setView(mcc);

		View pbgroup = this.mView.findViewById(R.id.id_progressbar_group);
		this.mProgressBarGroup.setView(pbgroup);

		View ringChartContainer = this.mView.findViewById(R.id.id_ringChart);
		this.mRingChart.setView(ringChartContainer);

	}

}
