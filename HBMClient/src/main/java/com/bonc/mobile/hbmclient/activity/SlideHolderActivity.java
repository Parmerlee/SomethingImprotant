package com.bonc.mobile.hbmclient.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.FrameLayout;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.view.SlideHolder;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;

/**
 * @author sunwei
 */
public class SlideHolderActivity extends BaseActivity {
	public SlideHolder slideHolder;
	public ExpandableListView sidemenu;
	public FrameLayout main_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slideholder);
		initSlideHolder();
	}

	@Override
	public void onBackPressed() {
		if (slideHolder.isOpened()) {
			// 如果侧边栏处于打开状态，点击回退键，先关闭侧边栏
			slideHolder.toggle();
			return;
		}
		super.onBackPressed();
	};

	protected void initSlideHolder() {
		slideHolder = (SlideHolder) findViewById(R.id.slideHolder);
		slideHolder.setEnabled(false);
		sidemenu = (ExpandableListView) findViewById(R.id.id_sidemenu);
		this.sidemenu.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				return true;
			}
		});
		main_content = (FrameLayout) findViewById(R.id.main_content);
	}

	public void setMainContent(int id) {
		main_content.removeAllViews();
		main_content.addView(View.inflate(this, id, null));
	}

	public void setMainContent(String menucode, int id) {
		buildSideMenu(menucode);
		setMainContent(id);
	}

	public void setMainContent(View v) {
		main_content.removeAllViews();
		main_content.addView(v);
	}

	public void setMainContent(String menucode, View v) {
		buildSideMenu(menucode);
		setMainContent(v);
	}

	public void buildSideMenu(String menuCode) {
		new MenuEntryAdapter(this, sidemenu, menuCode);
	}
}
