package com.bonc.mobile.hbmclient.terminal.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter.ColumnDisplyInfo;
import com.bonc.mobile.hbmclient.data.ComplexDimInfo;
import com.bonc.mobile.hbmclient.data.SQLHelper;
import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;
import com.bonc.mobile.hbmclient.terminal.subactivity.SaleAnalysisActivity;
import com.bonc.mobile.hbmclient.view.HorizontalProgressBarViewGroup;

/**
 * @author liweigao
 *
 */
public class ModuleBasicViewProgressBarGroupViewBranch extends ViewBranch {
	private HorizontalProgressBarViewGroup mHorizontalProgressBarViewGroup;

	// config
	private BusinessDao businessdao = new BusinessDao();
	private Map<String, String> userinfo = businessdao.getUserInfo(); // 区域用户信息

	/**
	 * @param c
	 * @param tae
	 */
	public ModuleBasicViewProgressBarGroupViewBranch(Context c,
			TerminalActivityEnum tae) {
		super(c, tae);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setView(View view) {
		// TODO Auto-generated method stub
		this.mHorizontalProgressBarViewGroup = (HorizontalProgressBarViewGroup) view;
	}

	@Override
	public void setViewListener() {
		// TODO Auto-generated method stub
		this.mHorizontalProgressBarViewGroup
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {

						Intent intent = new Intent(context,
								SaleAnalysisActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						Map<String, String> param = new HashMap<String, String>(); // 远程请求参数
						param.put(TerminalConfiguration.KEY_OPTIME,
								mTerminalActivityEnum.getOPtime());
						param.put(TerminalConfiguration.KEY_DATA_TYPE,
								mTerminalActivityEnum.getDateRange()
										.getDateFlag());
						param.put(TerminalConfiguration.KEY_AREA_CODE,
								userinfo.get("areaId")); // 地区id
						String action = null;
						String title_big = null;
						String menuCode = null;
						ComplexDimInfo dimkey = null;

						switch (mTerminalActivityEnum) {
						case PSS_DAY_ACTIVITY:
							title_big = "当月终端销量结构";
							menuCode = TerminalConfiguration.KEY_MENU_CODE_PSS_DAY;
							dimkey = businessdao
									.getMenuComplexDimKey(businessdao
											.getMenuInfo(menuCode));
							param.put(
									TerminalConfiguration.KEY_COLUMN_NAME,
									TerminalConfiguration.COLUMN_PSS_DAY_SALE_STRUCTURE); // 列
							param.put(TerminalConfiguration.KEY_DATATABLE,
									businessdao.getMenuColDataTable(menuCode));
							param.put(TerminalConfiguration.KEY_DIM_KEY,
									dimkey.getComplexDimKeyString());
							param.put(TerminalConfiguration.KEY_ISEXPAND, "1");
							action = "palmBiTermDataAction_purchargeRomDataQuery.do";
							intent.putExtra(
									TerminalConfiguration.TITLE_GROUP,
									TerminalConfiguration.TITLE_GROUP_PSS_DAY_SALE_STRUCTURE);
							intent.putExtra(
									TerminalConfiguration.TITLE_COLUMN,
									TerminalConfiguration.TITLE_COLUMN_PSS_DAY_SALE_STRUCTURE);
							intent.putExtra(
									TerminalConfiguration.KEY_KPI_CODES_ARRAY,
									TerminalConfiguration.KPI_PSS_DAY_SALE_STRUCTURE);
							intent.putExtra(
									TerminalConfiguration.KEY_RESPONSE_KEY,
									TerminalConfiguration.RESPONSE_PSS_DAY_SALE_STRUCTURE);
							intent.putExtra(
									TerminalConfiguration.KEY_COLUNM_KPI_CODE,
									TerminalConfiguration.COLUMN_KPI_CODE_SALE_STRUCTURE);
							break;
						case PSS_MONTH_ACTIVITY:
							title_big = "当月终端销量结构";
							menuCode = TerminalConfiguration.KEY_MENU_CODE_PSS_MONTH;
							dimkey = businessdao
									.getMenuComplexDimKey(businessdao
											.getMenuInfo(menuCode));
							param.put(
									TerminalConfiguration.KEY_COLUMN_NAME,
									TerminalConfiguration.COLUMN_PSS_MONTH_SALE_STRUCTURE); // 列
							param.put(TerminalConfiguration.KEY_DATATABLE,
									businessdao.getMenuColDataTable(menuCode));
							param.put(TerminalConfiguration.KEY_DIM_KEY,
									dimkey.getComplexDimKeyString());
							param.put(TerminalConfiguration.KEY_ISEXPAND, "1");
							action = "palmBiTermDataAction_purchargeRomDataQuery.do";
							intent.putExtra(
									TerminalConfiguration.TITLE_GROUP,
									TerminalConfiguration.TITLE_GROUP_PSS_MONTH_SALE_STRUCTURE);
							intent.putExtra(
									TerminalConfiguration.TITLE_COLUMN,
									TerminalConfiguration.TITLE_COLUMN_PSS_MONTH_SALE_STRUCTURE);
							intent.putExtra(
									TerminalConfiguration.KEY_KPI_CODES_ARRAY,
									TerminalConfiguration.KPI_PSS_MONTH_SALE_STRUCTURE);
							intent.putExtra(
									TerminalConfiguration.KEY_RESPONSE_KEY,
									TerminalConfiguration.RESPONSE_PSS_MONTH_SALE_STRUCTURE);
							intent.putExtra(
									TerminalConfiguration.KEY_COLUNM_KPI_CODE,
									TerminalConfiguration.COLUMN_KPI_CODE_SALE_STRUCTURE_MONTH);
							break;
						}

						intent.putExtra(TerminalConfiguration.TITLE_BIG,
								title_big);
						intent.putExtra(
								TerminalConfiguration.KEY_ACTIVITY_ENUM,
								mTerminalActivityEnum);
						intent.putExtra(TerminalConfiguration.KEY_ACTION,
								action);
						intent.putExtra(TerminalConfiguration.KEY_MAP,
								(Serializable) param);
						// context.startActivity(intent);
					}
				});
	}

	@Override
	public void setData(JSONObject data) {
		super.setData(data);

		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();

		String[] kpiNames = new String[] { "销售新机", "销售库存", "裸机", "合约机", "智能机",
				"普通机", "4G终端", "非4G终端", "自有资源", "代理商" };
		// 如果为进销存日
		if (mTerminalActivityEnum == TerminalActivityEnum.PSS_DAY_ACTIVITY) {
			this.kpi_statistics = "4600|4610|400|420|440|460|480|500|520|540";
			String[] kpiCodes = new String[] {
					TerminalConfiguration.KPI_PSS_TERMINAL_XJ_COUNT,
					TerminalConfiguration.KPI_PSS_TERMINAL_KCS_COUNT,
					TerminalConfiguration.KPI_PSS_TERMINAL_LJ_COUNT,
					TerminalConfiguration.KPI_PSS_TERMINAL_HYJ_COUNT,
					TerminalConfiguration.KPI_PSS_TERMINAL_ZNJ_COUNT,
					TerminalConfiguration.KPI_PSS_TERMINAL_PTJ_COUNT,
					TerminalConfiguration.KPI_PSS_TERMINAL_TD_COUNT,
					TerminalConfiguration.KPI_PSS_TERMINAL_2G_COUNT,
					TerminalConfiguration.KPI_PSS_TERMINAL_ZY_COUNT,
					TerminalConfiguration.KPI_PSS_TERMINAL_DL_COUNT };

			String inString = "";
			for (int i = 0; i < kpiCodes.length; i++) {
				if (!"".equals(inString)) {
					inString += ",";
				}
				inString += "'" + kpiCodes[i] + "'";
			}

			String sqlString = "select kpi_code as kpiCode,CURDAY_VALUE  as value,cast(CURDAY_VALUE_dr*100 as integer) as zb from TERMINAL_DAILY_JXC where kpi_code in("
					+ inString + ") and op_time=?";

			List<Map<String, String>> resultList = new SQLHelper()
					.queryForList(sqlString,
							new String[] { mTerminalActivityEnum.getOPtime() });
			resultList = getTermData("data8");

			if (resultList == null || resultList.size() == 0) {
				this.mHorizontalProgressBarViewGroup.setData(dataList);
				return;
			}

			Map<String, String> tempMap = null;
			for (int i = 0; i < kpiCodes.length && i < kpiNames.length; i++) {
				if (i % 2 == 0) {
					tempMap = new HashMap<String, String>();
				}

				for (int k = 0; k < resultList.size(); k++) {
					if (kpiCodes[i].equals(resultList.get(k).get("kpiCode"))) {
						ColumnDisplyInfo cdi = ColumnDataFilter.getInstance()
								.doFilter(Constant.TERMINAL_SALE_DEFAULT_RULE,
										"", resultList.get(k).get("value"));
						if (i % 2 == 0) {
							tempMap.put("leftName", kpiNames[i]);
							tempMap.put("leftValue",
									cdi.getValue() + cdi.getUnit());
							tempMap.put("pbLeft", resultList.get(k).get("zb"));
						} else {
							tempMap.put("rightName", kpiNames[i]);
							tempMap.put("rightValue",
									cdi.getValue() + cdi.getUnit());
							tempMap.put("pbRight", resultList.get(k).get("zb"));
						}

					}
				}

				if (i % 2 == 1) {
					if (tempMap.size() == 0) {
						tempMap.put("leftName", kpiNames[i - 1]);
						tempMap.put("leftValue", Constant.NULL_VALUE_INSTEAD);
						tempMap.put("rightName", kpiNames[i]);
						tempMap.put("rightValue", Constant.NULL_VALUE_INSTEAD);
					}

					dataList.add(tempMap);
				}
			}
		} else if (mTerminalActivityEnum == TerminalActivityEnum.PSS_MONTH_ACTIVITY) {
			// 如果为进销存月
			this.kpi_statistics = "5140|5150|2840|2860|2880|2900|2920|2940|2960|2980";
			String[] kpiCodes = new String[] { "5140", "5150", "2840", "2860",
					"2880", "2900", "2920", "2940", "2960", "2980" };

			String inString = "";
			for (int i = 0; i < kpiCodes.length; i++) {
				if (!"".equals(inString)) {
					inString += ",";
				}
				inString += "'" + kpiCodes[i] + "'";
			}

			String sqlString = "select kpi_code as kpiCode,CURMONTH_VALUE  as value,cast(CURMONTH_VALUE_dr*100 as integer) as zb from TERMINAL_MONTH_TERM where kpi_code in("
					+ inString + ") and op_time=?";

			List<Map<String, String>> resultList = new SQLHelper()
					.queryForList(sqlString,
							new String[] { mTerminalActivityEnum.getOPtime() });
			resultList = getTermData("data5");

			if (resultList == null || resultList.size() == 0) {
				this.mHorizontalProgressBarViewGroup.setData(dataList);
				return;
			}

			Map<String, String> tempMap = null;
			for (int i = 0; i < kpiCodes.length && i < kpiNames.length; i++) {
				if (i % 2 == 0) {
					tempMap = new HashMap<String, String>();
				}

				for (int k = 0; k < resultList.size(); k++) {
					if (kpiCodes[i].equals(resultList.get(k).get("kpiCode"))) {
						ColumnDisplyInfo cdi = ColumnDataFilter.getInstance()
								.doFilter(Constant.TERMINAL_SALE_DEFAULT_RULE,
										"", resultList.get(k).get("value"));
						if (i % 2 == 0) {
							tempMap.put("leftName", kpiNames[i]);
							tempMap.put("leftValue",
									cdi.getValue() + cdi.getUnit());
							tempMap.put("pbLeft", resultList.get(k).get("zb"));
						} else {
							tempMap.put("rightName", kpiNames[i]);
							tempMap.put("rightValue",
									cdi.getValue() + cdi.getUnit());
							tempMap.put("pbRight", resultList.get(k).get("zb"));
						}

					}
				}

				if (i % 2 == 1) {
					if (tempMap.size() == 0) {// 若无此对数据，则只显示名称
						tempMap.put("leftName", kpiNames[i - 1]);
						tempMap.put("leftValue", Constant.NULL_VALUE_INSTEAD);
						tempMap.put("rightName", kpiNames[i]);
						tempMap.put("rightValue", Constant.NULL_VALUE_INSTEAD);
					}

					dataList.add(tempMap);
				}

			}
		}

		this.mHorizontalProgressBarViewGroup.setData(dataList);
	}

	@Override
	public void updateView() {
		// TODO Auto-generated method stub
		this.mHorizontalProgressBarViewGroup.updateView();
	}

	@Override
	public void dispatchView() {
		// TODO Auto-generated method stub
		this.mHorizontalProgressBarViewGroup.dispatchView();
	}

}
