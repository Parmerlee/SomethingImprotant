package com.bonc.mobile.saleclient.common;

import java.util.Map;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;

public class PopWindow extends PopupWindow {

	private View view;
	private TextView popTitle;
	private TextView popContent, popMarket, popType;

	private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7;

	int mark = -1;

	int click_pos_heigt;
	int[] location = new int[2];

	Context context;

	public PopWindow(Context context, Map<String, String> map) {

		this.context = context;
		if (map.containsKey("mark")) {
			mark = Integer.valueOf(map.get("mark"));
			view = LayoutInflater.from(context).inflate(
					R.layout.pop_windows_layout, null);

			initScreen1(map);
			// this.setHeight(300);
			// this.setWidth(LayoutParams.MATCH_PARENT);
		} else {
			view = LayoutInflater.from(context).inflate(
					R.layout.pop_windows_layout2, null);
			initScreen2(map);

		}

		this.setContentView(view);

		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);

		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.update();
		ColorDrawable dw = new ColorDrawable(0000000000);
		this.setBackgroundDrawable(dw);
	}

	public int getPopHeight() {

		ViewTreeObserver vto = view.getViewTreeObserver();
		vto.addOnPreDrawListener(new OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {
				// TODO Auto-generated method stub

				return true;
			}
		});
		view.measure(0, 0);
		return view.getMeasuredHeight();
	}

	public void setPositionHeight(int height) {
		click_pos_heigt = height;
	}

	public void setLocationOnSc(int[] location) {
		this.location = location;
		Log.d("AAA", "XXXXX:" + location[0]);
	}

	public void showPop(View anchor, int xoff, int yoff) {

		// this.setBackgroundDrawable(new ColorDrawable(0000000000));

		if (this.isShowing())
			this.dismiss();
		else {

			Log.d("AAAAAA", "click_pos_heigt:" + click_pos_heigt);
			// Log.d("AAAAAA",
			// "Utils.getSystemHeight(context) / 2:"
			// + Utils.getSystemHeight(context) / 2);

			Log.d("AAAA", "X  location:" + location[0]);

			if (click_pos_heigt < Utils.getSystemHeight(context) / 2) {
				// this.showAsDropDown(anchor, xoff, yoff);

				if (location[0] <= Utils.getSystemWidth(context) / 3) {
					// this.setBackgroundDrawable(context.getResources()
					// .getDrawable(R.mipmap.popup_1));

					this.setBackgroundDrawable(context.getResources()
							.getDrawable(R.mipmap.popup_1));

				} else if ((location[0] > Utils.getSystemWidth(context) / 3)
						&& (location[0] <= 2 * Utils.getSystemWidth(context) / 3)) {
					this.setBackgroundDrawable(context.getResources()
							.getDrawable(R.mipmap.popup_2));
				} else if ((location[0] <= Utils.getSystemWidth(context))
						&& (location[0] > 2 * Utils.getSystemWidth(context) / 3)) {
					this.setBackgroundDrawable(context.getResources()
							.getDrawable(R.mipmap.popup_3));
				}
				this.update();
				this.showAsDropDown(anchor, xoff, yoff / 4);
			} else {
				if (location[0] <= Utils.getSystemWidth(context) / 3) {
					this.setBackgroundDrawable(context.getResources()
							.getDrawable(R.mipmap.popup_4));

				} else if ((location[0] > Utils.getSystemWidth(context) / 3)
						&& (location[0] <= 2 * Utils.getSystemWidth(context) / 3)) {
					this.setBackgroundDrawable(context.getResources()
							.getDrawable(R.mipmap.popup_5));
					// this.setHeight(200);
				} else if ((location[0] <= Utils.getSystemWidth(context))
						&& (location[0] > 2 * Utils.getSystemWidth(context) / 3)) {
					// this.set
					// this.setHeight(200);
					this.setBackgroundDrawable(context.getResources()
							.getDrawable(R.mipmap.popup_6));
				}
				this.update();
				anchor.measure(0, 0);
				Log.d("AAAAAA", "anchor1:" + anchor.getHeight());
				Log.d("AAAAAA",
						"anchor2:" + (getPopHeight() + anchor.getHeight()));
				// this.showAsDropDown(anchor, 0,
				// -(getPopHeight() + (int) (anchor.getHeight() * 2.5)));

				this.showAsDropDown(anchor, 0,
						-(getPopHeight() + (int) (anchor.getHeight() * 2.4)));
			}
		}
	}

	private void initScreen2(Map<String, String> map) {
		tv1 = (TextView) view.findViewById(R.id.pop_layout2_tv1);
		tv2 = (TextView) view.findViewById(R.id.pop_layout2_tv2);
		tv3 = (TextView) view.findViewById(R.id.pop_layout2_tv3);
		tv4 = (TextView) view.findViewById(R.id.pop_layout2_tv4);
		tv5 = (TextView) view.findViewById(R.id.pop_layout2_tv5);
		tv6 = (TextView) view.findViewById(R.id.pop_layout2_tv6);
		tv7 = (TextView) view.findViewById(R.id.pop_layout2_tv7);
		tv1.setText(map.get("currentStage"));
		tv2.setText(map.get("corporation"));
		tv3.setText(map.get("orgName"));
		tv4.setText(map.get("receiveStaff"));
		tv5.setText(map.get("dealStaff"));
		tv6.setText(map.get("nextStap"));
		tv7.setText(map.get("description"));
	}

	private void initScreen1(Map<String, String> map) {
		popTitle = (TextView) view.findViewById(R.id.pop_title);
		popContent = (TextView) view.findViewById(R.id.pop_content);
		popMarket = (TextView) view.findViewById(R.id.pop_market_detail);
		popType = (TextView) view.findViewById(R.id.pop_type_content);

		popTitle.setText(map.get("title"));
		popContent.setText(map.get("content"));
		popMarket.setText(map.get("market"));
		popType.setText(map.get("type"));

		TextView title_type = (TextView) view.findViewById(R.id.pop_type_title);
		TextView title_content = (TextView) view
				.findViewById(R.id.pop_content_title);
		TextView title_mark = (TextView) view
				.findViewById(R.id.pop_market_title);
		switch (mark) {
		case 3:
			title_type.setText("资费类型");
			title_mark.setText("档次描述");
			title_content.setText("营销案主要信息");

			break;
		case 2:
			title_type.setText("资费类型");
			title_mark.setText("细分市场");
			title_content.setText("资费案主要信息");
			break;

		case 4:
			title_type.setText("申请人部门");
			title_mark.setText("变更内容");
			title_content.setText("业务变更主要信息");
			break;

		default:
			break;
		}
	}
}
