package com.bonc.mobile.saleclient.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.common.Constant;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.view.FlowView;
import com.bonc.mobile.common.view.SimpleListView;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.saleclient.activity.MyOrderListActivity;
import com.bonc.mobile.saleclient.common.PopWindow;
import com.bonc.mobile.saleclient.common.Utils;

public class OrderAdapter extends ArrayAdapter<Object> implements
		OnClickListener {

	private MyOrderListActivity activity;

	protected List<Map<String, String>> list_date = new ArrayList<Map<String, String>>();

	List<Map<String, String>> list_date_base = new ArrayList<Map<String, String>>();

	Map<String, String> map = new HashMap<String, String>();

	// TimeAdapter adapter;

	private SimpleListView lv = null;

	private FlowView fv = null;

	private View view_pop;

	private String title = null;

	private TextView tv_id;

	private int pos = -1;

	private String menuCode;

	public OrderAdapter(Context context, int resource, JSONObject object,
			String menuCode) {
		super(context, resource);
		// TODO Auto-generated constructor stub
		activity = (MyOrderListActivity) context;

		list_date = JsonUtil.toList(JsonUtil.optJSONArray(object, "body"));

		this.menuCode = menuCode;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_date.size();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(activity).inflate(
					R.layout.order_adapter_item, null);
			holder.name = (TextView) convertView
					.findViewById(R.id.order_adapter_item_tv1);
			holder.id = (TextView) convertView
					.findViewById(R.id.order_adapter_item_tv2);
			holder.type = (TextView) convertView
					.findViewById(R.id.order_adapter_item_tv3);
			holder.creator = (TextView) convertView
					.findViewById(R.id.order_adapter_item_tv4);
			holder.status = (TextView) convertView
					.findViewById(R.id.order_adapter_item_tv5);
			holder.flowview = (FlowView) convertView
					.findViewById(R.id.flowview);
			holder.listview = (SimpleListView) convertView
					.findViewById(R.id.basetime_simple);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		initResource();

		holder.name.setText(list_date.get(position).get("orderName"));
		// holder.listview.setDivider(0);
		if (!TextUtils.isEmpty(list_date.get(position).get("taskId")))
			holder.id.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// if (holder.flowview.getVisibility() == View.GONE) {
					fv = holder.flowview;
					tv_id = (TextView) v;
					pos = position;
					doLoading(list_date.get(position).get("workflowId"));
					// }else{
					// holder.flowview.setVisibility(View.GONE);
					// }

				}
			});
		else {
			holder.id.setCompoundDrawables(null, null, null, null);
			holder.status.setVisibility(View.GONE);

		}
		holder.id.setText(list_date.get(position).get("orderId"));
		holder.type.setText(list_date.get(position).get("orderType"));
		holder.creator.setText(list_date.get(position).get("proposer"));
		holder.status.setText(list_date.get(position).get("orderType"));

		holder.status.setText("待处理");

		holder.name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String type = list_date.get(position).get("orderType");
				map = new HashMap<String, String>();
				Log.d("AAAAAAAAAAA", list_date.get(position).get("taskId"));
				map.put("orderId", list_date.get(position).get("orderId"));
				map.put("title", list_date.get(position).get("orderName"));

				map.put("clickCode", menuCode);
				map.put("menuCode", menuCode);
				map.put("appType", "BI_ANDROID");
				map.put("clickType", "MENU");

				title = list_date.get(position).get("orderName");
				view_pop = v;
				if (TextUtils.equals("资费", type)) {
					// activity.doAdapterClick(v, map);
					new BaseLoadDate(activity, Constant.BASE_PATH, 2).execute(
							"/bi/approve/QueryChargeInfo", map);
				} else if (TextUtils.equals("营销案", type)) {
					new BaseLoadDate(activity, Constant.BASE_PATH, 3).execute(
							"/bi/approve/QuerySaleInfo", map);
				} else if (TextUtils.equals("业务变更", type)) {
					new BaseLoadDate(activity, Constant.BASE_PATH, 4).execute(
							"/bi/approve/QueryBusiChangeInfo", map);
				} else {
					Toast.makeText(activity, "暂不支持该类型", Toast.LENGTH_SHORT).show();
				}

			}
		});
		holder.status.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				map = new HashMap<String, String>();
				map.put("taskId", list_date.get(position).get("taskId"));
				map.put("taskTemplateId",
						list_date.get(position).get("taskTemplateId"));
				map.put("templateCode",
						list_date.get(position).get("templateCode"));
				map.put("title", list_date.get(position).get("orderName"));
				activity.doAdapterClick(v, map);

				// map.put("mark", "" + 2);
				// map.put("title", title);
				// map.put("market", "test");
				//
				// map.put("type", "test");
				// map.put("content", "test");
				//
				// new PopWindow(activity, map).showAsDropDown(v, 0,
				// -50);
			}
		});
		return convertView;
	}

	protected void doLoading(String value) {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("workflowId", value);
		// param.put("workflowId", "HB^10^0000000000000006735");
		param.put("clickCode", menuCode);
		param.put("menuCode", menuCode);
		param.put("appType", "BI_ANDROID");
		param.put("clickType", "MENU");
		new BaseLoadDate(activity, Constant.BASE_PATH, 1).execute(
				"/bi/approve/QueryWFTaskInfo", param);

	}

	private class TimeAdapter extends ArrayAdapter<List<Map<String, String>>> {

		List<Map<String, String>> list;

		public TimeAdapter(Context context, int resource,
				List<Map<String, String>> list) {
			super(context, resource);
			// TODO Auto-generated constructor stub
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			convertView = LayoutInflater.from(activity).inflate(
					R.layout.time_base_layout, null);
			convertView.setLayoutParams(Utils.createParams(activity, 3));
			// View start = convertView.findViewById(R.id.view_start);
			View end = convertView.findViewById(R.id.view_end);
			TextView text = (TextView) convertView.findViewById(R.id.show_time);
			final ImageView image = (ImageView) convertView
					.findViewById(R.id.image);
			// start.setLayoutParams(Utils.createParams(0, activity, 6));
			// end.setLayoutParams(Utils.createParams(0, activity, 6));
			// if (position != 0) {
			// start.setVisibility(View.VISIBLE);
			// // end.setVisibility(View.VISIBLE);
			// }
			// else {
			// convertView.setLayoutParams(Utils.createParams(activity, 10));
			// }
			if (position == getCount() - 1) {
				// start.setVisibility(View.VISIBLE);
				end.setVisibility(View.GONE);
				convertView.setLayoutParams(Utils.createParams(activity, -1));
				this.notifyDataSetChanged();
			}
			// else {
			// convertView.setLayoutParams(Utils.createParams(0,activity, 6));
			// }

			String str = list.get(position).get("state");
			if (!TextUtils.equals(str, "已经完成")) {
				image.setBackgroundResource(R.mipmap.flow_item_red);
			} else {
				image.setBackgroundResource(R.mipmap.flow_item_green);
			}
			text.setText(list.get(position).get("currentStage"));
			// image.setOnClickListener(doClick(image, position, list));
			// text.setOnClickListener(doClick(image, position, list));

			image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					final Map<String, String> datemap = new HashMap<String, String>();
					datemap.put("nextStap", list.get(position).get("nextStap"));
					datemap.put("description",
							list.get(position).get("description"));
					datemap.put("taskReceiveDate",
							list.get(position).get("taskReceiveDate"));
					datemap.put("state", list.get(position).get("state"));
					datemap.put("dealStaff", list.get(position)
							.get("dealStaff"));
					datemap.put("receiveStaff",
							list.get(position).get("receiveStaff"));
					datemap.put("orgName", list.get(position).get("orgName"));
					datemap.put("decision", list.get(position).get("decision"));
					datemap.put("corporation",
							list.get(position).get("corporation"));
					datemap.put("taskFinishDate",
							list.get(position).get("taskFinishDate"));
					datemap.put("currentStage",
							list.get(position).get("currentStage"));

					if (!TextUtils.equals(list.get(position).get("state"),
							"已经完成")) {
						if (pos != -1) {
							v.setId(R.id.order_adapter_item_tv5);

							map = new HashMap<String, String>();
							map.put("taskId", list_date.get(pos).get("taskId"));
							map.put("taskTemplateId",
									list_date.get(pos).get("taskTemplateId"));
							map.put("templateCode",
									list_date.get(pos).get("templateCode"));
							map.put("title", list_date.get(pos)
									.get("orderName"));

							activity.doAdapterClick(v, map);
						}
					} else {
						PopWindow pop = new PopWindow(activity, datemap);
						Log.d("AAAAAAAAAAAA", "BB:" + pop.getPopHeight());
						int[] loaction = new int[2];

						v.getLocationOnScreen(loaction);
						// pop.showAsDropDown(v, 10, -pop.getPopHeight());
						pop.setLocationOnSc(loaction);
						pop.setPositionHeight(loaction[1]);
						pop.showPop(v, 10, 0);
						// Log.d("AAAAAAAAAAAA", "dd:" + loaction[1]);
						// Log.d("AAAAAAAAAAAA", "cc:" +pop.isShowing());
						// Utils.getSystemHeight(activity));
						// Utils.getSystemHeight(activity);

					}

				}
			});
			return convertView;
		}

	}

	public OnClickListener doClick(View v, final int position,
			final List<Map<String, String>> list) {

		final Map<String, String> datemap = new HashMap<String, String>();
		datemap.put("nextStap", list.get(position).get("nextStap"));
		datemap.put("description", list.get(position).get("description"));
		datemap.put("taskReceiveDate", list.get(position)
				.get("taskReceiveDate"));
		datemap.put("state", list.get(position).get("state"));
		datemap.put("dealStaff", list.get(position).get("dealStaff"));
		datemap.put("receiveStaff", list.get(position).get("receiveStaff"));
		datemap.put("orgName", list.get(position).get("orgName"));
		datemap.put("decision", list.get(position).get("decision"));
		datemap.put("corporation", list.get(position).get("corporation"));
		datemap.put("taskFinishDate", list.get(position).get("taskFinishDate"));
		datemap.put("currentStage", list.get(position).get("currentStage"));

		if (!TextUtils.equals(list.get(position).get("state"), "已经完成")) {

			View.OnClickListener listener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if (pos != -1) {
						v.setId(R.id.order_adapter_item_tv5);

						map = new HashMap<String, String>();
						map.put("taskId", list_date.get(pos).get("taskId"));
						map.put("taskTemplateId",
								list_date.get(pos).get("taskTemplateId"));
						map.put("templateCode",
								list_date.get(pos).get("templateCode"));
						map.put("title", list_date.get(pos).get("orderName"));

						activity.doAdapterClick(v, map);
					}
				}
			};
			return listener;
		} else {

			View.OnClickListener listener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					new PopWindow(activity, datemap).showAsDropDown(v, 10, 0);
				}
			};
			return listener;
		}

	}

	Drawable draw_up, draw_down;

	void initResource() {
		Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(),
				R.mipmap.up);
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		bitmap.recycle();
		bitmap = null;

		draw_up = activity.getResources().getDrawable(R.mipmap.up);
		draw_down = activity.getResources().getDrawable(R.mipmap.down);
		draw_up.setBounds(0, 0, width, height);
		draw_down.setBounds(0, 0, width, height);

	}

	public class BaseLoadDate extends HttpRequestTask {

		int request = -1;

		Map<String, String> map = new HashMap<String, String>();

		protected void bindData(JSONObject result, int requestCode) {

			Log.d("AAAAAAAAAAAAAA", "AAAAAAAAA:" + requestCode);
			if (!Utils.checkResult(result, activity))
				return;

			switch (requestCode) {
			case 1:

				list_date_base = JsonUtil.toList(JsonUtil.optJSONArray(result,
						"body"));
				int visible = fv.getVisibility();

				if (visible == View.GONE) {

					((TextView) tv_id).setCompoundDrawables(null, null, null,
							draw_up);
					if (list_date_base.size() != 0) {
						fv.setVisibility(View.VISIBLE);
						fv.setAdapter(new TimeAdapter(activity, 0,
								list_date_base));
						((TextView) tv_id).setCompoundDrawables(null, null,
								null, draw_up);
					} else {
						((TextView) tv_id).setCompoundDrawables(null, null,
								null, draw_down);
						fv.setVisibility(View.GONE);
						Toast.makeText(activity, "暂无数据", Toast.LENGTH_SHORT).show();
					}

				} else {
					((TextView) tv_id).setCompoundDrawables(null, null, null,
							draw_down);
					fv.setVisibility(View.GONE);
				}

				break;
			case 2:
				JSONObject obj = null;
				try {
					if (!result.equals(null)
							&& !result.getJSONArray("body").equals(null)
							&& (result.getJSONArray("body").length() > 0)) {

						obj = JsonUtil.optJSONArray(result, "body")
								.getJSONObject(0);
						map.put("mark", "" + requestCode);
						map.put("title", title);

						if (!obj.equals(null)
								&& !obj.getJSONArray("chargeLev").equals(null)
								&& (obj.getJSONArray("chargeLev").length() > 0)) {

							// if (!obj.equals(null)
							// && !obj.getJSONArray("chargeLev").equals(
							// null)
							// && (obj.getJSONArray("chargeLev").length() > 0))
							// {

							// if (!JsonUtil.optJSONArray(obj, "chargeLev")
							// .equals(null)
							// && !JsonUtil.optJSONArray(obj, "chargeLev")
							// .getJSONObject(0).equals(null))
							map.put("content",
									(String) JsonUtil
											.optJSONArray(obj, "chargeLev")
											.getJSONObject(0)
											.get("chargeLevDesc"));
							map.put("type", (String) obj.get("chargeType"));
							map.put("market", (String) obj.get("market"));

							// }
						}
					}

					if (!map.containsKey("content"))
						map.put("content", null);
					if (!map.containsKey("type"))
						map.put("type", null);
					if (!map.containsKey("market"))
						map.put("market", null);

					PopWindow pop = new PopWindow(activity, map);
					int[] location = new int[2];

					view_pop.getLocationOnScreen(location);
					pop.setPositionHeight(location[1]);
					pop.setLocationOnSc(location);
					pop.showPop(view_pop, 0, -10);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case 3:
				map.put("mark", "" + requestCode);
				map.put("title", title);
				try {
					if (!result.equals(null)
							&& !result.getJSONArray("body").equals(null)
							&& (result.getJSONArray("body").length() > 0)) {
						obj = JsonUtil.optJSONArray(result, "body")
								.getJSONObject(0);
						Log.d("aaaaa", "LENGTH:"
								+ obj.getJSONArray("saleBatch").length());
						if (!obj.equals(null)
								&& !obj.getJSONArray("saleBatch").equals(null)
								&& (obj.getJSONArray("saleBatch").length() > 0)) {

							obj = JsonUtil.optJSONArray(obj, "saleBatch")
									.getJSONObject(0);

							// JSONArray obj2 = ;
							if (!obj.equals(null)
									&& !obj.getJSONArray("saleLevs").equals(
											null)
									&& (obj.getJSONArray("saleLevs").length() > 0)) {

								obj = JsonUtil.optJSONArray(obj, "saleLevs")
										.getJSONObject(0);
								if (!obj.equals(null)) {

									map.put("market",
											(String) obj.get("levDes"));

									map.put("type", (String) obj.get("levType"));
									map.put("content",
											(String) obj.get("weaponDes"));

								}
							}
						}
					}
					if (!map.containsKey("market")) {
						map.put("market", null);
					}
					if (!map.containsKey("type")) {
						map.put("type", null);
					}
					if (!map.containsKey("content")) {
						map.put("content", null);
					}

					PopWindow pop = new PopWindow(activity, map);
					int[] location = new int[2];

					view_pop.getLocationOnScreen(location);
					pop.setPositionHeight(location[1]);
					pop.setLocationOnSc(location);
					pop.showPop(view_pop, 0, -10);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case 4:

				try {
					obj = JsonUtil.optJSONArray(result, "body")
							.getJSONObject(0);

					map.put("content", (String) obj.get("description"));

					obj = JsonUtil.optJSONArray(obj, "items").getJSONObject(0);
					map.put("mark", "" + requestCode);
					map.put("title", title);
					map.put("market", (String) obj.get("batchCode"));

					map.put("type", (String) obj.get("changeContent"));

					PopWindow pop = new PopWindow(activity, map);
					int[] location = new int[2];

					view_pop.getLocationOnScreen(location);
					pop.setPositionHeight(location[1]);
					pop.setLocationOnSc(location);
					pop.showPop(view_pop, 0, -10);
					// pop.showAsDropDown(v, 10, -pop.getPopHeight());
					// pop.setPositionHeight(height);
					// new PopWindow(activity, map).showAsDropDown(view_pop, 0,
					// -50);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			default:
				break;
			}

		}

		protected void bindData(JSONArray result, int requestCode) {
			// Toast.makeText(activity, "BBB", 1).show();
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

	public class ViewHolder {

		TextView name, id, type, creator, status;

		FlowView flowview;
		SimpleListView listview;
	}

	@Override
	public void onClick(View v) {
		Map<String, String> map = new HashMap<String, String>();
		switch (v.getId()) {
		case R.id.order_adapter_item_tv5:
			map = new HashMap<String, String>();
			map.put("taskId", list_date.get(0).get("taskId"));
			break;

		default:
			break;
		}
		activity.doAdapterClick(v, map);
	}
}
