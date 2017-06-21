/**
 * TODO
 */
package com.bonc.mobile.hbmclient.terminal;

import android.os.Bundle;
import android.widget.Button;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;
import com.bonc.mobile.hbmclient.terminal.component.RootUnsaleMonthViewBranch;

/**
 * @author liweigao
 *
 */
public class UnsaleMonthActivity extends SimpleTerminalActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unsale_month_layout);
		this.mTerminalActivityEnum = TerminalActivityEnum.UNSALE_MONTH_ACTIVITY;
		this.mRootView = new RootUnsaleMonthViewBranch(this,
				mTerminalActivityEnum);
		this.mRootView.setView(getWindow().getDecorView());
		this.mRootView.iteratorDispatchView();

		Button dateRangeSelect = (Button) this
				.findViewById(R.id.id_date_range_select);
		dateRangeSelect.setText(R.string.tag_month);
		this.initialDateRangeSelectListener(dateRangeSelect,
				TerminalActivityEnum.UNSALE_DAY_ACTIVITY);

		mDateSelect = (Button) this.findViewById(R.id.id_date_select);
		this.initialDateSelectListener(mDateSelect);

		initialBoardScrollView();
		initialTop10ScrollView();

		// 滞销月
		// this.mFirstLoadDataAsynTask = new FirstLoadDataAsynTask(this);
		// this.mFirstLoadDataAsynTask.execute(new String
		// []{TerminalConfiguration.KEY_MENU_CODE_UNSALE_MONTH,mTerminalActivityEnum.getMenuCode()});
		initArea();
		loadData();

	}

}
