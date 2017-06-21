/**
 * TODO
 */
package com.bonc.mobile.hbmclient.terminal.component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.bonc.mobile.hbmclient.util.NumberUtil;

/**
 * @author liweigao
 * 
 */
public class ModuleBasicViewRingChartViewBranch extends ViewBranch {
	private String title;
	int[] colorLib;
	String[] weidus;
	private LinearLayout ll;

	CategorySeries dataset;

	DefaultRenderer renderer;
	View chart;

	// config
	private BusinessDao businessdao = new BusinessDao();
	private Map<String, String> userinfo = businessdao.getUserInfo(); // 区域用户信息

	/**
	 * @param c
	 * @param tae
	 */
	public ModuleBasicViewRingChartViewBranch(Context c,
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
				case PSS_DAY_ACTIVITY:
					title_big = "三大品牌占比分析";
					menuCode = TerminalConfiguration.KEY_MENU_CODE_PSS_DAY;
					dimkey = businessdao.getMenuComplexDimKey(businessdao
							.getMenuInfo(menuCode));
					param.put(TerminalConfiguration.KEY_KPI_CODES,
							TerminalConfiguration.KPI_PSS_DAY_RINGCHART);
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
							TerminalConfiguration.TITLE_COLUMN_PSS_DAY_RINGCHART);
					intent.putExtra(
							TerminalConfiguration.KEY_RESPONSE_KEY,
							TerminalConfiguration.RESPONSE_PSS_DAY_RINGCHART_KEY);
					intent.putExtra(
							TerminalConfiguration.KEY_COLUNM_KPI_CODE,
							TerminalConfiguration.COLUMN_KPI_CODE_PSS_DAY_RINGCHART);
					break;
				case PSS_MONTH_ACTIVITY:
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

	@Override
	public void dispatchView() {
		// TODO Auto-generated method stub
		this.ll = (LinearLayout) this.mView
				.findViewById(R.id.id_ringChart_container);
		setDefaultRender();
	}

	private void setDefaultRender() {
		// TODO Auto-generated method stub
		renderer = new DefaultRenderer();
		int labelTextSize = (int) context.getResources().getDimension(
				R.dimen.heyue_text_size);
		renderer.setLabelsTextSize(labelTextSize);
		renderer.setLegendTextSize(labelTextSize);
		renderer.setMargins(new int[] { 3, 3, 3, 3 });

		renderer.setPanEnabled(false);// 禁止拖动图
		renderer.setInScroll(true);// 如果在scrollView中一定要设置，否则会出错
		renderer.setZoomEnabled(false);
		renderer.setIsPeculiarLegendTextColor(true);
		// 消除锯齿
		renderer.setAntialiasing(true);
		renderer.setLabelsColor(Color.BLACK);
		renderer.setDisplayValues(true);
		renderer.setShowLegend(false);
		renderer.setScale(0.7f);
		renderer.setShowLableBack(true);

		renderer.setFitLegend(false);
		renderer.setStartAngle(315);

	}

	@Override
	public void setData(JSONObject data) {
		super.setData(data);
		this.kpi_statistics = "4570|4580|4590";
		this.title = "三大品牌销量占比分析";
		weidus = new String[] { "动感地带", "全球通", "神州行" };
		dataset = new CategorySeries(title);

		colorLib = new int[] { 0xffbc1414, 0xffEFC800, 0xff3E7006,
				Color.MAGENTA, Color.RED, Color.YELLOW, Color.CYAN };

		String dateString = mTerminalActivityEnum.getOPtime();

		String sqlString = "Select '全球通' as pp,round(CURDAY_VALUE_DR*100,2) as zb From TERMINAL_DAILY_JXC where kpi_code ='4570' and op_time=?"
				+ " union "
				+ " Select '动感地带' as pp,round(CURDAY_VALUE_DR*100,2) as zb From TERMINAL_DAILY_JXC where kpi_code ='4580' and op_time=?"
				+ " union "
				+ " Select '神州行' as pp,round(CURDAY_VALUE_DR*100,2) as zb From TERMINAL_DAILY_JXC where kpi_code ='4590' and op_time=?";

		List<Map<String, String>> resultList = new SQLHelper().queryForList(
				sqlString, new String[] { dateString, dateString, dateString });
		resultList = getTermData("data9");

		if (resultList == null || resultList.size() == 0) {
			dataset.clear();
			renderer.removeAllRenderers();
			return;
		}

		dataset.clear();
		renderer.removeAllRenderers();
		for (int i = 0; i < resultList.size(); i++) {
			dataset.add(resultList.get(i).get("pp"),
					NumberUtil.changeToDouble(resultList.get(i).get("zb")));
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(colorLib[i % colorLib.length]);
			renderer.addSeriesRenderer(r);
		}
	}

	@Override
	public void updateView() {
		if (null != dataset && null != renderer && dataset.getItemCount() == 3
				&& renderer.getSeriesRendererCount() == 3) {
			this.ll.removeAllViews();
			chart = ChartFactory.getDoughnutChartView(this.context, dataset,
					renderer);

			this.ll.addView(chart);
		} else {
			this.ll.removeAllViews();
		}
	}

}
