package com.bonc.mobile.hbmclient.terminal.subactivity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.KpiTitleView;
import com.bonc.mobile.hbmclient.view.ListViewSetting;
import com.bonc.mobile.hbmclient.view.ToastNotice;
import com.bonc.mobile.hbmclient.view.adapter.TerminalLeftAdapter;
import com.bonc.mobile.hbmclient.view.adapter.TerminalRightAdapter;

/** 统计列表展示表格模块 */
public abstract class AbsStatisticsListActivity extends BaseTerminalActivity {
	/* 以下为测试数据 */
	final public static String DATA_ARRAY_KEY = "data";

	/* 单元格宽度 */
	/** 这个变量表示你的onLoading是异步的（后台线程）还是同步的。默认true，修改为false要慎重 */
	private boolean mIsAsynchronous = true;

	protected ListView mLeftList;
	private ListView mRightList;
	// 相关滑动用组件
	private ListViewSetting listViewSetting;
	private TextView mTitleView;
	// private LinearLayout mTopLayout;
	private TextView mTopText;
	private ProgressDialog mWaitingDia;
	private StatisticsTask mTask;
	private TerminalLeftAdapter mLeftAdapter;
	private TerminalRightAdapter mRightAdapter;
	private JSONObject mReturn; // 封装过的返回json
	private String[] mLeftTag;
	private String[] mKeys = null;
	protected String[] mKeyFilters = null;
	private String[][] mRightTag;
	private String[] columnNames;

	private boolean firstLoad = true;
	protected Map<String, String> mAreaNameCode = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_Translucent_NoTitleBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.terminal_list_lay);
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.id_terminal_list_lay);
		rl.setBackgroundDrawable(WatermarkImage.getWatermarkDrawable());

		mWaitingDia = new ProgressDialog(this);
		mTitleView = (TextView) findViewById(R.id.statistics_title_view);
		mLeftList = (ListView) findViewById(R.id.terminal_l_listview);
		View footer = LayoutInflater.from(this).inflate(
				R.layout.list_foot_view, null);
		this.mLeftList.addFooterView(footer, null, false);
		mRightList = (ListView) findViewById(R.id.terminal_r_listview);
		this.mRightList.addFooterView(footer, null, false);
		listViewSetting = new ListViewSetting();
		// mTopLayout = (LinearLayout) findViewById(R.id.statistics_top_layout);
		// mTopLayout.setBackgroundResource(R.drawable.glay_list_title_bar);
		// mTopText = (TextView) findViewById(R.id.top_text_view0);
		// mTopText.getLayoutParams().width=android.widget.FrameLayout.LayoutParams.MATCH_PARENT;
		mKeys = onGetKeys(savedInstanceState);
		columnNames = onGetColumnNames(savedInstanceState);
		String title = onGetTitle(savedInstanceState);
		if (title != null) {
			mTitleView.setText(title);
			setTitle(title);
		}

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		refresh();
	}

	/** 刷新数据<u>并呈现</u> */
	protected void refresh() {
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
		columnNames = onGetColumnNames(null);

		if (columnNames != null) {

			changeTitles(columnNames);
		}
		if (!mIsAsynchronous) {
			mReturn = onLoading(mKeys);
			parseResponseData();
			return;
		}
		if (mTask == null) {
			mTask = new StatisticsTask();
		}
		if (mTask.getStatus() == Status.RUNNING) {
			mTask.cancel(true);
		}
		mTask = new StatisticsTask();
		mTask.execute(mKeys);
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
		// KpiHasChartRightView(AbsStatisticsListActivity.this,R.layout.title_right_item,str,1,getWindowManager().getDefaultDisplay().getWidth()-this.getResources().getDimension(R.dimen.zhl_left_column_width));

		final KpiTitleView ret = new KpiTitleView(
				AbsStatisticsListActivity.this, R.layout.title_right_sortitem,
				str, 1, getWindowManager().getDefaultDisplay().getWidth()
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

				int len = mRightTag.length;

				if (state == KpiTitleView.TITLE_SORT_UP) {
					// 升序
					for (int i = 1; i < len - 1; i++) {

						for (int k = i + 1; k < len; k++) {

							double value1 = default_max_value;
							try {
								value1 = Double.parseDouble(mRightTag[i][col]);// areaData.get(i).get(col));
							} catch (Exception e) {
								e.printStackTrace();
							}

							double value2 = default_max_value;
							try {
								value2 = Double.parseDouble(mRightTag[k][col]);
							} catch (Exception e) {
								e.printStackTrace();
							}

							// 如果小于 则交换.
							if (value2 < value1) {
								String tempArea = mLeftTag[i];
								mLeftTag[i] = mLeftTag[k];
								mLeftTag[k] = tempArea;

								String[] temp = new String[mKeyFilters.length];
								System.arraycopy(mRightTag[i], 0, temp, 0,
										mKeyFilters.length);
								System.arraycopy(mRightTag[k], 0, mRightTag[i],
										0, mKeyFilters.length);
								System.arraycopy(temp, 0, mRightTag[k], 0,
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
								value1 = Double.valueOf(mRightTag[i][col]);
							} catch (Exception e) {
								e.printStackTrace();
							}

							double value2 = default_min_value;
							try {
								value2 = Double.valueOf(mRightTag[k][col]);
							} catch (Exception e) {
								e.printStackTrace();
							}

							// 如果小于 则交换.
							if (value2 > value1) {
								String tempArea = mLeftTag[i];
								mLeftTag[i] = mLeftTag[k];
								mLeftTag[k] = tempArea;

								String[] temp = new String[mKeyFilters.length];
								System.arraycopy(mRightTag[i], 0, temp, 0,
										mKeyFilters.length);
								System.arraycopy(mRightTag[k], 0, mRightTag[i],
										0, mKeyFilters.length);
								System.arraycopy(temp, 0, mRightTag[k], 0,
										mKeyFilters.length);

							}
						}
					}
				}

				mLeftAdapter = new TerminalLeftAdapter(
						AbsStatisticsListActivity.this, mLeftTag);
				mLeftList.setAdapter(mLeftAdapter);
				mRightAdapter = new TerminalRightAdapter(
						AbsStatisticsListActivity.this, mRightTag, mKeyFilters);

				mRightList.setAdapter(mRightAdapter);

				ret.setSelectSortIndex(childIndex);// 将当前指标选为被选中项
				ret.setSelectStatics(state);
				ret.refresh();
			}
		});
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mWaitingDia.dismiss();
	}

	/**
	 * 
	 * Note:该方法将视乎mIsAsynchronous的值而异步(为true时)或直接(为false时)调用
	 * 
	 * @param keys
	 *            你在onGetKeys中返回的key
	 * @return 应返回一个以符合文档中标准的json来封装的jsonObject.
	 */
	abstract protected JSONObject onLoading(String[] keys);

	/** 应返回标题，将在屏幕上方显示 */
	abstract protected String onGetTitle(Bundle savedInstanceState);

	/**
	 * 返回列名称
	 * 
	 * @param savedInstanceState
	 * @return
	 */
	abstract protected String[] onGetColumnNames(Bundle savedInstanceState);

	/**
	 * 获取指标对应的列
	 */
	protected String[] onGetColumnKpi() {
		return new String[] {};
	};

	/** 应返回Key的数组，其中index为0的key将作为左侧首列 */
	abstract protected String[] onGetKeys(Bundle savedInstanceState);

	/** 后台异步加载task。不建议改写，有需要请另构一个 */
	private class StatisticsTask extends AsyncTask<String[], Void, JSONObject> {
		/** 错误号 */
		private int mmErrorNumber;

		/** 返回0表示成功 */
		private int getErrorNunber() {
			return mmErrorNumber;
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mmErrorNumber = 0;
			mWaitingDia.show();
		}

		@Override
		protected JSONObject doInBackground(String[]... params) {
			// TODO Auto-generated method stub
			JSONObject ret = onLoading(mKeys);
			return ret;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mWaitingDia.dismiss();
			if (result == null) {// 数据获取错误时的响应
				switch (mmErrorNumber) {
				case 404:

					break;
				case -1:
					Toast.makeText(AbsStatisticsListActivity.this,
							"数据格式有误，请联系系统管理员", Toast.LENGTH_SHORT).show();
				default:
					break;
				}
				return;
			}
			// 数据获取正确时的响应
			mReturn = result;
			parseResponseData();
		}
	}

	/** 在数据获取完毕后调用的方法 */
	private void parseResponseData() {
		mAreaNameCode.clear();
		JSONArray array = mReturn.optJSONArray(DATA_ARRAY_KEY);
		/* 构建数据 */
		mLeftTag = new String[array.length()];

		mRightTag = new String[array.length()][];

		for (int i = 0; i < array.length(); i++) {
			mLeftTag[i] = array.optJSONObject(i).optString(mKeys[0]);
			mRightTag[i] = new String[mKeys.length - 1];
			String code = array.optJSONObject(i).optString("area_code");
			mAreaNameCode.put(mLeftTag[i], code);
			for (int j = 1; j < mKeys.length; j++) {
				mRightTag[i][j - 1] = array.optJSONObject(i)
						.optString(mKeys[j]);
			}
		}
		/* 以数据构建适配器并命令呈现 */
		mLeftAdapter = new TerminalLeftAdapter(AbsStatisticsListActivity.this,
				mLeftTag);
		mLeftList.setAdapter(mLeftAdapter);
		mRightAdapter = new TerminalRightAdapter(
				AbsStatisticsListActivity.this, mRightTag, mKeyFilters);

		mRightList.setAdapter(mRightAdapter);
		listViewSetting.setListViewOnTouchAndScrollListener(mLeftList,
				mRightList);
		if (this.firstLoad) {
			String name = this.getClass().getName();
			if (DaySaleCountStaticsActivity.class.getName().equals(name)) {
				new ToastNotice(AbsStatisticsListActivity.this, Gravity.TOP,
						80, 50).show();
			} else if (SaleAnalysisActivity.class.getName().equals(name)) {
				new ToastNotice(AbsStatisticsListActivity.this, Gravity.TOP,
						80, 100).show();
			}
			this.firstLoad = false;
		}

	}
}
