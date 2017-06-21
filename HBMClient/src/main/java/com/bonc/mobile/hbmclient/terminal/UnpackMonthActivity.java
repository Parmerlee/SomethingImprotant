/**
 * TODO
 */
package com.bonc.mobile.hbmclient.terminal;

import android.os.Bundle;
import android.widget.Button;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;
import com.bonc.mobile.hbmclient.terminal.component.RootUnpackMonthViewBranch;

/**
 * @author liweigao
 * 
 */
public class UnpackMonthActivity extends SimpleTerminalActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unpack_month_layout);
		this.mTerminalActivityEnum = TerminalActivityEnum.UNPACK_MONTH_ACTIVITY;
		this.mRootView = new RootUnpackMonthViewBranch(this,
				mTerminalActivityEnum);
		this.mRootView.setView(getWindow().getDecorView());
		this.mRootView.iteratorDispatchView();

		Button dateRangeSelect = (Button) this
				.findViewById(R.id.id_date_range_select);
		dateRangeSelect.setText(R.string.tag_month);
		this.initialDateRangeSelectListener(dateRangeSelect,
				TerminalActivityEnum.UNPACK_DAY_ACTIVITY);

		mDateSelect = (Button) this.findViewById(R.id.id_date_select);
		this.initialDateSelectListener(mDateSelect);

		initialBoardScrollView();
		initialTop10ScrollView();

		// 拆包月.
		// this.mFirstLoadDataAsynTask = new FirstLoadDataAsynTask(this);
		// this.mFirstLoadDataAsynTask.execute(new String
		// []{TerminalConfiguration.KEY_MENU_CODE_UNPACK_MONTH,mTerminalActivityEnum.getMenuCode()});
		initArea();
		loadData();

	}

}
