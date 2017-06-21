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
public class RootFleeGoodsViewBranch extends ViewBranch {

	// 趋势图
	private ModuleBasicViewCubeLineViewBranch mCubeBarViewBranch_bar;
	// top10
	private ModuleBasicViewTop10ViewBranch mTop10;
	// 当月窜货量 当月激活量
	private ModuleBasicViewFGH2Branch mH2Branch;
	// 省内窜出 疑似窜出
	// private ModuleBasicView2Row2ColumnViewBranch mFGout;
	private ModuleBasicViewFGH2Branch mInOut;

	/**
	 * @param c
	 * @param tae
	 */
	public RootFleeGoodsViewBranch(Context c, TerminalActivityEnum tae) {
		super(c, tae);
		// TODO Auto-generated constructor stub
		this.mTop10 = new ModuleBasicViewTop10ViewBranch(c, tae);
		this.mCubeBarViewBranch_bar = new ModuleBasicViewCubeLineViewBranch(c,
				tae);
		this.mH2Branch = new ModuleBasicViewFGH2Branch(c, tae);
		this.mH2Branch.setPosition(this.mH2Branch.UP);
		// this.mFGout = new ModuleBasicView2Row2ColumnViewBranch(c, tae);
		// this.mViewList.add(mFGout);
		this.mInOut = new ModuleBasicViewFGH2Branch(c, tae);
		this.mInOut.setPosition(this.mH2Branch.DOWN);
		this.mViewList.add(mInOut);
		this.mViewList.add(mH2Branch);
		this.mViewList.add(mCubeBarViewBranch_bar);
		this.mViewList.add(mTop10);
	}

	@Override
	public void dispatchView() {
		// TODO Auto-generated method stub
		View mcc = this.mView.findViewById(R.id.id_bar_chart_container);
		this.mCubeBarViewBranch_bar.setView(mcc);

		View m2hb = this.mView.findViewById(R.id.id_fg_row1);
		this.mH2Branch.setView(m2hb);

		View inout = this.mView.findViewById(R.id.id_fg_row2);
		this.mInOut.setView(inout);
		// this.mFGout.setView(m3hb);

		View top10 = this.mView.findViewById(R.id.id_ranking_view);
		this.mTop10.setView(top10);
	}
}
