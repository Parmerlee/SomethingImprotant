package com.bonc.mobile.hbmclient.terminal.subactivity;

import java.util.HashMap;
import java.util.Map;

import org.achartengine.chart.BarChart;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.terminal.view.SectionChartView;
import com.bonc.mobile.hbmclient.util.LogUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.KpiTitleView;
import com.bonc.mobile.hbmclient.view.ListViewSetting;
import com.bonc.mobile.hbmclient.view.ToastNotice;
import com.bonc.mobile.hbmclient.view.adapter.TerminalLeftAdapter;
import com.bonc.mobile.hbmclient.view.adapter.TerminalRightAdapter;

/** 上面柱形图 下方表格 */
public abstract class AbsSectionChartActivity extends BaseTerminalActivity
		implements AsynchronousGap {
	protected final static String KEY_CURRENT_MONTH_CHARTDATA = "part2";
	protected final static String KEY_LAST_MONTH_CHARTDATA = "part1";
	protected final static String KEY_REGIONAL_DATA = "part3";

	public final static int ORDER_REFRESH = 1208;
	public final static int ORDER_ORIGAN_REFRESH = 1209;

	protected ListView mRightList;
	protected ListView mLeftList;
	// 相关滑动用组件
	private ListViewSetting listViewSetting;
	private TextView mTitleView;
	protected SectionChartView mChartView;

	protected BarChart mChart;
	protected JSONObject mDataJson = null;
	private TerminalLeftAdapter mLeftAdapter;
	private TerminalRightAdapter mRightAdapter;

	private boolean mAsynchronous = false;
	private String[][] mListDatas;
	protected String[] mFirstColumns;
	private double[] mSeries1data;
	private double[] mSeries2data;
	private ProgressDialog mWaitingDia;
	private String[] mKeys;
	protected String[] mKeyFilters = null;
	protected Map<String, String> mAreaNameCode = new HashMap<String, String>();
	private String[] mColumnNames;
	private GetTask mTask;
	private boolean firstLoad = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_NoTitleBar); // 必须写到super.onCreate前面
													// 否则会失效.
		super.onCreate(savedInstanceState);
		mWaitingDia = new ProgressDialog(this);
		mWaitingDia.setCanceledOnTouchOutside(false);
		mWaitingDia.setCancelable(false);
		setContentView(R.layout.terminal_chartlist_layout);
		LinearLayout ll = (LinearLayout) findViewById(R.id.id_terminal_chartlist_layout);
		ll.setBackgroundDrawable(WatermarkImage.getWatermarkDrawable());

		mTitleView = (TextView) findViewById(R.id.section_title_tv);
		mTitleView.setText(onGetTitle(savedInstanceState));

		mLeftList = (ListView) findViewById(R.id.terminal_l_listview);
		View footer = LayoutInflater.from(this).inflate(
				R.layout.list_foot_view, null);
		this.mLeftList.addFooterView(footer, null, false);
		mRightList = (ListView) findViewById(R.id.terminal_r_listview);
		this.mRightList.addFooterView(footer, null, false);

		mColumnNames = onGetColumnNames(savedInstanceState);
		mChartView = (SectionChartView) findViewById(R.id.section_chart_view);

		TextView this_month_explain = (TextView) findViewById(R.id.section_this_month_tv);
		this_month_explain.setText("■ 当月");
		TextView last_month_explain = (TextView) findViewById(R.id.section_last_month_tv);
		last_month_explain.setText("■ 上月");

		ChangeListener l = new ChangeListener();
		mRightList.setOnItemClickListener(l);
		mLeftList.setOnItemClickListener(l);
		listViewSetting = new ListViewSetting();
		refresh();
	}

	/** 解析获取的json文件 */
	protected void parseResponse(JSONObject json) {

		LogUtil.info("llopop", "kkk " + json.toString());
		mAreaNameCode.clear();
		JSONArray list_json = json.optJSONArray(KEY_REGIONAL_DATA);
		JSONObject bar_json_1 = json.optJSONObject(KEY_CURRENT_MONTH_CHARTDATA);
		JSONObject bar_json_2 = json.optJSONObject(KEY_LAST_MONTH_CHARTDATA);
		if (list_json != null) {
			mListDatas = new String[list_json.length()][];
			mFirstColumns = new String[list_json.length()];
			for (int i = 0; i < list_json.length(); i++) {
				mListDatas[i] = new String[mKeys.length - 1];

				mFirstColumns[i] = list_json.optJSONObject(i).optString(
						mKeys[0]);
				String code = list_json.optJSONObject(i).optString("area_code");
				mAreaNameCode.put(mFirstColumns[i], code);
				for (int j = 1; j < mKeys.length; j++) {
					mListDatas[i][j - 1] = list_json.optJSONObject(i)
							.optString(mKeys[j]);
				}
			}
		}

		mSeries1data = new double[mKeys.length];
		mSeries2data = new double[mKeys.length];
		for (int i = 0; i < mKeys.length - 1; i++) {
			if (bar_json_1 != null) {
				mSeries1data[i] = bar_json_1.optDouble(mKeys[i + 1]);// 注意key[0]是忽略的
			}
			if (bar_json_2 != null) {
				mSeries2data[i] = bar_json_2.optDouble(mKeys[i + 1]);// 注意key[0]是忽略的
			}
		}
		mChartView.initialize(mSeries1data, mSeries2data);
		mChartView.invalidate();
		mLeftAdapter = new TerminalLeftAdapter(AbsSectionChartActivity.this,
				mFirstColumns);
		mLeftList.setAdapter(mLeftAdapter);
		mRightAdapter = new TerminalRightAdapter(AbsSectionChartActivity.this,
				mListDatas, mKeyFilters);

		mRightList.setAdapter(mRightAdapter);
		listViewSetting.setListViewOnTouchAndScrollListener(mLeftList,
				mRightList);
		if (this.firstLoad) {
			new ToastNotice(AbsSectionChartActivity.this, Gravity.TOP, 80, 300)
					.show();
			this.firstLoad = false;
		}
	}

	/**
	 * 刷新页面，所有数据将重载,但key不会变化
	 * 
	 * @return 非同步总是返回false
	 */
	protected boolean refresh() {
		mColumnNames = onGetColumnNames(null);
		String[] unparsedKeys = onGetKeys(null);
		mKeyFilters = new String[unparsedKeys.length - 1];
		mKeys = new String[unparsedKeys.length];
		for (int i = 0; i < unparsedKeys.length; i++) {
			String[] key_and_filter = unparsedKeys[i].split("\\|", 2);
			mKeys[i] = key_and_filter[0];
			if (key_and_filter.length > 1 && i > 0) {
				mKeyFilters[i - 1] = key_and_filter[1];
			}
		}
		if (mColumnNames != null) {

			changeTitles(mColumnNames);
		}

		for (int i = 0; i < mColumnNames.length; i++) {

			mChartView.getRenderer().removeXTextLabel(i);
			String s = mColumnNames[i];
			if (s.length() > 4) {
				s = mColumnNames[i].substring(0, 4) + "\n"
						+ mColumnNames[i].substring(4);
			}
			mChartView.getRenderer().setXRoundedLabels(true);
			mChartView.getRenderer().addXTextLabel(i, mColumnNames[i]);
		}

		if (mAsynchronous) {
			mDataJson = onLoading(mKeys);
			if (mDataJson != null) {
				parseResponse(mDataJson);
			}
			return mDataJson != null ? true : false;
		}
		if (mTask != null) {
			mTask.cancel(true);
		}
		mTask = new GetTask();
		mTask.execute();
		return mDataJson != null ? true : false;
	}

	/** 重置键值和键值名 */
	public void resetKeys() {
		mKeys = onGetKeys(null);
		mColumnNames = onGetColumnNames(null);
	}

	public boolean isAsynchronous() {
		return mAsynchronous;
	}

	public void setAsynchronous(boolean mAsynchronous) {
		this.mAsynchronous = mAsynchronous;
	}

	/** 异步加载数据类 */
	private class GetTask extends AsyncTask<Void, Integer, JSONObject> {
		@Override
		protected void onPreExecute() {
			mWaitingDia.show();
		};

		@Override
		protected JSONObject doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return onLoading(mKeys);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mWaitingDia.dismiss();
			if (result == null) {
				return;
			}
			mDataJson = result;
			if (mDataJson != null) {
				parseResponse(mDataJson);

			}
		}
	}

	/***/
	protected abstract void onListLineClicked(int index);

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getOrder()) {
		case ORDER_REFRESH:
			refresh();
			break;
		case ORDER_ORIGAN_REFRESH:

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	protected class ChangeListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			onListLineClicked(arg2);
		}
	}

	private void changeTitles(String[] columnNames) {
		// TODO Auto-generated method stub
		TextView tv = (TextView) findViewById(R.id.terminal_left_title);
		tv.setText(columnNames[0]);
		LinearLayout tl = (LinearLayout) findViewById(R.id.title_lineralayout);
		tl.setBackgroundResource(R.drawable.glay_list_title);
		LinearLayout layRightTitle = (LinearLayout) findViewById(R.id.terminal_right_title);
		String str[] = new String[columnNames.length - 1];
		for (int i = 0; i < str.length; i++) {
			str[i] = columnNames[i + 1];
		}

		// KpiHasChartRightView ret =new
		// KpiHasChartRightView(AbsSectionChartActivity.this,R.layout.title_right_item,str,1,getWindowManager().getDefaultDisplay().getWidth()-this.getResources().getDimension(R.dimen.zhl_left_column_width));
		final KpiTitleView ret = new KpiTitleView(AbsSectionChartActivity.this,
				R.layout.title_right_sortitem, str, 1, getWindowManager()
						.getDefaultDisplay().getWidth()
						- this.getResources().getDimension(
								R.dimen.zhl_left_column_width));

		// 添加指标说明
		String[] kpiCodes = onGetColumnKpi();

		if (kpiCodes != null && kpiCodes.length > 0) {
			ret.addKpiExplain(kpiCodes);
		}

		layRightTitle.removeAllViews();
		layRightTitle.addView(ret);
		ret.setAllBackgroundByID(R.drawable.glay_list_title);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, ret.getItemH());// (LayoutParams)
																		// ret.getLayoutParams();
		ret.setLayoutParams(lp);

		ret.setOnChildClickListener(new KpiTitleView.onChildClickListener() {
			@Override
			public void onClickChild(int childIndex, int selectIndex,
					int selectStatic) {
				// Toast.makeText(KPIAreaActivity.this, colKeyList[childIndex],
				// Toast.LENGTH_SHORT).show();

				if (childIndex > mKeyFilters.length - 1) {
					return;
				}

				int state = KpiTitleView.TITLE_SORT_UP;

				if (childIndex == selectIndex) {
					if (selectStatic == KpiTitleView.TITLE_SORT_UP) {
						state = KpiTitleView.TITLE_SORT_DOWN;
					} else {
						state = KpiTitleView.TITLE_SORT_UP;
					}
				}

				double default_max_value = 99999999999999999L;
				double default_min_value = -9999999999999999L;

				// String col = mKeys[childIndex + 1];
				int col = childIndex;

				if (mListDatas == null)
					return;
				int len = mListDatas.length;

				if (state == KpiTitleView.TITLE_SORT_UP) {
					// 升序
					for (int i = 1; i < len - 1; i++) {

						for (int k = i + 1; k < len; k++) {

							double value1 = default_max_value;
							try {
								value1 = Double.parseDouble(mListDatas[i][col]);// areaData.get(i).get(col));
							} catch (Exception e) {
								e.printStackTrace();
							}

							double value2 = default_max_value;
							try {
								value2 = Double.parseDouble(mListDatas[k][col]);
							} catch (Exception e) {
								e.printStackTrace();
							}

							// 如果小于 则交换.
							if (value2 < value1) {
								String tempArea = mFirstColumns[i];
								mFirstColumns[i] = mFirstColumns[k];
								mFirstColumns[k] = tempArea;

								String[] temp = new String[mKeyFilters.length];
								System.arraycopy(mListDatas[i], 0, temp, 0,
										mKeyFilters.length);
								System.arraycopy(mListDatas[k], 0,
										mListDatas[i], 0, mKeyFilters.length);
								System.arraycopy(temp, 0, mListDatas[k], 0,
										mKeyFilters.length);

							}
						}
					}
				} else {
					// 降序
					for (int i = 1; i < len - 1; i++) {
						for (int k = i + 1; k < len; k++) {
							double value1 = default_min_value;
							try {
								value1 = Double.valueOf(mListDatas[i][col]);
							} catch (Exception e) {
								e.printStackTrace();
							}

							double value2 = default_min_value;
							try {
								value2 = Double.valueOf(mListDatas[k][col]);
							} catch (Exception e) {
								e.printStackTrace();
							}

							// 如果小于 则交换.
							if (value2 > value1) {
								String tempArea = mFirstColumns[i];
								mFirstColumns[i] = mFirstColumns[k];
								mFirstColumns[k] = tempArea;

								String[] temp = new String[mKeyFilters.length];
								System.arraycopy(mListDatas[i], 0, temp, 0,
										mKeyFilters.length);
								System.arraycopy(mListDatas[k], 0,
										mListDatas[i], 0, mKeyFilters.length);
								System.arraycopy(temp, 0, mListDatas[k], 0,
										mKeyFilters.length);

							}
						}
					}
				}

				mLeftAdapter = new TerminalLeftAdapter(
						AbsSectionChartActivity.this, mFirstColumns);
				mLeftList.setAdapter(mLeftAdapter);
				mRightAdapter = new TerminalRightAdapter(
						AbsSectionChartActivity.this, mListDatas, mKeyFilters);

				mRightList.setAdapter(mRightAdapter);

				ret.setSelectSortIndex(childIndex);// 将当前指标选为被选中项
				ret.setSelectStatics(state);
				ret.refresh();
			}
		});
	}

	/**
	 * 获取指标对应的列
	 */
	protected String[] onGetColumnKpi() {
		return new String[] {};
	};

}
