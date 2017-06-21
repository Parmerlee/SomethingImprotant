/**
 * RelationKpiActivity
 */
package com.bonc.mobile.hbmclient.activity;

import android.content.Intent;
import android.os.Bundle;

import com.bonc.mobile.hbmclient.asyn_task.NoticeAsynTask;
import com.bonc.mobile.hbmclient.state.relationkpi.MachineRelationKpi;

/**
 * @author liweigao
 *
 */
public class RelationKpiActivity extends SlideHolderActivity {
	public final static String KEY_MAIN_KPI_CODE = "key_main_kpi_code";
	public final static String KEY_MAIN_KPI_TYPE = "key_main_kpi_type";
	public final static String KEY_OPTIME = "key_optime";
	public final static String KEY_AREACODE = "key_areacode";
	public final static String KEY_MENU_CODE = "key_level_menu_code";

	private MachineRelationKpi mMachine;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.activity.SlideHolderActivity#onCreate(android
	 * .os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		String mainKpiCode = i.getStringExtra(KEY_MAIN_KPI_CODE);
		String type = i.getStringExtra(KEY_MAIN_KPI_TYPE);
		String optime = i.getStringExtra(KEY_OPTIME);
		String areacode = i.getStringExtra(KEY_AREACODE);
		String levelMenuCode = i.getStringExtra(KEY_MENU_CODE);

		this.mMachine = new MachineRelationKpi(mainKpiCode, type, optime,
				areacode, levelMenuCode, this);
		this.mMachine.launch();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.activity.MenuActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		if (NoticeAsynTask.mPopupWindow != null
				&& NoticeAsynTask.mPopupWindow.isShowing()) {
			NoticeAsynTask.mPopupWindow.dismiss();
			NoticeAsynTask.mPopupWindow = null;
		}
		super.onDestroy();
	}

}
