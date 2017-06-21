/**
 * PortalActivity2
 */
package com.bonc.mobile.hbmclient.activity;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.Publicapp;
import com.bonc.mobile.hbmclient.data.AnnouncementTool;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import common.share.lwg.util.mediator.proxy_impl.port.PortMediator;

/**
 * @author liweigao
 * 
 */
public class PortalActivity extends BaseActivity {
	private PortMediator portMediator;
	public final static String ACTION_SHOW_BOARD = "action_show_board";
	public final static String ACTION_SHOW_INITIAL_PASSWORD = "action_show_initial_password";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.activity.BaseActivity#onCreate(android.os.Bundle
	 * )
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_first_layout);
		findViewById(R.id.id_menu_first).setBackgroundDrawable(
				WatermarkImage.getWatermarkDrawable());

		TextView home_tv_date = (TextView) findViewById(R.id.home_tv_date);
		home_tv_date.setText(DateUtil.formatter(Calendar.getInstance()
				.getTime(), "yyyy年MM月dd日, EEEE"));

		Button searchButton = (Button) findViewById(R.id.search_button);
		searchButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PortalActivity.this,
						KPISearchActivity.class);
				startActivity(intent);
			}
		});

		// 很重要
		this.portMediator = new PortMediator();
		// 初始化page及groupcollege并添加IMediator监听
		this.portMediator.createColleagues(this);
		this.portMediator.create();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
		String action = intent.getAction();
		if (ACTION_SHOW_BOARD.equals(action)) {
			this.portMediator.actionShowBoard();
		} else if (ACTION_SHOW_INITIAL_PASSWORD.equals(action)) {
			this.portMediator.actionPassword();
		}
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		String pushFlag = Publicapp.getNotifyFlag();
		AnnouncementTool anTool = new AnnouncementTool();
		List<Map<String, String>> data = anTool.queryUnreadMessage();
		if (data.size() > 0) {
			if (pushFlag.equals((getResources()
					.getString(R.string.notify_close)))) {
				Toast.makeText(PortalActivity.this,
						"您有" + data.size() + "条未读公告", Toast.LENGTH_SHORT)
						.show();
			} else {
				this.portMediator.showBoard(false);
			}

		}
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder bu = new AlertDialog.Builder(PortalActivity.this);
		bu.setTitle("确定要退出吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
						Process.killProcess(Process.myPid());
					}
				}).setNegativeButton("取消", null).create().show();

	}

}
