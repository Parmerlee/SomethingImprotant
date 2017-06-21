package com.bonc.mobile.portal.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.anhuimobile.ac.R;
import com.bonc.mobile.common.Constant;
import com.bonc.mobile.common.User;
import com.bonc.mobile.common.activity.BaseActivity;
import com.bonc.mobile.common.util.FileUtils;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.common.view.ArcMenu;
import com.bonc.mobile.common.view.ArcMenu.OnMenuItemClickListener;
import com.bonc.mobile.common.view.ClearEditText;
import com.bonc.mobile.common.view.FlowLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class SearchActionActivity extends BaseActivity implements
		OnKeyListener, OnItemClickListener, OnScrollListener {

	private ArcMenu mArcMenu;
	private FlowLayout fl_union;
	private List<String> mData_unions;
	private ListView mListView_first, mListView_second, mListView_mian;
	private String mSearchText;
	View stub;
	private ClearEditText actv;
	private Activity activity;

	private static final int LISTVIEW_FIRST = 1;
	private static final int LISTVIEW_SECOND = 2;
	private static final int LISTVIEW_MIAN = 3;
	private static final String TAG = "SearchActionActivity";

	private static final int INT_PADDING = 50;

	private final int INTEGER_SEARCH = 11;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_action);
		initView();
		initDate();
		drawUI();
		initLinster();
	}

	private void initView() {
		setWatermarkImage();
		activity = SearchActionActivity.this;
		mArcMenu = (ArcMenu) activity.findViewById(R.id.arcmenu_search);
		fl_union = (FlowLayout) activity.findViewById(R.id.search_fl_union);
		actv = (ClearEditText) activity.findViewById(R.id.search_actv_search);
		stub = (LinearLayout) activity
				.findViewById(R.id.activity_search_action_st);
		// stub.inflate();
		mListView_first = (ListView) stub
				.findViewById(R.id.activity_search_action_left_lv_first);
		mListView_second = (ListView) stub
				.findViewById(R.id.activity_search_action_left_lv_second);
		mListView_mian = (ListView) activity.findViewById(R.id.search_lv_main);

	}

	private List<Map<String, Object>> mData_first;

	private void initDate() {
		mSearchText = this.getIntent().getStringExtra("text");
		if (mSearchText != null) {
			actv.setText(mSearchText);
			// KeyEvent.KEYCODE_ENTER;
		}
		mData_unions = new ArrayList<String>();
		mData_first = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 30; i++) {
			mData_unions.add("AAAAA:" + i + "实打实的地方地方");
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("test", "test:" + i);
			mData_first.add(temp);
		}

		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 3; i++) {
		//	list.add(R.drawable.composer_camera);
		}
		mArcMenu.setDates(R.id.tag_arcmenu, list);

		User.getInstance().loadTest();
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("page", "" + 1);
		param.put("pageSize", "" + 10);
		param.put("keyInfo", "手机流量");
		new LoadDataTask(this, Constant.BASE_PATH, INTEGER_SEARCH).execute(
				"/oa/hbAppGather/search/getSosoInfo", param);
	}

	private void drawUI() {
		for (int i = 0; i < mData_unions.size(); i++) {
			TextView tv = (TextView) LayoutInflater.from(
					getApplicationContext()).inflate(R.layout.flowlayout_item,
					fl_union, false);
			tv.setText(mData_unions.get(i).toString().substring(0, 7));
			tv.setTag(R.id.tag_search_unions, mData_unions.get(i));
			tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// Map<String, Object> temp = (Map<String, Object>) v
					// .getTag(R.id.tag_search_unions);
					Toast.makeText(getApplicationContext(),
							v.getTag(R.id.tag_search_unions).toString(), 5)
							.show();

					toggle();
				}
			});
			fl_union.addView(tv);
		}

		// mListView_mian.setAdapter(new
		// MyPerferAdapter(getApplicationContext(),
		// mData_unions));

		// 啊啊啊啊啊

		mListView_first.addHeaderView(getHeadView(), null, false);

	}

	MyAdapter adapter_first;

	private void initLinster() {
		actv.setOnKeyListener(this);
		mListView_first.setOnItemClickListener(this);
		mListView_first.setTag(R.id.activity_search_action_left_lv_first,
				LISTVIEW_FIRST);

		mListView_second.setOnItemClickListener(this);
		mListView_second.setTag(R.id.activity_search_action_left_lv_first,
				LISTVIEW_SECOND);
		mListView_mian.setOnItemClickListener(this);
		mListView_mian.setTag(R.id.activity_search_action_left_lv_first,
				LISTVIEW_MIAN);

		// mListView_first.setOnScrollListener(this);
		// mListView_second.setOnScrollListener(this);
		// mListView_mian.setOnScrollListener(this);

		mArcMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public void onClick(View view, int pos) {
				// TODO Auto-generated method stub
				Toast.makeText(
						getApplicationContext(),
						"BBBBB:" + view.getTag(R.id.tag_arcmenu) + "pos:" + pos,
						Toast.LENGTH_SHORT).show();
				switch (pos - 1) {
				case 0:

					FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
							UIUtil.getDisplayWidth(activity) * 1 / 4
									+ INT_PADDING,
							FrameLayout.LayoutParams.MATCH_PARENT);

					adapter_first = new MyAdapter(activity, mListView_first,
							mData_unions);
					mListView_first.setAdapter(adapter_first);
					mListView_second.setVisibility(View.GONE);
					toggleLeft();
					break;
				case 1:
					startActivity(new Intent(activity,
							ChannelManagerActivity.class));
					break;
				case 2:

					new Handler().postAtTime(new Runnable() {

						@Override
						public void run() {

							FileUtils.shareScreen(activity);

						}

					}, 300);

					break;
				case 3:

					break;

				default:
					break;
				}
			}

		});

	}

	private void bindData(int flag, JSONObject result) {
		switch (flag) {
		case INTEGER_SEARCH:
			// aaaa;
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			if (result.optBoolean("flag")) {
				JSONArray array = result.optJSONArray("data");
				if (array != null) {
					for (int i = 0; i < array.length(); i++) {
						Map<String, String> map = new HashMap<String, String>();
						try {
							map.put("kpiName", ((JSONObject) array.get(i))
									.optString("kpiName"));
							map.put("levelMenuName",
									((JSONObject) array.get(i))
											.optString("levelMenuName"));
							list.add(map);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					mListView_mian
							.setAdapter(new SimpleAdapter(
									activity,
									list,
									android.R.layout.simple_expandable_list_item_2,
									new String[] { "kpiName", "levelMenuName" },
									new int[] { android.R.id.text1,
											android.R.id.text2 }));
				}

			} else {
				Toast.makeText(activity, result.optString("msg"),
						Toast.LENGTH_LONG).show();
				mListView_mian.setAdapter(null);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.id_button_back) {
			onBackPressed();
			return;
		}
		super.onClick(v);
		switch (v.getId()) {
		case R.id.activity_search_action_left_clear:
			toggleLeft();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			// 隐藏键盘
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(SearchActionActivity.this
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
			Toast.makeText(getApplicationContext(), "AAAA", 11).show();
			// 处理搜索逻辑

		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		switch (Integer.valueOf(parent.getTag(
				R.id.activity_search_action_left_lv_first).toString())) {
		case LISTVIEW_FIRST:
			stub.setLayoutParams(new FrameLayout.LayoutParams(UIUtil
					.getDisplayWidth(activity) * 2 / 4 + INT_PADDING,
					FrameLayout.LayoutParams.MATCH_PARENT));
			((MyAdapter) adapter_first).setSelectItem(position - 1);
			adapter_first.notifyDataSetChanged();

			// mListView_second.setLayoutParams(new LinearLayout.LayoutParams(
			// UIUtil.getDisplayWidth(activity) * 1 / 4,
			// LinearLayout.LayoutParams.MATCH_PARENT));

			mListView_second.setAdapter(new MyAdapter(activity,
					mListView_second, mData_unions));

			// stub.addView(mListView_second);
			break;

		case LISTVIEW_SECOND:
			break;

		case LISTVIEW_MIAN:
			toggle();
			startActivity(new Intent(activity, PreferActivity.class));
			break;
		default:
			break;
		}

	}

	public void toggleLeft() {
		// to close
		float FLOAT_START, FLOAT_END = 0f;
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) stub
				.getLayoutParams();
		final int visibility;
		if (stub.getVisibility() == View.VISIBLE) {
			FLOAT_START = 0.9f;
			FLOAT_END = 0f;
			visibility = View.GONE;
			// params.width = UIUtil.getDisplayWidth(activity) / 4 +
			// INT_PADDING;
		} else {
			FLOAT_START = 0.0f;
			FLOAT_END = 0.9f;
			visibility = View.VISIBLE;
		}
		params.width = UIUtil.getDisplayWidth(activity) / 4 + INT_PADDING;
		stub.setLayoutParams(params);
		AlphaAnimation animation = new AlphaAnimation(FLOAT_START, FLOAT_END);
		animation.setDuration(300);
		animation.setFillAfter(false);
		stub.startAnimation(animation);
		// animation.startNow();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// Log.d(TAG, "AAAA:" + visibility);
				stub.setVisibility(visibility);

				// stub.removeView(mListView_second);
				// mListView_second.setVisibility(View.GONE);
				// invalida

			}
		}, 300);

		return;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (stub.getVisibility() != View.GONE) {
			toggleLeft();
			// invalidate();
			// postInvalidate();

			return;
		}

		if (mArcMenu.isOpen()) {
			mArcMenu.toggleMenu(300);
			return;
		}
		super.onBackPressed();

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		Log.d(TAG, "AAAA");
		if (stub.getVisibility() != View.GONE) {
			toggleLeft();

			// return;
		}
		if (mArcMenu.isOpen()) {
			mArcMenu.toggleMenu(300);
			// return;
		}

	}

	private View getHeadView() {

		View view = LayoutInflater.from(activity).inflate(
				R.layout.flowlayout_item, null);

		final TextView tv = (TextView) view.findViewById(R.id.flowlayout_tv);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);
		tv.setText("全部");
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (TextUtils.equals(tv.getText().toString(), "全部")) {
					tv.setText("喜好");
				} else {
					tv.setText("全部");
				}
			}
		});
		return view;
	}

	private void toggle() {
		if (stub.getVisibility() != View.GONE) {
			toggleLeft();

			// return;
		}
		if (mArcMenu.isOpen()) {
			mArcMenu.toggleMenu(300);
			// return;
		}
	}

	class LoadDataTask extends com.bonc.mobile.common.net.HttpRequestTask {
		int flag;

		public LoadDataTask(Context context, String basepath, int flag) {
			super(context, basepath);
			this.flag = flag;
		}

		@Override
		protected void handleResult(JSONObject result) {
			bindData(flag, result);
		}

	}
}
