package com.bonc.mobile.portal.search;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.anhuimobile.ac.R;
import com.bonc.mobile.common.activity.BaseDataActivity;
import com.bonc.mobile.common.chart.BarChartView;
import com.bonc.mobile.common.chart.SimpleChartData;
import com.bonc.mobile.common.kpi.KpiConstant;
import com.bonc.mobile.common.kpi.KpiDataComparator;
import com.bonc.mobile.common.kpi.KpiDataModel;
import com.bonc.mobile.common.kpi.KpiDataView;
import com.bonc.mobile.common.kpi.SimpleKpiTitleRow.OnColumnSortListener;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.NumberUtil;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.common.view.ACLineChartView;
import com.bonc.mobile.common.view.ArcMenu;
import com.bonc.mobile.common.view.ArcMenu.OnMenuItemClickListener;
import com.bonc.mobile.common.view.FlowLayout;

public class SearchDetailActivity extends BaseDataActivity implements
		OnItemClickListener, OnMenuItemClickListener {

	private Activity activity;
	FlowLayout rela_layout;
	TextView word_title, word_content;

	// 标题栏名（只包括右侧滑动部分的指标名称）
	private String[] titleName;
	// 左侧列表
	// private ListView area_left_listView;
	// // 右侧列表
	// private ListView area_right_listView;
	// // // 列表相应的Adapter
	// private KpiAreaLeftAdapter leftAdapter;
	// private KpiAreaRightAdapter rightAdapter;
	// 相关滑动用组件
	// private ListViewSetting listViewSetting;

	private List<Map<String, String>> BarData; // 柱图数据.

	// 柱状图、左右联动listview相关
	protected KpiDataModel dataModel;
	// 柱状图
	protected BarChartView bar_chart;
	// 联动listview
	protected KpiDataView dataView;
	// ---end

	// 折线图
	ACLineChartView line_chart;

	// 卫星菜单
	private ArcMenu mArcMenu;

	private LinearLayout pie_chart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_detail);

		activity = this;

		initView();

		initData();

	}

	// private LinearLayout ll_dev_area_bar;

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();

		pie_chart = (LinearLayout) this.findViewById(R.id.pie_chart);

		date_button.setWithTime(true);

		date_button.setPattern("yyyy/MM/dd HH时");

		TextViewUtils.setText(activity, R.id.title, "详情");

		UIUtil.setWatermarkImage(activity, android.R.id.content);

		rela_layout = (FlowLayout) this.findViewById(R.id.search_fl_hot);
		word_title = (TextView) this.findViewById(R.id.perfer_item_title);
		word_content = (TextView) this.findViewById(R.id.perfer_item_content);

		// area_left_listView = (ListView)
		// findViewById(R.id.mon_area_left_listview);
		// area_right_listView = (ListView)
		// findViewById(R.id.mon_area_right_listview);
		// area_left_listView.setOnItemClickListener(this);
		// area_right_listView.setOnItemClickListener(this);
		//
		// ll_dev_area_bar = (LinearLayout) findViewById(R.id.ll_dev_area_bar);

		bar_chart = (BarChartView) findViewById(R.id.bar_chart);
		dataView = (KpiDataView) findViewById(R.id.data_view);

		mArcMenu = (ArcMenu) this.findViewById(R.id.arcmenu_search);
	}

	private List<Map<String, Object>> data_hot;

	private void initData() {
		// TODO Auto-generated method stub

		data_hot = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 25; i++) {
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("text", "测试数据:" + i);
			// data_his.add(temp);
			data_hot.add(temp);
		}
		for (int i = 0; i < data_hot.size(); i++) {
			TextView tv = (TextView) LayoutInflater.from(
					getApplicationContext()).inflate(R.layout.flowlayout_item,
					rela_layout, false);
			tv.setText(data_hot.get(i).get("text").toString());

			tv.setTag(R.id.tag_search_hot, data_hot.get(i));
			tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					@SuppressWarnings("unchecked")
					Map<String, Object> temp = (Map<String, Object>) v
							.getTag(R.id.tag_search_hot);
					Toast.makeText(getApplicationContext(),
							temp.get("text").toString(), 5).show();
				}
			});
			rela_layout.addView(tv);
		}

		word_title.setText(Html.fromHtml("shouru"));

		word_content
				.setText(Html
						.fromHtml("测试内容啊对方的方法对方的，测试自定义标签：<h1><font color=\"#aabb00\">测试自定义标签</h1>"));

		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 4; i++) {
		//	list.add(R.drawable.composer_camera);
		}
		mArcMenu.setDates(R.id.tag_arcmenu, list);

		mArcMenu.setOnMenuItemClickListener(this);

		bindChartData();

		buildPieLayout();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// switch (parent.getId()) {
		// case R.id.mon_area_left_listview:
		// if (listViewSetting.leftTouchFlag == 1) {
		//
		// }
		// break;
		//
		// case R.id.mon_area_right_listview:
		// if (listViewSetting.leftTouchFlag == 0) {
		//
		// }
		// break;
		//
		// default:
		// break;
		// }

	}

	// 柱状图
	void bindChartData() {
		// String val_col = model.getColumnKey(0);
		// double scale = NumberUtil.getScale(NumberUtil.getAverage(DataUtil
		// .extractValArray(model.getKpiDataList(), val_col)));
		// String unit = model.getColumnInfo(0, KpiConstant.KEY_KPIVALUE_UNIT);
		// TextViewUtils.setText(this, R.id.tv_w_unit, NumberUtil.getUnit(scale)
		// + unit);
		// SimpleChartData chartData = SimpleChartData.build(
		// model.getKpiDataList(), KpiConstant.KEY_AREA_NAME, val_col,
		// SimpleChartData.getConverter(scale));
		// bar_chart.setData(chartData);

		String val_col = "CURDAY_VALUE";
		double scale = 10000.0;
		String unit = "";
		TextViewUtils.setText(this, R.id.tv_w_unit, NumberUtil.getUnit(scale)
				+ unit);

		SimpleChartData chartData = SimpleChartData.build(getDate(),
				KpiConstant.KEY_AREA_NAME, val_col,
				SimpleChartData.getConverter(scale));
		bar_chart.setData(chartData);
	}

	String chartCol;

	// 折线图
	private void bindLineChartData(JSONObject result) {
		List<Map<String, String>> colInfo = JsonUtil.toList(result
				.optJSONArray("showColumn"));
		chartCol = colInfo.get(0).get(KpiConstant.KEY_RELATION_KPIVALUE_COLUMN);
		Map<String, String> kpiInfo = JsonUtil.toMap(result
				.optJSONObject("kpiInfo"));
		List<Map<String, String>> pre = result.isNull("pre") ? new ArrayList<Map<String, String>>()
				: JsonUtil.toList(result.optJSONArray("pre"));
		List<Map<String, String>> current = JsonUtil.toList(result
				.optJSONArray("current"));
		String[] labels = KpiDataModel.getTimeLabels(current.size() >= pre
				.size() ? current : pre);
		double[] currValue = DataUtil.extractValArray(current, chartCol);
		double[] lastValue = DataUtil.extractValArray(pre, chartCol);
		if (!TextUtils.isEmpty(kpiInfo.get("EXT2"))) {
			line_chart.setYAxis(kpiInfo.get("EXT2"));
		}
		line_chart.setData(labels, currValue, lastValue,
				kpiInfo.get(KpiConstant.KEY_KPI_UNIT));
		line_chart.setLineTitle(kpiInfo.get(KpiConstant.KEY_KPI_NAME));
		// if (monthQuery) {
		// line_chart.setLine1Name("本年");
		// line_chart.setLine2Name("上年");
		// } else {
		// line_chart.setLine1Name("本月");
		// line_chart.setLine2Name("上月");
		// }
	}

	// 构建左右联动listview数据
	private void bindListData(JSONObject result) {

		KpiDataModel model = new KpiDataModel();
		model.setType(1);
		model.setKpiCode("DQ020001");
		model.build(result);
		model.setColInfoLeft(KpiDataModel.COL_INFO_LEFT_AREA);
		KpiDataModel.mergeRuleUnit(model.getColumnInfoRight(),
				model.getKpiConfig("DQ020001"));
		dataModel = model;
		if (model.getKpiDataList().size() == 0) {
			showToast(getString(R.string.no_data));
		} else {
			// bindTopData(model);
			// bindChartData(model);
		}
		dataView.setModel(model);
		dataView.getRightKpiTitle().setOnColumnSortListener(
				new OnColumnSortListener() {
					@Override
					public void onColumnSort(int index, int direction) {
						sortDataView(index, direction);
					}
				});
	}

	private static int[] COLORS = new int[] { Color.RED, Color.GREEN,
			Color.BLUE, Color.MAGENTA, Color.CYAN, Color.YELLOW, Color.DKGRAY };
	double data[] = new double[] { 20, 30, 40, 50, 60, 70, 80, 90, 100 };

	private CategorySeries mSeries = new CategorySeries("");// PieChart的DataSet
															// 其实就是一些键值对，跟Map使用方法差不多

	private DefaultRenderer mRenderer = new DefaultRenderer();// PieChart的主要描绘器

	private GraphicalView mChartView;// 用来显示PieChart

	private LinearLayout mLinear;

	private static double VALUE = 0;// 总数

	// 绘制饼状图
	private void buildPieLayout() {

		// mRenderer.setZoomButtonsVisible(true);// 显示放大缩小功能按钮
		mRenderer.setStartAngle(315);// 设置为水平开始
		mRenderer.setDisplayValues(true);// 显示数据
		mRenderer.setFitLegend(false);// 设置是否显示图例
		// mRenderer.setLegendTextSize(30);// 设置图例字体大小
		// mRenderer.setLegendHeight(30);// 设置图例高度
		// mRenderer.setChartTitle("饼图示例");// 设置饼图标题
		mRenderer.setPanEnabled(false); // 设置不允许放大缩小.
		// mRenderer.setChartTitleTextSize(30); // 标题文字大小
		mRenderer.setLabelsColor(Color.WHITE);// 饼图上标记文字的颜色

		// mRenderer.setIsPeculiarLegendTextColor(true);
		mRenderer.setInScroll(true);
		mRenderer.setShowLegend(false); // 设置不显示左下角示例文字
		mRenderer.setFitLegend(true);
		mRenderer.setAntialiasing(true);
		mRenderer.setMargins(new int[] { 3, 3, 3, 3 });
		mRenderer.setZoomEnabled(false);// 设置不允许放大缩小.

		mRenderer.setShowLableBack(true);
		mRenderer.setDrawTextFrame(true);
		for (int i = 0; i < data.length; i++)
			VALUE += data[i];
		for (int i = 0; i < data.length; i++) {
			mSeries.add("示例 " + (i + 1), data[i] / VALUE);// 设置种类名称和对应的数值，前面是（key,value）键值对
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			if (i < COLORS.length) {
				renderer.setColor(COLORS[i]);// 设置描绘器的颜色
			} else {
				renderer.setColor(getRandomColor());// 设置描绘器的颜色
			}
			renderer.setChartValuesFormat(NumberFormat.getPercentInstance());// 设置百分比
			mRenderer.setChartTitleTextSize(14);// 设置饼图标题大小
			mRenderer.addSeriesRenderer(renderer);// 将最新的描绘器添加到DefaultRenderer中
			if (mChartView == null) {// 为空需要从ChartFactory获取PieChartView
				mChartView = ChartFactory.getPieChartView(
						getApplicationContext(), mSeries, mRenderer);// 构建mChartView
				mRenderer.setClickEnabled(true);// 允许点击事件
				mChartView.setOnClickListener(new OnClickListener() {// 具体内容
							@Override
							public void onClick(View v) {
								SeriesSelection seriesSelection = mChartView
										.getCurrentSeriesAndPoint();// 获取当前的类别和指针
								if (seriesSelection == null) {
									// Toast.makeText(getApplicationContext(),
									// "您未选择数据", Toast.LENGTH_SHORT)
									// .show();
								} else {
									for (int i = 0; i < mSeries.getItemCount(); i++) {
										mRenderer
												.getSeriesRendererAt(i)
												.setHighlighted(
														i == seriesSelection
																.getPointIndex());
									}
									mChartView.repaint();

									Toast.makeText(
											getApplicationContext(),
											mSeries.getCategory(seriesSelection
													.getPointIndex())
													+ " 百分比为  :"
													+ NumberFormat
															.getPercentInstance()
															.format(seriesSelection
																	.getValue()),
											Toast.LENGTH_SHORT).show();
								}
							}
						});
				pie_chart.addView(mChartView, new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			} else {
				mChartView.repaint();
			}
		}

		// double[] values = { 412.0, 542.0, 486.0, 900.1 };
		// CategorySeries dataset = buildCategoryDataset("测试饼图", values);
		//
		// int[] colors = { Color.BLUE, Color.GREEN, Color.MAGENTA, Color.RED };
		// DefaultRenderer renderer = buildCategoryRenderer(colors);
		//
		// graphicalView = ChartFactory.getPieChartView(getBaseContext(),
		// dataset,
		// renderer);// 饼状图
		//
		// pie_chart.removeAllViews();
		// pie_chart.setBackgroundColor(Color.BLACK);
		// pie_chart.addView(graphicalView, new LayoutParams(
		// LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	private int getRandomColor() {// 分别产生RBG数值
		Random random = new Random();
		int R = random.nextInt(255);
		int G = random.nextInt(255);
		int B = random.nextInt(255);
		return Color.rgb(R, G, B);
	}

	// 配置饼图data信息
	protected CategorySeries buildCategoryDataset(String title, double[] values) {
		CategorySeries series = new CategorySeries(title);
		series.add("差", values[0]);
		series.add("不达标", values[1]);
		series.add("达标", values[2]);
		series.add("优秀", values[3]);
		return series;
	}

	// 设置renderer的方法--饼图
	protected DefaultRenderer buildCategoryRenderer(int[] colors) {
		DefaultRenderer renderer = new DefaultRenderer();

		renderer.setStartAngle(180);
		renderer.setLegendTextSize(20);// 设置左下角表注的文字大小
		renderer.setFitLegend(false);
		// renderer.setLabelsTextSize(30);
		renderer.setZoomButtonsVisible(true);// 设置显示放大缩小按钮
		renderer.setZoomEnabled(false);// 设置不允许放大缩小.
		renderer.setChartTitleTextSize(30);// 设置图表标题的文字大小
		renderer.setChartTitle("统计结果");// 设置图表的标题 默认是居中顶部显示
		renderer.setLabelsTextSize(50);// 饼图上标记文字的字体大小
		renderer.setLabelsColor(Color.WHITE);// 饼图上标记文字的颜色
		// renderer.setPanEnabled(false);// 设置是否可以平移
		renderer.setDisplayValues(true);// 是否显示值
		renderer.setDrawTextFrame(true);
		renderer.setClickEnabled(true);// 设置是否可以被点击
		renderer.setMargins(new int[] { 20, 30, 15, 0 });
		// margins - an array containing the margin size values, in this order:
		// top, left, bottom, right
		for (int color : colors) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(color);
			r.setChartValuesFormat(NumberFormat.getPercentInstance());
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

	// 配合listview的点击排序
	void sortDataView(int index, int direction) {
		KpiDataComparator comparator = new KpiDataComparator(
				dataModel.getColumnKey(index), direction);
		Collections.sort(dataModel.getKpiDataList(), comparator);
		dataModel.syncKpiData(1);
		dataView.updateView();
	}

	private List<Map<String, String>> getDate() {
		// [{AREA_CODE=HB.WH, CD_COL=0.2341, CURDAY_VALUE=649639,
		// CD_MYOY=-0.4271, KPI_CODE=DQ020003, AREA_NAME=武汉},
		// {AREA_CODE=HB.YC, CD_COL=0, CURDAY_VALUE=179625, CD_MYOY=-0.2762,
		// KPI_CODE=DQ020003, AREA_NAME=宜昌},
		// {AREA_CODE=HB.ES, CD_COL=-0.0705, CURDAY_VALUE=81792,
		// CD_MYOY=-0.3645, KPI_CODE=DQ020003, AREA_NAME=恩施},
		// {AREA_CODE=HB.SY, CD_COL=-0.0726, CURDAY_VALUE=153571,
		// CD_MYOY=-0.3735, KPI_CODE=DQ020003, AREA_NAME=十堰},
		// {AREA_CODE=HB.HS, CD_COL=-0.0909, CURDAY_VALUE=109758,
		// CD_MYOY=-0.363, KPI_CODE=DQ020003, AREA_NAME=黄石},
		// {AREA_CODE=HB.XF, CD_COL=-0.0925, CURDAY_VALUE=274547,
		// CD_MYOY=-0.2562, KPI_CODE=DQ020003, AREA_NAME=襄阳},
		// {AREA_CODE=HB.EZ, CD_COL=-0.1015, CURDAY_VALUE=49478,
		// CD_MYOY=-0.3576, KPI_CODE=DQ020003, AREA_NAME=鄂州},
		// {AREA_CODE=HB.JM, CD_COL=-0.1143, CURDAY_VALUE=98095,
		// CD_MYOY=-0.3782, KPI_CODE=DQ020003, AREA_NAME=荆门},
		// {AREA_CODE=HB.JZ, CD_COL=-0.1318, CURDAY_VALUE=155327,
		// CD_MYOY=-0.396, KPI_CODE=DQ020003, AREA_NAME=荆州},
		// {AREA_CODE=HB.XG, CD_COL=-0.1764, CURDAY_VALUE=162878,
		// CD_MYOY=-0.4042, KPI_CODE=DQ020003, AREA_NAME=孝感},
		// {AREA_CODE=HB.HG, CD_COL=-0.2261, CURDAY_VALUE=150902,
		// CD_MYOY=-0.4139, KPI_CODE=DQ020003, AREA_NAME=黄冈},
		// {AREA_CODE=HB.SZ, CD_COL=-0.2522, CURDAY_VALUE=73728,
		// CD_MYOY=-0.3286, KPI_CODE=DQ020003, AREA_NAME=随州},
		// {AREA_CODE=HB.QJ, CD_COL=-0.256, CURDAY_VALUE=26638, CD_MYOY=-0.3653,
		// KPI_CODE=DQ020003, AREA_NAME=潜江},
		// {AREA_CODE=HB.JH, CD_COL=-0.2838, CURDAY_VALUE=39023,
		// CD_MYOY=-0.3956, KPI_CODE=DQ020003, AREA_NAME=江汉},
		// {AREA_CODE=HB.XN, CD_COL=-0.3086, CURDAY_VALUE=56752,
		// CD_MYOY=-0.5682, KPI_CODE=DQ020003, AREA_NAME=咸宁},
		// {AREA_CODE=HB.TM, CD_COL=-0.4524, CURDAY_VALUE=23005,
		// CD_MYOY=-0.1698, KPI_CODE=DQ020003, AREA_NAME=天门}]
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		// [{AREA_CODE=HB.WH, CD_COL=0.2341, CURDAY_VALUE=649639,
		// CD_MYOY=-0.4271, KPI_CODE=DQ020003, AREA_NAME=武汉},
		map.put("AREA_CODE", "HB.WH");
		map.put("CD_COL", "0.2341");
		map.put("CURDAY_VALUE", "649639");
		map.put("CD_MYOY", "--0.4271");
		map.put("KPI_CODE", "DQ020003");
		map.put("AREA_NAME", "武汉");
		data.add(map);

		// {AREA_CODE=HB.YC, CD_COL=0, CURDAY_VALUE=179625, CD_MYOY=-0.2762,
		// KPI_CODE=DQ020003, AREA_NAME=宜昌},
		map = new HashMap<String, String>();
		map.put("AREA_CODE", "HB.YC");
		map.put("CD_COL", "0");
		map.put("CURDAY_VALUE", "179625");
		map.put("CD_MYOY", "-0.2762");
		map.put("KPI_CODE", "DQ020003");
		map.put("AREA_NAME", "宜昌");
		data.add(map);

		// {AREA_CODE=HB.ES, CD_COL=-0.0705, CURDAY_VALUE=81792,
		// CD_MYOY=-0.3645, KPI_CODE=DQ020003, AREA_NAME=恩施},
		map = new HashMap<String, String>();
		map.put("AREA_CODE", "HB.ES");
		map.put("CD_COL", "-0.0705");
		map.put("CURDAY_VALUE", "81792");
		map.put("CD_MYOY", "-0.3645");
		map.put("KPI_CODE", "DQ020003");
		map.put("AREA_NAME", "恩施");
		data.add(map);

		// {AREA_CODE=HB.SY, CD_COL=-0.0726, CURDAY_VALUE=153571,
		// CD_MYOY=-0.3735, KPI_CODE=DQ020003, AREA_NAME=十堰},
		map = new HashMap<String, String>();
		map.put("AREA_CODE", "HB.SY");
		map.put("CD_COL", "-0.0726");
		map.put("CURDAY_VALUE", "153571");
		map.put("CD_MYOY", "-0.3735");
		map.put("KPI_CODE", "DQ020003");
		map.put("AREA_NAME", "十堰");
		data.add(map);

		// {AREA_CODE=HB.HS, CD_COL=-0.0909, CURDAY_VALUE=109758,
		// CD_MYOY=-0.363, KPI_CODE=DQ020003, AREA_NAME=黄石},
		map = new HashMap<String, String>();
		map.put("AREA_CODE", "HB.HS");
		map.put("CD_COL", "-0.0909");
		map.put("CURDAY_VALUE", "109758");
		map.put("CD_MYOY", "-0.363");
		map.put("KPI_CODE", "DQ020003");
		map.put("AREA_NAME", "黄石");
		data.add(map);

		// {AREA_CODE=HB.XF, CD_COL=-0.0925, CURDAY_VALUE=274547,
		// CD_MYOY=-0.2562, KPI_CODE=DQ020003, AREA_NAME=襄阳},

		map = new HashMap<String, String>();
		map.put("AREA_CODE", "HB.XF");
		map.put("CD_COL", "-0.0925");
		map.put("CURDAY_VALUE", "274547");
		map.put("CD_MYOY", "-0.2562");
		map.put("KPI_CODE", "DQ020003");
		map.put("AREA_NAME", "襄阳");
		data.add(map);

		// {AREA_CODE=HB.EZ, CD_COL=-0.1015, CURDAY_VALUE=49478,
		// CD_MYOY=-0.3576, KPI_CODE=DQ020003, AREA_NAME=鄂州},

		map = new HashMap<String, String>();
		map.put("AREA_CODE", "HB.EZ");
		map.put("CD_COL", "-0.1015");
		map.put("CURDAY_VALUE", "49478");
		map.put("CD_MYOY", "-0.3576");
		map.put("KPI_CODE", "DQ020003");
		map.put("AREA_NAME", "鄂州");
		data.add(map);

		// {AREA_CODE=HB.JM, CD_COL=-0.1143, CURDAY_VALUE=98095,
		// CD_MYOY=-0.3782, KPI_CODE=DQ020003, AREA_NAME=荆门},
		map = new HashMap<String, String>();
		map.put("AREA_CODE", "HB.JM");
		map.put("CD_COL", "-0.1143");
		map.put("CURDAY_VALUE", "98095");
		map.put("CD_MYOY", "-0.3782");
		map.put("KPI_CODE", "DQ020003");
		map.put("AREA_NAME", "荆门");
		data.add(map);

		// {AREA_CODE=HB.JZ, CD_COL=-0.1318, CURDAY_VALUE=155327,
		// CD_MYOY=-0.396, KPI_CODE=DQ020003, AREA_NAME=荆州},
		map = new HashMap<String, String>();
		map.put("AREA_CODE", "HB.JZ");
		map.put("CD_COL", "-0.1318");
		map.put("CURDAY_VALUE", "155327");
		map.put("CD_MYOY", "-0.396");
		map.put("KPI_CODE", "DQ020003");
		map.put("AREA_NAME", "荆州");
		data.add(map);

		// {AREA_CODE=HB.XG, CD_COL=-0.1764, CURDAY_VALUE=162878,
		// CD_MYOY=-0.4042, KPI_CODE=DQ020003, AREA_NAME=孝感},

		map = new HashMap<String, String>();
		map.put("AREA_CODE", "HB.XG");
		map.put("CD_COL", "-0.1764");
		map.put("CURDAY_VALUE", "162878");
		map.put("CD_MYOY", "-0.4042");
		map.put("KPI_CODE", "DQ020003");
		map.put("AREA_NAME", "孝感");
		data.add(map);

		// {AREA_CODE=HB.HG, CD_COL=-0.2261, CURDAY_VALUE=150902,
		// CD_MYOY=-0.4139, KPI_CODE=DQ020003, AREA_NAME=黄冈},

		map = new HashMap<String, String>();
		map.put("AREA_CODE", "HB.HG");
		map.put("CD_COL", "-0.2261");
		map.put("CURDAY_VALUE", "150902");
		map.put("CD_MYOY", "-0.4139");
		map.put("KPI_CODE", "DQ020003");
		map.put("AREA_NAME", "黄冈");
		data.add(map);

		// {AREA_CODE=HB.SZ, CD_COL=-0.2522, CURDAY_VALUE=73728,
		// CD_MYOY=-0.3286, KPI_CODE=DQ020003, AREA_NAME=随州},
		map = new HashMap<String, String>();
		map.put("AREA_CODE", "HB.SZ");
		map.put("CD_COL", "-0.2522");
		map.put("CURDAY_VALUE", "73728");
		map.put("CD_MYOY", "-0.3286");
		map.put("KPI_CODE", "DQ020003");
		map.put("AREA_NAME", "随州");
		data.add(map);

		// {AREA_CODE=HB.QJ, CD_COL=-0.256, CURDAY_VALUE=26638, CD_MYOY=-0.3653,
		// KPI_CODE=DQ020003, AREA_NAME=潜江},

		map = new HashMap<String, String>();
		map.put("AREA_CODE", "HB.QJ");
		map.put("CD_COL", "-0.256");
		map.put("CURDAY_VALUE", "26638");
		map.put("CD_MYOY", "-0.3653");
		map.put("KPI_CODE", "DQ020003");
		map.put("AREA_NAME", "潜江");
		data.add(map);

		// {AREA_CODE=HB.JH, CD_COL=-0.2838, CURDAY_VALUE=39023,
		// CD_MYOY=-0.3956, KPI_CODE=DQ020003, AREA_NAME=江汉},

		map = new HashMap<String, String>();
		map.put("AREA_CODE", "HB.JH");
		map.put("CD_COL", "-0.2838");
		map.put("CURDAY_VALUE", "39023");
		map.put("CD_MYOY", "-0.3956");
		map.put("KPI_CODE", "DQ020003");
		map.put("AREA_NAME", "江汉");
		data.add(map);

		// {AREA_CODE=HB.XN, CD_COL=-0.3086, CURDAY_VALUE=56752,
		// CD_MYOY=-0.5682, KPI_CODE=DQ020003, AREA_NAME=咸宁},

		map = new HashMap<String, String>();
		map.put("AREA_CODE", "HB.XN");
		map.put("CD_COL", "-0.3086");
		map.put("CURDAY_VALUE", "56752");
		map.put("CD_MYOY", "-0.5682");
		map.put("KPI_CODE", "DQ020003");
		map.put("AREA_NAME", "咸宁");
		data.add(map);

		// {AREA_CODE=HB.TM, CD_COL=-0.4524, CURDAY_VALUE=23005,
		// CD_MYOY=-0.1698, KPI_CODE=DQ020003, AREA_NAME=天门}]
		map = new HashMap<String, String>();
		map.put("AREA_CODE", "HB.TM");
		map.put("CD_COL", "-0.4524");
		map.put("CURDAY_VALUE", "23005");
		map.put("CD_MYOY", "-0.1698");
		map.put("KPI_CODE", "DQ020003");
		map.put("AREA_NAME", "天门");
		data.add(map);

		// map = new HashMap<String, String>();
		// map.put("AREA_CODE", "HB.TM");
		// map.put("CD_COL", "-0.4524");
		// map.put("CURDAY_VALUE", "23005");
		// map.put("CD_MYOY", "-0.1698");
		// map.put("KPI_CODE", "DQ020003");
		// map.put("AREA_NAME", "天门");
		// data.add(map);

		// Log.d("AAA", data.toString());
		return data;

	}

	@Override
	public void onClick(View view, int pos) {
		// TODO Auto-generated method stub
		switch (pos - 1) {
		case 0:

			break;
		case 1:

			break;
		case 2:

			break;
		case 3:

			break;

		default:
			break;
		}

	}
}
