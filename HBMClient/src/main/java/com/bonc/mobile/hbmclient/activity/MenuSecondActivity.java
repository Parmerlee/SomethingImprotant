package com.bonc.mobile.hbmclient.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.data.MenuDataSyncThread;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;

/**
 * 安徽数据门户主功能界面listItemView
 * 
 * @author LChao
 * 
 */
public class MenuSecondActivity extends BaseActivity implements OnClickListener {
	int flag = -1;
	AdapterContextMenuInfo menuInfo = null;
	public static final int CHANGE_MODULE_STATE = 100;
	private ArrayList<Boolean> moduleDataStateList = new ArrayList<Boolean>();
	public static final int DATEPICKERDIALOG = 0;
	public static final int RESETDIALOG = 1;
	public static final int DOUBLEDIALOG = 2;
	private ListView itemListView;
	private String moduleCode, moduleDesc;
	private Handler handler = new Handler();
	private View[] listItemView;
	private String[] TitleTextNew;
	// 显示当前手机
	private TextView synchhome_tv_date, tv_moduleDesc;

	// 二级菜单列表
	private List<Map<String, String>> secondMenuList;
	private MenuDataSyncThread dataThread;
	private Integer[] ImageIds = { R.mipmap.home_second_tongx,
			R.mipmap.home_second_yingy, R.mipmap.home_second_qianf,
			R.mipmap.home_second_3, R.mipmap.home_second_4,
			R.mipmap.home_second_5, R.mipmap.home_second_6,
			R.mipmap.home_second_7 };

	private boolean isFirstLoad = true;

	public final static int REQUEST_CODE_FINISH = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_NoTitleBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.synchhome);
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.relative);
		rl.setBackgroundDrawable(WatermarkImage.getWatermarkDrawable());

		synchhome_tv_date = (TextView) findViewById(R.id.synchhome_tv_date);
		synchhome_tv_date.setText(DateUtil.formatter(Calendar.getInstance()
				.getTime(), "yyyy年MM月dd日, EEEE"));
		// 从上一个activity获取参数A
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		try {
			moduleCode = bundle.getString("first_menu_code");// 使用字符串来接收Buddle里面的值，即前台的值
			moduleDesc = bundle.getString("first_menu_name");
			tv_moduleDesc = (TextView) findViewById(R.id.tv_moduleDesc);
			tv_moduleDesc.setText(moduleDesc);
		} catch (Exception e) {
		}

		secondMenuList = new BusinessDao().getMenuSecond(moduleCode);

		listItemView = new View[secondMenuList.size()];
		TitleTextNew = new String[secondMenuList.size()];

		for (int i = 0; i < secondMenuList.size(); i++) {
			TitleTextNew[i] = secondMenuList.get(i).get("menuName");
		}

		for (int i = 0; i < secondMenuList.size(); i++) {
			moduleDataStateList.add(false);
		}

		itemListView = (ListView) findViewById(R.id.listView);
		for (int i = 0; i < secondMenuList.size(); i++) {
			View viewHc = LayoutInflater.from(MenuSecondActivity.this).inflate(
					R.layout.home_list_view, null);

			ImageView imageViewItem = (ImageView) viewHc
					.findViewById(R.id.ImageViewItem);
			imageViewItem.setImageResource(ImageIds[i % ImageIds.length]);
			TextView textview = (TextView) viewHc
					.findViewById(R.id.TextViewItem);
			if (TitleTextNew[i].length() > 10) {// 宽带设置字体小点
				textview.setTextSize((float) 19);
			}
			textview.setText(TitleTextNew[i]);

			ImageView ImageViewProgres = (ImageView) viewHc
					.findViewById(R.id.ImageViewProgress);
			ProgressBar loadProgressBar = (ProgressBar) viewHc
					.findViewById(R.id.loadProgressBar);
			TextView textViewDate = (TextView) viewHc
					.findViewById(R.id.TextViewDate);
			textViewDate.setText("日期加载中...");
			ImageViewProgres
					.setImageResource(R.mipmap.ic_daydep_menu_icon_go);
			loadProgressBar.setVisibility(View.GONE);
			loadProgressBar.setIndeterminateDrawable(MenuSecondActivity.this
					.getResources()
					.getDrawable(R.drawable.progressbar_drawable));
			listItemView[i] = viewHc;
		}

		ListViewAdapter adapter = new ListViewAdapter();
		itemListView.setAdapter(adapter);

		itemListView.setOnItemClickListener(new ItemClickListener());

		// 菜单数据同步
		dataThread = new MenuDataSyncThread(MenuSecondActivity.this,
				secondMenuList);
		handler.post(new Runnable() {
			@Override
			public void run() {
				refreshListView();
				dataThread.start();
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (isFirstLoad) {
			isFirstLoad = false;
		} else {
			secondMenuList = new BusinessDao().getMenuSecond(moduleCode);
			final MenuDataSyncThread dataThread2 = new MenuDataSyncThread(
					MenuSecondActivity.this, secondMenuList);
			handler.post(new Runnable() {
				@Override
				public void run() {
					dataThread2.start();
				}
			});
		}
	}

	@Override
	public void onClick(View v) {

	}

	// 主界面ListView适配器
	class ListViewAdapter extends BaseAdapter {

		private Integer[] Progress = { R.mipmap.ic_daydep_menu_icon_go,
				R.mipmap.ic_daydep_menu_icon_na };

		@Override
		public int getCount() {
			return secondMenuList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (listItemView[position] != null) {
				convertView = listItemView[position];
			} else {
				convertView = LayoutInflater.from(MenuSecondActivity.this)
						.inflate(R.layout.home_list_view, null);
				TextView textview = (TextView) convertView
						.findViewById(R.id.TextViewItem);
				textview.setText(TitleTextNew[position]);
				ImageView ImageViewProgres = (ImageView) convertView
						.findViewById(R.id.ImageViewProgress);
				ProgressBar loadProgressBar = (ProgressBar) convertView
						.findViewById(R.id.loadProgressBar);
				TextView textViewDate = (TextView) convertView
						.findViewById(R.id.TextViewDate);
				textViewDate.setText("日期加载中...");
				ImageViewProgres.setImageResource(Progress[0]);
				loadProgressBar.setVisibility(View.GONE);
				loadProgressBar
						.setIndeterminateDrawable(MenuSecondActivity.this
								.getResources().getDrawable(
										R.drawable.progressbar_drawable));
				listItemView[position] = convertView;
			}
			return convertView;
		}
	}

	// 实现主界面ListView的OnItemClickListener接口，添加ListItem点击事件
	class ItemClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> arg0, View arg1,
				final int position, long arg3) {

			if (moduleDataStateList.get(position)) {
				if (MenuDataSyncThread.isSynchring()) {
					MenuDataSyncThread.setInterupt(true);
				}
				stopSync();
				Intent intent = new Intent(MenuSecondActivity.this,
						KPIHomeActivity.class);
				intent.putExtra("menu_name",
						secondMenuList.get(position).get("menuName"));
				intent.putExtra("menuCode",
						secondMenuList.get(position).get("menuCode"));

				startActivityForResult(intent, REQUEST_CODE_FINISH);
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_FINISH && resultCode == RESULT_OK) {
			finish();
		}
	}

	@Override
	public void finish() {
		stopSync();
		super.finish();
	}

	/**
	 * 刷新某个菜单.
	 * 
	 * @param i
	 */
	@Override
	public void refreshListView(final int which, final int state) {

		// 同步全部结束.
		if (which == -1) {
			return;
		}

		final String dataBetween = new BusinessDao().getMenuDataBetween(
				secondMenuList.get(which).get("menuCode"),
				secondMenuList.get(which).get("dataTable"),
				secondMenuList.get(which).get("dataType"));

		handler.post(new Runnable() {
			@Override
			public void run() {

				// 如果处于非同步中.
				if (state == 0) {
					((ProgressBar) (listItemView[which]
							.findViewById(R.id.loadProgressBar)))
							.setVisibility(View.GONE);

					((ImageView) listItemView[which]
							.findViewById(R.id.ImageViewProgress))
							.setVisibility(View.VISIBLE);

					if (dataBetween != null && !"".equals(dataBetween)) {
						((TextView) listItemView[which]
								.findViewById(R.id.TextViewDate))
								.setText(dataBetween);
						((ImageView) listItemView[which]
								.findViewById(R.id.ImageViewProgress))
								.setImageResource(R.mipmap.ic_daydep_menu_icon_go);

						moduleDataStateList.set(which, true);

					} else {
						((TextView) listItemView[which]
								.findViewById(R.id.TextViewDate)).setText("");

						((ImageView) listItemView[which]
								.findViewById(R.id.ImageViewProgress))
								.setImageResource(R.mipmap.ic_daydep_menu_icon_na);

						moduleDataStateList.set(which, false);
					}

				} else if (state == 1) // 如果处于同步中
				{

					((ProgressBar) (listItemView[which]
							.findViewById(R.id.loadProgressBar)))
							.setVisibility(View.VISIBLE);

					((ImageView) listItemView[which]
							.findViewById(R.id.ImageViewProgress))
							.setVisibility(View.GONE);

					((TextView) listItemView[which]
							.findViewById(R.id.TextViewDate))
							.setText(dataBetween);
				}

			}
		});

	}

	/**
	 * 刷新每个二级菜单的日期时间段.
	 */
	private void refreshListView() {
		for (int a = 0; a < secondMenuList.size(); a++) {
			refreshListView(a, 0);
		}
	}

	/**
	 * 停止同步
	 */
	public void stopSync() {
		if (MenuDataSyncThread.isSynchring()) {
			MenuDataSyncThread.setInterupt(true);
		}

	}

}
