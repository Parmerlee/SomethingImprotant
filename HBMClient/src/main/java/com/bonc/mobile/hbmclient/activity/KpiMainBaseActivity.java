package com.bonc.mobile.hbmclient.activity;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.net.HttpRequestTask;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.UIUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;

/**
 * kpi基础ACT
 */
public class KpiMainBaseActivity extends SlideHolderActivity {
	protected String optime, areaCode, dataType, menuCode;
	protected Calendar calendar, calendarMonth;
	protected List<Map<String, String>> areaInfoList;
	protected String[] areaNameList;
	protected int areaIndex;
	protected boolean monthQuery;

	protected Button sideLogo, dateSelect, areaSelect;

	protected void initView() {
		// 水印
		LinearLayout ll = (LinearLayout) findViewById(R.id.parent);
		ll.setBackgroundDrawable(WatermarkImage.getWatermarkDrawable());
		buildSideMenu(menuCode);
		sideLogo = (Button) findViewById(R.id.id_button_logo);
		sideLogo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				slideHolder.toggle();
			}
		});
		areaSelect = (Button) findViewById(R.id.id_area_select);
		areaSelect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				chooseArea();
			}
		});
		dateSelect = (Button) findViewById(R.id.id_date_select);
		dateSelect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				chooseDate();
			}
		});
	}

	protected void initBaseData() {
		menuCode = getIntent().getStringExtra(MenuEntryAdapter.KEY_MENU_CODE);
		dataType = "D";
		calendar = Calendar.getInstance();
		calendarMonth = Calendar.getInstance();
		optime = DateUtil
				.formatter(getCalendar().getTime(), DateUtil.PATTERN_8);
		initAreaData();
	}

	protected void initAreaData() {
		BusinessDao dao = new BusinessDao();
		Map<String, String> userInfo = dao.getUserInfo();
		areaInfoList = dao.getAreaInfo(userInfo.get("areaId"));
		areaNameList = new String[areaInfoList.size()];
		for (int i = 0; i < areaInfoList.size(); i++) {
			areaNameList[i] = areaInfoList.get(i).get("areaName");
		}
		areaIndex = 0;
		areaCode = areaInfoList.get(areaIndex).get("areaCode");
	}

	protected void loadData() {
	}

	protected void bindData(JSONObject result) {
	}

	protected void chooseDate() {
		UIUtil.showDatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				getCalendar().set(year, monthOfYear, dayOfMonth);
				updateView();
				loadData();
			}
		}, getCalendar());
	}

	protected Calendar getCalendar() {
		return monthQuery ? calendarMonth : calendar;
	}

	protected void chooseArea() {
		UIUtil.showAlertDialog(this, areaNameList,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int which) {
						areaIndex = which;
						updateView();
						loadData();
					}
				});
	}

	protected void updateView() {
		areaSelect.setText(areaNameList[areaIndex]);
		dateSelect.setText(DateUtil.formatter(getCalendar().getTime(),
				monthQuery ? DateUtil.PATTERN_MODEL2_7
						: DateUtil.PATTERN_MODEL2_10));
	}

	protected class LoadDataTask extends HttpRequestTask {
		public LoadDataTask(Context context) {
			super(context);
		}

		@Override
		protected void handleResult(JSONObject result) {
			bindData(result);
		}

	}

}
