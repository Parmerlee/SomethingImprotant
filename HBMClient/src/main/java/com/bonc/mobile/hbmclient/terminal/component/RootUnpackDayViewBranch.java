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
public class RootUnpackDayViewBranch extends ViewBranch {

	private ModuleBasicViewTop10ViewBranch mTop10;
	private ModuleBasicViewBottomBranch mUnpackCount;// 当日拆包量
	private ModuleBasicViewCubeLineViewBranch mCubeBarViewBranch_line;
	// private ModuleBasicViewUnpackRow2Branch mRow2Branch;
	private ModuleBasicView2Row2ColumnViewBranch m2Row2Column;

	/**
	 * @param c
	 * @param tae
	 */
	public RootUnpackDayViewBranch(Context c, TerminalActivityEnum tae) {
		super(c, tae);
		// TODO Auto-generated constructor stub
		this.mTop10 = new ModuleBasicViewTop10ViewBranch(c, tae);
		this.mUnpackCount = new ModuleBasicViewBottomBranch(c, tae);
		this.mCubeBarViewBranch_line = new ModuleBasicViewCubeLineViewBranch(c,
				tae);
		// this.mRow2Branch= new ModuleBasicViewUnpackRow2Branch(c, tae);
		// this.mViewList.add(mRow2Branch);
		this.m2Row2Column = new ModuleBasicView2Row2ColumnViewBranch(c, tae);
		this.mViewList.add(m2Row2Column);
		this.mViewList.add(mCubeBarViewBranch_line);
		this.mViewList.add(mTop10);
		this.mViewList.add(mUnpackCount);

	}

	@Override
	public void dispatchView() {
		// TODO Auto-generated method stub
		View mRow2L = this.mView.findViewById(R.id.id_unpack_row2);
		// this.mRow2Branch.setView(mRow2L);
		this.m2Row2Column.setView(mRow2L);
		View unBottom = this.mView.findViewById(R.id.id_unpack_bottom);
		this.mUnpackCount.setView(unBottom);
		View top10 = this.mView.findViewById(R.id.id_ranking_view);
		this.mTop10.setView(top10);
		View mcc_l = this.mView.findViewById(R.id.id_bar_line_container);
		this.mCubeBarViewBranch_line.setView(mcc_l);
	}

}
