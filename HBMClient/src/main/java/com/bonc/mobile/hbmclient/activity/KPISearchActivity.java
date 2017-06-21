package com.bonc.mobile.hbmclient.activity;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.terminal.TerminalGroupActivity;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.StringUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;

/***
 * KPI搜索act
 * 
 * @author Lenevo
 *
 */
public class KPISearchActivity extends MenuActivity {

	private ListView listView;
	private Button searchButton;
	private EditText kpiSearchContent;
	private List<Map<String, String>> datas = null;
	private Map<String, List<Map<String, String>>> kpiMenuRelation;
	private String searchContent;
	private KpiSearchAdapter kpiSearchAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_NoTitleBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kpi_search);

		RelativeLayout rl = (RelativeLayout) findViewById(R.id.id_kpi_search);
		rl.setBackgroundDrawable(WatermarkImage.getWatermarkDrawable());

		initWidget();
	}

	protected void initWidget() {
		// 初始化时间.
		TextView home_tv_date = (TextView) findViewById(R.id.home_tv_date);
		home_tv_date.setText(DateUtil.formatter(Calendar.getInstance()
				.getTime(), "yyyy年MM月dd日, EEEE"));
		listView = (ListView) findViewById(R.id.kpi_list_view);
		kpiSearchAdapter = new KpiSearchAdapter();
		listView.setAdapter(kpiSearchAdapter);

		searchButton = (Button) findViewById(R.id.kpi_search_button);
		kpiSearchContent = (EditText) findViewById(R.id.kpi_search_content);
		searchButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				searchContent = kpiSearchContent.getText().toString();
				if ("".equals(StringUtil.nullToString(searchContent))) {
					Toast.makeText(KPISearchActivity.this, "请输入搜索内容!",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (searchContent.contains("union")
						|| searchContent.contains("'")
						|| searchContent.contains("%")
						|| searchContent.contains("from")) {
					Toast.makeText(KPISearchActivity.this, "搜索内容包含非法字符,请重新输入!",
							Toast.LENGTH_SHORT).show();
					return;
				}

				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

				inputMethodManager.hideSoftInputFromWindow(
						KPISearchActivity.this.getCurrentFocus()
								.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);

				BusinessDao businessDao = new BusinessDao();
				datas = businessDao.qryKpiInfoByName(searchContent);
				kpiMenuRelation = businessDao
						.qryKpiMenusBykpiName(searchContent);

				if (datas == null || datas.size() == 0) {
					Toast.makeText(KPISearchActivity.this, "很遗憾,没有搜索到相关指标!",
							Toast.LENGTH_SHORT).show();
				}

				kpiSearchAdapter.notifyDataSetChanged();

			}
		});

	}

	/**
	 * kpi搜索适配器.
	 * 
	 * @author Administrator
	 * 
	 */
	class KpiSearchAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (datas == null) {
				return 0;
			}
			return datas.size();

		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {

			ViewHolder viewHolder = null;
			if (arg1 == null) {
				viewHolder = new ViewHolder();
				arg1 = LayoutInflater.from(KPISearchActivity.this).inflate(
						R.layout.kpi_search_list_view, null);
				viewHolder.kpiName = (TextView) arg1
						.findViewById(R.id.kpi_name);
				viewHolder.KpiDesc = (TextView) arg1
						.findViewById(R.id.kpi_desc);
				viewHolder.kpiMenusLayout = (LinearLayout) arg1
						.findViewById(R.id.menu_button_layout);
				arg1.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) arg1.getTag();
				viewHolder.kpiMenusLayout.removeAllViews();
			}

			String kpiName = datas.get(position).get("kpiName");
			SpannableString ss = new SpannableString(kpiName);
			// int start = kpiName.indexOf(searchContent);
			// // 设置0-2的字符颜色
			// ss.setSpan(new ForegroundColorSpan(Color.RED), start, start
			// + searchContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			viewHolder.kpiName.setText(ss);
			viewHolder.KpiDesc.setText(datas.get(position).get("kpiDesc"));

			List<Map<String, String>> menuList = kpiMenuRelation.get(datas.get(
					position).get("kpiCode"));

			if (menuList != null && menuList.size() > 0) {
				for (int i = 0; i < menuList.size(); i++) {
					final Map<String, String> menuInfoMap = menuList.get(i);
					final String content = menuInfoMap.get("menuName");
					TextView textView = new TextView(KPISearchActivity.this);
					textView.setGravity(Gravity.CENTER);
					textView.setTextColor(Color.rgb(255, 255, 255));
					textView.setTextSize(18);
					textView.setBackgroundResource(R.mipmap.menu_background);
					textView.setPadding(5, 0, 5, 0);

					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					if (i > 0)
						lp.leftMargin = 25;
					textView.setLayoutParams(lp);
					textView.setText(content);

					arg1.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							String menuCode = menuInfoMap.get("menuCode");
							BusinessDao businessDao = new BusinessDao();
							Map<String, String> pi = businessDao
									.getMenuParentInfo(menuCode);
							if ("12".equals(pi.get("menuType"))) {
								Intent intent = new Intent(
										KPISearchActivity.this,
										MainKpiActivity.class);
								intent.putExtra("key_menu_code",
										pi.get("menuCode"));
								startActivity(intent);
							} else if ("2".equals(pi.get("menuType"))) {
								Intent intent = new Intent(
										KPISearchActivity.this,
										TerminalGroupActivity.class);
								intent.putExtra("first_menu_code", "2");
								startActivity(intent);
							} else if ("11".equals(pi.get("menuType"))) {
								Intent intent = new Intent(
										KPISearchActivity.this,
										SimpleTerminalActivity.class);
								intent.putExtra("key_menu_code",
										pi.get("menuCode"));
								startActivity(intent);
							}
						}
					});

					viewHolder.kpiMenusLayout.addView(textView);
				}
			}

			/*
			 * String content2 = "经分日报"; TextView textView2 = new
			 * TextView(KPISearchActivity.this); LinearLayout.LayoutParams lp2
			 * =new LinearLayout.LayoutParams(content2.length()*35,50);
			 * lp2.leftMargin = 15; textView2.setLayoutParams(lp2);
			 * textView2.setText(content2);
			 * textView2.setGravity(Gravity.CENTER);
			 * textView2.setTextColor(Color.rgb(255,255,255));
			 * textView2.setTextSize(18);
			 * textView2.setBackgroundResource(R.drawable.menu_background);
			 * 
			 * String content = "业务健康度"; TextView textView = new
			 * TextView(KPISearchActivity.this); LinearLayout.LayoutParams lp
			 * =new LinearLayout.LayoutParams(content.length()*35,50);
			 * textView.setLayoutParams(lp); textView.setText(content);
			 * textView.setGravity(Gravity.CENTER);
			 * textView.setTextColor(Color.rgb(255,255,255));
			 * textView.setTextSize(18);
			 * textView.setBackgroundResource(R.drawable.menu_background);
			 * 
			 * viewHolder.kpiMenusLayout.addView(textView);
			 * viewHolder.kpiMenusLayout.addView(textView2);
			 * 
			 * textView.setOnClickListener(new View.OnClickListener() {
			 * 
			 * @Override public void onClick(View v) {
			 * //Toast.makeText(KPISearchActivity.this, "text",
			 * Toast.LENGTH_SHORT).show(); intent = new
			 * Intent(KPISearchActivity.this, KPIHomeActivity.class);
			 * intent.putExtra("menu_name", "经分日报"); intent.putExtra("menuCode",
			 * "9");
			 * 
			 * startActivity(intent); } });
			 */

			return arg1;
		}

		class ViewHolder {

			String kpiCode;
			TextView kpiName;
			TextView KpiDesc;
			LinearLayout kpiMenusLayout;

		}

	}

}
