package com.bonc.mobile.hbmclient.view.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.KPIHomeActivity;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.data.ColumnChart;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter.ColumnDisplyInfo;
import com.bonc.mobile.hbmclient.data.KpiData;
import com.bonc.mobile.hbmclient.data.KpiInfo;
import com.bonc.mobile.hbmclient.data.MenuColumnInfo;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.HttpUtil;
import com.bonc.mobile.hbmclient.util.NumberUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.ACBarChartView;
import com.bonc.mobile.hbmclient.view.ACLineChartView;
import com.bonc.mobile.hbmclient.view.BarChartView;
import com.bonc.mobile.hbmclient.view.BarChartView.BarGraphData;
import com.bonc.mobile.hbmclient.view.KpiHasChartRightView;
import com.bonc.mobile.hbmclient.view.StickyHorizontalScrollView;

public class KpiHomeRightExpandableAdapter extends BaseExpandableListAdapter {
	private boolean isChartClick = false;

	public boolean isChartClick() {
		return isChartClick;
	}

	public void setChartClick(boolean isChartClick) {
		this.isChartClick = isChartClick;
	}

	private Activity activity;

	private Context context;
	private Handler handler = new Handler();

	private LayoutInflater mInflater;// View布局填充用
	private PopupWindow popWindpow;

	private Map<String, String> param;
	private double[] lastValue;
	private double[] currValue;
	private String[] xLables;
	// 数据

	private List<ColumnChart> barChartDatas;
	private TextView title_tv;
	private TextView chartView_tv;
	// 曲线图
	private LinearLayout graphView_ll;
	private List<Map<String, String>> listdata;
	// 列表
	private ListView listView;

	private String[] graph_list_titleName;

	private boolean hasGroup = true;
	private int groupLayout = R.layout.kpi_com_group_right;// 组布局

	private List<String> groupTag = new ArrayList<String>(); // 分组 title组标题.
	private List<String> groupTitle = new ArrayList<String>(); // 分组 key信息
	private Map<String, List<Double>> trendList; // 趋势图数据
	private Map<String, List<Map<String, String>>> subList;

	private ViewGroup mBarContainer;
	private BarChartView barChartView;

	// 数据查询参数
	private String optime;
	private String datatype;
	private String cols;
	private String areacode;
	private String datatable;

	private List<String[]> unit;
	private String[] colkey; // 要显示的列的key
	private String[] trendcol; // 趋势图点开以后要展示的列.
	/*
	 * if(allColumnList.size()>=0&&colInfoMap!=null){ MenuColumnInfo cinfo =
	 * colInfoMap
	 * .get(allColumnList.get(0).get("col").toLowerCase(Locale.CHINA));
	 * if(cinfo!=null){ if("-1".equals(cinfo.getColRule()) && kpiInfo!=null) {
	 * orgUnit=kpiInfo.getKpiUnit(); }else { orgUnit=cinfo.getColUnit(); }
	 * 
	 * }
	 * 
	 * 
	 * }
	 */
	KpiData kpiData;

	public KpiData getKpiData() {
		return kpiData;
	}

	public void setKpiData(KpiData kpiData) {
		this.kpiData = kpiData;
		if (kpiData == null) {

			hasCharts = false;

			hasGroup = false;
			colkey = new String[] {};
			groupTag = new ArrayList<String>(); // 分组 title组标题.
			groupTitle = new ArrayList<String>(); // 分组 key信息

			subList = new HashMap<String, List<Map<String, String>>>();

			mColumnDataInfo = new ArrayList<List<ColumnDisplyInfo[]>>();

		} else {
			getValueUse();
		}

	}

	private List<List<ColumnDisplyInfo[]>> mColumnDataInfo;

	private boolean hasCharts = true;// 判断是否显示趋势图
	private float width = 0;;
	private Map<String, KpiInfo> kpiInfoMap;

	public KpiHomeRightExpandableAdapter(Context context, KpiData kpiData,
			Map<String, String> param) {
		this.activity = (Activity) context;
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.kpiData = kpiData;
		width = (((Activity) context).getWindowManager().getDefaultDisplay()
				.getWidth() - context.getResources().getDimension(
				R.dimen.zhl_left_column_width));
		optime = param.get("optime");
		datatype = param.get("datatype");
		cols = param.get("cols");
		areacode = param.get("areacode");
		datatable = param.get("datatable");
		getValueUse();

	}

	public void getValueUse() {
		if (kpiData != null) {

			hasCharts = kpiData.isHasTrendChart();

			hasGroup = kpiData.isHasGroup();
			colkey = kpiData.getColkey();
			trendList = kpiData.getTrendList();
			if (!hasGroup) {
				groupLayout = R.layout.kpi_com_group_no;
			}

			List<Map<String, String>> groupList = kpiData.getGroupList();
			subList = kpiData.getSubList();
			mColumnDataInfo = new ArrayList<List<ColumnDisplyInfo[]>>();
			kpiInfoMap = kpiData.getKpiInfoMap();

			trendcol = new String[colkey.length];
			trendcol[0] = "op_time";
			for (int i = 1; i < colkey.length; i++) {
				trendcol[i] = colkey[i];
			}

			unit = new ArrayList<String[]>();
			for (int i = 0; i < groupList.size(); i++) {
				groupTag.add(groupList.get(i).get("groupTag"));
				groupTitle.add(groupList.get(i).get("title"));
				List<Map<String, String>> tempSub = subList
						.get(groupTag.get(i));
				int scount = colkey.length - 1;
				int childCount = tempSub.size();
				List<ColumnDisplyInfo[]> mColT = new ArrayList<ColumnDisplyInfo[]>();
				;
				String[] unitSub = new String[childCount];
				for (int j = 0; j < childCount; j++) {

					ColumnDisplyInfo[] mColTSub = new ColumnDisplyInfo[scount];

					for (int scountI = 0; scountI < scount; scountI++) {

						String sValue = tempSub.get(j).get(colkey[scountI + 1]);
						MenuColumnInfo mci = kpiData.getColInfoMap().get(
								colkey[scountI + 1]);

						String ss = subList.get(this.groupTag.get(i)).get(j)
								.get("kpi_code");
						KpiInfo ki = kpiInfoMap.get(ss);

						ColumnDisplyInfo cdi = ColumnDataFilter.getInstance()
								.filter(mci, sValue, ki);

						mColTSub[scountI] = cdi;
						if (scountI == 0) {

							if (mci != null) {
								if ("-1".equals(mci.getColRule()) && ki != null) {
									unitSub[j] = ki.getKpiUnit();
								} else {
									unitSub[j] = mci.getColUnit();
								}

							} else {
								unitSub[j] = "";
							}
						}
					}

					mColT.add(mColTSub);

				}
				unit.add(unitSub);
				mColumnDataInfo.add(mColT);
			}
		} else {
		}
	}

	@Override
	public int getGroupCount() {

		return groupTag.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return subList.get(groupTag.get(groupPosition)) == null ? 0 : subList
				.get(groupTag.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	public PopupWindow getPopupWindow() {
		return popWindpow;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(groupLayout, null);
			holder = new ViewHolder();
			if (hasGroup) {

				holder.index = (TextView) convertView
						.findViewById(R.id.group_text1);
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (hasGroup) {

			String title = kpiData.getGroupList().get(groupPosition)
					.get("title");
			if (title.length() > 6) {
				holder.index.setText(title.substring(6));

			}

		}
		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, final ViewGroup parent) {
		KpiHasChartRightView ret;
		if (convertView == null) {

			if (!hasCharts) {
				ret = new KpiHasChartRightView(context, mColumnDataInfo.get(
						groupPosition).get(childPosition), width, false);
			} else {
				String nowKey = this.groupTag.get(groupPosition)
						+ Constant.DEFAULT_CONJUNCTION
						+ subList.get(this.groupTag.get(groupPosition))
								.get(childPosition).get("kpi_code");
				ColumnDisplyInfo[] colTemp = mColumnDataInfo.get(groupPosition)
						.get(childPosition);
				List<Double> tlt = new ArrayList<Double>();
				if (trendList != null) {
					tlt = trendList.get(nowKey);
				}
				ret = new KpiHasChartRightView(context, colTemp, tlt, width);

			}
		} else {
			ret = (KpiHasChartRightView) convertView;
			if (!hasCharts) {
				ret.rechargeData(
						mColumnDataInfo.get(groupPosition).get(childPosition),
						null);
			} else {
				String nowKey = this.groupTag.get(groupPosition)
						+ Constant.DEFAULT_CONJUNCTION
						+ subList.get(this.groupTag.get(groupPosition))
								.get(childPosition).get("kpi_code");

				ret.rechargeData(
						mColumnDataInfo.get(groupPosition).get(childPosition),
						trendList.get(nowKey));
			}

		}

		if (childPosition % 2 == 0) {
			ret.setAllBackgroundByID(R.color.zeven_list_color);// list_left_item
		} else {
			ret.setAllBackgroundByID(R.color.zeven_list_color);
		}
		if (hasCharts) {

			ret.getLinearlayoutChart().setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 设置趋势图的点击响应
							if (isChartClick)// 如果有趋势图被点击
							{
								showIsChartClickToast();
							} else {
								isChartClick = true;

								// 统计用户使用习惯（指标）
								String kpiCode = subList
										.get(groupTag.get(groupPosition))
										.get(childPosition).get("kpi_code");

								popWindpow = new PopupWindow(context);

								Intent intent = new Intent(
										"chart.request_orientation");
								intent.putExtra("Orientation", true);
								activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
								DisplayMetrics dm = new DisplayMetrics();
								activity.getWindowManager().getDefaultDisplay()
										.getMetrics(dm);
								int screenWidth = dm.heightPixels;
								int screenHeight = dm.widthPixels;
								View view = mInflater.inflate(
										R.layout.pop_window_2, null);

								barChartView = (BarChartView) view
										.findViewById(R.id.pop_barChart);
								mBarContainer = (ViewGroup) view
										.findViewById(R.id.pop_bar_layout);
								popWindpow.setContentView(view);
								popWindpow.setBackgroundDrawable(WatermarkImage
										.getWatermarkLandDrawable());
								/*
								 * popWindpow .setBackgroundDrawable(context
								 * .getResources() .getDrawable(
								 * R.drawable.section_chart_middle));
								 */
								popWindpow.setWidth(screenHeight);
								popWindpow.setHeight(screenWidth);
								popWindpow.setFocusable(true);
								popWindpow.setOutsideTouchable(false);
								popWindpow.showAtLocation(mInflater.inflate(
										R.layout.month_dev_main, null),
										Gravity.NO_GRAVITY, 0, 0);
								isChartClick = false;
								popWindpow
										.setOnDismissListener(new OnDismissListener() {
											@Override
											public void onDismiss() {
												Intent intent = new Intent(
														"chart.request_orientation");
												intent.putExtra("Orientation",
														false);
												parent.getContext()
														.sendBroadcast(intent);
											}
										});
								title_tv = (TextView) view
										.findViewById(R.id.pop_title);
								String title = "";
								title_tv.setText(title);
								// 曲线图
								graphView_ll = (LinearLayout) view
										.findViewById(R.id.pop_graph);

								// 列表
								listView = (ListView) view
										.findViewById(R.id.pop_listview);
								graph_list_titleName = kpiData.getTitleName();
								String[] rightT = new String[graph_list_titleName.length];
								rightT[0] = "时间";
								for (int i = 1; i < rightT.length; i++) {
									rightT[i] = graph_list_titleName[i];
								}
								changeTitle(view, rightT);
								initData(groupPosition, childPosition);
							}

						}
					});
		}
		return ret;
	}

	// 变更标题栏内容
	private void changeTitle(View fatherV, String[] namesRight) {
		// TODO Auto-generated method stub

		LinearLayout layRightTitle = (LinearLayout) fatherV
				.findViewById(R.id.pop_list_title);
		layRightTitle.setBackgroundResource(R.drawable.glay_list_title);

		KpiHasChartRightView ret = new KpiHasChartRightView(context,
				R.layout.title_right_item, namesRight, 0,
				(((Activity) context).getWindowManager()).getDefaultDisplay()
						.getWidth(), true, NumberUtil.DpToPx(context, 110));

		layRightTitle.removeAllViews();
		layRightTitle.addView(ret);
		ret.setAllBackgroundByID(R.drawable.glay_list_title);
		LayoutParams lp = new LayoutParams(
				LayoutParams.WRAP_CONTENT, ret.getItemH());// (LayoutParams)
																		// ret.getLayoutParams();
		ret.setLayoutParams(lp);
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	class ViewHolder {
		TextView index;
		RelativeLayout rl_com_list_left;
	}

	public void initData(final int groupPosition, final int childPosition) {

		KPIHomeActivity.showMessageDailog();

		String groupTag = kpiData.getGroupList().get(groupPosition)
				.get("groupTag");
		final Map<String, String> kpiinfo = kpiData.getSubList().get(groupTag)
				.get(childPosition);

		new Thread() {
			public void run() {
				final boolean f = getData(kpiinfo.get("kpi_code"),
						kpiinfo.get("dim_key"));
				handler.post(new Runnable() {
					@Override
					public void run() {

						if (!f) {
							Toast.makeText(
									KpiHomeRightExpandableAdapter.this.activity,
									"没有数据!!", Toast.LENGTH_LONG).show();

							KPIHomeActivity.pDialog.dismiss();
							popWindpow.dismiss();
							return;
						}

						if (barChartDatas == null/* ||barChartDatas.size()==0 */) {
							Toast.makeText(
									KpiHomeRightExpandableAdapter.this.activity,
									"没有数据!!", Toast.LENGTH_LONG).show();
							KPIHomeActivity.pDialog.dismiss();
							popWindpow.dismiss();
							return;
						} else if (barChartDatas.size() == 0) {
							StickyHorizontalScrollView scv = (StickyHorizontalScrollView) popWindpow
									.getContentView().findViewById(
											R.id.hor_pop_barChart);

							mBarContainer.setVisibility(View.GONE);
							scv.refreshDrawableState();
						}

						if (barChartDatas != null && barChartDatas.size() != 0) {
							initBarGraph(kpiinfo.get("kpi_name"),
									unit.get(groupPosition)[childPosition]);
						}

						if (listdata != null && listdata.size() != 0) {
							initGraphView(kpiinfo.get("kpi_name"),
									unit.get(groupPosition)[childPosition]);

							String ss = kpiinfo.get("kpi_code");
							KpiInfo ki = kpiData.getKpiInfoMap().get(ss);
							// 设置Adapter
							PopWindowListAdapter listAdapter = new PopWindowListAdapter(
									(Activity) context, listdata, ki, trendcol,
									kpiData.getColInfoMap());
							listView.setAdapter(listAdapter);
						}
						KPIHomeActivity.pDialog.dismiss();
					}
				});
			}
		}.start();
	}

	/**
	 * 实时查询获取数据.
	 */
	public boolean getData(String kpicode, String dimkey) {
		if (param == null) {
			param = new HashMap<String, String>();
		}
		param.clear();
		param.put("endtime", optime);
		if (datatype.equals(Constant.DATA_TYPE_DAY)) {
			// param.put("starttime",DateUtil.dayBefore(optime, "yyyyMMdd",
			// 20));
			param.put("starttime", DateUtil.getFirstDay(
					DateUtil.getDate(optime, DateUtil.PATTERN_8),
					DateUtil.PATTERN_8));
		} else {
			// param.put("starttime",DateUtil.monthBefore(optime, "yyyyMM",
			// 12));
			param.put("starttime", DateUtil.getFirstMonth(
					DateUtil.getDate(optime, DateUtil.PATTERN_6),
					DateUtil.PATTERN_6));
		}
		param.put("cols", cols);
		param.put("optime", optime);
		param.put("areacode", areacode);
		param.put("dimkey", dimkey);
		param.put("datatable", datatable);
		param.put("datatype", datatype);
		param.put("kpicode", kpicode);
		param.put("areacol", colkey[1]); // 取第一列的数据.
		param.put("isexpand", "1");
		String resultString = HttpUtil.sendRequest(
				ActionConstant.KPI_AREA_PERIOD_DATA, param);

		if (resultString == null || "".equals(resultString)) {
			barChartDatas = null;
			return false;
		}

		try {
			JSONObject jo = new JSONObject(resultString);
			JSONArray bcArray = jo.optJSONArray("area_data");

			if (bcArray == null || bcArray.length() == 0) {
				barChartDatas = null;
				return false;
			}
			// 柱图数据.
			if (barChartDatas == null) {
				barChartDatas = new ArrayList<ColumnChart>(bcArray.length());
			} else {
				barChartDatas.clear();
			}

			int len = bcArray.length();
			for (int i = 0; i < len; i++) {

				if (areacode.equals(bcArray.optJSONObject(i).getString(
						"area_code"))) {
					continue;
				}

				ColumnChart cc = new ColumnChart();
				cc.setColumn_area_desc(bcArray.optJSONObject(i).getString(
						"area_name"));
				cc.setColumn_curmon_value(bcArray.optJSONObject(i).getString(
						colkey[1]));

				barChartDatas.add(cc);
			}
			// 本月值

			if (listdata == null) {
				listdata = new ArrayList<Map<String, String>>();
			} else {
				listdata.clear();
			}
			JSONArray currArray = jo.optJSONArray("base_data");
			if (currArray == null || currArray.length() == 0) {
				barChartDatas = null;
				return false;
			}
			len = currArray.length();
			currValue = new double[len];
			lastValue = new double[len];
			xLables = new String[len];
			for (int i = 0; i < len; i++) {
				Map<String, String> tt = new HashMap<String, String>();

				tt.put("op_time", currArray.optJSONObject(len - 1 - i)
						.optString("op_time"));
				for (int k = 0; k < trendcol.length; k++) {
					String value = currArray.optJSONObject(len - 1 - i)
							.optString(trendcol[k]);
					/*
					 * if(value!=null&&value.contains(".")&&value.length()>6) {
					 * value = value.substring(value.indexOf(".")); }
					 */
					tt.put(trendcol[k], value);
				}

				listdata.add(tt);
				try {
					currValue[i] = currArray.optJSONObject(i).getDouble(
							trendcol[1]);
				} catch (Exception e) {
					currValue[i] = 0.0;
					e.printStackTrace();
				}

				try {

					JSONObject preDateData = currArray.optJSONObject(i)
							.optJSONObject("pre_date_data");

					if (preDateData != null) {
						lastValue[i] = preDateData.optDouble(trendcol[1]);
					}

				} catch (Exception e) {
					lastValue[i] = 0.0;
					e.printStackTrace();
				}

				String monId = currArray.optJSONObject(i).getString("op_time");
				xLables[i] = monId.length() > 6 ? monId.substring(6, 8) : monId;
			}
		} catch (JSONException e) {
			barChartDatas = null;
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// 初始化柱形图
	public void initBarGraph(String linetitle, String unit) {
		initBarGraphData(barChartView, linetitle, unit);
	}

	public void initBarGraphData(BarChartView chartView, String linetitle,
			String unit) {
		BarGraphData[] datas = new BarGraphData[barChartDatas.size()];
		for (int i = 0, n = datas.length; i < n; i++) {
			double valueY = 0.0;
			try {
				valueY = NumberUtil.changeToDouble(barChartDatas.get(i)
						.getColumn_curmon_value());
			} catch (Exception e) {
				e.printStackTrace();
			}
			datas[i] = new BarGraphData(i * 500, valueY, barChartDatas.get(i)
					.getColumn_area_desc());
		}

		ACBarChartView acv = new ACBarChartView(activity, datas, unit);
		acv.setLineTitle(linetitle);
		// acv.setmUnit(unit);
		mBarContainer.addView(acv);
	}

	// 曲线图初始化
	public void initGraphView(String linetitle, String unit) {
		graphView_ll.removeAllViews();
		if (listdata == null || listdata.size() == 0) {
			return;
		}

		ACLineChartView acharview = new ACLineChartView(activity, xLables,
				currValue, lastValue, unit);
		graphView_ll.addView(acharview);
		acharview.setLineTitle(linetitle);
		acharview.getLayoutParams().height = LayoutParams.MATCH_PARENT;
		acharview.getLayoutParams().width = LayoutParams.MATCH_PARENT;
		if (datatype.equals(Constant.DATA_TYPE_MONTH)) {
			acharview.setLine1Name("本年");
			acharview.setLine2Name("上年");
		} else {
			acharview.setLine1Name("本月");
			acharview.setLine2Name("上月");
		}
	}

	public void showIsChartClickToast() {
		(Toast.makeText(context, "正在响应您上一点击，请稍候", Toast.LENGTH_LONG)).show();
	}

}
