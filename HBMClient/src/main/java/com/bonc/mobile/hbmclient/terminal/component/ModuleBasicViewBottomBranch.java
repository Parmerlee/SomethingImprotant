package com.bonc.mobile.hbmclient.terminal.component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter.ColumnDisplyInfo;
import com.bonc.mobile.hbmclient.data.ComplexDimInfo;
import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;
import com.bonc.mobile.hbmclient.terminal.subactivity.DaySaleCountStaticsActivity;
import com.bonc.mobile.hbmclient.util.HttpUtil;

/**
 * @author liweigao
 * 
 */
public class ModuleBasicViewBottomBranch extends ViewBranch {

	private TextView titleTV;
	private String title;

	private TextView titleSmallTV;
	private String titleSmall;

	private TextView row2valueTV;
	private TextView row2unitTV;

	private TextView row3markTV;
	private TextView row3valueTV;
	private ImageView row3iconIV;

	private String row2value;
	private int row2valueColor;
	private String row2unit;
	private int row2unitColor;

	private String row3mark;
	private String row3value;
	private int row3valueColor;
	private int row3icon;

	private TextView leftCityTV;
	private TextView leftRankTV;
	private TextView leftValueTV;
	private TextView leftValueUnitTV;
	private TextView leftScaleKeyTV;

	private String leftCity;
	private String leftRank;
	private String leftValue;
	private String leftUnit;
	private String leftScaleKey;

	private TextView rightCityTV;
	private TextView rightRankTV;
	private TextView rightValueTV;
	private TextView rightValueUnitTV;
	private TextView rightScaleKeyTV;

	private String rightCity;
	private String rightRank;
	private String rightValue;
	private String rightUnit;
	private String rightScaleKey;

	// config
	private BusinessDao businessdao = new BusinessDao();
	private Map<String, String> userinfo = businessdao.getUserInfo(); // 区域用户信息

	/**
	 * @param c
	 * @param tae
	 */
	public ModuleBasicViewBottomBranch(Context c, TerminalActivityEnum tae) {
		super(c, tae);
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
						mTerminalActivityEnum.getAreaCode()); // 地区id
				String action = null;
				String title_big = null;
				String menuCode = null;
				ComplexDimInfo dimkey = null;

				switch (mTerminalActivityEnum) {
				case UNSALE_DAY_ACTIVITY:
					title_big = "滞销日";
					menuCode = TerminalConfiguration.KEY_MENU_CODE_UNSALE_DAY;
					dimkey = businessdao.getMenuComplexDimKey(businessdao
							.getMenuInfo(menuCode));
					param.put(TerminalConfiguration.KEY_KPI_CODES,
							TerminalConfiguration.KPI_UNSALE_DAY_COUNT);
					param.put(TerminalConfiguration.KEY_COLUMN_NAME,
							TerminalConfiguration.COLUMN_UNSALE_DAY_COUNT); // 列
					param.put(TerminalConfiguration.KEY_DATATABLE,
							businessdao.getMenuColDataTable(menuCode));
					param.put(TerminalConfiguration.KEY_DIM_KEY,
							dimkey.getComplexDimKeyString());
					param.put(TerminalConfiguration.KEY_ISEXPAND, "1");
					param.put(TerminalConfiguration.KEY_PART3TODATA, "1");
					action = "palmBiTermDataAction_manyKpiManyAreaManyTimeDataQuery.do";
					intent.putExtra(TerminalConfiguration.KEY_RESPONSE_KEY,
							TerminalConfiguration.RESPONSE_UNSALE_DAY_COUNT);
					intent.putExtra(TerminalConfiguration.TITLE_COLUMN,
							TerminalConfiguration.TITLE_COLUMN_UNSALE_DAY_COUNT);
					intent.putExtra(
							TerminalConfiguration.KEY_COLUNM_KPI_CODE,
							TerminalConfiguration.COLUMN_KPI_CODE_UNSALE_DAY_COUNT);
					break;
				case UNSALE_MONTH_ACTIVITY:
					title_big = "滞销月";
					menuCode = TerminalConfiguration.KEY_MENU_CODE_UNSALE_MONTH;
					dimkey = businessdao.getMenuComplexDimKey(businessdao
							.getMenuInfo(menuCode));
					param.put(TerminalConfiguration.KEY_KPI_CODES,
							TerminalConfiguration.KPI_UNSALE_MONTH_COUNT);
					param.put(TerminalConfiguration.KEY_COLUMN_NAME,
							TerminalConfiguration.COLUMN_UNSALE_MONTH_COUNT); // 列
					param.put(TerminalConfiguration.KEY_DATATABLE,
							businessdao.getMenuColDataTable(menuCode));
					param.put(TerminalConfiguration.KEY_DIM_KEY,
							dimkey.getComplexDimKeyString());
					param.put(TerminalConfiguration.KEY_ISEXPAND, "1");
					param.put(TerminalConfiguration.KEY_PART3TODATA, "1");
					action = "palmBiTermDataAction_manyKpiManyAreaManyTimeDataQuery.do";
					intent.putExtra(TerminalConfiguration.KEY_RESPONSE_KEY,
							TerminalConfiguration.RESPONSE_UNSALE_MONTH_COUNT);
					intent.putExtra(
							TerminalConfiguration.TITLE_COLUMN,
							TerminalConfiguration.TITLE_COLUMN_UNSALE_MONTH_COUNT);
					intent.putExtra(
							TerminalConfiguration.KEY_COLUNM_KPI_CODE,
							TerminalConfiguration.COLUMN_KPI_CODE_UNSALE_MONTH_COUN);
					break;
				case UNPACK_DAY_ACTIVITY:
					title_big = "拆包日";
					menuCode = TerminalConfiguration.KEY_MENU_CODE_UNPACK_DAY;
					dimkey = businessdao.getMenuComplexDimKey(businessdao
							.getMenuInfo(menuCode));
					param.put(TerminalConfiguration.KEY_KPI_CODES,
							TerminalConfiguration.KPI_UNPACK_DAY_COUNT);
					param.put(TerminalConfiguration.KEY_COLUMN_NAME,
							TerminalConfiguration.COLUMN_UNPACK_DAY_COUNT); // 列
					param.put(TerminalConfiguration.KEY_DATATABLE,
							businessdao.getMenuColDataTable(menuCode));
					param.put(TerminalConfiguration.KEY_DIM_KEY,
							dimkey.getComplexDimKeyString());
					param.put(TerminalConfiguration.KEY_ISEXPAND, "1");
					param.put(TerminalConfiguration.KEY_PART3TODATA, "1");
					action = "palmBiTermDataAction_manyKpiManyAreaManyTimeDataQuery.do";
					intent.putExtra(TerminalConfiguration.KEY_RESPONSE_KEY,
							TerminalConfiguration.RESPONSE_UNPACK_DAY_COUNT);
					intent.putExtra(TerminalConfiguration.TITLE_COLUMN,
							TerminalConfiguration.TITLE_COLUMN_UNPACK_DAY_COUNT);
					intent.putExtra(
							TerminalConfiguration.KEY_COLUNM_KPI_CODE,
							TerminalConfiguration.COLUMN_KPI_CODE_UNPACK_DAY_COUNT);
					break;
				case UNPACK_MONTH_ACTIVITY:
					title_big = "拆包月";
					menuCode = TerminalConfiguration.KEY_MENU_CODE_UNPACK_MONTH;
					dimkey = businessdao.getMenuComplexDimKey(businessdao
							.getMenuInfo(menuCode));
					param.put(TerminalConfiguration.KEY_KPI_CODES,
							TerminalConfiguration.KPI_UNPACK_MONTH_COUNT);
					param.put(TerminalConfiguration.KEY_COLUMN_NAME,
							TerminalConfiguration.COLUMN_UNPACK_MONTH_COUNT); // 列
					param.put(TerminalConfiguration.KEY_DATATABLE,
							businessdao.getMenuColDataTable(menuCode));
					param.put(TerminalConfiguration.KEY_DIM_KEY,
							dimkey.getComplexDimKeyString());
					param.put(TerminalConfiguration.KEY_ISEXPAND, "1");
					param.put(TerminalConfiguration.KEY_PART3TODATA, "1");
					action = "palmBiTermDataAction_manyKpiManyAreaManyTimeDataQuery.do";
					intent.putExtra(TerminalConfiguration.KEY_RESPONSE_KEY,
							TerminalConfiguration.RESPONSE_UNPACK_MONTH_COUNT);
					intent.putExtra(
							TerminalConfiguration.TITLE_COLUMN,
							TerminalConfiguration.TITLE_COLUMN_UNPACK_MONTH_COUNT);
					intent.putExtra(
							TerminalConfiguration.KEY_COLUNM_KPI_CODE,
							TerminalConfiguration.COLUMN_KPI_CODE_UNPACK_MONTH_COUNT);
					break;
				default:
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

	@Override
	public void setData(JSONObject data) {
		super.setData(data);
		switch (this.mTerminalActivityEnum) {
		case UNPACK_DAY_ACTIVITY:
			setUNPACK_DAY_DATA();
			break;
		case UNPACK_MONTH_ACTIVITY:
			setUNPACK_MONTH_DATA();
			break;
		case UNSALE_DAY_ACTIVITY:
			setUNSALE_DAY_DATA();
			break;
		case UNSALE_MONTH_ACTIVITY:
			setUNSALE_MONTH_DATA();
			break;
		default:
			break;
		}
	}

	/**
	 * 拆包（日）数据及同比 以及 最高最低占比
	 * 
	 *
	 */
	private void setUNPACK_DAY_DATA() {
		this.kpi_statistics = "4870|4880";
		// 顶级地区 拆包日数据及同比
		this.setSimpleKpiValueForBasicBottom("4870", "CURDAY_VALUE", "CD_MYOY",
				"TERMINAL_DAILY_CB");
		this.title = "当日拆包量";
		this.titleSmall = "(话费终止返还量)";
		this.row3mark = "同比";
		// 拆包率（日） 占比最大/最小 子级地市
		this.setUnpack_MaxMinData_Default();
		this.setSimpleMaxMinData(mTerminalActivityEnum.getDateRange()
				.getDateFlag(), "4880", "CURDAY_VALUE");
	}

	private void setSimpleKpiValueForBasicBottom(String kpiCode,
			String kpiCurValue, String kpiColValue, String tableName) {
		String opTime = this.mTerminalActivityEnum.getOPtime();
		String areaCode = userinfo.get("areaId");
		String sql = "select KPI_CODE as kpi_code, %s as curvalue,"
				+ " round(%s*100,1)||'%s' as kpicolvalue, case when %s>0 then 1 else -1 end as leftcolor"
				+ " from %s where kpi_code = '%s' and op_time ='%s' and AREA_CODE ='%s'";
		sql = String.format(sql, kpiCurValue, kpiColValue, "%", kpiColValue,
				tableName, kpiCode, opTime, areaCode);
		// List<Map<String, String>> res = new SQLHelper().queryForList(sql,
		// null);
		List<Map<String, String>> res = getTermData("data4");
		if (res == null || res.size() == 0) {
			this.row2value = this.row3value = Constant.NULL_VALUE_INSTEAD;
			this.row2unit = "";
			return;
		}

		Map<String, String> re = res.get(0);
		ColumnDisplyInfo cdi = ColumnDataFilter
				.getInstance()
				.filterWithDefaultValue(Constant.TERMINAL_SALE_DEFAULT_RULE,
						Constant.TERMINAL_SALE_DEFAULT_UNIT, re.get("curvalue"));

		this.row2value = cdi.getValue();
		this.row2unit = cdi.getUnit();
		this.row2valueColor = this.context.getResources().getColor(
				R.color.green);

		this.row3value = re.get("kpicolvalue");

		String leftcolor = re.get("leftcolor");

		if ("1".equals(leftcolor)) {
			this.row3valueColor = this.context.getResources().getColor(
					R.color.red);
			this.row3icon = R.mipmap.triangle_upward;
		} else {
			this.row3valueColor = this.context.getResources().getColor(
					R.color.green);
			this.row3icon = R.mipmap.triangle_downward;
		}
	}

	private void setUnpack_MaxMinData_Default() {
		this.leftRank = "最高";
		this.rightRank = "最低";
		switch (mTerminalActivityEnum) {
		case UNPACK_DAY_ACTIVITY:
		case UNPACK_MONTH_ACTIVITY:
			this.leftScaleKey = "拆包率";
			this.rightScaleKey = "拆包率";
			break;
		default:
			this.leftScaleKey = "滞销率";
			this.rightScaleKey = "滞销率";
			break;
		}

		this.leftCity = Constant.NULL_VALUE_INSTEAD;
		this.leftValue = Constant.NULL_VALUE_INSTEAD;
		this.rightCity = Constant.NULL_VALUE_INSTEAD;
		this.rightValue = Constant.NULL_VALUE_INSTEAD;

		this.leftUnit = "%";
		this.rightUnit = "%";
	}

	private void setSimpleMaxMinData(String dataType, String kpiCodes,
			String cols) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("areacode", mTerminalActivityEnum.getAreaCode());
		params.put("optime", this.mTerminalActivityEnum.getOPtime());

		params.put("datatype", dataType);
		params.put("kpicodes", kpiCodes);
		params.put("cols", cols);

		String reply = HttpUtil.sendRequest(
				ActionConstant.Term_GetAreaMaxMinKpiValue, params);
		try {
			if (reply != null & !"".equals(reply)) {
				final JSONArray reArray = new JSONArray(reply);
				if (reArray.length() == 2) {
					for (int i = 0; i < reArray.length(); i++) {
						JSONObject re = reArray.getJSONObject(i);
						String rank = re.getString("rank");
						if ("0".equals(rank)) {
							this.rightCity = re.getString("area_name");
							this.rightValue = re.getString("value");
							ColumnDisplyInfo cdi = ColumnDataFilter
									.getInstance()
									.filterWithDefaultValue(
											Constant.TERMINAL_SALE_PERCENT_RULE_0,
											Constant.TERMINAL_SALE_PERCENT_UNIT,
											this.rightValue);
							this.rightValue = cdi.getValue();
							this.rightUnit = cdi.getUnit();
						} else if ("1".equals(rank)) {
							this.leftCity = re.getString("area_name");
							this.leftValue = re.getString("value");
							ColumnDisplyInfo cdi = ColumnDataFilter
									.getInstance()
									.filterWithDefaultValue(
											Constant.TERMINAL_SALE_PERCENT_RULE_0,
											Constant.TERMINAL_SALE_PERCENT_UNIT,
											this.leftValue);
							this.leftValue = cdi.getValue();
							this.leftUnit = cdi.getUnit();
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void setUNPACK_MONTH_DATA() {
		this.kpi_statistics = "5410|5420";
		// 顶级地区 当月拆包量和环比
		this.setSimpleKpiValueForBasicBottom("5410", "CURMONTH_VALUE",
				"CM_COL", "TERMINAL_MONTH_TERM");
		this.title = "当月拆包量";
		this.titleSmall = "(话费终止返还量)";
		this.row3mark = "环比:";

		// 月拆包率 最高最低地市占比
		this.setUnpack_MaxMinData_Default();
		this.setSimpleMaxMinData(Constant.DATA_TYPE_MONTH, "5420",
				"CURMONTH_VALUE");
	}

	private void setUNSALE_DAY_DATA() {
		this.kpi_statistics = "4620|5990";
		// 当日滞销量 及 同比数据
		this.setSimpleKpiValueForBasicBottom("4620", "CURDAY_VALUE", "CD_MYOY",
				"TERMINAL_DAILY_ZX");
		this.title = "当日滞销量";
		this.row3mark = "同比:";
		// 当日滞销量占比 最高最低地市
		this.setUnpack_MaxMinData_Default();
		this.setSimpleMaxMinData(Constant.DATA_TYPE_DAY, "5990", "CURDAY_VALUE");
	}

	private void setUNSALE_MONTH_DATA() {
		this.kpi_statistics = "5160|6000";
		this.title = "当月滞销量";
		this.row3mark = "环比:";

		// 月滞销量 及 同比
		this.setSimpleKpiValueForBasicBottom("5160", "CURMONTH_VALUE",
				"CM_COL", "TERMINAL_MONTH_TERM");

		// 月滞销量 占比最低最高 地市
		this.setUnpack_MaxMinData_Default();
		this.setSimpleMaxMinData(Constant.DATA_TYPE_MONTH, "6000",
				"CURMONTH_VALUE");
	}

	@Override
	public void updateView() {
		this.titleTV.setText(title);
		this.row2valueTV.setTextColor(row2valueColor);
		// this.row2unitTV.setTextColor(row2unitColor);
		this.row2valueTV.setText(row2value);
		this.row2unitTV.setText(row2unit);
		switch (mTerminalActivityEnum) {
		case UNSALE_MONTH_ACTIVITY:
		case UNPACK_MONTH_ACTIVITY:
			this.titleSmallTV.setVisibility(View.VISIBLE);
			this.titleSmallTV.setText(titleSmall);
			this.row3markTV.setText(row3mark);
			this.row3valueTV.setTextColor(row3valueColor);
			this.row3valueTV.setText(row3value);
			this.row3iconIV.setBackgroundResource(row3icon);
			break;
		case UNPACK_DAY_ACTIVITY:
			this.titleSmallTV.setVisibility(View.VISIBLE);
			this.titleSmallTV.setText(titleSmall);
		case UNSALE_DAY_ACTIVITY:
			this.row3markTV.setVisibility(View.GONE);
			this.row3valueTV.setVisibility(View.GONE);
			this.row3iconIV.setVisibility(View.GONE);
			break;
		}

		this.leftRankTV.setText(leftRank);
		this.leftCityTV.setText(leftCity);
		this.leftValueTV.setText(leftValue);
		this.leftValueUnitTV.setText(leftUnit);
		this.leftScaleKeyTV.setText(leftScaleKey);

		this.rightRankTV.setText(rightRank);
		this.rightCityTV.setText(rightCity);
		this.rightValueTV.setText(rightValue);
		this.rightValueUnitTV.setText(rightUnit);
		this.rightScaleKeyTV.setText(rightScaleKey);

	}

	@Override
	public void dispatchView() {
		// ============第一列
		View b1 = (View) this.mView.findViewById(R.id.id_bottom_b1);
		this.titleTV = (TextView) b1.findViewById(R.id.id_title);
		this.titleSmallTV = (TextView) b1.findViewById(R.id.id_title_small);

		View item1 = b1.findViewById(R.id.id_item1);
		View item0 = b1.findViewById(R.id.id_item0);

		this.row2valueTV = (TextView) item1.findViewById(R.id.id_value);
		this.row2unitTV = (TextView) item1.findViewById(R.id.id_unit);

		this.row3markTV = (TextView) item0.findViewById(R.id.id_mark);
		this.row3valueTV = (TextView) item0.findViewById(R.id.id_value);
		this.row3iconIV = (ImageView) item0.findViewById(R.id.id_icon);

		// ============第二列
		View b2 = (View) this.mView.findViewById(R.id.id_bottom_b2);
		this.leftRankTV = (TextView) b2.findViewById(R.id.id_rank);
		this.leftCityTV = (TextView) b2.findViewById(R.id.id_city);
		this.leftValueTV = (TextView) b2.findViewById(R.id.id_value);
		this.leftValueUnitTV = (TextView) b2.findViewById(R.id.id_unit);
		this.leftScaleKeyTV = (TextView) b2.findViewById(R.id.id_scale_key);
		this.leftValueTV.setTextColor(this.context.getResources().getColor(
				R.color.red));
		this.leftCityTV.setTextColor(this.context.getResources().getColor(
				R.color.red));
		this.leftValueUnitTV.setTextColor(this.context.getResources().getColor(
				R.color.red));
		// ============第三列
		View b3 = (View) this.mView.findViewById(R.id.id_bottom_b3);
		this.rightRankTV = (TextView) b3.findViewById(R.id.id_rank);
		this.rightCityTV = (TextView) b3.findViewById(R.id.id_city);
		this.rightValueTV = (TextView) b3.findViewById(R.id.id_value);
		this.rightValueUnitTV = (TextView) b3.findViewById(R.id.id_unit);
		this.rightScaleKeyTV = (TextView) b3.findViewById(R.id.id_scale_key);
		this.rightValueTV.setTextColor(this.context.getResources().getColor(
				R.color.green));
		this.rightCityTV.setTextColor(this.context.getResources().getColor(
				R.color.green));
		this.rightValueUnitTV.setTextColor(this.context.getResources()
				.getColor(R.color.green));

	}

}
