package com.bonc.mobile.remoteview.activity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bonc.mobile.common.activity.BaseActivity;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.common.ConfigLoader;
import com.bonc.mobile.remoteview.common.Constant;
import com.bonc.mobile.remoteview.common.RemoteUtil;

public class OrderSettingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_order_setting);
		this.renderStaticData();
		this.loadOrderSetting(null);
		RemoteUtil.getInstance().addActivity(this);
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		showGuide();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if (MyUtils.doInfilter(this)) {

			if (!MyUtils.isBackground(this)) {
				MyUtils.startProtal(this);
				RemoteUtil.getInstance().callLogin();
			}
		}
	}
	void showGuide() {
		UIUtil.showGuideWindow(this, findViewById(R.id.root),
				"guide.order_set", new int[] { R.mipmap.guide_order_set });
	}

	private void renderStaticData() {
		// 水印图片
		this.setWatermarkImage();
		// 返回按钮事件
		Button btn = (Button) this.findViewById(R.id.id_button_back);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		// 栏目标题
		Bundle extras = this.getIntent().getExtras();
		String menuCode = (String) extras.get(BaseConfigLoader.KEY_MENU_CODE);
		String menuName = ConfigLoader.getInstance(this).getMenuName(menuCode);
		TextView view = (TextView) this.findViewById(R.id.title);
		view.setText(menuName);
	}

	private void loadOrderSetting(Map<String, String> extraParams) {
		Map<String, String> params = new LinkedHashMap<String, String>();
		if (extraParams != null) {
			params.putAll(extraParams);
		}
		LoadOrderSettingTask lst = new LoadOrderSettingTask(this);
		lst.setRetAsArray(true);
		lst.execute(Constant.Path_OrderSetting, params);
	}

	private void renderOrderSettingData(JSONArray result) {
		List<Map<String, String>> item = this.getItemDatas(result);
		String[] from = new String[] { "label", "checked" };
		int[] to = new int[] { R.id.itemText, R.id.itemCheck };

		final SimpleAdapter adapter = new OrderSettingAdapter(this, item,
				R.layout.activity_order_setting_item, from, to);
		ListView listView = (ListView) this.findViewById(R.id.itemList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Map<String, String> itemMap = (Map<String, String>) adapter
						.getItem(position);
				loadOrderSetting(itemMap);
			}
		});
	}

	private List<Map<String, String>> getItemDatas(JSONArray result) {
		List<Map<String, String>> datas = new LinkedList<Map<String, String>>();
		int len = result.length();
		for (int i = 0; i < len; i++) {
			Map<String, String> item = new HashMap<String, String>();
			JSONObject obj = result.optJSONObject(i);
			item.put("value", JsonUtil.optString(obj, "value"));
			item.put("label", JsonUtil.optString(obj, "label"));
			item.put("checked", String.valueOf(obj.optBoolean("checked")));
			datas.add(item);
		}
		return datas;
	}

	class OrderSettingAdapter extends SimpleAdapter {

		public OrderSettingAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
		}

		@Override
		public void setViewImage(ImageView v, String value) {
			if ("true".equalsIgnoreCase(value)) {
				v.setVisibility(ImageView.VISIBLE);
			}
		}

		@Override
		public void setViewText(TextView v, String text) {
			if (R.id.itemCheck == v.getId()) {
				if ("true".equalsIgnoreCase(text)) {
					v.setText("√");
				}
			} else {
				super.setViewText(v, text);
			}
		}

	}

	class LoadOrderSettingTask extends HttpRequestTask {

		public LoadOrderSettingTask(Context context) {
			super(context);
		}

		@Override
		protected void handleResult(JSONArray result) {
			renderOrderSettingData(result);
		}
	}

}
