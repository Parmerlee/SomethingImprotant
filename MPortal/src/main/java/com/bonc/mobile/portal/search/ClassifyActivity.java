package com.bonc.mobile.portal.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.bonc.anhuimobile.ac.R;
import com.bonc.mobile.common.activity.BaseActivity;
import com.bonc.mobile.common.util.UIUtil;

public class ClassifyActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener {

	private Activity activity;
	private ListView mListView;
	private GridView mGridView;
	private List<String> mDate;
	private MyAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classify);
		activity = this;
		initView();
		initDate();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.search_tv_search:

			Toast.makeText(activity, "AAA", 1).show();
			break;

		default:
			break;
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		UIUtil.setWatermarkImage(activity, android.R.id.content);
		mListView = (ListView) this.findViewById(R.id.listView1);
		mGridView = (GridView) this.findViewById(R.id.gridView1);

		this.findViewById(R.id.search_tv_search).setOnClickListener(this);
		mListView.setOnItemClickListener(this);
		mGridView.setOnItemClickListener(this);
	}

	private void initDate() {
		// TODO Auto-generated method stub
		mDate = new ArrayList<String>();
		for (int i = 0; i < 15; i++) {
			mDate.add("分类:" + i);
			// Map<String, Object> temp = new HashMap<String, Object>();
			// temp.put("test", "test:" + i);
			// mData_first.add(temp);
		}
		mAdapter = new MyAdapter(activity, mDate);
		mListView.setAdapter(mAdapter);

		mGridView.setAdapter(mAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
		case R.id.listView1:
			mDate.add("AAAAAAAAAAAAAAAA");
			mAdapter.notifyDataSetChanged();
			break;

		case R.id.gridView1:

			break;

		default:
			break;
		}

	}

}
