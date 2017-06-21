package com.bonc.mobile.hbmclient.activity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.HttpUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.MyDatePickerDialog;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;

public class CertQueryActivity extends SlideHolderActivity {
	private ListView mListView;
	private Button dateSelect;
	private String optime;
	private Calendar calendar;
	private JSONArray mJSONArray;

	private final String KEY_OPTIME = "datatime";

	private final String KEY_TIME = "send_time";
	private final String KEY_IMEI = "imei";
	private final String KEY_IMSI = "imsi";
	private final String KEY_PHONE = "mobile_no";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setTheme(android.R.style.Theme_NoTitleBar);
		super.onCreate(savedInstanceState);
		setMainContent(
				getIntent().getStringExtra(MenuEntryAdapter.KEY_MENU_CODE),
				R.layout.layout_cert_query_activity);
		LinearLayout ll = (LinearLayout) findViewById(R.id.parent);
		ll.setBackgroundDrawable(WatermarkImage.getWatermarkDrawable());

		TextView tv_title = (TextView) this
				.findViewById(R.id.logo_word_mon_dev);
		tv_title.setText("用户发送信息查询");
		Button navigator = (Button) this.findViewById(R.id.id_navigator);
		navigator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				slideHolder.toggle();
			}
		});

		optime = DateUtil.formatter(Calendar.getInstance().getTime(),
				DateUtil.PATTERN_9);
		calendar = DateUtil.getCalendar(optime, DateUtil.PATTERN_9);

		this.mListView = (ListView) this.findViewById(R.id.id_cert_list);
		View header = LayoutInflater.from(this).inflate(
				R.layout.layout_cert_list_item, null);
		header.setBackgroundDrawable(getResources().getDrawable(
				R.mipmap.glay_list_title_bar));
		this.mListView.addHeaderView(header, null, false);
		View footer = LayoutInflater.from(this).inflate(
				R.layout.list_foot_view, null);
		this.mListView.addFooterView(footer, null, false);
		this.dateSelect = (Button) this
				.findViewById(R.id.id_date_choice_button);
		this.dateSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDatePicker();
			}
		});

		initData();
	}

	private DatePickerDialog mDateDia; // 日期选择框.

	public void showDatePicker() {

		if (mDateDia != null && mDateDia.isShowing()) {
			mDateDia.dismiss();
		}

		mDateDia = new MyDatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				calendar.set(year, monthOfYear, dayOfMonth);
				optime = DateUtil.formatter(calendar.getTime(),
						DateUtil.PATTERN_9);

				initData();

			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		mDateDia.show();
	}

	private void nullDataRemind() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				removeDialog(LOADING_DIALOG);
				Toast.makeText(CertQueryActivity.this,
						getString(R.string.no_data), Toast.LENGTH_SHORT).show();
				setWidgeNodata();
			}

		});
	}

	private void setWidgeNodata() {
		// TODO Auto-generated method stub
		dateSelect.setText("未查获数据");
	}

	public void initData() {

		showDialog(LOADING_DIALOG);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 获得数据
				if (!getDataRemote()) {
					nullDataRemind();
					return;
				}

				// 开始布局
				handler.post(new Runnable() {
					@Override
					public void run() {

						ListAdapter listAdapter = new ListAdapter(
								CertQueryActivity.this, mJSONArray);
						mListView.setAdapter(listAdapter);
						String showTime = DateUtil.oneStringToAntherString(
								optime, DateUtil.PATTERN_9, "yyyy/MM/dd");
						dateSelect.setText(showTime);
						removeDialog(LOADING_DIALOG);
					}
				});
			}
		}).start();
	}

	private boolean getDataRemote() {

		Map<String, String> param = new HashMap<String, String>();

		param.put(KEY_OPTIME, optime);
		param.put("menuCode",
				getIntent().getStringExtra(MenuEntryAdapter.KEY_MENU_CODE));

		String json_reply = null;
		try {

			json_reply = HttpUtil.sendRequest(
					ActionConstant.GET_CERTQUERY_INFO, param);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		if (json_reply == null || "".equals(json_reply)) {
			return false;
		}
		try {

			mJSONArray = new JSONArray(json_reply);

		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private class ListAdapter extends BaseAdapter {
		private Context mContext;
		private LayoutInflater mInflater;
		private JSONArray jsonArray;

		public ListAdapter(Context c, JSONArray json) {
			mContext = c;
			mInflater = LayoutInflater.from(c);
			jsonArray = json;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return jsonArray.length();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.layout_cert_list_item,
						null);
			}
			TextView tv_time = (TextView) convertView
					.findViewById(R.id.id_cert_time);
			TextView tv_imei = (TextView) convertView
					.findViewById(R.id.id_cert_imei);
			TextView tv_imsi = (TextView) convertView
					.findViewById(R.id.id_cert_imsi);
			TextView tv_phone = (TextView) convertView
					.findViewById(R.id.id_cert_phone);
			if (position % 2 == 0) {
				convertView.setBackgroundColor(mContext.getResources()
						.getColor(R.color.zeven_list_color));
			} else {
				convertView.setBackgroundColor(mContext.getResources()
						.getColor(R.color.zodd_list_color));
			}
			JSONObject jo;
			try {
				jo = jsonArray.getJSONObject(position);
				String time = jo.optString(KEY_TIME);
				String imei = jo.optString(KEY_IMEI);
				String imsi = jo.optString(KEY_IMSI);
				String phone = jo.optString(KEY_PHONE);
				tv_time.setText(time);
				tv_imei.setText(imei);
				tv_imsi.setText(imsi);
				tv_phone.setText(phone);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return convertView;
		}
	}

}
