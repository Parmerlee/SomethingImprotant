package com.bonc.mobile.remoteview.activity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bonc.mobile.common.activity.AppInfoActivity;
import com.bonc.mobile.common.activity.BaseActivity;
import com.bonc.mobile.common.activity.UserInfoActivity;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.common.ConfigLoader;
import com.bonc.mobile.remoteview.common.RemoteUtil;

public class SystemSettingActivity extends BaseActivity {

	private static Map<String, Class<?>> activitys = new HashMap<String, Class<?>>();
	private static String UserInfo = "UserInfo";
	private static String AppInfo = "AppInfo";
	private static String MsgPush = "MsgPush";
	SharedPreferences pref;
	boolean msg_push;

	static {
		activitys.put(UserInfo, UserInfoActivity.class);
		activitys.put(AppInfo, AppInfoActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_ststem_setting);
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		msg_push = pref.getBoolean("msg_push", true);
		this.renderStaticData();
		this.renderMenuData();
		RemoteUtil.getInstance().addActivity(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (MyUtils.doInfilter(this)) {

			if (!MyUtils.isBackground(this)) {
				MyUtils.startProtal(this);
				RemoteUtil.getInstance().callLogin();
			}
		}
	}

	private void renderStaticData() {
		// 栏目标题
		TextView view = (TextView) this.findViewById(R.id.title);
		view.setText("系统设置");
		// 返回按钮事件
		Button btn = (Button) this.findViewById(R.id.id_button_back);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	private void renderMenuData() {
		List<Map<String, String>> menuList = new LinkedList<Map<String, String>>();
		menuList.add(this.getItemData("个人信息", "10090", UserInfo));
		menuList.add(this.getItemData("版本信息", "10091", AppInfo));
		menuList.add(this.getItemData("消息推送", "10043", MsgPush));

		String[] from = new String[] { BaseConfigLoader.KEY_MENU_NAME,
				BaseConfigLoader.KEY_MENU_ICON };
		int[] to = new int[] { R.id.text, R.id.icon };
		ListView listView = (ListView) this.findViewById(R.id.menuList);
		final SimpleAdapter adapter = new SysSettingAdapter(this, menuList,
				R.layout.setting_list_item, from, to);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Map<String, String> menuItem = (Map<String, String>) adapter
						.getItem(position);
				String menuCode = menuItem.get(BaseConfigLoader.KEY_MENU_CODE);
				switchSysMenu(menuCode);
			}
		});
	}

	private void switchSysMenu(String menuCode) {
		Class<?> clazz = activitys.get(menuCode);
		if (clazz == null) {
			return;
		}
		Intent intent = new Intent(this, clazz);
		this.startActivity(intent);
	}

	private Map<String, String> getItemData(String menuName, String iconImg,
			String activity) {
		Map<String, String> menuItem = new HashMap<String, String>();
		menuItem.put(BaseConfigLoader.KEY_MENU_NAME, menuName);
		menuItem.put(BaseConfigLoader.KEY_MENU_ICON, iconImg);
		menuItem.put(BaseConfigLoader.KEY_MENU_CODE, activity);
		return menuItem;
	}

	class SysSettingAdapter extends SimpleAdapter {

		private ConfigLoader instance = ConfigLoader
				.getInstance(SystemSettingActivity.this);

		public SysSettingAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = super.getView(position, convertView, parent);
			ToggleButton checked = (ToggleButton) v.findViewById(R.id.checked);
			checked.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
			checked.setChecked(msg_push);
			checked.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					msg_push = !msg_push;
					showToast("消息推送已" + (msg_push ? "开启" : "关闭"));
					Editor editor = pref.edit();
					editor.putBoolean("msg_push", msg_push);
					editor.commit();
					renderMenuData();
				}
			});
			return v;
		}

		@Override
		public void setViewImage(ImageView v, String value) {
			v.setImageResource(instance.getMenuIcon(value));
		}

	}

}
