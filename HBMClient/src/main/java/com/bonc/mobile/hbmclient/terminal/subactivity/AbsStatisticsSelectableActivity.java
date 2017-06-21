package com.bonc.mobile.hbmclient.terminal.subactivity;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.net.ConnectManager;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;
import com.bonc.mobile.hbmclient.terminal.view.ExclusiveButtonGroup;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.view.MyDatePickerDialog;

public abstract class AbsStatisticsSelectableActivity extends
		AbsStatisticsListActivity {
	public final static int LOCAL_ORDER_NUMER = 787;

	protected ViewGroup mBottomLayout;
	protected HorizontalScrollView mTopLayout;
	@Deprecated
	protected Button mLocationBtn;
	protected Button mDateBtn;
	protected ExclusiveButtonGroup mTopButtonGroup;
	private DatePickerDialog mDateDia;
	private Calendar calendar;
	/** 当前日期拼组字符串yyyyMMDD */
	protected String mDateString;
	protected int mTopSelectedID;
	protected String mLocalAreaID;
	private Handler handler = new Handler();
	private BusinessDao businessdao = new BusinessDao();
	private String[] title_group;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mDateString = this.intent.getExtras().getString(
				TerminalConfiguration.KEY_OPTIME);
		mBottomLayout = (ViewGroup) findViewById(R.id.statistics_bottom_layout);
		mBottomLayout.setVisibility(View.VISIBLE);
		mTopLayout = (HorizontalScrollView) findViewById(R.id.statistics_sub_title_layout);
		View.inflate(this, R.layout.section_act_bottom_merge, mBottomLayout);
		this.title_group = this.intent.getExtras()
				.getString(TerminalConfiguration.TITLE_GROUP)
				.split(TerminalConfiguration.DIV0);
		this.mTopButtonGroup = (ExclusiveButtonGroup) mTopLayout
				.findViewById(R.id.id_top_buttongroup);
		TextView[] bt = new TextView[this.title_group.length];
		for (int i = 0; i < bt.length; i++) {
			bt[i] = new TextView(this);
			bt[i].setText(this.title_group[i]);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(150,
					android.widget.RelativeLayout.LayoutParams.FILL_PARENT);
			bt[i].setGravity(Gravity.CENTER);
			bt[i].setLayoutParams(lp);
			mTopButtonGroup.addView(bt[i]);
		}
		mLocalAreaID = businessdao.getUserInfo().get("areaId");
		mTopButtonGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						if (!ConnectManager.isConnected()) {
							handler.post(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(
											AbsStatisticsSelectableActivity.this,
											"网络连接未打开!", Toast.LENGTH_SHORT)
											.show();
								}
							});
							return;
						}
						;
						mTopSelectedID = checkedId;
						refresh();
					}
				});
		mTopButtonGroup.selectItem(0);
		calendar = (Calendar) this.mTerminalActivityEnum.getCalendar().clone();
		mDateBtn = (Button) findViewById(R.id.button2);

		mDateDia = new MyDatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {

				if (!ConnectManager.isConnected()) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(
									AbsStatisticsSelectableActivity.this,
									"网络连接未打开!", Toast.LENGTH_SHORT).show();
						}
					});
					return;
				}
				;

				calendar.set(year, monthOfYear, dayOfMonth);

				/*
				 * if(!DataMessageControl.getInstance().isDateInvalid(calendar))
				 * { Toast.makeText(AbsStatisticsSelectableActivity.this,
				 * "无该日期数据!", Toast.LENGTH_LONG).show(); return ; }
				 */

				String newDateString = DateUtil.formatter(calendar.getTime(),
						mTerminalActivityEnum.getDateRange()
								.getDateServerPattern());

				if (!newDateString.equals(mDateString)) {
					mDateString = newDateString;
					mDateBtn.setText(DateUtil.formatter(calendar.getTime(),
							mTerminalActivityEnum.getDateRange()
									.getDateShowPattern()));
					configMap
							.put(TerminalConfiguration.KEY_OPTIME, mDateString);
					refresh();
				}

			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
				.get(Calendar.DAY_OF_MONTH));
		mDateBtn.setText(DateUtil.formatter(calendar.getTime(),
				mTerminalActivityEnum.getDateRange().getDateShowPattern()));
		mDateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDateDia.show();
			}
		});

		mLeftList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View viewNow,
					int arg2, long arg3) {

				if (!ConnectManager.isConnected()) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(
									AbsStatisticsSelectableActivity.this,
									"网络连接未打开!", Toast.LENGTH_SHORT).show();
						}
					});
					return;
				}

				if (arg0.getAdapter().getCount() <= 1) {
					mLocalAreaID = businessdao.getUserInfo().get("areaId");
					// mLocationBtn.setText(mArea[0]);
					configMap.put(TerminalConfiguration.KEY_AREA_CODE,
							mLocalAreaID);
					refresh();
					return;
				}

				String areaName = getText(viewNow);
				String code = mAreaNameCode.get(areaName);
				if (code != null) {
					// mLocationBtn.setText(areaName);
					mLocalAreaID = code;
					configMap.put(TerminalConfiguration.KEY_AREA_CODE,
							mLocalAreaID);
					refresh();
				}

			}
		});
	}

	public String getText(View v) {
		String result = "";
		TextView textV = (TextView) v.findViewById(R.id.zhibiao_left_1);
		result = (String) textV.getText();
		return result;
	}

	/** 隐藏上方按钮 */
	public void hideTopButtons() {
		mTopLayout.setVisibility(View.GONE);
	}
}
