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
import android.widget.ImageView;
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
public class ModuleBasicViewUnpackRow2Branch extends ViewBranch {

	private TextView titleLTV;
	private String titleL;
	private TextView titleRTV;
	private String titleR;

	private TextView row2valueTV;
	private TextView row2unitTV;

	private TextView row3markTV;
	private TextView row3valueTV;
	private ImageView row3iconIV;

	private String row2value;
	private String row2unit;

	private String row3mark;
	private String row3value;
	private int row3icon;

	private TextView leftValueTV;
	private TextView leftValueUnitTV;

	private String leftValue;
	private String leftUnit;

	// config
	private BusinessDao businessdao = new BusinessDao();
	private Map<String, String> userinfo = businessdao.getUserInfo(); // 区域用户信息

	/**
	 * @param c
	 * @param tae
	 */
	public ModuleBasicViewUnpackRow2Branch(Context c, TerminalActivityEnum tae) {
		super(c, tae);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setViewListener() {
		this.mView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

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
				context.startActivity(intent);
			}
		});
	}

	@Override
	public void setData(JSONObject data) {
		super.setData(data);
		this.titleL = "话费返还量";
		this.titleR = "机卡分离量";
		this.row3mark = "分离率:";

		if (mTerminalActivityEnum == TerminalActivityEnum.UNPACK_DAY_ACTIVITY) {
			this.kpi_statistics = "4910|4920|4930";
			String sqlString = "select kpi_code as kpiCode,curday_value as value"
					+ " from  TERMINAL_DAILY_CB where kpi_code in ('"
					+ TerminalConfiguration.KPI_4910
					+ "','"
					+ TerminalConfiguration.KPI_4920
					+ "','"
					+ TerminalConfiguration.KPI_4930 + "') and op_time = ?";

			List<Map<String, String>> resultList = new SQLHelper()
					.queryForList(sqlString,
							new String[] { mTerminalActivityEnum.getOPtime() });

			if (resultList == null || resultList.size() == 0) {

				return;
			}
			for (int i = 0; i < resultList.size(); i++) {
				Map<String, String> tempMap = resultList.get(i);
				if (TerminalConfiguration.KPI_4930.equals(tempMap
						.get("kpiCode"))) {
					ColumnDisplyInfo cdi = ColumnDataFilter.getInstance()
							.filterWithDefaultValue(
									Constant.TERMINAL_SALE_DEFAULT_RULE,
									Constant.TERMINAL_SALE_DEFAULT_UNIT,
									tempMap.get("value"));
					this.leftValue = cdi.getValue();
					this.leftUnit = cdi.getUnit();

				}

				if (TerminalConfiguration.KPI_4910.equals(tempMap
						.get("kpiCode"))) {
					ColumnDisplyInfo cdi = ColumnDataFilter.getInstance()
							.filterWithDefaultValue(
									Constant.TERMINAL_SALE_DEFAULT_RULE,
									Constant.TERMINAL_SALE_DEFAULT_UNIT,
									tempMap.get("value"));
					this.row2value = cdi.getValue();
					this.row2unit = cdi.getUnit();

				}

				if (TerminalConfiguration.KPI_4920.equals(tempMap
						.get("kpiCode"))) {
					ColumnDisplyInfo cdi = ColumnDataFilter.getInstance()
							.filterWithDefaultValue(
									Constant.TERMINAL_SALE_PERCENT_RULE,
									Constant.TERMINAL_SALE_PERCENT_UNIT,
									tempMap.get("value"));
					this.row3value = cdi.getValue() + cdi.getUnit();

				}
			}

		} else if (mTerminalActivityEnum == TerminalActivityEnum.UNPACK_MONTH_ACTIVITY) {
			this.kpi_statistics = "5450|5460|5470";
			String sqlString = "select kpi_code as kpiCode,curmonth_value as value"
					+ " from  TERMINAL_MONTH_TERM where kpi_code in ('"
					+ TerminalConfiguration.KPI_5450
					+ "','"
					+ TerminalConfiguration.KPI_5460
					+ "','"
					+ TerminalConfiguration.KPI_5470 + "') and op_time = ?";

			List<Map<String, String>> resultList = new SQLHelper()
					.queryForList(sqlString,
							new String[] { mTerminalActivityEnum.getOPtime() });

			for (int i = 0; i < resultList.size(); i++) {
				Map<String, String> tempMap = resultList.get(i);
				if (TerminalConfiguration.KPI_5470.equals(tempMap
						.get("kpiCode"))) {
					ColumnDisplyInfo cdi = ColumnDataFilter.getInstance()
							.filterWithDefaultValue(
									Constant.TERMINAL_SALE_DEFAULT_RULE,
									Constant.TERMINAL_SALE_DEFAULT_UNIT,
									tempMap.get("value"));
					this.leftValue = cdi.getValue();
					this.leftUnit = cdi.getUnit();

				}

				if (TerminalConfiguration.KPI_5450.equals(tempMap
						.get("kpiCode"))) {
					ColumnDisplyInfo cdi = ColumnDataFilter.getInstance()
							.filterWithDefaultValue(
									Constant.TERMINAL_SALE_DEFAULT_RULE,
									Constant.TERMINAL_SALE_DEFAULT_UNIT,
									tempMap.get("value"));
					this.row2value = cdi.getValue();
					this.row2unit = cdi.getUnit();

				}

				if (TerminalConfiguration.KPI_5460.equals(tempMap
						.get("kpiCode"))) {
					ColumnDisplyInfo cdi = ColumnDataFilter.getInstance()
							.filterWithDefaultValue(
									Constant.TERMINAL_SALE_PERCENT_RULE,
									Constant.TERMINAL_SALE_PERCENT_UNIT,
									tempMap.get("value"));
					this.row3value = cdi.getValue() + cdi.getUnit();

				}
			}
		}
	}

	@Override
	public void updateView() {
		// TODO Auto-generated method stub
		this.titleLTV.setText(titleL);
		this.titleRTV.setText(titleR);

		this.row2valueTV.setTextColor(this.context.getResources().getColor(
				R.color.red));
		this.row2valueTV.setText(row2value);
		this.row2unitTV.setText(row2unit);

		this.row3markTV.setText(row3mark);
		this.row3valueTV.setTextColor(this.context.getResources().getColor(
				R.color.green));
		this.row3valueTV.setText(row3value);
		this.row3iconIV.setBackgroundResource(row3icon);

		this.leftValueTV.setTextColor(this.context.getResources().getColor(
				R.color.green));
		this.leftValueTV.setText(leftValue);
		this.leftValueUnitTV.setText(leftUnit);

	}

	@Override
	public void dispatchView() {
		// TODO Auto-generated method stub
		// ============第一列
		View b1 = (View) this.mView.findViewById(R.id.id_col1);
		this.titleLTV = (TextView) b1.findViewById(R.id.id_title);
		View itemL1 = b1.findViewById(R.id.id_item1);
		this.leftValueTV = (TextView) itemL1.findViewById(R.id.id_value);
		this.leftValueUnitTV = (TextView) itemL1.findViewById(R.id.id_unit);

		// ============第二列
		View b2 = (View) this.mView.findViewById(R.id.id_col2);

		this.titleRTV = (TextView) b2.findViewById(R.id.id_title);
		View item1 = b2.findViewById(R.id.id_item1);
		View item0 = b2.findViewById(R.id.id_item0);

		this.row2valueTV = (TextView) item1.findViewById(R.id.id_value);
		this.row2unitTV = (TextView) item1.findViewById(R.id.id_unit);

		this.row3markTV = (TextView) item0.findViewById(R.id.id_mark);
		this.row3valueTV = (TextView) item0.findViewById(R.id.id_value);
		this.row3iconIV = (ImageView) item0.findViewById(R.id.id_icon);

	}

}
