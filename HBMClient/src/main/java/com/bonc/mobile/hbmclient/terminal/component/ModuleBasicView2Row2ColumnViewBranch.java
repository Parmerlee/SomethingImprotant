/**
 * TODO
 */
package com.bonc.mobile.hbmclient.terminal.component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter.ColumnDisplyInfo;
import com.bonc.mobile.hbmclient.data.ComplexDimInfo;
import com.bonc.mobile.hbmclient.data.SQLHelper;
import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;
import com.bonc.mobile.hbmclient.terminal.subactivity.DaySaleCountStaticsActivity;

/**
 * @author liweigao
 * 
 */
public class ModuleBasicView2Row2ColumnViewBranch extends ViewBranch {

	private TextView titleLTV;
	private String titleL;
	private TextView titleRTV;
	private String titleR;

	private TextView lrow2valueTV;
	private TextView lrow2unitTV;

	private String lrow2value;
	private String lrow2unit;

	private TextView rrow2valueTV;
	private TextView rrow2unitTV;

	private String rrow2value;
	private String rrow2unit;

	// config
	private BusinessDao businessdao = new BusinessDao();
	private Map<String, String> userinfo = businessdao.getUserInfo(); // 区域用户信息

	/**
	 * @param c
	 * @param tae
	 */
	public ModuleBasicView2Row2ColumnViewBranch(Context c,
			TerminalActivityEnum tae) {
		super(c, tae);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setViewListener() {
		// TODO Auto-generated method stub
		this.mView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(context,
						DaySaleCountStaticsActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Map<String, String> param = new HashMap<String, String>(); // 远程请求参数
				param.put(TerminalConfiguration.KEY_OPTIME,
						mTerminalActivityEnum.getOPtime());
				param.put(TerminalConfiguration.KEY_DATA_TYPE,
						mTerminalActivityEnum.getDateRange().getDateFlag());
				param.put(TerminalConfiguration.KEY_AREA_CODE,
						userinfo.get("areaId")); // 地区id
				String action = null;
				String title_big = null;
				String menuCode = null;
				ComplexDimInfo dimkey = null;

				switch (mTerminalActivityEnum) {
				case FG_MONTH_ACTIVITY:
					title_big = "窜货月";
					menuCode = TerminalConfiguration.KEY_MENU_CODE_FG_MONTH;
					dimkey = businessdao.getMenuComplexDimKey(businessdao
							.getMenuInfo(menuCode));
					param.put(TerminalConfiguration.KEY_KPI_CODES,
							TerminalConfiguration.KPI_FG_MONTH_FG);
					param.put(TerminalConfiguration.KEY_COLUMN_NAME,
							TerminalConfiguration.CURMONTH_VALUE); // 列
					param.put(TerminalConfiguration.KEY_DATATABLE,
							businessdao.getMenuColDataTable(menuCode));
					param.put(TerminalConfiguration.KEY_DIM_KEY,
							dimkey.getComplexDimKeyString());
					param.put(TerminalConfiguration.KEY_ISEXPAND, "1");
					param.put(TerminalConfiguration.KEY_PART3TODATA, "1");
					action = "palmBiTermDataAction_manyKpiManyAreaManyTimeDataQuery.do";
					intent.putExtra(TerminalConfiguration.KEY_RESPONSE_KEY,
							TerminalConfiguration.RESPONSE_FG_MONTH_FG);
					intent.putExtra(TerminalConfiguration.KEY_COLUNM_KPI_CODE,
							TerminalConfiguration.COLUMN_KPI_CODE_MONTH_FG);
					intent.putExtra(TerminalConfiguration.TITLE_COLUMN,
							TerminalConfiguration.TITLE_COLUMN_FG_MONTH_FG);
					break;
				case UNPACK_DAY_ACTIVITY:
					title_big = "拆包日";
					menuCode = TerminalConfiguration.KEY_MENU_CODE_UNPACK_DAY;
					dimkey = businessdao.getMenuComplexDimKey(businessdao
							.getMenuInfo(menuCode));
					param.put(TerminalConfiguration.KEY_KPI_CODES,
							TerminalConfiguration.KPI_UNPACK_DAY_CHARGE_UIM);
					param.put(TerminalConfiguration.KEY_COLUMN_NAME,
							TerminalConfiguration.CURDAY_VALUE); // 列
					param.put(TerminalConfiguration.KEY_DATATABLE,
							businessdao.getMenuColDataTable(menuCode));
					param.put(TerminalConfiguration.KEY_DIM_KEY,
							dimkey.getComplexDimKeyString());
					param.put(TerminalConfiguration.KEY_ISEXPAND, "1");
					param.put(TerminalConfiguration.KEY_PART3TODATA, "1");
					action = "palmBiTermDataAction_manyKpiManyAreaManyTimeDataQuery.do";
					intent.putExtra(
							TerminalConfiguration.KEY_RESPONSE_KEY,
							TerminalConfiguration.RESPONSE_UNPACK_DAY_CHARGE_UIM);
					intent.putExtra(
							TerminalConfiguration.TITLE_COLUMN,
							TerminalConfiguration.TITLE_COLUMN_UNPACK_DAY_CHARGE_UIM);
					intent.putExtra(
							TerminalConfiguration.KEY_COLUNM_KPI_CODE,
							TerminalConfiguration.COLUMN_KPI_CODE_UNPACK_DAY_CHARGE_UIM);
					break;
				case UNPACK_MONTH_ACTIVITY:
					title_big = "拆包月";
					menuCode = TerminalConfiguration.KEY_MENU_CODE_UNPACK_MONTH;
					dimkey = businessdao.getMenuComplexDimKey(businessdao
							.getMenuInfo(menuCode));
					param.put(TerminalConfiguration.KEY_KPI_CODES,
							TerminalConfiguration.KPI_UNPACK_MONTH_CHARGE_UIM);
					param.put(TerminalConfiguration.KEY_COLUMN_NAME,
							TerminalConfiguration.CURMONTH_VALUE); // 列
					param.put(TerminalConfiguration.KEY_DATATABLE,
							businessdao.getMenuColDataTable(menuCode));
					param.put(TerminalConfiguration.KEY_DIM_KEY,
							dimkey.getComplexDimKeyString());
					param.put(TerminalConfiguration.KEY_ISEXPAND, "1");
					param.put(TerminalConfiguration.KEY_PART3TODATA, "1");
					action = "palmBiTermDataAction_manyKpiManyAreaManyTimeDataQuery.do";
					intent.putExtra(
							TerminalConfiguration.KEY_RESPONSE_KEY,
							TerminalConfiguration.RESPONSE_UNPACK_MONTH_CHARGE_UIM);
					intent.putExtra(
							TerminalConfiguration.TITLE_COLUMN,
							TerminalConfiguration.TITLE_COLUMN_UNPACK_MONTH_CHARGE_UIM);
					intent.putExtra(
							TerminalConfiguration.KEY_COLUNM_KPI_CODE,
							TerminalConfiguration.COLUMN_KPI_CODE_UNPACK_MONTH_CHARGE_UIM);
					break;
				}

				intent.putExtra(TerminalConfiguration.TITLE_BIG, title_big);
				intent.putExtra(TerminalConfiguration.KEY_ACTIVITY_ENUM,
						mTerminalActivityEnum);
				intent.putExtra(TerminalConfiguration.KEY_ACTION, action);
				intent.putExtra(TerminalConfiguration.KEY_MAP,
						(Serializable) param);
				// context.startActivity(intent);
			}
		});
	}

	private void setDefaultData() {
		this.titleL = "省内窜出";
		this.titleR = "疑似省际窜出";

		this.lrow2value = Constant.NULL_VALUE_INSTEAD;
		this.lrow2unit = "";

		this.rrow2value = Constant.NULL_VALUE_INSTEAD;
		this.rrow2unit = "";
	}

	@Override
	public void setData(JSONObject data) {
		super.setData(data);
		switch (mTerminalActivityEnum) {
		case FG_MONTH_ACTIVITY:
			setFG_MONTH_DATA();
			break;
		case UNPACK_DAY_ACTIVITY:
			setUNPACK_DAY_DATA();
			break;
		case UNPACK_MONTH_ACTIVITY:
			setUNPACK_MONTH_DATA();
			break;
		}
	}

	private void setFG_MONTH_DATA() {
		this.setDefaultData();
		this.kpi_statistics = "5690|5700";

		String sqlString = "select kpi_code as kpiCode,curmonth_value as value"
				+ " from  TERMINAL_MONTH_TERM where kpi_code in ('"
				+ TerminalConfiguration.KPI_5690 + "','"
				+ TerminalConfiguration.KPI_5700 + "') and op_time = ? limit 2";

		List<Map<String, String>> resultList = new SQLHelper().queryForList(
				sqlString, new String[] { mTerminalActivityEnum.getOPtime() });

		this.lrow2value = this.rrow2value = Constant.NULL_VALUE_INSTEAD;
		this.lrow2unit = this.rrow2unit = Constant.TERMINAL_SALE_DEFAULT_UNIT;

		ColumnDisplyInfo cdi = null;
		for (int i = 0; i < resultList.size(); i++) {

			Map<String, String> tempMap = resultList.get(i);

			if (TerminalConfiguration.KPI_5690.equals(tempMap.get("kpiCode"))) {
				cdi = ColumnDataFilter.getInstance().filterWithDefaultValue(
						Constant.TERMINAL_SALE_DEFAULT_RULE,
						Constant.TERMINAL_SALE_DEFAULT_UNIT,
						tempMap.get("value"));
				this.lrow2value = cdi.getValue();
				this.lrow2unit = cdi.getUnit();
			}

			if (TerminalConfiguration.KPI_5700.equals(tempMap.get("kpiCode"))) {
				cdi = ColumnDataFilter.getInstance().filterWithDefaultValue(
						Constant.TERMINAL_SALE_DEFAULT_RULE,
						Constant.TERMINAL_SALE_DEFAULT_UNIT,
						tempMap.get("value"));
				this.rrow2value = cdi.getValue();
				this.rrow2unit = cdi.getUnit();
			}
		}
	}

	private void setUNPACK_DAY_DATA() {
		this.titleL = "机卡分离量";
		this.titleR = "机卡分离率";
		this.kpi_statistics = "4910|4920";

		String sqlString = "select kpi_code as kpiCode,curday_value as value"
				+ " from  TERMINAL_DAILY_CB where kpi_code in ('"
				+ TerminalConfiguration.KPI_4910 + "','"
				+ TerminalConfiguration.KPI_4920 + "') and op_time = ?";

		// List<Map<String, String>> resultList = new SQLHelper().queryForList(
		// sqlString, new String[] { mTerminalActivityEnum.getOPtime() });
		List<Map<String, String>> resultList = getTermData("data5");

		this.lrow2value = this.rrow2value = Constant.NULL_VALUE_INSTEAD;
		this.lrow2unit = this.rrow2unit = Constant.TERMINAL_SALE_DEFAULT_UNIT;

		for (int i = 0; i < resultList.size(); i++) {
			Map<String, String> tempMap = resultList.get(i);

			if (TerminalConfiguration.KPI_4910.equals(tempMap.get("kpiCode"))) {
				ColumnDisplyInfo cdi = ColumnDataFilter.getInstance()
						.filterWithDefaultValue(
								Constant.TERMINAL_SALE_DEFAULT_RULE,
								Constant.TERMINAL_SALE_DEFAULT_UNIT,
								tempMap.get("value"));
				this.lrow2value = cdi.getValue();
				this.lrow2unit = cdi.getUnit();

			}

			if (TerminalConfiguration.KPI_4920.equals(tempMap.get("kpiCode"))) {
				ColumnDisplyInfo cdi = ColumnDataFilter.getInstance()
						.filterWithDefaultValue(
								Constant.TERMINAL_SALE_PERCENT_RULE,
								Constant.TERMINAL_SALE_PERCENT_UNIT,
								tempMap.get("value"));
				this.rrow2value = cdi.getValue();
				this.rrow2unit = cdi.getUnit();
			}
		}
	}

	private void setUNPACK_MONTH_DATA() {
		this.titleL = "机卡分离量";
		this.titleR = "机卡分离率";
		this.kpi_statistics = "5450|5460";

		String sqlString = "select kpi_code as kpiCode,curmonth_value as value"
				+ " from  TERMINAL_MONTH_TERM where kpi_code in ('"
				+ TerminalConfiguration.KPI_5450 + "','"
				+ TerminalConfiguration.KPI_5460 + "') and op_time = ?";

		// List<Map<String, String>> resultList = new SQLHelper().queryForList(
		// sqlString, new String[] { mTerminalActivityEnum.getOPtime() });
		List<Map<String, String>> resultList = getTermData("data5");

		this.lrow2value = this.rrow2value = Constant.NULL_VALUE_INSTEAD;
		this.lrow2unit = this.rrow2unit = Constant.TERMINAL_SALE_DEFAULT_UNIT;

		for (int i = 0; i < resultList.size(); i++) {
			Map<String, String> tempMap = resultList.get(i);

			if (TerminalConfiguration.KPI_5450.equals(tempMap.get("kpiCode"))) {
				ColumnDisplyInfo cdi = ColumnDataFilter.getInstance()
						.filterWithDefaultValue(
								Constant.TERMINAL_SALE_DEFAULT_RULE,
								Constant.TERMINAL_SALE_DEFAULT_UNIT,
								tempMap.get("value"));
				this.lrow2value = cdi.getValue();
				this.lrow2unit = cdi.getUnit();
			}

			if (TerminalConfiguration.KPI_5460.equals(tempMap.get("kpiCode"))) {
				ColumnDisplyInfo cdi = ColumnDataFilter.getInstance()
						.filterWithDefaultValue(
								Constant.TERMINAL_SALE_PERCENT_RULE,
								Constant.TERMINAL_SALE_PERCENT_UNIT,
								tempMap.get("value"));
				this.rrow2value = cdi.getValue();
				this.rrow2unit = cdi.getUnit();

			}
		}
	}

	@Override
	public void updateView() {
		// TODO Auto-generated method stub
		this.titleLTV.setText(titleL);
		this.titleRTV.setText(titleR);

		this.lrow2valueTV.setTextColor(this.context.getResources().getColor(
				R.color.green));
		this.lrow2valueTV.setText(lrow2value);
		// this.lrow2unitTV.setTextColor(this.context.getResources().getColor(R.color.green));
		this.lrow2unitTV.setText(lrow2unit);

		this.rrow2valueTV.setTextColor(this.context.getResources().getColor(
				R.color.red));
		this.rrow2valueTV.setText(rrow2value);
		// this.rrow2unitTV.setTextColor(this.context.getResources().getColor(R.color.red));
		this.rrow2unitTV.setText(rrow2unit);

	}

	@Override
	public void dispatchView() {
		// TODO Auto-generated method stub
		// ============第一列
		View b1 = (View) this.mView.findViewById(R.id.id_col1);
		this.titleLTV = (TextView) b1.findViewById(R.id.id_title);
		View itemL1 = b1.findViewById(R.id.id_item1);

		this.lrow2valueTV = (TextView) itemL1.findViewById(R.id.id_value);
		this.lrow2unitTV = (TextView) itemL1.findViewById(R.id.id_unit);

		// ============第二列
		View b2 = (View) this.mView.findViewById(R.id.id_col2);

		this.titleRTV = (TextView) b2.findViewById(R.id.id_title);
		View itemR1 = b2.findViewById(R.id.id_item1);

		this.rrow2valueTV = (TextView) itemR1.findViewById(R.id.id_value);
		this.rrow2unitTV = (TextView) itemR1.findViewById(R.id.id_unit);
	}
}
