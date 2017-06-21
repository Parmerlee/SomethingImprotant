package com.bonc.mobile.hbmclient.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.data.ComplexDimInfo;
import com.bonc.mobile.hbmclient.data.KpiData;
import com.bonc.mobile.hbmclient.data.KpiInfo;
import com.bonc.mobile.hbmclient.data.MenuColumnInfo;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.net.ConnectManager;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.HttpUtil;
import com.bonc.mobile.hbmclient.util.LogUtil;
import com.bonc.mobile.hbmclient.util.StringUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.KpiHasChartRightView;
import com.bonc.mobile.hbmclient.view.ListViewSetting;
import com.bonc.mobile.hbmclient.view.MyDatePickerDialog;
import com.bonc.mobile.hbmclient.view.adapter.KpiHomeLeftExpanableAdapter;
import com.bonc.mobile.hbmclient.view.adapter.KpiHomeRightExpandableAdapter;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;

/**
 * 指标首页.
 * 
 * @author tengshibo
 * 
 */
public class KPIHomeActivity extends SlideHolderActivity {
	private class OrientationReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			boolean orientation = intent.getBooleanExtra("Orientation", false);
			if (orientation) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			} else {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
						| ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
			}
		}
	}

	// 变量.
	private OrientationReceiver mReveiver = new OrientationReceiver();
	private Handler myhHandler = new Handler();

	private TextView monDevAreaNew, monDevDateNew, monDevBrandNew;
	public static ExpandableListView monDevLeftListView;// 左侧列表
	public static ExpandableListView monDevRightListView;// 右侧列表
	private ListViewSetting listViewSetting;

	private KpiHomeLeftExpanableAdapter leftAdapter;
	private KpiHomeRightExpandableAdapter rightAdapter;
	// 表头Id
	private TextView mon_dev_logo;// 标题logo
	// 传送的数据
	String logo_name;
	String menuCode;
	// 参数

	private int screenWidth;
	// 控件.
	private Calendar calendar;
	public static ProgressDialog pDialog;
	private DatePickerDialog mDateDia; // 日期选择框.
	private AlertDialog alertDialog;

	private BusinessDao dao = new BusinessDao();
	private ComplexDimInfo complexDimInfo; // 复合维度信息
	private List<Map<String, String>> menuDimList; // 菜单维度信息
	private Map<String, String> menuInfo; // 菜单信息.
	private Map<String, String> userInfo; // 用户信息.
	private Map<String, String> menuAddInfo; // 菜单附加信息.
	private List<Map<String, String>> areaInfoList; // 地区信息.
	private List<Map<String, String>> menuThirdDimValueList; // 第三维度维值信息.
	List<Map<String, String>> allColumnList;// 菜单所有的列
	private Map<String, MenuColumnInfo> menuColInfoMap; // 存放所有指标所有列的信息.
	private Map<String, KpiInfo> kpiInfoMap; // 指标信息.
	private String[] colkeyList; // 菜单要展示的列
	private String[] titleList; // 菜单要展示的列中文名称.
	private Map<String, String> thirdDimToComplexDim; // 菜单下具有第三维度选择条件时,
														// 将该维度和复合后的复合dim做一个对应.
														// 与menuThirdDimValueList
														// 相对应.
	private Map<String, String> thirdDimInfo; // 第三维度信息.
	Map<String, String> param; // 实时请求参数.
	private String dataType; // 数据类型.
	private String optime; // 数据日期.
	private KpiData kpiData; // 数据.

	private String areaId;// 用户选择的地区ID
	private int areaLevel; // 地区级别
	private String thirdDimValueId; // 用户选择的第三维度的值.

	private DateRangeEnum dre;
	private final String KEY_BACK_DATE = "currtime";

	public void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_NoTitleBar);
		super.onCreate(savedInstanceState);
		this.setMainContent(R.layout.kpi_month_dev_main);
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.parent);
		rl.setBackgroundDrawable(WatermarkImage.getWatermarkDrawable());

		screenWidth = getWindowManager().getDefaultDisplay().getWidth();

		pDialog = new ProgressDialog(this);

		Intent intent = getIntent();
		menuCode = intent.getStringExtra(MenuEntryAdapter.KEY_MENU_CODE);
		if (menuCode == null || "".equals(menuCode)) {
			LogUtil.error(this.getClass().toString(), "指标首页获取菜单编码为空. 退出指标首页!");
			finish();
			return;
		}
		logo_name = dao.getMenuName(menuCode);
		buildSideMenu(menuCode);
		// 查询菜单信息.
		menuInfo = dao.getMenuInfo(menuCode);
		dataType = menuInfo.get("dataType");
		if (DateRangeEnum.DAY.getDateFlag().equals(dataType)) {
			this.dre = DateRangeEnum.DAY;
		} else {
			this.dre = DateRangeEnum.MONTH;
		}
		// 查询菜单维度信息
		menuDimList = dao.getMenuDim(menuCode);
		menuAddInfo = dao.getMenuAddInfo(menuCode);
		// 查询用户信息
		userInfo = dao.getUserInfo();

		if (userInfo == null) {
			LogUtil.error(this.getClass().toString(), "查询用户信息失败. 返回");
			finish();
			return;
		}

		Map<String, String> areaBaseInfo = dao.getAreaBaseInfo(userInfo
				.get("areaId"));

		int level = 4;
		try {
			level = Integer.parseInt(menuAddInfo.get("showAreaLevel"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		int userAreaLevel = Integer.parseInt(areaBaseInfo.get("areaLevel"));

		if (level > userAreaLevel) {
			areaInfoList = dao.getAreaInfo(userInfo.get("areaId"));
		} else {
			areaInfoList = new ArrayList<Map<String, String>>();
			areaInfoList.add(areaBaseInfo);
		}

		try {
			areaLevel = Integer.parseInt(areaInfoList.get(0).get("areaLevel"));
		} catch (Exception e) {
			e.printStackTrace();
			areaLevel = -1;
		}

		if (areaInfoList == null || areaInfoList.size() == 0) {
			LogUtil.error(this.getClass().toString(), "查询用户地市信息失败 . 返回");
			finish();
			return;
		}

		// 获取复合维度信息
		complexDimInfo = dao.getMenuComplexDimKey(menuInfo);

		if ("error".equals(complexDimInfo.getIsMenuCofingRight())) {
			LogUtil.error(this.getClass().toString(), "指标首页查询维度复合信息 返回出错.");
			finish();
			return;
		}

		allColumnList = dao.getMenuAllColumnList(menuCode);

		if (allColumnList == null || allColumnList.size() == 0) {
			LogUtil.error(this.getClass().toString(), "查询菜单要展示列为空. 数据初始化失败.");
		}

		int len = allColumnList.size();
		colkeyList = new String[len + 1];
		titleList = new String[len + 1];
		// 左侧的key和名称.
		colkeyList[0] = "kpi_name";
		titleList[0] = "指标名称";
		if (menuColInfoMap == null) {
			menuColInfoMap = new HashMap<String, MenuColumnInfo>();
		}
		for (int i = 0; i < len; i++) {
			String col = allColumnList.get(i).get("col")
					.toLowerCase(Locale.CHINA);
			colkeyList[i + 1] = col;
			titleList[i + 1] = allColumnList.get(i).get("colname")
					.toLowerCase(Locale.CHINA);

			MenuColumnInfo colInfo = new MenuColumnInfo();
			colInfo.setColKey(col);
			colInfo.setColName(allColumnList.get(i).get("colname")
					.toLowerCase(Locale.CHINA));
			colInfo.setColRule(allColumnList.get(i).get("rule")
					.toLowerCase(Locale.CHINA));
			colInfo.setColUnit(allColumnList.get(i).get("unit")
					.toLowerCase(Locale.CHINA));
			colInfo.setMenuCode(allColumnList.get(i).get("menucode")
					.toLowerCase(Locale.CHINA));
			menuColInfoMap.put(col, colInfo);

		}

		kpiInfoMap = new HashMap<String, KpiInfo>();
		List<Map<String, String>> kpiInfoList = dao.getMenuAllKpiList(menuCode);
		kpiInfoMap = new HashMap<String, KpiInfo>();
		int kilen = kpiInfoList.size();
		for (int i = 0; i < kilen; i++) {
			KpiInfo kInfo = new KpiInfo();
			kInfo.setKpiCode(kpiInfoList.get(i).get("kpi_code"));
			kInfo.setMenuCode(kpiInfoList.get(i).get("menu_code"));
			kInfo.setKpiName(kpiInfoList.get(i).get("kpiName"));
			kInfo.setKpiUnit(kpiInfoList.get(i).get("kpiUnit"));
			kInfo.setKpiRule(kpiInfoList.get(i).get("rule"));
			kInfo.setKpiDefine(kpiInfoList.get(i).get("kpi_define"));
			kpiInfoMap.put(kpiInfoList.get(i).get("kpi_code"), kInfo);

		}

		initWidget();
		refreshDataFromRemote();
	}

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter("chart.request_orientation");
		registerReceiver(mReveiver, filter);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		unregisterReceiver(mReveiver);
		super.onPause();
	}

	PopupWindow pp;
	int popShowCount = 0;// 记录当前打开的pop数

	public void initWidget() {
		mon_dev_logo = (TextView) findViewById(R.id.logo_word_mon_dev);
		mon_dev_logo.setText(logo_name);

		// 设置日期时间
		monDevDateNew = (TextView) findViewById(R.id.date_select_new);
		monDevDateNew.setOnClickListener(this);

		// 判断菜单是否具有地市维度.
		if (menuDimList != null && menuDimList.size() > 0) {
			String dimOrder = menuDimList.get(0).get("dimOrder");
			if ("2".equals(dimOrder)) { // 说明菜单下的指标具有地区维度.
				LinearLayout selectllLayout = (LinearLayout) findViewById(R.id.ll_area_select_new);
				selectllLayout.setVisibility(View.VISIBLE);
				monDevAreaNew = (TextView) findViewById(R.id.area_select_new);
				monDevAreaNew.setOnClickListener(this);
				monDevAreaNew.setText(areaInfoList.get(0).get("areaName"));
				areaId = areaInfoList.get(0).get("areaCode");
			} else {
				LinearLayout selectllLayout = (LinearLayout) findViewById(R.id.ll_area_select_new);
				selectllLayout.setVisibility(View.GONE);
			}

		} else // 如果菜单没有第二和第三维度 则隐藏掉第二和第三维度的view。
		{
			LinearLayout selectllLayout = (LinearLayout) findViewById(R.id.ll_area_select_new);
			selectllLayout.setVisibility(View.GONE);
			LinearLayout brandlLayout = (LinearLayout) findViewById(R.id.ll_brand_select_new);
			brandlLayout.setVisibility(View.GONE);
		}

		// 第三维度. 如果没有按第三维度展开 则检查菜单是否配置了第三维度。
		// 若配置了第三维度 则应该显示第三维度.

		monDevBrandNew = (TextView) findViewById(R.id.brand_select_new);
		monDevBrandNew.setOnClickListener(this);
		if (!complexDimInfo.isDimExpand()) {
			if (menuDimList != null && menuDimList.size() > 0) {
				String dimOrder = menuDimList.get(0).get("dimOrder");
				if (!"2".equals(dimOrder)) {
					thirdDimInfo = menuDimList.get(0);
				} else if (menuDimList.size() >= 2) {
					thirdDimInfo = menuDimList.get(1);
				}

				if (thirdDimInfo != null) {
					menuThirdDimValueList = dao.getDimValue(thirdDimInfo
							.get("dimCode"));
					LinearLayout brandlLayout = (LinearLayout) findViewById(R.id.ll_brand_select_new);
					brandlLayout.setVisibility(View.VISIBLE);
					monDevBrandNew.setText(menuThirdDimValueList.get(0).get(
							"dimvaluename"));
					thirdDimValueId = menuThirdDimValueList.get(0).get(
							"dimvalueid");
					// 如果存在第三维度的情况下 我们需要将第三维度 与其复合维度的关系映射出来 。 方便查询时 使用.
					thirdDimToComplexDim = dao.getSpecialDimComplexKey(
							menuCode, thirdDimInfo.get("dimCode"));

				} else {
					LinearLayout brandlLayout = (LinearLayout) findViewById(R.id.ll_brand_select_new);
					brandlLayout.setVisibility(View.GONE);
				}
			} else {
				LinearLayout brandlLayout = (LinearLayout) findViewById(R.id.ll_brand_select_new);
				brandlLayout.setVisibility(View.GONE);
			}
		} else {
			LinearLayout brandlLayout = (LinearLayout) findViewById(R.id.ll_brand_select_new);
			brandlLayout.setVisibility(View.GONE);
		}

		monDevLeftListView = (ExpandableListView) findViewById(R.id.list_view1);
		monDevRightListView = (ExpandableListView) findViewById(R.id.list_view2);
		View v = getLayoutInflater().inflate(R.layout.list_foot_view, null);
		monDevLeftListView.addFooterView(v, null, false);
		monDevRightListView.addFooterView(v, null, false);

		listViewSetting = new ListViewSetting();
		// monDevLeftListView.setLongClickable(true);
		monDevLeftListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> adView,
							View view, int arg2, long arg3) {
						if (rightAdapter.isChartClick()) {
							rightAdapter.showIsChartClickToast();
							return false;
						}
						int groupPos = (Integer) view
								.getTag(R.id.rl_com_list_left); // 参数值是在setTag时使用的对应资源id号
						int childPos = (Integer) view
								.getTag(R.id.zhibiao_left_1);
						if (childPos == -1) {// 长按的是父项
						} else {
							String groupTag = (String) kpiData.getGroupList()
									.get(groupPos).get("groupTag");
							String kpiCode = ((kpiData.getSubList()
									.get(groupTag)).get(childPos))
									.get("kpi_code");
							String leftValue = kpiInfoMap.get(kpiCode)
									.getKpiDefine();
							showPopWinDow(leftValue, view);
						}
						return true;
					}
				});
		monDevLeftListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				if (rightAdapter.isChartClick()) {
					rightAdapter.showIsChartClickToast();
					return false;
				}

				if (areaLevel == -1) {
					return false;
				}
				int level = 4;
				try {
					level = Integer.parseInt(menuAddInfo.get("showAreaLevel"));
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (areaLevel < level && listViewSetting.leftTouchFlag == 1) {

					Intent intent = new Intent(KPIHomeActivity.this,
							KPIAreaActivity.class);

					String groupTag = kpiData.getGroupList().get(groupPosition)
							.get("groupTag");

					Map<String, String> kdata = kpiData.getSubList()
							.get(groupTag).get(childPosition);

					intent.putExtra("areaId", areaId);
					intent.putExtra("areaName", monDevAreaNew.getText());
					intent.putExtra("opTime", optime);
					intent.putExtra("dataType", dataType);
					intent.putExtra("kpiCode", kdata.get("kpi_code"));
					intent.putExtra("kpiName", kdata.get("kpi_name"));
					intent.putExtra("menuCode", menuCode);
					intent.putExtra("kpiInfo", (Serializable) kpiInfoMap
							.get(kdata.get("kpi_code")));
					intent.putExtra("allColumnList",
							(Serializable) allColumnList);
					intent.putExtra("colInfoMap", (Serializable) menuColInfoMap);
					if (complexDimInfo.isDimExpand()) { // 如果事按第三维度展开. 则
						intent.putExtra("complexKey", kdata.get("dim_key"));
					} else if (menuThirdDimValueList == null
							|| menuThirdDimValueList.size() == 0) { // 不存在第三维度
						intent.putExtra("complexKey",
								complexDimInfo.getComplexDimKeyString());
					} else {
						// 攒在第三维度
						intent.putExtra("thirdDimValueId", thirdDimValueId);
						intent.putExtra("thirdDimeName",
								thirdDimInfo.get("dimName"));
						intent.putExtra("thirdDimValueName",
								monDevBrandNew.getText());
						intent.putExtra("menuThirdDimValueList",
								(Serializable) menuThirdDimValueList);
						intent.putExtra("thirdDimToComplexDim",
								(Serializable) thirdDimToComplexDim);
					}
					closePopWindow();
					startActivity(intent);
				}

				return false;
			}
		});

		monDevRightListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				if (rightAdapter.isChartClick()) {
					rightAdapter.showIsChartClickToast();
					return false;
				}
				if (kpiData == null)
					return false;
				String groupTag = kpiData.getGroupList().get(groupPosition)
						.get("groupTag");

				Map<String, String> kdata = kpiData.getSubList().get(groupTag)
						.get(childPosition);

				if (listViewSetting.leftTouchFlag == 0) {
					Intent intent = new Intent(KPIHomeActivity.this,
							KPIPeriodActivity.class);
					intent.putExtra("areaId", areaId);
					intent.putExtra("areaName", monDevAreaNew.getText());
					intent.putExtra("opTime", optime);
					intent.putExtra("dataType", dataType);
					intent.putExtra("kpiCode", kdata.get("kpi_code"));
					intent.putExtra("kpiName", kdata.get("kpi_name"));
					intent.putExtra("menuCode", menuCode);
					intent.putExtra("kpiInfo", (Serializable) kpiInfoMap
							.get(kdata.get("kpi_code")));
					intent.putExtra("allColumnList",
							(Serializable) allColumnList);
					intent.putExtra("colInfoMap", (Serializable) menuColInfoMap);
					if (thirdDimValueId == null) { // 如果事按第三维度展开. 则
						intent.putExtra("complexKey", kdata.get("dim_key"));
					} else {
						// 攒在第三维度
						intent.putExtra("complexKey",
								thirdDimToComplexDim.get(thirdDimValueId));
						intent.putExtra("thirdDimValueName",
								monDevBrandNew.getText());
					}

					closePopWindow();
					startActivity(intent);
				}
				return false;
			}
		});
		Button navigator = (Button) this.findViewById(R.id.id_navigator);
		navigator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				slideHolder.toggle();
			}
		});

	}

	/**
	 * 修改title.
	 * 
	 * @param titleNames
	 */
	private void changeTitle(String[] titleNames) {
		if (titleNames != null && !titleNames.equals("")) {
			TextView tvl = (TextView) findViewById(R.id.main_left_title);
			tvl.setText(titleNames[0]);
			LinearLayout tl = (LinearLayout) findViewById(R.id.title_lineralayout);
			tl.setBackgroundResource(R.drawable.glay_list_title);

			LinearLayout layRightTitle = (LinearLayout) findViewById(R.id.lineralayout_right_title);

			KpiHasChartRightView ret;
			if (kpiData.isHasTrendChart()) {
				String str[] = new String[titleNames.length];
				str[0] = kpiData.getTrendChartTitle();
				for (int i = 1; i < str.length; i++) {
					str[i] = titleNames[i];
				}

				ret = new KpiHasChartRightView(KPIHomeActivity.this,
						R.layout.title_right_item, str, 0, screenWidth
								- this.getResources().getDimension(
										R.dimen.zhl_left_column_width));
			} else {
				String str[] = new String[titleNames.length - 1];
				for (int i = 0; i < str.length; i++) {
					str[i] = titleNames[i + 1];
				}
				ret = new KpiHasChartRightView(KPIHomeActivity.this,
						R.layout.title_right_item, str, 0, screenWidth
								- this.getResources().getDimension(
										R.dimen.zhl_left_column_width));
			}
			layRightTitle.removeAllViews();
			layRightTitle.addView(ret);
			ret.setAllBackgroundByID(R.drawable.glay_list_title);

			LayoutParams lp = new LayoutParams(
					LayoutParams.WRAP_CONTENT, ret.getItemH());// (LayoutParams)
																			// ret.getLayoutParams();
			ret.setLayoutParams(lp);
		}
	}

	/**
	 * 远程刷新数据.
	 */
	public void refreshDataFromRemote() {

		if (!ConnectManager.isConnected()) {
			Toast.makeText(this, "网络未连接!!", Toast.LENGTH_SHORT).show();
			return;
		}
		showDialog(LOADING_DIALOG);

		new Thread(new Runnable() {
			@Override
			public void run() {
				String colkey = dao.getMenuAllColumnList(menuCode).get(0)
						.get("col").toLowerCase(Locale.CHINA);
				if (param == null) {
					param = new HashMap<String, String>();
					// 获取所有列.
					param.put("cols", dao.getMenuAllColumnString(menuCode));
					// 指标.
					param.put("kpicodes", dao.getMenuAllKpiString(menuCode));
					// 数据类型.
					param.put("datatype", dataType);
					// server端表
					param.put("datatable", dao.getMenuColDataTable(menuCode));
					// 趋势图数据要取的列
					param.put("kpicolumn", colkey);
					param.put("menuCode", menuCode);
				}

				// 地区参数.
				if (areaId != null) {
					param.put("areacode", areaId);
				}

				// 当前时间.
				param.put("currtime", optime);
				// param.put("starttime",
				// dre.getDateSpecified(dre.getTrendSize(),
				// dre.getDateServerPattern()));
				// 放入维度信息.
				if (!param.containsKey("dimkey")) {
					if (complexDimInfo.isDimExpand() || thirdDimInfo == null) {
						param.put("dimkey",
								complexDimInfo.getComplexDimKeyString());
					} else {
						param.put("dimkey",
								thirdDimToComplexDim.get(thirdDimValueId));
					}
				}

				String json_reply = HttpUtil.sendRequest(
						ActionConstant.KPI_HOME_DATA, param);

				if (json_reply == null || "".equals(json_reply)) {
					LogUtil.info(this.getClass().toString(), "实时查询结果返回为空.");
					nullDataRemind();
					return;
				}

				JSONObject jsonObject;
				try {
					if (kpiData == null) {
						bulidNewKpi();
					}

					jsonObject = new JSONObject(json_reply);
					try {
						if (jsonObject.isNull(KEY_BACK_DATE)
								|| "".equals(jsonObject
										.getString(KEY_BACK_DATE))) {
							optime = dre.getDateSpecified(-1,
									dre.getDateServerPattern());
						} else {
							optime = jsonObject.getString(KEY_BACK_DATE);
						}
					} catch (JSONException e) {
						optime = dre.getDateSpecified(-1,
								dre.getDateServerPattern());
					}

					JSONArray trendData = jsonObject.optJSONArray("trenddata");

					kpiData.buildTrendDataFromJson(trendData, colkey, menuCode);

					JSONArray basedata = jsonObject.optJSONArray("base_data");
					kpiData.buildDataFromJson(basedata, complexDimInfo,
							menuCode);

				} catch (JSONException e) {
					e.printStackTrace();
					LogUtil.error(this.getClass().toString(), "返回结果解析异常. ");
				}

				handler.post(new Runnable() {
					@Override
					public void run() {
						// 设置Title
						monDevDateNew.setText(DateUtil.oneStringToAntherString(
								optime, dre.getDateServerPattern(),
								dre.getDateShowPattern()));
						calendar = DateUtil.getCalendar(optime,
								dre.getDateServerPattern());
						if (kpiData == null) {
							nullDataRemind();

							try {
								leftAdapter.notifyDataSetChanged();
								rightAdapter.notifyDataSetChanged();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							changeTitle(kpiData.getTitleName());
							Map<String, String> paramMap = new HashMap<String, String>();
							paramMap.put("datatype", dataType);
							paramMap.put("optime", optime);
							paramMap.put("cols",
									dao.getMenuAllColumnString(menuCode));
							paramMap.put("areacode", areaId);
							paramMap.put("datatable",
									dao.getMenuColDataTable(menuCode));
							leftAdapter = new KpiHomeLeftExpanableAdapter(
									KPIHomeActivity.this, kpiData);
							rightAdapter = new KpiHomeRightExpandableAdapter(
									KPIHomeActivity.this, kpiData, paramMap);
							monDevLeftListView.setAdapter(leftAdapter);
							monDevRightListView.removeAllViewsInLayout();
							monDevRightListView.setAdapter(rightAdapter);
							listViewSetting.expandListViewAndSetNotClickGroup(
									monDevLeftListView, monDevRightListView,
									leftAdapter, rightAdapter);
							listViewSetting
									.setListViewOnTouchAndScrollListener(
											monDevLeftListView,
											monDevRightListView);
							leftAdapter.notifyDataSetChanged();
							rightAdapter.notifyDataSetChanged();
						}
						removeDialog(LOADING_DIALOG);
					}
				});
			}
		}).start();
	}

	protected void bulidNewKpi() {
		// TODO Auto-generated method stub
		kpiData = new KpiData();

		if (menuAddInfo != null) {
			kpiData.setHasTrendChart("0".equals(menuAddInfo
					.get("showThrendFlag")) ? false : true);
		}
		kpiData.setColkey(colkeyList);
		kpiData.setTitleName(titleList);
		kpiData.setColInfoMap(menuColInfoMap);
		kpiData.setKpiInfoMap(kpiInfoMap);
	}

	/**
	 * 沒有數據提醒.
	 */
	public void nullDataRemind() {
		myhHandler.post(new Runnable() {

			@Override
			public void run() {
				removeDialog(LOADING_DIALOG);
				Toast.makeText(KPIHomeActivity.this,
						getString(R.string.no_data), Toast.LENGTH_SHORT).show();
				optime = dre.getDateSpecified(-1, dre.getDateServerPattern());
				monDevDateNew.setText(DateUtil.oneStringToAntherString(optime,
						dre.getDateServerPattern(), dre.getDateShowPattern()));
				calendar = DateUtil.getCalendar(optime,
						dre.getDateServerPattern());
				kpiData = null;
				if (leftAdapter != null) {
					leftAdapter.setKpiData(kpiData);
					leftAdapter.notifyDataSetChanged();
				}
				if (rightAdapter != null) {
					rightAdapter.setKpiData(kpiData);
					rightAdapter.notifyDataSetChanged();
				}

			}
		});

	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		switch (arg0.getId()) {
		case R.id.brand_select_new:
			if (rightAdapter.isChartClick()) {
				rightAdapter.showIsChartClickToast();
				return;
			}
			int tdvl = menuThirdDimValueList.size();
			final String[] thirdDimValueList = new String[tdvl];

			for (int i = 0; i < tdvl; i++) {
				thirdDimValueList[i] = menuThirdDimValueList.get(i).get(
						"dimvaluename");
			}

			new AlertDialog.Builder(KPIHomeActivity.this)
					.setTitle(thirdDimInfo.get("dimName"))
					.setItems(thirdDimValueList,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int which) {
									thirdDimValueId = menuThirdDimValueList
											.get(which).get("dimvalueid");
									monDevBrandNew
											.setText(thirdDimValueList[which]);
									refreshDataFromRemote();
								}
							}).create().show();
			break;
		case R.id.area_select_new:

			if ("".equals(StringUtil.nullToString(monDevAreaNew.getText()
					.toString()))) {
				return;
			}

			if (rightAdapter != null && rightAdapter.isChartClick()) {
				rightAdapter.showIsChartClickToast();
				return;
			}
			if (areaInfoList == null || areaInfoList.size() == 0) {
				LogUtil.error(this.getClass().toString(), "地区维度信息为空. 无法选择地区.");
				break;
			}

			int len = areaInfoList.size();
			final String[] areaNameList = new String[len];

			for (int i = 0; i < len; i++) {
				areaNameList[i] = areaInfoList.get(i).get("areaName");
			}

			if (alertDialog == null) {
				alertDialog = new AlertDialog.Builder(KPIHomeActivity.this)
						.setTitle("区域选择")
						.setItems(areaNameList,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int which) {
										areaId = areaInfoList.get(which).get(
												"areaCode");
										try {
											areaLevel = Integer
													.parseInt(areaInfoList.get(
															which).get(
															"areaLevel"));
										} catch (Exception e) {
											e.printStackTrace();
											areaLevel = -1;
										}
										monDevAreaNew
												.setText(areaNameList[which]);
										refreshDataFromRemote();
										alertDialog.dismiss();
									}
								}).create();
			}

			if (alertDialog.isShowing()) {
				alertDialog.dismiss();
			}

			alertDialog.show();

			break;
		case R.id.date_select_new:

			if ("".equals(StringUtil.nullToString(monDevDateNew.getText()
					.toString()))) {
				return;
			}

			if (rightAdapter != null && rightAdapter.isChartClick()) {
				rightAdapter.showIsChartClickToast();
				return;
			}
			showDatePicker();

			break;
		}
	}

	public static void showMessageDailog() {
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setCancelable(false);
		pDialog.show();
		pDialog.setContentView(R.layout.progress);
	}

	/**
	 * 显示日期选择.
	 */
	public void showDatePicker() {

		if (mDateDia != null && mDateDia.isShowing()) {
			mDateDia.dismiss();
		}

		mDateDia = new MyDatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				calendar.set(year, monthOfYear, dayOfMonth);
				if (dataType.equals(Constant.DATA_TYPE_DAY)) {
					monDevDateNew.setText(DateUtil.formatter(
							calendar.getTime(), "yyyy/MM/dd"));
					optime = DateUtil.formatter(calendar.getTime(),
							DateUtil.PATTERN_8);

				} else {
					monDevDateNew.setText(DateUtil.formatter(
							calendar.getTime(), "yyyy/MM"));
					optime = DateUtil.formatter(calendar.getTime(),
							DateUtil.PATTERN_6);
				}
				refreshDataFromRemote();
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		mDateDia.show();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		closePopWindow();
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 关闭当前显示的pop
	 */
	protected void closePopWindow() {
		// TODO Auto-generated method stub
		if (pp != null && pp.isShowing()) {
			popShowCount = 0;
			pp.dismiss();

		}
	}

	protected void showPopWinDow(String leftValue, View view) {
		// TODO Auto-generated method stub
		if (pp != null) {
			if (pp.isShowing()) {
				pp.dismiss();

			}
		} else {
			pp = new PopupWindow(getLayoutInflater().inflate(
					R.layout.popup_window, null), LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);

		}
		TextView v = (TextView) pp.getContentView().findViewById(R.id.pop_text);
		v.setText(leftValue);
		int popMaxW = (int) (screenWidth - (KPIHomeActivity.this.getResources()
				.getDimension(R.dimen.zhl_left_column_width)));
		if (leftValue.length() * 15 > popMaxW) {
			pp.setWidth(popMaxW);
		}
		pp.showAsDropDown(view, (int) (KPIHomeActivity.this.getResources()
				.getDimension(R.dimen.zhl_left_column_width)),
				(int) -(KPIHomeActivity.this.getResources()
						.getDimension(R.dimen.zhl_item_height)));
		popShowCount += 1;
		Handler hd = (new Handler());
		hd.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (pp != null && pp.isShowing()) {
					// 到时间后如果还在显示则删除
					popShowCount -= 1;
					if (popShowCount == 0)
						pp.dismiss();
				}
			}
		}, 3000);
	}
}
