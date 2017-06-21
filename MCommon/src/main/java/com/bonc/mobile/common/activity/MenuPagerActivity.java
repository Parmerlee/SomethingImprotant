package com.bonc.mobile.common.activity;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.R;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.view.RadioGroupMenu;
import com.bonc.mobile.common.view.adapter.MenuListAdapter;

public abstract class MenuPagerActivity extends BaseActivity {
	protected ViewPager pager;
	protected RadioGroupMenu pagertab;
	protected int currentPage;

	protected void initPager(List<Map<String, String>> menuList) {

		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new MenuPagerAdapter(menuList));
		pager.setOnPageChangeListener(new SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				currentPage = position;
				pagertab.check(position);
			}
		});
		pagertab = (RadioGroupMenu) findViewById(R.id.pagertab);
		pagertab.setAdapter(new MenuTabAdapter(this,
				R.layout.menu_group_button, menuList));
		pagertab.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (currentPage != checkedId) {
					pager.setCurrentItem(checkedId);
				}
			}
		});
		pagertab.check(0);
	}

	protected class MenuTabAdapter extends ArrayAdapter<Map<String, String>> {
		public MenuTabAdapter(Context context, int resource,
				List<Map<String, String>> objects) {
			super(context, resource, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Map<String, String> m = (Map<String, String>) getItem(position);
			View v = super.getView(position, convertView, parent);
			v.setId(position);
			((TextView) v).setText(m.get(BaseConfigLoader.KEY_MENU_NAME));
			return v;
		}
	}

	protected class MenuPagerAdapter extends PagerAdapter {
		List<Map<String, String>> menuList;

		public MenuPagerAdapter(List<Map<String, String>> menuList) {
			this.menuList = menuList;
		}

		@Override
		public int getCount() {
			return menuList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Context c = container.getContext();
			View view = View.inflate(c, R.layout.menu_pager_item, null);
			ListView list = (ListView) view.findViewById(R.id.list);
			String menuCode = menuList.get(position).get(
					BaseConfigLoader.KEY_MENU_CODE);
			List<Map<String, String>> sub_menu = getConfigLoader()
					.getSecondMenu(menuCode);
			if (!AppConstant.SEC_ENH)
				Log.d(this.getClass().getName(), "sub_menu:" + sub_menu);
			SimpleAdapter adapter = new MenuListAdapter(c, sub_menu,
					R.layout.menu_list_item, new String[] {
							BaseConfigLoader.KEY_MENU_NAME,
							BaseConfigLoader.KEY_MENU_ICON }, new int[] {
							R.id.text, R.id.icon }, getConfigLoader());
			list.setAdapter(adapter);
			list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					onMenuItemClick((Map<String, String>) parent.getAdapter()
							.getItem(position));
				}
			});
			container.addView(view);
			return view;
		}
	}

	protected abstract BaseConfigLoader getConfigLoader();

	protected void onMenuItemClick(Map<String, String> menu_info) {
		Intent intent = getConfigLoader().getMenuIntent(this,
				menu_info.get(BaseConfigLoader.KEY_MENU_TYPE));
		intent.putExtra(BaseConfigLoader.KEY_MENU_CODE,
				menu_info.get(BaseConfigLoader.KEY_MENU_CODE));
		startActivity(intent);
	}
}
