/**
 * 
 */
package com.bonc.mobile.hbmclient.activity;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.cordova.CordovaActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;
import com.bonc.mobile.hbmclient.view.adapter.UnLoadDataSecondAdapter;

import common.share.lwg.util.mediator.proxy_impl.port.GetNewInfo;

/**
 * @author liweigao
 * 
 */
public class UnLoadDataSecondActivity extends BaseActivity {
	private List<Map<String, String>> secondMenuList;

	// private SQLHelper sqlHelper = new SQLHelper();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unload_data_second_activity_layout);
		findViewById(R.id.relative).setBackgroundDrawable(
				WatermarkImage.getWatermarkDrawable());

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String menuCode = bundle.getString(MenuEntryAdapter.KEY_MENU_CODE);
		BusinessDao dao = new BusinessDao();
		String title = dao.getMenuName(menuCode);

		secondMenuList = new BusinessDao().getMenuSecond(menuCode);
		TextView tv_title = (TextView) this.findViewById(R.id.id_title);
		tv_title.setText(title);
		TextView synchhome_tv_date = (TextView) findViewById(R.id.synchhome_tv_date);
		synchhome_tv_date.setText(DateUtil.formatter(Calendar.getInstance()
				.getTime(), "yyyy年MM月dd日, EEEE"));

		ListView lv = (ListView) this.findViewById(R.id.id_listview);
		lv.setAdapter(new UnLoadDataSecondAdapter(this, secondMenuList));
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String menuCode = secondMenuList.get(position).get("menuCode");
				String menuType = secondMenuList.get(position).get("menuType");
				Class goTo = null;

				System.out.println("menuCode:" + menuCode);

				if ("10".equalsIgnoreCase(menuType)) {
					goTo = DailyReportActivity.class;
				} else if ("12".equalsIgnoreCase(menuType)) {
					goTo = MainKpiActivity.class;
				} else if ("14".equalsIgnoreCase(menuType)) {
					goTo = ChannelAnalyzeActivity.class;
				} else if ("1".equalsIgnoreCase(menuType)) {
					goTo = KPIHomeActivity.class;
				} else if ("15".equalsIgnoreCase(menuType)) {
					goTo = BusinessOutletsActivity.class;
					// 新增menuType 27 2016年5月27日10:28:21
				} else if ("27".equalsIgnoreCase(menuType)) {
					goTo = BroadActivity.class;
					// goTo = TestActivity.class;
				} else {
					goTo = MenuEntryAdapter.getTargetClass(
							UnLoadDataSecondActivity.this, menuType, menuCode);
				}

				Intent intent = new Intent(UnLoadDataSecondActivity.this, goTo);

				// Intent intent = new Intent(UnLoadDataSecondActivity.this,
				// MenuEntryAdapter.getTargetClass(UnLoadDataSecondActivity.this,menuType,menuCode));

				intent.putExtra(MenuEntryAdapter.KEY_MENU_CODE, menuCode);
				startActivityForResult(intent,
						MenuSecondActivity.REQUEST_CODE_FINISH);
				overridePendingTransition(0, 0);

				GetNewInfo.getSingleInstance().insertScan(menuCode);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == MenuSecondActivity.REQUEST_CODE_FINISH
				&& resultCode == RESULT_OK) {
			finish();
		}
	}
}
