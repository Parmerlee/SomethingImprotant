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
public class ModuleBasicViewPSSmiddle2RowViewBranch extends ViewBranch {

	private TextView leftTitleTV;
	private TextView rightTitleTV;

	private String leftTitle;
	private String rightTitle;

	private TextView leftMarkTV;
	private TextView leftValueTV;
	private TextView leftUnitTV;

	private String leftMark;
	private String leftValue;
	private String leftUnit;

	private TextView middleMarkTV;
	private TextView middleValueTV;
	private TextView middleUnitTV;

	private String middleMark;
	private String middleValue;
	private String middleUnit;

	private TextView rightMarkTV;
	private TextView rightValueTV;
	private TextView rightUnitTV;

	private String rightMark;
	private String rightValue;
	private String rightUnit;

	// config
	private BusinessDao businessdao = new BusinessDao();
	private Map<String, String> userinfo = businessdao.getUserInfo(); // 区域用户信息

	/**
	 * @param c
	 * @param tae
	 */
	public ModuleBasicViewPSSmiddle2RowViewBranch(Context c,
			TerminalActivityEnum tae) {
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

				switch (mTerminalActivityEnum) {
				case PSS_DAY_ACTIVITY:
					title_big = "终端进销存";
					String menuCode = TerminalConfiguration.KEY_MENU_CODE_PSS_DAY;
					ComplexDimInfo dimkey = businessdao
							.getMenuComplexDimKey(businessdao
									.getMenuInfo(menuCode));
					param.put(TerminalConfiguration.KEY_KPI_CODES,
							TerminalConfiguration.KPI_PSS_DAY_IN_STORE);
					param.put(TerminalConfiguration.KEY_COLUMN_NAME,
							TerminalConfiguration.CURDAY_VALUE); // 列
					param.put(TerminalConfiguration.KEY_DATATABLE,
							businessdao.getMenuColDataTable(menuCode));
					param.put(TerminalConfiguration.KEY_DIM_KEY,
							dimkey.getComplexDimKeyString());
					param.put(TerminalConfiguration.KEY_ISEXPAND, "1");
					action = "palmBiTermDataAction_purchargeRomDataQuery.do";
					intent.putExtra(TerminalConfiguration.TITLE_COLUMN,
							TerminalConfiguration.TITLE_COLUMN_PSS_DAY_IN_STROE);
					intent.putExtra(TerminalConfiguration.KEY_RESPONSE_KEY,
							TerminalConfiguration.RESPONSE_PSS_DAY_IN_STORE_KEY);
					intent.putExtra(
							TerminalConfiguration.KEY_COLUNM_KPI_CODE,
							TerminalConfiguration.COLUMN_KPI_CODE_PSS_DAY_IN_STOR);
					break;
				case PSS_MONTH_ACTIVITY:

					break;
				}

				intent.putExtra(TerminalConfiguration.KEY_ACTION, action);
				intent.putExtra(TerminalConfiguration.KEY_MAP,
						(Serializable) param);
				intent.putExtra(TerminalConfiguration.KEY_ACTIVITY_ENUM,
						mTerminalActivityEnum);
				intent.putExtra(TerminalConfiguration.TITLE_BIG, title_big);
				// context.startActivity(intent);
			}
		});

	}

	@Override
	public void dispatchView() {
		// TODO Auto-generated method stub
		this.leftTitleTV = (TextView) this.mView
				.findViewById(R.id.id_left_title);
		this.rightTitleTV = (TextView) this.mView
				.findViewById(R.id.id_right_title);
		View leftItem = this.mView.findViewById(R.id.id_left_item_3column);
		this.leftMarkTV = (TextView) leftItem.findViewById(R.id.id_mark);
		this.leftValueTV = (TextView) leftItem.findViewById(R.id.id_value);
		this.leftUnitTV = (TextView) leftItem.findViewById(R.id.id_unit);

		View middleItem = this.mView
				.findViewById(R.id.id_right_item_3column_middle);
		this.middleMarkTV = (TextView) middleItem.findViewById(R.id.id_mark);
		this.middleValueTV = (TextView) middleItem.findViewById(R.id.id_value);
		this.middleUnitTV = (TextView) middleItem.findViewById(R.id.id_unit);

		View rightItem = this.mView
				.findViewById(R.id.id_right_item_3column_right);
		this.rightMarkTV = (TextView) rightItem.findViewById(R.id.id_mark);
		this.rightValueTV = (TextView) rightItem.findViewById(R.id.id_value);
		this.rightUnitTV = (TextView) rightItem.findViewById(R.id.id_unit);
	}

	@Override
	public void setData(JSONObject data) {
		super.setData(data);
		switch (this.mTerminalActivityEnum) {
		case PSS_DAY_ACTIVITY:
			setPSS_DAY_DATA();
			break;
		}
	}

	private void setPSS_DAY_DATA() {
		this.kpi_statistics = "370|380|390";

		String sqlString = "Select KPI_CODE as kpiCode,CURDAY_VALUE  as value From TERMINAL_DAILY_JXC where kpi_code in('370','380','390') and op_time=?";

		List<Map<String, String>> resultList = new SQLHelper().queryForList(
				sqlString, new String[] { mTerminalActivityEnum.getOPtime() });
		resultList = getTermData("data6");

		this.rightMark = "支撑天数:";
		this.rightTitle = "当月终端库存";
		this.middleMark = "库存:";
		this.leftTitle = "当月终端进货";
		this.leftMark = "进货:";

		if (resultList == null || resultList.size() == 0) {
			this.leftValue = "0";
			this.leftUnit = Constant.TERMINAL_SALE_DEFAULT_UNIT;
			this.middleValue = "0";
			this.middleUnit = Constant.TERMINAL_SALE_DEFAULT_UNIT;
			this.rightValue = Constant.NULL_VALUE_INSTEAD;
		} else {
			int len = resultList.size();
			ColumnDisplyInfo cdi = null;
			for (int i = 0; i < len; i++) {
				String kpiCode = resultList.get(i).get("kpiCode");
				if (TerminalConfiguration.KPI_PSS_TERMINAL_JH_COUNT
						.equals(kpiCode)) {
					cdi = ColumnDataFilter.getInstance()
							.filterWithDefaultValue(
									Constant.TERMINAL_SALE_DEFAULT_RULE,
									Constant.TERMINAL_SALE_DEFAULT_UNIT,
									resultList.get(i).get("value"));
					this.leftValue = cdi.getValue();
					this.leftUnit = cdi.getUnit();
					continue;
				}
				if (TerminalConfiguration.KPI_PSS_TERMINAL_KC_COUNT
						.equals(kpiCode)) {
					cdi = ColumnDataFilter.getInstance()
							.filterWithDefaultValue(
									Constant.TERMINAL_SALE_DEFAULT_RULE,
									Constant.TERMINAL_SALE_DEFAULT_UNIT,
									resultList.get(i).get("value"));
					this.middleValue = cdi.getValue();
					this.middleUnit = cdi.getUnit();
					continue;
				}
				if (TerminalConfiguration.KPI_PSS_TERMINAL_ZC_DAY
						.equals(kpiCode)) {
					cdi = ColumnDataFilter.getInstance()
							.filterWithDefaultValue(
									Constant.TERMINAL_SALE_DEFAULT_RULE, "天",
									resultList.get(i).get("value"));
					this.rightValue = cdi.getValue();
					this.rightUnit = cdi.getUnit();
					continue;
				}

			}
		}

	}

	@Override
	public void updateView() {
		// TODO Auto-generated method stub
		this.leftTitleTV.setText(leftTitle);
		this.rightTitleTV.setText(rightTitle);

		this.leftMarkTV.setText(leftMark);
		this.leftValueTV.setText(leftValue);
		this.leftUnitTV.setText(leftUnit);

		this.middleMarkTV.setText(middleMark);
		this.middleValueTV.setText(middleValue);
		this.middleUnitTV.setText(middleUnit);

		this.rightMarkTV.setText(rightMark);
		this.rightValueTV.setText(rightValue);
		this.rightUnitTV.setText(rightUnit);
	}

}
