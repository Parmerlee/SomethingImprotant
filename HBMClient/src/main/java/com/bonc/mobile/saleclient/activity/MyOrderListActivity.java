package com.bonc.mobile.saleclient.activity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bonc.mobile.common.Constant;
import com.bonc.mobile.common.activity.BaseListDataActivity;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.saleclient.adapter.OrderAdapter;
import com.bonc.mobile.saleclient.common.ConfigLoader;
import com.bonc.mobile.saleclient.common.Utils;

public class MyOrderListActivity extends BaseListDataActivity {

	private OrderAdapter order_Adapter;

	private Activity activity;

	Map<String, String> param = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myorder_list);
		initView();
		// loadData();
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		UIUtil.showGuideWindow(this, findViewById(R.id.mo_root), "guide.sale",
				new int[] { R.mipmap.guide_s1, R.mipmap.guide_s2,
						R.mipmap.guide_s3, R.mipmap.guide_s4,
						R.mipmap.guide_s5, R.mipmap.guide_s6 });
	}

	@Override
	protected void loadData() {
		String action = "/bi/approve/";
		param = new HashMap<String, String>();
		param.put("userId", ConfigLoader.getInstance(activity).userId);

		// clickCode 点击菜单编码
		// appType 应用类型
		// userCode
		// clickType KPI|MENU
		// queryCode 查询KPI数据时，代表查询地域编码
		// os
		param.put("clickCode", menuCode);

		param.put("appType", "BI_ANDROID");
		param.put("clickType", "MENU");

		if ("172".equals(ConfigLoader.getInstance(this).getMenuAttr(menuCode,
				ConfigLoader.KEY_MENU_TYPE))) {
			action += "QueryHsWFTaskInfo";
			param.put("beginTime", "2015-01-01");

		} else
			action += "QueryAwaitOrders";
		new LoadDataTask(this, Constant.BASE_PATH).execute(action, param);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loadData();
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		activity = this;
		View holderView = getLayoutInflater().from(activity).inflate(
				R.layout.order_adapter_item_head, null);
		View status = holderView.findViewById(R.id.order_adapter_item_head_tv5);
		if ("172".equals(ConfigLoader.getInstance(this).getMenuAttr(menuCode,
				ConfigLoader.KEY_MENU_TYPE))) {
			status.setVisibility(View.GONE);
		}
		mList.addHeaderView(holderView, null, false);
	}

	@Override
	protected void bindData(JSONObject result) {
		if (!Utils.checkResult(result, activity))
			return;

		order_Adapter = new OrderAdapter(activity, 0, result, menuCode);

		mList.setAdapter(order_Adapter);
		// Toast.makeText(this, "AAAAAAAA:" + result, 1).show();
	}

	@SuppressWarnings("unchecked")
	public void doAdapterClick(View v, Object object) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.order_adapter_item_tv1:
			param = new LinkedHashMap<String, String>();
			param.put("orderId",
					((HashMap<String, String>) object).get("taskId"));

			param.put("clickCode", menuCode);

			param.put("appType", "BI_ANDROID");
			param.put("clickType", "MENU");

			new BaseLoadDate(activity, Constant.BASE_PATH, 1).execute(
					"/oa/approve/QueryChargeInfo", param);
			break;
		case R.id.order_adapter_item_tv5:

			startActivity(new Intent(activity, OrderApproveActivity.class)
					.putExtra("map", (HashMap<String, String>) object)
					.putExtra("menuCode", menuCode));
			break;
		case R.id.order_adapter_item_tv2:

			break;
		default:
			break;
		}
	}

	public class BaseLoadDate extends HttpRequestTask {

		int request = -1;

		protected void bindData(JSONObject result, int requestCode) {
			// Toast.makeText(activity, "AAA", 1).show();
			// List<Map<String, String>> list_date = new ArrayList<Map<String,
			// String>>();

			// JSONArray arr = new JSONArray();
			// arr = JsonUtil.optJSONArray(result, "body");

			if (!Utils.checkResult(result, activity))
				return;

			JSONObject obj;
			try {
				obj = JsonUtil.optJSONArray(result, "body").getJSONObject(0);
				Map<String, String> map = new HashMap<String, String>();
				map.put("title", (String) obj.get("chargeName"));
				map.put("content",
						JsonUtil.toList((JSONArray) obj.get("chargeLev"))
								.get(0).get("chargeLevDesc"));
				map.put("type", (String) obj.get("chargeType"));
				map.put("market", (String) obj.get("market"));

				// new PopWindowDialog(activity, map);
				// new PopWindow(activity, map).showAsDropDown(vaaa);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		protected void bindData(JSONArray result, int requestCode) {
			// Toast.makeText(activity, "BBB", 1).show();
			// PopWindowDialog dialog = new PopWindowDialog(activity,
			// order_Adapter);
		}

		public BaseLoadDate(Context context) {
			super(context);
		}

		public BaseLoadDate(Context context, String basePath, int request) {
			super(context, basePath);
			this.request = request;
		}

		@Override
		protected void handleResult(JSONObject result) {
			bindData(result, request);
		}

		@Override
		protected void handleResult(JSONArray result) {
			bindData(result, request);
		}
	}

}
