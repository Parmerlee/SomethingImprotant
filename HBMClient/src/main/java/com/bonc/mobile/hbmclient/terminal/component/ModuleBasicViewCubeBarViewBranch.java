package com.bonc.mobile.hbmclient.terminal.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.data.ComplexDimInfo;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;
import com.bonc.mobile.hbmclient.terminal.subactivity.TerminalPriceBracketActivity;
import com.bonc.mobile.hbmclient.util.NumberUtil;

public class ModuleBasicViewCubeBarViewBranch extends ViewBranch {

	// config
	private BusinessDao businessdao = new BusinessDao();
	private Map<String, String> userinfo = businessdao.getUserInfo(); // 区域用户信息

	int xLableAngle = 0;

	public int getxLableAngle() {
		return xLableAngle;
	}

	public void setxLableAngle(int xLableAngle) {
		this.xLableAngle = xLableAngle;
	}

	int showW = 0;// 柱图显示区域宽度
	int lineW = 1;// 柱宽
	int pointSize = 2;// 点宽
	TextView /* mccTitleTextView, */mccTargeTextView, mccUnitTextView;// 模块标题，显示指标，指标单位所在View
	String title, target, unit;// 模块标题，显示指标，指标单位

	LinearLayout barLayout;// 图显示所在布局
	XYMultipleSeriesDataset dataset;

	public List<Map<String, String>> ser1Datas;// 柱图数据（或饼图数据）

	public List<Map<String, String>> ser2Datas;// 折线图图数据

	private DateRangeEnum mCType = DateRangeEnum.DAY;// 指定是数据周期类型为日/月/年/百年等等

	public DateRangeEnum getmCType() {
		return mCType;
	}

	public void setmCType(DateRangeEnum mCType) {
		this.mCType = mCType;
	}

	XYMultipleSeriesRenderer renderer;
	private double maxYValue = 0; // 数据的最大值
	public double minYValue = 0; // 数据的最小值
	public static final int BARW = 10;// 柱图间隔值
	public double unitDiv = 1;

	public ModuleBasicViewCubeBarViewBranch(Context c, TerminalActivityEnum tae) {
		super(c, tae);
		// TODO Auto-generated constructor stub

	}

	@Override
	public void setViewListener() {
		this.mView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(context,
						TerminalPriceBracketActivity.class);
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
				case PSS_DAY_ACTIVITY:
					title_big = "八大渠道占比分析";
					menuCode = TerminalConfiguration.KEY_MENU_CODE_PSS_DAY;
					dimkey = businessdao.getMenuComplexDimKey(businessdao
							.getMenuInfo(menuCode));
					param.put(TerminalConfiguration.KEY_KPI_CODES,
							TerminalConfiguration.KPI_PSS_DAY_8);
					param.put(TerminalConfiguration.KEY_COLUMN_NAME,
							TerminalConfiguration.CURDAY_VALUE_DR); // 列
					param.put(TerminalConfiguration.KEY_DATATABLE,
							businessdao.getMenuColDataTable(menuCode));
					param.put(TerminalConfiguration.KEY_DIM_KEY,
							dimkey.getComplexDimKeyString());
					param.put(TerminalConfiguration.KEY_ISEXPAND, "1");
					intent.putExtra(TerminalConfiguration.KEY_COLUNM_KPI_CODE,
							TerminalConfiguration.COLUMN_KPI_CODE_PSS_DAY_8);
					action = "palmBiTermDataAction_manyKpiManyAreaManyTimeDataQuery.do";
					break;
				case PSS_MONTH_ACTIVITY:
					menuCode = TerminalConfiguration.KEY_MENU_CODE_PSS_MONTH;
					dimkey = businessdao.getMenuComplexDimKey(businessdao
							.getMenuInfo(menuCode));
					param.put(TerminalConfiguration.KEY_OPTIME,
							mTerminalActivityEnum.getOPtime());
					param.put(TerminalConfiguration.KEY_DATA_TYPE,
							mTerminalActivityEnum.getDateRange().getDateFlag());
					param.put(TerminalConfiguration.KEY_KPI_CODES,
							"3010|3030|3050|3070");
					param.put(TerminalConfiguration.KEY_COLUMN_NAME,
							"curmonth_value"); // 列
					param.put(TerminalConfiguration.KEY_AREA_CODE,
							userinfo.get("areaId")); // 地区id
					param.put(TerminalConfiguration.KEY_DATATABLE,
							businessdao.getMenuColDataTable(menuCode));
					param.put(TerminalConfiguration.KEY_DIM_KEY,
							dimkey.getComplexDimKeyString());
					param.put(TerminalConfiguration.KEY_ISEXPAND, "1");
					action = "palmBiTermDataAction_manyKpiManyAreaManyTimeDataQuery.do";
					break;
				}

				intent.putExtra(TerminalConfiguration.TITLE_COLUMN,
						TerminalConfiguration.TITLE_COLUMN_PSS_DAY_8);
				intent.putExtra(TerminalConfiguration.KEY_RESPONSE_KEY,
						TerminalConfiguration.RESPONSE_PSS_DAY_8_KEY);
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

	public void dispatchView() {
		mccTargeTextView = (TextView) this.mView.findViewById(R.id.mcc_target);
		mccUnitTextView = (TextView) this.mView.findViewById(R.id.mcc_unit);
		barLayout = (LinearLayout) this.mView.findViewById(R.id.mcc_bar);
		setDefaultRender();
		dataset = new XYMultipleSeriesDataset();

		if (showW == 0) {
			WindowManager windowManager = ((Activity) (this.mView.getContext()))
					.getWindowManager();
			showW = windowManager.getDefaultDisplay().getWidth();
		}
	}

	public void setData(JSONObject data) {
		super.setData(data);
		this.kpi_statistics = "4490|4500|4510|4520|4530|4540|4550|4560";
		title = "终端销售八大渠道销量";
		target = "";
		unit = "%";
		unitDiv = 1;
		maxYValue = 0;

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

		String sqlString = "Select C.kPI_NAME as title,round(T.CURDAY_VALUE_DR*100,2) as value From TERMINAL_DAILY_JXC T,TERMINAL_CHANNEL_CODE C"
				+ " WHERE T.KPI_CODE=C.KPI_CODE AND T.OP_TIME=?"
				+ " ORDER BY value desc";

		// List<Map<String, String>> resuList = new SQLHelper().queryForList(
		// sqlString, new String[] { mTerminalActivityEnum.getOPtime() });
		List<Map<String, String>> resuList = getTermData("data7");

		if (resuList == null) {
			resuList = new ArrayList<Map<String, String>>(0);
		}

		ser1Datas.addAll(resuList);

		CategorySeries bar_series = new CategorySeries("");
		// 为柱形图添加值
		int len = ser1Datas.size();

		for (int i = 0; i < len; i++) {

			bar_series.add(NumberUtil.changeToDouble(ser1Datas.get(i).get(
					"value"))
					/ unitDiv);// 除以10000单位变成
								// "万"

		}

		int scale = 1;

		while (scale * 10 < Math.abs(maxYValue)) {
			scale *= 10;
			if (scale >= 100000000) {
				unitDiv = 100000000;
				unit = "亿" + unit + "";
			} else if (scale >= 10000) {
				unitDiv = 10000;
				unit = "万" + unit + "";
			}
		}
		unit = "(" + unit + ")";
		// 获得Y轴最大最小值

		if (dataset != null)
			dataset.removeAll();

		XYSeries series = new XYSeries(title);

		series = bar_series.toXYSeries();
		maxYValue = series.getMaxY();
		minYValue = series.getMinY();
		dataset.addSeries(series);// 添加该柱形图到数据设置列表

		maxYValue = maxYValue / unitDiv;
		minYValue = minYValue / unitDiv;
		maxYValue = maxYValue + (Math.abs(maxYValue) / 7); // 让左边刻度线高出数字的最大值

		minYValue = minYValue - (Math.abs(minYValue) / 7);
	}

	public void updateView() {
		mccTargeTextView.setText(target);
		mccUnitTextView.setText(unit);

		if (ser1Datas != null) {

			updateRender();

			GraphicalView view = null;

			view = ChartFactory.getBarChartView(this.mView.getContext(),
					dataset, renderer, Type.STACKED);

			barLayout.removeAllViews();
			barLayout.addView(view);

		}
	}

	private void updateRender() {
		if (this.ser1Datas == null || this.ser1Datas.size() == 0)
			return;

		renderer.clearXTextLabels();
		renderer.clearYTextLabels();

		// TODO Auto-generated method stub
		renderer.setXAxisMin(0.4);// 设置X轴的最小值为0.5
		renderer.setXAxisMax(ser1Datas.size() + 1);// 设置X轴的最大值为
		renderer.setYAxisMin(minYValue);// 设置Y轴的最小值为0
		renderer.setYAxisMax(maxYValue);// 设置Y轴最大值为500
		renderer.setXLabelsAngle(xLableAngle);

		int len = ser1Datas.size();
		for (int i = 0; i < len; i++) {
			renderer.addXTextLabel(i + 1, ser1Datas.get(i).get("title"));

		}
		int barW = BARW;
		if (len < 13 && showW != 0) {
			barW = (showW / len) / 3;
			barW = barW > 50 ? 50 : barW;
		}
		renderer.setBarWidth(barW);
		renderer.setMargins(new int[] { 16, 10, 0, 0 });

	}

	private void setDefaultRender() {

		renderer = new XYMultipleSeriesRenderer();
		SimpleSeriesRenderer r = new SimpleSeriesRenderer();
		// 柱子添加蓝色渐变效果
		r.setColor(Color.rgb(0, 171, 245));
		r.setGradientEnabled(true);
		r.setGradientStop(0, 0xff1eaef1);
		r.setGradientStart(0, 0xff1950da);
		r.setShowBarShadow(true);
		r.setShadowColor(0xff101010);
		renderer.removeAllRenderers();
		renderer.addSeriesRenderer(r);
		renderer.setDisplayChartValues(false); // 设置是否在柱体上方显示值
		renderer.setShowLegend(false);
		renderer.setShowGrid(true);// 设置是否在图表中显示网格
		renderer.setXLabels(0);// 设置X轴显示的刻度标签的个数
		renderer.setBarSpacing(0.1f); // 柱状间的间隔
		renderer.setPanEnabled(false, false);// 允许左右拖动,但不允许上下拖动.
		renderer.setZoomEnabled(false, false);// .setZoomEnabled(true);
		renderer.setLabelsColor(0xff000000);
		renderer.setYLabelsColor(0, 0xff000000);
		renderer.setXLabelsColor(0xff000000);
		renderer.setLabelsTextSize((float) 10);
		renderer.setInScroll(true);
		renderer.setBarWidth(BARW);
		switch (this.mTerminalActivityEnum) {
		case PSS_DAY_ACTIVITY:
		case PSS_MONTH_ACTIVITY:
			renderer.setLabelsTextSize((float) 15);
			break;

		default:
			break;
		}
	}

}