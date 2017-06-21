package com.bonc.mobile.portal.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.anhuimobile.ac.R;
import com.bonc.mobile.common.activity.BaseActivity;
import com.bonc.mobile.common.util.FileUtils;
import com.bonc.mobile.common.view.ArcMenu;
import com.bonc.mobile.common.view.ArcMenu.OnMenuItemClickListener;
import com.bonc.mobile.common.view.ClearEditText;
import com.bonc.mobile.common.view.FlowLayout;
import com.bonc.mobile.portal.common.AppManager;

public class SearchMainActivity extends BaseActivity implements OnKeyListener,
		OnClickListener, OnMenuItemClickListener, TextWatcher {

	private GridView gv_his, gv_hot;
	private List<Map<String, Object>> data_his, data_hot;
	private FlowLayout fl_his, fl_hot;
	private ArcMenu mArcMenu;
	private TextView tv_noHis;
	private Activity activity;

	private ListView mListView;

	private ClearEditText cet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_main);
		setWatermarkImage();
		initView();
		getDate();
		drawUI();

		AppManager.getInstance().addActivity(this);
	}

	private void initView() {
		// TODO Auto-generated method stub
		activity = this;
		gv_his = (GridView) this.findViewById(R.id.search_gv_his);
		gv_hot = (GridView) this.findViewById(R.id.search_gv_hot);
		fl_his = (FlowLayout) this.findViewById(R.id.search_fl_his);
		fl_hot = (FlowLayout) this.findViewById(R.id.search_fl_hot);
		// cet = (ClearEditText) this.findViewById(R.id.search_actv_search);
		// cet.setOnKeyListener(this);
		// cet.addTextChangedListener(this);
		// this.findViewById(R.id.search_btn_clear).setOnClickListener(this);
		mArcMenu = (ArcMenu) this.findViewById(R.id.arcmenu_search);
		tv_noHis = (TextView) this.findViewById(R.id.search_tv_nohis);
		mListView = (ListView) this.findViewById(R.id.search_listview);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		onBackPressed();
		super.onClick(v);
		switch (v.getId()) {
		// case R.id.search_btn_clear:
		// Toast.makeText(getApplicationContext(), "AAAA", 1).show();
		// break;

		default:
			break;
		}
	}

	private void getDate() {
		// TODO Auto-generated method stub
		data_his = new ArrayList<Map<String, Object>>();
		data_hot = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 25; i++) {
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("text", "测试数据:" + i);
			data_his.add(temp);
			data_hot.add(temp);
		}

	}

	SearchAdapter adapter;

	private void drawUI() {
		List<String> list2 = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			list2.add("大类:" + i);
		}
		adapter = new SearchAdapter(getApplicationContext(), list2);
		mListView.setAdapter(adapter);
		addHeaderAndFooter();
		for (int i = 0; i < data_his.size(); i++) {
			TextView tv = (TextView) LayoutInflater.from(
					getApplicationContext()).inflate(R.layout.flowlayout_item,
					fl_his, false);
			// TextView tv = (TextView) layout.findViewById(R.id.flowlayout_tv);
			// TextView mark = (TextView)
			// layout.findViewById(R.id.flowlayout_tv_mark);
			// mark.setText("热");
			tv.setText(data_his.get(i).get("text").toString());

			tv.setTag(R.id.tag_search_his, data_his.get(i));
			if (i == 0) {
				tv.setBackgroundColor(getResources().getColor(
						android.R.color.holo_red_light));
			}
			tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					@SuppressWarnings("unchecked")
					Map<String, Object> temp = (Map<String, Object>) v
							.getTag(R.id.tag_search_his);
					Toast.makeText(getApplicationContext(),
							temp.get("text").toString(), 5).show();
				}
			});
			fl_his.addView(tv);
		}

		if (fl_his.getChildCount() == 0) {
			tv_noHis.setVisibility(View.VISIBLE);
			tv_noHis.setText("暂无历史搜索记录");
			((View) fl_his.getParent()).setVisibility(View.GONE);
		}
		for (int i = 0; i < data_hot.size(); i++) {
			TextView tv = (TextView) LayoutInflater.from(
					getApplicationContext()).inflate(R.layout.flowlayout_item,
					fl_hot, false);
			tv.setText(data_hot.get(i).get("text").toString());

			tv.setTag(R.id.tag_search_hot, data_hot.get(i));
			tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					@SuppressWarnings("unchecked")
					Map<String, Object> temp = (Map<String, Object>) v
							.getTag(R.id.tag_search_hot);
					Toast.makeText(getApplicationContext(),
							temp.get("text").toString(), 5).show();
				}
			});
			fl_hot.addView(tv);
		}

		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 4; i++) {
		//	list.add(R.drawable.composer_camera);
		}
		mArcMenu.setDates(R.id.tag_arcmenu, list);

		mArcMenu.setOnMenuItemClickListener(this);

		initAutoCompleteEdittext();
	}

	private void initAutoCompleteEdittext() {
		// 定义字符串数组作为提示的文本
		String[] books = new String[] { "tt", "ccc", "bbbb", "ddddd" };

		// 创建一个ArrayAdapter封装数组
		ArrayAdapter<String> av = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, books);
		ClearEditText auto = (ClearEditText) findViewById(R.id.search_actv_search);
		// auto.setAdapter(av);
		// auto.setData(books);
		// auto.setAdapter(av);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			// 隐藏键盘
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(SearchMainActivity.this
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
			Toast.makeText(getApplicationContext(), "AAAA", 11).show();
			// 处理搜索逻辑

		}
		return false;
	}

	@Override
	public void onBackPressed() {

		if (mArcMenu.isOpen()) {
			mArcMenu.toggleMenu(300);
			return;
		}
		super.onBackPressed();
	}

	private void addHeaderAndFooter() {
		// TODO Auto-generated method stub
		// LinearLayout layout =
		// View footer =
		TextView tv = (TextView) LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.flowlayout_item, null);
		tv.setText("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(activity, "清除历史记录", 1).show();
			}
		});
		mListView.addFooterView(tv);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View view, int pos) {

		// TODO Auto-generated method stub

		switch (pos - 1) {
		case 0:
			startActivity(new Intent(getApplicationContext(),
					SearchActionActivity.class).putExtra("text", "搜索内容"));
			break;
		case 1:
			Toast.makeText(getApplicationContext(), "清空搜索历史",
					Toast.LENGTH_SHORT).show();

			tv_noHis.setVisibility(View.VISIBLE);
			tv_noHis.setText("暂无历史搜索记录");
			((View) fl_his.getParent()).setVisibility(View.GONE);

			startActivity(new Intent(activity, ClassifyActivity.class));
			break;
		case 2:

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					FileUtils.shareScreen(activity);

				}
			}, 1000);

			break;
		case 3:

			startActivity(new Intent(activity, PreferActivity.class));
			break;

		default:
			break;
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		addHeaderAndFooter();
		// Toast.makeText(activity, s.toString(), 1).show();
	}

}
