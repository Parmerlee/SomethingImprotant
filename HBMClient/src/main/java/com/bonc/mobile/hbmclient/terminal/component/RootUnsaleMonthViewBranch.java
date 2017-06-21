/**
 * TODO
 */
package com.bonc.mobile.hbmclient.terminal.component;

import org.achartengine.renderer.DefaultRenderer;

import android.content.Context;
import android.view.View;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;

/**
 * @author liweigao
 *
 */
public class RootUnsaleMonthViewBranch extends ViewBranch {
	private ModuleBasicViewPieChartViewBranch mPieChartViewBranch;
	private ModuleBasicViewTop10ViewBranch mTop10;
	private ModuleBasicViewBottomBranch mUnsaleBottom;
	private ModuleBasicViewCubeLineViewBranch mCubeBarViewBranch_line;

	/**
	 * @param c
	 * @param tae
	 */
	public RootUnsaleMonthViewBranch(Context c, TerminalActivityEnum tae) {
		super(c, tae);
		// TODO Auto-generated constructor stub
		this.mTop10 = new ModuleBasicViewTop10ViewBranch(c, tae);
		this.mUnsaleBottom = new ModuleBasicViewBottomBranch(c, tae);
		this.mCubeBarViewBranch_line = new ModuleBasicViewCubeLineViewBranch(c,
				tae);
		this.mPieChartViewBranch = new ModuleBasicViewPieChartViewBranch(c,
				tae, new boolean[] { false, true, false },
				DefaultRenderer.LEGEND_RIGHT);
		this.mViewList.add(mPieChartViewBranch);
		this.mViewList.add(mCubeBarViewBranch_line);
		this.mViewList.add(mTop10);
		this.mViewList.add(mUnsaleBottom);
	}

	@Override
	public void dispatchView() {
		// TODO Auto-generated method stub
		View unBottom = this.mView.findViewById(R.id.id_unsale_bottom);
		this.mUnsaleBottom.setView(unBottom);
		View top10 = this.mView.findViewById(R.id.id_ranking_view);
		this.mTop10.setView(top10);
		View mcc_l = this.mView.findViewById(R.id.id_bar_line_container);
		this.mCubeBarViewBranch_line.setView(mcc_l);
		View pieV = this.mView.findViewById(R.id.id_pie_chart);
		this.mPieChartViewBranch.setView(pieV);
	}
}
