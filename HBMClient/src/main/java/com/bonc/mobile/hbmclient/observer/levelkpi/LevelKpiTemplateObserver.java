/**
 * LevelKpiTemplateObserver
 */
package com.bonc.mobile.hbmclient.observer.levelkpi;

import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.SlideHolderActivity;
import com.bonc.mobile.hbmclient.adapter.levelkpi.ConfigAndDataAdapter;
import com.bonc.mobile.hbmclient.asyn_task.DailyReportAsynTask;
import com.bonc.mobile.hbmclient.levelkpi.LinearDirector;
import com.bonc.mobile.hbmclient.levelkpi.TitleBuilder;
import com.bonc.mobile.hbmclient.levelkpi.TitleProduct;
import com.bonc.mobile.hbmclient.command.date_change.AreaPickController;
import com.bonc.mobile.hbmclient.command.date_change.AreaSelectCommand;
import com.bonc.mobile.hbmclient.command.date_change.DatePickController;
import com.bonc.mobile.hbmclient.command.date_change.DateSelectCommand;
import com.bonc.mobile.hbmclient.observer.IObservable;
import com.bonc.mobile.hbmclient.observer.data_presentation.TemplateDataPresentationObserver;
import com.bonc.mobile.hbmclient.view.BulletinView;
import com.bonc.mobile.hbmclient.view.ListViewSetting;
import com.bonc.mobile.hbmclient.view.adapter.LevelKpiListLeftAdapter;
import com.bonc.mobile.hbmclient.view.adapter.LevelKpiListRightAdapter;
import com.bonc.mobile.hbmclient.visitor.IConfig;

/**
 * @author liweigao
 *
 */
public class LevelKpiTemplateObserver extends TemplateDataPresentationObserver {
	private LevelKpiTemplateObservable lktObservable;

	private LinearDirector mLinearDirector = new LinearDirector();

	private SlideHolderActivity activity;
	private View mContentView;

	private Button navigator;
	private Button dateSelect, areaSelect;
	private TextView titleName;
	private ListView listLeft, listRight;
	private BulletinView mBulletinView;

	private DatePickController mDatePickController;
	private AreaPickController mAreaPickController;
	private String showDailyReport;
	private boolean isFirstShow = true;

	private final String SHOW_DAILY_REPORT = "1";

	public LevelKpiTemplateObserver(IObservable observable,
			SlideHolderActivity activity, String menuCode) {
		super(activity);
		lktObservable = (LevelKpiTemplateObservable) observable;
		lktObservable.registerObserver(this);

		this.activity = activity;
		this.mDatePickController = new DatePickController(activity);
		DateSelectCommand dsc = new DateSelectCommand(lktObservable);
		this.mDatePickController.setCommand(dsc);

		this.mAreaPickController = new AreaPickController(activity, menuCode);
		AreaSelectCommand asc = new AreaSelectCommand(lktObservable);
		this.mAreaPickController.setCommand(asc);

		lktObservable.setAreaPickController(mAreaPickController);

	}

	private void initialStaticView() {
		LayoutInflater mInflater = LayoutInflater.from(activity);
		this.mContentView = mInflater.inflate(R.layout.level_kpi_layout, null);

		dispatchView();
	}

	public void setContentView() {
		if (this.mContentView != null) {

		} else {
			initialStaticView();
		}
		this.activity.setMainContent(this.lktObservable.getConfigVisitor()
				.getParentMenuCode(), this.mContentView);
	}

	private void dispatchView() {
		// RelativeLayout rl = (RelativeLayout)
		// mContentView.findViewById(R.id.id_parent);
		// rl.setBackgroundDrawable(WatermarkImage.getWatermarkDrawable());

		this.navigator = (Button) mContentView.findViewById(R.id.id_navigator);
		this.titleName = (TextView) mContentView
				.findViewById(R.id.logo_word_mon_dev);
		this.areaSelect = (Button) mContentView.findViewById(R.id.id_button1);
		this.dateSelect = (Button) mContentView.findViewById(R.id.id_button2);
		this.listLeft = (ListView) mContentView.findViewById(R.id.id_list_left);
		this.listRight = (ListView) mContentView
				.findViewById(R.id.id_list_right);
		ListViewSetting mListViewSetting = new ListViewSetting();
		mListViewSetting.setListViewOnTouchAndScrollListener(listLeft,
				listRight);
	}

	private void initialViewCallBack() {
		this.navigator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.slideHolder.toggle();
			}
		});

		this.dateSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDatePickController.pickDate();
			}
		});

		this.areaSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mAreaPickController.pickArea();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.observer.data_presentation.
	 * TemplateDataPresentationObserver#updateView()
	 */
	@Override
	protected void updateView() {
		final ConfigAndDataAdapter dataAdapter = this.lktObservable
				.getConfigAndDataAdapter();

		String showDate = dataAdapter.getShowTime();
		this.dateSelect.setText(showDate);
		this.mDatePickController.setCalendar(showDate, dataAdapter
				.getDataType().getDateShowPattern());
		this.areaSelect.setText(this.lktObservable.getAreaName());

		this.listLeft.setAdapter(new LevelKpiListLeftAdapter(dataAdapter));
		this.listRight.setAdapter(new LevelKpiListRightAdapter(dataAdapter));

		initialViewCallBack();
		endProgressDialog();

		List<Map<String, String>> kpiData = dataAdapter.getKpiData();
		if (kpiData == null || kpiData.size() <= 0) {
			showNoData();
		}

		if (isFirstShow) {
			if (SHOW_DAILY_REPORT.equals(showDailyReport)) {
				if (mBulletinView != null) {
					new DailyReportAsynTask(mBulletinView, "点击查看文字说明")
							.execute();
				}
			}
			isFirstShow = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.observer.data_presentation.
	 * TemplateDataPresentationObserver#showStaticEnd()
	 */
	@Override
	protected void showStaticEnd() {
		IConfig visitor = this.lktObservable.getConfigVisitor();
		// title
		showDailyReport = visitor.getShowDailyReport();
		if (SHOW_DAILY_REPORT.equals(showDailyReport)) {
			RelativeLayout rl = (RelativeLayout) getNeedView();
			LayoutParams lp = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.rightMargin = context.getResources().getDimensionPixelSize(
					R.dimen.daily_report_right_margin);
			String parentMenuCode = visitor.getParentMenuCode();
			mBulletinView = new BulletinView(activity, parentMenuCode);
			rl.addView(mBulletinView, lp);
		}
		// 小标题
		this.titleName.setText(visitor.getMenuName());

		TitleProduct tp_left = (TitleProduct) this.mContentView
				.findViewById(R.id.id_title_left);
		TitleBuilder builder_left = new TitleBuilder(tp_left);
		builder_left.setConfigData(visitor);
		mLinearDirector.buildLevelKpiNameProduct(builder_left);

		TitleProduct tp_right = (TitleProduct) this.mContentView
				.findViewById(R.id.id_title_right);
		TitleBuilder builder_right = new TitleBuilder(tp_right);
		builder_right.setConfigData(visitor);
		mLinearDirector.buildLevelTitleRightProduct(builder_right);
	}

	/**
	 * @return the mContentView
	 */
	public View getNeedView() {
		return this.mContentView.findViewById(R.id.id_mainkpi_title);
	}

	/**
	 * @return the activity
	 */
	public SlideHolderActivity getActivity() {
		return activity;
	}

	/**
	 * @return the mContentView
	 */
	public View getContentView() {
		return mContentView;
	}
}
