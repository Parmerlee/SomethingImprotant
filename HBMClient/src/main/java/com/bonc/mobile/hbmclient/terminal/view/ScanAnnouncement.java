package com.bonc.mobile.hbmclient.terminal.view;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.AnnouncementTool;
import com.bonc.mobile.hbmclient.state.view_switcher.AnnouncementSwitcher;

public class ScanAnnouncement {
	private Activity a;

	public ScanAnnouncement(Activity a) {
		this.a = a;
	}

	public void showBulletinBoard(boolean showToast) {
		// 公告查看
		AnnouncementTool anTool = new AnnouncementTool();
		List<Map<String, String>> data = null;
		if (showToast) {
			data = anTool.queryMessage();
		} else {
			data = anTool.queryUnreadMessage();
		}
		if (data.size() > 0) {
			View contentView = LayoutInflater.from(a).inflate(
					R.layout.announcement, null);
			final AnnouncementSwitcher as = (AnnouncementSwitcher) contentView
					.findViewById(R.id.id_announcementswitcher);
			ImageView close = (ImageView) contentView
					.findViewById(R.id.closebutton);
			int screenWidth = a.getResources().getDisplayMetrics().widthPixels;
			int screenHeight = a.getResources().getDisplayMetrics().heightPixels;

			int h = (screenHeight * 57 / 100) * 41 / 50;
			as.setHeight(h);
			as.setAnnouncement(anTool);
			as.setShowToast(showToast);
			as.setData(data);
			// 弹出框

			final PopupWindow popupWindow = new PopupWindow(contentView,
					screenWidth * 95 / 100, screenHeight * 57 / 100);

			popupWindow.setTouchable(true);
			popupWindow.setFocusable(true);
			// 允许popupWindow可以使用back键的笨方法
			popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000)
					.getCurrent());
			popupWindow.showAtLocation(a.findViewById(R.id.id_menu_first),
					Gravity.CENTER_HORIZONTAL, 0, 60);

			close.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					popupWindow.dismiss();
				}
			});
		} else {
			Toast.makeText(a, R.string.noneannouncement, Toast.LENGTH_SHORT)
					.show();
		}
	}
}
