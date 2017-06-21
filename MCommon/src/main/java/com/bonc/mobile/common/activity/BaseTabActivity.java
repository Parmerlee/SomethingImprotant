package com.bonc.mobile.common.activity;

import java.util.List;
import java.util.Map;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.util.FileUtils;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.common.util.UIUtil;

/**
 * @author sunwei
 */
public abstract class BaseTabActivity extends TabActivity  implements OnTabChangeListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UIUtil.disableScrShot(this);
	}

	public void onClick(View v) {
		if (v.getId() == R.id.id_button_back) {
			finish();
		} else if (v.getId() == R.id.id_share) {
			FileUtils.shareScreen(this);
		}
	}

	protected void onResume() {
		if (MyUtils.doInfilter(BaseTabActivity.this)) {
			if (!MyUtils.isBackground(this)) {
				MyUtils.startProtal(this);
			}
		}
		super.onResume();
	}

	protected void setWatermarkImage() {
		UIUtil.setWatermarkImage(this);
	}

	protected void setTitle(String menuCode) {
		TextView title = (TextView) findViewById(R.id.title);
		if (title != null)
			title.setText(getConfigLoader().getMenuName(menuCode));
	}

	protected void initTab(List<Map<String, String>> menuList) {
		TabHost tabHost = getTabHost();
		for (int i = 0; i < menuList.size(); i++) {
			Map<String, String> menu = menuList.get(i);
			addTab(tabHost, menu);
		}
		tabHost.setOnTabChangedListener(this);
	}

	protected void addTab(TabHost tabHost, Map<String, String> menu) {
		tabHost.addTab(tabHost
				.newTabSpec(menu.get(BaseConfigLoader.KEY_MENU_CODE))
				.setIndicator(
						getTabView(tabHost,
								menu.get(BaseConfigLoader.KEY_MENU_NAME),
								menu.get(BaseConfigLoader.KEY_MENU_ICON)))
				.setContent(
						getTabIntent(menu.get(BaseConfigLoader.KEY_MENU_CODE),
								menu.get(BaseConfigLoader.KEY_MENU_TYPE))));
	}

	protected View getTabView(TabHost tabHost, String name, String iconId) {
		View v = LayoutInflater.from(this).inflate(getTabViewLayout(),
				tabHost.getTabWidget(), false);
		TextViewUtils.setText(v, R.id.title, name);
		ImageView icon = (ImageView) v.findViewById(R.id.icon);
		if (icon != null)
			icon.setImageResource(getMenuIcon(iconId));
		return v;
	}

	protected int getTabViewLayout() {
		return R.layout.tab_button_1;
	}

	protected Intent getTabIntent(String code, String type) {
		Intent intent = getConfigLoader().getMenuIntent(this, type);
		intent.putExtra(BaseConfigLoader.KEY_MENU_CODE, code);
		return intent;
	}

	protected int getMenuIcon(String iconId) {
		return getConfigLoader().getMenuIcon(iconId);
	}

	protected void showTabs(boolean show) {
		getTabWidget().setVisibility(show ? View.VISIBLE : View.GONE);
	}

	protected abstract BaseConfigLoader getConfigLoader();

	@Override
	public void onTabChanged(String tabId) {
		
		
	}
	
	
}
