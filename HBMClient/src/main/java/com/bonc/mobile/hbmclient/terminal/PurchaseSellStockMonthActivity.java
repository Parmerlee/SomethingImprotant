/**
 * TODO
 */
package com.bonc.mobile.hbmclient.terminal;

import android.os.Bundle;
import android.widget.Button;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;
import com.bonc.mobile.hbmclient.terminal.component.RootPSSMonthViewBranch;

/**
 * @author liweigao
 *
 */
public class PurchaseSellStockMonthActivity extends SimpleTerminalActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.purchase_sell_stock_month_layout);
		this.mTerminalActivityEnum = TerminalActivityEnum.PSS_MONTH_ACTIVITY;

		this.mRootView = new RootPSSMonthViewBranch(this,
				this.mTerminalActivityEnum);
		this.mRootView.setView(getWindow().getDecorView());
		this.mRootView.iteratorDispatchView();

		Button dateRangeSelect = (Button) this
				.findViewById(R.id.id_date_range_select);
		dateRangeSelect.setText(R.string.tag_month);
		this.initialDateRangeSelectListener(dateRangeSelect,
				TerminalActivityEnum.PSS_DAY_ACTIVITY);

		mDateSelect = (Button) this.findViewById(R.id.id_date_select);
		this.initialDateSelectListener(mDateSelect);

		initialBoardScrollView();
		initialTop10ScrollView();

		// this.mFirstLoadDataAsynTask = new FirstLoadDataAsynTask(this);
		// mFirstLoadDataAsynTask.execute(new String
		// []{TerminalConfiguration.KEY_MENU_CODE_PSS_MONTH,mTerminalActivityEnum.getMenuCode(),TerminalConfiguration.KEY_MENU_CODE_PSS_MONTH_2});
		initArea();
		loadData();

	}

}
