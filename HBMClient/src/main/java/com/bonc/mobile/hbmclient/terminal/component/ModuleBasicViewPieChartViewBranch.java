package com.bonc.mobile.hbmclient.terminal.component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.data.ComplexDimInfo;
import com.bonc.mobile.hbmclient.data.SQLHelper;
import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;
import com.bonc.mobile.hbmclient.terminal.subactivity.TerminalPriceBracketActivity;
import com.bonc.mobile.hbmclient.util.ArrayUtil;
import com.bonc.mobile.hbmclient.util.LogUtil;

public class ModuleBasicViewPieChartViewBranch extends ViewBranch {
	LinearLayout chartLay;// ,lableLay;
	DefaultRenderer mRenderer;
	private View chartView;
	protected List<Map<String, String>> list;// 饼图数据
	CategorySeries chartData;// 饼图数据
	String[] mLableStrings;// 折线上文字内容
	int[] colorsLib = new int[] { 0xff3cb2ea, 0xffffb400, 0xffbf0707,
			0xff72b84c, 0xffa112d0 };
	int legendType;// 标签显示形式

	boolean[] renderSet;// render设置0:是否显示数据；1:是否显示折线和文字；3:是否显示折线上文字的背影;

	// config
	private BusinessDao businessdao = new BusinessDao();
	private Map<String, String> userinfo = businessdao.getUserInfo(); // 区域用户信息

	// "0"代表左边饼图， "1"代表右边饼图
	private String pieId;
	private Map<String, String> pieChartKpiText = new HashMap<String, String>();

	public ModuleBasicViewPieChartViewBranch(Context c,
			TerminalActivityEnum tae, boolean[] renderSet, int legendType) {
		super(c, tae);
		this.renderSet = renderSet;
		this.legendType = legendType;
		this.pieId = null;
	}

	public ModuleBasicViewPieChartViewBranch(Context c,
			TerminalActivityEnum tae, boolean[] renderSet, int legendType,
			String pieId) {
		super(c, tae);
		this.renderSet = renderSet;
		this.legendType = legendType;
		this.pieId = pieId;
	}

	@Override
	public void setViewListener() {
		this.mView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(context,
						TerminalPriceBracketActivity.class);
				intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
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
				case PSS_MONTH_ACTIVITY:
					if (TerminalConfiguration.POS_MONTH_PSS_LEFT_PIE
							.equals(pieId)) {
						title_big = "4G终端价格分档";
						menuCode = TerminalConfiguration.KEY_MENU_CODE_PSS_MONTH;
						dimkey = businessdao.getMenuComplexDimKey(businessdao
								.getMenuInfo(menuCode));
						param.put(
								TerminalConfiguration.KEY_KPI_CODES,
								TerminalConfiguration.KPI_PSS_MONTH_RATE_TRANCHE);
						param.put(TerminalConfiguration.KEY_COLUMN_NAME,
								TerminalConfiguration.CURMONTH_VALUE_DR); // 列
						param.put(TerminalConfiguration.KEY_DATATABLE,
								businessdao.getMenuColDataTable(menuCode));
						param.put(TerminalConfiguration.KEY_DIM_KEY,
								dimkey.getComplexDimKeyString());
						param.put(TerminalConfiguration.KEY_ISEXPAND, "1");
						action = "palmBiTermDataAction_manyKpiManyAreaManyTimeDataQuery.do";
						intent.putExtra(
								TerminalConfiguration.TITLE_COLUMN,
								TerminalConfiguration.TITLE_COLUMN_PSS_MONTH_RATE_TRANCHE);
						intent.putExtra(
								TerminalConfiguration.KEY_RESPONSE_KEY,
								TerminalConfiguration.RESPONSE_PSS_MONTH_RATE_TRANCHE);
						intent.putExtra(
								TerminalConfiguration.KEY_COLUNM_KPI_CODE,
								TerminalConfiguration.COLUMN_KPI_CODE_PM_RATE_TRANCHE);
					} else if (TerminalConfiguration.POS_MONTH_PSS_RIGHT_PIE
							.equals(pieId)) {
						title_big = "4G终端补贴分档";
						menuCode = TerminalConfiguration.KEY_MENU_CODE_PSS_MONTH;
						dimkey = businessdao.getMenuComplexDimKey(businessdao
								.getMenuInfo(menuCode));
						param.put(
								TerminalConfiguration.KEY_KPI_CODES,
								TerminalConfiguration.KPI_PSS_MONTH_SUBSIDY_TRANCHE);
						param.put(TerminalConfiguration.KEY_COLUMN_NAME,
								TerminalConfiguration.CURMONTH_VALUE_DR);
						param.put(TerminalConfiguration.KEY_DATATABLE,
								businessdao.getMenuColDataTable(menuCode));
						param.put(TerminalConfiguration.KEY_DIM_KEY,
								dimkey.getComplexDimKeyString());
						param.put(TerminalConfiguration.KEY_ISEXPAND, "1");
						action = "palmBiTermDataAction_manyKpiManyAreaManyTimeDataQuery.do";
						intent.putExtra(
								TerminalConfiguration.TITLE_COLUMN,
								TerminalConfiguration.TITLE_COLUMN_PSS_MONTH_SUBSIDY_TRANCHE);
						intent.putExtra(
								TerminalConfiguration.KEY_RESPONSE_KEY,
								TerminalConfiguration.RESPONSE_PSS_MONTH_SUBSIDY_TRANCHE);
						intent.putExtra(
								TerminalConfiguration.KEY_COLUNM_KPI_CODE,
								TerminalConfiguration.COLUMN_KPI_CODE_PM_SUBSIDY_TRANCHE);
					}
					break;
				case UNSALE_DAY_ACTIVITY:
					title_big = "滞销周期占比分析";
					menuCode = TerminalConfiguration.KEY_MENU_CODE_UNSALE_DAY;
					dimkey = businessdao.getMenuComplexDimKey(businessdao
							.getMenuInfo(menuCode));
					param.put(TerminalConfiguration.KEY_KPI_CODES,
							TerminalConfiguration.KPI_UNSALE_DAY_PIE_PERIOD);
					param.put(TerminalConfiguration.KEY_COLUMN_NAME,
							TerminalConfiguration.CURDAY_VALUE_DR); // 列
					param.put(TerminalConfiguration.KEY_DATATABLE,
							businessdao.getMenuColDataTable(menuCode));
					param.put(TerminalConfiguration.KEY_DIM_KEY,
							dimkey.getComplexDimKeyString());
					param.put(TerminalConfiguration.KEY_ISEXPAND, "1");
					action = "palmBiTermDataAction_manyKpiManyAreaManyTimeDataQuery.do";
					intent.putExtra(
							TerminalConfiguration.TITLE_COLUMN,
							TerminalConfiguration.TITLE_COLUMN_UNSALE_DAY_PIE_PERIOD);
					intent.putExtra(
							TerminalConfiguration.KEY_RESPONSE_KEY,
							TerminalConfiguration.RESPONSE_UNSALE_DAY_PIE_PERIOD);
					intent.putExtra(
							TerminalConfiguration.KEY_COLUNM_KPI_CODE,
							TerminalConfiguration.COLUMN_KPI_CODE_UNSALE_DAY_PIE_PERIOD);
					break;
				case UNSALE_MONTH_ACTIVITY:
					title_big = "滞销周期占比分析";
					menuCode = TerminalConfiguration.KEY_MENU_CODE_UNSALE_MONTH;
					dimkey = businessdao.getMenuComplexDimKey(businessdao
							.getMenuInfo(menuCode));
					param.put(TerminalConfiguration.KEY_KPI_CODES,
							TerminalConfiguration.KPI_UNSALE_MONTH_PIE_PERIOD);
					param.put(TerminalConfiguration.KEY_COLUMN_NAME,
							TerminalConfiguration.CURMONTH_VALUE_DR); // 列
					param.put(TerminalConfiguration.KEY_DATATABLE,
							businessdao.getMenuColDataTable(menuCode));
					param.put(TerminalConfiguration.KEY_DIM_KEY,
							dimkey.getComplexDimKeyString());
					param.put(TerminalConfiguration.KEY_ISEXPAND, "1");
					action = "palmBiTermDataAction_manyKpiManyAreaManyTimeDataQuery.do";
					intent.putExtra(
							TerminalConfiguration.TITLE_COLUMN,
							TerminalConfiguration.TITLE_COLUMN_UNSALE_MONTH_PIE_PERIOD);
					intent.putExtra(
							TerminalConfiguration.KEY_RESPONSE_KEY,
							TerminalConfiguration.RESPONSE_UNSALE_MONTH_PIE_PERIOD);
					intent.putExtra(
							TerminalConfiguration.KEY_COLUNM_KPI_CODE,
							TerminalConfiguration.COLUMN_KPI_CODE_UNSALE_MONTH_PIE_PERIOD);
					break;

				default:
					break;
				}

				intent.putExtra(TerminalConfiguration.KEY_ACTIVITY_ENUM,
						mTerminalActivityEnum);
				intent.putExtra(TerminalConfiguration.TITLE_BIG, title_big);
				intent.putExtra(TerminalConfiguration.KEY_ACTION, action);
				intent.putExtra(TerminalConfiguration.KEY_MAP,
						(Serializable) param);
				// context.startActivity(intent);
			}
		});
	}

	public void dispatchView() {
		chartLay = (LinearLayout) mView.findViewById(R.id.pie_chart_lay);
		initRenderer();
	}

	/**
	 * 初始化renderer
	 */
	protected void initRenderer() {
		mRenderer = new DefaultRenderer();
		int labelTextSize = (int) context.getResources().getDimension(
				R.dimen.heyue_text_size);
		mRenderer.setLabelsTextSize(labelTextSize);
		mRenderer.setLegendTextSize(labelTextSize);
		// 设置图表的外边框(上/左/下/右)
		mRenderer.setMargins(new int[] { 3, 3, 3, 3 });
		mRenderer.setChartTitle("");
		mRenderer.setPanEnabled(false);// 禁止拖动图

		mRenderer.setInScroll(true);
		mRenderer.setZoomEnabled(false);
		mRenderer.setIsPeculiarLegendTextColor(true);
		// 消除锯齿
		mRenderer.setAntialiasing(true);
		mRenderer.setLabelsColor(Color.BLACK);
		mRenderer.setClickEnabled(true);
		mRenderer.setShowLabels(true);

		mRenderer.setDrawTextFrame(true);
		mRenderer.setDisplayValues(renderSet[0]);
		mRenderer.setDrawTextFrame(renderSet[1]);
		mRenderer.setShowLableBack(renderSet[2]);
		mRenderer.setShowLegend(true);
		mRenderer.setmLegendType(legendType);
		mRenderer.setLegendTextSize(15);
	}

	public void setData(JSONObject data) {
		super.setData(data);
		if (chartData == null) {
			chartData = new CategorySeries("");
		} else {
			chartData.clear();
		}

		switch (this.mTerminalActivityEnum) {
		case PSS_MONTH_ACTIVITY:
			this.setPSSMonth_PieChartData(this.pieId);
			break;
		case UNSALE_DAY_ACTIVITY:
			this.setUnsaleDay_PieChartData();
			break;
		case UNSALE_MONTH_ACTIVITY:
			this.setUnsaleMonth_PieChartData();
			break;
		default:
			break;
		}

	}

	/**
	 * 滞销（日）的饼图
	 */
	private void setUnsaleDay_PieChartData() {
		this.kpi_statistics = "4630|4640|4650|4660";
		this.initUnsale_PieChart_KPIText();
		String[] kpis = new String[] {
				TerminalConfiguration.KPI_DAILY_UNSALE_4630,
				TerminalConfiguration.KPI_DAILY_UNSALE_4640,
				TerminalConfiguration.KPI_DAILY_UNSALE_4650,
				TerminalConfiguration.KPI_DAILY_UNSALE_4660 };
		String inClause = ArrayUtil.getInClause("kpi_code", kpis);
		String sql = "select KPI_CODE as kpi_code, round(CURDAY_VALUE_DR*100,1) as curday_value_dr from TERMINAL_DAILY_ZX where %s and op_time ='%s' order by curday_value_dr asc";
		sql = String.format(sql, inClause, mTerminalActivityEnum.getOPtime());
		this.drawSimplePieChart(sql, "curday_value_dr");
	}

	private void initUnsale_PieChart_KPIText() {
		this.pieChartKpiText.put(TerminalConfiguration.KPI_DAILY_UNSALE_4630,
				"7-12个月");
		this.pieChartKpiText.put(TerminalConfiguration.KPI_DAILY_UNSALE_4640,
				"13-18个月");
		this.pieChartKpiText.put(TerminalConfiguration.KPI_DAILY_UNSALE_4650,
				"19-24个月");
		this.pieChartKpiText.put(TerminalConfiguration.KPI_DAILY_UNSALE_4660,
				"大于24个月");

		this.pieChartKpiText.put(TerminalConfiguration.KPI_MONTH_UNSALE_5170,
				"7-12个月");
		this.pieChartKpiText.put(TerminalConfiguration.KPI_MONTH_UNSALE_5180,
				"13-18个月");
		this.pieChartKpiText.put(TerminalConfiguration.KPI_MONTH_UNSALE_5190,
				"19-24个月");
		this.pieChartKpiText.put(TerminalConfiguration.KPI_MONTH_UNSALE_5200,
				"大于24个月");
	}

	/**
	 * 滞销（月）的饼图
	 */
	private void setUnsaleMonth_PieChartData() {
		this.kpi_statistics = "5170|5180|5190|5200";
		this.initUnsale_PieChart_KPIText();
		String[] kpis = new String[] {
				TerminalConfiguration.KPI_MONTH_UNSALE_5170,
				TerminalConfiguration.KPI_MONTH_UNSALE_5180,
				TerminalConfiguration.KPI_MONTH_UNSALE_5190,
				TerminalConfiguration.KPI_MONTH_UNSALE_5200 };
		String inClause = ArrayUtil.getInClause("kpi_code", kpis);
		String sql = "select KPI_CODE as kpi_code, round(CURMONTH_VALUE_DR*100,1) as curmonth_value_dr from TERMINAL_MONTH_TERM where %s and op_time ='%s' order by curmonth_value_dr asc";
		sql = String.format(sql, inClause, mTerminalActivityEnum.getOPtime());
		this.drawSimplePieChart(sql, "curmonth_value_dr");
	}

	/**
	 * 进销存（月） 里面的饼图
	 * 
	 * @param pieId
	 */
	private void setPSSMonth_PieChartData(String pieId) {
		if (TerminalConfiguration.POS_MONTH_PSS_LEFT_PIE.equals(pieId)) {
			this.setPSSMonth_LeftPieChartData();
		} else if (TerminalConfiguration.POS_MONTH_PSS_RIGHT_PIE.equals(pieId)) {
			this.setPSSMonth_RightPieChartData();
		}
	}

	private void initPSSMonth_PieData(String[] kpis) {
		this.initPSSMonth_PieChartKpiCode();

		String inClause = ArrayUtil.getInClause("kpi_code", kpis);
		String sql = "select KPI_CODE as kpi_code, round(CURMONTH_VALUE_DR*100,1) as curmonth_value_dr from TERMINAL_MONTH_TERM where %s and op_time ='%s' order by curmonth_value_dr asc";
		sql = String.format(sql, inClause, mTerminalActivityEnum.getOPtime());
		this.drawSimplePieChart(sql, "curmonth_value_dr");
	}

	private void drawSimplePieChart(String sql, String columnName) {
		List<Map<String, String>> res = new SQLHelper().queryForList(sql, null);
		switch (this.mTerminalActivityEnum) {
		case PSS_MONTH_ACTIVITY:
			if (TerminalConfiguration.POS_MONTH_PSS_LEFT_PIE.equals(pieId)) {
				res = getTermData("data6");
			} else if (TerminalConfiguration.POS_MONTH_PSS_RIGHT_PIE
					.equals(pieId)) {
				res = getTermData("data7");
			}
			break;
		case UNSALE_DAY_ACTIVITY:
			res = getTermData("data5");
			break;
		case UNSALE_MONTH_ACTIVITY:
			res = getTermData("data5");
			break;
		}

		if (res == null || res.size() == 0) {
			return;
		}

		if (chartData != null) {
			chartData.clear();
		}

		mRenderer.removeAllRenderers();

		Random ran = new Random();
		for (int i = 0; i < res.size(); i++) {
			SimpleSeriesRenderer rend = new SimpleSeriesRenderer();
			if (i < colorsLib.length)
				rend.setColor(colorsLib[i]);
			else
				rend.setColor(ran.nextInt(0xffffff) + 0xff000000);
			mRenderer.addSeriesRenderer(rend);
		}

		int len = res.size();
		int med = (len + 1) / 2;
		for (int i = 0; i < med; i++) {
			Map<String, String> re = res.get(i);
			String kpi = re.get("kpi_code");
			String text = pieChartKpiText.get(kpi);
			chartData.add(text, Double.parseDouble(re.get(columnName)));
			int j = i + med;
			if (j < len) {
				re = res.get(j);
				text = pieChartKpiText.get(re.get("kpi_code"));
				chartData.add(text, Double.parseDouble(re.get(columnName)));
			}
		}
	}

	private void setPSSMonth_LeftPieChartData() {
		this.kpi_statistics = "3000|3020|3040|3060";
		String[] leftPieKpis = new String[] {
				TerminalConfiguration.KPI_MONTH_PSS_3000,
				TerminalConfiguration.KPI_MONTH_PSS_3020,
				TerminalConfiguration.KPI_MONTH_PSS_3040,
				TerminalConfiguration.KPI_MONTH_PSS_3060 };
		this.initPSSMonth_PieData(leftPieKpis);
	}

	private void setPSSMonth_RightPieChartData() {
		this.kpi_statistics = "3080|3100|3120|3140|3160";
		String[] rightPieKpis = new String[] {
				TerminalConfiguration.KPI_MONTH_PSS_3080,
				TerminalConfiguration.KPI_MONTH_PSS_3100,
				TerminalConfiguration.KPI_MONTH_PSS_3120,
				TerminalConfiguration.KPI_MONTH_PSS_3140,
				TerminalConfiguration.KPI_MONTH_PSS_3160 };
		this.initPSSMonth_PieData(rightPieKpis);
	}

	private void initPSSMonth_PieChartKpiCode() {
		// left pie chart kpiCode and text map
		if (TerminalConfiguration.POS_MONTH_PSS_LEFT_PIE.equals(pieId)) {
			this.pieChartKpiText.put(TerminalConfiguration.KPI_MONTH_PSS_3000,
					"1000以下");
			this.pieChartKpiText.put(TerminalConfiguration.KPI_MONTH_PSS_3020,
					"1000-2000");
			this.pieChartKpiText.put(TerminalConfiguration.KPI_MONTH_PSS_3040,
					"2000-3000");
			this.pieChartKpiText.put(TerminalConfiguration.KPI_MONTH_PSS_3060,
					"3000以上");
		} else {
			// right pie chart kpiCode and text map
			this.pieChartKpiText.put(TerminalConfiguration.KPI_MONTH_PSS_3080,
					"500以下");
			this.pieChartKpiText.put(TerminalConfiguration.KPI_MONTH_PSS_3100,
					"500-1000");
			this.pieChartKpiText.put(TerminalConfiguration.KPI_MONTH_PSS_3120,
					"1000-2000");
			this.pieChartKpiText.put(TerminalConfiguration.KPI_MONTH_PSS_3140,
					"2000-3000");
			this.pieChartKpiText.put(TerminalConfiguration.KPI_MONTH_PSS_3160,
					"3000以上");
		}
	}

	public void updateView() {
		if (chartData == null
				|| mRenderer == null
				|| chartData.getItemCount() != mRenderer
						.getSeriesRendererCount()) {
			LogUtil.error("***********",
					"chartdata = " + chartData.getItemCount());
			LogUtil.error("***********",
					"mRenderer = " + mRenderer.getSeriesRendererCount());
			chartLay.removeAllViews();
		} else {
			chartView = ChartFactory.getModulePieChartView(this.context,
					chartData, mRenderer);
			chartLay.removeAllViews();
			chartLay.addView(chartView);
		}
	}

	public void setLableString(String[] strings) {
		// TODO Auto-generated method stub
		mLableStrings = strings;
	}

}
