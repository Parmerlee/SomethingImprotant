/**
 * RelationKpiObserver
 */
package com.bonc.mobile.hbmclient.observer.relationkpi;

import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.SlideHolderActivity;
import com.bonc.mobile.hbmclient.adapter.levelkpi.RelationKpiConfigAndDataAdapter;
import com.bonc.mobile.hbmclient.levelkpi.LevelKpiItemBuilder;
import com.bonc.mobile.hbmclient.levelkpi.LevelKpiItemProduct;
import com.bonc.mobile.hbmclient.levelkpi.LinearDirector;
import com.bonc.mobile.hbmclient.levelkpi.TitleBuilder;
import com.bonc.mobile.hbmclient.levelkpi.TitleProduct;
import com.bonc.mobile.hbmclient.command.date_change.DatePickController;
import com.bonc.mobile.hbmclient.command.date_change.DateSelectCommand;
import com.bonc.mobile.hbmclient.observer.data_presentation.TemplateDataPresentationObserver;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.ListViewSetting;
import com.bonc.mobile.hbmclient.view.adapter.RelationKpiListLeftAdapter;
import com.bonc.mobile.hbmclient.view.adapter.RelationKpiListRightAdapter;
import com.bonc.mobile.hbmclient.visitor.IConfig;

/**
 * @author liweigao
 *
 */
public class ARelationKpiObserver extends TemplateDataPresentationObserver {
	protected SlideHolderActivity mActivity;
	protected ExpandableListView listLeft;
	protected ExpandableListView listRight;
	protected Button dateSelect;

	private LevelKpiItemProduct mainKpiLeft;
	private LevelKpiItemProduct mainKpiRight;
	private Button navigator;
	private TextView titleName;
	private DatePickController mDatePickController;
	private View mContentView;
	private LinearDirector mLinearDirector = new LinearDirector();
	private ARelationKpiObservable mObservable;

	/**
	 * @param c
	 */
	public ARelationKpiObserver(ARelationKpiObservable observable,
			SlideHolderActivity a) {
		super(a);
		observable.registerObserver(this);
		this.mObservable = observable;
		this.mActivity = a;

		this.mDatePickController = new DatePickController(a);
		DateSelectCommand dsc = new DateSelectCommand(observable);
		this.mDatePickController.setCommand(dsc);
	}

	public void setContentView() {
		if (this.mContentView != null) {

		} else {
			initialStaticView();
		}
		this.mActivity.setMainContent(this.mObservable.getConfigVisitor()
				.getMenuCode(), this.mContentView);
	}

	private void initialStaticView() {
		LayoutInflater mInflater = LayoutInflater.from(mActivity);
		this.mContentView = mInflater.inflate(R.layout.relation_kpi_layout,
				null);

		dispatchView();
	}

	private void dispatchView() {
		RelativeLayout rl = (RelativeLayout) mContentView
				.findViewById(R.id.id_parent);
		rl.setBackgroundDrawable(WatermarkImage.getWatermarkDrawable());

		this.navigator = (Button) mContentView.findViewById(R.id.id_navigator);
		this.titleName = (TextView) mContentView
				.findViewById(R.id.logo_word_mon_dev);
		this.dateSelect = (Button) mContentView.findViewById(R.id.id_button2);

		this.listLeft = (ExpandableListView) this.mContentView
				.findViewById(R.id.list_view1);
		setELV(listLeft);
		this.listRight = (ExpandableListView) this.mContentView
				.findViewById(R.id.list_view2);
		setELV(listRight);
		ListViewSetting mListViewSetting = new ListViewSetting();
		mListViewSetting.setListViewOnTouchAndScrollListener(listLeft,
				listRight);
	}

	private void setELV(ExpandableListView elv) {
		elv.setGroupIndicator(null);
		LayoutInflater inflater = LayoutInflater.from(elv.getContext());
		View v = inflater.inflate(R.layout.divider_hor, null);
		elv.addFooterView(v, null, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.observer.data_presentation.
	 * TemplateDataPresentationObserver#showStaticEnd()
	 */
	@Override
	protected void showStaticEnd() {
		IConfig visitor = this.mObservable.getConfigVisitor();
		this.titleName.setText(visitor.getMenuName());

		TitleProduct tp_left = (TitleProduct) this.mContentView
				.findViewById(R.id.id_title_left);
		TitleBuilder builder_left = new TitleBuilder(tp_left);
		builder_left.setConfigData(visitor);
		mLinearDirector.buildRelationKpiNameProduct(builder_left);

		TitleProduct tp_right = (TitleProduct) this.mContentView
				.findViewById(R.id.id_title_right);
		TitleBuilder builder_right = new TitleBuilder(tp_right);
		builder_right.setConfigData(visitor);
		mLinearDirector.buildRelationTitleRightProduct(builder_right);

		mainKpiLeft = (LevelKpiItemProduct) this.mContentView
				.findViewById(R.id.main_kpi_content_left);
		mainKpiLeft.setConfigAndDataAdapter(this.mObservable.getDataAdapter());
		LevelKpiItemBuilder leftBuilder = new LevelKpiItemBuilder(mainKpiLeft);
		this.mLinearDirector.buildMainKpiNameProduct(leftBuilder);

		mainKpiRight = (LevelKpiItemProduct) this.mContentView
				.findViewById(R.id.main_kpi_content_right);
		mainKpiRight.setConfigAndDataAdapter(this.mObservable.getDataAdapter());
		LevelKpiItemBuilder rightBuilder = new LevelKpiItemBuilder(mainKpiRight);
		this.mLinearDirector.buildMainKpiRightProduct(rightBuilder);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.observer.data_presentation.
	 * TemplateDataPresentationObserver#updateView()
	 */
	@Override
	protected void updateView() {
		RelationKpiConfigAndDataAdapter dataAdapter = this.mObservable
				.getDataAdapter();

		String showtime = dataAdapter.getShowTime();
		this.dateSelect.setText(showtime);
		this.mDatePickController.setCalendar(showtime, dataAdapter
				.getDataType().getDateShowPattern());

		updateMainKpiView(dataAdapter);

		RelationKpiListLeftAdapter rklla = new RelationKpiListLeftAdapter(
				dataAdapter);
		this.listLeft.setAdapter(rklla);
		int length = rklla.getGroupCount();
		for (int i = 0; i < length; i++) {
			this.listLeft.expandGroup(i);
		}

		RelationKpiListRightAdapter rklra = new RelationKpiListRightAdapter(
				dataAdapter);
		this.listRight.setAdapter(rklra);

		length = rklra.getGroupCount();
		for (int i = 0; i < length; i++) {
			this.listRight.expandGroup(i);
		}
		this.listLeft.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) { // TODO Auto-generated method
													// stub
				return true;
			}
		});
		this.listRight.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) { // TODO Auto-generated method
													// stub
				return true;
			}
		});

		initialViewCallBack();
		endProgressDialog();
	}

	private void updateMainKpiView(RelationKpiConfigAndDataAdapter dataAdapter) {
		String mainKpiName = null;
		try {
			// 左侧
			mainKpiName = dataAdapter.getMainKpiName();

			// 右侧
			List<Double> trendData = dataAdapter.getMainKpiTrend();
			if (trendData != null) {
				mainKpiRight.setTrendData(trendData);
			}

			Map<String, String> mainKpiData = dataAdapter.getMainKpiData();
			if (mainKpiData != null) {
				mainKpiRight.setMainKpiData(mainKpiData);
			}

			String mainKpiCode = dataAdapter.getMainKpiCode();
			if (mainKpiCode != null) {
				mainKpiLeft.setOnClickListener(mainKpiCode);
				mainKpiLeft.setOnLongClickListener(mainKpiCode);
				mainKpiRight.setOnClickListener(mainKpiCode);
			}
		} catch (Exception e) {
			mainKpiName = "--";
		} finally {
			mainKpiLeft.setMainKpiNameText(mainKpiName);
		}
	}

	private void initialViewCallBack() {
		this.navigator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mActivity.slideHolder.toggle();
			}
		});

		this.dateSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDatePickController.pickDate();
			}
		});
	}
}
