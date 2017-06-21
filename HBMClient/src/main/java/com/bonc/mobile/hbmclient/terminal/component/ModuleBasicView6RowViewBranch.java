/**
 * TODO
 */
package com.bonc.mobile.hbmclient.terminal.component;

import java.io.Serializable;
import java.util.HashMap;
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

/**
 * @author liweigao
 * 
 */
public class ModuleBasicView6RowViewBranch extends ViewBranch {
	private TextView row1markTV;
	private TextView row2valueTV;
	private TextView row2unitTV;

	private TextView row3markTV;
	private TextView row3valueTV;
	private ImageView row3iconIV;

	private TextView row3downmarkTV;
	private TextView row3downvalueTV;
	private ImageView row3downiconIV;

	private String row1mark;
	private String row2value;
	private int row2valueColor;
	private String row2unit;
	private int row2unitColor;

	private String row3mark;
	private String row3value;
	private int row3valueColor;
	private int row3icon;

	private String row3downmark;
	private String row3downvalue;
	private int row3downvalueColor;
	private int row3downicon;

	private TextView row4markTV;
	private TextView row5valueTV;
	private TextView row5unitTV;

	private TextView row6markTV;
	private TextView row6valueTV;
	private ImageView row6iconIV;

	private TextView row6downmarkTV;
	private TextView row6downvalueTV;
	private ImageView row6downiconIV;

	private String row4mark;
	private String row5value;
	private int row5valueColor;
	private String row5unit;
	private int row5unitColor;

	private String row6mark;
	private String row6value;
	private int row6valueColor;
	private int row6icon;

	private String row6downmark;
	private String row6downvalue;
	private int row6downvalueColor;
	private int row6downicon;

	// config
	private BusinessDao businessdao = new BusinessDao();
	private Map<String, String> userinfo = businessdao.getUserInfo(); // 区域用户信息

	/**
	 * @param c
	 * @param tae
	 */
	public ModuleBasicView6RowViewBranch(Context c, TerminalActivityEnum tae) {
		super(c, tae);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setViewListener() {
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

				switch (mTerminalActivityEnum) {
				case PSS_DAY_ACTIVITY:
					title_big = "进销存日";
					String menuCode = TerminalConfiguration.KEY_MENU_CODE_PSS_DAY;
					ComplexDimInfo dimkey = businessdao
							.getMenuComplexDimKey(businessdao
									.getMenuInfo(menuCode));
					param.put(TerminalConfiguration.KEY_KPI_CODES,
							TerminalConfiguration.KPI_PSS_DAY_6ROW);
					param.put(TerminalConfiguration.KEY_COLUMN_NAME,
							TerminalConfiguration.COLUMN_PSS_DAY_6ROW); // 列
					param.put(TerminalConfiguration.KEY_DATATABLE,
							businessdao.getMenuColDataTable(menuCode));
					param.put(TerminalConfiguration.KEY_DIM_KEY,
							dimkey.getComplexDimKeyString());
					param.put(TerminalConfiguration.KEY_ISEXPAND, "1");
					param.put(TerminalConfiguration.KEY_PART3TODATA, "1");
					action = "palmBiTermDataAction_manyKpiManyAreaManyTimeDataQuery.do";
					intent.putExtra(TerminalConfiguration.KEY_RESPONSE_KEY,
							TerminalConfiguration.RESPONSE_PSS_DAY_6ROW);
					intent.putExtra(TerminalConfiguration.KEY_COLUNM_KPI_CODE,
							TerminalConfiguration.COLUMN_KPI_CODE_PSS_DAY);

					intent.putExtra(TerminalConfiguration.TITLE_COLUMN,
							TerminalConfiguration.TITLE_COLUMN_PSS_DAY_6ROW);
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
	public void dispatchView() {
		// TODO Auto-generated method stub
		View top = this.mView.findViewById(R.id.id_top);
		View item1 = top.findViewById(R.id.id_item1);
		View item0 = top.findViewById(R.id.id_item0);
		View item2 = top.findViewById(R.id.id_item2);
		this.row1markTV = (TextView) item1.findViewById(R.id.id_title);
		this.row2valueTV = (TextView) item1.findViewById(R.id.id_value);
		this.row2unitTV = (TextView) item1.findViewById(R.id.id_unit);

		this.row3markTV = (TextView) item0.findViewById(R.id.id_mark);
		this.row3valueTV = (TextView) item0.findViewById(R.id.id_value);
		this.row3iconIV = (ImageView) item0.findViewById(R.id.id_icon);

		this.row3downmarkTV = (TextView) item2.findViewById(R.id.id_mark);
		this.row3downvalueTV = (TextView) item2.findViewById(R.id.id_value);
		this.row3downiconIV = (ImageView) item2.findViewById(R.id.id_icon);

		View bottom = this.mView.findViewById(R.id.id_bottom);
		View item1_b = bottom.findViewById(R.id.id_item1);
		View item0_b = bottom.findViewById(R.id.id_item0);
		View item2_b = bottom.findViewById(R.id.id_item2);
		this.row4markTV = (TextView) item1_b.findViewById(R.id.id_title);
		this.row5valueTV = (TextView) item1_b.findViewById(R.id.id_value);
		this.row5unitTV = (TextView) item1_b.findViewById(R.id.id_unit);

		this.row6markTV = (TextView) item0_b.findViewById(R.id.id_mark);
		this.row6valueTV = (TextView) item0_b.findViewById(R.id.id_value);
		this.row6iconIV = (ImageView) item0_b.findViewById(R.id.id_icon);

		this.row6downmarkTV = (TextView) item2_b.findViewById(R.id.id_mark);
		this.row6downvalueTV = (TextView) item2_b.findViewById(R.id.id_value);
		this.row6downiconIV = (ImageView) item2_b.findViewById(R.id.id_icon);
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

	// 日销量数据
	private void setPSS_DAY_DATA() {
		this.kpi_statistics = "340|360";

		String dateString = mTerminalActivityEnum.getOPtime();

		String sqlString = "Select  CURDAY_VALUE as value,"
				+ " round(CD_COL*100,1)||'%' as hbValue, round(cd_myoy*100,1)||'%' as tbValue,"
				+ " case when CD_COL>0 then 1 else -1 end as tag,"
				+ " case when cd_myoy>0 then 1 else -1 end as tag1"
				+ " From MAIN.TERMINAL_DAILY_JXC where kpi_code = ? and op_time=? limit 1";

		// Map<String, String> daySale = new SQLHelper().queryForMap(sqlString,
		// new String[] { TerminalConfiguration.KPI_PSS_DAY_SALE,
		// dateString });
		Map<String, String> daySale = getOneTermData("data4");

		if (daySale == null) {
			daySale = new HashMap<String, String>();
		}

		// Map<String, String> daySaleLj = new
		// SQLHelper().queryForMap(sqlString,
		// new String[] { TerminalConfiguration.KPI_PSS_MONTH_LJ_SALE,
		// dateString });
		Map<String, String> daySaleLj = getOneTermData("data5");

		if (daySaleLj == null) {
			daySaleLj = new HashMap<String, String>();
		}

		ColumnDisplyInfo cdi = ColumnDataFilter.getInstance()
				.filterWithDefaultValue(Constant.TERMINAL_SALE_DEFAULT_RULE,
						Constant.TERMINAL_SALE_DEFAULT_UNIT,
						daySaleLj.get("value"));

		this.row1mark = "当月销量累计";
		this.row2value = cdi == null ? "" : cdi.getValue();
		this.row2unit = cdi == null ? "" : cdi.getUnit();
		this.row2valueColor = this.context.getResources().getColor(R.color.red);
		this.row2unitColor = this.context.getResources().getColor(R.color.red);

		this.row3mark = "环比：";
		this.row3value = daySaleLj.get("hbValue");
		if ("1".equals(daySaleLj.get("tag"))) {
			this.row3valueColor = context.getResources().getColor(R.color.red);
			this.row3icon = R.mipmap.triangle_upward;
		} else {
			this.row3valueColor = context.getResources()
					.getColor(R.color.green);
			this.row3icon = R.mipmap.triangle_downward;
		}

		this.row3downmark = "同比：";
		this.row3downvalue = daySaleLj.get("tbValue");
		if ("1".equals(daySaleLj.get("tag1"))) {
			this.row3downvalueColor = context.getResources().getColor(
					R.color.red);
			this.row3downicon = R.mipmap.triangle_upward;
		} else {
			this.row3downvalueColor = context.getResources().getColor(
					R.color.green);
			this.row3downicon = R.mipmap.triangle_downward;
		}

		this.row4mark = "当日销量";

		cdi = ColumnDataFilter.getInstance().doFilter(
				Constant.TERMINAL_SALE_DEFAULT_RULE,
				Constant.TERMINAL_SALE_DEFAULT_UNIT, daySale.get("value"));

		this.row5value = cdi == null ? daySale.get("value") : cdi.getValue();
		this.row5unit = cdi == null ? "" : cdi.getUnit();

		this.row6mark = "环比：";
		this.row6value = daySale.get("hbValue");
		if ("1".equals(daySale.get("tag"))) {
			this.row6valueColor = context.getResources().getColor(R.color.red);
			this.row6icon = R.mipmap.triangle_upward;
		} else {
			this.row6valueColor = context.getResources()
					.getColor(R.color.green);
			this.row6icon = R.mipmap.triangle_downward;
		}

		this.row6downmark = "同比：";
		this.row6downvalue = daySale.get("tbValue");

		int row5Color;

		if ("1".equals(daySale.get("tag1"))) {
			this.row6downvalueColor = context.getResources().getColor(
					R.color.red);
			this.row6downicon = R.mipmap.triangle_upward;
			row5Color = R.color.red;
		} else {
			this.row6downvalueColor = context.getResources().getColor(
					R.color.green);
			this.row6downicon = R.mipmap.triangle_downward;
			row5Color = R.color.green;
		}
		this.row5valueColor = this.context.getResources().getColor(row5Color);
		this.row5unitColor = this.context.getResources().getColor(row5Color);
	}

	@Override
	public void updateView() {
		// TODO Auto-generated method stub
		this.row1markTV.setText(row1mark);
		this.row2valueTV.setTextColor(row2valueColor);
		this.row2valueTV.setText(row2value);
		this.row2unitTV.setTextColor(row2unitColor);
		this.row2unitTV.setText(row2unit);
		this.row3markTV.setText(row3mark);
		this.row3valueTV.setTextColor(row3valueColor);
		this.row3valueTV.setText(row3value);
		this.row3iconIV.setBackgroundResource(row3icon);
		this.row3downmarkTV.setText(row3downmark);
		this.row3downvalueTV.setTextColor(row3downvalueColor);
		this.row3downvalueTV.setText(row3downvalue);
		this.row3downiconIV.setBackgroundResource(row3downicon);

		this.row4markTV.setText(row4mark);
		this.row5valueTV.setTextColor(row5valueColor);
		this.row5unitTV.setTextColor(row5unitColor);
		this.row5valueTV.setText(row5value);
		this.row5unitTV.setText(row5unit);
		this.row6markTV.setText(row6mark);
		this.row6valueTV.setTextColor(row6valueColor);
		this.row6valueTV.setText(row6value);
		this.row6iconIV.setBackgroundResource(row6icon);
		this.row6downmarkTV.setText(row6downmark);
		this.row6downvalueTV.setTextColor(row6downvalueColor);
		this.row6downvalueTV.setText(row6downvalue);
		this.row6downiconIV.setBackgroundResource(row6downicon);
	}

}
