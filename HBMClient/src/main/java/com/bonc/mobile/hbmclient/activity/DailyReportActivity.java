package com.bonc.mobile.hbmclient.activity;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.bonc.mobile.common.User;
import com.bonc.mobile.common.view.ProgressDialogUtils;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.net.HttpRequestTask;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.FileUtils;
import com.bonc.mobile.hbmclient.util.HttpUtil;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.util.UIUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.KpiJSInterface;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;

/**
 * @author sunwei
 */
public class DailyReportActivity extends SlideHolderActivity {
	String menuCode, areaCode;
	WebView web_view;
	Button sideLogo, dateSelect, areaSelect;
	Calendar calendar = Calendar.getInstance(), calendarMax = Calendar
			.getInstance();
	String[] areaNameList;
	int areaIndex;
	int lastScrollY;
	Handler handler;
	BusinessDao business = new BusinessDao();
	private List<Map<String, String>> reportArea;

	int TOUCH_SLOP;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setMainContent(R.layout.activity_daily_report);

		TOUCH_SLOP = ViewConfiguration.get(this).getScaledTouchSlop() * 3;
		initBaseData();
		initView();
		checkDate();
	}

	protected void initBaseData() {
		menuCode = getIntent().getStringExtra(MenuEntryAdapter.KEY_MENU_CODE);
		Map<String, String> userInfo = business.getUserInfo();
		areaIndex = 0;
		areaCode = userInfo.get("areaId");
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		String kpi_last_date = sp.getString("kpi_last_date", "99999999");
		String kpi_cur_date = sp.getString("kpi_cur_date", kpi_last_date);
		calendarMax
				.setTime(DateUtil.getDate(kpi_last_date, DateUtil.PATTERN_8));
		calendar.setTime(DateUtil.getDate(kpi_cur_date, DateUtil.PATTERN_8));
	}

	protected void initView() {
		handler = new Handler();
		findViewById(R.id.parent).setBackgroundDrawable(
				WatermarkImage.getWatermarkDrawable());
		buildSideMenu(menuCode);
		((TextView) findViewById(R.id.act_title)).setText(business
				.getMenuName(menuCode));
		web_view = (WebView) findViewById(R.id.web_view);
		web_view.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});
		web_view.setBackgroundColor(0);
		web_view.getSettings().setJavaScriptEnabled(true);
		web_view.getSettings().setDefaultTextEncodingName("UTF-8");
		web_view.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						if (lastScrollY > 0
								&& web_view.getContentHeight() > lastScrollY) {
							web_view.scrollTo(0, lastScrollY);
							lastScrollY = 0;
						}
					}
				}, 500);
			}

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				handler.proceed();
			}
		});
		web_view.addJavascriptInterface(new KpiJSInterface(this), "JSInterface");
		final GestureDetector detector = new GestureDetector(this,
				new WebFlingGestureListener());

		web_view.setOnTouchListener(new MyTouchListener());

		web_view.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				if (newProgress != 100) {
					ProgressDialogUtils.showProgressDialog(
							DailyReportActivity.this, "加载数据中，请稍后...");
				} else {
					ProgressDialogUtils.dismissPrgressDialog();
				}
				super.onProgressChanged(view, newProgress);
			}

		});
		sideLogo = (Button) findViewById(R.id.id_button_logo);
		sideLogo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				slideHolder.toggle();
			}
		});
		findViewById(R.id.id_share).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FileUtils.shareScreen(DailyReportActivity.this);
			}
		});
		dateSelect = (Button) findViewById(R.id.id_date_select);
		dateSelect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				chooseDate();
			}
		});
		areaSelect = (Button) findViewById(R.id.id_area_select);
		areaSelect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				chooseArea();
			}
		});
		Button back = (Button) findViewById(R.id.id_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		if (!"1".equalsIgnoreCase(menuCode)) {
			areaSelect.setVisibility(View.GONE);
		}
	}

	protected void checkDate() {
		Map<String, String> m = new HashMap<String, String>();
		m.put("menuCode", menuCode);
		new CheckDateTask(this).execute(ActionConstant.GET_4G_REPORT_DATE, m);
	}

	protected void loadPage() {
		if (calendar.after(calendarMax)) {
			web_view.loadDataWithBaseURL(null, "今日数据暂未发布", null, "UTF-8", null);
			return;
		}
		Map<String, String> m = new HashMap<String, String>();
		m.putAll(User.getInstance().getUserMap());
		m.put("date",
				DateUtil.formatter(calendar.getTime(), DateUtil.PATTERN_8));
		if (reportArea != null) {
			areaCode = reportArea.get(areaIndex).get("area_code");
			m.put("areacode", areaCode);
		}
		m.put("menuCode", menuCode);
		try {
			String url = Constant.SERVER_PATH
					+ ActionConstant.GET_4G_DAILY_REPORT + "?"
					+ HttpUtil.getUrlParams(m);
			web_view.loadUrl(url);
		} catch (IOException e) {
		}
	}

	protected void updateView() {
		areaSelect.setText(areaNameList[areaIndex]);
		dateSelect.setText(DateUtil.formatter(calendar.getTime(),
				DateUtil.PATTERN_MODEL2_10));
	}

	protected void chooseDate() {
		UIUtil.showDatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				calendar.set(year, monthOfYear, dayOfMonth);
				loadPage();
				updateView();
			}
		}, calendar);
	}

	protected void addDate(int value) {
		calendar.add(Calendar.DATE, value);
		loadPage();
		updateView();
	}

	protected void chooseArea() {
		if (areaNameList != null && areaNameList.length >= 1) {
			UIUtil.showAlertDialog(this, areaNameList,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int which) {
							areaIndex = which;
							loadPage();
							updateView();
						}
					});
		} else {

		}

	}

	class CheckDateTask extends HttpRequestTask {
		public CheckDateTask(Context context) {
			super(context);
		}

		@Override
		protected void handleResult(JSONObject result) {
			JSONArray jaArea = result.optJSONArray("reportArea");
			if (jaArea == null || "".equals(jaArea) || "null".equals(jaArea)
					|| jaArea.length() <= 0) {
				areaNameList = new String[] { "--" };
			} else if (jaArea.length() > 0) {
				reportArea = JsonUtil.toDataList(jaArea);
				areaNameList = new String[reportArea.size()];
				for (int i = 0; i < reportArea.size(); i++) {
					areaNameList[i] = reportArea.get(i).get("area_name");
				}
			}
			loadPage();
			updateView();
		}

	}

	int lastX;
	int lastY;

	class MyTouchListener implements View.OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// detector.onTouchEvent(event);
			int x = 0;
			int y = 0;

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				lastX = (int) event.getX();
				lastY = (int) event.getY();
				break;

			case MotionEvent.ACTION_UP:

				x = (int) event.getX();
				y = (int) event.getY();
				int deltaX = x - lastX;
				int deltaY = y - lastY;
				if (Math.abs(deltaX) > TOUCH_SLOP
						&& Math.abs(deltaX) > Math.abs(deltaY)) {
					lastScrollY = web_view.getScrollY();
					if (deltaX > 0) {
						addDate(1);
					} else {
						addDate(-1);
					}
				}
				break;

			default:
				break;

			}

			return false;
		}
	}

	class WebFlingGestureListener extends SimpleOnGestureListener {
		final int FLING_LIMIT = 500;

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if ((Math.abs(velocityX) - Math.abs(velocityY)) > FLING_LIMIT) {
				lastScrollY = web_view.getScrollY();
				if (velocityX > 0) {
					addDate(-1);
				} else if (velocityX < 0) {
					addDate(1);
				}
			}
			return false;
		}

	}
}
