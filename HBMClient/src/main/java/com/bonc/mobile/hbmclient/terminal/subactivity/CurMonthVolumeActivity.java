package com.bonc.mobile.hbmclient.terminal.subactivity;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;
import com.bonc.mobile.hbmclient.terminal.view.CurMonthVolumeView;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.NumberUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.StickyHorizontalScrollView;

/** 图表类的Activity版本，需要传入intent的extra:String title 及CombinedXYChart chart */
public class CurMonthVolumeActivity extends BaseTerminalActivity {
	protected CurMonthVolumeView mChatView;
	protected StickyHorizontalScrollView mRootView;
	protected ViewGroup mChartFrameLayout;
	private int mCurrentThird;
	public static final int BARW = 40;
	private final String KEY_CURRENT = "key_current";
	private final String KEY_LAST = "key_last";
	private String[] xLabel;
	private String unit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_NoTitleBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.terminal_combie_chart);
		FrameLayout fl = (FrameLayout) findViewById(R.id.combie_ac_root);
		fl.setBackgroundDrawable(WatermarkImage.getWatermarkLandDrawable());

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 自动横屏
		mRootView = (StickyHorizontalScrollView) findViewById(R.id.combie_ac_root_layout);
		mChartFrameLayout = (ViewGroup) findViewById(R.id.combie_ac_layout);

		TextView tv_chartUnit = (TextView) findViewById(R.id.id_chart_unit);
		this.unit = this.intent.getExtras().getString(
				TerminalConfiguration.KPI_UNIT_CHART);
		tv_chartUnit.setText("(" + unit + ")");
		mChatView = new CurMonthVolumeView(this, new int[] { 3, 6 },
				mTerminalActivityEnum.getDateRange()) {

			@Override
			protected XYMultipleSeriesDataset onDeployData(
					XYMultipleSeriesDataset aDataset) {
				XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
				CategorySeries bar_series = new CategorySeries("");
				XYSeries line_series = new XYSeries("", 1);
				XYSeries bar_series_xy;
				Map<String, double[]> resultMap = getChartData((Map<String, String>) intent
						.getExtras().getSerializable(
								TerminalConfiguration.KEY_MAP_CHART));
				double[] test_data1 = new double[xLabel.length];
				double[] test_data2 = new double[xLabel.length];
				if (resultMap != null) {
					if (resultMap.get(KEY_CURRENT) != null) {
						test_data1 = (double[]) resultMap.get(KEY_CURRENT);
					}

					if (resultMap.get(KEY_LAST) != null) {
						test_data2 = (double[]) resultMap.get(KEY_LAST);
					}

				}

				if (test_data1 == null || test_data2 == null) {
					return null;
				}
				DecimalFormat df = new DecimalFormat("#.###");
				for (double data : test_data1) {
					bar_series.add(Double.parseDouble(df.format(data)));
				}
				setLongClickable(true);
				bar_series_xy = bar_series.toXYSeries();
				for (int i = 0; i < test_data2.length; i++) {
					line_series.add(i + 1, test_data2[i]);
				}
				dataset.addSeries(bar_series_xy);
				dataset.addSeries(line_series);
				this.setType(2);// 点击不响应
				return dataset;
			}
		};
		mChatView.setXLabel(xLabel);
		TextView titleView = (TextView) findViewById(R.id.cur_title_tv);
		titleView.setText(intent.getExtras().getString(
				TerminalConfiguration.TITLE_BIG_CHART));

		mChatView.setLongClickable(false);
		DateRangeEnum mType = mTerminalActivityEnum.getDateRange();
		String mDateString = mTerminalActivityEnum.getOPtime();
		mChatView.setCycleType(mType);
		mChartFrameLayout.addView(mChatView, 1);
		if (mDateString != null)
			mChatView.calculatecComplementary(mDateString);
	}

	public int getCurrentThird() {
		return mCurrentThird;
	}

	public void setCurrentThird(int mCurrentThird) {
		this.mCurrentThird = mCurrentThird;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	private Map<String, double[]> getChartData(Map<String, String> map) {
		String last_first = null;
		String last_last = null;
		String current_first = null;
		String current_last = null;

		int timeCount = 0;

		switch (mTerminalActivityEnum.getDateRange()) {
		case DAY:
			String[] dateNeed = DateUtil.getCurrentLastDay(
					mTerminalActivityEnum.getOPtime(),
					DateRangeEnum.DAY.getDateServerPattern());

			current_first = dateNeed[0];
			current_last = dateNeed[1];
			last_first = dateNeed[2];
			last_last = dateNeed[3];
			timeCount = 30;
			break;

		case MONTH:
			Date date = mTerminalActivityEnum.getCalendar().getTime();
			current_first = DateUtil.getFirstMonth(date, mTerminalActivityEnum
					.getDateRange().getDateServerPattern());
			current_last = DateUtil.formatter(date, mTerminalActivityEnum
					.getDateRange().getDateServerPattern());
			last_first = DateUtil
					.getFirstMonthLastyear(date, mTerminalActivityEnum
							.getDateRange().getDateServerPattern());
			last_last = DateUtil.getMonthLastyear(date, mTerminalActivityEnum
					.getDateRange().getDateServerPattern());
			timeCount = DateUtil
					.MonthBetween(current_first, current_last,
							mTerminalActivityEnum.getDateRange()
									.getDateServerPattern()) + 1;
			if (timeCount < 12) {
				timeCount = 12;
			}
			break;
		}

		String preSql = "Select "
				+ map.get(TerminalConfiguration.KEY_COLUMN_NAME)
				+ " as value,op_time as op_time from "
				+ map.get(TerminalConfiguration.KEY_DATATABLE)
				+ " WHERE KPI_CODE=? "
				+ "and OP_TIME between ? and ?  order by  OP_TIME asc";
		// 当值
		// List<Map<String, String>> resultList_current = new SQLHelper()
		// .queryForList(
		// preSql,
		// new String[] {
		// map.get(TerminalConfiguration.KEY_KPI_CODE),
		// current_first, current_last });
		List<Map<String, String>> resultList_current = (List<Map<String, String>>) intent
				.getSerializableExtra("chart_data1");
		// 上值
		// List<Map<String, String>> resultList_last = new SQLHelper()
		// .queryForList(
		// preSql,
		// new String[] {
		// map.get(TerminalConfiguration.KEY_KPI_CODE),
		// last_first, last_last });
		List<Map<String, String>> resultList_last = (List<Map<String, String>>) intent
				.getSerializableExtra("chart_data2");

		double[] num_current = new double[timeCount]; // 当值
		double[] num_last = new double[timeCount]; // 上值

		String temptime = current_first;
		String lasttemptime = last_first;
		int k = 0;

		switch (mTerminalActivityEnum.getDateRange()) {
		case DAY:
			xLabel = DateUtil.getDayInNum(temptime,
					DateRangeEnum.DAY.getDateServerPattern(), timeCount);
			break;
		case MONTH:
			xLabel = new String[timeCount];
			for (int i = 0; i < timeCount; i++) {
				xLabel[i] = String.valueOf(i + 1);
			}
			break;
		}

		if (resultList_current != null && resultList_current.size() > 0) {
			int len = resultList_current.size();
			for (int i = 0; i < len;) {
				String op_time = resultList_current.get(i).get("op_time");
				if (temptime.equals(op_time)) {
					num_current[k] = NumberUtil
							.changeToDouble(resultList_current.get(i).get(
									"value"));
					if ("%".equals(unit)) {
						num_current[k] *= 100;
					}
					i++;
				} else {
					num_current[k] = 0.0;
				}

				if (++k >= timeCount) {
					break;
				}
				switch (mTerminalActivityEnum.getDateRange()) {
				case DAY:
					temptime = DateUtil.getDateSpecified(temptime,
							DateRangeEnum.DAY.getDateServerPattern(), 1);
					break;
				case MONTH:
					temptime = DateUtil.monthBefore(temptime,
							DateRangeEnum.MONTH.getDateServerPattern(), -1);
					break;
				}
			}

			// k值初始化
			k = 0;
			if (resultList_last != null && resultList_last.size() > 0) {
				int len2 = resultList_last.size();
				for (int i = 0; i < len && i < len2;) {
					String last_op_time = resultList_last.get(i).get("op_time");
					if (lasttemptime.equals(last_op_time)) {
						num_last[k] = NumberUtil.changeToDouble(resultList_last
								.get(i).get("value"));
						if ("%".equals(unit)) {
							num_last[k] *= 100;
						}
						i++;
					} else {
						num_last[k] = 0.0;
					}
					if (++k >= timeCount) {
						break;
					}
					switch (mTerminalActivityEnum.getDateRange()) {
					case DAY:
						lasttemptime = DateUtil.getDateSpecified(lasttemptime,
								DateRangeEnum.DAY.getDateServerPattern(), 1);
						break;
					case MONTH:
						lasttemptime = DateUtil.monthBefore(lasttemptime,
								DateRangeEnum.MONTH.getDateServerPattern(), -1);
						break;
					}
				}

			}

			Map<String, double[]> result = new HashMap<String, double[]>();
			result.put(KEY_CURRENT, num_current);
			result.put(KEY_LAST, num_last);
			return result;
		} else {
			return null;
		}
	}
}
