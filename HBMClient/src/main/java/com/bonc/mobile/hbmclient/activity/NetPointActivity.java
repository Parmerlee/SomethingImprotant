package com.bonc.mobile.hbmclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.asyn_task.ChannelSummaryAsynTask;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.state.business_outlets.AddFocusWebsite;
import com.bonc.mobile.hbmclient.state.business_outlets.AddFocusWebsite.OnWebsitePostListener;
import com.bonc.mobile.hbmclient.util.HttpUtil;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.util.StringUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;

public class NetPointActivity extends BaseActivity {
	final String KEY_AREA_NAME = "AREA_NAME";
	final String KEY_STATE = "state";
	public final static int REQUEST_CODE = 0;
	//
	Handler handler = new Handler();
	ListView netPointListView;
	List<Map<String, String>> listData;
	NetPointAdapter mNetPointAdapter;
	private AddFocusWebsite addFocusWebsite;

	private EditText searchContent;
	private ChannelSummaryAsynTask mTask;
	private Button searchButton;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_NoTitleBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_netpoint);
		findViewById(R.id.parent).setBackgroundDrawable(
				WatermarkImage.getWatermarkDrawable());
		this.addFocusWebsite = new AddFocusWebsite(this);
		this.addFocusWebsite.setListener(new OnWebsitePostListener() {

			@Override
			public void onPost() {
				initData();
				back(RESULT_OK);
			}
		});
		initWidget();
		initData();
	}

	private void back(int result) {
		setResult(result);
		// finish();
	}

	public void initWidget() {
		TextView title = (TextView) findViewById(R.id.titleName);
		title.setText("添加关注网点");
		netPointListView = (ListView) findViewById(R.id.netpoint_listview);
		mNetPointAdapter = new NetPointAdapter();
		netPointListView.setAdapter(mNetPointAdapter);
		this.searchContent = (EditText) findViewById(R.id.netpoint_search_content);
		Button back = (Button) findViewById(R.id.id_date_select);
		back.setText("确定");
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				varifyChoose();
			}
		});
		this.searchButton = (Button) findViewById(R.id.netpoint_search_button);
		this.searchButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				initData();
			}
		});
	}

	private void varifyChoose() {
		try {
			List<String> areaCodes = new ArrayList<String>();
			for (int i = 0; i < listData.size(); i++) {
				Map<String, String> item = listData.get(i);
				String state = item.get(KEY_STATE);
				if ("1".equals(state)) {
					String areaCode = item.get("AREA_CODE");
					areaCodes.add(areaCode);
				}
			}
			if (areaCodes.size() > 0) {
				final String codes = StringUtil.listToString(areaCodes, "|");
				Map<String, String> questMap = new HashMap<String, String>();
				questMap.put("areaCode", codes);
				this.addFocusWebsite.addFocusWebsite(questMap);
			} else {
				back(RESULT_CANCELED);
			}
		} catch (Exception e) {

		}
	}

	public void initData() {

		showDialog(LOADING_DIALOG);
		if (!isNetWorkOk()) {
			removeDialog(LOADING_DIALOG);
			return;
		}
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

						mNetPointAdapter.notifyDataSetChanged();
						removeDialog(LOADING_DIALOG);

					}
				});
			}
		}).start();
	}

	/**
	 * 远程查询数据.
	 */
	public boolean getDataRemote() {
		String input = this.searchContent.getText().toString();
		Map<String, String> args = new HashMap<String, String>();
		args.put("areaName", input);
		String result = HttpUtil.sendRequest(
				ActionConstant.GET_BUSINESS_OUTLETS_FOCUS_WEBSITE_FOCUS, args);
		if (result == null) {
			return false;
		} else {
			try {
				JSONObject jo = new JSONObject(result);
				JSONArray dataJA = jo.optJSONArray("data");
				listData = JsonUtil.toList(dataJA);
			} catch (JSONException e) {
				listData = new ArrayList<Map<String, String>>();
			}

			return true;
		}
	}

	@Override
	public void onClick(View v) {

	}

	public void showtip(String message) {
		toast(NetPointActivity.this, message);
	}

	public void nullDataRemind() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				removeDialog(LOADING_DIALOG);

			}
		});

	}

	class NetPointAdapter extends BaseAdapter {
		public NetPointAdapter() {
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listData != null ? listData.size() : 0;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final int nowP = position;
			// TODO Auto-generated method stub
			ViewHolder holder = null;

			if (convertView == null) {
				convertView = LayoutInflater.from(NetPointActivity.this)
						.inflate(R.layout.item_netpoin, parent, false);
				holder = new ViewHolder();
				holder.pointName = (TextView) convertView
						.findViewById(R.id.net_name);
				holder.checkBox = (CheckBox) convertView
						.findViewById(R.id.net_box);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();

			}

			holder.checkBox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							Map<String, String> nowMap = listData.get(nowP);
							if (nowMap == null) {
								nowMap = new HashMap<String, String>();
							}
							if (isChecked) {
								nowMap.put(KEY_STATE, "1");
							} else {
								nowMap.put(KEY_STATE, "0");
							}
						}
					});

			Map<String, String> nowMap = listData.get(position);
			if (nowMap != null) {
				String nowName = nowMap.get(KEY_AREA_NAME);
				holder.pointName.setText(nowName);
				String nowChoose = nowMap.get(KEY_STATE);
				if ("1".equals(nowChoose)) {
					holder.checkBox.setChecked(true);
				} else {
					holder.checkBox.setChecked(false);
				}
			}

			return convertView;
		}

		class ViewHolder {

			TextView pointName;
			CheckBox checkBox;
		}
	}

}
