package com.bonc.mobile.hbmclient.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bonc.mobile.common.Constant;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.AnnounceSlideHolderActivity.LoadDataTask2;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AnnounceEditActivity extends AnnounceBaseActivity implements
		OnClickListener {

	private EditText anno_title, anno_contnet;

	private TextView title;

	private Activity activity;

	private Map<String, String> param;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_announce_edit);

		initView();
		initDate();
	}

	private void initDate() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();

		activity = this;
		anno_contnet = (EditText) this.findViewById(R.id.announce_content);
		anno_title = (EditText) this.findViewById(R.id.announce_title2);
		title = (TextView) this.findViewById(R.id.announce_title);
		this.findViewById(R.id.announce_logo).setOnClickListener(this);

		this.findViewById(R.id.announce_publish).setOnClickListener(this);

		this.findViewById(R.id.announce_deletle).setOnClickListener(this);

		this.findViewById(R.id.announce_cancel).setOnClickListener(this);
	}

	@Override
	protected void bindData(JSONObject result, int requestCode) {
		// super.bindData(result,requestCode);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.announce_cancel:
		case R.id.announce_logo:
			onBackPressed();
			break;

		case R.id.announce_deletle:
			// onBackPressed();
			break;

		case R.id.announce_publish:
			// onBackPressed();

			checkAndPublish();

			break;

		default:
			break;
		}
	}

	private void checkAndPublish() {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(anno_title.getText().toString().trim())) {
			Toast.makeText(activity, "公告标题不能为空", 1).show();
			return;
		}

		if (TextUtils.equals(date_button.getText().toString().trim(),
				"----/--/--")) {
			Toast.makeText(activity, "公告失效时间不能为空", 1).show();
			return;
		}
		Log.d("AAAA", "date_button.getDate():" + (date_button.getDate()));
		// if(date_button.getDate())
		if (TextUtils.isEmpty(anno_contnet.getText().toString().trim())) {
			Toast.makeText(activity, "公告内容不能为空", 1).show();
			return;
		}

		param = getDefaultMap();
		param.put("expTime", date_button.getText().toString().replace("/", "-"));
		param.put("msg", anno_contnet.getText().toString().trim());
		param.put("title", anno_title.getText().toString().trim());
		param.put("status", "1");
		// param.put(key, value);
		new LoadDataTask2(this, Constant.PORTAL_PATH, 1).execute(
				"/bi/notice/addNotice", param);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	private Map<String, String> getDefaultMap() {

		param = new HashMap<String, String>();
		param.put("clickCode", menuCode);
		param.put("menuCode", menuCode);
		param.put("appType", "BI");
		param.put("clickType", "MENU");

		return param;

	}
}
