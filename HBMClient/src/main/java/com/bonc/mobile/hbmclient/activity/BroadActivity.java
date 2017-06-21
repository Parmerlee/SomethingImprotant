package com.bonc.mobile.hbmclient.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.VelocityTrackerCompat;
import android.text.TextUtils;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.Constant;
import com.bonc.mobile.common.util.FileUtils;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.common.view.DatePickerButton;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.HorizontalScrollViewSynchronizer;
import com.bonc.mobile.hbmclient.view.LinkageHScrollView;
import com.bonc.mobile.hbmclient.view.TopNView;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;
import com.bonc.mobile.saleclient.common.Utils;

public class BroadActivity extends SlideHolderActivity {

	private Activity activity;
	// 第一行
	View broad_order_title, broad_order_up, broad_order_down;

	// 第二行
	View broad_handler_title, broad_handler_up, broad_handler_down;

	// 第三行
	View broad_channel_title, broad_channel_up, broad_channel_down;

	View[] title_array, content_array;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_broad);
		setMainContent(
				getIntent().getStringExtra(MenuEntryAdapter.KEY_MENU_CODE),
				R.layout.activity_broad);
		activity = this;
		initView();
		// initData();
		loadData();
	}

	private DatePickerButton dateSelect;

	private Calendar calendar;

	private void initView() {
		// TODO Auto-generated method stub

		findViewById(android.R.id.content).setBackgroundDrawable(
				WatermarkImage.getWatermarkDrawable());

		Button button = (Button) activity.findViewById(R.id.id_button_logo);
		button.setVisibility(View.VISIBLE);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				slideHolder.toggle();
			}
		});

		calendar = Calendar.getInstance();

		// 去掉地区选择框
		// View view= this.findViewById(R.id.kpi_panel);
		(this.findViewById(R.id.kpi_panel)).findViewById(R.id.area_button)
				.setVisibility(View.GONE);

		this.dateSelect = (DatePickerButton) this
				.findViewById(R.id.date_button);
		this.dateSelect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				chooseDate();
			}
		});

		// 第一行
		broad_order_title = this.findViewById(R.id.broad_order_title);
		TextViewUtils.setText(broad_order_title, R.id.group_title, "订单转化");

		broad_order_up = this.findViewById(R.id.broad_order_up);
		TextViewUtils.setText(broad_order_up, R.id.index_name, "受理");
		broad_order_down = this.findViewById(R.id.broad_order_down);
		TextViewUtils.setText(broad_order_down, R.id.index_name, "退单");

		// 第二行
		broad_handler_title = this.findViewById(R.id.broad_handler_title);
		TextViewUtils.setText(broad_handler_title, R.id.group_title, "处理效率");
		//
		broad_handler_up = this.findViewById(R.id.broad_handler_up);
		TextViewUtils.setText(broad_handler_up, R.id.index_name, "装机");

		broad_handler_down = this.findViewById(R.id.broad_handler_down);
		TextViewUtils.setText(broad_handler_down, R.id.index_name, "历时");

		// 第三行
		broad_channel_title = this.findViewById(R.id.broad_channel_title);
		TextViewUtils.setText(broad_channel_title, R.id.group_title, "订单渠道");
		//
		broad_channel_up = this.findViewById(R.id.broad_channel_up);
		TextViewUtils.setText(broad_channel_up, R.id.index_name, "订单");

		broad_channel_down = this.findViewById(R.id.broad_channel_down);
		TextViewUtils.setText(broad_channel_down, R.id.index_name, "APP订单");

		title_array = new View[] { broad_order_title, broad_handler_title,
				broad_channel_title };
		content_array = new View[] { broad_order_up, broad_order_down,
				broad_handler_up, broad_handler_down, broad_channel_up,
				broad_channel_down };
		HorizontalScrollViewSynchronizer sy = new HorizontalScrollViewSynchronizer();
		for (int i = 0; i < title_array.length; i++) {
			sy.addView((LinkageHScrollView) title_array[i]
					.findViewById(R.id.index_content));
		}
		for (int i = 0; i < content_array.length; i++) {
			sy.addView((LinkageHScrollView) content_array[i]
					.findViewById(R.id.index_content));
		}

	}

	String opTime = null;
	JSONArray array;

	private void initData() {
		// TODO Auto-generated method stub

		String str = FileUtils.readLocalJson(activity, "jiakuan1.json");
		try {
			JSONObject object = new JSONObject(str);
			if (object.optBoolean("flag")) {

				array = object.getJSONArray("data");
				opTime = object.getString("opTime");
				calendar.setTime(DateUtil.getDate(opTime, DateUtil.PATTERN_8));

				dateSelect.setText(DateUtil.oneStringToAntherString(opTime,
						DateUtil.PATTERN_8, "yyyy/MM/dd"));

				int flag = 0;

				for (int i = 0; i < array.length(); i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					// title
					map.put("kpiName",
							((JSONObject) array.get(i)).get("kpiName"));
					// value
					map.put("kpiValue",
							((JSONObject) array.get(i)).get("kpiValue"));
					// value
					map.put("kpiCode",
							((JSONObject) array.get(i)).get("kpiCode"));

					//
					List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();

					Object nameobject;
					nameobject = ((JSONObject) array.get(i)).get("areaName");
					if (!nameobject.equals(null)) {

						JSONArray name = ((JSONObject) array.get(i))
								.getJSONArray("areaName");
						JSONArray num = ((JSONObject) array.get(i))
								.getJSONArray("areaNum");
						for (int j = 0; j < name.length(); j++) {
							Map<String, Object> temp = new HashMap<String, Object>();
							if (!name.get(j).equals(null)) {
								temp.put("name", name.get(j));
								temp.put("num", Utils.changeValue(Double
										.valueOf((String) num.get(j))));
							}

							if (!temp.isEmpty()) {

								listems.add(temp);
							}
						}

						if (listems.size() != 0) {
							map.put("list", listems);
						}
					}

					// 点击信息
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					Object menujson = ((JSONObject) array.get(i)).get("detail");

					if (!menujson.equals(null)) {
						JSONArray menuCode = ((JSONObject) array.get(i))
								.getJSONArray("detail");
						for (int k = 0; k < menuCode.length(); k++) {
							if (!menuCode.isNull(k)) {
								Map<String, Object> temp = new HashMap<String, Object>();

								temp.put("detailName", ((JSONObject) menuCode
										.get(k)).getString("detailName"));

								temp.put("detailCode", ((JSONObject) menuCode
										.get(k)).getString("detailCode"));
								menuList.add(temp);
							}
						}
					}

					// detail
					map.put("detail", menuList);
					// System.out.println("menuList:"+map.toString());
					bindViewTop(i, map);

				}

			} else {
				Toast.makeText(activity, object.getString("msg"), 1).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void loadData() {

		Map<String, String> param = new LinkedHashMap<String, String>();

		param.put("menuCode", "15000002");
		param.put("page", String.valueOf(1));
		param.put("sTime", opTime);
		param.put("eTime", opTime);

		new LoadDataTask(this, Constant.BASE_PATH).execute(
				"/bi/hbmobile/network/getInfo", param);

	}

	private void bindData(JSONObject object) {

		try {

			if (object.optBoolean("flag")) {

				TextViewUtils.setText(activity, R.id.title,
						object.optString("menuName"));

				array = object.getJSONArray("data");
				opTime = object.getString("opTime");
				calendar.setTime(DateUtil.getDate(opTime, DateUtil.PATTERN_8));

				dateSelect.setText(DateUtil.oneStringToAntherString(opTime,
						DateUtil.PATTERN_8, "yyyy/MM/dd"));

				int flag = 0;

				for (int i = 0; i < array.length(); i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					// title
					map.put("kpiName",
							((JSONObject) array.get(i)).get("kpiName"));
					// value
					Object obj = ((JSONObject) array.get(i)).get("kpiValue");
					if (!obj.equals(null)) {

						map.put("kpiValue",
								((JSONObject) array.get(i)).get("kpiValue"));
					} else {
						map.put("kpiValue", null);
					}
					// value
					map.put("kpiCode",
							((JSONObject) array.get(i)).get("kpiCode"));

					List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();

					Object nameobject;
					nameobject = ((JSONObject) array.get(i)).get("areaName");
					if (!nameobject.equals(null)) {

						JSONArray name = ((JSONObject) array.get(i))
								.getJSONArray("areaName");
						JSONArray num = ((JSONObject) array.get(i))
								.getJSONArray("areaNum");
						for (int j = 0; j < name.length(); j++) {
							Map<String, Object> temp = new HashMap<String, Object>();
							if (!name.get(j).equals(null)) {
								temp.put("name", name.get(j));
								temp.put("num", (String) num.get(j));
							}

							if (!temp.isEmpty()) {

								listems.add(temp);
							}
						}

						if (listems.size() != 0) {
							// System.out.println("map:" + flag);

							// list
							map.put("list", listems);
							// 解析数据
							// bindViewTop(flag++, listems);
						}
					}

					// 点击信息
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					Object menujson = ((JSONObject) array.get(i)).get("detail");

					if (!menujson.equals(null)) {
						JSONArray menuCode = ((JSONObject) array.get(i))
								.getJSONArray("detail");
						for (int k = 0; k < menuCode.length(); k++) {
							if (!menuCode.isNull(k)) {
								Map<String, Object> temp = new HashMap<String, Object>();
								// System.out.println("AAAAAA:"
								// + ((JSONObject) menuCode.get(k))
								// .getString("detailName"));
								temp.put("detailName", ((JSONObject) menuCode
										.get(k)).getString("detailName"));
								// temp.put("rank", String.valueOf(i));
								temp.put("detailCode", ((JSONObject) menuCode
										.get(k)).getString("detailCode"));
								menuList.add(temp);
							}
						}
					}
					// else {
					// menuList.add(new HashMap<String, Object>());
					// // menuList.add(new HashMap<String, Object>());
					// }

					// detail
					map.put("detail", menuList);
					// System.out.println("menuList:"+map.toString());
					bindViewTop(i, map);

				}
				// menuList.add(list);
				// }
				// System.out.println("list:" + menuList.toString());
				// bindClick();

			} else {
				Toast.makeText(activity, object.getString("msg"), 1).show();
				removeDate();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(str);

	}

	private void removeDate() {
		int[] id_title = { R.id.value1, R.id.value2, R.id.value3 };
		int[] id_array = { R.id.extra1, R.id.extra2, R.id.extra3, R.id.extra4,
				R.id.extra5, R.id.extra6 };
		for (int i = 0; i < content_array.length; i++) {
			for (int j = 0; j < id_title.length; j++) {
				View view = content_array[i].findViewById(id_title[j]);
				if (view.getVisibility() == View.VISIBLE) {
					((TextView) view).setText("-- --");
				}
			}
			// R.id.area_topn
			View view = content_array[i].findViewById(R.id.area_topn);
			((TopNView) view).setAdapter(null);
			for (int j = 0; j < id_array.length; j++) {

				View view1 = content_array[i].findViewById(id_array[j]);
				view1.setEnabled(false);
			}
		}

	}

	private void bindClick(TextView tv, Map<String, Object> map) {

		// System.out.println("map:" + map.toString());
		tv.setEnabled(true);
		tv.setVisibility(View.VISIBLE);
		tv.setText((String) map.get("detailName"));
		tv.setTag((String) map.get("detailCode"));
		tv.setOnClickListener((OnClickListener) activity);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		Intent intent = new Intent(activity, BroadDetailActivity.class);
		intent.putExtra("opTime", opTime);
		intent.putExtra("title", ((TextView) v).getText().toString());
		intent.putExtra(MenuEntryAdapter.KEY_MENU_CODE, (String) v.getTag());
		startActivity(intent);
	}

	// 绑定topN列表，具体待定
	@SuppressWarnings("unchecked")
	private void bindViewTop(int pos, Map map) {

		if (map.get("kpiName").equals(null)) {
			return;
		}
		// if (map.get("kpiName").equals("平均历时")) {
		// System.out.println("AAAA");
		// }
		Object obj = null;
		View parantView = null;
		switch (pos) {
		case 0:

			// 受理量
			parantView = content_array[0];
			TextViewUtils.setText(parantView, R.id.title1,
					(String) map.get("kpiName"));
			// if (!TextUtils.equals(null, (String) map.get("kpiValue")))
			// TextViewUtils.setText(parantView, R.id.value1, Utils
			// .changeValue(Double.valueOf((String) map
			// .get("kpiValue"))));

			if (!TextUtils.equals(null, (String) map.get("kpiValue")))
				TextViewUtils.setText(parantView, R.id.value1,
						(String) map.get("kpiValue"));
			else {
				TextViewUtils.setText(parantView, R.id.value1, "- -");
			}
			// Utils.changeValue(Double.valueOf((String) map.get("kpiValue"))));

			obj = map.get("detail");
			if (obj.equals(null)) {
				break;
			}

			for (int i = 0; i < ((List<Map<String, Object>>) obj).size(); i++) {
				TextView tv = null;
				if (i == 0) {
					tv = (TextView) parantView.findViewById(R.id.extra1);
				}
				if (i == 1) {
					tv = (TextView) parantView.findViewById(R.id.extra2);
				}
				bindClick(tv, ((List<Map<String, Object>>) obj).get(i));
			}

			View view = (View) parantView.findViewById(R.id.title2).getParent();
			view.setVisibility(View.GONE);
			view = (View) parantView.findViewById(R.id.title3).getParent();
			view.setVisibility(View.GONE);

			break;
		case 1:
			// 退单量
			parantView = content_array[1];
			TextViewUtils.setText(parantView, R.id.title1,
					(String) map.get("kpiName"));
			if (!TextUtils.equals(null, (String) map.get("kpiValue")))
				TextViewUtils.setText(parantView, R.id.value1,
						(String) map.get("kpiValue"));
			else {
				TextViewUtils.setText(parantView, R.id.value1, "- -");
			}
			obj = map.get("detail");
			if (obj.equals(null)) {
				break;
			}

			for (int i = 0; i < ((List<Map<String, Object>>) obj).size(); i++) {
				TextView tv = null;
				if (i == 0) {
					tv = (TextView) parantView.findViewById(R.id.extra1);
				}
				if (i == 1) {
					tv = (TextView) parantView.findViewById(R.id.extra2);
				}
				bindClick(tv, ((List<Map<String, Object>>) obj).get(i));
			}
			break;
		case 2:
			// 退单率
			parantView = content_array[1];
			TextViewUtils.setText(parantView, R.id.title2,
					(String) map.get("kpiName"));
			if (!TextUtils.equals(null, (String) map.get("kpiValue")))
				TextViewUtils.setText(parantView, R.id.value2,
						(String) map.get("kpiValue"));
			else {
				TextViewUtils.setText(parantView, R.id.value2, "- -");
			}
			obj = map.get("detail");
			if (obj.equals(null)) {
				break;
			}

			for (int i = 0; i < ((List<Map<String, Object>>) obj).size(); i++) {
				TextView tv = null;
				if (i == 0) {
					tv = (TextView) parantView.findViewById(R.id.extra3);
				}
				if (i == 1) {
					tv = (TextView) parantView.findViewById(R.id.extra4);
				}
				bindClick(tv, ((List<Map<String, Object>>) obj).get(i));
			}

			view = (View) parantView.findViewById(R.id.title3).getParent();
			view.setVisibility(View.GONE);
			break;
		case 3:
			// 订单数
			parantView = content_array[2];
			TextViewUtils.setText(parantView, R.id.title1,
					(String) map.get("kpiName"));
			if (!TextUtils.equals(null, (String) map.get("kpiValue")))
				TextViewUtils.setText(parantView, R.id.value1,
						(String) map.get("kpiValue"));
			else {
				TextViewUtils.setText(parantView, R.id.value1, "- -");
			}
			obj = map.get("detail");
			if (obj.equals(null)) {
				break;
			}

			for (int i = 0; i < ((List<Map<String, Object>>) obj).size(); i++) {
				TextView tv = null;
				if (i == 0) {
					tv = (TextView) parantView.findViewById(R.id.extra1);
				}
				if (i == 1) {
					tv = (TextView) parantView.findViewById(R.id.extra2);
				}
				bindClick(tv, ((List<Map<String, Object>>) obj).get(i));
			}
			break;
		case 4:
			// 已装机
			parantView = content_array[2];
			TextViewUtils.setText(parantView, R.id.title2,
					(String) map.get("kpiName"));
			if (!TextUtils.equals(null, (String) map.get("kpiValue")))
				TextViewUtils.setText(parantView, R.id.value2,
						(String) map.get("kpiValue"));
			else {
				TextViewUtils.setText(parantView, R.id.value2, "- -");
			}
			obj = map.get("detail");
			if (obj.equals(null)) {
				break;
			}

			for (int i = 0; i < ((List<Map<String, Object>>) obj).size(); i++) {
				TextView tv = null;
				if (i == 0) {
					tv = (TextView) parantView.findViewById(R.id.extra3);
				}
				if (i == 1) {
					tv = (TextView) parantView.findViewById(R.id.extra4);
				}
				bindClick(tv, ((List<Map<String, Object>>) obj).get(i));
			}
			break;
		case 5:
			// 及时率
			parantView = content_array[2];
			TextViewUtils.setText(parantView, R.id.title3,
					(String) map.get("kpiName"));
			if (!TextUtils.equals(null, (String) map.get("kpiValue")))
				TextViewUtils.setText(parantView, R.id.value3,
						(String) map.get("kpiValue"));
			else {
				TextViewUtils.setText(parantView, R.id.value3, null);
			}
			obj = map.get("detail");
			if (obj.equals(null)) {
				break;
			}

			for (int i = 0; i < ((List<Map<String, Object>>) obj).size(); i++) {
				TextView tv = null;
				if (i == 0) {
					tv = (TextView) parantView.findViewById(R.id.extra5);
				}
				if (i == 1) {
					tv = (TextView) parantView.findViewById(R.id.extra6);
				}
				bindClick(tv, ((List<Map<String, Object>>) obj).get(i));
			}
			break;
		case 6:
			// 装机总时间
			parantView = content_array[3];
			TextViewUtils.setText(parantView, R.id.title1,
					(String) map.get("kpiName"));
			if (!TextUtils.equals(null, (String) map.get("kpiValue")))
				TextViewUtils.setText(parantView, R.id.value1,
						(String) map.get("kpiValue"));
			else {
				TextViewUtils.setText(parantView, R.id.value1, "- -");
			}
			obj = map.get("detail");
			if (obj.equals(null)) {
				break;
			}

			for (int i = 0; i < ((List<Map<String, Object>>) obj).size(); i++) {
				TextView tv = null;
				if (i == 0) {
					tv = (TextView) parantView.findViewById(R.id.extra1);
				}
				if (i == 1) {
					tv = (TextView) parantView.findViewById(R.id.extra2);
				}
				bindClick(tv, ((List<Map<String, Object>>) obj).get(i));
			}
			break;
		case 7:
			// 平均历时
			parantView = content_array[3];
			TextViewUtils.setText(parantView, R.id.title2,
					(String) map.get("kpiName"));
			if (!TextUtils.equals(null, (String) map.get("kpiValue")))
				TextViewUtils.setText(parantView, R.id.value2,
						(String) map.get("kpiValue"));
			else {
				TextViewUtils.setText(parantView, R.id.value2, "- -");
			}
			obj = map.get("detail");
			if (obj.equals(null)) {
				break;
			}

			for (int i = 0; i < ((List<Map<String, Object>>) obj).size(); i++) {
				TextView tv = null;
				if (i == 0) {
					tv = (TextView) parantView.findViewById(R.id.extra3);
				}
				if (i == 1) {
					tv = (TextView) parantView.findViewById(R.id.extra4);
				}
				bindClick(tv, ((List<Map<String, Object>>) obj).get(i));
			}

			view = (View) parantView.findViewById(R.id.title3).getParent();
			view.setVisibility(View.GONE);
			break;
		case 8:
			// 装机订单量
			parantView = content_array[4];
			TextViewUtils.setText(parantView, R.id.title1,
					(String) map.get("kpiName"));
			if (!TextUtils.equals(null, (String) map.get("kpiValue")))
				TextViewUtils.setText(parantView, R.id.value1,
						(String) map.get("kpiValue"));
			else {
				TextViewUtils.setText(parantView, R.id.value1, "- -");
			}
			obj = map.get("detail");
			if (obj.equals(null)) {
				break;
			}

			for (int i = 0; i < ((List<Map<String, Object>>) obj).size(); i++) {
				TextView tv = null;
				if (i == 0) {
					tv = (TextView) parantView.findViewById(R.id.extra1);
				}
				if (i == 1) {
					tv = (TextView) parantView.findViewById(R.id.extra2);
				}
				bindClick(tv, ((List<Map<String, Object>>) obj).get(i));
			}

			break;
		case 9:
			// APP装机使用率
			parantView = content_array[4];
			TextViewUtils.setText(parantView, R.id.title2,
					(String) map.get("kpiName"));
			if (!TextUtils.equals(null, (String) map.get("kpiValue")))
				TextViewUtils.setText(parantView, R.id.value2,
						(String) map.get("kpiValue"));
			else {
				TextViewUtils.setText(parantView, R.id.value2, "- -");
			}
			obj = map.get("detail");
			if (obj.equals(null)) {
				break;
			}

			for (int i = 0; i < ((List<Map<String, Object>>) obj).size(); i++) {
				TextView tv = null;
				if (i == 0) {
					tv = (TextView) parantView.findViewById(R.id.extra3);
				}
				if (i == 1) {
					tv = (TextView) parantView.findViewById(R.id.extra4);
				}
				bindClick(tv, ((List<Map<String, Object>>) obj).get(i));
			}
			view = (View) parantView.findViewById(R.id.title3).getParent();
			view.setVisibility(View.GONE);
			break;
		case 10:
			// APP订单成功率
			parantView = content_array[5];
			TextViewUtils.setText(parantView, R.id.title1,
					(String) map.get("kpiName"));
			if (!TextUtils.equals(null, (String) map.get("kpiValue")))
				TextViewUtils.setText(parantView, R.id.value1,
						(String) map.get("kpiValue"));
			else {
				TextViewUtils.setText(parantView, R.id.value1, "- -");
			}
			obj = map.get("detail");
			if (obj.equals(null)) {
				break;
			}

			for (int i = 0; i < ((List<Map<String, Object>>) obj).size(); i++) {
				TextView tv = null;
				if (i == 0) {
					tv = (TextView) parantView.findViewById(R.id.extra1);
				}
				if (i == 1) {
					tv = (TextView) parantView.findViewById(R.id.extra2);
				}
				bindClick(tv, ((List<Map<String, Object>>) obj).get(i));
			}

			break;
		case 11:
			// 成功率
			parantView = content_array[5];
			TextViewUtils.setText(parantView, R.id.title2,
					(String) map.get("kpiName"));
			if (!TextUtils.equals(null, (String) map.get("kpiValue")))
				TextViewUtils.setText(parantView, R.id.value2,
						(String) map.get("kpiValue"));
			else {
				TextViewUtils.setText(parantView, R.id.value2, "- -");
			}
			obj = map.get("detail");
			if (obj.equals(null)) {
				break;
			}

			for (int i = 0; i < ((List<Map<String, Object>>) obj).size(); i++) {
				TextView tv = null;
				if (i == 0) {
					tv = (TextView) parantView.findViewById(R.id.extra3);
				}
				if (i == 1) {
					tv = (TextView) parantView.findViewById(R.id.extra4);
				}
				bindClick(tv, ((List<Map<String, Object>>) obj).get(i));
			}

			view = (View) parantView.findViewById(R.id.title3).getParent();
			view.setVisibility(View.GONE);
			break;

		default:
			break;
		}

		List<Map<String, Object>> list = (List<Map<String, Object>>) map
				.get("list");
		if (list != null && list.size() > 0) {

			SimpleAdapter adapter = new SimpleAdapter(activity, list,
					R.layout.topn_view_item_down,
					new String[] { "name", "num" }, new int[] {
							R.id.topn_title, R.id.topn_value });
			((TopNView) parantView.findViewById(R.id.area_topn))
					.setAdapter(adapter);
		} else {
			((TopNView) parantView.findViewById(R.id.area_topn))
					.setAdapter(null);
		}
	}

	void chooseDate() {
		UIUtil.showDatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {

				calendar.set(year, monthOfYear, dayOfMonth);
				// initData();
				opTime = DateUtil.formatter(calendar.getTime(),
						DateUtil.PATTERN_8);
				loadData();
				System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAA");
				dateSelect.setDate(calendar.getTime());
			}
		}, calendar);
	}

	class LoadDataTask extends com.bonc.mobile.common.net.HttpRequestTask {
		public LoadDataTask(Context context, String basepath) {
			super(context, basepath);
		}

		@Override
		protected void handleResult(JSONObject result) {
			bindData(result);
		}

	}

}
