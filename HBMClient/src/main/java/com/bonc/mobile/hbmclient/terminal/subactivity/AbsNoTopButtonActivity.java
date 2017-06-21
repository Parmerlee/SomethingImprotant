/**
 * TODO
 */
package com.bonc.mobile.hbmclient.terminal.subactivity;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
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

/**
 * @author liweigao
 *
 */
public abstract class AbsNoTopButtonActivity extends AbsStatisticsListActivity {
	public final static int LOCAL_ORDER_NUMER = 787;

	protected ViewGroup mBottomLayout;
	@Deprecated
	protected Button mLocationBtn;
	protected Button mDateBtn;
	private DatePickerDialog mDateDia;
	private Calendar calendar;
	/** 当前日期拼组字符串yyyyMMDD */
	protected String mDateString;
	protected int mTopSelectedID;
	protected String mLocalAreaID;
	private Handler handler = new Handler();
	private BusinessDao businessdao = new BusinessDao();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mDateString = this.intent.getExtras().getString(
				TerminalConfiguration.KEY_OPTIME);
		mBottomLayout = (ViewGroup) findViewById(R.id.statistics_bottom_layout);
		mBottomLayout.setVisibility(View.VISIBLE);

		View.inflate(this, R.layout.layout_button_single_sub, mBottomLayout);
		mLocalAreaID = businessdao.getUserInfo().get("areaId");
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
							Toast.makeText(AbsNoTopButtonActivity.this,
									"网络连接未打开!", Toast.LENGTH_SHORT).show();
						}
					});
					return;
				}
				;

				calendar.set(year, monthOfYear, dayOfMonth);

				if (!mTerminalActivityEnum.isDateValid(calendar)) {
					Toast.makeText(AbsNoTopButtonActivity.this, "无该日期数据!",
							Toast.LENGTH_LONG).show();
					return;
				}

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
							Toast.makeText(AbsNoTopButtonActivity.this,
									"网络连接未打开!", Toast.LENGTH_SHORT).show();
						}
					});
					return;
				}
				;

				// TerminalLeftAdapter sla =
				// (TerminalLeftAdapter)arg0.getAdapter();
				if (arg0.getAdapter().getCount() <= 1) {
					mLocalAreaID = businessdao.getUserInfo().get("areaId");
					configMap.put(TerminalConfiguration.KEY_AREA_CODE,
							mLocalAreaID);
					refresh();
					return;
				}

				String areaName = getText(viewNow);
				String code = mAreaNameCode.get(areaName);
				if (code != null) {
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
}
