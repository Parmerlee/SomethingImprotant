/**
 * TODO
 */
package com.bonc.mobile.hbmclient.terminal.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.data.SQLHelper;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;
import com.bonc.mobile.hbmclient.terminal.subactivity.TopRankActivityGoTo;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.StringUtil;
import com.bonc.mobile.hbmclient.view.Top10HorizontalScrollView;
import com.bonc.mobile.hbmclient.view.Top10ViewGroup;

/**
 * @author liweigao
 * 
 */
public class ModuleBasicViewTop10ViewBranch extends ViewBranch {
	private Top10HorizontalScrollView mTop10HorizontalScrollView;
	private BusinessDao businessdao = new BusinessDao();
	private Map<String, String> userinfo = businessdao.getUserInfo(); // 区域用户信息

	/**
	 * @param c
	 * @param tae
	 */
	public ModuleBasicViewTop10ViewBranch(Context c, TerminalActivityEnum tae) {
		super(c, tae);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setView(View view) {
		// TODO Auto-generated method stub
		this.mTop10HorizontalScrollView = (Top10HorizontalScrollView) view;
	}

	@Override
	public void setViewListener() {
		// TODO Auto-generated method stub
		this.mTop10HorizontalScrollView
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(context,
								TopRankActivityGoTo.class);
						intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
						Map<String, String> param = new HashMap<String, String>(); // 远程请求参数
						String action = null;
						String title_big = null;
						String menuCode = null;
						param.put(TerminalConfiguration.KEY_OPTIME,
								mTerminalActivityEnum.getOPtime());
						param.put(TerminalConfiguration.KEY_DATA_TYPE,
								mTerminalActivityEnum.getDateRange()
										.getDateFlag());
						param.put(TerminalConfiguration.KEY_AREA_CODE,
								mTerminalActivityEnum.getAreaCode()); // 地区id

						switch (mTerminalActivityEnum) {
						case PSS_DAY_ACTIVITY:
							title_big = "TOP20进销存机型【日监控】";
							menuCode = TerminalConfiguration.KEY_MENU_CODE_TOP20_PSS_DAY;
							param.put(TerminalConfiguration.KEY_KPI_CODES,
									businessdao.getMenuAllKpiString(menuCode));
							param.put(TerminalConfiguration.KEY_COLUMN_NAME,
									TerminalConfiguration.TOP20_COLUMN_NAME_DAY); // 列
							param.put(TerminalConfiguration.KEY_DATATABLE,
									businessdao.getMenuColDataTable(menuCode));
							action = "palmBiTermDataAction_termRankDataQuery.do";
							break;
						case PSS_MONTH_ACTIVITY:
							title_big = "TOP20进销存机型【月盘点】";
							menuCode = TerminalConfiguration.KEY_MENU_CODE_TOP20_PSS_MONTH;
							param.put(TerminalConfiguration.KEY_KPI_CODES,
									businessdao.getMenuAllKpiString(menuCode));
							param.put(
									TerminalConfiguration.KEY_COLUMN_NAME,
									TerminalConfiguration.TOP20_COLUMN_NAME_MONTH); // 列
							param.put(TerminalConfiguration.KEY_DATATABLE,
									businessdao.getMenuColDataTable(menuCode));
							action = "palmBiTermDataAction_termRankDataQuery.do";
							break;
						case UNSALE_DAY_ACTIVITY:
							title_big = "TOP20滞销机型【日监控】";
							menuCode = TerminalConfiguration.KEY_MENU_CODE_TOP20_UNSALE_DAY;
							param.put(TerminalConfiguration.KEY_KPI_CODES,
									businessdao.getMenuAllKpiString(menuCode));
							param.put(TerminalConfiguration.KEY_COLUMN_NAME,
									TerminalConfiguration.TOP20_COLUMN_NAME_DAY); // 列
							param.put(TerminalConfiguration.KEY_DATATABLE,
									businessdao.getMenuColDataTable(menuCode));
							action = "palmBiTermDataAction_termRankDataQuery.do";
							break;
						case UNSALE_MONTH_ACTIVITY:
							title_big = "TOP20滞销机型【月盘点】";
							menuCode = mTerminalActivityEnum.getMenuCode();
							param.put(TerminalConfiguration.KEY_KPI_CODES,
									businessdao.getMenuAllKpiString(menuCode));
							param.put(
									TerminalConfiguration.KEY_COLUMN_NAME,
									TerminalConfiguration.TOP20_COLUMN_NAME_MONTH); // 列
							param.put(TerminalConfiguration.KEY_DATATABLE,
									businessdao.getMenuColDataTable(menuCode));
							action = "palmBiTermDataAction_termRankDataQuery.do";
							break;
						case UNPACK_DAY_ACTIVITY:
							title_big = "TOP20拆包机型【日监控】";
							menuCode = mTerminalActivityEnum.getMenuCode();
							param.put(TerminalConfiguration.KEY_KPI_CODES,
									businessdao.getMenuAllKpiString(menuCode));
							param.put(TerminalConfiguration.KEY_COLUMN_NAME,
									TerminalConfiguration.TOP20_COLUMN_NAME_DAY); // 列
							param.put(TerminalConfiguration.KEY_DATATABLE,
									businessdao.getMenuColDataTable(menuCode));
							action = "palmBiTermDataAction_termRankDataQuery.do";
							break;
						case UNPACK_MONTH_ACTIVITY:
							title_big = "TOP20拆包机型【月盘点】";
							menuCode = mTerminalActivityEnum.getMenuCode();
							param.put(TerminalConfiguration.KEY_KPI_CODES,
									businessdao.getMenuAllKpiString(menuCode));
							param.put(
									TerminalConfiguration.KEY_COLUMN_NAME,
									TerminalConfiguration.TOP20_COLUMN_NAME_MONTH); // 列
							param.put(TerminalConfiguration.KEY_DATATABLE,
									businessdao.getMenuColDataTable(menuCode));
							action = "palmBiTermDataAction_termRankDataQuery.do";
							break;
						case FG_MONTH_ACTIVITY:
							title_big = "TOP20窜货机型【月盘点】";
							menuCode = mTerminalActivityEnum.getMenuCode();
							param.put(TerminalConfiguration.KEY_KPI_CODES,
									businessdao.getMenuAllKpiString(menuCode));
							param.put(
									TerminalConfiguration.KEY_COLUMN_NAME,
									TerminalConfiguration.TOP20_COLUMN_NAME_MONTH); // 列
							param.put(TerminalConfiguration.KEY_DATATABLE,
									businessdao.getMenuColDataTable(menuCode));
							action = "palmBiTermDataAction_termRankDataQuery.do";
							break;
						}

						intent.putExtra(TerminalConfiguration.KEY_ACTION,
								action);
						intent.putExtra(TerminalConfiguration.KEY_MAP,
								(Serializable) param);
						intent.putExtra(
								TerminalConfiguration.KEY_ACTIVITY_ENUM,
								mTerminalActivityEnum);
						intent.putExtra(TerminalConfiguration.TITLE_BIG,
								title_big);
						context.startActivity(intent);
					}
				});
	}

	@Override
	public void setData(JSONObject data) {
		super.setData(data);

		String menuCode = null;
		String typeName = null;
		switch (mTerminalActivityEnum) {
		case PSS_DAY_ACTIVITY:
			menuCode = TerminalConfiguration.KEY_MENU_CODE_TOP20_PSS_DAY;
			typeName = "销量:";
			break;
		case UNSALE_DAY_ACTIVITY:
			menuCode = TerminalConfiguration.KEY_MENU_CODE_TOP20_UNSALE_DAY;
			typeName = "滞销:";
			break;
		case UNPACK_DAY_ACTIVITY:
			menuCode = TerminalConfiguration.KEY_MENU_CODE_TOP20_UNPACK_DAY;
			typeName = "拆包:";
			break;
		case UNSALE_MONTH_ACTIVITY:
			menuCode = TerminalConfiguration.KEY_MENU_CODE_TOP20_UNSALE_MONTH;
			typeName = "滞销:";
			break;
		case UNPACK_MONTH_ACTIVITY:
			menuCode = TerminalConfiguration.KEY_MENU_CODE_TOP20_UNPACK_MONTH;
			typeName = "拆包:";
			break;
		case FG_MONTH_ACTIVITY:
			menuCode = TerminalConfiguration.KEY_MENU_CODE_TOP20_FG_MONTH;
			typeName = "窜货:";
			break;
		case PSS_MONTH_ACTIVITY:
			menuCode = TerminalConfiguration.KEY_MENU_CODE_TOP20_PSS_MONTH;
			typeName = "销量:";
			break;
		default:
			break;
		}

		Calendar calendar = mTerminalActivityEnum.getCalendar();

		if (calendar == null) {
			this.mTop10HorizontalScrollView.setData(getMockData(typeName));
			return;
		}

		String dateString = null;
		switch (mTerminalActivityEnum.getDateRange()) {
		case DAY:
			dateString = DateUtil.formatter(calendar.getTime(),
					DateUtil.PATTERN_8);
			break;
		case MONTH:
			dateString = DateUtil.formatter(calendar.getTime(),
					DateUtil.PATTERN_6);
			break;
		}

		if (StringUtil.isNull(dateString)) {
			this.mTop10HorizontalScrollView.setData(getMockData(typeName));
			return;

		}

		BusinessDao businessDao = new BusinessDao();
		Map<String, String> menuInfoMap = businessDao.getMenuInfo(menuCode);
		List<Map<String, String>> kpiList = businessDao
				.getMenuAllKpiList(menuCode);

		if (menuInfoMap == null || kpiList == null || kpiList.size() == 0) {
			this.mTop10HorizontalScrollView.setData(getMockData(typeName));
			return;
		}

		String kpiCodes = "";
		int len = kpiList.size();

		for (int i = 0; i < len; i++) {
			if (!"".equals(kpiCodes)) {
				kpiCodes += ",";
			}
			kpiCodes += "'" + kpiList.get(i).get("kpi_code") + "'";
		}

		for (int i = 0; i < len; i++) {
			if (i == len - 1) {
				this.kpi_statistics += kpiList.get(i).get("kpi_code");
			} else {
				this.kpi_statistics = this.kpi_statistics
						+ kpiList.get(i).get("kpi_code") + "|";
			}
		}

		String dataTable = menuInfoMap.get("dataTable");

		String sqlString = null;
		if (mTerminalActivityEnum.getDateRange() == DateRangeEnum.DAY) {
			sqlString = "Select 'TOP10' as top10,'"
					+ typeName
					+ "' as typeName,'占比:'as zbName,"
					+ " case when cast(curday_value as integer)>10000 then round(curday_value/10000,2)||'万' else"
					+ " cast(curday_value as integer) end  as typeValue,round(curday_value_dr*100,2)||'%' as zbValue "
					+ ",term_type_name as modelName" + " from " + dataTable
					+ " t,kpi_info k WHERE  "
					+ " t.kpi_code = k.kpi_code and  k.KPI_CODE in(" + kpiCodes
					+ ") and t.OP_TIME = '" + dateString + "' "
					+ " order by cast(k.KPI_ORDER as integer) asc limit 10";
		} else {
			sqlString = "Select 'TOP10' as top10,'"
					+ typeName
					+ "' as typeName,'占比:'as zbName,"
					+ " case when cast(curmonth_value as integer)>10000 then round(curmonth_value/10000,2)||'万' else cast(curmonth_value as integer) end "
					+ " as typeValue,round(curmonth_value_dr*100,2)||'%' as zbValue "
					+ ",term_type_name as modelName" + " from " + dataTable
					+ " t,kpi_info k WHERE  "
					+ " t.kpi_code = k.kpi_code and  k.KPI_CODE in(" + kpiCodes
					+ ") and t.OP_TIME = '" + dateString + "' "
					+ " order by cast(k.KPI_ORDER as integer) asc limit 10";
		}

		List<Map<String, String>> topList = new SQLHelper().queryForList(
				sqlString, null);
		topList = getTermData("data1");

		if (topList == null || topList.size() == 0) {
			topList = getMockData(typeName);
		} else if (topList.size() > 0
				&& topList.size() < Top10ViewGroup.CHILD_NUM) {
			Map<String, String> temp = null;
			for (int i = 0; i < Top10ViewGroup.CHILD_NUM; i++) {
				try {
					temp = topList.get(i);
				} catch (Exception e) {
					// TODO: handle exception
					temp = new HashMap<String, String>();
					temp.put("top10", "TOP10");
					temp.put("modelName", Constant.NULL_VALUE_INSTEAD);
					temp.put("typeName", typeName);
					temp.put("typeValue", Constant.NULL_VALUE_INSTEAD);
					temp.put("zbName", "占比:");
					temp.put("zbValue", Constant.NULL_VALUE_INSTEAD);
					topList.add(temp);
				}
			}
		}

		this.mTop10HorizontalScrollView.setData(topList);
	}

	@Override
	public void updateView() {
		// TODO Auto-generated method stub
		this.mTop10HorizontalScrollView.updateView();
	}

	@Override
	public void dispatchView() {
		// TODO Auto-generated method stub
		this.mTop10HorizontalScrollView.dispatchView();
	}

	/**
	 * 获取测试数据.
	 * 
	 * @return
	 */
	private List<Map<String, String>> getMockData(String typeName) {

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < 10; i++) {
			Map<String, String> tempMap = new HashMap<String, String>();
			tempMap.put("top10", "TOP10");
			tempMap.put("modelName", Constant.NULL_VALUE_INSTEAD);
			tempMap.put("typeName", typeName);
			tempMap.put("typeValue", Constant.NULL_VALUE_INSTEAD);
			tempMap.put("zbName", "占比:");
			tempMap.put("zbValue", Constant.NULL_VALUE_INSTEAD);
			resultList.add(tempMap);
		}

		return resultList;
	}

}
