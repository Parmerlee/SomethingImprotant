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
import com.bonc.mobile.hbmclient.data.SQLHelper;
import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;
import com.bonc.mobile.hbmclient.terminal.subactivity.DaySaleCountStaticsActivity;

/**
 * @author liweigao
 * @author houyangyang
 */
public class ModuleBasicViewPSSmonthSales extends ViewBranch {
	private TextView leftRow1titleTV;
	private TextView leftRow2valueTV;
	private TextView leftRow2unitTV;

	private String leftRow1title;
	private String leftRow2value;
	private int leftRow2valueColor;
	private String leftRow2unit;
	private int leftRow2unitColor;

	private TextView leftRow3markTV;
	private TextView leftRow3valueTV;
	private ImageView leftRow3iconTV;

	private String leftRow3mark;
	private String leftRow3value;
	private int leftRow3valueColor;
	private int leftRow3icon;

	private TextView rightRow1titleTV;
	private TextView rightRow2valueTV;
	private TextView rightRow2unitTV;

	private String rightRow1title;
	private String rightRow2value;
	private int rightRow2valueColor;
	private String rightRow2unit;
	private int rightRow2unitColor;

	private TextView rightRow3markTV;
	private TextView rightRow3valueTV;
	private ImageView rightRow3iconIV;

	private String rightRow3mark;
	private String rightRow3value;
	private int rightRow3valueColor;
	private int rightRow3icon;

	// config
	private BusinessDao businessdao = new BusinessDao();
	private Map<String, String> userinfo = businessdao.getUserInfo(); // 区域用户信息

	/**
	 * @param c
	 * @param tae
	 */
	public ModuleBasicViewPSSmonthSales(Context c, TerminalActivityEnum tae) {
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
						userinfo.get("areaId")); // 地区id
				String action = null;
				String title_big = null;

				switch (mTerminalActivityEnum) {
				case PSS_MONTH_ACTIVITY:
					title_big = "进销存月";
					String menuCode = TerminalConfiguration.KEY_MENU_CODE_PSS_MONTH;
					ComplexDimInfo dimkey = businessdao
							.getMenuComplexDimKey(businessdao
									.getMenuInfo(menuCode));
					param.put(TerminalConfiguration.KEY_KPI_CODES,
							TerminalConfiguration.KPI_PSS_MONTH_SALE_COUNT);
					param.put(TerminalConfiguration.KEY_COLUMN_NAME,
							TerminalConfiguration.COLUMN_PSS_MONTH_SALE_COUNT); // 列
					param.put(TerminalConfiguration.KEY_DATATABLE,
							businessdao.getMenuColDataTable(menuCode));
					param.put(TerminalConfiguration.KEY_DIM_KEY,
							dimkey.getComplexDimKeyString());
					param.put(TerminalConfiguration.KEY_ISEXPAND, "1");
					param.put(TerminalConfiguration.KEY_PART3TODATA, "1");
					action = "palmBiTermDataAction_manyKpiManyAreaManyTimeDataQuery.do";
					intent.putExtra(TerminalConfiguration.KEY_RESPONSE_KEY,
							TerminalConfiguration.RESPONSE_PSS_MONTH_SALE_COUNT);
					intent.putExtra(
							TerminalConfiguration.TITLE_COLUMN,
							TerminalConfiguration.TITLE_COLUMN_PSS_MONTH_SALE_COUNT);
					intent.putExtra(
							TerminalConfiguration.KEY_COLUNM_KPI_CODE,
							TerminalConfiguration.COLUMN_KPI_CODE_PSS_MONTH_SALE_COUNT);
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
	public void dispatchView() {
		View left = this.mView.findViewById(R.id.id_left);
		View right = this.mView.findViewById(R.id.id_right);

		this.leftRow1titleTV = (TextView) left.findViewById(R.id.id_title);
		this.leftRow2valueTV = (TextView) left.findViewById(R.id.id_value);
		this.leftRow2unitTV = (TextView) left.findViewById(R.id.id_unit);

		View scale = left.findViewById(R.id.id_scale_item);
		this.leftRow3markTV = (TextView) scale.findViewById(R.id.id_mark);
		this.leftRow3valueTV = (TextView) scale.findViewById(R.id.id_value);
		this.leftRow3iconTV = (ImageView) scale.findViewById(R.id.id_icon);

		this.rightRow1titleTV = (TextView) right.findViewById(R.id.id_title);
		this.rightRow2valueTV = (TextView) right.findViewById(R.id.id_value);
		this.rightRow2unitTV = (TextView) right.findViewById(R.id.id_unit);

		View scale_r = right.findViewById(R.id.id_scale_item);
		this.rightRow3markTV = (TextView) scale_r.findViewById(R.id.id_mark);
		this.rightRow3valueTV = (TextView) scale_r.findViewById(R.id.id_value);
		this.rightRow3iconIV = (ImageView) scale_r.findViewById(R.id.id_icon);
	}

	@Override
	public void setData(JSONObject data) {
		super.setData(data);
		switch (this.mTerminalActivityEnum) {
		case PSS_MONTH_ACTIVITY:
			this.setDefaultValue();
			this.setDataFromDB();
			break;
		default:
			break;
		}
	}

	private void setDataFromDB() {
		this.kpi_statistics = "2780|2800";
		// 取出“当月终端销量[2780]”的 “值 和 CM_YOY[同比]” 和 “当年累计终端销量[2800]” 的 “值 和
		// CM_COL[环比]”
		String kpiStr = "'" + TerminalConfiguration.KPI_MONTH_PSS_2780 + "','"
				+ TerminalConfiguration.KPI_MONTH_PSS_2800 + "'";
		String sqlString = "Select KPI_CODE as kpi_code, CURMONTH_VALUE as curmonth_value,"
				+ " round(CM_COL*100,1)||'%' as cm_col, round(CM_YOY*100,1)||'%'  as cm_yoy, "
				+ " case when CM_COL>0 then 1 else -1 end as tag1,"
				+ " case when CM_YOY>0 then 1 else -1 end as tag2"
				+ " From TERMINAL_MONTH_TERM where kpi_code in("
				+ kpiStr
				+ ") and op_time=?";
		List<Map<String, String>> res = new SQLHelper().queryForList(sqlString,
				new String[] { mTerminalActivityEnum.getOPtime() });
		res = getTermData("data4");

		if (res != null) {
			for (Map<String, String> re : res) {
				String kpi_code = re.get("kpi_code");
				if (TerminalConfiguration.KPI_MONTH_PSS_2780
						.equalsIgnoreCase(kpi_code)) { // 当月终端销量展示
					ColumnDisplyInfo cdi = ColumnDataFilter.getInstance()
							.filterWithDefaultValue(
									Constant.TERMINAL_SALE_DEFAULT_RULE,
									Constant.TERMINAL_SALE_DEFAULT_UNIT,
									re.get("curmonth_value"));

					this.rightRow2value = cdi.getValue();
					this.rightRow2valueColor = this.context.getResources()
							.getColor(R.color.red);
					this.rightRow2unit = cdi.getUnit();

					this.rightRow3value = re.get("cm_col");
					if ("1".equals(re.get("tag2"))) {
						this.rightRow3valueColor = this.context.getResources()
								.getColor(R.color.red);
						this.rightRow3icon = R.mipmap.triangle_upward;
					} else {
						this.rightRow3valueColor = this.context.getResources()
								.getColor(R.color.green);
						this.rightRow3icon = R.mipmap.triangle_downward;
					}
				} else if (TerminalConfiguration.KPI_MONTH_PSS_2800
						.equalsIgnoreCase(kpi_code)) {// 当年累计终端销量展示
					ColumnDisplyInfo cdi = ColumnDataFilter.getInstance()
							.filterWithDefaultValue(
									Constant.TERMINAL_SALE_DEFAULT_RULE,
									Constant.TERMINAL_SALE_DEFAULT_UNIT,
									re.get("curmonth_value"));

					this.leftRow2value = cdi.getValue();
					this.leftRow2valueColor = this.context.getResources()
							.getColor(R.color.green);
					this.leftRow2unit = cdi.getUnit();

					this.leftRow3value = re.get("cm_col");
					if ("1".equals(re.get("tag1"))) {
						this.leftRow3valueColor = this.context.getResources()
								.getColor(R.color.red);
						this.leftRow3icon = R.mipmap.triangle_upward;
					} else {
						this.leftRow3valueColor = this.context.getResources()
								.getColor(R.color.green);
						this.leftRow3icon = R.mipmap.triangle_downward;
					}

				}
			}
		}
	}

	/**
	 * 如果无法从数据库中取到值，则默认展示一下数据
	 */
	private void setDefaultValue() {
		this.leftRow1title = "年销量累计";
		this.leftRow2value = Constant.NULL_VALUE_INSTEAD;
		this.leftRow2unit = "万台";

		this.leftRow3mark = "环比：";
		this.leftRow3value = Constant.NULL_VALUE_INSTEAD;

		this.rightRow1title = "当月销量";
		this.rightRow2value = Constant.NULL_VALUE_INSTEAD;
		this.rightRow2unit = "万台";

		this.rightRow3mark = "环比：";
		this.rightRow3value = Constant.NULL_VALUE_INSTEAD;
	}

	@Override
	public void updateView() {
		this.leftRow1titleTV.setText(leftRow1title);
		this.leftRow2valueTV.setTextColor(leftRow2valueColor);
		this.leftRow2valueTV.setText(leftRow2value);
		this.leftRow2unitTV.setText(leftRow2unit);

		this.leftRow3markTV.setText(leftRow3mark);
		this.leftRow3valueTV.setTextColor(leftRow3valueColor);
		this.leftRow3valueTV.setText(leftRow3value);
		this.leftRow3iconTV.setBackgroundResource(leftRow3icon);

		this.rightRow1titleTV.setText(rightRow1title);
		this.rightRow2valueTV.setTextColor(rightRow2valueColor);
		// this.rightRow2unitTV.setTextColor(rightRow2unitColor);
		this.rightRow2valueTV.setText(rightRow2value);
		this.rightRow2unitTV.setText(rightRow2unit);

		this.rightRow3markTV.setText(rightRow3mark);
		this.rightRow3valueTV.setTextColor(rightRow3valueColor);
		this.rightRow3valueTV.setText(rightRow3value);
		this.rightRow3iconIV.setBackgroundResource(rightRow3icon);
	}

}
