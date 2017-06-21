package com.bonc.mobile.hbmclient.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.asyn_task.ChannelSummaryAsynTask;
import com.bonc.mobile.hbmclient.asyn_task.ChannelSummaryAsynTask.OnPostListener;
import com.bonc.mobile.hbmclient.asyn_task.DailyReportAsynTask;
import com.bonc.mobile.hbmclient.command.business_outlets.DateSelector;
import com.bonc.mobile.hbmclient.command.business_outlets.DateSelector.OnDateSelectListener;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter.ColumnDisplyInfo;
import com.bonc.mobile.hbmclient.state.business_outlets.item.IBOItem;
import com.bonc.mobile.hbmclient.state.business_outlets.kpi.compare.ICompareState2;
import com.bonc.mobile.hbmclient.state.business_outlets.kpi.compare.NewCustomASCState;
import com.bonc.mobile.hbmclient.state.business_outlets.kpi.compare.NewCustomDESCState;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.util.NumberUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.ListViewSetting;
import com.bonc.mobile.hbmclient.view.NCRightView;

public class NewCustomActivity extends BaseActivity {
	public final static String KEY_DATE = "date";
	public final static String KEY_KPI_CODE = "kpiCode";
	boolean hasMeasured = false;
	int rightWidth;
	//
	Handler handler = new Handler();
	// 标题栏名
	private String[] titleName;
	// 左侧列表
	private ListView left_listView;
	// 右侧列表
	private ListView right_listView;
	// 列表相应的Adapter
	private NCLeftAdapter leftAdapter;
	private NCRightAdapter rightAdapter;
	// 相关滑动用组件
	private ListViewSetting listViewSetting;
	private List<ColumnDisplyInfo[]> mColT;
	private List<String> nameData;
	private String kpiCode;
	private ChannelSummaryAsynTask mTask;
	private DateSelector dateSelector;
	private TextView kpiNameTV;
	private Button dateSelect;
	private DecimalFormat decimalFormat;

	private ICompareState2 state, ascState, descState;
	private List<Map<String, String>> data;
	private NCRightView ret;
	private boolean isFirst = true;
	private DailyReportAsynTask noticeTask;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_NoTitleBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_newcustom);
		findViewById(R.id.parent).setBackgroundDrawable(
				WatermarkImage.getWatermarkDrawable());
		this.kpiCode = getIntent().getStringExtra(KEY_KPI_CODE);
		String date = getIntent().getStringExtra(KEY_DATE);
		this.dateSelector = new DateSelector(this);
		this.dateSelector.setDate(date);
		this.decimalFormat = new DecimalFormat(",###");
		this.ascState = new NewCustomASCState(this);
		this.descState = new NewCustomDESCState(this);
		this.state = this.ascState;
		initWidget();
		initData();
	}

	public void initWidget() {
		TextView titleTV = (TextView) findViewById(R.id.titleName);
		titleTV.setText("关注网点指标展示");
		titleName = new String[] { "网点", "当日值", "日累计" };
		left_listView = (ListView) findViewById(R.id.nc_left_listview);
		right_listView = (ListView) findViewById(R.id.nc_right_listview);
		rightWidth = (int) (getWindowManager().getDefaultDisplay().getWidth()
				- getResources().getDimension(R.dimen.nc_left_column_width) - 16);
		leftAdapter = new NCLeftAdapter();
		rightAdapter = new NCRightAdapter();
		left_listView.setAdapter(leftAdapter);
		right_listView.setAdapter(rightAdapter);
		listViewSetting = new ListViewSetting();
		listViewSetting.setListViewOnTouchAndScrollListener(left_listView,
				right_listView);
		this.kpiNameTV = (TextView) findViewById(R.id.kpiName);
		dateSelect = (Button) findViewById(R.id.id_date_select);
		dateSelect.setText(this.dateSelector.getShowDate());
		dateSelect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dateSelector.chooseDate();
			}
		});
		this.dateSelector.setOnDateSelectListener(new OnDateSelectListener() {

			@Override
			public void onDateSelect(DateSelector ds) {
				dateSelect.setText(ds.getShowDate());
				initData();
			}
		});
	}

	public void initData() {
		getDataRemote();
	}

	private void changeTitle(String[] titleName) {
		TextView tv = (TextView) findViewById(R.id.nc_title);
		tv.setText(titleName[0]);
		LinearLayout layRightTitle = (LinearLayout) findViewById(R.id.lineralayout_right_title);
		String str[] = new String[titleName.length - 1];
		for (int i = 0; i < str.length; i++) {
			str[i] = titleName[i + 1];
		}

		ret = new NCRightView(NewCustomActivity.this, str, rightWidth, true);

		layRightTitle.removeAllViews();
		layRightTitle.addView(ret);
		ret.setViewClickListener(0, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				state.sort(data, 0, v);
			}
		});
		ret.setViewClickListener(1, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				state.sort(data, 1, v);
			}
		});
		if (data != null) {
			if (isFirst) {
				noticeTask = new DailyReportAsynTask(ret, "点击可排序");
				noticeTask.execute();
				isFirst = false;
			} else {

			}
		}
	}

	/**
	 * 远程查询数据.
	 */
	public boolean getDataRemote() {
		this.mTask = new ChannelSummaryAsynTask(this, new OnPostListener() {
			@Override
			public void onPost(String s) {
				parseData(s);
			}
		});
		Map<String, String> questMap = new HashMap<String, String>();
		questMap.put(IBOItem.KEY_DATE, this.dateSelector.getServerDate());
		questMap.put(KEY_KPI_CODE, this.kpiCode);
		this.mTask.execute(
				ActionConstant.GET_BUSINESS_OUTLETS_FOCUS_WEBSITE_KPI_SHOW,
				questMap);
		return true;
	}

	private void parseData(String s) {
		JSONObject jo = null;

		try {
			if (s == null) {
				jo = new JSONObject();
			} else {
				jo = new JSONObject(s);
			}

			String validate = jo.optString("validate");
			if ("1".equals(validate)) {
				JSONObject headJO = jo.optJSONObject("header");
				Map<String, String> header = JsonUtil.toMap(headJO);
				if (header == null) {
					header = new HashMap<String, String>();
				}
				String kpiName = header.get("kpiName");
				if (kpiName == null) {
					kpiName = "--";
				}
				this.kpiNameTV.setText(kpiName);
				String kpiUnit = header.get("kpiUnit");
				if (kpiUnit == null) {
					kpiUnit = "-";
				}
				kpiUnit = "(" + kpiUnit + ")";
				titleName[1] = "当日值" + kpiUnit;
				titleName[2] = "日累计" + kpiUnit;

				JSONArray dataJA = jo.optJSONArray("data");
				data = JsonUtil.toList(dataJA);
				if (data == null) {
					data = new ArrayList<Map<String, String>>();
				}
				this.nameData = new ArrayList<String>();
				this.mColT = new ArrayList<ColumnDataFilter.ColumnDisplyInfo[]>();
				for (int i = 0; i < data.size(); i++) {
					Map<String, String> item = data.get(i);
					String websiteName = item.get("areaName");
					if (websiteName == null) {
						websiteName = "--";
					}
					nameData.add(websiteName);
					ColumnDisplyInfo[] nowColumnDisplyInfos = new ColumnDisplyInfo[2];
					String type = item.get("type");
					String keyCurDayValue = null;
					String keyCurMonthValue = null;
					if ("private".equalsIgnoreCase(type)) {
						keyCurDayValue = "privateCurrent";
						keyCurMonthValue = "privateTotal";
					} else {
						keyCurDayValue = "publicCurrent";
						keyCurMonthValue = "publicTotal";
					}
					String curDayValue = item.get(keyCurDayValue);
					curDayValue = getValue(curDayValue);
					ColumnDisplyInfo cdi = ColumnDataFilter.getInstance()
							.doFilter(null, null, curDayValue);
					nowColumnDisplyInfos[0] = cdi;

					String curMonthValue = item.get(keyCurMonthValue);
					curMonthValue = getValue(curMonthValue);
					ColumnDisplyInfo cdi2 = ColumnDataFilter.getInstance()
							.doFilter(null, null, curMonthValue);
					nowColumnDisplyInfos[1] = cdi2;
					mColT.add(nowColumnDisplyInfos);
				}
				changeTitle(titleName);
				leftAdapter.notifyDataSetChanged();
				rightAdapter.notifyDataSetChanged();
			} else {
				String msg = jo.optString("msg");
				Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(this, "数据加载异常", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {

	}

	public void showtip(String message) {
		toast(NewCustomActivity.this, message);
	}

	public void nullDataRemind() {

		// toast(KPIHomeActivity.this, getString(R.string.no_data));
		handler.post(new Runnable() {
			@Override
			public void run() {
				removeDialog(LOADING_DIALOG);

			}
		});

	}

	class NCLeftAdapter extends BaseAdapter {
		public NCLeftAdapter() {
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return nameData != null ? nameData.size() : 0;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final int nowP = position;
			// TODO Auto-generated method stub
			ViewHolder holder = null;

			if (convertView == null) {
				convertView = LayoutInflater.from(NewCustomActivity.this)
						.inflate(R.layout.nc_list_item_left, null);
				holder = new ViewHolder();
				holder.pointName = (TextView) convertView
						.findViewById(R.id.nc_index_name);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();

			}
			String nowName = nameData.get(position);
			if (nowName == null) {
				nowName = "--";
			}
			holder.pointName.setText(nowName);
			return convertView;
		}

		class ViewHolder {

			TextView pointName;

		}
	}

	class NCRightAdapter extends BaseAdapter {
		public NCRightAdapter() {
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mColT != null ? mColT.size() : 0;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final int nowP = position;
			// TODO Auto-generated method stub
			NCRightView ret;
			ViewHolder holder = null;

			if (convertView == null) {
				convertView = LayoutInflater.from(NewCustomActivity.this)
						.inflate(R.layout.nc_index_list_item_right, null);
				holder = new ViewHolder();
				holder.linearlayout = (LinearLayout) convertView
						.findViewById(R.id.zhl_right_static);
				ret = new NCRightView(NewCustomActivity.this,
						mColT.get(position), rightWidth, true);

				holder.linearlayout.removeAllViews();
				holder.linearlayout.addView(ret, 0);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				ret = new NCRightView(NewCustomActivity.this,
						mColT.get(position), rightWidth, true);

				holder.linearlayout.removeAllViews();
				holder.linearlayout.addView(ret, 0);
			}

			return convertView;
		}

		class ViewHolder {

			LinearLayout linearlayout;
		}
	}

	private String getValue(String value) {
		String temp = null;
		try {
			temp = this.decimalFormat.format(NumberUtil.changeToDouble(value));
		} catch (Exception e) {
			temp = "--";
		}

		return temp;
	}

	/**
	 * @return the ascState
	 */
	public ICompareState2 getAscState() {
		return ascState;
	}

	/**
	 * @return the descState
	 */
	public ICompareState2 getDescState() {
		return descState;
	}

	public void setSortState(ICompareState2 state) {
		this.state = state;
	}

	public void resetSort() {
		this.ret.setViewCompoundDrawable(0, 0);
		this.ret.setViewCompoundDrawable(1, 0);
		this.nameData = new ArrayList<String>();
		this.mColT = new ArrayList<ColumnDataFilter.ColumnDisplyInfo[]>();
		for (int i = 0; i < data.size(); i++) {
			Map<String, String> item = data.get(i);
			String websiteName = item.get("areaName");
			if (websiteName == null) {
				websiteName = "--";
			}
			nameData.add(websiteName);
			ColumnDisplyInfo[] nowColumnDisplyInfos = new ColumnDisplyInfo[2];
			String type = item.get("type");
			String keyCurDayValue = null;
			String keyCurMonthValue = null;
			if ("private".equalsIgnoreCase(type)) {
				keyCurDayValue = "privateCurrent";
				keyCurMonthValue = "privateTotal";
			} else {
				keyCurDayValue = "publicCurrent";
				keyCurMonthValue = "publicTotal";
			}
			String curDayValue = item.get(keyCurDayValue);
			curDayValue = getValue(curDayValue);
			ColumnDisplyInfo cdi = ColumnDataFilter.getInstance().doFilter(
					null, null, curDayValue);
			nowColumnDisplyInfos[0] = cdi;

			String curMonthValue = item.get(keyCurMonthValue);
			curMonthValue = getValue(curMonthValue);
			ColumnDisplyInfo cdi2 = ColumnDataFilter.getInstance().doFilter(
					null, null, curMonthValue);
			nowColumnDisplyInfos[1] = cdi2;
			mColT.add(nowColumnDisplyInfos);
		}

		leftAdapter.notifyDataSetChanged();
		rightAdapter.notifyDataSetChanged();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.activity.MenuActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		if (this.noticeTask != null) {
			if (this.noticeTask.isCancelled()) {

			} else {
				this.noticeTask.destroy();
			}
		}
		super.onDestroy();
	}

}
