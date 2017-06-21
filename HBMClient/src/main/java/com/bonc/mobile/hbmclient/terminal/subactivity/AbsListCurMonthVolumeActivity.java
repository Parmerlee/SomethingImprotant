package com.bonc.mobile.hbmclient.terminal.subactivity;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.net.ConnectManager;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;
import com.bonc.mobile.hbmclient.terminal.view.StatisticsRightAdapter;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.view.MyDatePickerDialog;
import com.bonc.mobile.hbmclient.view.StickyHorizontalScrollView;
import com.bonc.mobile.hbmclient.view.StickyHorizontalScrollView.OnAutoScrollListener;

/** 带有可展开数据列表版本的CurMonthVolumeActivity。需要在实现时同时实现AsynchronousGap里的方法 */
public abstract class AbsListCurMonthVolumeActivity extends
		CurMonthVolumeActivity implements AsynchronousGap {
	private static final int ORDER_LOCATION_BASE = 223;

	protected String[] mKeys = null;
	protected String[] mKeyFilters = null;
	private String[] mArea = null;
	private String[] mAreaCode = null;
	protected String[][] mListDatas = null;
	protected String[] mColumnNames = null;
	/** 当前地区代码 */
	protected String mCurrentArea;
	/** 当前日期代码 */
	protected String mDateString;// server时间

	protected LinearLayout mListLayout;
	protected LinearLayout mFieldNameLayout;
	private Button mDateSelectBtn;
	private LinearLayout mBottomlayout;
	private TextView mAreaNameTv;
	private ProgressDialog mWaitingDia;
	protected DatePickerDialog mDateDia;
	protected ListView mListView;
	protected ListTask mTask;
	private TextView mThisMonthTextView;
	private TextView mLastMonthTextView;
	protected StatisticsRightAdapter mAdapter;
	private Calendar calendar;
	private Handler handler;
	BusinessDao businessdao = new BusinessDao();
	protected String mLocalString = businessdao.getUserInfo().get("areaId");
	private String showDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		List<Map<String, String>> list = businessdao.getAreaInfo(mLocalString);
		mArea = new String[list.size()];
		mAreaCode = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			mArea[i] = list.get(i).get("areaName");
			mAreaCode[i] = list.get(i).get("areaCode");
		}
		LayoutInflater inflater = LayoutInflater.from(this);
		String[] s = new String[] { "■ " + "当月", "● " + "上月" };
		calendar = (Calendar) this.mTerminalActivityEnum.getCalendar().clone();
		showDate = DateUtil.formatter(calendar.getTime(), mTerminalActivityEnum
				.getDateRange().getDateShowPattern());
		mDateDia = new MyDatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {

				if (!ConnectManager.isConnected()) {
					showTips();
					return;
				}
				;

				calendar.set(year, monthOfYear, dayOfMonth);

				/*
				 * if(!DataMessageControl.getInstance().isDateInvalid(calendar))
				 * { Toast.makeText(AbsListCurMonthVolumeActivity.this,
				 * "无该日期数据!", Toast.LENGTH_LONG).show(); return ; }
				 */

				String newDateString = DateUtil.formatter(calendar.getTime(),
						mTerminalActivityEnum.getDateRange()
								.getDateServerPattern());
				if (!newDateString.equals(mDateString)) {
					mDateString = newDateString;
					showDate = DateUtil.formatter(calendar.getTime(),
							mTerminalActivityEnum.getDateRange()
									.getDateShowPattern());
					mDateSelectBtn.setText(showDate);
					configMap
							.put(TerminalConfiguration.KEY_OPTIME, mDateString);
					refreshList();
				}
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
				.get(Calendar.DAY_OF_MONTH));
		String[] unparsedKeys = onGetKeys(savedInstanceState);
		mKeyFilters = new String[unparsedKeys.length];
		mKeys = new String[unparsedKeys.length];
		for (int i = 0; i < unparsedKeys.length; i++) {
			String[] key_and_filter = unparsedKeys[i].split("\\|", 2);
			mKeys[i] = key_and_filter[0];
			if (key_and_filter.length > 1) {
				mKeyFilters[i] = key_and_filter[1];
			}
		}
		mThisMonthTextView = (TextView) findViewById(R.id.cur_this_month_tv);
		mLastMonthTextView = (TextView) findViewById(R.id.cur_last_month_tv);
		setChartCetagoryTitle(s);
		mColumnNames = onGetColumnNames(savedInstanceState);
		mListLayout = (LinearLayout) inflater.inflate(
				R.layout.cur_month_volume_list, mRootView);
		mListView = (ListView) findViewById(R.id.cur_month_list_view);
		View footer = LayoutInflater.from(this).inflate(
				R.layout.list_foot_view, null);
		mListView.addFooterView(footer, null, false);
		mAreaNameTv = (TextView) findViewById(R.id.cur_area_title_tv);
		mAreaNameTv.setVisibility(View.VISIBLE);
		mFieldNameLayout = (LinearLayout) findViewById(R.id.cur_month_list_field);
		mWaitingDia = new ProgressDialog(this);
		mWaitingDia.setCancelable(true);
		mDateSelectBtn = (Button) findViewById(R.id.cur_date_btn);
		mDateSelectBtn.setVisibility(View.GONE);
		mDateSelectBtn.setText(showDate);
		mDateSelectBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDateDia.show();
			}
		});

		mBottomlayout = (LinearLayout) findViewById(R.id.cur_bottom_ly);
		mBottomlayout.setVisibility(View.GONE);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		final int screenwidth = metrics.widthPixels;
		mWaitingDia.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				if (mTask != null && mTask.mmIsRunning) {
					mTask.cancel(true);
					BackToChart();
				}
			}
		});
		int i = 0;
		mFieldNameLayout.removeAllViews();
		for (String column : mColumnNames) {
			View.inflate(this, R.layout.section_list_column_textview,
					mFieldNameLayout);
			TextView tv = (TextView) mFieldNameLayout.getChildAt(i);
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tv
					.getLayoutParams();
			lp.weight = 1;
			lp.width = LayoutParams.MATCH_PARENT;
			lp.height = LayoutParams.MATCH_PARENT;
			if (column.length() < 4)
				tv.setSingleLine();
			tv.setGravity(Gravity.CENTER);
			tv.setText(column);
			i++;
		}
		mRootView.setOnAutoScrollListener(new OnAutoScrollListener() {
			@Override
			public void onAutoScrollEnd(int x, int y, int index) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAutoScrollStart(MotionEvent ev, int vx, int vy,
					int l, int d) {
				// TODO Auto-generated method stub
				if (mRootView.getStickyPageX() >= screenwidth
						|| vx < -StickyHorizontalScrollView.SNAP_VELOCITY) {
					if (mListDatas == null) {
						refreshList();
					}
					mBottomlayout.setVisibility(View.VISIBLE);
					mDateSelectBtn.setVisibility(View.VISIBLE);
				} else {
					mDateSelectBtn.setVisibility(View.GONE);
					mBottomlayout.setVisibility(View.GONE);
				}

			}
		});
	}

	/** 开始载入右侧列表数据 */
	public void refreshList() {
		String[] unparsedKeys = onGetKeys(null);
		mKeyFilters = new String[unparsedKeys.length];
		mKeys = new String[unparsedKeys.length];
		for (int i = 0; i < unparsedKeys.length; i++) {
			String[] key_and_filter = unparsedKeys[i].split("\\|", 2);
			mKeys[i] = key_and_filter[0];
			if (key_and_filter.length > 1) {
				mKeyFilters[i] = key_and_filter[1];
			}
		}
		mColumnNames = onGetColumnNames(null);
		if (mTask == null) {
			mTask = new ListTask();
		} else {
			mTask.cancel(true);
			mTask = new ListTask();
		}
		mTask.execute();
	}

	/** 弹回页首,待 */
	private void BackToChart() {
		mRootView.scrollTo(0, 0);
	}

	protected abstract JSONObject onChartDataLoading();

	/** 用于异步更新List的Task */
	protected class ListTask extends AsyncTask<Void, Void, JSONObject> {
		public boolean mmIsRunning = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mmIsRunning = true;
			mWaitingDia.show();
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			return onLoading(mKeys);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			mmIsRunning = false;
			mWaitingDia.dismiss();
			if (result == null) {

			} else {

				JSONArray array = result.optJSONArray("data");
				mListDatas = new String[array.length()][];
				for (int i = 0; i < array.length(); i++) {
					mListDatas[i] = new String[mKeys.length];
					for (int j = 0; j < mKeys.length; j++) {
						mListDatas[i][j] = array.optJSONObject(i).optString(
								mKeys[j]);
					}
				}
				mAdapter = new StatisticsRightAdapter(
						AbsListCurMonthVolumeActivity.this, mListDatas);
				mAdapter.setFilter(mKeyFilters);
				mListView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();

			}
			super.onPostExecute(result);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		if (!ConnectManager.isConnected()) {
			showTips();
			return false;
		}
		;
		int order = item.getOrder() - ORDER_LOCATION_BASE;
		if (order >= 0 && order < mAreaCode.length) {
			mCurrentArea = mAreaCode[order];
		}
		return super.onContextItemSelected(item);
	}

	/** texts[0]为第一个tv的文字内容，texts[1]为第二个 */
	protected void setChartCetagoryTitle(String[] texts) {
		mThisMonthTextView.setText(texts[0]);
		mLastMonthTextView.setText(texts[1]);
	}

	/**
	 * 提示网络不通
	 */
	public void showTips() {
		if (handler == null) {
			handler = new Handler();
		}
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(AbsListCurMonthVolumeActivity.this, "网络连接未打开!",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

}
