package com.bonc.mobile.hbmclient.terminal.component;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.CombinedXYChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.data.ComplexDimInfo;
import com.bonc.mobile.hbmclient.data.SQLHelper;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;
import com.bonc.mobile.hbmclient.terminal.subactivity.CurrentMonthActivity;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.LogUtil;
import com.bonc.mobile.hbmclient.util.NumberUtil;
import com.bonc.mobile.hbmclient.util.StringUtil;

public class ModuleBasicViewCubeLineViewBranch extends ViewBranch {
	int lineW = 1;// 柱宽
	int pointSize = 2;// 点宽
	TextView /* mccTitleTextView, */mccTargeTextView, mccUnitTextView;// 模块标题，显示指标，指标单位所在View
	String title, target, unit;// 模块标题，显示指标，指标单位
	DecimalFormat df = new DecimalFormat("#.#");
	LinearLayout barLayout;// 图显示所在布局
	XYMultipleSeriesDataset dataset;

	private String[] xLabel;

	public List<Map<String, String>> ser1Datas;// 柱图数据（或饼图数据）

	public List<Map<String, String>> ser2Datas;// 折线图图数据

	private DateRangeEnum mCType = DateRangeEnum.DAY;// 指定是数据周期类型为日/月/年/百年等等

	public DateRangeEnum getmCType() {
		return mCType;
	}

	public void setmCType(DateRangeEnum mCType) {
		this.mCType = mCType;
	}

	private int mMinimumDays = 12;

	XYMultipleSeriesRenderer renderer;
	public double maxYValue = 0; // 数据的最大值
	public double minYValue = 0; // 数据的最小值
	public double unitDiv = 1;
	private String originalUnit;

	// config
	private BusinessDao businessdao = new BusinessDao();
	private Map<String, String> userinfo = businessdao.getUserInfo(); // 区域用户信息

	public ModuleBasicViewCubeLineViewBranch(Context c, TerminalActivityEnum tae) {
		super(c, tae);
		// TODO Auto-generated constructor stub

	}

	@Override
	public void setViewListener() {
		this.mView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(context, CurrentMonthActivity.class);
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

				Map<String, String> param1 = new HashMap<String, String>(); // 远程请求参数
				Map<String, String> menuInfo = new BusinessDao()
						.getMenuInfo(mTerminalActivityEnum.getMenuCode());
				String title_big1 = null;

				switch (mTerminalActivityEnum) {
				case PSS_DAY_ACTIVITY:
					title_big = "日销量";
					menuCode = TerminalConfiguration.KEY_MENU_CODE_PSS_DAY;
					dimkey = businessdao.getMenuComplexDimKey(businessdao
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
					intent.putExtra(TerminalConfiguration.TITLE_COLUMN,
							TerminalConfiguration.TITLE_COLUMN_PSS_DAY_6ROW);

					// chart
					title_big1 = "日销量";
					param1.put(TerminalConfiguration.KEY_KPI_CODE,
							TerminalConfiguration.KPI_PSS_DAY_COUNT_CHART);
					param1.put(TerminalConfiguration.KEY_COLUMN_NAME,
							TerminalConfiguration.CURDAY_VALUE);
					param1.put(TerminalConfiguration.KEY_DATATABLE,
							menuInfo.get("dataTable"));
					break;
				case PSS_MONTH_ACTIVITY:
					title_big = "当年每月销量";
					menuCode = TerminalConfiguration.KEY_MENU_CODE_PSS_MONTH;
					dimkey = businessdao.getMenuComplexDimKey(businessdao
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

					// chart
					title_big1 = "当年每月销量";
					param1.put(TerminalConfiguration.KEY_KPI_CODE,
							TerminalConfiguration.KPI_PSS_MONTH_COUNT_CHART);
					param1.put(TerminalConfiguration.KEY_COLUMN_NAME,
							TerminalConfiguration.CURMONTH_VALUE);
					param1.put(TerminalConfiguration.KEY_DATATABLE,
							menuInfo.get("dataTable"));
					break;
				case UNSALE_DAY_ACTIVITY:
					title_big = "日滞销量";
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

					// chart
					title_big1 = "日滞销率";
					param1.put(TerminalConfiguration.KEY_KPI_CODE,
							TerminalConfiguration.KPI_UNSALE_DAY_COUNT_CHART);
					param1.put(TerminalConfiguration.KEY_COLUMN_NAME,
							TerminalConfiguration.CURDAY_VALUE);
					param1.put(TerminalConfiguration.KEY_DATATABLE,
							menuInfo.get("dataTable"));
					break;
				case UNSALE_MONTH_ACTIVITY:
					title_big = "当年每月滞销量";
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

					// chart
					title_big1 = "当年每月滞销率";
					param1.put(TerminalConfiguration.KEY_KPI_CODE,
							TerminalConfiguration.KPI_UNSALE_MONTH_COUNT_CHART);
					param1.put(TerminalConfiguration.KEY_COLUMN_NAME,
							TerminalConfiguration.CURMONTH_VALUE);
					param1.put(TerminalConfiguration.KEY_DATATABLE,
							menuInfo.get("dataTable"));
					break;
				case UNPACK_DAY_ACTIVITY:
					title_big = "日拆包量";
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

					// chart
					title_big1 = "日拆包率";
					param1.put(TerminalConfiguration.KEY_KPI_CODE,
							TerminalConfiguration.KPI_UNPACK_DAY_COUNT_CHART);
					param1.put(TerminalConfiguration.KEY_COLUMN_NAME,
							TerminalConfiguration.CURDAY_VALUE);
					param1.put(TerminalConfiguration.KEY_DATATABLE,
							menuInfo.get("dataTable"));
					break;
				case UNPACK_MONTH_ACTIVITY:
					title_big = "当年每月拆包量";
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

					// chart
					title_big1 = "当年每月拆包率";
					param1.put(TerminalConfiguration.KEY_KPI_CODE,
							TerminalConfiguration.KPI_UNPACK_MONTH_COUNT_CHART);
					param1.put(TerminalConfiguration.KEY_COLUMN_NAME,
							TerminalConfiguration.CURMONTH_VALUE);
					param1.put(TerminalConfiguration.KEY_DATATABLE,
							menuInfo.get("dataTable"));
					break;
				case FG_MONTH_ACTIVITY:
					title_big = "当年每月省内窜出率";
					menuCode = TerminalConfiguration.KEY_MENU_CODE_FG_MONTH;
					dimkey = businessdao.getMenuComplexDimKey(businessdao
							.getMenuInfo(menuCode));
					param.put(
							TerminalConfiguration.KEY_KPI_CODES,
							TerminalConfiguration.KPI_FG_MONTH_COUNT_ACTIVATE_CHART);
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
							TerminalConfiguration.RESPONSE_FG_MONTH_COUNT_ACTIVATE_CHART);
					intent.putExtra(
							TerminalConfiguration.TITLE_COLUMN,
							TerminalConfiguration.TITLE_COLUMN_FG_MONTH_COUNT_ACTIVATE_CHART);

					// chart
					title_big1 = "当年每月省内窜出率";
					param1.put(TerminalConfiguration.KEY_KPI_CODE,
							TerminalConfiguration.KPI_FG_MONTH_COUNT_CHART);
					param1.put(TerminalConfiguration.KEY_COLUMN_NAME,
							TerminalConfiguration.CURMONTH_VALUE);
					param1.put(TerminalConfiguration.KEY_DATATABLE,
							menuInfo.get("dataTable"));
					break;
				}

				intent.putExtra(TerminalConfiguration.TITLE_BIG, title_big);
				intent.putExtra(TerminalConfiguration.KEY_ACTIVITY_ENUM,
						mTerminalActivityEnum);
				intent.putExtra(TerminalConfiguration.KEY_ACTION, action);
				intent.putExtra(TerminalConfiguration.KEY_MAP,
						(Serializable) param);

				// chart
				intent.putExtra(TerminalConfiguration.TITLE_BIG_CHART,
						title_big1);
				intent.putExtra(TerminalConfiguration.KPI_UNIT_CHART,
						originalUnit);
				intent.putExtra(TerminalConfiguration.KEY_MAP_CHART,
						(Serializable) param1);

				intent.putExtra("chart_data1",
						(Serializable) getTermData("data2"));
				intent.putExtra("chart_data2",
						(Serializable) getTermData("data3"));
				// context.startActivity(intent);
			}
		});
	}

	public void dispatchView() {
		mccTargeTextView = (TextView) this.mView.findViewById(R.id.mcc_target);
		mccUnitTextView = (TextView) this.mView.findViewById(R.id.mcc_unit);
		barLayout = (LinearLayout) this.mView.findViewById(R.id.mcc_bar);
		setDefaultRender();
		dataset = new XYMultipleSeriesDataset();
	}

	public void setData(JSONObject data) {
		super.setData(data);
		target = "";
		unit = "台";
		unitDiv = 1;

		if (ser1Datas == null) {
			ser1Datas = new ArrayList<Map<String, String>>();
		} else {
			ser1Datas.clear();
		}
		if (ser2Datas == null) {
			ser2Datas = new ArrayList<Map<String, String>>();
		} else {
			ser2Datas.clear();
		}
		// 查询数据
		Calendar calendar = (Calendar) mTerminalActivityEnum.getView().getTag();

		if (calendar == null) {
			LogUtil.debug("xxx", "数据日期为空!返回！");
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
			LogUtil.debug("xxx", "日期字符串为空,返回!");
			return;
		}

		String kpi_code = null;

		switch (mTerminalActivityEnum) {
		case PSS_DAY_ACTIVITY:
			kpi_code = TerminalConfiguration.KPI_PSS_DAY_SALE;
			break;
		case UNSALE_DAY_ACTIVITY:
			unit = "%";
			kpi_code = "5990";
			break;
		case UNPACK_DAY_ACTIVITY:
			unit = "%";
			kpi_code = "4880";
			break;
		case UNSALE_MONTH_ACTIVITY:
			unit = "%";
			kpi_code = "6000";
			break;
		case UNPACK_MONTH_ACTIVITY:
			unit = "%";
			kpi_code = "5420";
			break;
		case FG_MONTH_ACTIVITY:
			unit = "%";
			kpi_code = "5930";
			break;
		case PSS_MONTH_ACTIVITY:
			kpi_code = "2780";
			break;
		default:
			break;
		}
		this.originalUnit = unit;

		Map<String, String> menuInfo = new BusinessDao()
				.getMenuInfo(mTerminalActivityEnum.getMenuCode());

		if (StringUtil.isNull(kpi_code) || menuInfo == null
				|| StringUtil.isNull(menuInfo.get("dataTable"))) {
			LogUtil.debug("xxx", "获取不到菜单信息或者菜单的数据表的信息");
			return;
		}

		this.kpi_statistics = kpi_code;

		switch (mTerminalActivityEnum.getDateRange()) {
		case DAY:
			String preSql = "Select CURDAY_VALUE as value,op_time as op_time from "
					+ menuInfo.get("dataTable")
					+ " WHERE KPI_CODE=? "
					+ "and OP_TIME between ? and ?  order by  OP_TIME asc";

			String[] dateNeed = DateUtil.getCurrentLastDay(dateString,
					DateRangeEnum.DAY.getDateServerPattern());
			// 当月没日销量
			List<Map<String, String>> resultList = new SQLHelper()
					.queryForList(preSql, new String[] { kpi_code, dateNeed[0],
							dateNeed[1] });
			// 上个月每日销量
			List<Map<String, String>> resultList2 = new SQLHelper()
					.queryForList(preSql, new String[] { kpi_code, dateNeed[2],
							dateNeed[3] });
			if (mData != null) {
				resultList = getTermData("data2");
				resultList2 = getTermData("data3");
			}

			int daynum = 30;

			double[] nums = new double[daynum]; // 当月值
			double[] last_nums = new double[daynum]; // 上月同期值

			String temptime = dateNeed[0];
			String lasttemptime = dateNeed[2];
			int k = 0;

			xLabel = DateUtil.getDayInNum(temptime,
					DateRangeEnum.DAY.getDateServerPattern(), daynum);
			if (resultList != null && resultList.size() > 0) {
				int len = resultList.size();
				for (int i = 0; i < len;) {
					String op_time = resultList.get(i).get("op_time");
					if (temptime.equals(op_time)) {
						nums[k] = NumberUtil.changeToDouble(resultList.get(i)
								.get("value"));
						if ("%".equals(unit)) {
							nums[k] *= 100;
						}
						i++;
					} else {
						nums[k] = 0.0;
					}

					if (++k >= daynum) {
						break;
					}

					temptime = DateUtil.getDateSpecified(temptime,
							DateRangeEnum.DAY.getDateServerPattern(), 1);
				}

				// 当月每日销量。

				ser1Datas.clear();

				for (int mBar = 0; mBar < nums.length; mBar++) {
					Map<String, String> barMap = new HashMap<String, String>();
					barMap.put("value", "" + nums[mBar]);
					maxYValue = maxYValue > nums[mBar] ? maxYValue : nums[mBar];
					ser1Datas.add(barMap);
				}

				// k值初始化
				k = 0;

				if (resultList2 != null && resultList2.size() > 0) {
					int len2 = resultList2.size();
					for (int i = 0; i < len && i < len2;) {
						String last_op_time = resultList2.get(i).get("op_time");
						if (lasttemptime.equals(last_op_time)) {
							last_nums[k] = NumberUtil
									.changeToDouble(resultList2.get(i).get(
											"value"));
							if ("%".equals(unit)) {
								last_nums[k] *= 100;
							}
							i++;
						} else {
							last_nums[k] = 0.0;
						}
						if (++k >= daynum) {
							break;
						}
						lasttemptime = DateUtil.getDateSpecified(lasttemptime,
								DateRangeEnum.DAY.getDateServerPattern(), 1);
					}

				}
				// 去年同期每日销量

				ser2Datas.clear();
				for (int mBar = 0; mBar < last_nums.length; mBar++) {
					Map<String, String> barMap = new HashMap<String, String>();
					barMap.put("value", "" + last_nums[mBar]);
					maxYValue = maxYValue > last_nums[mBar] ? maxYValue
							: last_nums[mBar];
					ser2Datas.add(barMap);
				}
			} else {
				// 如果查询结果为空,则添加默认值.
				for (int mBar = 0; mBar < daynum; mBar++) {
					Map<String, String> barMap = new HashMap<String, String>();
					barMap.put("value", "0.0");
					maxYValue = maxYValue > 0.0 ? maxYValue : 0.0;
					ser1Datas.add(barMap);
					ser2Datas.add(barMap);
					xLabel[mBar] = String.valueOf(mBar + 1);
				}
			}
			break;
		case MONTH:
			String preSql0 = "Select CURMONTH_VALUE as value,op_time as op_time from "
					+ menuInfo.get("dataTable")
					+ " WHERE KPI_CODE=? "
					+ "and OP_TIME between ? and ?  order by  OP_TIME asc";

			Date curDate0 = DateUtil.getDate(dateString, DateUtil.PATTERN_6);
			String curr_first_month = DateUtil.getFirstMonth(curDate0,
					DateUtil.PATTERN_6);
			// 当年每月销量
			List<Map<String, String>> resultList0 = new SQLHelper()
					.queryForList(preSql0, new String[] { kpi_code,
							curr_first_month, dateString });
			// 上年每月销量
			String last_first_month = DateUtil.getFirstMonthLastyear(curDate0,
					DateUtil.PATTERN_6);
			String last_curr_month = DateUtil.getMonthLastyear(curDate0,
					DateUtil.PATTERN_6);
			List<Map<String, String>> resultList20 = new SQLHelper()
					.queryForList(preSql0, new String[] { kpi_code,
							last_first_month, last_curr_month });
			if (mData != null) {
				resultList0 = getTermData("data2");
				resultList20 = getTermData("data3");
			}

			int month_numbs = DateUtil.MonthBetween(curr_first_month,
					dateString, DateUtil.PATTERN_6) + 1;

			if (month_numbs < 12) {
				month_numbs = 12;
			}

			xLabel = new String[month_numbs];
			for (int i = 0; i < month_numbs; i++) {
				xLabel[i] = String.valueOf(i + 1);
			}

			double[] nums0 = new double[month_numbs]; // 当月值
			double[] last_nums0 = new double[month_numbs]; // 上月同期值

			int month_count = 0;

			if (resultList0 != null && resultList0.size() > 0) {
				int len = resultList0.size();

				for (int i = 0; i < len;) {
					if (curr_first_month.equals(resultList0.get(i).get(
							"op_time"))) {
						nums0[month_count] = NumberUtil
								.changeToDouble(resultList0.get(i).get("value"));
						if ("%".equals(unit)) {
							nums0[month_count] *= 100;
						}
						i++;
					}

					if (++month_count >= month_numbs) {
						break;
					}
					curr_first_month = DateUtil.monthBefore(curr_first_month,
							DateUtil.PATTERN_6, -1);
				}

				for (int mBar = 0; mBar < nums0.length; mBar++) {
					Map<String, String> barMap = new HashMap<String, String>();
					barMap.put("value", "" + nums0[mBar]);
					maxYValue = maxYValue > nums0[mBar] ? maxYValue
							: nums0[mBar];
					ser1Datas.add(barMap);
				}

				month_count = 0;
				if (resultList20 != null && resultList20.size() > 0) {
					int len2 = resultList20.size();
					for (int i = 0; i < len && i < len2;) {
						if (last_first_month.equals(resultList20.get(i).get(
								"op_time"))) {
							last_nums0[month_count] = NumberUtil
									.changeToDouble(resultList20.get(i).get(
											"value"));
							if ("%".equals(unit)) {
								last_nums0[month_count] *= 100;
							}
							i++;
						}
						if (++month_count >= month_numbs) {
							break;
						}
						last_first_month = DateUtil.monthBefore(
								last_first_month, DateUtil.PATTERN_6, -1);
					}

				}

				for (int mBar = 0; mBar < last_nums0.length; mBar++) {
					Map<String, String> barMap = new HashMap<String, String>();
					barMap.put("value", "" + last_nums0[mBar]);
					maxYValue = maxYValue > last_nums0[mBar] ? maxYValue
							: last_nums0[mBar];
					ser2Datas.add(barMap);
				}
			} else {
				for (int mBar = 0; mBar < month_numbs; mBar++) {
					Map<String, String> barMap = new HashMap<String, String>();
					barMap.put("value", "0.0");
					ser1Datas.add(barMap);
					ser2Datas.add(barMap);
					maxYValue = maxYValue > 0.0 ? maxYValue : 0.0;
					xLabel[mBar] = String.valueOf(mBar + 1);
				}
			}
			break;
		}
		// 获得Y轴最大最小值
		if ("%".equals(unit)) {

		} else {
			unitDiv = NumberUtil.getScale(maxYValue);
			unit = NumberUtil.getUnit(unitDiv) + unit;
		}

		if (dataset != null)
			dataset.removeAll();
		CategorySeries bar_series = new CategorySeries("");
		// 为柱形图添加值
		int len = ser1Datas.size();

		for (int i = 0; i < len; i++) {

			bar_series.add(NumberUtil.changeToDouble(ser1Datas.get(i).get(
					"value"))
					/ unitDiv);// 除以10000单位变成
								// "万"
		}
		XYSeries line_series_xy = new XYSeries("", 1);
		for (int i = 0; i < ser2Datas.size(); i++) {
			line_series_xy.add(i + 1,
					NumberUtil.changeToDouble(ser2Datas.get(i).get("value"))
							/ unitDiv);
		}
		XYSeries bar_series_xy;
		bar_series_xy = bar_series.toXYSeries();
		if (bar_series_xy.getItemCount() < mMinimumDays) {
			for (int i = bar_series_xy.getItemCount(); i < mMinimumDays; i++) {
				bar_series_xy.add(i + 1, 0);
			}
		}
		dataset.addSeries(bar_series_xy);
		dataset.addSeries(line_series_xy);

		if (unitDiv > 1) {

			if (minYValue > 0)
				minYValue = 0;
		}
		maxYValue = maxYValue / unitDiv;
		minYValue = minYValue / unitDiv;
		maxYValue = maxYValue + (Math.abs(maxYValue) / 7); // 让左边刻度线高出数字的最大值

		minYValue = minYValue - (Math.abs(minYValue) / 7);
	}

	public void updateView() {
		mccTargeTextView.setText(target);
		mccUnitTextView.setText("(" + unit + ")");

		if (ser1Datas != null) {

			updateRender();

			GraphicalView view = null;

			CombinedXYChart mChart = new CombinedXYChart(dataset, renderer,
					new String[] { BarChart.TYPE, LineChart.TYPE });

			view = new GraphicalView(this.mView.getContext(), mChart);
			view.setDrawingCacheEnabled(true);
			view.ignoreAllTouchEvent();

			barLayout.removeAllViews();
			barLayout.addView(view);
		}
	}

	private void updateRender() {

		df.setMaximumFractionDigits(1);

		int daycount = dataset.getSeriesAt(0).getItemCount();
		int date_space = 1;
		renderer.clearYTextLabels();
		renderer.clearXTextLabels();
		switch (mCType) {
		case MONTH:

			renderer.setDisplayChartValues(false);
			date_space = 5;
			renderer.addXTextLabel(dataset.getSeriesAt(0).getX(0), "1");
			for (int i = date_space - 1; i < daycount; i += date_space) {
				if (i + 1 != 30) {
					renderer.addXTextLabel(dataset.getSeriesAt(0).getX(i),
							xLabel[i]);
				}
			}

			renderer.addXTextLabel(dataset.getSeriesAt(0).getX(daycount - 1),
					daycount + "");
			break;
		case DAY:
			renderer.setDisplayChartValues(false);
			for (int i = 0; i < daycount; i += date_space) {
				renderer.addXTextLabel(dataset.getSeriesAt(0).getX(i),
						xLabel[i]);
			}
			break;
		default:
			renderer.setDisplayChartValues(false);
			for (int i = 0; i < daycount; i += date_space) {
				renderer.addXTextLabel(dataset.getSeriesAt(0).getX(i), (i + 1)
						+ "");
			}
			break;
		}

		renderer.setYAxisMax(maxYValue, 0);
		renderer.setYAxisMax(maxYValue, 1);
		float labelvalue = (float) (maxYValue / 5);
		if (Math.abs(labelvalue) > 1) {
			df.applyPattern("#.#");
		} else {
			df.applyPattern("#.###");
		}
		for (int i = 1; i < 5; i++) {
			String floats = df.format(i * labelvalue);
			renderer.addYTextLabel(i * labelvalue, floats, 0);
		}

		renderer.setXAxisMin(dataset.getSeriesAt(0).getMinX() - 0.3);
		renderer.setXAxisMin(dataset.getSeriesAt(0).getMinX() - 0.3, 1);
		renderer.setShowLegend(false);
		renderer.setXAxisMax(daycount + 0.3);
		renderer.setXAxisMax(daycount + 0.3, 1);

	}

	private void setDefaultRender() {

		renderer = new XYMultipleSeriesRenderer(2);
		renderer.setMarginsColor(Color.TRANSPARENT);
		renderer.setInScroll(true);
		XYSeriesRenderer mBarRender = new XYSeriesRenderer();
		mBarRender.setColor(Color.rgb(16, 75, 215));
		mBarRender.setGradientEnabled(true);
		mBarRender.setGradientStart(15000, 0xff1950da);
		mBarRender.setGradientStop(0, 0xff1eaef1);
		mBarRender.setShadowColor(0xff000000);
		mBarRender.setShowBarShadow(true);
		XYSeriesRenderer mLine1Render = new XYSeriesRenderer();
		mLine1Render.setColor(0xff000000);
		mLine1Render.setPointStyle(PointStyle.CIRCLE);
		mLine1Render.setFillPoints(true);

		mLine1Render.setLineWidth(lineW);
		renderer.removeAllRenderers();
		renderer.addSeriesRenderer(mBarRender);
		renderer.addSeriesRenderer(mLine1Render);
		renderer.setBarSpacing(1.2);
		renderer.setXLabels(0);
		renderer.setXLabelsColor(0xff000000);
		renderer.setLabelsTextSize(10);
		renderer.setMargins(new int[] { 5, 30, 0, 10 });
		renderer.setShowAxes(false);
		renderer.setYAxisMin(0, 0);
		renderer.setYAxisMin(0, 1);
		renderer.setPointSize(pointSize);

		renderer.setYLabelsColor(0, 0xff000000);
		renderer.setYLabelsColor(1, 0x00000000);
		renderer.setYLabelsAlign(Align.RIGHT, 0);
		renderer.setShowCustomTextGrid(true);

		renderer.setGridColor(0xff7ca6c7);
		renderer.setYLabels(0);
		renderer.setYLabelsPadding(5);
		renderer.setInScroll(true);

	}

}
