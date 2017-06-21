/**
 * TODO
 */
package com.bonc.mobile.hbmclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;

import java.util.List;
import java.util.Map;

/**
 * @author liweigao
 * 
 */
public class Top10ViewGroupSpring extends LinearLayout {
	public static final int CHILD_NUM = 10;
	private LayoutInflater mLayoutInflater;
	private TextView[] titleTV = new TextView[CHILD_NUM];
	private TextView[] nameTV = new TextView[CHILD_NUM];
	private TextView[] rankTV = new TextView[CHILD_NUM];
	private TextView[] key1TV = new TextView[CHILD_NUM];
	private TextView[] value1TV = new TextView[CHILD_NUM];
	private TextView[] key2TV = new TextView[CHILD_NUM];
	private TextView[] value2TV = new TextView[CHILD_NUM];

	private String[] title = new String[CHILD_NUM];
	private String[] name = new String[CHILD_NUM];
	private String[] rank = new String[CHILD_NUM];
	private String[] key1 = new String[CHILD_NUM];
	private String[] value1 = new String[CHILD_NUM];
	private String[] key2 = new String[CHILD_NUM];
	private String[] value2 = new String[CHILD_NUM];

	private int itemWidth;

	/**
	 * @param context
	 * @param attrs
	 */
	public Top10ViewGroupSpring(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public Top10ViewGroupSpring(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mLayoutInflater = LayoutInflater.from(context);
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		if (width == 720) {
			itemWidth = (width - 4 * 8) / 3;
		} else {
			itemWidth = (width - 4 * 8) / 3 + 1;
		}
	}

	public void dispatchView() {
		for (int i = 0; i < CHILD_NUM; i++) {
			View view = this.mLayoutInflater.inflate(
					R.layout.top10_item_layout, null);
			/*
			 * if(i == 9) { itemWidth = itemWidth + 10; }
			 */
			LayoutParams lp = new LayoutParams(itemWidth,
					LayoutParams.WRAP_CONTENT);
			if (i > 0) {
				lp.setMargins(5, 0, 0, 0);
			}
			view.setLayoutParams(lp);
			this.addView(view);

			this.titleTV[i] = (TextView) view.findViewById(R.id.id_title);
			this.nameTV[i] = (TextView) view.findViewById(R.id.id_name);
			this.rankTV[i] = (TextView) view.findViewById(R.id.id_rank);
			this.key1TV[i] = (TextView) view.findViewById(R.id.id_key1);
			this.value1TV[i] = (TextView) view.findViewById(R.id.id_value1);
			this.key2TV[i] = (TextView) view.findViewById(R.id.id_key2);
			this.value2TV[i] = (TextView) view.findViewById(R.id.id_value2);
		}
	}

	public void setData(List<Map<String, String>> data) {
		if (data == null || data.size() == 0) {
			return;
		}

		for (int i = 0; i < CHILD_NUM && i < data.size(); i++) {
			Map<String, String> tempMap = data.get(i);
			this.title[i] = tempMap.get("top10");
			this.name[i] = tempMap.get("modelName");
			this.rank[i] = "" + (i + 1);
			this.key1[i] = tempMap.get("typeName");
			this.value1[i] = tempMap.get("typeValue");
			this.key2[i] = tempMap.get("zbName");
			this.value2[i] = tempMap.get("zbValue");
		}
	}

	public void updateView() {
		for (int i = 0; i < CHILD_NUM; i++) {
			this.titleTV[i].setText(this.title[i]);
			this.nameTV[i].setTextSize(11);
			this.nameTV[i].setText(this.name[i]);
			this.rankTV[i].setText(this.rank[i]);
			this.key1TV[i].setText(this.key1[i]);
			this.value1TV[i].setText(this.value1[i]);
			this.key2TV[i].setText(this.key2[i]);
			this.value2TV[i].setText(this.value2[i]);
		}
	}

}
