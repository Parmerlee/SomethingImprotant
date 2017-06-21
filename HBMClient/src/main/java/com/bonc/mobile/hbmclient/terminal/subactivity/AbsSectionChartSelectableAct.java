package com.bonc.mobile.hbmclient.terminal.subactivity;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.net.ConnectManager;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.view.MyDatePickerDialog;

public abstract class AbsSectionChartSelectableAct extends
		AbsSectionChartActivity {
	public final static int LOCAL_ORDER_NUMER = 781;

	protected ViewGroup mBottomLayout;
	protected Button mLocationBtn;
	protected Button mDateBtn;
	private DatePickerDialog mDateDia;
	private Calendar calendar;

	/** 当前日期拼组字符串yyyyMMDD */
	protected String mDateString;
	BusinessDao businessdao = new BusinessDao();
	// protected String mLocalString = Business.getDeafaultAreaCode();
	protected String mLocalString = businessdao.getUserInfo().get("areaId");
	private String[] mArea;
	private String[] mAreaCode;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.mDateString = this.intent.getExtras().getString(
				TerminalConfiguration.KEY_OPTIME);
		mBottomLayout = (ViewGroup) findViewById(R.id.section_bottom_layout);
		mBottomLayout.setVisibility(View.VISIBLE);
		View.inflate(this, R.layout.section_act_bottom_merge, mBottomLayout);
		mLocationBtn = (Button) findViewById(R.id.button1);
		mDateBtn = (Button) findViewById(R.id.button2);
		calendar = (Calendar) this.mTerminalActivityEnum.getCalendar().clone();

		List<Map<String, String>> list = businessdao.getAreaInfo(mLocalString);
		mArea = new String[list.size()];
		mAreaCode = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			mArea[i] = list.get(i).get("areaName");
			mAreaCode[i] = list.get(i).get("areaCode");
		}

		mLeftList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View viewNow,
					int arg2, long arg3) {

				if (!ConnectManager.isConnected()) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AbsSectionChartSelectableAct.this,
									"网络连接未打开!", Toast.LENGTH_SHORT).show();
						}
					});
					return;
				}
				;

				if (arg0.getAdapter().getCount() <= 1) {
					// mLocalString = Business.getDeafaultAreaCode();
					// mLocationBtn.setText(Business.getDeafaultAreaName());
					mLocalString = businessdao.getUserInfo().get("areaId");
					// mLocationBtn.setText(mArea[0]);
					configMap.put(TerminalConfiguration.KEY_AREA_CODE,
							mLocalString);
					refresh();
					return;
				}

				String areaName = getText(viewNow);
				String code = mAreaNameCode.get(areaName);
				if (code != null) {
					// mLocationBtn.setText(areaName);
					mLocalString = code;
					configMap.put(TerminalConfiguration.KEY_AREA_CODE,
							mLocalString);
					refresh();
				}
				// int len = mArea.length;
				// for(int i=0;i<len;i++){
				// if(areaName.equals(mArea[i])){
				// mLocationBtn.setText(areaName);
				// mLocalString = mAreaCode[i];
				// refresh();
				// break;
				// }
				// }
			}
		});
		mDateBtn.setText(DateUtil.formatter(calendar.getTime(),
				this.mTerminalActivityEnum.getDateRange().getDateShowPattern()));
		mDateDia = new MyDatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {

				if (!ConnectManager.isConnected()) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AbsSectionChartSelectableAct.this,
									"网络连接未打开!", Toast.LENGTH_SHORT).show();
						}
					});
					return;
				}
				;

				calendar.set(year, monthOfYear, dayOfMonth);
				String newDateString = DateUtil.formatter(calendar.getTime(),
						mTerminalActivityEnum.getDateRange()
								.getDateServerPattern());
				if (newDateString.equals(mDateString)) {
					return;
				}

				/*
				 * if(!DataMessageControl.getInstance().isDateInvalid(calendar))
				 * { Toast.makeText(AbsSectionChartSelectableAct.this,
				 * "无该日期数据!", Toast.LENGTH_LONG).show(); return ; }
				 */

				mDateString = newDateString;
				mDateBtn.setText(DateUtil.formatter(calendar.getTime(),
						mTerminalActivityEnum.getDateRange()
								.getDateShowPattern()));
				configMap.put(TerminalConfiguration.KEY_OPTIME, mDateString);
				refresh();

			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		mDateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDateDia.show();
			}
		});
		mLocationBtn
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						// TODO Auto-generated method stub
						// for(int i=0;i<mArea.length;i++){
						// menu.add(LOCAL_ORDER_NUMER,i+100,i,mArea[i]);
						// }
						for (int i = 0; i < mFirstColumns.length; i++) {
							menu.add(LOCAL_ORDER_NUMER, i + 100, i,
									mFirstColumns[i]);
						}

					}
				});
		// mLocationBtn.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// mLocationBtn.showContextMenu();
		// }
		// });
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		if (!ConnectManager.isConnected()) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(AbsSectionChartSelectableAct.this,
							"网络连接未打开!", Toast.LENGTH_SHORT).show();
				}
			});
			return false;
		}
		;

		if (item.getGroupId() == LOCAL_ORDER_NUMER) {
			mLocalString = mAreaCode[item.getOrder()];
			mLocationBtn.setText(mArea[item.getOrder()]);
		}
		refresh();
		return super.onContextItemSelected(item);
	}

	public String getText(View v) {
		String result = "";
		TextView textV = (TextView) v.findViewById(R.id.zhibiao_left_1);
		result = (String) textV.getText();
		return result;
	}

}
