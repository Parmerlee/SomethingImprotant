package com.bonc.mobile.portal.search;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.anhuimobile.ac.R;
import com.bonc.mobile.common.activity.BaseActivity;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.common.util.UIUtil;

public class PreferActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener {

	private Activity activity;
	private ListView mListView;
	private List<String> mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prefer);
		activity = this;
		UIUtil.setWatermarkImage(activity, android.R.id.content);

		initView();
		initDate();
		drawUI();
	}

	private void initView() {
		// TODO Auto-generated method stub
		TextViewUtils.setText(activity, R.id.title, "搜索简介");
		mListView = (ListView) this.findViewById(R.id.prefer_listView);
		mListView.setOnItemClickListener(this);

		TextView more = (TextView) this.findViewById(R.id.title_end);
		more.setVisibility(View.VISIBLE);
		more.setText("更多");
	}

	private void initDate() {
		mList = new ArrayList<String>();
		for (int i = 0; i < 15; i++) {
			mList.add("测试数据测试诗句及阿三哒哒哒水水打啊杀的大神的：" + i);
		}
	}

	private void drawUI() {

		MyPerferAdapter adapter = new MyPerferAdapter(activity, mList);
		mListView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Toast.makeText(activity,
				"equal:" + (parent.getId() == R.id.prefer_listView), 1).show();
		switch (parent.getId()) {
		case R.id.prefer_listView:
			// if (position == mListView.getCount() - 1) {
			// System.out.println("AAAAA");
			// }
			Toast.makeText(activity, "AAAAA", 1).show();
			startActivity(new Intent(activity, SearchDetailActivity.class));
			break;

		default:
			break;
		}
	}

}
