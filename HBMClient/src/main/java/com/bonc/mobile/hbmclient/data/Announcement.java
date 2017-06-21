package com.bonc.mobile.hbmclient.data;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.util.DateUtil;

/***
 * 显示公告牌信息类
 * 
 * @author Administrator
 *
 */
public class Announcement implements OnClickListener {

	private Context context;
	private PopupWindow popupWindow;
	private ImageView frontitem;
	private ImageView nextitem;
	private List<Map<String, String>> resultlist;
	private TextView textviewdate;
	private TextView textviewcontent;
	private AnnouncementTool toolannouncement;
	private int i = 0;
	private Integer MessageId;
	private TextView view2;
	private ImageView image_count;
	// 用于标示手机屏幕宽高
	public static int screenWidth;
	public static int screenHeight;

	public Announcement(Context context) {
		this.context = context;
	}

	public void getAnnouncement(View view1, TextView view2,
			ImageView image_count) {
		this.view2 = view2;
		this.image_count = image_count;
		getAnnouncement(view1);
		setCount(view2, image_count);
	}

	public void getAnnouncement(View view) {
		// 公告的statue标志为0 表示未读，为 1 表示已读
		View contentView = LayoutInflater.from(context).inflate(
				R.layout.announcement, null);
		toolannouncement = new AnnouncementTool();
		resultlist = toolannouncement.queryMessage();
		frontitem = (ImageView) contentView.findViewById(R.id.frontitem);
		frontitem.setOnClickListener(this);
		nextitem = (ImageView) contentView.findViewById(R.id.nextitem);
		nextitem.setOnClickListener(this);
		ImageView imageViewbutton = (ImageView) contentView
				.findViewById(R.id.closebutton);
		textviewdate = (TextView) contentView.findViewById(R.id.textviewdate);
		textviewcontent = (TextView) contentView
				.findViewById(R.id.textviewcontent);
		if (resultlist.size() != 0) {
			toastNotReadAnnouncement();
			if (resultlist.size() == 1) {
				String dateString = resultlist.get(0).get("Create_date")
						.toString();
				// dateString = dateString.substring(0,
				// 4)+"年"+dateString.substring(4,
				// 6)+"月"+dateString.substring(6)+"日";
				dateString = DateUtil.oneStringToAntherString(dateString,
						DateUtil.PATTERN_8, "yyyy年MM月dd日");
				String statusString = resultlist.get(0).get("MessageStatus")
						.toString();
				if ("0".equals(statusString)) {
					dateString = dateString + "(未读)";
				}
				textviewdate.setText(dateString);
				textviewcontent.setText(resultlist.get(0).get("Message")
						.toString());
				MessageId = Integer.parseInt(resultlist.get(0).get("MessageId")
						.toString());
				toolannouncement.updateDB(MessageId);
				frontitem.setImageResource(R.mipmap.img_icon_frontitemgray);
				nextitem.setImageResource(R.mipmap.img_icon_nexttitem1);
			} else {
				String dateString = resultlist.get(0).get("Create_date")
						.toString();
				dateString = DateUtil.oneStringToAntherString(dateString,
						DateUtil.PATTERN_8, "yyyy年MM月dd日");
				String statusString = resultlist.get(0).get("MessageStatus")
						.toString();
				if ("0".equals(statusString)) {
					dateString = dateString + "(未读)";
				}
				frontitem.setImageResource(R.mipmap.img_icon_frontitemgray1);
				textviewdate.setText(dateString);
				textviewcontent.setText(resultlist.get(0).get("Message")
						.toString());
				MessageId = Integer.parseInt(resultlist.get(0).get("MessageId")
						.toString());
				toolannouncement.updateDB(MessageId);
				frontitem.setImageResource(R.mipmap.img_icon_frontitemgray);
			}
			imageViewbutton.setOnClickListener(this);

			screenWidth = context.getResources().getDisplayMetrics().widthPixels;
			screenHeight = context.getResources().getDisplayMetrics().heightPixels;

			popupWindow = new PopupWindow(contentView, screenWidth * 95 / 100,
					screenHeight * 57 / 100);

			popupWindow.setTouchable(true);
			popupWindow.setFocusable(true);
			// 允许popupWindow可以使用back键的笨方法
			popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000)
					.getCurrent());
			popupWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 60);
		} else {
			Toast.makeText(context, R.string.noneannouncement,
					Toast.LENGTH_SHORT).show();
			frontitem.setImageResource(R.mipmap.img_icon_frontitemgray);
			nextitem.setImageResource(R.mipmap.img_icon_nexttitem1);
		}

	}

	private void toastNotReadAnnouncement() {
		String status = null;
		int num_notRead = 0;
		for (int i = 0; i < resultlist.size(); i++) {
			status = resultlist.get(i).get("MessageStatus");
			if ("0".equalsIgnoreCase(status)) {
				num_notRead++;
			}
		}
		if (num_notRead > 0) {
			Toast.makeText(context, "您有" + num_notRead + "条未读公告",
					Toast.LENGTH_SHORT).show();
			num_notRead = 0;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.closebutton:
			popupWindow.dismiss();
			break;

		case R.id.frontitem:
			if (resultlist.size() != 0) {
				if (resultlist.size() == 1) {
					nextitem.setImageResource(R.mipmap.img_icon_nexttitem1);
				} else if (i == resultlist.size() - 1) {
					nextitem.setImageResource(R.mipmap.img_icon_nexttitem);
				}
				if (i > 0) {
					i--;

					MessageId = Integer.parseInt(resultlist.get(i)
							.get("MessageId").toString());

					toolannouncement.updateDB(MessageId);
					if (view2 != null) {
						setCount(view2, image_count);
					}
					String dateString = resultlist.get(i).get("Create_date")
							.toString();
					dateString = DateUtil.oneStringToAntherString(dateString,
							DateUtil.PATTERN_8, "yyyy年MM月dd日");
					String statusString = resultlist.get(i)
							.get("MessageStatus").toString();
					if ("0".equals(statusString)) {
						dateString = dateString + "(未读)";
					}

					textviewdate.setText(dateString);
					textviewcontent.setText(resultlist.get(i).get("Message")
							.toString());
					if (i == 0) {
						frontitem
								.setImageResource(R.mipmap.img_icon_frontitemgray);
					}

				}
			}
			break;
		case R.id.nextitem:
			if (resultlist.size() != 0) {
				if (resultlist.size() == 1) {
					frontitem
							.setImageResource(R.mipmap.img_icon_frontitemgray);
				} else if (i == 0) {
					frontitem
							.setImageResource(R.mipmap.img_icon_frontitemgray1);
				}
				if (i % 5 == 0) {
					resultlist.addAll(toolannouncement.queryReadMessage(i + 5,
							5));
				}
				if (i < resultlist.size() - 1) {
					i++;
					String dateString = resultlist.get(i).get("Create_date")
							.toString();
					dateString = DateUtil.oneStringToAntherString(dateString,
							DateUtil.PATTERN_8, "yyyy年MM月dd日");
					String statusString = resultlist.get(i)
							.get("MessageStatus").toString();
					if ("0".equals(statusString)) {
						dateString = dateString + "(未读)";
					}

					textviewdate.setText(dateString);
					MessageId = Integer.parseInt(resultlist.get(i)
							.get("MessageId").toString());
					toolannouncement.updateDB(MessageId);
					if (view2 != null) {
						setCount(view2, image_count);
					}
					frontitem
							.setImageResource(R.mipmap.img_icon_frontitemgray1);
					textviewcontent.setText(resultlist.get(i).get("Message")
							.toString());
					if (i == resultlist.size() - 1) {
						nextitem.setImageResource(R.mipmap.img_icon_nexttitem1);
					}

				}
			}
			break;
		}

	}

	public void setCount(TextView view, ImageView image_count) {
		toolannouncement = new AnnouncementTool();
		int count = 0;
		count = toolannouncement.querycountMessage();
		view.setText(count + "");
		int oldcount = Integer.parseInt(view.getText() + "");
		if (oldcount == 0) {
			image_count.setVisibility(View.INVISIBLE);
			view.setVisibility(View.INVISIBLE);
		}
	}

}
