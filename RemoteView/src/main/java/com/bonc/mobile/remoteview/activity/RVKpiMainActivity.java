package com.bonc.mobile.remoteview.activity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.kpi.KpiConstant;
import com.bonc.mobile.common.kpi.KpiDataModel;
import com.bonc.mobile.common.kpi.KpiMainActivity;
import com.bonc.mobile.common.kpi.KpiTableColumn;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.StringUtil;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.remoteview.common.ConfigLoader;
import com.bonc.mobile.remoteview.common.Constant;
import com.bonc.mobile.remoteview.common.RemoteUtil;

public class RVKpiMainActivity extends KpiMainActivity {
	@Override
	protected BaseConfigLoader getConfigLoader() {
		return ConfigLoader.getInstance(this);
	}

	@Override
	protected String getQueryAction() {
		String action = "/kpi", menuType = getConfigLoader().getMenuAttr(
				menuCode, ConfigLoader.KEY_MENU_TYPE);
		if ("11".equals(menuType)) {
			action = "/kpi";
		}
		return action;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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

	@Override
	protected Intent getSubKpiIntent(String kpiCode, int columnType,
			KpiDataModel dataModel) {
		if (columnType == KpiTableColumn.COLUMN_TYPE_NAME) {
			return new Intent(this, RVKpiAreaActivity.class);
		} else if (columnType == KpiTableColumn.COLUMN_TYPE_TREND) {
			return new Intent(this, RVKpiTrendChartActivity.class);
		} else if (columnType == KpiTableColumn.COLUMN_TYPE_DATA) {
			return new Intent(this, RVKpiTimeActivity.class);
		}
		return null;
	}

	@Override
	protected void addKpiToCustom(final String kpiCode) {
		final List<Map<String, String>> ch_list = ConfigLoader
				.getInstance(this).getChannel();
		String[] ch_names = DataUtil.extractList(ch_list,
				ConfigLoader.KEY_MENU_NAME).toArray(new String[0]);
		if (ch_names.length == 0) {
			new AlertDialog.Builder(this)
					.setTitle(R.string.hint)
					.setMessage("尚未创建自定义频道")
					.setPositiveButton("管理自定义频道",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent(
											RVKpiMainActivity.this,
											ChannelMgrActivity.class);
									intent.putExtra(ConfigLoader.KEY_MENU_CODE,
											"12000045");
									startActivity(intent);
								}
							}).setNegativeButton(R.string.cancel, null).show();
		} else {
			String kpiName = dataView.getModel().getKpiConfig(kpiCode)
					.get(KpiConstant.KEY_KPI_NAME);
			UIUtil.showAlertDialog(this, StringUtil.abbr(kpiName, 10)
					+ "\n请选择要加入的自定义频道", ch_names,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							doAddChannelKpi(
									kpiCode,
									ch_list.get(which).get(
											ConfigLoader.KEY_MENU_CODE));
						}
					});
		}
	}

	void doAddChannelKpi(String kpiCode, String channelCode) {
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("appType", Constant.APP_TYPE);
		param.put("menuCode", channelCode);
		param.put("kpiCodes", kpiCode);
		new KpiActionTask(this).execute("/custmenu/saveKpi", param);
	}

	class KpiActionTask extends HttpRequestTask {
		public KpiActionTask(Context context) {
			super(context);
		}

		@Override
		protected void handleResult(JSONObject result) {
			showToast(result.optString("msg"));
		}
	}

	@Override
	protected void onNoKpi(JSONObject result) {
		super.onNoKpi(result);
		if (isKpiMutable()) {
			new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
					.setTitle(R.string.hint)
					.setMessage("该自定义频道未关联指标，是否现在关联指标？")
					.setPositiveButton("是",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent(
											RVKpiMainActivity.this,
											ChannelKpiMgrActivity.class);
									intent.putExtra(
											ChannelKpiMgrActivity.CH_CODE,
											menuCode);
									startActivity(intent);
								}
							}).setNegativeButton("否", null).show();
		}
	}
}
