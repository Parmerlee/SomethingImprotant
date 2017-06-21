package com.bonc.mobile.hbmclient.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.anim;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.common.Constant;
import com.bonc.mobile.common.R.color;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.util.DateUtil;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;

public class AnnounceMgrActivity extends AnnounceSlideHolderActivity implements
		OnClickListener, OnItemClickListener, OnItemLongClickListener,
		OnScrollListener {

	private ImageView mAdd, mDevide;
	private ListView mListView;

	private Activity activity;

	Map<String, String> param = new HashMap<String, String>();

	private int mPage = 1;

	MyAadpter adapter;

	private List<Map<String, String>> mCurrentList = new ArrayList<Map<String, String>>();

	private List<Map<String, String>> mList = new ArrayList<Map<String, String>>();

	private String menuCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setMainContent(
				getIntent().getStringExtra(MenuEntryAdapter.KEY_MENU_CODE),
				R.layout.activity_announce_mgr);
		initView();
		initDate();

	}

	private Map<String, String> getDefaultParam() {
		param = new HashMap<String, String>();
		param.put("clickCode", menuCode);
		param.put("menuCode", menuCode);
		param.put("appType", "RVS");
		param.put("clickType", "MENU");

		return param;
	}

	@Override
	protected void bindData(JSONObject result, int requestCode) {
		// TODO Auto-generated method stub
		super.bindData(result, requestCode);
		switch (requestCode) {
		case 1:

			mList.addAll(getDates(result));
			// aaaa;
			adapter = new MyAadpter(activity, mList);

			mListView.setAdapter(adapter);

			break;

		case 2:
			try {
				if (!result.getBoolean("flag")) {
					Toast.makeText(activity, result.getString("msg"), 1).show();
					return;
				}

				mList.remove(1);
				adapter.notifyDataSetChanged();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
	}

	private List<Map<String, String>> getDates(JSONObject result) {
		// TODO Auto-generated method stub
		try {
			if (!result.getBoolean("flag")) {
				Toast.makeText(activity, result.getString("msg"), 1).show();
				return null;
			}

			mCurrentList = JsonUtil.toList(JsonUtil.optJSONArray(
					result.getJSONObject("data"), "rows"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mCurrentList;
	}

	private void initDate() {
		// TODO Auto-generated method stub

		param = getDefaultParam();
		param.put("page", String.valueOf(mPage));

		param.put("rows", "");

		param.put("operId", "");
		param.put("title", "");
		param.put("msg", "");
		param.put("pubTime", "");
		param.put("expTime", "");
		param.put("status", "");

		new LoadDataTask2(this, Constant.PORTAL_PATH, 1).execute(
				"/bi/notice/getNotices", param);

		// adapter = new MyAadpter(activity, mList);
		// mListView.setAdapter(adapter);

	}

	private void initView() {
		// TODO Auto-generated method stub

		LinearLayout ll = (LinearLayout) findViewById(R.id.parent);
		ll.setBackgroundDrawable(WatermarkImage.getWatermarkDrawable());

		mAdd = (ImageView) this.findViewById(R.id.announce_add);
		mDevide = (ImageView) this.findViewById(R.id.announce_devide);
		mListView = (ListView) this.findViewById(R.id.announce_list);
		activity = this;

		this.findViewById(R.id.announce_logo).setOnClickListener(this);

		mDevide.setOnClickListener(this);
		mAdd.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
		mListView.setOnItemLongClickListener(this);
		// mListView.setOnScrollListener(this);

		// menuCode =
		// getIntent().getStringExtra(BaseConfigLoader.KEY_MENU_CODE);
		menuCode = getIntent().getStringExtra(MenuEntryAdapter.KEY_MENU_CODE);

		this.findViewById(R.id.announce_bottom).setOnClickListener(this);

		// this.findViewById(R.id.announce_hs).seton
		// navigator.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// }
		// });
		View header = LayoutInflater.from(this).inflate(
				R.layout.announce_list_item, null);

		mListView.addHeaderView(header);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);

		switch (v.getId()) {
		case R.id.announce_logo:
			slideHolder.toggle();
			break;

		case R.id.announce_devide:
			Toast.makeText(activity, "AAAA", 1).show();
			break;

		case R.id.announce_add:

			startActivity(new Intent(activity, AnnounceEditActivity.class)
					.putExtra(MenuEntryAdapter.KEY_MENU_CODE, menuCode));

			// Toast.makeText(activity, "BBB", 1).show();

			// mPage++;
			// // param = new LinkedHashMap<String, String>();
			// param.put("page", String.valueOf(mPage));
			//
			// new LoadDataTask2(this, Constant.PORTAL_PATH, 1).execute(
			// "/bi/notice/getNotices", param);

			break;
		case R.id.announce_bottom:
			mPage++;
			initDate();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		param = new LinkedHashMap<String, String>();

		// Map<String, String> map = new HashMap<String, String>();
		// map = (Map<String, String>) (parent.getItemAtPosition(position));
		// map.get("FLOW_ID");

		param.put("flowIds", ((Map<String, String>) (parent
				.getItemAtPosition(position))).get("FLOW_ID"));
		new LoadDataTask2(this, Constant.PORTAL_PATH, 2).execute(
				"/bi/notice/delNotice", param);
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		// view.setBackgroundColor(color.aliceblue);

		adapter.setSelectedItem(position - 1 < 0 ? 0 : position - 1);
		adapter.notifyDataSetChanged();

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

		// 当不滚动时
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			// 判断是否滚动到底部
			if (view.getLastVisiblePosition() == view.getCount() - 1) {
				// 加载更多功能的代码
				mPage++;
				// param = new LinkedHashMap<String, String>();
				param.put("page", String.valueOf(mPage));

				new LoadDataTask2(this, Constant.PORTAL_PATH, 1).execute(
						"/bi/notice/getNotices", param);

				// Toast.makeText(activity, "MORE", 1).show();
			}
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

	class MyAadpter extends BaseAdapter {

		int old = -1;
		SparseBooleanArray selected = new SparseBooleanArray();
		JSONObject result;

		Context context;

		private List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		public MyAadpter(Context context, List<Map<String, String>> list) {
			// super(context, textViewResourceId);
			// TODO Auto-generated constructor stub
			// this.result = result;
			this.context = context;
			// parseDate();
			// mList.addAll(list);
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			return list.size();
		}

		@SuppressWarnings("deprecation")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			convertView = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.announce_list_item, null);

			TextView tv1 = (TextView) convertView
					.findViewById(R.id.announce_item_tv1);
			TextView tv2 = (TextView) convertView
					.findViewById(R.id.announce_item_tv2);
			TextView tv3 = (TextView) convertView
					.findViewById(R.id.announce_item_tv3);
			TextView tv4 = (TextView) convertView
					.findViewById(R.id.announce_item_tv4);
			TextView tv5 = (TextView) convertView
					.findViewById(R.id.announce_item_tv5);

			try {

				if (TextUtils.equals("1", list.get(position).get("STATUS"))) {

					tv1.setText("未发布");
				} else if (TextUtils.equals("0",
						list.get(position).get("STATUS"))) {

					tv1.setText("已发布");
				}
				// Log.d("AAAA", "AAAAA:"+list.get(position).get("TITLE"));
				tv2.setText(list.get(position).get("TITLE"));
				tv3.setText(list.get(position).get("OPER_NAME"));

				// DateUtil.getDate(currentTime, "")

				tv4.setText(DateUtil.formatter(
						new Date(Long.parseLong(list.get(position).get(
								"NOTICE_PUBLISH_DATE"))), DateUtil.PATTERN_9));

				tv5.setText(DateUtil.formatter(
						new Date(Long.parseLong(list.get(position).get(
								"NOTICE_EXPIRE_DATE"))), DateUtil.PATTERN_9));

			} catch (Exception e) {
				// TODO: handle exception
				return convertView;
			}

			// Log.d("AAAAA", "AAAAA:" + selected.get(position));
			if (selected.get(position)) {
				convertView.setBackgroundResource(R.color.lightyellow);
			} else {
				convertView.setBackgroundResource(R.color.white);
			}
			return convertView;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		public void setSelectedItem(int pos) {
			if (old != -1) {
				this.selected.put(old, false);
			}
			this.selected.put(pos, true);
			old = pos;
		}

	}

}
