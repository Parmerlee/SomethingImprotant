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

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter.ColumnDisplyInfo;
import com.bonc.mobile.hbmclient.data.ComplexDimInfo;
import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;
import com.bonc.mobile.hbmclient.terminal.subactivity.DaySaleCountStaticsActivity;
import com.bonc.mobile.hbmclient.util.NumberUtil;

/**
 * @author liweigao
 * 
 */
public class ModuleBasicViewFGH2Branch extends ViewBranch {

	private TextView titleLTV;
	private String titleL;
	private TextView titleRTV;
	private String titleR;

	private TextView lrow2valueTV;
	private TextView lrow2unitTV;

	private TextView lrow3markTV;
	private TextView lrow3valueTV;
	private ImageView lrow3iconIV;

	private String lrow2value = Constant.NULL_VALUE_INSTEAD;
	private int lrow2valueColor;
	private String lrow2unit = "万台";
	private int lrow2unitColor;

	private String lrow3mark;
	private String lrow3value = Constant.NULL_VALUE_INSTEAD;
	private int lrow3valueColor;
	private int lrow3icon;

	private TextView rrow2valueTV;
	private TextView rrow2unitTV;

	private TextView rrow3markTV;
	private TextView rrow3valueTV;
	private ImageView rrow3iconIV;

	private String rrow2value = Constant.NULL_VALUE_INSTEAD;
	private int rrow2valueColor;
	private String rrow2unit = "万台";
	private int rrow2unitColor;

	private String rrow3mark;
	private String rrow3value = Constant.NULL_VALUE_INSTEAD;
	private int rrow3valueColor;
	private int rrow3icon;

	// config
	private BusinessDao businessdao = new BusinessDao();
	private Map<String, String> userinfo = businessdao.getUserInfo(); // 区域用户信息

	private int position;
	public final int UP = 0;
	public final int DOWN = 1;

	/**
	 * @param c
	 * @param tae
	 */
	public ModuleBasicViewFGH2Branch(Context c, TerminalActivityEnum tae) {
		super(c, tae);
		// TODO Auto-generated constructor stub
	}

	public void setPosition(int position) {
		this.position = position;
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
				case FG_MONTH_ACTIVITY:
					if (UP == position) {
						title_big = "窜货月";
						menuCode = TerminalConfiguration.KEY_MENU_CODE_FG_MONTH;
						dimkey = businessdao.getMenuComplexDimKey(businessdao
								.getMenuInfo(menuCode));
						param.put(
								TerminalConfiguration.KEY_KPI_CODES,
								TerminalConfiguration.KPI_FG_MONTH_COUNT_ACTIVATE);
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
								TerminalConfiguration.RESPONSE_FG_MONTH_COUNT_ACTIVATE);
						intent.putExtra(
								TerminalConfiguration.TITLE_COLUMN,
								TerminalConfiguration.TITLE_COLUMN_FG_MONTH_COUNT_ACTIVATE);
						intent.putExtra(
								TerminalConfiguration.KEY_COLUNM_KPI_CODE,
								TerminalConfiguration.COLUMN_KPI_CODE_FG_MONTH_COUNT_ACTIVATE);
					} else {
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
						intent.putExtra(
								TerminalConfiguration.KEY_COLUNM_KPI_CODE,
								TerminalConfiguration.COLUMN_KPI_CODE_MONTH_FG);
						intent.putExtra(TerminalConfiguration.TITLE_COLUMN,
								TerminalConfiguration.TITLE_COLUMN_FG_MONTH_FG);
					}
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

	private void setDefaultData() {
		this.titleL = "当月激活量";
		this.titleR = "疑似省际窜出";
		this.lrow3mark = "激活率:";
		this.rrow3mark = "窜出率:";

		// 设置默认值
		this.lrow2value = Constant.NULL_VALUE_INSTEAD;
		this.lrow2unit = "";
		this.lrow3value = Constant.NULL_VALUE_INSTEAD;
		this.rrow2value = Constant.NULL_VALUE_INSTEAD;
		this.rrow2unit = "";
		this.rrow3value = Constant.NULL_VALUE_INSTEAD;
	}

	@Override
	public void setData(JSONObject data) {
		super.setData(data);
		switch (mTerminalActivityEnum) {
		case FG_MONTH_ACTIVITY:
			if (UP == this.position) {
				setFG_MONTH_UP_DATA();
			} else if (DOWN == this.position) {
				setFG_MONTH_DOWN_DATA();
			}
			break;
		}
	}

	private void setFG_MONTH_DOWN_DATA() {
		this.titleL = "省内窜入";
		this.titleR = "省内窜出";
		this.lrow3mark = "窜入率:";
		this.rrow3mark = "窜出率:";
		this.kpi_statistics = "5690|5930|6010|6020";

		String sqlString = "select kpi_code as kpiCode,curmonth_value as value"
				+ " from  TERMINAL_MONTH_TERM where kpi_code in ('"
				+ TerminalConfiguration.KPI_5690 + "','"
				+ TerminalConfiguration.KPI_5930 + "','"
				+ TerminalConfiguration.KPI_6010 + "','"
				+ TerminalConfiguration.KPI_6020 + "') and op_time = ? limit 4";

		// List<Map<String, String>> resultList = new SQLHelper().queryForList(
		// sqlString, new String[] { mTerminalActivityEnum.getOPtime() });
		List<Map<String, String>> resultList = getTermData("data5");

		this.lrow2value = this.rrow2value = this.lrow3value = this.rrow3value = Constant.NULL_VALUE_INSTEAD;
		this.lrow2unit = this.rrow2unit = Constant.TERMINAL_SALE_DEFAULT_UNIT;
		this.lrow3value = this.rrow3value = Constant.NULL_VALUE_INSTEAD;

		ColumnDisplyInfo cdi = null;
		for (int i = 0; i < resultList.size(); i++) {

			Map<String, String> tempMap = resultList.get(i);

			if (TerminalConfiguration.KPI_6010.equals(tempMap.get("kpiCode"))) {
				cdi = ColumnDataFilter.getInstance().filterWithDefaultValue(
						Constant.TERMINAL_SALE_DEFAULT_RULE,
						Constant.TERMINAL_SALE_DEFAULT_UNIT,
						tempMap.get("value"));
				this.lrow2value = cdi.getValue();
				this.lrow2unit = cdi.getUnit();
			}
			if (TerminalConfiguration.KPI_6020.equals(tempMap.get("kpiCode"))) {
				cdi = ColumnDataFilter.getInstance().filterWithDefaultValue(
						Constant.TERMINAL_SALE_PERCENT_RULE,
						Constant.TERMINAL_SALE_PERCENT_UNIT,
						tempMap.get("value"));
				this.lrow3value = cdi.getValue() + "%";
			}

			if (TerminalConfiguration.KPI_5690.equals(tempMap.get("kpiCode"))) {
				cdi = ColumnDataFilter.getInstance().filterWithDefaultValue(
						Constant.TERMINAL_SALE_DEFAULT_RULE,
						Constant.TERMINAL_SALE_DEFAULT_UNIT,
						tempMap.get("value"));
				this.rrow2value = cdi.getValue();
				this.rrow2valueColor = this.context.getResources().getColor(
						R.color.red);
				this.rrow2unit = cdi.getUnit();
			}
			if (TerminalConfiguration.KPI_5930.equals(tempMap.get("kpiCode"))) {
				cdi = ColumnDataFilter.getInstance().filterWithDefaultValue(
						Constant.TERMINAL_SALE_PERCENT_RULE,
						Constant.TERMINAL_SALE_PERCENT_UNIT,
						tempMap.get("value"));
				this.rrow3value = cdi.getValue() + "%";
			}
		}
	}

	private void setFG_MONTH_UP_DATA() {
		this.setDefaultData();
		this.kpi_statistics = "5700|6030|5720|5980";

		String sqlString = "select kpi_code as kpiCode,curmonth_value as value,"
				+ " round(curmonth_value_dr*100,2)||'%' as zb,"
				+ " round(cm_yoy*100,1) as tb"
				+ " from  TERMINAL_MONTH_TERM where kpi_code in ('"
				+ TerminalConfiguration.KPI_5700
				+ "','"
				+ TerminalConfiguration.KPI_6030
				+ "','"
				+ TerminalConfiguration.KPI_5720
				+ "','"
				+ TerminalConfiguration.KPI_5980 + "') and op_time = ?";
		// List<Map<String, String>> resultList = new SQLHelper().queryForList(
		// sqlString, new String[] { mTerminalActivityEnum.getOPtime() });
		List<Map<String, String>> resultList = getTermData("data4");

		for (int i = 0; i < resultList.size(); i++) {

			Map<String, String> tempMap = resultList.get(i);

			if (TerminalConfiguration.KPI_5700.equals(tempMap.get("kpiCode"))) {
				String value = tempMap.get("value");
				if ("".equals(value) || value == null) {
					this.rrow2value = Constant.NULL_VALUE_INSTEAD;
					this.rrow2unit = Constant.NULL_VALUE_INSTEAD;
				} else {
					ColumnDisplyInfo cdi = ColumnDataFilter.getInstance()
							.filterWithDefaultValue(
									Constant.TERMINAL_SALE_DEFAULT_RULE,
									Constant.TERMINAL_SALE_DEFAULT_UNIT, value);

					this.rrow2value = cdi.getValue();
					this.rrow2valueColor = this.context.getResources()
							.getColor(R.color.red);
					this.rrow2unit = cdi.getUnit();
				}
			}

			if (TerminalConfiguration.KPI_6030.equals(tempMap.get("kpiCode"))) {
				String value = tempMap.get("value");
				if ("".equals(value) || value == null) {
					this.rrow3value = "--%";
				} else {
					ColumnDisplyInfo cdi = ColumnDataFilter.getInstance()
							.filterWithDefaultValue(
									Constant.TERMINAL_SALE_PERCENT_RULE,
									Constant.TERMINAL_SALE_PERCENT_UNIT, value);
					this.rrow3value = cdi.getValue() + "%";
				}
			}

			if (TerminalConfiguration.KPI_5720.equals(tempMap.get("kpiCode"))) {
				ColumnDisplyInfo cdi = ColumnDataFilter.getInstance()
						.filterWithDefaultValue(
								Constant.TERMINAL_SALE_DEFAULT_RULE,
								Constant.TERMINAL_SALE_DEFAULT_UNIT,
								tempMap.get("value"));
				this.lrow2value = cdi.getValue();
				this.lrow2unit = cdi.getUnit();

				if (NumberUtil.changeToDouble(tempMap.get("tb")) > 0) {
					this.lrow2valueColor = this.context.getResources()
							.getColor(R.color.red);
					this.lrow2unitColor = this.context.getResources().getColor(
							R.color.red);
				} else {
					this.lrow2valueColor = this.context.getResources()
							.getColor(R.color.green);
					this.lrow2unitColor = this.context.getResources().getColor(
							R.color.green);
				}
			}

			if (TerminalConfiguration.KPI_5980.equals(tempMap.get("kpiCode"))) {
				String value = tempMap.get("value");
				if ("".equals(value) || value == null) {
					this.lrow3value = "--%";
				} else {
					ColumnDisplyInfo cdi = ColumnDataFilter.getInstance()
							.filterWithDefaultValue(
									Constant.TERMINAL_SALE_PERCENT_RULE,
									Constant.TERMINAL_SALE_PERCENT_UNIT, value);
					this.lrow3value = cdi.getValue() + "%";
				}
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
		this.lrow2unitTV.setText(lrow2unit);
		this.lrow3markTV.setText(lrow3mark);
		this.lrow3valueTV.setTextColor(this.context.getResources().getColor(
				R.color.green));
		this.lrow3valueTV.setText(lrow3value);
		this.lrow3iconIV.setBackgroundResource(lrow3icon);

		this.rrow2valueTV.setTextColor(rrow2valueColor);
		// this.rrow2unitTV.setTextColor(rrow2unitColor);
		this.rrow2valueTV.setText(rrow2value);
		this.rrow2unitTV.setText(rrow2unit);
		this.rrow3markTV.setText(rrow3mark);
		this.rrow3valueTV.setTextColor(this.context.getResources().getColor(
				R.color.red));
		this.rrow3valueTV.setText(rrow3value);
		switch (mTerminalActivityEnum) {
		case FG_MONTH_ACTIVITY:
			this.rrow3iconIV.setVisibility(View.GONE);
			break;
		default:
			this.rrow3iconIV.setBackgroundResource(rrow3icon);
			break;
		}

	}

	@Override
	public void dispatchView() {
		// TODO Auto-generated method stub
		// ============第一列
		View b1 = (View) this.mView.findViewById(R.id.id_col1);
		this.titleLTV = (TextView) b1.findViewById(R.id.id_title);
		View itemL1 = b1.findViewById(R.id.id_item1);
		View itemL0 = b1.findViewById(R.id.id_item0);

		this.lrow2valueTV = (TextView) itemL1.findViewById(R.id.id_value);
		this.lrow2unitTV = (TextView) itemL1.findViewById(R.id.id_unit);

		this.lrow3markTV = (TextView) itemL0.findViewById(R.id.id_mark);
		this.lrow3valueTV = (TextView) itemL0.findViewById(R.id.id_value);
		this.lrow3iconIV = (ImageView) itemL0.findViewById(R.id.id_icon);

		// ============第二列
		View b2 = (View) this.mView.findViewById(R.id.id_col2);

		this.titleRTV = (TextView) b2.findViewById(R.id.id_title);
		View itemR1 = b2.findViewById(R.id.id_item1);
		View itemR0 = b2.findViewById(R.id.id_item0);

		this.rrow2valueTV = (TextView) itemR1.findViewById(R.id.id_value);
		this.rrow2unitTV = (TextView) itemR1.findViewById(R.id.id_unit);

		this.rrow3markTV = (TextView) itemR0.findViewById(R.id.id_mark);
		this.rrow3valueTV = (TextView) itemR0.findViewById(R.id.id_value);
		this.rrow3iconIV = (ImageView) itemR0.findViewById(R.id.id_icon);

	}

}
