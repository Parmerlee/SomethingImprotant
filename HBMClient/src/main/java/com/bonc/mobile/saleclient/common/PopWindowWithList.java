package com.bonc.mobile.saleclient.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;

public class PopWindowWithList extends PopupWindow {

	private View view;
	private TextView popTitle;
	private ListView listview;

	int mark = -1;

	private Activity activity;

	private SimpleAdapter adapter;

	List<? extends Map<String, String>> list = new ArrayList<Map<String, String>>();

	public PopWindowWithList(Activity activity, Map<String, Object> map) {

		this.activity = activity;
		if (map.containsKey("mark")) {
			mark = Integer.valueOf((String) map.get("mark"));
			view = LayoutInflater.from(activity)
					.inflate(R.layout.poplist, null);
			initScreen1(map);
		} else {
			view = LayoutInflater.from(activity).inflate(
					R.layout.pop_windows_layout2, null);
		}

		this.setContentView(view);

		this.setWidth(Utils.getSystemWidth(activity) / 3 * 2);
		// this.setHeight(Utils.getSystemWidth(activity));

		// this.setWidth(LayoutParams.WRAP_CONTENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);

		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.update();
		ColorDrawable dw = new ColorDrawable(00000000);
		this.setBackgroundDrawable(new BitmapDrawable());
		this.showAtLocation(view, Gravity.CENTER, 0, 0);
	}

	@SuppressWarnings("unchecked")
	private void initScreen1(Map<String, Object> map) {
		popTitle = (TextView) view.findViewById(R.id.poptilte);
		listview = (ListView) view.findViewById(R.id.poplistView);
		// popTitle.setText((String) map.get("title"));
		// listview.setAdapter(new SimpleAdapter(activity,
		// (List<? extends Map<String, String>>) map.get("list"),
		// android.R.layout.simple_list_item_1,
		// new String[] { (String) map.get("key") },
		// new int[] { android.R.id.text1 }));

		list = (List<? extends Map<String, String>>) map.get("list");

		adapter = new SimpleAdapter(activity,
				(List<? extends Map<String, String>>) map.get("list"),
				R.layout.pop_list_item,
				new String[] { (String) map.get("key") },
				new int[] { R.id.textView1 });
		listview.setAdapter(adapter);
		// listview.setOnItemClickListener(this);

	}

	public ListView getListView() {
		return listview;
	}

	@SuppressWarnings("unchecked")
	public void setDate(List list2) {
		list.clear();
		list.addAll(list2);
		adapter.notifyDataSetInvalidated();
		adapter.notifyDataSetChanged();
		this.showAtLocation(view, Gravity.CENTER, 0, 0);

	}
}
