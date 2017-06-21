/**
 * 
 */
package com.bonc.mobile.hbmclient.observer.data_presentation;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.command.date_change.DateChangeCommand;
import com.bonc.mobile.hbmclient.command.date_change.DatePickController;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.observer.IObservable;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.view.adapter.DataPresentationELVadapter;

/**
 * @author liweigao 观察者模式具有推，拉两种形式。 对于本例，由于数据的返回结果处理之后可能存在多种情况，不同的数据请求的结果情况也不同，
 *         如果采用推模式，那么需要在update（）中添加结果参数，会造成添加多个update方法；
 *         如果采用拉模式，只需要向下转型一次subject，就可以用subject的get方法获取数据，省去了添加多个方法的麻烦
 */
public class DataPresentationObserver extends TemplateDataPresentationObserver {
	private DataPresentationObservable dataPresentation;

	private DateChangeCommand dateCommand;
	private DatePickController mDatePickController;
	private Activity mActivity;

	private Button datePicker;
	private ExpandableListView mExpandableListView;
	private TextView mEmptyView;

	public DataPresentationObserver(IObservable dp, Activity a) {
		super(a);

		this.dataPresentation = (DataPresentationObservable) dp;
		this.dataPresentation.registerObserver(this);

		this.mActivity = a;
		this.mDatePickController = new DatePickController(this.mActivity);
		this.dateCommand = new DateChangeCommand(this.dataPresentation);
		this.mDatePickController.setCommand(this.dateCommand);

		initialWidget();
	}

	public void showDefaultView() {
		this.mDatePickController.initialCalendar();
	}

	private void initialWidget() {
		this.datePicker = (Button) this.mActivity.findViewById(R.id.id_button2);
		this.datePicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDatePickController.pickDate();
			}
		});

		this.mExpandableListView = (ExpandableListView) this.mActivity
				.findViewById(R.id.id_expandableListView);
		this.mEmptyView = (TextView) this.mActivity
				.findViewById(R.id.id_emptyview);
		this.mExpandableListView.setEmptyView(this.mEmptyView);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.observer.data_presentation.
	 * TemplateDataPresentationObserver#updateView()
	 */
	@Override
	protected void updateView() {
		// TODO Auto-generated method stub
		this.datePicker.setText(this.dataPresentation.getShowTime());
		List<String> groupData = this.dataPresentation.getGroupList();
		List<List<String>> childData = this.dataPresentation.getChildList();
		updateELV(groupData, childData);
		if (groupData.size() <= 0 || childData.size() <= 0) {
			showNoData();
		}

		endProgressDialog();
	}

	@Override
	protected void showException() {
		// TODO Auto-generated method stub
		updateELV(this.dataPresentation.getGroupList(),
				this.dataPresentation.getChildList());
		super.showException();
	}

	private void updateELV(List<String> groupData, List<List<String>> childData) {
		Calendar c = this.mDatePickController.getCalendar();
		if (c != null) {

		} else {
			c = DateUtil.getCalendar(this.dataPresentation.getShowTime(),
					DateRangeEnum.DAY.getDateShowPattern());
			this.mDatePickController.setCalendar(c);
		}
		DataPresentationELVadapter adapter = new DataPresentationELVadapter(
				mActivity, groupData, childData);
		this.mExpandableListView.setAdapter(adapter);
		this.mExpandableListView
				.setOnGroupClickListener(new OnGroupClickListener() {

					@Override
					public boolean onGroupClick(ExpandableListView parent,
							View v, int groupPosition, long id) {
						// TODO Auto-generated method stub
						return true;
					}
				});
		for (int i = 0; i < adapter.getGroupCount(); i++) {
			this.mExpandableListView.expandGroup(i);
		}
	}

}
