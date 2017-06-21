package com.bonc.mobile.hbmclient.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.HttpUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.AutoFitView;
import com.bonc.mobile.hbmclient.view.MyDatePickerDialog;
import com.bonc.mobile.hbmclient.view.adapter.LoginStatisticsAdapter;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;

public class LoginStatisticsActivity extends SlideHolderActivity {
	private String termType = "ANDROID";

	Handler handler = new Handler();

	// 布局控件
	private TextView date_select_new2;// 填写日期
	private TextView platform_choice;

	// 标题栏名
	private String[] titleName;
	// 标题栏指标Code
	private String[] titleCode;
	// 右侧列表
	private ListView area_right_listView;
	// 列表相应的Adapter

	private LoginStatisticsAdapter StatisAdapter;
	// 当前日期
	String optime;
	String showTime;
	Map<String, String> widgetData;
	// private TextView statics_title_name;

	private TextView to_day_title, to_day_account, to_day_people, to_day_num;
	private TextView day_title, day_people, day_num;
	private List<String[]> dataList; // 各行数值
	private DatePickerDialog mDateDia; // 日期选择框.
	private Calendar calendar; // 操作日期.

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_NoTitleBar);
		super.onCreate(savedInstanceState);
		setMainContent(
				getIntent().getStringExtra(MenuEntryAdapter.KEY_MENU_CODE),
				R.layout.login_statis_layout);
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.parent);
		rl.setBackgroundDrawable(WatermarkImage.getWatermarkDrawable());

		initWidget();
		initData();

	}

	public void initWidget() {
		// 显示当日时间
		to_day_title = (TextView) findViewById(R.id.to_day_title);
		to_day_account = (TextView) findViewById(R.id.to_day_account);
		to_day_people = (TextView) findViewById(R.id.to_day_people);
		to_day_num = (TextView) findViewById(R.id.to_day_num);

		day_title = (TextView) findViewById(R.id.day_title);

		day_people = (TextView) findViewById(R.id.day_people);
		day_num = (TextView) findViewById(R.id.day_num);

		date_select_new2 = (TextView) findViewById(R.id.date_select_new2);
		date_select_new2.setOnClickListener(this);
		platform_choice = (TextView) findViewById(R.id.id_platform_choice);
		platform_choice.setOnClickListener(this);
		optime = DateUtil.formatter(Calendar.getInstance().getTime(),
				DateUtil.PATTERN_8);
		showTime = DateUtil.oneStringToAntherString(optime, DateUtil.PATTERN_8,
				"yyyy年MM月dd日");
		to_day_title.setText("截至" + showTime + "的登录统计");
		showTime = DateUtil.oneStringToAntherString(optime, DateUtil.PATTERN_8,
				"yyyy/MM/dd");
		date_select_new2.setText(showTime);
		day_title.setText(showTime);

		calendar = DateUtil.getCalendar(optime, DateUtil.PATTERN_8);

		area_right_listView = (ListView) findViewById(R.id.lsl_listview);

		TextView tv_title = (TextView) this
				.findViewById(R.id.logo_word_mon_dev);
		tv_title.setText("每日登录情况展示");
		Button navigator = (Button) this.findViewById(R.id.id_navigator);
		navigator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				slideHolder.toggle();
			}
		});

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

						if (widgetData != null && widgetData.size() > 0)
							setWidgeData(widgetData);
						else
							nullDataRemind();
						if (dataList != null && dataList.size() > 0) {
							if (titleName != null && titleName.length != 0) {
								changeTitle(titleName);

							}

						} else {
							LinearLayout layRightTitle = (LinearLayout) findViewById(R.id.lineralayout_right_title);
							layRightTitle.removeAllViews();
						}
						// TODO Auto-generated method stub

						StatisAdapter = new LoginStatisticsAdapter(
								LoginStatisticsActivity.this, dataList);

						area_right_listView.setAdapter(StatisAdapter);

						showTime = DateUtil.oneStringToAntherString(optime,
								DateUtil.PATTERN_8, "yyyy年MM月dd日");
						to_day_title.setText("截至" + showTime + "的登录统计");
						showTime = DateUtil.oneStringToAntherString(optime,
								DateUtil.PATTERN_8, "yyyy/MM/dd");
						date_select_new2.setText(showTime);
						day_title.setText(showTime);

						removeDialog(LOADING_DIALOG);
					}
				});
			}
		}).start();
	}

	/**
	 * 设置数字部分为“未查获”
	 */
	private void setWidgeNodata() {
		// TODO Auto-generated method stub
		to_day_account.setText("未查获数据");
		to_day_people.setText("未查获数据");
		to_day_num.setText("未查获数据");

		day_people.setText("未查获数据");
		day_num.setText("未查获数据");
	}

	/**
	 * 设置上部信息
	 */
	private void setWidgeData(Map<String, String> widgetD) {
		// TODO Auto-generated method stub
		try {
			to_day_account.setText(widgetD.get("tda"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			to_day_account.setText("未查获数据");
		}
		try {
			to_day_people.setText(widgetD.get("tdp"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			to_day_people.setText("未查获数据");

		}
		try {
			to_day_num.setText(widgetD.get("tdn"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			to_day_num.setText("未查获数据");

		}

		try {
			day_people.setText(widgetD.get("dp"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			day_people.setText("未查获数据");

		}
		try {
			day_num.setText(widgetD.get("dn"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			day_num.setText("未查获数据");
		}
	}

	/**
	 * 远程查询数据.
	 */
	public boolean getDataRemote() {

		Map<String, String> param = new HashMap<String, String>();

		param.put("datatime", optime);
		param.put("termtype", "");
		param.put("menuCode",
				getIntent().getStringExtra(MenuEntryAdapter.KEY_MENU_CODE));

		String json_reply = null;
		try {

			json_reply = HttpUtil.sendRequest(ActionConstant.GET_LOGIN_SUM,
					param);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		if (json_reply == null || "".equals(json_reply)) {
			return false;
		}

		JSONObject jsonOA = null;
		try {

			jsonOA = new JSONObject(json_reply);

		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		{
			// 获得头部信息,如果信息为null或""则设定为“未查获数据”
			JSONObject jsonOS = null;
			String tda = "未查获数据";
			String tdp = "未查获数据";
			String tdn = "未查获数据";
			String dp = "未查获数据";
			String dn = "未查获数据";
			try {
				jsonOS = jsonOA.getJSONObject("dataall");
				tda = jsonOS.optString("tda");
				tdp = jsonOS.optString("tdp");
				tdn = jsonOS.optString("tdn");
				dp = jsonOS.optString("dp");
				dn = jsonOS.optString("dn");
				if (tda == null || "".equals(tda) || "null".equals(tda)) {
					tda = "未查获数据";
				}
				if (tdp == null || "".equals(tdp) || "null".equals(tdp)) {
					tdp = "未查获数据";
				}
				if (tdn == null || "".equals(tdn) || "null".equals(tdn)) {
					tdn = "未查获数据";
				}
				if (dp == null || "".equals(dp) || "null".equals(dp)) {
					dp = "未查获数据";
				}
				if (dn == null || "".equals(dn) || "null".equals(dn)) {
					dn = "未查获数据";
				}

			} catch (JSONException e) {
				e.printStackTrace();
				// setSEmpty();
			}
			widgetData = new HashMap<String, String>();
			widgetData.put("tda", tda);
			widgetData.put("tdp", tdp);
			widgetData.put("tdn", tdn);
			widgetData.put("dp", dp);
			widgetData.put("dn", dn);
		}

		// 获得Title数据,,如果信息为null或""则设定为“指标”+i
		titleName = new String[4];
		titleName[0] = "所属部门";
		titleName[1] = "用户名";
		titleName[2] = "登陆次数";
		titleName[3] = "类型";
		titleCode = new String[4];
		titleCode[0] = "user_self_area_name";
		titleCode[1] = "user_name";
		titleCode[2] = "login_count";
		titleCode[3] = "term_type";

		// 获得列表数据
		dataList = new ArrayList<String[]>();
		JSONArray jsonOD = null;
		try {
			jsonOD = jsonOA.getJSONArray("logdetallist");
			for (int i = 0; i < jsonOD.length(); i++) {
				JSONObject jsonODT = jsonOD.getJSONObject(i);
				String[] nowS = new String[titleCode.length];
				for (int j = 0; j < nowS.length; j++) {
					if (!"".equals(titleCode[j])) {
						nowS[j] = jsonODT.optString(titleCode[j]);
					} else {
						nowS[j] = "未知数据";
					}

				}

				dataList.add(nowS);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	// 变更标题栏内容
	private void changeTitle(String[] namesRight) {
		LinearLayout layRightTitle = (LinearLayout) findViewById(R.id.lineralayout_right_title);

		final AutoFitView ret = new AutoFitView(LoginStatisticsActivity.this,
				R.layout.autview_item, namesRight.length, getWindowManager()
						.getDefaultDisplay().getWidth() - 6, true);
		for (int i = 0; i < namesRight.length; i++) {
			TextView tt = (TextView) ret.getChildAt(i).findViewById(
					R.id.autview_text);
			tt.setText(namesRight[i]);
		}
		layRightTitle.removeAllViews();
		layRightTitle.addView(ret);

		ret.setAllBackgroundByID(R.drawable.glay_list_title);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, ret.getItemH());
		ret.setLayoutParams(lp);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.date_select_new2:
			showDatePicker();
			break;

		case R.id.id_platform_choice:
			showPlatformChoice();
			break;
		}
	}

	/*
	 * 平台选择对话框
	 */
	private void showPlatformChoice() {
		final String[] platform_name = new String[] { "ANDROID", "IPAD" };
		new AlertDialog.Builder(LoginStatisticsActivity.this).setTitle("平台选择")
				.setItems(platform_name, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int which) {
						termType = platform_name[which];
						platform_choice.setText(termType);
						initData();
					}
				}).create().show();
	}

	public void showtip(String message) {
		toast(LoginStatisticsActivity.this, message);
	}

	public void nullDataRemind() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				removeDialog(LOADING_DIALOG);
				Toast.makeText(LoginStatisticsActivity.this,
						getString(R.string.no_data), Toast.LENGTH_SHORT).show();
				setWidgeNodata();
				removeTL();
			}

		});
	}

	private void removeTL() {
		// TODO Auto-generated method stub
		LinearLayout layRightTitle = (LinearLayout) findViewById(R.id.lineralayout_right_title);
		layRightTitle.removeAllViews();
		dataList = null;
		StatisAdapter = new LoginStatisticsAdapter(
				LoginStatisticsActivity.this, dataList);

		area_right_listView.setAdapter(StatisAdapter);
	}

	/**
	 * 显示日期选择.
	 */
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
						DateUtil.PATTERN_8);

				initData();

			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		mDateDia.show();
	}

}
