package com.bonc.mobile.hbmclient.terminal;

import android.os.Bundle;
import android.widget.Button;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;
import com.bonc.mobile.hbmclient.terminal.component.RootPSSDayViewBranch;

/**
 * @author liweigao
 * 
 */
public class PurchaseSellStockDayActivity extends SimpleTerminalActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.purchase_sell_stock_day_layout);

		this.mTerminalActivityEnum = TerminalActivityEnum.PSS_DAY_ACTIVITY;
		this.mRootView = new RootPSSDayViewBranch(this,
				this.mTerminalActivityEnum);
		this.mRootView.setView(getWindow().getDecorView());
		this.mRootView.iteratorDispatchView();

		Button dateRangeSelect = (Button) this
				.findViewById(R.id.id_date_range_select);
		dateRangeSelect.setText(R.string.tag_day);
		this.initialDateRangeSelectListener(dateRangeSelect,
				TerminalActivityEnum.PSS_MONTH_ACTIVITY);

		mDateSelect = (Button) this.findViewById(R.id.id_date_select);
		this.initialDateSelectListener(mDateSelect);

		initialBoardScrollView();
		initialTop10ScrollView();

		// 检查是否有新数据 134代表进销存日
		// this.mFirstLoadDataAsynTask = new FirstLoadDataAsynTask(this);
		// mFirstLoadDataAsynTask.execute(new String
		// []{TerminalConfiguration.KEY_MENU_CODE_PSS_DAY,mTerminalActivityEnum.getMenuCode(),TerminalConfiguration.KEY_MENU_CODE_PSS_DAY_2});
		initArea();
		loadData();
	}

}
