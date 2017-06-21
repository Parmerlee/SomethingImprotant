package com.bonc.mobile.saleclient.activity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.common.Constant;
import com.bonc.mobile.common.activity.BaseDataActivity;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.common.view.DataChooseButton;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.saleclient.common.ConfigLoader;
import com.bonc.mobile.saleclient.common.PopWindowWithList;
import com.bonc.mobile.saleclient.common.Utils;

public class OrderApproveActivity extends BaseDataActivity implements
		OnClickListener, OnItemClickListener, OnDismissListener, TextWatcher {

	Map<String, String> param = new HashMap<String, String>();
	private Activity activity;

	private EditText et_people;

	private View ll_people;

	private TextView et_action;

	private ImageView iv_action, iv_people;

	private String staffId = "";
	String menuCode;

	private boolean click_status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_approve);
		// staffId = ConfigLoader.getInstance(this).userId;
		initView();
		loadData();
	}

	private DataChooseButton action, people;
	EditText text;
	Button btn, search;

	private String taskId;

	private JSONObject result_action, result_people;

	@Override
	protected void initView() {
		super.initView();
		activity = this;
		action = (DataChooseButton) this.findViewById(R.id.approve_action);
		people = (DataChooseButton) this.findViewById(R.id.approve_people);
		text = (EditText) this.findViewById(R.id.approve_discripetion);
		btn = (Button) this.findViewById(R.id.approve_sumit);
		btn.setOnClickListener(this);

		et_action = (TextView) this.findViewById(R.id.approve_action_tv);
		et_people = (EditText) this.findViewById(R.id.approve_people_tv);
		ll_people = this.findViewById(R.id.approve_people_ll);
		// iv_action = (ImageView) this.findViewById(R.id.approve_action_iv);
		iv_people = (ImageView) this.findViewById(R.id.approve_people_iv);
		// iv_action.setOnClickListener(this);
		iv_people.setOnClickListener(this);
		search = (Button) this.findViewById(R.id.button1);
		search.setOnClickListener(this);
		et_action.setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		menuCode = this.getIntent().getExtras().getString("menuCode");
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) this.getIntent()
				.getExtras().get("map");
		param = new HashMap<String, String>();
		param.put("taskTemplateId", map.get("taskTemplateId"));
		param.put("templateCode", map.get("templateCode"));
		param.put("taskId", map.get("taskId"));

		param.put("clickCode", menuCode);
		param.put("menuCode", menuCode);
		param.put("appType", "BI_ANDROID");
		param.put("clickType", "MENU");

		TextViewUtils.setText(this, R.id.title, map.get("title"));
		new LoadDataTask(this, Constant.BASE_PATH).execute(
				"/bi/approve/QueryNextStageInWF", param);
		taskId = map.get("taskId");

	}

	@Override
	protected void bindData(JSONObject result) {

		if (!Utils.checkResult(result, activity)) {
			return;
		}
		result_action = result;
		changeView_act(0);
		// et_action.setText(JsonUtil
		// .toList(JsonUtil.optJSONArray(result, "body")).get(0)
		// .get("nextStepDes"));
		// action.setData(JsonUtil.toList(JsonUtil.optJSONArray(result,
		// "body")),
		// "nextStepDes", "nextStepOps");
		//

		// param.put("userId", "'weiwei'");
		// param.put("roleId",
		// JsonUtil.toList(JsonUtil.optJSONArray(result, "body")).get(0)
		// .get("roleId"));
		// new BaseLoadDate(activity, Constant.BASE_PATH, 2).execute(
		// "/oa/approve/QueryStaffByRoleId", param);

		// people.setData(JsonUtil.toList(JsonUtil.optJSONArray(result,
		// "body")),
		// "roleId", "roleId");
	}

	String nextStepOps;

	private void changeView_act(int i) {
		// TODO Auto-generated method stub

		et_action.setText(JsonUtil
				.toList(JsonUtil.optJSONArray(result_action, "body")).get(i)
				.get("nextStepDes"));

		// et_people.setText(JsonUtil
		// .toList(JsonUtil.optJSONArray(result_action, "body")).get(i)
		// .get("roleId"));
		try {

			nextStepOps = JsonUtil
					.toList(JsonUtil.optJSONArray(result_action, "body"))
					.get(i).get("nextStepOps");
		} catch (Exception e) {
			// TODO: handle exception
			nextStepOps = "";
		}
		et_people.setSelection(et_people.getText().length());
		// et_action.setEnabled(false);
		int roleId = Integer.valueOf(JsonUtil
				.toList(JsonUtil.optJSONArray(result_action, "body")).get(i)
				.get("roleId"));
		if (roleId == -1) {
			ll_people.setVisibility(View.GONE);
			et_people.setText(null);
		} else {
			ll_people.setVisibility(View.VISIBLE);
			param = new LinkedHashMap<String, String>();
			param.put("userId", ConfigLoader.getInstance(activity).userId);
			param.put("roleId", "" + roleId);
			// param.put("userId", "'weiwei'");
			// param.put("userId", "liying1");
			// param.put("roleId", "" + 15);

			param.put("clickCode", menuCode);
			param.put("menuCode", menuCode);
			param.put("appType", "BI_ANDROID");
			param.put("clickType", "MENU");
			new BaseLoadDate(activity, Constant.BASE_PATH, 2).execute(
					"/bi/approve/QueryStaffByRoleId", param);
		}
	}

	PopWindowWithList pop_act, pop_peo;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.approve_sumit:

			String name = et_people.getText().toString();
			// param = new HashMap<String, String>();
			// param.put("staffNames", name);
			// new BaseLoadDate(activity, Constant.BASE_PATH, 3).execute(
			// "/oa/approve/QueryByStaffName", param);

			if (ll_people.getVisibility() != View.GONE)
				// 选择的下一环节处理人
				if (!TextUtils.isEmpty(name)) {
					// Toast.makeText(activity,
					// "bbbbbbbbbbb:" + et_people.getText().toString(), 1)
					// .show();

					// param.put("nsStaffId", et_people.getText().toString());
					doSmuit();
				} else {
					Toast.makeText(activity, "处理人不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
			else {

				staffId = "";
				doSmuit();
			}
			// param.put("nsStaffId", "");

			break;

		// case R.id.approve_action_iv:
		case R.id.approve_action_tv:
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("key", "nextStepDes");
			map.put("mark", "1");

			map.put("list", JsonUtil.toList(JsonUtil.optJSONArray(
					result_action, "body")));
			// iv_action.setImageResource(R.mipmap.up);
			pop_act = new PopWindowWithList(activity, map);
			pop_act.showAtLocation(v, Gravity.CENTER, 0, 0);
			pop_act.getListView().setTag("pop_act");
			pop_act.getListView().setOnItemClickListener(this);
			pop_act.setOnDismissListener(this);
			break;
		case R.id.approve_people_iv:

			try {

				iv_people.setImageResource(R.mipmap.up);
				// if (result_people.length() > 0) {

				map = new HashMap<String, Object>();
				map.put("mark", "" + 1);

				map.put("key", "staffName");
				map.put("list", JsonUtil.toList(JsonUtil.optJSONArray(
						result_people, "body")));
				pop_peo = new PopWindowWithList(activity, map);
				pop_peo.setOnDismissListener(this);
				pop_peo.getListView().setTag("pop_peo");
				pop_peo.getListView().setOnItemClickListener(this);
				et_people.removeTextChangedListener(OrderApproveActivity.this);
				// }
			} catch (Exception e) {
				// TODO: handle exception
				iv_people.setImageResource(R.mipmap.down);
			}
			break;

		case R.id.button1:

			if (et_people.getTag() != null) {
				if (pop_peo == null) {

					map = new HashMap<String, Object>();
					map = new HashMap<String, Object>();
					map.put("mark", "" + 1);

					map.put("key", "staffName");
					map.put("list", JsonUtil.toList(JsonUtil.optJSONArray(
							result_people, "body")));
					pop_peo = new PopWindowWithList(activity, map);
					pop_peo.setOnDismissListener(OrderApproveActivity.this);
					pop_peo.getListView().setTag("pop_peo");
					pop_peo.getListView().setOnItemClickListener(
							OrderApproveActivity.this);
				} else {
					pop_peo.showAtLocation(btn, Gravity.CENTER, 0, 0);
					pop_peo.setDate(JsonUtil.toList(JsonUtil.optJSONArray(
							result_people, "body")));
				}
				break;
			} else {
				pop_peo = null;
			}

			String str = et_people.getText().toString();
			// if (!TextUtils.isEmpty(str)) {
			//
			// // Pattern p = Pattern.compile("[\u4e00-\u9fa5]+");
			// // Matcher m = p.matcher(str);
			// // b = m.matches();
			// if (Pattern.compile("[\u4e00-\u9fa5]+").matcher(str).matches()) {

			param = new HashMap<String, String>();
			param.put("staffNames", str);

			param.put("clickCode", menuCode);
			param.put("menuCode", menuCode);
			param.put("appType", "BI_ANDROID");
			param.put("clickType", "MENU");
			new BaseLoadDate(activity, Constant.BASE_PATH, 3).execute(
					"/bi/approve/QueryByStaffName", param);
			// }
			// }
			break;
		default:
			break;
		}
	}

	void doSmuit() {

		param = new LinkedHashMap<String, String>();
		param.put("taskId", taskId);
		param.put("nextStepOps", et_action.getText().toString());
		String name = et_people.getText().toString();
		param.put("nsStaffId", staffId);
		param.put("comment", text.getText().toString().trim());
		param.put("nextStepOps", nextStepOps);

		param.put("clickCode", menuCode);
		param.put("menuCode", menuCode);
		param.put("appType", "BI_ANDROID");
		param.put("clickType", "MENU");

		new BaseLoadDate(activity, Constant.BASE_PATH, 1).execute(
				"/bi/approve/Audit", param);

		return;
	}

	public class BaseLoadDate extends HttpRequestTask {

		int request = -1;

		protected void bindData(JSONObject result, int requestCode) {
			if (!Utils.checkResult(result, activity)) {
				return;
			}
			switch (requestCode) {
			case 1:
				try {
					if (result.getJSONObject("head").getInt("retCode") == 1) {
						Toast.makeText(
								activity,
								result.getJSONObject("head")
										.getString("retMsg"), Toast.LENGTH_SHORT).show();
						activity.finish();
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				break;
			case 2:
				// if (false) {
				if (JsonUtil.toList(JsonUtil.optJSONArray(result, "body"))
						.size() > 0) {
					result_people = result;
					et_people.setEnabled(false);
					et_people.setTag("AAAAA");
					et_people.setText(JsonUtil
							.toList(JsonUtil.optJSONArray(result, "body"))
							.get(0).get("staffName"));
					et_people.setSelection(et_people.getText().length());

					staffId = (JsonUtil.toList(
							JsonUtil.optJSONArray(result, "body")).get(0)
							.get("opId"));
					// iv_people.setVisibility(View.VISIBLE);
					// iv_people.setOnClickListener(null);

					break;
				} else {
					et_people.setText(null);
					et_people.setEnabled(true);
					// iv_people.setVisibility(View.VISIBLE);
					// et_people.addTextChangedListener(OrderApproveActivity.this);
					search.setVisibility(View.VISIBLE);
					break;
				}
			case 3:

				if (JsonUtil.toList(JsonUtil.optJSONArray(result, "body"))
						.size() > 0) {

					result_people = result;

					JSONObject obj;
					try {
						obj = JsonUtil.optJSONArray(result, "body")
								.getJSONObject(0);
						Map<String, Object> map = new HashMap<String, Object>();
						map = new HashMap<String, Object>();
						map.put("mark", "" + 1);

						map.put("key", "staffName");
						map.put("list", JsonUtil.toList(JsonUtil.optJSONArray(
								result, "body")));

						// pop_peo.setDate(JsonUtil.toList(JsonUtil.optJSONArray(
						// result, "body")));
						if (pop_peo == null) {

							pop_peo = new PopWindowWithList(activity, map);
							pop_peo.setOnDismissListener(OrderApproveActivity.this);
							pop_peo.getListView().setTag("pop_peo");
							pop_peo.getListView().setOnItemClickListener(
									OrderApproveActivity.this);
						} else {
							pop_peo.showAtLocation(btn, Gravity.CENTER, 0, 0);
							pop_peo.setDate(JsonUtil.toList(JsonUtil
									.optJSONArray(result, "body")));
						}

						et_people
								.removeTextChangedListener(OrderApproveActivity.this);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;

			default:
				break;
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		// Toast.makeText(activity, "AAAAAAAAA:" + parent.getTag(), 1).show();
		// parent.get
		if (parent.getTag().equals("pop_act")) {

			changeView_act(position);

		} else {

			// Toast.makeText(activity, "AAAAAAAAA", 1).show();
			// changeView_act(position);
			@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>) parent
					.getItemAtPosition(position);
			et_people.setText(map.get("staffName"));
			// et_people.setText((JsonUtil.toList(
			// JsonUtil.optJSONArray(result_people, "body")).get(position)
			// .get("staffName")));
			et_people.setSelection(et_people.getText().length());

			staffId = (JsonUtil.toList(
					JsonUtil.optJSONArray(result_people, "body")).get(position)
					.get("opId"));

			// doSmuit();

		}
		if (pop_act != null)
			pop_act.dismiss();
		if (pop_peo != null) {
			pop_peo.dismiss();

		}
	}

	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub
		// iv_action.setBackground(activity.getResources().getDrawable(R.mipmap.down));
		// iv_action.setImageResource(R.mipmap.down);
		iv_people.setImageResource(R.mipmap.down);
		et_people.setTag(null);

		if (iv_people.getVisibility() != View.VISIBLE) {
			// et_people.
			// et_people.addTextChangedListener(this);
		}
		// else {
		// et_people.removeTextChangedListener(this);
		// }

		// Toast.makeText(activity, "AAAAAA", 1).show();
		// (activity.getResources().getDrawable(R.mipmap.down));

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

		String str = s.toString();
		// boolean b = false;
		if (!TextUtils.isEmpty(str)) {

			// Pattern p = Pattern.compile("[\u4e00-\u9fa5]+");
			// Matcher m = p.matcher(str);
			// b = m.matches();
			if (Pattern.compile("[\u4e00-\u9fa5]+").matcher(str).matches()) {

				param = new HashMap<String, String>();
				param.put("staffNames", s.toString());

				param.put("clickCode", menuCode);
				param.put("menuCode", menuCode);
				param.put("appType", "BI_ANDROID");
				param.put("clickType", "MENU");
				new BaseLoadDate(activity, Constant.BASE_PATH, 3).execute(
						"/bi/approve/QueryByStaffName", param);
			}
		}
	}
}
